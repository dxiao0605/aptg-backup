import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Fade from '@material-ui/core/Fade';

const FadeMenu = ({ data, menuList, onMenuClick }) => {
    const { t } = useTranslation();
    const [anchorEl, setAnchorEl] = useState(null);//展開選單欄

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };
    const onClick = ({ event, data, item }) => {
        setAnchorEl(null);
        onMenuClick({ event, data, item })
    }

    return (
        <div className="d-flex justify-content-center">
            <div className="btn_icon" onClick={handleClick}>
                <i className="fas fa-ellipsis-v" />
            </div>
            <Menu
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleClose}
                TransitionComponent={Fade}
                // PaperProps={{
                //     style: {
                //         transform: 'translateX(-71%) translateY(32%)',
                //     }
                // }}
                getContentAnchorEl={null}
                anchorOrigin={{
                    vertical: 'center',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                {
                    menuList?.length > 0 &&
                    menuList.map((item, idx) => (<MenuItem key={idx} onClick={(event) => onClick({ event, data, item })}>{t(item)}</MenuItem>))
                }
            </Menu>
        </div>
    );
}
FadeMenu.defaultProps = {
    data: {},
    menuList: [],
    onMenuClick: () => { }
}
FadeMenu.propTypes = {
    data: PropTypes.object,
    menuList: PropTypes.array,
    onMenuClick: PropTypes.func,
}
export default FadeMenu;