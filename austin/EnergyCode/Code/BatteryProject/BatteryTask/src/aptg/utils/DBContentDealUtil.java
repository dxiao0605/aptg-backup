package aptg.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.beans.BatteryGroupNBListBean;
import aptg.beans.CompanyDisconnectBean;
import aptg.model.BatteryGroupModel;
import aptg.model.BatteryRecordSummaryModel;
import aptg.model.EventModel;
import aptg.model.GroupDailyStatusModel;
import aptg.model.NBListModel;

public class DBContentDealUtil {

	public static List<BatteryGroupModel> getBatteryGroup(List<DynaBean> rows) {
		List<BatteryGroupModel> list = new ArrayList<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			BatteryGroupModel bg = new BatteryGroupModel();
			bg.setSeqNo((int) bean.get("seqno"));
			bg.setCompanyCode((int) bean.get("companycode"));
			
			list.add(bg);
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
			
			bgnb.setStartTime(ToolUtil.getInstance().convertDateToString((Date) bean.get("starttime"), "yyyy-MM-dd HH:mm:ss"));
			bgnb.setEndTime(ToolUtil.getInstance().convertDateToString((Date) bean.get("endtime"), "yyyy-MM-dd HH:mm:ss"));
			
			list.add(bgnb);
		}
		return list;
	}
	
	public static List<BatteryRecordSummaryModel> getBatteryRecordSummary(List<DynaBean> rows) {
		List<BatteryRecordSummaryModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
		
			BatteryRecordSummaryModel br = new BatteryRecordSummaryModel();
			br.setNbid((String) bean.get("nbid"));
			br.setBatteryID((int) bean.get("batteryid"));
			br.setRecTime(ToolUtil.getInstance().convertDateToString((Date) bean.get("rectime"), "yyyy-MM-dd HH:mm:ss"));
			
//			long uploadstamp = (long) bean.get("uploadstamp");
//			br.setUploadStamp((int) uploadstamp);
			
			br.setTimeZone((int) bean.get("timezone"));
//			br.setMaxIR((BigDecimal) bean.get("maxir"));
//			br.setMinIR((BigDecimal) bean.get("minir"));
//			br.setMaxVol((BigDecimal) bean.get("maxvol"));
//			br.setMinVol((BigDecimal) bean.get("minvol"));
//			br.setTemperature((BigDecimal) bean.get("temperature"));
			br.setStatus((int) bean.get("status"));
			
			br.setCreatetime( (bean.get("createtime")!=null) ? (Date)bean.get("createtime") : null );
			
			list.add(br);
		}
		return list;
	}
	
	public static Map<String, CompanyDisconnectBean> getCompanyDisconnect(List<DynaBean> rows) {
		Map<String, CompanyDisconnectBean> map = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
		
			CompanyDisconnectBean cd = new CompanyDisconnectBean();
			cd.setNbID((String) bean.get("nbid"));
			cd.setCompanyCode((int) bean.get("companycode"));
			cd.setDisconnect((long) bean.get("disconnect"));
			cd.setImpType((int) bean.get("imptype"));
			cd.setStartTime((bean.get("starttime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("starttime"), "yyyy-MM-dd HH:mm:ss") : null);
			cd.setEndTime((bean.get("endtime")!=null) ? ToolUtil.getInstance().convertDateToString((Date) bean.get("endtime"), "yyyy-MM-dd HH:mm:ss") : null);
			cd.setDefaultGroup((int) bean.get("defaultgroup"));
			
			map.put(cd.getNbID(), cd);
		}
		return map;
	}
	
	public static Map<Integer, GroupDailyStatusModel> getGroupDailyStatus(List<DynaBean> rows) {
		Map<Integer, GroupDailyStatusModel> map = new HashMap<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
		
			GroupDailyStatusModel gd = new GroupDailyStatusModel();
			gd.setGroupInternalID((int) bean.get("groupinternalid"));
			gd.setRecDate(ToolUtil.getInstance().convertDateToString((Date) bean.get("recdate"), "yyyy-MM-dd"));
			gd.setStatus((int) bean.get("status"));
			gd.setTimeZone((int) bean.get("timezone"));
			
			map.put(gd.getGroupInternalID(), gd);
		}
		return map;
	}
	
	public static Map<String, EventModel> getUnsolvedOfflineEvent(List<DynaBean> rows) {
		Map<String, EventModel> map = new HashMap<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();

			EventModel event = new EventModel();
			event.setNbID((String) bean.get("nbid"));
			event.setBatteryID((Integer) bean.get("batteryid"));
			event.setEventType((Integer) bean.get("eventtype"));
			event.setEventStatus((Integer) bean.get("eventstatus"));
			event.setImpType((Integer) bean.get("imptype"));
			event.setDisconnect((Long) bean.get("disconnect"));
			event.setTimeZone((Integer) bean.get("timezone"));

			String key = event.getNbID()+"_"+event.getBatteryID();
			map.put(key, event);
		}
		return map;
	}

	public static Map<String, NBListModel> getNBList(List<DynaBean> rows) {
		Map<String, NBListModel> map = new HashMap<>();

		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();

			 NBListModel nbList = new NBListModel();
			 nbList.setNbID((String) bean.get("nbid"));
			 nbList.setGroupInternalID((int) bean.get("groupinternalid"));
			 nbList.setAllocate((int) bean.get("allocate"));
			 nbList.setActive((int) bean.get("active"));
			 
			 map.put(nbList.getNbID(), nbList);
		}
		return map;
	}
	
	public static List<NBListModel> getActiveNBList(List<DynaBean> rows) {
		List<NBListModel> list = new ArrayList<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			 DynaBean bean = (DynaBean)iter.next();
			 
			 NBListModel nbList = new NBListModel();
			 nbList.setNbID((String) bean.get("nbid"));
			 nbList.setGroupInternalID((int) bean.get("groupinternalid"));
			 nbList.setAllocate((int) bean.get("allocate"));
			 nbList.setActive((int) bean.get("active"));
			 
			 list.add(nbList);
		}
		return list;
	}
}
