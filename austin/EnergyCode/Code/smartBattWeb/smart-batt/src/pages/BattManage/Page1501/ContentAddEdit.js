import React from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import DatePicker from "react-datepicker";
import { zhTW } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";

const ContentAddEdit = ({ BatteryTypeNameList, inputList, data, handleSelectChange, handleInputChange, handleDateChange }) => {
    const { t } = useTranslation();
    const { BatteryGroupID, BatteryType } = inputList;
    const {
        date,
        BatteryGroupIDValue,
        BatteryTypeValue,
    } = data;
    return (
        <>
            {/* 電池組ID */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1026")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={BatteryGroupID} value={BatteryGroupIDValue} autoComplete="off" disabled={true} onChange={() => { }} />
                </div>
            </div>
            {/* 電池型號 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1030")}</div>
                <div className="d-flex my-1">
                    <input className="form-control" list={BatteryType} value={BatteryTypeValue} onChange={(event) => handleSelectChange(event)} autoComplete="off" />
                    <datalist id={BatteryType} >
                        {
                            Array.isArray(BatteryTypeNameList) && BatteryTypeNameList.length > 0 && BatteryTypeNameList.map(item => {
                                return <option key={item.Value} value={item.Label} />
                            })
                        }
                    </datalist>
                </div>
            </div>
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1027")}</div>
                <div className="d-flex my-1">
                    <DatePicker
                        className="form-control"
                        locale={zhTW}
                        dateFormat='yyyy-MM-dd'
                        selected={date}
                        onChange={handleDateChange}
                        selectsStart
                        start={date}
                        maxDate={new Date()}
                    />
                    <div className="input-group-append">
                        <i className="input-group-text far fa-calendar-alt" />
                    </div>
                </div>
            </div>
        </>
    )
}
ContentAddEdit.defaultProps = {
    data: {
        date: new Date(),
        BatteryGroupIDValue: '',
        BatteryTypeValue: '',
    },
    BatteryTypeNameList: [],
    handleSelectChange: () => { },
    handleInputChange: () => { }
}
ContentAddEdit.propTypes = {
    data: PropTypes.object,
    BatteryTypeNameList: PropTypes.array,
    inputList: PropTypes.object,
    handleSelectChange: PropTypes.func,
    handleInputChange: PropTypes.func
}
export default ContentAddEdit;


