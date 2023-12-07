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
 * Servlet implementation class genPowerRecord 新增PowerRecord
 */
@WebServlet("/genPowerRecord")
public class genPowerRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(genPowerRecord.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public genPowerRecord() {
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
		logger.debug("genPowerRecord start");
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
					powerRecordDAO.addPowerRecord(VOList);		
					
					rspJson.put("code", "00");
					rspJson.put("msg", "Insert Success");	
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
		logger.debug("genPowerRecord end");
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
				powerRecordVO.setI1(request.optString("I1"));
				powerRecordVO.setI2(request.optString("I2"));
				powerRecordVO.setI3(request.optString("I3"));
				powerRecordVO.setIavg(request.optString("Iavg"));
				powerRecordVO.setV1(request.optString("V1"));
				powerRecordVO.setV2(request.optString("V2"));
				powerRecordVO.setV3(request.optString("V3"));
				powerRecordVO.setVavg(request.optString("Vavg"));
				powerRecordVO.setV12(request.optString("V12"));
				powerRecordVO.setV23(request.optString("V23"));
				powerRecordVO.setV31(request.optString("V31"));
				powerRecordVO.setVavgP(request.optString("VavgP"));
				powerRecordVO.setW(request.optString("W"));
				powerRecordVO.setVar(request.optString("Var"));
				powerRecordVO.setVA(request.optString("VA"));
				powerRecordVO.setPF(request.optString("PF"));
				powerRecordVO.setKWh(request.optString("KWh"));
				powerRecordVO.setKvarh(request.optString("Kvarh"));
				powerRecordVO.setHz(request.optString("Hz"));
				powerRecordVO.setTHVavg(request.optString("THVavg"));
				powerRecordVO.setTHIavg(request.optString("THIavg"));
				powerRecordVO.setMode1(request.optString("Mode1"));
				powerRecordVO.setMode2(request.optString("Mode2"));
				powerRecordVO.setMode3(request.optString("Mode3"));
				powerRecordVO.setMode4(request.optString("Mode4"));
				powerRecordVO.setDemandPK(request.optString("DemandPK"));
				powerRecordVO.setDemandSP(request.optString("DemandSP"));
				powerRecordVO.setDemandSatSP(request.optString("DemandSatSP"));
				powerRecordVO.setDemandOP(request.optString("DemandOP"));
				powerRecordVO.setMCECPK(request.optString("MCECPK"));
				powerRecordVO.setMCECSP(request.optString("MCECSP"));
				powerRecordVO.setMCECSatSP(request.optString("MCECSatSP"));
				powerRecordVO.setMECEOP(request.optString("MCECOP"));
				powerRecordVO.setHighCECPK(request.optString("HighCECPK"));
				powerRecordVO.setHighCECSP(request.optString("HighCECSP"));
				powerRecordVO.setHighCECOP(request.optString("HighCECOP"));
				
				List<DynaBean> list = powerRecordDAO.checkPowerRecord(powerRecordVO);
				if(list!=null && list.size()>0) {
					powerRecordDAO.delPowerRecord(powerRecordVO);
				}
				
				voList.add(powerRecordVO);
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
