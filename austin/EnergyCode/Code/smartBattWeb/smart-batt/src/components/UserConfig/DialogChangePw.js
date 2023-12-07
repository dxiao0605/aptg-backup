import React, {useState} from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import {ajax,locpath } from '../../utils/ajax';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { ErrorAlert,SuccessAlert } from '../CusAlert';
import { DialogTitleStyle,CloseBtnStyle,DialogContentStyle,DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';


const DialogChangePw = ({token,language,timezone,company,account,open,handleOpen,handleClose,handleLogout,alertValidDate}) => {
    const { t } = useTranslation();
    const [currentPw,setCurrentPw] = useState('');
    const [newPw,setNewPw] = useState('');
    const [newPwCheck,setNewPwCheck] = useState('');
    const [successMsg,setSuccessMsg] = useState('');
    const [errorMsg,setErrorMsg] = useState('');


    // 變更inputvalue
    const changePwHandler = (event) => {
        const inputVal = event.target.value;
        setErrorMsg('');
        setSuccessMsg('');
        setCurrentPw(inputVal);
    }
    // 新密碼
    const changeNewPwHandler = (event) => {
        const inputVal = event.target.value;
        setErrorMsg('');
        setSuccessMsg('');
        setNewPw(inputVal);
    }
    // 再次輸入
    const changeNewPwCheckHandler = (event) => {
        const inputVal = event.target.value;
        setErrorMsg('');
        setSuccessMsg('');
        setNewPwCheck(inputVal);
    }

    // 清空欄位並關閉彈跳視窗
    const handleCancel = () => {
        // reset
        setCurrentPw('');
        setNewPw('');
        setNewPwCheck('');
        setErrorMsg('');
        setSuccessMsg('');
        handleClose()
    }
    // 保存
    const handlesubmit = () => {
        fetchUpdPassword().then( response => {
            // console.log('response',response)
            if( response.code === '00') {
                // reset
                setCurrentPw('');
                setNewPw('');
                setNewPwCheck('');
                // message
                setErrorMsg('');
                setSuccessMsg(response.msg);
                setTimeout( async()=>{
                    await handleLogout()
                    window.location.href = locpath()
                }, 500)
            }else{
                // reset
                setCurrentPw('');
                setNewPw('');
                setNewPwCheck('');
                // message
                setErrorMsg(response.msg);
                setSuccessMsg('');
            }
        })
    }

    // get API
    const fetchUpdPassword = () => {
        const url = `updPassword`;
        const list = {
            msg: {
              "Account": account,
              "Password": currentPw,
                  "NewPassword": newPw,
                  "NewPasswordCheck": newPwCheck
            }
          }
        return ajax(url,'POST',token,language,timezone,company,list)
    }


    return (
        <CusDialog 
            open={open} 
            handleOpen={handleOpen} 
            handleClose={handleClose}
        >
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                {/* 更改密碼 */}
                <div className="col-6 p-0 d-inline-block">{t('1112')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={handleCancel} />
                </div>
            </DialogTitle>
            <DialogContent style={DialogContentStyle}>
                {
                    (successMsg && successMsg !== '') ? <SuccessAlert message={successMsg} /> : ''
                }
                {
                    (errorMsg && errorMsg !== '') ? <ErrorAlert message={errorMsg} /> : ''
                }
                { alertValidDate !== '' && <div>{t('1126')}{alertValidDate}{t('1127')}</div> }

                <div className="col-12 m-0 p-0 mb-4 mt-4">
                    {/* 當前密碼 */}
                    <div>{t('1115')}</div>
                    <div className="col-6 p-0">
                        <input type="password" value={currentPw} style={{width: '90%'}} onChange={(event)=>{changePwHandler(event)}} />
                    </div>
                    
                </div>
                <div className="col-12 m-0 p-0 mb-4">
                    <div className="col-6 d-inline-block p-0">{t('1116')}</div>{/* 新密碼 */}
                    <div className="col-6 d-inline-block p-0">{t('1733')}</div>{/* 再次輸入新密碼 */}
                    {/* 新密碼Input */}
                    <div className="col-6 d-inline-block p-0 align-top">
                        <input type="password" value={newPw} style={{width: '90%'}} onChange={(event)=>{changeNewPwHandler(event)}} />
                    </div>
                    {/* 再次輸入新密碼Input */}
                    <div className="col-6 d-inline-block p-0 align-top">
                        <input type="password" value={newPwCheck} style={{width: '90%'}} onChange={(event)=>{changeNewPwCheckHandler(event)}} />
                    </div>
                </div>
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
                {/* 取消 */}
                <Button onClick={handleCancel}>{t('1003')}</Button>
                {/* 保存 */}
                <CusMainBtnStyle name={t('1004')} icon="fas fa-check" clickEvent={handlesubmit} />
                {/* <Button onClick={handlesubmit} color="primary"><i className="fas fa-check" />{t('1004')}</Button> */}
            </DialogActions>
        </CusDialog>
    )
}
DialogChangePw.defaultProps = {
    alertValidDate: '',
}
DialogChangePw.propTypes = {
    alertValidDate: PropTypes.string,
}
export default DialogChangePw;