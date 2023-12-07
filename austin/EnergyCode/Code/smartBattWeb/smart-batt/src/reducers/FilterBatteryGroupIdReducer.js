import {
    FETCH_FILTERBATTERYGROUPID_LOAD,
    FETCH_FILTERBATTERYGROUPID_SUCCESS,
    FETCH_FILTERBATTERYGROUPID_ERROR,
} from '../constants/FilterBatteryGroupId-action-type';



// init
const initialState = {
    loading:false,
    filterBatteryGroupId: [],
    filterBatteryGroupIdError: '',
}
const FilterBatteryGroupIdReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_FILTERBATTERYGROUPID_LOAD:
            return{
                ...state,
                loading: action.payload.loading,
                filterBatteryGroupId:[],
                filterBatteryGroupIdError: '',
            }
        case FETCH_FILTERBATTERYGROUPID_SUCCESS:
            return{
                ...state,
                loading: action.payload.loading,
                filterBatteryGroupId: action.payload.filterBatteryGroupId,
                filterBatteryGroupIdError: '',
            }
        case FETCH_FILTERBATTERYGROUPID_ERROR:
            return{
                ...state,
                loading: action.payload.loading,
                filterBatteryGroupId:[],
                filterBatteryGroupIdError: action.payload.filterBatteryGroupIdError,
            }
        default: 
            return state
    }
}
export default FilterBatteryGroupIdReducer;