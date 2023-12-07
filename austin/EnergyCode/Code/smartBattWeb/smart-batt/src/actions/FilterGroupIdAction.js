import { call, put, takeLatest } from 'redux-saga/effects';
import {
    FETCH_FILTERGROUPID_LOAD,
    FETCH_FILTERGROUPID_SUCCESS,
    FETCH_FILTERGROUPID_ERROR,
} from '../constants/FilterGroupId-action-type';
import { apipath } from '../utils/ajax';


export const fetchFilterGroupIdLoad = (data) => ({
    type: FETCH_FILTERGROUPID_LOAD,
    payload: {
        loading: true,
        token: data.token,
        company: data.company,
        filterGroupId:[],
        filterGroupIdError: '',
    }
})

export const fetchFilterGroupIdSuccess = (data) => ({
    type: FETCH_FILTERGROUPID_SUCCESS,
    payload: {
        loading: false,
        filterGroupId: data,
        filterGroupIdError: '',
    }
})

export const fetchFilterGroupIdError = (error) => ({
    type: FETCH_FILTERGROUPID_ERROR,
    payload: {
        loading: false,
        filterGroupId: [],
        filterGroupIdError: error,
    }
})

function* fetchFilterGroupId(data) {
    const { token, company } = data.payload;
    const url = `${apipath()}getGroupIdList?groupId=`;
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
            yield put(fetchFilterGroupIdSuccess(data.msg.List))
        }else{
            yield put(fetchFilterGroupIdError(data.msg))
        }
    }
    catch(error){
        yield put(fetchFilterGroupIdError(error.message));
    }
}

export function* FilterGroupIdSaga(){
    yield takeLatest(FETCH_FILTERGROUPID_LOAD,fetchFilterGroupId);
}