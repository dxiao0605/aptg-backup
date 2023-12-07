import React, { Fragment } from "react";
import { makeStyles } from "@material-ui/core/styles";
import PropTypes from 'prop-types';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles((theme) => ({
    checkBox: {
        minWidth: 75,
        top: '1px',
    },
    select: {
        width: '44%',
        top: '-2px',
    }
}));

// 篩選核取清單
const CommandSelect = ({
    title,
    openCheckBox,
    openSelMultiple,
    defSelectVal,
    defSelectList,
    onChangeHandler,
}) => {
    const { t } = useTranslation();
    const classes = useStyles([]);
    const onClickSelectAll = (event) => {
        if (event.target.checked) {//全選
            const valList = defSelectList.map(item => {
                return item.Value.toString()
            })
            onChangeHandler(title, valList);
        } else {//清空
            onChangeHandler(title, []);
        }
    };

    const handleChange = (event) => {
        if (openSelMultiple) {
            onChangeHandler(title, event.target.value);
        } else {
            onChangeHandler(title, [event.target.value]);
        }
    };
    return (
        <Fragment>
            <span className={classes.checkBox + ' custom-control custom-checkbox mr-2'}>
                {
                    openCheckBox &&
                    <>
                        <input type='checkbox' className="custom-control-input " id={'P' + title}
                            onChange={(e) => { onClickSelectAll(e) }}
                            checked={Array.isArray(defSelectVal) && defSelectList.length > 0 && defSelectList.length === defSelectVal.length ? true : false}
                        />
                        <label className="custom-control-label" htmlFor={'P' + title}>{t('1076')}</label>
                    </>
                }
            </span>
            <Select
                className={classes.select}
                value={openSelMultiple ? defSelectVal : (defSelectVal.length === 1 ? defSelectVal[0] : "")}
                onChange={handleChange}
                multiple={openSelMultiple}
            >
                {Array.isArray(defSelectList) && defSelectList.length > 0 ?
                    defSelectList.map((item) => {
                        return item?.Value !== undefined && <MenuItem key={item.Value.toString()} value={item.Value.toString()}>{item.Label}</MenuItem>;
                    }) :
                    <MenuItem value=''></MenuItem>}
            </Select>
        </Fragment>
    );
};
CommandSelect.defaultProps = {
    title: '',
    openCheckBox: false,
    openSelMultiple: true,
    defSelectVal: [],
    defSelectList: [],
    onChangeHandler: () => { },
}
CommandSelect.propTypes = {
    title: PropTypes.string,
    openCheckBox: PropTypes.bool,
    openSelMultiple: PropTypes.bool,
    defSelectVal: PropTypes.array,
    defSelectList: PropTypes.array,
    onChangeHandler: PropTypes.func,
}
export default CommandSelect;
