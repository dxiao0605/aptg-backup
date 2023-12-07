import React, { Component, Fragment } from "react";
import { Line } from "react-chartjs-2";
import "chart.js";
import "chartjs-plugin-zoom";
import "chartjs-plugin-annotation";
import "chartjs-plugin-datalabels";
import { CardSpinner } from "../../components/Spinner";

class CusChart extends Component {
	constructor(props) {
		super(props);
		this.state = {
			data: {},
			tooltipData: null,
		};
		this.chartRef = React.createRef();
	}


	componentDidMount() {
		this.setState({
			data: this.props.data
		})
	}
	componentDidUpdate(prevProps, prevState) {
		if (this.props.data !== prevState.data) {
			this.setState({
				data: prevProps.data
			})
		}
		//變更螢幕尺寸，重整頁面
		if( this.props.windowSize !== prevProps.windowSize) {
			window.location.reload(false);
		}
	}


	render() {
		const { stepSize, unitStepSize, showFloatValue, windowSize } = this.props;
		return (
			<Fragment>
				{
					(this.props.data) ? (
						<Line
							ref={this.chartRef}
							// height={80}
							height={windowSize <= 1366 ? (windowSize <= 1024 ? (windowSize < 768 ? (windowSize <= 568 ? 320 : 280) : 155 ): 105) : 90}
							data={{
								labels: this.props.labels,
								datasets: this.props.data,
							}}
							legend={{
								position: "top",
								fullWidth: true,
								layout: {
									padding: 15
								},
								labels: {
									boxWidth: 10,
									fontSize: 13,
									padding:  15,
								},
							}}
							options={{
								legend: {
									display: true,
									maintainAspectRatio: false,
									gridLines: {
										offsetGridLines: false,
									},
								},
								layout: {
									padding: {top:50,bottom:5},
								},
								elements: {
									point: {
										radius: 0
									}
								},
								scales: {
									xAxes: [
										{
											type: "time",
											time: {
												displayFormats: { month: "YYYY/MM", day: "YYYY/MM/DD", hour: 'YYYY/MM/DD HH:mm ', minute: 'YYYY/MM/DD HH:mm', second: 'YYYY/MM/DD HH:mm:ss' },
												unit: 'second',
												unitStepSize: unitStepSize, //  24Hr:3600 | 7D:25200 | 1Mouth:108000 
											},
											stacked: true,
											ticks: {
												autoSkip: false,
												// maxRotation: 90,
												// minRotation: 90,
												maxRotation: 70,
												minRotation: 70,
												padding: 8,
											},
											gridLines: {
												drawOnChartArea: false
											},
										},
									],
									yAxes: [
										{
											id: "state1-y-axis",
											type: "linear",
											ticks: {
												stepSize: stepSize,
												padding: 3,
												callback: function(value) {
													return showFloatValue ? value.toFixed(1) : value
												  },
												type: 'logarithmic',
											},
											gridLines: {
												  display: true,
												  categorySpacing: 90,
												  drawBorder: false,
												  color: "rgba(0, 0, 0, 0.1)"
											},
										},
									],
								},
								plugins: {
									datalabels: {
										display: false,
									},
								},
								padn: {
									enabled: true,
									mode: 'x',
								},
								zoom: {
									enabled: true,
									mode: 'x',
								},
								tooltips: {
									mode: 'index',
									intersect: false,
									yAlign: 'center',
									titleFontSize: 11,
									bodyFont: 11,
								},
								hover: {
									mode: 'index',
									intersect: false,
								}
							}}
						/>
					) : <CardSpinner />
				}
			</Fragment>
		);
	}

}

CusChart.defaultProps = {
	labels: [],
	data: [],
	maxValue: 0,
	minValue: 0,
	stepSize: 1,
	unitStepSize: 3600, //  24Hr:3600 | 7D:25200 | 1Mouth:108000 
	showFloatValue: false,
	windowSize: [0,0],
}
export default CusChart;
