import React, { Component, Fragment } from "react";
import { Line } from "react-chartjs-2";
import { connect } from "react-redux";
import "chart.js";
import "chartjs-plugin-zoom";
import "chartjs-plugin-annotation";
import "chartjs-plugin-datalabels";
import { CardSpinner } from "../Spinner";

class BattStateLineChart extends Component {
	constructor(props) {
		super(props);
		this.state = {
			data: {},
			label:[],
		};
	}


	componentDidMount() {
		const { data,language } = this.props;
		
		// 資料是否存在
		if(data){
			this.setState({
				data: data
			})
			// 確認語系
			switch(language) {
				case 1:
					this.setState({label:this.props.data.Label});
					break;
				case 2:
					this.setState({label:this.props.data.LabelE})
					break;
				case 3:
					this.setState({label:this.props.data.LabelJ})
					break;
				default:
					this.setState({label:this.props.data.Label})
					break;
			}
		}
	}
	componentDidUpdate(prevProps, prevState){
		if(this.props.data !== prevState.data){
			this.setState({
				data: prevProps.data
			})
		}
	}

	render() {
		const {data,label} = this.state;
		return (
			<Fragment>
				{
					(this.props.data && data !== {} && label.length > 0) ? (
						<Line
							height={80}
							data={{
								labels: this.state.data.RecDate,
								datasets: [
									{
										type: "line",
										label: label[0],
										data: this.state.data.Status1,
										backgroundColor: "rgba(32,140,38,1)",
										pointBackgroundColor: "rgba(32,140,38,1)",
										borderColor: "rgba(32,140,38,1)",
										fill: "none",
										lineTension: 0,			//線不圓滑
										order: 0,
									},
									{
										type: "line",
										label: label[1],
										data: this.state.data.Status2,
										backgroundColor: 'rgba(255,190,23,1)',
										pointBackgroundColor: 'rgba(255,190,23,1)',
										borderColor: 'rgba(255,190,23,1)',
										fill: "none",
										lineTension: 0,			//線不圓滑
										order: 1,
									},
									{
										type: "line",
										label: label[2],
										data: this.state.data.Status4,
										backgroundColor: 'rgba(47,197,185,1)',
										pointBackgroundColor: 'rgba(47,197,185,1)',
										borderColor: 'rgba(47,197,185,1)',
										fill: "none",
										lineTension: 0,			//線不圓滑
										order: 2,
									},
									{
										type: "line",
										label: label[3],
										data: this.state.data.Status3,
										backgroundColor: "rgba(221,40,73,1)",
										pointBackgroundColor: "rgba(221,40,73,1)",
										borderColor: "rgba(221,40,73,1)",
										fill: "none",
										lineTension: 0,			//線不圓滑
										order: 3,
									},
								],
							}}
							legend={{
								position: "bottom",
								fullWidth: true,
								labels: {
									boxWidth: 10,
									fontSize: 13
								},
							}}
							options={{
								legend: {
									display: true,
									maintainAspectRatio : false,
									gridLines: {
										offsetGridLines: false,
									},
									layout: {
										padding: {
											left: 0,
											right: 0,
											top: 0,
											bottom: 0,
										},
									},
								},
								scales: {
									xAxes: [
										{
											stacked: true,
											gridLines: {
												display:false
											}
										},
									],
									yAxes: [
										{
											id: "state1-y-axis",
											type: "linear",
											ticks: {
												beginAtZero: true,
												stepSize: 1,
												callback: function (label, index, labels) {
													return label.toLocaleString(navigator.language, {
														minimumFractionDigits: 0,
													});
												},
											},
										},
										{
											id: "state2-y-axis",
											type: "linear",
											ticks: {
												beginAtZero: true,
												stepSize: 1,
												display: false,
												callback: function (label, index, labels) {
													return label.toLocaleString(navigator.language, {
														minimumFractionDigits: 0,
													});
												},
											},
											gridLines: {
												display: false,
												drawBorder: false
											}
										},
										{
											id: "state3-y-axis",
											type: "linear",
											ticks: {
												beginAtZero: true,
												stepSize: 1,
												display: false,
												callback: function (label, index, labels) {
													return label.toLocaleString(navigator.language, {
														minimumFractionDigits: 0,
													});
												},
											},
											gridLines: {
												display: false,
												drawBorder: false
											}
										},
										{
											id: "state4-y-axis",
											type: "linear",
											ticks: {
												beginAtZero: true,
												stepSize: 1,
												display: false,
												callback: function (label, index, labels) {
													return label.toLocaleString(navigator.language, {
														minimumFractionDigits: 0,
													});
												},
											},
											gridLines: {
												display: false,
												drawBorder: false
											}
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
								}
							}}
						/>
					): <CardSpinner/>
				}
			</Fragment>
		);
	}
}
const mapStateToProps = (state, ownProps) => {
    return {
        language: state.LoginReducer.curLanguage,   //語系
    }
}
BattStateLineChart.defaultProps = {
	data: {
		RecDate: [],
		Status1: [],
		Status2: [],
		Status3: [],
		Status4: [],
		Label:[],
		LabelE:[],
		LabelJ:[],
	},
}
export default connect(mapStateToProps)(BattStateLineChart);
