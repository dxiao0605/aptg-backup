package aptg.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.models.ControllerSetupModel;
import aptg.models.MeterEventRecordModel;
import aptg.models.MeterEventTxModel;
import aptg.models.MeterSetupModel;
import aptg.models.SysConfigModel;

public class DBContentDealUtil {

	public static List<ControllerSetupModel> getControllerSetupList(List<DynaBean> rows) {
		List<ControllerSetupModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();

			ControllerSetupModel model = new ControllerSetupModel();
			model.setSeqno((int) bean.get("seqno"));
			model.setEco5Account((String) bean.get("eco5account"));
			model.setEco5Password((String) bean.get("eco5password"));
			model.setBankCode((String) bean.get("bankcode"));
			model.setInstallPosition((String) bean.get("installposition"));
			model.setEnabled((int) bean.get("enabled"));
			model.setEnabledTime( (bean.get("enabledtime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("enabledtime"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setExpireDate( (bean.get("expiredate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("expiredate"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setIp((String) bean.get("ip"));
			model.setCreateTime( ToolUtil.getInstance().convertDateToString((Date) bean.get("createtime"), "yyyy-MM-dd HH:mm:ss") );
			model.setUpdateTime( (bean.get("updatetime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("updatetime"), "yyyy-MM-dd HH:mm:ss") : null );

			list.add(model);
		}
		return list;
	}
	public static ControllerSetupModel getControllerSetup(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();

			ControllerSetupModel model = new ControllerSetupModel();
			model.setSeqno((int) bean.get("seqno"));
			model.setEco5Account((String) bean.get("eco5account"));
			model.setEco5Password((String) bean.get("eco5password"));
			model.setBankCode((String) bean.get("bankcode"));
			model.setInstallPosition((String) bean.get("installposition"));
			model.setEnabled((int) bean.get("enabled"));
			model.setEnabledTime( (bean.get("enabledtime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("enabledtime"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setExpireDate( (bean.get("expiredate")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("expiredate"), "yyyy-MM-dd HH:mm:ss") : null );
			model.setIp((String) bean.get("ip"));
			model.setCreateTime( ToolUtil.getInstance().convertDateToString((Date) bean.get("createtime"), "yyyy-MM-dd HH:mm:ss") );
			model.setUpdateTime( (bean.get("updatetime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("updatetime"), "yyyy-MM-dd HH:mm:ss") : null );
			
			return model;
		}
		return null;
	}

	public static List<MeterSetupModel> getMeterSetupList(List<DynaBean> rows) {
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
			model.setDfUpLimit((Integer) bean.get("dfuplimit"));
			model.setDfLoLimit((Integer) bean.get("dflolimit"));
			model.setDfCycle((Integer) bean.get("dfcycle"));
			
			model.setUsuallyCC((Integer) bean.get("usuallycc"));
			model.setSpcc((Integer) bean.get("spcc"));
			model.setSatSPCC((Integer) bean.get("satspcc"));
			model.setOpcc((Integer) bean.get("opcc"));
			
			model.setCurUpLimit((BigDecimal) bean.get("curuplimit"));
			model.setCurLoLimit((BigDecimal) bean.get("curlolimit"));
			model.setCurAlertEnabled((Integer) bean.get("curalertenabled"));
			
			model.setVolAlertType((Integer) bean.get("volalerttype"));
			model.setVolUpLimit((BigDecimal) bean.get("voluplimit"));
			model.setVolLoLimit((BigDecimal) bean.get("vollolimit"));
			model.setVolAlertEnabled((Integer) bean.get("volalertenabled"));
			
			model.setEcUpLimit((BigDecimal) bean.get("ecuplimit"));
			model.setEcAlertEnabled((Integer) bean.get("ecalertenabled"));
			
			model.setCreateTime( ToolUtil.getInstance().convertDateToString((Date) bean.get("createtime"), "yyyy-MM-dd HH:mm:ss") );
			model.setUpdateTime( (bean.get("updatetime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("updatetime"), "yyyy-MM-dd HH:mm:ss") : null );
			
			list.add(model);
		}
		return list;
	}
	public static MeterSetupModel getMeterSetup(List<DynaBean> rows) {
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
			model.setDfUpLimit((Integer) bean.get("dfuplimit"));
			model.setDfLoLimit((Integer) bean.get("dflolimit"));
			model.setDfCycle((Integer) bean.get("dfcycle"));
			
			model.setUsuallyCC((Integer) bean.get("usuallycc"));
			model.setSpcc((Integer) bean.get("spcc"));
			model.setSatSPCC((Integer) bean.get("satspcc"));
			model.setOpcc((Integer) bean.get("opcc"));
			
			model.setCurUpLimit((BigDecimal) bean.get("curuplimit"));
			model.setCurLoLimit((BigDecimal) bean.get("curlolimit"));
			model.setCurAlertEnabled((Integer) bean.get("curalertenabled"));
			
			model.setVolAlertType((Integer) bean.get("volalerttype"));
			model.setVolUpLimit((BigDecimal) bean.get("voluplimit"));
			model.setVolLoLimit((BigDecimal) bean.get("vollolimit"));
			model.setVolAlertEnabled((Integer) bean.get("volalertenabled"));
			
			model.setEcUpLimit((BigDecimal) bean.get("ecuplimit"));
			model.setEcAlertEnabled((Integer) bean.get("ecalertenabled"));
			
			model.setCreateTime( ToolUtil.getInstance().convertDateToString((Date) bean.get("createtime"), "yyyy-MM-dd HH:mm:ss") );
			model.setUpdateTime( (bean.get("updatetime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("updatetime"), "yyyy-MM-dd HH:mm:ss") : null );
			
			return model;
		}
		return null;
	}
	
	public static MeterEventRecordModel getMeterEventRecord(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MeterEventRecordModel record = new MeterEventRecordModel();
			record.setSeqno((int) bean.get("seqno"));
			record.setEco5Account((String) bean.get("eco5account"));
			record.setDeviceID((String) bean.get("deviceid"));
			record.setEventTime( (bean.get("eventtime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("eventtime"), "yyyy-MM-dd HH:mm:ss") : null );
			record.setEventCode((Integer) bean.get("eventcode"));
			record.setEventDesc((String) bean.get("eventdesc"));
			
			return record;
		}
		return null;
	}

	
	public static List<MeterEventRecordModel> getMeterEventRecordList(List<DynaBean> rows) {
		List<MeterEventRecordModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MeterEventRecordModel record = new MeterEventRecordModel();
			record.setSeqno((int) bean.get("seqno"));
			record.setEco5Account((String) bean.get("eco5account"));
			record.setDeviceID((String) bean.get("deviceid"));
			record.setEventTime( (bean.get("eventtime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("eventtime"), "yyyy-MM-dd HH:mm:ss") : null );
			record.setEventCode((Integer) bean.get("eventcode"));
			record.setEventDesc((String) bean.get("eventdesc"));
			
			list.add(record);
		}
		return list;
	}
	
	public static List<MeterEventTxModel> getMeterEventTx(List<DynaBean> rows) {
		List<MeterEventTxModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			MeterEventTxModel tx = new MeterEventTxModel();
			tx.setSeqno((Integer) bean.get("seqno"));
			tx.setEco5Account((String) bean.get("eco5account"));
			tx.setDeviceID((String) bean.get("deviceid"));
			tx.setOpenTime((String) bean.get("opentime"));
			tx.setCloseTime((String) bean.get("closetime"));
			tx.setEventCode((int) bean.get("eventcode"));
			tx.setPriority((int) bean.get("priority"));
			tx.setEventStatus((int) bean.get("eventstatus"));
			tx.setOpenSeqno((Integer) bean.get("openseqno"));
			tx.setCloseSeqno((Integer) bean.get("closeseqno"));
			
			list.add(tx);
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
}
