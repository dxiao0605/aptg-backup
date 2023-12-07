import React from "react";
import PropTypes from 'prop-types';
// component
import ChartZoomIn from './ChartZoomIn';
import ChartRefresh from './ChartRefresh';
import ChartOptions from './ChartOptions';


const ChartHeader = ({title,refreshChartData,handlePrintEvent,handleExcelEvent,handlePDFEvent,content}) => {
	return (
		<div className='chart_header'>
			<div className='col-6 d-inline-block chart_header-title'>{title}</div>
			<div className=' col-6 d-inline-block text-right'>
				{/* zoom in */}
				<div className='p-2 d-inline-block'>
					<ChartZoomIn title={title} children={content} />
				</div>
				{/* refresh */}
				<div className='p-2 d-inline-block'>
					<ChartRefresh refreshChartData={refreshChartData} />
				</div>
				{/* options */}
				<div className='p-2 d-inline-block'>
					<ChartOptions handlePrintEvent={handlePrintEvent} handleExcelEvent={handleExcelEvent} handlePDFEvent={handlePDFEvent} />
				</div>
			</div>
		</div>
	);
};

ChartHeader.default = {
	title: '',
	refreshChartData: () => {},
	handleExcelEvent: () => {},
	handlePrintEvent: () => {},
	handlePDFEvent: () => {},
}
ChartHeader.propTypes = {
	refreshChartData: PropTypes.func,
	handleExcelEvent: PropTypes.func,
	handlePrintEvent: PropTypes.func,
	handlePDFEvent: PropTypes.func,
}
export default ChartHeader;
