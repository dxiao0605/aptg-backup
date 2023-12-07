import { call, put, takeLatest } from 'redux-saga/effects';
import {
    INIT_LASTB5LIST,
    UPD_LASTB5LIST,
    FETCH_LASTB5LIST_LOAD,
    FETCH_LASTB5LIST_SUCCESS,
    FETCH_LASTB5LIST_ERROR,
} from '../constants/LastB5List-action-type';
import { apipath } from '../utils/ajax';


// 初始化B5設定資料(批次時預設資料)
export const initialB5List = (list) => ({
    type:INIT_LASTB5LIST,
    payload: {
        lastB5List: {// 校正電壓(B5)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "B5",
            Vol:[],
            UserName: list.username,
        },
        lastB5Date: '',
        lastB5ListError: '',
    }
})

// 更新B5(181)設定清單
export const updLastB5List = (data) => ({
    type: UPD_LASTB5LIST,
    payload: {
        lastB5List: data,
    }
})

// 取得B5設定清單
export const fetchLastB5ListLoad = (data) => ({
    type:FETCH_LASTB5LIST_LOAD,
    payload: {
        token: data.token,
        language: data.curLanguage,
        timezone: data.timeZone,
        company: data.company,
        batteryGroupID: data.batteryGroupID,
        username: data.username,
        lastB5List:{},
        lastB5Date: '',
        lastB5ListError: '',
    }
})
export const fetchLastB5ListSuccess = (list) => ({
    type:FETCH_LASTB5LIST_SUCCESS,
    payload: {
        lastB5List: {
            // 校正電壓(B5)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "B5",
            Vol: list.data.Vol,
            UserName: list.username,
        },
        lastB5Date: list.data.LastSettingTime,
        lastB5ListError: '',
    }
})
export const fetchLastB5ListError = (error) => ({
    type:FETCH_LASTB5LIST_ERROR,
    payload: {
        lastB5List:{},
        lastB5Date: '',
        lastB5ListError: error,
    }
})

function* fetchLastB5List(data) {
    const {token,language,timezone,company,batteryGroupID,username} = data.payload;
    const url = `${apipath()}getCorrectionVol?batteryGroupId=${batteryGroupID}`;
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
            // console.log('fetchLastB5List',data.msg)
            yield put(fetchLastB5ListSuccess({data:data.msg,batteryGroupID,username}));
        }else{
            console.error('Error',data.msg)
            yield put(fetchLastB5ListError(data.msg))
        }
    }
    catch(error){
        console.error('Error',error.message)
        yield put(fetchLastB5ListError(error.message));
    }

}
export function* LastB5ListSaga(){
    yield takeLatest(FETCH_LASTB5LIST_LOAD,fetchLastB5List);
}