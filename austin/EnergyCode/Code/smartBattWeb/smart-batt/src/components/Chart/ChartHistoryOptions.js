import React from 'react';
import { useTranslation } from 'react-i18next'
import PropTypes from 'prop-types';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';


const ITEM_HEIGHT = 48;


const ChartHistoryOptions = ({handlePrintEvent,handleCSVEvent,handleExcelEvent,handlePDFEvent}) => {
  const { t } = useTranslation();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  
    const handleClick = (event) => {
      setAnchorEl(event.currentTarget);
    };
  
    const handleClose = () => {
      setAnchorEl(null);
    };
  
    return (
      <div>
        <div className="btn_icon" onClick={handleClick}>
            <i className='fas fa-ellipsis-v' />
        </div>
        <Menu
          id="long-menu"
          anchorEl={anchorEl}
          keepMounted
          open={open}
          onClose={handleClose}
          PaperProps={{
            style: {
              maxHeight: ITEM_HEIGHT * 4.5,
              width: '24ch',
            },
          }}
        >
          {/* 列印 */}
          <MenuItem onClick={()=>{handlePrintEvent()}}>
            <ListItemIcon> <i className="fas fa-print" /> </ListItemIcon>
            <ListItemText primary={t('1005')}  />
          </MenuItem>
          {/* CSV */}
          <MenuItem onClick={()=>{handleCSVEvent()}}>
            <ListItemIcon> <i className="fas fa-file-csv"/> </ListItemIcon>
            <ListItemText primary={t('1006')} />
          </MenuItem>
          {/* Excel */}
          <MenuItem onClick={()=>{handleExcelEvent()}}>
            <ListItemIcon> <i className="fas fa-file-excel" /> </ListItemIcon>
            <ListItemText primary={t('1099')} />
          </MenuItem>
          {/* PNG */}
          {/* <MenuItem onClick={()=>{handlePNGEvent()}}>
            <ListItemIcon> <i className="fas fa-file-excel" /> </ListItemIcon>
            <ListItemText primary={t('1007')} />
          </MenuItem> */}
          {/* 下載JPEG */}
          {/* <MenuItem onClick={()=>{handleJPEGEvent()}}>
            <ListItemIcon> <i className="fas fa-file-excel" /> </ListItemIcon>
            <ListItemText primary={t('1008')} />
          </MenuItem> */}
          {/* PDF */}
          {/* <MenuItem onClick={()=>{handlePDFEvent()}}>
            <ListItemIcon> <i className="fas fa-file-pdf" /> </ListItemIcon>
            <ListItemText primary={t('1009')} />
          </MenuItem> */}
        </Menu>
      </div>
    );
}
ChartHistoryOptions.defaultProps = {
  handlePrintEvent: () => {},
  handleExcelEvent: () => {},
  handlePNGEvent: () => {},
  handleJPEGEvent: () => {},
  handlePDFEvent: () => {},
}
ChartHistoryOptions.propTypes = {
  handlePrintEvent: PropTypes.func,
  handleExcelEvent: PropTypes.func,
  handlePNGEvent: PropTypes.func,
  handleJPEGEvent: PropTypes.func,
  handlePDFEvent: PropTypes.func,
}
export default ChartHistoryOptions;


