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
import { FilterNames, initFilter, initFilterDate } from './InitDataFormat';//初始化格式

const apiUrl = apipath();
class Filter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            isFilterSelectTable: [],//選單清單
            isFilterSaveList: [],//儲存清單資訊
            isFilterCompanyList: [],//預設公司別清單
            isFilterBatteryGroupIdList: [],//預設電池組ID清單
            isFilterInstallDateList: [//預設安裝日期清單
                { Value: "0", Label: "1073", Type: "String", show: false },
                { Value: "1", Label: "", Type: "Date", show: true },
                { Value: "2", Label: "1083", Type: "String", show: true },
                { Value: "3", Label: "1109", Type: "String", show: false },
                { Value: "4", Label: "1110", Type: "String", show: false },
            ],
            isFilterCompanyData: {//預設公司別
                ...initFilter
            },
            isFilterBatteryGroupIdData: {//預設電池組ID
                ...initFilter
            },
            isFilterInstallDateData: {//預設安裝日期
                ...initFilterDate
            },
            isFilterTotal: 1,//總數，預設有日期所以會有1   
            // defRadioRangeMaxDay: '31',//往未來多少天
            // defRadioRangeMinDay: '31',//往從前多少天
        }
    }
    componentDidMount() {
        const { isFilterCompanyData, isFilterBatteryGroupIdData,
            isFilterInstallDateData } = this.props;
        this.getBattManageFilter(isFilterCompanyData, isFilterBatteryGroupIdData,
        );//取選單資料
        this.getFilter();//篩選儲存清單API(GET)
        this.setState({
            isFilterInstallDateData: isFilterInstallDateData
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterCompanyData.isDataList.length !== this.state.isFilterCompanyData.isDataList.length ||
            prevState.isFilterBatteryGroupIdData.isDataList.length !== this.state.isFilterBatteryGroupIdData.isDataList.length
        ) {
            const list = [
                this.state.isFilterCompanyData.isDataList.length,
                this.state.isFilterBatteryGroupIdData.isDataList.length,
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
        if (prevState.isFilterBatteryGroupIdData.isChecked !== this.state.isFilterBatteryGroupIdData.isChecked) {
            this.setState({
                isFilterBatteryGroupIdData: this.state.isFilterBatteryGroupIdData
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
        const { isFilterTotal, isFilterSaveList } = this.state;//條件總數、已儲存標籤
        const { isFilterCompanyList, isFilterCompanyData } = this.state;//公司
        const { isFilterBatteryGroupIdList, isFilterBatteryGroupIdData } = this.state;//電池組ID
        const { isFilterInstallDateList, isFilterInstallDateData } = this.state;//安裝日期        
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
                        this.props.company === '1' &&
                        <li>{/* 公司別 */}
                            <FilterItemHeader
                                title={"1064"}
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
                    <li>{/* 電池組ID */}
                        <FilterItemHeader
                            title={"1026"}
                            total={isFilterBatteryGroupIdData.isDataList.length}
                            detailList={isFilterBatteryGroupIdData.isButtonList}
                            isChecked={isFilterBatteryGroupIdData.isChecked}
                            isOpen={isFilterBatteryGroupIdData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterBatteryGroupIdList', boolean)}
                            onUpdateChainSelectList={() => this.onUpdateChainSelectList('BatteryValue', this.getSelectObj(isFilterBatteryGroupIdList))}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryGroupIdData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterBatteryGroupIdData.isDataList}
                                defSelectList={isFilterBatteryGroupIdList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterBatteryGroupIdList', list)}
                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList('BatteryValue', object)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryGroupIdData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 安裝日期 */}
                        <FilterItemHeader
                            title={"1027"}
                            total={1}
                            isChecked={false}
                            showCheckBox={false}
                            isOpen={true}
                        >
                            <FilterItemRadio
                                // defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
                                // defRadioRangeMinDay={this.state.defRadioRangeMinDay}
                                defRadioVal={isFilterInstallDateData.Radio}
                                defRadioStart={isFilterInstallDateData.Start}
                                defRadioEnd={isFilterInstallDateData.End}
                                defRadioList={isFilterInstallDateList}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterInstallDateData, object)}
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
            BatteryGroupId: isFilterBatteryGroupIdData,
            InstallDate: isFilterInstallDateData
        } = FilterConfig;
        const postData = {
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
            isFilterInstallDateData: isFilterInstallDateData,
        };
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdData, checkObject(isFilterBatteryGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterInstallDateData, isFilterInstallDateData);
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
        const { isFilterCompanyData, isFilterBatteryGroupIdData,isFilterInstallDateData, ...other } = this.state;
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdData, checkObject(isFilterBatteryGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterInstallDateData, isFilterInstallDateData);
        this.props.submitFilterData(this.state);
        this.setState({
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
            isFilterInstallDateData: isFilterInstallDateData,
            ...other
        });
    }

    getSelectObj = (selectList) => {
        let isSelectObject = {};
        selectList.forEach(item => {
            isSelectObject[item.Value] = item;
        });
        return isSelectObject
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
        let BatteryGroupIdObj = {};
        List.forEach(item => {
            if (item.BatteryValue !== "") { BatteryGroupIdObj[item.BatteryValue] = { Value: item.BatteryValue, Label: item.BatteryLabel, selectShow: true }; }
        });
        const BatteryGroupIdList = Object.values(BatteryGroupIdObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        switch (Virtue) {
            case 'CompanyValue':
                this.setState({
                    isFilterBatteryGroupIdList: [...BatteryGroupIdList],
                    isFilterBatteryGroupIdData: { ...initFilter },
                });
                break;
            default:
        }
    }

    //電池組管理篩選清單API:
    getBattManageFilter = (defaultCompany, defaultBatteryGroupId,
        // defaultBattTypeList
    ) => {
        this.ajaxGetBattManageFilter().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                let CompanyObj = {};
                let BatteryGroupIdObj = {};
                List.forEach(item => {
                    if (item.CompanyValue !== "") { CompanyObj[item.CompanyValue] = { Value: item.CompanyValue, Label: item.CompanyLabel, selectShow: true }; }
                    if (item.BatteryValue !== "") { BatteryGroupIdObj[item.BatteryValue] = { Value: item.BatteryValue, Label: item.BatteryLabel, selectShow: true }; }
                });
                const Companys = Object.values(CompanyObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const BatteryGroupIds = Object.values(BatteryGroupIdObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                this.setState({
                    isFilterSelectTable: [...List],//選單清單
                    isFilterCompanyList: [...Companys],
                    isFilterCompanyData: {
                        ...defaultCompany
                    },
                    isFilterBatteryGroupIdList: [...BatteryGroupIds],
                    isFilterBatteryGroupIdData: {
                        ...defaultBatteryGroupId
                    },
                });
            } else {
                this.setState({
                    isFilterSelectTable: [],//選單清單
                    isFilterCompanyList: [],
                    isFilterBatteryGroupIdList: [],
                    isFilterCompanyData: { ...initFilter },
                    isFilterBatteryGroupIdData: { ...initFilter },
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
                isFilterBatteryGroupIdData: { ...defaultBatteryGroupId },
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
                console.error('電池組管理 ajaxDelFilter', response);
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }

    //電池組管理篩選清單API:
    ajaxGetBattManageFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getBattManageFilter`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //電池組管理篩選清單API:
        //https://www.gtething.tw/battery/getBattManageFilter
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
    //刪除篩選API(POST):
    ajaxDelFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}delFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //刪除篩選API(POST):
        //https://www.gtething.tw/battery/delFilter
    }

}

Filter.defaultProps = {
    functionId: '0',
    isFilterCompanyData: {},
    isFilterBatteryGroupIdData: {},
    isFilterInstallDateData: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
}
Filter.propTypes = {
    functionId: PropTypes.string,
    company: PropTypes.string,
    isFilterCompanyData: PropTypes.object,
    isFilterBatteryGroupIdData: PropTypes.object,
    isFilterInstallDateData: PropTypes.object,
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
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Filter);


