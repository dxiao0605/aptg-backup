import { call, put, takeLatest } from 'redux-saga/effects';
import {
    INIT_LASTBBLIST,
    UPD_LASTBBLIST,
    FETCH_LASTBBLIST_LOAD,
    FETCH_LASTBBLIST_SUCCESS,
    FETCH_LASTBBLIST_ERROR,
} from '../constants/LastBBList-action-type';
import { apipath } from '../utils/ajax';
const apiUrl = apipath();


// 初始化BB設定資料(批次時預設資料)
export const initialBBList = (list) => ({
    type:INIT_LASTBBLIST,
    payload: {
        lastBBList: {// 內阻設定測試值(BB)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "BB",
            IRTestTime: 20,
            BatteryCapacity: 100,
            CorrectionValue: 55,
            Resistance: '1.5',
            UserName: list.username,
        },
        lastBBDate: '',
        lastBBListError: '',
    }
})

// 更新BB(187)設定清單
export const updLastBBList = (data) => ({
    type: UPD_LASTBBLIST,
    payload: {
        lastBBList: data,
    }
})

// 取得BB設定清單
export const fetchLastBBListLoad = (data) => ({
    type:FETCH_LASTBBLIST_LOAD,
    payload: {
        token: data.token,
        language: data.curLanguage,
        timezone: data.timeZone,
        company: data.company,
        batteryGroupID: data.batteryGroupID,
        username: data.username,
        lastBBList:{},
        lastBBDate: '',
        lastBBListError: '',
    }
})
export const fetchLastBBListSuccess = (list) => ({
    type:FETCH_LASTBBLIST_SUCCESS,
    payload: {
        lastBBList: {
            // 內阻設定測試值(BB)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "BB",
            IRTestTime: Number(list.data.IRTestTime),
            BatteryCapacity: Number(list.data.BatteryCapacity),
            CorrectionValue: Number(list.data.CorrectionValue),
            Resistance: list.data.Resistance,
            UserName: list.username,
        },
        lastBBDate: list.data.LastSettingTime,
        lastBBListError: '',
    }
})
export const fetchLastBBListError = (error) => ({
    type:FETCH_LASTBBLIST_ERROR,
    payload: {
        lastBBList:{},
        lastBBDate: '',
        lastBBListError: error,
    }
})

function* fetchLastBBList(data) {
    const {token,language,timezone,company,batteryGroupID,username} = data.payload;
    const url = `${apiUrl}getIRSetting?batteryGroupId=${batteryGroupID}`;
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
            // console.log('fetchLastBBList',data.msg)
            yield put(fetchLastBBListSuccess({data:data.msg,batteryGroupID,username}));
        }else{
            console.error('Error',data.msg)
            yield put(fetchLastBBListError(data.msg))
        }
    }
    catch(error){
        console.error('Error',error.message)
        yield put(fetchLastBBListError(error.message));
    }

}
export function* LastBBListSaga(){
    yield takeLatest(FETCH_LASTBBLIST_LOAD,fetchLastBBList);
}