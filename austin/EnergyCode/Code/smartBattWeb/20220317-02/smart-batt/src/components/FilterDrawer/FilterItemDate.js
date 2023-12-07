import React, { Fragment, useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
/* i18n Functional Components */
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";
import 'react-date-range/dist/styles.css'; // main css file
import 'react-date-range/dist/theme/default.css'; // theme css file
import { DateRange } from 'react-date-range';
import moment from "moment";

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
        minWidth: '12em'
    },
}));


// 篩選日期區間
const FilterItemDate = ({
    // defRadioVal,
    defRadioStart,
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
    const [bookMarkNum, setBookMarkNum] = useState(0);
    // const [radioVal, setRadioVal] = useState(defRadioVal); //選單設定Radio值
    const [openDateRange, setOpenDateRange] = useState(false);//打開日期選單
    const [state, setState] = useState([//操作DateRange使用
        {
            startDate: defRadioStart.match(/\d{4}[-]\d{2}[-]\d{2}/g) ? new Date(defRadioStart) : new Date(),
            endDate: defRadioEnd.match(/\d{4}[-]\d{2}[-]\d{2}/g) ? new Date(defRadioEnd) : new Date(),
            startHH: defRadioStartHH,
            startMM: defRadioStartMM,
            endHH: defRadioEndHH,
            endMM: defRadioEndMM,
            key: 'selection'
        }
    ]);
    // console.log('openDateRange state',state)
    useEffect(() => {
        // console.log('defRadioStart, defRadioEnd',defRadioStart, defRadioEnd)
        if (moment(state[0].startDate).format(format) !== defRadioStart || moment(state[0].endDate).format(format) !== defRadioEnd) {
            setState([{
                startDate: defRadioStart ? new Date(defRadioStart) : new Date(),
                endDate: defRadioEnd ? new Date(defRadioEnd) : new Date(),
                startHH: defRadioStartHH,
                startMM: defRadioStartMM,
                endHH: defRadioEndHH,
                endMM: defRadioEndMM,
                key: 'selection'
            }]);
        }
    }, [state, defRadioStart, defRadioEnd,defRadioStartHH,defRadioStartMM,defRadioEndHH,defRadioEndMM]);
// }, [defRadioVal, state, defRadioStart, defRadioEnd]);

    const HHList = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
    const MMList = ['00', '10', '20', '30', '40', '50'];
    //起
    const [startHH, setStartHH] = useState(defRadioStartHH);
    const [startMM, setStartMM] = useState(defRadioStartMM);
    const [startHHHover, setStartHHHover] = useState(0);//H_idx
    const [startMMHover, setStartMMHover] = useState(0);//M_idx
    //迄
    const [endHH, setEndHH] = useState(defRadioEndHH);
    const [endMM, setEndMM] = useState(defRadioEndMM);
    const [endHHHover, setEndHHHover] = useState(0);//H_idx
    const [endMMHover, setEndMMHover] = useState(0);//M_idx

    // const handleChange = (event) => {//Radio ChangeEvent
    //     // setRadioVal(event.target.value);//設定Radio值
    //     if (event.target.value !== "1") setOpenDateRange(false);//關閉日期選單
    //     UpdateData({ radioVal: event.target.value, state });//回傳父組件
    // }
    const onClickDateRange = () => {//Div Click Event
        setOpenDateRange(!openDateRange);
        // setRadioVal("1");
        // UpdateData({ radioVal: "1", state });//回傳父組件
        UpdateData(state);//回傳父組件
    }
    const onChangeDateRange = (item) => {//DateRange ChangeEvent
        setState([item.selection]);//設定DateRange
        // UpdateData({ radioVal, state: [item.selection] });//回傳父組件
        UpdateData([item.selection]);//回傳父組件
    }
    const UpdateData = (state) => {//回傳父組件
        const { startDate, endDate } = state[0];
        const start = moment(startDate).format(format);
        const end = moment(endDate).format(format);
        // const label = radioVal === "1" ? inputText(state) : defRadioList[radioVal].Label
        const label = inputText(state)
        // console.log('updateData FilterRecordDate',label)
        const installDate = {
            // "Radio": radioVal,
            "Start": start,
            "StartHH": defRadioOpenStartTime ? startHH : "",
            "StartMM": defRadioOpenStartTime ? startMM : "",
            "End": end,
            "EndHH": defRadioOpenStartTime ? endHH : "",
            "EndMM": defRadioOpenStartTime ? endMM : "",
            isButtonList: [label],
            // isButtonList: [{ Value: radioVal, Label: label }]
        };
        onUpdateList(installDate);
    }
    const inputText = (state) => {//顯示文字串
        const { startDate, endDate } = state[0];
        const start = moment(startDate).format(format);
        const end = moment(endDate).format(format);
        if (defRadioOpenStartTime) {
            return start + "_" + startHH + ":" + startMM + " ~ " + end + "_" + endHH + ":" + endMM;
        } else {
            return start + " ~ " + end;
        }
    }
    return (
        <Fragment>
            <div className="col-12">
                <div>
                    <div className={classes.inputText} onClick={onClickDateRange} >
                        {inputText(state)}
                        {/* {defRadioList} */}
                        {/* {console.log('{inputText(state)}',inputText(state))} */}
                    </div>
                </div>
            </div>
            {
                openDateRange &&
                <div className={classes.dateRange}>
                    <ul className="form-inline px-0 dateRangeBookMark">
                        <li className={`col-4 px-0 dateRangeBookMarkli ${bookMarkNum === 0 && 'active'}`} onClick={() => { setBookMarkNum(0) }}>{t('1120')}</li>
                        {
                            defRadioOpenStartTime &&
                            <>
                                <li className={`col-4 px-0 dateRangeBookMarkli ${bookMarkNum === 1 && 'active'}`} onClick={() => { setBookMarkNum(1) }}>{t('1121')}</li>
                                <li className={`col-4 px-0 dateRangeBookMarkli ${bookMarkNum === 2 && 'active'}`} onClick={() => { setBookMarkNum(2) }}>{t('1122')}</li>
                            </>
                        }
                    </ul>
                    <div className="form-inline px-0" style={{ alignItems: "initial", boxShadow: '1px -1.5px 3px 0px' }}>
                        {/* 日曆 */}
                        {
                            bookMarkNum === 0 &&
                            <DateRange
                                editableDateInputs={true}
                                onChange={item => onChangeDateRange(item)}
                                moveRangeOnFirstSelection={false}
                                ranges={state}
                            />
                        }
                        {/* 起小時/起分鐘 */}
                        {
                            defRadioOpenStartTime && bookMarkNum === 1 &&
                            <>
                                <div className="dateRangeTime">
                                    <div className='dateRangeTimeHeader'>
                                        <div className='dateRangeTimeHeader_Div' style={{ boxShadow: '0 1px 2px 0 rgb(35 57 66 / 21%)' }}>
                                            <div className='dateRangeTimeHeader_Label'>{t('1123')}</div>
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
                                                            onClick={() => { setStartHH(item) }}
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
                                            <div className='dateRangeTimeHeader_Label'>{t('1124')}</div>
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
                                                            onClick={() => { setStartMM(item) }}
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
                                            <div className='dateRangeTimeHeader_Label'>{t('1123')}</div>
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
                                                            onClick={() => { setEndHH(item) }}
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
                                            <div className='dateRangeTimeHeader_Label'>{t('1124')}</div>
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
                                                            onClick={() => { setEndMM(item) }}
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
                </div>
            }
        </Fragment >
    );
};
FilterItemDate.defaultProps = {
    defRadioOpenStartTime: false,
    defRadioStartHH: '00',
    defRadioStartMM: '00',
    defRadioEndHH: '00',
    defRadioEndMM: '00',
    defRadioList: [],
    onUpdateList: () => { }
}
FilterItemDate.propTypes = {
    defRadioStart: PropTypes.string,
    defRadioOpenStartTime: PropTypes.bool,
    defRadioStartHH: PropTypes.string,
    defRadioStartMM: PropTypes.string,
    defRadioEndHH: PropTypes.string,
    defRadioEndMM: PropTypes.string,
    defRadioEnd: PropTypes.string,
    defRadioList: PropTypes.array,
    onUpdateList: PropTypes.func,
}
export default FilterItemDate;
