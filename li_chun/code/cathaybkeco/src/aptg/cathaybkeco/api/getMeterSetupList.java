package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getMeterSetup 電表列表
 */
@WebServlet("/getMeterSetupList")
public class getMeterSetupList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterSetupList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterSetupList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("getMeterSetupList start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String deviceId = ObjectUtils.toString(request.getParameter("deviceId")); 
			String eco5Account = ObjectUtils.toString(request.getParameter("eco5Account"));	 
			String enabled = ObjectUtils.toString(request.getParameter("enabled"));
			logger.debug("token: " + token);
			logger.debug("BankCode:" + bankCode + ",DeviceID:" + deviceId);
			logger.debug("ECO5Account:" + eco5Account+ ",Enabled:" + enabled);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setBankCode(bankCode);
					meterSetupVO.setDeviceId(deviceId);
					meterSetupVO.setEco5Account(eco5Account);
					meterSetupVO.setEnabled(enabled);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> list = meterSetupDAO.getMeterSetup(meterSetupVO);
					rspJson.put("code", "00");
					rspJson.put("count", list != null ? list.size() : 0);
					if (list != null && list.size() > 0) {
						rspJson.put("msg", convertToJson(list));
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "未裝設電錶");
					}
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", e.toString());
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("getMeterSetupList end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			List<String> eco5List = new ArrayList<String>();
			Map<String, List<String>> eco5Map = new HashMap<String, List<String>>();
			Map<String, List<String>> level1Map = new HashMap<String, List<String>>();
			Map<String, List<String>> level2Map = new HashMap<String, List<String>>();
			Map<String, List<String>> level3Map = new HashMap<String, List<String>>();
			
			Map<String, MeterSetupVO> meterMap = new HashMap<String, MeterSetupVO>();
			
			List<String> list = null;
			for (DynaBean bean : rows) {				
				String eco5account = ObjectUtils.toString(bean.get("eco5account"));//ECO5帳號							
				String treeChartID = ObjectUtils.toString(bean.get("treechartid"));		
				String level1 = treeChartID.substring(0, 2);
				String level2 = treeChartID.substring(3, 5);
				String level3 = treeChartID.substring(6, 8);
				String level4 = treeChartID.substring(9, 11);
				
				if(!eco5List.contains(eco5account)) {
					eco5List.add(eco5account);
				}
				
				if(eco5Map.containsKey(eco5account)) {
					list = eco5Map.get(eco5account);
					if(!list.contains(eco5account+"#"+level1))
						list.add(eco5account+"#"+level1);
				}else {
					list = new ArrayList<String>();
					list.add(eco5account+"#"+level1);
					eco5Map.put(eco5account, list);
				}
				
				if(!level1Map.containsKey(eco5account+"#"+level1)) {	
					level1Map.put(eco5account+"#"+level1, new ArrayList<String>());
				}
				
				if(StringUtils.isNotBlank(level2)) {
					if(level1Map.containsKey(eco5account+"#"+level1)) {
						list = level1Map.get(eco5account+"#"+level1);
						if(!list.contains(eco5account+"#"+level1+"-"+level2))
							list.add(eco5account+"#"+level1+"-"+level2);
					}
					
					if(!level2Map.containsKey(eco5account+"#"+level1+"-"+level2)) {
						level2Map.put(eco5account+"#"+level1+"-"+level2, new ArrayList<String>());
					}
				}
				
				if(StringUtils.isNotBlank(level3)) {
					if(level2Map.containsKey(eco5account+"#"+level1+"-"+level2)) {
						list = level2Map.get(eco5account+"#"+level1+"-"+level2);
						if(!list.contains(eco5account+"#"+level1+"-"+level2+"-"+level3))
							list.add(eco5account+"#"+level1+"-"+level2+"-"+level3);
					}
					if(!level3Map.containsKey(eco5account+"#"+level1+"-"+level2+"-"+level3)) {
						level3Map.put(eco5account+"#"+level1+"-"+level2+"-"+level3, new ArrayList<String>());
					}
				}
				
				if(StringUtils.isNotBlank(level4)) {
					if(level3Map.containsKey(eco5account+"#"+level1+"-"+level2+"-"+level3)) {
						list = level3Map.get(eco5account+"#"+level1+"-"+level2+"-"+level3);
						list.add(eco5account+"#"+treeChartID);
					}
				}
				
				MeterSetupVO meterSetupVO = new MeterSetupVO();
				meterSetupVO.setDeviceId(ObjectUtils.toString(bean.get("deviceid")));
				meterSetupVO.setMeterName(ObjectUtils.toString(bean.get("metername")));//電表名稱
				meterSetupVO.setInstallPosition(ObjectUtils.toString(bean.get("installposition")));//電表安裝位置				
				meterSetupVO.setUsageDesc(ObjectUtils.toString(bean.get("usagedesc")));//耗能分類
				meterSetupVO.setPowerAccount(ObjectUtils.toString(bean.get("poweraccount")));//電號
				meterSetupVO.setTreeChartID(treeChartID);//樹狀圖編號
				if(ToolUtil.add(bean.get("eventcount1"), bean.get("eventcount2")).compareTo(new BigDecimal(0))>0) {
					meterSetupVO.setPriority("2");
				}else if(ToolUtil.add(bean.get("eventcount3"), bean.get("eventcount4")).compareTo(new BigDecimal(0))>0) {
					meterSetupVO.setPriority("1");
				}else {
					meterSetupVO.setPriority("0");
				}

				meterMap.put(eco5account+"#"+treeChartID, meterSetupVO);
			}
			
			JSONArray level1Arr = new JSONArray();
			for(String key : eco5List){
				List<String> level1List = eco5Map.get(key);
				for(String level1 : level1List){				
					JSONObject meterSetup1 = addMeterObject(level1+"-  -  -  ", meterMap);
					JSONArray level2Arr = new JSONArray();
					List<String> level2List = level1Map.get(level1);
					
					for(String level2 : level2List){				
						JSONObject meterSetup2 = addMeterObject(level2+"-  -  ", meterMap);
						JSONArray level3Arr = new JSONArray();
						List<String> level3List = level2Map.get(level2);
						
						for(String level3 : level3List){				
							JSONObject meterSetup3 = addMeterObject(level3+"-  ", meterMap);
							JSONArray level4Arr = new JSONArray();
							List<String> level4List = level3Map.get(level3);
							
							for(String level4 : level4List) {			
								JSONObject meterSetup4 = addMeterObject(level4, meterMap);
								level4Arr.put(meterSetup4);
							}	
			
							meterSetup3.put("Child", level4Arr);
							level3Arr.put(meterSetup3);
						}

						meterSetup2.put("Child", level3Arr);
						level2Arr.put(meterSetup2);
					}
					meterSetup1.put("Child", level2Arr);
					level1Arr.put(meterSetup1);
				}		
			}
			data.put("MeterSetup", level1Arr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private JSONObject addMeterObject(String meterKey, Map<String, MeterSetupVO> meterMap) {
		JSONObject meterSetup = new JSONObject();
		if(meterMap.containsKey(meterKey)) {
			MeterSetupVO meterSetupVO = meterMap.get(meterKey);
			
			meterSetup.put("DeviceID", meterSetupVO.getDeviceId());
			meterSetup.put("MeterName", meterSetupVO.getMeterName());//電表名稱
			meterSetup.put("InstallPosition", meterSetupVO.getInstallPosition());//電表安裝位置				
			meterSetup.put("UsageDesc",meterSetupVO.getUsageDesc());//耗能分類
			meterSetup.put("PowerAccount", meterSetupVO.getPowerAccount());//電號
			meterSetup.put("TreeChartID", meterSetupVO.getTreeChartID());//樹狀圖編號
			meterSetup.put("Priority", meterSetupVO.getPriority());
		}else {
			meterSetup.put("DeviceID", "");
			meterSetup.put("MeterName", "");//電表名稱
			meterSetup.put("InstallPosition", "");//電表安裝位置				
			meterSetup.put("UsageDesc", "");//耗能分類
			meterSetup.put("PowerAccount", "");//電號			
			meterSetup.put("TreeChartID", meterKey.split("#")[1]);//樹狀圖編號
			meterSetup.put("Priority", "");	
		}
		meterSetup.put("ID", meterKey);
		return meterSetup;
	}
}
