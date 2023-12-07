package aptg.battery.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

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

import aptg.battery.dao.CompanyDAO;
import aptg.battery.util.ToolUtil;
import aptg.battery.vo.CompanyVO;

/**
 * Servlet implementation class getCompanyLogo 判斷是否有公司Logo
 */
@WebServlet("/getCompanyLogo")
public class getCompanyLogo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getCompanyLogo.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getCompanyLogo() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("getCompanyLogo start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String language = ObjectUtils.toString(request.getHeader("language"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String company = ObjectUtils.toString(request.getParameter("companyCode"));
			ResourceBundle resource = ToolUtil.getLanguage(language);
			if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userCompany)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					CompanyVO companyVO = new CompanyVO();
					if(StringUtils.isNotBlank(company)) {
						companyVO.setCompanyCode(company);
					}else {
						companyVO.setCompanyCode(userCompany);	
					}					
					CompanyDAO companyDAO = new CompanyDAO();					
					List<DynaBean> list = companyDAO.getCompanyInfo(companyVO);					
					if (list != null && !list.isEmpty()) {						
						rspJson.put("msg", convertToJson(list));						
						rspJson.put("code", "00");
					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", resource.getString("5004"));// 查無資料
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
		ToolUtil.response(rspJson.toString(), response);
		logger.debug("getCompanyLogo end");
	}
	
	/**
	 * 組Json
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<DynaBean> rows) throws Exception {
		JSONObject data = new JSONObject();
		ByteArrayOutputStream baos = null;
		try {
			DynaBean bean = rows.get(0);						
			int imagesFlag = 0;//0:沒圖檔, 1:有圖檔
			String imagesPath = ObjectUtils.toString(bean.get("logopath"));
			if(StringUtils.isNotBlank(imagesPath)) {
				imagesFlag = 1;
//				File imagesFile = new File(imagesPath);
//				BufferedImage image = ImageIO.read(imagesFile);
//				int height = image.getHeight();
//				if(height<75) {
//					data.put("Width", "100%");
//				}else {
//					BigDecimal heightP = ToolUtil.divide(75, height, 2);
//					data.put("Width", heightP.multiply(new BigDecimal(100)).setScale(0)+"%");
//				}				
			}
//			else {
//				data.put("Width", "");
//			}
			data.put("ImagesFlag", imagesFlag);						
			data.put("ShowName", ObjectUtils.toString(bean.get("showname")));
			data.put("ShortName", ObjectUtils.toString(bean.get("shortname")));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}finally {
			if(baos!=null)baos.close();
		}
		return data;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
