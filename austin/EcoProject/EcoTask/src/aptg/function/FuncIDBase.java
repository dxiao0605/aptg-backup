package aptg.function;

import java.math.BigDecimal;

import aptg.models.PowerRecordModel;

public class FuncIDBase {

	private static final BigDecimal thousand 		= new BigDecimal("1000");
	
	/**
	 * S25
	 * AttrID 未定義於 Table1
	 * 
	 * @param ecoAttr
	 * @param attrID
	 * @param attrValue
	 * @return
	 */
	protected void checkAttrID(PowerRecordModel power, String attrID, String attrValue) {
		attrID = attrID.toLowerCase();
		switch(attrID) {
			case "i1":	// 三相之A(R)相電流
				power.setI1(new BigDecimal(attrValue));	// I1	電流R
				break;
			case "i2":	// 三相之B(S)相電流
				power.setI2(new BigDecimal(attrValue)); // I2	電流S
				break;
			case "i3":	// 三相之C(T)相電流
				power.setI3(new BigDecimal(attrValue));	// I3	電流T
				break;
			case "iavg":		// 單相三線：平均電流, 三相三線：平均電流, 三相四線：平均電流
				power.setIavg(new BigDecimal(attrValue));	// Iavg	平均電流
				break;

			case "v1":	// 三相之A(R)相電壓
				power.setV1(new BigDecimal(attrValue));	// V1	相電壓R
				break;
			case "v2":	// 三相之B(S)相電壓
				power.setV2(new BigDecimal(attrValue));	// V2	相電壓S
				break;
			case "v3":	// 三相之C(T)相電壓
				power.setV3(new BigDecimal(attrValue));	// V3	相電壓T
				break;
			case "vavg":	// 電壓: 直流電壓
				power.setVavg(new BigDecimal(attrValue));	// Vavg	平均相電壓
				break;
				
			case "v12":	// 三相：A 相( R 相)到 B 相(S相)線電壓, 單位V
				power.setV12(new BigDecimal(attrValue));	// V12	線電壓AB
				break;
			case "v23":	// 三相：B 相( S 相)到 C 相(T相)線電壓, 單位V
				power.setV23(new BigDecimal(attrValue));	// V23	線電壓BC
				break;
			case "v31":	// 三相：C 相( T 相)到 A 相(R相)線電壓, 單位V
				power.setV31(new BigDecimal(attrValue));	// V31	線電壓CA
				break;
			case "vavgp":		// 三相三線：平均線電壓, 三相四線：平均線電壓, 單位V
				power.setVavgP(new BigDecimal(attrValue));	// VavgP	平均線電壓
				break;

			case "w":	// 實功率: 直流功率
				power.setW(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "var":
				power.setVar(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "va":
				power.setVA(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "pf":
				power.setPF(new BigDecimal(attrValue));
				break;

			case "kwh":	// 正相累積電能: 直流累積能
				power.setKWh(new BigDecimal(attrValue));
				break;
			case "kvarh":
				power.setKvarh(new BigDecimal(attrValue));
				break;
			case "hz":
				power.setHz(new BigDecimal(attrValue));
				break;
			case "thdvavg":
				power.setTHVavg(new BigDecimal(attrValue));
				break;
			case "thdiavg":
				power.setTHIavg(new BigDecimal(attrValue));
				break;

			case "mode1":
				power.setMode1(new BigDecimal(attrValue));
				break;
			case "mode2":
				power.setMode2(new BigDecimal(attrValue));
				break;
			case "mode3":
				power.setMode3(new BigDecimal(attrValue));
				break;
			case "mode4":
				power.setMode4(new BigDecimal(attrValue));
				break;
				
			case "demand":
				power.setDemandPK(new BigDecimal(attrValue));
				break;
			case "demandhalf":
				power.setDemandSP(new BigDecimal(attrValue));
				break;
			case "demandsathalf":
				power.setDemandSatSP(new BigDecimal(attrValue));
				break;
			case "demandoff":
				power.setDemandOP(new BigDecimal(attrValue));
				break;

			case "rushhour":
				power.setMCECPK(new BigDecimal(attrValue));
				break;
			case "halfhour":
				power.setMCECSP(new BigDecimal(attrValue));
				break;
			case "sathalfhour":
				power.setMCECSatSP(new BigDecimal(attrValue));
				break;
			case "offhour":
				power.setMCECOP(new BigDecimal(attrValue));
				break;
				
			case "rushhourtoday":
				power.setHighCECPK(new BigDecimal(attrValue));
				break;
			case "halfhourtoday":
				power.setHighCECSP(new BigDecimal(attrValue));
				break;
			case "offhourtoday":
				power.setHighCECOP(new BigDecimal(attrValue));
				break;
				
			default:
		}
	}
}
