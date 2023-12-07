import React,{useState,Fragment} from 'react';
import PropTypes from 'prop-types';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';

const SelectPerPageCmd = ({perPage,getPerPage}) => {
    const { t } = useTranslation();
    const [row, setRow] = useState(perPage)
    const onChangePerPage = (event) => {
        setRow(event.target.value)
        getPerPage(event.target.value)
    }    
    return (
        <Fragment>
            {t('1042')} {/* 顯示 */}
            <Select
                onChange={(event)=>{onChangePerPage(event)}}
                value={row}>
                <MenuItem value='5'>5</MenuItem>
                <MenuItem value='10'>10</MenuItem>
                <MenuItem value='20'>20</MenuItem>
                <MenuItem value='50'>50</MenuItem>
            </Select>
            {t('1075')} {/* 站台 */}
        </Fragment>
    )
}
SelectPerPageCmd.propTypes = {
    perPage: PropTypes.number,
}
export default SelectPerPageCmd;