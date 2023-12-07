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
import { FilterNames, initFilterSelectData } from './InitDataFormat';//初始化格式

const apiUrl = apipath();
class Filter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            isFilterCompanyList: [],//預設公司別清單
            isFilterBatteryTypeNameList: [],//預設電池型號中文清單
            isFilterCompanyData: {//預設公司別
                ...initFilterSelectData
            },
            isFilterBatteryTypeNameData: {//電池型號中文
                ...initFilterSelectData
            },
            isFilterTotal: 0,//總數
        }
    }
    componentDidMount() {
        const { isFilterCompanyData, isFilterBatteryTypeNameData } = this.props;
        this.getBattTypeFilter(isFilterCompanyData, isFilterBatteryTypeNameData,);//取選單資料
        this.getFilter();//篩選儲存清單API(GET)
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterCompanyData.isDataList.length !== this.state.isFilterCompanyData.isDataList.length ||
            prevState.isFilterBatteryTypeNameData.isDataList.length !== this.state.isFilterBatteryTypeNameData.isDataList.length
        ) {
            const list = [
                this.state.isFilterCompanyData.isDataList.length,
                this.state.isFilterBatteryTypeNameData.isDataList.length,
            ];
            let total = 0;
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
        if (prevState.isFilterBatteryTypeNameData.isChecked !== this.state.isFilterBatteryTypeNameData.isChecked) {
            this.setState({
                isFilterBatteryTypeNameData: this.state.isFilterBatteryTypeNameData
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
        const { isFilterBatteryTypeNameList, isFilterBatteryTypeNameData } = this.state;//電池型號中文
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
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                            >
                                <FilterItemSelect
                                    defSelectVal={isFilterCompanyData.isDataList}
                                    defSelectList={isFilterCompanyList}
                                    onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCompanyList', list)}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                                />
                            </FilterItemHeader>
                        </li>
                    }
                    <li>{/* 電池型號中文 */}
                        <FilterItemHeader
                            title={"1506"}
                            total={isFilterBatteryTypeNameData.isDataList.length}
                            detailList={isFilterBatteryTypeNameData.isButtonList}
                            isChecked={isFilterBatteryTypeNameData.isChecked}
                            isOpen={isFilterBatteryTypeNameData.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterBatteryTypeNameList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryTypeNameData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterBatteryTypeNameData.isDataList}
                                defSelectList={isFilterBatteryTypeNameList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterBatteryTypeNameList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryTypeNameData, object)}
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
            BatteryTypeName: isFilterBatteryTypeNameData,
        } = FilterConfig;
        const postData = {
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterBatteryTypeNameData: checkObject(isFilterBatteryTypeNameData),
        };
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterBatteryTypeNameData, checkObject(isFilterBatteryTypeNameData));
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
        const { isFilterCompanyData, isFilterBatteryTypeNameData, ...other } = this.state;
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterBatteryTypeNameData, checkObject(isFilterBatteryTypeNameData));
        this.props.submitFilterData(this.state);
        this.setState({
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterBatteryTypeNameData: checkObject(isFilterBatteryTypeNameData),
            ...other
        });
    }

    //電池組管理篩選清單API:
    getBattTypeFilter = (defaultCompany, defaultBatteryTypeName) => {
        this.ajaxGetBattTypeFilter().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Company, BattTypeList } = msg;
                const Companys = Array.isArray(Company) ? Company.map(item => ({ ...item, selectShow: true })) : [];
                const BattTypeLists = Array.isArray(BattTypeList) ? BattTypeList.map(item => ({ ...item, selectShow: true })) : [];
                this.setState({
                    isFilterCompanyList: [...Companys],
                    isFilterCompanyData: {
                        ...defaultCompany
                    },
                    isFilterBatteryTypeNameList: [...BattTypeLists],
                    isFilterBatteryTypeNameData: {
                        ...defaultBatteryTypeName
                    },
                });
            } else {
                this.setState({
                    isFilterCompanyList: [],
                    isFilterBatteryTypeNameList: [],
                    isFilterCompanyData: { ...initFilterSelectData },
                    isFilterBatteryTypeNameData: { ...initFilterSelectData },
                });
            }
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
                console.error('電池型號管理 ajaxDelFilter', response);
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }

    //電池型號管理篩選清單API:
    ajaxGetBattTypeFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getBattTypeFilter`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //電池型號管理篩選清單API:
        //https://www.gtething.tw/battery/getBattTypeFilter
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
    isFilterBatteryTypeNameData: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
}
Filter.propTypes = {
    functionId: PropTypes.string,
    isFilterCompanyData: PropTypes.object,
    isFilterBatteryTypeNameData: PropTypes.object,
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


