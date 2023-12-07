import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_BATTTYPELIST_LOAD,
    FETCH_BATTTYPELIST_SUCCESS,
    FETCH_BATTTYPELIST_ERROR,
} from '../constants/BattTypeList-action-type';
import { apipath } from '../utils/ajax';

// 取得電池型號清單
export const fetchBattTypeListLoad = (data) => ({
    type:FETCH_BATTTYPELIST_LOAD,
    payload: {
        loading: true,
        token: data.token,
        language: data.language,
        timezone: data.timezone,
        company: data.company,          //使用者所屬公司
        companyCode: data.companyCode,  //單筆資料的公司代碼
        battTypeList: [],
        battTypeListError: '',
    }
})
export const fetchBattTypeListSuccess = (data) => ({
    type:FETCH_BATTTYPELIST_SUCCESS,
    payload: {
        battTypeList: data,
        battTypeListError: '',
    }
})
export const fetchBattTypeListError = (error) => ({
    type:FETCH_BATTTYPELIST_ERROR,
    payload: {
        battTypeList: [],
        battTypeListError: error,
    }
})

function* fetchBattTypeList(data) {
    const {token,language,timezone,company,companyCode} = data.payload;
    // console.log('fetchBattTypeList',data)
    const url = `${apipath()}getBattTypeList?companyCode=${companyCode}`;

    // 使用 data 接收請求的資料
    try{
        const data = yield call(
            () => fetch(url,{
                method: 'GET',
                headers: new Headers({
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'token': token,
                    'language':language,
                    'timezone':timezone,
                    'company': company,
                })
            }).then(response => response.json())
        );
        if(data.code === '00'){
            yield put(fetchBattTypeListSuccess(data.msg.List));
        }else{
            yield put(fetchBattTypeListError(data.msg))
        }
    }
    catch(error){
        yield put(fetchBattTypeListError(error.message));
    }

}
export function* BattTypeListSaga(){
    yield takeLatest(FETCH_BATTTYPELIST_LOAD,fetchBattTypeList);
}