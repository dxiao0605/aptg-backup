import React, { Component, Fragment } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { apipath, ajax } from "../../../utils/ajax";
import { Trans } from "react-i18next";
import AlertMsg from "../../../components/AlertMsg"; //訊息視窗
import { MenuItem, Select } from "@material-ui/core";

const apiUrl = apipath();
class Page1815 extends Component {
	// 匯入與分配
	ajaxCancel = false; //判斷ajax是否取消
	constructor(props) {
		super(props);
		this.state = {
			functionId: "1800", //funcId
			pageId: "1801", //頁面Id
			limits: {
				//權限設定
				Edit: 0,
				Button: {},
			},
			loading: true, //總畫面讀取
			CommandSetup: [], //指令限制
			CompanyCode: this.props.company, //目前所選公司
			B3ToReplace: false, //目前所選公司 B3ToReplace

			//訊息視窗
			openAlertMsg: false, //開啟訊息視窗
			titleAlertMsg: "", //訊息視窗-標頭
			textAlertMsg: "", //訊息視窗-文字欄位
		};
	}

	// React Lifecycle
	componentDidMount() {
		//設定權限
		const { functionList } = this.props;
		const { functionId } = this.state;
		const newFList = functionList.filter((item) => {
			return item.FunctionId === Number(functionId);
		});
		if (newFList.length === 1) {
			this.setState({
				limits: {
					Edit: newFList[0].Edit,
					Button: { ...newFList[0].Button },
				},
			});
		}
		this.setState({
			loading: false,
		});
		this.getDefaultSetting();
	}
	componentDidUpdate(prevProps, prevState) {
		if (
			this.props.curLanguage !== undefined &&
			this.props.curLanguage !== prevProps.curLanguage
		) {
		}
	}
	componentWillUnmount() {
		this.ajaxCancel = true;
		this.setState = (state, callback) => {
			return;
		};
	}

	render() {
		const {
			loading,
			limits,
			CommandSetup, //指令限制清單
			CompanyCode, // 目前所選公司
			B3ToReplace, //目前所選公司 B3ToReplace
		} = this.state;
		if (loading) {
			return "";
		} else {
			return (
				<Fragment>
					{/* 指令限制 */}
					<div className='col-12 col-xl-6 mt-4 pl-0 pr-0'>
						{/* 指令限制 */}
						<div
							className='font-weight-bold col-12'
							style={{
								backgroundColor: "#525b6c",
								color: "#fff",
								lineHeight: "3",
							}}>
							<Trans i18nKey={"1815"} />
						</div>
						<div className='px-4 py-4 bg-white'>
							<div className='form-inline align-items-center'>
								{/* 選擇公司 */}
								{this.props.company === "1" && (
									<div className='form-inline ml-xl-2 w-100 my-2'>
										<label className='mr-1 my-1'>
											<Trans i18nKey={"1074"} />：
										</label>
										<Select
											className='my-1 col-xl-3 col-12'
											name='CompanyCode'
											value={`${CompanyCode}`}
											onChange={(e) => {
												this.onUpdateCompanySelect(e);
											}}
											disabled={limits.Button.P1815 === 1 ? false : true}>
											{CommandSetup.map((item) => {
												return (
													<MenuItem
														value={`${item.CompanyValue}`}
														key={item.CompanyValue}>
														{item.CompanyLabel}
													</MenuItem>
												);
											})}
											<MenuItem value=''></MenuItem>
										</Select>
									</div>
								)}
								{/* B3-限制需更換狀態校正內阻 */}
								<div className='form-inline ml-xl-2 w-100 my-2'>
									<span className='custom-control custom-checkbox'>
										<input
											type='checkbox'
											className='custom-control-input '
											id='B3ToReplace'
											checked={B3ToReplace}
											onChange={(e) => this.onUpdateB3ToReplace(e)}
											disabled={limits.Button.P1815 === 1 ? false : true}
										/>
										<label
											className='custom-control-label'
											htmlFor='B3ToReplace'>
											<Trans i18nKey={"1816"} />
										</label>
									</span>
								</div>
								{/* 確認 */}
								{limits.Button.P1815 === 1 && (
									<div className='form-inline ml-xl-2 w-100 my-2 justify-content-end'>
										<button
											type='button'
											className='btn btn-sm btn-secondary exportBtnShadow col-12 col-xl-2 px-0'
											style={{ background: "#03c3ff", borderColor: "#03c3ff" }}
											onClick={() => {
												this.handleSubmit();
											}}>
											<Trans i18nKey={"1010"} />
										</button>
									</div>
								)}
							</div>
						</div>
					</div>
					{/* 彈跳視窗 */}
					<AlertMsg
						msgTitle={this.state.titleAlertMsg} //Title
						open={this.state.openAlertMsg} //開啟視窗
						handleClose={this.handleAlertMsgClose} //連動關閉視窗
						onIsRefreshChange={(boolean) =>
							this.props.onIsRefreshChange(boolean)
						} //刷新頁面
						isDisabledBtn={false}>
						{/* 視窗資料 */}
						<div className='col-12 p-0 my-4'>
							<div className='my-1'>{this.state.textAlertMsg}</div>
						</div>
					</AlertMsg>
				</Fragment>
			);
		}
	}
	// 變更目前顯示公司及指令限制
	onUpdateCompanySelect = (e) => {
		const { CommandSetup } = this.state;
		const curB3ToReplace = CommandSetup.filter(
			(filterItem) => filterItem.CompanyValue === parseInt(e.target.value)
		).map((item) => item.B3ToReplace);
		this.setState({
			[e.target.name]: e.target.value,
			B3ToReplace: curB3ToReplace[0],
		});
	};

	onUpdateB3ToReplace = (e) => {
		this.setState({
			[e.target.id]: e.target.checked,
		});
	};

	handleSubmit = () => {
		//確認送出
		const { limits } = this.state;
		const { CompanyCode, B3ToReplace } = this.state;
		if (limits?.Button === undefined && limits.Button.P1815 !== 1) {
			return new Promise((resolve, reject) => reject(""));
		}
		const postData = {
			CompanyCode: CompanyCode,
			B3ToReplace: B3ToReplace,
			UserName: this.props.username,
		};
		return this.ajaxUpdCommandSetup(postData).then((response) => {
			if (this.ajaxCancel) {
				return new Promise((resolve, reject) => reject(""));
			} //強制結束頁面
			if (Object.keys(response).length === 0) {
				return new Promise((resolve, reject) => reject(""));
			}
			const { code, msg } = response;
			if (code === "00" && msg) {
				this.setState((prevState, prevProps) => ({
					openAlertMsg: true, //開啟訊息視窗
					titleAlertMsg: "1089",
					textAlertMsg: msg, //填寫訊息
				}));
			} else {
				this.setState((prevState, prevProps) => ({
					openAlertMsg: true, //開啟訊息視窗
					titleAlertMsg: "1089",
					textAlertMsg: msg, //填寫訊息
				}));
			}
		});
	};

	// 取得預設B3ToReplace,CompanyCode
	getDefaultSetting = () => {
        this.ajaxGetCommandSetup().then((response) => {
			if (this.ajaxCancel) {
				return new Promise((resolve, reject) => reject(""));
			} //強制結束頁面
			if (Object.keys(response).length === 0) {
				return new Promise((resolve, reject) => reject(""));
			}
			const { code, msg } = response;
			if (code === "00" && msg) {
				const { CommandSetup } = msg;
                const curB3ToReplace = CommandSetup.filter(
                    (filterItem) => filterItem.CompanyValue === parseInt(this.props.company)
                ).map((item) => item.B3ToReplace);
				this.setState({
                    CompanyCode: this.props.company,
					CommandSetup: CommandSetup,
                    B3ToReplace: curB3ToReplace[0],
				});
			} else {
				this.setState({
					CommandSetup: [],
				});
			}
		});
	};

	//公司清單及內阻呈現下拉選單API(GET):
	getCommandSetup = () => {
		this.ajaxGetCommandSetup().then((response) => {
			if (this.ajaxCancel) {
				return new Promise((resolve, reject) => reject(""));
			} //強制結束頁面
			if (Object.keys(response).length === 0) {
				return new Promise((resolve, reject) => reject(""));
			}
			const { code, msg } = response;
			if (code === "00" && msg) {
				const { CommandSetup } = msg;
				this.setState({
					CommandSetup: CommandSetup,
				});
			} else {
				this.setState({
					CommandSetup: [],
				});
			}
		});
	};

	//開msg視窗
	handleAlertMsgOpen = (title, msg) => {
		this.setState({
			openAlertMsg: true,
			titleAlertMsg: title,
			textAlertMsg: msg, //填寫訊息
		});
		this.props.onIsRefreshChange(false); //關閉5min刷新頁面
	};

	//關msg視窗
	handleAlertMsgClose = () => {
		this.setState({
			openAlertMsg: false,
			titleAlertMsg: "",
			textAlertMsg: "", //填寫訊息
		});
		this.props.onIsRefreshChange(true); //重新開始5min刷新頁面
	};

	//公司清單及指令限制API(GET):
	ajaxGetCommandSetup = () => {
		const { token, curLanguage, timeZone, company } = this.props;
		const url = `${apiUrl}getCommandSetup`;
		return ajax(url, "GET", token, curLanguage, timeZone, company);
		//指令限制API(GET):
		//https://www.gtething.tw/battery/getCommandSetup
	};
	//修改指令限制API(POST):
	ajaxUpdCommandSetup = (postData) => {
		const { token, curLanguage, timeZone, company } = this.props;
		const url = `${apiUrl}updCommandSetup`;
		return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
		//修改指令限制API(POST):
		//https://www.gtething.tw/battery/updCommandSetup
	};
}

Page1815.defaultProps = {
	onIsRefreshChange: () => {},
};

Page1815.propTypes = {
	onIsRefreshChange: PropTypes.func,
	perPage: PropTypes.number,
};

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
	};
};
const mapDispatchToProps = (dispatch, ownProps) => {
	return {};
};
export default connect(mapStateToProps, mapDispatchToProps)(Page1815);
