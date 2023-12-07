import React from 'react';
import { useTranslation } from 'react-i18next'
import PropTypes from 'prop-types';


const CusAccountInput = ({ placeholderName, value, name, disabled, isFullWidth, onChangeEvent, onBlurEvent }) => {
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
CusAccountInput.defaultProps = {
    placeholderName: '',
    value: '',
    name: '',
    disabled: false,
    isFullWidth: false,
    onChangeEvent: () => { },
    onBlurEvent: () => { },
}
CusAccountInput.propTypes = {
    placeholderName: PropTypes.string,
    value: PropTypes.string,
    name: PropTypes.string,
    disabled: PropTypes.bool,
    isFullWidth: PropTypes.bool,
    onChangeEvent: PropTypes.func,
    onBlurEvent: PropTypes.func,
}
export default CusAccountInput;