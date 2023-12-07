import React from 'react';
import PropTypes from 'prop-types';

const ChartRefresh = ({refreshChartData}) => {
    return (
        <div className="btn_icon" onClick={refreshChartData}>
            <i className='fas fa-redo-alt' />
        </div>

    )
}
ChartRefresh.defaultProps = {
    refreshChartData: () =>{},
}
ChartRefresh.propTypes = {
    refreshChartData: PropTypes.func,
}
export default ChartRefresh;