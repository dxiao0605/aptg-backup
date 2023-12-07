import React from 'react';
import { useTranslation } from 'react-i18next';
import ContentAddEdit from '../../pages/BattManage/Page1501/ContentAddEdit';
import {inputList} from '../../pages/BattManage/Page1501/InitDataFormat';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';


const BatteryEditDialog = ({
    list,   //變更清單
    BatteryTypeNameList,
    onDialogClose,
    handleInputChange,
    handleSelectChange,
    handleDateChange,
    handleSubmit
}) => {
    const { t } = useTranslation();
    return (
        <CusDialog
            open={true}
            maxWidth='sm'
            handleClose={onDialogClose}
        >
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                <div className="col-6 p-0 d-inline-block">{t('1551')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={onDialogClose} />
                </div>
            </DialogTitle>
            <DialogContent className="overflowY" style={DialogContentStyle}>
                {
                    (list && list.InstallDate !== "Invalid date") ? (
                        <ContentAddEdit
                            data={{date: new Date(list.InstallDate) ,BatteryGroupIDValue:list.BatteryGroupID,BatteryTypeValue:list.BatteryType.Label}}
                            BatteryTypeNameList={BatteryTypeNameList}
                            inputList={inputList}
                            handleSelectChange={(event) => handleSelectChange(event)}
                            handleInputChange={(event) => handleInputChange(event)}
                            handleDateChange={(date) => handleDateChange(date)}
                        /> 
                    ) : (
                        <ContentAddEdit
                            data={{date: null ,BatteryGroupIDValue:list.BatteryGroupID,BatteryTypeValue:list.BatteryType.Label}}
                            BatteryTypeNameList={BatteryTypeNameList}
                            inputList={inputList}
                            handleSelectChange={(event) => handleSelectChange(event)}
                            handleInputChange={(event) => handleInputChange(event)}
                            handleDateChange={(date) => handleDateChange(date)}
                        /> 
                    )
                }
                 
            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
                {/* 取消 */}
                <Button onClick={onDialogClose}>{t('1003')}</Button>
                {/* 確認 */}
                <CusMainBtnStyle name={t('1010')} icon="fas fa-check" clickEvent={handleSubmit} />
            </DialogActions>
        </CusDialog>
    )
}
BatteryEditDialog.defaultProps = {
    list: {
        InstallDate: null,
    },
    BatteryTypeNameList:[],
    onDialogClose: () => {},
    handleInputChange: () => {},
    handleSelectChange: () => {},
    handleDateChange: () => {},
    handleSubmit: () => {},
    getListData: () => {},          //前一次資料(預設值)
}
export default BatteryEditDialog;