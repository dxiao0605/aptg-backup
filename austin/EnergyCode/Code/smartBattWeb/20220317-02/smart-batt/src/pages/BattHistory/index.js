import React,{Component, Fragment } from 'react';
import { connect } from 'react-redux';
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import {setBattInternalID} from '../../actions/BattDataAction';
import {
    updateBatteryGroupIdBatt, updateRecordTime,
    resetAllBattData, 
    resetBatteryGroupId, resetRecordTime,
} from '../../actions/BattFilterAction';
import {apipath,ajax,locpath} from '../../utils/ajax';
import {exportExcel} from '../../utils/exportExcel';
import {getChartDatas} from './utils';
import { Translation } from 'react-i18next';                          // i18n

import LocationMarkerMap from '../../components/LocationMarkerMap';         // 電池位置,無顯示詳細資訊(地圖)
// 表格
import BattGroupIDTable from './BattGroupIDTable';                          // 地圖左側資訊
import PageHeader from '../../components/PageHeader';                       // PageHeader
import DataTable from './DataTable';
import TabComponent from './TabComponent';                                  // (內阻、電壓、溫度)曲線
// 訊息視窗
import AlertMsg from '../../components/AlertMsg';                           // 顯示訊息視窗
// 篩選
import FilterDrawer from '../../components/FilterDrawer';                   // 右側篩選欄外框
import Filter from './Filter';                                              // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                     // 儲存標籤
import { FilterNames, initFilterSelectData, initFilterDate } from '../../components/BattFilter/InitDataFormat';//初始化格式
import FilterHistoryItemTag from '../../components/BattFilter/FilterHistoryItemTag';     // 篩選條件Button



const apiUrl = apipath();
class BattHistory extends Component {
    constructor(props){
        super(props)
        this.state={
            loading: true,
            isRefresh: true,
            isDisabledBtn: false,                                           // 防止重複點擊
            pageId: '1600_2',                                               // 儲存篩選時使用funciton_id
            battHistory:{},                                                 // 電池歷史全頁資訊(api Get)
            battHistoryErrorMsg:'loading...',
            tableHeader: [],
            openMsg: false,                                                 // 顯示隱藏訊息視窗
            msgTitle: '',                                                   // 訊息視窗標題
            message: '',                                                    // 成功訊息/錯誤訊息
            battHistoryRecord: [],                                          // 電池歷史下方表格資訊
            mapCenter:{lat: parseFloat(125),lng: parseFloat(23.25)},        // 下方地圖預設中心點
            mapInfo:[],                                                     // 下方地圖資訊(Maker)
            mapInfoError: 'loading...',
            // 電池歷史圖表(內阻、電壓、溫度)曲線
            chart: false,                                                   // 是否顯示圖表
            chartData: [],                                                  // 圖表(內阻,電壓,溫度)資訊
            IRChart:[],                                                     // 內阻圖表資訊
            VolChart:[],                                                    // 電壓圖表資訊
            TemperatureChart:[],                                            // 溫度圖表資訊
            MinVol: 0,
            MaxVol: 0,
            stepSizeVol: 0.2,
            MinIR: 0,
            MaxIR: 0,
            stepSizeIR: 1,
            MaxTemperature: 0,
            MinTemperature: 0,
            stepSizeTemperature: 1,
            RecTime:[],                                                     // 圖表時間(x軸)
            IMPType: 20,                                                    // 內阻名稱代碼
            chartErrorMessage: 'loading...',
            // 搜查時間(預設24小時)
            start: new Date(new Date().getTime() - (24 * 60 * 60 * 1000)),  // yesterday
            end: new Date(),
            // 篩選
			isFilterOpen: false,                        // 篩選
			isFilterSaveInput: '',                      // 儲存標籤 
        }
        
    }
    intervalID = 0;


    // React Lifecycle
    componentDidMount() {
        const {isRefresh} = this.state;
        this.props.setTreeFunctionId('1600');//設定功能選單指項位置
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
        if (this.props.curLanguage !== prevProps.curLanguage) {
            if(mounted){
                this.submitFilterData();
            }
        }
        if (this.props.battInternalId === '') {
            if(mounted){
                const batteryId = this.props.isFilterBatteryGroupIdData.isDataList[0];
                this.props.setBattInternalID(batteryId);
            }
        }
        return () => mounted = false;
    }


    componentWillUnmount() {
        this.resetData();            //清空欄位
        clearInterval(this.intervalID);
    }

    



    render() {
        const { perPage,ApiKey } = this.props;
        const { isFilterBatteryGroupIdData, isFilterRecordTimeData } = this.props;//篩選
        const { 
                loading,isDisabledBtn,
                // 電池歷史
                battHistory,battHistoryRecord,battHistoryErrorMsg,
                tableHeader,
                openMsg,msgTitle,message,
                // 地圖
                mapInfo,mapCenter,mapInfoError,
                // 圖表
                chart,chartData,IRChart,VolChart,TemperatureChart,RecTime,IMPType,chartErrorMessage,
                MaxVol,MinVol,stepSizeVol,
                MaxIR,MinIR,stepSizeIR,
                MaxTemperature,MinTemperature,stepSizeTemperature,
                // 篩選
                isFilterOpen,               //開啟篩選視窗
              } = this.state;
            if (loading) { return ''} // return <CusLoader />
            else {
            return (
                <Fragment>
                    {/* 電池歷史 */}
                    <PageHeader
						title={<Translation>{(t) => <>{t("1600")}</>}</Translation>}
					/> 
                    
                    <div className="container-fuild">
                        <div className="col-12 p-0 mt-4 print-hide">
                            {/* 篩選 */}
							<FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                                resetEvent={this.resetFilterTrash}    //清空所有欄位
                            >
                                <Filter
                                    functionId='1600_2' //判斷篩選清單顯示項目(電池歷史2層 1600_2)
                                    isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
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
                                <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={this.resetFilterTrash}>
                                    <Translation>{(t) => <>{t("1038")}</>}</Translation>
                                </button>
                            </div>
                            <FilterHistoryItemTag
                                company={this.props.company}
                                isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                isFilterRecordTimeData={isFilterRecordTimeData}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
                        </div>
    
    
                        {/* 圖表(內阻、電壓、溫度)曲線 */}
                        <TabComponent 
                            chart={chart}   //是否顯示圖表，如果chartData為空不顯示。
                            chartData={chartData} 
                            IRChart={IRChart} 
                            VolChart={VolChart} 
                            TemperatureChart={TemperatureChart}
                            MinVol={MinVol}
                            MaxVol={MaxVol}
                            stepSizeVol={stepSizeVol}
                            MinIR={MinIR}
                            MaxIR={MaxIR}
                            stepSizeIR={stepSizeIR}
                            MaxTemperature={MaxTemperature}
                            MinTemperature={MinTemperature}
                            stepSizeTemperature={stepSizeTemperature}
                            RecTime={RecTime} 
                            IMPType={IMPType}
                            handleCSVEvent={this.exportBattHistoryCSV}
                            handleExcelEvent={this.exportBattHistoryExcel} 
                            chartErrorMessage={chartErrorMessage} />
    
                        <div className="card col-12 pt-4 pb-4 pl-0 pr-0 text-center d-inline-block  print-hide">
                            <div className="col-12 col-md-4 align-top d-inline-block m-0 p-0">
                                <div className="col-12 align-top mb-2">{/* 地圖左側地圖資訊 */}
                                    {battHistory && <BattGroupIDTable battHistory={battHistory} />}
                                </div>
                                <div className="col-12 map">
                                    {   //判斷是否有地圖資訊
                                        (()=>{
                                            if(mapInfo.length <= 0){
                                                return <div className="text-center p-4">{mapInfoError}</div>
                                            }else{
                                                return <LocationMarkerMap data={mapInfo} ApiKey={ApiKey} mapCenter={mapCenter} dfZoom={18} mapHeight='40vh'/>
                                            }
                                        })()
                                    }
                                </div>
                            </div>
                            <div className="col-12 col-sm-8 align-top d-inline-block m-0 p-0">
                            {/* 地圖右側資訊 */}
                            {
                                battHistoryRecord.length <= 0
                                ? <div className="text-center">{battHistoryErrorMsg}</div>
                                : <DataTable 
                                    data={battHistoryRecord} 
                                    tableHeader={tableHeader}
                                    IMPType={IMPType}
                                    perPage={perPage}
                                    setBatteryGroupID={this.props.setBatteryGroupID}    //取得電池組ID
                                    exportExcel={this.exportBattHistoryExcel} />
                            }
                            </div>
                        </div>
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
                                    </div>
                                </div>
                            </AlertMsg>
                        )
                    }

                </Fragment>
            )
        }
    }





    // 清空欄位
    resetData = () => {
        this.setState({
            loading: true,
            isRefresh: true,
            isDisabledBtn: false,                                           // 防止重複點擊
            battHistory:{},
            battHistoryRecord: [],
            mapInfo: [],
            mapCenter:{lat: parseFloat(125),lng: parseFloat(23.25)},
            mapInfoError: 'loading...',
            battHistoryErrorMsg: 'loading...',
            openMsg: false,                                                 // 顯示隱藏訊息視窗
            msgTitle: '',                                                   // 訊息視窗標題
            message: '',                                                    // 成功訊息/錯誤訊息
            chart: false,                                                   // 電池歷史圖表資訊(內組、電壓、溫度)
            chartData: {},
            IRChart:[],
            VolChart:[],
            TemperatureChart:[],
            MinVol: 0,
            MaxVol: 0,
            stepSizeVol: 0.2,
            MinIR: 0,
            MaxIR: 0,
            stepSizeIR: 1,
            MaxTemperature: 0,
            MinTemperature: 0,
            stepSizeTemperature: 1,
            RecTime:[],
            IMPType:20,
            chartErrorMessage:'loading...',
            isFilterOpen: false,                                            // 篩選
        })
    }
    
    // 變更頁面刷新狀態
    onIsRefreshChange = (value) => {
        this.setState({isRefresh: value})
    }
    //右邊篩選全部初始化
    resetFilterData = () => {
        this.props.resetAllBattData();
        let postData = {
            isFilterBatteryGroupIdData: initFilterSelectData,
            isFilterRecordTimeData: initFilterDate,
        }
        this.submitFilterData(postData);
    }
    //電池數據篩選內垃圾桶功能(返回電池數據第一層)
	resetFilterTrash = () => {
		const str = locpath().toString();
		const newUrl = str.replace('BattHistory', 'BattHistoryManage');
		this.props.resetAllBattData();
		window.location.href = newUrl;
	}
    // 取得電池歷史
    submitFilterData = async(props) => {
        await this.resetData();// reset
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // get api 電池歷史
        await this.fetchBattHistory(postData).then((response) => {
            if(response.code === '00' && response.msg) {
                this.setState({
                    loading: false,
                    isRefresh: true,
                    isDisabledBtn: false,
                    // 電池歷史資訊
                    battHistory: response.msg.Battery,
                    battHistoryRecord: response.msg.Battery.Record,
                    battHistoryErrorMsg: '',
                    tableHeader:[
                        {id:'1604',sortName:'RecTime',active:true},
                        {id: `${response.msg.Battery.IMPType}`, sortName:'',active:true},
                        {id:'1017',sortName:'',active:true},
                        {id:'1018',sortName:'Temperature',active:true},
                        {id:'1021',sortName:'StatusCode',active:true},
                    ],
                    IMPType: response.msg.Battery.IMPType,
                    // 地圖資訊
                    mapInfo: [{
                        Lat: response.msg.Battery.Lat,
                        Lng: response.msg.Battery.Lng,
                        Seq: 1,
                        Type: "Battery"
                    }],
                    mapInfoError: '',
                    mapCenter:{
                        lat: parseFloat(response.msg.Battery.Lat),
                        lng: parseFloat(response.msg.Battery.Lng),
                    },
                })
            }else {
                this.setState({
                    loading: false,
                    isRefresh: true,
                    isDisabledBtn: false,
                    battHistory:{},
                    battHistoryRecord: [],
                    tableHeader:[
                        {id:'1604',sortName:'RecTime'},
                        {id: '20', sortName:''},
                        {id:'1017',sortName:''},
                        {id:'1018',sortName:'Temperature'},
                        {id:'1021',sortName:'StatusCode'},
                    ],
                    IMPType: 20,
                    mapInfo: [],
                    mapInfoError: response.msg,
                    battHistoryErrorMsg: response.msg,
                    // chartErrorMessage: response.msg,
                })
            }
        })
        
        // get chart api 電池歷史圖表
        await this.fetchBattHistoryChart(postData).then( (response) => {
            const { curLanguage } = this.props;
            if(response.code === '00') {
                const {IR,Vol,Temperature} = response.msg;

                // 資料讀取成功並整理數值
                const data = getChartDatas(IR,Vol,Temperature,curLanguage);
                const minVol_decimals = response.msg.MinVol - Math.floor(response.msg.MinVol);
                const minVol = Math.floor(response.msg.MinVol) + Number((Math.floor(minVol_decimals*10)/10).toFixed(2));
                const maxVol = Number((Math.ceil(response.msg.MaxVol*20)/20).toFixed(1));
                const minIR = Math.floor(response.msg.MinIR) ;
                // const minIR = Math.floor(response.msg.MinIR / 5) * 5;
                const maxIR = Math.ceil(response.msg.MaxIR) ;
                // const maxIR = Math.ceil(response.msg.MaxIR / 5) * 5;
                const minTemperature = Math.floor(response.msg.MinTemperature);
                // const minTemperature = Math.floor(response.msg.MinTemperature / 5) * 5;
                const maxTemperature = Math.ceil(response.msg.MaxTemperature);
                // const maxTemperature = Math.ceil(response.msg.MaxTemperature / 5) * 5;


                this.setState({// 電池歷史圖表資訊(內組、電壓、溫度)
                    isRefresh: true,
                    chart: true,
                    chartData: response.msg,
                    IRChart: data.IRChart,
                    VolChart: data.VolChart,
                    TemperatureChart: data.TemperatureChart,
                    MinVol: parseFloat(minVol), //response.msg.MinVol,
                    MaxVol: parseFloat(maxVol), //response.msg.MaxVol,
                    stepSizeVol: Math.ceil((maxVol - minVol) / 5 * 10) / 10 ,
                    MinIR: minIR,
                    MaxIR: maxIR,
                    stepSizeIR: Math.ceil( ((maxIR - minIR) / 5) ),
                    MaxTemperature: maxTemperature,
                    MinTemperature: minTemperature,
                    stepSizeTemperature: Math.ceil( ((maxTemperature - minTemperature) / 5) ) ,
                    RecTime: response.msg.RecTime,
                    chartErrorMessage:'',
                })
            }else {
                this.setState({// 電池歷史圖表資訊(內組、電壓、溫度)
                    isRefresh: true,
                    chart: false,
                    chartData: {},
                    IRChart:[],VolChart:[],TemperatureChart:[],RecTime:[],
                    MinVol: 0,
                    MaxVol: 0,
                    stepSizeVol: 0.2,
                    MinIR: 0,
                    MaxIR: 0,
                    stepSizeIR: 1,
                    MaxTemperature: 0,
                    MinTemperature: 0,
                    stepSizeTemperature: 1,
                    chartErrorMessage:response.msg,
                })
            }
        })
    }
    returnPostData = (state, type) => {
        const {battInternalId} = this.props;
        const {isFilterBatteryGroupIdData, isFilterRecordTimeData,} = state;
        return {
            "BatteryGroupId": {
                "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryGroupIdData.isDataList]
            },
            "RecTime": { ...isFilterRecordTimeData },
            "BattInternalId": battInternalId,  //第一層超連結到第二層使用
            "Type": type
        }
    }




    /* 篩選，搜查 */
    //打開右邊篩選(顯示/隱藏篩選清單)
    setOpenIsFilter = (value) => {
        if(value === true) {
            this.setState({isFilterOpen: value})
            this.onIsRefreshChange(false)
        }else{
            this.setState({isFilterOpen: value})
            this.onIsRefreshChange(true)
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
        const { isFilterBatteryGroupIdData, isFilterRecordTimeData } = FilterNames;
        switch (name) {
            case isFilterBatteryGroupIdData:
                this.props.updateBatteryGroupIdBatt(object);
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
                "BatteryGroupId": { ...this.props.isFilterBatteryGroupIdData },
                "RecTime": { ...this.props.isFilterRecordTimeData },
            },
        }
        this.ajaxSaveFilter(postData).then((response) => {
            // if (this.ajaxCancel) { return; }//強制結束頁面
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
        const { isFilterBatteryGroupIdData, isFilterRecordTimeData } = FilterNames;
        if (name === isFilterRecordTimeData && isFilterRecordTimeData.Radio === "0") { return; }
        let postData = {
            isFilterBatteryGroupIdData: this.props.isFilterBatteryGroupIdData,
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
                case isFilterBatteryGroupIdData:
                    this.props.updateBatteryGroupIdBatt(object);
                    break;
                case isFilterRecordTimeData:
                    this.props.updateRecordTime(object);
                    break;
                default:
            }
        }
        this.submitFilterData(postData);
    }





    // get api 取得電池歷史
	fetchBattHistory = (postData) => {
        const {token,curLanguage,timeZone,company} = this.props;
        const url = `${apiUrl}getBatteryHistory`;
        return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
    }
    // get api 取得電池歷史圖表資料
    fetchBattHistoryChart = () => {
        const {token,curLanguage,timeZone,company,battInternalId} = this.props;
        const {isFilterRecordTimeData} = this.props;
        
        const postData = {
            Type:"",
            BattInternalId: battInternalId,
            RecTime: isFilterRecordTimeData
        };
        const url = `${apiUrl}getBatteryHistoryChart`;
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

    // 匯出excel檔(歷史圖表)
    exportBattHistoryExcel = () => {
        const {isFilterBatteryGroupIdData,isFilterRecordTimeData,battInternalId} = this.props;
        const postData = {
            "BatteryGroupId": {
                "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryGroupIdData.isDataList]
            },
            "RecTime": { ...isFilterRecordTimeData },
            "BattInternalId": battInternalId,  //第一層超連結到第二層使用
            "Type": 'excelcheck'
        };
        this.setState({
            openMsg: true,                      // 開啟訊息視窗
            message: "1091",                    // 填寫訊息
            msgTitle:"1089",                    // 訊息標題(處理結果)
            isDisabledBtn: true,                // 防止重複點擊
        })

        this.fetchCheckBattHistoryEXCEL(postData).then(response => {
            const {token,curLanguage,timeZone,company} = this.props;
            // const {isFilterRecordTimeData} = this.props;
            if(response.code === '00') {
                const exportURL = `${apiUrl}getBatteryHistory`;
                const excelName = response.msg;
                const postData = {
                    "BatteryGroupId": {
                        "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                        "List": [...isFilterBatteryGroupIdData.isDataList]
                    },
                    "RecTime": { ...isFilterRecordTimeData },
                    "BattInternalId": battInternalId,  //第一層超連結到第二層使用
                    "Type": "excel",
                };
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
            }else{
                console.error('匯出EXCEL',response.msg)
            }
        })
    }
    // 匯出CSV檔(歷史圖表)
    exportBattHistoryCSV = () => {
        // console.log('CSV')
        const {isFilterBatteryGroupIdData,isFilterRecordTimeData,battInternalId} = this.props;
        const postData = {
            "BatteryGroupId": {
                "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryGroupIdData.isDataList]
            },
            "RecTime": { ...isFilterRecordTimeData },
            "BattInternalId": battInternalId,  //第一層超連結到第二層使用
            "Type": 'csvcheck'
        };
        this.setState({
            openMsg: true,                      // 開啟訊息視窗
            message: "1091",                    // 填寫訊息
            msgTitle:"1089",                    // 訊息標題(處理結果)
            isDisabledBtn: true,                // 防止重複點擊
        })

        this.fetchCheckBattHistoryEXCEL(postData).then(response => {
            const {token,curLanguage,timeZone,company} = this.props;
            // const {isFilterRecordTimeData} = this.props;
            if(response.code === '00') {
                const exportURL = `${apiUrl}getBatteryHistory`;
                const excelName = response.msg;
                const postData = {
                    "BatteryGroupId": {
                        "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                        "List": [...isFilterBatteryGroupIdData.isDataList]
                    },
                    "RecTime": { ...isFilterRecordTimeData },
                    "BattInternalId": battInternalId,  //第一層超連結到第二層使用
                    "Type": "csv",
                };
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
            }else{
                console.error('匯出EXCEL',response.msg)
            }
        })
    }
    // type=excelcheck檢查有EXCEL無資料,取得EXCEL檔名;type=csvcheck檢查有CSV無資料,取得CSV檔名
    fetchCheckBattHistoryEXCEL = (postData) => {
        const {token,curLanguage,timeZone,company} = this.props;
        const url = `${apiUrl}getBatteryHistory`;
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
        curLanguage: state.LoginReducer.curLanguage,             //目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        perPage: state.LoginReducer.perPage,
        ApiKey: state.LoginReducer.Key,                         //地圖金鑰
        groupInternalId: state.BattDataReducer.groupInternalId, //電池數據(第一層)站台編號
        batteryGroupId: state.BattDataReducer.batteryGroupId,   //電池數據(第二層)電池組ID(battHistory)
        battInternalId: state.BattDataReducer.battInternalId, //跳轉至電池歷史(2)
        // 篩選條件
        isFilterBatteryGroupIdData: state.BattFilterReducer.isFilterBatteryGroupIdData,
        isFilterRecordTimeData: state.BattFilterReducer.isFilterRecordTimeData,

    }
}
const mapDispatchToProps = (dispatch,ownProps) => {
    return {
        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        // 篩選條件
        updateBatteryGroupIdBatt: (object) => dispatch(updateBatteryGroupIdBatt(object)),
        setBattInternalID: (value) => dispatch(setBattInternalID(value)), 
        updateRecordTime: (object) => dispatch(updateRecordTime(object)),
        resetBatteryGroupId: () => dispatch(resetBatteryGroupId()),
        resetRecordTime: () => dispatch(resetRecordTime()),
        resetAllBattData: () => dispatch(resetAllBattData()),
    }
}
export default connect(mapStateToProps,mapDispatchToProps)(BattHistory);