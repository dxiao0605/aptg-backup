import React, { Component } from 'react';
import { Trans } from 'react-i18next';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';
import './index.css';

class AlertDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    componentDidMount() {
        this.props.onIsRefreshChange(false);//關掉計時器
    }
    componentWillUnmount() {
        this.props.onIsRefreshChange(true);//開啟計時器(平常沒作用，除非組件被刪除)
    }
    render() {
        const { open, maxWidth, handleClose, handleSubmit, openTitleI18n, title, titleMore, sumbitBtnText, children, className } = this.props;
        return (
            <CusDialog
                open={open}
                maxWidth={maxWidth}
                handleClose={handleClose}
                style={{ zIndex: 1200, }}
            >
                <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                    <div className="col-9 p-0 d-inline-block">
                        {
                            openTitleI18n ? <Trans i18nKey={title} /> :
                                <div className="form-inline">
                                    <div><Trans i18nKey={title} /></div>
                                    <div>{titleMore}</div>
                                </div>
                        }
                    </div>
                    <div className="col-3 m-0 p-0 d-inline-block text-right">
                        <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                    </div>
                </DialogTitle>
                <DialogContent className={`${className}`} style={DialogContentStyle}>
                    {children}
                </DialogContent>
                <DialogActions style={DialogActionsStyle}>
                    {/* 取消 */}
                    <Button onClick={handleClose}><Trans i18nKey="1003" /></Button>
                    {/* 確認 */}
                    <CusMainBtnStyle name={<Trans i18nKey={sumbitBtnText} />} disabled={this.props.disabled} icon="fas fa-check" clickEvent={handleSubmit} />
                </DialogActions>
            </CusDialog>
        )
    }
}

AlertDialog.defaultProps = {
    disabled: false,
    openTitleI18n: true,
    titleMore: '',
    data: {},
    maxWidth: 'md',
    sumbitBtnText: '1010',      // 確認按鈕(name)
    className:'',
    handleOpen: () => {},
    onIsRefreshChange: () => {},
}
AlertDialog.propTypes = {
    disabled: PropTypes.bool,
    openTitleI18n: PropTypes.bool,
    className: PropTypes.string,
    title: PropTypes.string,
    titleMore: PropTypes.string,
    data: PropTypes.object,
    open: PropTypes.bool,
    handleOpen: PropTypes.func,
    handleClose: PropTypes.func,
    handleSubmit: PropTypes.func,
}

export default AlertDialog;


