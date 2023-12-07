import { call, put, takeLatest } from 'redux-saga/effects';
import {
    INIT_LASTBALIST,
    UPD_LASTBALIST,
    FETCH_LASTBALIST_LOAD,
    FETCH_LASTBALIST_SUCCESS,
    FETCH_LASTBALIST_ERROR,
} from '../constants/LastBAList-action-type';
import { apipath } from '../utils/ajax';


// 初始化BA設定資料(批次時預設資料)
export const initialBAList = (list) => ({
    type:INIT_LASTBALIST,
    payload: {
        lastBAList: {// 時間週期設定(BA)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "BA",
            UploadCycle: 300,
            IRCycle: 60,
            CommunicationCycle: 15,
            UserName: list.username,
        },
        lastBADate: '',
        lastBAListError: '',
    }
})

// 更新BA(186)設定清單
export const updLastBAList = (data) => ({
    type: UPD_LASTBALIST,
    payload: {
        lastBAList: data,
    }
})

// 取得BA設定清單
export const fetchLastBAListLoad = (data) => ({
    type:FETCH_LASTBALIST_LOAD,
    payload: {
        token: data.token,
        language: data.curLanguage,
        timezone: data.timeZone,
        company: data.company,
        batteryGroupID: data.batteryGroupID,
        username: data.username,
        lastBAList:{},
        lastBADate: '',
        lastBAListError: '',
    }
})
export const fetchLastBAListSuccess = (list) => ({
    type:FETCH_LASTBALIST_SUCCESS,
    payload: {
        lastBAList: {//list.data,
            // 時間週期設定(BA)
            BatteryGroupID: list.batteryGroupID,
            CommandID: "BA",
            UploadCycle: Number(list.data.UploadCycle),
            IRCycle: Number(list.data.IRCycle),
            CommunicationCycle: Number(list.data.CommunicationCycle),
            UserName: list.username,
        },
        lastBADate: list.data.LastSettingTime,
        lastBAListError: '',
    }
})
export const fetchLastBAListError = (error) => ({
    type:FETCH_LASTBALIST_ERROR,
    payload: {
        lastBAList:{},
        lastBADate: '',
        lastBAListError: error,
    }
})

function* fetchLastBAList(data) {
    const {token,language,timezone,company,batteryGroupID,username} = data.payload;
    const url = `${apipath()}getPeriodSetting?batteryGroupId=${batteryGroupID}`;
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
            // console.log('fetchLastBAList',data.msg)
            yield put(fetchLastBAListSuccess({data:data.msg,batteryGroupID,username}));
        }else{
            console.error('Error',data.msg)
            yield put(fetchLastBAListError(data.msg))
        }
    }
    catch(error){
        console.error('Error',error.message)
        yield put(fetchLastBAListError(error.message));
    }

}
export function* LastBAListSaga(){
    yield takeLatest(FETCH_LASTBALIST_LOAD,fetchLastBAList);
}