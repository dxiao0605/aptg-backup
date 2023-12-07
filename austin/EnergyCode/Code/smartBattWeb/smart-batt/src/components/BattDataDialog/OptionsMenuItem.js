import React from "react";
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemText from '@material-ui/core/ListItemText';



const OptionsMenuItem = ({name,enabled,handleItemClick}) => {
    const { t } = useTranslation();
	switch (enabled) {
        case 1:
            return (
                <MenuItem
                    onClick={() => {
                        handleItemClick(name);
                    }}>
                    <ListItemText primary={t(name)} />
                </MenuItem>
            );
        case 2:
            return (
                <MenuItem
                    onClick={() => {
                        handleItemClick(name);
                    }}>
                    <ListItemText primary={t(name)} />
                </MenuItem>
            );
        default:
            return "";
    }
}
OptionsMenuItem.defaultProps = {
    name: '',
    enabled: 3,
    handleItemClick: () =>{},
}
OptionsMenuItem.propTypes = {
    name: PropTypes.string,
    enabled: PropTypes.number,
    handleItemClick: PropTypes.func,
}
export default OptionsMenuItem;
