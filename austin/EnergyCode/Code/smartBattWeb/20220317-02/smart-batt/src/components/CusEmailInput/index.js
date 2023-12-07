import React from 'react';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'


const CusEmailInput = ({ placeholderName, value, name, disabled, isFullWidth, onChangeEvent, onBlurEvent }) => {
    const { t } = useTranslation();
    return (
        <input
            type='email'
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
CusEmailInput.defaultProps = {
    placeholderName: '',
    value: '',
    name: '',
    disabled: false,
    isFullWidth: false,
    onChangeEvent: () => { },
    onBlurEvent: () => { },
}
export default CusEmailInput;