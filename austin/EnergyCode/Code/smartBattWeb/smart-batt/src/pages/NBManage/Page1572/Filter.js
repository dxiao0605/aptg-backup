import React, { Component, Fragment } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { Trans } from "react-i18next";

import { ajaxDelFilter, ajaxGetFilter, ajaxGetNBGroupHisFilter} from "../../../components/FilterDrawer/utils"; //整理checkbox狀態
import { checkObject, getSelectObj } from "../../../components/FilterDrawer/utils"; //整理checkbox狀態
import { FilterNames,FilterNames_API,initFilterSelectData,initFilterDate, } from "./InitDataFormat"; //初始化格式

import Divider from "@material-ui/core/Divider";
import FilterItemSelect from "../../../components/FilterDrawer/FilterItemSelect"; //每筆Item的Select
import FilterItemRadio from "../../../components/FilterDrawer/FilterItemRadio"; //每筆Item的Radio
import FilterSave from "./FilterSave"; //儲存標籤紀錄
import FilterItemHeader from "../../../components/FilterDrawer/FilterItemHeader"; //每筆Item的header
import { CusMainBtnStyle } from "../../../components/CusMainBtnStyle";

class Filter extends Component {
	ajaxCancel = false; //判斷ajax是否取消
	constructor(props) {
		super(props);
		this.state = {
			isFilterSelectTable: [], //選單清單
			isFilterCompanyList: [], //預設公司清單
			isFilterCountryList: [], //預設國家清單
			isFilterAreaList: [], //預設地域清單
			isFilterGroupIdList: [], //預設站台編號清單
			isFilterPreviousNBIDList: [], //預設(接續)通訊序號清單
			isFilterStartTimeList: [
				//預設(接續)開始時間
				{ Value: "0", Label: "1073", Type: "String", show: false },
				{ Value: "1", Label: "", Type: "Date", show: true },
				{ Value: "2", Label: "1083", Type: "String", show: false },
				{ Value: "3", Label: "1109", Type: "String", show: true },
				{ Value: "4", Label: "1110", Type: "String", show: false },
			],
			isFilterCompanyData: {
				//預設公司
				...initFilterSelectData,
			},
			isFilterCountryData: {
				////預設國家
				...initFilterSelectData,
			},
			isFilterAreaData: {
				//預設地域
				...initFilterSelectData,
			},
			isFilterGroupIdData: {
				//預設站台編號
				...initFilterSelectData,
			},
			isFilterPreviousNBIDData: {
				//預設(接續)通訊序號
				...initFilterSelectData,
			},
			isFilterStartTimeData: {
				//預設(接續)開始時間
				...initFilterDate,
			},
			isFilterTotal: 1, //總數
			defRadioRangeMaxDay: "31", //往未來多少天
			defRadioRangeMinDay: "31", //往從前多少天
		};
	}
	componentDidMount() {
		const {
			isFilterCompanyData,
			isFilterCountryData,
			isFilterAreaData,
			isFilterGroupIdData,
			isFilterPreviousNBIDData,
			isFilterStartTimeData,
		} = this.props;
		this.getNBHistoryFilter(
			isFilterCompanyData,
			isFilterCountryData,
			isFilterAreaData,
			isFilterGroupIdData,
			isFilterPreviousNBIDData,
		); //取選單資料
		this.getFilter(); //篩選儲存清單API(GET)
		this.setState({
			isFilterStartTimeData: isFilterStartTimeData,
		});
	}
	componentDidUpdate(prevProps, prevState) {
		if (
			//更新全部的數字
			prevState.isFilterCompanyData.isDataList.length !==
				this.state.isFilterCompanyData.isDataList.length ||
			prevState.isFilterCountryData.isDataList.length !==
				this.state.isFilterCountryData.isDataList.length ||
			prevState.isFilterAreaData.isDataList.length !==
				this.state.isFilterAreaData.isDataList.length ||
			prevState.isFilterGroupIdData.isDataList.length !==
				this.state.isFilterGroupIdData.isDataList.length ||
			prevState.isFilterPreviousNBIDData.isDataList.length !==
				this.state.isFilterPreviousNBIDData.isDataList.length 
		) {
			const list = [
				this.state.isFilterCompanyData.isDataList.length,
				this.state.isFilterCountryData.isDataList.length,
				this.state.isFilterAreaData.isDataList.length,
				this.state.isFilterGroupIdData.isDataList.length,
				this.state.isFilterPreviousNBIDData.isDataList.length,
			];
			let total = 1;
			list.forEach((num) => {
				total = total + num;
			});
			this.setState({
				isFilterTotal: total,
			});
		}
		if (
			prevState.isFilterCompanyData.isChecked !==
			this.state.isFilterCompanyData.isChecked
		) {
			this.setState({
				isFilterCompanyData: this.state.isFilterCompanyData,
			});
		}
		if (
			prevState.isFilterCountryData.isChecked !==
			this.state.isFilterCountryData.isChecked
		) {
			this.setState({
				isFilterCountryData: this.state.isFilterCountryData,
			});
		}
		if (
			prevState.isFilterAreaData.isChecked !==
			this.state.isFilterAreaData.isChecked
		) {
			this.setState({
				isFilterAreaData: this.state.isFilterAreaData,
			});
		}
		if (
			prevState.isFilterGroupIdData.isChecked !==
			this.state.isFilterGroupIdData.isChecked
		) {
			this.setState({
				isFilterGroupIdData: this.state.isFilterGroupIdData,
			});
		}
		if (
			prevState.isFilterPreviousNBIDData.isChecked !==
			this.state.isFilterPreviousNBIDData.isChecked
		) {
			this.setState({
				isFilterPreviousNBIDData: this.state.isFilterPreviousNBIDData,
			});
		}
	}
	componentWillUnmount() {
		this.ajaxCancel = true;
		this.setState = (state, callback) => {
			return;
		};
	}

	render() {
		const { isFilterTotal, isFilterSaveList } = this.state;
		const { isFilterCompanyList, isFilterCompanyData } = this.state; //公司
		const { isFilterCountryList, isFilterCountryData } = this.state; //國家
		const { isFilterAreaList, isFilterAreaData } = this.state; //地域
		const { isFilterGroupIdList, isFilterGroupIdData } = this.state; //站台編號
		const { isFilterPreviousNBIDList, isFilterPreviousNBIDData } = this.state; //(接續)通訊序號
		const { isFilterStartTimeList, isFilterStartTimeData } = this.state; //接續開始時間
		return (
			<Fragment>
				<div className='form-inline align-items-center justify-content-center pt-1 pb-2'>
					<i className='fas fa-filter mr-1' />
					<Trans i18nKey={this.props.functionId} />
					{/* 總計 */}
					<span className='filter_prompt ml-1 noDetail'>{isFilterTotal}</span>
				</div>
				<Divider />
				{/* 紀錄 */}
				<FilterSave
					company={this.props.company}
					list={isFilterSaveList}
					onClickBookmark={(FilterConfig) => {
						this.onClickBookmark(FilterConfig);
					}}
					onClickTrash={(FilterID) => {
						this.delFilter(FilterID);
					}}
				/>
				<Divider />
				{/* 篩選器 */}
				<ul className={`filter overflowY mb-0 ${navigator.userAgent.indexOf('Windows') > 0 ? 'Windows' : 'Mac'}`}>
					{this.props.company === "1" && (
						<li>
							{/* 公司 */}
							<FilterItemHeader
								title={"1064"}
								total={isFilterCompanyData.isDataList.length}
								detailList={isFilterCompanyData.isButtonList}
								isChecked={isFilterCompanyData.isChecked}
								isOpen={isFilterCompanyData.isOpen}
								onClickAllSelectList={(boolean) =>
									this.onClickAllSelectList("isFilterCompanyList", boolean)
								}
								onUpdateChainSelectList={() =>
									this.onUpdateChainSelectList(
										"CompanyValue",
										getSelectObj(isFilterCompanyList)
									)
								}
								onUpdateList={(object) =>
									this.onUpdateList(FilterNames.isFilterCompanyData, object)
								}>
								<FilterItemSelect
									defSelectVal={isFilterCompanyData.isDataList}
									defSelectList={isFilterCompanyList}
									onUpdateSelectList={(list) =>
										this.onUpdateSelectList("isFilterCompanyList", list)
									}
									onUpdateChainSelectList={(object) =>
										this.onUpdateChainSelectList("CompanyValue", object)
									}
									onUpdateList={(object) =>
										this.onUpdateList(FilterNames.isFilterCompanyData, object)
									}
								/>
							</FilterItemHeader>
						</li>
					)}

					<li>
						{/* 國家 */}
						<FilterItemHeader
							title={"1028"}
							total={isFilterCountryData.isDataList.length}
							detailList={isFilterCountryData.isButtonList}
							isChecked={isFilterCountryData.isChecked}
							isOpen={isFilterCountryData.isOpen}
							onClickAllSelectList={(boolean) =>
								this.onClickAllSelectList("isFilterCountryList", boolean)
							}
							onUpdateChainSelectList={() =>
								this.onUpdateChainSelectList(
									FilterNames_API.isFilterCountryVirtue,
									getSelectObj(isFilterCountryList)
								)
							}
							onUpdateList={(object) =>
								this.onUpdateList(FilterNames.isFilterCountryData, object)
							}>
							<FilterItemSelect
								defSelectVal={isFilterCountryData.isDataList}
								defSelectList={isFilterCountryList}
								onUpdateSelectList={(list) =>
									this.onUpdateSelectList("isFilterCountryList", list)
								}
								onUpdateChainSelectList={(object) =>
									this.onUpdateChainSelectList(
										FilterNames_API.isFilterCountryVirtue,
										object
									)
								}
								onUpdateList={(object) =>
									this.onUpdateList(FilterNames.isFilterCountryData, object)
								}
							/>
						</FilterItemHeader>
					</li>
					<li>
						{/* 地域 */}
						<FilterItemHeader
							title={"1029"}
							total={isFilterAreaData.isDataList.length}
							detailList={isFilterAreaData.isButtonList}
							isChecked={isFilterAreaData.isChecked}
							isOpen={isFilterAreaData.isOpen}
							onClickAllSelectList={(boolean) =>
								this.onClickAllSelectList("isFilterAreaList", boolean)
							}
							onUpdateChainSelectList={() =>
								this.onUpdateChainSelectList(
									FilterNames_API.isFilterAreaVirtue,
									getSelectObj(isFilterAreaList)
								)
							}
							onUpdateList={(object) =>
								this.onUpdateList(FilterNames.isFilterAreaData, object)
							}>
							<FilterItemSelect
								defSelectVal={isFilterAreaData.isDataList}
								defSelectList={isFilterAreaList}
								onUpdateSelectList={(list) =>
									this.onUpdateSelectList("isFilterAreaList", list)
								}
								onUpdateChainSelectList={(object) =>
									this.onUpdateChainSelectList(
										FilterNames_API.isFilterAreaVirtue,
										object
									)
								}
								onUpdateList={(object) =>
									this.onUpdateList(FilterNames.isFilterAreaData, object)
								}
							/>
						</FilterItemHeader>
					</li>

					<li>
						{/* 站台編號/名稱 */}
						<FilterItemHeader
							title={"1125"}
							total={isFilterGroupIdData.isDataList.length}
							detailList={isFilterGroupIdData.isButtonList}
							isChecked={isFilterGroupIdData.isChecked}
							isOpen={isFilterGroupIdData.isOpen}
							onClickAllSelectList={(boolean) =>
								this.onClickAllSelectList("isFilterGroupIdList", boolean)
							}
							onUpdateList={(object) =>
								this.onUpdateList(FilterNames.isFilterGroupIdData, object)
							}>
							<FilterItemSelect
								defSelectVal={isFilterGroupIdData.isDataList}
								defSelectList={isFilterGroupIdList}
								onUpdateSelectList={(list) =>
									this.onUpdateSelectList("isFilterGroupIdList", list)
								}
								onUpdateList={(object) =>
									this.onUpdateList(FilterNames.isFilterGroupIdData, object)
								}
							/>
						</FilterItemHeader>
					</li>

					<li>
						{/* (接續)通訊序號 */}
						<FilterItemHeader
							title={"1573"}
							total={isFilterPreviousNBIDData.isDataList.length}
							detailList={isFilterPreviousNBIDData.isButtonList}
							isChecked={isFilterPreviousNBIDData.isChecked}
							isOpen={isFilterPreviousNBIDData.isOpen}
							onClickAllSelectList={(boolean) =>
								this.onClickAllSelectList("isFilterPreviousNBIDList", boolean)
							}
							onUpdateChainSelectList={() =>
								this.onUpdateChainSelectList(
									"NBIDValue",
									getSelectObj(isFilterPreviousNBIDList)
								)
							}
							onUpdateList={(object) =>
								this.onUpdateList(FilterNames.isFilterPreviousNBIDData, object)
							}>
							<FilterItemSelect
								defSelectVal={isFilterPreviousNBIDData.isDataList}
								defSelectList={isFilterPreviousNBIDList}
								onUpdateSelectList={(list) =>
									this.onUpdateSelectList("isFilterPreviousNBIDList", list)
								}
								onUpdateChainSelectList={(object) =>
									this.onUpdateChainSelectList("NBIDValue", object)
								}
								onUpdateList={(object) =>
									this.onUpdateList(FilterNames.isFilterPreviousNBIDData, object)
								}
							/>
						</FilterItemHeader>
					</li>
					<li>
						{/* 接續開始時間 */}
						<FilterItemHeader
							title={"1574"}
							total={1}
							isChecked={false}
							showCheckBox={false}
							isOpen={true}>
							<FilterItemRadio
								defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
								defRadioRangeMinDay={this.state.defRadioRangeMinDay}
								defRadioVal={isFilterStartTimeData.Radio}
								defRadioStart={isFilterStartTimeData.Start}
								defRadioEnd={isFilterStartTimeData.End}
								defRadioList={isFilterStartTimeList}
								defRadioOpenStartTime={true}
								defRadioStartHH={isFilterStartTimeData.StartHH}
								defRadioStartMM={isFilterStartTimeData.StartMM}
								defRadioEndHH={isFilterStartTimeData.EndHH}
								defRadioEndMM={isFilterStartTimeData.EndMM}
								onUpdateList={(object) =>
									this.onUpdateList(FilterNames.isFilterStartTimeData, object)
								}
							/>
						</FilterItemHeader>
					</li>
					<Divider />
					<div className='d-block py-3 px-4'>
						<CusMainBtnStyle
							name={<Trans i18nKey={"1002"} />}
							icon={"fas fa-check mr-2"}
							fullWidth={true}
							clickEvent={() => this.handleSumbit()}
						/>
					</div>
				</ul>
			</Fragment>
		);
	}
	onClickBookmark = (FilterConfig) => {
		console.log('FilterConfig',FilterConfig)
		const {
            Company: isFilterCompanyData,
            Country: isFilterCountryData,
            Area: isFilterAreaData,
            GroupID: isFilterGroupIdData,
			NBID: isFilterPreviousNBIDData,
			PreviousTime: isFilterStartTimeData,	//StartTime
		} = FilterConfig;
		const postData = {
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterCountryData: checkObject(isFilterCountryData),
            isFilterAreaData: checkObject(isFilterAreaData),
            isFilterGroupIdData: checkObject(isFilterGroupIdData),
			isFilterPreviousNBIDData: checkObject(isFilterPreviousNBIDData),
			isFilterStartTimeData: isFilterStartTimeData,
		};
		this.props.updateFilterData(
			FilterNames.isFilterCompanyData,
			checkObject(isFilterCompanyData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterCountryData,
			checkObject(isFilterCountryData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterAreaData,
			checkObject(isFilterAreaData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterGroupIdData, 
			checkObject(isFilterGroupIdData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterPreviousNBIDData,
			checkObject(isFilterPreviousNBIDData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterStartTimeData,
			isFilterStartTimeData
		);
		this.props.submitFilterData(postData);
		this.setState({ ...postData });
	};
	//checkboxAll更改選單顯示
	onClickAllSelectList = (name, boolean) => {
        const newList = this.state[name].map(item => {
            return { ...item, selectShow: boolean }
        });
        this.setState({ [name]: [...newList] });
	};
	//更新選單list
	onUpdateSelectList = (name, list) => {
		this.setState({ [name]: [...list] });
	};
	//更新選單data
	onUpdateList = (name, object) => {
		this.setState({ [name]: { ...object } });
	};
	handleSumbit = () => {
		const {
			isFilterCompanyData,
			isFilterCountryData,
			isFilterAreaData,
			isFilterGroupIdData,
			isFilterPreviousNBIDData,
			isFilterStartTimeData,
			...other
		} = this.state;
		this.props.updateFilterData(
			FilterNames.isFilterCompanyData,
			checkObject(isFilterCompanyData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterCountryData,
			checkObject(isFilterCountryData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterAreaData,
			checkObject(isFilterAreaData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterGroupIdData,
			checkObject(isFilterGroupIdData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterPreviousNBIDData,
			checkObject(isFilterPreviousNBIDData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterStartTimeData,
			isFilterStartTimeData
		);
		this.props.submitFilterData(this.state);
		this.setState({
			isFilterCompanyData: checkObject(isFilterCompanyData),
			isFilterCountryData: checkObject(isFilterCountryData),
			isFilterAreaData: checkObject(isFilterAreaData),
			isFilterGroupIdData: checkObject(isFilterGroupIdData),
			isFilterPreviousNBIDData: checkObject(isFilterPreviousNBIDData),
			isFilterStartTimeData: isFilterStartTimeData,
			...other,
		});
	};

	//接續歷史篩選清單API:
	getNBHistoryFilter = (
		defaultCompany,
		defaultCountry,
		defaultArea,
		defaultGroupId,
		defaultNBID,
	) => {
		const { token, curLanguage, timeZone, company } = this.props;
		const query = { token, curLanguage, timeZone, company };
		ajaxGetNBGroupHisFilter({query})
			.then((response) => {
				if (this.ajaxCancel) { new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
				if (Object.keys(response).length === 0) {
					return;
				}
				const { code, msg } = response;
				if (code === "00" && msg) {
					const { List } = msg;
					let CompanyObj = {};
					let CountryObj = {};
					let AreaObj = {};
					let GroupIdObj = {};
					let NBIDObj = {};
					List.forEach((item) => {
						if (item.CompanyValue !== "") {
							CompanyObj[item.CompanyValue] = {
								Value: item.CompanyValue,
								Label: item.CompanyLabel,
								selectShow: true,
							};
						}
						if (item.CountryValue !== "") {
							CountryObj[item.CountryValue] = {
								Value: item.CountryValue,
								Label: item.CountryLabel,
								selectShow: true,
							};
						}
						if (item.AreaValue !== "") {
							AreaObj[item.AreaValue] = {
								Value: item.AreaValue,
								Label: item.AreaLabel,
								selectShow: true,
							};
						}
						if (item.GroupValue !== "") {
							GroupIdObj[item.GroupValue] = {
								Value: item.GroupValue,
								Label: item.GroupLabel,
								selectShow: true,
							};
						}
						if (item.NBIDValue !== "") {
							NBIDObj[item.NBIDValue] = {
								Value: item.NBIDValue,
								Label: item.NBIDLabel,
								selectShow: true,
							};
						}
					});
					const CompanyList = Object.values(CompanyObj).sort(function (a, b) {
						return a.Label.localeCompare(b.Label, "zh-hant");
					});
					const CountryList = Object.values(CountryObj).sort(function (a, b) {
						return a.Label.localeCompare(b.Label, "zh-hant");
					});
					const AreaList = Object.values(AreaObj).sort(function (a, b) {
						return a.Label.localeCompare(b.Label, "zh-hant");
					});
					const GroupList = Object.values(GroupIdObj).sort(function (a, b) {
						return a.Label.localeCompare(b.Label, "zh-hant");
					});
					const NBIDList = Object.values(NBIDObj).sort(function (a, b) {
						return a.Label.localeCompare(b.Label, "zh-hant");
					});
					this.setState({
						isFilterSelectTable: List,
						isFilterCompanyList: [...CompanyList],
						isFilterCompanyData: {
							...defaultCompany,
						},
						isFilterCountryList: [...CountryList],
						isFilterCountryData: {
							...defaultCountry,
						},
						isFilterAreaList: [...AreaList],
						isFilterAreaData: {
							...defaultArea,
						},
						isFilterGroupIdList: [...GroupList],
						isFilterGroupIdData: {
							...defaultGroupId,
						},
						isFilterPreviousNBIDList: [...NBIDList],
						isFilterPreviousNBIDData: {
							...defaultNBID,
						},
					});
					return new Promise((resolve,reject) => resolve(""));
				} else {
					this.setState({
						isFilterSelectTable: [],
						isFilterCompanyList: [], //預設公司別清單
						isFilterCountryList: [], //預設國家清單
						isFilterAreaList: [], //預設地域清單
						isFilterGroupIdList: [], //預設站台編號清單
						isFilterPreviousNBIDList: [], //預設(接續)通訊序號清單
						isFilterCompanyData: { ...initFilterSelectData },
						isFilterAreaData: { ...initFilterSelectData },
						isFilterGroupIdData: { ...initFilterSelectData },
						isFilterPreviousNBIDData: { ...initFilterSelectData },
						isFilterStartTimeData: { ...initFilterDate },
					});
					return new Promise((resolve, reject) => reject(""));
				}
			})
			.then(() => {
				//處理已選擇項目的選單
				if (defaultCompany.isDataList.length > 0) {
					let obj = {};
					defaultCompany.isButtonList.forEach(
						(item) => (obj[item.Value] = item)
					);
					this.onUpdateChainSelectList(
						FilterNames_API.isFilterCompanyVirtue,
						obj
					);
				}
				if (defaultCountry.isDataList.length > 0) {
					let obj = {};
					defaultCountry.isButtonList.forEach(
						(item) => (obj[item.Value] = item)
					);
					this.onUpdateChainSelectList(
						FilterNames_API.isFilterCountryVirtue,
						obj
					);
				}
				if (defaultArea.isDataList.length > 0) {
					let obj = {};
					defaultArea.isButtonList.forEach((item) => (obj[item.Value] = item));
					this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, obj);
				}
                if (defaultGroupId.isDataList.length > 0) {
					let obj = {};
					defaultGroupId.isButtonList.forEach((item) => (obj[item.Value] = item));
					this.onUpdateChainSelectList(FilterNames_API.isFilterGroupIdVirtue, obj);
				}
                if (defaultNBID.isDataList.length > 0) {
					let obj = {};
					defaultNBID.isButtonList.forEach((item) => (obj[item.Value] = item));
					this.onUpdateChainSelectList(FilterNames_API.isFilterNBIDVirtue, obj);
				}
				this.setState({
					isFilterCompanyData: { ...defaultCompany },
					isFilterCountryData: { ...defaultCountry },
					isFilterAreaData: { ...defaultArea },
					isFilterGroupIdData: { ...defaultGroupId },
					isFilterPreviousNBIDData: { ...defaultNBID },
				});
			});
	};
	//取得篩選儲存清單API(GET)
	getFilter = () => {
		const { token, curLanguage, timeZone, company, account, functionId  } = this.props;
		const query = { token, curLanguage, timeZone, company, account, functionId  };
		ajaxGetFilter({query}).then((response) => {
			if (this.ajaxCancel) {
				return;
			} //強制結束頁面
			if (Object.keys(response).length === 0) {
				return;
			}
			const { code, msg } = response;
			if (code === "00" && msg) {
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
	};
	//刪除篩選API(GET)
	delFilter = (FilterID) => {
		const { token, curLanguage, timeZone, company } = this.props;
		const query = { token, curLanguage, timeZone, company };
		const postData = { FilterID };
		ajaxDelFilter({query,postData}).then((response) => {
			if (this.ajaxCancel) {
				return;
			} //強制結束頁面
			if (Object.keys(response).length === 0) {
				return;
			}
			const { code, msg } = response;
			if (code === "00" && msg) {
				this.props.alertFilterMsgOpen(msg);
			} else {
				this.props.alertFilterMsgOpen(msg);
			}
		});
	};

	//選擇公司別，更新其他連動選單
	onUpdateChainSelectList = (Virtue, object) => {
		const { isFilterSelectTable } = this.state;
		const Lists = isFilterSelectTable.filter((item) => {
			if (object[item[Virtue]]) {
				return true;
			} else {
				return false;
			}
		});
		let List = [];
		switch (Virtue) {
			case FilterNames_API.isFilterCompanyVirtue:
				List = [...Lists];
				break;
			case FilterNames_API.isFilterCountryVirtue:
				//過濾公司別
				if (this.state.isFilterCompanyData.isDataList.length > 0) {
					List = Lists.filter((item) =>
						this.state.isFilterCompanyData.isDataList.findIndex(
							(element) => element === item["CompanyValue"]
						) >= 0
							? true
							: false
					);
				} else {
					List = [...Lists];
				}
				break;
			case FilterNames_API.isFilterAreaVirtue:
				let List1 = [];
				//過濾公司別
				if (this.state.isFilterCompanyData.isDataList.length > 0) {
					List1 = Lists.filter((item) =>
						this.state.isFilterCompanyData.isDataList.findIndex(
							(element) => element === item["CompanyValue"]
						) >= 0
							? true
							: false
					);
				} else {
					List1 = [...Lists];
				}
				//過濾國家
				if (this.state.isFilterCountryData.isDataList.length > 0) {
					List = List1.filter((item) =>
						this.state.isFilterCountryData.isDataList.findIndex(
							(element) => element === item["CountryValue"]
						) >= 0
							? true
							: false
					);
				} else {
					List = [...List1];
				}
				break;
			case FilterNames_API.isFilterGroupIdVirtue:
				let List2 = [];
				//過濾公司別
				if (this.state.isFilterCompanyData.isDataList.length > 0) {
					List2 = Lists.filter((item) =>
						this.state.isFilterCompanyData.isDataList.findIndex(
							(element) => element === item["CompanyValue"]
						) >= 0
							? true
							: false
					);
				} else {
					List2 = [...Lists];
				}
				//過濾國家
				if (this.state.isFilterCountryData.isDataList.length > 0) {
					List = List2.filter((item) =>
						this.state.isFilterCountryData.isDataList.findIndex(
							(element) => element === item["CountryValue"]
						) >= 0
							? true
							: false
					);
				} else {
					List = [...List2];
				}
				//過濾地域
				if (this.state.isFilterAreaData.isDataList.length > 0) {
					List = List2.filter((item) =>
						this.state.isFilterAreaData.isDataList.findIndex(
							(element) => element === item["AreaValue"]
						) >= 0
							? true
							: false
					);
				} else {
					List = [...List2];
				}
				break;
			default:
		}
		let CountryObj = {};
		let AreaObj = {};
		let GroupObj = {};
		let NBIDObj = {};
		List.forEach((item) => {
			if (item.CountryValue !== "") {
				CountryObj[item.CountryValue] = {
					Value: item.CountryValue,
					Label: item.CountryLabel,
					selectShow: true,
				};
			}
			if (item.AreaValue !== "") {
				AreaObj[item.AreaValue] = {
					Value: item.AreaValue,
					Label: item.AreaLabel,
					selectShow: true,
				};
			}
			if (item.GroupValue !== "") {
				GroupObj[item.GroupValue] = {
					Value: item.GroupValue,
					Label: item.GroupLabel,
					selectShow: true,
				};
			}
			if (item.NBIDValue !== "") {
				NBIDObj[item.NBIDValue] = {
					Value: item.NBIDValue,
					Label: item.NBIDLabel,
					selectShow: true,
				};
			}
		});
		const CountryList = Object.values(CountryObj).sort(function (a, b) {
			return a.Label.localeCompare(b.Label, "zh-hant");
		});
		const AreaList = Object.values(AreaObj).sort(function (a, b) {
			return a.Label.localeCompare(b.Label, "zh-hant");
		});
		const GroupList = Object.values(GroupObj).sort(function (a, b) {
			return a.Label.localeCompare(b.Label, "zh-hant");
		});
		const NBIDList = Object.values(NBIDObj).sort(function (a, b) {
			return a.Label.localeCompare(b.Label, "zh-hant");
		});
		switch (Virtue) {
			case FilterNames_API.isFilterCompanyVirtue:
				this.setState({
					isFilterCountryList: [...CountryList],
					isFilterCountryData: { ...initFilterSelectData },
					isFilterAreaList: [...AreaList],
					isFilterAreaData: { ...initFilterSelectData },
					isFilterGroupIdList: [...GroupList],
					isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterPreviousNBIDList: [...NBIDList],
                    isFilterPreviousNBIDData: { ...initFilterSelectData },
				});
				break;
			case FilterNames_API.isFilterCountryVirtue:
				this.setState({
					isFilterAreaList: [...AreaList],
					isFilterAreaData: { ...initFilterSelectData },
					isFilterGroupIdList: [...GroupList],
					isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterPreviousNBIDList: [...NBIDList],
                    isFilterPreviousNBIDData: { ...initFilterSelectData },
				});
				break;
			case FilterNames_API.isFilterAreaVirtue:
				this.setState({
					isFilterGroupIdList: [...GroupList],
					isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterPreviousNBIDList: [...NBIDList],
                    isFilterPreviousNBIDData: { ...initFilterSelectData },
				});
				break;
            case FilterNames_API.isFilterGroupIdVirtue:
                this.setState({
                    isFilterPreviousNBIDList: [...NBIDList],
                    isFilterPreviousNBIDData: { ...initFilterSelectData },
                });
                break;
			default:
		}
	};
}

Filter.defaultProps = {
	functionId: "0",
	isFilterCompanyData: {},
	isFilterCountryData: {},
	isFilterAreaData: {},
	isFilterGroupIdData: {},
	isFilterPreviousNBIDData: {},
	isFilterStartTimeData: {},
	updateFilterData: () => {},
	submitFilterData: () => {},
	alertFilterMsgOpen: () => {},
};
Filter.propTypes = {
	functionId: PropTypes.string,
	isFilterCompanyData: PropTypes.object,
	isFilterCountryData: PropTypes.object,
	isFilterAreaData: PropTypes.object,
	isFilterGroupIdData: PropTypes.object,
	isFilterPreviousNBIDData: PropTypes.object,
	isFilterStartTimeData: PropTypes.object,
	updateFilterData: PropTypes.func,
	submitFilterData: PropTypes.func,
	alertFilterMsgOpen: PropTypes.func,
};
const mapStateToProps = (state, ownProps) => {
	return {
		token: state.LoginReducer.token,
		account: state.LoginReducer.account,
		company: state.LoginReducer.company,
		curLanguage: state.LoginReducer.curLanguage,
		timeZone: state.LoginReducer.timeZone,
	};
};
const mapDispatchToProps = (dispatch, ownProps) => {
	return {};
};
export default connect(mapStateToProps, mapDispatchToProps)(Filter);
