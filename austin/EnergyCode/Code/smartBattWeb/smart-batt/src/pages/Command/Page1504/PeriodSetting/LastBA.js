import React, { Fragment } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";
import CusInput from '../../../../components/CusInput';


// 篩選核取清單
const LastBA = ({
    data,
    handleInputChange,
    handleInputBlur
}) => {    
    const { t } = useTranslation();
    return (
        <Fragment>
            {/* 命令 */}
            <div className="my-2">
                <div className="col-2 d-inline-block p-0 align-top">{t('1065')}</div>
                <div className="col-10 d-inline-block p-0 align-top">{t('186')}</div>
            </div>
            {/* 參數: */}
            <div className="my-2">
                <div className="col-2 d-inline-block p-0 align-top">{t('1552')}</div>
                <div className="col-10 d-inline-block p-0 align-top pr-3">
                    <div>{t('1555')}</div>
                    <CusInput
                        name="UploadCycle"
                        value={(data.UploadCycle === 0) ? '' : data.UploadCycle}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }} />
                    <div>{t('1556')}</div>
                    <CusInput
                        name="IRCycle"
                        value={(data.IRCycle === 0) ? '' : data.IRCycle}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }} />
                    <div>{t('1557')}</div>
                    <CusInput
                        name="CommunicationCycle"
                        value={(data.CommunicationCycle === 0) ? '' : data.CommunicationCycle}
                        isFullWidth={true}
                        onChangeEvent={(e) => { handleInputChange(e) }}
                        onBlurEvent={(e) => { handleInputBlur(e) }} />
                </div>
            </div>
            {/* 最後設定時間 */}
            {data.LastSettingTime && <div className="pt-2">{t('1417')}{data.LastSettingTime}</div>}
        </Fragment >
    );
};
LastBA.defaultProps = {
    data: {},
    andleInputChange: () => { },
    handleInputBlur: () => { },
}
LastBA.propTypes = {
    data: PropTypes.object,
    handleInputChange: PropTypes.func,
    handleInputBlur: PropTypes.func,
}
export default LastBA;
