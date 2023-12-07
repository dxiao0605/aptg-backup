// import { call, put, takeLatest } from 'redux-saga/effects';

import {
    SET_INITBATTDATA,           //登出後初始化資料
    SET_GROUPID,                //設定電池數據(第一層)站台編號(無使用)
    SET_BATTERYGROUPID,         //設定電池數據(第二層)電池組ID(無使用)
    SET_BATTINTERNALID,         //設定電池組編號(BatteryGroupID,BatteryID)
    SET_BATTINTERNALID_ALERT,   //電池數據第二層至未解決告警(BatteryID)
    SET_BUTTONCONTROLLIST,      //取得電池數據(第二層)按鈕控制清單
    UPD_BATTGROUPTHEADER,       //更新目前電池數據(第一層)表格欄位顯示隱藏
    UPD_BATTDATATHEADER,        //更新目前電池數據(第二層)表格欄位顯示隱藏

} from '../constants/BattData-action-type';
// 初始化資料
export const setInitBattData = () => ({
    type:SET_INITBATTDATA,
    // payload: {}
})
// 電池數據 站台編號(無使用)
export const setGroupID = (value) => ({
    type:SET_GROUPID,
    payload:{
        groupInternalId: value
    }
})
// 電池數據 電池組ID(無使用)
export const setBatteryGroupID = (value) => ({
    type: SET_BATTERYGROUPID,
    payload: {
        batteryGroupId: value
    }
})
// 電池數據(2),電池歷史(1),至電池歷史(2)電池組ID編號
export const setBattInternalID = (value) => ({
    type: SET_BATTINTERNALID,
    payload: {
        battInternalId: value
    }
})
// 電池數據(2),至未解決告警電池組ID編號
export const setBattInternalIDAlert = (value) => ({
    type: SET_BATTINTERNALID_ALERT,
    payload: {
        battInternalIdAlert: value
    }
})
// 電池數據按鈕權限
export const setButtonControlList = (list) => ({
    type: SET_BUTTONCONTROLLIST,
    payload: {
        buttonControlList: list,
    }
})
// 電池數據(第一層)表格標題
export const updBattGroupTheader = (list) => ({
    type: UPD_BATTGROUPTHEADER,
    payload: {
        battGroupTheader: list,
    }
})
// 電池數據(第二層)表格標題
export const updBattDataTheader = (list) => ({
    type: UPD_BATTDATATHEADER,
    payload: {
        battDataTheader: list,
    }
})