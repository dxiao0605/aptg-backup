import {
    SET_ACTIVENUM_BATTMANAGE, //設定頁籤
    RESET_ACTIVENUM_BATTMANAGE,//還原預設值
} from '../constants/BattManage-action-type';

export const setActiveNum = (int) => ({
    type:SET_ACTIVENUM_BATTMANAGE,
    payload:{
        activeNum: int
    }
})

export const resetActiveNum_P1501 = () => ({
    type:RESET_ACTIVENUM_BATTMANAGE,
});