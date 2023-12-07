package aptg.battery.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

import aptg.battery.bean.BatteryHistoryBean;
import aptg.battery.bean.RecordBean;
import aptg.battery.config.SysConfig;
import aptg.battery.dao.BatteryDAO;
import aptg.battery.dao.CompanyDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.util.CsvUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryVO;
import aptg.battery.vo.NbListVO;




/**
 * Servlet implementation class getBatteryHistoryExcel 電池歷史報表
 */
@WebServlet("/getBatteryHistoryExcel")
public class getBatteryHistoryExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBatteryHistoryExcel.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBatteryHistoryExcel() {
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
		logger.debug("GetBatteryHistoryExcel start");
		JSONObject rspJson = new JSONObject();
		File dir = null;
		try {			
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String req = ToolUtil.getStringFromInputStream(request.getInputStream());
			
			logger.debug("UserCompany:"+userCompany);
			logger.debug("request: " + req);
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(req)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {										
					BatteryVO batteryVO = this.parseJson(req, timezone, resource);
					if (batteryVO.isError()) {
						rspJson.put("code", batteryVO.getCode());
						rspJson.put("msg", batteryVO.getDescription());
					} else {
						if(ToolUtil.checkAdminCompany(userCompany))
							batteryVO.setCompanyCode(userCompany);					
									
						BatteryDAO batteryDAO = new BatteryDAO();
						List<DynaBean> batteryList = batteryDAO.getBattInfo(batteryVO);
						if(batteryList!=null && !batteryList.isEmpty()) {						
							int impType = ToolUtil.getIMPType(userCompany);
							ExecutorService threadPool = Executors.newFixedThreadPool(10);
							
							String path = SysConfig.getInstance().getCsvDir()+UUID.randomUUID().toString();
							dir = new File(path);
					        //判斷檔案是否存在;
					        if (!dir.exists()) {
					            //建立檔案;
					        	dir.mkdirs();
					        }
							
							for(DynaBean bean : batteryList) {
								String battInternalId = ObjectUtils.toString(bean.get("seqno"));
								String batteryGroupId = ObjectUtils.toString(bean.get("batterygropid"));
								process process = new process(battInternalId, batteryGroupId, batteryVO, 
										impType, timezone, language, path, userCompany);
								threadPool.execute(process);
							}
							threadPool.shutdown();
							while (!threadPool.awaitTermination(30000, TimeUnit.SECONDS)) {
								threadPool.shutdownNow();
							}
							logger.debug("export Csv");
							CsvUtil.exportCsvZip(dir, "BattHistory"+ ToolUtil.getDateFormat("yyyyMMddHHmmss", timezone).format(new Date())+".zip", response);
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
		}finally {
			if(dir!=null) 
				deleteDir(dir);
		}
		logger.debug("GetBatteryHistoryExcel end");
	}
	
	/**
	 *  解析Json
	 * @param json
	 * @param timezone
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private BatteryVO parseJson(String json, String timezone, ResourceBundle resource) throws Exception {
		BatteryVO batteryVO = new BatteryVO();
		try {
			if (StringUtils.isNotBlank(json)) {
				JSONObject request = new JSONObject(json);
				if (!ToolUtil.isNull(request, "Company")) {
					JSONObject company = request.getJSONObject("Company");
					batteryVO.setCompanyCodeArr(ToolUtil.jsonArrToSqlStr(company.optJSONArray("List")));
				}
				
				if (!ToolUtil.isNull(request, "Country")) {
					JSONObject country = request.getJSONObject("Country");
					batteryVO.setCountryArr(ToolUtil.jsonArrToSqlStr(country.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "Area")) {
					JSONObject area = request.getJSONObject("Area");
					batteryVO.setAreaArr(ToolUtil.jsonArrToSqlStr(area.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "GroupID")) {
					JSONObject groupId = request.getJSONObject("GroupID");
					batteryVO.setGroupInternalIdArr(ToolUtil.jsonArrToSqlStr(groupId.optJSONArray("List")));
				}

				if (!ToolUtil.isNull(request, "BatteryGroupId")) {
					JSONObject batteryGroupId = request.getJSONObject("BatteryGroupId");
					batteryVO.setBatteryGroupIdArr(ToolUtil.jsonArrToSqlStr(batteryGroupId.optJSONArray("List")));
				}
				if (!ToolUtil.isNull(request, "RecTime")) {
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					JSONObject recTime = request.getJSONObject("RecTime");
					String radio = recTime.optString("Radio");
					if ("1".equals(radio)) {//radio選1，則帶入前端日期
						String start = recTime.optString("Start")+" "+recTime.optString("StartHH")+":"+recTime.optString("StartMM")+":00";
						String end = recTime.optString("End")+" "+recTime.optString("EndHH")+":"+recTime.optString("EndMM")+":00";

						if(ToolUtil.isNull(recTime, "Start") || ToolUtil.isNull(recTime, "StartHH") || ToolUtil.isNull(recTime, "StartMM") ||
								ToolUtil.isNull(recTime, "End") || ToolUtil.isNull(recTime, "EndHH") || ToolUtil.isNull(recTime, "EndMM")) {				
							batteryVO.setError(true);
							batteryVO.setCode("24");
							batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
							return batteryVO;
						}else if (!ToolUtil.dateCheck(start, "yyyy-MM-dd HH:mm:ss") || !ToolUtil.dateCheck(end, "yyyy-MM-dd HH:mm:ss")) {
							batteryVO.setError(true);
							batteryVO.setCode("16");
							batteryVO.setDescription(resource.getString("5007") + "(yyyy-MM-dd HH:mm)");// 日期格式錯誤
							return batteryVO;
						}
						
						batteryVO.setStartDate(sdf2.format(sdf.parse(start)));
						batteryVO.setEndDate(sdf2.format(sdf.parse(end)));
					}else if ("3".equals(radio)) {//預設1個月
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 1);
						batteryVO.setEndDate(sdf2.format(cal.getTime()));
						cal.add(Calendar.MONTH, -1);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}else if ("5".equals(radio)) {//預設7天
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 1);
						batteryVO.setEndDate(sdf2.format(cal.getTime()));
						cal.add(Calendar.DAY_OF_MONTH, -7);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}else if ("0".equals(radio)) {//預設1天
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, 1);
						batteryVO.setEndDate(sdf2.format(cal.getTime()));
						cal.add(Calendar.DAY_OF_MONTH, -1);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}else {//radio選其他，則帶入預設前一天日期
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_MONTH, -1);
						batteryVO.setStartDate(sdf2.format(cal.getTime()));
					}				
					batteryVO.setJson(recTime);
				}else {
					batteryVO.setError(true);
					batteryVO.setCode("24");
					batteryVO.setDescription(resource.getString("1080")+resource.getString("5008"));// 日期必填欄位不能為空
				}
				
				batteryVO.setType(request.optString("Type"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return batteryVO;
	}
	
	public static class process implements Runnable {
		private String battInternalId;
		private String batteryGroupId;
		private BatteryVO batteryVO;
		private int impType;
		private String timezone;
		private String language;
		private String path;
		private String companyCode;

		public process( String battInternalId, String batteryGroupId, BatteryVO batteryVO, 
				int impType, String timezone, String language, String path, String companyCode) {
			this.battInternalId = battInternalId;
			this.batteryGroupId = batteryGroupId;
			this.batteryVO = batteryVO;
			this.impType = impType;
			this.timezone = timezone;
			this.language = language;
			this.path = path;
			this.companyCode = companyCode;
		}

		public void run() {
			BufferedWriter csvFileOutputStream = null;
			String threadId = String.valueOf(Thread.currentThread().getId());
			try {			
				logger.debug("Process" +threadId+" "+ batteryGroupId + " Start");
				
				BigDecimal ol = new BigDecimal(SysConfig.getInstance().getOL());//內阻超過設定值呈顯OL
				SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", timezone);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat date = ToolUtil.getDateFormat("yyyy/MM/dd", timezone);
				ResourceBundle resource = ToolUtil.getLanguage(language);				
				
				BatteryVO headerVO = new BatteryVO();
				headerVO.setBattInternalId(battInternalId);
				
				BatteryDAO batteryDAO = new BatteryDAO();
				List<DynaBean> rows = batteryDAO.getBatteryHistoryHeader(headerVO);//查詢電池資訊
				if(rows!=null && !rows.isEmpty()) {
					BatteryHistoryBean historyBean = new BatteryHistoryBean();
					DynaBean headerBean = rows.get(0);
					historyBean.setBatteryGroupID(ObjectUtils.toString(headerBean.get("nbid"))+"_"+ObjectUtils.toString(headerBean.get("batteryid")));
					historyBean.setCountry(ObjectUtils.toString(headerBean.get("country")));//國家
					historyBean.setArea(ObjectUtils.toString(headerBean.get("area")));//地域
					historyBean.setGroupID(ObjectUtils.toString(headerBean.get("groupid")));//基地台號碼
					historyBean.setGroupName(ObjectUtils.toString(headerBean.get("groupname")));//基地台名稱
					historyBean.setInstallDate(ToolUtil.dateFormat(headerBean.get("installdate"), date));//安裝日期
					historyBean.setBatteryType(ObjectUtils.toString(headerBean.get("batterytypename")));//電池型號
					historyBean.setAddress(ObjectUtils.toString(headerBean.get("address")));//地址

					historyBean.setIMPType(impType);
					String batteryId = ObjectUtils.toString(headerBean.get("batteryid"));

					/*
					 * Add by Austin (2021/12/20)
					 */
					String rceCompanyCode = new String();
					CompanyDAO companyDAO = new CompanyDAO();
					//查詢預設公司(RCE)					
					List<DynaBean> list = companyDAO.getDefault();
					if (list != null && !list.isEmpty()) {
						DynaBean bean = list.get(0);
						rceCompanyCode = ObjectUtils.toString(bean.get("companycode"));	// RCE => CompanyCode=1
					}
					/*
					 * 
					 */
					
					NbListVO nbListVO = new NbListVO();
					nbListVO.setNbId(ObjectUtils.toString(headerBean.get("nbid")));
					nbListVO.setGroupInternalId(ObjectUtils.toString(headerBean.get("seqno")));
					nbListVO.setContinuousSeqNo(ObjectUtils.toString(headerBean.get("continuousseqno")));
					if (!companyCode.equals(rceCompanyCode))
						nbListVO.setCompanyCode(companyCode);
					NbListDAO nbListDAO = new NbListDAO();			
					List<DynaBean> groupHisList = nbListDAO.getNBGroupHis(nbListVO);//查詢通訊序號紀錄
					List<RecordBean> recordList = new ArrayList<RecordBean>();
					if(groupHisList != null && !groupHisList.isEmpty()) {
						int seq = 0;
						RecordBean recordBean = null;				
						Map<String, RecordBean> recordBeanMap = new HashMap<String, RecordBean>();
						String zone="";
						
						for(DynaBean hisBean : groupHisList) {
							String groupStartTime = ToolUtil.dateFormat(hisBean.get("starttime"), sdf2);
							String groupEndTime = ToolUtil.dateFormat(hisBean.get("endtime"), sdf2);
							
							BatteryVO vo = new BatteryVO();
							vo.setNbId(ObjectUtils.toString(hisBean.get("nbid")));
							vo.setBatteryId(batteryId);
							if(sdf2.parse(batteryVO.getStartDate()).after(sdf2.parse(groupStartTime))) {
								vo.setStartDate(batteryVO.getStartDate());
							}else {
								vo.setStartDate(groupStartTime);
							}
							if(sdf2.parse(batteryVO.getEndDate()).before(sdf2.parse(groupEndTime))) {
								vo.setEndDate(batteryVO.getEndDate());
							}else {
								vo.setEndDate(groupEndTime);
							}
							vo.setRecTimeDesc(true);
							
							List<DynaBean> reclist = batteryDAO.getBatteryHistory(vo);//查詢電池歷史紀錄					
							if (reclist != null && !reclist.isEmpty()) {	
								for(int i=0; i<reclist.size(); i++) {							
									DynaBean bean = reclist.get(i);
									if(bean.get("timezone")!=null && !zone.equals(bean.get("timezone").toString())) {
										zone = bean.get("timezone").toString();
										sdf = ToolUtil.getDateFormat("yyyy/MM/dd HH:mm:ss Z", zone);
									}
									String rectime = ToolUtil.dateFormat(bean.get("rectime"), sdf);
									if(recordBeanMap.containsKey(rectime)) {
										recordBean = recordBeanMap.get(rectime);					
									}else {				
										recordBean = new RecordBean();				
										recordBean.setSeq(++seq);
										recordBean.setBatteryGroupID(ObjectUtils.toString(bean.get("nbid"))+"_"+ObjectUtils.toString(bean.get("batteryid")));
										recordBean.setRecTime(rectime);//數據更新時間

										recordBean.setIR(new ArrayList<String>());
										recordBean.setVol(new ArrayList<String>());
										recordBean.setStatusDesc(new ArrayList<String>());
										recordBean.setStatusCode(new ArrayList<Integer>());
										
										recordBeanMap.put(rectime, recordBean);
										recordList.add(recordBean);
									}
									
									String category = ObjectUtils.toString(bean.get("category"));
									String ch = "CH"+bean.get("orderno");
									if("1".equals(category)) {										
										BigDecimal ir;
										if(impType==22) {//電導值
											ir = ToolUtil.divide(1000000, bean.get("value"), 3);
										}else if(impType==21) {//毫內阻值
											ir = ToolUtil.divide(bean.get("value"), 1000, 3);
										}else {//內阻值
											ir = ToolUtil.getBigDecimal(bean.get("value"), 0);
										}
										if(ToolUtil.getBigDecimal(bean.get("value"), 0).compareTo(ol)>=0) {
											recordBean.getIR().add(ch+" OL");
										}else {						
											recordBean.getIR().add(ch+" "+ir);
										}
																				
										if(StringUtils.isNotBlank(ObjectUtils.toString(bean.get("status")))) {
											recordBean.getStatusCode().add(Integer.parseInt(bean.get("status").toString()));
											recordBean.getStatusDesc().add(resource.getString(ObjectUtils.toString(bean.get("status"))));
										}else {
											recordBean.getStatusCode().add(1);
											recordBean.getStatusDesc().add(resource.getString("1"));
										}
									}else if("2".equals(category)) {
										BigDecimal vol = ToolUtil.divide(bean.get("value"), 1000, 2);
										recordBean.getVol().add(ch+" "+vol);//電壓
									}else if("3".equals(category)) {
										BigDecimal temperature = ToolUtil.getBigDecimal(bean.get("value"));
										recordBean.setTemperature(temperature);//溫度
									}				
								}								
							}
						}
					}		
					
					logger.debug("Process" +threadId+" Create " + batteryGroupId + " Sheet");		
					
					StringBuilder str = new StringBuilder();
					str.append(resource.getString("1026")).append(",")//電池組ID
					   .append(resource.getString("1028")).append(",")//國家
					   .append(resource.getString("1029")).append(",")//地域
					   .append(resource.getString("1012")).append(",")//站台編號
					   .append(resource.getString("1013")).append(",")//站台名稱
					   .append(resource.getString("1027")).append(",")//安裝日期
					   .append(resource.getString("1030")).append(",")//型號
					   .append(resource.getString("1031")).append("\n");//地址
					   
					str.append(historyBean.getBatteryGroupID()).append(",")
					   .append(historyBean.getCountry()).append(",")
					   .append(historyBean.getArea()).append(",")
					   .append(historyBean.getGroupID()).append(",")
					   .append(historyBean.getGroupName()).append(",")
					   .append(historyBean.getInstallDate()).append(",")
					   .append(historyBean.getBatteryType()).append(",")
					   .append(CsvUtil.csvHandlerStr(historyBean.getAddress())).append("\n");
					   
					str.append("\n");
					str.append(resource.getString("1015")).append(",");//數據更新時間					
					if(historyBean.getIMPType()==22) {//電導值
						str.append(resource.getString("1020")).append(",");
					}else if(historyBean.getIMPType()==21) {//毫內阻值
						str.append(resource.getString("1019")).append(",");
					}else {//內阻值
						str.append(resource.getString("1016")).append(",");
					}				
					str.append(resource.getString("1017")).append(",")//電壓
					   .append(resource.getString("1018")).append(",")//溫度
					   .append(resource.getString("1021")).append("\n");//電池狀態
		
										
					for(RecordBean bean : recordList) {	
						List<String> irlList = bean.getIR();
						List<String> volList = bean.getVol();
						List<Integer> statusList = bean.getStatusCode();
						int count = irlList.size()>volList.size()?irlList.size():volList.size();				
						for(int j=0; j<count; j++) {

							if(j==0) {
								str.append(bean.getRecTime()).append(",");
							}else {
								str.append("").append(",");
							}
							
							if(j<irlList.size()) {
								str.append(irlList.get(j)).append(",");
							}else {
								str.append("").append(",");
							}
							if(j<volList.size()) {
								str.append(volList.get(j)).append(",");
							}else {
								str.append("").append(",");
							}
							
							if(j==0) {
								str.append(bean.getTemperature()).append(",");
							}else {
								str.append("").append(",");
							}
												
							if(j<statusList.size()) {				
								String status = String.valueOf(statusList.get(j));								
								str.append(resource.getString(status)).append("\n");									
							}else {
								str.append("").append("\n");	
							}							
						}			
					}
					
					File csvFile = new File(path+File.separator+historyBean.getBatteryGroupID()+".csv");
					csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
							  csvFile),"UTF-8"),1024);

					//寫入前段位元組流，防止亂碼
					csvFileOutputStream.write(CsvUtil.getBOM());
					csvFileOutputStream.write(str.toString());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}finally {
				if(csvFileOutputStream!=null)
					try {
						csvFileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				logger.debug("Process" +threadId+ " "+ batteryGroupId + " Success");
			}
		}
	}
	
	/**
	 * 刪除目錄(需先刪除底下檔案)
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	        	boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
		}
	    return dir.delete();
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
