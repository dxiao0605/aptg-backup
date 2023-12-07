
import {
    FETCH_BATTTYPELIST_LOAD,
    FETCH_BATTTYPELIST_SUCCESS,
    FETCH_BATTTYPELIST_ERROR,
} from '../constants/BattTypeList-action-type';


const initialState = {
    loading: true,
    // token: '',
    // language: 0,
    // timezone: '',
    // company: '',
    battTypeList: [],
    battTypeListError: '',
}

const BattTypeListReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_BATTTYPELIST_LOAD:
            return {
                ...state,
                battTypeList: action.payload.battTypeList,
                battTypeListError: action.payload.battTypeListError,
            }
        case FETCH_BATTTYPELIST_SUCCESS:
            return {
                ...state,
                loading: false,
                battTypeList: action.payload.battTypeList,
                battTypeListError: action.payload.battTypeListError,
            }
        case FETCH_BATTTYPELIST_ERROR:
            return {
                ...state,
                loading: false,
                battTypeList: action.payload.battTypeList,
                battTypeListError: action.payload.battTypeListError,
            }
        default:
            return state
    }

}
export default BattTypeListReducer;