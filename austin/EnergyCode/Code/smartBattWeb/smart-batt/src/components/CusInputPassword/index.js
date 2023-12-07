import React from 'react';
import PropTypes from 'prop-types';
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
CusInputPassword.propTypes = {
    placeholderName: PropTypes.string,
    value: PropTypes.string,
    onChangeEvent: PropTypes.func,
}
export default CusInputPassword;