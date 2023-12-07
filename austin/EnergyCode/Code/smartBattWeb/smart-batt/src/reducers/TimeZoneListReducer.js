import {
    FETCH_TIMEZONELIST_LOAD,
    FETCH_TIMEZONELIST_SUCCESS,
    FETCH_TIMEZONELIST_ERROR,
} from '../constants/TimeZone-action-type';

// 初始化
const initialState = {
    loading: false,
    timeZoneList: [],
    timeZoneListErrorMsg: '',
}

const TimeZoneListReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_TIMEZONELIST_LOAD:
            return {
                ...state,
                loading: true,
                timeZoneList: [],
                timeZoneListErrorMsg: '',
            }
        case FETCH_TIMEZONELIST_SUCCESS:
            return {
                ...state,
                loading: false,
                timeZoneList: action.payload.timeZoneList,
                timeZoneListErrorMsg: '',
            }
        case FETCH_TIMEZONELIST_ERROR:
            return {
                ...state,
                loading: false,
                timeZoneList: [],
                timeZoneListErrorMsg: action.payload.timeZoneListErrorMsg,
            }
        default:
            return state
    }
}
export default TimeZoneListReducer;