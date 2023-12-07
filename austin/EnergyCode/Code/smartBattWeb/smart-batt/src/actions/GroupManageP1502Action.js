import {
    UPDATE_COMPANY_P1502,
    UPDATE_COUNTRY_P1502,
    UPDATE_AREA_P1502,
    UPDATE_GROUPID_P1502,
    UPDATE_GROUPNAME_P1502,
    RESET_COMPANY_P1502,
    RESET_COUNTRY_P1502,
    RESET_AREA_P1502,
    RESET_GROUPID_P1502,
    RESET_GROUPNAME_P1502,
    RESET_ALL_P1502,
} from '../constants/GroupManage-action-type';


export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_P1502,
    payload: { ...object }
})
export const updateCountry = (object) => ({
    type: UPDATE_COUNTRY_P1502,
    payload: { ...object }
})
export const updateArea = (object) => ({
    type: UPDATE_AREA_P1502,
    payload: { ...object }
})
export const updateGroupId = (object) => ({
    type: UPDATE_GROUPID_P1502,
    payload: { ...object }
})
export const updateGroupName = (object) => ({
    type: UPDATE_GROUPNAME_P1502,
    payload: { ...object }
})
export const resetCompany = () => ({
    type: RESET_COMPANY_P1502,
})
export const resetCountry = () => ({
    type: RESET_COUNTRY_P1502,
})
export const resetArea = () => ({
    type: RESET_AREA_P1502,
})
export const resetGroupId = () => ({
    type: RESET_GROUPID_P1502,
})
export const resetGroupName = () => ({
    type: RESET_GROUPNAME_P1502,
})
export const resetAll = () => ({
    type: RESET_ALL_P1502,
})
//登出用
export const resetAll_P1502_Action = () => ({
    type: RESET_ALL_P1502,
})