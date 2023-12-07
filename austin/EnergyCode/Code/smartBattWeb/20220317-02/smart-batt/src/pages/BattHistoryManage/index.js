import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import { setBattInternalID } from '../../actions/BattDataAction';
import {
    updateCompany, updateCountry, updateArea, updateGroupId,
    updateBatteryGroupIdBatt, updateBatteryStatus, updateRecordTime,
    resetAllBattData, resetCompany, resetCountry, resetArea, resetGroupId,
    resetBatteryGroupId, resetBatteryStatus, resetRecordTime,
} from '../../actions/BattFilterAction';
import { apipath, ajax } from '../../utils/ajax';
import { exportExcel } from '../../utils/exportExcel';
import { Translation,Trans } from 'react-i18next';                              // i18n
import PageHeader from '../../components/PageHeader';                           // PageHeader
import AlertMsg from '../../components/AlertMsg'; 						        // 顯示訊息視窗
// 篩選
import FilterDrawer from '../../components/FilterDrawer';                       // 右側篩選欄外框
import Filter from './Filter';                                                  // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                         // 儲存標籤
import { FilterNames,initFilterDate } from '../../components/BattFilter/InitDataFormat';         // 初始化格式
import FilterItemTag from '../../components/BattFilter/FilterItemTag';          // 篩選條件Button



const apiUrl = apipath();
class BattHistoryManage extends Component {
    constructor(props){
        super(props)
        this.state={
            loading: false,
            isRefresh: false, 							// 是否執行每五分鐘刷新
            isDisabledBtn: false, 						// 防止重複點擊
            pageId: '1600_1',                           // 儲存篩選時使用funciton_id
            data:[],                                    // 篩選後取得資料
            openMsg: false,                             // 顯示隱藏訊息視窗
            msgTitle: '',                               // 訊息視窗標題
            message: '',                                // 成功訊息/錯誤訊息
            isFilterOpen: true,                         // 篩選(預設開啟)
			isFilterSaveInput: '',                      // 儲存標籤 
        }
    }

    componentDidMount() {
        this.props.setTreeFunctionId('1600');//設定功能選單指項位置
        // 清空篩選欄位
        this.props.resetAllBattData();
        this.props.resetCompany();
        this.props.resetCountry();
        this.props.resetArea();
        this.props.resetGroupId();
        this.props.resetBatteryGroupId();
        this.props.resetBatteryStatus();
        this.props.resetRecordTime();
    }

    componentWillUnmount() {
        this.resetData(); // reset
    }
    render() {
         const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
             isFilterBatteryGroupIdData, isFilterBatteryStatusData, isFilterRecordTimeData,
         } = this.props;//篩選
         const {
             loading,isDisabledBtn,
             openMsg,                    //顯示訊息視窗
             msgTitle,                   //訊息視窗標題
             message,
             isFilterOpen,               //開啟篩選視窗
         } = this.state;
         if (loading) { return ''}
         else {
            return (
                <Fragment>
                    {/* 電池歷史 */}
                    <PageHeader
						title={<Translation>{(t) => <>{t("1600")}</>}</Translation>}
					/>

                    <div className="container-fuild">
                        <div className="col-12 p-0 mt-4">
                            {/* 篩選 */}
							<FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                                resetEvent={()=>{this.resetFilterData()}}    //清空所有欄位
                            >
                                <Filter
                                    functionId='1600_1' //判斷篩選清單顯示項目(電池歷史1層 1600_1)
                                    isFilterCompanyData={isFilterCompanyData}
                                    isFilterCountryData={isFilterCountryData}
                                    isFilterAreaData={isFilterAreaData}
                                    isFilterGroupIdData={isFilterGroupIdData}
                                    isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                    isFilterBatteryStatusData={isFilterBatteryStatusData}
                                    isFilterRecordTimeData={isFilterRecordTimeData}
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
                                <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={()=>{this.props.resetAllBattData()}}>
                                    <Trans i18nKey="1038" />
                                </button>
                            </div>
                            <FilterItemTag
                                company={this.props.company}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterCountryData={isFilterCountryData}
                                isFilterAreaData={isFilterAreaData}
                                isFilterGroupIdData={isFilterGroupIdData}
                                isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                isFilterBatteryStatusData={isFilterBatteryStatusData}
                                isFilterRecordTimeData={isFilterRecordTimeData}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
                        </div>


                        <h3 className="col-12 text-center m-4"><Translation>{(t) => <>{t('1605')}</>}</Translation></h3>

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
                                        <div className='my-1'><Translation>{(t) => <>{t(message)}</>}</Translation></div>
                                    </div>
                                </AlertMsg>
                            )
                        }
                        </div>
                </Fragment>
            )
        }
    }

    
    // 清空資料
    resetData = () => {
        this.setState({
            loading: false,
            isRefresh: false, 							// 是否執行每五分鐘刷新
            isDisabledBtn: false, 						// 防止重複點擊
            data:[],                                    // 篩選後取得資料
            openMsg: false,                             // 顯示隱藏訊息視窗
            msgTitle: '',                               // 訊息視窗標題
            message: '',                                // 成功訊息/錯誤訊息
            isFilterSaveInput: '',                      // 儲存標籤 
            isFilterOpen: true,                         // 篩選(預設開啟)
        });
    }
    resetFilterData = async() => {//右邊篩選全部初始化
        await this.setState({isFilterOpen: false})
        await this.setOpenIsFilter(false);
        await this.props.resetAllBattData();
    }
    /* 取得電池歷史篩選資料 */
    submitFilterData = (props) => {
        this.resetData();// reset
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 取得電池歷史篩選(POST)
        this.fetchBattHistoryData(postData).then((response) => {// get api 多個電池群組
            this.setState({
                isFilterOpen: false,
            })
            if(response.code === '07'){// 無資料訊息,錯誤訊息
                this.setState({
                    isFilterOpen: false,
                    openMsg: true,
                    message: response.msg,
                })
            }else{
                if(response.msg.BattInternalId && response.msg.BattInternalId !== ''){// 單筆
                    const BatteryId = {
                        isButtonList: [{Value:response.msg.BattInternalId,Label:response.msg.BatteryGroupId}],
                        isChecked: false,
                        isDataList: [response.msg.BattInternalId],
                        isOpen: true,
                    }
                    this.props.setBattInternalID(response.msg.BattInternalId)
                    this.props.updateBatteryGroupIdBatt(BatteryId);
                    window.location.href = '#/BattHistory';
                }else{// 多筆
                    this.props.setBattInternalID(response.msg.BattInternalId)
                    this.setOpenIsFilter(false);    //關閉篩選視窗
                    this.exportBattHistoryExcel(response.msg);
                }

            }
        })
    }
    returnPostData = (state, type) => {
        const {
            isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterRecordTimeData,
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
            "RecTime": { ...isFilterRecordTimeData },
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
    }
    //開關msg視窗
    handleAlertMsgClose = () => {
        this.setState({
            isFilterOpen: false,
            openMsg: false,
            message: '',//填寫訊息
		});
    }
    updateFilterData = (name, object) => {//更新右邊篩選值內容
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterBatteryStatusData, isFilterRecordTimeData,
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
            case isFilterBatteryGroupIdData:
                this.props.updateBatteryGroupIdBatt(object);
                break;
            case isFilterBatteryStatusData:
                this.props.updateBatteryStatus(object);
                break;
            case isFilterRecordTimeData:
                this.props.updateRecordTime(object);
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
                "BatteryGroupId": { ...this.props.isFilterBatteryGroupIdData },
                "Status": { ...this.props.isFilterBatteryStatusData },
                "RecTime": { ...this.props.isFilterRecordTimeData },
            },
        }
        this.ajaxSaveFilter(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openMsg: true,//開啟訊息視窗
                    message: msg,//填寫訊息
                });
            } else {
                this.setState({
                    openMsg: true,//開啟訊息視窗
                    message: msg,//填寫訊息
                });
            }
        });
    }
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryStatusData, isFilterBatteryGroupIdData, isFilterRecordTimeData,
        } = FilterNames;
        if (name === isFilterRecordTimeData && isFilterRecordTimeData.Radio === "0") { return; }
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
            isFilterBatteryGroupIdData: this.props.isFilterBatteryGroupIdData,
            isFilterBatteryStatusData: this.props.isFitlerBatteryStatusData,
            isFilterRecordTimeData: this.props.isFilterRecordTimeData,
        }
        if (name === isFilterRecordTimeData) {
            this.props.resetRecordTime();//設定時間初始化
            postData.isFilterRecordTimeData = initFilterDate;
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
                case isFilterBatteryGroupIdData:
                    this.props.updateBatteryGroupIdBatt(object);
                    break;
                case isFilterBatteryStatusData:
                    this.props.updateBatteryStatus(object);
                    break;
                case isFilterRecordTimeData:
                    this.props.updateRecordTime(object);
                    break;
                default:
            }
        }
        this.submitFilterData(postData);
    }



    // 清空訊息
    resetMessage = () => {
        const {message} = this.state;
        if(message !== '') {
            this.setState({message: ''})
        }
        return 
    }




    // get api 取得多個電池群組(POST)
    fetchBattHistoryData = (postData) => {
        const {token, company, curLanguage, timeZone} = this.props;
        const url = `${apiUrl}getBatteryHistoryCheck`;
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
    // 匯出EXCEL
    exportBattHistoryExcel = (list) => {
        this.setState({
            openMsg:true,                      // 開啟訊息視窗
            message:"1091",                    // 填寫訊息
            msgTitle:"1089",                    // 訊息標題(處理結果)
            isDisabledBtn:true,                // 防止重複點擊
        })
        // 執行下載excel
        const {token, curLanguage, timeZone, company} = this.props;
        const exportURL = `${apiUrl}getBatteryHistoryExcel`;
        const postData = this.returnPostData(this.props, "excel");
        const excelName = list.FileName;
        exportExcel(token, curLanguage, timeZone, company, exportURL, excelName, postData, ()=>{
            this.setState({
                openMsg: true,          // 開啟訊息視窗
                message: "1090",        // 填寫訊息
                isDisabledBtn: false,   // 防止重複點擊
            })
        },'POST', ()=>{
            this.setState({
                openMsg: true,          // 開啟訊息視窗
                message: "5003",        // 填寫訊息
                isDisabledBtn: false,   // 防止重複點擊
            })
        })
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
        perPage: state.LoginReducer.perPage,
        buttonControlList: state.BattDataReducer.buttonControlList,     //電池數據按鈕權限清單
        battGroupTheader: state.BattDataReducer.battGroupTheader,       //電池數據(第一層)表格標題
        battInternalId: state.BattDataReducer.battInternalId,
        // 篩選條件
        isFilterCompanyData: state.BattFilterReducer.isFilterCompanyData,
        isFilterCountryData: state.BattFilterReducer.isFilterCountryData,
        isFilterAreaData: state.BattFilterReducer.isFilterAreaData,
        isFilterGroupIdData: state.BattFilterReducer.isFilterGroupIdData,
        isFilterBatteryGroupIdData: state.BattFilterReducer.isFilterBatteryGroupIdData,
        isFilterBatteryStatusData: state.BattFilterReducer.isFilterBatteryStatusData,
        isFilterRecordTimeData: state.BattFilterReducer.isFilterRecordTimeData,
    };
};
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setBattInternalID: (value) => dispatch(setBattInternalID(value)), 
        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        // 篩選條件
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateCountry: (object) => dispatch(updateCountry(object)),
        updateArea: (object) => dispatch(updateArea(object)),
        updateGroupId: (object) => dispatch(updateGroupId(object)),
        updateBatteryGroupIdBatt: (object) => dispatch(updateBatteryGroupIdBatt(object)),
        updateBatteryStatus: (object) => dispatch(updateBatteryStatus(object)),
        updateRecordTime: (object) => dispatch(updateRecordTime(object)),
        resetCompany: () => dispatch(resetCompany()),
        resetCountry: () => dispatch(resetCountry()),
        resetArea: () => dispatch(resetArea()),
        resetGroupId: () => dispatch(resetGroupId()),
        resetBatteryGroupId: () => dispatch(resetBatteryGroupId()),
        resetBatteryStatus: () => dispatch(resetBatteryStatus()),
        resetRecordTime: () => dispatch(resetRecordTime()),
        resetAllBattData: () => dispatch(resetAllBattData()),
    };
};
export default connect(mapStateToProps,mapDispatchToProps)(BattHistoryManage);