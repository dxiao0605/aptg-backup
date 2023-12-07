import React,{useState} from 'react';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../../components/CusDialog';
import Divider from '@material-ui/core/Divider';
import {DialogTitleStyle,CloseBtnStyle,DialogContentStyle,DialogActionsStyle} from '../../components/CusDialog/style';
import { useTranslation } from 'react-i18next'
// component
import LocationMarkerMap from '../../components/LocationMarkerMap';
import StatusDesc from '../../components/CusTable/StatusDesc';



const AlertInfoDialog = ({
    data,
    ApiKey,             //google地圖金鑰
    open,
    handleOpen,
    handleClose,
}) => {
    const { t } = useTranslation();
    const [textValue,setTextValue] = useState('')              //寫下你的解決方案



    // 變更解決方案
    const handleTextChange = (e) => {
        if(e.target.value.length <= 255) {
            setTextValue(e.target.value)
        }
        
    }
    
    
    
    // 保存
    const handlesubmit = () => {}


    return (
        <CusDialog 
            open={open}
            maxWidth={'md'}
            handleOpen={handleOpen} 
            handleClose={handleClose}
        >
            {/* 用戶設定 */}
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                <div className="col-6 p-0 d-inline-block">{t('1318')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                </div>
            </DialogTitle>
            <DialogContent style={DialogContentStyle}>

                {/* 類型 */}
                <div className="col-12 p-0">{t('1305')}:<span className="d-inline-block"><StatusDesc StatusCode={Number(data.EvenTypeCode)}/></span></div>
                {/* EventStatus */}
                
                <div className="col-12 p-0">
                    {/* 發生時間 */}
                    <div className="alertDialog_leftSide">
                        {t('1306')}: <div className="col-12 col-sm-6 d-inline-block p-0 text-left">{data.OccurTime}</div>
                    </div>
                    {/* 告警條件 */}
                    <div className="alertDialog_rightSide">
                        {t('1303')}:
                        {data.CloseUser}
                        
                        {/* 判斷值 */}
                        <div className="col-8 pl-0 d-inline-block align-top">
                            <div>{t('1032')}:{data.Alert1}</div>
                            <div>{t('1033')}:{data.Alert2}</div>
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
                        <div>{t('1307')}: {data.CloseContent}  </div>
                        <textarea 
                            value={textValue} 
                            onChange={(e)=>{handleTextChange(e)}} 
                            rows={12}
                            cols={48}
                        />
                    </div>
                </div>

                
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
            {/* 取消 */}
            <Button onClick={handleClose}>{t('1003')}</Button>
            {/* 標記為[已解決] */}
            <Button onClick={handlesubmit} color="primary"><i className="fas fa-check" />{t('1308')}</Button>
            </DialogActions>
        </CusDialog>
    )
}
export default AlertInfoDialog;