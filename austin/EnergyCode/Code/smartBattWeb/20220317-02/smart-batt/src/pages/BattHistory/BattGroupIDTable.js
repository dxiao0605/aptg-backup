import React from 'react';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'

const BattGroupIDTable = ({battHistory}) => {
    const { t } = useTranslation();
    return (
        <div className="battGroupIDTable">
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1026')}</div> {/* 電池組ID */}
            <div className="battGroupIDTable-value">{battHistory.BatteryGroupID}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1028')}</div>{/* 國家 */}
            <div className="battGroupIDTable-value">{battHistory.Country}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1029')}</div>{/* 地域 */}
            <div className="battGroupIDTable-value">{battHistory.Area}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1012')}</div>{/* 站台編號 */}
            <div className="battGroupIDTable-value">{battHistory.GroupID}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1013')}</div>{/* 站台名稱 */}
            <div className="battGroupIDTable-value">{battHistory.GroupName}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1027')}</div>{/* 安裝日期 */}
            <div className="battGroupIDTable-value">{battHistory.InstallDate}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1030')}</div>{/* 型號 */}
            <div className="battGroupIDTable-value">{battHistory.BatteryType}</div>
        </div>
        <div className="battGroupIDTable-item">
            <div className="battGroupIDTable-label">{t('1031')}</div>{/* 地址 */}
            <div className="battGroupIDTable-value">{battHistory.Address}</div>
        </div>
    </div>
    )
}
export default BattGroupIDTable;