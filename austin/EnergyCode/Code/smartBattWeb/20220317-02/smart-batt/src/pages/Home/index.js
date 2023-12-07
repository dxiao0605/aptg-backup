import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { setTreeFunctionId, resetTreeFunctionId } from '../../actions/MainNavAction';
import { setButtonControlList } from '../../actions/BattDataAction';
import {
    updateHomeCompany, updateHomeCountry, updateHomeArea, updateHomeGroupId,
    resetAllHomeData, resetHomeCompany, resetHomeCountry, resetHomeArea, resetHomeGroupId,
} from '../../actions/HomeFilterAction';
import classNames from 'classnames';
import { apipath, ajax } from '../../utils/ajax';
import { exportExcel } from '../../utils/exportExcel';
import { Translation, Trans } from 'react-i18next';                      // i18n
// components
import PageHeader from '../../components/PageHeader'                    // PageHeader
import WrappedMap from '../../components/WrappedMap'                    // 電池地圖
import ChartHeader from '../../components/Chart/ChartHeader'            // 圖表Header
import BattStatePieChart from '../../components/BattStatePieChart'      // 電池狀態
import BattStateLineChart from '../../components/BattStateLineChart'    // 電池狀態變化
import { CardSpinner } from '../../components/Spinner';
// 訊息視窗
import AlertMsg from '../../components/AlertMsg';                       // 顯示訊息視窗
// 篩選
import FilterDrawer from '../../components/FilterDrawer';               // 右側篩選欄外框
import HomeFilter from './HomeFilter';                                  // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                 // 儲存標籤
import { FilterNames, initFilterSelectData } from './HomeFilter/InitDataFormat';// 初始化格式
import FilterItemTag from './HomeFilter/FilterItemTag';                 // 篩選條件Button




const apiUrl = apipath();
class Home extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loading: true,
            isRefresh: true,
            pageId: '1200',                           // 儲存篩選時使用funciton_id
            dfZoom: 11,
            mapCenter: { lat: parseFloat(125), lng: parseFloat(23.25) },    // 地圖預設中心點
            mapInfo: [],                                                 // 地圖群組資訊
            IMPType: 20,                                                // 地圖內組名稱代碼(20,21,22)
            mapInfoError: '',                                           // 地圖錯誤訊息
            stateChartData: {},                                         // 電池狀態
            stateLineChartData: {},                                     // 電池狀態變化
            stateChartDataMessage: '',                                  // 電池狀態錯誤訊息
            stateLineChartDataMessage: '',                              // 電池狀態變化錯誤訊息
            stateChart: false,                                          // 是否顯示圖餅圖
            stateLineChart: false,                                      // 是否顯示折線圖
            printPieChart: false,                                       // 列印圓餅圖
            printLineChart: false,                                      // 列印階梯圖
            ApiKey: this.props.Key,                                     // 金鑰
            openMsg: false,                             // 顯示隱藏訊息視窗
            msgTitle: '1089',                           // 訊息視窗標題
            message: '',                                // 成功訊息/錯誤訊息
            isFilterOpen: false,                        // 篩選
            isFilterSaveInput: '',                      // 儲存標籤 
        }
    }
    intervalID = 0;



    // React Lifecycle
    componentDidMount() {
        const { functionList,isGroupIdListStatus } = this.props;
        const { isRefresh } = this.state;
        const getBtnControlList = functionList
            .filter((filterItem) => filterItem.FunctionId === 1400)
            .map((item) => item.Button);
        this.props.setButtonControlList(getBtnControlList[0]);  // 電池數據按鈕權限清單
        this.props.setTreeFunctionId('1200');//設定功能選單指項位置
        this.submitFilterData(); // 預設值


        if(isGroupIdListStatus === true) {
            this.setState({
                isFilterOpen: true
            })
        }


        if(isRefresh) {  // 每五分鐘刷新頁面
            clearInterval(this.intervalID);
            // let counter = 0; // 跑幾次
            this.intervalID = setInterval(() => {
                // counter++;
                if (!this.state.isFilterOpen && !this.state.openMsg) {
                    this.submitFilterData();
                }
            }, 300000); // 5min: 5*60*1000 = 300000
        }else{
            clearInterval(this.intervalID);
        }
    }



    componentDidUpdate(prevProps, prevState) {
        let mounted = true;
        // 當curLanguage更新時做
        if (this.props.curLanguage !== prevProps.curLanguage) {
            if (mounted) { this.submitFilterData(); }
        }
        // 當language更新時做
        if (this.props.language !== prevProps.language) {
            if (mounted) { this.submitFilterData(); }
        }
        // 當timeZone更新時做
        if (this.props.timeZone !== prevProps.timeZone) {
            if (mounted) { this.submitFilterData(); }
        }
        return () => mounted = false
    }


    componentWillUnmount() {
        clearInterval(this.intervalID);// 卸載timer
        this.setState({ isRefresh: false })
        this.resetData();
    }





    render() {
        const { curLanguage } = this.props;
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,} = this.props;//篩選
        const {
            loading,
            mapCenter, dfZoom, ApiKey, mapInfo, mapInfoError,
            openMsg,	//顯示訊息視窗
            msgTitle,	//訊息視窗標題
            IMPType,
            printPieChart, printLineChart,
            stateChart, stateLineChart,
            stateChartData, stateChartDataMessage,
            stateLineChartData, stateLineChartDataMessage,
            message,
            isFilterOpen, //開啟篩選視窗
        } = this.state;

        // (顯示圖表)列印判斷
        const checkPrintPieChart = classNames({
            'print-hide': printPieChart === false || printLineChart === true,
            'print-show': printPieChart === true,
        })
        const checkPrintLineChart = classNames({
            'print-hide': printLineChart === false || printPieChart === true,
            'print-show': printLineChart === true,
        })
        const checkPrint = classNames({
            'print': printPieChart === true || printLineChart === true,
        })



        if (loading) { return '' }
        else {
            return (
                <Fragment>
                    {/* 文字-電池地圖 */}
                    <PageHeader title={<Translation>{(t) => <>{t("1201")}</>}</Translation>} />

                    <div className="container-fuild">

                        {/* 篩選 */}
                        <div className='col-12 p-0 mt-4 print-hide' style={{ zIndex: '100' }}>
                            {/* 右側篩選列表 */}
                            <FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                                resetEvent={this.resetFilterData}    //清空所有欄位
                            >
                                <HomeFilter
                                    functionId='1200'
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
                                company={this.props.company}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterCountryData={isFilterCountryData}
                                isFilterAreaData={isFilterAreaData}
                                isFilterGroupIdData={isFilterGroupIdData}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
                        </div>
                        {/* map */}
                        <div className="col-12 p-0 map mt-2 mb-4 print-hide" style={{ zIndex: '99' }}>
                            {   //判斷是否有地圖資訊
                                (
                                    () => {
                                        if (mapInfo.length > 0) {
                                            return <WrappedMap data={mapInfo} IMPType={IMPType} ApiKey={ApiKey} mapCenter={mapCenter} dfZoom={dfZoom} mapHeight='68vh' />
                                        } else {
                                            return <div className="text-center p-4">{mapInfoError}</div>
                                        }
                                    }
                                )()
                            }
                        </div>

                        {/* chart */}
                        <div className={`col-12 col-md-6 d-inline-block p-0 pr-1 pb-2 print-hide`}>
                            <div className="card p-2">
                                {/* 電池狀態 */}
                                <ChartHeader
                                    title={<Translation>{(t) => <>{t('1021')}</>}</Translation>}
                                    data={stateChartData}
                                    language={curLanguage}
                                    content={<BattStatePieChart data={stateChartData} language={curLanguage} />}
                                    refreshChartData={this.getBattStateChartData}
                                    handleExcelEvent={this.exportStatusNowDataExcel}
                                    handlePrintEvent={this.handlePieChartPrintEvent}
                                />
                                <div className="chart_content height-200">
                                    {   //判斷是否有電池狀態
                                        (() => {
                                            if (stateChart === true) {
                                                if (stateChartDataMessage === '') {
                                                    return <BattStatePieChart data={stateChartData} language={curLanguage} />
                                                } else {
                                                    return <div className="text-center p-4">{stateChartDataMessage}</div>
                                                }
                                            } else { return <CardSpinner /> }
                                        })()
                                    }
                                </div>
                            </div>
                        </div>
                        <div className={`col-12 col-md-6 d-inline-block p-0 pl-1 pb-2 print-hide`}>
                            <div className="card p-2">
                                {/* 電池狀態變化 */}
                                <ChartHeader
                                    title={<Translation>{(t) => <>{t('1203')}</>}</Translation>}
                                    refreshChartData={this.getBattStateLineChartData}
                                    handleExcelEvent={this.exportStatusDataExcel}
                                    handlePrintEvent={this.handleLineChartPrintEvent}
                                    content={<BattStateLineChart data={stateLineChartData} language={curLanguage} />} />
                                <div className="chart_content height-200">
                                    {   //判斷是否有電池狀態變化
                                        (() => {
                                            if (stateLineChart === true) {
                                                if (stateLineChartDataMessage === '') {
                                                    return <BattStateLineChart data={stateLineChartData} language={curLanguage} />
                                                } else {
                                                    return <div className="text-center p-4">{stateLineChartDataMessage}</div>
                                                }
                                            } else { return <CardSpinner /> }
                                        })()
                                    }
                                </div>
                            </div>
                        </div>
                    </div>



                    {/* PDF */}
                    <div className={`pt-2 pinnt-gray-mask ${checkPrint}`} style={{ width: 1043.1496063116 }}>
                        <div className={`col-12 pt-2 ${checkPrintPieChart}`} >
                            <div className="w-100 mt-4">
                                <div className="m-4 text-center"><Translation>{(t) => <>{t('1021')}</>}</Translation></div>
                                {   //判斷是否有電池狀態
                                    (() => {
                                        if (stateChart === true) {
                                            if (stateChartDataMessage === '') {
                                                return (
                                                    <div className="d-flex">
                                                        <BattStatePieChart data={stateChartData} language={curLanguage} />
                                                    </div>
                                                )
                                            } else {
                                                return <div className="text-center p-4">{stateChartDataMessage}</div>
                                            }
                                        } else { return <CardSpinner /> }
                                    })()
                                }
                            </div>
                        </div>
                        <div className={`col-12 pt-2 ${checkPrintLineChart}`}>
                            <div className="w-100 mt-4">
                                <div className="m-4 text-center"><Translation>{(t) => <>{t('1203')}</>}</Translation></div>
                                {   //判斷是否有電池狀態變化
                                    (
                                        () => {
                                            if (stateLineChart === true) {
                                                if (stateLineChartDataMessage === '') {
                                                    return <BattStateLineChart data={stateLineChartData} language={curLanguage} />
                                                } else {
                                                    return <div className="text-center p-4">{stateLineChartDataMessage}</div>
                                                }
                                            } else {
                                                return <CardSpinner />
                                            }
                                        }
                                    )()
                                }
                            </div>
                        </div>
                    </div>


                    {/* 顯示訊息視窗 */}
                    <AlertMsg
                        msgTitle={msgTitle}
                        open={openMsg}
                        handleClose={() => { this.setState({ openMsg: !openMsg }) }}
                    >
                        <div className='col-12 p-0 my-4'>
                            <div className='my-1'>
                                <Translation>{(t) => <>{t(message)}</>}</Translation>
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
            isRefresh: true, // 是否執行每五分鐘刷新
            isDisabledBtn: false, // 防止重複點擊
            dfZoom: 11,
            mapCenter: this.props.mapCenter,                            // 地圖預設中心點
            mapInfo: [],                                                // 地圖群組資訊
            IMPType: 20,                                                // 地圖內組名稱代碼(20,21,22)
            mapInfoError: '',                                           // 地圖錯誤訊息
            printPieChart: false,                                       // 列印圓餅圖
            printLineChart: false,                                      // 列印階梯圖
            stateChartData: {},                                         // 電池狀態
            stateLineChartData: {},                                     // 電池狀態變化
            stateChartDataMessage: '',                                  // 電池狀態錯誤訊息
            stateLineChartDataMessage: '',                              // 電池狀態變化錯誤訊息
            stateChart: false,                                          // 是否顯示圖餅圖
            stateLineChart: false,                                      // 是否顯示折線圖
            ApiKey: this.props.Key,                                     // 金鑰
            openMsg: false, // 顯示隱藏訊息視窗
            msgTitle: "", // 訊息視窗標題
            message: "", // 成功/錯誤訊息
            isFilterOpen: false, // 篩選
        });
    };
    resetFilterData = () => {//右邊篩選全部初始化
        this.props.resetAllHomeData();
        let postData = {
            isFilterCompanyData: initFilterSelectData,
            isFilterCountryData: initFilterSelectData,
            isFilterAreaData: initFilterSelectData,
            isFilterGroupIdData: initFilterSelectData,
        }
        this.submitFilterData(postData);
    }
    /* 取得總覽資料 */
    submitFilterData = (props) => {
        this.resetData();// reset
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 取得地圖群組資訊資料(POST)
        this.fetchMapInfo(postData).then((response) => {
            if (response.code === '00' && response.msg) {
                this.setState({
                    mapInfo: response.msg.Marker,
                    IMPType: response.msg.IMPType,
                    mapCenter: { lat: parseFloat(response.msg.Lat), lng: parseFloat(response.msg.Lng) },
                    dfZoom: response.msg.Zoom,
                    mapInfoError: '',
                })
            } else if (response.code === '07') {
                this.setState({
                    mapInfo: [],
                    mapCenter: { lat: parseFloat(25.0870915), lng: parseFloat(121.5549154) },
                    dfZoom: 11,
                    mapInfoError: response.msg
                })
            } else {
                console.error('地圖資訊', response)
                this.setState({
                    mapInfo: [],
                    mapCenter: { lat: parseFloat(25.0870915), lng: parseFloat(121.5549154) },
                    dfZoom: 11,
                    mapInfoError: response.msg
                })
            }
        })

        // get api 電池狀態
        this.getBattStateChartData(postData);

        // get api 電池狀態變化
        this.getBattStateLineChartData(postData);

        // change loding state
        this.setState({ loading: false });

    }


    // 取得電池狀態資料
    getBattStateChartData = (props) => {
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = props
        }

        // alert('refresh 電池狀態')
        this.fetchBattStateChartData(postData).then((response) => {
            if (response.code === '00' && response.msg) {
                this.setState({ stateChartData: response.msg, stateChartDataMessage: '', stateChart: true, })
            } else if (response.code === '07') {
                this.setState({ stateChartData: {}, stateChartDataMessage: response.msg, stateChart: true })
            } else {
                console.error('電池狀態資料', response)
                this.setState({ stateChartData: {}, stateChartDataMessage: response.msg, stateChart: false })
            }
        })
    }

    // 取得電池狀態變化資料
    getBattStateLineChartData = (props) => {
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = props
        }


        this.fetchBattStateLineChartData(postData).then((response) => {
            if (response.code === '00' && response.msg) {
                this.setState({ stateLineChartData: response.msg, stateLineChart: true })
            } else if (response.code === '07') {
                this.setState({ stateLineChartData: {}, stateLineChartDataMessage: response.msg, stateLineChart: true })
            } else {
                console.error('電池狀態變化資料', response)
                this.setState({ stateLineChartData: {}, stateLineChartDataMessage: response.msg, stateLineChart: false })
            }
        })
    }

    // PostData
    returnPostData = (state, type) => {
        const {isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,} = state;
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

    // 列印
    // 列印Pie圖表
    handlePieChartPrintEvent = async () => {
        await this.setState({
            printPieChart: true,        // 列印圓餅圖
            printLineChart: false,      // 列印階梯圖
        })
        await window.print();
    }
    // 列印Line圖表
    handleLineChartPrintEvent = async () => {
        await this.setState({
            printPieChart: false,       // 列印圓餅圖
            printLineChart: true,       // 列印階梯圖
        })
        await window.print();
    }


    /* 篩選，搜查 */
    //打開右邊篩選(顯示/隱藏篩選清單)
    setOpenIsFilter = (value) => {
        if (value === true) {
            this.setState({ isFilterOpen: value, isRefresh: false })
        } else {
            this.setState({ isFilterOpen: value, isRefresh: true })
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
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData } = FilterNames;
        switch (name) {
            case isFilterCompanyData:
                this.props.updateHomeCompany(object);
                break;
            case isFilterCountryData:
                this.props.updateHomeCountry(object);
                break;
            case isFilterAreaData:
                this.props.updateHomeArea(object);
                break;
            case isFilterGroupIdData:
                this.props.updateHomeGroupId(object);
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
            }
        }
        this.ajaxSaveFilter(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    isRefresh: false,
                    openMsg: true,//開啟訊息視窗
                    message: msg,//填寫訊息
                });
            } else {
                this.setState({
                    isRefresh: false,
                    openMsg: true,//開啟訊息視窗
                    message: msg,//填寫訊息
                });
            }
        });
    }


    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData } = FilterNames;
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
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
                this.props.updateHomeCompany(object);
                break;
            case isFilterCountryData:
                this.props.updateHomeCountry(object);
                break;
            case isFilterAreaData:
                this.props.updateHomeArea(object);
                break;
            case isFilterGroupIdData:
                this.props.updateHomeGroupId(object);
                break;
            default:
        }
        this.submitFilterData(postData);
    }


    // 清空訊息
    resetMessage = () => {
        const { message } = this.state;
        if (message !== '') { this.setState({ message: '' }) }
        return
    }
    // 變更頁面刷新狀態
    onIsRefreshChange = (value) => {
        this.setState({ isRefresh: value })
    }







    // get api 地圖群組資訊(pie)
    fetchMapInfo = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getMapInfo`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    }

    // get api 電池狀態
    fetchBattStateChartData = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getStatusNow`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    }

    // get api 電池狀態變化(line)
    fetchBattStateLineChartData = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getStatus`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    }

    //儲存篩選API(POST):
    ajaxSaveFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}saveFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //儲存篩選API(POST):
        //https://www.gtething.tw/battery/saveFilter
    }


    // 匯出excel檔(圓餅圖)pie
    exportStatusNowDataExcel = () => {
        this.fetchCheckStatusNowDataExcel().then(response => {
            if (response.code === '00') {
                // type=csv 輸出CSV檔
                const { token, curLanguage, timeZone, company } = this.props;
                const exportURL = `${apiUrl}getStatusNow`;
                const excelName = response.msg;
                const postData = this.returnPostData(this.props, 'csv');
                exportExcel(token, curLanguage, timeZone, company, exportURL, excelName, postData, () => {
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "1090",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                }, 'POST',()=>{
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "5003",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                })
            } else { console.error('匯出EXCEL', response.msg) }
        })
    }
    // type=check檢查有EXCEL無資料
    fetchCheckStatusNowDataExcel = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getStatusNow`;
        const postData = this.returnPostData(this.props, 'check');
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    }

    // 匯出excel檔(階梯圖line)
    exportStatusDataExcel = () => {
        this.fetchCheckStatusDataEXCEL().then(response => {
            if (response.code === '00') {
                // type=csv 輸出CSV檔
                const { token, curLanguage, timeZone, company } = this.props;
                const exportURL = `${apiUrl}getStatus?type=csv`;
                const excelName = response.msg;
                const postData = this.returnPostData(this.props, 'csv');
                exportExcel(token, curLanguage, timeZone, company, exportURL, excelName, postData, () => {
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "1090",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                }, 'POST',()=>{
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "5003",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                })
            } else {
                console.error('匯出EXCEL', response.msg)
            }
        })
    }
    // type=check檢查有EXCEL無資料
    fetchCheckStatusDataEXCEL = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getStatus`;
        const postData = this.returnPostData(this.props, 'check');
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    }
}



const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,
        company: state.LoginReducer.company,    //公司別
        curLanguage: state.LoginReducer.curLanguage,   //語系
        timeZone: state.LoginReducer.timeZone,  //時區
        mapCenter: state.LoginReducer.mapCenter,    //預設導覽地圖位置
        functionList: state.LoginReducer.functionList,  //功能選單
        Key: state.LoginReducer.Key,    //google map key
        data: state.LoginReducer.data,   //使用者資訊

        // 篩選條件
        isFilterCompanyData: state.HomeFilterReducer.isFilterCompanyData,
        isFilterCountryData: state.HomeFilterReducer.isFilterCountryData,
        isFilterAreaData: state.HomeFilterReducer.isFilterAreaData,
        isFilterGroupIdData: state.HomeFilterReducer.isFilterGroupIdData,
        isGroupIdListStatus: state.HomeFilterReducer.isGroupIdListStatus,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setButtonControlList: (data) => dispatch(setButtonControlList(data)),//電池數據按鈕權限清單

        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        // 篩選條件
        updateHomeCompany: (object) => dispatch(updateHomeCompany(object)),
        updateHomeCountry: (object) => dispatch(updateHomeCountry(object)),
        updateHomeArea: (object) => dispatch(updateHomeArea(object)),
        updateHomeGroupId: (object) => dispatch(updateHomeGroupId(object)),
        resetHomeCompany: () => dispatch(resetHomeCompany()),
        resetHomeCountry: () => dispatch(resetHomeCountry()),
        resetHomeArea: () => dispatch(resetHomeArea()),
        resetHomeGroupId: () => dispatch(resetHomeGroupId()),
        resetAllHomeData: () => dispatch(resetAllHomeData()),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Home);