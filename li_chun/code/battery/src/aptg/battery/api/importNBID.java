package aptg.battery.api;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.battery.dao.CompanyDAO;
import aptg.battery.dao.NbListDAO;
import aptg.battery.util.ExcelUtil;
import aptg.battery.util.MqttUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.NbListVO;

/**
 * Servlet implementation class importNBID 匯入通訊序號
 */
@WebServlet("/importNBID")
public class importNBID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(importNBID.class.getName());
	private static final int maxCount = 5000;
	private static final String MQTT = "mqtt";
	private static final String PUBLISH_TOPIC = "nbcompany.topics";
	private static final String PUBLISH_MQTTID = "nbcompany.mqttid";
	private static final String PUBLISH_QOS = "nbcompany.qos";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public importNBID() {
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
		logger.debug("importNBID start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
//			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			logger.debug("token: " + token);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					resource = ToolUtil.getLanguage(language);
					NbListVO nbListVO = this.parseExcel(request, resource);
					if (nbListVO.isError()) {
						JSONObject msg = new JSONObject();
						rspJson.put("code", nbListVO.getCode());
						msg.put("NBList", new JSONArray());
						msg.put("Message", resource.getString("5057") +" "+ nbListVO.getDescription());
						rspJson.put("msg", msg);
					} else {
						NbListDAO nbListDAO = new NbListDAO();
						int count = nbListVO.getDataList().size();
						if (count > 0) {
							nbListDAO.addNBListBatch(nbListVO);

							// 通知MQTT
							ResourceBundle mqttConfig = ResourceBundle.getBundle(MQTT);
							String topic = mqttConfig.getString(PUBLISH_TOPIC);
							String mqttid = mqttConfig.getString(PUBLISH_MQTTID);
							String qos = mqttConfig.getString(PUBLISH_QOS);
							String payload = nbListVO.getJson().toString();
							logger.debug("Send MQTT: " + payload);
							MqttUtil.getInstance().sendCMD(topic, mqttid, qos, payload);
						}
						JSONObject msg = new JSONObject();
						rspJson.put("code", "00");
						msg.put("NBList", nbListVO.getJson());
						msg.put("Message", resource.getString("5021") + count);// 匯入成功，筆數:
						rspJson.put("msg", msg);
					}
				}
			} else {
				rspJson.put("code", "01");
				rspJson.put("msg", "缺少參數");
			}
		} catch (Exception e) {
			rspJson.put("code", "99");
			rspJson.put("msg", resource.getString("5003"));// 保存失敗
			logger.error("", e);
		}
		logger.debug("rsp: " + rspJson);
		logger.debug("importNBID end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Excel
	 * 
	 * @param request
	 * @param resource
	 * @return NbListVO
	 * @throws Exception
	 */
	private NbListVO parseExcel(HttpServletRequest request, ResourceBundle resource) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		InputStream is = null;
		NbListVO nbListVO = new NbListVO();
		try {
			String userName = "";
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
						}
					}
				}
				String fileType = (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())).toLowerCase();
				if ("xlsx".equals(fileType) || "xls".equals(fileType)) {
					// 取得預設公司、預設群組
					String companyCode = "", companyName = "", groupInternalId = "";
					CompanyDAO companyDAO = new CompanyDAO();
					List<DynaBean> list = companyDAO.getDefault();
					if (list != null && !list.isEmpty()) {
						DynaBean bean = list.get(0);
						companyCode = ObjectUtils.toString(bean.get("companycode"));
						companyName = ObjectUtils.toString(bean.get("companyname"));
						groupInternalId = ObjectUtils.toString(bean.get("seqno"));
					}
					ExcelUtil excelUtil = new ExcelUtil();
					Workbook wb = null;
					if ("xlsx".equals(fileType)) {
						wb = new XSSFWorkbook(is);
					} else {
						wb = new HSSFWorkbook(is);
					}
					Sheet sheet = wb.getSheetAt(0);
					int count = sheet.getLastRowNum();
					if (count > maxCount) {
						nbListVO.setError(true);
						nbListVO.setCode("23");
						nbListVO.setDescription(resource.getString("5025"));// 超過筆數上限
					} else if (count > 0) {
						List<String> nbidList = new ArrayList<String>();
						List<NbListVO> dataList = new ArrayList<NbListVO>();
						List<NbListVO> insertList = new ArrayList<NbListVO>();
						List<NbListVO> updateList = new ArrayList<NbListVO>();
						JSONArray nbList = new JSONArray();
						String sysdate = sdf.format(new Date());
						String endTime = "2038-01-01 00:00:00";
						for (int i = 1; i <= count; i++) {
							Row row = sheet.getRow(i);
							if (excelUtil.checkBlankRow(row, 1)) {
								break;
							}
							String nbid = excelUtil.getCellValue(row.getCell(0));
							
							if (checkNBID(nbid)) {
								nbListVO.setError(true);
								nbListVO.setCode("20");
								nbListVO.setDescription(resource.getString("5022") + nbid);// 通訊序號重複
								break;
							} else if (nbidList.contains(nbid)) {
								nbListVO.setError(true);
								nbListVO.setCode("21");
								nbListVO.setDescription(resource.getString("5023") + nbid);// 檔案內資料重複
								break;
							} else if (nbid.length() != 10) {
								nbListVO.setError(true);
								nbListVO.setCode("22");
								nbListVO.setDescription(resource.getString("1057") + resource.getString("5024"));// 通訊序號長度不符
								break;
							} else if (!ToolUtil.strCheck(nbid)) {
								nbListVO.setError(true);
								nbListVO.setCode("22");
								nbListVO.setDescription(resource.getString("1057") + resource.getString("5034"));// 通訊序號格式錯誤
								break;
							} else {
								
								
								
								nbidList.add(nbid);
								NbListVO vo = new NbListVO();
								JSONObject nb = new JSONObject();
								vo.setNbId(nbid);
								vo.setGroupInternalId(groupInternalId);
								vo.setCompanyCode(companyCode);
								vo.setStartTime(sysdate);
								vo.setEndTime(endTime);
								vo.setUserName(userName);
								vo.setModifyItem("7");// 7:匯入並啟用		
								
								nb.put("NBID", nbid);
								nb.put("CompanyCode", companyCode);
								nb.put("CompanyName", companyName);
								if(checkDelNBID(nbid)) {//是否為刪除序號)
									updateList.add(vo);
								}else {
									insertList.add(vo);
								}								
								dataList.add(vo);
								nbList.put(nb);
							}
						}
						nbListVO.setDataList(dataList);
						nbListVO.setInsertList(insertList);
						nbListVO.setUpdateList(updateList);
						nbListVO.setJson(nbList);
					}
				} else {
					nbListVO.setError(true);
					nbListVO.setCode("19");
					nbListVO.setDescription(resource.getString("5028"));// 請上傳Excel檔
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if(is!=null)
				is.close();
		}
		return nbListVO;
	}

	/**
	 * 檢核NBID是否存在
	 * 
	 * @param nbid
	 * @return
	 * @throws Exception
	 */
	private boolean checkNBID(String nbid) throws Exception {
		NbListVO nbListVO = new NbListVO();
		nbListVO.setNbId(nbid);
		NbListDAO nbListDAO = new NbListDAO();
		List<DynaBean> list = nbListDAO.checkNBID(nbListVO);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 檢核NBID是否為刪除
	 * 
	 * @param nbid
	 * @return
	 * @throws Exception
	 */
	private boolean checkDelNBID(String nbid) throws Exception {
		NbListVO nbListVO = new NbListVO();
		nbListVO.setNbId(nbid);
		NbListDAO nbListDAO = new NbListDAO();
		List<DynaBean> list = nbListDAO.checkDelNBID(nbListVO);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
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
