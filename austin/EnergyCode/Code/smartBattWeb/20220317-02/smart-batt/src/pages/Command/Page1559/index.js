import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { apipath, ajax } from '../../../utils/ajax';
import { exportExcel } from '../../../utils/exportExcel';
import {
    setTableHeader,
    updateCompany, updateCountry, updateArea, updateGroupId, updateGroupName,
    updateBatteryGroupId, updateBatteryType, updateCommand, updateResponse, updateSendTime,
    resetAll, resetCompany, resetCountry, resetArea, resetGroupId, resetGroupName,
    resetBatteryGroupId, resetBatteryType, resetCommand, resetResponse, resetSendTime,
} from '../../../actions/CommandP1559Action';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { Trans } from 'react-i18next';
import CusSearchInput from '../../../components/CusSearchInput'; //搜尋關鍵字
import FilterDrawer from '../../../components/FilterDrawer';   //右側篩選欄外框
import CusBookMark from '../../../components/CusBookMark'; //儲存標籤
import { FilterNames, initFilterSelectData, initFilterDate } from './InitDataFormat';//初始化格式
import FilterItemTag from './FilterItemTag';   //篩選條件Button
import Filter from './Filter';//篩選內容
import Table from './Table';
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray } from '../../../components/CusSearchInput/utils';//Search Tool


const apiUrl = apipath();
class Page1559 extends Component {
    // 參數設定歷史
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1504',//funcId
            pageId: '1559',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取     
            searchInput: '',//搜尋欄位
            //儲存標籤            
            isFilterSaveInput: '',
            //篩選視窗
            isFilterOpen: false,//開啟篩選視窗

            ajaxData: [],//ajaxTableData(原始資料)
            data: [],//tableData(顯示資料)
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
            //訊息視窗            
            openAlertMsg: false,//開啟訊息視窗
            titleAlertMsg: '',//訊息視窗-標頭
            textAlertMsg: '',//訊息視窗-文字欄位
        }

    }


    // React Lifecycle
    componentDidMount() {
        //設定權限
        const { functionList } = this.props;
        const { functionId } = this.state;
        if (this.props.treeFunctionId !== 1504) {
            this.props.setTreeFunctionId(1504);
        }
        const newFList = functionList.filter(item => {
            return item.FunctionId === Number(functionId)
        });
        if (newFList.length === 1) {
            this.setState({
                limits: {
                    Edit: newFList[0].Edit,
                    Button: { ...newFList[0].Button },
                }
            });
        }
        const { company } = this.props;
        //當非管理員權限，則table不顯示公司欄位
        if (company !== "1") {
            const { tableHeader } = this.props;
            let newList = tableHeader;
            newList[0] = { id: '1064', sortName: 'Company', fixed: true, active: false };
            this.props.setTableHeader(newList);
        }else{
            const { tableHeader } = this.props;
            let newList = tableHeader;
            newList[0] = { id: '1064', sortName: 'Company', fixed: false, active: false };
            this.props.setTableHeader(newList);
        }
        this.setState({
            loading: true,
        });
        this.submitFilterData();
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.curLanguage !== undefined && this.props.curLanguage !== prevProps.curLanguage) {
            this.submitFilterData();
        }
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.importExcelCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {             
        const { perPage } = this.props;
        const { loading, pageId, searchInput, isFilterOpen, data, tableErrMsg, limits } = this.state;
        const { tableHeader, isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData, isFilterSendTimeData,
        } = this.props;//篩選      
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* filter */}
                    <div className="col-12 pt-4 pl-0 pr-0">
                        <div className="d-inline-block mr-2 my-2">
                            {/* 關鍵字搜索 */}
                            <CusSearchInput
                                placeholderName='1037'
                                value={searchInput}
                                onClickEvent={(value) => this.onClickEvent(value)}
                            />
                        </div>
                        {/* 篩選 */}
                        <FilterDrawer
                            isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                            setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                            resetEvent={this.resetFilterData}    //清空所有欄位
                        >
                            <Filter
                                functionId={pageId}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterCountryData={isFilterCountryData}
                                isFilterAreaData={isFilterAreaData}
                                isFilterGroupIdData={isFilterGroupIdData}
                                isFilterCommandData={isFilterCommandData}
                                isFilterResponseData={isFilterResponseData}
                                isFilterSendTimeData={isFilterSendTimeData}
                                updateFilterData={this.updateFilterData}
                                submitFilterData={this.submitFilterData}
                                alertFilterMsgOpen={this.alertFilterMsgOpen}
                            />
                        </FilterDrawer>
                        {/* 標籤儲存 */}
                        <CusBookMark
                            value={this.state.isFilterSaveInput}
                            placeholderName={'1087'}
                            onClickEvent={this.saveFilterData}
                        />
                        {/* 重置 */}
                        <div className="d-inline-block">
                            <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={this.resetFilterData}>
                                <Trans i18nKey="1038" />
                            </button>
                        </div>
                        <FilterItemTag
                            isFilterCompanyData={isFilterCompanyData}
                            isFilterCountryData={isFilterCountryData}
                            isFilterAreaData={isFilterAreaData}
                            isFilterGroupIdData={isFilterGroupIdData}
                            isFilterCommandData={isFilterCommandData}
                            isFilterResponseData={isFilterResponseData}
                            isFilterSendTimeData={isFilterSendTimeData}
                            submitFilterItemTag={this.submitFilterItemTag}
                        />
                    </div>
                    <ToggleBtnBar company={this.props.company} list={tableHeader} onClickEvent={this.onToggleBtnChange} />
                    {/* table */}
                    <div className="col-12 pb-4 pl-0 pr-0">
                        <Table
                            limits={limits} //設定權限，影響可看到的內容
                            perPage={perPage} //表單一次顯示幾個
                            tableHeader={tableHeader} //表單標頭
                            data={data} //顯示資料
                            active={this.state.tableActive}
                            exportExcel={this.onExportExcel} //匯出表單功能
                            refreshEvent={this.refreshEvent} //重新整理功能
                            tableErrMsg={tableErrMsg} //錯誤訊息
                            onUpdActive={this.onUpdActive}
                        />
                    </div>

                    {/* 彈跳視窗 */}
                    <AlertMsg
                        msgTitle={this.state.titleAlertMsg}//Title
                        open={this.state.openAlertMsg} //開啟視窗
                        handleClose={this.handleAlertMsgClose} //連動關閉視窗
                        onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
                        isDisabledBtn={false}
                    >
                        {/* 視窗資料 */}
                        <div className="col-12 p-0 my-4">
                            <div className="my-1">{this.state.textAlertMsg}</div>
                        </div>
                    </AlertMsg>
                </Fragment>
            )
        }
    }
    // 變更表格顯示隱藏欄位
    onToggleBtnChange = (value) => {
        const { tableHeader } = this.props;
        const newList = setNewTableHeader(tableHeader, value);
        this.props.setTableHeader(newList);
    }
    //匯出Excel
    onExportExcel = () => {
        this.setState({
            openAlertMsg: true,//開啟訊息視窗
            titleAlertMsg: '1089',
            textAlertMsg: <Trans i18nKey="1091" />,//填寫訊息
        });
        const postData = this.returnPostData(this.props, "check");
        this.ajaxGetCommandHistory(postData).then((response) => {
            if (this.ajaxCancel || this.importExcelCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00') {
                const excelName = msg;
                const url = `${apiUrl}getCommandHistory`;
                const data = this.returnPostData(this.props, "csv");
                const { token, curLanguage, timeZone, company } = this.props;
                exportExcel(token, curLanguage, timeZone, company, url, excelName, data, () => {
                    this.setState({
                        openAlertMsg: true,//開啟訊息視窗
                        titleAlertMsg: '1089',
                        textAlertMsg: <Trans i18nKey="1090" />,//填寫訊息
                    });
                }, 'POST',()=>{
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "5003",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                });

            } else {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }

    //重新整理
    refreshEvent = () => {
        this.submitFilterData();
    }
    onUpdActive = (idx) => {
        this.setState({tableActive: idx})
    }
    //關鍵字搜尋
    onClickEvent = (value) => {
        const { ajaxData } = this.state;
        const { tableHeader } = this.props;
        const allowArray = [];
        tableHeader.forEach(item => {
            if (!item.active) { return; }
            allowArray.push(item.sortName);
        });
        const key = {
            dataArray: ajaxData,
            searchText: value,
            allowArray: [...allowArray],
        };
        const newTableData = filterArray(key);
        this.setState({
            data: newTableData,
            tableActive: 1
        })
    }
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData, isFilterSendTimeData,
        } = FilterNames;
        if (name === isFilterSendTimeData && isFilterSendTimeData.Radio === "0") { return; }
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
            isFilterCommandData: this.props.isFilterCommandData,
            isFilterResponseData: this.props.isFilterResponseData,
            isFilterSendTimeData: this.props.isFilterSendTimeData,
        }
        if (name === isFilterSendTimeData) {
            this.props.resetSendTime();//設定時間初始化
            postData.isFilterSendTimeData = initFilterDate;
        } else {
            const { isOpen, isDataList, isButtonList } = this.props[name];
            const newIsDataList = isDataList.filter(ite => ite !== item.Value);
            const newIsButtonList = isButtonList.filter(ite => ite !== item);
            const object = {
                isOpen: isOpen,
                isChecked: newIsDataList.length === 0 ? true : false,
                isDataList: [...newIsDataList],
                isButtonList: [...newIsButtonList],
            }
            postData[name] = object;
            switch (name) {
                case isFilterCompanyData:
                    this.props.updateCompany(object);
                    break;
                case isFilterCountryData:
                    this.props.updateCountry(object);
                    break;
                case isFilterAreaData:
                    this.props.updateArea(object);
                    break;
                case isFilterGroupIdData:
                    this.props.updateGroupId(object);
                    break;
                case isFilterCommandData:
                    this.props.updateCommand(object);
                    break;
                case isFilterResponseData:
                    this.props.updateResponse(object);
                    break;
                case isFilterSendTimeData:
                    this.props.updateSendTime(object);
                    break;
                default:
            }
        }
        this.submitFilterData(postData);
    }

    //打開右邊篩選
    setOpenIsFilter = (value) => {
        this.setState({
            isFilterOpen: value
        })
    }
    updateFilterData = (name, object) => {//更新右邊篩選值內容
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData, isFilterSendTimeData,
        } = FilterNames;
        switch (name) {
            case isFilterCompanyData:
                this.props.updateCompany(object);
                break;
            case isFilterCountryData:
                this.props.updateCountry(object);
                break;
            case isFilterAreaData:
                this.props.updateArea(object);
                break;
            case isFilterGroupIdData:
                this.props.updateGroupId(object);
                break;
            case isFilterCommandData:
                this.props.updateCommand(object);
                break;
            case isFilterResponseData:
                this.props.updateResponse(object);
                break;
            case isFilterSendTimeData:
                this.props.updateSendTime(object);
                break;
            default:
        }
    }
    saveFilterData = (FilterName) => {//右邊篩選全部儲存
        const postData = {
            "Account": this.props.account,
            "FunctionId": this.state.pageId,
            "FilterName": FilterName,
            "FilterConfig": {
                "Company": { ...this.props.isFilterCompanyData },
                "Country": { ...this.props.isFilterCountryData },
                "Area": { ...this.props.isFilterAreaData },
                "GroupID": { ...this.props.isFilterGroupIdData },
                "Command": { ...this.props.isFilterCommandData },
                "Response": { ...this.props.isFilterResponseData },
                "SendTime": { ...this.props.isFilterSendTimeData },
            }
        }
        this.ajaxSaveFilter(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { msg } = response;
            this.setState({
                openAlertMsg: true,//開啟訊息視窗
                titleAlertMsg: '1089',
                textAlertMsg: msg,//填寫訊息
            });
            // const { code, msg } = response;
            // if (code === '00' && msg) {
            //     this.setState({
            //         openAlertMsg: true,//開啟訊息視窗
            //         titleAlertMsg: '1089',
            //         textAlertMsg: msg,//填寫訊息
            //     });
            // } else {
            //     this.setState({
            //         openAlertMsg: true,//開啟訊息視窗
            //         titleAlertMsg: '1089',
            //         textAlertMsg: msg,//填寫訊息
            //     });
            // }
        });
    }
    resetFilterData = () => {//右邊篩選全部初始化        
        this.props.resetAll();
        let postData = {
            isFilterCompanyData: initFilterSelectData,
            isFilterCountryData: initFilterSelectData,
            isFilterAreaData: initFilterSelectData,
            isFilterGroupIdData: initFilterSelectData,
            isFilterCommandData: initFilterSelectData,
            isFilterResponseData: initFilterSelectData,
            isFilterSendTimeData: initFilterDate,
        }
        this.submitFilterData(postData);
    }

    // get Data
    submitFilterData = (props) => {
        // reset
        this.setState({
            loading: true,
            ajaxData: [],
            data: [],
            tableErrMsg: '',
        });
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 站台管理API(POST):
        this.ajaxGetCommandHistory(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Command } = msg;
                const newData = Command.map((item) => {
                    return { ...item, checked: false, slider: false }
                });
                this.setState({
                    loading: false,
                    ajaxData: [...newData],
                    data: [...newData],
                    tableErrMsg: '',
                    isFilterOpen: false,
                    tableActive: 1
                })
            } else {
                this.setState({ loading: false, ajaxData: [], data: [], tableErrMsg: msg, isFilterOpen: false, tableActive: 1 })
            }
        });
    }
    returnPostData = (state, type) => {
        const {
            isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterCommandData, isFilterResponseData, isFilterSendTimeData,
        } = state;
        return {
            "Company": {
                "All": isFilterCompanyData.isChecked ? "1" : "0",
                "List": [...isFilterCompanyData.isDataList]
            },
            "Country": {
                "All": isFilterCountryData.isChecked ? "1" : "0",
                "List": [...isFilterCountryData.isDataList]
            },
            "Area": {
                "All": isFilterAreaData.isChecked ? "1" : "0",
                "List": [...isFilterAreaData.isDataList]
            },
            "GroupID": {
                "All": isFilterGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterGroupIdData.isDataList]
            },
            "Command": {
                "All": isFilterCommandData.isChecked ? "1" : "0",
                "List": [...isFilterCommandData.isDataList]
            },
            "Response": {
                "All": isFilterResponseData.isChecked ? "1" : "0",
                "List": [...isFilterResponseData.isDataList]
            },
            "SendTime": { ...isFilterSendTimeData },
            "Type": type

        }
    }

    //刪除成功後顯示msg視窗
    alertFilterMsgOpen = (title, msg) => {
        this.setState({
            isFilterOpen: false,
            openAlertMsg: true,
            titleAlertMsg: title,
            textAlertMsg: msg,//填寫訊息
        });
        this.props.onIsRefreshChange(false);    //重新開始5min刷新頁面       
    }

    //開關msg視窗
    handleAlertMsgClose = () => {
        this.setState({
            openAlertMsg: false,
            titleAlertMsg: '',
            textAlertMsg: '',//填寫訊息
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面       
    }


    //參數設定歷史(POST):(含EXCEL)
    ajaxGetCommandHistory = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getCommandHistory`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //參數設定歷史(POST):
        //https://www.gtething.tw/battery/getCommandHistory
    }

    //儲存篩選API(POST):
    ajaxSaveFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}saveFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //儲存篩選API(POST):
        //https://www.gtething.tw/battery/saveFilter
    }
}

Page1559.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1559.propTypes = {
    onIsRefreshChange: PropTypes.func,
    perPage: PropTypes.number,
}

const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
        perPage: state.LoginReducer.perPage,
        functionList: state.LoginReducer.functionList,
        tableHeader: state.CommandP1559Reducer.tableHeader,
        isFilterCompanyData: state.CommandP1559Reducer.isFilterCompanyData,
        isFilterCountryData: state.CommandP1559Reducer.isFilterCountryData,
        isFilterAreaData: state.CommandP1559Reducer.isFilterAreaData,
        isFilterGroupIdData: state.CommandP1559Reducer.isFilterGroupIdData,
        isFilterGroupNameData: state.CommandP1559Reducer.isFilterGroupNameData,
        isFilterBatteryGroupIdData: state.CommandP1559Reducer.isFilterBatteryGroupIdData,
        isFilterBatteryTypeData: state.CommandP1559Reducer.isFilterBatteryTypeData,
        isFilterCommandData: state.CommandP1559Reducer.isFilterCommandData,
        isFilterResponseData: state.CommandP1559Reducer.isFilterResponseData,
        isFilterSendTimeData: state.CommandP1559Reducer.isFilterSendTimeData,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setTableHeader: (list) => dispatch(setTableHeader(list)),
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateCountry: (object) => dispatch(updateCountry(object)),
        updateArea: (object) => dispatch(updateArea(object)),
        updateGroupId: (object) => dispatch(updateGroupId(object)),
        updateGroupName: (object) => dispatch(updateGroupName(object)),
        updateBatteryGroupId: (object) => dispatch(updateBatteryGroupId(object)),
        updateBatteryType: (object) => dispatch(updateBatteryType(object)),
        updateCommand: (object) => dispatch(updateCommand(object)),
        updateResponse: (object) => dispatch(updateResponse(object)),
        updateSendTime: (object) => dispatch(updateSendTime(object)),
        resetCompany: () => dispatch(resetCompany()),
        resetCountry: () => dispatch(resetCountry()),
        resetArea: () => dispatch(resetArea()),
        resetGroupId: () => dispatch(resetGroupId()),
        resetGroupName: () => dispatch(resetGroupName()),
        resetBatteryGroupId: () => dispatch(resetBatteryGroupId()),
        resetBatteryType: () => dispatch(resetBatteryType()),
        resetCommand: () => dispatch(resetCommand()),
        resetResponse: () => dispatch(resetResponse()),
        resetSendTime: () => dispatch(resetSendTime()),
        resetAll: () => dispatch(resetAll()),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1559);