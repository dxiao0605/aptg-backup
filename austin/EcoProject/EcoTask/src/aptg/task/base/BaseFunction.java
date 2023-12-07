package aptg.task.base;

import java.util.ArrayList;
import java.util.List;

import aptg.models.MeterSetupModel;
import aptg.models.PowerAccountModel;
import aptg.utils.DBQueryUtil;

public class BaseFunction {
	
	public static boolean isReprice = false;
	public static List<String> failedDeviceList = new ArrayList<>();
	public static List<String> failedPowerAccountList = new ArrayList<>();
	
	public static List<String> successDeviceList = new ArrayList<>();
	
	public void setIsReprice(boolean reprice) {
		isReprice = reprice;
	}
	public boolean getIsReprice() {
		return isReprice;
	}
	
	public void setFailedDevice(String deviceID) {
		failedDeviceList.add(deviceID);
	}
	public List<String> getFailedDevice() {
		return failedDeviceList;
	}
	
	public void setFailedPowerAccount(String powerAccount) {
		failedPowerAccountList.add(powerAccount);
	}
	public List<String> getFailedPowerAccount() {
		return failedPowerAccountList;
	}

	public void setSuccessDevice(String deviceID) {
		successDeviceList.add(deviceID);
	}
	public List<String> getSuccessDevice() {
		return successDeviceList;
	}

	/**
	 *	以BankCode查找enabled的MeterSetup device列表
	 *
	 * @param bankCode
	 * @return
	 */
	public List<MeterSetupModel> getDeviceByBank(String bankCode) {
		List<MeterSetupModel> deviceList = new ArrayList<>();
		deviceList = DBQueryUtil.getInstance().queryEnabledMeterByBankCode(bankCode);
		return deviceList;
	}
	/**
	 *	以BankCode查找enabled, UsageCode1的MeterSetup device列表
	 *
	 * @param bankCode
	 * @return
	 */
	public List<MeterSetupModel> getUsageCode1DeviceByBank(String bankCode) {
		List<MeterSetupModel> deviceList = new ArrayList<>();
		deviceList = DBQueryUtil.getInstance().queryEnabledMeterUsageCode1ByBankCode(bankCode);
		return deviceList;
	}
	
	/**
	 *	以DeviceID查找出此PowerAccount的所有enabled的MeterSetup device列表
	 *
	 * @param deviceID
	 * @return
	 */
	public List<MeterSetupModel> getDevice(String deviceID) {
		List<MeterSetupModel> deviceList = new ArrayList<>();

		MeterSetupModel meter = DBQueryUtil.getInstance().getMeterSetup(deviceID);
		deviceList.add(meter);
		return deviceList;
	}
	/**
	 *	以DeviceID查找出此PowerAccount的所有enabled, UsageCode1的MeterSetup device列表
	 *
	 * @param deviceID
	 * @return
	 */
	public List<MeterSetupModel> getUsageCode1Device(String deviceID) {
		MeterSetupModel meter = DBQueryUtil.getInstance().getMeterSetup(deviceID);
		List<MeterSetupModel> deviceList = DBQueryUtil.getInstance().getEnabledMeterSetupUsageCode1(meter.getPowerAccount());
		return deviceList;
	}
	/**
	 *	取得所有enabled的Meter device列表
	 *
	 * @return
	 */
	public List<MeterSetupModel> getAllDevice() {
		// 取得所有enabled的Meter Device => 逐一統計&計算
		List<MeterSetupModel> deviceList = DBQueryUtil.getInstance().getAllEnabledMeter();
		return deviceList;
	}
	/**
	 *	取得所有enabled, UsageCode1的Meter device列表
	 * 
	 * @return
	 */
	public List<MeterSetupModel> getAllUsageCode1Device() {
		// 取得所有enabled的Meter Device => 逐一統計&計算
		List<MeterSetupModel> deviceList = DBQueryUtil.getInstance().getEnabledMeterSetupUsageCode1();
		return deviceList;
	}
	
	/**
	 *	以BankCode查找PowerAccount列表
	 * 
	 * @param bankCode
	 * @return
	 */
	public List<PowerAccountModel> getPowerAccountByBank(String bankCode) {
		List<PowerAccountModel> paList = new ArrayList<>();
		paList = DBQueryUtil.getInstance().queryPowerAccountByBankCode(bankCode);
		return paList;
	}
	/**
	 *	取得所有PowerAccount列表
	 *
	 * @return
	 */
	public List<PowerAccountModel> getAllPowerAccount() {
		List<PowerAccountModel> paList = DBQueryUtil.getInstance().getAllPowerAccount();
		return paList;
	}
	/**
	 *	以DeviceID查找PowerAccount
	 * 
	 * @param deviceID
	 * @return
	 */
	public List<PowerAccountModel> getPowerAccountByDID(String deviceID) {
		MeterSetupModel meter = DBQueryUtil.getInstance().getMeterSetup(deviceID);
		PowerAccountModel pa = DBQueryUtil.getInstance().getPowerAccount(meter.getPowerAccount());
		
		List<PowerAccountModel> paList = new ArrayList<>();
		paList.add(pa);
		return paList;
	}
	/**
	 *	以PowerAccount查找PowerAccount
	 *
	 * @param powerAccount
	 * @return
	 */
	public List<PowerAccountModel> getPowerAccountByPA(String powerAccount) {
		PowerAccountModel pa = DBQueryUtil.getInstance().getPowerAccount(powerAccount);
		
		List<PowerAccountModel> paList = new ArrayList<>();
		paList.add(pa);
		return paList;
	}

	public void delFcstCharge(String pa) {
		
	}
	public void updateFcstCharge(String pa) {
		
	}

	public void delPowerMonth(String pa) {
		
	}
	public void updatePowerMonth(String pa) {
		
	}

	public void delBestRatePlan(String pa) {
		
	}
	public void updateBestRatePlan(String pa) {
		
	}

	public void delBestCC(String pa) {
		
	}
	public void updateBestCC(String pa) {
		
	}

	public void delPowerAccountMaxDemand(String pa) {
		
	}
	public void updatePowerAccountMaxDemand(String pa) {
		
	}
	
//	public void updateExecuteFlag() {
//		
//	}
}
