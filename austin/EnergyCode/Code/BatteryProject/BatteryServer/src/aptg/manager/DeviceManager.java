package aptg.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.bean.DelNBIDBean;
import aptg.handle.chgconf.WebDelNBIDHandle;
import aptg.model.BatteryModel;
import aptg.utils.DBQueryUtil;

public class DeviceManager {
	
	private static final Logger logger = LogManager.getFormatterLogger(WebDelNBIDHandle.class.getName());
	
	private Map<String, BatteryModel> nbbattMap = new HashMap<>();	// key: nbID+"_"+batteryID, value: BatteryModel

	private static final String KEY_SPLIT = "_";
	
	private static DeviceManager instances;
	public static DeviceManager getInstance() {
		if (instances==null) {
			synchronized (DeviceManager.class) {
				if (instances==null) {
					instances = new DeviceManager();
				}
			}
		}
		return instances;
	}
	
	/**
	 * 新增電池
	 * 
	 * @param model
	 */
	public void addBattery(BatteryModel model) {
		// insert into Battery
		DBQueryUtil.getInstance().insertBattery(model);
		
		// update cache
		String key = model.getNbID() +KEY_SPLIT+ model.getBatteryID();
		setNBbattMap(key, model);
	}
	
	/**
	 * 更新電池IRRecords, VRecords, TRecords value
	 * 
	 * @param model
	 */
	public void updBatteryRecords(BatteryModel model) {
		// update into Battery
		DBQueryUtil.getInstance().updateBatteryRecords(model);
		
		// update cache
		String key = model.getNbID() +KEY_SPLIT+ model.getBatteryID();
		setNBbattMap(key, model);
	}
	
	/**
	 * 依NBID移除所有battery
	 * 
	 * @param nbid
	 */
//	public void removeBattery(List<DelNBIDBean> list) {
//		for (DelNBIDBean bean: list) {
//			String nbID = bean.getNBID();
//			boolean isMatch = nbbattMap.keySet().stream().anyMatch(mapKey -> mapKey.contains(nbID));
//			if (isMatch) {
//				synchronized (nbbattMap) {
//					nbbattMap.remove(nbID);	
//				}
//			}
//		}
//	}
	public void removeBattery(List<DelNBIDBean> list) {
		Set<String> set = nbbattMap.keySet();
		Iterator<String> itr = set.iterator();
		while (itr.hasNext()) {
			String nbidbatt = (String) itr.next();
			
			DelNBIDBean del = list.stream().filter(obj -> nbidbatt.contains(obj.getNBID())).findAny().orElse(null);
			if (del!=null) {
				synchronized (nbbattMap) {
					itr.remove();
					logger.info("Remove Battery cache, NBID & BatteryID=["+nbidbatt+"]");
				}
			}
		}
	}

	/**
	 * 以電池組ID(通訊板序號+_+電池序號)查詢是否已有Battery紀錄
	 * @param nbIDbattID
	 * @return
	 */
	public BatteryModel getBattery(String nbIDbattID) {
		if (nbbattMap.containsKey(nbIDbattID)) {
			return nbbattMap.get(nbIDbattID);
		}
		return null;
	}
	
	/**
	 * init
	 */
	public void init() {
		List<BatteryModel> list = DBQueryUtil.getInstance().queryBattery();
		for (BatteryModel model: list) {
			String nbid = model.getNbID();
			int batteryID = model.getBatteryID();
			
			String key = nbid +KEY_SPLIT+ batteryID;

			// 查是否存在使用
			setNBbattMap(key, model);
		}
	}
	private void setNBbattMap(String key, BatteryModel model) {
		synchronized (nbbattMap) {
			nbbattMap.put(key, model);	
		}
	}
}
