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
import { FilterNames, FilterNames_API, initFilterSelectData, initFilterDate } from './InitDataFormat';//初始化格式

const apiUrl = apipath();
class Filter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            isFilterSelectTable: [],//選單清單total
            isFilterCompanyList: [],//預設公司別清單
            isFilterCountryList: [],//預設國家清單
            isFilterAreaList: [],//預設地域清單
            isFilterGroupIdList: [],//預設站台編號清單
            isFilterCommandList: [],//預設指令清單
            isFilterResponseList: [],//預設回應訊息清單            
            isFilterSendTimeList: [//預設傳送時間
                { Value: "0", Label: "1073", Type: "String", show: false },
                { Value: "1", Label: "", Type: "Date", show: true },
                { Value: "2", Label: "1083", Type: "String", show: false },
                { Value: "3", Label: "1109", Type: "String", show: true },
                { Value: "4", Label: "1110", Type: "String", show: false },
            ],
            isFilterCompanyData: {//預設公司別
                ...initFilterSelectData
            },
            isFilterCountryData: {//國家
                ...initFilterSelectData
            },
            isFilterAreaData: {//預設地域
                ...initFilterSelectData
            },
            isFilterGroupIdData: {//預設站台編號
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
            isFilterTotal: 1,//總數
            defRadioRangeMaxDay: '31',//往未來多少天
            defRadioRangeMinDay: '31',//往從前多少天
        }
    }
    componentDidMount() {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData, isFilterSendTimeData,
        } = this.props;
        this.getCommandFilter(isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData
        );//取選單資料        
        this.getFilter();//篩選儲存清單API(GET)
        this.setState({
            isFilterSendTimeData: isFilterSendTimeData
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterCompanyData.isDataList.length !== this.state.isFilterCompanyData.isDataList.length ||
            prevState.isFilterCountryData.isDataList.length !== this.state.isFilterCountryData.isDataList.length ||
            prevState.isFilterAreaData.isDataList.length !== this.state.isFilterAreaData.isDataList.length ||
            prevState.isFilterGroupIdData.isDataList.length !== this.state.isFilterGroupIdData.isDataList.length ||
            prevState.isFilterCommandData.isDataList.length !== this.state.isFilterCommandData.isDataList.length ||
            prevState.isFilterResponseData.isDataList.length !== this.state.isFilterResponseData.isDataList.length
        ) {
            const list = [
                this.state.isFilterCompanyData.isDataList.length,
                this.state.isFilterCountryData.isDataList.length,
                this.state.isFilterAreaData.isDataList.length,
                this.state.isFilterGroupIdData.isDataList.length,
                this.state.isFilterCommandData.isDataList.length,
                this.state.isFilterResponseData.isDataList.length,
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
        if (prevState.isFilterCountryData.isChecked !== this.state.isFilterCountryData.isChecked) {
            this.setState({
                isFilterCountryData: this.state.isFilterCountryData
            })
        }
        if (prevState.isFilterAreaData.isChecked !== this.state.isFilterAreaData.isChecked) {
            this.setState({
                isFilterAreaData: this.state.isFilterAreaData
            })
        }
        if (prevState.isFilterGroupIdData.isChecked !== this.state.isFilterGroupIdData.isChecked) {
            this.setState({
                isFilterGroupIdData: this.state.isFilterGroupIdData
            })
        }
        if (prevState.isFilterCommandData.isChecked !== this.state.isFilterCommandData.isChecked) {            
            this.setState({
                isFilterCommandData: this.state.isFilterCommandData
            })
        }
        if (prevState.isFilterResponseData.isChecked !== this.state.isFilterResponseData.isChecked) {
            this.setState({
                isFilterResponseData: this.state.isFilterResponseData
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
        const { isFilterCountryList, isFilterCountryData } = this.state;//國家
        const { isFilterAreaList, isFilterAreaData } = this.state;//地域
        const { isFilterGroupIdList, isFilterGroupIdData } = this.state;//站台編號
        const { isFilterCommandList, isFilterCommandData } = this.state;//指令
        const { isFilterResponseList, isFilterResponseData } = this.state;//回應訊息
        const { isFilterSendTimeList, isFilterSendTimeData } = this.state;//傳送時間        
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
                        <li>{/* 公司別 */}
                            <FilterItemHeader
                                title={"1064"}
                                total={isFilterCompanyData.isDataList.length}
                                detailList={isFilterCompanyData.isButtonList}
                                isChecked={isFilterCompanyData.isChecked}
                                isOpen={isFilterCompanyData.isOpen}
                                onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCompanyList', boolean)}
                                onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, this.getSelectObj(isFilterCompanyList))}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                            >
                                <FilterItemSelect
                                    defSelectVal={isFilterCompanyData.isDataList}
                                    defSelectList={isFilterCompanyList}
                                    onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCompanyList', list)}
                                    onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, object)}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                                />
                            </FilterItemHeader>
                        </li>
                    }
                    <li>{/* 國家 */}
                        <FilterItemHeader
                            title={"1028"}
                            total={isFilterCountryData.isDataList.length}
                            detailList={isFilterCountryData.isButtonList}
                            isChecked={isFilterCountryData.isChecked}
                            isOpen={isFilterCountryData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCountryList', boolean)}
                            onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, this.getSelectObj(isFilterCountryList))}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCountryData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterCountryData.isDataList}
                                defSelectList={isFilterCountryList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCountryList', list)}
                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, object)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCountryData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 地域 */}
                        <FilterItemHeader
                            title={"1029"}
                            total={isFilterAreaData.isDataList.length}
                            detailList={isFilterAreaData.isButtonList}
                            isChecked={isFilterAreaData.isChecked}
                            isOpen={isFilterAreaData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterAreaList', boolean)}
                            onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, this.getSelectObj(isFilterAreaList))}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterAreaData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterAreaData.isDataList}
                                defSelectList={isFilterAreaList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterAreaList', list)}
                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, object)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterAreaData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 站台名稱/號碼 */}
                        <FilterItemHeader
                            title={"1125"}
                            total={isFilterGroupIdData.isDataList.length}
                            detailList={isFilterGroupIdData.isButtonList}
                            isChecked={isFilterGroupIdData.isChecked}
                            isOpen={isFilterGroupIdData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterGroupIdList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterGroupIdData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterGroupIdData.isDataList}
                                defSelectList={isFilterGroupIdList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterGroupIdList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterGroupIdData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 指令 */}
                        <FilterItemHeader
                            title={"1566"}
                            total={isFilterCommandData.isDataList.length}
                            detailList={isFilterCommandData.isButtonList}
                            isChecked={isFilterCommandData.isChecked}
                            isOpen={isFilterCommandData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCommandList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCommandData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterCommandData.isDataList}
                                defSelectList={isFilterCommandList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCommandList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCommandData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 回應訊息 */}
                        <FilterItemHeader
                            title={"1070"}
                            total={isFilterResponseData.isDataList.length}
                            detailList={isFilterResponseData.isButtonList}
                            isChecked={isFilterResponseData.isChecked}
                            isOpen={isFilterResponseData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterResponseList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterResponseData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterResponseData.isDataList}
                                defSelectList={isFilterResponseList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterResponseList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterResponseData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 傳送時間 */}
                        <FilterItemHeader
                            title={"1066"}
                            total={1}
                            isChecked={false}
                            showCheckBox={false}
                            isOpen={true}
                        >
                            <FilterItemRadio
                                defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
                                defRadioRangeMinDay={this.state.defRadioRangeMinDay}
                                defRadioVal={isFilterSendTimeData.Radio}
                                defRadioStart={isFilterSendTimeData.Start}
                                defRadioEnd={isFilterSendTimeData.End}
                                defRadioList={isFilterSendTimeList}
                                defRadioOpenStartTime={true}
                                defRadioStartHH={isFilterSendTimeData.StartHH}
                                defRadioStartMM={isFilterSendTimeData.StartMM}
                                defRadioEndHH={isFilterSendTimeData.EndHH}
                                defRadioEndMM={isFilterSendTimeData.EndMM}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterSendTimeData, object)}
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
            Country: isFilterCountryData,
            Area: isFilterAreaData,
            GroupID: isFilterGroupIdData,
            Command: isFilterCommandData,
            Response: isFilterResponseData,
            SendTime: isFilterSendTimeData,
        } = FilterConfig;
        const postData = {
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterCountryData: checkObject(isFilterCountryData),
            isFilterAreaData: checkObject(isFilterAreaData),
            isFilterGroupIdData: checkObject(isFilterGroupIdData),
            isFilterCommandData: checkObject(isFilterCommandData),
            isFilterResponseData: checkObject(isFilterResponseData),
            isFilterSendTimeData: isFilterSendTimeData,
        };
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterCountryData, checkObject(isFilterCountryData));
        this.props.updateFilterData(FilterNames.isFilterAreaData, checkObject(isFilterAreaData));
        this.props.updateFilterData(FilterNames.isFilterGroupIdData, checkObject(isFilterGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterCommandData, checkObject(isFilterCommandData));
        this.props.updateFilterData(FilterNames.isFilterResponseData, checkObject(isFilterResponseData));
        this.props.updateFilterData(FilterNames.isFilterSendTimeData, isFilterSendTimeData);
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
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData, isFilterSendTimeData,
            ...other } = this.state;
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterCountryData, checkObject(isFilterCountryData));
        this.props.updateFilterData(FilterNames.isFilterAreaData, checkObject(isFilterAreaData));
        this.props.updateFilterData(FilterNames.isFilterGroupIdData, checkObject(isFilterGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterCommandData, checkObject(isFilterCommandData));
        this.props.updateFilterData(FilterNames.isFilterResponseData, checkObject(isFilterResponseData));
        this.props.updateFilterData(FilterNames.isFilterSendTimeData, isFilterSendTimeData);
        this.props.submitFilterData(this.state);
        this.setState({
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterCountryData: checkObject(isFilterCountryData),
            isFilterAreaData: checkObject(isFilterAreaData),
            isFilterGroupIdData: checkObject(isFilterGroupIdData),
            isFilterCommandData: checkObject(isFilterCommandData),
            isFilterResponseData: checkObject(isFilterResponseData),
            isFilterSendTimeData: isFilterSendTimeData,
            ...other
        });

    }

    //電池組管理篩選清單API:
    getCommandFilter = (defaultCompany, defaultCountry, defaultArea, defaultGroupId,defaultCommand, defaultResponse,) => {
        this.ajaxGetCommandFilter().then((response) => {
            if (this.ajaxCancel) { new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List, Command, Response, } = msg;
                let CompanyObj = {};
                let CountryObj = {};
                let AreaObj = {};
                let GroupIdObj = {};
                List.forEach(item => {
                    if (item.CompanyValue !== "") { CompanyObj[item.CompanyValue] = { Value: item.CompanyValue, Label: item.CompanyLabel, selectShow: true }; }
                    if (item.CountryValue !== "") { CountryObj[item.CountryValue] = { Value: item.CountryValue, Label: item.CountryLabel, selectShow: true }; }
                    if (item.AreaValue !== "") { AreaObj[item.AreaValue] = { Value: item.AreaValue, Label: item.AreaLabel, selectShow: true }; }
                    if (item.GroupValue !== "") { GroupIdObj[item.GroupValue] = { Value: item.GroupValue, Label: item.GroupLabel, selectShow: true }; }
                });
                const CompanyList = Object.values(CompanyObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const CountryList = Object.values(CountryObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const AreaList = Object.values(AreaObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const GroupList = Object.values(GroupIdObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const CommandList = Command.map(item => ({ ...item, selectShow: true }));
                const ResponseList = Response.map(item => ({ ...item, selectShow: true }));
                this.setState({
                    isFilterSelectTable: [...List],
                    isFilterCompanyList: [...CompanyList],
                    isFilterCompanyData: {
                        ...defaultCompany
                    },
                    isFilterCountryList: [...CountryList],
                    isFilterCountryData: {
                        ...defaultCountry
                    },
                    isFilterAreaList: [...AreaList],
                    isFilterAreaData: {
                        ...defaultArea
                    },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: {
                        ...defaultGroupId
                    },
                    isFilterCommandList: [...CommandList],
                    isFilterCommandData: {
                        ...defaultCommand
                    },
                    isFilterResponseList: [...ResponseList],
                    isFilterResponseData: {
                        ...defaultResponse
                    }
                });
                return new Promise((resolve, reject) => (resolve('')));
            } else {
                this.setState({
                    isFilterSelectTable: [],
                    isFilterCompanyList: [],//預設公司別清單
                    isFilterCountryList: [],//預設國家清單
                    isFilterAreaList: [],//預設地域清單
                    isFilterGroupIdList: [],//預設站台編號清單
                    isFilterCommandList: [],//預設指令清單
                    isFilterResponseList: [],//預設回應訊息清單     
                    isFilterCompanyData: { ...initFilterSelectData },
                    isFilterCountryData: { ...initFilterSelectData },
                    isFilterAreaData: { ...initFilterSelectData },
                    isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterCommandData: { ...initFilterSelectData },
                    isFilterResponseData: { ...initFilterSelectData },
                    isFilterSendTimeData: { ...initFilterDate },
                });
                return new Promise((resolve, reject) => (reject('')));
            }
        }).then(() => {
            //處理已選擇項目的選單   
            if (defaultCompany.isDataList.length > 0) {
                let obj = {};
                defaultCompany.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, obj);
            }
            if (defaultCountry.isDataList.length > 0) {
                let obj = {};
                defaultCountry.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, obj);
            }
            if (defaultArea.isDataList.length > 0) {
                let obj = {};
                defaultArea.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, obj);
            }
            this.setState({
                isFilterCompanyData: { ...defaultCompany },
                isFilterCountryData: { ...defaultCountry },
                isFilterAreaData: { ...defaultArea },
                isFilterGroupIdData: { ...defaultGroupId },
            });
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
            case FilterNames_API.isFilterCompanyVirtue:
                List = [...Lists];
                break;
            case FilterNames_API.isFilterCountryVirtue:
                //過濾公司別    
                if (this.state.isFilterCompanyData.isDataList.length > 0) {
                    List = Lists.filter(item => (this.state.isFilterCompanyData.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List = [...Lists];
                }
                break;
            case FilterNames_API.isFilterAreaVirtue:
                let List1 = [];
                //過濾公司別   
                if (this.state.isFilterCompanyData.isDataList.length > 0) {
                    List1 = Lists.filter(item => (this.state.isFilterCompanyData.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List1 = [...Lists];
                }
                //過濾國家           
                if (this.state.isFilterCountryData.isDataList.length > 0) {
                    List = List1.filter(item => (this.state.isFilterCountryData.isDataList.findIndex((element) => element === item['CountryValue']) >= 0 ? true : false));
                } else {
                    List = [...List1];
                }
                break;
            default:
        }
        let CountryObj = {};
        let AreaObj = {};
        let GroupObj = {};
        List.forEach(item => {
            if (item.CountryValue !== "") { CountryObj[item.CountryValue] = { Value: item.CountryValue, Label: item.CountryLabel, selectShow: true }; }
            if (item.AreaValue !== "") { AreaObj[item.AreaValue] = { Value: item.AreaValue, Label: item.AreaLabel, selectShow: true }; }
            if (item.GroupValue !== "") { GroupObj[item.GroupValue] = { Value: item.GroupValue, Label: item.GroupLabel, selectShow: true }; }
        });
        const CountryList = Object.values(CountryObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        const AreaList = Object.values(AreaObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        const GroupList = Object.values(GroupObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        switch (Virtue) {
            case FilterNames_API.isFilterCompanyVirtue:
                this.setState({
                    isFilterCountryList: [...CountryList],
                    isFilterCountryData: { ...initFilterSelectData },
                    isFilterAreaList: [...AreaList],
                    isFilterAreaData: { ...initFilterSelectData },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: { ...initFilterSelectData },
                });
                break;
            case FilterNames_API.isFilterCountryVirtue:
                this.setState({
                    isFilterAreaList: [...AreaList],
                    isFilterAreaData: { ...initFilterSelectData },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: { ...initFilterSelectData },
                });
                break;
            case FilterNames_API.isFilterAreaVirtue:
                this.setState({
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: { ...initFilterSelectData },
                });
                break;
            default:
        }
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
                console.error('參數設定歷史 ajaxDelFilter', response);
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }

    //取得參數設定篩選清單API(GET):
    ajaxGetCommandFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getCommandFilter`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //取得參數設定篩選清單API(GET):
        //https://www.gtething.tw/battery/getCommandFilter
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
    isFilterCountryData: {},
    isFilterAreaData: {},
    isFilterGroupIdData: {},
    isFilterCommandData: {},
    isFilterResponseData: {},
    isFilterSendTimeData: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
}
Filter.propTypes = {
    functionId: PropTypes.string,
    isFilterCompanyData: PropTypes.object,
    isFilterCountryData: PropTypes.object,
    isFilterAreaData: PropTypes.object,
    isFilterGroupIdData: PropTypes.object,
    isFilterCommandData: PropTypes.object,
    isFilterResponseData: PropTypes.object,
    isFilterSendTimeData: PropTypes.object,
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


