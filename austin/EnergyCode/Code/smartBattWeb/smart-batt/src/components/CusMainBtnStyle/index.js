import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';




const ColorButton = withStyles((theme) => ({
  root: {
    color: '#ffffff',
    backgroundColor: '#03c3ff',
    '&:hover': {
      backgroundColor: '#03c3ff',
    },
  },
}))(Button);




export const CusMainBtnStyle = ({ name, icon, fullWidth, clickEvent, disabled }) => {
  const keyDownEvent = (e) => {
    // "Enter" 
    if(e.keyCode === 13 ) {
       return clickEvent
    }else {
      return e.preventDefault()
    }
  }

  return (
    <ColorButton variant="contained" color="primary" className={fullWidth ? 'w-100' : ''} disabled={disabled} onClick={clickEvent} onKeyDown={keyDownEvent}>
      {icon === '' ? '' : <i className={icon} />}
      {name}
    </ColorButton>
  );
}

CusMainBtnStyle.defaultProps = {
  fullWidth: false,
  name: '',
  icon: '',
  disabled: false,
  clickEvent: () => {}
}
CusMainBtnStyle.propTypes = {
  fullWidth: PropTypes.bool,
  icon: PropTypes.string,
  disabled: PropTypes.bool,
  clickEvent: PropTypes.func,
}