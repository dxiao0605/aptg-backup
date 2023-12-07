import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'


const StatusDesc = ({StatusCode}) => {
    const { t } = useTranslation();
    return(
        <Fragment>
            {
                (
                    ()=>{
                        switch(StatusCode) {
                            case 1: //正常
                                return <p className="text-green m-0 p-0">{t('1')}</p>
                            case 2: //警戒
                                return <p className="text-yellow m-0 p-0">{t('2')}</p>
                            case 3: //需更換
                                return <p className="text-red m-0 p-0">{t('3')}</p>
                            case 4: //離線
                                return <p className="text-blue m-0 p-0">{t('4')}</p>
                            case 5: //未解決
                                return <p className="d-inline-block text-red m-0 p-0">{t('5')}</p>
                            case 6: //已解決
                                return <p className="d-inline-block text-green m-0 p-0">{t('6')}</p>
                            case 25: //溫度
                            return <p className="d-inline-block text-orange m-0 p-0">{t('25')}</p>
                            default:
                                return <p>{t('4')}</p>
                        }
                    }
                )()
            }
        </Fragment>
    )
}
StatusDesc.propTypes = {
    StatusCode: PropTypes.number,
}
export default StatusDesc;