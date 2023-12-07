import React,{Component, Fragment } from 'react';
import { connect } from 'react-redux';
import {updUnsolvedTheader} from '../../actions/AlertAction';
import { fetchAlertCountLoad } from '../../actions/AlertCountAction';
import {
    updateEventTypeCodeUnSolved, updateCompanyUnSolved, updateCountryUnSolved, updateAreaUnSolved, updateGroupIdUnSolved, updateGroupNameUnSolved,
    updateBatteryGroupIdUnSolved, updateBatteryTypeUnSolved,updateRecordTimeUnSolved,
    resetAllAlertUnsolved, resetEventTypeCodeUnSolved, resetCompanyUnSolved, resetCountryUnSolved, resetAreaUnSolved, resetGroupIdUnSolved, resetGroupNameUnSolved,
    resetBatteryGroupIdUnSolved, resetBatteryTypeUnSolved, resetRecordTimeUnSolved,
} from '../../actions/AlertUnsolvedFilterAction';
import { setBattInternalIDAlert } from "../../actions/BattDataAction"; //setBattInternalID
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import {apipath,ajax} from '../../utils/ajax';
import {exportExcel} from '../../utils/exportExcel';
import {
    setNewTableHeader,      //變更表格顯示隱藏欄位
    setNewSliderList,       //變更表格內(電壓、內阻...)滑桿展開/隱藏
    setNewCheckboxList,     //變更各別checkbox狀態
    setSelectedList,        //變更已選則清單(selectedList)
    setSelectAllSliderData, //變更checkbox全選(data有含有控制slider功能)
} from '../../components/CusTable/utils';
import { Translation,Trans } from 'react-i18next';  
// components
// import CusLoader from '../../components/CusLoader';
import PageTabHeader from '../../components/PageTabHeader';
import CusSearchInput from '../../components/CusSearchInput';                   //關鍵字搜查
import { SuccessAlert } from '../../components/CusAlert';                       //成功訊息
import AlertUnsolvedTable from './AlertUnsolvedTable';
import ToggleBtnBar from '../../components/CusTable/ToggleBtnBar';              //表格欄位顯示隱藏
import AlertMsg from '../../components/AlertMsg';                               // 顯示訊息視窗
import { filterArray } from '../../components/CusSearchInput/utils';            //關鍵字搜尋整理資料
// 篩選
import FilterDrawer from '../../components/FilterDrawer';                       // 右側篩選欄外框
import AlertUnSolvedFilter from './AlertUnSolvedFilter';                         // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                         // 儲存標籤
import { FilterNames, initFilterDate, initFilterSelectData } from './AlertUnSolvedFilter/InitDataFormat';//初始化格式
import FilterItemTag from './AlertUnSolvedFilter/FilterItemTag';         // 篩選條件Button




const apiUrl = apipath();
class AlertUnsolved extends Component {
    constructor(props){
        super(props)
        this.state={
            loading: true,
            isRefresh: true,                                        // 是否執行每五分鐘刷新
            isDisabledBtn: false,                                   // 防止重複點擊
            pageId: '1301',                                         // 儲存篩選時使用funciton_id
            isAlertDialog: false,                                   // 是否展開彈跳視窗(單一告警)
            isMultiAlertDialog: false,                              // 是否展開彈跳視窗(批次告警)
            alertDialogContent:{},                                  // 單一告警顯示內容
            headerList:[],                                          // 頁面頁籤
            keySearch: '',                                          // 關鍵字搜尋
            tableHeader: this.props.unsolvedTheader,                // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)                    
            resultData: [],                                         // 關鍵字搜尋後資料
            data: [],                                               // api傳回預設資料(無checkbox狀態)
            currentPage:1,                                          // 目前表格顯示第幾頁(預設第一頁)
            IMPType: 20,                                            // 判斷顯示IMPType名稱
            selectAll: false,                                       // 未解決告警表格全選
            selectedList: [],                                       // 己選取未解決項目清單
            unsolvedData: [],                                       // 未解決告警表格內容(含checkbox狀態)
            unsolvedDataErrorMsg: 'Loading...',
            successMsg: '',                                         // 成功訊息
            errorMsg: '',                                           // 錯誤訊息
            openMsg: false,                                         // 顯示隱藏訊息視窗
            msgTitle: '',                                           // 訊息視窗標題
            // 篩選
            isFilterOpen: false,                                    // 是否開啟篩選視窗
			isFilterSaveInput: '',                                  // 儲存標籤 
        }
    }
    intervalID = 0;


    // React Lifecycle
    componentDidMount() {
        const {isRefresh} = this.state;
        this.props.setTreeFunctionId('1301');//設定功能選單指項位置
        this.submitFilterData(); // 預設值
        if(isRefresh){  // 每五分鐘刷新頁面
            clearInterval(this.intervalID);
            this.intervalID = setInterval(() => {
                if(!this.state.isFilterOpen && !this.state.openMsg){
                    this.submitFilterData();
                }
            },300000);  // 5min: 5*60*1000 = 300000
        }else {
            clearInterval(this.intervalID);
        }
    }
    

    componentDidUpdate(prevProps, prevState){
        let mounted = true;
		if(this.props.isFilteGroupIdDataUnSolved !== prevProps.isFilteGroupIdDataUnSolved){
            if (mounted) {this.submitFilterData();}
		}
        // 當curLanguage更新時做
        if(this.props.curLanguage !== prevProps.curLanguage) {
            if(mounted){this.submitFilterData();}
        }
        return () => mounted = false; 
    }


    componentWillUnmount() {
        this.resetData();// reset
        // this.props.resetTreeFunctionId();
        this.resetFilterData();
        // this.props.setBattInternalID('');
        this.props.setBattInternalIDAlert('');
        clearInterval(this.intervalID);
    }

    



    render(){
        const { company,curLanguage,functionList } = this.props;
        const { isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, 
            isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved, isFilterBatteryGroupIdDataUnSolved,
        } = this.props;//篩選
        const pageInfo = functionList.filter(element => element.FunctionId === 1301)[0];
        const { 
            loading,isDisabledBtn,
            isAlertDialog,
            isMultiAlertDialog,
            alertDialogContent,
            headerList,
            tableHeader,
            currentPage,                //目前表格顯示第幾頁(預設第一頁)
            IMPType,
            selectAll,selectedList,
            resultData,unsolvedDataErrorMsg,
            successMsg,errorMsg,
            openMsg,                    //顯示訊息視窗
            msgTitle,                   //訊息視窗標題
            message,
			// 篩選
			isFilterOpen,               //開啟篩選視窗
        } = this.state;
        if (loading) { return ''}
        else{
            return (
                <Fragment>
                    {/* 告警 未解決 */}
                    <PageTabHeader list={headerList} /> 
                    <div className="container-fuild">
                        {/* 篩選 */}
                        <div className="col-12 p-0 mt-4">
                            <div className="d-inline-block mr-2">
                                {/* 關鍵字搜索 */}
                                <CusSearchInput placeholderName='1037' value={this.state.keySearch} onClickEvent={(value)=>{this.getKeywordData(value)}} />{/* 關鍵字搜索 */}
                                {/* Chaff for Chorme Smart Lock */}
                                <div style={{display:'none'}}> <input type="password" /></div>
                            </div>
                            {/* 右側篩選列表 */}
                            <FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                                resetEvent={this.resetFilterData}    //清空所有欄位
                            >
                                <AlertUnSolvedFilter
                                    functionId='1301'//1302,1303
                                    isFilterEventTypeCodeDataUnSolved={isFilterEventTypeCodeDataUnSolved}
                                    isFilterCompanyDataUnSolved={isFilterCompanyDataUnSolved}
                                    isFilterCountryDataUnSolved={isFilterCountryDataUnSolved}
                                    isFilterAreaDataUnSolved={isFilterAreaDataUnSolved}
                                    isFilterGroupIdDataUnSolved={isFilterGroupIdDataUnSolved}
                                    isFilterRecordTimeUnSolved={isFilterRecordTimeUnSolved}
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
                                company={this.props.company}
                                isFilterEventTypeCodeDataUnSolved={isFilterEventTypeCodeDataUnSolved}
                                isFilterCompanyDataUnSolved={isFilterCompanyDataUnSolved}
                                isFilterCountryDataUnSolved={isFilterCountryDataUnSolved}
                                isFilterAreaDataUnSolved={isFilterAreaDataUnSolved}
                                isFilterGroupIdDataUnSolved={isFilterGroupIdDataUnSolved}
                                isFilterBatteryGroupIdDataUnSolved={isFilterBatteryGroupIdDataUnSolved}
                                isFilterRecordTimeUnSolved={isFilterRecordTimeUnSolved}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
                        </div>

                        <ToggleBtnBar company={company} list={tableHeader} onClickEvent={this.onToggleBtnChange} IMPType={IMPType} />

                        {/* 成功關閉告警視窗 */}
                        {(successMsg && successMsg !== '') ? <SuccessAlert message={successMsg} resetMessage={()=>{this.resetSuccessMessage()}} /> : ''}
                        {/* 未解決事件表格 */}
                        <AlertUnsolvedTable 
                            isDisabledBtn={isDisabledBtn}                       // 解決重複點擊問題
                            isEdit={pageInfo.Edit}                              // 是否有編輯權限
                            selectAll={selectAll}                               // 未解決告警表格全選
                            tableHeader={tableHeader}                           // 表格欄位名稱
                            data={resultData}                                   // 未解決告警
                            dataTotal={resultData.length}                       // 目前表格總筆數
                            active={currentPage}                                // 目前表格顯示第幾頁
                            IMPType={IMPType}                                   // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
                            selectedList={selectedList}                         // 已選取未解決項目清單
                            errorMsg={errorMsg}                                 // 失敗訊息
                            language={curLanguage}                              // 目前語系
                            isAlertDialog={isAlertDialog}                       // 是否展開彈跳視窗(單一告警)
                            isMultiAlertDialog={isMultiAlertDialog}             // 是否展開彈跳視窗(批次告警)
                            alertDialogContent={alertDialogContent}             // 單一告警內容
                            tableErrorMsg={unsolvedDataErrorMsg}                     // 表格錯誤訊息
                            // function
                            resetErrorMessage={this.resetErrorMessage}          // 清空失敗訊息
                            getActive={this.getTableActive}                     // 取得目前表格為第幾頁
                            onIsRefreshChange={this.onIsRefreshChange}          // 變更頁面刷新狀態
                            onSliderChange={this.onSliderChange}                // 變更表格內(電壓、內阻...)滑桿展開/隱藏
                            exportExcel={this.exportunsolvedDataExcel}          // 輸出未解決表格
                            onSelectAllChange={this.onSelectAllChange}          // 變更表格checkbox全選
                            onCheckboxChange={this.onCheckboxChange}            // 變更各別checkbox狀態
                            cancelSelectedAll={this.cancelSelectedAll}          // 取消全選
                            setIsAlertDialog={this.setIsAlertDialog}            // 變更彈跳視窗(單一告警)
                            setIsMultiAlertDialog={this.setIsMultiAlertDialog}  // 變更彈跳視窗(批次告警)
                            onCloseAlertSumbit={this.onCloseAlertSumbit}        // 送出解決告警方案
                        />
                    </div>
                    
                    {/* 顯示訊息視窗 */}
                    <AlertMsg 
                        msgTitle={msgTitle}
                        open={openMsg}
                        isDisabledBtn={isDisabledBtn}
                        handleClose={()=>{this.setState({openMsg:!openMsg})}} 
                    >
                        <div className="col-12 p-0 my-4">
                            <div className="my-1">
                                <Translation> 
                                    {(t) => <>{t(message)}</>} 
                                </Translation>
                            </div>
                        </div>
                    </AlertMsg>
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
            isAlertDialog: false,                                               // 是否展開彈跳視窗(單一告警)
            isMultiAlertDialog: false,                                          // 是否展開彈跳視窗(批次告警)
            alertDialogContent:{},                                              // 單一告警顯示內容
            headerList:[                                                        // 頁面頁籤
                {Name: '1301',Url:'/AlertUnsolved',Active:true},
                {Name: '1302',Url:'/AlertSolved',Active:false},
                {Name: '1303',Url:'/AlertCondition',Active:false},
            ],
            keySearch: '',                                                      // 關鍵字搜尋 
            resultData: [],                                                     // 關鍵字搜尋後資料
            data: [],                                                           // api傳回預設資料(無checkbox狀態)
            currentPage:1,                                                      // 目前表格顯示第幾頁(預設第一頁)
            IMPType: 20,                                                        // 判斷顯示IMPType名稱
            selectAll: false,                                                   // 未解決告警表格全選
            selectedList: [],                                                   // 己選取未解決告警項目清單
            unsolvedData: [],                                                   // 未解決告警表格內容(含checkbox狀態)
            unsolvedDataErrorMsg: 'Loading...',
            successMsg: '',                                                     // 成功訊息
            errorMsg: '',                                                       // 錯誤訊息
            openMsg: false,                                                     // 顯示隱藏訊息視窗
            msgTitle: '',                                                       // 訊息視窗標題
            isFilterOpen: false,                                                // 是否開啟篩選視窗
        })
    };
    resetFilterData = async() => {//右邊篩選全部初始化
		await this.props.resetAllAlertUnsolved();
        await this.props.setBattInternalIDAlert('');
        let postData = {
            isFilterEventTypeCodeDataUnSolved: initFilterSelectData,
            isFilterCompanyDataUnSolved: initFilterSelectData,
            isFilterCountryDataUnSolved: initFilterSelectData,
            isFilterAreaDataUnSolved: initFilterSelectData,
            isFilterGroupIdDataUnSolved: initFilterSelectData,
            isFilterBatteryGroupIdDataUnSolved: initFilterSelectData,
            isFilterRecordTimeUnSolved: initFilterDate,
        }
        await this.submitFilterData(postData);
    }
    /* 取得告警未解決資料 */
    submitFilterData = (props) => {
        this.resetData();// reset
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // get api 取得未解決告警
        this.fetchUnsolvedData(postData).then((response) => {
            if(response.code === '00' && response.msg) {
                // 新增checked欄位，控制各checkbox狀態
                const newData = [];
                response.msg.Alert.map( (item) => {
                    return newData.push({...item,checked: false,slider:true})
                })
                this.setState({
                    loading: false,
                    data: response.msg.Alert,
                    IMPType: response.msg.IMPType,
                    resultData: newData,
                    unsolvedData: newData,
                    unsolvedDataErrorMsg: '',
                })
            }else {
                this.setState({loading:false,IMPType:20,data:[],resultData:[],unsolvedData:[],unsolvedDataErrorMsg: response.msg})
            }
        })
    }
    
    returnPostData = (state, type) => {
        const {
            isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, 
            isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved,
        } = state;
        return {
            "Alert": {//EventTypeCode
                "All": isFilterEventTypeCodeDataUnSolved.isChecked ? "1" : "0",
                "List": [...isFilterEventTypeCodeDataUnSolved.isDataList]
            },
            "Company": {
                "All": isFilterCompanyDataUnSolved.isChecked ? "1" : "0",
                "List": [...isFilterCompanyDataUnSolved.isDataList]
            },
            "Country": {
                "All": isFilterCountryDataUnSolved.isChecked ? "1" : "0",
                "List": [...isFilterCountryDataUnSolved.isDataList]
            },
            "Area": {
                "All": isFilterAreaDataUnSolved.isChecked ? "1" : "0",
                "List": [...isFilterAreaDataUnSolved.isDataList]
            },
            "GroupID": {
                "All": isFilterGroupIdDataUnSolved.isChecked ? "1" : "0",
                "List": [...isFilterGroupIdDataUnSolved.isDataList]
            },
            "BattInternalId": this.props.battInternalId,
            "RecTime": { ...isFilterRecordTimeUnSolved },
            "Type": type

        }
    }
    



	/* 篩選，搜查 */
    //打開右邊篩選(顯示/隱藏篩選清單)
	setOpenIsFilter = (value) => {
        if(value === true) {
            this.setState({isFilterOpen: value,isRefresh:false})
        }else{
            this.setState({isFilterOpen: value,isRefresh:true})
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
        const { isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, 
            isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved,
        } = FilterNames;
        switch (name) {
            case isFilterEventTypeCodeDataUnSolved:
                this.props.updateEventTypeCodeUnSolved(object);
                break;
            case isFilterCompanyDataUnSolved:
                this.props.updateCompanyUnSolved(object);
                break;
            case isFilterCountryDataUnSolved:
                this.props.updateCountryUnSolved(object);
                break;
            case isFilterAreaDataUnSolved:
                this.props.updateAreaUnSolved(object);
                break;
            case isFilterGroupIdDataUnSolved:
                this.props.updateGroupIdUnSolved(object);
                break;
            case isFilterRecordTimeUnSolved:
                this.props.updateRecordTimeUnSolved(object);
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
                "Alert": { ...this.props.isFilterEventTypeCodeDataUnSolved },
                "Company": { ...this.props.isFilterCompanyDataUnSolved },
                "Country": { ...this.props.isFilterCountryDataUnSolved },
                "Area": { ...this.props.isFilterAreaDataUnSolved },
                "GroupID": { ...this.props.isFilterGroupIdDataUnSolved },
                "BattInternalId": '',
                "RecTime": { ...this.props.isFilterRecordTimeUnSolved },
            }
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
        const { unsolvedData } = this.state;
        const resTableData = filterArray({
            dataArray:unsolvedData,
            searchText:keyword,
            allowArray: ['Address','Area','BatteryGroupID','BatteryType','Company','Country','EventType','GroupID','GroupName','IR','RecTime','Temperature','Vol']
        });
        this.setState({
            keySearch: keyword,
            resultData: resTableData,
            currentPage: 1,
        })
    }
    
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, 
            isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved,isFilterBatteryGroupIdDataUnSolved,
        } = FilterNames;
        let postData = {
            isFilterEventTypeCodeDataUnSolved: this.props.isFilterEventTypeCodeDataUnSolved,
            isFilterCompanyDataUnSolved: this.props.isFilterCompanyDataUnSolved,
            isFilterCountryDataUnSolved: this.props.isFilterCountryDataUnSolved,
            isFilterAreaDataUnSolved: this.props.isFilterAreaDataUnSolved,
            isFilterGroupIdDataUnSolved: this.props.isFilterGroupIdDataUnSolved,
            isFilterBatteryGroupIdDataUnSolved: this.props.isFilterBatteryGroupIdDataUnSolved,
            isFilterRecordTimeUnSolved: this.props.isFilterRecordTimeUnSolved,
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
            case isFilterEventTypeCodeDataUnSolved:
                this.props.updateEventTypeCodeUnSolved(object);
                break;
			case isFilterCompanyDataUnSolved:
				this.props.updateCompanyUnSolved(object);
				break;
			case isFilterCountryDataUnSolved:
				this.props.updateCountryUnSolved(object);
				break;
			case isFilterAreaDataUnSolved:
				this.props.updateAreaUnSolved(object);
				break;
			case isFilterGroupIdDataUnSolved:
				this.props.updateGroupIdUnSolved(object);
				break;
			case isFilterBatteryGroupIdDataUnSolved:
				this.props.updateBatteryGroupIdUnSolved(object);
				break;
            case isFilterRecordTimeUnSolved:
                this.props.updateRecordTimeUnSolved(item);
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
        this.props.updUnsolvedTheader(newList);
    }
    // 變更表格內(電壓、內阻...)滑桿展開/隱藏
    onSliderChange = (e) => {
        const { unsolvedData } = this.state;
        const newData = setNewSliderList(unsolvedData,e.target.id);
        this.setState({
            resultData: newData,
            unsolvedData: newData,
        })
    }
    // 清空成功訊息
    resetSuccessMessage = () => {
        const {successMsg} = this.state;
        if(successMsg !== ''){
            this.setState({successMsg: ''})
        }
        return 
    }
    // 清空失敗訊息
    resetErrorMessage = () => {
        const {errorMsg} = this.state;
        if(errorMsg !== '') {
            this.setState({errorMsg: ''})
        }
        return 
    }
    // 變更彈跳視窗(單一告警)
    setIsAlertDialog = ({data,value}) => {
        this.setState({isAlertDialog: value,alertDialogContent:data})
        value === true ? this.onIsRefreshChange(false): this.onIsRefreshChange(true);
        
    }
    // 變更彈跳視窗(批次告警)
    setIsMultiAlertDialog = (value) => {
        if(value === true){
            this.setState({isMultiAlertDialog: value,isRefresh:false})
        }else{
            this.setState({isMultiAlertDialog: value,isRefresh:true})
        }
        
    }
    // 變更checkbox全選
    onSelectAllChange = (e,active,row,data) => {
        // 變更checkbox全選(data有含有控制slider功能)
        const newData = setSelectAllSliderData(data,e.target.checked,active,row);
        // 變更已選則清單(selectedList)
        const selectedList = setSelectedList(newData)

        this.setState({
            selectAll: e.target.checked,
            resultData: newData,
            selectedList:selectedList,
        })
    }
    // 變更各別checkbox狀態
    onCheckboxChange = (e) => {
        const { resultData } = this.state;
        // 變更各別checkbox狀態
        const newData = setNewCheckboxList(resultData,e)
        // 變更已選則清單(selectedList)
        const selectedList = setSelectedList(newData)
        
        this.setState({
            resultData: newData,
            unsolvedData: newData,
            selectedList: selectedList,
        })
    }
    // 取消全選
    cancelSelectedAll = () => {
        const { data } = this.state;
        const newData = [];
        data.map( (item) => {
            return newData.push({...item,checked: false,slider:true})
        })

        // 變更已選則清單(selectedList)
        const selectedList = setSelectedList(newData)

        this.setState({
            selectAll: false,
            resultData: newData,
            unsolvedData: newData,
            selectedList:selectedList,
        })
    }
    // 變更頁面刷新狀態
    onIsRefreshChange = (value) => {
        this.setState({isRefresh: value})
    }

    //  展開篩選視窗
    changeIsFilterOpen = (value) => {
        if(value === true){
            this.setState({isFilterOpen: value})
            this.onIsRefreshChange(false)
        }else{
            this.setState({isFilterOpen: value})
            this.onIsRefreshChange(true)
        }
    }


    // 解決告警方案
    onCloseAlertSumbit = (list) => {
        const {token,company} =this.props;
        this.fetchCloseAlert(list)
        .then( response => {
            if(response.code === '00'){
                this.setState({
                    isDisabledBtn: false,
                    successMsg: response.msg,
                    errorMsg: '',
                })
                this.setIsAlertDialog(false);            // 關閉視窗(單一)
                this.setIsMultiAlertDialog(false);       // 關閉視窗(批次)
                this.props.fetchAlertCountLoad({token:token,company:company});   // 重新讀取告警數字
            }else{
                this.setState({
                    isDisabledBtn: false,
                    successMsg: '',
                    errorMsg: response.msg,
                })
            }
        })
        .then(
            setTimeout( async()=>{
                this.setState({
                    successMsg: '',
                    errorMsg: '',
                })
                window.location.reload(false); 
            }, 2000)
        )
    }

    // get api 取得未解決告警
	fetchUnsolvedData = (postData) => {
        const {token,company,curLanguage,timeZone} = this.props;
        const url = `${apiUrl}getAlert?eventStatus=5`;   //未解決
        return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
    }
    //儲存篩選API(POST):
    ajaxSaveFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}saveFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //儲存篩選API(POST):
        //https://www.gtething.tw/battery/saveFilter
    }

    // 關閉多個告警API(POST) 暫無
    fetchCloseAlert = (list) => {
        const {token,company,curLanguage,timeZone} = this.props;
        const url = `${apiUrl}closeAlert`;
        this.setState({isDisabledBtn: true})
        return ajax(url,'POST',token,curLanguage,timeZone,company,list)
        // https://www.gtething.tw/battery/battery/closeAlert
    }
    

    // 匯出excel檔
    exportunsolvedDataExcel = () => {
        this.setState({
            openMsg: true,                      // 開啟訊息視窗
            message: "1091",                    // 填寫訊息
            msgTitle:"1089",                    // 訊息標題(處理結果)
            isDisabledBtn: true,                // 防止重複點擊
        })
        // 判斷是否有excel可執行下載
        this.fetchCheckunsolvedDataEXCEL().then(response => {
            if(response.code === '00') {
                // type=csv 輸出CSV檔
                const {token,curLanguage,timeZone,company} = this.props;
                const exportURL = `${apiUrl}getAlert?eventStatus=5`;
                const postData = this.returnPostData(this.props,'csv');
                const excelName = response.msg;
                exportExcel(token,curLanguage,timeZone,company,exportURL,excelName,postData,()=>{
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
            } else {console.error("匯出EXCEL", response.msg)}
        })
    }
    // type=check檢查有EXCEL無資料
    fetchCheckunsolvedDataEXCEL = () => {
        const { token,curLanguage,timeZone,company} = this.props;
        const url = `${apiUrl}getAlert?eventStatus=5`;
        const postData = this.returnPostData(this.props,'check');
        return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
    }
    


}



const mapStateToProps = ( state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,                // 目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        groupInternalId: state.BattDataReducer.groupInternalId,     // 從電池數據查看告警來的(站台編號)
        unsolvedTheader: state.AlertReducer.unsolvedTheader,        // 未解決表格標題
    
        // 篩選條件
        isFilterEventTypeCodeDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterEventTypeCodeDataUnSolved,
        isFilterCompanyDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterCompanyDataUnSolved,
        isFilterCountryDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterCountryDataUnSolved,
        isFilterAreaDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterAreaDataUnSolved,
        isFilterGroupIdDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterGroupIdDataUnSolved,
        isFilterGroupNameDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterGroupNameDataUnSolved,
        isFilterBatteryGroupIdDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterBatteryGroupIdDataUnSolved,
        isFilterBatteryTypeDataUnSolved: state.AlertUnsolvedFilterReducer.isFilterBatteryTypeDataUnSolved,
        isFilterRecordTimeUnSolved: state.AlertUnsolvedFilterReducer.isFilterRecordTimeUnSolved,
        // 電池數據第二層跳轉至未解決時battInternalId
        battInternalId: state.BattDataReducer.battInternalIdAlert,
    }
}
const mapDispatchToProps = (dispatch , ownProps) => {
    return {
        fetchAlertCountLoad: (data) => dispatch(fetchAlertCountLoad(data)),
        updUnsolvedTheader: (data) => dispatch(updUnsolvedTheader(data)),
        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        setBattInternalIDAlert: (value) => dispatch(setBattInternalIDAlert(value)),
        // 篩選條件
        updateEventTypeCodeUnSolved: (object) => dispatch(updateEventTypeCodeUnSolved(object)),
        updateCompanyUnSolved: (object) => dispatch(updateCompanyUnSolved(object)),
        updateCountryUnSolved: (object) => dispatch(updateCountryUnSolved(object)),
        updateAreaUnSolved: (object) => dispatch(updateAreaUnSolved(object)),
        updateGroupIdUnSolved: (object) => dispatch(updateGroupIdUnSolved(object)),
        updateGroupNameUnSolved: (object) => dispatch(updateGroupNameUnSolved(object)),
        updateBatteryGroupIdUnSolved: (object) => dispatch(updateBatteryGroupIdUnSolved(object)),
        updateBatteryTypeUnSolved: (object) => dispatch(updateBatteryTypeUnSolved(object)),
        updateRecordTimeUnSolved: (object) => dispatch(updateRecordTimeUnSolved(object)),
        resetEventTypeCodeUnSolved: () => dispatch(resetEventTypeCodeUnSolved()),
        resetCompanyUnSolved: () => dispatch(resetCompanyUnSolved()),
        resetCountryUnSolved: () => dispatch(resetCountryUnSolved()),
        resetAreaUnSolved: () => dispatch(resetAreaUnSolved()),
        resetGroupIdUnSolved: () => dispatch(resetGroupIdUnSolved()),
        resetGroupNameUnSolved: () => dispatch(resetGroupNameUnSolved()),
        resetBatteryGroupIdUnSolved: () => dispatch(resetBatteryGroupIdUnSolved()),
        resetBatteryTypeUnSolved: () => dispatch(resetBatteryTypeUnSolved()),
        resetRecordTimeUnSolved: () => dispatch(resetRecordTimeUnSolved()),
        resetAllAlertUnsolved: () => dispatch(resetAllAlertUnsolved()),
    }
}
export default connect(mapStateToProps,mapDispatchToProps)(AlertUnsolved);