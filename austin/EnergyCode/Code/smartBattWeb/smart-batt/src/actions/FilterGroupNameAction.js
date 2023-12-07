import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_FILTERGROUPNAME_LOAD,
    FETCH_FILTERGROUPNAME_SUCCESS,
    FETCH_FILTERGROUPNAME_ERROR,
} from '../constants/FilterGroupName-action-type';
import { apipath } from '../utils/ajax';


export const fetchFilterGroupNameLoad = (data) => ({
    type: FETCH_FILTERGROUPNAME_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        filterGroupName:[],
        filterGroupNameError: '',
    }
})

export const fetchFilterGroupNameSuccess = (data) => ({
    type: FETCH_FILTERGROUPNAME_SUCCESS,
    payload: {
        loading: false,
        filterGroupName: data,
        filterGroupNameError: '',
    }
})

export const fetchFilterGroupNameError = (error) => ({
    type: FETCH_FILTERGROUPNAME_ERROR,
    payload: {
        loading: false,
        filterGroupName: [],
        filterGroupNameError: error,
    }
})

function* fetchFilterGroupName(data) {
    const { token, company } = data.payload;
    const url = `${apipath()}getGroupNameList`;
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
            yield put(fetchFilterGroupNameSuccess(data.msg.List))
        }else{
            yield put(fetchFilterGroupNameError(data.msg))
        }
    }
    catch(error){
        yield put(fetchFilterGroupNameError(error.message));
    }
}

export function* FilterGroupNameSaga(){
    yield takeLatest(FETCH_FILTERGROUPNAME_LOAD,fetchFilterGroupName);
}