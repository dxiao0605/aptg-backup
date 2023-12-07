import React, { Component, Fragment } from "react";
import { Link } from "react-router-dom";
import { connect } from "react-redux";
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import {setBattInternalIDAlert,setBattInternalID,updBattDataTheader,} from "../../actions/BattDataAction";
import {
    updateCompany, updateCountry, updateArea, updateGroupId,
    updateBatteryGroupIdBatt, updateBatteryStatus,
    resetAllBattData, resetCompany, resetCountry, resetArea, resetGroupId,
    resetBatteryGroupId, resetRecordTime, resetBatteryStatus, 
} from '../../actions/BattFilterAction';
import { updateGroupIdUnSolved,resetAllAlertUnsolved,updateGroupNameUnSolved,updateBatteryGroupIdUnSolved,resetRecordTimeUnSolved, } from '../../actions/AlertUnsolvedFilterAction';
import { setActiveNum } from '../../actions/CommandAction';
import { setSubmitKeyList } from '../../actions/CommandP1504Action';
import { apipath, ajax, locpath } from "../../utils/ajax";
import { exportExcel } from "../../utils/exportExcel";
import {
    setNewTableHeader,      //變更表格顯示隱藏欄位
    setNewSliderList,       //變更表格內(電壓、內阻...)滑桿展開/隱藏
    setNewCheckboxList,     //變更各別checkbox狀態
    setSelectedList,        //變更已選則清單(selectedList)
    setSelectAllSliderData, //變更checkbox全選(data有含有控制slider功能)
} from "../../components/CusTable/utils";
import { Translation } from "react-i18next"; // i18n
// 關鍵字
import CusSearchInput from '../../components/CusSearchInput';                   // 關鍵字搜查
import { filterArray } from '../../components/CusSearchInput/utils'; 			// 關鍵字搜尋整理資料
// 表格
import PageHeader from '../../components/PageHeader'; 							// PageHeader
import ToggleBtnBar from '../../components/CusTable/ToggleBtnBar'; 				// 表格欄位顯示隱藏
import BattDataTable from './BattDataTable';
// 訊息視窗
import AlertMsg from '../../components/AlertMsg'; 								// 顯示訊息視窗
// 篩選
import FilterDrawer from '../../components/FilterDrawer';                       // 右側篩選欄外框
import BattFilter from '../../components/BattFilter';                           // 右側篩選選單內容(核選清單)
import CusBookMark from '../../components/CusBookMark';                         // 儲存標籤
import { FilterNames, initFilterSelectData } from '../../components/BattFilter/InitDataFormat';//初始化格式
import FilterItemTag from '../../components/BattFilter/FilterItemTag';          // 篩選條件Button




const apiUrl = apipath();
class BattData extends Component {
	constructor(props) {
		super(props)
		this.state = {
			loading: true,
			isRefresh: true, 							// 是否執行每五分鐘刷新
			isDisabledBtn: false, 						// 防止重複點擊
            pageId: '1400_2',                           // 儲存篩選時使用funciton_id
			dialogId: '', 								// 選則顯示彈跳視窗(BA,BB,B3,B5...)
			keySearch: '', 								// 關鍵字搜尋
			tableHeader: this.props.battDataTheader, 	// 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
			resultData: [], 							// 關鍵字搜尋後資料
			data: [], 									// api傳回預設資料(無checkbox狀態)
            currentPage:1,                              // 目前表格顯示第幾頁(預設第一頁)
			IMPType: 20, 								// 判斷顯示IMPType名稱
			TaskId:[],									// 跳轉至電池管理/電池參數設定
			selectAll: false, 							// 電池數據表格全選
			selectedList: [], 							// 己選取電池數據項目清單
			battData: [], 								// 電池數據表格內容(含checkbox狀態)
			battDataErrorMsg: 'Loading...',
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
		const {isRefresh} = this.state;
		
        this.props.setTreeFunctionId('1400');//設定功能選單指項位置
		this.props.resetBatteryGroupId();	//預設清空電池組ID
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
		// 當isFilterBatteryGroupIdData更新時(電池歷史(2)返回時,不會更新資料問題)
		if(this.props.isFilterBatteryGroupIdData !== prevProps.isFilterBatteryGroupIdData){
            if (mounted) {this.submitFilterData();}
		}
		// 當curLanguage更新時做
		if (this.props.curLanguage !== prevProps.curLanguage) {
            if (mounted) {this.submitFilterData();}
		}
		return () => mounted = false;
	}


	componentWillUnmount() {
		this.resetData();// reset
		this.setState({TaskId:[]})
		this.props.resetCompany();
		clearInterval(this.intervalID);
	}





	render() {
		const { company } = this.props;
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryStatusData, isFilterBatteryGroupIdData,
        } = this.props;//篩選
		const {
			loading,
			isDisabledBtn,
			dialogId,	//顯示(批次)彈跳視窗(BA,BB,B3,B5,...)
			openMsg,	//顯示訊息視窗
			msgTitle,	//訊息視窗標題
			tableHeader,
            currentPage,// 目前表格顯示第幾頁(預設第一頁)
			IMPType,
			TaskId,		//跳轉至電池管理/電池參數設定
			selectAll,selectedList,
			resultData,battDataErrorMsg,
			message,
			// 篩選
			isFilterOpen, //開啟篩選視窗
		} = this.state;
		if (loading) { return ''}
		else {
			return (
				<Fragment>
					{/* 電池數據 */}
					<PageHeader
						title={<Translation>{(t) => <>{t("1400")}</>}</Translation>}
					/>

					<div className='container-fuild'>
						{/* 篩選 */}
						<div className='col-12 p-0 mt-4'>
							<div className='d-inline-block mr-2'>
								<div className='d-inline-block'>
									<Link to='/BattGroup' className='mr-2' onClick={()=>{this.redirectURLBattDataLayer1()}}>
										<i className='fas fa-long-arrow-alt-left' /> <Translation>{(t) => <>{t("1097")}</>}</Translation>
									</Link>
								</div>
								{/* 關鍵字搜索 */}
								<div className='d-inline-block'>
									<CusSearchInput placeholderName='1037' onClickEvent={(value)=>{this.getKeywordData(value)}} />
								</div>
							</div>
							{/* 右側篩選列表 */}
							<FilterDrawer
                                isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                                setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
								resetEvent={()=>{this.resetFilterTrash()}} //跳轉至電池數據第一層
                            >
                                <BattFilter
                                    functionId='1400_2'
                                    isFilterCompanyData={isFilterCompanyData}
                                    isFilterCountryData={isFilterCountryData}
                                    isFilterAreaData={isFilterAreaData}
                                    isFilterGroupIdData={isFilterGroupIdData}
                                    isFilterBatteryStatusData={isFilterBatteryStatusData}
                                    isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
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
                            <FilterItemTag
                                functionId='1400_2'
                                company={this.props.company}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterCountryData={isFilterCountryData}
                                isFilterAreaData={isFilterAreaData}
                                isFilterGroupIdData={isFilterGroupIdData}
								isFilterBatteryStatusData={isFilterBatteryStatusData}
                                isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                submitFilterItemTag={this.submitFilterItemTag}
                            />
						</div>

						<ToggleBtnBar company={company} list={tableHeader} onClickEvent={this.onToggleBtnChange} IMPType={IMPType} />

						{/* 電池數據表格(第二層) */}
						{!resultData || resultData.length <= 0 ? (
							<div className='text-center'>{battDataErrorMsg}</div>
						) : (
							<BattDataTable
								isDisabledBtn={isDisabledBtn} // 解決重複點擊問題
								buttonControlList={this.props.buttonControlList} // 電池數據按鈕權限清單
								selectAll={selectAll} // 電池數據表格全選
								tableHeader={tableHeader} // 表格欄位名稱
								data={resultData} // 電池數據表格
								IMPType={IMPType} // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
								selectedList={selectedList} // 已選取電池數據項目清單
								checkedBatchCmdItem={this.checkedBatchCmdItem} // 判斷下方下行指令顯示欄位(B3,B5僅BatteryID不為零時顯示)
								dialogId={dialogId} // 顯示彈跳視窗(BA,BB,B3,B5,Group 1416,Battery 1551,...)
								active={currentPage} // 目前表格顯示第幾頁
								getActive={this.getTableActive} // 取得目前表格為第幾頁
								setOpenDialog={this.setOpenDialog} // 變更顯示打開視窗(BB、BA...)
								onIsRefreshChange={this.onIsRefreshChange} // 變更頁面刷新狀態
								onSliderChange={this.onSliderChange} // 變更表格內(電壓、內阻...)滑桿展開/隱藏
								redirectURLHistory={this.redirectURLHistory}//跳轉至電池歷史(第二層)
								redirectURLAlert={this.redirectURLAlert} // 跳轉至告警(未解決)
								exportExcel={this.exportBattDataExcel} // 輸出電池數據表格
								onSelectAllChange={this.onSelectAllChange} // 變更表格checkbox全選
								cancelSelectedAll={this.cancelSelectedAll} // 取消全選
								onCheckboxChange={this.onCheckboxChange} // 變更各別checkbox狀態
								handleSubmit={this.handleSubmit} // 發送批次BA,BB,B3,B5,Group,Battery指令
							/>
						)}
					</div>

					{/* 顯示訊息視窗 */}
					<AlertMsg
						msgTitle={msgTitle}
						open={openMsg}
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
			dialogId: "", // 選則顯示彈跳視窗(批次)(BA,BB,...)
			keySearch: "", // 關鍵字搜尋
			resultData: [], // 關鍵字搜尋後資料
			data: [], // api傳回預設資料(無checkbox狀態)
            currentPage:1,// 目前表格顯示第幾頁(預設第一頁)
			IMPType: 20, // 判斷顯示IMPType名稱
			selectAll: false, // 電池數據表格全選
			selectedList: [], // 己選取電池數據項目清單
			battData: [], // api傳回預設資料(無checkbox狀態)
			battDataErrorMsg: "Loading...",
			openMsg: false, // 顯示隱藏訊息視窗
			msgTitle: "", // 訊息視窗標題
			message: "", // 成功/錯誤訊息
			isFilterOpen: false, // 篩選
		});
	};
	resetFilterData = () => {//右邊篩選全部初始化(公司,站台號碼不更變)
		// this.props.resetAllBattData();
		this.props.resetCountry();
		this.props.resetArea();
		this.props.resetBatteryStatus();
		this.props.resetBatteryGroupId();
        let postData = {
            // isFilterCompanyData: initFilterSelectData,
			isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: initFilterSelectData,
            isFilterAreaData: initFilterSelectData,
            // isFilterGroupIdData: initFilterSelectData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
            // isFilterGroupNameData: initFilterSelectData,
            // isFilterBatteryTypeData: initFilterSelectData,
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
		this.fetchBattData(postData).then((response) => {
			// get api 單一電池組
			if (response.code === "00" && response.msg) {
				const newData = [];
				response.msg.Battery.map((item) => {// 新增checked欄位，控制各checkbox狀態
					return newData.push({ ...item, checked: false, slider: true });
				});
				this.setState({
					loading: false,
					isRefresh: true, // 是否執行每五分鐘刷新
					data: response.msg.Battery,
					IMPType: response.msg.IMPType,
					resultData: [...newData],		//最後輸出資料
					battData: [...newData],			//api回傳原始資料
					battDataErrorMsg: "",
				})
			} else {
				this.setState({loading: false,isRefresh:true,IMPType: 20,data: [],resultData: [],battData: [],battDataErrorMsg: response.msg,});
			}
		})
	}
    returnPostData = (state, type) => {
        const {
            isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData, //isFilterGroupNameData, isFilterBatteryTypeData,
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
            // "GroupName": {
            //     "All": isFilterGroupNameData.isChecked ? "1" : "0",
            //     "List": [...isFilterGroupNameData.isDataList]
            // },
            "BatteryGroupId": {
                "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryGroupIdData.isDataList]
            },
			"Status": {
				"All": isFilterBatteryStatusData.isChecked ? "1" : "0",
				"List": [...isFilterBatteryStatusData.isDataList]
			},
            // "BatteryType": {
            //     "All": isFilterBatteryTypeData.isChecked ? "1" : "0",
            //     "List": [...isFilterBatteryTypeData.isDataList]
            // },
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
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData, //isFilterGroupNameData, isFilterBatteryTypeData,
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
            // case isFilterGroupNameData:
            //     this.props.updateGroupName(object);
            //     break;
            // case isFilterBatteryTypeData:
            //     this.props.updateBatteryType(object);
            //     break;
			case isFilterBatteryStatusData:
				this.props.updateBatteryStatus(object);
				break;
            case isFilterBatteryGroupIdData:
                this.props.updateBatteryGroupIdBatt(object);
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
                // "GroupName": { ...this.props.isFilterGroupNameData },
                // "BatteryType": { ...this.props.isFilterBatteryTypeData },
				"Status": { ...this.props.isFilterBatteryStatusData },
                "BatteryGroupId": { ...this.props.isFilterBatteryGroupIdData },
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
		const { battData } = this.state;
		const resTableData = filterArray({
            dataArray:battData,
            searchText:keyword,
            allowArray: ['Address','Area','BatteryGroupID','BatteryType','Company','Country','GroupID','GroupName','InstallDate','RecTime','StatusDesc','Temperature']
        })
		// console.log('resTableData',resTableData);
        this.setState({
            keySearch: keyword,
            resultData: resTableData,
            currentPage: 1,
        })
	}
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData, //isFilterGroupNameData, isFilterBatteryStatusData,
			isFilterBatteryStatusData, isFilterBatteryGroupIdData,
        } = FilterNames;
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterCountryData: this.props.isFilterCountryData,
            isFilterAreaData: this.props.isFilterAreaData,
            isFilterGroupIdData: this.props.isFilterGroupIdData,
            // isFilterGroupNameData: this.props.isFilterGroupNameData,
            // isFilterBatteryTypeData: this.props.isFilterBatteryTypeData,
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
			// case isFilterGroupNameData:
			// 	this.props.updateGroupName(object);
			// 	break;
			// case isFilterBatteryTypeData:
			// 	this.props.updateBatteryType(object);
			// 	break;
			case isFilterBatteryStatusData:
				this.props.updateBatteryStatus(object);
				break;
			case isFilterBatteryGroupIdData:
				this.props.updateBatteryGroupIdBatt(object);
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
		this.props.updBattDataTheader(newList);
	};
	// 變更表格內(電壓、內阻...)滑桿展開/隱藏
	onSliderChange = (e) => {
		const { battData } = this.state;
		const newData = setNewSliderList(battData, e.target.id);
		this.setState({
			resultData: newData,
			battData: newData,
		});
	};
	// 變更checkbox全選
	onSelectAllChange = (e,active,row,data) => {
        const newData = setSelectAllSliderData(data,e.target.checked,active,row);
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
			return newData.push({...item, checked: false, slider: true })
		})
		// 變更已選則清單(selectedList)
		const selectedList = setSelectedList(newData);
		this.setState({
			selectAll: false,
			resultData: newData,
			battData: newData,
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
			battData: newData,
			selectedList: selectedList,
		})
	};


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
	//電池數據篩選內垃圾桶功能(返回電池數據第一層)
	resetFilterTrash = () => {
		const str = locpath().toString();
		const newUrl = str.replace('BattData', 'BattGroup');
		this.props.resetAllBattData();
		window.location.href = newUrl;
	}
	// 跳轉至電池數據(第一層)
	redirectURLBattDataLayer1 = () => {
		this.props.resetAllBattData();
	}
	// 跳轉至電池歷史(第二層)
	redirectURLHistory = async(data) => {
		await this.props.setBattInternalID(data.battInternalId)
        const batteryGroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [data.battInternalId],
            isButtonList: [{Value:data.battInternalId,Label:data.batteryGroupId}],
        }
		// await this.props.setBattInternalID('');
        await this.props.updateBatteryGroupIdBatt(batteryGroupId);
		await this.props.resetRecordTime();
	}
	// 跳轉至告警(跳轉至告警未解決)(alertAction)
    redirectURLAlert = (data) => {
		const {groupName,groupLabel,batteryGroupId,battInternalID} = data; //groupId,
        const GroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [groupName],
            isButtonList: [{Value:groupName,Label:groupLabel}],
        }
        const BatteryGroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [battInternalID],
            isButtonList: [{Value:battInternalID,Label:batteryGroupId}],
        }
		// this.props.resetAllAlertUnsolved();
		this.props.resetRecordTimeUnSolved();						//重置告警未解決(數據時間)
		this.props.updateGroupIdUnSolved(GroupId);					//更新未解決站台編號
		this.props.setBattInternalIDAlert(battInternalID)			//設定電池組ID(表格顯示用)
		// this.props.setBattInternalID(battInternalID);				//設定電池組ID(表格顯示用)
        this.props.updateBatteryGroupIdUnSolved(BatteryGroupId);	//更新未解決電池組ID(篩選用)
    }
    // 跳轉至電池管理/電池參數設定
    redirectURLCommand = (data) => {
        // console.log('redirectURLCommand',data)
        this.props.setActiveNum(0);
        this.props.setSubmitKeyList(data);

    }

	/* 指令視窗 */
	// 判斷下方下行指令顯示欄位(B3,B5僅BatteryID為零時顯示)
	checkedBatchCmdItem = () => {
		const {selectedList} = this.state;
		// 判斷所選的項目batteryID為零的有幾筆
		const Id0Array = selectedList.filter( filterItem => filterItem.BatteryID === '0');
		return Id0Array.length
	}
	// 變更彈跳指令視窗(BA,BB,B3,B5,...)
	setOpenDialog = (value) => {
        if(value === ''){ this.setState({isRefresh:true})}
		this.setState({dialogId: value})
	}
	// 發送批次BA,BB,B3,B5,Group,Battery指令
	handleSubmit = async(code,list) => {
		//彈跳視窗顯示處理中
		await this.setState({dialogId:'',message: '1091',msgTitle:"1089",openMsg: true,isDisabledBtn:true,isRefresh:false})
		if (code === "186") {// 發送批次BA指令並回傳BASetting訊息
			await this.setState({msgTitle: '1558'})// 訊息標題(指令結果)
			await this.fetchBASetting(list).then((response) => {
				// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
				this.afterSubmitResponse(response);
			})
		} else if (code === "187") {// 發送批次BB指令並回傳BBSetting訊息
			await this.setState({msgTitle: '1558'})// 訊息標題(指令結果)
			await this.fetchBBSetting(list).then((response) => {
				// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
				this.afterSubmitResponse(response);
			})
		} else if (code === "179") {// 發送批次B3指令並回傳B3Setting訊息
			await this.setState({msgTitle: '1558'})// 訊息標題(指令結果)
			await this.fetchB3Setting(list).then((response) => {
				// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
				this.afterSubmitResponse(response);
			})
		} else if (code === "181") {// 發送批次B5指令並回傳B5Setting訊息
			await this.setState({msgTitle: '1558'})// 訊息標題(指令結果)
			await this.fetchB5Setting(list).then((response) => {
				// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
				this.afterSubmitResponse(response);
			})
		} else if (code === "1416") {// 變更站台設定清單
			const GroupId = {
				isOpen: true,
				isChecked: false,
				isDataList: [list.GroupInternalID],
				isButtonList:[{Value:list.GroupInternalID,Label:list.GroupLabel}],
			}
			await this.props.updateGroupId(GroupId);//變更為新的站台ID
			await this.setState({msgTitle:"1089"})// 訊息標題(處理結果)
			await this.fetchGroupSetting(list).then(async(response) => {
				if (response.code === "00") {
					await this.submitFilterData();
					await this.setState({
						TaskId: [],
						message: response.msg,
						openMsg: true,
						isDisabledBtn: false,
						isRefresh: true,
					});			
				} else {
					this.setState({
						TaskId: [],
						message: response.msg,
						openMsg: true,
						isDisabledBtn: false,
						isRefresh:true,
					});
				}
			})
		} else if (code === "1551") {// 變更電池組編輯清單
			let newList = {msg:list};
			await this.setState({msgTitle:"1089"})// 訊息標題(處理結果)
			await this.fetchBatterySetting(newList).then(async(response) => {
				if (response.code === "00") {
					await this.submitFilterData()
					await this.setState({
						TaskId: [],
						message: response.msg,
						msgTitle:"1089",
						openMsg: true,
						isDisabledBtn: false,
						isRefresh:true,
					});
				} else {
					this.setState({
						TaskId: [],
						message: response.msg,
						msgTitle:"1089",
						openMsg: true,
						isDisabledBtn: false,
						isRefresh:true,
					});
				}
			})
		}
	}
	// 送出指令後,取得跳轉電池管理/電池參數設定值,回傳訊息
	afterSubmitResponse = (response) => {
		if(response.msg.TaskId){
			this.setState({TaskId: response.msg.TaskId})
			this.redirectURLCommand(response.msg.TaskId)
		}else{
			this.setState({TaskId: []})
		}
		this.getMessage(response);
	}
	// 回傳訊息
	getMessage = (response) => {
		if(response.code === "00") {
			this.setState({message: response.msg.Message,openMsg: true,isDisabledBtn:false})
		}else{
			this.setState({message: response.msg,openMsg: true,isDisabledBtn:false})
		}
	}



	// get api 取得電池組ID(POST)
	fetchBattData = (postData) => {
		const {token, company, curLanguage, timeZone} = this.props;
		const url = `${apiUrl}getBattery`;
		// const postData = { Type: "" };
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData)
		/**
		 * Seq	序
		 * Company	公司
		 * Country	國家
		 * Area	地域
		 * GroupID	站台編號
		 * GroupName	站台名稱
		 * Address	地址
		 * IMPType	0: 內阻值 1:電導值 2:毫內阻
		 * BatteryGroupID	電池組ID
		 * InstallDate	安裝日期
		 * BatteryType	型號
		 * RecTime	數據更新時間
		 * IR	內阻
		 * Vol	電壓
		 * Temperature	溫度
		 * StatusCode	狀態代碼
		 * StatusDesc	狀態說明
		 */
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
	// api 變更B3設定清單(POST)
	fetchB3Setting = (postData) => {
		const { token, curLanguage, timeZone, company } = this.props;
		const url = `${apiUrl}addCorrectionIRTask`;
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	};
	// api 變更B5設定清單(POST)
	fetchB5Setting = (postData) => {
		const { token, curLanguage, timeZone, company } = this.props;
		const url = `${apiUrl}addCorrectionVolTask`;
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	};
	// api 變更站台編輯(POST)
	fetchGroupSetting = (postData) => {
		const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}updGroupSetup`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
	};
	// api 變更電池組編輯(POST)
	fetchBatterySetting = (postData) => {
		const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}updBattery`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
	};




	// 匯出EXCEL
	exportBattDataExcel = () => {
        const {
            isFilterCompanyData,isFilterCountryData,isFilterAreaData,
			isFilterGroupIdData,
			isFilterBatteryStatusData} = this.props;
		this.setState({
			openMsg:true, // 開啟訊息視窗
			message:"1091", // 填寫訊息
			msgTitle:"1089", // 訊息標題(處理結果)
			isDisabledBtn:true, // 防止重複點擊
		})
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
			Status: {
				All: isFilterBatteryStatusData.isChecked ? '1' : '0',
				List: isFilterBatteryStatusData.isDataList,
			},
            Type:'csv'
        }
		// 判斷是否有excel可執行下載
		this.fetchCheckBattDataEXCEL().then((response) => {
			if (response.code === "00") {// type=csv 輸出CSV檔
				const {token, curLanguage, timeZone, company} = this.props;
				const exportURL = `${apiUrl}getBattery`;
				const excelName = response.msg;
				exportExcel(token, curLanguage, timeZone, company, exportURL, excelName, postData, ()=>{
						this.setState({
							openMsg: true, // 開啟訊息視窗
							message: "1090", // 填寫訊息
							isDisabledBtn: false, // 防止重複點擊
						})
					},'POST',()=>{
						this.setState({
							openMsg: true,          // 開啟訊息視窗
							message: "timeout",        // 填寫訊息
							isDisabledBtn: false,   // 防止重複點擊
						})
					})
			} else {console.error("匯出EXCEL", response.msg)}
		})
	};
	// type=check檢查有EXCEL無資料
	fetchCheckBattDataEXCEL = () => {
		const {token,curLanguage,timeZone,company } = this.props;
		const url = `${apiUrl}getBattery`;
		const postData = { Type: "check" };
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	};



}



const mapStateToProps = (state, ownProps) => {
	return {
		token: state.LoginReducer.token,
		account: state.LoginReducer.account,
		username: state.LoginReducer.username,
		role: state.LoginReducer.role,
		company: state.LoginReducer.company,
		curLanguage: state.LoginReducer.curLanguage, //目前語系
		timeZone: state.LoginReducer.timeZone,
		functionList: state.LoginReducer.functionList,
		groupInternalId: state.BattDataReducer.groupInternalId, //電池數據(第一層)站台編號
		batteryGroupId: state.BattDataReducer.batteryGroupId, //電池數據(第二層)電池組ID
		buttonControlList: state.BattDataReducer.buttonControlList, //電池數據按鈕權限清單
		battDataTheader: state.BattDataReducer.battDataTheader, //電池數據(第一層)表格標題
		battTypeList: state.BattTypeListReducer.battTypeList,//電池類型清單
		    
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
		setBattInternalID: (value) => dispatch(setBattInternalID(value)),
		setBattInternalIDAlert: (value) => dispatch(setBattInternalIDAlert(value)),
		updBattDataTheader: (data) => dispatch(updBattDataTheader(data)),
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
        resetCompany: () => dispatch(resetCompany()),
        resetCountry: () => dispatch(resetCountry()),
        resetArea: () => dispatch(resetArea()),
        resetGroupId: () => dispatch(resetGroupId()),
        resetBatteryGroupId: () => dispatch(resetBatteryGroupId()),
		resetBatteryStatus: () => dispatch(resetBatteryStatus()),
		resetRecordTime: () => dispatch(resetRecordTime()),
        resetAllBattData: () => dispatch(resetAllBattData()),
		// 告警篩選條件
		updateGroupIdUnSolved: (object) => dispatch(updateGroupIdUnSolved(object)),
		updateGroupNameUnSolved: (object) => dispatch(updateGroupNameUnSolved(object)),
		updateBatteryGroupIdUnSolved: (object) => dispatch(updateBatteryGroupIdUnSolved(object)),
		resetAllAlertUnsolved: () => dispatch(resetAllAlertUnsolved()),
		resetRecordTimeUnSolved: () => dispatch(resetRecordTimeUnSolved()),
		// 電池管理/電池參數設定篩選條件
        setActiveNum: (value) => dispatch(setActiveNum(value)),
        setSubmitKeyList: (object) => dispatch(setSubmitKeyList(object)),
	};
};
export default connect(mapStateToProps, mapDispatchToProps)(BattData);