import {
    SET_INITBATTDATA,
    SET_BUTTONCONTROLLIST,      //取得電池數據按鈕控制清單
    SET_GROUPID,                //設定電池數據(第一層)站台編號
    SET_BATTERYGROUPID,         //設定電池數據(第二層)電池組ID
    SET_BATTINTERNALID,         //跳轉至電池歷史(2)時使用
    SET_BATTINTERNALID_ALERT,   //電池數據第二層至未解決告警(BatteryID)
    UPD_BATTGROUPTHEADER,       //更新目前電池數據(第一層)表格欄位顯示隱藏
    UPD_BATTDATATHEADER,        //更新目前電池數據(第二層)表格欄位顯示隱藏
} from '../constants/BattData-action-type';


// 初始化
const initialState = {
    loading: false,
    batchCmdId: [],                                                     // 己選則BatteryGroupID
    groupInternalId: '',
    batteryGroupId: '',
    battInternalId: '',
    battInternalIdAlert: '',
    buttonControlList: [],
    battGroupTheader: [                                                 // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
        {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
        {id:'1028',sortName:'Country',fixed:false,active:true},         // 國家
        {id:'1029',sortName:'Area',fixed:false,active:true},            // 地域
        {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
        {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
        {id:'1401',sortName:'BatteryCount',fixed:false,active:true},    // 電池組數
        {id:'1036',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
        {id:'1402',sortName:'MaxIMP',fixed:false,active:true},          // 最大內阻max[UΩ]
        {id:'1403',sortName:'MinIMP',fixed:false,active:true},          // 最小內阻min[UΩ]
        {id:'1408',sortName:'MaxIMP',fixed:false,active:true},          // 內阻max[mΩ]
        {id:'1409',sortName:'MinIMP',fixed:false,active:true},          // 內阻min[mΩ]
        {id:'1410',sortName:'MaxIMP',fixed:false,active:true},          // 電導值max[S]
        {id:'1411',sortName:'MinIMP',fixed:false,active:true},          // 電導值min[S]
        {id:'1404',sortName:'MaxVol',fixed:false,active:true},          // 最大電壓max[V]
        {id:'1405',sortName:'MinVol',fixed:false,active:true},          // 最小電壓min[V]
        {id:'1406',sortName:'MaxTemperature',fixed:false,active:true},  // 最大溫度max[℃]
        {id:'1407',sortName:'MinTemperature',fixed:false,active:true},  // 最小溫度min[℃]
        {id:'1021',sortName:'StatusCode',fixed:false,active:true},      // 狀態說明
    ],
    battDataTheader: [                                                  // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
        {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
        {id:'1028',sortName:'Country',fixed:false,active:true},         // 國家
        {id:'1029',sortName:'Area',fixed:false,active:true},            // 地域
        {id:'1031',sortName:'Address',fixed:false,active:true},         // 地址
        {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
        {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
        {id:'1026',sortName:'BatteryGroupID',fixed:false,active:true},  // 電池組ID
        {id:'1027',sortName:'InstallDate',fixed:false,active:true},     // 安裝日期
        {id:'1030',sortName:'BatteryType',fixed:false,active:true},     // 型號
        {id:'1015',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
        {id:'20',sortName:'',fixed:false,active:true},                  // 內阻
        {id:'21',sortName:'',fixed:false,active:true},                  // 豪內阻
        {id:'22',sortName:'',fixed:false,active:true},                  // 電導值
        {id:'1017',sortName:'',fixed:false,active:true},                // 電壓
        {id:'1018',sortName:'Temperature',fixed:false,active:true},     // 溫度[℃]
        {id:'1021',sortName:'',fixed:false,active:true},                // 電池狀態
    ],
    // battData: [],
    // battDataErrorMsg: '',
}

const BattDataReducer = ( state = initialState , action) => {
    switch(action.type){
        case SET_INITBATTDATA:
            return {
                loading: false,
                batchCmdId: [],                                                     // 己選則BatteryGroupID
                groupInternalId: '',
                batteryGroupId: '',
                battInternalId: '',
                buttonControlList: [],
                battGroupTheader: [                                                 // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                    {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
                    {id:'1028',sortName:'Country',fixed:false,active:true},         // 國家
                    {id:'1029',sortName:'Area',fixed:false,active:true},            // 地域
                    {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
                    {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
                    {id:'1401',sortName:'BatteryCount',fixed:false,active:true},    // 電池組數
                    {id:'1036',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
                    {id:'1402',sortName:'MaxIMP',fixed:false,active:true},          // 最大內阻max[UΩ]
                    {id:'1403',sortName:'MinIMP',fixed:false,active:true},          // 最小內阻min[UΩ]
                    {id:'1408',sortName:'MaxIMP',fixed:false,active:true},          // 內阻max[mΩ]
                    {id:'1409',sortName:'MinIMP',fixed:false,active:true},          // 內阻min[mΩ]
                    {id:'1410',sortName:'MaxIMP',fixed:false,active:true},          // 電導值max[S]
                    {id:'1411',sortName:'MinIMP',fixed:false,active:true},          // 電導值min[S]
                    {id:'1404',sortName:'MaxVol',fixed:false,active:true},          // 最大電壓max[V]
                    {id:'1405',sortName:'MinVol',fixed:false,active:true},          // 最小電壓min[V]
                    {id:'1406',sortName:'MaxTemperature',fixed:false,active:true},  // 最大溫度max[℃]
                    {id:'1407',sortName:'MinTemperature',fixed:false,active:true},  // 最小溫度min[℃]
                    {id:'1021',sortName:'StatusCode',fixed:false,active:true},      // 狀態說明
                ],
                battDataTheader: [                                                  // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                    {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
                    {id:'1028',sortName:'Country',fixed:false,active:true},         // 國家
                    {id:'1029',sortName:'Area',fixed:false,active:true},            // 地域
                    {id:'1031',sortName:'Address',fixed:false,active:true},         // 地址
                    {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
                    {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
                    {id:'1026',sortName:'BatteryGroupID',fixed:false,active:true},  // 電池組ID
                    {id:'1027',sortName:'InstallDate',fixed:false,active:true},     // 安裝日期
                    {id:'1030',sortName:'BatteryType',fixed:false,active:true},     // 型號
                    {id:'1015',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
                    {id:'20',sortName:'',fixed:false,active:true},                  // 內阻
                    {id:'21',sortName:'',fixed:false,active:true},                  // 豪內阻
                    {id:'22',sortName:'',fixed:false,active:true},                  // 電導值
                    {id:'1017',sortName:'',fixed:false,active:true},                // 電壓
                    {id:'1018',sortName:'Temperature',fixed:false,active:true},     // 溫度[℃]
                    {id:'1021',sortName:'',fixed:false,active:true},                // 電池狀態
                ],
            }
        case SET_GROUPID:
            return{
                ...state,
                groupInternalId: action.payload.groupInternalId,
            }
        case SET_BATTERYGROUPID: 
            return{
                ...state,
                batteryGroupId: action.payload.batteryGroupId,
            }
        case SET_BATTINTERNALID:
            return {
                ...state,
                battInternalId: action.payload.battInternalId,
            }
        case SET_BATTINTERNALID_ALERT:
            return {
                ...state,
                battInternalIdAlert: action.payload.battInternalIdAlert,
            }
        case SET_BUTTONCONTROLLIST:
            return {
                ...state,
                buttonControlList: action.payload.buttonControlList,
            }
        case UPD_BATTGROUPTHEADER:
            return {
                ...state,
                battGroupTheader: action.payload.battGroupTheader,
            }
        case UPD_BATTDATATHEADER:
            return {
                ...state,
                battDataTheader: action.payload.battDataTheader,
            }
        // case FETCH_BATTDATA_LOAD:
        //     return{
        //         ...state,
        //         loading: action.payload.loading,
        //         company: action.payload.company,
        //         groupInternalId: action.payload.groupInternalId,
        //         battDataErrorMsg: '',
        //     }
        // case FETCH_BATTDATA_SUCCESS:
        //     return{
        //         ...state,
        //         loading: action.payload.loading,
        //         battData: action.payload.battData,
        //         battDataErrorMsg: '',
        //     }
        // case FETCH_BATTDATA_ERROR:
        //     return{
        //         ...state,
        //         loading: action.payload.loading,
        //         battData: [],
        //         battDataErrorMsg: action.payload.battDataErrorMsg,
        //     }
        default:
            return state
    }
}
export default BattDataReducer;