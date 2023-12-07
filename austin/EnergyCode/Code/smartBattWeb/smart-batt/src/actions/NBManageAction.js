import {
    SET_ACTIVENUM_NBMANAGE, //設定頁籤
    RESET_ACTIVENUM_NBMANAGE,//還原預設值
} from '../constants/NBManage-action-type';

export const setActiveNum = (int) => ({
    type:SET_ACTIVENUM_NBMANAGE,
    payload:{
        activeNum: int
    }
})

export const resetActiveNum_P1503 = () => ({
    type:RESET_ACTIVENUM_NBMANAGE,
});