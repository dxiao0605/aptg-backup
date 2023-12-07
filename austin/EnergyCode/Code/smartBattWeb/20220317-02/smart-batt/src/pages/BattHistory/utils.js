// 圖表時區(X軸)畫面顯示間格
export const checkUnitStepSize = ({item,type}) => {
    let time = 3600;
    switch (type) {
        case "0":   // 24Hr
            break;
        case "1":   // customer
            const start = `${item.Start} ${item.StartHH}:${item.StartMM}:00`;
            const end = `${item.End} ${item.EndHH}:${item.EndMM}:00`;
            const gap = new Date(end) - new Date(start);
            time = gap / 24000 ;
            break;
        case "2":
            break;
        case "3":   // 1 Month
            time = 108000;
            break;
        case "5":   // 7 Day
            time = 25200;
            break;
        default:
            break;
    }
    return time;
}
// dynamicColors
const dynamicColors = () => {
    let r = Math.floor(Math.random() * 255);
    let g = Math.floor(Math.random() * 255);
    let b = Math.floor(Math.random() * 255);
    return "rgb(" + r + "," + g + "," + b + ")";
}
// IR,Vol,Temperature data
export const getChartDatas = (IR,Vol,Temperature,language) => {
    const IRChart = [];
    const VolChart = [];
    const TemperatureChart = [];
    // 內阻
    IR.map( (item,idx) => {
        return (
            ()=>{
                let color = dynamicColors();
                IRChart.push({
                    label:`CH${idx+1}`,
                    type: 'line',
                    data: item,
                    backgroundColor: color,
                    borderColor: color,
                    fill: "none",
                    lineTension: 0,			//線不圓滑
                    order: idx,
                })
            }
        )()
    })
    // 電壓
    Vol.map( (item,idx) => {
        return (
            ()=>{
                let color = dynamicColors();
                VolChart.push({
                    label:`CH${idx+1}`,
                    type: 'line',
                    data: item,
                    backgroundColor: color,
                    borderColor: color,
                    fill: "none",
                    lineTension: 0,			//線不圓滑
                    order: idx,
                })
            }
        )()
    })
    // 溫度
    let color = dynamicColors();
    TemperatureChart.push({
        label:`${
            (
                ()=>{
                    if(language === 1 ){
                        return '溫度'
                    }
                    else if (language === 2) {
                        return 'Temperature'
                    }
                    else{
                        return '温度'
                    }
                }
            )()
        }`,
        type: 'line',
        data: Temperature,
        backgroundColor: color,
        borderColor: color,
        fill: "none",
        lineTension: 0,			//線不圓滑
    })

    return {
        IRChart,
        VolChart,
        TemperatureChart,
    }
}