import {
    UPDATE_USERMANAGE_P1701, //設定查看使用者資訊
    RESET_USERMANAGE_P1701,//還原預設值
} from '../constants/UserManage-action-type';

export const updateUSERMANAGE = (data) => ({
    type:UPDATE_USERMANAGE_P1701,
    payload:{
        forward: data.forward,
        searchInput:data.searchInput,
    }
})

export const reset_USERMANAGE= () => ({
    type:RESET_USERMANAGE_P1701,
});