import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { exportExcel } from '../../../utils/exportExcel';
import {
    updateCompany, updateCountry, updateArea, updateGroupId, updateGroupName,
    resetAll, resetCompany, resetCountry, resetArea, resetGroupId, resetGroupName
} from '../../../actions/GroupManageP1502Action';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { updateCompanyToBattFilter, updateGroupIdToBattFilter, } from '../../../actions/BattFilterAction';
import { FilterNames, initOpenData, initFilterSelectData, CompareWindowns } from './InitDataFormat';//初始化格式
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray } from '../../../components/CusSearchInput/utils';//Search Tool
import { ajaxSaveFilter } from '../../../components/FilterDrawer/utils';
import { ajaxGetGroupManage, ajaxAddBatteryGroup, ajaxUpdBatteryGroup, ajaxDelBatteryGroup } from './getApi';

import CusSearchInput from '../../../components/CusSearchInput'; //搜尋關鍵字
import FilterDrawer from '../../../components/FilterDrawer';   //右側篩選欄外框
import CusBookMark from '../../../components/CusBookMark'; //儲存標籤
import FilterItemTag from './FilterItemTag';   //篩選條件Button
import Filter from './Filter';//篩選內容
import Table from './Table';
import PageAlertDialog from './PageAlertDialog';//彈跳視窗
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏


class Page1502 extends Component {
    // 站台管理
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1502',//funcId
            pageId: '1502',//頁面Id
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
            tableHeader: [ // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                { id: '1064', sortName: 'Company', fixed: true, active: true },// 公司
                { id: '1028', sortName: 'Country', fixed: true, active: true },// 國家
                { id: '1029', sortName: 'Area', fixed: false, active: true },// 地域
                { id: '1013', sortName: 'GroupName', fixed: false, active: true },// 站台名稱
                { id: '1012', sortName: 'GroupID', fixed: false, active: true },// 站台編號
                { id: '1031', sortName: 'Address', fixed: false, active: true },// 地址
                { id: '1401', sortName: 'Count', fixed: false, active: true },// 電池組數
            ],
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
            //彈跳視窗
            openDialog: false,//開啟新增、編輯、刪除視窗
            openWindows: '',//目前開啟的視窗(1050:編輯、1051:新增、1052:刪除、1053:匯出、1054:匯入)
            openTitle: '',//視窗標頭
            openData: { ...initOpenData },//視窗資料
            //訊息視窗            
            openAlertMsg: false,//開啟訊息視窗
            titleAlertMsg: '',//訊息視窗-標頭
            textAlertMsg: '',//訊息視窗-文字欄位
            updResult: {},//更新資料回傳結果
            addResult: {},//新增資料回傳結果
        }

    }


    // React Lifecycle
    componentDidMount() {
        //設定權限
        const { functionList } = this.props;
        const { functionId } = this.state;
        if (this.props.treeFunctionId !== 1502) {
            this.props.setTreeFunctionId(1502);
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
            const { tableHeader } = this.state;
            let newList = tableHeader;
            newList[0] = { id: '1064', sortName: 'Company', fixed: true, active: false };
            this.setState({ tableHeader: newList });
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
        const { loading, pageId, searchInput, isFilterOpen, data, tableErrMsg, openDialog, openWindows, openTitle, openData, tableHeader, limits } = this.state;
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData } = this.props;//篩選      
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* filter */}
                    <div className="col-12 pt-4 pb-4 pl-0 pr-0">
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
                            submitFilterItemTag={this.submitFilterItemTag}
                        />
                    </div>
                    {false && <ToggleBtnBar list={tableHeader} onClickEvent={this.onToggleBtnChange} />}
                    {/* table */}
                    <div className="col-12 pb-4 pl-0 pr-0">
                        <Table
                            limits={limits} //設定權限，影響可看到的內容
                            perPage={perPage} //表單一次顯示幾個
                            tableHeader={tableHeader} //表單標頭
                            data={data} //顯示資料
                            active={this.state.tableActive}
                            addEvent={this.addEvent} //新增功能
                            deleteEvent={this.deleteEvent} //刪除功能
                            importExcel={this.onImportExcel} //匯入表單功能
                            exportExcel={this.onExportExcel} //匯出表單功能
                            tableErrMsg={tableErrMsg} //錯誤訊息
                            getEditData={this.getEditData} //編輯功能
                            onUpdateTable={this.onUpdateTable} //checkbox功能
                            onUpdActive={this.onUpdActive}
                            redirectURLBattLayer2={this.redirectURLBattLayer2}  // 跳轉至電池數據第二層(儲存站台名稱,號碼)
                        />
                    </div>

                    {/* 彈跳視窗 */}
                    {
                        limits.Edit === 1 && openDialog &&
                        <PageAlertDialog
                            windows={openWindows} //指定開啟的視窗
                            title={openTitle} //title                            
                            open={openDialog} //開啟視窗
                            data={openData} //視窗資料
                            handleClose={this.handleClose} //連動關閉視窗
                            handleEditSubmit={this.handleEditSubmit}//編輯送出
                            handleAddSubmit={this.handleAddSubmit}//新增送出
                            handleDelSubmit={this.handleDelSubmit}//刪除送出
                            handleImportSubmit={this.handleImportSubmit}//匯入送出
                            onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
                            updResult={this.state.updResult}
                            addResult={this.state.addResult}
                        />

                    }
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
        const { tableHeader } = this.state;
        const newList = setNewTableHeader(tableHeader, value);
        this.setState({
            tableHeader: newList,
        })
    }
    // 更新table資訊
    onUpdateTable = (data) => {
        this.setState({
            data: [...data]
        });
    }
    onUpdActive = (idx) => {
        this.setState({
            tableActive: idx
        })
    }
    // 取得編輯某一行資料
    getEditData = ({ data }) => {
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Edit.model,
            openTitle: CompareWindowns.Edit.title,
            openData: { ...data },
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }
    //新增功能
    addEvent = () => {
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Add.model,
            openTitle: CompareWindowns.Add.title,
            openData: { ...initOpenData }
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }

    //刪除功能
    deleteEvent = () => {
        const { data } = this.state;
        const GroupInternalID = [];
        data.forEach(item => {
            if (item.checked) {
                GroupInternalID.push(item.GroupInternalID);
            }
        });
        if (GroupInternalID.length === 0) { return; }
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Del.model,
            openTitle: CompareWindowns.Del.title,
            openData: { ...initOpenData }
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }
    //匯入Excel
    onImportExcel = () => {
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Import.model,
            openTitle: CompareWindowns.Import.title,
            openData: { ...initOpenData }
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }
    //匯出Excel
    onExportExcel = () => {
        this.setState({
            openAlertMsg: true,//開啟訊息視窗
            titleAlertMsg: '1089',
            textAlertMsg: <Trans i18nKey="1091" />,//填寫訊息
        });
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = this.returnPostData(this.props, "check");
        ajaxGetGroupManage({query,postData}).then((response) => {
            if (this.ajaxCancel || this.importExcelCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00') {
                const excelName = msg;
                const url = `getGroupManage`;
                const data = this.returnPostData(this.props, "csv");
                const { token, curLanguage, timeZone, company } = this.props;
                exportExcel(token, curLanguage, timeZone, company, url, excelName, data, () => {
                    this.setState({
                        openAlertMsg: true,//開啟訊息視窗
                        titleAlertMsg: '1089',
                        textAlertMsg: <Trans i18nKey="1090" />,//填寫訊息
                    });
                }, "POST",()=>{
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
    //  彈跳視窗 - 編輯

    handleClose = () => {
        this.setState({
            openDialog: false,
            openWindows: '',
            openTitle: '',
            openData: { ...initOpenData },
            updResult: {},
            addResult: {},
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    //新增送出
    handleAddSubmit = (data) => {
        const { Address, Area, Company, Country, GroupID, GroupName, } = data;
        const { username } = this.props;
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = {
            "Company": Company,
            "GroupName": GroupName,
            "GroupID": GroupID,
            "Country": Country,
            "Area": Area,
            "Address": Address,
            "UserName": username
        }
        ajaxAddBatteryGroup({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                    updResult: {},//更新資料回傳結果
                    addResult: {},//新增資料回傳結果
                });
                this.submitFilterData();
                this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
            } else {
                this.setState({
                    // openDialog: false,
                    openData: { ...initOpenData },
                    // openAlertMsg: true,//開啟訊息視窗
                    // titleAlertMsg: '1089',
                    // textAlertMsg: msg,//填寫訊息
                    updResult: {},//更新資料回傳結果
                    addResult: {code:response.code,msg:response.msg},//新增資料回傳結果
                });
            }
        });
    }
    //編輯送出
    handleEditSubmit = (data) => {
        const { Address, Area, GroupInternalID, Country, GroupID, GroupName, } = data;
        const { username } = this.props;
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = {
            "GroupInternalID": GroupInternalID,
            "GroupName": GroupName,
            "GroupID": GroupID,
            "Country": Country,
            "Area": Area,
            "Address": Address,
            "UserName": username
        }
        ajaxUpdBatteryGroup({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code !== '00') {
                this.setState({
                    // openDialog: false,
                    openData: { ...initOpenData },
                    // openAlertMsg: true,//開啟訊息視窗
                    // titleAlertMsg: '1089',
                    // textAlertMsg: msg,//填寫訊息
                    updResult: {code:response.code,msg:response.msg},//更新資料回傳結果
                    addResult: {},//新增資料回傳結果
                });
            }
            else if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                    updResult: {}, //更新資料回傳結果
                    addResult: {},//新增資料回傳結果
                });
                this.submitFilterData();
                this.props.onIsRefreshChange(true); //重新開始5min刷新頁面
            } 
            // else {
            //     this.setState({
            //         // openDialog: false,
            //         openData: { ...initOpenData },
            //         // openAlertMsg: true,//開啟訊息視窗
            //         // titleAlertMsg: '1089',
            //         // textAlertMsg: msg,//填寫訊息
            //         updResult: response,//更新資料回傳結果
            //     });
            // }
        });
        // this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    handleImportSubmit = (data) => {
        const { username, token, curLanguage, timeZone, company } = this.props;
        const { File, Company } = data;
        var formData = new FormData();
        formData.append('username', username);
        formData.append('company', Company);
        formData.append('files', File);
        var xhr = new XMLHttpRequest();  // 先new 一個XMLHttpRequest。
        xhr.open('POST', `importBatteryGroup`);   // 設置xhr得請求方式和url。
        xhr.setRequestHeader('token', token);
        xhr.setRequestHeader('language', curLanguage);
        xhr.setRequestHeader('timezone', timeZone);
        xhr.setRequestHeader('company', company);
        xhr.onreadystatechange = (event) => {   // 等待ajax請求完成。
            if (this.ajaxCancel) { return; }//強制結束頁面	
            if (xhr.status === 200) {
                if (xhr.responseText === "") { return; }
                // console.log('File upload => Server Status:' + xhr.status + ' 伺服器已接收');
                const { msg, code } = JSON.parse(xhr.responseText);
                if (code === '00') {
                    // console.log('File upload => Server Response Code:' + code + ' 上傳成功');
                    this.setState({
                        openDialog: false,
                        openData: { ...initOpenData },
                        openAlertMsg: true,
                        titleAlertMsg: '1089',
                        textAlertMsg: msg
                    })
                    this.submitFilterData();
                } else {
                    console.error('File upload => Server Response Code:' + code + ' 上傳失敗');
                    this.setState({
                        openDialog: false,
                        openData: { ...initOpenData },
                        openAlertMsg: true,
                        titleAlertMsg: '1089',
                        textAlertMsg: msg
                    })
                }
            } else {
                console.error('File upload => Server Status:' + xhr.status + ' 伺服器發生錯誤');
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,
                    titleAlertMsg: '1089',
                    textAlertMsg: 'File upload => Server Status:' + xhr.status
                })
            }
        };
        // 獲取上傳進度
        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) {
                // var percent = Math.floor(event.loaded / event.total * 100);
                // document.querySelector("#progress .progress-item").style.width = percent + "%";
                // 設置進度顯示
                // this.setState({
                //     openAlertMsg: true,
                //     textAlertMsg: percent + "%"
                // })
            }
        };
        xhr.send(formData);

    }
    //刪除送出
    handleDelSubmit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { data } = this.state;
        const GroupInternalID = [];
        data.forEach(item => {
            if (item.checked) {
                GroupInternalID.push(item.GroupInternalID);
            }
        });
        if (GroupInternalID.length === 0) { return; }
        const postData = {
            "GroupInternalID": [...GroupInternalID],
            "UserName": username
        }
        ajaxDelBatteryGroup({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                    updResult: {},//更新資料回傳結果
                });
                this.submitFilterData();
            } else {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                    updResult: {},//更新資料回傳結果
                });
            }
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }


    //關鍵字搜尋
    onClickEvent = (value) => {
        const { ajaxData, tableHeader } = this.state;
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
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData} = FilterNames;
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
        }

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
            default:
        }

        this.submitFilterData(postData);
    }

    //打開右邊篩選
    setOpenIsFilter = (value) => {
        this.setState({isFilterOpen: value})
    }
    updateFilterData = (name, object) => {//更新右邊篩選值內容
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,} = FilterNames;
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
            default:
        }
    }
    saveFilterData = (FilterName) => {//右邊篩選全部儲存
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = {
            "Account": this.props.account,
            "FunctionId": this.state.pageId,
            "FilterName": FilterName,
            "FilterConfig": {
                "Company": { ...this.props.isFilterCompanyData },
                "Country": { ...this.props.isFilterCountryData },
                "Area": { ...this.props.isFilterAreaData },
                "GroupID": { ...this.props.isFilterGroupIdData },
            }
        }
        ajaxSaveFilter({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                    updResult: {},//更新資料回傳結果
                });
            } else {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                    updResult: {},//更新資料回傳結果
                });
            }
        });
    }
    resetFilterData = () => {//右邊篩選全部初始化        
        this.props.resetAll();
        let postData = {
            isFilterCompanyData: initFilterSelectData,
            isFilterCountryData: initFilterSelectData,
            isFilterAreaData: initFilterSelectData,
            isFilterGroupIdData: initFilterSelectData,
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
            openDialog: false,//開啟編輯視窗
            openTitle: '',//視窗標頭
            openData: { ...initOpenData },//視窗資料
            updResult: {},
            addResult: {},
        });
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 站台管理API(POST):
        ajaxGetGroupManage({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Group } = msg;
                const newData = Group.map((item) => {
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
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData} = state;
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

    // 跳轉至電池數據(第二層)// 取得(站台編號,站台名稱)
    redirectURLBattLayer2 = (data) => {
        const Company = {
            isOpen: true,
            isChecked: false,
            isDataList: [data.companyCode],
            isButtonList: [{ Value: data.companyCode, Label: data.company }],
        }
        const GroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [data.groupId],
            isButtonList: [{ Value: data.groupId, Label: data.groupLabel }],
        }
        this.props.updateCompanyToBattFilter(Company);
        this.props.updateGroupIdToBattFilter(GroupId);
    }


    
}

Page1502.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1502.propTypes = {
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
        isFilterCompanyData: state.GroupManageP1502Reducer.isFilterCompanyData,
        isFilterCountryData: state.GroupManageP1502Reducer.isFilterCountryData,
        isFilterAreaData: state.GroupManageP1502Reducer.isFilterAreaData,
        isFilterGroupIdData: state.GroupManageP1502Reducer.isFilterGroupIdData,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateCountry: (object) => dispatch(updateCountry(object)),
        updateArea: (object) => dispatch(updateArea(object)),
        updateGroupId: (object) => dispatch(updateGroupId(object)),
        updateGroupName: (object) => dispatch(updateGroupName(object)),
        resetCompany: () => dispatch(resetCompany()),
        resetCountry: () => dispatch(resetCountry()),
        resetArea: () => dispatch(resetArea()),
        resetGroupId: () => dispatch(resetGroupId()),
        resetGroupName: () => dispatch(resetGroupName()),
        resetAll: () => dispatch(resetAll()),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
        updateCompanyToBattFilter: (object) => dispatch(updateCompanyToBattFilter(object)),
        updateGroupIdToBattFilter: (object) => dispatch(updateGroupIdToBattFilter(object)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1502);