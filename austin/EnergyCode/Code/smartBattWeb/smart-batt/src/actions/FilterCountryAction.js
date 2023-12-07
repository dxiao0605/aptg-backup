import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_FILTERCOUNTRY_LOAD,
    FETCH_FILTERCOUNTRY_SUCCESS,
    FETCH_FILTERCOUNTRY_ERROR,
} from '../constants/FilterCountry-action-type';
import { apipath } from '../utils/ajax';


export const fetchFilterCountryLoad = (data) => ({
    type: FETCH_FILTERCOUNTRY_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        filterCountry:[],
        filterCountryError: '',
    }
})

export const fetchFilterCountrySuccess = (data) => ({
    type: FETCH_FILTERCOUNTRY_SUCCESS,
    payload: {
        loading: false,
        filterCountry: data,
        filterCountryError: '',
    }
})

export const fetchFilterCountryError = (error) => ({
    type: FETCH_FILTERCOUNTRY_ERROR,
    payload: {
        loading: false,
        filterCountry: [],
        filterCountryError: error,
    }
})

function* fetchFilterCountry(data) {
    const { token, company } = data.payload;
    const url = `${apipath()}getCountryList`;
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
            yield put(fetchFilterCountrySuccess(data.msg.List))
        }else{
            yield put(fetchFilterCountryError(data.msg))
        }
    }
    catch(error){
        yield put(fetchFilterCountryError(error.message));
    }
}

export function* FilterCountrySaga(){
    yield takeLatest(FETCH_FILTERCOUNTRY_LOAD,fetchFilterCountry);
}