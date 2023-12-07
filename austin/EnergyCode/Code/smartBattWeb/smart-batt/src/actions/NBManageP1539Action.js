import {
    UPDATE_COMPANY_P1539,
    UPDATE_BATTERYGROUPID_P1539,
    UPDATE_NBID_P1539,
    UPDATE_MODIFYITEM_P1539,
    UPDATE_MODIFYTIME_P1539,
    RESET_COMPANY_P1539,
    RESET_BATTERYGROUPID_P1539,
    RESET_NBID_P1539,
    RESET_MODIFYITEM_P1539,
    RESET_MODIFYTIME_P1539,
    RESET_ALL_P1539,
} from '../constants/NBManage-action-type';


export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_P1539,
    payload: { ...object }
})
export const updateBatteryGroupId = (object) => ({
    type: UPDATE_BATTERYGROUPID_P1539,
    payload: { ...object }
})
export const updateNBID = (object) => ({
    type: UPDATE_NBID_P1539,
    payload: { ...object }
})
export const updateModifyItem = (object) => ({
    type: UPDATE_MODIFYITEM_P1539,
    payload: { ...object }
})
export const updateModifyTime = (object) => ({
    type: UPDATE_MODIFYTIME_P1539,
    payload: { ...object }
})
export const resetCompany = () => ({
    type: RESET_COMPANY_P1539,
})
export const resetBatteryGroupId = () => ({
    type: RESET_BATTERYGROUPID_P1539,
})
export const resetNBID = () => ({
    type: RESET_NBID_P1539,
})
export const resetModifyItem = () => ({
    type: RESET_MODIFYITEM_P1539,
})
export const resetModifyTime = () => ({
    type: RESET_MODIFYTIME_P1539,
})
export const resetAll = () => ({
    type: RESET_ALL_P1539,
})

export const resetAll_P1539_Action = () => ({
    type: RESET_ALL_P1539,
})
