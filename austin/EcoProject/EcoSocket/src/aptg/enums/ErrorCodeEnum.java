package aptg.enums;

public enum ErrorCodeEnum {

	/**
	 * DB: 存取資料庫發生錯誤
	 */
	ERRCDE_DB_ERROR("DB", "1001"),

//	/**
//	 * Request代碼錯誤
//	 */
//	ERRCDE_REQUEST_ERROR("REQUEST", "2001"),
	/**
	 * Response錯誤
	 */
	ERRCDE_RESPONSE_ERROR("RESPONSE", "2003"),

	/**
	 * Timestamp欄位格式檢查
	 */
	ERRCDE_TIMESTAMP_ERROR("TIMESTAMP", "2002"),

	/**
	 * AttrValue欄位非數字，轉型錯誤
	 */
	ERRCDE_ATTRVALUE_ERROR("FORMAT", "2017"),

	/**
	 * S20: 校時
	 */
	ERRCDE_S20_GATEWAY_NOT_EXIST("S20", "1S2001"),
	
	/**
	 * S01: 閘道登入
	 */
	ERRCDE_S01_GATEWAY_NOT_EXIST("S01", "1S0101"),	// 查無 GatewayID
	ERRCDE_S01_PASSWORD_ERROR	("S01", "1S0102"),	// 密碼錯誤
//	ERRCDE_S01_PASSWORD_FORMAT	("S01", "1S0103"),	// 密碼格式
	ERRCDE_S01_GATEWAY_LIMIT 	("S01", "1S0104"),	// Gateway 連線數已達限制值，一個Gateway 同時間只能有一個連線
	ERRCDE_S01_TZOFFSET_ERROR	("S01", "1S0105"),	// 時區
	ERRCDE_S01_PASSWORD_NULL	("S01", "1S0107"),	// 密碼為空
//	ERRCDE_S01_GW_FORMAT("S01", "1S0106"),	// Gateway格式錯誤、為空
	
	/**
	 * S10: 事件回報
	 */
	ERRCDE_S10_EVTCDE			("S10", "1S1001"),	// 事件未定義 => 確認 EvtType 及對應的 EvtID 是否正確
	ERRCDE_S10_DEVICE_NOT_EXIST	("S10", "1S1002"),	// 裝置不存在 => 裝置代碼是否正確? / 裝置是否已正確地回報
//	ERRCDE_S10_EVTTYPE_ERROR	("S10", "1S1003"),	// 事件類型（EvtType）未定義或未實作
//	ERRCDE_S10_EVTPARAS_ERROR	("S10", "1S1004"),	// 事件參數（EvtParas）未定義或未實作
	ERRCDE_S10_REPORTTIME_ERROR	("S10", "1S1007"),	// ReportTime欄位錯誤 (預留代碼，暫不實作。)
	ERRCDE_S10_EVTCDE_FORMAT	("S10", "1S1009"),	// 事件回報值不允許為NULL 或格式錯誤

	/**
	 * S34: 裝置組態回報
	 */
	ERRCDE_S34_ATTRLIST_ERROR	("S34", "1S3401"),	// AttrList錯誤
	ERRCDE_S34_ATTR_UNDEFINE	("S34", "1S3402"),	// 未定義的屬性 => Table4
	ERRCDE_S34_HEXTOASCII_ERROR ("S34", "1S3403"),	// AttValue轉碼錯誤
//	ERRCDE_S34_ATTRVALUE_ERROR	("S34", "1S3404"),	// AttrValue錯誤 => 如何判斷??
//	ERRCDE_S34_MESSAGE_ERROR	("S34", "1S3405"),	// 訊息欄位錯誤 => 什麼欄位? 如何判斷??
//	ERRCDE_S34_EVTCDE_FORMAT	("S34", "1S3407"),	// 資料庫查無裝置 => 和1S3403有什麼差別
	
	/**
	 * S31: 裝置回報
	 */
//	ERRCDE_S31_GATEWAYID_ERROR	("S31", "1S3101"),	// GatewayID與註冊不一致
	ERRCDE_S31_DEVICE_COUNT		("S31", "1S3102"),	// Count值與ReportField的總數不一致 
//	ERRCDE_S31_DEVICE_NOT_EXIST	("S31", "1S3103"),	// Gateway底下的設備不存在。
	ERRCDE_S31_REGISTER_FAILED	("S31", "1S3104"),	// 無法處理裝置註冊 
	ERRCDE_S31_REPORTFIELD_ERROR("S31", "1S3105"),	// ReportField 欄位錯誤  (LinkType與DeviceExtType 不在Table2 與Table3的定義內時回報此錯誤)
	ERRCDE_S31_DEVICE_NAMING	("S31", "1S3106"),	// 回報裝置清單含有不支援的 DeviceID 

//	/**
//	 * S07: 偵測連線
//	 */
//	ERRCDE_S25_ATTRID_FORMAT	("S07", "1S07"),	// AttrID_AttrVal解析錯誤
	
	/**
	 * S25: 通用電力量測回報
	 */
	ERRCDE_S25_ATTRID_FORMAT	("S25", "1S2501"),	// AttrID_AttrVal解析錯誤
	ERRCDE_S25_ATTRID_UNDEFINE	("S25", "1S2502"),	// 未定義的屬性(AttrID)
	ERRCDE_S25_DEVICE_NOT_EXIST	("S25", "1S2503"),	// 無此設備(Device)
	ERRCDE_S25_PHASE_ERROR		("S25", "1S2507"),	// 裝置為單相電表，量測回報屬性含三相屬性
	

	ERRCDE_A50_ATTRID_FORMAT	("A50", "1S5001"),
	ERRCDE_C50_ATTRID_FORMAT	("C50", "1S5001"),
	ERRCDE_A51_ATTRID_FORMAT	("A51", "1S5101"),
	ERRCDE_C51_ATTRID_FORMAT	("C51", "1S5101");
	
	
    private String func;
    private String errcode;
    
    private ErrorCodeEnum(String func, String errcode) {
        this.func = func;
        this.errcode = errcode;
    }

    // 呼叫此方法 可以根據Enum變數取得編號及地點
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "func: "+func+ ", 錯誤代碼: "+errcode;
    }
    
    // 呼叫此方法 可以根據Enum變數取得編號
    public String getFunc() {
        return func;
    }
    
    // 呼叫此方法 可以根據Enum變數取得編號及地點
    public String getErrCode() {
        return errcode;
    }
    
    // 呼叫此方法 可以根據編號 找到地點
    public static ErrorCodeEnum ByFunc(final String func) {
        for (ErrorCodeEnum e : ErrorCodeEnum.values()) {
            if (e.func.contentEquals(func)) {
                return e;
            }
        }
        return null;
    }
    
    // 呼叫此方法 可以根據編號 找到地點
    public static ErrorCodeEnum ByErrCode(final String errcode) {
         for (ErrorCodeEnum e : ErrorCodeEnum.values()) {
             if (e.errcode.contentEquals(errcode)) {
                 return e;
             }
         }
         return null;
     }
}
