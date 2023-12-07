import React , {useState} from 'react';
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
import { useTranslation } from 'react-i18next'
// component
import LocationMarkerMap from '../../components/LocationMarkerMap';
import StatusDesc from '../../components/CusTable/StatusDesc';



const useStyles = makeStyles((theme) => ({
	formControl: {
	  margin: theme.spacing(0),
	  minWidth: 120,
	}
}));



const CloseAlertDialog = ({
    data,
    ApiKey,                                         //google地圖金鑰
    open,
    handleOpen,
    handleClose,
}) => {
	const classes = useStyles();
    const { t } = useTranslation();
    const [selectStatus, setSelectStatus] = useState('0');
    

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
                <>
                {/* 類型 */}
                <div className="col-12 p-0">{t('1305')}:<span className="d-inline-block"><StatusDesc StatusCode={Number(data.EventTypeCode)}/></span></div>
                {/* EventStatus */}
                
                <div className="col-12 p-0">
                    {/* 數據時間 */}
                    <div className="alertDialog_leftSide">
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
                                                return <MenuItem value={`${idx}`}>CH{idx+1} <div className="d-inline-block"><StatusDesc StatusCode={Number(item)}/></div></MenuItem>
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
                                            {
                                                data.EventTypeCode === '4' &&<>{t('1034')} {data.Disconnect}{t('1035')}</>
                                            }
                                            {
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
                    {/* 狀態、解決時間、解決人員、解決方案 */}
                    <div className="alertDialog_closeContent">
                        <div>{t('1317')}:<StatusDesc StatusCode={Number(data.EventStatus)}/></div>
                        <div>{t('1312')}:{data.CloseTime}</div>
                        <div>{t('1313')}:{data.CloseUser}</div>
                        <div>{t('1314')}:{data.CloseContent}</div>
                    </div>
                </div>

                </>
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
            {/* 關閉 */}
            <Button onClick={handleClose}>{t('1024')}</Button>
            
            </DialogActions>
        </CusDialog>
    )
}
export default CloseAlertDialog;