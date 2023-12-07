import {
    SUBMIT,
    EXIT,
    CHANGE_ACCOUNT,
    CHANGE_PASSWORD,
    CHANGE_LANGUAGE,    //變更預設語系
    CHANGE_CURLANGUAGE, //變更目前語系
    CHANGE_TIMEZONE,
    FETCH_LOGIN_LOAD,
    FETCH_LOGIN_SUCCESS,
    FETCH_LOGIN_ERROR,
} from '../constants/Login-action-type';

// 初始化
const initialState = {
    loading: false,
    account: '',
    password: '',
    redirectToReferrer: false,  //是否跳轉
    token: '',
    username: '',   //使用者名稱
    role: '',    //權限
    company: '',    //公司id
    companyName: '', //公司名稱
    timeZone: '',   //時區
    language: 0,    //語系
    curLanguage: 0,    //目前語系
    mapCenter: { lat: 25.0870915, lng: 121.5549154 },   //預設地點
    functionList: [],   //功能選單
    SystemId: '',   //系統編號
    Key: '',    //google map 金鑰
    DisableTime: '',//密碼有效期限
    perPage: 10,    //表格預設每頁顯示幾筆
    errorMsg: '',   //登入失敗訊息
    data: {},   //使用者資料
}

// 登入時的狀態類型，由 /constants/login-action-type 建立出來
const LoginReducer = (state = initialState, action) => {
    switch (action.type) {
        case SUBMIT:
            return {
                ...state,
                redirectToReferrer: action.payload.redirectToReferrer,
                errorMsg: ''
            }
        case CHANGE_ACCOUNT:
            return {
                ...state,
                account: action.payload.account,
                errorMsg: ''
            }
        case CHANGE_PASSWORD:
            return {
                ...state,
                password: action.payload.password,
                errorMsg: ''
            }
        case CHANGE_LANGUAGE:   //變更預設語系
            return {
                ...state,
                language: action.payload.language,
                curLanguage: action.payload.curLanguage,
            }
        case CHANGE_CURLANGUAGE:   //變更預設語系
            return {
                ...state,
                curLanguage: action.payload.curLanguage,
            }
        case CHANGE_TIMEZONE:   //變更預設語系
            return {
                ...state,
                timeZone: action.payload.timeZone,
            }
        case EXIT:
            return {
                loading: false,
                account: '',
                password: '',
                redirectToReferrer: false,  //是否跳轉
                token: '',
                username: '',   //使用者名稱
                role: '',    //權限
                company: '',    //公司id
                companyName: '', //公司名稱
                timeZone: '',   //時區
                language: 0,    //語系
                curLanguage: 0,    //目前語系
                mapCenter: { lat: 25.0870915, lng: 121.5549154 },   //預設地點
                functionList: [],   //功能選單
                SystemId: '',   //系統編號
                Key: '',    //google map 金鑰 
                perPage: 10,    //表格預設每頁顯示幾筆
                DisableTime: '', //密碼有效期限
                errorMsg: '',   //登入失敗訊息
                data: {},   //使用者資料
            }
        case FETCH_LOGIN_LOAD:
            return {
                ...state,
                loading: true,
            }
        case FETCH_LOGIN_SUCCESS:
            return {
                ...state,
                loading: false,
                password: '',
                token: action.payload.token,
                username: action.payload.username,
                role: action.payload.role,
                company: action.payload.company,
                companyName: action.payload.companyName, //公司名稱
                timeZone: action.payload.timeZone,
                language: action.payload.language,
                curLanguage: action.payload.curLanguage,
                mapCenter: action.payload.mapCenter,   //預設地點
                functionList: action.payload.functionList,
                SystemId: action.payload.SystemId,
                data: action.payload.data,
                Key: action.payload.Key,    //google map 金鑰 
                DisableTime: action.payload.DisableTime,
                perPage: 10,
                errorMsg: ''
            }
        case FETCH_LOGIN_ERROR:
            return {
                ...state,
                loading: false,
                password: '',
                errorMsg: action.payload.errorMsg,
            }
        default:
            return state
    }
}
export default LoginReducer;