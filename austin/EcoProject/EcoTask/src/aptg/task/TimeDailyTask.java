package aptg.task;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.dao.ElectricityTimeDailyDao;
import aptg.models.ElectricityTimeDailyModel;
import aptg.models.ElectricityTimeModel;
import aptg.models.OPDayListModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.ToolUtil;

/**
 * 每日記算所有RatePlan前一日是否為假日、及其PK, SP, SatSP, OP時間
 * 
 * @author austinchen
 *
 */
public class TimeDailyTask {

	private static final String CLASS_NAME = TimeDailyTask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

	private List<ElectricityTimeDailyModel> insertList = new ArrayList<>();
	private List<ElectricityTimeDailyModel> updateList = new ArrayList<>();

	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");
		
		TimeDailyTask task = new TimeDailyTask();

		Calendar startCal = ToolUtil.getInstance().setDateEndTime(Calendar.getInstance());
		
		Calendar end = Calendar.getInstance();
		end.setTime(ToolUtil.getInstance().getInitYesterday());	// 前一天00:00:00
		Calendar endCal = ToolUtil.getInstance().setDateEndTime(end);	// 時間由 00:00:00 設成 23:59:00
		
		if (args.length==0) {
			/*
			 * 	正常流程
			 */
			startCal.add(Calendar.DATE, -1);	// 計算前一天

			for ( ; startCal.compareTo(endCal)<=0 ; startCal.add(Calendar.DATE, 1)) {
				logger.info("統計 RecDate '"+ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd")+"' ElectricityTimeDaily info");
				task.timeDaily(startCal.getTime());
			}
			
			/*
			 * 檢查是否更新次年資料
			 */
			task.checkNextYear();
			
		} else {
			String specifyDate = args[0];
			Date calculateDate = ToolUtil.getInstance().convertStringToDate(specifyDate, "yyyy-MM-dd");
			startCal.setTime(calculateDate);	// 指定日期
			startCal = ToolUtil.getInstance().setDateEndTime(startCal);

			String deviceID = (args.length>=2) ? args[1] : null;

			/*
			 *	想算到哪天就寫到哪天, ex: 2020-11-17 (含)
			 */
			String endDate = (args.length>=3) ? args[2] : null;
			if (StringUtils.isNotBlank(endDate)) {
				// 想算到哪天就寫到哪天, ex: 2020-11-17 (含)
				Date calculateEndDate = ToolUtil.getInstance().convertStringToDate(endDate, "yyyy-MM-dd");
				endCal.setTime(calculateEndDate);
				endCal = ToolUtil.getInstance().setDateEndTime(endCal);
			}

			logger.info("重新統計 RecDate '"+ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd")+"' ~ '"+
											ToolUtil.getInstance().convertDateToString(endCal.getTime(), "yyyy-MM-dd")+"' ElectricityTimeDaily info");
			for (Calendar cal=startCal ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
				logger.info("統計 RecDate '"+ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd")+"' ElectricityTimeDaily info");
				task.timeDaily(cal.getTime());
			}
		}

		task.updateInsertElectricityTime();
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	private void timeDaily(Date date) {
		int month = ToolUtil.getInstance().getMonth(date);
		boolean isSummer = ToolUtil.getInstance().isSummer(month);
		int dayOfweek = ToolUtil.getInstance().getDayOfWeek(date);
		
		// 依summer & dayofweek 撈取定義
		List<ElectricityTimeModel> list = DBQueryUtil.getInstance().queryElectricityTime((isSummer)?1:0, dayOfweek);
		for (ElectricityTimeModel et: list) {
			String recDate = ToolUtil.getInstance().convertDateToString(date, "yyyy-MM-dd");
			
			ElectricityTimeDailyModel daily = new ElectricityTimeDailyModel();
			if (isOPDay(date)) {
				// 國定假日
				daily.setRecDate(recDate);
				daily.setUsuallyHour(BigDecimal.ZERO);
				daily.setSPHour(BigDecimal.ZERO);
				daily.setSatSPHour(BigDecimal.ZERO);
				daily.setOPHour(new BigDecimal(24));
				
			} else {
				// 非國定假日
				daily.setRecDate(recDate);
				daily.setUsuallyHour(et.getUsuallyHour());
				daily.setSPHour(et.getSPHour());
				daily.setSatSPHour(et.getSatSPHour());
				daily.setOPHour(et.getOPHour());
			}
			
			boolean isExist = DBQueryUtil.getInstance().isExistElectricityTimeDaily(recDate);
			if (!isExist) {
				insertList.add(daily);
			} else {
				updateList.add(daily);
			}
		}
	}
	
	public void checkNextYear() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, 1);
		int nextYear = now.get(Calendar.YEAR);
		
		// select OPDayList 
		List<OPDayListModel> opdayList = DBQueryUtil.getInstance().queryOPDayList(nextYear);
		logger.info("檢查 OPDayList Year=["+nextYear+"], OPDayList size: "+opdayList.size());
		
		// if opdayList 有下一年的新資料 => 檢查 ElectricityTimeDaily 是否也有資料了
		if (opdayList.size()!=0) {
			// 檢查ElectricityTimeDaily 是否有下一年的新資料
			List<ElectricityTimeDailyModel> timeList = DBQueryUtil.getInstance().queryElectricityTimeDaily(nextYear);
			logger.info("檢查 ElectricityTimeDaily Year=["+nextYear+"], ElectricityTimeDaily size: "+timeList.size());
			if (timeList.size()==0) {
				// 無下一年資料 => 計算下一年整年度
				Calendar startCal = Calendar.getInstance();
				startCal.add(Calendar.YEAR, 1);
				startCal.set(Calendar.MONTH, 0);
				startCal.set(Calendar.DAY_OF_MONTH, 1);

				Calendar endCal = Calendar.getInstance();
				endCal.add(Calendar.YEAR, 1);
				endCal.set(Calendar.MONTH, 11);
				endCal.set(Calendar.DAY_OF_MONTH, 31);

				logger.info("統計次年 RecDate '"+ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd")+"' ~ '"+
												ToolUtil.getInstance().convertDateToString(endCal.getTime(), "yyyy-MM-dd")+"' ElectricityTimeDaily info");
				for (Calendar cal=startCal ; cal.compareTo(endCal)<=0 ; cal.add(Calendar.DATE, 1)) {
					logger.info("統計 RecDate '"+ToolUtil.getInstance().convertDateToString(cal.getTime(), "yyyy-MM-dd")+"' ElectricityTimeDaily info");
					timeDaily(cal.getTime());
				}
			}
		}
	}
	
	
	/**
	 * 查詢此日是否為假日(離峰日)
	 * 
	 * @param recDate
	 * @return
	 */
	private boolean isOPDay(Date date) {
		String opday = ToolUtil.getInstance().convertDateToString(date, "yyyy-MM-dd");
		OPDayListModel op = DBQueryUtil.getInstance().queryOPDay(opday);
		
		if (op!=null)
			return true;
		else
			return false;
	}
	
	private void updateInsertElectricityTime() {
		try {
			ElectricityTimeDailyDao dao = new ElectricityTimeDailyDao(); 
			
			if (insertList.size()!=0) {
				dao.insertElectricityTimeDaily(insertList);
			}

			if (updateList.size()!=0) {
				dao.updateElectricityTimeDaily(updateList);
			}

			insertList.clear();
			updateList.clear();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
