import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { setAreaList, setGroupList, setSubmitKeyList } from '../../../actions/CommandP1504Action';
import { FilterNames, initLastBB, initLastBA, initLastB5, initLastB3 } from './InitDataFormat';
import { 
    ajaxGetCompanyList, ajaxGetCommandList, ajaxGetCommandBattIdList, //下拉選單
    ajaxGetIRSetting, ajaxGetPeriodSetting, ajaxGetCorrectionVol, ajaxGetCorrectionIR, //參數設定
    ajaxAddIRSettingBatch, ajaxAddPeriodSettingBatch, ajaxAddCorrectionVolTask, ajaxAddCorrectionIRTask,//新增
} from './getApi';

import CommandRadio from './CommandRadio';//指令
import CommandSelect from './CommandSelect';
import PeriodSetting from './PeriodSetting';//參數


class Filter extends Component {
    // 參數設定歷史
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props)
        this.state = {
            radioList: [//指令清單
                { Value: "BB", Label: "187" },
                { Value: "BA", Label: "186" },
                { Value: "B5", Label: "181" },
                { Value: "B3", Label: "179" },
            ],
            radioVal: "BB",//指令-val值 commandId
            companyList: [],//公司別清單
            companyVal: [],//公司別-val值
            areaList: [],//國家/地域清單
            areaVal: [],//國家/地域-val值
            groupList: [],//站台清單
            groupVal: [],//站台-val值
            batteryGroupIdList: [],//電池組ID清單
            batteryGroupIdVal: [],//電池組ID-val值            
            batteryGroupIdMultiple: true,//電池組ID-控制單選或多選
            isShowPeriodSetting: false,//顯示參數

        }
    }


    // React Lifecycle
    componentDidMount() {
        const { radioVal } = this.state;
        this.getRefreshList({radioVal});
    }
    componentDidUpdate(prevProps, prevState) {
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { radioList, radioVal } = this.state;//指令
        const { companyList, companyVal } = this.state;//公司別
        const { areaList, areaVal } = this.state;//國家/地域
        const { groupList, groupVal } = this.state;//站台
        const { batteryGroupIdList, batteryGroupIdVal, batteryGroupIdMultiple } = this.state;//電池組ID
        const { isShowPeriodSetting, periodSetting } = this.state;//參數
        return (
            <Fragment>
                {/* 電池參數設定 */}
                <div className="col-12 pt-4 pb-4 pl-0 pr-0">
                    {/* 電池參數設定 */}
                    <div className="mb-2 font-weight-bold col-12" style={{ backgroundColor: '#525b6c', color: '#fff', lineHeight: '3' }}><Trans i18nKey={'1504'} /></div>
                    {/* 指令 */}
                    <div className="col-xl-11 col-12 px-0">
                        <div className="mb-1 d-flex align-items-center">
                            <div className="col-xl-2 col-lg-2 col-4 text-align-end"><Trans i18nKey={FilterNames.CommandI18nKey} /></div>
                            <CommandRadio
                                defRadioVal={radioVal}
                                defRadioList={radioList}
                                // onClickHandler={(value) => { this.onClickCommandHandler(value) }}
                                onClickHandler={(value) => {this.getRefreshList({radioVal:value})}}
                            />
                        </div>
                        {/* 公司 */}
                        {
                            this.props.company === "1" &&
                            <div className="mb-1 pb-2 form-inline">
                                <div className="col-xl-2 col-lg-2 col-4"><Trans i18nKey={FilterNames.CompanyI18nKey} /></div>
                                <CommandSelect
                                    title={FilterNames.CompanyI18nKey}
                                    openCheckBox={false}
                                    openSelMultiple={false}
                                    defSelectVal={companyVal}
                                    defSelectList={companyList}
                                    onChangeHandler={(name, data) => { this.onChangeHandler(name, data) }}
                                />
                            </div>
                        }
                        {/* 國家/地域 */}
                        <div className="mb-1 pb-2 form-inline">
                            <div className="col-xl-2 col-lg-2 col-4"><Trans i18nKey={FilterNames.AreaI18nKey} /></div>
                            <CommandSelect
                                title={FilterNames.AreaI18nKey}
                                defSelectVal={areaVal}
                                defSelectList={areaList}
                                openCheckBox={true}
                                openSelMultiple={true}
                                onChangeHandler={(name, data) => { this.onChangeHandler(name, data) }}
                            />
                        </div>
                        {/* 站台 */}
                        <div className="mb-1 pb-2 form-inline">
                            <div className="col-xl-2 col-lg-2 col-4"><Trans i18nKey={FilterNames.GroupI18nKey} /></div>
                            <CommandSelect
                                title={FilterNames.GroupI18nKey}
                                defSelectVal={groupVal}
                                defSelectList={groupList}
                                openCheckBox={true}
                                openSelMultiple={true}
                                onChangeHandler={(name, data) => { this.onChangeHandler(name, data) }}
                            />
                            {/* 查找 */}
                            <button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 mt-3 mt-xl-0 col-xl-1 px-0 ml-0 ml-xl-2" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                                onClick={() => { this.getCommandBattIdList() }}>
                                <Trans i18nKey={"1025"} />
                            </button>
                        </div>
                        {/* 電池組ID */}
                        <div className="mb-1 pb-2 form-inline">
                            <div className="col-xl-2 col-lg-2 col-4"><Trans i18nKey={FilterNames.BatteryGroupIdI18nKey} /></div>
                            <CommandSelect
                                title={FilterNames.BatteryGroupIdI18nKey}
                                defSelectVal={batteryGroupIdVal}
                                defSelectList={batteryGroupIdList}
                                openCheckBox={batteryGroupIdMultiple}
                                openSelMultiple={batteryGroupIdMultiple}
                                onChangeHandler={(name, data) => { this.onChangeHandler(name, data) }}
                            />
                            {/* 顯示參數 */}
                            <button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 mt-3 mt-xl-0 col-xl-2 px-0 ml-0 ml-xl-2" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                                onClick={() => { this.onClickPeriodSetting() }}>
                                <Trans i18nKey={"1536"} /></button>
                            {/* 重置 */}
                            <button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 mt-3 mt-xl-0 col-xl-1 px-0 ml-0 ml-xl-2" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                                onClick={() => { this.getRefreshList({radioVal}) }}>
                                <Trans i18nKey={"1063"} /></button>
                        </div>
                        <div className="mb-1 pb-2 col-12"><Trans i18nKey={'1567'} />{batteryGroupIdVal.length}</div>
                    </div>
                    {/* 顯示參數 */}
                    <div className="form-inline" style={{ alignItems: 'flex-end' }}>
                        <div className="col-12 col-xl-6 px-0 my-1" style={{ border: '1px solid #333333' }}>
                            <div className="col-12 my-2 font-weight-bold"><Trans i18nKey={"1552"} /></div>
                            {
                                isShowPeriodSetting === false && <div className="col-12 my-2 font-weight-bold"><Trans i18nKey={"1534"} /></div>
                            }
                            <div className="col-12 my-2 pb-2">
                                <PeriodSetting
                                    isShowPeriodSetting={isShowPeriodSetting}
                                    radioVal={radioVal}
                                    data={periodSetting}
                                    handleUpdateData={(data) => { this.handleUpdateData(data) }}
                                />
                            </div>
                        </div>
                        {/* 送出指令 */}
                        <button type="button" className="btn btn-secondary exportBtnShadow col-12 col-xl-2 ml-xl-2 px-0 my-1" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                            onClick={() => { this.handleSubmit() }}
                        ><Trans i18nKey={"1554"} /></button>
                    </div>
                </div>
            </Fragment>
        )
    }
    //Radio Btn
    onClickCommandHandler = (value) => {
        this.setState({
            radioVal: value,
            batteryGroupIdList: [],//電池組ID清單
            batteryGroupIdVal: [],//電池組ID-val值
            isShowPeriodSetting: false,//顯示參數
            periodSetting: {},//參數設定
        })
        
        const { radioList } = this.state;
        if (radioList[0].Value === value) {//BB
            this.setState({ batteryGroupIdMultiple: true });
        } else if (radioList[1].Value === value) {//BA
            this.setState({ batteryGroupIdMultiple: true });
        } else if (radioList[2].Value === value) {//B5            
            this.setState({ batteryGroupIdMultiple: false });
        } else if (radioList[3].Value === value) {//B3
            this.setState({ batteryGroupIdMultiple: false });
        }
    }
    //Select
    onChangeHandler = (name, data) => {
        switch (name) {
            case FilterNames.CompanyI18nKey://公司別                
                this.getCommandList(data);
                break;
            case FilterNames.AreaI18nKey://國家/地域
                const { groupList } = this.props;
                let groupValList = [];
                let groupVal = [];
                data.forEach(element => {
                    groupList.forEach(item => {
                        if (item.Area.toString() === element.toString()) {
                            const { Value, ...other } = item;
                            groupValList.push({ Value: Value.toString(), ...other });
                            groupVal.push(item.Value.toString());
                        }
                    });
                });
                this.setState({
                    areaVal: [...data],
                    groupList: [...groupValList],
                    groupVal: [...groupVal],
                    batteryGroupIdVal: [],
                    batteryGroupIdList: [],
                    isShowPeriodSetting: false,//顯示參數
                    periodSetting: {},//參數設定
                });
                break;
            case FilterNames.GroupI18nKey://站台
                this.setState({
                    groupVal: [...data],
                    batteryGroupIdVal: [],
                    batteryGroupIdList: [],
                    isShowPeriodSetting: false,//顯示參數
                    periodSetting: {},//參數設定
                });
                break;
            case FilterNames.BatteryGroupIdI18nKey://電池組ID
                this.setState({
                    batteryGroupIdVal: [...data],
                    isShowPeriodSetting: false,//顯示參數
                    periodSetting: {},//參數設定
                });
                break;
            default:
        }
    }
    handleUpdateData = (data) => {
        this.setState({ periodSetting: { ...data } });
    }


    getRefreshList = ({radioVal}) => {
        this.setState({
            radioVal: radioVal,//指令-val值 預設"BB"
            companyList: [],//公司別清單
            companyVal: [],//公司別-val值
            areaList: [],//國家/地域清單
            areaVal: [],//國家/地域-val值
            groupList: [],//站台清單
            groupVal: [],//站台-val值
            batteryGroupIdList: [],//電池組ID清單
            batteryGroupIdVal: [],//電池組ID-val值
            isShowPeriodSetting: false,//顯示參數
            periodSetting: {},//參數設定
        });
        this.onClickCommandHandler(radioVal);
        this.getInitList().then(() => {
            const { companyVal } = this.state;
            this.getCommandList(companyVal);
        });
    }


    //init選單
    getInitList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        return ajaxGetCompanyList({query}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                const valList = List.map(item => {
                    return item.Value.toString()
                });                
                this.setState({
                    companyList: [...List],
                    companyVal: [valList[0]]
                });
            } else {
                this.setState({ companyList: [], companyVal: [] })
            }
        });
    }

    //國家/地域/站台下拉選單
    getCommandList = (companyVal) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const companyCode = Array.isArray(companyVal) ? companyVal.sort().join(',') : [];
	    const { radioVal } = this.state; //commandId
        //初始化
        this.setState({
            companyVal: [...companyVal],//更新公司別-val值
            areaList: [],//國家/地域清單
            areaVal: [],//國家/地域-val值
            groupList: [],//站台清單
            groupVal: [],//站台-val值
            batteryGroupIdList: [],//電池組ID清單
            batteryGroupIdVal: [],//電池組ID-val值
            isShowPeriodSetting: false,//顯示參數
            periodSetting: {},//參數設定
        });
        this.props.setAreaList([]);
        this.props.setGroupList([]);
        if (companyCode.length === 0) { return; }
        return ajaxGetCommandList({ query,companyCode, radioVal }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { AreaList, GroupList } = msg;
                const areaValList = AreaList.map(item => {
                    return item.Value.toString()
                });
                const groupValList = GroupList.map(item => {
                    return item.Value.toString()
                });
                this.setState({
                    areaList: [...AreaList],//國家/地域清單
                    areaVal: [...areaValList],//國家/地域-val值
                    groupList: [...GroupList],//站台清單
                    groupVal: [...groupValList],//站台-val值
                    batteryGroupIdList: [],//電池組ID清單
                    batteryGroupIdVal: [],//電池組ID-val值
                    isShowPeriodSetting: false,//顯示參數
                    periodSetting: {},//參數設定
                });
                this.props.setAreaList(AreaList);
                this.props.setGroupList(GroupList);
            } else {
                this.setState({ areaList: [], areaVal: [], groupList: [], groupVal: [], batteryGroupIdList: [], batteryGroupIdVal: [], isShowPeriodSetting: false, periodSetting: {}, });
            }
        });
    }

    //電池組ID 下拉選單(GET):
    getCommandBattIdList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { groupVal, radioVal } = this.state;
        const groupInternalId = Array.isArray(groupVal) ? groupVal.sort().join(',') : [];
        //初始化
        this.setState({
            batteryGroupIdList: [],//電池組ID清單
            batteryGroupIdVal: [],//電池組ID-val值
            isShowPeriodSetting: false,//顯示參數
            periodSetting: {},//參數設定
        })
        if (groupInternalId.length === 0) { return; }
        return ajaxGetCommandBattIdList({ query,groupInternalId, commandId: radioVal }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                const valList = List.map(item => {
                    return item.Value.toString()
                });
                this.setState({
                    batteryGroupIdList: [...List],//電池組ID清單                   
                    isShowPeriodSetting: false,//顯示參數
                    periodSetting: {},//參數設定
                });
                const { radioList } = this.state;
                if (radioList[0].Value === radioVal) {//BB
                    this.setState({ batteryGroupIdVal: [...valList], });//電池組ID-val值
                } else if (radioList[1].Value === radioVal) {//BA
                    this.setState({ batteryGroupIdVal: [...valList], });
                } else if (radioList[2].Value === radioVal) {//B5
                    this.setState({ batteryGroupIdVal: [valList[0]], });
                } else if (radioList[3].Value === radioVal) {//B3
                    this.setState({ batteryGroupIdVal: [valList[0]], });
                }
            } else {
                this.setState({
                    batteryGroupIdList: [], batteryGroupIdVal: [], isShowPeriodSetting: false, periodSetting: {},
                });
            }
        });
    }
    //顯示參數Button
    onClickPeriodSetting = () => {
        const { radioList, radioVal } = this.state;//指令
        const { batteryGroupIdVal } = this.state;//電池組ID
        if (batteryGroupIdVal.length === 0) { return; }
        switch (radioVal) {
            case radioList[0].Value://BB                
                if (batteryGroupIdVal.length === 1) {//只有單選才會回去找資訊
                    this.getIRSetting();
                } else {//給予預設初始化
                    this.setState({
                        isShowPeriodSetting: true,
                        periodSetting: { ...initLastBB }
                    })
                }
                break;
            case radioList[1].Value://BA                  
                if (batteryGroupIdVal.length === 1) {//只有單選才會回去找資訊             
                    this.getPeriodSetting();
                } else {//給予預設初始化
                    this.setState({
                        isShowPeriodSetting: true,
                        periodSetting: { ...initLastBA }
                    })
                }
                break;
            case radioList[2].Value://B5
                if (batteryGroupIdVal.length === 1) {//只有單選才會回去找資訊             
                    this.getCorrectionVolg();
                } else {//給予預設初始化
                    this.setState({
                        isShowPeriodSetting: true,
                        periodSetting: { ...initLastB5 }
                    })
                }
                break;
            case radioList[3].Value://B3
                if (batteryGroupIdVal.length === 1) {//只有單選才會回去找資訊             
                    this.getCorrectionIR();
                } else {//給予預設初始化
                    this.setState({
                        isShowPeriodSetting: true,
                        periodSetting: { ...initLastB3 }
                    })
                }
                break;
            default:
        }
    }
    //參數設定 查詢內阻設定BB:
    getIRSetting = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        const batteryGroupId = Array.isArray(batteryGroupIdVal) ? batteryGroupIdVal.sort().join(',') : [];
        // //初始化
        this.setState({
            periodSetting: {},//參數設定
        })
        if (batteryGroupId.length === 0) { return; }
        return ajaxGetIRSetting({ query,batteryGroupId }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    isShowPeriodSetting: true,//顯示參數
                    periodSetting: { ...msg },//參數設定
                });
            } else if (code === '07') {
                this.setState({
                    isShowPeriodSetting: true, periodSetting: { ...initLastBB },
                });
            } else {
                console.error('電池參數設定 ajaxGetIRSetting', response)
                this.setState({
                    isShowPeriodSetting: false, periodSetting: {},
                });
            }
        });
    }

    //參數設定 查詢時間週期設定BA:
    getPeriodSetting = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        const batteryGroupId = Array.isArray(batteryGroupIdVal) ? batteryGroupIdVal.sort().join(',') : [];
        // //初始化
        this.setState({
            periodSetting: {},//參數設定
        })
        if (batteryGroupId.length === 0) { return; }
        return ajaxGetPeriodSetting({ query,batteryGroupId }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    isShowPeriodSetting: true,//顯示參數
                    periodSetting: { ...msg },//參數設定
                });
            } else if (code === '07') {
                this.setState({
                    isShowPeriodSetting: true, periodSetting: { ...initLastBA },
                });
            } else {
                console.error('電池參數設定 ajaxGetPeriodSetting', response)
                this.setState({
                    isShowPeriodSetting: false, periodSetting: {},
                });
            }
        });
    }

    //參數設定 查詢校正電壓B5:
    getCorrectionVolg = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        const batteryGroupId = Array.isArray(batteryGroupIdVal) ? batteryGroupIdVal.sort().join(',') : [];
        // //初始化
        this.setState({
            periodSetting: {},//參數設定
        })
        if (batteryGroupId.length === 0) { return; }
        return ajaxGetCorrectionVol({ query,batteryGroupId }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    isShowPeriodSetting: true,//顯示參數
                    periodSetting: { ...msg },//參數設定
                });
            } else if (code === '07') {
                this.setState({
                    isShowPeriodSetting: true, periodSetting: { ...initLastB5 },
                });
            } else {
                console.error('電池參數設定 ajaxGetCorrectionVol', response)
                this.setState({
                    isShowPeriodSetting: false, periodSetting: {},
                });
            }
        });
    }

    //參數設定 查詢校正內阻B3:
    getCorrectionIR = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        const batteryGroupId = Array.isArray(batteryGroupIdVal) ? batteryGroupIdVal.sort().join(',') : [];
        // //初始化
        this.setState({
            periodSetting: {},//參數設定
        })
        if (batteryGroupId.length === 0) { return; }
        return ajaxGetCorrectionIR({ query,batteryGroupId }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    isShowPeriodSetting: true,//顯示參數
                    periodSetting: { ...msg },//參數設定
                });
            } else if (code === '07') {
                this.setState({
                    isShowPeriodSetting: true, periodSetting: { ...initLastB3 },
                });
            } else {
                console.error('電池參數設定 ajaxGetCorrectionIR', response)
                this.setState({
                    isShowPeriodSetting: false, periodSetting: {},
                });
            }
        });
    }


    handleSubmit = () => {
        const { periodSetting } = this.state;//postData
        const { radioList, radioVal } = this.state;//指令
        if (Object.keys(periodSetting).length === 0) { return; }
        switch (radioVal) {
            case radioList[0].Value://BB                
                this.addIRSettingBatch(periodSetting, radioVal).then(() => {
                    this.props.showTableData(this.props.submitKeyList);
                }).catch(() => { });
                break;
            case radioList[1].Value://BA                  
                this.addPeriodSettingBatch(periodSetting, radioVal).then(() => {
                    this.props.showTableData(this.props.submitKeyList);
                }).catch(() => { });
                break;
            case radioList[2].Value://B5
                this.addCorrectionVolTask(periodSetting, radioVal).then(() => {
                    this.props.showTableData(this.props.submitKeyList);
                }).catch(() => { });
                break;
            case radioList[3].Value://B3
                this.addCorrectionIRTask(periodSetting, radioVal).then(() => {
                    this.props.showTableData(this.props.submitKeyList);
                }).catch(() => { });
                break;
            default:
        }
    }


    //Sumbit function
    //新增內阻設定BB(POST):
    addIRSettingBatch = (data, CommandID) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        if (batteryGroupIdVal.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.IRTestTime === "") { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.BatteryCapacity === "") { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.CorrectionValue === "") { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.Resistance === "") { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            BatteryGroupID: [...batteryGroupIdVal],
            CommandID: CommandID,
            IRTestTime: data.IRTestTime,
            BatteryCapacity: data.BatteryCapacity,
            CorrectionValue: data.CorrectionValue,
            Resistance: data.Resistance,
            UserName: this.props.username
        }
        return ajaxAddIRSettingBatch({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response; console.log(msg);
            const title = '1089';
            if (code === '00' && msg) {
                this.props.handleAlertMsgOpen(title, msg.Message);
                this.props.setSubmitKeyList(msg.TaskId);
            } else if (code === '07') {
                this.props.handleAlertMsgOpen(title, msg.Message);
            } else {
                console.error('電池參數設定 ajaxAddIRSettingBatch', response)
                this.props.handleAlertMsgOpen(title, msg);
            }
        });
    }

    //新增時間週期設定BA(POST):
    addPeriodSettingBatch = (data, CommandID) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        if (batteryGroupIdVal.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.UploadCycle === "") { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.IRCycle === "") { return new Promise((resolve, reject) => (reject(''))); }
        if (data?.CommunicationCycle === "") { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            BatteryGroupID: [...batteryGroupIdVal],
            CommandID: CommandID,
            UploadCycle: data.UploadCycle,
            IRCycle: data.IRCycle,
            CommunicationCycle: data.CommunicationCycle,
            UserName: this.props.username
        }
        return ajaxAddPeriodSettingBatch({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            const title = '1089';
            if (code === '00' && msg) {
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg.Message} />);
                this.props.setSubmitKeyList(msg.TaskId);
            } else if (code === '07') {
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg.Message} />);
            } else {
                console.error('電池參數設定 ajaxAddPeriodSettingBatch', response)
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg} />);
            }
        });
    }


    //新增校正電壓B5(POST):
    addCorrectionVolTask = (data, CommandID) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        if (batteryGroupIdVal.length === 0 && data?.Vol?.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const NaNDataList = data.Vol.filter(item => item !== "");
        if (NaNDataList.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            BatteryGroupID: batteryGroupIdVal[0],
            CommandID: CommandID,
            Vol: data.Vol,
            UserName: this.props.username
        };
        return ajaxAddCorrectionVolTask({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            const title = '1089';
            if (code === '00' && msg) {
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg.Message} />);
                this.props.setSubmitKeyList(msg.TaskId);
            } else if (code === '07') {
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg.Message} />);
            } else {
                console.error('電池參數設定 ajaxAddCorrectionVolTask', response)
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg} />);
            }
        });
    }

    //新增校正內阻B3(POST):
    addCorrectionIRTask = (data, CommandID) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { batteryGroupIdVal } = this.state;
        if (batteryGroupIdVal.length === 0 && data?.IR?.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const NaNDataList = data.IR.filter(item => item !== "");
        if (NaNDataList.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            BatteryGroupID: batteryGroupIdVal[0],
            CommandID: CommandID,
            IR: data.IR,
            UserName: this.props.username
        }
        return ajaxAddCorrectionIRTask({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            const title = '1089';
            if (code === '00' && msg) {
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg.Message} />);
                this.props.setSubmitKeyList(msg.TaskId);
            } else if (code === '07') {
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg.Message} />);
            } else {

                console.error('電池參數設定 ajaxAddCorrectionIRTask', response)
                this.props.handleAlertMsgOpen(title, <Trans i18nKey={msg} />);
            }
        });
    }
}

Filter.defaultProps = {
    handleAlertMsgOpen: () => {}
}
Filter.propTypes = {
    handleAlertMsgOpen: PropTypes.func,
}

const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
        areaList: state.CommandP1504Reducer.areaList,
        groupList: state.CommandP1504Reducer.groupList,
        submitKeyList: state.CommandP1504Reducer.submitKeyList,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setAreaList: (List) => dispatch(setAreaList(List)),
        setGroupList: (List) => dispatch(setGroupList(List)),
        setSubmitKeyList: (List) => dispatch(setSubmitKeyList(List)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Filter);