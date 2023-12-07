import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_FILTERBATTERYGROUPID_LOAD,
    FETCH_FILTERBATTERYGROUPID_SUCCESS,
    FETCH_FILTERBATTERYGROUPID_ERROR,
} from '../constants/FilterBatteryGroupId-action-type';
import { apipath } from '../utils/ajax';


export const fetchFilterBatteryGroupIdLoad = (data) => ({
    type: FETCH_FILTERBATTERYGROUPID_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        filterBatteryGroupId:[],
        filterBatteryGroupIdError: '',
    }
})

export const fetchFilterBatteryGroupIdSuccess = (data) => ({
    type: FETCH_FILTERBATTERYGROUPID_SUCCESS,
    payload: {
        loading: false,
        filterBatteryGroupId: data,
        filterBatteryGroupIdError: '',
    }
})

export const fetchFilterBatteryGroupIdError = (error) => ({
    type: FETCH_FILTERBATTERYGROUPID_ERROR,
    payload: {
        loading: false,
        filterBatteryGroupId: [],
        filterBatteryGroupIdError: error,
    }
})

function* fetchFilterBatteryGroupId(data) {
    const { token, company } = data.payload;
    const url = `${apipath()}getBatteryGroupIdList`;
    // 使用 data 接收請求的資料
    try{
        const data = yield call(
            ()=> fetch(url,{
                method: 'GET',
                headers: new Headers({
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'token': token,
                    'company': company
                })
            }).then(response => response.json())
        );

        if( data.code === '00'){
            yield put(fetchFilterBatteryGroupIdSuccess(data.msg.List))
        }else{
            yield put(fetchFilterBatteryGroupIdError(data.msg))
        }
    }
    catch(error){
        yield put(fetchFilterBatteryGroupIdError(error.message));
    }
}

export function* FilterBatteryGroupIdSaga(){
    yield takeLatest(FETCH_FILTERBATTERYGROUPID_LOAD,fetchFilterBatteryGroupId);
}