import React,{Fragment,useState} from "react";
import { makeStyles } from "@material-ui/core/styles";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import {CusSelectStyle , menuUnderSelectStyle} from '../CusSelectStyle';

const useStyles = makeStyles((theme) => ({
	margin: {
        display: "inline-block",
        margin: theme.spacing(0),
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1),
        verticalAlign:'middle',
        color: '#6d6d6d',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
	},
}));




const SelectTimeZone = ({dfTimeZone,timeZoneList,timeZoneListErrorMsg,getTimeZone}) => {
    const classes = useStyles();
    const [timeZone,setTimeZone] = useState(dfTimeZone);  //目前時區
    // 變更時區
    const onChangeTimeZone = (e) => {
        // 判斷型別統一為string
        const value = e.target.value;
        if(typeof(value) === 'string'){
            setTimeZone(value);
            getTimeZone(value);
        }else{
            setTimeZone(value.toString());
            getTimeZone(value.toString());
        }
        
    }
    
    return (
        <Fragment>
            {
                timeZoneList && timeZoneList.length > 0 ? (
                    <FormControl className={classes.margin} style={{minWidth: '230px'}}>
                        <Select
                            labelId='timeZone-select-label'
                            id='timeZone-select'
                            value={timeZone}
                            onChange={(e)=>{onChangeTimeZone(e)}}
                            MenuProps={menuUnderSelectStyle}    //選單從下方顯示
                            input={<CusSelectStyle />}
                        >
                            {
                                timeZoneList && timeZoneList.map( (locale) => {
                                    return <MenuItem value={(locale.Value).toString()} key={locale.Value}>{locale.Label}</MenuItem>
                                })
                            }
                        </Select>
                    </FormControl>
                ): <Fragment>{timeZoneListErrorMsg}</Fragment>
            }
        </Fragment>
    );
}
export default SelectTimeZone;
