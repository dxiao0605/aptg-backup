import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import AlertDialog from '../../../components/AlertDialog';//彈跳視窗
import { apipath, ajax } from '../../../utils/ajax';
//內容
import ContentAddEdit from './ContentAddEdit';
import { CompareWindowns, inputList } from './InitDataFormat';//初始化格式
import { InputCheck } from '../../../utils/InputCheck';//輸入格式化
import './PageAlertDialog.css';

const apiUrl = apipath();
class PageAlertDialog extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            date: new Date(),
            BatteryGroupIDValue: '',
            BatteryTypeValue: '',
            BatteryTypeJSON: {
                Value: '',
                Label: ''
            },
            BatteryTypeNameList: [],
        }
    }
    componentDidMount() {
        const { InstallDate, BatteryGroupID, BatteryTypeName } = this.props.data;
        const date = new Date(InstallDate);
        this.setState({
            date: date,
            BatteryGroupIDValue: BatteryGroupID,
            BatteryTypeValue: BatteryTypeName,
        });
        this.getBattTypeList(BatteryTypeName);//取選單資料

    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }
    handleDateChange = (date) => {
        this.setState({ date: date });
    };
    handleInputChange = (event) => {
        const { BatteryGroupID } = inputList;
        switch (event.target.name) {
            case BatteryGroupID:
                const result = InputCheck(event.target.value);
                event.preventDefault();
                if (result === null) {
                    event.target.value = this.state[BatteryGroupID + "Value"];
                    return;
                }
                if (result.length === 1 && result[0] !== event.target.value) {
                    event.target.value = this.state[BatteryGroupID + "Value"];
                    return;
                }
                this.setState({ [BatteryGroupID + "Value"]: event.target.value });
                break;
            default:
        }
    }
    handleSelectChange = (event) => {
        const { BatteryTypeNameList } = this.state;
        const keyList = BatteryTypeNameList.filter(item => item.Label === event.target.value);
        this.setState((prevState, props) => ({
            BatteryTypeValue: event.target.value,
            BatteryTypeJSON: {
                Value: keyList.length === 0 ? "" : keyList[0].Value,
                Label: keyList.length === 0 ? event.target.value : keyList[0].Label
            }
        }));
    }

    render() {
        const { windows, title, open, handleClose } = this.props;
        const { date, BatteryGroupIDValue, BatteryTypeValue, BatteryTypeNameList } = this.state;
        return (
            <AlertDialog
                className={'no_overflow'}
                title={title} //title
                open={open} //開啟視窗                
                handleClose={handleClose} //連棟關閉視窗
                handleSubmit={this.handleSubmit}
                onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
            >
                {//編輯
                    (CompareWindowns.Edit.model === windows) &&
                    <ContentAddEdit
                        data={{ date, BatteryGroupIDValue, BatteryTypeValue }}
                        BatteryTypeNameList={BatteryTypeNameList}
                        inputList={inputList}
                        handleSelectChange={(event) => this.handleSelectChange(event)}
                        handleInputChange={(event) => this.handleInputChange(event)}
                        handleDateChange={(date) => this.handleDateChange(date)}
                    />
                }
            </AlertDialog>
        )
    }
    handleSubmit = () => {
        const { windows, handleEditSubmit } = this.props;
        const { BatteryGroupIDValue, date, BatteryTypeJSON } = this.state;
        switch (windows) {
            case CompareWindowns.Edit.model://編輯
                handleEditSubmit({ BatteryGroupIDValue, date, BatteryTypeJSON });
                break;
            default:
        }
    }
    // get Data
    getBattTypeList = (BatteryTypeName) => {
        this.ajaxGetBattTypeList().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                const newList = List.filter(item => item.Value !== "N");
                let keyList = [];
                if (BatteryTypeName !== undefined) {
                    keyList = newList.filter(item => item.Label === BatteryTypeName);
                }
                this.setState({
                    BatteryTypeNameList: [...newList],
                    BatteryTypeJSON: {
                        Value: keyList.length > 0 ? keyList[0].Value : '',
                        Label: keyList.length > 0 ? keyList[0].Label : ''
                    }
                })
            } else if (code === '07') {
                this.setState({ BatteryTypeNameList: [], BatteryTypeJSON: { Value: '', Label: '' } });
            } else {
                console.error('電池組管理GetBattTypeList', response);
                this.setState({ BatteryTypeNameList: [], BatteryTypeJSON: { Value: '', Label: '' } });
            }
        });
    }

    //電池型號下拉選單API:
    ajaxGetBattTypeList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const { CompanyCode } = this.props.data;
        const url = `${apiUrl}getBattTypeList?companyCode=${CompanyCode}`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //電池型號下拉選單API:
        //https://www.gtething.tw/battery/getBattTypeList?companyCode=2
    }

}

PageAlertDialog.defaultProps = {
    windows: '',
    title: '',
    open: false,
    data: {},
    handleOpen: () => { },
    handleClose: () => { },
    handleSubmit: () => { },
    handleImportSubmit: () => { },
    onIsRefreshChange: () => { },
}
PageAlertDialog.propTypes = {
    windows: PropTypes.string,
    title: PropTypes.string,
    open: PropTypes.bool,
    data: PropTypes.object,
    handleOpen: PropTypes.func,
    handleClose: PropTypes.func,
    handleSubmit: PropTypes.func,
    handleImportSubmit: PropTypes.func,
    onIsRefreshChange: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(PageAlertDialog);


