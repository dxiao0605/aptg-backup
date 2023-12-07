package aptg.cathaybkeco.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.cathaybkeco.bean.AccountTempBean;
import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.dao.BankInfDAO;
import aptg.cathaybkeco.dao.RankListDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.AdminSetupVO;
import aptg.cathaybkeco.vo.AreaVO;

/**
 * Servlet implementation class importAccountTemp 匯入帳號暫存檔
 */
@WebServlet("/importAccountTemp")
public class importAccountTemp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(importAccountTemp.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public importAccountTemp() {
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
		logger.debug("importAccountTemp start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			logger.debug("token: " + token);
			if (StringUtils.isNotBlank(token) ) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					boolean isOK = true;
					AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
					List<DynaBean> lock = adminSetupDAO.checkImport();
					if(lock!=null && !lock.isEmpty()) {
						DynaBean bean = lock.get(0);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						int locked = ToolUtil.parseInt(bean.get("locked"));
						String lockTime = ToolUtil.dateFormat(bean.get("locktime"), sdf);						
						if(locked==1) {//鎖定註記為鎖定
							Calendar unlockTime = Calendar.getInstance();
							unlockTime.setTime(sdf.parse(lockTime));
							unlockTime.add(Calendar.MINUTE, 10);
							if(unlockTime.after(Calendar.getInstance())) {//未超過解鎖時間(鎖定時間+10分鐘)
								rspJson.put("code", "26");
								rspJson.put("msg", "目前有其他管理者在做匯入，請稍候嘗試。");		
								isOK = false;
							}							
						}						
					}
										
					if(isOK) {					
						AdminSetupVO adminSetupVO = this.parseExcel(request);
						if (adminSetupVO.isError()) {
							rspJson.put("code", adminSetupVO.getCode());
							rspJson.put("msg", "匯入資料失敗，"+adminSetupVO.getDescription());
						} else {
							if(adminSetupVO.getAccountTempList()!=null && adminSetupVO.getAccountTempList().size()>0) {							
								adminSetupDAO.addAccountTempBatch(adminSetupVO);											
							}
							rspJson.put("code", "00");
							rspJson.put("msg", adminSetupVO.getRspJson());						
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
		logger.debug("importAccountTemp end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Excel
	 * 
	 * @param json
	 * @return adminSetupVO
	 * @throws Exception
	 */
	private AdminSetupVO parseExcel(HttpServletRequest request) throws Exception {
		InputStream is = null;
		AdminSetupVO adminSetupVO = new AdminSetupVO();
		AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
		try {
	        String userName = "", userAccount = "";
			String fileName = "";
			if (ServletFileUpload.isMultipartContent(request)) {
				// 1. 建立DiskFileItemFactory物件，設定緩衝區大小和臨時檔案目錄
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// 2. 建立ServletFileUpload物件
				ServletFileUpload sfu = new ServletFileUpload(factory);
				List<FileItem> formItems = sfu.parseRequest(request);
				for (int i = 0; i < formItems.size(); i++) {
					FileItem item = formItems.get(i);
					if (!item.isFormField()) {//判斷是否為檔案
						fileName = item.getName();//取得上傳的檔名稱
						is = item.getInputStream();
					} else {
						String field = item.getFieldName();
						String value = Streams.asString(item.getInputStream(), CharEncoding.UTF_8);
						if (field.equals("username")) {
							userName = value;
							adminSetupVO.setUserName(value);
						}
						if (field.equals("account")) {
							userAccount = value;
							adminSetupVO.setAccount(value);
						}
					}
				}
				
				logger.debug("UserName: " + userName + ",UserAccount:" + userAccount);
				String fileType = (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())).toLowerCase();
				if ("csv".equals(fileType)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
															
					//匯入鎖定
					Calendar cal = Calendar.getInstance();
					adminSetupVO.setLockTime(sdf.format(cal.getTime()));					
					adminSetupDAO.importLock(adminSetupVO);

					//查詢權限
					List<String> rankList = new ArrayList<String>();
					RankListDAO rankListDAO = new RankListDAO();
					List<DynaBean> rank = rankListDAO.getRankList("");
					if(rank!=null && !rank.isEmpty()) {
						for(DynaBean r : rank) {
							rankList.add(ObjectUtils.toString(r.get("rankcode")));
						}
					}
					
					//查詢使用者資訊
					List<DynaBean> userList = adminSetupDAO.getAdminSetup(adminSetupVO);
					String userBankCode = "", userRankCode = "", userAreaCodeNo = "";
					if(userList!=null && !userList.isEmpty()) {
						DynaBean userBaen = userList.get(0);
						userBankCode = ObjectUtils.toString(userBaen.get("bankcode"));
						userRankCode = ObjectUtils.toString(userBaen.get("rankcode"));
						userAreaCodeNo = ObjectUtils.toString(userBaen.get("areacodeno"));
					}
					
					//查詢目前帳號資訊做比對					
					List<String> accountList = new ArrayList<String>();
					List<String> bankManageList = new ArrayList<String>();
					AdminSetupVO compareVO = new AdminSetupVO();
					if("5".equals(userRankCode)) {//分行管理者
						compareVO.setBankCode(userBankCode);
						compareVO.setRankCodeBE(userRankCode);
					}else if("3".equals(userRankCode)) {//區域管理者
						compareVO.setRankCodeBT(userRankCode);
						AreaDAO areaDAO = new AreaDAO();
						List<DynaBean> areaList = areaDAO.getAccessBanks(userAreaCodeNo);
						if(areaList!=null && !areaList.isEmpty()) {
							String valueStr = "", bank;
							for (DynaBean area : areaList) {
								bank = ObjectUtils.toString(area.get("bankcode"));
								valueStr += ((StringUtils.isNotBlank(valueStr) ? ",'" : "'") + bank + "'");
								bankManageList.add(bank);
							}
							compareVO.setBankCodeArr(valueStr);
						}
					}else {//總行管理者、系統管理者
						compareVO.setRankCodeBE("1");
					}
					List<DynaBean> adminSetupList = adminSetupDAO.getAdminSetup(compareVO);
					if(adminSetupList!=null && !adminSetupList.isEmpty()) {
						for(DynaBean a : adminSetupList) {
							if(!userAccount.equals(ObjectUtils.toString(a.get("account"))))
								accountList.add(ObjectUtils.toString(a.get("account")));
						}
					}
			
					//判斷檔案格式
					String charsetName = "BIG5";
					byte[] b = new byte[3];
					is.read(b);
				    if (b[0] == -17 && b[1] == -69 && b[2] == -65)
				    	charsetName = "UTF-8";
				    else if(b[0] == (byte) 0xFF && b[1] ==  (byte) 0xFE)
				    	charsetName = "UTF-16LE";
				    else if(b[0] == (byte) 0xFE && b[1] ==  (byte) 0xFF)
				    	charsetName = "UTF-16BE";
					
				    logger.debug("CSV編碼為" + charsetName);
					BankInfDAO bankInfDAO = new BankInfDAO();
					JSONObject rspJson = new JSONObject();
					String addAccount = "", suspendAccount = "";
					String uuid = UUID.randomUUID().toString();
					int importCount = 0;
					AreaDAO areaDAO = new AreaDAO();
					List<String> bankList = new ArrayList<String>();
					Map<String, String> areaMap = new HashMap<String, String>();
					List<AccountTempBean> accountTempList = new ArrayList<AccountTempBean>();
					List<String> importAccountList = new ArrayList<String>();//匯入帳號(檢核匯入是否有重複)								
					boolean error = false;
					String description = "";//紀錄檢核錯誤訊息
					int row = 0;//記錄行數
					BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
					String line = null;
					while((line=br.readLine())!=null){
						if(row!=0) {//排除第一行標題
							String item[] = getStrArr(line);//帳號,使用者名稱,分行,email,權限,區域,停權狀態
							if(ToolUtil.isBlankArray(item)) {
								break;
							}
							
							if(!(userAccount.equals(item[0]) || "0".equals(item[4]))) {//針對白名單中自己的帳號與系統管理者做忽略，不做任何檢核與更動
								AccountTempBean bean = new AccountTempBean();
								bean.setUuid(uuid);
								
								String account = item[0];
								String accountName = item[1];
								String bankCode = StringUtils.leftPad(item[2], 3, "0");
								
								if(StringUtils.isBlank(account)) {
									error = true;
									description += ("\n"+row+"行缺少帳號");									
								}else if(!ToolUtil.lengthCheck(account, 50)) {
									error = true;
									description += ("\n"+row+"行帳號超過50碼");								
								}
								bean.setAccount(account);
								
								if(importAccountList.contains(account)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+"，帳號重複於同檔案");
								}else {
									importAccountList.add(account);	
								}
																
								if(StringUtils.isBlank(accountName)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+"，缺少使用者名稱");					
								}else if(!ToolUtil.lengthCheck(accountName, 50)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+"，使用者名稱超過50碼");
								}
								bean.setAccountName(accountName);								
								
								if(StringUtils.isBlank(item[2])) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" 缺少分行");
								}else if(!bankList.contains(bankCode)) {
									if(bankInfDAO.checkBankExist(bankCode)) {
										bankList.add(bankCode);
									}else {
										error = true;
										description += ("\n"+row+"行 帳號"+account+" 分行代碼"+bankCode+"不存在");
									}
								}
								bean.setBankCode(bankCode);

								String email = item[3];
								if(StringUtils.isBlank(email)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" 缺少email");							
								}else if(email.indexOf("@")==-1 || !ToolUtil.lengthCheck(email, 30)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" email格式不符");							
								}
								bean.setEmail(email);
								
								String rankCode = item[4];
								if(StringUtils.isBlank(rankCode)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" 缺少權限");
								}else if(!rankList.contains(rankCode)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" 權限"+rankCode+"不存在");
								}
								bean.setRankCode(rankCode);
								
								if("3".equals(rankCode)||"4".equals(rankCode)) {
									if(StringUtils.isBlank(item[5])) {
										error = true;
										description += ("\n"+row+"行 帳號"+account+" 缺少區域");						
									}
									String areaCode = StringUtils.leftPad(item[5], 2, "0");
									if(!areaMap.containsKey(areaCode)){
										AreaVO areaVO = new AreaVO();
										areaVO.setAreaCode(areaCode);
										areaVO.setEnabled("1");//啟用									
										List<DynaBean> area = areaDAO.getArea(areaVO);
										if(area!=null && !area.isEmpty()) {
											String areaCodeNo = ObjectUtils.toString(area.get(0).get("seqno"));
											areaMap.put(areaCode, areaCodeNo);
											bean.setAreaCodeNo(areaCodeNo);
										}else {
											error = true;
											description += ("\n"+row+"行 帳號"+account+" 區域代碼 "+areaCode+" 不存在");	
										}
									}else {
										bean.setAreaCodeNo(areaMap.get(areaCode));	
									}
								}
															
								String suspend = item[6];
								if(StringUtils.isBlank(suspend)) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" 缺少停權狀態");								
								}else if(!("0".equals(suspend) || "1".equals(suspend))) {
									error = true;
									description += ("\n"+row+"行 帳號"+account+" 停權狀態錯誤");								
								}
								bean.setSuspend(suspend);
								
								if("5".equals(userRankCode)) {//分行管理者檢核
									if(!userBankCode.equals(bankCode)) {
										error = true;
										description += ("\n"+row+"行 帳號"+account+"，非管理者可管理帳號");
									}
									if(!("5".equals(rankCode) || "6".equals(rankCode))){
										error = true;
										description += ("\n"+row+"行 帳號"+account+"非管理者可管理權限");
									}
								}else if("3".equals(userRankCode)) {//區域管理者檢核
									if(!bankManageList.contains(bankCode)) {
										error = true;
										description += ("\n"+row+"行 帳號"+account+"，非管理者可管理帳號");
									}else if(!("4".equals(rankCode) || "5".equals(rankCode) || "6".equals(rankCode))){
										error = true;
										description += ("\n"+row+"行 帳號"+account+" 權限錯誤");
									}else if("4".equals(rankCode) && !userAreaCodeNo.equals(bean.getAreaCodeNo())){
										error = true;
										description += ("\n"+row+"行 帳號"+account+"，區域代碼錯誤");
									}								
								}

								if(accountList.contains(account)) {
									bean.setProcessFlag("U");
									accountList.remove(account);								
								}else {
									bean.setProcessFlag("A");
									addAccount += ((StringUtils.isNotBlank(addAccount)?",":"")+account);
								}
								bean.setUserName(userName);
								accountTempList.add(bean);
							}
							importCount++;
						}
						row++;
					}
					br.close();
						
					adminSetupVO.setError(error);
					if(error) {						
						adminSetupVO.setCode("27");
						adminSetupVO.setDescription(description);
					}else {
						for(String account : accountList) {
							AccountTempBean bean = new AccountTempBean();
							bean.setUuid(uuid);
							bean.setAccount(account);
							bean.setSuspend("1");//0:非停權, 1:停權
							bean.setProcessFlag("S");
							bean.setUserName(userName);
							
							accountTempList.add(bean);
							suspendAccount += ((StringUtils.isNotBlank(suspendAccount)?",":"")+account);
						}
					}
					adminSetupVO.setAccountTempList(accountTempList);
					
					rspJson.put("UUID", uuid);
					rspJson.put("ImportCount", importCount);
					rspJson.put("AddAccount", addAccount);
					rspJson.put("SuspendAccount", suspendAccount);					
					cal.add(Calendar.MINUTE, 5);
					rspJson.put("UnlockTime", sdf.format(cal.getTime()));
					adminSetupVO.setRspJson(rspJson);
				} else {
					adminSetupVO.setError(true);
					adminSetupVO.setCode("21");
					adminSetupVO.setDescription("請上傳CSV檔");					
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			adminSetupVO.setError(true);
			adminSetupVO.setCode("20");
			adminSetupVO.setDescription("Excel解析失敗");
		} finally {
			if(adminSetupVO.isError())
				adminSetupDAO.importUnlock(adminSetupVO);
			if(is!=null)
				is.close();
		}
		return adminSetupVO;
	}
	
	private String[] getStrArr(String str) {
		String[] result = new String[7];
		String[] arr = str.split(",");
	    for(int i=0; i<arr.length; i++) {
	    	result[i] = arr[i];
	    }
		return result;
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
