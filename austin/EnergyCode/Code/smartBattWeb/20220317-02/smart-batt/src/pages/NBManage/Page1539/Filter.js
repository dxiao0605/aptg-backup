import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import Divider from '@material-ui/core/Divider';
import { apipath, ajax } from '../../../utils/ajax';
import { CusMainBtnStyle } from '../../../components/CusMainBtnStyle';
import FilterSave from './FilterSave';//儲存標籤紀錄
import FilterItemHeader from '../../../components/FilterDrawer/FilterItemHeader';//每筆Item的header
import { checkObject } from '../../../components/FilterDrawer/utils';//整理checkbox狀態
import FilterItemSelect from '../../../components/FilterDrawer/FilterItemSelect';//每筆Item的Select
import FilterItemRadio from '../../../components/FilterDrawer/FilterItemRadio';//每筆Item的Radio
import { FilterNames, initFilterSelectData, initFilterDate } from './InitDataFormat';//初始化格式

const apiUrl = apipath();
class Filter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            isFilterSelectTable: [],//選單清單
            isFilterCompanyList: [],//預設異動公司清單
            isFilterNBIDList: [],//預設通訊序號清單
            isFilterModifyItemList: [],//預設異動項目清單           
            isFilterModifyTimeList: [//預設異動時間
                { Value: "0", Label: "1073", Type: "String", show: false },
                { Value: "1", Label: "", Type: "Date", show: true },
                { Value: "2", Label: "1083", Type: "String", show: false },
                { Value: "3", Label: "1109", Type: "String", show: true },
                { Value: "4", Label: "1110", Type: "String", show: false },
            ],
            isFilterCompanyData: {//預設異動公司
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
            isFilterTotal: 1,//總數
            defRadioRangeMaxDay: '31',//往未來多少天
            defRadioRangeMinDay: '31',//往從前多少天
        }
    }
    componentDidMount() {
        const { isFilterCompanyData,isFilterNBIDData, isFilterModifyItemData, isFilterModifyTimeData,} = this.props;
        this.getNBHistoryFilter(isFilterCompanyData,isFilterNBIDData, isFilterModifyItemData);//取選單資料        
        this.getFilter();//篩選儲存清單API(GET)
        this.setState({
            isFilterModifyTimeData: isFilterModifyTimeData
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterCompanyData.isDataList.length !== this.state.isFilterCompanyData.isDataList.length ||
            prevState.isFilterNBIDData.isDataList.length !== this.state.isFilterNBIDData.isDataList.length ||
            prevState.isFilterModifyItemData.isDataList.length !== this.state.isFilterModifyItemData.isDataList.length
        ) {
            const list = [
                this.state.isFilterCompanyData.isDataList.length,
                this.state.isFilterNBIDData.isDataList.length,
                this.state.isFilterModifyItemData.isDataList.length,
            ];
            let total = 1;
            list.forEach(num => {
                total = total + num
            });
            this.setState({
                isFilterTotal: total
            })
        }
        if (prevState.isFilterCompanyData.isChecked !== this.state.isFilterCompanyData.isChecked) {
            this.setState({
                isFilterCompanyData: this.state.isFilterCompanyData
            })
        }
        if (prevState.isFilterNBIDData.isChecked !== this.state.isFilterNBIDData.isChecked) {
            this.setState({
                isFilterNBIDData: this.state.isFilterNBIDData
            })
        }
        if (prevState.isFilterModifyItemData.isChecked !== this.state.isFilterModifyItemData.isChecked) {
            this.setState({
                isFilterModifyItemData: this.state.isFilterModifyItemData
            })
        }
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { isFilterTotal, isFilterSaveList } = this.state;
        const { isFilterCompanyList, isFilterCompanyData } = this.state;//公司
        const { isFilterNBIDList, isFilterNBIDData } = this.state;//通訊序號
        const { isFilterModifyItemList, isFilterModifyItemData } = this.state;//異動項目
        const { isFilterModifyTimeList, isFilterModifyTimeData } = this.state;//異動時間           
        return (
            <Fragment>
                <div className="form-inline align-items-center justify-content-center pt-1 pb-2">
                    <i className="fas fa-filter mr-1" />
                    <Trans i18nKey={this.props.functionId} />
                    {/* 總計 */}
                    <span className="filter_prompt ml-1 noDetail">
                        {isFilterTotal}
                    </span>
                </div>
                <Divider />
                {/* 紀錄 */}
                <FilterSave
                    company={this.props.company}
                    list={isFilterSaveList}
                    onClickBookmark={(FilterConfig) => { this.onClickBookmark(FilterConfig) }}
                    onClickTrash={(FilterID) => { this.delFilter(FilterID) }}
                />
                <Divider />
                {/* 篩選器 */}
                <ul className={`filter overflowY mb-0 ${navigator.userAgent.indexOf('Windows') > 0 ? 'Windows' : 'Mac'}`}>
                    {
                        this.props.company === "1" &&
                        <li>{/* 異動者公司 */}
                            <FilterItemHeader
                                title={"1538"}
                                total={isFilterCompanyData.isDataList.length}
                                detailList={isFilterCompanyData.isButtonList}
                                isChecked={isFilterCompanyData.isChecked}
                                isOpen={isFilterCompanyData.isOpen}
                                onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCompanyList', boolean)}
                                onUpdateChainSelectList={() => this.onUpdateChainSelectList('CompanyValue', this.getSelectObj(isFilterCompanyList))}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                            >
                                <FilterItemSelect
                                    defSelectVal={isFilterCompanyData.isDataList}
                                    defSelectList={isFilterCompanyList}
                                    onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCompanyList', list)}
                                    onUpdateChainSelectList={(object) => this.onUpdateChainSelectList('CompanyValue', object)}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                                />
                            </FilterItemHeader>
                        </li>
                    }
                    <li>{/* 異動項目 */}
                        <FilterItemHeader
                            title={"1059"}
                            total={isFilterModifyItemData.isDataList.length}
                            detailList={isFilterModifyItemData.isButtonList}
                            isChecked={isFilterModifyItemData.isChecked}
                            isOpen={isFilterModifyItemData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterModifyItemList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterModifyItemData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterModifyItemData.isDataList}
                                defSelectList={isFilterModifyItemList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterModifyItemList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterModifyItemData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 通訊序號 */}
                        <FilterItemHeader
                            title={"1057"}
                            total={isFilterNBIDData.isDataList.length}
                            detailList={isFilterNBIDData.isButtonList}
                            isChecked={isFilterNBIDData.isChecked}
                            isOpen={isFilterNBIDData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterNBIDList', boolean)}
                            onUpdateChainSelectList={() => this.onUpdateChainSelectList('NBIDValue', this.getSelectObj(isFilterNBIDList))}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterNBIDData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterNBIDData.isDataList}
                                defSelectList={isFilterNBIDList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterNBIDList', list)}
                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList('NBIDValue', object)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterNBIDData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 異動時間 */}
                        <FilterItemHeader
                            title={"1060"}
                            total={1}
                            isChecked={false}
                            showCheckBox={false}
                            isOpen={true}
                        >
                            <FilterItemRadio
                                defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
                                defRadioRangeMinDay={this.state.defRadioRangeMinDay}
                                defRadioVal={isFilterModifyTimeData.Radio}
                                defRadioStart={isFilterModifyTimeData.Start}
                                defRadioEnd={isFilterModifyTimeData.End}
                                defRadioList={isFilterModifyTimeList}
                                defRadioOpenStartTime={true}
                                defRadioStartHH={isFilterModifyTimeData.StartHH}
                                defRadioStartMM={isFilterModifyTimeData.StartMM}
                                defRadioEndHH={isFilterModifyTimeData.EndHH}
                                defRadioEndMM={isFilterModifyTimeData.EndMM}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterModifyTimeData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <Divider />
                    <div className="d-block py-3 px-4">
                        <CusMainBtnStyle
                            name={<Trans i18nKey={"1002"} />}
                            icon={"fas fa-check mr-2"}
                            fullWidth={true}
                            clickEvent={() => this.handleSumbit()}
                        />
                    </div>
                </ul>
            </Fragment>
        )
    }
    onClickBookmark = (FilterConfig) => {
        const {
            Company: isFilterCompanyData,
            NBID: isFilterNBIDData,
            ModifyItem: isFilterModifyItemData,
            ModifyTime: isFilterModifyTimeData,
        } = FilterConfig;
        const postData = {
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterNBIDData: checkObject(isFilterNBIDData),
            isFilterModifyItemData: checkObject(isFilterModifyItemData),
            isFilterModifyTimeData: isFilterModifyTimeData,
        };
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterNBIDData, checkObject(isFilterNBIDData));
        this.props.updateFilterData(FilterNames.isFilterModifyItemData, checkObject(isFilterModifyItemData));
        this.props.updateFilterData(FilterNames.isFilterModifyTimeData, isFilterModifyTimeData);
        this.props.submitFilterData(postData);
        this.setState({ ...postData });
    }
    //checkboxAll更改選單顯示
    onClickAllSelectList = (name, boolean) => {
        const newList = this.state[name].map(item => {
            return { ...item, selectShow: boolean }
        });
        this.setState({ [name]: [...newList] });
    }
    //更新選單list
    onUpdateSelectList = (name, list) => {
        this.setState({ [name]: [...list] });
    }
    //更新選單data
    onUpdateList = (name, object) => {
        this.setState({ [name]: { ...object } });
    }
    handleSumbit = () => {
        const { isFilterCompanyData,isFilterNBIDData, isFilterModifyItemData, isFilterModifyTimeData, ...other } = this.state;
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterNBIDData, checkObject(isFilterNBIDData));
        this.props.updateFilterData(FilterNames.isFilterModifyItemData, checkObject(isFilterModifyItemData));
        this.props.updateFilterData(FilterNames.isFilterModifyTimeData, isFilterModifyTimeData);
        this.props.submitFilterData(this.state);
        this.setState({
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterNBIDData: checkObject(isFilterNBIDData),
            isFilterModifyItemData: checkObject(isFilterModifyItemData),
            isFilterModifyTimeData: isFilterModifyTimeData,
            ...other
        });

    }

    //異動記錄篩選清單API:
    getNBHistoryFilter = (defaultCompany,
        // defaultBatteryGroupId, 
        defaultNBID, defaultModifyItem,) => {
        this.ajaxGetNBHistoryFilter().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { NBID, ModifyItem, } = msg;
                let CompanyObj = {};
                let NBIDObj = {};
                NBID.forEach(item => {
                    if (item.CompanyValue !== "") { CompanyObj[item.CompanyValue] = { Value: item.CompanyValue, Label: item.CompanyLabel, selectShow: true }; }
                    if (item.NBIDValue !== "") { NBIDObj[item.NBIDValue] = { Value: item.NBIDValue, Label: item.NBIDLabel, selectShow: true }; }
                });
                const Companys = Object.values(CompanyObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const NBIDs = Object.values(NBIDObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const ModifyItems = Array.isArray(ModifyItem) ? ModifyItem.map(item => ({ ...item, selectShow: true })) : [];
                this.setState({
                    isFilterSelectTable: NBID,
                    isFilterCompanyList: [...Companys],
                    isFilterCompanyData: {
                        ...defaultCompany
                    },
                    isFilterNBIDList: [...NBIDs],
                    isFilterNBIDData: {
                        ...defaultNBID
                    },
                    isFilterModifyItemList: [...ModifyItems],
                    isFilterModifyItemData: {
                        ...defaultModifyItem
                    },
                });
            } else {
                this.setState({
                    isFilterSelectTable: [],
                    isFilterCompanyList: [],//預設公司別清單
                    isFilterNBIDList: [],//預設通訊序號清單
                    isFilterModifyItemList: [],//預設異動項目清單
                    isFilterCompanyData: { ...initFilterSelectData },
                    isFilterNBIDData: { ...initFilterSelectData },
                    isFilterModifyItemData: { ...initFilterSelectData },
                    isFilterModifyTimeData: { ...initFilterDate },
                });
            }
        }).then(() => {
            //處理已選擇項目的選單   
            if (defaultCompany.isDataList.length > 0) {
                let obj = {};
                defaultCompany.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList('CompanyValue', obj);
            }
            this.setState({
                isFilterCompanyData: { ...defaultCompany },
                isFilterNBIDData: { ...defaultNBID },
                isFilterModifyItemData: { ...defaultModifyItem },
            });
        });
    }
    //取得篩選儲存清單API(GET)
    getFilter = () => {
        this.ajaxGetFilter().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Filter } = msg;
                this.setState({
                    isFilterSaveList: [...Filter],
                });
            } else {
                this.setState({
                    isFilterSaveList: [],
                });
            }
        });
    }
    //刪除篩選API(GET)
    delFilter = (FilterID) => {
        this.ajaxDelFilter({ FilterID }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.props.alertFilterMsgOpen(msg);
            } else if (code === '07') {
                this.props.alertFilterMsgOpen(msg);
            } else {
                console.error('異動記錄 ajaxDelFilter', response);
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }

    //選擇公司別，更新其他連動選單
    onUpdateChainSelectList = (Virtue, object) => {
        const { isFilterSelectTable } = this.state;
        const Lists = isFilterSelectTable.filter(item => {
            if (object[item[Virtue]]) {
                return true;
            } else {
                return false;
            }
        });
        let List = [];
        switch (Virtue) {
            case 'CompanyValue':
                List = [...Lists];
                break;
            default:
        }
        let NBIDObj = {};
        List.forEach(item => {
            if (item.NBIDValue !== "") { NBIDObj[item.NBIDValue] = { Value: item.NBIDValue, Label: item.NBIDLabel, selectShow: true }; }
        });
        const NBIDs = Object.values(NBIDObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        switch (Virtue) {
            case 'CompanyValue':
                this.setState({
                    isFilterNBIDList: [...NBIDs],
                    isFilterNBIDData: { ...initFilterSelectData },
                });
                break;
            default:
        }
    }
    getSelectObj = (selectList) => {
        let isSelectObject = {};
        selectList.forEach(item => {
            isSelectObject[item.Value] = item;
        });
        return isSelectObject
    }

    //取得通訊模組異動紀錄篩選(GET):
    ajaxGetNBHistoryFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getNBHistoryFilter`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //取得通訊模組異動紀錄篩選(GET):
        //https://www.gtething.tw/battery/getNBHistoryFilter
    }
    //取得篩選儲存清單API(GET)
    ajaxGetFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const { account, functionId } = this.props;
        const url = `${apiUrl}getFilter?account=${account}&functionId=${functionId}`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //取得篩選儲存清單API(GET)
        //https://www.gtething.tw/battery/getFilter?account=admin&functionId=1501
    }
    //刪除篩選儲存清單API(POST):
    ajaxDelFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}delFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //刪除篩選儲存清單API(POST):
        //https://www.gtething.tw/battery/delFilter
    }
}

Filter.defaultProps = {
    functionId: '0',
    isFilterCompanyData: {},
    isFilterNBIDData: {},
    isFilterModifyItemData: {},
    isFilterModifyTimeData: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
}
Filter.propTypes = {
    functionId: PropTypes.string,
    isFilterCompanyData: PropTypes.object,
    isFilterNBIDData: PropTypes.object,
    isFilterModifyItemData: PropTypes.object,
    isFilterModifyTimeData: PropTypes.object,
    updateFilterData: PropTypes.func,
    submitFilterData: PropTypes.func,
    alertFilterMsgOpen: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
    }
}
export default connect(mapStateToProps)(Filter);


