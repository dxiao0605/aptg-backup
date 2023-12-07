import React, { Fragment, useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
/* i18n Functional Components */
import PropTypes from 'prop-types';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import { CusMainBtnStyle } from '../CusMainBtnStyle';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import { useTranslation, Trans } from "react-i18next";
import 'react-date-range/dist/styles.css'; // main css file
import 'react-date-range/dist/theme/default.css'; // theme css file
import { DateRange } from 'react-date-range';
import moment from "moment";
// import { CompareSharp } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            minWidth: 120,
            width: '45%',
        },
        '& .MuiFormControlLabel-root': {
            margin: 0,
            '& label': {
                margin: 0,
            }
        },
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
        width: '45%',
    },
    radioBoxGroup: {
        display: 'inline-block',
        margin: 0,
    },
    dateRange: {
        position: 'fixed',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        zIndex: 100,
        boxShadow: '4px 4px 2px 1px rgba(0, 0, 0, 0.2)',
        border: '5px solid #fff',
        borderRadius: '5px',
        display: 'inline-table',
    },
    inputText: {
        minWidth: '5em'
    },
}));

const initStartDate = new Date(new Date().getTime() - (7 * 24 * 60 * 60 * 1000));//初始化開始日期
const initEndDate = new Date();//初始化結束日期
const initRanges = [365, 365];//maxRange,minRange


// 篩選核取清單
const FilterItemRadio = ({
    defRadioVal,
    defRadioStart,
    defRadioRangeMaxDay,//往未來多少天
    defRadioRangeMinDay,//往從前多少天
    defRadioOpenStartTime,
    defRadioStartHH,
    defRadioStartMM,
    defRadioEnd,
    defRadioEndHH,
    defRadioEndMM,
    defRadioList,
    onUpdateList,
}) => {
    const { t } = useTranslation();
    const format = "YYYY-MM-DD";
    const classes = useStyles([]);
    const [bookMarkNum, setBookMarkNum] = useState(0);//書籤
    const [radioVal, setRadioVal] = useState(defRadioVal); //選單設定Radio值
    const [openDateRange, setOpenDateRange] = useState(false);//打開日期選單
    const [state, setState] = useState([//操作DateRange使用
        {
            startDate: defRadioStart.match(/\d{4}[-]\d{2}[-]\d{2}/g) ? new Date(defRadioStart) : initStartDate,
            endDate: defRadioEnd.match(/\d{4}[-]\d{2}[-]\d{2}/g) ? new Date(defRadioEnd) : initEndDate,
            key: 'selection'
        }
    ]);
    const [clickDateRange, setClickDateRange] = useState(false);
    const ranges = React.useMemo(() => {
        return [(defRadioRangeMaxDay && Number(defRadioRangeMaxDay) ? Number(defRadioRangeMaxDay) : initRanges[0]) * 24 * 60 * 60 * 1000
            , (defRadioRangeMinDay && Number(defRadioRangeMinDay) ? Number(defRadioRangeMinDay) : initRanges[1]) * 24 * 60 * 60 * 1000]
    }, [defRadioRangeMaxDay, defRadioRangeMinDay]);
    //最大日期區間
    const [stateMaxDate, setStateMaxDate] = useState(new Date((defRadioEnd.match(/\d{4}[-]\d{2}[-]\d{2}/g) ? new Date(defRadioEnd) : initStartDate).getTime() + ranges[0]));
    //最小日期區間
    const [stateMinDate, setStateMinDate] = useState(new Date((defRadioEnd.match(/\d{4}[-]\d{2}[-]\d{2}/g) ? new Date(defRadioEnd) : initStartDate).getTime() - ranges[1]));
    const HHList = React.useMemo(() => { return ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'] }, []);
    const MMList = React.useMemo(() => { return ['00', '10', '20', '30', '40', '50'] }, []);
    //起
    const [startHH, setStartHH] = useState(defRadioStartHH);
    const [startMM, setStartMM] = useState(defRadioStartMM);
    const [startHHHover, setStartHHHover] = useState(HHList.findIndex(item => item === defRadioStartHH) >= 0 ? HHList.findIndex(item => item === defRadioStartHH) : 0);//H_idx
    const [startMMHover, setStartMMHover] = useState(MMList.findIndex(item => item === defRadioStartMM) >= 0 ? MMList.findIndex(item => item === defRadioStartMM) : 0);//M_idx
    //迄
    const [endHH, setEndHH] = useState(defRadioEndHH);
    const [endMM, setEndMM] = useState(defRadioEndMM);
    const [endHHHover, setEndHHHover] = useState(HHList.findIndex(item => item === defRadioEndHH) >= 0 ? HHList.findIndex(item => item === defRadioEndHH) : 0);//H_idx
    const [endMMHover, setEndMMHover] = useState(MMList.findIndex(item => item === defRadioEndMM) >= 0 ? MMList.findIndex(item => item === defRadioEndMM) : 0);//M_idx

    useEffect(() => {
        if (defRadioVal.toString() !== radioVal.toString()) {
            setRadioVal(defRadioVal);
        }
        if (moment(state[0].startDate).format(format) !== defRadioStart || moment(state[0].endDate).format(format) !== defRadioEnd) {
            setState([{
                startDate: defRadioStart ? new Date(defRadioStart) : initStartDate,
                endDate: defRadioEnd ? new Date(defRadioEnd) : initEndDate,
                key: 'selection'
            }]);
            setStateMaxDate(new Date(new Date(defRadioEnd).getTime() + ranges[0]));
            setStateMinDate(new Date(new Date(defRadioEnd).getTime() - ranges[1]));
        }
        if (defRadioStartHH !== startHH) {
            setStartHH(defRadioStartHH);
            const idx = HHList.findIndex(item => item === defRadioStartHH);
            setStartHHHover(idx);
        }
        if (defRadioStartMM !== startMM) {
            setStartMM(defRadioStartMM);
            const idx = MMList.findIndex(item => item === defRadioStartMM);
            setStartMMHover(idx);
        }
        if (defRadioEndHH !== endHH) {
            setEndHH(defRadioEndHH);
            const idx = HHList.findIndex(item => item === defRadioEndHH);
            setEndHHHover(idx);
        }
        if (defRadioEndMM !== endMM) {
            setEndMM(defRadioEndMM);
            const idx = MMList.findIndex(item => item === defRadioEndMM);
            setEndMMHover(idx);
        }

    }, [radioVal, defRadioVal, state, defRadioStart, defRadioEnd, HHList, MMList, ranges,
        defRadioStartHH, startHH,
        defRadioStartMM, startMM,
        defRadioEndHH, endHH,
        defRadioEndMM, endMM,
    ]);

    const handleChange = (event) => {//Radio ChangeEvent
        setRadioVal(event.target.value);//設定Radio值   
        if (event.target.value !== "1") setOpenDateRange(false);//關閉日期選單
        UpdateData({ radioVal: event.target.value, state, labelShow: !event.target.disabled });//回傳父組件
    }
    const onClickDateRange = () => {//Div Click Event
        setOpenDateRange(!openDateRange);
        setRadioVal("1");
        UpdateData({ radioVal: "1", state, labelShow: true });//回傳父組件
    }
    const onChangeDateRange = (item) => {//DateRange ChangeEvent
        const { selection } = item;
        if (moment(selection.startDate).format(format) === moment(selection.endDate).format(format)) {
            //點第一次
            setStateMaxDate(new Date(new Date(moment(selection.endDate).format(format)).getTime() + ranges[0]));
            setStateMinDate(new Date(new Date(moment(selection.endDate).format(format)).getTime() - ranges[1]));
            setClickDateRange(true);
        } else {
            setClickDateRange(false);
        }
        setState([selection]);//設定DateRange
        UpdateData({ radioVal, state: [selection], labelShow: true });//回傳父組件
    }
    const onRangeFocusChange = () => {

    }
    const UpdateData = ({ radioVal, state, labelShow }) => {//回傳父組件
        const { startDate, endDate } = state[0];
        const start = moment(startDate).format(format);
        const end = moment(endDate).format(format);
        const selectItem = defRadioList.filter(filterItem => filterItem.Value === radioVal);
        // const label = radioVal === "1" ? inputText(state) : defRadioList[radioVal].Label
        // console.log('selectItem[0].Value',selectItem[0].Value,'up',selectItem[0].Label)
        const label = radioVal === "1" ? inputText(state) : selectItem[0].Label
        const installDate = {
            "Radio": radioVal,
            "Start": start,
            "StartHH": defRadioOpenStartTime ? startHH : "00",
            "StartMM": defRadioOpenStartTime ? startMM : "00",
            "End": end,
            "EndHH": defRadioOpenStartTime ? endHH : "00",
            "EndMM": defRadioOpenStartTime ? endMM : "00",
            isButtonList: [{ Value: radioVal, Label: label, LabelShow: labelShow, ButtonShow: false }]
        };
        onUpdateList(installDate);
    }
    const inputText = (state, object) => {//顯示文字串
        const { startDate, endDate } = state[0];
        const start = moment(startDate).format(format);
        const end = moment(endDate).format(format);
        if (defRadioOpenStartTime) {
            if (object === undefined) {
                return start + "_" + startHH + ":" + startMM + " ~ " + end + "_" + endHH + ":" + endMM;
            } else {
                switch (object.name) {
                    case 'StartHH':
                        return start + "_" + object.time + ":" + startMM + " ~ " + end + "_" + endHH + ":" + endMM;
                    case 'StartMM':
                        return start + "_" + startHH + ":" + object.time + " ~ " + end + "_" + endHH + ":" + endMM;
                    case 'EndHH':
                        return start + "_" + startHH + ":" + startMM + " ~ " + end + "_" + object.time + ":" + endMM;
                    case 'EndMM':
                        return start + "_" + startHH + ":" + startMM + " ~ " + end + "_" + endHH + ":" + object.time;
                    default:
                        return '';
                }
            }
        } else {
            return start + " ~ " + end;
        }
    };

    const onChangeHHMMRange = (name, time) => {//DateRange ChangeEvent
        switch (name) {
            case 'StartHH':
                setStartHH(time);
                break;
            case 'StartMM':
                setStartMM(time);
                break;
            case 'EndHH':
                setEndHH(time);
                break;
            case 'EndMM':
                setEndMM(time);
                break;
            default:
        }
        const { startDate, endDate } = state[0];
        const start = moment(startDate).format(format);
        const end = moment(endDate).format(format);
        const selectItem = defRadioList.filter(filterItem => filterItem.Value === radioVal);
        // const label = radioVal === "1" ? inputText(state, { name, time }) : defRadioList[radioVal].
        // console.log('selectItem[0].Value',selectItem[0].Value,'d',selectItem[0].Label)
        const label = radioVal === "1" ? inputText(state, { name, time }) : selectItem[0].Label
        let installDate = {
            "Radio": radioVal,
            "Start": start,
            "StartHH": startHH,
            "StartMM": startMM,
            "End": end,
            "EndHH": endHH,
            "EndMM": endMM,
            isButtonList: [{ Value: radioVal, Label: label, LabelShow: true, ButtonShow: false }]
        };
        installDate[name] = time;
        onUpdateList(installDate);
    }
    // console.log('max', moment(stateMaxDate).format(format), 'min', moment(stateMinDate).format(format));
    return (
        <Fragment>
            <div className="col-12">
                <RadioGroup name={radioVal} value={radioVal} className={classes.radioBoxGroup} >
                    {
                        Array.isArray(defRadioList) && defRadioList.length > 0 &&
                        defRadioList.map((item, idx) => {
                            if (item.Type === "String" && item.show) {
                                return <FormControlLabel
                                    key={idx}
                                    className='w-100 justify-content-start'
                                    value={item.Value}
                                    control={
                                        <Radio
                                            color="primary"
                                            onClick={(event) => { handleChange(event) }}
                                            disabled={!item.show}
                                        />}
                                    label={t(item.Label)} />
                            } else if (item.Type === "Date" && item.show) {
                                return <FormControlLabel
                                    key={idx}
                                    className='w-100 justify-content-start pointer'
                                    value={item.Value}
                                    control={
                                        <Radio
                                            color="primary"
                                            onClick={(event) => { handleChange(event) }}
                                            disabled={!item.show}
                                        />}
                                    label={<div><div className={classes.inputText} onClick={onClickDateRange} >{inputText(state)}</div></div>}
                                />
                            } else {
                                return '';
                            }
                        })
                    }
                </RadioGroup>
            </div>
            {
                openDateRange &&
                <div className={classes.dateRange}>
                    <ul className="form-inline px-0 dateRangeBookMark">
                        <li className={`col-4 px-0 dateRangeBookMarkli ${bookMarkNum === 0 && 'active'}`} onClick={() => { setBookMarkNum(0) }}><Trans i18nKey="1120" /></li>
                        {
                            defRadioOpenStartTime &&
                            <>
                                <li className={`col-4 px-0 dateRangeBookMarkli ${bookMarkNum === 1 && 'active'}`} onClick={() => { setBookMarkNum(1) }}><Trans i18nKey="1121" /></li>
                                <li className={`col-4 px-0 dateRangeBookMarkli ${bookMarkNum === 2 && 'active'}`} onClick={() => { setBookMarkNum(2) }}><Trans i18nKey="1122" /></li>
                            </>
                        }
                    </ul>
                    <div className="form-inline px-0" style={{ alignItems: "initial", boxShadow: '1px -1.5px 3px 0px' }}>
                        {/* 日曆 */}
                        {
                            bookMarkNum === 0 &&
                            <DateRange
                                editableDateInputs={false}//月曆中日期INPUT輸入功能關閉
                                onChange={item => onChangeDateRange(item)}//日曆的Range
                                // focusedRange={false}//關閉月曆中日期INPUT點選功能
                                onRangeFocusChange={(DateRange) => onRangeFocusChange()}//修改月曆中日期INPUT點選EVENT
                                moveRangeOnFirstSelection={false}//move range on startDate selection. Otherwise endDate will replace with startDate.
                                months={1}//一次顯示一個月
                                ranges={state}//val值
                                maxDate={defRadioRangeMaxDay && clickDateRange ? (stateMaxDate > new Date() ? new Date() : stateMaxDate) : new Date()}//限制最大日期
                                minDate={defRadioRangeMinDay && clickDateRange ? stateMinDate : undefined}//限制最小日期

                            // direction={'horizontal'} //水平，預設垂直                                
                            />
                        }
                        {/* 起小時/起分鐘 */}
                        {
                            defRadioOpenStartTime && bookMarkNum === 1 &&
                            <>
                                <div className="dateRangeTime">
                                    <div className='dateRangeTimeHeader'>
                                        <div className='dateRangeTimeHeader_Div' style={{ boxShadow: '0 1px 2px 0 rgb(35 57 66 / 21%)' }}>
                                            <div className='dateRangeTimeHeader_Label'><Trans i18nKey="1123" /></div>
                                        </div>
                                    </div>
                                    <div className='dateRangeTimeContext'>
                                        {
                                            HHList.map((item, idx) => {
                                                return (
                                                    <div key={idx} className='dateRangeTimeContext_HDiv'>
                                                        <div className={`dateRangeTimeContext_HItem 
                                                    ${startHHHover === idx ? 'dateRangeTimeContext_Hover' : ''}
                                                    ${startHH === item ? 'dateRangeTimeContext_Active' : ''}`}
                                                            onMouseEnter={() => { setStartHHHover(idx) }}
                                                            onMouseLeave={() => { setStartHHHover('') }}
                                                            onClick={() => { onChangeHHMMRange("StartHH", item) }}
                                                        >{item}</div>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                                <div className="dateRangeTime">
                                    <div className='dateRangeTimeHeader'>
                                        <div className='dateRangeTimeHeader_Div' style={{ boxShadow: '0 1px 2px 0 rgb(35 57 66 / 21%)' }}>
                                            <div className='dateRangeTimeHeader_Label'><Trans i18nKey="1124" /></div>
                                        </div>
                                    </div>
                                    <div className='dateRangeTimeContext'>
                                        {
                                            MMList.map((item, idx) => {
                                                return (
                                                    <div key={idx} className='dateRangeTimeContext_MDiv'>
                                                        <div className={`dateRangeTimeContext_MItem 
                                                            ${startMMHover === idx ? 'dateRangeTimeContext_Hover' : ''}
                                                            ${startMM === item ? 'dateRangeTimeContext_Active' : ''}`}
                                                            onMouseEnter={() => { setStartMMHover(idx) }}
                                                            onMouseLeave={() => { setStartMMHover('') }}
                                                            onClick={() => { onChangeHHMMRange("StartMM", item) }}
                                                        >{item}</div>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                            </>
                        }
                        {/* 迄小時/迄分鐘 */}
                        {
                            defRadioOpenStartTime && bookMarkNum === 2 &&
                            <>
                                <div className="dateRangeTime">
                                    <div className='dateRangeTimeHeader'>
                                        <div className='dateRangeTimeHeader_Div' style={{ boxShadow: '0 1px 2px 0 rgb(35 57 66 / 21%)' }}>
                                            <div className='dateRangeTimeHeader_Label'><Trans i18nKey="1123" /></div>
                                        </div>
                                    </div>
                                    <div className='dateRangeTimeContext'>
                                        {
                                            HHList.map((item, idx) => {
                                                return (
                                                    <div key={idx} className='dateRangeTimeContext_HDiv'>
                                                        <div className={`dateRangeTimeContext_HItem 
                                                            ${endHHHover === idx ? 'dateRangeTimeContext_Hover' : ''}
                                                            ${endHH === item ? 'dateRangeTimeContext_Active' : ''}`}
                                                            onMouseEnter={() => { setEndHHHover(idx) }}
                                                            onMouseLeave={() => { setEndHHHover('') }}
                                                            onClick={() => { onChangeHHMMRange("EndHH", item) }}
                                                        >{item}</div>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                                <div className="dateRangeTime">
                                    <div className='dateRangeTimeHeader'>
                                        <div className='dateRangeTimeHeader_Div' style={{ boxShadow: '0 1px 2px 0 rgb(35 57 66 / 21%)' }}>
                                            <div className='dateRangeTimeHeader_Label'><Trans i18nKey="1124" /></div>
                                        </div>
                                    </div>
                                    <div className='dateRangeTimeContext'>
                                        {
                                            MMList.map((item, idx) => {
                                                return (
                                                    <div key={idx} className='dateRangeTimeContext_MDiv'>
                                                        <div className={`dateRangeTimeContext_MItem 
                                                            ${endMMHover === idx ? 'dateRangeTimeContext_Hover' : ''}
                                                            ${endMM === item ? 'dateRangeTimeContext_Active' : ''}`}
                                                            onMouseEnter={() => { setEndMMHover(idx) }}
                                                            onMouseLeave={() => { setEndMMHover('') }}
                                                            onClick={() => { onChangeHHMMRange("EndMM", item) }}
                                                        >{item}</div>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                            </>
                        }
                    </div>


                    <div style={{ background: '#fff', boxShadow: '1px -3px 3px 0px #333333', border: '3px solid #ffffff' }}>
                        <div className="d-block py-3 px-4">
                            <CusMainBtnStyle
                                name={<Trans i18nKey={"1010"} />}
                                icon={"fas fa-check mr-2"}
                                fullWidth={true}
                                clickEvent={() => onClickDateRange()}
                            />
                        </div>
                    </div>
                </div>
            }
        </Fragment >
    );
};
FilterItemRadio.defaultProps = {
    defRadioVal: '0',
    defRadioOpenStartTime: false,
    defRadioStartHH: '00',
    defRadioStartMM: '00',
    defRadioEndHH: '00',
    defRadioEndMM: '00',
    defRadioList: [],
    onUpdateList: () => { }
}
FilterItemRadio.propTypes = {
    defRadioVal: PropTypes.string,
    defRadioStart: PropTypes.string,
    defRadioRangeMaxDay: PropTypes.string,
    defRadioRangeMinDay: PropTypes.string,
    defRadioOpenStartTime: PropTypes.bool,
    defRadioStartHH: PropTypes.string,
    defRadioStartMM: PropTypes.string,
    defRadioEndHH: PropTypes.string,
    defRadioEndMM: PropTypes.string,
    defRadioEnd: PropTypes.string,
    defRadioList: PropTypes.array,
    onUpdateList: PropTypes.func,
}
export default FilterItemRadio;
