package aptg.function;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.AttrBean;
import aptg.manager.SysConfigManager;
import aptg.models.PowerRecordModel;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 	系統功能
 * 
 * @author austinchen
 */
public class FuncID_S extends FuncIDBase {

	private static final Logger loggerIllegal = LogManager.getFormatterLogger(FuncID_S_IllegalValue.class.getName());

	private static final String Sysconfig_IllegalDemandValue 	= "IllegalDemandValue";

	private BigDecimal illegalDemandValue;
	
	public FuncID_S() {
		this.illegalDemandValue = new BigDecimal(SysConfigManager.getInstance().getSysconfig(Sysconfig_IllegalDemandValue));
	}

	/**
	 * S25
	 * 回傳電力數值
	 * 
	 * @param message
	 * @return
	 */
	public PowerRecordModel updateS25(String[] message) throws Exception {
		String func = message[1];	// S25
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		String deviceID = message[4];	// 設備ID: ES110000000000TEST00
		String reportTime = message[6];	// 回報時間: 130691738

		List<AttrBean> attrInfoList = new ArrayList<AttrBean>();
		String attrList = message[5];	// 電力數值ID_電力數值: 4_0.9,6_123,7_123,8_123,9_123,14_123,15_12,27_123,28_12,40_123,41_12
		if (attrList.indexOf(",")!=-1) {
			// 多筆數據
			String[] attr = attrList.split(",");
			for (String attrInfo: attr) {
				String[] info = attrInfo.split("_");
				AttrBean bean = new AttrBean();
				bean.setAttrID(info[0]);
				bean.setAttrValue(info[1]);
				attrInfoList.add(bean);
			}
		} else {
			// 一筆數據
			String[] info = attrList.split("_");
			AttrBean bean = new AttrBean();
			bean.setAttrID(info[0]);
			bean.setAttrValue(info[1]);
			attrInfoList.add(bean);
		}
		
		// AttrID未定義於Table1 (1S2502, 2017)
		PowerRecordModel power = new PowerRecordModel();
		for (AttrBean bean: attrInfoList) {
			checkAttrID(power, bean.getAttrID(), bean.getAttrValue());
		}
		attrInfoList = null;
		
		// db info
		power.setDeviceID(deviceID);
		String time = ToolUtil.getInstance().convertDateToString(new Date(Long.valueOf(reportTime) * 1000L), "yyyy-MM-dd HH:mm:ss");
		power.setRecTime(time);
		
		// 檢查Mode1~4, DemandPK, SP, SatSP, OP
		checkIllegalDemandValue(power);
		
		return power;
	}
	private void checkIllegalDemandValue(PowerRecordModel power) {
		boolean illegal = false;
		
		PowerRecordModel illegalRecord = new PowerRecordModel();
		illegalRecord.setDeviceID(power.getDeviceID());
		illegalRecord.setRecTime(power.getRecTime());
		illegalRecord.setMode1(power.getMode1());
		illegalRecord.setMode2(power.getMode2());
		illegalRecord.setMode3(power.getMode3());
		illegalRecord.setMode4(power.getMode4());
		illegalRecord.setDemandPK(power.getDemandPK());
		illegalRecord.setDemandSP(power.getDemandSP());
		illegalRecord.setDemandSatSP(power.getDemandSatSP());
		illegalRecord.setDemandOP(power.getDemandOP());

		if (power.getMode1().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setMode1(BigDecimal.ZERO);
		}
		if (power.getMode2().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setMode2(BigDecimal.ZERO);
		}
		if (power.getMode3().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setMode3(BigDecimal.ZERO);
		}
		if (power.getMode4().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setMode4(BigDecimal.ZERO);
		}
		if (power.getDemandPK().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setDemandPK(BigDecimal.ZERO);
		}
		if (power.getDemandSP().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setDemandSP(BigDecimal.ZERO);
		}
		if (power.getDemandSatSP().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setDemandSatSP(BigDecimal.ZERO);
		}
		if (power.getDemandOP().compareTo(illegalDemandValue)==1) {
			illegal = true;
			power.setDemandOP(BigDecimal.ZERO);
		}

		if (illegal) {
			loggerIllegal.info("原值: "+JsonUtil.getInstance().convertObjectToJsonstring(illegalRecord));
		}
		illegalRecord = null;
	}
	
}
