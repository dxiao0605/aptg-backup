package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import aptg.cathaybkeco.dao.MeterEventRecordDAO;
import aptg.cathaybkeco.dao.MeterSetupDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.MeterSetupVO;

/**
 * Servlet implementation class getMeterOverview 電表總覽
 */
@WebServlet("/getMeterOverview")
public class getMeterOverview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getMeterOverview.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getMeterOverview() {
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
		logger.debug("getMeterOverview start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String usageCode = ObjectUtils.toString(request.getParameter("usageCode"));
			String status = ObjectUtils.toString(request.getParameter("status"));
			logger.debug("token: " + token);			
			logger.debug("City:" + city + ",PostCodeNo:" + postCodeNo);
			logger.debug("BankCode:" + bankCode+ ",UsageCode:" + usageCode+ ",Status:" + status);
			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					MeterSetupVO meterSetupVO = new MeterSetupVO();
					meterSetupVO.setCityArr(ToolUtil.strToSqlStr(city));
					meterSetupVO.setPostCodeNoArr(ToolUtil.strToSqlStr(postCodeNo));
					meterSetupVO.setBankCodeArr(ToolUtil.strToSqlStr(bankCode));
					meterSetupVO.setUsageCodeArr(usageCode);
					MeterSetupDAO meterSetupDAO = new MeterSetupDAO();
					List<DynaBean> meterOverviewList = meterSetupDAO.getMeterOverview(meterSetupVO);
					MeterEventRecordDAO meterEventRecordDAO = new MeterEventRecordDAO();
					List<DynaBean> disconnectList = meterEventRecordDAO.getDisconnect();								
					JSONArray meterArr = convertToJson(meterOverviewList, disconnectList, status);						
					if (meterArr != null && meterArr.length()>0) {
						rspJson.put("code", "00");
						JSONObject data = new JSONObject();
						data.put("Meter", meterArr);
						rspJson.put("msg", data);
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "查無資料");
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
		logger.debug("getMeterOverview end");
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
	private JSONArray convertToJson(List<DynaBean> meterOverviewList, List<DynaBean> disconnectList, String statusArr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		JSONArray meterArr= new JSONArray();
		try {
			if (meterOverviewList != null && !meterOverviewList.isEmpty()) {
				List<String> meterDisconnectList = new ArrayList<String>();
				List<String> eco5DisconnectList = new ArrayList<String>();
				for(DynaBean bean : disconnectList) {
					if(bean.get("deviceid")!=null) {
						meterDisconnectList.add(ObjectUtils.toString(bean.get("deviceid")));
					}else {
						eco5DisconnectList.add(ObjectUtils.toString(bean.get("eco5account")));
					}
				}
				
				List<String> statusList = new ArrayList<String>();
				if(StringUtils.isNotBlank(statusArr))
					statusList = Arrays.asList(statusArr.split(","));
				
				Calendar five = Calendar.getInstance();
				five.add(Calendar.MINUTE, -5);
				Calendar ten = Calendar.getInstance();
				ten.add(Calendar.MINUTE, -10);
				List<JSONObject> sortRows = new ArrayList<JSONObject>();
				ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
				for(DynaBean bean : meterOverviewList) {
					JSONObject meter = new JSONObject();					
					
					String connectionTime = ToolUtil.dateFormat(bean.get("createtime"), sdf);
					String status = "9";
					if(bean.get("poweraccount")== null || ToolUtil.parseInt(bean.get("eco5count"))==0 || bean.get("deviceid")== null) {
						meter.put("Status", "未設定");//(沒有電號、Eco5、電表)
						status = "10";
					}else if("0".equals(ObjectUtils.toString(bean.get("enabled")))||"0".equals(ObjectUtils.toString(bean.get("eco5enabled")))) {
						meter.put("Status", "未啟用");//(電表未啟用)
						status = "11";
					}else if(eco5DisconnectList.contains(ObjectUtils.toString(bean.get("eco5account")))) {
						meter.put("Status", "ECO5斷線");
						status = "1";
					}else if(meterDisconnectList.contains(ObjectUtils.toString(bean.get("deviceid")))
							|| StringUtils.isBlank(connectionTime)
							||(StringUtils.isNotBlank(connectionTime) && sdf.parse(connectionTime).compareTo(ten.getTime())<0)) {
						meter.put("Status", "電表斷線");
						status = "2";					
					}else {
						meter.put("Status", "連線中");					
					}
					
					meter.put("StatusCode", status);//狀態代碼
					meter.put("City", bean.get("city"));//縣市
					meter.put("Dist", bean.get("dist"));//行政區
					meter.put("BankCode", bean.get("bankcode"));//分行代號
					meter.put("BankName", bean.get("bankname"));//分行名稱
					meter.put("PowerAccount", ObjectUtils.toString(bean.get("poweraccount")));//電號
					meter.put("MeterName", ObjectUtils.toString(bean.get("metername")));//電表名稱
					meter.put("UsageDesc", ObjectUtils.toString(bean.get("usagedesc")));//耗能分類
					meter.put("DeviceID", ObjectUtils.toString(bean.get("deviceid")));//DeviceID
					meter.put("Enabled", ToolUtil.getEnabled(bean.get("enabled")));//電表啟用狀態
					String rectime = ToolUtil.dateFormat(bean.get("rectime"), sdf);			
					if(StringUtils.isNotBlank(rectime) && sdf.parse(rectime).compareTo(five.getTime())>=0) {
						meter.put("VavgP", ToolUtil.getBigDecimal(bean.get("vavgp"), 0));//電壓
						meter.put("Iavg", ToolUtil.getBigDecimal(bean.get("iavg"), 0));//電流
						meter.put("W", ToolUtil.getBigDecimal(bean.get("w"), 1));//功率
						meter.put("RP", bean.get("var")!= null ? bean.get("var") : "");//虛功
						meter.put("VA", bean.get("va")!= null ? bean.get("va") : "");//視在
						meter.put("PF", ToolUtil.getBigDecimal(bean.get("pf"), 2).multiply(new BigDecimal(100)));//功因
						meter.put("Hz", bean.get("hz")!= null ? bean.get("hz") : "");//頻率
						meter.put("DF", bean.get("mode1")!= null ? bean.get("mode1") : "");//需量預測
					}else {
						meter.put("VavgP", "");//電壓
						meter.put("Iavg", "");//電流
						meter.put("W", "");//功率
						meter.put("RP", "");//虛功
						meter.put("VA", "");//視在
						meter.put("PF", "");//功因
						meter.put("Hz", "");//頻率
						meter.put("DF", "");//需量預測
					}
					meter.put("RecTime", rectime);//最新資料時間
					meter.put("ConnectionTime", connectionTime);//最新連線時間	
					
					if(statusList.isEmpty()||statusList.contains(status)) {
						
						jsons.add(meter);
					}
				}
	
				sortRows = jsons.stream().sorted(Comparator.comparing(new Function<JSONObject, String>() {
					public String apply(JSONObject bean) {
						return bean.getString("StatusCode");
					}
				})).collect(Collectors.toList());
				
				for (int i = 0; i < sortRows.size(); i++) {
					JSONObject obj = sortRows.get(i);
					obj.put("Seq", i+1);
					meterArr.put(obj);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return meterArr;
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

}
