package aptg.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.beans.FcstCollectionBean;
import aptg.beans.MaxMinCCBean;
import aptg.beans.MaxUsuallyCCBean;
import aptg.beans.PowerRecordBean;
import aptg.models.BGTaskModel;
import aptg.models.BankInfModel;
import aptg.models.BestRatePlanModel;
import aptg.models.ControllerSetupModel;
import aptg.models.ElectricityPriceModel;
import aptg.models.ElectricityTimeDailyModel;
import aptg.models.ElectricityTimeModel;
import aptg.models.FcstChargeModel;
import aptg.models.MeterEventTxModel;
import aptg.models.MeterSetupModel;
import aptg.models.OPDayListModel;
import aptg.models.PowerAccountHistoryModel;
import aptg.models.PowerAccountModel;
import aptg.models.PowerMonthModel;
import aptg.models.PowerRecordCollectionModel;
import aptg.models.PowerRecordMaxDemandModel;
import aptg.models.PowerRecordModel;
import aptg.models.RepriceTaskModel;
import aptg.models.SysConfigModel;

public class DBContentDealUtil {
	
	public static List<MeterSetupModel> getMeterSetup(List<DynaBean> rows) {
		List<MeterSetupModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MeterSetupModel model = new MeterSetupModel();
			model.setSeqno((int) bean.get("seqno"));
			model.setDeviceID((String) bean.get("deviceid"));
			model.setEco5Account((String) bean.get("eco5account"));
			model.setMeterSerialNr((Integer) bean.get("meterserialnr"));
			model.setMeterId((Integer) bean.get("meterid"));
			model.setMeterName((String) bean.get("metername"));
			model.setMeterTypeCode((Integer) bean.get("metertypecode"));
			model.setInstallPosition((String) bean.get("installposition"));
			model.setTreeChartId((String) bean.get("treechartid"));
			model.setEnabled((int) bean.get("enabled"));
			model.setWiringCode((Integer) bean.get("wiringcode"));
			model.setUsageCode((Integer) bean.get("usagecode"));
			model.setPowerAccount((String) bean.get("poweraccount"));
			model.setPowerFactorEnabled((Integer) bean.get("powerfactorenabled"));
			model.setAreaName((String) bean.get("areaname"));
			model.setArea((BigDecimal) bean.get("area"));
			model.setPeople((Integer) bean.get("people"));
			model.setRatedPower((BigDecimal) bean.get("ratedpower"));
			model.setDfEnabled((Integer) bean.get("dfenabled"));
			model.setDfCode((Integer) bean.get("dfcode"));
			model.setDfCycle((Integer) bean.get("dfcycle"));
			model.setDfUpLimit((Integer) bean.get("dfuplimit"));
			model.setDfLoLimit((Integer) bean.get("dflolimit"));
			model.setUsuallyCC((Integer) bean.get("usuallycc"));
			model.setSpcc((Integer) bean.get("spcc"));
			model.setSatSPCC((Integer) bean.get("satspcc"));
			model.setOpcc((Integer) bean.get("opcc"));
			model.setCurAlertEnabled((Integer) bean.get("curalertenabled"));
			model.setCurUpLimit((BigDecimal) bean.get("curuplimit"));
			model.setCurLoLimit((BigDecimal) bean.get("curlolimit"));
			model.setVolAlertEnabled((Integer) bean.get("volalertenabled"));
			model.setVolAlertType((Integer) bean.get("volalerttype"));
			model.setVolUpLimit((BigDecimal) bean.get("voluplimit"));
			model.setVolLoLimit((BigDecimal) bean.get("vollolimit"));
			model.setEcAlertEnabled((Integer) bean.get("ecalertenabled"));
			model.setEcUpLimit((BigDecimal) bean.get("ecuplimit"));
			model.setCreateTime( ToolUtil.getInstance().convertDateToString((Date) bean.get("createtime"), "yyyy-MM-dd HH:mm:ss") );
			model.setUpdateTime( (bean.get("updatetime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("updatetime"), "yyyy-MM-dd HH:mm:ss") : null );
			
			list.add(model);
		}
		return list;
	}
	
	public static List<PowerRecordModel> getDeviceMinRectimePowerRecordList(List<DynaBean> rows) {
		List<PowerRecordModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordModel model = new PowerRecordModel();
			model.setDeviceID((String) bean.get("deviceid"));
			model.setRecTime( (bean.get("rectime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd") : null );
			
			list.add(model);
		}
		return list;
	}
	
	/*
	 * version 1
	 */
//	public static List<PowerRecordMaxDemandModel> getPowerRecordMaxDemandList(List<DynaBean> rows) {
//		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
//		
//		Iterator<DynaBean> iter = rows.iterator();
//		while (iter.hasNext()) {
//			DynaBean bean = (DynaBean)iter.next();
//			
//			PowerRecordMaxDemandModel model = new PowerRecordMaxDemandModel();
//
//			model.setDeviceID((String) bean.get("deviceid"));
//			model.setRecTime( (bean.get("rectime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd HH:mm:ss") : null );
//			model.setTotalDemand( (bean.get("totaldemand")!=null)?(BigDecimal) bean.get("totaldemand"):BigDecimal.ZERO);
//			
//			list.add(model);
//		}
//		return list;
//	}
	
	/*
	 * version 2
	 */
	public static List<PowerRecordMaxDemandModel> getPowerRecordMaxDemandList(List<DynaBean> rows) {
		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordMaxDemandModel model = new PowerRecordMaxDemandModel();

			model.setDeviceID((String) bean.get("deviceid"));
			model.setRecTime( (bean.get("rectime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setMaxDemand( (bean.get("maxdemand")!=null)?(BigDecimal) bean.get("maxdemand"):BigDecimal.ZERO);
		
			list.add(model);
		}
		return list;
	}
	public static List<PowerRecordMaxDemandModel> getPowerRecordDemandList(List<DynaBean> rows) {
		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordMaxDemandModel model = new PowerRecordMaxDemandModel();

			model.setDeviceID((String) bean.get("deviceid"));
			model.setRecTime( (bean.get("rectime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setMaxDemand( (bean.get("maxdemand")!=null)?(BigDecimal) bean.get("maxdemand"):BigDecimal.ZERO);
			
			list.add(model);
		}
		return list;
	}
	

	public static List<PowerRecordModel> getBasicPowerRecord(List<DynaBean> rows) {
		List<PowerRecordModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordModel record = new PowerRecordModel();
			record.setIavg((bean.get("iavg")!=null)?(BigDecimal) bean.get("iavg"):BigDecimal.ZERO);
			record.setVavg((bean.get("vavg")!=null)?(BigDecimal) bean.get("vavg"):BigDecimal.ZERO);
			record.setW((bean.get("w")!=null)?(BigDecimal) bean.get("w"):BigDecimal.ZERO);
			record.setVavgP((bean.get("vavgp")!=null)?(BigDecimal) bean.get("vavgp"):BigDecimal.ZERO);

			list.add(record);
		}
		return list;
	}
	
	public static PowerRecordModel getLastPowerRecord(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordModel model = new PowerRecordModel();
			model.setDeviceID((String) bean.get("deviceid"));
			model.setRecTime( (bean.get("recdate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("recdate"), "yyyy-MM-dd") : null );

			model.setDemandPK((bean.get("demandpk")!=null)?(BigDecimal) bean.get("demandpk"):BigDecimal.ZERO);
			model.setDemandSP((bean.get("demandsp")!=null)?(BigDecimal) bean.get("demandsp"):BigDecimal.ZERO);
			model.setDemandSatSP((bean.get("demandsatsp")!=null)?(BigDecimal) bean.get("demandsatsp"):BigDecimal.ZERO);
			model.setDemandOP((bean.get("demandop")!=null)?(BigDecimal) bean.get("demandop"):BigDecimal.ZERO);

			model.setMCECPK((bean.get("mcecpk")!=null)?(BigDecimal) bean.get("mcecpk"):BigDecimal.ZERO);
			model.setMCECSP((bean.get("mcecsp")!=null)?(BigDecimal) bean.get("mcecsp"):BigDecimal.ZERO);
			model.setMCECSatSP((bean.get("mcecsatsp")!=null)?(BigDecimal) bean.get("mcecsatsp"):BigDecimal.ZERO);
			model.setMCECOP((bean.get("mcecop")!=null)?(BigDecimal) bean.get("mcecop"):BigDecimal.ZERO);
			
			model.setKWh((BigDecimal) bean.get("kwh"));

			// for 計算PowerRecordCollection前撈取此record使用的ratePlanCode
			model.setPowerAccount((String) bean.get("poweraccount"));
			model.setApplyDate( (bean.get("applydate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("applydate"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setRatePlanCode((Integer) bean.get("rateplancode"));
			
			return model;
		}
		return null;
	}
	public static List<PowerRecordModel> getPowerRecords(List<DynaBean> rows) {
		List<PowerRecordModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordModel model = new PowerRecordModel();
			model.setDeviceID((String) bean.get("deviceid"));
			model.setRecTime( (bean.get("recdate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("recdate"), "yyyy-MM-dd") : null );

			model.setDemandPK((bean.get("demandpk")!=null)?(BigDecimal) bean.get("demandpk"):BigDecimal.ZERO);
			model.setDemandSP((bean.get("demandsp")!=null)?(BigDecimal) bean.get("demandsp"):BigDecimal.ZERO);
			model.setDemandSatSP((bean.get("demandsatsp")!=null)?(BigDecimal) bean.get("demandsatsp"):BigDecimal.ZERO);
			model.setDemandOP((bean.get("demandop")!=null)?(BigDecimal) bean.get("demandop"):BigDecimal.ZERO);

			model.setMCECPK((bean.get("mcecpk")!=null)?(BigDecimal) bean.get("mcecpk"):BigDecimal.ZERO);
			model.setMCECSP((bean.get("mcecsp")!=null)?(BigDecimal) bean.get("mcecsp"):BigDecimal.ZERO);
			model.setMCECSatSP((bean.get("mcecsatsp")!=null)?(BigDecimal) bean.get("mcecsatsp"):BigDecimal.ZERO);
			model.setMCECOP((bean.get("mcecop")!=null)?(BigDecimal) bean.get("mcecop"):BigDecimal.ZERO);
			
			model.setKWh((BigDecimal) bean.get("kwh"));

			// for 計算PowerRecordCollection前撈取此record使用的ratePlanCode
			model.setPowerAccount((String) bean.get("poweraccount"));
			model.setApplyDate( (bean.get("applydate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("applydate"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setRatePlanCode((Integer) bean.get("rateplancode"));
			
			list.add(model);
		}
		return list;
	}

	public static PowerRecordModel getMaxPowerRecord(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordModel model = new PowerRecordModel();
//			model.setDeviceID((String) bean.get("deviceid"));
//			model.setRecTime( (bean.get("rectime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd") : null );

			model.setMode1((bean.get("mode1")!=null) ? (BigDecimal) bean.get("mode1") : BigDecimal.ZERO);
			model.setMode2((bean.get("mode2")!=null) ? (BigDecimal) bean.get("mode2") : BigDecimal.ZERO);
			model.setMode3((bean.get("mode3")!=null) ? (BigDecimal) bean.get("mode3") : BigDecimal.ZERO);
			model.setMode4((bean.get("mode4")!=null) ? (BigDecimal) bean.get("mode4") : BigDecimal.ZERO);

			model.setDemandPK((bean.get("demandpk")!=null)?(BigDecimal) bean.get("demandpk"):BigDecimal.ZERO);
			model.setDemandSP((bean.get("demandsp")!=null)?(BigDecimal) bean.get("demandsp"):BigDecimal.ZERO);
			model.setDemandSatSP((bean.get("demandsatsp")!=null)?(BigDecimal) bean.get("demandsatsp"):BigDecimal.ZERO);
			model.setDemandOP((bean.get("demandop")!=null)?(BigDecimal) bean.get("demandop"):BigDecimal.ZERO);

			return model;
		}
		return null;
	}
	public static List<PowerRecordModel> getMaxPowerRecordList(List<DynaBean> rows) {
		List<PowerRecordModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordModel model = new PowerRecordModel();
			model.setDeviceID((String) bean.get("deviceid"));
			model.setRecTime( (bean.get("rectime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd") : null );

//			model.setMode1((bean.get("mode1")!=null) ? (BigDecimal) bean.get("mode1") : BigDecimal.ZERO);
//			model.setMode2((bean.get("mode2")!=null) ? (BigDecimal) bean.get("mode2") : BigDecimal.ZERO);
//			model.setMode3((bean.get("mode3")!=null) ? (BigDecimal) bean.get("mode3") : BigDecimal.ZERO);
//			model.setMode4((bean.get("mode4")!=null) ? (BigDecimal) bean.get("mode4") : BigDecimal.ZERO);

			model.setDemandPK((bean.get("demandpk")!=null)?(BigDecimal) bean.get("demandpk"):BigDecimal.ZERO);
			model.setDemandSP((bean.get("demandsp")!=null)?(BigDecimal) bean.get("demandsp"):BigDecimal.ZERO);
			model.setDemandSatSP((bean.get("demandsatsp")!=null)?(BigDecimal) bean.get("demandsatsp"):BigDecimal.ZERO);
			model.setDemandOP((bean.get("demandop")!=null)?(BigDecimal) bean.get("demandop"):BigDecimal.ZERO);

			list.add(model);
		}
		return list;
	}
	
	public static FcstChargeModel getFcstKPIinfo(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
		
			FcstChargeModel fcst = new FcstChargeModel();
			
			fcst.setUseTime((String) bean.get("usemonth"));
			fcst.setTotalCharge((BigDecimal) bean.get("totalcharge"));
			fcst.setTPMCEC((BigDecimal) bean.get("tpmcec"));
					
			return fcst;
		}
		return null;
	}

	
	public static List<FcstCollectionBean> getFcstCharge(List<DynaBean> rows) {
		List<FcstCollectionBean> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
		
			FcstCollectionBean fc = new FcstCollectionBean();
			fc.setPowerAccount((String) bean.get("poweraccount"));
			fc.setDeviceID((String) bean.get("deviceid"));
			fc.setPowerPhase(((Integer) bean.get("powerphase")==1) ? 1 : 3);	// default 3
			fc.setRecDate( (bean.get("recdate")!=null)?ToolUtil.getInstance().convertDateToString((Date) bean.get("recdate"), "yyyy-MM-dd") : null);
			fc.setRatePlanCode((int) bean.get("rateplancode"));
			fc.setUsuallyCC( ((Integer) bean.get("usuallycc")!=null)?(Integer) bean.get("usuallycc"):0 );
			fc.setSPCC( ((Integer) bean.get("spcc")!=null)?(Integer) bean.get("spcc"):0 );
			fc.setSatSPCC( ((Integer) bean.get("satspcc")!=null)?(Integer) bean.get("satspcc"):0 );
			fc.setOPCC( ((Integer) bean.get("opcc")!=null)?(Integer) bean.get("opcc"):0 );
			
			fc.setMDemandPK((BigDecimal) bean.get("mdemandpk"));
			fc.setMDemandSP((BigDecimal) bean.get("mdemandsp"));
			fc.setMDemandSatSP((BigDecimal) bean.get("mdemandsatsp"));
			fc.setMDemandOP((BigDecimal) bean.get("mdemandop"));
			
			fc.setMCECPK((BigDecimal) bean.get("mcecpk"));
			fc.setMCECSP((BigDecimal) bean.get("mcecsp"));
			fc.setMCECSatSP((BigDecimal) bean.get("mcecsatsp"));
			fc.setMCECOP((BigDecimal) bean.get("mcecop"));
			fc.setMCEC((BigDecimal) bean.get("cec"));
			
			fc.setFcstECO5MCECPK((BigDecimal) bean.get("fcsteco5mcecpk"));
			fc.setFcstECO5MCECSP((BigDecimal) bean.get("fcsteco5mcecsp"));
			fc.setFcstECO5MCECSatSP((BigDecimal) bean.get("fcsteco5mcecsatsp"));
			fc.setFcstECO5MCECOP((BigDecimal) bean.get("fcsteco5mcecop"));
			fc.setFcstECO5MCEC((BigDecimal) bean.get("fcsteco5mcec"));
			
			list.add(fc);
		}
		return list;
	}
	
	public static Map<String, FcstChargeModel> getFcstChargeModel(List<DynaBean> rows) {
		Map<String, FcstChargeModel> fcMap = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			FcstChargeModel fc = new FcstChargeModel();

			fc.setPowerAccount((String) bean.get("poweraccount"));
			fc.setUseMonth((String) bean.get("usemonth"));
			fc.setUseTime( (bean.get("usetime")!=null)?ToolUtil.getInstance().convertDateToString((Date) bean.get("usetime"), "yyyy-MM-dd HH:mm:ss") : null);
			fc.setRatePlanCode((int) bean.get("rateplancode"));

			fc.setUsuallyCC( ((Integer) bean.get("usuallycc")!=null)?(Integer) bean.get("usuallycc"):0 );
			fc.setSPCC( ((Integer) bean.get("spcc")!=null)?(Integer) bean.get("spcc"):0 );
			fc.setSatSPCC( ((Integer) bean.get("satspcc")!=null)?(Integer) bean.get("satspcc"):0 );
			fc.setOPCC( ((Integer) bean.get("opcc")!=null)?(Integer) bean.get("opcc"):0 );

			fc.setMDemandPK((BigDecimal) bean.get("mdemandpk"));
			fc.setMDemandSP((BigDecimal) bean.get("mdemandsp"));
			fc.setMDemandSatSP((BigDecimal) bean.get("mdemandsatsp"));
			fc.setMDemandOP((BigDecimal) bean.get("mdemandop"));
			
			fc.setTPMDemandPK((BigDecimal) bean.get("tpmdemandpk"));
			fc.setTPMDemandSP((BigDecimal) bean.get("tpmdemandsp"));
			fc.setTPMDemandSatSP((BigDecimal) bean.get("tpmdemandsatsp"));
			fc.setTPMDemandOP((BigDecimal) bean.get("tpmdemandop"));

			fc.setMCECPK((BigDecimal) bean.get("mcecpk"));
			fc.setMCECSP((BigDecimal) bean.get("mcecsp"));
			fc.setMCECSatSP((BigDecimal) bean.get("mcecsatsp"));
			fc.setMCECOP((BigDecimal) bean.get("mcecop"));
			fc.setMCEC((BigDecimal) bean.get("mcec"));
			
			fc.setTPMCECPK((BigDecimal) bean.get("tpmcecpk"));
			fc.setTPMCECSP((BigDecimal) bean.get("tpmcecsp"));
			fc.setTPMCECSatSP((BigDecimal) bean.get("tpmcecsatsp"));
			fc.setTPMCECOP((BigDecimal) bean.get("tpmcecop"));
			fc.setTPMCEC((BigDecimal) bean.get("tpmcec"));
			
			fc.setBaseCharge((BigDecimal) bean.get("basecharge"));
			fc.setUsageCharge((BigDecimal) bean.get("usagecharge"));
			fc.setOverCharge((BigDecimal) bean.get("overcharge"));
			fc.setTotalCharge((BigDecimal) bean.get("totalcharge"));
			
			fc.setFcstMCECPK((BigDecimal) bean.get("fcstmcecpk"));
			fc.setFcstMCECSP((BigDecimal) bean.get("fcstmcecsp"));
			fc.setFcstMCECSatSP((BigDecimal) bean.get("fcstmcecsatsp"));
			fc.setFcstMCECOP((BigDecimal) bean.get("fcstmcecop"));
			fc.setFcstMCEC((BigDecimal) bean.get("fcstmcec"));
			
			fc.setFcstBaseCharge((BigDecimal) bean.get("fcstbasecharge"));
			fc.setFcstUsageCharge((BigDecimal) bean.get("fcstusagecharge"));
			fc.setFcstOverCharge((BigDecimal) bean.get("fcstovercharge"));
			fc.setFcstTotalCharge((BigDecimal) bean.get("fcsttotalcharge"));
			
			fc.setOverPK( ((Integer) bean.get("overpk")!=null) ? (Integer) bean.get("overpk"):0 );
			fc.setOverSP( ((Integer) bean.get("oversp")!=null) ? (Integer) bean.get("oversp"):0 );
			fc.setOverSatSP( ((Integer) bean.get("oversatsp")!=null) ? (Integer) bean.get("oversatsp"):0 );
			fc.setOverOP( ((Integer) bean.get("overop")!=null) ? (Integer) bean.get("overop"):0);
			
			fc.setRealPlan((Integer) bean.get("realplan"));

			fcMap.put(fc.getPowerAccount(), fc);
		}
		return fcMap;
	}
	
	public static Map<Integer, ElectricityPriceModel> getElectricityPrice(List<DynaBean> rows) {
		Map<Integer, ElectricityPriceModel> map = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			ElectricityPriceModel model = new ElectricityPriceModel();
			model.setYear((String) bean.get("year"));
			model.setMonth((String) bean.get("month"));
			model.setRatePlanCode((int) bean.get("rateplancode"));
			model.setLampPrice1((bean.get("lampprice1")!=null)?(BigDecimal) bean.get("lampprice1"):BigDecimal.ZERO);
			model.setLampPrice2((bean.get("lampprice2")!=null)?(BigDecimal) bean.get("lampprice2"):BigDecimal.ZERO);
			model.setLampPrice3((bean.get("lampprice3")!=null)?(BigDecimal) bean.get("lampprice3"):BigDecimal.ZERO);
			model.setLampPrice4((bean.get("lampprice4")!=null)?(BigDecimal) bean.get("lampprice4"):BigDecimal.ZERO);
			model.setLampPrice5((bean.get("lampprice5")!=null)?(BigDecimal) bean.get("lampprice5"):BigDecimal.ZERO);
			model.setLampPrice6((bean.get("lampprice6")!=null)?(BigDecimal) bean.get("lampprice6"):BigDecimal.ZERO);
			model.setLampPrice1S((bean.get("lampprice1s")!=null)?(BigDecimal) bean.get("lampprice1s"):BigDecimal.ZERO);
			model.setLampPrice2S((bean.get("lampprice2s")!=null)?(BigDecimal) bean.get("lampprice2s"):BigDecimal.ZERO);
			model.setLampPrice3S((bean.get("lampprice3s")!=null)?(BigDecimal) bean.get("lampprice3s"):BigDecimal.ZERO);
			model.setLampPrice4S((bean.get("lampprice4s")!=null)?(BigDecimal) bean.get("lampprice4s"):BigDecimal.ZERO);
			model.setLampPrice5S((bean.get("lampprice5s")!=null)?(BigDecimal) bean.get("lampprice5s"):BigDecimal.ZERO);
			model.setLampPrice6S((bean.get("lampprice6s")!=null)?(BigDecimal) bean.get("lampprice6s"):BigDecimal.ZERO);
			model.setLampStep1((bean.get("lampstep1")!=null)?(Integer) bean.get("lampstep1"):0);
			model.setLampStep2((bean.get("lampstep2")!=null)?(Integer) bean.get("lampstep2"):0);
			model.setLampStep3((bean.get("lampstep3")!=null)?(Integer) bean.get("lampstep3"):0);
			model.setLampStep4((bean.get("lampstep4")!=null)?(Integer) bean.get("lampstep4"):0);
			model.setLampStep5((bean.get("lampstep5")!=null)?(Integer) bean.get("lampstep5"):0);
			model.setLampBPrice1((bean.get("lampbprice1")!=null)?(BigDecimal) bean.get("lampbprice1"):BigDecimal.ZERO);
			model.setLampBPrice2((bean.get("lampbprice2")!=null)?(BigDecimal) bean.get("lampbprice2"):BigDecimal.ZERO);
			model.setLampBPrice3((bean.get("lampbprice3")!=null)?(BigDecimal) bean.get("lampbprice3"):BigDecimal.ZERO);
			model.setLampBPrice4((bean.get("lampbprice4")!=null)?(BigDecimal) bean.get("lampbprice4"):BigDecimal.ZERO);
			model.setLampBPrice1S((bean.get("lampbprice1s")!=null)?(BigDecimal) bean.get("lampbprice1s"):BigDecimal.ZERO);
			model.setLampBPrice2S((bean.get("lampbprice2s")!=null)?(BigDecimal) bean.get("lampbprice2s"):BigDecimal.ZERO);
			model.setLampBPrice3S((bean.get("lampbprice3s")!=null)?(BigDecimal) bean.get("lampbprice3s"):BigDecimal.ZERO);
			model.setLampBPrice4S((bean.get("lampbprice4s")!=null)?(BigDecimal) bean.get("lampbprice4s"):BigDecimal.ZERO);
			model.setLampBStep1((Integer) bean.get("lampbstep1"));
			model.setLampBStep2((Integer) bean.get("lampbstep2"));
			model.setLampBStep3((Integer) bean.get("lampbstep3"));
			model.setBaseCharge1phase((bean.get("basecharge1phase")!=null)?(BigDecimal) bean.get("basecharge1phase"):BigDecimal.ZERO);
			model.setBaseCharge3phase((bean.get("basecharge3phase")!=null)?(BigDecimal) bean.get("basecharge3phase"):BigDecimal.ZERO);
			model.setBaseChargeUsually((bean.get("basechargeusually")!=null)?(BigDecimal) bean.get("basechargeusually"):BigDecimal.ZERO);
			model.setBaseChargeSP((bean.get("basechargesp")!=null)?(BigDecimal) bean.get("basechargesp"):BigDecimal.ZERO);
			model.setBaseChargeSatSP((bean.get("basechargesatsp")!=null)?(BigDecimal) bean.get("basechargesatsp"):BigDecimal.ZERO);
			model.setBaseChargeOP((bean.get("basechargeop")!=null)?(BigDecimal) bean.get("basechargeop"):BigDecimal.ZERO);
			model.setBaseChargeUsuallyS((bean.get("basechargeusuallys")!=null)?(BigDecimal) bean.get("basechargeusuallys"):BigDecimal.ZERO);
			model.setBaseChargeSPS((bean.get("basechargesps")!=null)?(BigDecimal) bean.get("basechargesps"):BigDecimal.ZERO);
			model.setBaseChargeSatSPS((bean.get("basechargesatsps")!=null)?(BigDecimal) bean.get("basechargesatsps"):BigDecimal.ZERO);
			model.setBaseChargeOPS((bean.get("basechargeops")!=null)?(BigDecimal) bean.get("basechargeops"):BigDecimal.ZERO);
			model.setTimeCharge((bean.get("timecharge")!=null)?(BigDecimal) bean.get("timecharge"):BigDecimal.ZERO);
			model.setTimeChargeSP((bean.get("timechargesp")!=null)?(BigDecimal) bean.get("timechargesp"):BigDecimal.ZERO);
			model.setTimeChargeSatSP((bean.get("timechargesatsp")!=null)?(BigDecimal) bean.get("timechargesatsp"):BigDecimal.ZERO);
			model.setTimeChargeOP((bean.get("timechargeop")!=null)?(BigDecimal) bean.get("timechargeop"):BigDecimal.ZERO);
			model.setTimeChargeS((bean.get("timecharge")!=null)?(BigDecimal) bean.get("timecharge"):BigDecimal.ZERO);
			model.setTimeChargeSPS((bean.get("timechargesp")!=null)?(BigDecimal) bean.get("timechargesp"):BigDecimal.ZERO);
			model.setTimeChargeSatSPS((bean.get("timechargesatsp")!=null)?(BigDecimal) bean.get("timechargesatsp"):BigDecimal.ZERO);
			model.setTimeChargeOPS((bean.get("timechargeop")!=null)?(BigDecimal) bean.get("timechargeop"):BigDecimal.ZERO);
			model.setOver2KPrice((bean.get("over2kprice")!=null)?(BigDecimal) bean.get("over2kprice"):BigDecimal.ZERO);
			
			map.put(model.getRatePlanCode(), model);
		}
		return map;
	}
	public static List<ElectricityPriceModel> getElectricityPriceList(List<DynaBean> rows) {
		List<ElectricityPriceModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			ElectricityPriceModel model = new ElectricityPriceModel();
			model.setYear((String) bean.get("year"));
			model.setMonth((String) bean.get("month"));
			model.setRatePlanCode((int) bean.get("rateplancode"));
			model.setLampPrice1((bean.get("lampprice1")!=null)?(BigDecimal) bean.get("lampprice1"):BigDecimal.ZERO);
			model.setLampPrice2((bean.get("lampprice2")!=null)?(BigDecimal) bean.get("lampprice2"):BigDecimal.ZERO);
			model.setLampPrice3((bean.get("lampprice3")!=null)?(BigDecimal) bean.get("lampprice3"):BigDecimal.ZERO);
			model.setLampPrice4((bean.get("lampprice4")!=null)?(BigDecimal) bean.get("lampprice4"):BigDecimal.ZERO);
			model.setLampPrice5((bean.get("lampprice5")!=null)?(BigDecimal) bean.get("lampprice5"):BigDecimal.ZERO);
			model.setLampPrice6((bean.get("lampprice6")!=null)?(BigDecimal) bean.get("lampprice6"):BigDecimal.ZERO);
			model.setLampPrice1S((bean.get("lampprice1s")!=null)?(BigDecimal) bean.get("lampprice1s"):BigDecimal.ZERO);
			model.setLampPrice2S((bean.get("lampprice2s")!=null)?(BigDecimal) bean.get("lampprice2s"):BigDecimal.ZERO);
			model.setLampPrice3S((bean.get("lampprice3s")!=null)?(BigDecimal) bean.get("lampprice3s"):BigDecimal.ZERO);
			model.setLampPrice4S((bean.get("lampprice4s")!=null)?(BigDecimal) bean.get("lampprice4s"):BigDecimal.ZERO);
			model.setLampPrice5S((bean.get("lampprice5s")!=null)?(BigDecimal) bean.get("lampprice5s"):BigDecimal.ZERO);
			model.setLampPrice6S((bean.get("lampprice6s")!=null)?(BigDecimal) bean.get("lampprice6s"):BigDecimal.ZERO);
			model.setLampStep1((bean.get("lampstep1")!=null)?(Integer) bean.get("lampstep1"):0);
			model.setLampStep2((bean.get("lampstep2")!=null)?(Integer) bean.get("lampstep2"):0);
			model.setLampStep3((bean.get("lampstep3")!=null)?(Integer) bean.get("lampstep3"):0);
			model.setLampStep4((bean.get("lampstep4")!=null)?(Integer) bean.get("lampstep4"):0);
			model.setLampStep5((bean.get("lampstep5")!=null)?(Integer) bean.get("lampstep5"):0);
			model.setLampBPrice1((bean.get("lampbprice1")!=null)?(BigDecimal) bean.get("lampbprice1"):BigDecimal.ZERO);
			model.setLampBPrice2((bean.get("lampbprice2")!=null)?(BigDecimal) bean.get("lampbprice2"):BigDecimal.ZERO);
			model.setLampBPrice3((bean.get("lampbprice3")!=null)?(BigDecimal) bean.get("lampbprice3"):BigDecimal.ZERO);
			model.setLampBPrice4((bean.get("lampbprice4")!=null)?(BigDecimal) bean.get("lampbprice4"):BigDecimal.ZERO);
			model.setLampBPrice1S((bean.get("lampbprice1s")!=null)?(BigDecimal) bean.get("lampbprice1s"):BigDecimal.ZERO);
			model.setLampBPrice2S((bean.get("lampbprice2s")!=null)?(BigDecimal) bean.get("lampbprice2s"):BigDecimal.ZERO);
			model.setLampBPrice3S((bean.get("lampbprice3s")!=null)?(BigDecimal) bean.get("lampbprice3s"):BigDecimal.ZERO);
			model.setLampBPrice4S((bean.get("lampbprice4s")!=null)?(BigDecimal) bean.get("lampbprice4s"):BigDecimal.ZERO);
			model.setLampBStep1((Integer) bean.get("lampbstep1"));
			model.setLampBStep2((Integer) bean.get("lampbstep2"));
			model.setLampBStep3((Integer) bean.get("lampbstep3"));
			
			model.setBaseCharge1phase((bean.get("basecharge1phase")!=null)?(BigDecimal) bean.get("basecharge1phase"):BigDecimal.ZERO);
			model.setBaseCharge3phase((bean.get("basecharge3phase")!=null)?(BigDecimal) bean.get("basecharge3phase"):BigDecimal.ZERO);
			
			model.setBaseChargeUsually((bean.get("basechargeusually")!=null)?(BigDecimal) bean.get("basechargeusually"):BigDecimal.ZERO);
			model.setBaseChargeSP((bean.get("basechargesp")!=null)?(BigDecimal) bean.get("basechargesp"):BigDecimal.ZERO);
			model.setBaseChargeSatSP((bean.get("basechargesatsp")!=null)?(BigDecimal) bean.get("basechargesatsp"):BigDecimal.ZERO);
			model.setBaseChargeOP((bean.get("basechargeop")!=null)?(BigDecimal) bean.get("basechargeop"):BigDecimal.ZERO);
			
			model.setBaseChargeUsuallyS((bean.get("basechargeusuallys")!=null)?(BigDecimal) bean.get("basechargeusuallys"):BigDecimal.ZERO);
			model.setBaseChargeSPS((bean.get("basechargesps")!=null)?(BigDecimal) bean.get("basechargesps"):BigDecimal.ZERO);
			model.setBaseChargeSatSPS((bean.get("basechargesatsps")!=null)?(BigDecimal) bean.get("basechargesatsps"):BigDecimal.ZERO);
			model.setBaseChargeOPS((bean.get("basechargeops")!=null)?(BigDecimal) bean.get("basechargeops"):BigDecimal.ZERO);
			
			model.setTimeCharge((bean.get("timecharge")!=null)?(BigDecimal) bean.get("timecharge"):BigDecimal.ZERO);
			model.setTimeChargeSP((bean.get("timechargesp")!=null)?(BigDecimal) bean.get("timechargesp"):BigDecimal.ZERO);
			model.setTimeChargeSatSP((bean.get("timechargesatsp")!=null)?(BigDecimal) bean.get("timechargesatsp"):BigDecimal.ZERO);
			model.setTimeChargeOP((bean.get("timechargeop")!=null)?(BigDecimal) bean.get("timechargeop"):BigDecimal.ZERO);
			
			model.setTimeChargeS((bean.get("timecharges")!=null)?(BigDecimal) bean.get("timecharges"):BigDecimal.ZERO);
			model.setTimeChargeSPS((bean.get("timechargesps")!=null)?(BigDecimal) bean.get("timechargesps"):BigDecimal.ZERO);
			model.setTimeChargeSatSPS((bean.get("timechargesatsps")!=null)?(BigDecimal) bean.get("timechargesatsps"):BigDecimal.ZERO);
			model.setTimeChargeOPS((bean.get("timechargeops")!=null)?(BigDecimal) bean.get("timechargeops"):BigDecimal.ZERO);
			
			model.setOver2KPrice((bean.get("over2kprice")!=null)?(BigDecimal) bean.get("over2kprice"):BigDecimal.ZERO);
			
			list.add(model);
		}
		return list;
	}

	public static Map<String, SysConfigModel> getSysConfig(List<DynaBean> rows) {
		Map<String, SysConfigModel> map = new HashMap<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			SysConfigModel sys = new SysConfigModel();
			sys.setParamname((String) bean.get("paramname"));
			sys.setParamvalue((String) bean.get("paramvalue"));
			
			map.put(sys.getParamname(), sys);
		}
		return map;
	}

	public static List<PowerAccountModel> getPowerAccountList(List<DynaBean> rows) {
		List<PowerAccountModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerAccountModel pa = new PowerAccountModel();
			pa.setPowerAccount((String) bean.get("poweraccount"));
			pa.setBankCode((String) bean.get("bankcode"));
			pa.setCustomerName((String) bean.get("customername"));
			pa.setAccountDesc((String) bean.get("accountdesc"));
			pa.setPATypeCode((Integer) bean.get("patypecode"));
			pa.setPowerPhase((int) bean.get("powerphase"));
			
			list.add(pa);
		}
		return list;
	}
	public static PowerAccountModel getPowerAccount(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerAccountModel pa = new PowerAccountModel();
			pa.setPowerAccount((String) bean.get("poweraccount"));
			pa.setBankCode((String) bean.get("bankcode"));
			pa.setCustomerName((String) bean.get("customername"));
			pa.setAccountDesc((String) bean.get("accountdesc"));
			pa.setPATypeCode((Integer) bean.get("patypecode"));
			pa.setPowerPhase((int) bean.get("powerphase"));
			pa.setModifyStatus((Integer) bean.get("modifystatus"));
			
			return pa;
		}
		return null;
	}
	
	public static Map<String, PowerMonthModel> getPowerMonth(List<DynaBean> rows) {
		Map<String, PowerMonthModel> map = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerMonthModel pm = new PowerMonthModel();
			
			pm.setPowerAccount((String) bean.get("poweraccount"));
			pm.setUseMonth((String) bean.get("usemonth"));
			pm.setRatePlanCode((int) bean.get("rateplancode"));

			pm.setUsuallyCC( ((Integer) bean.get("usuallycc")!=null)?(Integer) bean.get("usuallycc"):0 );
			pm.setSPCC( ((Integer) bean.get("spcc")!=null)?(Integer) bean.get("spcc"):0 );
			pm.setSatSPCC( ((Integer) bean.get("satspcc")!=null)?(Integer) bean.get("satspcc"):0 );
			pm.setOPCC( ((Integer) bean.get("opcc")!=null)?(Integer) bean.get("opcc"):0 );

			pm.setMDemandPK((BigDecimal) bean.get("mdemandpk"));
			pm.setMDemandSP((BigDecimal) bean.get("mdemandsp"));
			pm.setMDemandSatSP((BigDecimal) bean.get("mdemandsatsp"));
			pm.setMDemandOP((BigDecimal) bean.get("mdemandop"));
			
			pm.setMCECPK((BigDecimal) bean.get("mcecpk"));
			pm.setMCECSP((BigDecimal) bean.get("mcecsp"));
			pm.setMCECSatSP((BigDecimal) bean.get("mcecsatsp"));
			pm.setMCECOP((BigDecimal) bean.get("mcecop"));
			pm.setMCEC((BigDecimal) bean.get("mcec"));
			
			pm.setRealPlan((Integer) bean.get("realplan"));
			
//			pm.setTPMDemandPK((BigDecimal) bean.get("tpmdemandpk"));
//			pm.setTPMDemandSP((BigDecimal) bean.get("tpmdemandsp"));
//			pm.setTPMDemandSatSP((BigDecimal) bean.get("tpmdemandsatsp"));
//			pm.setTPMDemandOP((BigDecimal) bean.get("tpmdemandop"));
//			
//			pm.setOverPK( ((Integer) bean.get("overpk")!=null)?(Integer) bean.get("overpk"):0 );
//			pm.setOverSP( ((Integer) bean.get("oversp")!=null)?(Integer) bean.get("oversp"):0 );
//			pm.setOverSatSP( ((Integer) bean.get("oversatsp")!=null)?(Integer) bean.get("oversatsp"):0 );
//			pm.setOverOP( ((Integer) bean.get("overop")!=null)?(Integer) bean.get("overop"):0);
			
			map.put(pm.getPowerAccount(), pm);
		}
		return map;
	}
	
	public static BestRatePlanModel getBestRatePlan(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();

			BestRatePlanModel best = new BestRatePlanModel();
			best.setPowerAccount((String) bean.get("poweraccount"));
			best.setUseMonth((String) bean.get("usemonth"));
			best.setInUse((int) bean.get("inuse"));
			best.setRatePlanCode((int) bean.get("rateplancode"));

			best.setUsuallyCC( ((Integer) bean.get("usuallycc")!=null)?(Integer) bean.get("usuallycc"):0 );
			best.setSPCC( ((Integer) bean.get("spcc")!=null)?(Integer) bean.get("spcc"):0 );
			best.setSatSPCC( ((Integer) bean.get("satspcc")!=null)?(Integer) bean.get("satspcc"):0 );
			best.setOPCC( ((Integer) bean.get("opcc")!=null)?(Integer) bean.get("opcc"):0 );
			
			best.setTPMDemandPK((BigDecimal) bean.get("tpmdemandpk"));
			best.setTPMDemandSP((BigDecimal) bean.get("tpmdemandsp"));
			best.setTPMDemandSatSP((BigDecimal) bean.get("tpmdemandsatsp"));
			best.setTPMDemandOP((BigDecimal) bean.get("tpmdemandop"));
			
			best.setTPMCECPK((BigDecimal) bean.get("tpmcecpk"));
			best.setTPMCECSP((BigDecimal) bean.get("tpmcecsp"));
			best.setTPMCECSatSP((BigDecimal) bean.get("tpmcecsatsp"));
			best.setTPMCECOP((BigDecimal) bean.get("tpmcecop"));
			best.setTPMCEC((BigDecimal) bean.get("tpmcec"));
			
			best.setBaseCharge((BigDecimal) bean.get("basecharge"));
			best.setUsageCharge((BigDecimal) bean.get("usagecharge"));
			best.setOverCharge((BigDecimal) bean.get("overcharge"));
			best.setTotalCharge((BigDecimal) bean.get("totalcharge"));

			return best;
		}
		return null;
	}
	
	public static BankInfModel getBankInf(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			BankInfModel bank = new BankInfModel();
//			bank.setBankCode((String) bean.get("bankcode"));
//			bank.setBankName((String) bean.get("bankname"));
//			bank.setPostCodeNo((Integer) bean.get("postcodeno"));
//			bank.setBankAddress((String) bean.get("bankaddress"));
//			bank.setPhoneNr((String) bean.get("phonenr"));
			bank.setArea((BigDecimal) bean.get("area"));
			BigDecimal people = (BigDecimal) bean.get("people");
			bank.setPeople(people.intValue());
			
			return bank;
		}
		return null;
	}
	
	public static PowerRecordCollectionModel getCollectionKPIinfo(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordCollectionModel col = new PowerRecordCollectionModel();
			col.setCEC((BigDecimal) bean.get("cec"));
			
			return col;
		}
		return null;
	}

//	public static List<PowerRecordCollectionModel> getCollectionDemandList(List<DynaBean> rows) {
//		List<PowerRecordCollectionModel> list = new ArrayList<>();
//		
//		Iterator<DynaBean> iter = rows.iterator();
//		while (iter.hasNext()) {
//			DynaBean bean = (DynaBean)iter.next();
//			
//			PowerRecordCollectionModel col = new PowerRecordCollectionModel();
//			col.setDemandPK((BigDecimal) bean.get("demandpk"));
//			col.setDemandSP((BigDecimal) bean.get("demandsp"));
//			col.setDemandSatSP((BigDecimal) bean.get("demandsatsp"));
//			col.setDemandOP((BigDecimal) bean.get("demandop"));
//			
//			list.add(col);
//		}
//		return list;
//	}
	
	public static List<PowerRecordCollectionModel> getCollectionMDemandList(List<DynaBean> rows) {
		List<PowerRecordCollectionModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerRecordCollectionModel col = new PowerRecordCollectionModel();
			col.setMDemandPK((BigDecimal) bean.get("mdemandpk"));
			col.setMDemandSP((BigDecimal) bean.get("mdemandsp"));
			col.setMDemandSatSP((BigDecimal) bean.get("mdemandsatsp"));
			col.setMDemandOP((BigDecimal) bean.get("mdemandop"));
			
			list.add(col);
		}
		return list;
	}
	
	public static OPDayListModel getOPDay(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();

			OPDayListModel op = new OPDayListModel();
			op.setOpDay(ToolUtil.getInstance().convertDateToString((Date) bean.get("opday"), "yyyy-MM-dd"));
			
			return op;
		}
		return null;
	}
	public static List<OPDayListModel> getOPDayList(List<DynaBean> rows) {
		List<OPDayListModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();

			OPDayListModel op = new OPDayListModel();
			op.setOpDay(ToolUtil.getInstance().convertDateToString((Date) bean.get("opday"), "yyyy-MM-dd"));
			
			list.add(op);
		}
		return list;
	}
	
	public static List<ElectricityTimeModel> getElectricityTime(List<DynaBean> rows) {
		List<ElectricityTimeModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			ElectricityTimeModel et = new ElectricityTimeModel();
			et.setSummer((int) bean.get("summer"));
			et.setDayOfWeek((int) bean.get("dayofweek"));
			et.setUsuallyHour((BigDecimal) bean.get("usuallyhour"));
			et.setSPHour((BigDecimal) bean.get("sphour"));
			et.setSatSPHour((BigDecimal) bean.get("satsphour"));
			et.setOPHour((BigDecimal) bean.get("ophour"));
			
			list.add(et);
		}
		return list;
	}
	
	public static ElectricityTimeDailyModel getElectricityTimeDaily(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			ElectricityTimeDailyModel daily = new ElectricityTimeDailyModel();
			daily.setRecDate( (bean.get("recdate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("recdate"), "yyyy-MM-dd") : null);
			daily.setUsuallyHour((BigDecimal) bean.get("usuallyhour"));
			daily.setSPHour((BigDecimal) bean.get("sphour"));
			daily.setSatSPHour((BigDecimal) bean.get("satsphour"));
			daily.setOPHour((BigDecimal) bean.get("ophour"));
			
			return daily;
		}
		return null;
	}
	public static List<ElectricityTimeDailyModel> getElectricityTimeDailyList(List<DynaBean> rows) {
		List<ElectricityTimeDailyModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			ElectricityTimeDailyModel daily = new ElectricityTimeDailyModel();
			daily.setRecDate( (bean.get("recdate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("recdate"), "yyyy-MM-dd") : null);
			daily.setUsuallyHour((BigDecimal) bean.get("usuallyhour"));
			daily.setSPHour((BigDecimal) bean.get("sphour"));
			daily.setSatSPHour((BigDecimal) bean.get("satsphour"));
			daily.setOPHour((BigDecimal) bean.get("ophour"));

			list.add(daily);
		}
		return list;
	}
	
	public static MaxMinCCBean getMaxMinCCByPowerAccount(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MaxMinCCBean max = new MaxMinCCBean();
			max.setPowerAccount((String)bean.get("poweraccount"));
			max.setMaxCC((Integer) bean.get("maxcc"));
			max.setMinCC((Integer) bean.get("mincc"));

			if (max.getPowerAccount()==null)
				return null;
			else
				return max;
		}
		return null;
	}
	public static List<MaxUsuallyCCBean> getMaxUsuallyCCgroupbyRatePlanCode(List<DynaBean> rows) {
		List<MaxUsuallyCCBean> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MaxUsuallyCCBean max = new MaxUsuallyCCBean();
			max.setMaxCC((Integer) bean.get("maxcc"));
			max.setMinCC((Integer) bean.get("mincc"));
			
			max.setSpcc((Integer) bean.get("spcc"));
			max.setSatSPCC((Integer) bean.get("satspcc"));
			max.setOpcc((Integer) bean.get("opcc"));
			
			max.setRatePlanCode((Integer) bean.get("rateplancode"));
			max.setPowerAccount((String)bean.get("poweraccount"));
			max.setUseMonth((String)bean.get("usemonth"));
			
			list.add(max);
		}
		return list;
	}
	
	public static List<ControllerSetupModel> getEnabledController(List<DynaBean> rows) {
		List<ControllerSetupModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			ControllerSetupModel cs = new ControllerSetupModel();
			cs.setECO5Account((String)bean.get("eco5account"));
			
			list.add(cs);
		}
		return list;
	}
	
	public static Map<String, MeterEventTxModel> getMeterEventTx(List<DynaBean> rows) {
		Map<String, MeterEventTxModel> map = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MeterEventTxModel tx = new MeterEventTxModel();
			tx.setSeqno((Integer) bean.get("seqno"));
			tx.setEco5Account((String) bean.get("eco5account"));
			tx.setDeviceID((String) bean.get("deviceid"));
			tx.setOpenTime(ToolUtil.getInstance().convertDateToString((Date) bean.get("opentime"), "yyyy-MM-dd HH:mm:ss"));
			tx.setCloseTime(ToolUtil.getInstance().convertDateToString((Date) bean.get("closetime"), "yyyy-MM-dd HH:mm:ss"));
			tx.setEventCode((int) bean.get("eventcode"));
			tx.setPriority((int) bean.get("priority"));
			tx.setEventStatus((int) bean.get("eventstatus"));
			tx.setOpenSeqno((Integer) bean.get("openseqno"));
			tx.setCloseSeqno((Integer) bean.get("closeseqno"));
			
			map.put(tx.getEco5Account(), tx);
		}
		return map;
	}

	public static List<RepriceTaskModel> getRepriceTask(List<DynaBean> rows) {
		List<RepriceTaskModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			RepriceTaskModel rp = new RepriceTaskModel();
			rp.setSeqno((int) bean.get("seqno"));
			rp.setDeviceID((String) bean.get("deviceid"));
			rp.setPowerAccount((String) bean.get("poweraccount"));
			rp.setRepriceFrom((String) bean.get("repricefrom"));
			rp.setStartDate(ToolUtil.getInstance().convertDateToString((Date) bean.get("startdate"), "yyyy-MM-dd"));
			rp.setStatusCode((int) bean.get("statuscode"));

//			rp.setPowerPhaseOld((Integer) bean.get("powerphaseold"));
//			rp.setApplyDateOld(ToolUtil.getInstance().convertDateToString((Date) bean.get("applydateold"), "yyyy-MM-dd HH:mm:ss"));
//			rp.setRatePlanCodeOld((int) bean.get("rateplancodeold"));
//			rp.setUsuallyCCOld((int) bean.get("usuallyccold"));
//			rp.setSPCCOld((int) bean.get("spccold"));
//			rp.setSatSPCCOld((int) bean.get("satspccold"));
//			rp.setOPCCOld((int) bean.get("opccold"));
//
//			rp.setPowerPhaseNew((Integer) bean.get("powerphasenew"));
//			rp.setApplyDateNew(ToolUtil.getInstance().convertDateToString((Date) bean.get("applydatenew"), "yyyy-MM-dd HH:mm:ss"));
//			rp.setRatePlanCodeNew((int) bean.get("rateplancodenew"));
//			rp.setUsuallyCCNew((int) bean.get("usuallyccnew"));
//			rp.setSPCCNew((int) bean.get("spccnew"));
//			rp.setSatSPCCNew((int) bean.get("satspccnew"));
//			rp.setOPCCNew((int) bean.get("opccnew"));
			
			list.add(rp);
		}
		return list;
	}
	public static List<BGTaskModel> getBGTask(List<DynaBean> rows) {
		List<BGTaskModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			BGTaskModel bg = new BGTaskModel();
			bg.setSeqno((int) bean.get("seqno"));
			bg.setBGTaskType((String) bean.get("bgtasktype"));
			bg.setPowerAccountOld((String) bean.get("poweraccountold"));
			bg.setPowerAccountNew((String) bean.get("poweraccountnew"));
			bg.setStatusCode((int) bean.get("statuscode"));
			
			list.add(bg);
		}
		return list;
	}
	
	public static PowerAccountHistoryModel getPowerAccountHistory(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			PowerAccountHistoryModel pah = new PowerAccountHistoryModel();
			pah.setRatePlanCode((Integer) bean.get("rateplancode"));
			pah.setUsuallyCC((Integer) bean.get("usuallycc"));
			pah.setSPCC((Integer) bean.get("spcc"));
			pah.setSatSPCC((Integer) bean.get("satspcc"));
			pah.setOPCC((Integer) bean.get("opcc"));
			
			return pah;
		}
		return null;
	}
}
