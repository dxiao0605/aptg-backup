// import {call,put,takeLatest} from 'redux-saga/effects';

import {
    SET_TREE_FUNCTIONID,   //指定目前頁面
    RESET_TREE_FUNCTIONID, //還原預設值
    UPDATE_LOGO,           //更新目前LOGO 
} from '../constants/MainNav-action-type';


// 指定目前頁面
export const setTreeFunctionId = (functionId) => ({
    type: SET_TREE_FUNCTIONID,
    payload: {
        treeFunctionId: functionId,
    }
})

// 還原預設值
export const resetTreeFunctionId = () => ({
    type: RESET_TREE_FUNCTIONID,
    payload: {
    }
})

// 更新目前LOGO
export const updateLogo = (boolean) => ({
    type: UPDATE_LOGO,
    payload: {
        boolean: boolean
    }
})