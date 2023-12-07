
import {
    FETCH_FILTERGROUPNAME_LOAD,
    FETCH_FILTERGROUPNAME_SUCCESS,
    FETCH_FILTERGROUPNAME_ERROR,
} from '../constants/FilterGroupName-action-type';



// init
const initialState = {
    loading:false,
    filterGroupName: [],
    filterGroupNameError: '',
}
const FilterGroupNameReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_FILTERGROUPNAME_LOAD:
            return{
                ...state,
                loading: action.payload.loading,
                filterGroupName:[],
                filterGroupNameError: '',
            }
        case FETCH_FILTERGROUPNAME_SUCCESS:
            return{
                ...state,
                loading: action.payload.loading,
                filterGroupName: action.payload.filterGroupName,
                filterGroupNameError: '',
            }
        case FETCH_FILTERGROUPNAME_ERROR:
            return{
                ...state,
                loading: action.payload.loading,
                filterGroupName:[],
                filterGroupNameError: action.payload.filterGroupNameError,
            }
        default: 
            return state
    }
}
export default FilterGroupNameReducer;