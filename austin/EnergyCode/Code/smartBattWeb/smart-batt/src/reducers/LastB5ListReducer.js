import {
    INIT_LASTB5LIST,
    UPD_LASTB5LIST,
    FETCH_LASTB5LIST_LOAD,
    FETCH_LASTB5LIST_SUCCESS,
    FETCH_LASTB5LIST_ERROR,
} from '../constants/LastB5List-action-type';


const initialState = {
    lastB5List: {},
    lastB5Date: '',
    lastB5ListError: '',
}

const LastB5ListReducer = ( state = initialState , action) => {
    switch(action.type){
        case INIT_LASTB5LIST:
            return {
                ...state,
                lastB5List: action.payload.lastB5List,
                lastB5Date: action.payload.lastB5Date,
                lastB5ListError: action.payload.lastB5ListError,
            }
        case UPD_LASTB5LIST:
            return {
                ...state,
                lastB5List: action.payload.lastB5List,
            }
        case FETCH_LASTB5LIST_LOAD:
            return {
                ...state,
                lastB5List: action.payload.lastB5List,
                lastB5Date: action.payload.lastB5Date,
                lastB5ListError: action.payload.lastB5ListError,
            }
        case FETCH_LASTB5LIST_SUCCESS:
            return {
                ...state,
                lastB5List: action.payload.lastB5List,
                lastB5Date: action.payload.lastB5Date,
                lastB5ListError: action.payload.lastB5ListError,
            }
        case FETCH_LASTB5LIST_ERROR:
            return {
                ...state,
                lastB5List: action.payload.lastB5List,
                lastB5Date: action.payload.lastB5Date,
                lastB5ListError: action.payload.lastB5ListError,
            }
        default:
            return state
    }
}
export default LastB5ListReducer;