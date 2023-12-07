package aptg.fixdata;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.fixdata.model.RecordCompare2;

public class ContentDetail {

	public static Map<String, RecordCompare2> getCompare2(List<DynaBean> rows) {
		Map<String, RecordCompare2> map = new HashMap<>();
		
		Iterator<DynaBean> iter = rows.iterator();
		while (iter.hasNext()) {
			DynaBean bean = (DynaBean)iter.next();
			
			RecordCompare2 record = new RecordCompare2();
			record.setDeviceID((String) bean.get("deviceid"));
			record.setMinRecTime( ((Date) bean.get("minrectime")!=null) ? (Date) bean.get("minrectime") : null );
			record.setNewTime( ((Date) bean.get("newtime")!=null) ? (Date) bean.get("newtime") : null );
			
			map.put(record.getDeviceID().toUpperCase(), record);
		}
		return map;
	}
}
