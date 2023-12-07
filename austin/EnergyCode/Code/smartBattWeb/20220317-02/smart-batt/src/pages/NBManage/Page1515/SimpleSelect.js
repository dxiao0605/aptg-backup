import React, { Fragment } from "react";
import { makeStyles } from "@material-ui/core/styles";
import PropTypes from 'prop-types';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { useTranslation } from 'react-i18next';
import { NBSelectNames } from './InitDataFormat';

const useStyles = makeStyles((theme) => ({
    select: {
        minWidth: '180px',
        textAlign: 'center',
    }
}));

// 篩選核取清單
const SimpleSelect = ({
    title,
    className,
    openI18nKey,
    openSelMultiple,
    defSelectVal,
    defSelectList,
    onChangeHandler,
}) => {
    const { t } = useTranslation();
    const classes = useStyles([]);

    const handleChange = (event) => {
        if (openSelMultiple) {
            onChangeHandler(title, event.target.value);
        } else {
            onChangeHandler(title, [event.target.value]);
        }
    };

    return (
        <Fragment>
            <Select
                className={classes.select + ' ' + className}
                value={openSelMultiple ? defSelectVal : (defSelectVal.length === 1 ? defSelectVal[0] : "")}
                onChange={handleChange}
                multiple={openSelMultiple}
            >
                {
                    NBSelectNames.AllocateI18nKey === title && (Array.isArray(defSelectList) && defSelectList.length > 0 ?
                        defSelectList.map((item) => {
                            return item?.Value !== undefined &&
                                <MenuItem key={item.Value.toString()} value={item.Value.toString()}>{openI18nKey ? t(item.Label) : item.Label}</MenuItem>;
                        }) :
                        <MenuItem value=''></MenuItem>)
                }
                {
                    NBSelectNames.CompanyCodeI18nKey === title && (Array.isArray(defSelectList) && defSelectList.length > 0 ?
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
SimpleSelect.defaultProps = {
    title: '',
    isModel: '',
    className: '',
    openI18nKey: false,
    openSelMultiple: true,
    defSelectVal: [],
    defSelectList: [],
    onChangeHandler: () => { },
}
SimpleSelect.propTypes = {
    title: PropTypes.string,
    isModel: PropTypes.string,
    className: PropTypes.string,
    openI18nKey: PropTypes.bool,
    openSelMultiple: PropTypes.bool,
    defSelectVal: PropTypes.array,
    defSelectList: PropTypes.array,
    onChangeHandler: PropTypes.func,
}
export default SimpleSelect;
