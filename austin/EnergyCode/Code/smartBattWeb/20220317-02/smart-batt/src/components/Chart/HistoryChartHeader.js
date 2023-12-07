import React from "react";
// component
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

HistoryChartHeader.default = {
	title: '',
	handlePrintEvent: ()=>{},
	handleCSVEvent: () => {},
	handleExcelEvent: ()=>{},
	handlePDFEvent: ()=>{},
}
export default HistoryChartHeader;
