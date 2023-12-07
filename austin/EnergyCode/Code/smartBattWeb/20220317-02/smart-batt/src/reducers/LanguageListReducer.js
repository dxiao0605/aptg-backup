import {
    FETCH_LANGUAGELIST_LOAD,
    FETCH_LANGUAGELIST_SUCCESS,
    FETCH_LANGUAGELIST_ERROR
}from  '../constants/LanguageList-action-type';


// 初始化
const initialState = {
    loading: false,
    // token: '',
    languageList: [],
    languageListErrorMsg: '',
}
const LanguageListReducer = ( state = initialState , action) => {
    switch(action.type){
        case FETCH_LANGUAGELIST_LOAD:
            return {
                ...state,
                loading: true,
                // token: action.payload.token,
                languageList: [],
                languageListErrorMsg: '',
            }
        case FETCH_LANGUAGELIST_SUCCESS:
            return {
                ...state,
                loading: false,
                languageList: action.payload.languageList,
                languageListErrorMsg: '',
            }
        case FETCH_LANGUAGELIST_ERROR:
            return {
                ...state,
                loading: false,
                languageList: [],
                languageListErrorMsg: action.payload.languageListErrorMsg,
            }
        default:
            return state
    }
}
export default LanguageListReducer;