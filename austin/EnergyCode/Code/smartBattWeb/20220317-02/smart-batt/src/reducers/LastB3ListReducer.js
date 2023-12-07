import {
    INIT_LASTB3LIST,
    UPD_LASTB3LIST,
    FETCH_LASTB3LIST_LOAD,
    FETCH_LASTB3LIST_SUCCESS,
    FETCH_LASTB3LIST_ERROR,
} from '../constants/LastB3List-action-type';


const initialState = {
    lastB3List: {},
    lastB3Date: '',
    lastB3ListError: '',
}

const LastB3ListReducer = ( state = initialState , action) => {
    switch(action.type){
        case INIT_LASTB3LIST:
            return {
                ...state,
                lastB3List: action.payload.lastB3List,
                lastB3Date: action.payload.lastB3Date,
                lastB3ListError: action.payload.lastB3ListError,
            }
        case UPD_LASTB3LIST:
            return {
                ...state,
                lastB3List: action.payload.lastB3List,
            }
        case FETCH_LASTB3LIST_LOAD:
            return {
                ...state,
                lastB3List: action.payload.lastB3List,
                lastB3Date: action.payload.lastB3Date,
                lastB3ListError: action.payload.lastB3ListError,
            }
        case FETCH_LASTB3LIST_SUCCESS:
            return {
                ...state,
                lastB3List: action.payload.lastB3List,
                lastB3Date: action.payload.lastB3Date,
                lastB3ListError: action.payload.lastB3ListError,
            }
        case FETCH_LASTB3LIST_ERROR:
            return {
                ...state,
                lastB3List: action.payload.lastB3List,
                lastB3Date: action.payload.lastB3Date,
                lastB3ListError: action.payload.lastB3ListError,
            }
        default:
            return state
    }
}
export default LastB3ListReducer;