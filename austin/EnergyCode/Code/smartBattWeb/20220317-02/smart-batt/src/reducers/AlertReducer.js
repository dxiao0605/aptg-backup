import {
    SET_INITALERT_THEADER,  //初始化告警表格
    UPD_UNSOLVED_THEADER,   //更新目前未解決告警表格欄位顯示隱藏
    UPD_SOLVED_THEADER,     //更新目前已解決告警表格欄位顯示隱藏
} from '../constants/Alert-action-type';

// 初始化
const initialState = {
    unsolvedTheader:[                                                   // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏 ) 
        {id:'1315',sortName:'EventTypeCode',fixed:false,active:true},   // 告警類型 
        {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
        {id:'1028',sortName:'Country',fixed:false,active:false},        // 國家
        {id:'1029',sortName:'Area',fixed:false,active:false},           // 地域
        {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
        {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
        // {id:'1031',sortName:'Address',fixed:false,active:false},        // 地址
        {id:'1026',sortName:'BatteryGroupID',fixed:false,active:true},   // 電池組ID
        {id:'1027',sortName:'InstallDate',fixed:false,active:false},    // 安裝日期
        {id:'1030',sortName:'BatteryType',fixed:false,active:false},    // 型號
        {id:'1036',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
        {id:'20',sortName:'',fixed:false,active:true},                  // 內阻[UΩ]
        {id:'21',sortName:'',fixed:false,active:true},                  // 毫內阻[mΩ]
        {id:'22',sortName:'',fixed:false,active:true},                  // 電導值[S]
        {id:'1017',sortName:'',fixed:false,active:true},                // 電壓[V]
        {id:'1018',sortName:'Temperature',fixed:false,active:true},     // 溫度
        // {id:'1316',sortName:'OccurTime',fixed:false,active:true},       // 發生時間
    ],
    solvedTheader:[                                                      // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
        {id:'1315',sortName:'EventTypeCode',fixed:false,active:true},   // 告警類型 
        {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
        {id:'1028',sortName:'Country',fixed:false,active:true},         // 國家
        {id:'1029',sortName:'Area',fixed:false,active:true},            // 地域
        {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
        {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
        {id:'1026',sortName:'BatteryGroupID',fixed:false,active:true},  // 電池組ID
        {id:'1027',sortName:'InstallDate',fixed:false,active:true},     // 安裝日期
        {id:'1030',sortName:'BatteryType',fixed:false,active:true},     // 型號
        {id:'1036',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
        {id:'20',sortName:'',fixed:false,active:true},                  // 內阻[UΩ]
        {id:'21',sortName:'',fixed:false,active:true},                  // 毫內阻[mΩ]
        {id:'22',sortName:'',fixed:false,active:true},                  // 電導值[S]
        {id:'1017',sortName:'',fixed:false,active:true},                // 電壓[V]
        {id:'1018',sortName:'Temperature',fixed:false,active:true},     // 溫度
        {id:'1312',sortName:'CloseTime',fixed:false,active:true},       // 解決時間
    ],
}

const AlertReducer = (state = initialState , action) => {
    switch(action.type){
        case SET_INITALERT_THEADER:
            return {
                unsolvedTheader:[                                                   // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏 ) 
                    {id:'1315',sortName:'EventTypeCode',fixed:false,active:true},   // 告警類型 
                    {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
                    {id:'1028',sortName:'Country',fixed:false,active:false},        // 國家
                    {id:'1029',sortName:'Area',fixed:false,active:false},           // 地域
                    {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
                    {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
                    // {id:'1031',sortName:'Address',fixed:false,active:false},        // 地址
                    {id:'1026',sortName:'BatteryGroupID',fixed:false,active:true},   // 電池組ID
                    {id:'1027',sortName:'InstallDate',fixed:false,active:false},    // 安裝日期
                    {id:'1030',sortName:'BatteryType',fixed:false,active:false},    // 型號
                    {id:'1036',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
                    {id:'20',sortName:'',fixed:false,active:true},                  // 內阻[UΩ]
                    {id:'21',sortName:'',fixed:false,active:true},                  // 毫內阻[mΩ]
                    {id:'22',sortName:'',fixed:false,active:true},                  // 電導值[S]
                    {id:'1017',sortName:'',fixed:false,active:true},                // 電壓[V]
                    {id:'1018',sortName:'Temperature',fixed:false,active:true},     // 溫度
                    // {id:'1316',sortName:'OccurTime',fixed:false,active:true},       // 發生時間
                ],
                solvedTheader:[                                                      // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                    {id:'1315',sortName:'EventTypeCode',fixed:false,active:true},   // 告警類型 
                    {id:'1064',sortName:'Company',fixed:false,active:false},        // 公司
                    {id:'1028',sortName:'Country',fixed:false,active:true},         // 國家
                    {id:'1029',sortName:'Area',fixed:false,active:true},            // 地域
                    {id:'1012',sortName:'GroupID',fixed:false,active:true},         // 站台編號
                    {id:'1013',sortName:'GroupName',fixed:false,active:true},       // 站台名稱
                    {id:'1026',sortName:'BatteryGroupID',fixed:false,active:true},  // 電池組ID
                    {id:'1027',sortName:'InstallDate',fixed:false,active:true},     // 安裝日期
                    {id:'1030',sortName:'BatteryType',fixed:false,active:true},     // 型號
                    {id:'1036',sortName:'RecTime',fixed:false,active:true},         // 數據更新時間
                    {id:'20',sortName:'',fixed:false,active:true},                  // 內阻[UΩ]
                    {id:'21',sortName:'',fixed:false,active:true},                  // 毫內阻[mΩ]
                    {id:'22',sortName:'',fixed:false,active:true},                  // 電導值[S]
                    {id:'1017',sortName:'',fixed:false,active:true},                // 電壓[V]
                    {id:'1018',sortName:'Temperature',fixed:false,active:true},     // 溫度
                    {id:'1312',sortName:'CloseTime',fixed:false,active:true},       // 解決時間
                ],
            }
        case UPD_UNSOLVED_THEADER:
            return{
                ...state,
                unsolvedTheader: action.payload.unsolvedTheader,
            }
        case UPD_SOLVED_THEADER:
            return{
                ...state,
                solvedTheader: action.payload.solvedTheader,
            }
        default:
            return state
    }
}
export default AlertReducer;