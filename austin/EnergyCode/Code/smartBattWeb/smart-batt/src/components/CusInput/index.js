import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'


const CusInput = ({ placeholderName, value, name, disabled, isFullWidth, onChangeEvent, onBlurEvent }) => {
    const { t } = useTranslation();
    return (
        <input
            type='text'
            name={name}
            placeholder={t(placeholderName)}
            value={value}
            disabled={disabled}
            className={`align-middle ${isFullWidth ? 'w-100' : ''}`}
            onChange={(event) => { onChangeEvent(event) }}
            onBlur={(event) => { onBlurEvent(event) }}
            autoComplete="off"
        />
    )

}
CusInput.defaultProps = {
    placeholderName: '',
    value: '',
    name: '',
    disabled: false,
    isFullWidth: false,
    onChangeEvent: () => { },
    onBlurEvent: () => { },
}
CusInput.propTypes = {
    placeholderName: PropTypes.string,
    value: PropTypes.string,
    name: PropTypes.string,
    disabled: PropTypes.bool,
    isFullWidth: PropTypes.bool,
    onChangeEvent: PropTypes.func,
    onBlurEvent: PropTypes.func,
}
export default CusInput;