package aptg.cathaybkeco.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.json.JSONObject;

import aptg.cathaybkeco.dao.AdminSetupDAO;
import aptg.cathaybkeco.dao.AreaDAO;
import aptg.cathaybkeco.dao.BestCCDAO;
import aptg.cathaybkeco.util.ToolUtil;
import aptg.cathaybkeco.vo.BestCCVO;

/**
 * Servlet implementation class getBestCC 契約容量最佳化
 */
@WebServlet("/getBestCC")
public class getBestCC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getBestCC.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getBestCC() {
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
		logger.debug("getBestCC start");
		JSONObject rspJson = new JSONObject();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			String city = ObjectUtils.toString(request.getParameter("city"));
			String postCodeNo = ObjectUtils.toString(request.getParameter("postCodeNo"));
			String bankCode = ObjectUtils.toString(request.getParameter("bankCode"));
			String mode = ObjectUtils.toString(request.getParameter("mode"));//0:簡易 1:詳細
			String start = ObjectUtils.toString(request.getParameter("start"));
			String end = ObjectUtils.toString(request.getParameter("end"));
			String account = ObjectUtils.toString(request.getParameter("account"));
			logger.debug("token: " + token);
			logger.debug("City:" + city + ",PostCodeNo:" + postCodeNo);
			logger.debug("BankCode:" + bankCode + ",Mode:"+mode + ",Account:"+account);
			logger.debug("date:" + start + " ~ " + end);
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspJson.put("code", "02");
					rspJson.put("msg", "身分驗證失敗");			
				}else if (!ToolUtil.dateCheck(start, "yyyyMM") || !ToolUtil.dateCheck(end, "yyyyMM")) {
					rspJson.put("code", "18");
					rspJson.put("msg", "日期格式錯誤(yyyyMM)");
				} else {
					BestCCVO bestCCVO = new BestCCVO();		
					bestCCVO.setCity(city);
					bestCCVO.setPostCodeNo(postCodeNo);
					if(StringUtils.isNotBlank(bankCode)) {
						bestCCVO.setBankCode(bankCode);
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
							bestCCVO.setBankCodeArr(valueStr);
						}
					}
					bestCCVO.setStartDate(start);
					bestCCVO.setEndDate(end);				
					BestCCDAO bestCCDAO = new BestCCDAO();
					List<DynaBean> bestList = bestCCDAO.getBestCC(bestCCVO);
					List<DynaBean> nowList = bestCCDAO.getPACharge(bestCCVO);
					if (bestList != null && nowList!=null && !bestList.isEmpty() && !nowList.isEmpty()) {
						List<BestCCVO> VOList = beanToVO(bestList, nowList, mode);
						rspJson.put("code", "00");
						rspJson.put("count", VOList != null ? VOList.size() : 0);

						rspJson.put("msg", convertToJson(VOList));
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
		logger.debug("getBestCC end");
	}

	private List<BestCCVO> beanToVO(List<DynaBean> bestList, List<DynaBean> nowList, String mode) throws Exception {
		List<BestCCVO> bestCCList = new ArrayList<BestCCVO>();
		BigDecimal cc,baseCharge,overCharge,bsetCC,bestBase,bestOver,bestTotal,diffBase,diffOver,diffTotal;
		BigDecimal best = new BigDecimal(0);
		boolean add = true;
		try {
			Map<String, BestCCVO> paMap = new HashMap<String, BestCCVO>();
			for (DynaBean bean : nowList) {
				BestCCVO bestCCVO = new BestCCVO();
				bestCCVO.setPowerAccount(ObjectUtils.toString(bean.get("poweraccount")));//電號
				bestCCVO.setRatePlanCode(ObjectUtils.toString(bean.get("rateplancode")));//用電種類
				bestCCVO.setRatePlanDesc(ObjectUtils.toString(bean.get("rateplandesc")));//用電種類說明
				bestCCVO.setCc(ToolUtil.getBigDecimal(bean.get("cc")));//目前契約
				bestCCVO.setBaseCharge(ToolUtil.getBigDecimal(bean.get("basecharge")));//目前基本
				bestCCVO.setOverCharge(ToolUtil.getBigDecimal(bean.get("overcharge")));//目前非約定		
				bestCCVO.setTotalCharge(ToolUtil.add(bean.get("basecharge"), bean.get("overcharge")));//目前總計
				
				paMap.put(bestCCVO.getPowerAccount(), bestCCVO);
			}			
			
			String key = new String();
			BestCCVO bestCCVO = null;
			for (DynaBean bean : bestList) {
				String powerAccount = ObjectUtils.toString(bean.get("poweraccount"));
				BestCCVO nowVO = paMap.get(powerAccount);
				
				cc = nowVO.getCc();
				baseCharge = nowVO.getBaseCharge();
				overCharge = nowVO.getOverCharge();
				bsetCC = ToolUtil.getBigDecimal(bean.get("bsetcc"));
				bestBase = ToolUtil.getBigDecimal(bean.get("bestbasecharge"));
				bestOver = ToolUtil.getBigDecimal(bean.get("bestovercharge"));
				bestTotal = bestBase.add(bestOver);
				diffBase = bestBase.subtract(baseCharge);
				diffOver = bestOver.subtract(overCharge);
				diffTotal = diffBase.add(diffOver);
				
				if (!key.equals(powerAccount+("1".equals(mode)?"###"+ObjectUtils.toString(bean.get("bestrateplancode")):""))) {
					key = powerAccount+("1".equals(mode)?"###"+ObjectUtils.toString(bean.get("bestrateplancode")):"");					
					add = true;
					bestCCVO = new BestCCVO();
					bestCCList.add(bestCCVO);

					bestCCVO.setBankCode(ObjectUtils.toString(bean.get("bankcode")));
					bestCCVO.setBankName(ObjectUtils.toString(bean.get("bankname")));
					bestCCVO.setPowerAccount(ObjectUtils.toString(bean.get("poweraccount")));//電號
					bestCCVO.setRatePlanCode(nowVO.getRatePlanCode());//用電種類
					bestCCVO.setRatePlanDesc(nowVO.getRatePlanDesc());//用電種類說明
					bestCCVO.setCc(cc);//目前契約
					bestCCVO.setBaseCharge(baseCharge);//目前基本
					bestCCVO.setOverCharge(overCharge);//目前非約定		
					bestCCVO.setTotalCharge(baseCharge.add(overCharge));//目前總計
				}
				if (add || diffTotal.compareTo(best) < 0) {
					best = diffTotal;

					bestCCVO.setBestRatePlan(ObjectUtils.toString(bean.get("bestrateplancode")));//最佳用電種類
					bestCCVO.setBestRatePlanDesc(ObjectUtils.toString(bean.get("bestrateplandesc")));//最佳用電種類說明
					bestCCVO.setBsetCC(bsetCC);//最佳化契約
					bestCCVO.setBestBase(bestBase);//最佳化基本
					bestCCVO.setBestOver(bestOver);//最佳化非約定
					bestCCVO.setBestTotal(bestTotal);//最佳化總計
					bestCCVO.setDiffCC(bsetCC.subtract(cc));//契約差額
					bestCCVO.setDiffBase(diffBase);//基本差額
					bestCCVO.setDiffOver(diffOver);//非約定差額
					bestCCVO.setDiffTotal(diffTotal);//差額總計
					add = false;
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return bestCCList;
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject convertToJson(List<BestCCVO> rows) throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			for (int i=0; i<rows.size(); i++) {
				BestCCVO bestCCVO = rows.get(i);
				JSONObject bestCC = new JSONObject();
				bestCC.put("Seq", i+1);
				bestCC.put("BankCode", bestCCVO.getBankCode());//分行代碼
				bestCC.put("BankName", bestCCVO.getBankName());//分行名稱
				bestCC.put("PowerAccount", bestCCVO.getPowerAccount());//電號
				bestCC.put("RatePlanCode", bestCCVO.getRatePlanCode());//用電種類
				bestCC.put("RatePlanDesc", bestCCVO.getRatePlanDesc());//用電種類說明
				bestCC.put("CC", bestCCVO.getCc());//目前契約
				bestCC.put("BaseCharge", bestCCVO.getBaseCharge());//目前基本
				bestCC.put("OverCharge", bestCCVO.getOverCharge());//目前非約定
				bestCC.put("TotalCharge", bestCCVO.getTotalCharge());//目前總計
				bestCC.put("BestRatePlanCode", bestCCVO.getBestRatePlan());//最佳用電種類
				bestCC.put("BestRatePlanDesc", bestCCVO.getBestRatePlanDesc());//最佳用電種類說明
				bestCC.put("BsetCC", bestCCVO.getBsetCC());//最佳化契約
				bestCC.put("BestBase", bestCCVO.getBestBase());//最佳化基本
				bestCC.put("BestOver", bestCCVO.getBestOver());//最佳化非約定
				bestCC.put("BestTotal", bestCCVO.getBestTotal());//最佳化總計
				bestCC.put("DiffCC", bestCCVO.getDiffCC());//契約差額
				bestCC.put("DiffBase", bestCCVO.getDiffBase());//基本差額
				bestCC.put("DiffOver", bestCCVO.getDiffOver());//非約定差額
				bestCC.put("DiffTotal", bestCCVO.getDiffTotal());//差額總計
				
				list.put(bestCC);
			}
			data.put("BestCC", list);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return data;
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
