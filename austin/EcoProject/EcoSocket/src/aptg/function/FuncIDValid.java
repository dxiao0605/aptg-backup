package aptg.function;

import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.AttrBean;
import aptg.beans.S31ReportBean;
import aptg.enums.ErrorCodeEnum;
import aptg.manager.EventManager;
import aptg.models.ControllerSetupModel;
import aptg.models.MeterEventRecordModel;
import aptg.models.MeterSetupModel;
import aptg.models.PowerRecordModel;

public class FuncIDValid {
	
	private static final Logger logger = LogManager.getFormatterLogger(FuncIDValid.class.getName());

	private static final String SUCCESS_CDE 		= "0";
//	private static final String DEVICE_TYPE 		= "electric";
//	private static final String OFFLINE 			= "offline";
//	private static final String ONLINE 				= "online";

	private static final BigDecimal thousand 		= new BigDecimal("1000");
	
	public String getSuccessCode() {
		return SUCCESS_CDE;
	}
	
	/**
	 * S31
	 * 
	 * @param str
	 * @return
	 */
	protected String checkSplitStringIllegal(String str) {
		if (str.indexOf(",")==-1) {
			return ErrorCodeEnum.ERRCDE_S31_REPORTFIELD_ERROR.getErrCode();
		} else {
			int count = StringUtils.countMatches(str, ",");
			if (count!=2) {
				return ErrorCodeEnum.ERRCDE_S31_REPORTFIELD_ERROR.getErrCode();
			}
			return SUCCESS_CDE;
		}
	}
	
	/**
	 * S25
	 * 
	 * @param str
	 * @return
	 */
	protected String checkUnderLineStringIllegal(String str) {
		if (str.indexOf("_")==-1) {
			return ErrorCodeEnum.ERRCDE_S25_ATTRID_FORMAT.getErrCode();
		} else {
			String attrValue = str.substring(str.indexOf("_")+1, str.length());
			if (StringUtils.isBlank(attrValue)) {
				return ErrorCodeEnum.ERRCDE_S25_ATTRID_FORMAT.getErrCode();
			}
		}
		return SUCCESS_CDE;
	}
	
	/**
	 * S01, S20
	 * Gateway是否存在於DB
	 * 
	 * @return 
	 */
	protected String isGatewayExist(ControllerSetupModel ctlMdl, String func) {
		if (ctlMdl==null) {
			// DB若無 => response error
			if (func.equals("S01")) return ErrorCodeEnum.ERRCDE_S01_GATEWAY_NOT_EXIST.getErrCode();
			if (func.equals("S20")) return ErrorCodeEnum.ERRCDE_S20_GATEWAY_NOT_EXIST.getErrCode();
		}
		return SUCCESS_CDE;
	}
//	
////	/**
////	 * S01
////	 * Gateway連線數已達限制值，一個Gateway同時間只能有一個連線
////	 * 
////	 * @param gatewayID
////	 * @return
////	 */
////	protected String isGatewayConnected(String gatewayID, Socket newSocket) {
////		Socket socket = GatewayManager.getInstance().getLoginGatewaySocket(gatewayID);
////		if (socket==null) {
////			// 未曾記錄過此Gateway的Socket => 允許連線，紀錄Socket
////			GatewayManager.getInstance().updateLoginGatewaySocket(gatewayID, newSocket);
////		} else if (socket!=null && socket.isClosed()) {
////			// 有記錄過此Socket，但Socket已斷 => 允許連線
////			
////		} else {
////			// 有連線中的Gateway Socket
////			return ErrorCodeEnum.ERRCDE_S01_GATEWAY_LIMIT.getErrCode();
////		}
////
////		return SUCCESS_CDE;
////	}
	
	/**
	 * S01
	 * TZOffset 欄位錯誤
	 * 
	 * @param tzOffset
	 * @return 1S0105
	 */
	protected String isTZOffsetFormatError(String tzOffset) {
		if (tzOffset!=null) {
			String sign = tzOffset.substring(0, 1);
			if (!sign.contentEquals("+") && !sign.contentEquals("-")) {
				return ErrorCodeEnum.ERRCDE_S01_TZOFFSET_ERROR.getErrCode();
			}
			
			if (tzOffset.contains(":")) {
				String[] info = tzOffset.split(":");
				int hour = Integer.parseInt(info[0].substring(1, info[0].length()));
				int min = Integer.parseInt(info[1]);
				
				if (hour<0 || hour>23) return ErrorCodeEnum.ERRCDE_S01_TZOFFSET_ERROR.getErrCode();
				if (min<0 || min>59) return ErrorCodeEnum.ERRCDE_S01_TZOFFSET_ERROR.getErrCode();

			} else {
				String hourStr = tzOffset.substring(1, tzOffset.length());
				int hour = Integer.parseInt(hourStr);
				
				if (hour<0 || hour>23) return ErrorCodeEnum.ERRCDE_S01_TZOFFSET_ERROR.getErrCode();
			}
		}
		return SUCCESS_CDE;
	}
//
////	/**
////	 * S01
////	 * Gateway格式欄位是否正確
////	 *
////	 * @param gatewayID
////	 * @return	1S0106
////	 */
////	protected String isGatewayNull(String gatewayID) {
////		if (StringUtils.isBlank(gatewayID) || !gatewayID.matches("\\w{20}")) {	// 20個英數混合
////			return ErrorCodeEnum.ERRCDE_S01_GW_FORMAT.getErrCode();
////		}
////		return SUCCESS_CDE;
////	}

	/**
	 * S01
	 * 
	 * @param password
	 * @return 1S0103, 1S0107
	 */
	protected String isPasswordFormatError(String password) {
//		if (!password.matches("\\w{1,6}")) {	// 英數混合，1~6個字母
//			// 格式錯誤: 1~6 碼大小寫英數字(預設 abc123)
//			return ErrorCodeEnum.ERRCDE_S01_PASSWORD_FORMAT.getErrCode();
//		} else if (password.length()==0) {
//			// 密碼為空
//			return ErrorCodeEnum.ERRCDE_S01_PASSWORD_NULL.getErrCode();
//		}
		if (StringUtils.isBlank(password) || password.length()==0) {
			// 密碼為空
			return ErrorCodeEnum.ERRCDE_S01_PASSWORD_NULL.getErrCode();
		}
		return SUCCESS_CDE;
	}
	
	/**
	 * S01
	 * 密碼是否正確
	 * 
	 * @param password
	 * @return 1S0102
	 */
	protected String isPasswordError(ControllerSetupModel ctlMdl, String password) {
		// 先compare cache，若與cache不符合
		if (!ctlMdl.getEco5Password().equals(password)) {
			return ErrorCodeEnum.ERRCDE_S01_PASSWORD_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}
//
////	/**
////	 * S07
////	 * 
////	 * @param gateway
////	 * @return
////	 */
////	protected String isGatewayDeviceFormatError(String gateway) {
////		if (StringUtils.isBlank(gateway) || gateway.length()==0) {
////			// 密碼為空
////			return ErrorCodeEnum.ERRCDE_REQUEST_ERROR.getErrCode();
////		}
////		return SUCCESS_CDE;
////	}
	
	/**
	 * S25, S10
	 * timestamp欄位錯誤
	 * 
	 * @param time
	 * @return
	 */
	protected String checkTimestamp(String time) {
		if (!time.matches("\\d{10}")) {	// 只能數字，10個字母
			// 只允許數字
			// length=10 (ex: 1306917380)
			return ErrorCodeEnum.ERRCDE_TIMESTAMP_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}
	
	/**
	 * S34
	 * AttrList錯誤
	 * 
	 * @param attr
	 * @param attrCount
	 * @return 1S3401
	 */
	protected String attrList(String[] attr, int attrCount) {
		if (attr==null) {
			return ErrorCodeEnum.ERRCDE_S34_ATTRLIST_ERROR.getErrCode();
		} else if (attr.length!=attrCount) {
			return ErrorCodeEnum.ERRCDE_S34_ATTRLIST_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}

	/**
	 * S10, S25, S31
	 * device是否存在
	 * 
	 * @param deviceID
	 * @param func
	 * @return 
	 */
	protected String isDeviceExist(MeterSetupModel meterMdl, String func) {
		if (meterMdl==null) {
			if (func.contentEquals("S10")) return ErrorCodeEnum.ERRCDE_S10_DEVICE_NOT_EXIST.getErrCode();
			if (func.contentEquals("S25")) return ErrorCodeEnum.ERRCDE_S25_DEVICE_NOT_EXIST.getErrCode();
		}
//		if (!deviceID.matches("\\w{4}-\\w{3}-\\w{4}-\\w{6}")) {	// 英數-混合，20個字母 Ex: IN11-ECO-TEST-002601
//		}
		return SUCCESS_CDE;
	}

	/**
	 * S31
	 * 
	 * @param gatewayID
	 * @return
	 */
	protected String checkDeviceReportfieldCount(int deviceCount, int reportFieldSize) {
		if (deviceCount!=reportFieldSize) {
			return ErrorCodeEnum.ERRCDE_S31_DEVICE_COUNT.getErrCode();
		}
		return SUCCESS_CDE;
	}

	/**
	 * S31
	 * DeviceID和Gateway比對是否符合命名規則
	 * 
	 * @param gatewayID
	 * @param deviceID
	 * @return
	 */
	protected String checkGatewayDeviceNaming(String gatewayID, String deviceID) {
		// 長度、英數
		if (deviceID.length()!=20) {
			// 長度錯誤 (1S3106)
			return ErrorCodeEnum.ERRCDE_S31_DEVICE_NAMING.getErrCode();
		}
		
		String rule = gatewayID.substring(5, 18);	// ABCD-ECO-TEST-0026-	=> ECO-TEST-0026
		String name = deviceID.substring(5, 18);	// IN11-ECO-TEST-002601	=> ECO-TEST-0026
		if (!name.equals(rule)) {
			// 命名規則不正確 (1S3106)
			return ErrorCodeEnum.ERRCDE_S31_DEVICE_NAMING.getErrCode();
		}
		
		return SUCCESS_CDE;
	}
	
//	/**
//	 * S31
//	 * device註冊
//	 * 
//	 * @param gatewayID
//	 * @param reportList
//	 * @return
//	 */
//	protected String registerDevice(String gatewayID, List<S31ReportBean> reportList) {
//		DeviceTypeBean typeBean = DeviceTypeManager.getInstance().getDeviceTypeInfo(DEVICE_TYPE);
//		GatewayListBean gatewayBean = GatewayManager.getInstance().getGatewayInfo(gatewayID);
//		
//		if (typeBean==null || gatewayBean==null) {
//			return ErrorCodeEnum.ERRCDE_S31_REGISTER_FAILED.getErrCode();
//			
//		} else {
//			List<DeviceBean> deviceList = new ArrayList<DeviceBean>();
//			for (DeviceReportBean reportBean: reportList) {
//				String device_key = reportBean.getDeviceID();
//				DeviceBean device = DeviceManager.getInstance().getDeviceInfo(device_key);
//				if (device!=null) {	// 已存在於cache內，略過
//					continue;
//				}
//				
//				DeviceBean deviceBean = new DeviceBean();
//				deviceBean.setDevice_key(reportBean.getDeviceID());
//				deviceBean.setAlias(reportBean.getDeviceID());
//				deviceBean.setType_id(typeBean.getType_id());
//				deviceBean.setLocation_id(gatewayBean.getLocation_id());
//				deviceBean.setCompany_id(gatewayBean.getCompany_id());
//				deviceBean.setGateway_id(gatewayBean.getGateway_id());
//				
//				deviceList.add(deviceBean);
//			}
//			
//			try {
//				if (deviceList.size()!=0) {
//					// size>0 => 有需要新增至DB
//					DeviceDao dao = new DeviceDao();
//					List<Integer> ids = dao.batchAddDevice(deviceList);
//					if (ids.size()!=0) {
//						// DB更新成功 => 更新cache
//						for (DeviceBean deviceBean: deviceList) {
//							DeviceManager.getInstance().updateDeviceCache(deviceBean);
//						}
//						return SUCCESS_CDE;
//					} else {
//						return ErrorCodeEnum.ERRCDE_S31_REGISTER_FAILED.getErrCode();
//					}
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return ErrorCodeEnum.ERRCDE_DB_ERROR.getErrCode();
//			}
//		}
//		return SUCCESS_CDE;
//	}
	

	/**
	 * S10
	 * evtType, evtId對照Table5
	 * 
	 * @param evtType
	 * @param evtID
	 * @return 1S1001
	 */
	protected String checkEvtTypeEvtId(String evtType, String evtID, MeterEventRecordModel record) {
//		if (StringUtils.isBlank(evtType) || !evtType.matches("^.[0-9]+$") || StringUtils.isBlank(evtID)) {
//			// evtType, evtID不可為null	(1S1001)
//			return ErrorCodeEnum.ERRCDE_S10_EVTCDE.getErrCode();
//		}
		
		logger.info("[S10] evtType: "+evtType+", evtID: "+evtID);
		switch(evtType) {
			case "11":
				if (evtID.equals("1")) {
					// ComPort 存取錯誤 (COM Failure)
					
				} else if (evtID.equals("6")) {
					// 不正確的裝置模式 (Incorrect Device Mode)
					
				} else if (evtID.equals("11")) {
					// evtType:11 對應 evtID: 11	=> 裝置斷線事件
					// record, tx斷線紀錄
					MeterEventRecordModel eventRecord = EventManager.getInstance().getOfflineDevice(record.getDeviceID());
					if (eventRecord==null) {
						// 無離線中的紀錄 => 新增一筆離線record
						EventManager.getInstance().recordDeviceOffline(record);	
					}
					
				} else if (evtID.equals("12")) {
					// evtType:11 對應 evtID: 12	=> 裝置恢復連線事件
					// record, tx重連紀錄
					MeterEventRecordModel eventRecord = EventManager.getInstance().getOfflineDevice(record.getDeviceID());
					if (eventRecord!=null) {
						// 找到離線紀錄 => 新增恢復連線
						EventManager.getInstance().recordDeviceOnline(record);	
					}

				} else {
					// evtId事件代號	(1S1001)
					return ErrorCodeEnum.ERRCDE_S10_EVTCDE.getErrCode();
				}
				break;
				
			default:
				// evtId事件代號	(1S1001)
				return ErrorCodeEnum.ERRCDE_S10_EVTCDE.getErrCode();
		}
		return SUCCESS_CDE;
	}

	/**
	 * S10
	 * evtValue不為null，可為NULL字串
	 * 
	 * @param evtValue
	 * @return 1S1009
	 */
	protected String checkEvtValue(String evt) {
		// evt = 11,11,NULL
		if (evt.indexOf(",")!=-1) {
			int count = StringUtils.countMatches(evt, ",");
			if (count!=2) {
				return ErrorCodeEnum.ERRCDE_S10_EVTCDE_FORMAT.getErrCode();
			} else {
				// 11,11,
				String evtValue = evt.substring(evt.lastIndexOf(",")+1, evt.length());
				if (StringUtils.isBlank(evtValue) || !evtValue.equals("NULL")) {
					return ErrorCodeEnum.ERRCDE_S10_EVTCDE_FORMAT.getErrCode();
				}
			}
		} else {
			return ErrorCodeEnum.ERRCDE_S10_EVTCDE_FORMAT.getErrCode();
		}
		return SUCCESS_CDE;
	}
	
	/**
	 * S34
	 * 
	 * @param bean
	 * @return
	 */
	protected String checkAttr(AttrBean bean) {
		switch(bean.getAttrID()) {
			case "600100":	// AliasName: 裝置名稱、別名
				break;
			case "600200":	// ModelName: 裝置型號
				break;
			case "600600":	// SubID: 子裝置編號
				break;
			case "600700":	// DeviceVersion: 裝置版本號
				break;
			case "600800":	// UsStatsEnable: 是否進行統計運算
				break;
				
			default:
				// AttrID不在Table4 的定義內 (1S3402)
				return ErrorCodeEnum.ERRCDE_S34_ATTR_UNDEFINE.getErrCode();
		}
		return SUCCESS_CDE;
	}
	
	/**
	 * S34
	 * attvalue是否轉碼成功
	 * 
	 * @param hexStr
	 * @return
	 */
	protected String checkHexToAscii(String hexStr) {
	    StringBuilder output = new StringBuilder("");
	    try {
		    for (int i = 0; i < hexStr.length(); i += 2) {
		        String str = hexStr.substring(i, i + 2);
		        output.append((char) Integer.parseInt(str, 16));
		    }	
	    } catch(Exception e) {
			return ErrorCodeEnum.ERRCDE_S34_HEXTOASCII_ERROR.getErrCode();
	    }
		return SUCCESS_CDE;
	}
	
	/**
	 * S31
	 *  
	 * @param reportBean
	 * @return
	 */
	protected String checkLinkType(S31ReportBean reportBean) {
		switch(reportBean.getLinkType()) {
			case "1":	// ZigBee
				break;
			case "2":	// RS485
				break;
			case "3":	// PLC
				break;
			case "4":	// Ethernet
				break;
			case "5":	// DALI
				break;
				
			default:
				// LinkType不在Table2 的定義內 (1S3105)
				return ErrorCodeEnum.ERRCDE_S31_REPORTFIELD_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}

	/**
	 * S31
	 * 
	 * @param reportBean
	 * @return
	 */
	protected String checkDeviceExtType(S31ReportBean reportBean) {
		switch(reportBean.getDeviceExtType()) {
			case "00":	// Server Plugin
				break;
			case "01":	// RF 通訊閘道器 
				break;
			case "02":	// 插座型電力計
				break;
			case "03":	// 雙 CT 電力計
				break;
			case "04":	// 雙 CT 電力計
				break;
			case "05":	// PLC 插座型電力計 
				break;
			case "06":	// 三相電力計
				break;
			case "10":	// 單相單迴路電表 
				break;
			case "11":	// 三相單迴路電表
				break;
			case "12":	// 交流單/三相多迴路電表 （iMeter） 
				break;
			case "13":	// 直流電表
				break;
			case "20":	// 日照計 
				break;
			case "21":	// 環境感測器
				break;
			case "22":	// 紅外線感測器
				break;
			case "23":	// 空調子機
				break;
			case "32":	// 燈光
				break;
			case "33":	// 發電組串監測器（DC StringMeter） 
				break;
			case "90":	// ZigBee 閘道器 （iZBGateway） 
				break;
			case "91":	// iMeter 閘道器 （iMeterGateway） 
				break;
			case "92":	// 空調閘道器 （iACGateway）
				break;
			case "93":	// PLC 網路閘道器
				break;
			case "99":	// 通用型閘道器
				break;
			case "C1":	// 複合型閘道器 
				break;
			case "L1":	// 複合型燈具（單相電表、環境感測） 
				break;
			case "L2":	// 複合型燈具（三相電表、環境 感測） 
				break;
			case "L3":	// 複合型燈具（電力計） 
				break;

			case "114":	// 不詳 
				break;
				
			default:
				// DeviceExtType不在Table3 的定義內 (1S3105)
				return ErrorCodeEnum.ERRCDE_S31_REPORTFIELD_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}
	
	/**
	 * S25
	 * AttrID 未定義於 Table1
	 * 
	 * @param ecoAttr
	 * @param attrID
	 * @param attrValue
	 * @return
	 */
	protected String checkAttrID(PowerRecordModel power, String attrID, String attrValue) {
		if (!isAttrValueFormatValid(attrValue)) {
			// attrValue非數字，型態轉換失敗
			return ErrorCodeEnum.ERRCDE_ATTRVALUE_ERROR.getErrCode();
		}
		
		attrID = attrID.toLowerCase();
		switch(attrID) {
			case "i1":	// 三相之A(R)相電流
				power.setI1(new BigDecimal(attrValue));	// I1	電流R
				break;
			case "i2":	// 三相之B(S)相電流
				power.setI2(new BigDecimal(attrValue)); // I2	電流S
				break;
			case "i3":	// 三相之C(T)相電流
				power.setI3(new BigDecimal(attrValue));	// I3	電流T
				break;
			case "iavg":		// 單相三線：平均電流, 三相三線：平均電流, 三相四線：平均電流
				power.setIavg(new BigDecimal(attrValue));	// Iavg	平均電流
				break;

			case "v1":	// 三相之A(R)相電壓
				power.setV1(new BigDecimal(attrValue));	// V1	相電壓R
				break;
			case "v2":	// 三相之B(S)相電壓
				power.setV2(new BigDecimal(attrValue));	// V2	相電壓S
				break;
			case "v3":	// 三相之C(T)相電壓
				power.setV3(new BigDecimal(attrValue));	// V3	相電壓T
				break;
			case "vavg":	// 電壓: 直流電壓
				power.setVavg(new BigDecimal(attrValue));	// Vavg	平均相電壓
				break;
				
			case "v12":	// 三相：A 相( R 相)到 B 相(S相)線電壓, 單位V
				power.setV12(new BigDecimal(attrValue));	// V12	線電壓AB
				break;
			case "v23":	// 三相：B 相( S 相)到 C 相(T相)線電壓, 單位V
				power.setV23(new BigDecimal(attrValue));	// V23	線電壓BC
				break;
			case "v31":	// 三相：C 相( T 相)到 A 相(R相)線電壓, 單位V
				power.setV31(new BigDecimal(attrValue));	// V31	線電壓CA
				break;
			case "vavgp":		// 三相三線：平均線電壓, 三相四線：平均線電壓, 單位V
				power.setVavgP(new BigDecimal(attrValue));	// VavgP	平均線電壓
				break;

			case "w":	// 實功率: 直流功率
				power.setW(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "var":
				power.setVar(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "va":
				power.setVa(new BigDecimal(attrValue).divide(thousand, 2, BigDecimal.ROUND_HALF_UP));
				break;
			case "pf":
				power.setPf(new BigDecimal(attrValue));
				break;

			case "kwh":	// 正相累積電能: 直流累積能
				power.setkWh(new BigDecimal(attrValue));
				break;
			case "kvarh":
				power.setKvarh(new BigDecimal(attrValue));
				break;
			case "hz":
				power.setHz(new BigDecimal(attrValue));
				break;
			case "thdvavg":
				power.setThVavg(new BigDecimal(attrValue));
				break;
			case "thdiavg":
				power.setThIavg(new BigDecimal(attrValue));
				break;

			case "mode1":
				power.setMode1(new BigDecimal(attrValue));
				break;
			case "mode2":
				power.setMode2(new BigDecimal(attrValue));
				break;
			case "mode3":
				power.setMode3(new BigDecimal(attrValue));
				break;
			case "mode4":
				power.setMode4(new BigDecimal(attrValue));
				break;
				
			case "demand":
				power.setDemandPK(new BigDecimal(attrValue));
				break;
			case "demandhalf":
				power.setDemandSP(new BigDecimal(attrValue));
				break;
			case "demandsathalf":
				power.setDemandSatSP(new BigDecimal(attrValue));
				break;
			case "demandoff":
				power.setDemandOP(new BigDecimal(attrValue));
				break;

			case "rushhour":
				power.setMcecPK(new BigDecimal(attrValue));
				break;
			case "halfhour":
				power.setMcecSP(new BigDecimal(attrValue));
				break;
			case "sathalfhour":
				power.setMcecSatSP(new BigDecimal(attrValue));
				break;
			case "offhour":
				power.setMcecOP(new BigDecimal(attrValue));
				break;
				
			case "rushhourtoday":
				power.setHighCECPK(new BigDecimal(attrValue));
				break;
			case "halfhourtoday":
				power.setHighCECSP(new BigDecimal(attrValue));
				break;
			case "offhourtoday":
				power.setHighCECOP(new BigDecimal(attrValue));
				break;
				
			default:
				// AttrID不在Table1 的定義內 (1S2502)
				return ErrorCodeEnum.ERRCDE_S25_ATTRID_UNDEFINE.getErrCode();
		}
		return SUCCESS_CDE;
	}
	private boolean isAttrValueFormatValid(String attrValue) {
		try {
			Float.valueOf(attrValue);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * A50, C50
	 * AttrID 未定義於 Table6
	 * 
	 * @param bean
	 * @return
	 */
	protected String checkDemandAttr(MeterSetupModel meter, String attrID, String attrValue) {
		switch(attrID) {
			case "1":	// 經常契約容量: 0~999999
				meter.setUsuallyCC(Integer.valueOf(attrValue));
				break;
			case "2":	// 半尖峰契約容量: 0~999999
				meter.setSpcc(Integer.valueOf(attrValue));
				break;
			case "3":	// 周六半尖峰契約容量: 0~999999
				meter.setSatSPCC(Integer.valueOf(attrValue));
				break;
			case "4":	// 離峰契約容量: 0~999999
				meter.setOpcc(Integer.valueOf(attrValue));
				break;
				
			case "5":	// 預測模式: 1:混合式, 2:浮動式, 3:固定式, 4:平均式
				meter.setDfCode(Integer.valueOf(attrValue));
				break;
			case "6":	// 上限: 0~200
				meter.setDfUpLimit(Integer.valueOf(attrValue));
				break;
			case "7":	// 下限: 0~200
				meter.setDfLoLimit(Integer.valueOf(attrValue));
				break;
			case "8":	// 計算週期: 1~14
				meter.setDfCycle(Integer.valueOf(attrValue));
				break;
			case "9":	// 是否啟用: 0:不啟用, 1:啟用 
				meter.setDfEnabled(Integer.valueOf(attrValue));
				break;
//			case "10":	// 卸載設備: 0:無卸載設備, 1:DO 模組, 2:ECO-3(空調卸載), 3:客製化控制程式
//				meter.setUninstallDevice(Integer.valueOf(attrValue));
//				break;
//			case "11":	// 控制數量: 1~10
//				meter.setControlQuantity(Integer.valueOf(attrValue));
//				break;
//			case "12":	// 需量控制模式: 1:先停止，先啟動, 2:先停止，後啟動
//				meter.setControlDemandMode(Integer.valueOf(attrValue));
//				break;
//			case "13":	// 臨時需量: 2019/01/02:20-2019/08/30:35 (無資料以 NULL 表示)
//				meter.setTempDemand(Integer.valueOf(attrValue));
//				break;
//				
//			case "1011":// DO模組IP: IPv4
//				meter.setDoIP(attrValue);
//				break;
//			case "1012":// DO模組Port: 0~65535
//				meter.setDoPort(Integer.valueOf(attrValue));
//				break;
//			case "1013":// DO模組站號: 1~255
//				meter.setDoNum(Integer.valueOf(attrValue));
//				break;
//			case "1014":// DO模組Modbus通訊格式: 0:ModbusRTU, 1:ModbusTCP
//				meter.setDoModbus(Integer.valueOf(attrValue));
//				break;
//			case "1015":// DO模組命令起始位址: 0~65535
//				meter.setDoCmdStartNum(Integer.valueOf(attrValue));
//				break;
				
			default:
				// Response錯誤 (2003)
				return ErrorCodeEnum.ERRCDE_RESPONSE_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}

	protected String checkAlertAttr(MeterSetupModel meter, String attrID, String attrValue) {
		switch(attrID) {
			case "1":	// 電流警報上限: 0~99999.99
				meter.setCurUpLimit(new BigDecimal(attrValue));
				break;
			case "2":	// 電流警報下限: 0~99999.99
				meter.setCurLoLimit(new BigDecimal(attrValue));
				break;
			case "3":	// 是否啟用電流警報
				meter.setCurAlertEnabled(Integer.valueOf(attrValue));
				break;
				
			case "4":	// 電壓警報上限 
				meter.setVolUpLimit(new BigDecimal(attrValue));
				break;
			case "5":	// 電壓警報下限 
				meter.setVolLoLimit(new BigDecimal(attrValue));
				break;
			case "6":	// 電壓類型
				meter.setVolAlertType(Integer.valueOf(attrValue));
				break;
			case "7":	// 是否啟用電壓警報
				meter.setVolAlertEnabled(Integer.valueOf(attrValue));
				break;
				
			case "8":	// 本月電量上限
				meter.setEcUpLimit(new BigDecimal(attrValue));
				break;
			case "9":	// 是否啟用本月電量上限 
				meter.setEcAlertEnabled(Integer.valueOf(attrValue));
				break;
				
			default:
				// Response錯誤 (2003)
				return ErrorCodeEnum.ERRCDE_RESPONSE_ERROR.getErrCode();
		}
		return SUCCESS_CDE;
	}				
}
