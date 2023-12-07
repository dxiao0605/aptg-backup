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
import { initFilterSelectData, initFilterDate } from '../pages/NBManage/Page1539/InitDataFormat';


// 初始化
const initialState = {
    isFilterCompanyData: {//預設公司別
        ...initFilterSelectData
    },
    isFilterBatteryGroupIdData: {//預設電池組ID
        ...initFilterSelectData
    },
    isFilterNBIDData: {//預設通訊序號
        ...initFilterSelectData
    },
    isFilterModifyItemData: {//預設異動項目
        ...initFilterSelectData
    },
    isFilterModifyTimeData: {//預設異動時間
        ...initFilterDate
    },
}

const NBManageP1539Reducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_P1539://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYGROUPID_P1539://電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_NBID_P1539://通訊序號
            return {
                ...state,
                isFilterNBIDData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_MODIFYITEM_P1539://異動項目
            return {
                ...state,
                isFilterModifyItemData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_MODIFYTIME_P1539://異動時間
            return {
                ...state,
                isFilterModifyTimeData:  {
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
        case RESET_COMPANY_P1539:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYGROUPID_P1539:
            return {
                ...state,
                isFilterBatteryGroupIdData: {//初始值電池組ID
                    ...initFilterSelectData
                }
            }
        case RESET_NBID_P1539:
            return {
                ...state,
                isFilterNBIDData: {//初始值通訊序號
                    ...initFilterSelectData
                },
            }
        case RESET_MODIFYITEM_P1539:
            return {
                ...state,
                isFilterModifyItemData: {//初始值異動項目
                    ...initFilterSelectData
                }
            }
        case RESET_MODIFYTIME_P1539:
            return {
                ...state,
                isFilterModifyTimeData: {
                    ...initFilterDate
                }
            }
        case RESET_ALL_P1539://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default NBManageP1539Reducer;