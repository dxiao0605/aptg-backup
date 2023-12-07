import {
    SET_ACTIVENUM_NBMANAGE, //設定頁籤
    RESET_ACTIVENUM_NBMANAGE,//還原預設值
} from '../constants/NBManage-action-type';


// 初始化
const initialState = {
    activeNum: 0,//第一個頁籤
}

const NBManageReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_ACTIVENUM_NBMANAGE:
            return {
                ...state,
                activeNum: action.payload.activeNum,
            }
        case RESET_ACTIVENUM_NBMANAGE:
            return {
                ...initialState,
            }
        default:
            return state
    }
}
export default NBManageReducer;