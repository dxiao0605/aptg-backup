import {
    UPDATE_EVENTTYPECODE_ALERTUNSOLVED,
    UPDATE_COMPANY_ALERTUNSOLVED,
    UPDATE_COUNTRY_ALERTUNSOLVED,
    UPDATE_AREA_ALERTUNSOLVED,
    UPDATE_GROUPID_ALERTUNSOLVED,
    UPDATE_GROUPNAME_ALERTUNSOLVED,
    UPDATE_BATTERYGROUPID_ALERTUNSOLVED,
    UPDATE_BATTERYTYPE_ALERTUNSOLVED,
    UPDATE_RECORDTIME_ALERTUNSOLVED,
    RESET_EVENTTYPECODE_ALERTUNSOLVED,
    RESET_COMPANY_ALERTUNSOLVED,
    RESET_COUNTRY_ALERTUNSOLVED,
    RESET_AREA_ALERTUNSOLVED,
    RESET_GROUPID_ALERTUNSOLVED,
    RESET_GROUPNAME_ALERTUNSOLVED,
    RESET_BATTERYGROUPID_ALERTUNSOLVED,
    RESET_BATTERYTYPE_ALERTUNSOLVED,
    RESET_RECORDTIME_ALERTUNSOLVED,
    RESET_ALL_ALERTUNSOLVED,
} from '../constants/AlertUnsolvedFilter-action-type';
import { initFilterSelectData,initFilterDate } from '../pages/AlertUnsolved/AlertUnSolvedFilter/InitDataFormat';


// 初始化
const initialState = {
    isFilterEventTypeCodeDataUnSolved: {//預設告警類型
        ...initFilterSelectData
    },
    isFilterCompanyDataUnSolved: {//預設公司別
        ...initFilterSelectData
    },
    isFilterCountryDataUnSolved: {//預設國家
        ...initFilterSelectData
    },
    isFilterAreaDataUnSolved: {//預設地域
        ...initFilterSelectData
    },
    isFilterGroupIdDataUnSolved: {//預設站台編號
        ...initFilterSelectData
    },
    isFilterGroupNameDataUnSolved: {//預設站台名稱
        ...initFilterSelectData
    },
    isFilterBatteryGroupIdDataUnSolved: {//預設電池組ID
        ...initFilterSelectData
    },
    isFilterBatteryTypeDataUnSolved: {//預設電池型號
        ...initFilterSelectData
    },
    isFilterRecordTimeUnSolved: {
        ...initFilterDate
    }
}

const AlertUnsolvedFilterReducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_EVENTTYPECODE_ALERTUNSOLVED://告警類型
            return {
                ...state,
                isFilterEventTypeCodeDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COMPANY_ALERTUNSOLVED://公司別
            return {
                ...state,
                isFilterCompanyDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_ALERTUNSOLVED://國家
            return {
                ...state,
                isFilterCountryDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_ALERTUNSOLVED://地域
            return {
                ...state,
                isFilterAreaDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_ALERTUNSOLVED://站台編號
            return {
                ...state,
                isFilterGroupIdDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPNAME_ALERTUNSOLVED://站台名稱
            return {
                ...state,
                isFilterGroupNameDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYGROUPID_ALERTUNSOLVED://電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYTYPE_ALERTUNSOLVED://電池型號
            return {
                ...state,
                isFilterBatteryTypeDataUnSolved: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_RECORDTIME_ALERTUNSOLVED://數據時間
            return {
                ...state,
                isFilterRecordTimeUnSolved: {
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

        case RESET_EVENTTYPECODE_ALERTUNSOLVED:
            return {
                ...state,
                isFilterEventTypeCodeDataUnSolved: {//初始值告警類型
                    ...initFilterSelectData
                }
            }
        case RESET_COMPANY_ALERTUNSOLVED:
            return {
                ...state,
                isFilterCompanyDataUnSolved: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_ALERTUNSOLVED:
            return {
                ...state,
                isFilterCountryDataUnSolved: {//初始值國家
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_ALERTUNSOLVED:
            return {
                ...state,
                isFilterAreaDataUnSolved: {//初始值地域
                    ...initFilterSelectData
                },
            }
        case RESET_GROUPID_ALERTUNSOLVED:
            return {
                ...state,
                isFilterGroupIdDataUnSolved: {//初始值站台編號
                    ...initFilterSelectData
                }
            }
        case RESET_GROUPNAME_ALERTUNSOLVED:
            return {
                ...state,
                isFilterGroupNameDataUnSolved: {//初始值站台名稱
                    ...initFilterSelectData
                }
            }


        case RESET_BATTERYGROUPID_ALERTUNSOLVED://初始值電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdDataUnSolved: {
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYTYPE_ALERTUNSOLVED://初始值電池型號
            return {
                ...state,
                isFilterBatteryTypeDataUnSolved: {
                    ...initFilterSelectData
                }
            }
        case RESET_RECORDTIME_ALERTUNSOLVED://初始值數據間
            return {
                ...state,
                isFilterRecordTimeUnSolved: {
                    ...initFilterDate
                }
            }

        case RESET_ALL_ALERTUNSOLVED://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default AlertUnsolvedFilterReducer;