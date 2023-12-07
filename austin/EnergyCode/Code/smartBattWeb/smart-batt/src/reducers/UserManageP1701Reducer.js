import {
    UPDATE_USERMANAGE_P1701, //查看使用者資訊
    RESET_USERMANAGE_P1701,//還原預設值
} from '../constants/UserManage-action-type';


// 初始化
const initialState = {
    forward: false,//查看使用者設定
    searchInput:'',
}

const UserManageP1701Reducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_USERMANAGE_P1701:
            return {
                ...state,
                forward: action.payload.forward,
                searchInput: action.payload.searchInput,
            }
        case RESET_USERMANAGE_P1701:
            return {
                ...initialState,
            }
        default:
            return state
    }
}
export default UserManageP1701Reducer;