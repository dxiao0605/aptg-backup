import { apipath } from '../utils/ajax';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
    SET_INITALERT_COUNT,        //初始化告警筆數
    FETCH_ALERTCOUNT_LOAD,      //告警數筆數
    FETCH_ALERTCOUNT_SUCCESS,
    FETCH_ALERTCOUNT_ERROR,
}from '../constants/Alert-action-type';

export const setInitAlertCount = () => ({
    type:SET_INITALERT_COUNT,
    // payload:{}
})
// set data
export const fetchAlertCountLoad = (data) => ({
    type: FETCH_ALERTCOUNT_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        alertCount: 0,
        alertCountErrorMsg: '',
        
    }
})
export const fetchAlertCountSuccess = (data) => ({
    type: FETCH_ALERTCOUNT_SUCCESS,
    payload: {
        loading: false,
        alertCount: data,
        alertCountErrorMsg: '',
        
    }
})
export const fetchAlertCountError = (error) => ({
    type: FETCH_ALERTCOUNT_ERROR,
    payload: {
        loading: false,
        alertCount: 0,
        alertCountErrorMsg: error,
        
    }
})

function* fetchAlertCount(data) {
    const {token,company} = data.payload;
    const url = `${apipath()}getAlertCount`;
    // 使用 data 接收請求的資料
    try{
        const data = yield call(
            () => fetch(url,{
                method: 'GET',
                headers: new Headers({
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'token': token,
                    'company': company
                })
            }).then(response => response.json())
        );

        if(data.code === '00'){
            yield put(fetchAlertCountSuccess(data.msg.Count))
        }else{
            yield put(fetchAlertCountError(data.msg))
        }
    }
    catch(error) {
        yield put(fetchAlertCountError(error.message));
    }
}
export function* AlertCountSaga() {
    yield takeLatest(FETCH_ALERTCOUNT_LOAD,fetchAlertCount);
}