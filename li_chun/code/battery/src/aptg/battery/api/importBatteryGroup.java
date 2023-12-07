package aptg.battery.api;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.json.JSONObject;

import aptg.battery.dao.BatteryGroupDAO;
import aptg.battery.util.ExcelUtil;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.BatteryGroupVO;

/**
 * Servlet implementation class importBatteryGroup 匯入站台
 */
@WebServlet("/importBatteryGroup")
public class importBatteryGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(importBatteryGroup.class.getName());
	private static final int maxCount = 5000;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public importBatteryGroup() {
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
		logger.debug("importBatteryGroup start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			logger.debug("token: " + token);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					resource = ToolUtil.getLanguage(language);
					SimpleDateFormat sdf = ToolUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", timezone);
					BatteryGroupVO batteryGroupVO = this.parseExcel(request, resource, sdf);
					if (batteryGroupVO.isError()) {
						rspJson.put("code", batteryGroupVO.getCode());
						rspJson.put("msg", resource.getString("5057") +" "+ batteryGroupVO.getDescription());
					} else {
						BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
						int count = batteryGroupVO.getDataList().size();
						if (count > 0) {
							batteryGroupDAO.addBatteryGroupBatch(batteryGroupVO.getDataList());
						}
						rspJson.put("code", "00");
						rspJson.put("msg", resource.getString("5021") + count);// 匯入成功，筆數:
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
		logger.debug("importBatteryGroup end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Excel
	 * 
	 * @param request
	 * @param resource
	 * @param sdf
	 * @return
	 * @throws Exception
	 */
	private BatteryGroupVO parseExcel(HttpServletRequest request, ResourceBundle resource, SimpleDateFormat sdf)
			throws Exception {
		InputStream is = null;
		BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
		try {
			String userName = "";
			String companyCode = "";
			String fileName = "";
			if (ServletFileUpload.isMultipartContent(request)) {
				// 1. 建立DiskFileItemFactory物件
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// 2. 建立ServletFileUpload物件
				ServletFileUpload sfu = new ServletFileUpload(factory);
				List<FileItem> formItems = sfu.parseRequest(request);
				for (int i = 0; i < formItems.size(); i++) {
					FileItem item = formItems.get(i);
					if (!item.isFormField()) {// 判斷是否為檔案
						fileName = item.getName();// 取得上傳的檔名稱
						is = item.getInputStream();
					} else {
						String field = item.getFieldName();
						String value = Streams.asString(item.getInputStream(), CharEncoding.UTF_8);
						if (field.equals("username")) {
							userName = value;
						} else if (field.equals("company")) {
							companyCode = value;
						}
					}
				}

				String fileType = (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())).toLowerCase();
				if ("xlsx".equals(fileType) || "xls".equals(fileType)) {
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
						batteryGroupVO.setError(true);
						batteryGroupVO.setCode("23");
						batteryGroupVO.setDescription(resource.getString("5025"));// 超過筆數上限
					} else if (count > 0) {
						List<String> groupIdList = new ArrayList<String>();
						List<BatteryGroupVO> dataList = new ArrayList<BatteryGroupVO>();
						for (int i = 1; i <= count; i++) {
							Row row = sheet.getRow(i);
							if (excelUtil.checkBlankRow(row, 5)) {
								break;
							}
							String groupId = excelUtil.getCellValue(row.getCell(0));
							String groupName = excelUtil.getCellValue(row.getCell(1));
							String country = excelUtil.getCellValue(row.getCell(2));
							String area = excelUtil.getCellValue(row.getCell(3));
							String address = excelUtil.getCellValue(row.getCell(4));

							if (StringUtils.isBlank(groupId)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("24");
								batteryGroupVO.setDescription(resource.getString("1012") + resource.getString("5008"));// 站台號碼必填欄位不能為空
								break;
							} else if (!ToolUtil.lengthCheck(groupId, 20)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("25");
								batteryGroupVO.setDescription(resource.getString("1012") + resource.getString("5024"));// 站台號碼長度不符
								break;
							} else if (!ToolUtil.strCheck(groupId)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("27");
								batteryGroupVO.setDescription(resource.getString("1012") + resource.getString("5034"));// 站台號碼格式錯誤
								break;
							} else if (StringUtils.isBlank(groupName)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("24");
								batteryGroupVO.setDescription(resource.getString("1013") + resource.getString("5008"));// 站台名稱必填欄位不能為空
								break;
							} else if (!ToolUtil.lengthCheck(groupName, 20)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("25");
								batteryGroupVO.setDescription(resource.getString("1013") + resource.getString("5024"));// 站台名稱長度不符
								break;
							} else if (StringUtils.isBlank(country)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("24");
								batteryGroupVO.setDescription(resource.getString("1028") + resource.getString("5008"));// 國家必填欄位不能為空
								break;
							} else if (!ToolUtil.lengthCheck(country, 20)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("25");
								batteryGroupVO.setDescription(resource.getString("1028") + resource.getString("5024"));// 國家長度不符
								break;
							} else if (StringUtils.isBlank(area)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("24");
								batteryGroupVO.setDescription(resource.getString("1029") + resource.getString("5008"));// 地域必填欄位不能為空
								break;
							} else if (!ToolUtil.lengthCheck(area, 20)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("25");
								batteryGroupVO.setDescription(resource.getString("1029") + resource.getString("5024"));// 地域長度不符
								break;
							} else if (StringUtils.isBlank(address)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("24");
								batteryGroupVO.setDescription(resource.getString("1031") + resource.getString("5008"));// 地址必填欄位不能為空
								break;
							} else if (!ToolUtil.lengthCheck(address, 200)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("25");
								batteryGroupVO.setDescription(resource.getString("1031") + resource.getString("5024"));// 地址長度不符
								break;
							} else if (checkGroupID(companyCode, groupId)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("20");
								batteryGroupVO.setDescription(groupId + resource.getString("5032"));// 站台號碼重複
								break;
							} else if (groupIdList.contains(groupId)) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("21");
								batteryGroupVO.setDescription(groupId + resource.getString("5023"));// 檔案內資料重複
								break;
							} else if (count > maxCount) {
								batteryGroupVO.setError(true);
								batteryGroupVO.setCode("23");
								batteryGroupVO.setDescription(resource.getString("5025"));// 超過筆數上限
								break;
							} else {
								groupIdList.add(groupId);
								BatteryGroupVO vo = new BatteryGroupVO();
								vo.setCompanyCode(companyCode);
								vo.setGroupID(groupId);
								vo.setGroupName(groupName);
								vo.setCountry(country);
								vo.setArea(area);
								vo.setAddress(address);
								JSONObject addressJson = ToolUtil.getLonLat(address);
								if (address != null) {
									vo.setLat(addressJson.optString("lat"));
									vo.setLng(addressJson.optString("lng"));
								}
								vo.setDefaultGroup("1");
								vo.setUserName(userName);

								dataList.add(vo);
							}
						}
						batteryGroupVO.setDataList(dataList);
					}
				} else {
					batteryGroupVO.setError(true);
					batteryGroupVO.setCode("19");
					batteryGroupVO.setDescription(resource.getString("5028"));// 請上傳Excel檔
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return batteryGroupVO;
	}

	/**
	 * 檢核站台號碼是否存在
	 * 
	 * @param companyCode
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	private boolean checkGroupID(String companyCode, String groupId) throws Exception {
		BatteryGroupVO batteryGroupVO = new BatteryGroupVO();
		batteryGroupVO.setCompanyCode(companyCode);
		batteryGroupVO.setGroupID(groupId);
		BatteryGroupDAO batteryGroupDAO = new BatteryGroupDAO();
		List<DynaBean> list = batteryGroupDAO.getBatteryGroup(batteryGroupVO);
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
