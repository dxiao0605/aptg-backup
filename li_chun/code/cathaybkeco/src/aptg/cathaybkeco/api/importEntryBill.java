package aptg.cathaybkeco.api;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.EntryBillDAO;
import aptg.cathaybkeco.util.ExcelUtil;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.EntryBillVO;

/**
 * Servlet implementation class importEntryBill 匯入電費單
 */
@WebServlet("/importEntryBill")
public class importEntryBill extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(importEntryBill.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public importEntryBill() {
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
		logger.debug("importEntryBill start");
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
					EntryBillVO entryBillVO = this.parseExcel(request);
					if (entryBillVO.isError()) {
						rspJson.put("code", entryBillVO.getCode());
						rspJson.put("msg", entryBillVO.getDescription());
					} else {							
						EntryBillDAO entryBillDAO = new EntryBillDAO();														
						entryBillDAO.addEntryBillBatch(entryBillVO);
						ToolUtil.addLogRecord(entryBillVO.getUserName(), "15", "匯入電費單");
						
						rspJson.put("code", "00");
						rspJson.put("msg", "Import Success");						
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
		logger.debug("importEntryBill end");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspJson.toString());
	}

	/**
	 * 解析Excel
	 * 
	 * @param json
	 * @return entryBillVO
	 * @throws Exception
	 */
	private EntryBillVO parseExcel(HttpServletRequest request) throws Exception {
		FileOutputStream fileOutputStream = null;
		InputStream is = null;
		File file = null;
		EntryBillVO entryBillVO = new EntryBillVO();
		try {
			//讀取請求Body
	        byte[] body = readBody(request);
	        //取得所有Body內容的字串表示
	        String textBody = new String(body, "ISO-8859-1");
	        //取得上傳的檔名稱
	        String fileName = getFileName(textBody);
			String fileType = (fileName.substring(fileName.lastIndexOf(".")+1, fileName.length())).toLowerCase();
	        if("xlsx".equals(fileType) || "xls".equals(fileType)) {  
	        	String userName = getUserName(textBody);	        	
	        	
		        //取得檔案開始與結束位置
		        Position p = getFilePosition(request, textBody);
		        //輸出至檔案
		        file = new File(fileName);
		        					
		        fileOutputStream = new FileOutputStream(file);
		        fileOutputStream.write(body, p.begin, (p.end - p.begin));
		        is = new FileInputStream(file);
		        
		        Workbook wb = null ; 		        
		        if("xlsx".equals(fileType)) {
		        	wb = new XSSFWorkbook(is);
		        }else {
		        	wb = new HSSFWorkbook(is);
		        }
		        Sheet sheet = wb.getSheetAt(0);
				List<EntryBillVO> dataList = new ArrayList<EntryBillVO>();
				Map<String, EntryBillVO> dataMap = new LinkedHashMap<String, EntryBillVO>();
				EntryBillVO vo;
				int count = sheet.getLastRowNum();
				logger.debug("匯入Excel:"+fileName+",筆數:"+count);
				if(count>0) {
					Map<String, BigDecimal> billMonthsMap = new HashMap<String, BigDecimal>();
					for(int i=1; i<=count; i++) {
						Row row = sheet.getRow(i);
						BigDecimal cec = BigDecimal.ZERO;
						String powerAccount = ExcelUtil.getCellValue(row.getCell(0));
						String billMon = ExcelUtil.getCellValue(row.getCell(1));
						
						if(ExcelUtil.checkBlankRow(row, 19)) {
							break;
						}
						
						if(dataMap.containsKey(powerAccount+billMon)) {
							vo = dataMap.get(powerAccount+billMon);
						}else {
							vo = new EntryBillVO();
							dataList.add(vo);
						}
						
						if(!ToolUtil.lengthCheck(powerAccount, 11)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("12");
							entryBillVO.setDescription("第"+i+"筆電號超過長度限制");
							break;
						}else {
							vo.setPowerAccount(powerAccount);	
						}
						
						if(!billMonthsMap.containsKey(powerAccount)) {
							EntryBillDAO entryBillDAO = new EntryBillDAO();
							List<DynaBean> list = entryBillDAO.getBillMonths(powerAccount);
							if(list!=null && !list.isEmpty()) {
								DynaBean bean = list.get(0);
								billMonthsMap.put(powerAccount, ToolUtil.getBigDecimal(bean.get("billmonths")));
							}
						}
						
						vo.setBillMon(billMon);
						vo.setBillStartDay(ExcelUtil.getCellValue(row.getCell(2)));
						vo.setBillEndDay(ExcelUtil.getCellValue(row.getCell(3)));
												
						if(checkBillMon(vo)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("12");
							entryBillVO.setDescription("第"+i+"筆電費單已存在");
							break;							
						}else if(checkBillPeriod(vo)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("19");
							entryBillVO.setDescription("第"+i+"筆計費日期起迄期間重疊");
							break;					
						}
						
						BigDecimal totalCharge = BigDecimal.ZERO;
						String baseCharge = ExcelUtil.getCellValue(row.getCell(4));
						if (!ToolUtil.IntegerCheck(baseCharge) || !ToolUtil.numberLengthCheck(baseCharge, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆基本電費數字格式錯誤");
							break;
						}else {
							vo.setBaseCharge(baseCharge);
							totalCharge = ToolUtil.add(totalCharge, baseCharge);
						}
						String usageCharge = ExcelUtil.getCellValue(row.getCell(5));
						if (!ToolUtil.IntegerCheck(usageCharge) || !ToolUtil.numberLengthCheck(usageCharge, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆流動電費數字格式錯誤");
							break;
						}else {
							vo.setUsageCharge(usageCharge);
							totalCharge = ToolUtil.add(totalCharge, usageCharge);
						}
						String overCharge = ExcelUtil.getCellValue(row.getCell(6));
						if (!ToolUtil.IntegerCheck(overCharge) || !ToolUtil.numberLengthCheck(overCharge, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆非約定電費數字格式錯誤");
							break;
						}else {
							vo.setOverCharge(overCharge);
							totalCharge = ToolUtil.add(totalCharge, overCharge);
						}
						
						String shareCharge = ExcelUtil.getCellValue(row.getCell(7));
						if (!ToolUtil.IntegerCheck(shareCharge) || !ToolUtil.numberLengthCheck(shareCharge, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆分攤電費數字格式錯誤");
							break;
						}else {
							vo.setShareCharge(shareCharge);
							totalCharge = ToolUtil.add(totalCharge, shareCharge);
						}
						
						String pfCharge = ExcelUtil.getCellValue(row.getCell(8));
						if (!ToolUtil.IntegerCheck(pfCharge) || !ToolUtil.numberLengthCheck(pfCharge, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆功因補償款數字格式錯誤");
							break;
						}else {
							vo.setPfCharge(pfCharge);
							totalCharge = ToolUtil.add(totalCharge, pfCharge);
						}
						
						
						vo.setTotalCharge(totalCharge.toString());
						vo.setShowCharge(ToolUtil.divide(totalCharge, billMonthsMap.get(powerAccount), 0).toString());
						
						
						String maxDemandPK = ExcelUtil.getCellValue(row.getCell(10));
						if (!ToolUtil.IntegerCheck(maxDemandPK) || !ToolUtil.numberLengthCheck(maxDemandPK, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆尖峰最大需量數字格式錯誤");
							break;
						}else {
							vo.setMaxDemandPK(maxDemandPK);
						}
						
						String maxDemandSP = ExcelUtil.getCellValue(row.getCell(11));
						if (!ToolUtil.IntegerCheck(maxDemandSP) || !ToolUtil.numberLengthCheck(maxDemandSP, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆半尖峰最大需量數字格式錯誤");
							break;
						}else {
							vo.setMaxDemandSP(maxDemandSP);
						}
						
						String maxDemandSatSP = ExcelUtil.getCellValue(row.getCell(12));
						if (!ToolUtil.IntegerCheck(maxDemandSatSP) || !ToolUtil.numberLengthCheck(maxDemandSatSP, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆周六半尖峰最大需量數字格式錯誤");
							break;
						}else {
							vo.setMaxDemandSatSP(maxDemandSatSP);
						}
						
						String maxDemandOP = ExcelUtil.getCellValue(row.getCell(13));
						if (!ToolUtil.IntegerCheck(maxDemandOP) || !ToolUtil.numberLengthCheck(maxDemandOP, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆離峰最大需量數字格式錯誤");
							break;
						}else {
							vo.setMaxDemandOP(maxDemandOP);
						}
						
						String cecpk = ExcelUtil.getCellValue(row.getCell(14));
						if (!ToolUtil.IntegerCheck(cecpk) || !ToolUtil.numberLengthCheck(cecpk, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆尖峰累積用電量數字格式錯誤");
							break;
						}else {
							cec = ToolUtil.add(cec, cecpk);
							vo.setCecPK(cecpk);
						}
						
						String cecsp = ExcelUtil.getCellValue(row.getCell(15));
						if (!ToolUtil.IntegerCheck(cecsp) || !ToolUtil.numberLengthCheck(cecsp, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆半尖峰累積用電量數字格式錯誤");
							break;
						}else {
							cec = ToolUtil.add(cec, cecsp);
							vo.setCecSP(cecsp);
						}
						
						String cecsatsp = ExcelUtil.getCellValue(row.getCell(16));
						if (!ToolUtil.IntegerCheck(cecsatsp) || !ToolUtil.numberLengthCheck(cecsatsp, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆周六半尖峰累積用電量數字格式錯誤");
							break;
						}else {
							cec = ToolUtil.add(cec, cecsatsp);
							vo.setCecSatSP(cecsatsp);
						}
						
						String cecop = ExcelUtil.getCellValue(row.getCell(17));
						if (!ToolUtil.IntegerCheck(cecop) || !ToolUtil.numberLengthCheck(cecop, 35, 2)) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆離峰累積用電量數字格式錯誤");
							break;
						}else {
							cec = ToolUtil.add(cec, cecop);
							vo.setCecOP(cecop);
						}
						
						String pf = ExcelUtil.getCellValue(row.getCell(18));
						if (!ToolUtil.IntegerCheck(pf) && Double.parseDouble(pf)<0 && Double.parseDouble(pf)>1) {
							entryBillVO.setError(true);
							entryBillVO.setCode("13");
							entryBillVO.setDescription("第"+i+"筆功率因數數字格式錯誤");
							break;
						}else {
							vo.setPf(ToolUtil.multiply(pf, 100).toString());
						}					
						vo.setShowCEC(ToolUtil.divide(cec, billMonthsMap.get(powerAccount), 0).toString());	
						vo.setUserName(userName);
					}
					entryBillVO.setDataList(dataList);
					entryBillVO.setUserName(userName);
				}
	        }else {
	        	entryBillVO.setError(true);
				entryBillVO.setCode("21");
				entryBillVO.setDescription("請上傳Excel檔");
	        }
	        
		} catch (Exception e) {
			logger.error("", e);
			entryBillVO.setError(true);
			entryBillVO.setCode("20");
			entryBillVO.setDescription("Excel解析失敗");
		}finally {
			if(fileOutputStream != null) {
				fileOutputStream.flush();
		        fileOutputStream.close();
			}
			if(is != null) {
				is.close();
			}
			if(file != null && file.exists()) {
				file.delete();
			}
		}
		return entryBillVO;
	}
	
	/**
	 * 檢核電費單是否存在
	 * @param entryBillVO
	 * @return boolean
	 * @throws Exception
	 */
	private boolean checkBillMon(EntryBillVO entryBillVO) throws Exception {
		EntryBillDAO entryBillDAO = new EntryBillDAO();
		List<DynaBean> list = entryBillDAO.getEntryBill(entryBillVO);
		if(list!=null && !list.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 計費日期起迄期間是否有重疊
	 * @param entryBillVO
	 * @return boolean
	 * @throws Exception
	 */
	private boolean checkBillPeriod(EntryBillVO entryBillVO) throws Exception {
		EntryBillDAO entryBillDAO = new EntryBillDAO();
		List<DynaBean> list = entryBillDAO.checkBillPeriod(entryBillVO);
		if(list!=null && !list.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	class Position {

        int begin;
        int end;

        public Position(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
	}
	private byte[] readBody(HttpServletRequest request) throws Exception {
		 DataInputStream dataStream = null;
		 byte[] body = null;
		 try {
			//獲取請求文字位元組長度
		        int formDataLength = request.getContentLength();
		        //取得ServletInputStream輸入流物件
		        dataStream = new DataInputStream(request.getInputStream());
		        body = new byte[formDataLength];
		        int totalBytes = 0;
		        while (totalBytes < formDataLength) {
		            int bytes = dataStream.read(body, totalBytes, formDataLength);
		            totalBytes += bytes;
		        }
		 }catch (Exception e) {
			 throw new Exception(e.getMessage());
		 }finally {
			 if(dataStream!=null)
				 dataStream.close();
		 }
		 return body;
	 }
	 
	 private Position getFilePosition(HttpServletRequest request, String textBody) throws IOException {
        //取得檔案區段邊界資訊
        String contentType = request.getContentType();
        String boundaryText = contentType.substring(contentType.lastIndexOf("=") + 1, contentType.length());
        //取得實際上傳檔案的氣勢與結束位置
        int pos = textBody.indexOf("filename=\"");
        pos = textBody.indexOf("\n", pos) + 1;
        pos = textBody.indexOf("\n", pos) + 1;
        pos = textBody.indexOf("\n", pos) + 1;
        int boundaryLoc = textBody.indexOf(boundaryText, pos) - 4;
        int begin = ((textBody.substring(0, pos)).getBytes("ISO-8859-1")).length;
        int end = ((textBody.substring(0, boundaryLoc)).getBytes("ISO-8859-1")).length;

        return new Position(begin, end);
    }

    private String getFileName(String requestBody) {
        String fileName = "";
        if(requestBody.indexOf("filename=\"")!=-1) {
	        fileName = requestBody.substring(requestBody.indexOf("filename=\"") + 10);
	        fileName = fileName.substring(0, fileName.indexOf("\n"));
	        fileName = fileName.substring(fileName.indexOf("\n") + 1, fileName.indexOf("\""));
        }
        return fileName;
    }
    
    private String getUserName(String requestBody) throws UnsupportedEncodingException {
    	String userName = "";
    	if(requestBody.indexOf("\"username\"")!=-1) {
    		userName = requestBody.substring(requestBody.indexOf("\"username\"") + 10);
    		userName = userName.substring(0, userName.indexOf("------"));
    		userName = StringUtils.trimToNull(userName);
    		userName = new String(userName.getBytes("ISO-8859-1"),"UTF-8");

    	}
        return userName;
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
