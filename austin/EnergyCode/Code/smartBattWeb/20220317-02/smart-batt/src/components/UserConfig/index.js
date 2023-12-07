import React, { Component, Fragment } from "react";
import { connect } from "react-redux";
import { exit, changeCurLanguage, changeLanguage, changeTimeZone } from '../../actions/LoginAction';
import { setInitAlertCount } from '../../actions/AlertCountAction';
import { setInitAlertTheader } from '../../actions/AlertAction';
import { setInitBattData } from '../../actions/BattDataAction';
import { resetTreeFunctionId } from '../../actions/MainNavAction';
//清空篩選欄位
import { resetAllHomeData } from '../../actions/HomeFilterAction';
import { resetAllAlert } from '../../actions/AlertFilterAction';
import { resetAllAlertUnsolved } from '../../actions/AlertUnsolvedFilterAction';
import { resetAllBattData } from '../../actions/BattFilterAction';
//電池管理介面
import { resetActiveNum_P1501 } from '../../actions/BattManageAction';//1501
import { resetAll_P1501_Action } from '../../actions/BattManageP1501Action';
import { resetAll_P1505_Action } from '../../actions/BattManageP1505Action';
import { resetActiveNum_P1502 } from '../../actions/GroupManageAction';//1502
import { resetAll_P1502_Action } from '../../actions/GroupManageP1502Action';
import { resetActiveNum_P1503 } from '../../actions/NBManageAction';//1503
import { resetAll_P1539_Action } from '../../actions/NBManageP1539Action';
import { resetAll_P1572_Action } from '../../actions/NBManageP1572Action';
import { resetActiveNum_P1504 } from '../../actions/CommandAction';//1504
import { resetAll_P1504_Action } from '../../actions/CommandP1504Action';
import { resetAll_P1559_Action } from '../../actions/CommandP1559Action';
// 使用者設定
import { resetActiveNum_USERMANAGE } from '../../actions/UserManageAction';//1700
//系統設定
import { resetActiveNum_SYSYEMSETTINGS } from '../../actions/SystemSettingsAction';//1800

// component
import FormControl from "@material-ui/core/FormControl";
import Button from '@material-ui/core/Button';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import MenuItem from '@material-ui/core/MenuItem';
import { StyledMenu } from '../CusMenuStyle';
import DialogChangePw from './DialogChangePw';
import DialogChangeSetting from './DialogChangeSetting';
// i18n
import { Translation } from 'react-i18next';
// icon
import PersonIcon from '@material-ui/icons/Person';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowDropUpIcon from '@material-ui/icons/ArrowDropUp';



// 用戶設定(語系、時區)
class UserConfig extends Component {
	constructor(props) {
		super(props)
		this.state = {
			anchorEl: null, //展開選單欄
			isChangePWOpen: false, //顯示變更密碼彈跳視窗
			isChangeSetOpen: false, //顯示變更密碼彈跳視窗

		}
	}
	render() {
		const { token, account, username, language, timeZone, languageList, languageListErrorMsg, timeZoneList, timeZoneListErrorMsg } = this.props;
		const { anchorEl, isChangePWOpen, isChangeSetOpen } = this.state;
		return (
			<Fragment>
				{/* Menu */}
				<FormControl className="userConfig-margin">
					<Button onClick={(event) => { this.handleClick(event) }} className="userConfig-margin" style={{ padding: '3px', textTransform: 'none' }}>
						<PersonIcon className="mr-2" /> {username}
						{Boolean(anchorEl) === true ? <ArrowDropUpIcon /> : <ArrowDropDownIcon />}
					</Button>
					<StyledMenu
						id="customized-menu"
						anchorEl={anchorEl}
						keepMounted
						open={Boolean(anchorEl)}
						onClose={this.handleClose} >
						{/* 更改密碼 */}
						<MenuItem onClick={this.handleChangePWOpen}>
							<ListItemIcon> <i className="fas fa-key" /> </ListItemIcon>
							<ListItemText primary={
								<Translation>
									{(t) => <>{t('1112')}</>}
								</Translation>
							} />
						</MenuItem>
						{/* 設置 */}
						<MenuItem onClick={this.handleChangeSetOpen}>
							<ListItemIcon> <i className="fas fa-cog" /> </ListItemIcon>
							<ListItemText primary={
								<Translation>
									{(t) => <>{t('1113')}</>}
								</Translation>
							} />
						</MenuItem>
						{/* 登出 */}
						<MenuItem onClick={this.onSubmitLogout}>
							<ListItemIcon> <i className="fas fa-sign-out-alt" /> </ListItemIcon>
							<ListItemText primary={
								<Translation>
									{(t) => <>{t('1114')}</>}
								</Translation>
							} />
						</MenuItem>
						{/* <MenuItem>
							<ListItemIcon> <i className="fas fa-question-circle" /> </ListItemIcon>
							<ListItemText primary="幫助" />
						</MenuItem> */}
						{/* <div className="text-center user_configInfo--versionText"><i className="fas fa-globe-asia mr-4" />Vision 1.0.0</div> */}
					</StyledMenu>
				</FormControl>

				{/* Dialog */}
				{/* 變更密碼 */}
				<DialogChangePw
					token={token}
					account={account}
					language={language}
					timeZone={timeZone}
					open={isChangePWOpen} 	//是否展開變更密碼視窗
					handleOpen={this.handleChangePWOpen}
					handleClose={this.handleChangePWClose}
					handleLogout={this.onSubmitLogout}
				/>
				{/* 用戶設定 */}
				<DialogChangeSetting
					token={token}
					account={account}
					language={language}
					timeZone={timeZone}
					languageList={languageList}
					languageListErrorMsg={languageListErrorMsg} 	//語系清單錯誤訊息
					timeZoneList={timeZoneList}
					timeZoneListErrorMsg={timeZoneListErrorMsg}
					open={isChangeSetOpen} 	//是否展開用戶設定視窗
					handleOpen={this.handleChangeSetOpen}
					handleClose={this.handleChangeSetClose}
					updCurLanguage={this.props.changeCurLanguage}	//變更目前語系
					updLanguage={this.props.changeLanguage}	//變更預設語系
					updTimeZone={this.props.changeTimeZone}	//變更預設時區
				/>

			</Fragment>
		);
	}



	// function
	// 開啟選單
	handleClick = (event) => {
		this.setState({ anchorEl: event.currentTarget })
	};
	// 關閉選單
	handleClose = () => {
		this.setState({ anchorEl: null })
	};
	// 顯示變更密碼彈跳視窗
	handleChangePWOpen = () => {
		this.setState({ isChangePWOpen: true })
	}
	// 關閉變更密碼彈跳視窗
	handleChangePWClose = () => {
		this.setState({ isChangePWOpen: false })
	}
	// 顯示變更設定(時區、語系)彈跳視窗
	handleChangeSetOpen = () => {
		this.setState({ isChangeSetOpen: true })
	}
	// 關閉變更設定(時區、語系)彈跳視窗
	handleChangeSetClose = () => {
		this.setState({ isChangeSetOpen: false })
	}
	//登出
	onSubmitLogout = () => {
		localStorage.clear();
		this.props.resetTreeFunctionId();//初始化左側表單
		this.props.setInitAlertCount();//初始化左側表單告警筆數

		// 初始化總覽
		this.props.resetAllHomeData();//初始化總覽篩選欄位
		// 初始化告警
		this.props.setInitAlertTheader();//初始化告警表格標題欄位
		this.props.resetAllAlert();//初始化告警(已解決)篩選欄位
		this.props.resetAllAlertUnsolved();//初始化告警(未解決)篩選欄位
		// 初始化電池數據/歷史
		this.props.setInitBattData();//初始化電池數據
		this.props.resetAllBattData();//初始化電池數據,電池歷史篩選欄位

		// 初始化電池組管理(1501)
		this.props.resetActiveNum_P1501();//初始化頁籤
		this.props.resetAll_P1501_Action();//初始化電池組管理
		this.props.resetAll_P1505_Action();//初始化電池型號管理
		// 初始化站台管理(1502)
		this.props.resetActiveNum_P1502();//初始化頁籤
		this.props.resetAll_P1502_Action();//初始化站台管理
		// 初始化通訊序號管理(1503)
		this.props.resetActiveNum_P1503();//初始化頁籤
		this.props.resetAll_P1539_Action();//初始化異動紀錄
		this.props.resetAll_P1572_Action();//初始化接續歷史
		// 清空電池參數(1504)
		this.props.resetActiveNum_P1504();//初始化頁籤
		this.props.resetAll_P1504_Action();//初始化參數設定
		this.props.resetAll_P1559_Action();//初始化參數歷史
		// 清空使用者設定(1700)
		this.props.resetActiveNum_USERMANAGE();//初始化頁籤
		// 清空系統設定(1800)
		this.props.resetActiveNum_SYSYEMSETTINGS();//初始化頁籤

		this.props.exit();
	};
}



const mapStateToProps = (state, ownProps) => {
	return {
		token: state.LoginReducer.token,
		account: state.LoginReducer.account,
		username: state.LoginReducer.username,
		role: state.LoginReducer.role,
		company: state.LoginReducer.company,
		timeZone: state.LoginReducer.timeZone,
		language: state.LoginReducer.language,
		curLanguage: state.LoginReducer.curLanguage,
		functionList: state.LoginReducer.functionList,
		data: state.LoginReducer.data,
		languageList: state.LanguageListReducer.languageList,	//語系清單
		languageListErrorMsg: state.LanguageListReducer.languageListErrorMsg,	//語系清單錯誤訊息
		timeZoneList: state.TimeZoneListReducer.timeZoneList,	//時區清單
		timeZoneListErrorMsg: state.TimeZoneListReducer.timeZoneListErrorMsg,	//時區清單錯誤訊息
	};
};
const mapDispatchToProps = (dispatch, ownProps) => {
	return {
		// 取得時區&語系下拉清單(Header/index.js內已取得)
		changeCurLanguage: (value) => dispatch(changeCurLanguage(value)),	//變更目前語系
		changeLanguage: (value) => dispatch(changeLanguage(value)),	//變更預設語系
		changeTimeZone: (value) => dispatch(changeTimeZone(value)),	//變更預設時區
		// 登出
		exit: (data) => dispatch(exit(data)),
		// 初始化左邊表單
		resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),//初始化左邊表單
		setInitAlertCount: () => dispatch(setInitAlertCount()),//初始化告警筆數
		// 初始化總覽
		resetAllHomeData: () => dispatch(resetAllHomeData()),//初始化總覽
		// 初始化告警
		setInitAlertTheader: () => dispatch(setInitAlertTheader()),//初始化告警表格欄位
		resetAllAlert: () => dispatch(resetAllAlert()),//初始化告警(已解決)
		resetAllAlertUnsolved: () => dispatch(resetAllAlertUnsolved()),//初始化告警(未解決)
		// 初始化電池數據/歷史
		setInitBattData: () => dispatch(setInitBattData()),	//初始化電池數據
		resetAllBattData: () => dispatch(resetAllBattData()),//初始化電池數據(歷史)篩選
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
		resetAll_P1572_Action: () => dispatch(resetAll_P1572_Action()),
		// 初始化電池參數(1504)
		resetActiveNum_P1504: () => dispatch(resetActiveNum_P1504()),
		resetAll_P1504_Action: () => dispatch(resetAll_P1504_Action()),
		resetAll_P1559_Action: () => dispatch(resetAll_P1559_Action()),
		// 清空使用者設定(1700)		
		resetActiveNum_USERMANAGE: () => dispatch(resetActiveNum_USERMANAGE()),
		// 初始化系統設定(1800)
		resetActiveNum_SYSYEMSETTINGS: () => dispatch(resetActiveNum_SYSYEMSETTINGS()),
		

	};
};

export default connect(mapStateToProps, mapDispatchToProps)(UserConfig);
