import {
    FETCH_FILTERCOMPANY_LOAD,
    FETCH_FILTERCOMPANY_SUCCESS,
    FETCH_FILTERCOMPANY_ERROR,
} from '../constants/FilterCompany-action-type';



// init
const initialState = {
    loading:false,
    filterCompany: [],
    filterCompanyError: '',
}
const FilterCompanyReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_FILTERCOMPANY_LOAD:
            return{
                ...state,
                loading: action.payload.loading,
                filterCompany:[],
                filterCompanyError: '',
            }
        case FETCH_FILTERCOMPANY_SUCCESS:
            return{
                ...state,
                loading: action.payload.loading,
                filterCompany: action.payload.filterCompany,
                filterCompanyError: '',
            }
        case FETCH_FILTERCOMPANY_ERROR:
            return{
                ...state,
                loading: action.payload.loading,
                filterCompany:[],
                filterCompanyError: action.payload.filterCompanyError,
            }
        default: 
            return state
    }
}
export default FilterCompanyReducer;