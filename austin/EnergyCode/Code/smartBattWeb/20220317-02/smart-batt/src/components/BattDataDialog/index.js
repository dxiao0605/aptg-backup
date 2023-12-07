import React, { Fragment,Component } from 'react';
import { connect } from 'react-redux';
import {fetchBattTypeListLoad} from '../../actions/BattTypeListAction';
import {fetchLastBAListLoad,initialBAList} from '../../actions/LastBAListAction';
import {fetchLastBBListLoad,initialBBList} from '../../actions/LastBBListAction';
import {fetchLastB3ListLoad,initialB3List} from '../../actions/LastB3ListAction';
import {fetchLastB5ListLoad,initialB5List} from '../../actions/LastB5ListAction';
import {checkedInputValue} from '../../pages/Command/Page1504/PeriodSetting/utils';
import moment from "moment";
import BADialog from './BADialog';                                      //BA(時間週期設定)
import BBDialog from './BBDialog';                                      //BB(內阻設定測試值)
import B3Dialog from './B3Dialog';                                      //B3(校正內阻)
import B5Dialog from './B5Dialog';                                      //B5(校正電壓)
import GroupEditDialog from './GroupEditDialog';                        //站台設定
import BatteryEditDialog from './BatteryEditDialog';                    //電池組編輯




class BattDataDialog extends Component {
    constructor(props){
        super(props)
        this.state={
            openMsg: false,                     // 顯示隱藏訊息視窗
            msgTitle: '',                       // 訊息視窗標題
            message: '',                        // 成功訊息/錯誤訊息
            BAList: this.props.lastBAList,      // BA(186)設定清單
            BBList: this.props.lastBBList,      // BB(187)設定清單
            B3List: this.props.lastB3List,      // B3(179)設定清單
            B5List: this.props.lastB5List,      // B5(181)設定清單
            GroupList: {                        // 站台設定(1416)設定清單
                GroupInternalID: null,
                NBID: "",
                PreviousNBID: '',
                UserName: this.props.username,
            },
            BatteryList: {                      // 電池組編輯(1551)設定清單
                BatteryGroupID: '',
                BatteryType: {Label:'',Value:''},
                InstallDate: null,
                UserName: this.props.username,
            },
            BALastDate: this.props.lastBADate,  // 前次時間週期設定(BA)修改時間
            BBLastDate: this.props.lastBBDate,  // 前次內阻設定測試值(BB)修改時間
            B3LastDate: this.props.lastB3Date,  // 前次校正內阻(B3)修改時間
            B5LastDate: this.props.lastB5Date,  // 前次校正電壓(B5)修改時間
        }
    }


    componentDidMount(){
        const {token,curLanguage,timeZone,company,username,selectedData} = this.props;
        const batteryGroupID = '';
        
        
        // 電池類型下拉清單
        if(selectedData.length === 1 && selectedData[0].CompanyCode){
            const companyCode = selectedData[0].CompanyCode;
            this.props.fetchBattTypeListLoad({token,curLanguage,timeZone,company,companyCode});
        }
        
        
        // 當選則項目不為單一時,初始化設定清單[BA,BB,B3,B5,...]
        if(selectedData.length !== 1) {
            this.initialList(batteryGroupID,username);
        }
    }

    componentDidUpdate(prevProps) {
        const {
            selectedData,
            lastBAList,lastBADate,
            lastBBList,lastBBDate,
            lastB3List,lastB3Date,
            lastB5List,lastB5Date,
            username,dialogId,
            token,curLanguage,timeZone,company } = this.props;
        let batchCMDID = selectedData.map((item)=> {return item.BatteryGroupID});
        if(selectedData !== prevProps.selectedData || dialogId !== prevProps.dialogId){

            if(selectedData.length === 1){
                // 電池類型下拉清單
                if(dialogId === '1551'){
                    const companyCode = selectedData[0].CompanyCode;
                    this.props.fetchBattTypeListLoad({token,language:curLanguage,timezone:timeZone,company,companyCode});
                }
                // 取得下行指令清單(B3,B5,站台設定,電池組編輯)目前資料
                const CMDIDs = ['179','181','1416','1551']
                if(CMDIDs.includes(dialogId)){
                    this.getLastList(batchCMDID[0])
                }else{
                    this.getLastList(batchCMDID)
                }
            }
            else {
                this.initialList(batchCMDID,username)
            }
        }

        // 更新BA設定清單
        if(lastBAList !== prevProps.lastBAList || lastBADate !== prevProps.lastBADate) {
            this.setState({
                BAList: this.props.lastBAList,      // BA設定清單
                BALastDate: this.props.lastBADate,  // 前次時間週期設定(BA)修改時間
            })
        }

        // 更新BB設定清單
        if(lastBBList !== prevProps.lastBBList || lastBBDate !== prevProps.lastBBDate) {
            this.setState({
                BBList: this.props.lastBBList,      // BB設定清單
                BBLastDate: this.props.lastBBDate,  // 前次內阻設定測試值(BB)修改時間
            })
        }

        // 更新B3設定清單
        if(lastB3List !== prevProps.lastB3List || lastB3Date !== prevProps.lastB3Date) {
            this.setState({
                B3List: this.props.lastB3List,      // B3設定清單
                B3LastDate: this.props.lastB3Date,  // 前次校正內阻(B3)修改時間
            })
        }

        // 更新B5設定清單
        if(lastB5List !== prevProps.lastB5List || lastB5Date !== prevProps.lastB5Date) {
            this.setState({
                B5List: this.props.lastB5List,      // B5設定清單
                B5LastDate: this.props.lastB5Date,  // 前次校正電壓(B5)修改時間
            })
        }
    }
    
    render() {
        const {
            perPage,                // 表格每面呈現幾筆
            dialogId,               // 選則顯示彈跳視窗(批次)(BA,BB,B3,B5,...)
            selectedData,           // 己選取項目清單
            source,                 // 從哪開啟(BattGroup,BattData)
            onDialogClose,          // 取消全選
            handleSubmit,           // 送出命令清單
            buttonControlList,      // 權限清單
        } = this.props;
        const {
            BAList,                 // BA設定清單
            BBList,                 // BB設定清單
            B3List,                 // B3設定清單
            B5List,                 // B5設定清單
            GroupList,              // 站台設定清單(1416)
            BatteryList,            // 電池組編輯清單(1551)
            BALastDate,             // 前次時間週期設定(BA)修改時間
            BBLastDate,             // 前次內阻設定測試值(BB)修改時間
            B3LastDate,             // 前次校正內阻(B3)修改時間
            B5LastDate,             // 前次校正電壓(B5)修改時間
        } = this.state;

        return (
            <Fragment>
                {(
                    ()=>{
                        switch(dialogId){
                            case '':
                                return ''
                            case '186': //BA(時間週期設定)
                                return <BADialog 
                                        data={selectedData}
                                        list={BAList}                           //api回傳清單
                                        lastDate={BALastDate}                   //最後修改日期
                                        perPage={perPage}
                                        role={buttonControlList['BA']}
                                        source={source}
                                        onDialogClose={onDialogClose}
                                        handleInputChange={(e)=>{this.handleInputChange(e)}}
                                        handleSubmit={()=>{handleSubmit('186',BAList)}} />
                            case '187': //BB(內阻設定測試值)
                                return <BBDialog 
                                        data={selectedData}
                                        list={BBList}                           //api回傳清單
                                        lastDate={BBLastDate}                   //最後修改日期
                                        perPage={perPage}
                                        role={buttonControlList['BB']}
                                        source={source}
                                        onDialogClose={onDialogClose}
                                        handleInputChange={(e)=>{this.handleInputChange(e)}}
                                        handleSubmit={()=>{handleSubmit('187',BBList)}} />
                            case '181': //校正電壓
                                return <B5Dialog 
                                        data={selectedData}
                                        list={B5List}                           //api回傳清單
                                        lastDate={B5LastDate}                   //最後修改日期
                                        perPage={perPage}
                                        role={buttonControlList['B5']}
                                        onDialogClose={onDialogClose}
                                        handleInputChange={(e)=>{this.handleInputChange(e)}}
                                        handleSubmit={()=>{handleSubmit('181',B5List)}} />
                            case '179': //校正內阻
                                return <B3Dialog 
                                        data={selectedData}
                                        list={B3List}                           //api回傳清單
                                        lastDate={B3LastDate}                   //最後修改日期
                                        perPage={perPage}
                                        role={buttonControlList['B3']}
                                        onDialogClose={onDialogClose}
                                        handleInputChange={(e)=>{this.handleInputChange(e)}}
                                        handleSubmit={()=>{handleSubmit('179',B3List)}} />
                            case '1416': //站台設定
                                return <GroupEditDialog 
                                        data={selectedData}
                                        // list={GroupList}                        //api回傳清單
                                        perPage={perPage}
                                        role={buttonControlList['GroupEdit']}
                                        onDialogClose={onDialogClose}
                                        handleListChange={this.handleGroupListChange}
                                        handleGroupListNBChange={this.handleGroupListNBChange}
                                        handleSubmit={()=>{handleSubmit('1416',GroupList)}} />
                            case '1551'://電池組編輯
                                return <BatteryEditDialog 
                                        list={BatteryList}                       //api回傳清單
                                        BatteryTypeNameList={this.props.battTypeList}
                                        perPage={perPage}
                                        role={buttonControlList['BattEdit']}
                                        onDialogClose={onDialogClose}
                                        handleSelectChange={(e)=>{this.handleSelectChange(e)}}
                                        handleDateChange={(e)=>{this.handleDateChange(e)}}
                                        handleInputChange={(e)=>{this.handleInputChange(e)}}
                                        handleSubmit={()=>{handleSubmit('1551',BatteryList)}} />
                            default:
                                return ''
                        }
                    }
                )()}

            </Fragment>
        )
    }





    // 初始化資料
    initialList = (batteryGroupID,username) => {
        this.props.initialBAList({batteryGroupID,username});
        this.props.initialBBList({batteryGroupID,username});
        this.props.initialB3List({batteryGroupID,username});
        this.props.initialB5List({batteryGroupID,username});
    }

    // 目前清單
    getLastList = (batteryGroupID) => {
        const {token,curLanguage,timeZone,company,username,dialogId,selectedData} = this.props;
        const GroupInternalID = selectedData[0].GroupInternalID;
        switch(dialogId){
            case '186': // 最後編輯BA設定清單
                return this.props.fetchLastBAListLoad({
                    token,
                    curLanguage,
                    timeZone,
                    company,
                    batteryGroupID,
                    username
                })
            case '187': // 最後編輯BB設定清單
                return this.props.fetchLastBBListLoad({
                    token,
                    curLanguage,
                    timeZone,
                    company,
                    batteryGroupID,
                    username
                })
            case '179': // 最後編輯B3設定清單
                return this.props.fetchLastB3ListLoad({
                    token,
                    curLanguage,
                    timeZone,
                    company,
                    batteryGroupID,
                    username
                })
            case '181': // 最後編輯B5設定清單
                return this.props.fetchLastB5ListLoad({
                    token,
                    curLanguage,
                    timeZone,
                    company,
                    batteryGroupID,
                    username
                })
            case '1416': // 站台設定清單(Group)
                return this.getGroupList(GroupInternalID,username)
            case '1551': // 電池組編輯清單(Battery)
                return this.getBatteryList(batteryGroupID,username)
            default:
                return this.initialList(batteryGroupID,username)
        }
    }

    // 取得站台設定資料(1416)
    getGroupList = (GroupInternalID,username) => {
        const {selectedData} = this.props;
        this.setState({
            GroupList: {
                GroupInternalID: GroupInternalID,
                GroupLabel: '', //變更站台時使用,讀取時為空
                NBID: selectedData[0].NBID,
                PreviousNBID: '',
                UserName: username
            }
        })
    }
    // 取得電池編輯資料(先以新增為主，handleSumbit時再判斷是否存在於batteryList)
    getBatteryList = (batteryGroupID,username) => {
        const {selectedData,battTypeList} = this.props;
        // 判斷list.BatteryType 是否已存在於BattTypeList內(電池組型號是否已存在於battTypeList)
        const battTypeLabel = selectedData[0].BatteryType;
        const battTypeCode = battTypeList.filter(filterItem => filterItem.Label === battTypeLabel).map( item => item.Value)[0]
        if(selectedData[0].InstallDate === "Invalid date") {
            this.setState({
                BatteryList: {
                    BatteryGroupID: batteryGroupID,
                    BatteryType: {
                        Label:battTypeLabel,
                        Value:`${battTypeCode}`
                    },
                    InstallDate: null,
                    UserName: username,
                }
            })
        }else{
            this.setState({
                BatteryList: {
                    BatteryGroupID: batteryGroupID,
                    BatteryType: {
                        Label:battTypeLabel,
                        Value:`${battTypeCode}`
                    },
                    InstallDate: moment(selectedData[0].InstallDate).format('YYYY-MM-DD'),
                    UserName: username,
                }
            })
        }
        


    }


    // 變更站台設定清單(1416)
    handleGroupListChange = (data) => {
        const { GroupInternalID,GroupLabel } = data
        this.setState((prevState) => ({
            GroupList: {
                ...prevState.GroupList,
                GroupInternalID: GroupInternalID,
                GroupLabel: GroupLabel,
            }
        }))
    }
    // 變更站台設定-接續通訊序號(1416)
    handleGroupListNBChange = (PreviousNBID) => {
        this.setState((prevState) => ({
            GroupList: {
                ...prevState.GroupList,
                PreviousNBID:PreviousNBID,
            }
        }))
    }

    // 更新設定清單,當輪入數值符合條件變更數值(僅判斷最大數值和長度)
    handleInputChange = (e) => {
        const {dialogId} = this.props;
        const {name,value} = e.target;
        let newValue = checkedInputValue(dialogId,value,name,true);
        if(dialogId === '186'){ //BA
            if(newValue !== undefined){
                this.setState((prevState) =>({
                    BAList:{
                        ...prevState.BAList,
                        [name]: parseFloat(newValue),
                    }
                }))
            }
        }
        else if (dialogId === '187'){ //BB
            if(newValue !== undefined){
                this.setState((prevState) =>({
                    BBList:{
                        ...prevState.BBList,
                        [name]: parseFloat(newValue),
                    }
                }))
            }
        }
        else if (dialogId === '179'){ //B3
            if(newValue !== undefined){
                const { B3List } = this.state;
            const oldList = [...B3List.IR];
            const newList = [];
            oldList.map( (item,idx) => {
                if(`${idx}` === name){
                    return newList.push(parseFloat(value))
                }else{
                    return newList.push(parseFloat(item))
                }
            })
            this.setState( (prevState) => ({
                B3List: {
                    ...prevState.B3List,
                    IR: newList
                }
            }))
            }
        }
        else if (dialogId === '181'){ //B5
            if(newValue !== undefined){
                const { B5List } = this.state;
                const oldList = [...B5List.Vol];
                const newList = [];
                oldList.map( (item,idx) => {
                    if(`${idx}` === name){
                        return newList.push(parseFloat(value))
                    }else{
                        return newList.push(parseFloat(item))
                    }
                })
                this.setState( (prevState) => ({
                    B5List: {
                        ...prevState.B5List,
                        Vol: newList
                    }
                }))
            }
        }
        else if (dialogId === '1551'){
            this.setState((prevState) =>({
                BatteryList:{
                    ...prevState.BatteryList,
                    [name]: value,
                }
            }))

        }
    }

    // 更新select
    handleSelectChange = (e) => {
        const {battTypeList} = this.props;
        const {value} = e.target;
        const newType = battTypeList.filter( (filterItem => filterItem.Label === value)).map( (item) => item.Value);
        this.setState((prevState) => ({
            BatteryList: {
                ...prevState.BatteryList,
                BatteryType: {Label:value,Value:newType[0]},
            }
        }))

    }

    // 更新Date
    handleDateChange = (e) => {
        const InstallDate = moment(e).format('YYYY-MM-DD');
        this.setState((prevState) =>({
            BatteryList: {
                ...prevState.BatteryList,
                InstallDate: InstallDate,
            }
        }))
    }


}
const mapStateToProps = (state,ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,                    //目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        buttonControlList: state.BattDataReducer.buttonControlList,     //電池數據按鈕權限清單
        battTypeList: state.BattTypeListReducer.battTypeList,           //電池類型清單
        
        lastBAList: state.LastBAListReducer.lastBAList,                 //BA前次設定清單
        lastBADate: state.LastBAListReducer.lastBADate,                 //BA前次設定日期
        lastBAListError: state.LastBAListReducer.lastBAListError,       //BA前次設定清單錯誤訊息

        lastBBList: state.LastBBListReducer.lastBBList,                 //BB前次設定清單
        lastBBDate: state.LastBBListReducer.lastBBDate,                 //BB前次設定日期
        lastBBListError: state.LastBBListReducer.lastBBListError,       //BB前次設定清單錯誤訊息

        lastB3List: state.LastB3ListReducer.lastB3List,                 //B3前次設定清單
        lastB3Date: state.LastB3ListReducer.lastB3Date,                 //B3前次設定日期
        lastB3ListError: state.LastB3ListReducer.lastB3ListError,       //B3前次設定清單錯誤訊息

        lastB5List: state.LastB5ListReducer.lastB5List,                 //B5前次設定清單
        lastB5Date: state.LastB5ListReducer.lastB5Date,                 //B5前次設定日期
        lastB5ListError: state.LastB5ListReducer.lastB5ListError,       //B5前次設定清單錯誤訊息
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
	return {
        fetchBattTypeListLoad: (data) => dispatch(fetchBattTypeListLoad(data)),
        // initial List
        initialBAList: (list) => dispatch(initialBAList(list)),
        initialBBList: (list) => dispatch(initialBBList(list)),
        initialB3List: (list) => dispatch(initialB3List(list)),
        initialB5List: (list) => dispatch(initialB5List(list)),
        fetchLastBAListLoad: (data) => dispatch(fetchLastBAListLoad(data)),
        fetchLastBBListLoad: (data) => dispatch(fetchLastBBListLoad(data)),
        fetchLastB3ListLoad: (data) => dispatch(fetchLastB3ListLoad(data)),
        fetchLastB5ListLoad: (data) => dispatch(fetchLastB5ListLoad(data)),
	};
};
BattDataDialog.defaultProps = {
    isDisabledBtn: false,           // 防止重複點擊問題
    perPage: 10,                    // 表格每面呈現幾筆
    dialogId: '',                   // 選則顯示彈跳視窗(批次)(BA,BB,B3,B5,Group,Battery...)
    selectedData: [],               // 己選取項目清單
    companyList:[],                 // 公司清單 
    company:'',                     // 選則公司
    onDialogClose: ()=>{},          // 取消全選
    handleSubmit:() => {},          // 發送批次BA,BB,B3,B5,Group站台,Battery電池組指令
}
export default connect(mapStateToProps,mapDispatchToProps)(BattDataDialog);