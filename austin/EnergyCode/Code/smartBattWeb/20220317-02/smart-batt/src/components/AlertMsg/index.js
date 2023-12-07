import React, { Component } from 'react';
import { Trans } from 'react-i18next';
import PropTypes from 'prop-types';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';
import { Button } from '@material-ui/core';

class AlertMsg extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
    }
    componentDidMount() {
        this.props.onIsRefreshChange(false);//關掉計時器
    }
    componentWillUnmount() {
        this.props.onIsRefreshChange(true);//開啟計時器(平常沒作用，除非組件被刪除)
    }
    render() {
        const { open, isDisabledBtn, handleClose, msgTitle, children } = this.props;
        return (
            <CusDialog
                open={open}
                maxWidth={'sm'}
                handleClose={handleClose}
                style={{ zIndex: 1500 }}
            >
                <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                    <div className="col-9 p-0 d-inline-block">
                        <Trans i18nKey={msgTitle} />
                    </div>
                    <div className="col-3 m-0 p-0 d-inline-block text-right">
                        <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                    </div>
                </DialogTitle>
                <DialogContent style={DialogContentStyle}>
                    {children}
                </DialogContent>
                <DialogActions style={DialogActionsStyle}>
                    {/* 確認 */}
                    {
                        isDisabledBtn ? <Button variant="contained" disabled><Trans i18nKey="1010" /></Button>
                            : <CusMainBtnStyle name={<Trans i18nKey="1010" />} icon="fas fa-check" clickEvent={handleClose} />
                    }
                </DialogActions>
            </CusDialog>
        )
    }
}

AlertMsg.defaultProps = {
    msgTitle: '',
    open: false,
    isDisabledBtn: false,
    handleClose: () => { },
    onIsRefreshChange: () => { },
}
AlertMsg.propTypes = {
    msgTitle: PropTypes.string,
    isDisabledBtn: PropTypes.bool,
    open: PropTypes.bool,
    handleClose: PropTypes.func,
    onIsRefreshChange: PropTypes.func,
}

export default AlertMsg;


