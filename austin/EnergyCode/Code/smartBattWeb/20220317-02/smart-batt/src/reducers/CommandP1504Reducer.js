import {
    SET_TABLEHEADER_P1504,
    SET_AREALIST_P1504,
    SET_GROUPLIST_P1504,
    SET_SUBMITKEYLIST_P1504,
    RESET_ALL_P1504,
} from '../constants/Command-action-type';
import { initAreaList, initGroupList, initSubmitKeyList } from '../pages/Command/Page1504/InitDataFormat';


// 初始化
const initialState = {
    tableHeader: [ // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
        { id: '1064', sortName: 'Company', fixed: false, active: true },// 公司
        { id: '1028', sortName: 'Country', fixed: false, active: true },// 國家
        { id: '1029', sortName: 'Area', fixed: false, active: true },// 地域
        { id: '1012', sortName: 'GroupID', fixed: false, active: true },// 站台編號
        { id: '1013', sortName: 'GroupName', fixed: false, active: true },// 站台名稱
        { id: '1030', sortName: 'BatteryTypeName', fixed: false, active: true },// 電池型號
        { id: '1026', sortName: 'BatteryGroupID', fixed: false, active: true },// 電池組ID                
        { id: '1566', sortName: 'Command', fixed: false, active: true },// 指令
        { id: '1066', sortName: 'SendTime', fixed: false, active: true },// 傳送時間
        { id: '1067', sortName: 'PublishTime', fixed: false, active: true },// 發佈時間
        { id: '1068', sortName: 'AckTime', fixed: false, active: true },// Ack時間
        { id: '1069', sortName: 'ResponseTime', fixed: false, active: true },// Resp時間
        { id: '1070', sortName: 'Response', fixed: false, active: true },// 回應訊息
        { id: '1071', sortName: 'Config', fixed: false, active: true },// 指令設定值
    ],
    //紀錄公司別查詢到的國家/地域
    areaList: [...initAreaList],
    //紀錄公司別查詢到的站台選單
    groupList: [...initGroupList],
    //紀錄table Key值
    submitKeyList: [...initSubmitKeyList],
}

const CommandP1504Reducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_TABLEHEADER_P1504://表格欄位名稱
            return {
                ...state,
                tableHeader: [...action.payload.tableHeader]
            }
        case SET_AREALIST_P1504://紀錄公司別查詢到的國家/地域
            return {
                ...state,
                areaList: [...action.payload.list]
            }
        case SET_GROUPLIST_P1504://紀錄公司別查詢到的站台選單
            return {
                ...state,
                groupList: [...action.payload.list]
            }
        case SET_SUBMITKEYLIST_P1504://紀錄table Key值
            return {
                ...state,
                submitKeyList: [...action.payload.list]
            }
        case RESET_ALL_P1504://紀錄table Key值
            return {
                ...initialState,
            }            
        default:
            return state
    }
}
export default CommandP1504Reducer;