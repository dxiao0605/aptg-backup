import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import { setGroupID,setBattInternalIDAlert,updBattGroupTheader } from '../../actions/BattDataAction';
import {
    updateCompany, updateCountry, updateArea, updateGroupId, updateGroupName,
    updateBatteryStatus,
    resetAllBattData, resetCompany, resetCountry, resetArea, resetGroupId,
    resetBatteryStatus, resetBatteryGroupId, 
} from '../../actions/BattFilterAction';
import { resetAllAlertUnsolved,updateGroupIdUnSolved,updateGroupNameUnSolved } from '../../actions/AlertUnsolvedFilterAction';
import { setActiveNum } from '../../actions/CommandAction';
import { setSubmitKeyList } from '../../actions/CommandP1504Action';
import { apipath, ajax } from '../../utils/ajax';
import { exportExcel } from '../../utils/exportExcel';
import {
    setNewTableHeader,      //變更表格顯示隱藏欄位
    setNewCheckboxList,     //變更各別checkbox狀態
    setSelectedList,        //變更已選則清單(selectedList)
    setSelectAllData,       //變更checkbox全選
} from '../../components/CusTable/utils';
import { Translation,Trans } from 'react-i18next';                              // i18n
import CusSearchInput from '../../components/CusSearchInput';                   // 關鍵字搜查
import FilterDrawer from '../../components/FilterDrawer';                       // 右側篩選欄外框
import BattFilter from '../../components/BattFilter';                           // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                         // 儲存標籤
import { FilterNames, initFilterSelectData } from '../../components/BattFilter/InitDataFormat';//初始化格式
import FilterItemTag from '../../components/BattFilter/FilterItemTag';          // 篩選條件Button
import PageHeader from '../../components/PageHeader'; 							// PageHeader
import ToggleBtnBar from '../../components/CusTable/ToggleBtnBar'; 				// 表格欄位顯示隱藏
import BattGroupDataTable from './BattGroupDataTable';
import AlertMsg from '../../components/AlertMsg'; 						        // 顯示訊息視窗
import { filterArray } from '../../components/CusSearchInput/utils'; 			// 關鍵字搜尋整理資料



const apiUrl = apipath();
class BattGroup extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loading: true,
            isRefresh: true, 							// 是否執行每五分鐘刷新
            isDisabledBtn: false, 						// 防止重複點擊
            pageId: '1400_1',                           // 儲存篩選時使用funciton_id
			dialogId: '', 								// 選則顯示彈跳視窗(BA,BB...)
			keySearch: '', 								// 關鍵字搜尋
            tableHeader: this.props.battGroupTheader,   // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
            resultData: [],                             // 關鍵字搜尋後資料
            data: [],                                   // api傳回預設資料(無checkbox狀態)
            currentPage:1,                              // 目前表格顯示第幾頁(預設第一頁)
            IMPType: 20,                                // 判斷顯示IMPType名稱
            TaskId: [],                                 // 跳轉至電池管理/電池參數設定
            selectAll: false,                           // 電池數據表格全選
            selectedList: [],                           // 己選取電池數據項目清單
            battGroupData: [],                          // 電池數據表格內容(含checkbox狀態)
            battGroupDataErrorMsg: 'Loading...',
            openMsg: false,                             // 顯示隱藏訊息視窗
            msgTitle: '',                               // 訊息視窗標題
            message: '',                                // 成功訊息/錯誤訊息
            isFilterOpen: false,                        // 篩選           
            isFilterSaveInput: '',                      // 儲存標籤 
        }
    }
    intervalID = 0;


    // React Lifecycle
    async componentDidMount() {
        const {isRefresh} = this.state;
        await this.props.setTreeFunctionId('1400');//設定功能選單指項位置
        await this.props.resetAllBattData();//預設清空篩選欄位
        await this.submitFilterData(); // 預設值
        if(isRefresh){  // 每五分鐘刷新頁面
            clearInterval(this.intervalID);
            this.intervalID = setInterval(() => {
                if(!this.state.isFilterOpen && !this.state.openMsg){
                    this.submitFilterData();
                }
            },300000); // 5min: 5*60*1000 = 300000
        }else{
            clearInterval(this.intervalID);
        }
    }


    componentDidUpdate(prevProps, prevState) {
        let mounted = true;
        // 當curLanguage更新時做
        if (this.props.curLanguage !== prevProps.curLanguage) {
            if (mounted) {this.submitFilterData();}
        }
        return () => mounted = false;
    }


    componentWillUnmount() {
        this.resetData(); // reset
        this.setState({TaskId: []})
        clearInterval(this.intervalID);
    }





    render() {
        const { company } = this.props;
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterBatteryStatusData
        } = this.props;//篩選
        const {
            loading,isDisabledBtn,
            dialogId,                   //顯示(批次)彈跳視窗(BA,BB,...)
            openMsg,                    //顯示訊息視窗
            msgTitle,                   //訊息視窗標題
            tableHeader,
            currentPage,                //目前表格顯示第幾頁(預設第一頁)
            IMPType,
            TaskId,                    //跳轉至電池管理/電池參數設定
            selectAll,selectedList,
            resultData,battGroupDataErrorMsg,
            message,
            // 篩選
            isFilterOpen,               //開啟篩選視窗
        } = this.state;

        if (loading) { return ''} // return <CusLoader />
        else {
            return (
                <Fragment>
                    {/* 電池數據 */}
                    <PageHeader
						title={<Translation>{(t) => <>{t("1400")}</>}</Translation>}
					/>

                    <div className="container-fuild">
                        <div className="col-12 p-0 mt-4">
                            <div className="d-inline-block mr-2">
                                {/* 關鍵字搜索 */}
                                <CusSearchInput placeholderName='1037' onClickEvent={(value)=>{this.getKeywordData(value)}} />
                            </div>
                            {/* 篩選 */}
                            <FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                                resetEvent={this.resetFilterData}    //清空所有欄位
                            >
                                <BattFilter
                                    functionId='1400_1' //判斷篩選清單顯示項目(電池數據1層 1400_1)
                                    isFilterCompanyData={isFilterCompanyData}
                                    isFilterCountryData={isFilterCountryData}
                                    isFilterAreaData={isFilterAreaData}
                                    isFilterGroupIdData={isFilterGroupIdData}
                                    isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                    isFilterBatteryStatusData={isFilterBatteryStatusData}
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
                                functionId='1400_1'
                                company={this.props.company}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterCountryData={isFilterCountryData}
                                isFilterAreaData={isFilterAreaData}
                                isFilterGroupIdData={isFilterGroupIdData}
                                isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                isFilterBatteryStatusData={isFilterBatteryStatusData}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
                        </div>
                        
                        <ToggleBtnBar company={company} list={tableHeader} onClickEvent={this.onToggleBtnChange} IMPType={IMPType} />

                        {/* 電池數據表格(第一層)  */}
                        {
                            !resultData || resultData.length <= 0
                            ? (<div className="text-center">{battGroupDataErrorMsg}</div>)
                            : (
                                <BattGroupDataTable
                                    isDisabledBtn={isDisabledBtn}                       // 解決重複點擊問題
                                    buttonControlList={this.props.buttonControlList}    // 電池數據按鈕權限清單
                                    selectAll={selectAll}                               // 電池數據表格全選
                                    tableHeader={tableHeader}                           // 表格欄位名稱
                                    data={resultData}                                   // 電池數據表格
                                    active={currentPage}                                // 目前表格顯示第幾頁(預設第一頁)
                                    IMPType={IMPType}                                   // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
                                    selectedList={selectedList}                         // 已選取電池數據項目清單
                                    dialogId={dialogId}                                 // 顯示彈跳視窗(BA,BB,'',...)
                                    getActive={this.getTableActive}                     // 取得目前表格為第幾頁
                                    setOpenDialog={this.setOpenDialog}                  // 變更顯示打開視窗(BB、BA...)
                                    onIsRefreshChange={this.onIsRefreshChange}          // 變更頁面刷新狀態
                                    updateGroup={this.updateGroup}                      // 取得(站台編號,站台名稱)
                                    redirectURLBattLayer2={this.redirectURLBattLayer2}  // 跳轉至電池數據第二層(儲存站台名稱,號碼)
                                    redirectURLAlert={this.redirectURLAlert}            // 查看告警(跳轉至告警未解決)
                                    exportExcel={this.exportBattGroupExcel}             // 輸出電池數據表格
                                    onSelectAllChange={this.onSelectAllChange}          // 變更表格checkbox全選
                                    cancelSelectedAll={this.cancelSelectedAll}          // 取消全選
                                    onCheckboxChange={this.onCheckboxChange}            // 變更各別checkbox狀態
                                    handleSubmit={this.handleSubmit}                    // 發送批次BA,BB指令
                                />
                            )
                        }
                    </div>


                    {/* 顯示訊息視窗 */}
                    {
                        openMsg === true && (
                            <AlertMsg 
                                msgTitle={msgTitle}
                                open={openMsg}
                                isDisabledBtn={isDisabledBtn}
                                handleClose={()=>{this.setState({openMsg:!openMsg})}} 
                            >
                                <div className='col-12 p-0 my-4'>
                                    <div className='my-1'>
                                        <Translation>{(t) => <>{t(message)}</>}</Translation>
                                        <div>{/* 移動至電池管理/電池參數設定 有TaskId才顯示 */}
                                        {
                                            TaskId.length > 0 ? 
                                            <Translation>
                                                {(t) => <>{t('1561')}<Link to="/Command">{t('1562')}</Link>{t('1563')}</>}
                                            </Translation>
                                            : ''
                                        }
                                        </div>
                                    </div>
                                </div>
                            </AlertMsg>
                        )
                    }
                    
                </Fragment>
            )
        }
    }





    //Function
    // 清空資料
    resetData = () => {
        this.setState({
            loading: true,
            isRefresh: true,                                                    // 是否執行每五分鐘刷新
            isDisabledBtn: false,                                               // 防止重複點擊
            dialogId: '',                                                       // 選則顯示彈跳視窗(批次)(BA,BB,...)
            keySearch: '',                                                      // 關鍵字搜尋
            resultData: [],                                                     // 關鍵字搜尋後資料
            data: [],                                                           // api傳回預設資料(無checkbox狀態)
            currentPage:1,                                                      // 目前表格顯示第幾頁(預設第一頁)
            IMPType: 20,                                                        // 判斷顯示IMPType名稱
            selectAll: false,                                                   // 電池數據表格全選
            selectedList: [],                                                   // 己選取電池數據項目清單
            battGroupData: [],                                                  // 電池數據表格內容(含checkbox狀態)
            battGroupDataErrorMsg: 'Loading...',
            openMsg: false,                                                     // 顯示隱藏訊息視窗
            msgTitle: '',                                                       // 訊息視窗標題
            message: '',                                                        // 成功/錯誤訊息
            isFilterOpen: false,                                                // 篩選
        })
    }
    resetFilterData = () => {//右邊篩選全部初始化        
        this.props.resetAllBattData();
        let postData = {
            isFilterCompanyData: initFilterSelectData,
            isFilterCountryData: initFilterSelectData,
            isFilterAreaData: initFilterSelectData,
            isFilterGroupIdData: initFilterSelectData,
            isFilterBatteryStatusData: initFilterSelectData,
            isFilterBatteryGroupIdData: initFilterSelectData,
        }
        this.submitFilterData(postData);
    }
    /* 取得電池數據資料 */
    submitFilterData = (props) => {
        this.resetData();// reset
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 取得電池數據資料(POST)
        this.fetchBattGroupData(postData).then((response) => {// get api 多個電池群組
            if (response.code === '00' && response.msg) {
                const newData = [];
                response.msg.Battery.map((item) => {// 新增checked欄位，控制各checkbox狀態
                    return newData.push({...item,checked: false})
                });
                this.setState({
                    loading: false,
					isRefresh: true, // 是否執行每五分鐘刷新
                    data: response.msg.Battery,
                    IMPType: response.msg.IMPType,
                    resultData: [...newData],       //最後輸出資料
                    battGroupData: [...newData],    //api回傳原始資料
                    battGroupDataErrorMsg: '',
                })
            } else {
                console.error('電池數據(1)', response)
                this.setState({ loading: false, isRefresh:true,IMPType:20,data:[],resultData:[],battGroupData:[], battGroupDataErrorMsg: response.msg })
            }
        })
    }
    returnPostData = (state, type) => {
        const {
            isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterBatteryStatusData,
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
            "BatteryGroupId": {
                "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryGroupIdData.isDataList]
            },
            "Status": {
                "All": isFilterBatteryStatusData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryStatusData.isDataList]
            },
            "Type": type

        }
    }




    /* 篩選，搜查 */
    //打開右邊篩選(顯示/隱藏篩選清單)
    setOpenIsFilter = (value) => {
        this.setState({isFilterOpen: value})
        if(value) {
            this.setState({isRefresh:false})
        }else{
            this.setState({isRefresh:true})
        }
    }
    //刪除篩選條件成功後顯示msg視窗
    alertFilterMsgOpen = (msg) => {
        this.setState({
            isFilterOpen: false,
            openMsg: true,
            message: msg,
        })
        this.onIsRefreshChange(false)
    }
    //開關msg視窗
    handleAlertMsgClose = () => {
        this.setState({
            isFilterOpen: false,
            openMsg: false,
            message: '',//填寫訊息
        });
        this.onIsRefreshChange(true)
    }
    updateFilterData = (name, object) => {//更新右邊篩選值內容
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterBatteryStatusData,
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
            case isFilterBatteryStatusData:
                this.props.updateBatteryStatus(object);
                break;
            case isFilterBatteryGroupIdData:
                this.props.resetBatteryGroupId();
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
                "Status": { ...this.props.isFilterBatteryStatusData },
                "BatteryGroupId": { ...this.props.isFilterBatteryGroupIdData },
            },
        }
        this.ajaxSaveFilter(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { msg } = response;
            this.setState({
                isRefresh: false,
                openMsg: true,//開啟訊息視窗
                message: msg,//填寫訊息
            });
            // const { code, msg } = response;
            // if (code === '00' && msg) {
            //     this.setState({
			// 		isRefresh: false,
            //         openMsg: true,//開啟訊息視窗
            //         message: msg,//填寫訊息
            //     });
            // } else {
            //     this.setState({
			// 		isRefresh: false,
            //         openMsg: true,//開啟訊息視窗
            //         message: msg,//填寫訊息
            //     });
            // }
        });
    }

    // 關鍵字搜查
    getKeywordData = (value) => {
        const keyword = value.trim();
        const { battGroupData } = this.state;
        const resTableData = filterArray({
            dataArray:battGroupData,
            searchText:keyword,
            allowArray: ['Area','BatteryCount','BatteryGroupID','Company','Country','GroupID','GroupName','MaxIMP','MaxTemperature','MaxVol','MinIMP','MinTemperature','MinVol','RecTime','StatusDesc']
        });
        this.setState({
            keySearch: keyword,
            resultData: resTableData,
            currentPage: 1,
        })
    }
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterBatteryStatusData,
        } = FilterNames;
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
            isFilterBatteryStatusData: this.props.isFilterBatteryStatusData,
            isFilterBatteryGroupIdData: this.props.isFilterBatteryGroupIdData,
        }
        const { isDataList, isButtonList } = this.props[name];
            const newIsDataList = isDataList.filter(ite => ite !== item.Value);
            const newIsButtonList = isButtonList.filter(ite => ite !== item);
            const object = {
                isOpen: newIsButtonList.length > 0 ? true : false,
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
                case isFilterBatteryStatusData:
                    this.props.updateBatteryStatus(object);
                    break;
                case isFilterBatteryGroupIdData:
                    this.props.resetBatteryGroupId();
                    break;
                default:
            }
        this.submitFilterData(postData);
    }



    /* 表格 */
    // 取得目前第幾頁
    getTableActive = (value) => {
        this.setState({currentPage: value})
    }
    // 變更表格顯示隱藏欄位
    onToggleBtnChange = (value) => {
        const {tableHeader} = this.state;
        const newList = setNewTableHeader(tableHeader,value);
        this.props.updBattGroupTheader(newList);
    }
    // 變更checkbox全選
    onSelectAllChange = (e,active,row,data) => {
        // const { resultData } = this.state;
        const newData = setSelectAllData(data,e.target.checked,active,row);
        // 變更已選則清單(selectedList)
        const selectedList = setSelectedList(newData);

        this.setState({
            selectAll: e.target.checked,
            resultData: newData,
            selectedList:selectedList,
        });
    };
    // 取消全選
    cancelSelectedAll = () => {
        const { data } = this.state;
        const newData = [];
        data.map((item) => {
            return newData.push({...item,checked: false})
        })

        // 變更已選則清單(selectedList)
        const selectedList = setSelectedList(newData);

        this.setState({
            selectAll: false,
            resultData: newData,
            battGroupData: newData,
            selectedList:selectedList,
        })
    };
    // 變更各別checkbox狀態
    onCheckboxChange = (e) => {
        const { resultData } = this.state;
        const newData = setNewCheckboxList(resultData,e)

        // 變更已選則清單(selectedList)
        const selectedList = setSelectedList(newData)
        
        this.setState({
            resultData: newData,
            battGroupData: newData,
            selectedList: selectedList,
        })
    };

    // 查看告警 跳轉至告警未解決(alertAction)
    redirectURLAlert = (data) => {
        const { groupName,groupLabel } = data; //groupId
        const GroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [groupName],
            isButtonList: [{Value:groupName,Label:groupLabel}],
        }
        this.props.setBattInternalIDAlert('');
		this.props.resetAllAlertUnsolved();
		this.props.updateGroupIdUnSolved(GroupId);
    }
    // 跳轉至電池數據(第二層)// 取得(站台編號,站台名稱)
    redirectURLBattLayer2 = (data) => {
        const Company = {
            isOpen: true,
            isChecked: false,
            isDataList: [`${data.companyCode}`],
            isButtonList: [{Value:data.companyCode,Label:data.company}],
            // isButtonList: [data.companyCode],
        }
        const GroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [Number(data.groupId)],
            isButtonList: [{Value:Number(data.groupId),Label:data.groupLabel}],
        }
        this.props.updateCompany(Company)
        this.props.updateGroupId(GroupId);
    }
    // 跳轉至電池管理/電池參數設定
    redirectURLCommand = (data) => {
        this.props.setActiveNum(0);
        this.props.setSubmitKeyList(data);

    }

    // 清空訊息
    resetMessage = () => {
        const {message} = this.state;
        if(message !== '') {
            this.setState({message: ''})
        }
        return 
    }
    // 變更頁面刷新狀態
    onIsRefreshChange = (value) => {
        this.setState({isRefresh: value})
    }


    /* 指令視窗 */
    // 變更彈跳指令視窗(BA,BB)
    setOpenDialog = (value) => {
        if(value === ''){ this.setState({isRefresh:true})}
        this.setState({dialogId: value})
    }
    // 發送批次BA,BB指令
    handleSubmit = (code,list) => {
        // 當BatteryGroupID為多筆時,去除外層[](因單筆時為string,BatteryGroupID回傳值必須為Array)
        if(list['BatteryGroupID'].length === 1) { // 當只有一筆時
            if(Array.isArray(list['BatteryGroupID'][0])) {
                list['BatteryGroupID'] = list['BatteryGroupID'][0]
            }
        }else{//多筆時
            const newList = [];
            list['BatteryGroupID'].map((item) =>{
                if(Array.isArray(item)){            // 當item為Array時
                    if(item.length >= 1) {          // 當item為多筆時
                        item.map( (element) => {
                            return newList.push(element)
                         })
                    }else {                         // 單筆時
                        return newList.push(item.join(''))
                    }
                }else{
                    return newList.push(item)
                }
                return null
            })
            list['BatteryGroupID'] = newList;
        }

		//彈跳視窗顯示處理中
        this.setState({dialogId:'',message: '1091',msgTitle: '1558',openMsg: true,isDisabledBtn:true,isRefresh:false})
        if(code === '186'){
			// 發送批次BA指令並回傳BASetting訊息
            this.fetchBASetting(list).then( (response) => {
				// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
				this.afterSubmitResponse(response);
            })
        }
        else if (code === '187'){
			// 發送批次BB指令並回傳BBSetting訊息
            this.fetchBBSetting(list).then( (response) => {
				// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
				this.afterSubmitResponse(response);
            })
        }
    }
    
	// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
	afterSubmitResponse = (response) => {
        if(response.msg.TaskId){
            this.setState({TaskId: response.msg.TaskId})
            this.redirectURLCommand(response.msg.TaskId);
        }else{
            this.setState({TaskId: []})
        }
        this.getMessage(response)
    }
    getMessage = (response) => {
		if(response.code === "00") {
            this.setState({message: response.msg.Message,openMsg: true,isDisabledBtn:false})
        }else{
            this.setState({message: response.msg,openMsg: true,isDisabledBtn:false})
        }
    }



    // get api 取得多個電池群組(POST)
    fetchBattGroupData = (postData) => {
        const {token, company, curLanguage, timeZone} = this.props;
        const url = `${apiUrl}getBatteryGroup`;
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData)
    }
    //儲存篩選API(POST):
    ajaxSaveFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}saveFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //儲存篩選API(POST):
        //https://www.gtething.tw/battery/saveFilter
    }
    


    
    // api 變更BA設定清單(POST)
    fetchBASetting = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}addPeriodSettingBatch`;
		return ajax(url, "POST", token, curLanguage, timeZone, company,postData);
    };
    // api 變更BB設定清單(POST)
    fetchBBSetting = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}addIRSettingBatch`;
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData);      
    };
    
    
    //excel用postData
    postExcelData = (type) =>{
        const {
            isFilterCompanyData,isFilterCountryData,isFilterAreaData,isFilterGroupIdData,isFilterBatteryStatusData,
        } = this.props;
        const postData = {
            Company: {
                All: isFilterCompanyData.isChecked ? '1' :'0',
                List: isFilterCompanyData.isDataList
            },
            Country: {
                All: isFilterCountryData.isChecked ? '1' :'0',
                List: isFilterCountryData.isDataList
            },
            Area: {
                All: isFilterAreaData.isChecked ? '1' :'0',
                List: isFilterAreaData.isDataList,
            },
            GroupID: {
                All: isFilterGroupIdData.isChecked ? '1' :'0',
                List: isFilterGroupIdData.isDataList,
            },
            BatteryStatus: {
                All: isFilterBatteryStatusData.isChecked ? '1' : '0',
                List: isFilterBatteryStatusData.isDataList,
            },
            Type:type
        }
        return postData
    }
    // 匯出EXCEL
    exportBattGroupExcel = () => {
        this.setState({
            openMsg:true,                      // 開啟訊息視窗
            message:"1091",                    // 填寫訊息
            msgTitle:"1089",                    // 訊息標題(處理結果)
            isDisabledBtn:true,                // 防止重複點擊
        })
        
        // 判斷是否有excel可執行下載
        this.fetchCheckBattGroupDataEXCEL().then(response => {
            if (response.code === '00') {       // type=csv 輸出CSV檔
                const {token, curLanguage, timeZone, company} = this.props;
                const exportURL = `${apiUrl}getBatteryGroup`;
                const excelName = response.msg;
                const postData = this.postExcelData('csv');
                exportExcel(token, curLanguage, timeZone, company, exportURL, excelName, postData, ()=>{
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "1090",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                },'POST',()=>{
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "5003",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                })
            } else {console.error('匯出EXCEL', response.msg)}
        })
    };
    // type=check檢查有EXCEL無資料
    fetchCheckBattGroupDataEXCEL = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getBatteryGroup`;
        const postData = this.postExcelData('check');
        return ajax(url, 'POST', token, curLanguage, timeZone, company ,postData)
    };



}



const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,                //目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        buttonControlList: state.BattDataReducer.buttonControlList,     //電池數據按鈕權限清單
        battGroupTheader: state.BattDataReducer.battGroupTheader,       //電池數據(第一層)表格標題
		groupInternalId: state.BattDataReducer.groupInternalId,         //電池數據(第一層)站台編號
        // 篩選條件
        isFilterCompanyData: state.BattFilterReducer.isFilterCompanyData,
        isFilterCountryData: state.BattFilterReducer.isFilterCountryData,
        isFilterAreaData: state.BattFilterReducer.isFilterAreaData,
        isFilterGroupIdData: state.BattFilterReducer.isFilterGroupIdData,
        isFilterBatteryGroupIdData: state.BattFilterReducer.isFilterBatteryGroupIdData,
        isFilterBatteryStatusData: state.BattFilterReducer.isFilterBatteryStatusData,
		// 告警篩選條件
		isFilterGroupIdDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterGroupIdDataUnSolved,
		isFilterGroupNameDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterGroupNameDataUnSolved,
        // 跳轉至電池管理-電池參數設定
        activeNum: state.CommandReducer.activeNum,
        submitKeyList: state.CommandP1504Reducer.submitKeyList,
    };
};
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setGroupID: (value) => dispatch(setGroupID(value)),
        updBattGroupTheader: (data) => dispatch(updBattGroupTheader(data)),
        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        // 篩選條件
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateCountry: (object) => dispatch(updateCountry(object)),
        updateArea: (object) => dispatch(updateArea(object)),
        updateGroupId: (object) => dispatch(updateGroupId(object)),
        updateGroupName: (object) => dispatch(updateGroupName(object)),
        updateBatteryStatus: (object) => dispatch(updateBatteryStatus(object)),
        resetCompany: () => dispatch(resetCompany()),
        resetCountry: () => dispatch(resetCountry()),
        resetArea: () => dispatch(resetArea()),
        resetGroupId: () => dispatch(resetGroupId()),
        resetBatteryGroupId: () => dispatch(resetBatteryGroupId()),
        resetBatteryStatus: () => dispatch(resetBatteryStatus()),
        resetAllBattData: () => dispatch(resetAllBattData()),
		// 告警
        setBattInternalIDAlert: (value) => dispatch(setBattInternalIDAlert(value)),
		updateGroupIdUnSolved: (object) => dispatch(updateGroupIdUnSolved(object)),
		updateGroupNameUnSolved: (object) => dispatch(updateGroupNameUnSolved(object)),
		resetAllAlertUnsolved: () => dispatch(resetAllAlertUnsolved()),
		// 電池管理/電池參數設定篩選條件
        setActiveNum: (value) => dispatch(setActiveNum(value)),
        setSubmitKeyList: (object) => dispatch(setSubmitKeyList(object)),
    };
};
export default connect(mapStateToProps,mapDispatchToProps)(BattGroup);