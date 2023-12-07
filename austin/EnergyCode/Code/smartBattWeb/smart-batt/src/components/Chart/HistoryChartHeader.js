import React from "react";
import PropTypes from 'prop-types';
import ChartHistoryOptions from './ChartHistoryOptions';


const HistoryChartHeader = ({title,handlePrintEvent,handleCSVEvent,handleExcelEvent,handlePDFEvent}) => {
	return (
		<div className='chart_header history'>
			<div className='d-inline-block text-center chart_header-historyTitle'>
				{title}

				{/* options */}
				<div className='btn--options'>
					<ChartHistoryOptions handlePrintEvent={handlePrintEvent} handleCSVEvent={handleCSVEvent} handleExcelEvent={handleExcelEvent} handlePDFEvent={handlePDFEvent} />
				</div>
			</div>
		</div>
	);
};

HistoryChartHeader.defaultProps = {
	title: '',
	handlePrintEvent: ()=>{},
	handleCSVEvent: () => {},
	handleExcelEvent: ()=>{},
	handlePDFEvent: ()=>{},
}
HistoryChartHeader.propTypes = {
	handlePrintEvent: PropTypes.func,
	handleCSVEvent: PropTypes.func,
	handleExcelEvent: PropTypes.func,
	handlePDFEvent: PropTypes.func,
}
export default HistoryChartHeader;
