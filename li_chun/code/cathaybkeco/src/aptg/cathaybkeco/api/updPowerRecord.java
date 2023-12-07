package aptg.cathaybkeco.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import aptg.cathaybkeco.dao.PowerRecordDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.PowerRecordVO;

/**
 * Servlet implementation class genPowerRecord 修改PowerRecord
 */
@WebServlet("/updPowerRecord")
public class updPowerRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(updPowerRecord.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public updPowerRecord() {
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
		logger.debug("updPowerRecord start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			logger.debug("token: " + token);
			logger.debug("request: " + req);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					List<PowerRecordVO> VOList = this.parseJson(req);
					
					PowerRecordDAO powerRecordDAO = new PowerRecordDAO();
					powerRecordDAO.updPowerRecord(VOList);		
					
					rspJson.put("code", "00");
					rspJson.put("msg", "Update Success");	
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
		logger.debug("updPowerRecord end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Json
	 * 
	 * @param json
	 * @return bankInfVO
	 * @throws Exception
	 */
	private List<PowerRecordVO> parseJson(String json) throws Exception {
		PowerRecordDAO powerRecordDAO = new PowerRecordDAO();
		List<PowerRecordVO> voList = new ArrayList<PowerRecordVO>();
		try {
			JSONArray requestArr = new JSONArray(json);
			for(int i=0; i<requestArr.length(); i++) {
				JSONObject request = requestArr.getJSONObject(i);
				PowerRecordVO powerRecordVO = new PowerRecordVO();
				powerRecordVO.setDeviceID(request.optString("DeviceID"));
				powerRecordVO.setRecTime(request.optString("RecTime"));
				if(!ToolUtil.isNull(request, "I1"))
					powerRecordVO.setI1(request.optString("I1"));
				if(!ToolUtil.isNull(request, "I2"))
					powerRecordVO.setI2(request.optString("I2"));
				if(!ToolUtil.isNull(request, "I3"))
					powerRecordVO.setI3(request.optString("I3"));
				if(!ToolUtil.isNull(request, "Iavg"))
					powerRecordVO.setIavg(request.optString("Iavg"));
				if(!ToolUtil.isNull(request, "V1"))
					powerRecordVO.setV1(request.optString("V1"));
				if(!ToolUtil.isNull(request, "V2"))
					powerRecordVO.setV2(request.optString("V2"));
				if(!ToolUtil.isNull(request, "V3"))
					powerRecordVO.setV3(request.optString("V3"));
				if(!ToolUtil.isNull(request, "Vavg"))
					powerRecordVO.setVavg(request.optString("Vavg"));
				if(!ToolUtil.isNull(request, "V12"))
					powerRecordVO.setV12(request.optString("V12"));
				if(!ToolUtil.isNull(request, "V23"))
					powerRecordVO.setV23(request.optString("V23"));
				if(!ToolUtil.isNull(request, "V31"))
					powerRecordVO.setV31(request.optString("V31"));
				if(!ToolUtil.isNull(request, "VavgP"))
					powerRecordVO.setVavgP(request.optString("VavgP"));
				if(!ToolUtil.isNull(request, "W"))
					powerRecordVO.setW(request.optString("W"));
				if(!ToolUtil.isNull(request, "Var"))
					powerRecordVO.setVar(request.optString("Var"));
				if(!ToolUtil.isNull(request, "VA"))
					powerRecordVO.setVA(request.optString("VA"));
				if(!ToolUtil.isNull(request, "PF"))
					powerRecordVO.setPF(request.optString("PF"));
				if(!ToolUtil.isNull(request, "KWh"))
					powerRecordVO.setKWh(request.optString("KWh"));
				if(!ToolUtil.isNull(request, "Kvarh"))
					powerRecordVO.setKvarh(request.optString("Kvarh"));
				if(!ToolUtil.isNull(request, "Hz"))
					powerRecordVO.setHz(request.optString("Hz"));
				if(!ToolUtil.isNull(request, "THVavg"))
					powerRecordVO.setTHVavg(request.optString("THVavg"));
				if(!ToolUtil.isNull(request, "THIavg"))
					powerRecordVO.setTHIavg(request.optString("THIavg"));
				if(!ToolUtil.isNull(request, "Mode1"))
					powerRecordVO.setMode1(request.optString("Mode1"));
				if(!ToolUtil.isNull(request, "Mode2"))
					powerRecordVO.setMode2(request.optString("Mode2"));
				if(!ToolUtil.isNull(request, "Mode3"))
					powerRecordVO.setMode3(request.optString("Mode3"));
				if(!ToolUtil.isNull(request, "Mode4"))
					powerRecordVO.setMode4(request.optString("Mode4"));
				if(!ToolUtil.isNull(request, "DemandPK"))
					powerRecordVO.setDemandPK(request.optString("DemandPK"));
				if(!ToolUtil.isNull(request, "DemandSP"))
					powerRecordVO.setDemandSP(request.optString("DemandSP"));
				if(!ToolUtil.isNull(request, "DemandSatSP"))
					powerRecordVO.setDemandSatSP(request.optString("DemandSatSP"));
				if(!ToolUtil.isNull(request, "DemandOP"))
					powerRecordVO.setDemandOP(request.optString("DemandOP"));
				if(!ToolUtil.isNull(request, "MCECPK"))
					powerRecordVO.setMCECPK(request.optString("MCECPK"));
				if(!ToolUtil.isNull(request, "MCECSP"))
					powerRecordVO.setMCECSP(request.optString("MCECSP"));
				if(!ToolUtil.isNull(request, "MCECSatSP"))
					powerRecordVO.setMCECSatSP(request.optString("MCECSatSP"));
				if(!ToolUtil.isNull(request, "MCECOP"))
					powerRecordVO.setMECEOP(request.optString("MCECOP"));
				if(!ToolUtil.isNull(request, "HighCECPK"))
					powerRecordVO.setHighCECPK(request.optString("HighCECPK"));
				if(!ToolUtil.isNull(request, "HighCECSP"))
					powerRecordVO.setHighCECSP(request.optString("HighCECSP"));
				if(!ToolUtil.isNull(request, "HighCECOP"))
					powerRecordVO.setHighCECOP(request.optString("HighCECOP"));
								
				List<DynaBean> list = powerRecordDAO.checkPowerRecord(powerRecordVO);
				if(list!=null && list.size()>0) {
					voList.add(powerRecordVO);
				}				
			}
			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return voList;
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
