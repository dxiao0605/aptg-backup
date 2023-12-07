package aptg.cathaybkeco.api;

import java.io.IOException;
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
import org.json.JSONObject;

import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BankInfVO;

/**
 * Servlet implementation class addBankInf 新增分行基本資料
 */
@WebServlet("/addBankInf")
public class addBankInf extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(addBankInf.class.getName()); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addBankInf() {
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
		logger.debug("addBankInf start");
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
					BankInfVO bankInfVO = this.parseJson(req);
					if(bankInfVO.isError()) {
						rspJson.put("code", bankInfVO.getCode());
						rspJson.put("msg", bankInfVO.getDescription());
					}else {
						BankInfDAO bnkInfDAO = new BankInfDAO();
						List<DynaBean> list = bnkInfDAO.getBankInf(bankInfVO);
						if(list!=null && list.size()>0) {
							rspJson.put("code", "10");
							rspJson.put("msg", "分行代碼已存在");
						}else {
							bnkInfDAO.addBankInf(bankInfVO);
							ToolUtil.addLogRecord(bankInfVO.getUserName(), "3", "新增"+bankInfVO.getBankCode()+"分行資訊");
							
							rspJson.put("code", "00");
							rspJson.put("msg", "Insert Success");
						}
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
		logger.debug("addBankInf end");
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
	private BankInfVO parseJson(String json) throws Exception {
		BankInfVO bankInfVO = new BankInfVO();
		try {
			JSONObject request = new JSONObject(json);
			JSONObject msg = request.getJSONObject("msg");
			
			if(!ToolUtil.lengthCheck(msg.optString("BankCode"), 3)) {
				bankInfVO.setError(true);
				bankInfVO.setCode("12");
				bankInfVO.setDescription("分行代號超過長度限制");
			}else {
				bankInfVO.setBankCode(msg.optString("BankCode"));	
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("BankName"), 30)) {
				bankInfVO.setError(true);
				bankInfVO.setCode("12");
				bankInfVO.setDescription("分行名稱超過長度限制");
			}else {
				bankInfVO.setBankName(msg.optString("BankName"));	
			}
			
			bankInfVO.setPostCodeNo(msg.optString("PostCodeNo"));
			
			if(!ToolUtil.lengthCheck(msg.optString("BankAddress"), 100)) {
				bankInfVO.setError(true);
				bankInfVO.setCode("12");
				bankInfVO.setDescription("分行地址超過長度限制");
			}else {
				bankInfVO.setBankAddress(msg.optString("BankAddress"));
			}
			
			if(!ToolUtil.lengthCheck(msg.optString("PhoneNr"), 20)) {
				bankInfVO.setError(true);
				bankInfVO.setCode("12");
				bankInfVO.setDescription("電話超過長度限制");
			}else {
				bankInfVO.setPhoneNr(msg.optString("PhoneNr"));
			}
			
			if(ToolUtil.isNull(msg, "Area")) {
				bankInfVO.setArea(null);
			}else if (!ToolUtil.numberCheck(msg.optString("Area"))) {
				bankInfVO.setError(true);
				bankInfVO.setCode("13");
				bankInfVO.setDescription("行舍面積數字格式錯誤");
			}else {
				bankInfVO.setArea(msg.optString("Area"));
			}
					
			if(ToolUtil.isNull(msg, "People")) {
				bankInfVO.setPeople(null);
			}else if (!ToolUtil.numberCheck(msg.optString("People"))) {
				bankInfVO.setError(true);
				bankInfVO.setCode("13");
				bankInfVO.setDescription("行舍人數數字格式錯誤");
			}else {
				bankInfVO.setPeople(msg.optString("People"));
			}
	
			bankInfVO.setUserName(msg.optString("UserName"));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return bankInfVO;
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
