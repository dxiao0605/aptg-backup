import {
    SET_TABLEHEADER_P1559,
    UPDATE_COMPANY_P1559,
    UPDATE_COUNTRY_P1559,
    UPDATE_AREA_P1559,
    UPDATE_GROUPID_P1559,
    UPDATE_GROUPNAME_P1559,
    UPDATE_BATTERYGROUPID_P1559,
    UPDATE_BATTERYTYPE_P1559,
    UPDATE_COMMAND_P1559,
    UPDATE_RESPONSE_P1559,
    UPDATE_SENDTIME_P1559,
    RESET_COMPANY_P1559,
    RESET_COUNTRY_P1559,
    RESET_AREA_P1559,
    RESET_GROUPID_P1559,
    RESET_GROUPNAME_P1559,
    RESET_BATTERYGROUPID_P1559,
    RESET_BATTERYTYPE_P1559,
    RESET_COMMAND_P1559,
    RESET_RESPONSE_P1559,
    RESET_SENDTIME_P1559,
    RESET_ALL_P1559,
} from '../constants/Command-action-type';

export const setTableHeader = (list) => ({
    type: SET_TABLEHEADER_P1559,
    payload: { tableHeader: [...list] }
})
export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_P1559,
    payload: { ...object }
})
export const updateCountry = (object) => ({
    type: UPDATE_COUNTRY_P1559,
    payload: { ...object }
})
export const updateArea = (object) => ({
    type: UPDATE_AREA_P1559,
    payload: { ...object }
})
export const updateGroupId = (object) => ({
    type: UPDATE_GROUPID_P1559,
    payload: { ...object }
})
export const updateGroupName = (object) => ({
    type: UPDATE_GROUPNAME_P1559,
    payload: { ...object }
})

export const updateBatteryGroupId = (object) => ({
    type: UPDATE_BATTERYGROUPID_P1559,
    payload: { ...object }
})
export const updateBatteryType = (object) => ({
    type: UPDATE_BATTERYTYPE_P1559,
    payload: { ...object }
})
export const updateCommand = (object) => ({
    type: UPDATE_COMMAND_P1559,
    payload: { ...object }
})
export const updateResponse = (object) => ({
    type: UPDATE_RESPONSE_P1559,
    payload: { ...object }
})
export const updateSendTime = (object) => ({
    type: UPDATE_SENDTIME_P1559,
    payload: { ...object }
})

export const resetCompany = () => ({
    type: RESET_COMPANY_P1559,
})
export const resetCountry = () => ({
    type: RESET_COUNTRY_P1559,
})
export const resetArea = () => ({
    type: RESET_AREA_P1559,
})
export const resetGroupId = () => ({
    type: RESET_GROUPID_P1559,
})
export const resetGroupName = () => ({
    type: RESET_GROUPNAME_P1559,
})


export const resetBatteryGroupId = () => ({
    type: RESET_BATTERYGROUPID_P1559,
})
export const resetBatteryType = () => ({
    type: RESET_BATTERYTYPE_P1559,
})
export const resetCommand = () => ({
    type: RESET_COMMAND_P1559,
})
export const resetResponse = () => ({
    type: RESET_RESPONSE_P1559,
})
export const resetSendTime = () => ({
    type: RESET_SENDTIME_P1559,
})
export const resetAll = () => ({
    type: RESET_ALL_P1559,
})

export const resetAll_P1559_Action = () => ({
    type: RESET_ALL_P1559,
})