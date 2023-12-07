import { apipath } from '../utils/ajax';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
    SUBMIT,
    EXIT,
    CHANGE_ACCOUNT,
    CHANGE_PASSWORD,
    CHANGE_LANGUAGE,
    CHANGE_CURLANGUAGE,
    CHANGE_TIMEZONE,
    FETCH_LOGIN_LOAD,
    FETCH_LOGIN_SUCCESS,
    FETCH_LOGIN_ERROR,
} from '../constants/Login-action-type';

// get API
export const submit = (data) => {
    return {
        type: SUBMIT,
        payload: {
            redirectToReferrer: true,
            errorMsg: '',
        }
    }
}

// 登出
export const exit = () => ({
    type: EXIT,
    payload: {
        loading: false,
        account: '',
        password: '',
        redirectToReferrer: false,  //是否跳轉
        token: '',
        username: '',   //使用者名稱
        role: '',    //權限
        company: '',    //公司
        timeZone: '',   //時區
        language: 0,    //語系
        curLanguage: 0,    //目前語系
        mapCenter: { lat: 25.0870915, lng: 121.5549154 },   //台灣
        functionList: [],   //功能選單
        SystemId: '',   //系統編號
        Key: '',    //google map 金鑰 
        perPage: 10,    //表格預設每頁顯示幾筆
        DisableTime: '',//密碼有效期限
        errorMsg: '',   //登入失敗訊息
        data: {},   //使用者資料
    }
})

// 更新account Input內容放入Store
export const updateAccount = (inputVal) => ({
    type: CHANGE_ACCOUNT,
    payload: {
        account: inputVal,
        errorMsg: '',
    }
})

// 更新passWord Input內容放入Store
export const updatePassword = (inputVal) => ({
    type: CHANGE_PASSWORD,
    payload: {
        password: inputVal,
        errorMsg: '',
    }
})

// 變更預設語系
export const changeLanguage = (value) => ({
    type: CHANGE_LANGUAGE,
    payload: {
        language: Number(value),
        curLanguage: Number(value),
        errorMsg: '',
    }
})

//變更目前語系
export const changeCurLanguage = (value) => ({
    type: CHANGE_CURLANGUAGE,
    payload: {
        curLanguage: Number(value),
        errorMsg: '',
    }
})

// 變更時區
export const changeTimeZone = (value) => ({
    type: CHANGE_TIMEZONE,
    payload: {
        timeZone: value,
        errorMsg: '',
    }
})

// submit data放入Store
export const fetchLoginLoad = (obj) => ({
    type: FETCH_LOGIN_LOAD,
    payload: {
        account: obj.account,
        password: obj.password,
        loading: true,
        errorMsg: '',
    }
});

// 登入成功
export const fetchLoginSuccess = (data) => ({
    type: FETCH_LOGIN_SUCCESS,
    payload: {
        loading: false,
        password: '',
        token: data.msg.Account.Token,
        username: data.msg.Account.UserName,    //使用者名稱
        role: data.msg.Account.RoleId,  //權限
        company: data.msg.Company,  //公司id
        companyName: data.msg.CompanyName, //公司名稱
        timeZone: data.msg.TimeZone,    //時區
        language: data.msg.Language,    //語系
        curLanguage: data.msg.Language,    //目前語系
        mapCenter: { lat: data.msg.Lat, lng: data.msg.Lng },   //預設地點
        functionList: data.msg.FunctionList,    //清單
        SystemId: data.msg.SystemId,
        Key: data.msg.Key,    //google map 金鑰 
        perPage: 10,    //表格每頁幾筆資料
        DisableTime: data.msg.DisableTime,
        errorMsg: '',  //錯誤訊息
        data: data.msg, //使用者資訊
    }
})

// 登入失敗
export const fetchLoginError = (errorMsg) => ({
    type: FETCH_LOGIN_ERROR,
    payload: {
        loading: false,
        password: '',
        errorMsg: errorMsg
    }
})


function* fetchLogin(data) {
    const url = `${apipath()}authorization`;
    const { account, password } = data.payload;
    // 使用 data 接收請求的資料
    try {
        const data = yield call(
            () => fetch(url, {
                method: 'POST',
                headers: new Headers({
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'account': account,
                    'pw': password
                })
            }).then(response => response.json())
        );

        if (data.code === '00') {            
            yield put(fetchLoginSuccess(data));
            yield put(submit());
        } else {
            console.error('error', data.msg)
            yield put(fetchLoginError(data.msg));
        }
    }
    catch (error) {
        console.error('error', data.msg)
        yield put(fetchLoginError(error.message));
    }
}


export function* LoginSaga() {
    yield takeLatest(FETCH_LOGIN_LOAD, fetchLogin);
}