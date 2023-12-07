package aptg.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.bean.BatteryGroupNBListBean;
import aptg.bean.DelNBIDBean;
import aptg.handle.chgconf.WebDelNBIDHandle;
import aptg.utils.DBQueryUtil;

public class NBGroupManager {

	private static final Logger logger = LogManager.getFormatterLogger(WebDelNBIDHandle.class.getName());

	private Map<String, BatteryGroupNBListBean> groupNBListMap = new HashMap<>();	// key: nbID
	
	private static NBGroupManager instances;
	public static NBGroupManager getInstance() {
		if (instances==null) {
			synchronized (NBGroupManager.class) {
				if (instances==null) {
					instances = new NBGroupManager();
				}
			}
		}
		return instances;
	}

	/**
	 * init
	 */
	public void init() {
		List<BatteryGroupNBListBean> list = DBQueryUtil.getInstance().queryAllBatteryGroupNBListActive();
		
		for (BatteryGroupNBListBean bgnb: list) {
			setBatteryGroupNBListMap(bgnb);
		}
	}
	private void setBatteryGroupNBListMap(BatteryGroupNBListBean bgnb) {
		synchronized (groupNBListMap) {
			groupNBListMap.put(bgnb.getNBID(), bgnb);
		}
	}
	
	public BatteryGroupNBListBean getBatteryGroupNBList(String nbID) {
		if (groupNBListMap.containsKey(nbID)) {
			return groupNBListMap.get(nbID);
		}
		return null;
	}
	
	public void updateCompany(BatteryGroupNBListBean bgnb) {
		setBatteryGroupNBListMap(bgnb);
	}
	
	public BatteryGroupNBListBean addBatteryGroupNBListActive(String nbID) {
		BatteryGroupNBListBean bgnb = DBQueryUtil.getInstance().queryBatteryGroupNBListActiveByNBID(nbID);
		if (bgnb!=null) {
			setBatteryGroupNBListMap(bgnb);
		}
		return bgnb;
	}
	
	public BatteryGroupNBListBean addBatteryGroupNBList(String nbID) {
		BatteryGroupNBListBean bgnb = DBQueryUtil.getInstance().queryBatteryGroupNBListByNBID(nbID);
		if (bgnb!=null) {
			setBatteryGroupNBListMap(bgnb);
		}
		return bgnb;
	}
	
	/**
	 * 	停用or刪除
	 * 
	 * @param nbID
	 */
	public void stopNBList(String nbID) {
		if (groupNBListMap.containsKey(nbID)) {
			synchronized (groupNBListMap) {
				groupNBListMap.remove(nbID);	
			}
		}
	}

	public void removeBatteryGroupNBList(List<DelNBIDBean> list) {
		Set<String> set = groupNBListMap.keySet();
		Iterator<String> itr = set.iterator();
		while (itr.hasNext()) {
			String nbID = (String) itr.next();
			
			DelNBIDBean del = list.stream().filter(obj -> nbID.equals(obj.getNBID())).findAny().orElse(null);
			if (del!=null) {
				synchronized (groupNBListMap) {
					itr.remove();
					logger.info("Remove Battery cache, NBID=["+nbID+"]");
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
