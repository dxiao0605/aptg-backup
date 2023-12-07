import React, { Fragment } from "react";
import { makeStyles } from "@material-ui/core/styles";
import PropTypes from 'prop-types';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles((theme) => ({
    select: {
        minWidth: '180px',
    }
}));

// 篩選核取清單
const CommonSelect = ({
    className,
    openI18nKey,
    openSelMultiple,
    defSelectVal,
    defSelectList,
    onChangeHandler,
    disabled,
}) => {
    const { t } = useTranslation();
    const classes = useStyles([]);

    const handleChange = (event) => {
        if (openSelMultiple) {
            onChangeHandler(event.target.value);
        } else {
            onChangeHandler([event.target.value]);
        }
    };
    return (
        <Fragment>
            <Select
                className={classes.select + ' ' + className}
                value={openSelMultiple ? defSelectVal.toString() : (defSelectVal.length === 1 && defSelectList.length > 0 ? defSelectVal[0].toString() : "")}
                onChange={handleChange}
                multiple={openSelMultiple}
                disabled={disabled}
            >
                {
                    (Array.isArray(defSelectList) && defSelectList.length > 0 ?
                        defSelectList.map((item) => {
                            return item?.Value !== undefined &&
                                <MenuItem key={item.Value.toString()} value={item.Value.toString()}>{openI18nKey ? t(item.Label) : item.Label}</MenuItem>;
                        }) :
                        <MenuItem value=''></MenuItem>)
                }
            </Select>
        </Fragment>
    );
};
CommonSelect.defaultProps = {
    className: '',
    openI18nKey: false,
    openSelMultiple: true,
    defSelectVal: [],
    defSelectList: [],
    onChangeHandler: () => { },
    disabled: true,
}
CommonSelect.propTypes = {
    className: PropTypes.string,
    openI18nKey: PropTypes.bool,
    openSelMultiple: PropTypes.bool,
    defSelectVal: PropTypes.array,
    defSelectList: PropTypes.array,
    onChangeHandler: PropTypes.func,
    disabled: PropTypes.bool,
}
export default CommonSelect;
