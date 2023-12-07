import {
    SET_ACTIVENUM_USERMANAGE, //設定頁籤
    RESET_ACTIVENUM_USERMANAGE,//還原預設值
} from '../constants/UserManage-action-type';


// 初始化
const initialState = {
    activeNum: 0,//第一個頁籤
}

const UserManageReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_ACTIVENUM_USERMANAGE:
            return {
                ...state,
                activeNum: action.payload.activeNum,
            }
        case RESET_ACTIVENUM_USERMANAGE:
            return {
                ...initialState,
            }
        default:
            return state
    }
}
export default UserManageReducer;