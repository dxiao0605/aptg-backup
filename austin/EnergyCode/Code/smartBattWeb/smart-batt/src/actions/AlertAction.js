// import {call,put,takeLatest} from 'redux-saga/effects';

import {
    SET_INITALERT_THEADER,  //初始化告警表格
    UPD_UNSOLVED_THEADER,   //更新目前未解決告警表格欄位顯示隱藏
    UPD_SOLVED_THEADER,     //更新目前已解決告警表格欄位顯示隱藏
} from '../constants/Alert-action-type';

// 初始化告警表格
export const setInitAlertTheader = () => ({
    type: SET_INITALERT_THEADER,
    // payload:{},
})

// 未解決告警表格欄位顯示隱藏
export const updUnsolvedTheader = (list) => ({
    type: UPD_UNSOLVED_THEADER,
    payload: {
        unsolvedTheader: list,
    }
})
// 已解決告警表格欄位顯示隱藏
export const updSolvedTheader = (list) => ({
    type: UPD_SOLVED_THEADER,
    payload: {
        solvedTheader: list,
    }
})