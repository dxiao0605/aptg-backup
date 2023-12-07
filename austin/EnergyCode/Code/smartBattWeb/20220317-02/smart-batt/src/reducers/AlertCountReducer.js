import {
    SET_INITALERT_COUNT,
    FETCH_ALERTCOUNT_LOAD,   //告警數筆數
    FETCH_ALERTCOUNT_SUCCESS,
    FETCH_ALERTCOUNT_ERROR,
}from '../constants/Alert-action-type';


// 初始化
const initialState = {
    loading:false,
    alertCount: 0,
    alertCountErrorMsg: '',
}

const AlertCountReducer = (state = initialState , action) => {
    switch(action.type){
        case SET_INITALERT_COUNT:
            return {
                loading:false,
                alertCount: 0,
                alertCountErrorMsg: '',
            }
        case FETCH_ALERTCOUNT_LOAD:
            return{
                loading: action.payload.loading,
                alertCount:action.payload.alertCount,
                alertCountErrorMsg:action.payload.alertCountErrorMsg,
            }
        case FETCH_ALERTCOUNT_SUCCESS:
            return{
                loading: action.payload.loading,
                alertCount:action.payload.alertCount,
                alertCountErrorMsg:action.payload.alertCountErrorMsg,
            }
        case FETCH_ALERTCOUNT_ERROR:
            return{
                loading: action.payload.loading,
                alertCount:action.payload.alertCount,
                alertCountErrorMsg:action.payload.alertCountErrorMsg,
            }
        default:
            return state
    }
}
export default AlertCountReducer;