import React, { Fragment } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";
import CusInput from '../../../../components/CusInput';


// 篩選核取清單
const LastBB = ({
    data,
    handleInputChange,
    handleInputBlur,
}) => {
    const { t } = useTranslation();
    return (
        <Fragment>
            {/* 命令 */}
            <div className="my-2">
                <div className="col-2 d-inline-block p-0 align-top">{t('1065')}</div>
                <div className="col-10 d-inline-block p-0 align-top">{t('187')}</div>
            </div>
            {/* 參數: */}
            <div className="my-2">
                <div className="col-2 d-inline-block p-0 align-top">{t('1552')}</div>
                <div className="col-10 d-inline-block p-0 align-top pr-3">
                    <div>{t('1548')}</div>
                    <CusInput
                        name="IRTestTime"
                        value={(data.IRTestTime === 0) ? '' : data.IRTestTime}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }}
                    />
                    <div>{t('1549')}</div>
                    <CusInput
                        name="BatteryCapacity"
                        value={(data.BatteryCapacity === 0) ? '' : data.BatteryCapacity}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }}
                    />
                    <div>{t('1550')}</div>
                    <CusInput
                        name="CorrectionValue"
                        value={(data.CorrectionValue === 0) ? '' : data.CorrectionValue}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }}
                    />
                    <div>{t('1553')}</div>
                    <CusInput
                        name="Resistance"
                        value={data.Resistance === 0 ? '' : data.Resistance}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }}
                    />
                </div>
            </div>

            {/* 最後設定時間 */}
            {data.LastSettingTime && <div className="pt-2">{t('1417')}{data.LastSettingTime}</div>}
        </Fragment >
    );
};
LastBB.defaultProps = {
    data: {},
    handleInputChange: () => { },
    handleInputBlur: () => { },
}
LastBB.propTypes = {
    data: PropTypes.object,
    handleInputChange: PropTypes.func,
    handleInputBlur: PropTypes.func,
}
export default LastBB;
