import React, { Component, Fragment } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { Trans } from "react-i18next";

import { ajaxDelFilter, ajaxGetFilter, ajaxGetBatteryFilter } from '../FilterDrawer/utils';
import { checkObject, getSelectObj } from "../FilterDrawer/utils"; //整理checkbox狀態
import { FilterNames,FilterNames_API,initFilterSelectData, } from "./InitDataFormat"; //初始化格式

import Divider from "@material-ui/core/Divider";
import FilterSave from "./FilterSave"; //儲存標籤紀錄
import FilterItemHeader from "../FilterDrawer/FilterItemHeader"; //每筆Item的header
import FilterItemSelect from "../FilterDrawer/FilterItemSelect"; //每筆Item的Select
import { CusMainBtnStyle } from "../CusMainBtnStyle";


class BattFilter extends Component {
	ajaxCancel = false; //判斷ajax是否取消
	constructor(props) {
		super(props);
		this.state = {
			isFilterCompanyList: [], //預設公司別清單
			isFilterCountryList: [], //預設國家清單
			isFilterAreaList: [], //預設地域清單
			isFilterGroupIdList: [], //預設站台編號清單
			isFilterBatteryGroupIdList: [], //預設電池組ID清單
			isFilterBatteryStatusList: [], //預設電池狀態清單
			isFilterCompanyData: {
				//預設公司別
				...initFilterSelectData,
			},
			isFilterCountryData: {
				//國家
				...initFilterSelectData,
			},
			isFilterAreaData: {
				//預設地域
				...initFilterSelectData,
			},
			isFilterGroupIdData: {
				...initFilterSelectData,
			}, //預設站台
			isFilterBatteryGroupIdData: {
				//預設電池組ID
				...initFilterSelectData,
			},
			isFilterBatteryStatusData: {
				//預設電池狀態
				...initFilterSelectData,
			},
			isFilterTotal: this.checkedIsFilterTotal(), //總數
		};
	}
	componentDidMount() {
		const {
			isFilterCompanyData,
			isFilterCountryData,
			isFilterAreaData,
			isFilterGroupIdData,
			isFilterBatteryGroupIdData,
			isFilterBatteryStatusData,
		} = this.props;
		this.getBatteryFilter(
			isFilterCompanyData,
			isFilterCountryData,
			isFilterAreaData,
			isFilterGroupIdData,
			isFilterBatteryGroupIdData,
			isFilterBatteryStatusData
		); //取選單資料
		this.getFilter(); //篩選儲存清單API(GET)
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
			prevState.isFilterBatteryGroupIdData.isDataList.length !==
				this.state.isFilterBatteryGroupIdData.isDataList.length ||
			prevState.isFilterBatteryStatusData.isDataList.length !==
				this.state.isFilterBatteryStatusData.isDataList.length
		) {
			const list = [
				this.state.isFilterCompanyData.isDataList.length,
				this.state.isFilterCountryData.isDataList.length,
				this.state.isFilterAreaData.isDataList.length,
				this.state.isFilterGroupIdData.isDataList.length,
				this.state.isFilterBatteryGroupIdData.isDataList.length,
				this.state.isFilterBatteryStatusData.isDataList.length,
			];
			let total = 0;
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
			prevState.isFilterBatteryGroupIdData.isChecked !==
			this.state.isFilterBatteryGroupIdData.isChecked
		) {
			this.setState({
				isFilterBatteryGroupIdData: this.state.isFilterBatteryGroupIdData,
			});
		}
		if (
			prevState.isFilterBatteryStatusData.isChecked !==
			this.state.isFilterBatteryStatusData.isChecked
		) {
			this.setState({
				isFilterBatteryStatusData: this.state.isFilterBatteryStatusData,
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
		const { functionId } = this.props;
		const filterTitle = functionId.slice(0, 4);
		const { isFilterTotal, isFilterSaveList } = this.state;
		const { isFilterCompanyList, isFilterCompanyData } = this.state; //公司
		const { isFilterCountryList, isFilterCountryData } = this.state; //國家
		const { isFilterAreaList, isFilterAreaData } = this.state; //地域
		const { isFilterGroupIdList, isFilterGroupIdData } = this.state; //站台編號
		const { isFilterBatteryGroupIdList, isFilterBatteryGroupIdData } =
			this.state; //電池組ID
		const { isFilterBatteryStatusList, isFilterBatteryStatusData } = this.state; //電池狀態
		return (
			<Fragment>
				<div className='form-inline align-items-center justify-content-center pt-1 pb-2'>
					<i className='fas fa-filter mr-1' />
					<Trans i18nKey={filterTitle} />
					{/* 總計 */}
					<span className='filter_prompt ml-1 noDetail'>{isFilterTotal}</span>
				</div>
				<Divider />
				{/* 紀錄 */}
				<FilterSave
					functionId={this.props.functionId} // 1400_1, 1400_2, 1600_1, 1600_2
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
							{/* 公司別 */}
							{
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
											FilterNames_API.isFilterCompanyVirtue,
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
											this.onUpdateChainSelectList(
												FilterNames_API.isFilterCompanyVirtue,
												object
											)
										}
										onUpdateList={(object) =>
											this.onUpdateList(FilterNames.isFilterCompanyData, object)
										}
									/>
								</FilterItemHeader>
							}
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
					{functionId !== "1600_2" && (
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
					)}
					{
						//電池狀態 1021
						functionId === "1600_1" || functionId === "1600_2" ? (
							""
						) : (
							<li>
								<FilterItemHeader
									title={"1021"}
									total={isFilterBatteryStatusData.isDataList.length}
									detailList={isFilterBatteryStatusData.isButtonList}
									isChecked={isFilterBatteryStatusData.isChecked}
									isOpen={isFilterBatteryStatusData.isOpen}
									onClickAllSelectList={(boolean) =>
										this.onClickAllSelectList(
											"isFilterBatteryStatusList",
											boolean
										)
									}
									onUpdateList={(object) =>
										this.onUpdateList(
											FilterNames.isFilterBatteryStatusData,
											object
										)
									}>
									<FilterItemSelect
										defSelectVal={isFilterBatteryStatusData.isDataList}
										defSelectList={isFilterBatteryStatusList}
										onUpdateSelectList={(list) =>
											this.onUpdateSelectList("isFilterBatteryStatusList", list)
										}
										onUpdateList={(object) =>
											this.onUpdateList(
												FilterNames.isFilterBatteryStatusData,
												object
											)
										}
									/>
								</FilterItemHeader>
							</li>
						)
					}
					{
						// 電池組ID 僅電池歷史顯示
						functionId === "1400_1" || functionId === "1400_2" ? (
							""
						) : (
							<li>
								<FilterItemHeader
									title={"1026"}
									total={isFilterBatteryGroupIdData.isDataList.length}
									detailList={isFilterBatteryGroupIdData.isButtonList}
									isChecked={isFilterBatteryGroupIdData.isChecked}
									isOpen={isFilterBatteryGroupIdData.isOpen}
									onClickAllSelectList={(boolean) =>
										this.onClickAllSelectList(
											"isFilterBatteryGroupIdList",
											boolean
										)
									}
									onUpdateList={(object) =>
										this.onUpdateList(
											FilterNames.isFilterBatteryGroupIdData,
											object
										)
									}>
									<FilterItemSelect
										defSelectVal={isFilterBatteryGroupIdData.isDataList}
										defSelectList={isFilterBatteryGroupIdList}
										onUpdateSelectList={(list) =>
											this.onUpdateSelectList(
												"isFilterBatteryGroupIdList",
												list
											)
										}
										onUpdateList={(object) =>
											this.onUpdateList(
												FilterNames.isFilterBatteryGroupIdData,
												object
											)
										}
									/>
								</FilterItemHeader>
							</li>
						)
					}

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
	// 檢查預設篩選條件總數
	checkedIsFilterTotal = () => {
		return 0;
	};
	// 書籤
	onClickBookmark = (FilterConfig) => {
		const {
			Company: isFilterCompanyData,
			Country: isFilterCountryData,
			Area: isFilterAreaData,
			GroupID: isFilterGroupIdData,
			Status: isFilterBatteryStatusData,
			BatteryGroupId: isFilterBatteryGroupIdData,
		} = FilterConfig;
		const postData = {
			isFilterCompanyData: checkObject(isFilterCompanyData),
			isFilterCountryData: checkObject(isFilterCountryData),
			isFilterAreaData: checkObject(isFilterAreaData),
			isFilterGroupIdData: checkObject(isFilterGroupIdData),
			isFilterBatteryStatusData: checkObject(isFilterBatteryStatusData),
			isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
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
			FilterNames.isFilterBatteryStatusData,
			checkObject(isFilterBatteryStatusData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterBatteryGroupIdData,
			checkObject(isFilterBatteryGroupIdData)
		);
		this.props.submitFilterData(postData);
		this.setState({ ...postData });
	};
	//checkboxAll更改選單顯示
	onClickAllSelectList = (name, boolean) => {
		const newList = this.state[name].map((item) => {
			return { ...item, selectShow: boolean };
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
	// 送出篩選條件
	handleSumbit = () => {
		const {
			isFilterCompanyData,
			isFilterCountryData,
			isFilterAreaData,
			isFilterGroupIdData,
			isFilterBatteryGroupIdData,
			isFilterBatteryStatusData,
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
			FilterNames.isFilterBatteryGroupIdData,
			checkObject(isFilterBatteryGroupIdData)
		);
		this.props.updateFilterData(
			FilterNames.isFilterBatteryStatusData,
			checkObject(isFilterBatteryStatusData)
		);
		this.props.submitFilterData(this.state);
		this.setState({
			isFilterCompanyData: checkObject(isFilterCompanyData),
			isFilterCountryData: checkObject(isFilterCountryData),
			isFilterAreaData: checkObject(isFilterAreaData),
			isFilterGroupIdData: checkObject(isFilterGroupIdData),
			isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
			isFilterBatteryStatusData: checkObject(isFilterBatteryStatusData),
			...other,
		});
	};

	//電池數據篩選清單API:
	getBatteryFilter = (
		defaultCompany,
		defaultCountry,
		defaultArea,
		defaultGroupId,
		defaultBatteryGroupId,
		defaultBatteryStatus
	) => {
		const { token, curLanguage, timeZone, company } = this.props;
		const query = { token, curLanguage, timeZone, company };
		ajaxGetBatteryFilter({query})
			.then((response) => {
				if (this.ajaxCancel) {
					new Promise((resolve, reject) => reject(""));
				} //強制結束頁面
				if (Object.keys(response).length === 0) {
					new Promise((resolve, reject) => reject(""));
				}
				const { code, msg } = response;
				if (code === "00" && msg) {
					const { List, Status } = msg;
					let StatusArr = [];
					let CompanyObj = {};
					let CountryObj = {};
					let AreaObj = {};
					let GroupIdObj = {};
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
					});
					Status.forEach((item, idx) => {
						StatusArr.push({
							Value: item.Value,
							Label: item.Label,
							I18NCode: item.I18NCode,
							selectShow: true,
						});
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
					this.setState({
						isFilterSelectTable: [...List],
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
						isFilterBatteryStatusList: [...StatusArr], //Status
						isFilterBatteryStatusData: {
							...defaultBatteryStatus,
						},
						isFilterBatteryGroupIdList: [{ Value: "", Label: "" }],
						isFilterBatteryGroupIdData: {
							...defaultBatteryGroupId,
						},
					});
					return new Promise((resolve, reject) => resolve(""));
				} else {
					this.setState({
						isFilterCompanyList: [], //預設公司別清單
						isFilterCountryList: [], //預設國家清單
						isFilterAreaList: [], //預設地域清單
						isFilterGroupIdList: [], //預設站台編號清單
						isFilterBatteryGroupIdList: [], //預設電池組ID清單
						isFilterBatteryTypeList: [], //預設電池型號清單
						isFilterCompanyData: { ...initFilterSelectData },
						isFilterCountryData: { ...initFilterSelectData },
						isFilterAreaData: { ...initFilterSelectData },
						isFilterGroupIdData: { ...initFilterSelectData },
						isFilterBatteryStatusData: { ...initFilterSelectData },
						isFilterBatteryGroupIdData: { ...initFilterSelectData },
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
				this.setState({
					isFilterCompanyData: { ...defaultCompany },
					isFilterCountryData: { ...defaultCountry },
					isFilterAreaData: { ...defaultArea },
					isFilterGroupIdData: { ...defaultGroupId },
				});
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
			default:
		}
		let CountryObj = {};
		let AreaObj = {};
		let GroupObj = {};
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
		switch (Virtue) {
			case FilterNames_API.isFilterCompanyVirtue:
				this.setState({
					isFilterCountryList: [...CountryList],
					isFilterCountryData: { ...initFilterSelectData },
					isFilterAreaList: [...AreaList],
					isFilterAreaData: { ...initFilterSelectData },
					isFilterGroupIdList: [...GroupList],
					isFilterGroupIdData: { ...initFilterSelectData },
				});
				break;
			case FilterNames_API.isFilterCountryVirtue:
				this.setState({
					isFilterAreaList: [...AreaList],
					isFilterAreaData: { ...initFilterSelectData },
					isFilterGroupIdList: [...GroupList],
					isFilterGroupIdData: { ...initFilterSelectData },
				});
				break;
			case FilterNames_API.isFilterAreaVirtue:
				this.setState({
					isFilterGroupIdList: [...GroupList],
					isFilterGroupIdData: { ...initFilterSelectData },
				});
				break;
			default:
		}
	};

	//取得篩選儲存清單API(GET)
	getFilter = () => {
		const { token, curLanguage, timeZone, company, account, functionId } = this.props;
		const query = { token, curLanguage, timeZone, company,account, functionId };
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
		const postData = {FilterID};
		ajaxDelFilter({query,postData}).then((response) => {
			if (this.ajaxCancel) {
				return;
			} //強制結束頁面
			if (Object.keys(response).length === 0) {
				return;
			}
			const { msg } = response;
			if (msg) {
				this.props.alertFilterMsgOpen(msg);
			}
		});
	};
}

BattFilter.defaultProps = {
	functionId: "0",
	isFilterCompanyData: {},
	isFilterCountryData: {},
	isFilterAreaData: {},
	isFilterGroupIdData: {},
	isFilterGroupNameData: {},
	isFilterBatteryGroupIdData: {},
	isFilterBatteryTypeData: {},
	updateFilterData: () => {},
	submitFilterData: () => {},
	alertFilterMsgOpen: () => {},
};
BattFilter.propTypes = {
	functionId: PropTypes.string,
	isFilterCompanyData: PropTypes.object,
	isFilterCountryData: PropTypes.object,
	isFilterAreaData: PropTypes.object,
	isFilterGroupIdData: PropTypes.object,
	isFilterGroupNameData: PropTypes.object,
	isFilterBatteryGroupIdData: PropTypes.object,
	isFilterBatteryTypeData: PropTypes.object,
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
export default connect(mapStateToProps, mapDispatchToProps)(BattFilter);
