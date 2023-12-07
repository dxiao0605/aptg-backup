import React,{Fragment, useEffect} from "react";
import { makeStyles } from "@material-ui/core/styles";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import {CusSelectStyle , menuUnderSelectStyle} from '../CusSelectStyle';
/* 切換語系 */
import { setCurrentLanguage } from '../../utils/setCurrentLanguage';
import { useTranslation } from 'react-i18next';
// icon
import TranslateOutlinedIcon from '@material-ui/icons/TranslateOutlined';

const useStyles = makeStyles((theme) => ({
	margin: {
        display: "inline-block",
        margin: theme.spacing(0),
        // marginLeft: theme.spacing(1),
        // marginRight: theme.spacing(1),
        verticalAlign:'middle',
        color: '#6d6d6d',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
	},
}));




const SelectLanguage = ({currentLocale,languageList,languageListErrorMsg,getCurLocale,hasIcon}) => {
    /* 切換語系 */
    const { i18n } = useTranslation();
    const classes = useStyles();

    useEffect(()=>{
        if(currentLocale){
            const lang = setCurrentLanguage(currentLocale.toString());
            i18n.changeLanguage(lang) 
        }
    },[currentLocale,i18n])
    // 變更語系
    const onChangeLanguage = (e) => {
        // 判斷型別統一為string
        const value = e.target.value;
        if(typeof(value) === 'string'){
            const lang = setCurrentLanguage(value.toString());
            getCurLocale(value); //變更props.curLanguage & props.language
            i18n.changeLanguage(lang.toString())  //變更i18n
        }else{
            const lang = setCurrentLanguage(value.toString());
            getCurLocale(value); //變更props.curLanguage & props.language
            i18n.changeLanguage(lang.toString())  //變更i18n
        }
        
    }
     
    if(languageList){
        return (
            <Fragment>
                {
                    languageList.length > 0  ? (
                        <FormControl className={classes.margin}>
                            {
                                hasIcon === true && <TranslateOutlinedIcon className="d-inline-block" style={{fontSize:'1.25rem'}} /> 
                            }
                            <Select
                                labelId='language-select-label'
                                id='language-select'
                                value={currentLocale.toString()}
                                onChange={(e)=>{onChangeLanguage(e)}}
                                MenuProps={menuUnderSelectStyle}    //選單從下方顯示
                                input={<CusSelectStyle />}
                            >
                                {
                                    languageList.map( (locale) => {
                                        return <MenuItem value={(locale.Value).toString()} key={locale.Value}>{locale.Label}</MenuItem>
                                    })
                                }
                            </Select>
                        </FormControl>
                    ): <Fragment>{languageListErrorMsg}</Fragment>
                }
            </Fragment>
        )
    }else{
        return <>Loading...</>
    }
}



SelectLanguage.defaultProps = {
    hasIcon: false,
}
export default SelectLanguage;
