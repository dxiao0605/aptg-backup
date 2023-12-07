package aptg.battery.api;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import aptg.battery.config.SysConfig;
import aptg.battery.dao.CompanyDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CompanyVO;

/**
 * Servlet implementation class uploadLogo 取消Logo設定
 */
@WebServlet("/uploadLogo")
public class uploadLogo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(uploadLogo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public uploadLogo() {
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
		logger.debug("uploadLogo start");
		JSONObject rspJson = new JSONObject();
		ResourceBundle resource = null;
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String timezone = ObjectUtils.toString(request.getHeader("timezone"));
			resource = ToolUtil.getLanguage(language);
			logger.debug("token: " + token);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {					
					CompanyVO companyVO = this.parseRequest(request, resource, timezone);
					if (companyVO.isError()) {
						rspJson.put("msg", companyVO.getDescription());
						rspJson.put("code", companyVO.getCode());
					} else {
						CompanyDAO companyDAO = new CompanyDAO();
						if(StringUtils.isNotBlank(companyVO.getLogoPath())) {
							List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);
							if (list != null && !list.isEmpty()) {
								//刪除舊的圖檔
								DynaBean bean = list.get(0);
								if(bean.get("logopath")!=null) {
									File oldImage = new File(bean.get("logopath").toString());
									if(oldImage!=null && oldImage.exists())
										oldImage.delete();
								}							
							}
						}

						companyDAO.updLogoPathAndShowName(companyVO);
						
						rspJson.put("msg", resource.getString("5002"));
						rspJson.put("code", "00");
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
		logger.debug("uploadLogo end");
		ToolUtil.response(rspJson.toString(), response);
	}

	/**
	 * 解析Request
	 * 
	 * @param request
	 * @param timezone
	 * @return
	 * @throws Exception
	 */
	private CompanyVO parseRequest(HttpServletRequest request, ResourceBundle resource, String timezone) throws Exception {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		InputStream itemStream = null;
		File uploadFile = null;
		CompanyVO companyVO = new CompanyVO();
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				// 1. 建立DiskFileItemFactory物件，設定緩衝區大小和臨時檔案目錄
				DiskFileItemFactory factory = new DiskFileItemFactory();

				// 2. 建立ServletFileUpload物件，並設定上傳檔案的大小限制。
				ServletFileUpload sfu = new ServletFileUpload(factory);
				sfu.setSizeMax(10 * 1024 * 1024);// 以byte為單位 不能超過10M 1024byte =
				// 1kb 1024kb=1M 1024M = 1G
				FileItemIterator fii = sfu.getItemIterator(request);// 解析request 請求,並返回FileItemIterator集合
				while (fii.hasNext()) {
					FileItemStream fis = fii.next();// 從集合中獲得一個檔案流
					if (!fis.isFormField()) {// 過濾掉表單中非檔案域
						String fileName = fis.getName();// 獲得上傳檔案的檔名
						String fileType = (fileName.substring(fileName.lastIndexOf(".")+1, fileName.length())).toLowerCase();
						if("png".equals(fileType)) {
							String uploadPath = SysConfig.getInstance().getImagesPath();// 選定上傳的目錄此處為當前目錄
							File uploadDir = new File(uploadPath);
							if (!uploadDir.isDirectory())// 選定上傳的目錄此處為當前目錄,沒有則建立
								uploadDir.mkdirs();
							// 將時間轉化為字串用於給檔案或者資料夾改名,防止傳上來的圖片名稱相同
							String dirTime = ToolUtil.getDateFormat("yyyyMMddHHmmssSSS", timezone).format(new Date());
							int extIndex = fileName.lastIndexOf(".");
							if (extIndex > 0) {
								fileName = fileName.substring(0, extIndex) + "-" + dirTime + fileName.substring(extIndex);
							}
							String uploadFilePath = uploadPath + File.separator + fileName;
							companyVO.setLogoPath(uploadFilePath);
							uploadFile = new File(uploadFilePath);
							in = new BufferedInputStream(fis.openStream());// 獲得檔案輸入流
							out = new BufferedOutputStream(new FileOutputStream(uploadFile));// 獲得檔案輸出流
							Streams.copy(in, out, true);// 開始把檔案寫到你指定的上傳資料夾
							
							//檢核圖檔長寬
							BufferedImage image = ImageIO.read(uploadFile);
							if(image.getWidth()>200 || image.getHeight()>75) {
								companyVO.setError(true);
								companyVO.setCode("29");	
								companyVO.setDescription(resource.getString("5038"));//圖檔長寬過長								
							}
						}else {
							companyVO.setError(true);
							companyVO.setCode("19");
							companyVO.setDescription(resource.getString("5037"));//請上傳PNG檔
						}
					} else {
						itemStream = fis.openStream();
						String field = fis.getFieldName();
						String value = Streams.asString(itemStream, CharEncoding.UTF_8);
						if (field.equals("username")) {
							companyVO.setUserName(value);
						} else if (field.equals("showName")) {							
							if(StringUtils.isNotBlank(value)&&value.length()>10) {
								companyVO.setError(true);
								companyVO.setCode("22");
								companyVO.setDescription(resource.getString("1724") + resource.getString("5024"));// 顯示名稱長度不符
								return companyVO;		
							}
							companyVO.setShowName(value);
						} else if (field.equals("shortName")) {							
							if(StringUtils.isNotBlank(value)&&value.length()>5) {
								companyVO.setError(true);
								companyVO.setCode("22");
								companyVO.setDescription(resource.getString("1724") + resource.getString("5024"));// 顯示名稱長度不符
								return companyVO;		
							}
							companyVO.setShortName(value);
						} else if (field.equals("Company")) {							
							if(StringUtils.isBlank(value)) {
								companyVO.setError(true);
								companyVO.setCode("22");
								companyVO.setDescription(resource.getString("1064")+resource.getString("5008"));//公司不能為空
								return companyVO;		
							}
							companyVO.setCompanyCode(value);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (itemStream != null)
				itemStream.close();
			if(companyVO.isError()) {
				if(uploadFile!=null && uploadFile.exists())
					uploadFile.delete();
			}
		}
		return companyVO;
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
