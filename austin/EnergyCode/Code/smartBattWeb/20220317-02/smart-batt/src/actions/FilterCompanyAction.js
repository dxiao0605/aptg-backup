import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_FILTERCOMPANY_LOAD,
    FETCH_FILTERCOMPANY_SUCCESS,
    FETCH_FILTERCOMPANY_ERROR,
} from '../constants/FilterCompany-action-type';
import { apipath } from '../utils/ajax';
const apiUrl = apipath();


export const fetchFilterCompanyLoad = (data) => ({
    type: FETCH_FILTERCOMPANY_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        filterCompany:[],
        filterCompanyError: '',
    }
})

export const fetchFilterCompanySuccess = (data) => ({
    type: FETCH_FILTERCOMPANY_SUCCESS,
    payload: {
        loading: false,
        filterCompany: data,
        filterCompanyError: '',
    }
})

export const fetchFilterCompanyError = (error) => ({
    type: FETCH_FILTERCOMPANY_ERROR,
    payload: {
        loading: false,
        filterCompany: [],
        filterCompanyError: error,
    }
})

function* fetchFilterCompany(data) {
    const { token, company } = data.payload;
    const url = `${apiUrl}getCompanyList`;
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
            yield put(fetchFilterCompanySuccess(data.msg.List))
        }else{
            yield put(fetchFilterCompanyError(data.msg))
        }
    }
    catch(error){
        yield put(fetchFilterCompanyError(error.message));
    }
}

export function* FilterCompanySaga(){
    yield takeLatest(FETCH_FILTERCOMPANY_LOAD,fetchFilterCompany);
}