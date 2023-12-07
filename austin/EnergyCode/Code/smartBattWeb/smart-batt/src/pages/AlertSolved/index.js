import React,{Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Translation,Trans } from 'react-i18next';

import {exportExcel} from '../../utils/exportExcel';
import {updSolvedTheader} from '../../actions/AlertAction';
import {
    updateEventTypeCode, updateCompany, updateCountry, updateArea, updateGroupId,
    updateRecordTimeAlert,
    resetAllAlert, resetEventTypeCode, resetCompany, resetCountry, resetArea, resetGroupId,
    resetRecordTimeAlert,
} from '../../actions/AlertFilterAction';
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import {setNewTableHeader,setNewSliderList} from '../../components/CusTable/utils';
import { filterArray } from '../../components/CusSearchInput/utils';            // 關鍵字搜尋整理資料
import { FilterNames, initFilterSelectData, initFilterDate  } from './AlertFilter/InitDataFormat';//初始化格式
import { returnPostData } from './AlertFilter/utils';
import { ajaxSaveFilter } from '../../components/FilterDrawer/utils';           // 篩選用
import { getSolvedData, fetchCheckSolvedDataEXCEL } from './getApi';

import PageTabHeader from '../../components/PageTabHeader';
import CusSearchInput from '../../components/CusSearchInput';                   // 關鍵字搜查
import AlertSolvedTable from './AlertSolvedTable';
import ToggleBtnBar from '../../components/CusTable/ToggleBtnBar';              // 表格欄位顯示隱藏
import AlertMsg from '../../components/AlertMsg';                               // 顯示訊息視窗
import FilterDrawer from '../../components/FilterDrawer';                       // 右側篩選欄外框
import AlertFilter from './AlertFilter';                                        // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                         // 儲存標籤
import FilterItemTag from './AlertFilter/FilterItemTag';                        // 篩選條件Button




class AlertSolved extends Component {
    constructor(props){
        super(props)
        this.state = {
            loading: true,
            isRefresh: true,                            // 是否執行每五分鐘刷新
            isDisabledBtn: false,                       // 防止重複點擊
            isAlertDialog: false,                       // 是否展開彈跳視窗(單一告警)
            isMultiAlertDialog: false,                  // 是否展開彈跳視窗(批次告警)
            pageId: '1302',                             // 儲存篩選時使用funciton_id
            alertDialogContent:{},                      // 單一告警顯示內容
            headerList:[],                              // 頁面頁籤
            keySearch: '',                              // 關鍵字搜尋
            tableHeader: this.props.solvedTheader,      // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
            resultData: [],                             // 關鍵字搜尋後資料
            data: [],                                   // api傳回預設資料(無checkbox狀態)
            currentPage:1,                              // 目前表格顯示第幾頁(預設第一頁)
            IMPType: 20,                                // 判斷顯示IMPType名稱
            selectAll: false,                           // 已解決告警表格全選
            selectedList: [],                           // 己選取已解決項目清單
            solvedData: [],                             // 已解決告警表格內容(含checkbox狀態)
            solvedDataErrorMsg: 'Loading...',
            openMsg: false,                             // 顯示隱藏訊息視窗
            msgTitle: '',                               // 訊息視窗標題
            // 篩選
            isFilterOpen: false,                        // 篩選
			isFilterSaveInput: '',                      // 儲存標籤 
        }
    }
    intervalID = 0;


    // React Lifecycle
    componentDidMount() {
        const {isRefresh} = this.state;
        this.props.setTreeFunctionId('1302');//設定功能選單指項位置
        this.submitFilterData(); // 預設值
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
    componentDidUpdate(prevProps, prevState){
        let mounted = true;
        // 當curLanguage更新時做
        if(this.props.curLanguage !== prevProps.curLanguage) {
            if(mounted){this.submitFilterData();}
        }
        return () => mounted = false; 
    }
    componentWillUnmount() {
        this.resetData();// reset
        clearInterval(this.intervalID);
    }




    render(){
        const { company,curLanguage } = this.props;
        const { isFilterEventTypeCodeData ,isFilterCompanyData, isFilterCountryData, isFilterAreaData, 
            isFilterGroupIdData, isFilterRecordTimeAlert,
        } = this.props;//篩選
        const { 
            loading,isDisabledBtn,
            isAlertDialog,              //顯示單筆詳細資訊
            alertDialogContent,         //已解決彈跳視窗內容
            headerList,
            tableHeader,
            currentPage,                //目前表格顯示第幾頁
            IMPType,
            resultData,solvedDataErrorMsg,
            openMsg,                    //顯示訊息視窗
            msgTitle,                   //訊息視窗標題
            message,
			isFilterOpen,               //開啟篩選視窗
        } = this.state;
        if (loading) { return ''}
        else{
            return (
                <Fragment>
                    {/* 告警 已解決 */}
                    <PageTabHeader list={headerList} /> 
                    <div className="container-fuild">
                        {/* 篩選 */}
                        <div className="col-12 p-0 mt-4">
                            <div className="d-inline-block mr-2">
                                <CusSearchInput placeholderName='1037' onClickEvent={(value)=>{this.getKeywordData(value)}} />{/* 關鍵字搜索 */}
                            </div>
                            {/* 右側篩選列表 */}
							<FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                                resetEvent={this.resetFilterData}    //清空所有欄位
                            >
                                <AlertFilter
                                    functionId='1302'//1301,103
                                    isFilterEventTypeCodeData={isFilterEventTypeCodeData}
                                    isFilterCompanyData={isFilterCompanyData}
                                    isFilterCountryData={isFilterCountryData}
                                    isFilterAreaData={isFilterAreaData}
                                    isFilterGroupIdData={isFilterGroupIdData}
                                    isFilterRecordTimeAlert={isFilterRecordTimeAlert}
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
                                isFilterEventTypeCodeData={isFilterEventTypeCodeData}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterCountryData={isFilterCountryData}
                                isFilterAreaData={isFilterAreaData}
                                isFilterGroupIdData={isFilterGroupIdData}
                                isFilterRecordTimeAlert={isFilterRecordTimeAlert}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
						</div>

                        <ToggleBtnBar company={company} list={tableHeader} onClickEvent={this.onToggleBtnChange} IMPType={IMPType} />

                        {/* 已解決事件表格 */}
                        <AlertSolvedTable 
                            tableHeader={tableHeader}                           // 表格欄位名稱
                            data={resultData}                                   // 已解決告警
                            IMPType={IMPType}                                   // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
                            language={curLanguage}                              // 目前語系
                            isAlertDialog={isAlertDialog}                       // 是否展開彈跳視窗(單一告警)
                            alertDialogContent={alertDialogContent}             // 單一告警顯示內容
                            active={currentPage}                                // 目前表格顯示第幾頁
                            getActive={this.getTableActive}                     // 取得目前表格為第幾頁
                            resetErrorMessage={this.resetErrorMessage}          // 清空失敗訊息
                            setIsAlertDialog={this.setIsAlertDialog}            // 變更彈跳視窗(單一告警)
                            onIsRefreshChange={this.onIsRefreshChange}          // 變更頁面刷新狀態
                            onSliderChange={this.onSliderChange}                // 變更表格內(電壓、內阻...)滑桿展開/隱藏
                            exportExcel={this.exportSolvedDataExcel}            // 輸出已解決表格
                            tableErrorMsg={solvedDataErrorMsg}
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
                {Name: '1301',Url:'/AlertUnsolved',Active:false},
                {Name: '1302',Url:'/AlertSolved',Active:true},
                {Name: '1303',Url:'/AlertCondition',Active:false},
            ],
            keySearch: '',                                                      // 關鍵字搜尋
            resultData: [],                                                     // 關鍵字搜尋後資料
            data: [],                                                           // api傳回預設資料(無checkbox狀態)
            currentPage: 1,                                                     // 表格頁數(預設第1頁)
            IMPType: 20,                                                        // 判斷顯示IMPType名稱
            selectAll: false,                                                   // 已解決告警表格全選
            selectedList: [],                                                   // 己選取已解決項目清單
            solvedData: [],                                                     // 已解決告警表格內容(含checkbox狀態)
            solvedDataErrorMsg: 'Loading...',
            openMsg: false,                                                     // 顯示隱藏訊息視窗
            msgTitle: '',                                                       // 訊息視窗標題
            isFilterOpen: false,                                                  // 是否開啟篩選視窗
        })
    };
    resetFilterData = () => {//右邊篩選全部初始化
		this.props.resetAllAlert();
        let postData = {
            isFilterEventTypeCodeData: initFilterSelectData,
            isFilterCompanyData: initFilterSelectData,
            isFilterCountryData: initFilterSelectData,
            isFilterAreaData: initFilterSelectData,
            isFilterGroupIdData: initFilterSelectData,
            isFilterRecordTimeAlert: initFilterDate,
        }
        this.submitFilterData(postData);
    }
    /* 取得告警已解決資料 */
    submitFilterData = (props) => {
        const { token, curLanguage, timeZone, company }  = this.props;
        const query = { token, curLanguage, timeZone, company } 
        this.resetData();// reset
        let postData;
        if (props === undefined) {
            postData = returnPostData(this.props, "");
        } else {
            postData = returnPostData(props, "");
        }
        // get api 取得已解決告警
        getSolvedData({query,postData}).then((response) => {
            const { code, msg } = response;
            if(code === '00' && msg) {
                // 新增checked欄位，控制各checkbox狀態
                const newData = [];
                msg.Alert.map( (item) => {
                    return newData.push({...item,checked: false,slider:true})
                })
                this.setState({
                    loading: false,
                    data: msg.Alert,
                    IMPType: msg.IMPType,
                    resultData: newData,
                    solvedData: newData,
                    solvedDataErrorMsg: '',
                })
            }else {
                this.setState({loading:false,data:[],IMPType:20,resultData:[],solvedData:[],solvedDataErrorMsg: msg})
            }
        })
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
        const { isFilterEventTypeCodeData, isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData, isFilterRecordTimeAlert,
        } = FilterNames;
        switch (name) {
            case isFilterEventTypeCodeData:
                this.props.updateEventTypeCode(object);
                break;
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
            case isFilterRecordTimeAlert:
                this.props.updateRecordTimeAlert(object);
                break;
            default:
        }
    }
    saveFilterData = (FilterName) => {//右邊篩選全部儲存
        const {token,company,curLanguage,timeZone} = this.props;
        const query = {token,company,curLanguage,timeZone};
        const postData = {
            "Account": this.props.account,
            "FunctionId": this.state.pageId,
            "FilterName": FilterName,
            "FilterConfig": {
                "Alert": { ...this.props.isFilterEventTypeCodeData },
                "Company": { ...this.props.isFilterCompanyData },
                "Country": { ...this.props.isFilterCountryData },
                "Area": { ...this.props.isFilterAreaData },
                "GroupID": { ...this.props.isFilterGroupIdData },
                "RecTime": { ...this.props.isFilterRecordTimeAlert},
            }
        }
        ajaxSaveFilter({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { msg } = response;
            if ( msg ) {
                this.setState({
					isRefresh: false,
                    openMsg: true,//開啟訊息視窗
                    message: msg,//填寫訊息
                });
            }
        });
    }

    // 關鍵字搜查
    getKeywordData = (value) => {
        const keyword = value.trim();
        const { solvedData } = this.state;
        const resTableData = filterArray({
            dataArray:solvedData,
            searchText:keyword,
            allowArray: ['Address','Area','BatteryGroupID','BatteryType','Company','Country','EventType','GroupID','GroupName','RecTime','Temperature'],
        })
        this.setState({
            keySearch: keyword,
            resultData: resTableData,
            currentPage: 1,
        })
    }
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterEventTypeCodeData, isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData, isFilterRecordTimeAlert,
        } = FilterNames;
        let postData = {
            isFilterEventTypeCodeData: this.props.isFilterEventTypeCodeData,
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
            isFilterRecordTimeAlert: this.props.isFilterRecordTimeAlert,
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
            case isFilterEventTypeCodeData:
                this.props.updateEventTypeCode(object);
                break;
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
            case isFilterRecordTimeAlert:
                this.props.updateRecordTimeAlert(item);
                break;
			default:
		}
        this.submitFilterData(postData);
    }



	/* 表格 */
    // 取得目前第幾頁
    getTableActive = (value) => { this.setState({currentPage: value}) }
    // 變更表格顯示隱藏欄位
    onToggleBtnChange = (value) => {
        const {tableHeader} = this.state;
        const newList = setNewTableHeader(tableHeader,value);
        this.props.updSolvedTheader(newList);
    }
    // 變更表格內(電壓、內阻...)滑桿展開/隱藏
    onSliderChange = (e) => {
        const { solvedData } = this.state;
        const newData = setNewSliderList(solvedData,e.target.id);
        this.setState({
            resultData: newData,
            solvedData: newData,
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



	// 匯出EXCEL
    exportSolvedDataExcel = () => {
        const { token,curLanguage,timeZone,company} = this.props;
        const query = { token,curLanguage,timeZone,company};
        const postData = returnPostData(this.props,'check');
        this.setState({
            openMsg: true,                      // 開啟訊息視窗
            message: "1091",                    // 填寫訊息
            msgTitle:"1089",                    // 訊息標題(處理結果)
            isDisabledBtn: true,                // 防止重複點擊
        })
        // 判斷是否有excel可執行下載
        fetchCheckSolvedDataEXCEL({query,postData}).then(response => {
            if(response.code === '00') {
                // type=csv 輸出CSV檔
                const {token,curLanguage,timeZone,company} = this.props;
                const exportURL = `getAlert?eventStatus=6`;
                const postData = returnPostData(this.props,'csv');
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
    
    


}



const mapStateToProps = ( state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage, //目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        solvedTheader: state.AlertReducer.solvedTheader, //已解決表格標題

        // 篩選條件
        isFilterEventTypeCodeData: state.AlertFilterReducer.isFilterEventTypeCodeData,
        isFilterCompanyData: state.AlertFilterReducer.isFilterCompanyData,
        isFilterCountryData: state.AlertFilterReducer.isFilterCountryData,
        isFilterAreaData: state.AlertFilterReducer.isFilterAreaData,
        isFilterGroupIdData: state.AlertFilterReducer.isFilterGroupIdData,
        isFilterRecordTimeAlert: state.AlertFilterReducer.isFilterRecordTimeAlert,
    }
}
const mapDispatchToProps = (dispatch,ownProps) => {
    return {
        updSolvedTheader: (data) => dispatch(updSolvedTheader(data)),
        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        // 篩選條件
        updateEventTypeCode: (object) => dispatch(updateEventTypeCode(object)),
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateCountry: (object) => dispatch(updateCountry(object)),
        updateArea: (object) => dispatch(updateArea(object)),
        updateGroupId: (object) => dispatch(updateGroupId(object)),
        updateRecordTimeAlert: (object) => dispatch(updateRecordTimeAlert(object)),
        resetEventTypeCode: () => dispatch(resetEventTypeCode()),
        resetCompany: () => dispatch(resetCompany()),
        resetCountry: () => dispatch(resetCountry()),
        resetArea: () => dispatch(resetArea()),
        resetGroupId: () => dispatch(resetGroupId()),
        resetRecordTimeAlert: () => dispatch(resetRecordTimeAlert()),
        resetAllAlert: () => dispatch(resetAllAlert()),
    }
}
export default connect(mapStateToProps,mapDispatchToProps)(AlertSolved);