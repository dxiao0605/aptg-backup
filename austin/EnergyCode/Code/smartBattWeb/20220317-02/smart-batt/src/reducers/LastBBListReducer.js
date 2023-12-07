import {
    INIT_LASTBBLIST,
    UPD_LASTBBLIST,
    FETCH_LASTBBLIST_LOAD,
    FETCH_LASTBBLIST_SUCCESS,
    FETCH_LASTBBLIST_ERROR,
} from '../constants/LastBBList-action-type';


const initialState = {
    lastBBList: {},
    lastBBDate: '',
    lastBBListError: '',
}

const LastBBListReducer = ( state = initialState , action) => {
    switch(action.type){
        case INIT_LASTBBLIST:
            return {
                ...state,
                lastBBList: action.payload.lastBBList,
                lastBBDate: action.payload.lastBBDate,
                lastBBListError: action.payload.lastBBListError,
            }
        case UPD_LASTBBLIST:
            return {
                ...state,
                lastBBList: action.payload.lastBBList,
            }
        case FETCH_LASTBBLIST_LOAD:
            return {
                ...state,
                lastBBList: action.payload.lastBBList,
                lastBBDate: action.payload.lastBBDate,
                lastBBListError: action.payload.lastBBListError,
            }
        case FETCH_LASTBBLIST_SUCCESS:
            return {
                ...state,
                lastBBList: action.payload.lastBBList,
                lastBBDate: action.payload.lastBBDate,
                lastBBListError: action.payload.lastBBListError,
            }
        case FETCH_LASTBBLIST_ERROR:
            return {
                ...state,
                lastBBList: action.payload.lastBBList,
                lastBBDate: action.payload.lastBBDate,
                lastBBListError: action.payload.lastBBListError,
            }
        default:
            return state
    }
}
export default LastBBListReducer;