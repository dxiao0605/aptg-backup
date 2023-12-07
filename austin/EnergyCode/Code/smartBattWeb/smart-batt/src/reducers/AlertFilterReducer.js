import {
    UPDATE_EVENTTYPECODE_ALERT,
    UPDATE_COMPANY_ALERT,
    UPDATE_COUNTRY_ALERT,
    UPDATE_AREA_ALERT,
    UPDATE_GROUPID_ALERT,
    UPDATE_GROUPNAME_ALERT,
    UPDATE_BATTERYGROUPID_ALERT,
    UPDATE_BATTERYTYPE_ALERT,
    RESET_EVENTTYPECODE_ALERT,
    RESET_COMPANY_ALERT,
    RESET_COUNTRY_ALERT,
    RESET_AREA_ALERT,
    RESET_GROUPID_ALERT,
    RESET_GROUPNAME_ALERT,
    RESET_BATTERYGROUPID_ALERT,
    RESET_BATTERYTYPE_ALERT,
    RESET_ALL_ALERT,
    UPDATE_RECORDTIME_ALERT,
    RESET_RECORDTIME_ALERT,
} from '../constants/AlertFilter-action-type';
import { initFilterSelectData ,initFilterDate} from '../pages/AlertSolved/AlertFilter/InitDataFormat';


// 初始化
const initialState = {
    isFilterEventTypeCodeData: {//預設告警類型
        ...initFilterSelectData
    },
    isFilterCompanyData: {//預設公司別
        ...initFilterSelectData
    },
    isFilterCountryData: {//預設國家
        ...initFilterSelectData
    },
    isFilterAreaData: {//預設地域
        ...initFilterSelectData
    },
    isFilterGroupIdData: {//預設站台編號
        ...initFilterSelectData
    },
    isFilterGroupNameData: {//預設站台名稱
        ...initFilterSelectData
    },
    isFilterBatteryGroupIdData: {//預設電池組ID
        ...initFilterSelectData
    },
    isFilterBatteryTypeData: {//預設電池型號
        ...initFilterSelectData
    },
    isFilterRecordTimeAlert: {//預設數據時間
        ...initFilterDate,
    },
}

const AlertFilterReducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_EVENTTYPECODE_ALERT://告警類型
            return {
                ...state,
                isFilterEventTypeCodeData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COMPANY_ALERT://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_ALERT://國家
            return {
                ...state,
                isFilterCountryData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_ALERT://地域
            return {
                ...state,
                isFilterAreaData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_ALERT://站台編號
            return {
                ...state,
                isFilterGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPNAME_ALERT://站台名稱
            return {
                ...state,
                isFilterGroupNameData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYGROUPID_ALERT://電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYTYPE_ALERT://電池型號
            return {
                ...state,
                isFilterBatteryTypeData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_RECORDTIME_ALERT://數據時間
            return {
                ...state,
                isFilterRecordTimeAlert: {
                    Radio: action.payload.Radio,
                    Start: action.payload.Start,
                    StartHH: action.payload.StartHH,
                    StartMM: action.payload.StartMM,
                    End: action.payload.End,
                    EndHH: action.payload.EndHH,
                    EndMM: action.payload.EndMM,
                    isButtonList: action.payload.isButtonList
                }
            }
        case RESET_EVENTTYPECODE_ALERT:
            return {
                ...state,
                isFilterEventTypeCodeData: {//初始值告警類型
                    ...initFilterSelectData
                }
            }
        case RESET_COMPANY_ALERT:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_ALERT:
            return {
                ...state,
                isFilterCountryData: {//初始值國家
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_ALERT:
            return {
                ...state,
                isFilterAreaData: {//初始值地域
                    ...initFilterSelectData
                },
            }
        case RESET_GROUPID_ALERT:
            return {
                ...state,
                isFilterGroupIdData: {//初始值站台編號
                    ...initFilterSelectData
                }
            }
        case RESET_GROUPNAME_ALERT:
            return {
                ...state,
                isFilterGroupNameData: {//初始值站台名稱
                    ...initFilterSelectData
                }
            }


        case RESET_BATTERYGROUPID_ALERT://初始值電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYTYPE_ALERT://初始值電池型號
            return {
                ...state,
                isFilterBatteryTypeData: {
                    ...initFilterSelectData
                }
            }
        case RESET_RECORDTIME_ALERT://初始值數據時間
            return {
                ...state,
                isFilterRecordTimeAlert: {
                    ...initFilterDate
                }
            }

        case RESET_ALL_ALERT://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default AlertFilterReducer;