
import {
    FETCH_FILTERGROUPID_LOAD,
    FETCH_FILTERGROUPID_SUCCESS,
    FETCH_FILTERGROUPID_ERROR,
} from '../constants/FilterGroupId-action-type';



// init
const initialState = {
    loading:false,
    filterGroupId: [],
    filterGroupIdError: '',
}
const FilterGroupIdReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_FILTERGROUPID_LOAD:
            return{
                ...state,
                loading: action.payload.loading,
                filterGroupId:[],
                filterGroupIdError: '',
            }
        case FETCH_FILTERGROUPID_SUCCESS:
            return{
                ...state,
                loading: action.payload.loading,
                filterGroupId: action.payload.filterGroupId,
                filterGroupIdError: '',
            }
        case FETCH_FILTERGROUPID_ERROR:
            return{
                ...state,
                loading: action.payload.loading,
                filterGroupId:[],
                filterGroupIdError: action.payload.filterGroupIdError,
            }
        default: 
            return state
    }
}
export default FilterGroupIdReducer;