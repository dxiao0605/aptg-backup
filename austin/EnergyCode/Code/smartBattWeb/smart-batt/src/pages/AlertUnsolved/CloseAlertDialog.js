import React,{useState} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../../components/CusDialog';
import Divider from '@material-ui/core/Divider';
import FormControl from "@material-ui/core/FormControl";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import {DialogTitleStyle,CloseBtnStyle,DialogContentStyle,DialogActionsStyle} from '../../components/CusDialog/style';
import { ErrorAlert } from '../../components/CusAlert';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'
// component
import LocationMarkerMap from '../../components/LocationMarkerMap';
import StatusDesc from '../../components/CusTable/StatusDesc';
import {CusMainBtnStyle} from '../../components/CusMainBtnStyle';



const useStyles = makeStyles((theme) => ({
	formControl: {
	  margin: theme.spacing(0),
	  minWidth: 120,
	}
}));



const CloseAlertDialog = ({
    isDisabledBtn,
    isEdit,                                         //判斷是否有可編輯權限
    username,
    data,
    ApiKey,                                         //google地圖金鑰
    open,
    errorMsg,
    resetErrorMessage,                              //清空失敗訊息
    handleOpen,
    handleClose,
    handleSubmit,
}) => {
	const classes = useStyles();
    const { t } = useTranslation();
	const [selectStatus, setSelectStatus] = React.useState('0');
    const [list,setList] = useState({
        msg: {
            EventSeq: [data.EventSeq],              //單一個(寫下你的解決方案)
            CloseContent:'',
            UserName: username,
        }
    })



    // 變更解決方案
    const handleTextChange = (e) => {
        if(e.target.value.length <= 255) {
            setList({
                msg: {
                    ...list.msg,
                    CloseContent: e.target.value,
                }
            })
        }
    }

    // 變更顯示的電池狀態
    const handleChange = (event) => {
		setSelectStatus(event.target.value);
	};



    return (
        <CusDialog 
            open={open}
            maxWidth={'md'}
            handleOpen={handleOpen} 
            handleClose={handleClose}
        >
            {/* 告警詳情 */}
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                <div className="col-6 p-0 d-inline-block">{t('1320')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                </div>
            </DialogTitle>
            <DialogContent style={DialogContentStyle}>
                {/* EventStatus */}
                <div className="col-12 p-0">
                    
                    <div className="alertDialog_leftSide">
                        
                        {/* 類型 */}
                        <div className='p-0 align-top'>{t('1305')}:
                            <span className="d-inline-block">
                                <StatusDesc StatusCode={Number(data.EventTypeCode)}/>
                            </span>
                        </div>
                        
                        {/* 數據時間 */}
                        {t('1036')}: <div className="col-12 col-sm-6 d-inline-block p-0 text-left">{data.RecTime}</div>
                    </div>


                    {/* 告警條件 */}
                    <div className="alertDialog_rightSide">
                        {
                            //需更換時顯示電池狀態
                            data.EventTypeCode === '3'  ? (
                                <>
                                {t('1021')}:
                                <FormControl className={classes.formControl}>
                                    <Select
                                        value={selectStatus}
                                        onChange={handleChange}>
                                        {
                                            data.Status ? data.Status.map( (item,idx) => {
                                                return <MenuItem key={idx} value={`${idx}`}>CH{idx+1} <div className="d-inline-block"><StatusDesc StatusCode={Number(item)}/></div></MenuItem>
                                            }): ''
                                        }
                                    </Select>
                                </FormControl>
                                </>
                            ): ''
                        }
                        <div>
                            {/* 告警條件 */}
                            {t('1303')}:
                            {/* 判斷值 */}
                            <div className={`${data.EventTypeCode === '3' ? 'col-8' : 'col-12'} pl-0 d-inline-block align-top`}>
                                {
                                    // 需更換顯示判斷值
                                    data.EventTypeCode === '3' ? (
                                        <>
                                            <div>{t('1032')}:{data.Alert1}</div>
                                            <div>{t('1033')}:{data.Alert2}</div>
                                        </>
                                    ):(
                                        // 離線顯示離線時間(小時)
                                        <div className="d-inline-blcok">
                                            {//時間
                                                data.EventTypeCode === '4' && <>{t('1034')} {data.Disconnect}{t('1035')}</>
                                            }
                                            {//溫度
                                                data.EventTypeCode === '25' && <>{t('1034')} {data.Temperature1}{t('1325')}</>  
                                            }
                                        </div>

                                    )
                                }
                                
                            </div>
                        </div>
                    </div>
                </div>
                <Divider />
                <div className="col-12 mt-4 p-0">
                    {/* 地址 */}
                    <div className="alertDialog_locationMarker">
                        <div>{t('1031')}:{data.Address} </div>
                        <div className="col-12 mt-2 mb-2 pl-0 pr-0">
                            <LocationMarkerMap 
                                data={[data]}
                                ApiKey={ApiKey}
                                mapCenter={{lat:parseFloat(data.Lat),lng:parseFloat(data.Lng)}}
                                dfZoom={18}
                                mapHeight={'280px'}
                            />
                        </div>
                    </div>
                    {/* 寫下你的解決方案 */}
                    <div className="alertDialog_closeContent">
                        <div className="pb-2">{t('1307')}: </div>
                        <textarea                            
                            value={list.msg.CloseContent} 
                            onChange={(e)=>{handleTextChange(e)}} 
                            rows={11}
                            cols={52}
                            style={{width:'100%'}}
                        />
                    </div>
                </div>
                
                {
                    (errorMsg && errorMsg !== '') ? <ErrorAlert message={errorMsg} resetMessage={resetErrorMessage} /> : ''
                }

                
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
            {/* 取消 */}
            <Button onClick={handleClose}>{t('1003')}</Button>
            {/* 標記為[已解決] */}
            {
                (isDisabledBtn || isEdit === 0) ? <Button variant="contained" disabled>{t('1308')}</Button>
                :(<CusMainBtnStyle name={t('1308')} icon="fas fa-check" clickEvent={()=>{handleSubmit(list)}} />)
            }
            
            </DialogActions>
        </CusDialog>
    )
}
export default CloseAlertDialog;