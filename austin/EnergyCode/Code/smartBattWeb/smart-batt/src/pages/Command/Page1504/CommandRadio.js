import React, { Fragment, useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import PropTypes from 'prop-types';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import { useTranslation } from "react-i18next";

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            minWidth: 120,
            width: '45%',
        },
        '& .MuiFormControlLabel-root': {
            margin: 0,
            '& label': {
                margin: 0,
            }
        },
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
        width: '45%',
    },
    radioBoxGroup: {
        display: 'inline-block',
        margin: 0,
    },
}));


// 篩選核取清單
const CommandRadio = ({
    defRadioVal,
    defRadioList,
    onClickHandler,
}) => {
    const { t } = useTranslation();
    const classes = useStyles([]);
    const [radioVal, setRadioVal] = useState(defRadioVal); //選單設定Radio值
    useEffect(() => {
        if (defRadioVal.toString() !== radioVal.toString()) {
            setRadioVal(defRadioVal);
        }
    }, [radioVal, defRadioVal]);

    const handleChange = (event) => {//Radio ChangeEvent
        setRadioVal(event.target.value);//設定Radio值       
        onClickHandler(event.target.value);//回傳父組件
    }
    return (
        <Fragment>
            <RadioGroup value={radioVal} className={classes.radioBoxGroup} >
                {
                    Array.isArray(defRadioList) && defRadioList.length > 0 &&
                    defRadioList.map((item, idx) => (
                        < FormControlLabel
                            key={idx}
                            className='my-0'
                            value={item.Value}
                            control={
                                <Radio
                                    color="primary"
                                    onClick={(event) => { handleChange(event) }}
                                />}
                            label={t(item.Label)} />
                    ))
                }
            </RadioGroup>
        </Fragment >
    );
};
CommandRadio.defaultProps = {
    defRadioVal: "",
    defRadioList: [],
    onClickHandler: () => { },
}
CommandRadio.propTypes = {
    defRadioVal: PropTypes.string,
    defRadioList: PropTypes.array,
    onClickHandler: PropTypes.func,
}
export default CommandRadio;
