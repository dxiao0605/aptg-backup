import {
    SET_TABLEHEADER_P1504,
    SET_AREALIST_P1504,
    SET_GROUPLIST_P1504,
    SET_SUBMITKEYLIST_P1504,
    RESET_ALL_P1504
} from '../constants/Command-action-type';

export const setTableHeader = (list) => ({
    type: SET_TABLEHEADER_P1504,
    payload: { tableHeader: [...list] }
})
//紀錄公司別查詢到的國家/地域
export const setAreaList = (List) => ({
    type: SET_AREALIST_P1504,
    payload: {
        list: [...List]
    }
})
//紀錄公司別查詢到的站台選單
export const setGroupList = (List) => ({
    type: SET_GROUPLIST_P1504,
    payload: {
        list: [...List]
    }
})
//紀錄table Key值
export const setSubmitKeyList = (List) => ({
    type: SET_SUBMITKEYLIST_P1504,
    payload: {
        list: [...List]
    }
})
//還原預設值
export const resetAll_P1504_Action = () => ({
    type: RESET_ALL_P1504,
})