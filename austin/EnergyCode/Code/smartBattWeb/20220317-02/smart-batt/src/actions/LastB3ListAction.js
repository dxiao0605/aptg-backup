import { call, put, takeLatest } from 'redux-saga/effects';
import {
    INIT_LASTB3LIST,
    UPD_LASTB3LIST,
    FETCH_LASTB3LIST_LOAD,
    FETCH_LASTB3LIST_SUCCESS,
    FETCH_LASTB3LIST_ERROR,
} from '../constants/LastB3List-action-type';
import { apipath } from '../utils/ajax';
const apiUrl = apipath();


// 初始化B3設定資料(批次時預設資料)
export const initialB3List = (list) => ({
    type:INIT_LASTB3LIST,
    payload: {
        lastB3List: {// 校正內阻(B3)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "B3",
            IR:[],
            UserName: list.username,
        },
        lastB3Date: '',
        lastB3ListError: '',
    }
})

// 更新B3(179)設定清單
export const updLastB3List = (data) => ({
    type: UPD_LASTB3LIST,
    payload: {
        lastB3List: data,
    }
})

// 取得B3設定清單
export const fetchLastB3ListLoad = (data) => ({
    type:FETCH_LASTB3LIST_LOAD,
    payload: {
        token: data.token,
        language: data.curLanguage,
        timezone: data.timeZone,
        company: data.company,
        batteryGroupID: data.batteryGroupID,
        username: data.username,
        lastB3List:{},
        lastB3Date: '',
        lastB3ListError: '',
    }
})
export const fetchLastB3ListSuccess = (list) => ({
    type:FETCH_LASTB3LIST_SUCCESS,
    payload: {
        lastB3List: {
            // 校正內阻(B3)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "B3",
            IR: list.data.IR,
            UserName: list.username,
        },
        lastB3Date: list.data.LastSettingTime,
        lastB3ListError: '',
    }
})
export const fetchLastB3ListError = (error) => ({
    type:FETCH_LASTB3LIST_ERROR,
    payload: {
        lastB3List:{},
        lastB3Date: '',
        lastB3ListError: error,
    }
})

function* fetchLastB3List(data) {
    const {token,language,timezone,company,batteryGroupID,username} = data.payload;
    const url = `${apiUrl}getCorrectionIR?batteryGroupId=${batteryGroupID}`;
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
            // console.log('fetchLastB3List',data.msg)
            yield put(fetchLastB3ListSuccess({data:data.msg,batteryGroupID,username}));
        }else{
            console.error('Error',data.msg)
            yield put(fetchLastB3ListError(data.msg))
        }
    }
    catch(error){
        console.error('Error',error.message)
        yield put(fetchLastB3ListError(error.message));
    }

}
export function* LastB3ListSaga(){
    yield takeLatest(FETCH_LASTB3LIST_LOAD,fetchLastB3List);
}