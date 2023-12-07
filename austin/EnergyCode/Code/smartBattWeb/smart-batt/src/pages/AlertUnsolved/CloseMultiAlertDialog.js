import React,{useEffect, useState} from 'react';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../../components/CusDialog';
import Divider from '@material-ui/core/Divider';
import {DialogTitleStyle,CloseBtnStyle,DialogContentStyle,DialogActionsStyle} from '../../components/CusDialog/style';
import { ErrorAlert } from '../../components/CusAlert';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'
// component
import CloseMultiAlertTable from './CloseMultiAlertTable';
import { CusMainBtnStyle } from '../../components/CusMainBtnStyle';




const CloseMultiAlertDialog = ({
    isDisabledBtn,                                         // 防止重複點擊
    username,
    open,
    data,
    errorMsg,
    resetErrorMessage,                                     // 清空失敗訊息
    perPage,
    handleSubmit,
    handleOpen,
    handleClose
}) => {
    const { t } = useTranslation();
    const [showTable,setShowTable] = useState(true);       // 是否顯示表格
    const [list,setList] = useState({
        msg: {
            EventSeq: [],                                  // 寫下你的解決方案
            CloseContent: '',
            UserName: username
          }
    })
    
    // 相當於componentDidUpdate
    useEffect( ()=> {
        if(data ) {
            const seqList = data.filter( (filterItem) => filterItem.checked === true).map((item)=>{return item.EventSeq});
            setList({msg:{
                EventSeq: seqList,                         // 寫下你的解決方案
                CloseContent: '',
                UserName: username
            }})
        }

        // 相當於componentWillUnmount
        return () => {
            setList({})
        }
    },[data,username])

    // 變更解決方案
    const handleTextChange = (e) => {
        if(e.target.value.length <= 255) {
            setList({
                msg:{
                ...list.msg,
                CloseContent: e.target.value
            }
            })
        }
    }



    return (
        <CusDialog 
            open={open}
            maxWidth={'sm'}
            handleOpen={handleOpen} 
            handleClose={handleClose}
        >
            {/* 批次解決告警 */}
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                <div className="col-6 p-0 d-inline-block">{t('1309')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                </div>
            </DialogTitle>
            <DialogContent style={DialogContentStyle}>
                <div className="alertDialog_accordion">
                    <div className="alertDialog_accordion-title">
                        {
                            showTable ? (
                                <div onClick={()=>{setShowTable(false)}} className="p-2">
                                    <i className="fa fa-minus"/> {t('1322')}
                                </div>
                            ) : (
                                <div onClick={()=>{setShowTable(true)}} className="p-2">
                                <i className="fa fa-plus"/> {t('1310')}
                                </div>
                            )
                        }
                    </div>
                </div>
                {
                    showTable ? <CloseMultiAlertTable data={data} perPage={perPage} /> : ''
                }
                <Divider />
                <div className="col-12 mt-4 p-0 text-center">
                    {/* 寫下你的解決方案 */}
                    <div className="alertDialog_subTitle">{t('1307')}: ({t('1311')})</div>
                    <div className="col-xl-10 px-0 d-inline-block" >
                        <textarea 
                            value={list.msg.CloseContent} 
                            onChange={(e)=>{handleTextChange(e)}} 
                            rows={8}
                            cols={48}
                            style={{width:'100%'}}
                        />
                    </div>
                </div>
                <div>
                {
                    (errorMsg && errorMsg !== '') ? <ErrorAlert message={errorMsg} resetMessage={resetErrorMessage} /> : ''
                }
                </div>
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
                {/* 取消 */}
                <Button onClick={handleClose}>{t('1003')}</Button>
                {/* 標記為[已解決] */}
                {
                    isDisabledBtn ? <Button variant="contained" disabled>{t('1308')}</Button> 
                    : <CusMainBtnStyle name={t('1308')} icon="fas fa-check" clickEvent={()=>{handleSubmit(list)}} />
                }
            </DialogActions>
        </CusDialog>
    )
}
export default CloseMultiAlertDialog;