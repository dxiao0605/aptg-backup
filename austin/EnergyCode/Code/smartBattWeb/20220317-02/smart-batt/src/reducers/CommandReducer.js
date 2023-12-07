import {
    SET_ACTIVENUM_COMMAND, //設定頁籤
    RESET_ACTIVENUM_COMMAND,//還原預設值
} from '../constants/Command-action-type';


// 初始化
const initialState = {
    activeNum: 0,//第一個頁籤
}

const CommandReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_ACTIVENUM_COMMAND:
            return {
                ...state,
                activeNum: action.payload.activeNum,
            }
        case RESET_ACTIVENUM_COMMAND:
            return {
                ...initialState,
            }
        default:
            return state
    }
}
export default CommandReducer;