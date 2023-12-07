import {
    SET_TREE_FUNCTIONID,   //指定目前頁面 
    RESET_TREE_FUNCTIONID, //還原預設值
    UPDATE_LOGO,           //更新目前LOGO 
} from '../constants/MainNav-action-type';


// 初始化
const initialState = {
    treeFunctionId: 1200,
    updateLogo: false,
}

const MainNavReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_TREE_FUNCTIONID:
            return {
                ...state,
                treeFunctionId: action.payload.treeFunctionId,
            }
        case RESET_TREE_FUNCTIONID:
            return {
                ...initialState
            }
        case UPDATE_LOGO:
            return {
                ...state,
                updateLogo: action.payload.boolean,
            }
        default:
            return state
    }
}
export default MainNavReducer;