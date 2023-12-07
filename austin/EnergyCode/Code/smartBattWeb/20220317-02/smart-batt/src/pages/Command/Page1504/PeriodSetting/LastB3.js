import React, { Fragment } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";
import CusInput from '../../../../components/CusInput';


// 篩選核取清單
const LastB3 = ({
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
                <div className="col-10 d-inline-block p-0 align-top">{t('179')}</div>
            </div>
            {/* 參數: */}
            <div className="my-2">
                <div className="col-2 d-inline-block p-0 align-top">{t('1552')}</div>
                <div className="col-10 d-inline-block p-0 align-top pr-3">
                    {
                        data?.IR?.map((item, idx) => {
                            return (
                                <Fragment key={idx}>
                                    <div>CH{idx + 1} {t('1565')}</div>
                                    <CusInput                                        
                                        name={idx}
                                        value={(item === 0) ? '' : item}
                                        isFullWidth={true}
                                        onChangeEvent={(e) => { handleInputChange(e) }}
                                        onBlurEvent={(e) => { handleInputBlur(e) }} />
                                </Fragment>
                            )
                        })
                    }
                </div>
            </div>

            {/* 最後設定時間 */}
            {data.LastSettingTime && <div className="pt-2">{t('1417')}{data.LastSettingTime}</div>}
        </Fragment >
    );
};
LastB3.defaultProps = {
    data: {},
    handleInputChange: () => { },    
    handleInputBlur: () => { },
}
LastB3.propTypes = {
    data: PropTypes.object,
    handleInputChange: PropTypes.func,
    handleInputBlur: PropTypes.func,
}
export default LastB3;
