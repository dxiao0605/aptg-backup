import {
    FETCH_FILTERAREA_LOAD,
    FETCH_FILTERAREA_SUCCESS,
    FETCH_FILTERAREA_ERROR,
} from '../constants/FilterArea-action-type';



// init
const initialState = {
    loading:false,
    filterArea: [],
    filterAreaError: '',
}
const FilterAreaReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_FILTERAREA_LOAD:
            return{
                ...state,
                loading: action.payload.loading,
                filterArea:[],
                filterAreaError: '',
            }
        case FETCH_FILTERAREA_SUCCESS:
            return{
                ...state,
                loading: action.payload.loading,
                filterArea: action.payload.filterArea,
                filterAreaError: '',
            }
        case FETCH_FILTERAREA_ERROR:
            return{
                ...state,
                loading: action.payload.loading,
                filterArea:[],
                filterAreaError: action.payload.filterAreaError,
            }
        default: 
            return state
    }
}
export default FilterAreaReducer;