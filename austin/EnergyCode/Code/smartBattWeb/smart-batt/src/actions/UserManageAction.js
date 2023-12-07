import {
    SET_ACTIVENUM_USERMANAGE, //設定頁籤
    RESET_ACTIVENUM_USERMANAGE,//還原預設值
} from '../constants/UserManage-action-type';

export const setActiveNum = (int) => ({
    type:SET_ACTIVENUM_USERMANAGE,
    payload:{
        activeNum: int
    }
})

export const resetActiveNum_USERMANAGE= () => ({
    type:RESET_ACTIVENUM_USERMANAGE,
});