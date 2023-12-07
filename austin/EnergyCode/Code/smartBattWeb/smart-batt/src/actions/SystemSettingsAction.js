import {
    SET_ACTIVENUM_SYSYEMSETTINGS, //設定頁籤
    RESET_ACTIVENUM_SYSYEMSETTINGS,//還原預設值
} from '../constants/SystemSettings-action-type';

export const setActiveNum = (int) => ({
    type:SET_ACTIVENUM_SYSYEMSETTINGS,
    payload:{
        activeNum: int
    }
})

export const resetActiveNum_SYSYEMSETTINGS = () => ({
    type:RESET_ACTIVENUM_SYSYEMSETTINGS,
});