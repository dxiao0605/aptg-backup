import React, { Component, Fragment } from "react";
import { Pie } from "react-chartjs-2";
import { connect } from "react-redux";
import "chart.js";
import "chartjs-plugin-zoom";
import "chartjs-plugin-annotation";
import "chartjs-plugin-datalabels";



class BattStatePieChart extends Component {
    constructor(props){
        super(props)
        this.state = {
            data: {},
            label: [],
            colorList: [],
        }
    }

    componentDidMount() {
        const { data, language } = this.props;
        const colorArr = [];    //顏色清單
		
		// 資料是否存在
		if(data){
			this.setState({
				data: data
            })
            data.Status.map( (item) => {
                const normalText = ['Normal','正常','正常'];
                const alertText = ['Alert','警戒','アラート'];
                const offlineText = ['Offline','離線','オフライン'];
                const toReplaceText = ['To Replace','需更換','交換する必要'];

                if(normalText.includes(item)){ //正常
                    colorArr.push('rgba(32,140,38,1)')
                }
                else if(alertText.includes(item)){ //警戒
                    colorArr.push('rgba(255,190,23,1)')
                }
                else if(offlineText.includes(item)){ //離線
                    colorArr.push('rgba(47,197,185,1)')
                }
                else if(toReplaceText.includes(item)){ //需更換
                    colorArr.push('rgba(221,40,73,1)')
                }
                return this.setState({colorList:colorArr})
            })
			// 確認語系
			switch(language) {
				case 1:
					this.setState({label:this.props.data.Status})
					break;
				case 2:
					this.setState({label:this.props.data.Status})
					break;
				case 3:
					this.setState({label:this.props.data.Status})
					break;
				default:
					this.setState({label:this.props.data.Status})
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
    

    render(){
        const { data,label,colorList } = this.state;
        return (
            <Fragment>
                {
                    (this.props.data && data !== {} && this.state.data.Count) ? (
                        <Pie
                            height={80}
                            data={{
                                labels: label, //['正常','警戒','離線','需更換'],
                                datasets: [{
                                    data: this.state.data.Count,
                                    backgroundColor: colorList,
                                    borderWidth: 0,//border寬度      
                                }]
                            }}
                            
                            legend={{
                                position: 'bottom',
                                fullWidth: true,
                                labels: {
                                    boxWidth: 15,
                                    fontSize: 14
                                }
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
                                plugins: {
                                    datalabels: {
                                        clip: true,
                                        color: '#ffffff',
                                        font: {
                                            size: "14",
                                        },
                                    }
                                }
                            }}
                        />
                    ): ''
                }
            </Fragment>
        )
    }

}
const mapStateToProps = (state, ownProps) => {
    return {
        language: state.LoginReducer.curLanguage,   //語系
    }
}
BattStatePieChart.defaultProps = {
    data: {
        Status: [],
        Count:[],
    },
}
export default connect(mapStateToProps)(BattStatePieChart);
