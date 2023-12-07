import React from 'react';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'


const CusInputPassword = ({placeholderName,value,onChangeEvent}) => {
    const { t } = useTranslation();
    return (
        <input type='password' placeholder={t(placeholderName)} value={value} onChange={(event)=>{onChangeEvent(event)}} autoComplete="off"/>
    )

}
CusInputPassword.defaultProps = {
    placeholderName: '',
    value: '',
    onChangeEvent: ()=> {},
}
export default CusInputPassword;