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

import aptg.cathaybkeco.bean.BaseBean;
import aptg.cathaybkeco.bean.MsgBean;
import aptg.cathaybkeco.bean.RecordBean;
import aptg.cathaybkeco.util.JsonUtil;
import aptg.cathaybkeco.util.ToolUtil;

/**
 * Servlet implementation class getPowerRecordList 電力數值選項
 */
@WebServlet("/getPowerRecordList")
public class getPowerRecordList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getFormatterLogger(getPowerRecordList.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getPowerRecordList() {
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
	 * @return
	 * @throws Exception
	 */
	private MsgBean convertToJson() throws Exception {
		MsgBean msgBean = new MsgBean();
		try {
			RecordBean recordBean = new RecordBean();
			
//			//電流: R相/S相/T相/平均
			BaseBean cur = new BaseBean();
			cur.setId("Cur");
			cur.setDesc("電流");
			cur.setSortName("電流");
			List<BaseBean> curList = new ArrayList<BaseBean>();
			BaseBean i1 = new BaseBean();
			i1.setId("I1");
			i1.setDesc("R相");
			i1.setSortName("電流R相");
			curList.add(i1);
			BaseBean i2 = new BaseBean();
			i2.setId("I2");
			i2.setDesc("S相");
			i2.setSortName("電流S相");
			curList.add(i2);
			BaseBean i3 = new BaseBean();
			i3.setId("I3");
			i3.setDesc("T相");
			i3.setSortName("電流T相");
			curList.add(i3);
			BaseBean iavg = new BaseBean();
			iavg.setId("Iavg");
			iavg.setDesc("平均");
			iavg.setSortName("平均電流");
			curList.add(iavg);
			cur.setChildren(curList);
			recordBean.setCur(cur);
			
//			//相電壓:R相/S相/T相/平均電壓
			BaseBean vol = new BaseBean();
			vol.setId("Vol");
			vol.setDesc("相電壓");
			vol.setSortName("相電壓");
			List<BaseBean> volList = new ArrayList<BaseBean>();
			BaseBean v1 = new BaseBean();
			v1.setId("V1");
			v1.setDesc("R相");
			v1.setSortName("相電壓R相");
			volList.add(v1);
			BaseBean v2 = new BaseBean();
			v2.setId("V2");
			v2.setDesc("S相");
			v2.setSortName("相電壓S相");
			volList.add(v2);
			BaseBean v3 = new BaseBean();
			v3.setId("V3");
			v3.setDesc("T相");
			v3.setSortName("相電壓T相");
			volList.add(v3);
			BaseBean vavg = new BaseBean();
			vavg.setId("Vavg");
			vavg.setDesc("平均");
			vavg.setSortName("平均相電壓");
			volList.add(vavg);
			vol.setChildren(volList);
			recordBean.setVol(vol);
			
//			//線電壓:R相/S相/T相/平均電壓
			BaseBean volP = new BaseBean();
			volP.setId("VolP");
			volP.setDesc("線電壓");
			volP.setSortName("線電壓");
			List<BaseBean> volPList = new ArrayList<BaseBean>();
			BaseBean v12 = new BaseBean();
			v12.setId("V12");
			v12.setDesc("R相");
			v12.setSortName("線電壓R相");
			volPList.add(v12);
			BaseBean v23 = new BaseBean();
			v23.setId("V23");
			v23.setDesc("S相");
			v23.setSortName("線電壓S相");
			volPList.add(v23);
			BaseBean v31 = new BaseBean();
			v31.setId("V31");
			v31.setDesc("T相");
			v31.setSortName("線電壓T相");
			volPList.add(v31);
			BaseBean vavgP = new BaseBean();
			vavgP.setId("VavgP");
			vavgP.setDesc("平均");
			vavgP.setSortName("平均線電壓");
			volPList.add(vavgP);
			volP.setChildren(volPList);
			recordBean.setVolP(volP);
			
//			//功率: 實功/虛功/視在
			BaseBean power = new BaseBean();
			power.setId("Power");
			power.setDesc("功率");
			power.setSortName("功率");
			List<BaseBean> powerList = new ArrayList<BaseBean>();
			BaseBean w = new BaseBean();
			w.setId("W");
			w.setDesc("實功");
			w.setSortName("實功");
			powerList.add(w);
			BaseBean var = new BaseBean();
			var.setId("RP");
			var.setDesc("虛功");
			var.setSortName("虛功");
			powerList.add(var);
			BaseBean va = new BaseBean();
			va.setId("VA");
			va.setDesc("視在");
			va.setSortName("視在");
			powerList.add(va);
			BaseBean pf = new BaseBean();
			pf.setId("PF");
			pf.setDesc("功因");
			pf.setSortName("功因");
			powerList.add(pf);
			BaseBean hz = new BaseBean();
			hz.setId("Hz");
			hz.setDesc("頻率");
			hz.setSortName("頻率");
			powerList.add(hz);
			power.setChildren(powerList);
			recordBean.setPower(power);
			
//			//需量預測:混合式/浮動式/固定式/平均式
			BaseBean df = new BaseBean();
			df.setId("DF");
			df.setDesc("需量預測");
			df.setSortName("需量預測");
			List<BaseBean> dfList = new ArrayList<BaseBean>();
			BaseBean mode1 = new BaseBean();
			mode1.setId("Mode1");
			mode1.setDesc("混合式");
			mode1.setSortName("混合式");
			dfList.add(mode1);
			BaseBean mode2 = new BaseBean();
			mode2.setId("Mode2");
			mode2.setDesc("浮動式");
			mode2.setSortName("浮動式");
			dfList.add(mode2);
			BaseBean mode3 = new BaseBean();
			mode3.setId("Mode3");
			mode3.setDesc("固定式");
			mode3.setSortName("固定式");
			dfList.add(mode3);
			BaseBean mode4 = new BaseBean();
			mode4.setId("Mode4");
			mode4.setDesc("平均式");
			mode4.setSortName("平均式");
			dfList.add(mode4);
			df.setChildren(dfList);
			recordBean.setDF(df);
			
			//需量:尖峰/半尖峰/週六半尖峰/離峰
			BaseBean demand = new BaseBean();
			demand.setId("Demand");
			demand.setDesc("需量");
			demand.setSortName("需量");
			List<BaseBean> demandList = new ArrayList<BaseBean>();
			BaseBean demandPK = new BaseBean();
			demandPK.setId("DemandPK");
			demandPK.setDesc("尖峰");
			demandPK.setSortName("尖峰需量");
			demandList.add(demandPK);		
			BaseBean demandSP = new BaseBean();
			demandSP.setId("DemandSP");
			demandSP.setDesc("半尖峰");
			demandSP.setSortName("半尖峰需量");
			demandList.add(demandSP);
			BaseBean demandSatSP = new BaseBean();
			demandSatSP.setId("DemandSatSP");
			demandSatSP.setDesc("週六半尖峰");
			demandSatSP.setSortName("週六半尖峰需量");
			demandList.add(demandSatSP);
			BaseBean demandOP = new BaseBean();
			demandOP.setId("DemandOP");
			demandOP.setDesc("離峰");
			demandOP.setSortName("離峰需量");
			demandList.add(demandOP);
			demand.setChildren(demandList);
			recordBean.setDemand(demand);
			
			//ECO-5 時段用電量:尖峰/半尖峰/週六半尖峰/離峰
			BaseBean eco5 = new BaseBean();
			eco5.setId("ECO5");
			eco5.setDesc("ECO-5 時段用電量");
			eco5.setSortName("ECO-5 時段用電量");
			eco5.setChecked(true);
			List<BaseBean> eco5List = new ArrayList<BaseBean>();
			BaseBean eco5PK = new BaseBean();
			eco5PK.setId("ECO5PK");
			eco5PK.setDesc("尖峰");
			eco5PK.setSortName("尖峰用電量");
			eco5PK.setChecked(true);
			eco5List.add(eco5PK);		
			BaseBean eco5SP = new BaseBean();
			eco5SP.setId("ECO5SP");
			eco5SP.setDesc("半尖峰");
			eco5SP.setSortName("半尖峰用電量");
			eco5SP.setChecked(true);
			eco5List.add(eco5SP);
			BaseBean eco5SatSP = new BaseBean();
			eco5SatSP.setId("ECO5SatSP");
			eco5SatSP.setDesc("週六半尖峰");
			eco5SatSP.setSortName("週六半尖峰用電量");
			eco5SatSP.setChecked(true);
			eco5List.add(eco5SatSP);
			BaseBean eco5OP = new BaseBean();
			eco5OP.setId("ECO5OP");
			eco5OP.setDesc("離峰");
			eco5OP.setSortName("離峰用電量");
			eco5OP.setChecked(true);
			eco5List.add(eco5OP);
			eco5.setChildren(eco5List);
			recordBean.setECO5(eco5);
			
			//電表值KWH/無效電量Kvarh/電壓總諧波率THVavg/電流總諧波率 THIavg
			BaseBean other = new BaseBean();
			other.setId("Other");
			other.setDesc("");
			other.setSortName("");
			List<BaseBean> otherList = new ArrayList<BaseBean>();
			BaseBean kwh = new BaseBean();
			kwh.setId("KWH");
			kwh.setDesc("電表值KWH");
			kwh.setSortName("KWH");
			otherList.add(kwh);		
			BaseBean kvarh = new BaseBean();
			kvarh.setId("Kvarh");
			kvarh.setDesc("無效電量Kvarh");
			kvarh.setSortName("Kvarh");
			otherList.add(kvarh);
			BaseBean thvavg = new BaseBean();
			thvavg.setId("THVavg");
			thvavg.setDesc("電壓總諧波率THVavg");
			thvavg.setSortName("THVavg");
			otherList.add(thvavg);
			BaseBean thiavg = new BaseBean();
			thiavg.setId("THIavg");
			thiavg.setDesc("電流總諧波率THIavg");
			thiavg.setSortName("THIavg");
			otherList.add(thiavg);
			other.setChildren(otherList);
			recordBean.setOther(other);
			
			msgBean.setMsg(recordBean);
			msgBean.setCode("00");			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
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
