import React from 'react';


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
export default ChartRefresh;