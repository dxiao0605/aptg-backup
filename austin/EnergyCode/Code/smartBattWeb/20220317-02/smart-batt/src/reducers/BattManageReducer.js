import {
    SET_ACTIVENUM_BATTMANAGE, //設定頁籤
    RESET_ACTIVENUM_BATTMANAGE,//還原預設值
} from '../constants/BattManage-action-type';


// 初始化
const initialState = {
    activeNum: 0,//第一個頁籤
}

const BattManageReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_ACTIVENUM_BATTMANAGE:
            return {
                ...state,
                activeNum: action.payload.activeNum,
            }
        case RESET_ACTIVENUM_BATTMANAGE:
            return {
                ...initialState,
            }
        default:
            return state
    }
}
export default BattManageReducer;