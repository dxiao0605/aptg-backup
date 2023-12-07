package aptg.task;

import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.models.BankInfModel;
import aptg.models.FcstChargeModel;
import aptg.models.KPIModel;
import aptg.models.PowerRecordCollectionModel;
import aptg.utils.DBQueryUtil;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 計算上個月的KPI值(UnitPriceKPIavg, EUIKPIavg, EPUIKPIavg, AirKPIavg)
 * 
 * @author austinchen
 *
 */
public class KPITask {

	private static final String CLASS_NAME = KPITask.class.getName();
	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);
	
	private static final BigDecimal To_Percent = new BigDecimal(100);

	public static void main(String[] args) {
		logger.info(CLASS_NAME+" Task Start !");

		KPITask task = new KPITask();

		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.MONTH, -1);	// 上個月
		startCal.set(Calendar.DATE, 1);
		String startdate = ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyy-MM-dd");
		
		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.DATE, 1);
		String enddate = ToolUtil.getInstance().convertDateToString(endCal.getTime(), "yyyy-MM-dd");
		
		String useMonth = ToolUtil.getInstance().convertDateToString(startCal.getTime(), "yyyyMM");
		logger.info("計算 "+useMonth+" 月 KPI");
		task.calculateLastMonthKPI(startdate, enddate, useMonth);
		
		logger.info(CLASS_NAME+" Task Finish !" + "\r\n");
	}
	
	private void calculateLastMonthKPI(String startdate, String enddate, String useMonth) {
		FcstChargeModel fcst = DBQueryUtil.getInstance().queryFcstKPIinfo(startdate, enddate);
		BankInfModel bank =	DBQueryUtil.getInstance().queryBankKIPinfo(useMonth);
		PowerRecordCollectionModel col = DBQueryUtil.getInstance().queryCollectionKPIinfo(startdate, enddate);
		
		// 電費單價上月平均值
		BigDecimal unitPrice = unitPriceKPIavg(fcst);
		
		// EUI上月平均值
		BigDecimal eui = euiKPIavg(fcst, bank);
		
		// EPUI上月平均值
		BigDecimal epui = epuiKPIavg(fcst, bank);
		
		// 主要空調佔比上月平均值
		BigDecimal air = airKPIavg(fcst, col);
		
		KPIModel kpi = new KPIModel();
		kpi.setUnitPriceKPIavg(unitPrice);
		kpi.setEUIKPIavg(eui);
		kpi.setEPUIKPIavg(epui);
		kpi.setAirKPIavg(air);

//		logger.info("UnitPriceKPIavg="+unitPrice);
//		logger.info("EUIKPIavg="+eui);
//		logger.info("EPUIKPIavg="+epui);
//		logger.info("AirKPIavg="+air);
		logger.info("KPI value: "+ JsonUtil.getInstance().convertObjectToJsonstring(kpi));
		DBQueryUtil.getInstance().updateKPIinfo(kpi);
	}
	
	/*
	 *	電費單價上月平均值
	 * 
	 *	分子: 上月最後一筆所有分行總電價 (FcstCharge.TotalCharge)
 	 *	分母: 所有分行總用電用電量 (TPMCEC)
 	 *
	 *	四捨五入到小數點第一位
	 */
	private BigDecimal unitPriceKPIavg(FcstChargeModel fcst) {
		BigDecimal totalCharge = fcst.getTotalCharge();
		BigDecimal tpmcec = fcst.getTPMCEC();
		
		logger.info("@@@@@@@@@ totalCharge:"+totalCharge+", tpmcec:"+tpmcec);
		BigDecimal avg = BigDecimal.ZERO;
		if (tpmcec.compareTo(BigDecimal.ZERO)>0)
			avg = totalCharge.divide(tpmcec, 1, BigDecimal.ROUND_HALF_UP);
		return avg;
	}
	
	/*
	 *	EUI上月平均值
	 * 
	 *	分子: 為統計上個月所有分行總用電 電表的實際用電量總和 (所有分行總用電用電量(TPMCEC))
 	 *	分母: 為所有分行面積總和 （不排除任何分行）
 	 *
	 *	四捨五入到小數點第一位
	 */
	private BigDecimal euiKPIavg(FcstChargeModel fcst, BankInfModel bank) {
		BigDecimal tpmcec = fcst.getTPMCEC();
		BigDecimal area = bank.getArea();

		logger.info("@@@@@@@@@ tpmcec:"+tpmcec+", area:"+area);
		
		BigDecimal avg = tpmcec.divide(area, 1, BigDecimal.ROUND_HALF_UP);
		return avg;
	}
	
	/*
	 *	EPUI上月平均值
	 * 
	 *	分子: 為統計上個月所有分行總用電 電表的實際用電量總和
 	 *	分母: 為所有分行的人數總和（不排除任何分行）
 	 *
	 *	四捨五入到小數點第一位
	 */
	private BigDecimal epuiKPIavg(FcstChargeModel fcst, BankInfModel bank) {
		BigDecimal tpmcec = fcst.getTPMCEC();
		BigDecimal people = new BigDecimal(bank.getPeople());

		logger.info("@@@@@@@@@ tpmcec:"+tpmcec+", people:"+people);
		
		BigDecimal avg = tpmcec.divide(people, 1, BigDecimal.ROUND_HALF_UP);
		return avg;
	}
	
	/*
	 *	主要空調佔比上月平均值
	 *	
	 *	分子: 為統計所有電表為主要空調用電的上月用電量 (耗能分類為主要空調的PowerRecordCollection.CEC)
	 *	分母: 為統計所有分行總用電 電表的上月用電量
	 *
	 *	平均值計算 四捨五入到小數點第二位  （畫面 百分比呈現到整數位)
	 */
	private BigDecimal airKPIavg(FcstChargeModel fcst, PowerRecordCollectionModel col) {
		BigDecimal cec = col.getCEC();
		BigDecimal tpmcec = fcst.getTPMCEC();

		logger.info("@@@@@@@@@ cec:"+cec+", tpmcec:"+tpmcec);
		BigDecimal avg = BigDecimal.ZERO;
		if (tpmcec.compareTo(BigDecimal.ZERO)>0) {
			avg = cec.divide(tpmcec, 2, BigDecimal.ROUND_HALF_UP);
			avg = avg.multiply(To_Percent);
		}
		return avg;
	}
}
