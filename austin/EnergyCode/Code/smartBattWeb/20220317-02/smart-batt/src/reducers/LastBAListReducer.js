import {
    INIT_LASTBALIST,
    UPD_LASTBALIST,
    FETCH_LASTBALIST_LOAD,
    FETCH_LASTBALIST_SUCCESS,
    FETCH_LASTBALIST_ERROR,
} from '../constants/LastBAList-action-type';


const initialState = {
    lastBAList: {},
    lastBADate: '',
    lastBAListError: '',
}

const LastBAListReducer = ( state = initialState , action) => {
    switch(action.type){
        case INIT_LASTBALIST:
            return {
                ...state,
                lastBAList: action.payload.lastBAList,
                lastBADate: action.payload.lastBADate,
                lastBAListError: action.payload.lastBAListError,
            }
        case UPD_LASTBALIST:
            return {
                ...state,
                lastBAList: action.payload.lastBAList,
            }
        case FETCH_LASTBALIST_LOAD:
            return {
                ...state,
                lastBAList: action.payload.lastBAList,
                lastBADate: action.payload.lastBADate,
                lastBAListError: action.payload.lastBAListError,
            }
        case FETCH_LASTBALIST_SUCCESS:
            return {
                ...state,
                lastBAList: action.payload.lastBAList,
                lastBADate: action.payload.lastBADate,
                lastBAListError: action.payload.lastBAListError,
            }
        case FETCH_LASTBALIST_ERROR:
            return {
                ...state,
                lastBAList: action.payload.lastBAList,
                lastBADate: action.payload.lastBADate,
                lastBAListError: action.payload.lastBAListError,
            }
        default:
            return state
    }
}
export default LastBAListReducer;