package aptg.manager;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.dao.ElectricityPriceDao;
import aptg.models.ElectricityPriceModel;
import aptg.utils.DBContentDealUtil;
import aptg.utils.ToolUtil;

public class ElectricityPriceManager {

	private Map<Integer, Map<Date, ElectricityPriceModel>> priceHistory = new HashMap<>();	// Map<Date, ElectricityPriceModel> => key: year+month=yyyy-MM
//	private Map<Integer, ElectricityPriceModel> priceMap = new HashMap<>();						// key: RatePlanCode
	
	private static ElectricityPriceManager instances;
	private ElectricityPriceManager() {
		init();
	}
	
	public static ElectricityPriceManager getInstance() {
		if (instances==null) {
			synchronized (ElectricityPriceManager.class) {
				if (instances==null) {
					instances = new ElectricityPriceManager();
				}
			}
		}
		return instances;
	}

//	/**
//	 *	撈取依RatePlanCode的電價表
//	 */
//	private void init() {
//		try {
//			ElectricityPriceDao dao = new ElectricityPriceDao();
//			List<DynaBean> rows = dao.queryElectricityPrice();
//			if (rows.size()!=0) {
//				priceMap = DBContentDealUtil.getElectricityPrice(rows);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 *	撈取依RatePlanCode的電價表
	 */
	private void init() {
		try {
			ElectricityPriceDao dao = new ElectricityPriceDao();
			List<DynaBean> rows = dao.queryElectricityPrice();
			if (rows.size()!=0) {
				List<ElectricityPriceModel> list = DBContentDealUtil.getElectricityPriceList(rows);	// 所有的ElectricityPrice
				for (ElectricityPriceModel ep: list) {
					int year = Integer.valueOf(ep.getYear());
					int month = Integer.valueOf(ep.getMonth());
					int ratePlanCode = ep.getRatePlanCode();
					
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, year);
					cal.set(Calendar.MONTH, month-1);
					String startTime = ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM");
					
					if (priceHistory.containsKey(ratePlanCode)) {
						Map<Date, ElectricityPriceModel> priceMap = priceHistory.get(ratePlanCode);
						priceMap.put(ToolUtil.getInstance().convertStringToDate(startTime, "yyyy-MM"), ep);
					} else {
						Map<Date, ElectricityPriceModel> priceMap = new HashMap<>();
						priceMap.put(ToolUtil.getInstance().convertStringToDate(startTime, "yyyy-MM"), ep);
						priceHistory.put(ratePlanCode, priceMap);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * 	依ratePlanCode取得電價表
//	 * 
//	 * @param ratePlanCode
//	 * @return
//	 */
//	public ElectricityPriceModel getPrice(int ratePlanCode) {
//		if (priceMap.containsKey(ratePlanCode)) {
//			return priceMap.get(ratePlanCode);
//		}
//		return null;
//	}
	/**
	 * 	依ratePlanCode取得電價表
	 * 
	 * @param recTime: PowerRecord的RecTime, 格式: yyyy-MM
	 * @param ratePlanCode
	 * @return
	 */
	public ElectricityPriceModel getPrice(String useTime, int ratePlanCode) {
		Date rectime = ToolUtil.getInstance().convertStringToDate(useTime, "yyyy-MM"); // ex: rectime="2020-09"

		if (priceHistory.containsKey(ratePlanCode)) {
			Map<Date, ElectricityPriceModel> ep = priceHistory.get(ratePlanCode);

			Date tmpDate = null;
			for (Date date: ep.keySet()) {
				// ex: date="2020-03"
				if (tmpDate==null && !rectime.before(date)) {
					// recTime >= ElectricityPrice的啟用日year+month (yyyy-MM)
					// "2020-09" >= "2020-03"
					tmpDate = date;	// tmpDate="2020-03"
				}
				else if (tmpDate!=null) {
					// ex: date="2020-01", tmpDate="2020-03"
					// "2020-09" >= "2020-01" 但 "2020-01"需>"2020-03"
					if (!rectime.before(date) && date.after(tmpDate)) {
						// recTime >= ElectricityPrice的year+month (yyyy-MM)
						tmpDate = date;	// tmpDate="2020-03"
					}
				}
			}
			if (ep.containsKey(tmpDate)) {
				return ep.get(tmpDate);
			}
		}
		return null;
	}
}
