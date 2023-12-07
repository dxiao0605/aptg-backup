package aptg.battery.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
 * Servlet implementation class getLogoImages 取得公司Logo
 */
@WebServlet("/getLogoImages")
public class getLogoImages extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getLogoImages.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getLogoImages() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("getLogoImages start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String userCompany = ObjectUtils.toString(request.getHeader("company"));
			String company = ObjectUtils.toString(request.getParameter("companyCode"));
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token) && StringUtils.isNotBlank(userCompany) ) {
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
						getImages(list, response);												
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
			ToolUtil.response(rspJson.toString(), response);
		} 
		logger.debug("getLogoImages end");
	}
	
	/**
	 * 取得圖檔
	 * @param rows
	 * @param response
	 * @throws Exception
	 */
	private void getImages(List<DynaBean> rows, HttpServletResponse response) throws Exception {
		ServletOutputStream out = null;
		BufferedInputStream buf = null;
		try {
			DynaBean bean = rows.get(0);						
			String path = ObjectUtils.toString(bean.get("logopath"));
			if(StringUtils.isNotBlank(path)) {
				File file = new File(path);			
				// 使用输入读取缓冲流读取一个文件输入流
				buf = new BufferedInputStream(new FileInputStream(file));
				// 利用response获取一个字节流输出对象
				out = response.getOutputStream();
				// 定义个数组，由于读取缓冲流中的内容
				byte[] buffer = new byte[1024];
				// while循环一直读取缓冲流中的内容到输出的对象中
				while (buf.read(buffer) != -1) {
					out.write(buffer);
				}
				// 写出到请求的地方
				out.flush();
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		} finally {
			if (buf != null)buf.close();
			if (out != null)out.close();
		}
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
