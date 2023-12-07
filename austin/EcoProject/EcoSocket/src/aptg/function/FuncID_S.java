package aptg.function;

import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.beans.AttrBean;
import aptg.beans.S31ReportBean;
import aptg.dao.PowerRecordDao;
import aptg.dao.SysConfigDao;
import aptg.manager.ControllerSetupManager;
import aptg.manager.EventManager;
import aptg.manager.MeterSetupManager;
import aptg.manager.SysConfigManager;
import aptg.models.ControllerSetupModel;
import aptg.models.MeterEventRecordModel;
import aptg.models.MeterSetupModel;
import aptg.models.PowerRecordModel;
import aptg.utils.JsonUtil;
import aptg.utils.ToolUtil;

/**
 * 	系統功能
 * 
 * @author austinchen
 */
public class FuncID_S extends FuncIDBase {

	private static final Logger logger = LogManager.getFormatterLogger(FuncID_S.class.getName());
	private static final Logger loggerS25 = LogManager.getFormatterLogger(FuncID_S25.class.getName());

	private static final String Sysconfig_IllegalDemandValue 	= "IllegalDemandValue";
	
	private BigDecimal illegalDemandValue;
	
	public FuncID_S(Socket socket) {
		this.socket = socket;
		this.illegalDemandValue = new BigDecimal(SysConfigManager.getInstance().getSysconfig(Sysconfig_IllegalDemandValue));
	}

	public void systemFunc(String[] message, int semicolonCount, String request) throws Exception {
		String response = "";
		
		// ex: S01
		String func = message[1];
		
		// 檢查Request sof+type是否為*1
		String sofTypeValid = requestSOFType(message);
		if (!StringUtils.isBlank(sofTypeValid)) {
			response = sofTypeValid;
			
		} else {
			// 依Function種類執行
			if (func.contains("01")) {
				// (1) 登入
				response = responseS01(message, semicolonCount);

			} else if (func.contains("20")) {
				// (2) 校時
				response = responseS20(message, semicolonCount);

			} else if (func.contains("34")) {
				// (3) 裝置組態回報
				response = responseS34(message, semicolonCount);

			} else if (func.contains("31")) {
				// (4) 裝置回報
				response = responseS31(message, semicolonCount);

			} else if (func.contains("25")) {
				// (5) 電力數值回傳
				response = updateS25(message, semicolonCount, request);	// 電表數據 => insert to db

			} else if (func.contains("07")) {
				// 偵測連線(heart beat)
				response = responseS07(message, semicolonCount);

			} else if (func.contains("10")) {
				// 事件回報
				response = updateS10(message, semicolonCount);	// 電表狀態 => insert/update to db
				
			} else {
//	        	logger.info("else ~~~");
			}
		}
		
		// send response to client
		sendSocketMsg(socket, response);
		logger.info("[Socket] send resp to ECO5Account=["+message[3]+"], response: "+response);
	}

	/**
	 * S01
	 * 登入驗證
	 * 
	 * @param message
	 * @return
	 */
	private String responseS01(String[] message, int semicolonCount) throws Exception {
		// 驗證參數欄位
		String validResult;

		// 長度、格式錯誤
		if (semicolonCount!=5 && semicolonCount!=6) {
			return HEADER_ERROR;
		}
		
		// 取得參數
		String func = message[1];	// S01
		String cmdSN = message[2];	// 00000001
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		String password = null;
		try {
			password = message[4];	// 密碼: abc123	
		} catch(Exception e) {
			// 檢查密碼是否正確 (1S0102)
			validResult = isPasswordFormatError(password);
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		}
		String tzOffset = null;
		if (semicolonCount==6) {
			tzOffset = message[5];		// 時區
		}

		// 查詢ECO5Account
		ControllerSetupModel ctlMdl = ControllerSetupManager.getInstance().getControllerSetup(gatewayID);

		// 查無GatewayID=ECO5Account (1S0101)
		validResult = isGatewayExist(ctlMdl, "S01");
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// 檢查密碼是否為空&格式是否正確 (1S0103 & 1S0107)
		validResult = isPasswordFormatError(password);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// 檢查密碼是否正確 (1S0102)
		validResult = isPasswordError(ctlMdl, password);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// 檢查時區 (1S0105)
		validResult = isTZOffsetFormatError(tzOffset);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		return getResponse(func, cmdSN, validResult, null);
	}

	/**
	 * S20
	 * 校時
	 * 
	 * @param message
	 * @return
	 */
	private String responseS20(String[] message, int semicolonCount) throws Exception {
		// 驗證參數欄位
		String validResult = SUCCESS_CDE;
		
		// 長度、格式錯誤
		if (semicolonCount!=4) {	// *1;S20;00000003;ES990000000000TEST00;LF 
			return HEADER_ERROR;
		}
		
		String func = message[1];	// S20
		String cmdSN = message[2];	// 00000003
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		
		long time = new Date().getTime() / 1000L;
		String timestamp = String.valueOf(time);

		// 查詢ECO5Account
		ControllerSetupModel ctlMdl = ControllerSetupManager.getInstance().getControllerSetup(gatewayID);
		
		// 查無GatewayID=ECO5Account (1S0101)
		validResult = isGatewayExist(ctlMdl, "S20");
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		return getResponse(func, cmdSN, validResult, timestamp);
	}

	/**
	 * S34
	 * 裝置組態回報
	 * 
	 * @param message
	 * @return
	 */
	private String responseS34(String[] message, int semicolonCount) throws Exception {
		// 驗證參數欄位
		String validResult = SUCCESS_CDE;
		
		// 長度、格式錯誤
		if (semicolonCount!=7) {
			return HEADER_ERROR;
		}
		
		String func = message[1];	// S34
		String cmdSN = message[2];	// 00000005
		String gatewayID = message[3];	// GatewayID: ABCD-ECO-TEST-0026--
		String deviceID = message[4];	// 設備ID: ABCD-ECO-TEST-0026--	= Gateway
		String attrCount = message[5];	// 屬性數量: 2
		
		List<AttrBean> attrInfoList = new ArrayList<AttrBean>();
		String attrList = message[6];	// 用逗號分開: 600700_v1.0.1.5,600200_45434F35
		String[] attr = null;
		if (attrList.indexOf(",")!=-1) {
			attr = attrList.split(",");
			for (String attrInfo: attr) {
				String[] info = attrInfo.split("_");
				AttrBean bean = new AttrBean();
				bean.setAttrID(info[0]);
				bean.setAttrValue(info[1]);
				attrInfoList.add(bean);
			}
		}

		// 檢查attrList筆數是否正確 (1S3401)
		validResult = attrList(attr, Integer.parseInt(attrCount));
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// 是否為未定義的屬性 (1S3402)
		for (AttrBean bean: attrInfoList) {
			validResult = checkAttr(bean);
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

			if (bean.getAttrID().equals("600200")) {	// 裝置型號
				validResult = checkHexToAscii(bean.getAttrValue());	// 轉碼是否成功
				if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
			}
		}
		attrInfoList = null;

//		// 檢查是否曾有eco斷線紀錄 => 新增EventRecord, 更新EventTx
//		MeterEventRecordModel ecoRecord = checkEcoStatus(gatewayID);

		return getResponse(func, cmdSN, validResult, null);
	}
//	private String hexToAscii(String hexStr) throws Exception {
//	    StringBuilder output = new StringBuilder("");
//	     
//	    for (int i = 0; i < hexStr.length(); i += 2) {
//	        String str = hexStr.substring(i, i + 2);
//	        output.append((char) Integer.parseInt(str, 16));
//	    }
//	    return output.toString();
//	}
	
	/**
	 * S31
	 * 裝置回報
	 * 
	 * @param message
	 * @return
	 */
	private String responseS31(String[] message, int semicolonCount) throws Exception {
		// 驗證參數欄位
		String validResult = SUCCESS_CDE;
		
		// 長度、格式錯誤
		if (semicolonCount!=6) {
			return HEADER_ERROR;
		}
		
		String func = message[1];	// S31
		String cmdSN = message[2];	// 00000005
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		String deviceCount = message[4];// 回報的裝置總數: 2

		// deviceCount = device.length
		List<S31ReportBean> reportList = new ArrayList<S31ReportBean>();
		String payload = message[5];		// 用斜線分開裝置: ES110000000000TEST00,2,114/ES110000000000TEST01,2,114
		if (payload.indexOf("/")!=-1) {
			// 有一個以上裝置
			String[] device = payload.split("/");
			for (String deviceInfo: device) {
				// (1S3105)
				validResult = checkSplitStringIllegal(deviceInfo);
				if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
				
				String[] info = deviceInfo.split(",");
				S31ReportBean bean = new S31ReportBean();
				bean.setDeviceID(info[0]);
				bean.setLinkType(info[1]);
				bean.setDeviceExtType(info[2]);
				reportList.add(bean);
			}
		} else {
			// (1S3105)
			validResult = checkSplitStringIllegal(payload);
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
			
			// 只有一個裝置
			String[] info = payload.split(",");
			S31ReportBean bean = new S31ReportBean();
			bean.setDeviceID(info[0]);
			bean.setLinkType(info[1]);
			bean.setDeviceExtType(info[2]);
			reportList.add(bean);
		}
		

		// Count值與 ReportField 的總數不一致 (1S3102)
		validResult = checkDeviceReportfieldCount(Integer.parseInt(deviceCount), reportList.size());
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		
		for (S31ReportBean reportBean: reportList) {
			// ReportField欄位錯誤 (1S3105)：LinkType與DeviceExtType 不在Table2 與Table3的定義內時回報此錯誤)
			validResult = checkLinkType(reportBean);
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
			
			// 回報裝置清單含有不支援的DeviceID (1S3105)
			validResult = checkDeviceExtType(reportBean);
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		
			// 比對Gateway & Device中間字串是否合法相符 (1S3106)
			validResult = checkGatewayDeviceNaming(gatewayID, reportBean.getDeviceID());
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		}
		reportList = null;

		// *** 不自動新增，不用此功能
//		// 無法處理裝置註冊 (1S3104)
//		validResult = registerDevice(gatewayID, reportList);
//		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		return getResponse(func, cmdSN, validResult, null);
	}

	/**
	 * S25
	 * 回傳電力數值
	 * 
	 * @param message
	 * @return
	 */
	private String updateS25(String[] message, int semicolonCount, String request) throws Exception {
		// 驗證參數欄位
		String validResult = SUCCESS_CDE;
		
		// 長度、格式錯誤
		if (semicolonCount!=7) {
			return HEADER_ERROR;
		}
		
		String func = message[1];	// S25
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		String deviceID = message[4];	// 設備ID: ES110000000000TEST00
		String reportTime = message[6];	// 回報時間: 130691738

		List<AttrBean> attrInfoList = new ArrayList<AttrBean>();
//		S25AttributeInfo ecoAttr = new S25AttributeInfo();
		String attrList = message[5];	// 電力數值ID_電力數值: 4_0.9,6_123,7_123,8_123,9_123,14_123,15_12,27_123,28_12,40_123,41_12
		if (attrList.indexOf(",")!=-1) {
			// 多筆數據
			String[] attr = attrList.split(",");
			for (String attrInfo: attr) {
				// (1S2501)
				validResult = checkUnderLineStringIllegal(attrInfo);
				if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
				
				String[] info = attrInfo.split("_");
				AttrBean bean = new AttrBean();
				bean.setAttrID(info[0]);
				bean.setAttrValue(info[1]);
				attrInfoList.add(bean);
			}
		} else {
			// (1S2501)
			validResult = checkUnderLineStringIllegal(attrList);
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
			
			// 一筆數據
			String[] info = attrList.split("_");
			AttrBean bean = new AttrBean();
			bean.setAttrID(info[0]);
			bean.setAttrValue(info[1]);
			attrInfoList.add(bean);
		}

		// 檢查時間格式 (2002)
		validResult = checkTimestamp(reportTime);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// 撈取出meter
		MeterSetupModel meterMdl = MeterSetupManager.getInstance().getMeterSetup(deviceID);
		
		// Device是否存在 (1S2503)
		validResult = isDeviceExist(meterMdl, "S25");
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		
		// AttrID未定義於Table1 (1S2502, 2017)
		PowerRecordModel power = new PowerRecordModel();
		for (AttrBean bean: attrInfoList) {
			validResult = checkAttrID(power, bean.getAttrID(), bean.getAttrValue());
			if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		}
		attrInfoList = null;
		
		// db info
		power.setDeviceId(deviceID);
		String time = ToolUtil.getInstance().convertDateToString(new Date(Long.valueOf(reportTime) * 1000L), "yyyy-MM-dd HH:mm:ss");
		power.setRecTime(time);
		
		// ************* 
//		PowerRecordDao dao = new PowerRecordDao();
////		List<Integer> ids = dao.insertPowerRecord(power);
//		dao.insertPowerRecordWithPartition(power);
		
		// 紀錄檢驗過的S25 request
		loggerS25.info("[S25] "+request);
		
		return getResponse(func, cmdSN, validResult, null);
	}
	
	/**
	 * S07
	 * 偵測連線(heart beat)
	 * 
	 * @param message
	 * @return
	 */
	private String responseS07(String[] message, int semicolonCount) throws Exception {
		// 長度、格式錯誤
		if (semicolonCount!=4) {
			return HEADER_ERROR;
		}
		
		String func = message[1];	// S07
		String cmdSN = message[2];	// 00000002
		String gatewayID = null;
		try {
			gatewayID = message[3];	// GatewayID: ES990000000000TEST00
			
			// 更新Gateway LastConnectionTime
//			GatewayListDao dao = new GatewayListDao();
//			dao.updateGatewayLastConnectionTime(gatewayID, ToolUtil.getInstance().convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			// 檢查Gateway是否為空 (2001)
			return HEADER_ERROR;
		}

		return getResponse(func, cmdSN, SUCCESS_CDE, null);
	}

	/**
	 * S10
	 * 事件回報
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private String updateS10(String[] message, int semicolonCount) throws Exception {
		// 驗證參數欄位
		String validResult = SUCCESS_CDE;
		
		// 長度、格式錯誤
		if (semicolonCount!=7) {
			return HEADER_ERROR;
		}
		
		// 取得參數
		String func = message[1];	// S10
		String cmdSN = message[2];	// 00000004
		String gatewayID = message[3];	// GatewayID: ES990000000000TEST00
		String deviceID = message[4];	// DeviceID: ES110000000000TEST00
		
		String evt = message[5];
		String[] evtInfo = evt.split(",");
		String evtType = evtInfo[0];	// 事件類型: 11(連線、斷線事件)
		String evtID = evtInfo[1];		// 事件代碼: 11(斷線)、12(恢復連線)
		String evtValue = evtInfo[2];	// 數值，若無值 => NULL字串
		String reportTime = message[6];	// 回報時間: 1306917380
		

		// 檢查時間格式
		validResult = checkTimestamp(reportTime);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);
		
		// EvtType, EvtID 在Table5可查找到 (1S1009)
		MeterEventRecordModel record = EventManager.getInstance().getMeterEventRecord(gatewayID, deviceID, reportTime);
		validResult = checkEvtTypeEvtId(evtType, evtID, record);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// EvtValue 不允許為NULL 或格式錯誤 (1S1009)
		validResult = checkEvtValue(evt);
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		// Device不存在 (1S1002)
		MeterSetupModel meterMdl = MeterSetupManager.getInstance().getMeterSetup(deviceID);
		validResult = isDeviceExist(meterMdl, "S10");
		if (!validResult.contentEquals(SUCCESS_CDE)) return getResponse(func, cmdSN, validResult, null);

		return getResponse(func, cmdSN, validResult, null);
	}
}
