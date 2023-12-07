import React, {useState} from 'react';
import { useTranslation } from 'react-i18next'
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { ErrorAlert,SuccessAlert } from '../CusAlert';
import SelectLanguage from '../SelectLanguage';
import SelectTimeZone from '../SelectTimeZone';
import {DialogTitleStyle,CloseBtnStyle,DialogContentStyle,DialogActionsStyle} from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';

const DialogChangeSetting = ({
    token,
    account,        //帳號
    language,       // 預設語系
    timeZone,       // 預設時區
    languageList,   // 語系清單
    languageListErrorMsg,// 語系清單錯誤訊息
    timeZoneList,   // 時區清單
    timeZoneListErrorMsg,   // 時區清單錯誤訊息
    open,
    handleOpen,
    handleClose,
    updCurLanguage,	//變更目前語系
    updLanguage,	//變更預設語系
    updTimeZone,	//變更預設時區
}) => {
    const { t } = useTranslation();
    const [errorMsg,setErrorMsg] = useState('');            // 成功訊息
    const [successMsg,setSuccessMsg] = useState('');        // 失敗訊息
    const [locale, setLocale] = useState(language);         // 預設語系
    const [curTimeZone, setTimeZone] = useState(timeZone);  // 預設時區
    // 保存
    const handlesubmit = () => {
        resetMsg();// 清空訊息
        // 變更語系&變更時區
        fetchUpdAccount(token,account,locale,curTimeZone);
    }

    // function
    const changeLanguage = (value) => {  // 變更預設語系
        resetMsg();// 清空訊息
        setLocale(value)

    }
    const changeTimeZone = (value) => { // 變更預設時區
        resetMsg();// 清空訊息
        setTimeZone(value);
    }

    const resetMsg = () => { // 清空訊息
        setErrorMsg('')
        setSuccessMsg('')
    }
    // get API 更新使用者資訊
    const fetchUpdAccount= (token,account,locale,curTimeZone) => {
        const url = `updAccount`;
        const list = {
            msg: {
              Account: account,
              TimeZone: curTimeZone,
              Language: locale,
            }
        }
        fetch(url,{
            method: 'POST',
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'token': token,
                // 'language':language,
                // 'timezone':timezone
            }),
            body: JSON.stringify(list)
        }).then(response => {
            if(response.status === 200) {
                return response.json()
            }else {
                return {}
            }
        }).then( response => {
            let timer ;
            setSuccessMsg(response.msg);
            // 變更目前顯示語系(props.curLanguage&props.language)
            updCurLanguage(locale);
            updLanguage(locale);
            // 變更預設時區(props.timeZone)
            updTimeZone(curTimeZone);

            // 清空訊息&關閉視窗
            if(timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(()=>{
                setSuccessMsg('');
                handleClose();
            },2000)
        })
    }


    return (
        <CusDialog 
            open={open} 
            handleOpen={handleOpen} 
            handleClose={handleClose}
        >
            {/* 用戶設定 */}
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                <div className="col-6 p-0 d-inline-block">{t('1117')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                </div>
            </DialogTitle>
            <DialogContent style={DialogContentStyle}>
                {
                    (successMsg && successMsg !== '') ? <SuccessAlert message={successMsg} /> : ''
                }
                {
                    (errorMsg && errorMsg !== '') ? <ErrorAlert message={errorMsg} /> : ''
                }

                {/* 語言 */}
                <div className="col-12 m-0 p-0 mb-4 mt-4">
                    <div>{t('1118')}</div>
                    <div className="col-6 p-0">
                        <SelectLanguage 
                            currentLocale={locale} 	//目前語系
                            languageList={languageList} 	//語系清單
                            languageListErrorMsg={languageListErrorMsg} //語系清單錯誤訊息
                            getCurLocale={changeLanguage} 	//變更目前語系(畫面上)
                        />
                    </div>
                    
                </div>
                {/* 時區 */}
                <div className="col-12 m-0 p-0 mb-4">
                    <div>{t('1119')}</div>
                    <div className="col-6 d-inline-block p-0 align-top">
                        <SelectTimeZone
                            dfTimeZone={curTimeZone}   //目前時區
                            timeZoneList={timeZoneList} //時區清單
                            timeZoneListErrorMsg={timeZoneListErrorMsg} //時區清單錯誤訊息
                            getTimeZone={changeTimeZone}    //變更目前時區(畫面上)
                        />
                    </div>
                </div>
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
            {/* 取消 */}
            <Button onClick={handleClose}>{t('1003')}</Button>
            {/* 保存 */}
            <CusMainBtnStyle name={t('1004')} icon="fas fa-check" clickEvent={handlesubmit} />
            {/* <Button onClick={handlesubmit} color="primary"><i className="fas fa-check" />{t('1004')}</Button> */}
            </DialogActions>
        </CusDialog>
    )
}
export default DialogChangeSetting;