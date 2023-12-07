import React, { Component } from "react";
import classNames from "classnames";
import { checkFunctionList } from "./utils/checkFunctionList";
import {
	HashRouter as Router,
	Route,
	Switch,
	Redirect,
} from "react-router-dom";
import { connect } from "react-redux";
import { setInitAlertCount } from "./actions/AlertCountAction";
import { setInitAlertTheader } from "./actions/AlertAction";
import { setInitBattData } from "./actions/BattDataAction";
import { resetTreeFunctionId } from "./actions/MainNavAction";
//清空篩選欄位
import { exit } from "./actions/LoginAction";
import { resetAllHomeData } from "./actions/HomeFilterAction";
import { resetAllAlert } from "./actions/AlertFilterAction";
import { resetAllAlertUnsolved } from "./actions/AlertUnsolvedFilterAction";
import { resetAllBattData } from "./actions/BattFilterAction";
//電池管理介面
import { resetActiveNum_P1501 } from "./actions/BattManageAction"; //1501
import { resetAll_P1501_Action } from "./actions/BattManageP1501Action";
import { resetAll_P1505_Action } from "./actions/BattManageP1505Action";
import { resetActiveNum_P1502 } from "./actions/GroupManageAction"; //1502
import { resetAll_P1502_Action } from "./actions/GroupManageP1502Action";
import { resetActiveNum_P1503 } from "./actions/NBManageAction"; //1503
import { resetAll_P1539_Action } from "./actions/NBManageP1539Action";
import { resetActiveNum_P1504 } from "./actions/CommandAction"; //1504
import { resetAll_P1504_Action } from "./actions/CommandP1504Action";
import { resetAll_P1559_Action } from "./actions/CommandP1559Action";
// 使用者設定
import { resetActiveNum_USERMANAGE } from "./actions/UserManageAction"; //1700
//系統設定
import { resetActiveNum_SYSYEMSETTINGS } from "./actions/SystemSettingsAction"; //1800

// i18n
import { setCurrentLanguage } from "./utils/setCurrentLanguage";
// components
import MainNav from "./components/MainNav";
import CusLoader from "./components/CusLoader";
import DialogChangePw from "./components/UserConfig/DialogChangePw";
// pages
import {
	Home,Login,
	AlertUnsolved,AlertSolved,AlertCondition,
	BattGroup,BattData,BattHistory,BattHistoryManage,
	BattManage,GroupManage,NBManage,Command,UserManage,SystemSettings
} from "./Route/lazyLoad";
// style
import "bootstrap/dist/css/bootstrap.min.css";
import "./style/App.scss";

class App extends Component {
	constructor(props) {
		super(props);
		this.state = {
			isShow: true, //是否顯示功能清單
			width: window.innerWidth, //目前裝置大小
			height: window.innerHeight, //目前裝置大小
			curLanguage: this.props.curLanguage, //目前語系
			langCode: "", //目前語系簡碼，預設載入語系檔名
			isChangePWOpen: true, //變更密碼視窗
		};
	}

	componentDidMount() {
		const { curLanguage } = this.props;
		const { width } = this.state;
		// 功能選單
		this.onToggleMainNav(width);
		// RWD
		window.addEventListener("resize", this.updateDimensions);
		// 判斷語系
		this.onChangeTranslations(curLanguage);
	}

	componentDidUpdate(prevProps, prevState) {
		// 判斷語系&變更語系檔
		if (this.props.curLanguage !== prevState.curLanguage) {
			this.onChangeTranslations(this.props.curLanguage);
			this.setState({
				curLanguage: this.props.curLanguage,
			});
		}
	}

	componentWillUnmount() {
		window.removeEventListener("resize", this.updateDimensions);
	}

	render() {
		const {
			token,
			account,
			language,
			timeZone,
			redirectToReferrer,
			DisableTime,
			functionList,
		} = this.props;
		const { isShow, width, height, langCode, isChangePWOpen } = this.state;
		// 判斷使用者是否可以觀看此頁面
		const currentHash = window.location.hash.replace("#", "");
		const checkPage = checkFunctionList(functionList, currentHash);
		// style
		const classCheckedNav = classNames({
			"main_content open": isShow === true,
			"main_content hide": isShow === false,
			main_content: window.location.href.indexOf("login") > 0,
		});

		// 當沒有登入時，強制轉至登入頁
		if (token === "" && !redirectToReferrer) {
			return <Redirect to='/login' />;
		}
		if(checkPage) {
			return (
				<>
					{/* IntlProvider -> i18n */}
					{langCode === "" ? (
						<CusLoader />
					) : (
						<Router>
							{/* 解決google chrome瀏覽器自動代入input值(已儲存帳號) Chaff for Chrome Smart Lock */}
							<div
								style={{
									visibility: "hidden",
									height: 0,
									width: 0,
									position: "fixed",
									top: 0,
									left: 0,
									opacity: 0,
								}}>
								<input
									type='password'
									value=''
									title='Chaff for Chrome Smart Lock'
									autoComplete='off'
									onChange={() => {}}
								/>
								<input
									type='text'
									value=''
									autoComplete='off'
									onChange={() => {}}
								/>
							</div>
							<div className='App container-fuild'>
								{/* 變更密碼 */}
								{DisableTime !== "" && (
									<DialogChangePw
										token={token}
										account={account}
										language={language}
										timeZone={timeZone}
										open={isChangePWOpen} //是否展開變更密碼視窗
										handleOpen={this.handleChangePWOpen}
										handleClose={this.handleChangePWClose}
										handleLogout={this.onSubmitLogout}
										alertValidDate={this.props.DisableTime} //密碼有效期限
									/>
								)}
	
								{/* 左側選單欄 */}
								{
									// 當是登入頁面時不顯示
									window.location.href.indexOf("login") < 0 && (
										<MainNav
											isShow={isShow} //是否展開選單欄
											screen={{ width: width, height: height }} //目前裝置大小
											getIsShow={this.getIsShow}
										/>
									)
								}
	
								<main className={classCheckedNav}>
									<Switch>
										<Route path='/login' component={Login} />
										<Route path='/' exact component={Home} />
										{/* 告警 */}
										<Route path='/AlertUnsolved' component={AlertUnsolved} />
										<Route path='/AlertSolved' component={AlertSolved} />
										<Route path='/AlertCondition' component={AlertCondition} />
										{/* 電池數據 */}
										<Route path='/BattGroup' component={BattGroup} />
										<Route path='/BattData' component={BattData} />
										{/* 電池管理 */}
										<Route path='/BattManage' component={BattManage} />{" "}
										{/* 電池組管理 */}
										<Route path='/GroupManage' component={GroupManage} />{" "}
										{/* 站台管理 */}
										<Route path='/NBManage' component={NBManage} />{" "}
										{/* 通訊序號管理 */}
										<Route path='/Command' component={Command} />{" "}
										{/* 電池參數設定 */}
										{/* 電池歷史 */}
										<Route
											path='/BattHistoryManage'
											component={BattHistoryManage}
										/>{" "}
										{/* 多筆 */}
										<Route path='/BattHistory' component={BattHistory} />{" "}
										{/* 單筆 */}
										{/* 使用者管理 */}
										<Route path='/User' component={UserManage} />
										<Route path='/Role' component={UserManage} />
										{/* 系統設定 */}
										<Route path='/SystemSettings' component={SystemSettings} />
									</Switch>
								</main>
							</div>
						</Router>
					)}
				</>
			)
		}else {return <Redirect to="/" />}
	}

	// 顯示隱藏功能選單
	getIsShow = (value) => {
		this.setState({ isShow: value });
	};
	// 判斷功能選單展開/隱藏
	onToggleMainNav = () => {
		if (window.innerWidth > 768) {
			this.setState({ isShow: true });
		} else {
			this.setState({ isShow: false });
		}
	};
	// 判斷語系變更語系檔
	onChangeTranslations = (language) => {
		const lang = setCurrentLanguage(language);
		this.setState({
			langCode: lang,
		});
	};

	// 顯示變更密碼彈跳視窗
	handleChangePWOpen = () => {
		this.setState({ isChangePWOpen: true });
	};
	// 關閉變更密碼彈跳視窗
	handleChangePWClose = () => {
		this.setState({ isChangePWOpen: false });
	};
	onSubmitLogout = () => {
		localStorage.clear();
		this.props.resetTreeFunctionId(); //初始化左側表單
		this.props.setInitAlertCount(); //初始化左側表單告警筆數

		// 初始化總覽
		this.props.resetAllHomeData(); //初始化總覽篩選欄位
		// 初始化告警
		this.props.setInitAlertTheader(); //初始化告警表格標題欄位
		this.props.resetAllAlert(); //初始化告警(已解決)篩選欄位
		this.props.resetAllAlertUnsolved(); //初始化告警(未解決)篩選欄位
		// 初始化電池數據/歷史
		this.props.setInitBattData(); //初始化電池數據
		this.props.resetAllBattData(); //初始化電池數據,電池歷史篩選欄位

		// 初始化電池組管理(1501)
		this.props.resetActiveNum_P1501(); //初始化頁籤
		this.props.resetAll_P1501_Action(); //初始化電池組管理
		this.props.resetAll_P1505_Action(); //初始化電池型號管理
		// 初始化站台管理(1502)
		this.props.resetActiveNum_P1502(); //初始化頁籤
		this.props.resetAll_P1502_Action(); //初始化站台管理
		// 初始化通訊序號管理(1503)
		this.props.resetActiveNum_P1503(); //初始化頁籤
		this.props.resetAll_P1539_Action(); //初始化異動紀錄
		// 清空電池參數(1504)
		this.props.resetActiveNum_P1504(); //初始化頁籤
		this.props.resetAll_P1504_Action(); //初始化參數設定
		this.props.resetAll_P1559_Action(); //初始化參數歷史
		// 清空使用者設定(1700)
		this.props.resetActiveNum_USERMANAGE(); //初始化頁籤
		// 清空系統設定(1800)
		this.props.resetActiveNum_SYSYEMSETTINGS(); //初始化頁籤

		this.props.exit();
	};

	// 變更裝置大小時
	updateDimensions = () => {
		this.setState({ width: window.innerWidth, height: window.innerHeight });
		// 判斷功能選單展開/隱藏
		this.onToggleMainNav(window.innerWidth);
	};
}

const mapStateToProps = (state, ownProps) => {
	return {
		redirectToReferrer: state.LoginReducer.redirectToReferrer,
		token: state.LoginReducer.token,
		account: state.LoginReducer.account,
		username: state.LoginReducer.username, //用戶名
		role: state.LoginReducer.role, //權限
		company: state.LoginReducer.company, //公司別
		timeZone: state.LoginReducer.timeZone, //時區
		language: state.LoginReducer.language, //語系
		curLanguage: state.LoginReducer.curLanguage, //目前語系
		functionList: state.LoginReducer.functionList, //功能選單
		DisableTime: state.LoginReducer.DisableTime, // 密碼有效期限
		data: state.LoginReducer.data,
	};
};
const mapDispatchToProps = (dispatch, ownProps) => {
	return {
		// 登出
		exit: (data) => dispatch(exit(data)),
		// 初始化左邊表單
		resetTreeFunctionId: () => dispatch(resetTreeFunctionId()), //初始化左邊表單
		setInitAlertCount: () => dispatch(setInitAlertCount()), //初始化告警筆數
		// 初始化總覽
		resetAllHomeData: () => dispatch(resetAllHomeData()), //初始化總覽
		// 初始化告警
		setInitAlertTheader: () => dispatch(setInitAlertTheader()), //初始化告警表格欄位
		resetAllAlert: () => dispatch(resetAllAlert()), //初始化告警(已解決)
		resetAllAlertUnsolved: () => dispatch(resetAllAlertUnsolved()), //初始化告警(未解決)
		// 初始化電池數據/歷史
		setInitBattData: () => dispatch(setInitBattData()), //初始化電池數據
		resetAllBattData: () => dispatch(resetAllBattData()), //初始化電池數據(歷史)篩選
		// 初始化電池組管理(1501)
		resetActiveNum_P1501: () => dispatch(resetActiveNum_P1501()),
		resetAll_P1501_Action: () => dispatch(resetAll_P1501_Action()),
		resetAll_P1505_Action: () => dispatch(resetAll_P1505_Action()),
		// 初始化站台管理(1502)
		resetActiveNum_P1502: () => dispatch(resetActiveNum_P1502()),
		resetAll_P1502_Action: () => dispatch(resetAll_P1502_Action()),
		// 初始化通訊序號管理(1503)
		resetActiveNum_P1503: () => dispatch(resetActiveNum_P1503()),
		resetAll_P1539_Action: () => dispatch(resetAll_P1539_Action()),
		// 初始化電池參數(1504)
		resetActiveNum_P1504: () => dispatch(resetActiveNum_P1504()),
		resetAll_P1504_Action: () => dispatch(resetAll_P1504_Action()),
		resetAll_P1559_Action: () => dispatch(resetAll_P1559_Action()),
		// 清空使用者設定(1700)
		resetActiveNum_USERMANAGE: () => dispatch(resetActiveNum_USERMANAGE()),
		// 初始化系統設定(1800)
		resetActiveNum_SYSYEMSETTINGS: () =>
			dispatch(resetActiveNum_SYSYEMSETTINGS()),
	};
};

export default connect(mapStateToProps, mapDispatchToProps)(App)
