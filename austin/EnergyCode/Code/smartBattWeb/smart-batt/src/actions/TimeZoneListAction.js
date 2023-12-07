import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_TIMEZONELIST_LOAD,
    FETCH_TIMEZONELIST_SUCCESS,
    FETCH_TIMEZONELIST_ERROR,
} from '../constants/TimeZone-action-type';
import { apipath } from '../utils/ajax';

export const fetchTimeZoneListLoad = (token) => ({
    type: FETCH_TIMEZONELIST_LOAD,
    payload: {
        loading: true,
        token: token,
        timeZoneList: [],
        timeZoneListErrorMsg:'',
    }
})
export const fetchTimeZoneListSuccess = (data) => ({
    type: FETCH_TIMEZONELIST_SUCCESS,
    payload: {
        loading: false,
        timeZoneList: data,
        timeZoneListErrorMsg:'',
    }
})
export const fetchTimeZoneListError = (error) => ({
    type: FETCH_TIMEZONELIST_ERROR,
    payload: {
        loading: false,
        timeZoneList: [],
        timeZoneListErrorMsg:error,
    }
})

function* fetchTimeZoneList(data){
    const {token} = data.payload;
    // 使用data接收請求的資料
    try{
        const url = `${apipath()}getTimeZoneList`;
        const data = yield call(
            () => fetch(url,{
                method: 'GET',
                headers: new Headers({
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'token': token,
                })
            }).then(response => response.json())
        );
        if(data.code === '00'){
            yield put(fetchTimeZoneListSuccess(data.msg.List));
        }else{
            yield put(fetchTimeZoneListError(data.msg));
        }
    }
    catch(error) {
        yield put(fetchTimeZoneListError(error.message))
    }
}
export function* TimeZoneListSaga() {
    yield takeLatest(FETCH_TIMEZONELIST_LOAD,fetchTimeZoneList)
}
