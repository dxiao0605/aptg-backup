package aptg.cathaybkeco.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import aptg.cathaybkeco.config.SysConfig;
import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.util.CsvUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;

/**
 * Servlet implementation class getAdminSetup 帳號資料
 */
@WebServlet("/getAdminSetup")
public class getAdminSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getAdminSetup.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getAdminSetup() {
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
		logger.debug("getAdminSetup start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String rankCode = ObjectUtils.toString(request.getParameter("rankCode"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			logger.debug("token: " + token);
			logger.debug("Account:"+ account +", City:" + city + ", PostCodeNo:" + postCodeNo+
						", BankCode: " + bankCode+ ", RankCode:" + rankCode + ", Type:"+type);			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					String userRank = "", userArea = "", userBank = "";
					AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
					List<DynaBean> rank = adminSetupDAO.getRankCode(account);
					if (rank != null && !rank.isEmpty()) {
						userRank = ObjectUtils.toString(rank.get(0).get("rankcode"));
						userArea = ObjectUtils.toString(rank.get(0).get("areacodeno"));
						userBank = ObjectUtils.toString(rank.get(0).get("bankcode"));
						
						AdminSetupVO adminSetupVO = new AdminSetupVO();
						adminSetupVO.setCity(city);
						adminSetupVO.setPostCodeNo(postCodeNo);
						adminSetupVO.setBankCode(bankCode);
						adminSetupVO.setRankCodeArr(ToolUtil.strToSqlStr(rankCode));
						adminSetupVO.setRankCodeBE(userRank);
						if("5".equals(userRank)) {//分行管理者
							adminSetupVO.setBankCode(userBank);						
						}else if("2".equals(userRank) || "4".equals(userRank) || "6".equals(userRank)) {//總行使用者、區域使用者、分行使用者
							adminSetupVO.setAccount(account);			
						}
						List<DynaBean> list = adminSetupDAO.getAdminSetupList(adminSetupVO);
						if (list != null && list.size() > 0) {
							if("check".equals(type)) {
								rspJson.put("msg", "帳號管理"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv");
							}else if ("csv".equals(type)) {
								rspJson.put("msg", composeCSV(list, account, userRank, userArea));
							}  else {
								rspJson.put("msg", convertToJson(list, account, userRank, userArea));
							}											
							rspJson.put("code", "00");
						} else {
							rspJson.put("code", "07");
							rspJson.put("msg", "查無資料");
						}
					}else {
						rspJson.put("code", "23");
						rspJson.put("msg", "查無角色權限");
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
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
		logger.debug("getAdminSetup end");
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows, String userAccount, String userRank, String userArea) throws Exception {
		JSONObject data = new JSONObject();
		try {
			List<String> bankList = new ArrayList<String>();
			if("3".equals(userRank)) {//區域管理者
				AreaDAO areaDAO = new AreaDAO();
				List<DynaBean> bank = areaDAO.getAccessBanks(userArea);
				if (bank != null && !bank.isEmpty()) {
					for (DynaBean bean : bank) {
						bankList.add(ObjectUtils.toString(bean.get("bankcode")));
					}
				}
			}
			
			int enabledCount = 0, suspendCount = 0, count =0;
			JSONArray accountArr = new JSONArray();
			for(int i=0; i<rows.size(); i++) {
				JSONObject account = new JSONObject();
				DynaBean bean = rows.get(i);
				int rankcode = ToolUtil.parseInt(bean.get("rankcode"));
				int enabled = ToolUtil.parseInt(bean.get("enabled"));
				int suspend = ToolUtil.parseInt(bean.get("suspend"));
				String bankCode = ObjectUtils.toString(bean.get("bankcode"));
				
				//1.區域管理者不能看到其他區域管理者 2.區域管理者只能看到自己管理分行的帳號
				if(!"3".equals(userRank) ||
						("3".equals(userRank) && ((rankcode==3 && userAccount.equals(ObjectUtils.toString(bean.get("account"))))
						  || (rankcode!=3 && bankList.contains(bankCode)))
						)
					) {
					if(enabled==1) {
						enabledCount++;
					}
					if(suspend==1) {
						suspendCount++;
					}	
					account.put("Account", ObjectUtils.toString(bean.get("account")));
					account.put("UserName", ObjectUtils.toString(bean.get("username")));
					account.put("BankCode", bankCode);
					account.put("BankName", ObjectUtils.toString(bean.get("bankname")));
					account.put("Email", ObjectUtils.toString(bean.get("email")));				
					account.put("RankCode", rankcode);
					account.put("RankDesc", ObjectUtils.toString(bean.get("rankdesc")));
					if(bean.get("areacodeno")!=null) {					
						account.put("Area", bean.get("areacode")+"-"+bean.get("areaname"));
						account.put("AreaCodeNo", bean.get("areacodeno"));
					}else {
						account.put("Area", "");
						account.put("AreaCodeNo", "");
					}							
					account.put("Enabled", ToolUtil.getEnabled(enabled));
					account.put("Suspend", suspend);
					accountArr.put(account);
					count++;
				}
			}
			
			data.put("AccountCount", count);//總帳號數 
			data.put("EnabledCount", enabledCount);//啟用 
			data.put("SuspendCount", suspendCount);//停權
			data.put("List", accountArr);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	/**
	 * 產生CSV
	 * @param rows
	 * @param response
	 * @throws Exception
	 */
	private JSONObject composeCSV(List<DynaBean> rows, String userAccount, String userRank, String userArea) throws Exception {
		BufferedWriter out = null;
		File file = null;
		JSONObject data = new JSONObject();
		try {
			List<String> bankList = new ArrayList<String>();
			if("3".equals(userRank)) {//區域管理者
				AreaDAO areaDAO = new AreaDAO();
				List<DynaBean> bank = areaDAO.getAccessBanks(userArea);
				if (bank != null && !bank.isEmpty()) {
					for (DynaBean bean : bank) {
						bankList.add(ObjectUtils.toString(bean.get("bankcode")));
					}
				}
			}
			
			String fileName = "帳號管理"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".csv";
			String path = SysConfig.getInstance().getTempDir()+fileName;
			file = new File(path);			
			
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					file),"UTF-8"),1024);
			out.write(CsvUtil.getBOM());  
					
			out.write("帳號");
			out.write(getDelimiter());
			out.write("使用名稱");
			out.write(getDelimiter());
			out.write("分行");
			out.write(getDelimiter());
			out.write("E-mail");
			out.write(getDelimiter());
			out.write("權限");
			out.write(getDelimiter());
			out.write("區域");
			out.write(getDelimiter());
			out.write("停權狀態");
			out.newLine();
	
			for(DynaBean bean : rows) {
				int rankcode = ToolUtil.parseInt(bean.get("rankcode"));
				String bankCode = ObjectUtils.toString(bean.get("bankcode"));
				
				//1.區域管理者不能看到其他區域管理者 2.區域管理者只能看到自己管理分行的帳號
				if(!"3".equals(userRank) ||
						("3".equals(userRank) && ((rankcode==3 && userAccount.equals(ObjectUtils.toString(bean.get("account"))))
						  || (rankcode!=3 && bankList.contains(bankCode)))
						)
					) {
					
					out.write(ObjectUtils.toString(bean.get("account")));
					out.write(getDelimiter());
					out.write(ObjectUtils.toString(bean.get("username")));
					out.write(getDelimiter());
					out.write(StringUtils.leftPad(bean.get("bankcode").toString(), 3, "0"));
					out.write(getDelimiter());
					out.write(ObjectUtils.toString(bean.get("email")));
					out.write(getDelimiter());
					out.write(ObjectUtils.toString(bean.get("rankcode")));
					out.write(getDelimiter());
					out.write(ObjectUtils.toString(bean.get("areacode")));
					out.write(getDelimiter());
					out.write(ObjectUtils.toString(bean.get("suspend")));
					out.newLine();
				}
			}
	
			out.flush();
			out.close();

			data.put("FileName", fileName);
			data.put("Base64", ToolUtil.getFileBase64(file));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}finally {
			if(file!=null)
				file.delete();
		}
		return data;
	}

	private String getDelimiter() {
		return ",";
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
