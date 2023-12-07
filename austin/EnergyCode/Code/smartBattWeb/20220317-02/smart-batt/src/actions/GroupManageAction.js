import {
   SET_ACTIVENUM_GROUPMANAGE, //設定頁籤
   RESET_ACTIVENUM_GROUPMANAGE,//還原預設值
} from '../constants/GroupManage-action-type';

export const setActiveNum = (int) => ({
    type:SET_ACTIVENUM_GROUPMANAGE,
    payload:{
        activeNum: int
    }
})

export const resetActiveNum_P1502 = () => ({
    type:RESET_ACTIVENUM_GROUPMANAGE,
});