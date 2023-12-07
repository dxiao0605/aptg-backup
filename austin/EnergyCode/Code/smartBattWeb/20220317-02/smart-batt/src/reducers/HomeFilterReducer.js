import {
    UPDATE_COMPANY_HOME,
    UPDATE_COUNTRY_HOME,
    UPDATE_AREA_HOME,
    UPDATE_GROUPID_HOME,
    // UPDATE_GROUPNAME_HOME,
    UPDATE_GROUPIDLIST_STATUS,
    RESET_COMPANY_HOME,
    RESET_COUNTRY_HOME,
    RESET_AREA_HOME,
    RESET_GROUPID_HOME,
    // RESET_GROUPNAME_HOME,
    RESET_ALL_HOME,
} from '../constants/HomeFilter-action-type';
import { initFilterSelectData } from '../pages/Home/HomeFilter/InitDataFormat';


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
    // isFilterGroupNameData: {//預設站台名稱
    //     ...initFilterSelectData
    // },
    isGroupIdListStatus: false,
}

const HomeFilterReducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_HOME://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_HOME://國家
            return {
                ...state,
                isFilterCountryData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_HOME://地域
            return {
                ...state,
                isFilterAreaData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_HOME://站台編號
            return {
                ...state,
                isFilterGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        // case UPDATE_GROUPNAME_HOME://站台名稱
        //     return {
        //         ...state,
        //         isFilterGroupNameData: {
        //             isOpen: action.payload.isOpen,
        //             isChecked: action.payload.isChecked,
        //             isDataList: action.payload.isDataList,
        //             isButtonList: action.payload.isButtonList,
        //         }
        //     }
        case UPDATE_GROUPIDLIST_STATUS: // 判斷通訊序號管理是否執行啟用 
            return {
                ...state,
                isGroupIdListStatus: action.payload.isGroupIdListStatus,
            }

        case RESET_COMPANY_HOME:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_HOME:
            return {
                ...state,
                isFilterCountryData: {//初始值國家
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_HOME:
            return {
                ...state,
                isFilterAreaData: {//初始值地域
                    ...initFilterSelectData
                },
            }
        case RESET_GROUPID_HOME:
            return {
                ...state,
                isFilterGroupIdData: {//初始值站台編號
                    ...initFilterSelectData
                }
            }
        // case RESET_GROUPNAME_HOME:
        //     return {
        //         ...state,
        //         isFilterGroupNameData: {//初始值站台名稱
        //             ...initFilterSelectData
        //         }
        //     }


        case RESET_ALL_HOME://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default HomeFilterReducer;