import {
    SET_ACTIVENUM_SYSYEMSETTINGS, //設定頁籤
    RESET_ACTIVENUM_SYSYEMSETTINGS,//還原預設值
} from '../constants/SystemSettings-action-type';


// 初始化
const initialState = {
    activeNum: 0,//第一個頁籤
}

const SystemSettingsReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_ACTIVENUM_SYSYEMSETTINGS:
            return {
                ...state,
                activeNum: action.payload.activeNum,
            }
        case RESET_ACTIVENUM_SYSYEMSETTINGS:
            return {
                ...initialState,
            }
        default:
            return state
    }
}
export default SystemSettingsReducer;