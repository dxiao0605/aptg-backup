import {
    FETCH_FILTERCOUNTRY_LOAD,
    FETCH_FILTERCOUNTRY_SUCCESS,
    FETCH_FILTERCOUNTRY_ERROR,
} from '../constants/FilterCountry-action-type';



// init
const initialState = {
    loading:false,
    filterCountry: [],
    filterCountryError: '',
}
const FilterCountryReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_FILTERCOUNTRY_LOAD:
            return{
                ...state,
                loading: action.payload.loading,
                filterCountry:[],
                filterCountryError: '',
            }
        case FETCH_FILTERCOUNTRY_SUCCESS:
            return{
                ...state,
                loading: action.payload.loading,
                filterCountry: action.payload.filterCountry,
                filterCountryError: '',
            }
        case FETCH_FILTERCOUNTRY_ERROR:
            return{
                ...state,
                loading: action.payload.loading,
                filterCountry:[],
                filterCountryError: action.payload.filterCountryError,
            }
        default: 
            return state
    }
}
export default FilterCountryReducer;