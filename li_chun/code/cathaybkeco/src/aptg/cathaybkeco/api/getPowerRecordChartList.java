package aptg.cathaybkeco.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.cathaybkeco.bean.BaseChartBean;
import aptg.cathaybkeco.bean.MsgChartBean;
import aptg.cathaybkeco.bean.RecordChartBean;
import aptg.cathaybkeco.util.JsonUtil;
import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getPowerRecordChartList 電力趨勢選項
 */
@WebServlet("/getPowerRecordChartList")
public class getPowerRecordChartList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerRecordChartList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerRecordChartList() {
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
		String rspStr = new String();
		try {
			// 取得Params
			String token = ObjectUtils.toString(request.getHeader("token"));
			logger.debug("token: " + token);			
			if (StringUtils.isNotBlank(token)) {
				// 身分驗證
				if (!ToolUtil.checkToken(token)) {
					rspStr = ToolUtil.getErrRspJson("身分驗證失敗", "02");
				} else {
					rspStr = JsonUtil.getInstance().convertObjectToJsonstring(convertToJson());	
				}
			} else {
				rspStr = ToolUtil.getErrRspJson("缺少參數", "01");
			}
		} catch (Exception e) {
			rspStr = ToolUtil.getErrRspJson(e.toString(), "99");
			logger.error("", e);
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(rspStr);
	}

	/**
	 * 組Json
	 * 
	 * @param rows
	 * @return JSONObject
	 * @throws Exception 
	 */
	private MsgChartBean convertToJson() throws Exception {
		MsgChartBean msgBean = new MsgChartBean();
		try {
			RecordChartBean recordBean = new RecordChartBean();

//			//功率: 實功/虛功/視在
			BaseChartBean power = new BaseChartBean();
			power.setId("Power");
			power.setDesc("功率");
			List<BaseChartBean> powerList = new ArrayList<BaseChartBean>();
			BaseChartBean w = new BaseChartBean();
			w.setId("W");
			w.setDesc("實功");
			powerList.add(w);
			BaseChartBean var = new BaseChartBean();
			var.setId("RP");
			var.setDesc("虛功");
			powerList.add(var);
			BaseChartBean va = new BaseChartBean();
			va.setId("VA");
			va.setDesc("視在");
			powerList.add(va);
			power.setChildren(powerList);
			recordBean.setPower(power);
			
//			//電流: R相/S相/T相/平均電流
			BaseChartBean cur = new BaseChartBean();
			cur.setId("Cur");
			cur.setDesc("電流");
			List<BaseChartBean> curList = new ArrayList<BaseChartBean>();
			BaseChartBean i1 = new BaseChartBean();
			i1.setId("I1");
			i1.setDesc("電流R相");
			curList.add(i1);
			BaseChartBean i2 = new BaseChartBean();
			i2.setId("I2");
			i2.setDesc("電流S相");
			curList.add(i2);
			BaseChartBean i3 = new BaseChartBean();
			i3.setId("I3");
			i3.setDesc("電流T相");
			curList.add(i3);
			BaseChartBean iavg = new BaseChartBean();
			iavg.setId("Iavg");
			iavg.setDesc("平均電流");
			curList.add(iavg);
			cur.setChildren(curList);
			recordBean.setCur(cur);
			
//			//相電壓:R相/S相/T相/平均電壓
			BaseChartBean vol = new BaseChartBean();
			vol.setId("Vol");
			vol.setDesc("相電壓");
			List<BaseChartBean> volList = new ArrayList<BaseChartBean>();
			BaseChartBean v1 = new BaseChartBean();
			v1.setId("V1");
			v1.setDesc("電壓R相");
			volList.add(v1);
			BaseChartBean v2 = new BaseChartBean();
			v2.setId("V2");
			v2.setDesc("電壓S相");
			volList.add(v2);
			BaseChartBean v3 = new BaseChartBean();
			v3.setId("V3");
			v3.setDesc("電壓T相");
			volList.add(v3);
			BaseChartBean vavg = new BaseChartBean();
			vavg.setId("Vavg");
			vavg.setDesc("平均電壓");
			volList.add(vavg);
			vol.setChildren(volList);
			recordBean.setVol(vol);
			
//			//線電壓:R相/S相/T相/平均電壓
			BaseChartBean volP = new BaseChartBean();
			volP.setId("VolP");
			volP.setDesc("線電壓");
			List<BaseChartBean> volPList = new ArrayList<BaseChartBean>();
			BaseChartBean v12 = new BaseChartBean();
			v12.setId("V12");
			v12.setDesc("線電壓R相");
			volPList.add(v12);
			BaseChartBean v23 = new BaseChartBean();
			v23.setId("V23");
			v23.setDesc("線電壓S相");
			volPList.add(v23);
			BaseChartBean v31 = new BaseChartBean();
			v31.setId("V31");
			v31.setDesc("線電壓T相");
			volPList.add(v31);
			BaseChartBean vavgP = new BaseChartBean();
			vavgP.setId("VavgP");
			vavgP.setDesc("平均線電壓");
			volPList.add(vavgP);
			volP.setChildren(volPList);
			recordBean.setVolP(volP);
			
//			//需量預測:混合式/浮動式/固定式/平均式
			BaseChartBean df = new BaseChartBean();
			df.setId("DF");
			df.setDesc("需量預測");
			List<BaseChartBean> dfList = new ArrayList<BaseChartBean>();
			BaseChartBean mode1 = new BaseChartBean();
			mode1.setId("Mode1");
			mode1.setDesc("混合式");
			dfList.add(mode1);
			BaseChartBean mode2 = new BaseChartBean();
			mode2.setId("Mode2");
			mode2.setDesc("浮動式");
			dfList.add(mode2);
			BaseChartBean mode3 = new BaseChartBean();
			mode3.setId("Mode3");
			mode3.setDesc("固定式");
			dfList.add(mode3);
			BaseChartBean mode4 = new BaseChartBean();
			mode4.setId("Mode4");
			mode4.setDesc("平均式");
			dfList.add(mode4);
			df.setChildren(dfList);
			recordBean.setDF(df);
			
			
			msgBean.setMsg(recordBean);
			msgBean.setCode("00");			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
//		return new JSONObject(json);
		return msgBean;
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
