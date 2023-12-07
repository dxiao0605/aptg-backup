import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_LANGUAGELIST_LOAD,
    FETCH_LANGUAGELIST_SUCCESS,
    FETCH_LANGUAGELIST_ERROR
}from  '../constants/LanguageList-action-type';
import { apipath } from '../utils/ajax';

export const fetchLanguageListLoad = (token) => ({
    type: FETCH_LANGUAGELIST_LOAD,
    payload: {
        loading: true,
        token: token,
        languageList: [],
        languageListErrorMsg: '',
    }
})
export const fetchLanguageListSuccess = (data) => ({
    type: FETCH_LANGUAGELIST_SUCCESS,
    payload: {
        loading: false,
        languageList: data,
        languageListErrorMsg: '',
    }
})
export const fetchLanguageListError = (error) => ({
    type: FETCH_LANGUAGELIST_ERROR,
    payload: {
        loading: false,
        languageList: [],
        languageListErrorMsg: error,
    }
})

function* fetchLanguageList(data) {
    const { token } = data.payload;
    // 使用data接收請求的資料
    try{
        const url = `${apipath()}getLanguageList`;
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
            yield put(fetchLanguageListSuccess(data.msg.List));
        }else{
            yield put(fetchLanguageListError(data.msg));
        }
    }
    catch(error) {
        yield put(fetchLanguageListError(error.message))
    }
}

export function* LanguageListSaga() {
    yield takeLatest(FETCH_LANGUAGELIST_LOAD,fetchLanguageList);
}


