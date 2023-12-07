package aptg.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.bean.AllocationNBListBean;
import aptg.bean.BatteryGroupNBListBean;
import aptg.model.BatteryModel;
import aptg.model.CommandTaskModel;
import aptg.model.CompanyModel;
import aptg.model.EventModel;
import aptg.model.NBAllocationHisModel;
import aptg.model.NBListModel;
import aptg.model.SysConfigModel;

public class DBContentDealUtil {

	public static List<CompanyModel> getCompany(List<DynaBean> rows) {
		List<CompanyModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 
			 CompanyModel model = new CompanyModel();
			 
			 model.setCompanyCode((int) bean.get("companycode"));
			 model.setCompanyName((String) bean.get("companyname"));
			 model.setLanguage((int) bean.get("language"));
			 model.setImpType((int) bean.get("imptype"));
			 model.setAlert1((BigDecimal) bean.get("alert1"));
			 model.setAlert2((BigDecimal) bean.get("alert2"));
			 model.setDisconnect((long) bean.get("disconnect"));
			 model.setLogoPath((String) bean.get("logopath"));
			 model.setTemperature1((int) bean.get("temperature1"));
			 
			 list.add(model);
		}
		return list;
	}
	
	public static List<AllocationNBListBean> getAllocationNBList(List<DynaBean> rows) {
		List<AllocationNBListBean> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();

			 NBAllocationHisModel nbAllocatin = new NBAllocationHisModel(); 
			 nbAllocatin.setNBID((String) bean.get("nbid"));
			 nbAllocatin.setCompanyCode((int) bean.get("companycode"));
			 nbAllocatin.setStarttime( (bean.get("starttime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("starttime"), "yyyy-MM-dd HH:mm:ss") : null);
			 nbAllocatin.setEndtime(null);

			 NBListModel nbList = new NBListModel();
			 nbList.setNbID((String) bean.get("nbid"));
			 nbList.setGroupInternalID((int) bean.get("groupinternalid"));
			 nbList.setAllocate((int) bean.get("allocate"));
			 nbList.setActive((int) bean.get("active"));

			 AllocationNBListBean an = new AllocationNBListBean();
			 an.setNbAllocatin(nbAllocatin);
			 an.setNbList(nbList);
			 list.add(an);
		}
		return list;
	}
	
	public static List<BatteryGroupNBListBean> getBatteryGroupNBList(List<DynaBean> rows) {
		List<BatteryGroupNBListBean> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			BatteryGroupNBListBean bgnb = new BatteryGroupNBListBean();
			bgnb.setGroupInternalID((int) bean.get("seqno"));
			bgnb.setCompanyCode((int) bean.get("companycode"));
			bgnb.setNBID((String) bean.get("nbid"));
//			bgnb.setActive((int) bean.get("active"));
			bgnb.setDefaultGroup((int) bean.get("defaultgroup"));
			
			list.add(bgnb);
		}
		return list;
	}
	
	public static List<NBListModel> getNBList(List<DynaBean> rows) {
		List<NBListModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();

			 NBListModel nbList = new NBListModel();
			 nbList.setGroupInternalID((int) bean.get("groupinternalid"));
			 nbList.setNbID((String) bean.get("nbid"));
			 nbList.setAllocate((int) bean.get("allocate"));
			 nbList.setActive((int) bean.get("active"));
			 
			 list.add(nbList);
		}
		return list;
	}
	
	public static List<BatteryModel> getBattery(List<DynaBean> rows) {
		List<BatteryModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
		
			 BatteryModel model = new BatteryModel();
			 
			 model.setNbID((String) bean.get("nbid"));
			 model.setBatteryID((int) bean.get("batteryid"));
			 
			 model.setIrRecords((int) bean.get("irrecords"));
			 model.setvRecords((int) bean.get("vrecords"));
			 model.settRecords((int) bean.get("trecords"));
			 
			 list.add(model);
		}
		return list;
	}

	public static Map<String, String> getConfig(List<DynaBean> rows) {
		Map<String, String> map = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 
			 String key = (String) bean.get("key");
			 String value = (String) bean.get("value");
			 
			 map.put(key, value);
		}
		return map;
	}
	
	public static List<CommandTaskModel> getCommandTask(List<DynaBean> rows) {
		List<CommandTaskModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 
			 CommandTaskModel model = new CommandTaskModel();
			 model.setTaskID((String) bean.get("taskid"));
			 model.setGroupInternalID((Integer) bean.get("groupinternalid"));
			 model.setNbID((String) bean.get("nbid"));
			 model.setBatteryID((int) bean.get("batteryid"));
			 model.setCommandID((String) bean.get("commandid"));
			 model.setTaskStatus((int) bean.get("taskstatus"));
			 model.setReqTime(((Date) bean.get("reqtime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("reqtime"), "yyyy-MM-dd HH:mm:ss") : null);
			 model.setConfig((String) bean.get("config"));
			 model.setHexConfig((String) bean.get("hexconfig"));
			 
			 list.add(model);
		}
		return list;
	}
	
	public static EventModel getUnsolvedEvent(List<DynaBean> rows) {
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 
			 EventModel event = new EventModel();
			 event.setNbID((String) bean.get("nbid"));
			 event.setBatteryID((Integer) bean.get("batteryid"));
//			 event.setRecordTime(recordTime);
			 event.setEventType((Integer) bean.get("eventtype"));
			 event.setEventStatus((Integer) bean.get("eventstatus"));
//			 event.setCloseTime(closeTime);
//			 event.setCloseUser(closeUser);
//			 event.setCloseContent(closeContent);
			 event.setImpType((Integer) bean.get("imptype"));
//			 event.setAlert1(alert1);
//			 event.setAlert2(alert2);
			 event.setDisconnect((Long) bean.get("disconnect"));
			 event.setTimeZone((Integer) bean.get("timezone"));
			 
			 return event;
		}
		return null;
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
