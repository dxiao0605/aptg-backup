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
import { initFilterSelectData, initFilterDate } from '../pages/Command/Page1559/InitDataFormat';


// 初始化
const initialState = {
    tableHeader: [ // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
        { id: '1064', sortName: 'Company', fixed: false, active: true },// 公司
        { id: '1028', sortName: 'Country', fixed: false, active: true },// 國家
        { id: '1029', sortName: 'Area', fixed: false, active: true },// 地域
        { id: '1012', sortName: 'GroupID', fixed: false, active: true },// 站台編號
        { id: '1013', sortName: 'GroupName', fixed: false, active: true },// 站台名稱
        { id: '1030', sortName: 'BatteryTypeName', fixed: false, active: true },// 電池型號
        { id: '1026', sortName: 'BatteryGroupID', fixed: false, active: true },// 電池組ID                
        { id: '1566', sortName: 'Command', fixed: false, active: true },// 指令
        { id: '1066', sortName: 'SendTime', fixed: false, active: true },// 傳送時間
        { id: '1067', sortName: 'PublishTime', fixed: false, active: true },// 發佈時間
        { id: '1068', sortName: 'AckTime', fixed: false, active: true },// Ack時間
        { id: '1069', sortName: 'ResponseTime', fixed: false, active: true },// Resp時間
        { id: '1070', sortName: 'Response', fixed: false, active: true },// 回應訊息
        { id: '1071', sortName: 'Config', fixed: false, active: true },// 指令設定值
    ],
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
    isFilterCommandData: {//預設指令
        ...initFilterSelectData
    },
    isFilterResponseData: {//預設回應訊息
        ...initFilterSelectData
    },
    isFilterSendTimeData: {//預設傳送時間
        ...initFilterDate
    },
}

const CommandP1559Reducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_TABLEHEADER_P1559://表格欄位名稱
            return {
                ...state,
                tableHeader: [...action.payload.tableHeader]
            }
        case UPDATE_COMPANY_P1559://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_P1559://國家
            return {
                ...state,
                isFilterCountryData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_P1559://地域
            return {
                ...state,
                isFilterAreaData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_P1559://站台編號
            return {
                ...state,
                isFilterGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPNAME_P1559://站台名稱
            return {
                ...state,
                isFilterGroupNameData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYGROUPID_P1559://電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYTYPE_P1559://電池型號
            return {
                ...state,
                isFilterBatteryTypeData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COMMAND_P1559://指令
            return {
                ...state,
                isFilterCommandData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_RESPONSE_P1559://回應訊息
            return {
                ...state,
                isFilterResponseData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_SENDTIME_P1559://傳送時間
            return {
                ...state,
                isFilterSendTimeData: {
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

        case RESET_COMPANY_P1559:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_P1559:
            return {
                ...state,
                isFilterCountryData: {//初始值國家
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_P1559:
            return {
                ...state,
                isFilterAreaData: {//初始值地域
                    ...initFilterSelectData
                },
            }
        case RESET_GROUPID_P1559:
            return {
                ...state,
                isFilterGroupIdData: {//初始值站台編號
                    ...initFilterSelectData
                }
            }
        case RESET_GROUPNAME_P1559:
            return {
                ...state,
                isFilterGroupNameData: {//初始值站台名稱
                    ...initFilterSelectData
                }
            }


        case RESET_BATTERYGROUPID_P1559://初始值電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYTYPE_P1559://初始值電池型號
            return {
                ...state,
                isFilterBatteryTypeData: {
                    ...initFilterSelectData
                }
            }
        case RESET_COMMAND_P1559://初始值指令
            return {
                ...state,
                isFilterCommandData: {
                    ...initFilterSelectData
                }
            }
        case RESET_RESPONSE_P1559://初始值回應訊息
            return {
                ...state,
                isFilterResponseData: {
                    ...initFilterSelectData
                }
            }
        case RESET_SENDTIME_P1559://初始值傳送時間
            return {
                ...state,
                isFilterSendTimeData: {
                    ...initFilterDate
                }
            }

        case RESET_ALL_P1559://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default CommandP1559Reducer;