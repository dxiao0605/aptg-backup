import {
    SET_ACTIVENUM_COMMAND, //設定頁籤
    RESET_ACTIVENUM_COMMAND,//還原預設值
} from '../constants/Command-action-type';

export const setActiveNum = (int) => ({
    type:SET_ACTIVENUM_COMMAND,
    payload:{
        activeNum: int
    }
});

export const resetActiveNum_P1504 = () => ({
    type:RESET_ACTIVENUM_COMMAND,
});

