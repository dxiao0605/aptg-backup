import {
    UPDATE_COMPANY_P1501,
    UPDATE_BATTERYGROUPID_P1501,
    UPDATE_BATTERYTYPE_P1501,
    UPDATE_INSTALLDATE_P1501,
    RESET_COMPANY_P1501,
    RESET_BATTERYGROUPID_P1501,
    RESET_BATTERYTYPE_P1501,
    RESET_INSTALLDATE_P1501,
    RESET_ALL_P1501,
} from '../constants/BattManage-action-type';

export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_P1501,
    payload: { ...object }
})
export const updateBatteryGroupId = (object) => ({
    type: UPDATE_BATTERYGROUPID_P1501,
    payload: { ...object }
})
export const updateBatteryType = (object) => ({
    type: UPDATE_BATTERYTYPE_P1501,
    payload: { ...object }
})
export const updateInstallDate = (object) => ({
    type: UPDATE_INSTALLDATE_P1501,
    payload: { ...object }
})
export const resetCompany = () => ({
    type: RESET_COMPANY_P1501,
})
export const resetBatteryGroupId = () => ({
    type: RESET_BATTERYGROUPID_P1501,
})
export const resetBatteryType = () => ({
    type: RESET_BATTERYTYPE_P1501,
})
export const resetInstallDate = () => ({
    type: RESET_INSTALLDATE_P1501,
})
export const resetAll = () => ({
    type: RESET_ALL_P1501,
})

export const resetAll_P1501_Action = () => ({
    type: RESET_ALL_P1501,
})