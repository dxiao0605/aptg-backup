package aptg.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import aptg.beans.FcstCollectionBean;
import aptg.beans.MaxMinCCBean;
import aptg.beans.MaxUsuallyCCBean;
import aptg.dao.BGTaskDao;
import aptg.dao.BankInfDao;
import aptg.dao.BestCCDao;
import aptg.dao.BestRatePlanDao;
import aptg.dao.ControllerSetupDao;
import aptg.dao.ElectricityTimeDailyDao;
import aptg.dao.ElectricityTimeDao;
import aptg.dao.FcstChargeDao;
import aptg.dao.KPIDao;
import aptg.dao.MeterEventTxDao;
import aptg.dao.MeterSetupDao;
import aptg.dao.OPDayDao;
import aptg.dao.PowerAccountDao;
import aptg.dao.PowerAccountHistoryDao;
import aptg.dao.PowerMonthDao;
import aptg.dao.PowerRecordCollectionDao;
import aptg.dao.PowerRecordDao2;
import aptg.dao.RepriceTaskDao;
import aptg.models.BGTaskModel;
import aptg.models.BankInfModel;
import aptg.models.BestRatePlanModel;
import aptg.models.ControllerSetupModel;
import aptg.models.ElectricityTimeDailyModel;
import aptg.models.ElectricityTimeModel;
import aptg.models.FcstChargeModel;
import aptg.models.KPIModel;
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
import aptg.utils.DBContentDealUtil;

public class DBQueryUtil {

	private static DBQueryUtil instances;
	
	private DBQueryUtil() {}
	
	public static DBQueryUtil getInstance() {
		if (instances==null) {
			synchronized (DBQueryUtil.class) {
				if (instances==null) {
					instances = new DBQueryUtil();
				}
			}
		}
		return instances;
	}

	/* ======================================================
	 * 							電號
	 * ====================================================== */
	/**
	 * 	取得所有的電號
	 * 
	 * @return
	 */
	public List<PowerAccountModel> getAllPowerAccount() {
		List<PowerAccountModel> list = new ArrayList<>();
		try {
			PowerAccountDao dao = new PowerAccountDao();
			List<DynaBean> rows = dao.queryPowerAccount();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getPowerAccountList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 	查詢指定電號
	 * 
	 * @param powerAccount
	 * @return
	 */
	public PowerAccountModel getPowerAccount(String powerAccount) {
		try {
			PowerAccountDao dao = new PowerAccountDao();
			List<DynaBean> rows = dao.queryPowerAccount(powerAccount);
			if (rows.size()!=0) {
				PowerAccountModel pa = DBContentDealUtil.getPowerAccount(rows);
				return pa;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* ======================================================
	 * 					ControllerSetup (ECO5Account)
	 * ====================================================== */
	public List<ControllerSetupModel> getAllEnabledECO5Account() {
		List<ControllerSetupModel> list = new ArrayList<>();
		try {
			ControllerSetupDao dao = new ControllerSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledECO5Account();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getEnabledController(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<MeterSetupModel> getAllEnabledECO5AccountMeter() {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			ControllerSetupDao dao = new ControllerSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledECO5AccountMeter();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* ======================================================
	 * 							電表
	 * ====================================================== */
	/**
	 * 	取得所有enabled的Meter
	 * 
	 * @return
	 */
	public List<MeterSetupModel> getAllEnabledMeter() {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledMeter();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public MeterSetupModel getMeterSetup(String deviceID) {
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryEnabledMeter(deviceID);
			if (rows.size()!=0) {
				List<MeterSetupModel> list = DBContentDealUtil.getMeterSetup(rows);
				for (MeterSetupModel meter: list) {
					return meter;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<MeterSetupModel> getEnabledMeterSetupUsageCode1() {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledMeterUsageCode1();
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<MeterSetupModel> getEnabledMeterSetupUsageCode1(String powerAccount) {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledMeterUsageCode1(powerAccount);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<MeterSetupModel> getEnabledMeterSetup(String powerAccount) {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryAllEnabledMeter(powerAccount);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/* ======================================================
	 * 						PowerRecord
	 * ====================================================== */
	/**
	 * 	取得指定日期的第2筆的PowerRecord
	 * 
	 * @param deviceID
	 * @param recDate
	 * @return
	 */
	public PowerRecordModel getSpecifyDateSecondRecord(String deviceID, String startdate, String enddate) {
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.getSpecifyDateSecondRecord(deviceID, startdate, enddate);
			if (rows.size()!=0) {
				List<PowerRecordModel> records = DBContentDealUtil.getPowerRecords(rows);
				if (records.size()==2) {
					return records.get(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("############## rows.size: "+rows.size()+", record date: "+ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd")+", record: "+JsonUtil.getInstance().convertObjectToJsonstring(record));
		return null;
	}
	/**
	 * 	取得指定日期的最後一筆的PowerRecord
	 * 
	 * @param deviceID
	 * @param recDate
	 * @return
	 */
	public PowerRecordModel getSpecifyDateLastRecord(String deviceID, String startdate, String enddate) {
		// 指定日最後一筆PowerRecord
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.getSpecifyDateLastRecord(deviceID, startdate, enddate);
			if (rows.size()!=0) {
				PowerRecordModel record = DBContentDealUtil.getLastPowerRecord(rows);
				return record;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public PowerRecordModel getBeforeSpecifyDateNewestRecord(String deviceID, Date recDate) {
		// 指定日最後一筆PowerRecord
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.querySpecifyDateNewestRecord(deviceID, recDate);
			if (rows.size()!=0) {
				PowerRecordModel record = DBContentDealUtil.getLastPowerRecord(rows);
				return record;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("############## rows.size: "+rows.size()+", record date: "+ToolUtil.getInstance().convertDateToString(recDate, "yyyy-MM-dd")+", record: "+JsonUtil.getInstance().convertObjectToJsonstring(record));
		return null;
	}
	/**
	 * 	查詢device在欲計算日的資料
	 * ex: 現為8/20，程式執行時，整理計算8/19之資料
	 */
//	public List<PowerRecordModel> getRecdateList(String deviceID, String startdate, String enddate) {
//		List<PowerRecordModel> list = new ArrayList<>();
//		try {
//			PowerRecordDao2 dao = new PowerRecordDao2();
//			List<DynaBean> rows = dao.queryDailyCollection(deviceID, startdate, enddate);
//			
//			if (rows.size()!=0) {
//				list = DBContentDealUtil.getDeviceMinRectimePowerRecordList(rows);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	public List<PowerRecordModel> getRecdateList(String startdate, String enddate) {
		List<PowerRecordModel> list = new ArrayList<>();
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.queryDailyCollection(startdate, enddate);
			
			if (rows.size()!=0) {
				list = DBContentDealUtil.getDeviceMinRectimePowerRecordList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * version 1
	 */
//	public List<PowerRecordMaxDemandModel> getRecordDemandList(String deviceID, String startdate, String enddate, Boolean isJob) {
//		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
//		try {
//			PowerRecordDao2 dao = new PowerRecordDao2();
//			List<DynaBean> rows = new ArrayList<>();
//			
//			if (isJob)
//				rows = dao.queryRecordDemand_Daily(deviceID, startdate, enddate);
//			else
//				rows = dao.queryRecordDemand(deviceID, startdate, enddate);
//			
//			if (rows.size()!=0) {
//				list = DBContentDealUtil.getPowerRecordMaxDemandList(rows);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	/*
	 * version 2
	 */
	public List<PowerRecordMaxDemandModel> getMaxRecordDemandList(String deviceID, String startdate, String enddate) {
		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.queryMaxRecordDemand(deviceID, startdate, enddate);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getPowerRecordMaxDemandList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<PowerRecordMaxDemandModel> getRecordDemandList(String powerAccount, String startdate, String enddate) {
		List<PowerRecordMaxDemandModel> list = new ArrayList<>();
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.queryRecordDemand(powerAccount, startdate, enddate);
			
			if (rows.size()!=0) {
				list = DBContentDealUtil.getPowerRecordDemandList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<PowerRecordModel> queryRecordGroupbyDeviceWithPartition(String startdate, String enddate) {
		List<PowerRecordModel> list = new ArrayList<>();
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.queryRecordGroupbyDeviceWithPartition(startdate, enddate);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getDeviceMinRectimePowerRecordList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<PowerRecordModel> queryBasicIVW(String deviceID, String startdate, String enddate) {
		List<PowerRecordModel> list = new ArrayList<>();
		try {
			PowerRecordDao2 dao = new PowerRecordDao2();
			List<DynaBean> rows = dao.queryBasicIVW(deviceID, startdate, enddate);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBasicPowerRecord(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public PowerAccountHistoryModel queryCollectionHistoryCC(String deviceID, String recDate) {
		try {
			PowerAccountHistoryDao dao = new PowerAccountHistoryDao();
			List<DynaBean> rows = dao.queryCollectionHistoryCC(deviceID, recDate);

			if (rows.size()!=0) {
				PowerAccountHistoryModel model = DBContentDealUtil.getPowerAccountHistory(rows);
				return model;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* ======================================================
	 * 						FcstCharge
	 * ====================================================== */
	/**
	 * 	查詢是否已有FcstCharge紀錄
	 * 
	 * @param fcst
	 * @return
	 */
	public boolean queryFcstCharge(FcstChargeModel fcst) {
		try {
			FcstChargeDao dao = new FcstChargeDao();
			List<DynaBean> rows = dao.queryFcstCharge(fcst.getPowerAccount(), fcst.getUseMonth(), fcst.getUseTime(), fcst.getRatePlanCode());
			if (rows.size()!=0)
				return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * FcstCharge時間最大一筆紀錄 key: powerAccount
	 * 
	 * @param month
	 * @return
	 */
	public Map<String, FcstChargeModel> getMaxUseTimeFcstCharge(int year, int month) {
		Map<String, FcstChargeModel> fcMap = new HashMap<>();
		try {
			FcstChargeDao dao = new FcstChargeDao();
			List<DynaBean> rows = dao.queryMaxUseTimeFcstCharge(year, month);
			if (rows.size()!=0) {
				fcMap = DBContentDealUtil.getFcstChargeModel(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fcMap;
	}
	

	/* ======================================================
	 * 						PowerMonth
	 * ====================================================== */
	public Map<String, PowerMonthModel> getPowerMonth(String useMonth) {
		Map<String, PowerMonthModel> pmMap = new HashMap<>();
		try {
			PowerMonthDao dao = new PowerMonthDao();
			List<DynaBean> rows = dao.queryByPowerMonth(useMonth);
			if (rows.size()!=0) {
				pmMap = DBContentDealUtil.getPowerMonth(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pmMap;
	}


	/* ======================================================
	 * 						BestRatePlan
	 * ====================================================== */
	/**
	 * 	查詢是否已有最適電費紀錄
	 * 
	 * @param powerAccount
	 * @param useMonth
	 * @param ratePlanCode
	 * @return
	 */
	public BestRatePlanModel getBestRatePlan(String powerAccount, String useMonth, int ratePlanCode) {
		try {
			BestRatePlanDao dao = new BestRatePlanDao();
			List<DynaBean> rows = dao.queryBestRatePlan(powerAccount, useMonth, ratePlanCode);
			if (rows.size()!=0) {
				BestRatePlanModel best = DBContentDealUtil.getBestRatePlan(rows);
				return best;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/* ======================================================
	 * 						BestCC
	 * ====================================================== */
	public boolean isBestCCExist(String powerAccount, String useMonth, int ratePlanCode, int usuallyCC) {
		try {
			BestCCDao dao = new BestCCDao();
			List<DynaBean> rows = dao.queryBestCC(powerAccount, useMonth, ratePlanCode, usuallyCC);
			if (rows.size()!=0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public MaxMinCCBean queryMaxMinCCByPowerAccount(String powerAccount) {
		try {
			BestCCDao dao = new BestCCDao();
			List<DynaBean> rows = dao.queryMaxMinCCByPowerAccount(powerAccount);
			if (rows.size()!=0) {
				MaxMinCCBean bean = DBContentDealUtil.getMaxMinCCByPowerAccount(rows);
				return bean;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public List<MaxUsuallyCCBean> queryMaxUsuallyCCgroupbyRatePlanCode(String powerAccount, String useMonth) {
//		List<MaxUsuallyCCBean> list = new ArrayList<>();
//		try {
//			BestCCDao dao = new BestCCDao();
//			List<DynaBean> rows = dao.queryMaxUsuallyCCgroupbyRatePlanCode(powerAccount, useMonth);
//			if (rows.size()!=0) {
//				list = DBContentDealUtil.getMaxUsuallyCCgroupbyRatePlanCode(rows);
//			}
//		} catch(SQLException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	
	public List<MaxUsuallyCCBean> queryAllMaxMinCC(String powerAccount) {
		List<MaxUsuallyCCBean> list = new ArrayList<>();
		try {
			BestCCDao dao = new BestCCDao();
			List<DynaBean> rows = dao.queryAllMaxMinCC(powerAccount);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMaxUsuallyCCgroupbyRatePlanCode(rows);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 *	撈取電號底下電表為enabeld, usagecode=1的powercollection
	 *
	 * @return
	 */
	public List<FcstCollectionBean> getFcstChargeByDeviceID(String recDate, String deviceID) {
		List<FcstCollectionBean> list = new ArrayList<>();
		try {
			PowerAccountHistoryDao dao = new PowerAccountHistoryDao();
			List<DynaBean> rows = dao.queryPowerAccountMeterByDeviceID(recDate, deviceID);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getFcstCharge(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	

	/* ======================================================
	 * 						BankInf
	 * ====================================================== */
	public FcstChargeModel queryFcstKPIinfo(String startdate, String enddate) {
		try {
			FcstChargeDao dao = new FcstChargeDao();
			List<DynaBean> rows = dao.queryKPIinfo(startdate, enddate);
			if (rows.size()!=0) {
				FcstChargeModel fcst = DBContentDealUtil.getFcstKPIinfo(rows);
				return fcst;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BankInfModel queryBankKIPinfo(String useMonth) {
		try {
			BankInfDao dao = new BankInfDao();
			List<DynaBean> rows = dao.queryBankKIPinfo(useMonth);
			if (rows.size()!=0) {
				BankInfModel bank = DBContentDealUtil.getBankInf(rows);
				return bank;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public PowerRecordCollectionModel queryCollectionKPIinfo(String startdate, String enddate) {
		try {
			PowerRecordCollectionDao dao = new PowerRecordCollectionDao();
			List<DynaBean> rows = dao.queryCollectionKPIinfo(startdate, enddate);
			if (rows.size()!=0) {
				PowerRecordCollectionModel collection = DBContentDealUtil.getCollectionKPIinfo(rows);
				return collection;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateKPIinfo(KPIModel kpi) {
		try {
			KPIDao dao = new KPIDao();
			dao.updateKPIinfo(kpi);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public OPDayListModel queryOPDay(String opday) {
		try {
			OPDayDao dao = new OPDayDao();
			List<DynaBean> rows = dao.queryOPDay(opday);
			if (rows.size()!=0) {
				OPDayListModel op = DBContentDealUtil.getOPDay(rows);
				return op;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<OPDayListModel> queryOPDayList(int year) {
		List<OPDayListModel> list = new ArrayList<>();
		try {
			OPDayDao dao = new OPDayDao();
			List<DynaBean> rows = dao.queryOPDayByYear(year);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getOPDayList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ElectricityTimeModel> queryElectricityTime(int summer, int dayOfweek) {
		List<ElectricityTimeModel> list = new ArrayList<>();
		try {
			ElectricityTimeDao dao = new ElectricityTimeDao();
			List<DynaBean> rows = dao.queryElectricityTime(summer, dayOfweek);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getElectricityTime(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public boolean isExistElectricityTimeDaily(String recDate) {
		try {
			ElectricityTimeDailyDao dao = new ElectricityTimeDailyDao();
			List<DynaBean> rows = dao.queryElectricityTimeDaily(recDate);
			if (rows.size()!=0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public ElectricityTimeDailyModel queryElectricityTimeDaily(String recDate) {
		try {
			ElectricityTimeDailyDao dao = new ElectricityTimeDailyDao();
			List<DynaBean> rows = dao.queryElectricityTimeDaily(recDate);
			if (rows.size()!=0) {
				ElectricityTimeDailyModel daily = DBContentDealUtil.getElectricityTimeDaily(rows);
				return daily;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<ElectricityTimeDailyModel> queryElectricityTimeDaily(int year) {
		List<ElectricityTimeDailyModel> list = new ArrayList<>();
		try {
			ElectricityTimeDailyDao dao = new ElectricityTimeDailyDao();
			List<DynaBean> rows = dao.queryElectricityTimeDaily(year);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getElectricityTimeDailyList(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ElectricityTimeDailyModel queryElectricityUseHour(String startdate, String enddate) {
		try {
			ElectricityTimeDailyDao dao = new ElectricityTimeDailyDao();
			List<DynaBean> rows = dao.queryElectricityUseHour(startdate, enddate);
					
			if (rows.size()!=0) {
				ElectricityTimeDailyModel daily = DBContentDealUtil.getElectricityTimeDaily(rows);
				if (daily.getRecDate()!=null) {
					return daily;	
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* ======================================================
	 * 						MeterEventTx
	 * ====================================================== */
	public Map<String, MeterEventTxModel> queryEventTx(int eventCode, int priority, int eventStatus) {
		Map<String, MeterEventTxModel> map = new HashMap<>();
		try {
			MeterEventTxDao dao = new MeterEventTxDao();
			List<DynaBean> rows = dao.queryEventTx(eventCode, priority, eventStatus);
			if (rows.size()!=0) {
				map = DBContentDealUtil.getMeterEventTx(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public List<MeterSetupModel> queryEnabledMeterByBankCode(String bankCode) {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryEnabledMeterByBankCode(bankCode);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<MeterSetupModel> queryEnabledMeterUsageCode1ByBankCode(String bankCode) {
		List<MeterSetupModel> list = new ArrayList<>();
		try {
			MeterSetupDao dao = new MeterSetupDao();
			List<DynaBean> rows = dao.queryEnabledMeterUsageCode1ByBankCode(bankCode);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getMeterSetup(rows);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<PowerAccountModel> queryPowerAccountByBankCode(String bankCode) {
		List<PowerAccountModel> list = new ArrayList<>();
		try {
			PowerAccountDao dao = new PowerAccountDao();
			List<DynaBean> rows = dao.queryPowerAccountByBankCode(bankCode);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getPowerAccountList(rows);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// ---------------------------------- BGTask ---------------------------------------------
	public List<BGTaskModel> queryBGTask(int statusCode) {
		List<BGTaskModel> list = new ArrayList<>();
		try {
			BGTaskDao dao = new BGTaskDao();
			List<DynaBean> rows = dao.queryTaskByStatus(statusCode);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getBGTask(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public void updateBGTaskStatus(List<BGTaskModel> list, int statusCode) {
		try {
			BGTaskDao dao = new BGTaskDao();
			dao.updateStatus(list, statusCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int deleteBGTaskInfo(String powerAccount) {
		int count = 0;
		try {
			BGTaskDao dao = new BGTaskDao();
			count = dao.delBGTaskInfo(powerAccount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	public int updateBGTaskInfo(String oldPowerAccount, String newPowerAccount, int modifyStatus) {
		int count = 0;
		try {
			BGTaskDao dao = new BGTaskDao();
			count = dao.updateBGTaskInfo(oldPowerAccount, newPowerAccount, modifyStatus);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// ---------------------------------- RepriceTask ---------------------------------------------
	public List<RepriceTaskModel> queryRepriceTask(int statusCode) {
		List<RepriceTaskModel> list = new ArrayList<>();
		try {
			RepriceTaskDao dao = new RepriceTaskDao();
			List<DynaBean> rows = dao.queryTaskByStatus(statusCode);
			if (rows.size()!=0) {
				list = DBContentDealUtil.getRepriceTask(rows);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public void updateRepriceTaskExecuting(List<RepriceTaskModel> list) {
		try {
			RepriceTaskDao dao = new RepriceTaskDao();
			dao.updateStatusCodeExecuting(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateRepriceTaskCancel(List<RepriceTaskModel> list) {
		try {
			RepriceTaskDao dao = new RepriceTaskDao();
			dao.updateStatusCodeCancel(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateRepriceTaskStatus(int statusCode, String deviceID, String powerAccount, String startDate) {
		try {
			RepriceTaskDao dao = new RepriceTaskDao();
			dao.updateRepriceTaskStatus(statusCode, deviceID, powerAccount, startDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteRepriceTaskInfo(RepriceTaskModel rp) {
		try {
			RepriceTaskDao dao = new RepriceTaskDao();
			dao.deleteRepriceTaskInfo(rp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateMeterSetupRepriceStatus(String deviceID, int repriceStatus) {
		try {
			MeterSetupDao dao = new MeterSetupDao();
			dao.updateRepriceStatus(deviceID, repriceStatus);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updatePowerAccountModifyStatus(String powerAccount, int modifyStatus) {
		try {
			PowerAccountDao dao = new PowerAccountDao();
			dao.updateModifyStatus(powerAccount, modifyStatus);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
