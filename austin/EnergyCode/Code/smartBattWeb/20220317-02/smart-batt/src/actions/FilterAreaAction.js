import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_FILTERAREA_LOAD,
    FETCH_FILTERAREA_SUCCESS,
    FETCH_FILTERAREA_ERROR,
} from '../constants/FilterArea-action-type';
import { apipath } from '../utils/ajax';
const apiUrl = apipath();


export const fetchFilterAreaLoad = (data) => ({
    type: FETCH_FILTERAREA_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        filterArea:[],
        filterAreaError: '',
    }
})

export const fetchFilterAreaSuccess = (data) => ({
    type: FETCH_FILTERAREA_SUCCESS,
    payload: {
        loading: false,
        filterArea: data,
        filterAreaError: '',
    }
})

export const fetchFilterAreaError = (error) => ({
    type: FETCH_FILTERAREA_ERROR,
    payload: {
        loading: false,
        filterArea: [],
        filterAreaError: error,
    }
})

function* fetchFilterArea(data) {
    const { token, company } = data.payload;
    const url = `${apiUrl}getAreaList`;
    // 使用 data 接收請求的資料
    try{
        const data = yield call(
            ()=> fetch(url,{
                method: 'GET',
                headers: new Headers({
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'token': token,
                    'company': company,
                })
            }).then(response => response.json())
        );

        if( data.code === '00'){
            yield put(fetchFilterAreaSuccess(data.msg.List))
        }else{
            yield put(fetchFilterAreaError(data.msg))
        }
    }
    catch(error){
        yield put(fetchFilterAreaError(error.message));
    }
}

export function* FilterAreaSaga(){
    yield takeLatest(FETCH_FILTERAREA_LOAD,fetchFilterArea);
}