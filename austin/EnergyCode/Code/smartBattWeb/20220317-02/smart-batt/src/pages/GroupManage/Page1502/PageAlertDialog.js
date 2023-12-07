import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import AlertDialog from '../../../components/AlertDialog';//彈跳視窗
import { apipath, ajax } from '../../../utils/ajax';
//內容
import ContentAddEdit from './ContentAddEdit';
import ContentDel from './ContentDel';
import ContentImport from './ContentImport';
import { CompareWindowns, inputList, initOpenData } from './InitDataFormat';//初始化格式
import { InputCheck } from '../../../utils/InputCheck';//輸入格式化

const apiUrl = apipath();
class PageAlertDialog extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            data: {
                ...initOpenData
            },
            companyList: [],
        }
    }
    componentDidMount() {
        if (CompareWindowns.Del.model !== this.props.windows) {
            this.getCompanyList();//取選單資料
        }
        this.setState({
            data: this.props.data,
        });

    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }
    handleSelectChange = (event) => {
        this.setState((prevState, props) => ({
            data: {
                ...prevState.data,
                Company: event.target.value,
            }
        }));
    }
    handleInputChange = (event) => {
        const { GroupID } = inputList;
        if (event.target.name === GroupID) {
            const { data } = this.state;
            const result = InputCheck(event.target.value);
            event.preventDefault();
            if (result === null) {
                event.target.value = data[event.target.name];
                return;
            }
            if (result.length === 1 && result[0] !== event.target.value) {
                event.target.value = data[event.target.name];
                return;
            }
            this.setState((prevState, props) => ({
                data: {
                    ...prevState.data,
                    [event.target.name]:  event.target.value,
                }
            }));
        } else {
            this.setState((prevState, props) => ({
                data: {
                    ...prevState.data,
                    [event.target.name]: event.target.value,
                }
            }));
        }
    }
    handleFileChange = (event) => {
        this.setState((prevState, props) => ({
            data: {
                ...prevState.data,
                File: event.target.files[0],
                FileName: event.target.files[0].name,
            }
        }));
    }

    render() {
        const { company, windows, title, open, handleClose, updResult, addResult } = this.props;
        const { companyList, data } = this.state;
        return (
            <AlertDialog
                title={title} //title
                open={open} //開啟視窗                
                handleClose={handleClose} //連棟關閉視窗
                handleSubmit={this.handleSubmit}
                onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
            >
                {//新增
                    (CompareWindowns.Add.model === windows) &&
                    <ContentAddEdit
                        data={data}
                        companyList={companyList}
                        company={company}
                        inputList={inputList}
                        isAdd={true}
                        handleSelectChange={(event) => this.handleSelectChange(event)}
                        handleInputChange={(event) => this.handleInputChange(event)}
                        errorMsg={addResult}
                    />
                }
                {//編輯
                    (CompareWindowns.Edit.model === windows) &&
                    <ContentAddEdit
                        data={data}
                        companyList={companyList}
                        company={company}
                        inputList={inputList}
                        handleSelectChange={(event) => this.handleSelectChange(event)}
                        handleInputChange={(event) => this.handleInputChange(event)}
                        errorMsg={updResult}
                    />
                }
                {//刪除
                    (CompareWindowns.Del.model === windows) && <ContentDel />
                }
                {//匯入
                    (CompareWindowns.Import.model === windows) &&
                    <ContentImport
                        data={data}
                        companyList={companyList}
                        company={company}
                        handleSelectChange={(event) => this.handleSelectChange(event)}
                        handleFileChange={(event) => this.handleFileChange(event)}
                    />
                }
            </AlertDialog>
        )
    }
    handleSubmit = () => {
        const { windows, handleEditSubmit, handleAddSubmit, handleDelSubmit, handleImportSubmit } = this.props;
        const { data } = this.state;
        switch (windows) {
            case CompareWindowns.Add.model://新增
                handleAddSubmit(data);
                break;
            case CompareWindowns.Edit.model://編輯
                handleEditSubmit(data);
                break;
            case CompareWindowns.Del.model://刪除
                handleDelSubmit();
                break;
            case CompareWindowns.Import.model://匯入
                handleImportSubmit(data);
                break;
            default:
        }
    }
    // get Data
    getCompanyList = () => {
        this.ajaxGetCompanyList().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                this.setState((prevState) => ({
                    companyList: [...List],
                    data: {
                        ...prevState.data,
                        Company: List?.length > 0 ? List[0].Value.toString() : this.props.company,
                    }
                }));
            } else {
                this.setState({ companyList: [] });
            }
        });
    }

    //公司下拉選單API(GET):
    ajaxGetCompanyList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getCompanyList`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //公司下拉選單API(GET):
        //https://www.gtething.tw/battery/getCompanyList
    }
    //站台匯入API(POST):
    ajaxImportBatteryGroup = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}importBatteryGroup`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //站台匯入API(POST):
        //https://www.gtething.tw/battery/importBatteryGroup
    }

}

PageAlertDialog.defaultProps = {
    windows: '',
    title: '',
    open: false,
    data: {},
    updResult: {},
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
    updResult: PropTypes.object,    //更新資料回傳訊息
    handleOpen: PropTypes.func,
    handleClose: PropTypes.func,
    handleSubmit: PropTypes.func,
    handleImportSubmit: PropTypes.func,
    onIsRefreshChange: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        username: state.LoginReducer.username,
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


