package aptg.cathaybkeco.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.dao.BestRatePlanDAO;
import aptg.cathaybkeco.dao.RatePlanListDAO;
import aptg.cathaybkeco.util.ExcelUtil;
import aptg.cathaybkeco.util.PdfUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BestRatePlanVO;

/**
 * Servlet implementation class getBestRatePlan 最適電費
 */
@WebServlet("/getBestRatePlan")
public class getBestRatePlan extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBestRatePlan.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBestRatePlan() {
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
		logger.debug("getBestRatePlan start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String filter = ObjectUtils.toString(request.getParameter("filter"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String powerAccount = ObjectUtils.toString(request.getParameter("powerAccount"));
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			String type = ObjectUtils.toString(request.getParameter("type"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			logger.debug("token: " + token);
			logger.debug("City:" + city + ",PostCodeNo:" + postCodeNo+
					",BankCode: " + bankCode + ", PowerAccount: " + powerAccount);
			logger.debug("date: " + start + " ~ " + end);
			logger.debug("type:" + type + ",filter:" + filter+ ",Account:"+account);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");
				} else {
					BestRatePlanVO bestRatePlanVO = new BestRatePlanVO();
					bestRatePlanVO.setCity(city);
					bestRatePlanVO.setPostCodeNo(postCodeNo);
					bestRatePlanVO.setBankCode(bankCode);
					if(StringUtils.isNotBlank(bankCode)) {
						bestRatePlanVO.setBankCode(bankCode);
					}else {
						String userRank = "", userArea = "";
						AdminSetupDAO adminSetupDAO = new AdminSetupDAO();
						List<DynaBean> rank = adminSetupDAO.getRankCode(account);
						if (rank != null && !rank.isEmpty()) {
							userRank = ObjectUtils.toString(rank.get(0).get("rankcode"));
							userArea = ObjectUtils.toString(rank.get(0).get("areacodeno"));
						}
	
						if("3".equals(userRank) || "4".equals(userRank)) {//區域管理者或區域使用者
							AreaDAO areaDAO = new AreaDAO();
							List<DynaBean> area = areaDAO.getAccessBanks(userArea);
							String valueStr = "";
							if (area != null && !area.isEmpty()) {
								for (DynaBean bean : area) {
									valueStr += ((StringUtils.isNotBlank(valueStr) ? ",'":"'") + ObjectUtils.toString(bean.get("bankcode")) + "'");
								}
							}
							bestRatePlanVO.setBankCodeArr(valueStr);
						}
					}
					bestRatePlanVO.setPowerAccount(powerAccount);
					bestRatePlanVO.setStartDate(start);
					bestRatePlanVO.setEndDate(end);
					String ratePlanCode = new String();

					if (filter.contains("Lamp")) {
						ratePlanCode = "1,2,3,4,5";
					}
					if (filter.contains("Low")) {
						ratePlanCode += (StringUtils.isNotBlank(ratePlanCode) ? "," : "") + "6,7";
					}
					if (filter.contains("High")) {
						ratePlanCode += (StringUtils.isNotBlank(ratePlanCode) ? "," : "") + "8,9";
					}
					bestRatePlanVO.setRatePlanCode(ratePlanCode);
					BestRatePlanDAO bestRatePlanDAO = new BestRatePlanDAO();
					List<DynaBean> list = bestRatePlanDAO.getBestRatePlan(bestRatePlanVO);
					if (list != null && list.size() > 0) {
						List<BestRatePlanVO> VOList = beanToVO(list);
						rspJson.put("code", "00");
						rspJson.put("count", VOList != null ? VOList.size() : 0);
						if ("excel".equals(type)) {							
							rspJson.put("msg", composeExcel(VOList, filter));
						} else {
							rspJson.put("msg", convertToJson(VOList));
						}

					} else {
						rspJson.put("code", "07");
						rspJson.put("msg", "查無資料");
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
		logger.debug("getBestRatePlan end");
	}

	private List<BestRatePlanVO> beanToVO(List<DynaBean> rows) throws Exception {
		List<BestRatePlanVO> bestRatePlanList = new ArrayList<BestRatePlanVO>();
		BigDecimal best = new BigDecimal(0);
		try {
			Map<String, String> ratePlanMap = new HashMap<String, String>();
			
			RatePlanListDAO ratePlanListDAO = new RatePlanListDAO();
			List<DynaBean> ratePlanList = ratePlanListDAO.getRatePlanCodeList(null);
			if (ratePlanList != null && !ratePlanList.isEmpty()) {
				for (DynaBean bean : ratePlanList) {
					ratePlanMap.put(ObjectUtils.toString(bean.get("rateplancode")), ObjectUtils.toString(bean.get("rateplandesc")));
				}
			}
			
			List<String> bestList = null; 
			String powerAccount = new String();
			BestRatePlanVO bestRatePlanVO = null;
			for (DynaBean bean : rows) {
				if (!powerAccount.equals(bean.get("poweraccount"))) {
					powerAccount = bean.get("poweraccount").toString();
					bestList = new ArrayList<String>();
					best = ToolUtil.getBigDecimal(bean.get("totalcharge"));
					bestRatePlanVO = new BestRatePlanVO();
					bestRatePlanList.add(bestRatePlanVO);
					bestRatePlanVO.setBankCode(ObjectUtils.toString(bean.get("bankcode")));
					bestRatePlanVO.setBankName(ObjectUtils.toString(bean.get("bankname")));
					bestRatePlanVO.setPowerAccount(powerAccount);
					bestRatePlanVO.setInUse(ObjectUtils.toString(bean.get("inuse")));
					bestRatePlanVO.setInUseDesc(ratePlanMap.get(ObjectUtils.toString(bean.get("inuse"))));
					bestRatePlanVO.setInUseTotal(ToolUtil.getBigDecimal(bean.get("inusetotal")));
					bestRatePlanVO.setBestRatePlanList(bestList);
					bestRatePlanVO.setBestRatePlanDesc(ratePlanMap.get(ObjectUtils.toString(bean.get("rateplancode"))));
					bestRatePlanVO.setBestTotal(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setBestDiff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				}
				if (ToolUtil.getBigDecimal(bean.get("totalcharge")).compareTo(best) < 0) {
					best = ToolUtil.getBigDecimal(bean.get("totalcharge"));
					bestList = new ArrayList<String>();
					bestList.add(ObjectUtils.toString(bean.get("rateplancode")));
					bestRatePlanVO.setBestRatePlanList(bestList);
					bestRatePlanVO.setBestRatePlanDesc(ratePlanMap.get(ObjectUtils.toString(bean.get("rateplancode"))));
					bestRatePlanVO.setBestTotal(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setBestDiff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				}else if (ToolUtil.getBigDecimal(bean.get("totalcharge")).compareTo(best) == 0) {					
					bestList.add(ObjectUtils.toString(bean.get("rateplancode")));
				}

				if ("1".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLampBTotal(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLampBDiff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("2".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLampTotal(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLampDiff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("3".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLampE2Total(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLampE2Diff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("4".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLampE3Total(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLampE3Diff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("5".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLampS2Total(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLampS2Diff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("6".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLowNTotal(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLowNDiff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("7".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setLow2Total(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setLow2Diff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("8".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setHigh2Total(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setHigh2Diff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				} else if ("9".equals(ObjectUtils.toString(bean.get("rateplancode")))) {
					bestRatePlanVO.setHigh3Total(ToolUtil.getBigDecimal(bean.get("totalcharge")));
					bestRatePlanVO.setHigh3Diff(ToolUtil.getBigDecimal(bean.get("diffcharge")));
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return bestRatePlanList;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<BestRatePlanVO> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for (BestRatePlanVO bestRatePlanVO : rows) {
				JSONObject bestRatePlan = new JSONObject();
				bestRatePlan.put("BankCode", bestRatePlanVO.getBankCode());
				bestRatePlan.put("BankName", bestRatePlanVO.getBankName());
				bestRatePlan.put("PowerAccount", bestRatePlanVO.getPowerAccount());
				bestRatePlan.put("inUse", bestRatePlanVO.getInUse());
				bestRatePlan.put("inUseDesc", bestRatePlanVO.getInUseDesc());
				bestRatePlan.put("inUseTotle", bestRatePlanVO.getInUseTotal());
				JSONArray bestArr = new JSONArray();
				for(String best : bestRatePlanVO.getBestRatePlanList()) {				
					bestArr.put(best);
				}	
				bestRatePlan.put("BestRatePlan", bestArr);
				bestRatePlan.put("BestRatePlanDesc", bestRatePlanVO.getBestRatePlanDesc());
				bestRatePlan.put("BestTotal", bestRatePlanVO.getBestTotal());
				bestRatePlan.put("BestDiff", bestRatePlanVO.getBestDiff());
				bestRatePlan.put("TotalLampB", bestRatePlanVO.getLampBTotal());
				bestRatePlan.put("TotalLamp", bestRatePlanVO.getLampTotal());
				bestRatePlan.put("TotalLampE2", bestRatePlanVO.getLampE2Total());
				bestRatePlan.put("TotalLampE3", bestRatePlanVO.getLampE3Total());
				bestRatePlan.put("TotalLampS2", bestRatePlanVO.getLampS2Total());
				bestRatePlan.put("TotallowN", bestRatePlanVO.getLowNTotal());
				bestRatePlan.put("TotalLow2", bestRatePlanVO.getLow2Total());
				bestRatePlan.put("TotalHigh2", bestRatePlanVO.getHigh2Total());
				bestRatePlan.put("TotalHigh3", bestRatePlanVO.getHigh3Total());
				bestRatePlan.put("DiffLampB", bestRatePlanVO.getLampBDiff());
				bestRatePlan.put("DiffLamp", bestRatePlanVO.getLampDiff());
				bestRatePlan.put("DiffLampE2", bestRatePlanVO.getLampE2Diff());
				bestRatePlan.put("DiffLampE3", bestRatePlanVO.getLampE3Diff());
				bestRatePlan.put("DiffLampS2", bestRatePlanVO.getLampS2Diff());
				bestRatePlan.put("DiffLowN", bestRatePlanVO.getLowNDiff());
				bestRatePlan.put("DiffLow2", bestRatePlanVO.getLow2Diff());
				bestRatePlan.put("DiffHigh2", bestRatePlanVO.getHigh2Diff());
				bestRatePlan.put("DiffHigh3", bestRatePlanVO.getHigh3Diff());

				list.put(bestRatePlan);
			}
			data.put("BestRatePlan", list);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}

	/**
	 * 產生PDF
	 * @param rows
	 * @param response
	 * @throws Exception
	 */
	private void composePDF(List<BestRatePlanVO> rows, String filter, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = null;
		DecimalFormat df = new DecimalFormat(",###");  

		try {
			baos = new ByteArrayOutputStream();
			// Step 1—Create a Document.
//			Document document = new Document(PageSize.A4.rotate());
			Document document = new Document(PageSize.A3.rotate());
			
			// Step 2—Get a PdfWriter instance.
			PdfWriter.getInstance(document, baos);
			// Step 3—Open the Document.
			document.open();
			// Step 4—Add content.
			
			// 文件屬性
			document.addTitle("BestRatePlan");
			document.addKeywords("zzzz");
			
			Font title = PdfUtil.getFont(12, Font.BOLD, null);
			Font font = PdfUtil.getFont(12, Font.NORMAL, null);
			Font fontR = PdfUtil.getFont(12, Font.NORMAL, BaseColor.RED);
			Font fontG = PdfUtil.getFont(12, Font.NORMAL, BaseColor.GREEN);
			
			PdfPTable table = new PdfPTable(7);
			table.setSpacingBefore(10);
			table.addCell(PdfUtil.createCell(title, "C", "代號/分行名稱"));
			table.addCell(PdfUtil.createCell(title, "C", "電號"));
			table.addCell(PdfUtil.createCell(title, "C", "目前用電種類"));
			table.addCell(PdfUtil.createCell(title, "C", "使用中總金額"));
			table.addCell(PdfUtil.createCell(title, "C", "最適用電種類"));
			table.addCell(PdfUtil.createCell(title, "C", "最適總金額"));
			table.addCell(PdfUtil.createCell(title, "C", "價差"));  

			for(BestRatePlanVO bestRatePlanVO : rows) {
				table.addCell(PdfUtil.createCell(font, "L", bestRatePlanVO.getBankCode()+bestRatePlanVO.getBankName()));
				table.addCell(PdfUtil.createCell(font, "L", bestRatePlanVO.getPowerAccount()));
				table.addCell(PdfUtil.createCell(font, "L", bestRatePlanVO.getInUseDesc()));
				table.addCell(PdfUtil.createCell(font, "R", bestRatePlanVO.getInUseTotal()!=null?df.format(bestRatePlanVO.getInUseTotal()):"0"));
				table.addCell(PdfUtil.createCell(font, "L", bestRatePlanVO.getBestRatePlanDesc()));
				table.addCell(PdfUtil.createCell(font, "R", bestRatePlanVO.getBestTotal()!=null?df.format(bestRatePlanVO.getBestTotal()):"0"));
				table.addCell(PdfUtil.createCell(font, "R", bestRatePlanVO.getBestDiff()!=null?df.format(bestRatePlanVO.getBestDiff()):"0"));
			}
			document.add(table);
			
			int column = 0;
			if (filter.contains("Lamp")) {
				column +=5;
			}
			if (filter.contains("Low")) {
				column +=2;
			}
			if (filter.contains("High")) {
				column +=2;
			}
			
			PdfPTable table2 = new PdfPTable(2+2*column);
			table2.setSpacingBefore(10);
			PdfPCell bank = PdfUtil.createCell(title, "C", "代號/分行名稱");
			bank.setRowspan(2);
			table2.addCell(bank);
			
			PdfPCell poweraccount = PdfUtil.createCell(title, "C", "電號");
			poweraccount.setRowspan(2);
			table2.addCell(poweraccount);
			
			PdfPCell total = PdfUtil.createCell(title, "C", "電費");
			total.setColspan(column);
			table2.addCell(total);
			
			PdfPCell diff = PdfUtil.createCell(title, "C", "價差");
			diff.setColspan(column);
			table2.addCell(diff);
			
			if (filter.contains("Lamp")) {
				table2.addCell(PdfUtil.createCell(title, "C", "表燈營業用"));
				table2.addCell(PdfUtil.createCell(title, "C", "表燈非營業用"));  
				table2.addCell(PdfUtil.createCell(title, "C", "表燈簡易二段式"));  
				table2.addCell(PdfUtil.createCell(title, "C", "表燈簡易三段式"));  
				table2.addCell(PdfUtil.createCell(title, "C", "表燈標準二段式"));  
			}
			if (filter.contains("Low")) {
				table2.addCell(PdfUtil.createCell(title, "C", "低壓非時間"));  
				table2.addCell(PdfUtil.createCell(title, "C", "低壓二段式"));
			}
			if (filter.contains("High")) {
				table2.addCell(PdfUtil.createCell(title, "C", "高壓二段式"));  
				table2.addCell(PdfUtil.createCell(title, "C", "高壓三段式"));
			}
			
			if (filter.contains("Lamp")) {
				table2.addCell(PdfUtil.createCell(title, "C", "表燈營業用"));
				table2.addCell(PdfUtil.createCell(title, "C", "表燈非營業用"));  
				table2.addCell(PdfUtil.createCell(title, "C", "表燈簡易二段式"));  
				table2.addCell(PdfUtil.createCell(title, "C", "表燈簡易三段式"));  
				table2.addCell(PdfUtil.createCell(title, "C", "表燈標準二段式"));  
			}
			if (filter.contains("Low")) {
				table2.addCell(PdfUtil.createCell(title, "C", "低壓非時間"));  
				table2.addCell(PdfUtil.createCell(title, "C", "低壓二段式"));  
			}
			if (filter.contains("High")) {
				table2.addCell(PdfUtil.createCell(title, "C", "高壓二段式"));  
				table2.addCell(PdfUtil.createCell(title, "C", "高壓三段式"));
			}	

			for(BestRatePlanVO bestRatePlanVO : rows) {
				table2.addCell(PdfUtil.createCell(font, "L", bestRatePlanVO.getBankCode()+bestRatePlanVO.getBankName()));
				table2.addCell(PdfUtil.createCell(font, "L", bestRatePlanVO.getPowerAccount()));

				if (filter.contains("Lamp")) {
					table2.addCell(createBestRateCell("1", bestRatePlanVO, bestRatePlanVO.getLampBTotal()!=null?df.format(bestRatePlanVO.getLampBTotal()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("2", bestRatePlanVO, bestRatePlanVO.getLampTotal()!=null?df.format(bestRatePlanVO.getLampTotal()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("3", bestRatePlanVO, bestRatePlanVO.getLampE2Total()!=null?df.format(bestRatePlanVO.getLampE2Total()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("4", bestRatePlanVO, bestRatePlanVO.getLampE3Total()!=null?df.format(bestRatePlanVO.getLampE3Total()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("5", bestRatePlanVO, bestRatePlanVO.getLampS2Total()!=null?df.format(bestRatePlanVO.getLampS2Total()):"0", font, fontR, fontG));
				}
				if (filter.contains("Low")) {
					table2.addCell(createBestRateCell("6", bestRatePlanVO, bestRatePlanVO.getLowNTotal()!=null?df.format(bestRatePlanVO.getLowNTotal()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("7", bestRatePlanVO, bestRatePlanVO.getLow2Total()!=null?df.format(bestRatePlanVO.getLow2Total()):"0", font, fontR, fontG));
				}
				if (filter.contains("High")) {
					table2.addCell(createBestRateCell("8", bestRatePlanVO, bestRatePlanVO.getHigh2Total()!=null?df.format(bestRatePlanVO.getHigh2Total()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("9", bestRatePlanVO, bestRatePlanVO.getHigh3Total()!=null?df.format(bestRatePlanVO.getHigh3Total()):"0", font, fontR, fontG));
				}			

				if (filter.contains("Lamp")) {
					table2.addCell(createBestRateCell("1", bestRatePlanVO, bestRatePlanVO.getLampBDiff()!=null?df.format(bestRatePlanVO.getLampBDiff()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("2", bestRatePlanVO, bestRatePlanVO.getLampDiff()!=null?df.format(bestRatePlanVO.getLampDiff()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("3", bestRatePlanVO, bestRatePlanVO.getLampE2Diff()!=null?df.format(bestRatePlanVO.getLampE2Diff()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("4", bestRatePlanVO, bestRatePlanVO.getLampE3Diff()!=null?df.format(bestRatePlanVO.getLampE3Diff()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("5", bestRatePlanVO, bestRatePlanVO.getLampS2Diff()!=null?df.format(bestRatePlanVO.getLampS2Diff()):"0", font, fontR, fontG));
				}
				if (filter.contains("Low")) {
					table2.addCell(createBestRateCell("6", bestRatePlanVO, bestRatePlanVO.getLowNDiff()!=null?df.format(bestRatePlanVO.getLowNDiff()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("7", bestRatePlanVO, bestRatePlanVO.getLow2Diff()!=null?df.format(bestRatePlanVO.getLow2Diff()):"0", font, fontR, fontG));
				}
				if (filter.contains("High")) {
					table2.addCell(createBestRateCell("8", bestRatePlanVO, bestRatePlanVO.getHigh2Diff()!=null?df.format(bestRatePlanVO.getHigh2Diff()):"0", font, fontR, fontG));
					table2.addCell(createBestRateCell("9", bestRatePlanVO, bestRatePlanVO.getHigh3Diff()!=null?df.format(bestRatePlanVO.getHigh3Diff()):"0", font, fontR, fontG));
				}				
			}
			
			document.add(table2);

			// Step 5—Close the Document.
			document.close();

			// 輸出PDF
			PdfUtil.exportPDF(baos, "最適電費"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".pdf", response);
		} catch (Exception e) {
			throw new Exception(e.toString());
		} finally {
			if (baos != null) {
				try {
					baos.flush();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 判斷最適電費呈顯顏色
	 * @param rateplan
	 * @param bestRatePlanVO
	 * @param price
	 * @param font
	 * @param fontR
	 * @param fontG
	 * @return PdfPCell
	 * @throws Exception
	 */
	private PdfPCell createBestRateCell(String rateplan, BestRatePlanVO bestRatePlanVO, String price, Font font, Font fontR, Font fontG) throws Exception {
		try {
			if(bestRatePlanVO.getBestRatePlanList().contains(rateplan)) {
				return PdfUtil.createCell(fontG, "R", price);
			}else if(rateplan.equals(bestRatePlanVO.getInUse())){
				return PdfUtil.createCell(fontR, "R", price);
			}else {
				return PdfUtil.createCell(font, "R", price);
			}
		}catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	private JSONObject composeExcel(List<BestRatePlanVO> rows, String filter) throws Exception {
		JSONObject data = new JSONObject();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			
			XSSFSheet sheet = workbook.createSheet("BestRatePlan");
			XSSFCellStyle titleStyle = ExcelUtil.getTitleStyle(workbook, true, 14);//標題樣式
			XSSFCellStyle styleL = ExcelUtil.getTextStyle(workbook, "L", true, 12);
			XSSFCellStyle styleR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12);
			XSSFCellStyle styleRR = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12, IndexedColors.RED.getIndex());
			XSSFCellStyle styleRG = ExcelUtil.getNumberStyle(workbook, "R", "#,##0", true, 12, IndexedColors.GREEN.getIndex());
			
			XSSFRow row0 = sheet.createRow(0);	
			ExcelUtil.createCell(row0, titleStyle, 0, XSSFCell.CELL_TYPE_STRING, "代號/分行名稱");
			ExcelUtil.createCell(row0, titleStyle, 1, XSSFCell.CELL_TYPE_STRING, "電號");
			ExcelUtil.createCell(row0, titleStyle, 2, XSSFCell.CELL_TYPE_STRING, "目前用電種類");
			ExcelUtil.createCell(row0, titleStyle, 3, XSSFCell.CELL_TYPE_STRING, "使用中總金額");
			ExcelUtil.createCell(row0, titleStyle, 4, XSSFCell.CELL_TYPE_STRING, "最適用電種類");
			ExcelUtil.createCell(row0, titleStyle, 5, XSSFCell.CELL_TYPE_STRING, "最適總金額");
			ExcelUtil.createCell(row0, titleStyle, 6, XSSFCell.CELL_TYPE_STRING, "價差"); 
			
			int y = 1;
			for(BestRatePlanVO bestRatePlanVO : rows) {
				XSSFRow row = sheet.createRow(y++);
				ExcelUtil.createCell(row, styleL, 0, XSSFCell.CELL_TYPE_STRING, bestRatePlanVO.getBankCode()+bestRatePlanVO.getBankName());
				ExcelUtil.createCell(row, styleL, 1, XSSFCell.CELL_TYPE_STRING, bestRatePlanVO.getPowerAccount());
				ExcelUtil.createCell(row, styleR, 2, XSSFCell.CELL_TYPE_STRING, bestRatePlanVO.getInUseDesc());
				ExcelUtil.createCell(row, styleR, 3, XSSFCell.CELL_TYPE_NUMERIC, bestRatePlanVO.getInUseTotal());
				ExcelUtil.createCell(row, styleR, 4, XSSFCell.CELL_TYPE_STRING, bestRatePlanVO.getBestRatePlanDesc());
				ExcelUtil.createCell(row, styleR, 5, XSSFCell.CELL_TYPE_NUMERIC, bestRatePlanVO.getBestTotal());
				ExcelUtil.createCell(row, styleR, 6, XSSFCell.CELL_TYPE_NUMERIC, bestRatePlanVO.getBestDiff());
			}
			
			y=y+2;
			
			int column = 0;
			if (filter.contains("Lamp")) {
				column +=5;
			}
			if (filter.contains("Low")) {
				column +=2;
			}
			if (filter.contains("High")) {
				column +=2;
			}
			int x = 2;
			sheet.addMergedRegion(new CellRangeAddress(y, y+1, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(y, y+1, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(y, y, x, x+column-1));
			sheet.addMergedRegion(new CellRangeAddress(y, y, x+column, x+column+column-1));
			
			x=0;
			XSSFRow row1 = sheet.createRow(y++);
			ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "代號/分行名稱");
			ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "電號");
			
			if (filter.contains("Lamp")) {
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "電費");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Low")) {
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "電費");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("High")) {
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "電費");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Lamp")) {
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "價差");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("Low")) {
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "價差");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			if (filter.contains("High")) {
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "價差");
				ExcelUtil.createCell(row1, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			}
			x=0;
			
			XSSFRow row2 = sheet.createRow(y++);
			ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_BLANK, "");
			if (filter.contains("Lamp")) {
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈營業用");
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈非營業用");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈簡易二段式");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈簡易三段式");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈標準二段式"); 
			}
			if (filter.contains("Low")) {
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "低壓非時間");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "低壓二段式");
			}
			if (filter.contains("High")) {
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "高壓二段式");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "高壓三段式 ");
			}	
			
			if (filter.contains("Lamp")) {
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈營業用");
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈非營業用");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈簡易二段式");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈簡易三段式");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "表燈標準二段式"); 
			}
			if (filter.contains("Low")) {
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "低壓非時間");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "低壓二段式"); 
			}
			if (filter.contains("High")) {
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "高壓二段式");  
				ExcelUtil.createCell(row2, titleStyle, x++, XSSFCell.CELL_TYPE_STRING, "高壓三段式 ");
			}	
			x=0;
			
			for(BestRatePlanVO bestRatePlanVO : rows) {
				XSSFRow row = sheet.createRow(y++);
				ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bestRatePlanVO.getBankCode()+bestRatePlanVO.getBankName());
				ExcelUtil.createCell(row, styleL, x++, XSSFCell.CELL_TYPE_STRING, bestRatePlanVO.getPowerAccount());
			
				if (filter.contains("Lamp")) {
					createBestRateCell(row, x++, "1", bestRatePlanVO, bestRatePlanVO.getLampBTotal(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "2", bestRatePlanVO, bestRatePlanVO.getLampTotal(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "3", bestRatePlanVO, bestRatePlanVO.getLampE2Total(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "4", bestRatePlanVO, bestRatePlanVO.getLampE3Total(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "5", bestRatePlanVO, bestRatePlanVO.getLampS2Total(), styleR, styleRR, styleRG);
				}
				if (filter.contains("Low")) {
					createBestRateCell(row, x++, "6", bestRatePlanVO, bestRatePlanVO.getLowNTotal(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "7", bestRatePlanVO, bestRatePlanVO.getLow2Total(), styleR, styleRR, styleRG);
				}
				if (filter.contains("High")) {
					createBestRateCell(row, x++, "8", bestRatePlanVO, bestRatePlanVO.getHigh2Total(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "9", bestRatePlanVO, bestRatePlanVO.getHigh3Total(), styleR, styleRR, styleRG);
				}
				
				
				
				if (filter.contains("Lamp")) {
					createBestRateCell(row, x++, "1", bestRatePlanVO, bestRatePlanVO.getLampBDiff(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "2", bestRatePlanVO, bestRatePlanVO.getLampDiff(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "3", bestRatePlanVO, bestRatePlanVO.getLampE2Diff(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "4", bestRatePlanVO, bestRatePlanVO.getLampE3Diff(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "5", bestRatePlanVO, bestRatePlanVO.getLampS2Diff(), styleR, styleRR, styleRG);
				}
				if (filter.contains("Low")) {
					createBestRateCell(row, x++, "6", bestRatePlanVO, bestRatePlanVO.getLowNDiff(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "7", bestRatePlanVO, bestRatePlanVO.getLow2Diff(), styleR, styleRR, styleRG);
				}
				if (filter.contains("High")) {
					createBestRateCell(row, x++, "8", bestRatePlanVO, bestRatePlanVO.getHigh2Diff(), styleR, styleRR, styleRG);
					createBestRateCell(row, x++, "9", bestRatePlanVO, bestRatePlanVO.getHigh3Diff(), styleR, styleRR, styleRG);
				}				
				x=0;
			}
					
			//設定自動欄寬
			for (int i = 0; i <= 2+2*column; i++) {
                sheet.autoSizeColumn(i, true);
                sheet.setColumnWidth(i,sheet.getColumnWidth(i)*18/10);
            }
			ExcelUtil.setSizeColumn(sheet, column);
			
			String fileName = "最適電費"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";
			data.put("FileName", fileName);
			data.put("Base64", ExcelUtil.exportBase64(workbook, fileName));
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
	}
	
	/**
	 * 判斷最適電費呈顯顏色
	 * @param row
	 * @param column
	 * @param rateplan
	 * @param bestRatePlanVO
	 * @param price
	 * @param style
	 * @param styleR
	 * @param styleG
	 * @throws Exception
	 */
	private void createBestRateCell(XSSFRow row, int column, String rateplan, BestRatePlanVO bestRatePlanVO, BigDecimal price, XSSFCellStyle style, XSSFCellStyle styleR, XSSFCellStyle styleG) throws Exception {
		try {
			if(bestRatePlanVO.getBestRatePlanList().contains(rateplan)) {
				ExcelUtil.createCell(row, styleG, column, XSSFCell.CELL_TYPE_NUMERIC, price);
			}else if(rateplan.equals(bestRatePlanVO.getInUse())){
				ExcelUtil.createCell(row, styleR, column, XSSFCell.CELL_TYPE_NUMERIC, price);
			}else {
				ExcelUtil.createCell(row, style, column, XSSFCell.CELL_TYPE_NUMERIC, price);
			}
		}catch (Exception e) {
			throw new Exception(e.toString());
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
