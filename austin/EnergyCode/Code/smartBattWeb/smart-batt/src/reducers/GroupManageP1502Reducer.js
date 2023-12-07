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
import { initFilterSelectData } from '../pages/GroupManage/Page1502/InitDataFormat';

// 初始化
const initialState = {
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
}

const GroupManageP1502Reducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_P1502://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_P1502://國家
            return {
                ...state,
                isFilterCountryData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_P1502://地域
            return {
                ...state,
                isFilterAreaData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_P1502://站台編號
            return {
                ...state,
                isFilterGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPNAME_P1502://站台名稱
            return {
                ...state,
                isFilterGroupNameData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case RESET_COMPANY_P1502:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_P1502:
            return {
                ...state,
                isFilterCountryData: {//初始值國家
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_P1502:
            return {
                ...state,
                isFilterAreaData: {//初始值地域
                    ...initFilterSelectData
                },
            }
        case RESET_GROUPID_P1502:
            return {
                ...state,
                isFilterGroupIdData: {//初始值站台編號
                    ...initFilterSelectData
                }
            }
        case RESET_GROUPNAME_P1502:
            return {
                ...state,
                isFilterGroupNameData: {//初始值站台名稱
                    ...initFilterSelectData
                }
            }
        case RESET_ALL_P1502://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default GroupManageP1502Reducer;