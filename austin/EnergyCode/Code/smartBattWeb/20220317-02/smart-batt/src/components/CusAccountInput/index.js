import React from 'react';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'


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
export default CusAccountInput;