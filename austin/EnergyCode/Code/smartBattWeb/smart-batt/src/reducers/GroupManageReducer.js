import {
    SET_ACTIVENUM_GROUPMANAGE, //設定頁籤
    RESET_ACTIVENUM_GROUPMANAGE,//還原預設值
} from '../constants/GroupManage-action-type';


// 初始化
const initialState = {
    activeNum: 0,//第一個頁籤
}

const GroupManageReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_ACTIVENUM_GROUPMANAGE:
            return {
                ...state,
                activeNum: action.payload.activeNum,
            }
        case RESET_ACTIVENUM_GROUPMANAGE:
            return {
                ...initialState,                
            }
        default:
            return state
    }
}
export default GroupManageReducer;