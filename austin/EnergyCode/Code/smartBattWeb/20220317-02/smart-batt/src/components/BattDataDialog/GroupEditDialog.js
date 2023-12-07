import React, {Component, Fragment} from 'react';
import { connect } from 'react-redux';
import { Trans } from 'react-i18next';
import { apipath, ajax } from "../../utils/ajax";
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';


const apiUrl = apipath();
class GroupEditDialog extends Component {
    constructor(props){
        super(props)
        this.state={
            DefaultGroup: 1,//(0:預設站台, 1:非預設站台)，判斷是否呈顯接續通訊序號選單
            isNB: false,    //是否顯示接續通訊序號(預設不顯示)
            groupData: [],  //下拉清單資料
            NBList: [],     //接續通訊序號資料清單
            curData: {},    //目前所選擇資料
            curGroupInternalID: this.props.data[0].GroupInternalID, //目前站台GroupInternalID
            defaultGroupName: this.props.data[0].GroupName,
            NBSelectedValue: '',//目前所選接續通訊序號
            message: '',
        }
    }



    componentDidMount(){
        const {NBID} = this.props.data[0];   //目前GroupInternalID,NBID
        const { curGroupInternalID } = this.state;
        this.getGroupSetup(curGroupInternalID,NBID);
    }
    componentDidUpdate(prevState){
        const {NBID} = this.props.data[0];   //目前GroupInternalID,NBID
        const {curGroupInternalID} = this.state;
        if(this.props.data !== prevState.data){
			this.getGroupSetup(curGroupInternalID,NBID);
		}
    }

    componentWillUnmount(){

    }


    render(){
        const {data,userCompany} = this.props;
        const {isNB,groupData,NBList,NBSelectedValue,curData,defaultGroupName,message} = this.state;
        return (
            <CusDialog
                open={true}
                maxWidth='sm'
                handleClose={()=>{this.props.onDialogClose()}}
            >
                <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                    <div className="col-6 p-0 d-inline-block"><Trans i18nKey="1416" /></div>
                    <div className="col-6 m-0 p-0 d-inline-block text-right">
                        <i className="fas fa-times" style={CloseBtnStyle} onClick={()=>{this.props.onDialogClose()}} />
                    </div>
                </DialogTitle>
                <DialogContent className="overflowY" style={DialogContentStyle}>
    
                    {
                        curData !== {} ? (
                            <Fragment>
                                {/* 公司別 */}
                                {
                                    // 當this.props.userCompany === 1,時才顯示公司別
                                    userCompany === "1" && 
                                    <div className="col-12 p-0 my-4">
                                        <div><Trans i18nKey="1064" /></div>
                                        <div>{data[0].Company}</div>
                                    </div>
                                }
                                {/* 站台名稱 */}
                                <div className="col-12 p-0 my-4">
                                    <div><Trans i18nKey="1013" /></div>
                                    <select
                                        className="form-control"
                                        value={curData.GroupName}
                                        onChange={(e)=>{this.handleChange(e)}}
                                        >
                                        {
                                            groupData &&
                                            groupData.length > 0 &&
                                            groupData.map( (item) => {
                                                return <option key={item.Seq} value={item.GroupName}>{item.GroupName}</option>
                                            })
                                        }
                                     </select>
                                </div>
                                {/* 站台編號 */}
                                <div className="col-12 p-0 my-4">
                                    <div><Trans i18nKey="1012" /></div>
                                    <div>{curData.GroupID}</div>
                                </div>
                                {/* 國家 */}
                                <div className="col-12 p-0 my-4">
                                    <div><Trans i18nKey="1028" /></div>
                                    <div>{curData.Country}</div>
                                </div>
                                {/* 地域 */}
                                <div className="col-12 p-0 my-4">
                                    <div><Trans i18nKey="1029" /></div>
                                    <div>{curData.Area}</div>
                                </div>
                                {/* 地址 */}
                                <div className="col-12 p-0 my-4">
                                    <div><Trans i18nKey="1031" /></div>
                                    <div>{curData.Address}</div>
                                </div>
                                {/* 接續通訊序號_isNB //DefaultGroup === 0(判斷如果是預設站台時) */}
                                { console.log('NBList',NBList)}
                                {
                                    isNB === true && NBList && NBList.length > 0 && (curData.GroupName !== defaultGroupName) && (curData.DefaultGroup === 1) && (
                                        <div className="col-12 p-0 my-4">
                                            <div><Trans i18nKey="1419" /></div>
                                            <select
                                                className="form-control"
                                                value={NBSelectedValue}
                                                onChange={(e)=>{this.handleNBChange(e)}}
                                                >
                                                {
                                                    NBList &&
                                                    NBList.length > 0 &&
                                                    NBList.map( (item,idx) => {
                                                        return <option key={idx} value={item.Value}>{item.Label}</option>
                                                    })
                                                }
                                            </select>
                                        </div>
                                    )
                                }
                                
                            </Fragment>
                        ): <div className="text-center">{message}</div>
                    }
                </DialogContent>
                <DialogActions style={DialogActionsStyle}>
                    {/* 取消 */}
                    <Button onClick={()=>{this.props.onDialogClose()}}><Trans i18nKey="1003" /></Button>
                    {/* 確認 */}
                    <CusMainBtnStyle name={<Trans i18nKey="1010" />} icon="fas fa-check" clickEvent={()=>{this.props.handleSubmit()}} />
                </DialogActions>
            </CusDialog>
        )
    }



    // 變更站台名稱(編號)
    handleChange = (e) => {
        const { isNB,groupData } = this.state;
        const value = e.target.value;
        const newlist = groupData.filter(filterItem => filterItem.GroupName === value);
        const groupLabel = `${newlist[0].GroupName}/${newlist[0].GroupID}`;
        // 變更顯示資訊
        this.setState({curData: newlist[0],curGroupInternalID:newlist[0].GroupInternalID})

        // 判斷是否顯示接續通訊序號 變更的站台不可為預設站台
        if( isNB === false ){
            this.setState({isNB: true})
        }

        // 變更站台名稱&站台名稱清單
        this.props.handleListChange({GroupInternalID:newlist[0].GroupInternalID,GroupLabel:groupLabel});
        // 變更NBList
        this.getNBList(newlist[0].GroupInternalID,"")

    }
    // 變更接續通訊序號
    handleNBChange = (e) => {
        this.setState({
            NBSelectedValue: e.target.value,
        })
        this.props.handleGroupListNBChange(e.target.value); //變更回傳清單接續通訊序號
    }
    // 取得站台設定清單
    getGroupSetup = (GroupInternalID,NBID) => {
        this.fetchGroupSetup().then( async(response)=>{
            if(response.code === '00'){
                console.log('%ccureent','background:yellow',response.msg)
                // 取得目前選擇資訊
                const curData = response.msg.Group.filter( filterItem => filterItem.GroupInternalID === GroupInternalID)[0];
                const nbid = NBID || '';
                await this.setState({
                    groupData: response.msg.Group,
                    DefaultGroup: curData.DefaultGroup,//(0:預設站台, 1:非預設站台)，判斷是否呈顯接續通訊序號選單
                    curData: curData,
                    message: '',
                })
                await this.getNBList(GroupInternalID,nbid);
            }else {
                this.setState({
                    groupData: [],
                    message: response.msg,
                })
            }
        })
    }
    // 取得接續通訊序號清單
    getNBList = (GroupInternalId,nbid) => {
        this.fetchNBList(GroupInternalId,nbid).then( (response) => {
            if(response.code === '00') {
                this.setState({
                    NBList: response.msg.NBList,
                })
            }else {
                this.setState({
                    NBList: [],
                })
            }
        })
    }
    // fetch站台設定清單
    fetchGroupSetup = () => {
        const { token,userCompany,timeZone,curLanguage} = this.props;
        const {CompanyCode} = this.props.data[0];
        const url = `${apiUrl}getGroupSetup?companyCode=${CompanyCode}`;
        return ajax(url,'GET',token,curLanguage,timeZone,userCompany)
    }
    // fetch 站台編輯 -電池數據-接續通訊序號清單
    fetchNBList = (GroupInternalId,nbid) => {
        const { token,userCompany,timeZone,curLanguage} = this.props;
        const url = `${apiUrl}getGroupNBList?groupInternalId=${GroupInternalId}&nbid=${nbid}`;
        return ajax(url,"GET",token,curLanguage,timeZone,userCompany);
    }
}



const mapStateToProps = (state,ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        userCompany: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,                    //目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        buttonControlList: state.BattDataReducer.buttonControlList,     //電池數據按鈕權限清單
        battTypeList: state.BattTypeListReducer.battTypeList,           //電池類型清單
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
	return {};
};
GroupEditDialog.defaultProps = {
    list: {},                       //回傳清單
    onDialogClose: () => {},
    handleSubmit: () => {},
    handleListChange: () => {},    //變更回傳清單
    handleGroupListNBChange: () => {},//變更回傳清單-接續通訊序號
}
export default connect(mapStateToProps,mapDispatchToProps)(GroupEditDialog);