// import moment from "moment";
import moment from 'moment-timezone';

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
// 隨機產生線顏色
const dynamicColors = () => {
    let r = Math.floor(Math.random() * 255);
    let g = Math.floor(Math.random() * 255);
    let b = Math.floor(Math.random() * 255);
    return "rgb(" + r + "," + g + "," + b + ")";
}
// 取得開始時間
const getStartDate = (date,timezone) => {
    let startDate = moment(new Date().getTime() - (24 * 60 * 60 * 1000)).tz(timezone).format('YYYY-MM-DD HH:mm');
    switch(date.Radio) {
        case '0':   //過去24小時
            startDate = moment(new Date().getTime() - (24 * 60 * 60 * 1000)).tz(timezone).format('YYYY-MM-DD HH:mm');
            break;
        case '5':   //過去七天
            startDate = moment(new Date().getTime() - (7 * 24 * 60 * 60 * 1000)).tz(timezone).format('YYYY-MM-DD HH:mm');
            break;
        case '3':   //過去一個月
            startDate = moment(new Date().getTime()- (30 * 24 * 60 * 60 * 1000)).tz(timezone).format('YYYY-MM-DD HH:mm');
            break;
        case '1':   //自訂義
            startDate = `${date.Start} ${date.StartHH}:${date.StartMM}`;
            break;
        default:
            break;
    }
    return startDate;
}
// 取得結束日期(自訂義時取直，其餘皆以當日)
const getEndDate = (date,timezone) => {
    let endDate = moment(new Date()).tz(timezone).format('YYYY-MM-DD HH:mm');
    switch (date.Radio) {
        case '1':   //自訂義
            endDate = `${date.End} ${date.EndHH}:${date.EndMM}`;
            break;
        default:
            break;
    }
    return endDate;
}
// 取得消失的時間清單
const getDisappearTime = ({startDate,point,gap}) => {
    const disconnectedList = [];
    let ms = moment(new Date(startDate)).format('YYYY-MM-DD HH:mm:ss');
    Array.apply(null,Array(point-1)).map( (element,index) => {
        return disconnectedList.push(moment(new Date(new Date(ms).getTime() + gap*(index+1))).format('YYYY-MM-DD HH:mm:ss'))
    })
    return disconnectedList;
}
// 取得中間斷點清單
const getMiddleDisappearList = ({data,gap}) => {
    const list = data.map((element,index) => {
        if( new Date(data[index+1]) - new Date(element) > gap*2){
            const jetlag = Math.ceil(new Date(data[index+1]) - new Date(element));//前後時間差(無條件進位)
            return {
                startId: index, //原始index
                text: element,  // 字串格式日期
                jetlag: jetlag ,    //前後時間差(無條件進位)
                point: Math.floor(jetlag / gap), //相差間隔(無條件捨去)
                startDate: new Date(element),   //開始時間
                endDate:new Date(data[index+1])  //結束時間
            }
        }else{return false}
    }).filter( (filterItem) => filterItem !== false)

    return list;
}



// 取得新的X軸(時間)陣列
const getNewRecTimeLabel = ({data,item,gap,prevPoint}) => {
    let newList = [];
    const slicePoint = item.startId + 1 + prevPoint;
    const frontTimeArray = (data.slice(0, slicePoint));
    const secondTimeArray = (data.slice(slicePoint, data.length));
    const middleTimeArray = getDisappearTime({startDate:item.startDate,point:item.point,gap});

    newList = frontTimeArray.concat(middleTimeArray,secondTimeArray);

    return newList;
}
// 取得最前面新的Y軸(內阻/電壓)陣列
const getfirstNewValueList = ({data,nullValue}) => {
    let temp = [];
    data.map(element => {
        return temp.push(nullValue.concat(element))
    })
    return temp;
}
// 取得中間新的Y軸(內組/電壓)陣列
const getMiddleNewValueList = ({data,item,prevPoint}) => {
    let newList = [];
    let temp = [];
    const slicePoint = item.startId + 1 + prevPoint;
    // 取得含中斷時間y軸(空值)IR Vol 數值陣列
    const nullArray = new Array(item.point-1).fill(null);
    data.map((element) => {
        const firstArray = element.slice(0,slicePoint);
        const secondArray = element.slice(slicePoint,element.length);
        return temp.push(firstArray.concat(nullArray,secondArray))
    })
    newList = temp;
    return newList;
}
// 取得新的Y軸(溫度)陣列
const getNewTempList = ({data,item,prevPoint}) => {
    let newList = [];
    const slicePoint = item.startId + 1 + prevPoint;
    const frontArray = (data.slice(0, slicePoint));
    const secondArray = (data.slice(slicePoint, data.length));
    const middleArray = new Array(item.point-1).fill(null);    // Temperature
    newList = frontArray.concat(middleArray,secondArray);
    return newList;

}


// IR,Vol,Temperature data
export const getChartDatas = (IR,Vol,Temperature,RecTime,filterDate,language,timezone) => {
    const startDate = getStartDate(filterDate,timezone);
    const endDate = getEndDate(filterDate,timezone);
    // console.log('endDate',timezone,endDate)
    // 第一筆資料 - (篩選)起始日期 -- 前後時間差(無條件進位)
    const startDateJetLag = Math.ceil(new Date(RecTime[0]) - new Date(startDate));
    // (篩選)結束日期 - 最後一筆資料 -- 前後時間差(無條件進位) timzone
    const endDateJetLag = Math.ceil((new Date(endDate) )- new Date(RecTime[RecTime.length - 1]));
    // 暫存數值陣列
    let newIR = [];
    let newVol = [];
    let newTemperature = [];
    // 圖表資料格式
    let RecTimeLabel = [];
    const IRChart = [];
    const VolChart = [];
    const TemperatureChart = [];
    // gap = 日期陣列(第二筆-第一筆) -- 數值間距(無條件捨去)
    const gap = Math.floor(new Date(RecTime[1]) - new Date(RecTime[0]));
    // console.log('%cRecTime','background:yellow',RecTime)
    // 判斷第一筆資料與篩選的起始日期不一致時
    // console.log("%c startDateJetLag",'background:yellow',startDateJetLag , gap*2)
    if(startDateJetLag > gap*2) {
        // console.log('%c起始有斷點','background:tomato')
        const point = Math.floor(startDateJetLag/gap) -1;
        const disappearTimeList = getDisappearTime({startDate,point,gap});
        const nullValue = new Array(point-1).fill(null);
        newIR = getfirstNewValueList({data:IR,nullValue});
        newVol = getfirstNewValueList({data:Vol,nullValue});
        newTemperature = nullValue.concat(Temperature);
        RecTimeLabel = [startDate].concat(disappearTimeList,RecTime);

        // 取得中間的斷點清單
        const middleDisappearList = getMiddleDisappearList({data:RecTimeLabel,gap});
        // console.log('%c起始有斷點，且中間有中斷點:middleDisappearList','background:tomato',middleDisappearList)

        if(middleDisappearList.length > 0) {
            let totalPrevPoint = 0;
            // 起始有斷點，且中間有中斷點
            middleDisappearList.map( (item,idx) => {
                // const prevPoint = idx > 0 ? (middleDisappearList[idx-1].point -1) : 0;
                const prevPoint = idx > 0 ? (middleDisappearList[idx-1].point - 1) : 0;
                totalPrevPoint += prevPoint;
                // console.log('prevPoint idx',idx,'prevPoint',prevPoint,'totalPrevPoint',totalPrevPoint)

                RecTimeLabel = getNewRecTimeLabel({data:RecTimeLabel,item,gap,prevPoint:totalPrevPoint});
                newIR = getMiddleNewValueList({data:newIR,item,prevPoint:totalPrevPoint});
                newVol = getMiddleNewValueList({data:newVol,item,prevPoint:totalPrevPoint});
                newTemperature = getNewTempList({data:newTemperature,item,prevPoint:totalPrevPoint});
                // console.log('%c起始有斷點，且中間有中斷點','background:tomato',RecTimeLabel)
                // console.log('%c起始沒有斷點，中間有斷點','background:yellowgreen',newIR)
                // console.log('%c起始沒有斷點，中間有斷點','background:yellowgreen',newVol)
                // console.log('%c newTemperature','background:tomato',newTemperature)
            })
        }


    }else {
        // console.log('%c起始沒有斷點','background:yellowgreen')
        // 起始沒有斷點
        RecTimeLabel = [...RecTime];
        newIR = [...IR];
        newVol = [...Vol];
        newTemperature = [...Temperature];

        // 取得中間的斷點清單
        let totalPrevPoint = 0;
        const middleDisappearList = getMiddleDisappearList({data:RecTimeLabel,gap});
        // console.log('%c起始沒有斷點，中間有斷點','background:yellowgreen',middleDisappearList)
        if(middleDisappearList.length > 0) {
            // 起始沒有斷點，中間有斷點
            middleDisappearList.map( (item,idx) => {
                // const prevPoint = idx > 0 ? (middleDisappearList[idx-1].point -1) : 0;
                const prevPoint = idx > 0 ? (middleDisappearList[idx-1].point -1) : 0;
                totalPrevPoint += prevPoint ;
                // console.log('prevPoint idx',idx,'prevPoint',prevPoint,'totalPrevPoint',totalPrevPoint);
                // console.log('prevPoint idx',idx,'prevPoint',prevPoint)
                RecTimeLabel = getNewRecTimeLabel({data:RecTimeLabel,item,gap,prevPoint:totalPrevPoint});
                newIR = getMiddleNewValueList({data:newIR,item,prevPoint:totalPrevPoint});
                newVol = getMiddleNewValueList({data:newVol,item,prevPoint:totalPrevPoint});
                newTemperature = getNewTempList({data:newTemperature,item,prevPoint:totalPrevPoint});
                // console.log('%c起始沒有斷點，中間有斷點','background:yellowgreen',RecTimeLabel)
                // console.log('%c起始沒有斷點，中間有斷點','background:yellowgreen',newIR)
                // console.log('%c起始沒有斷點，中間有斷點','background:yellowgreen',newVol)
                // console.log('%c newTemperature','background:yellowgreen',newTemperature)
            })
        }
    }



    if(endDateJetLag > gap*2) {
        // 最後有斷點(篩選的結束日-資料的最後一筆不一致時)
        // console.log('%c結束有斷點','background:pink')
        const lostPoint = Math.ceil(endDateJetLag / gap); //相差間隔
        const lostDate = getDisappearTime({startDate: RecTimeLabel[RecTimeLabel.length-1],point:lostPoint,gap});
        RecTimeLabel = RecTimeLabel.concat(lostDate)
    }else if (endDateJetLag < 0) {
        // 當結束日期小於資料最後一筆時
        // console.log('%c當結束日期小於資料最後一筆時','background:red')
    }

    // console.log('%cRecTimeLabel','background:yellow',RecTimeLabel)


    // 內阻
    newIR.map( (item,idx) => {
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
    newVol.map( (item,idx) => {
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
        data: newTemperature,
        backgroundColor: color,
        borderColor: color,
        fill: "none",
        lineTension: 0,			//線不圓滑
    })

    return {
        IRChart,
        VolChart,
        TemperatureChart,
        RecTimeLabel,
    }
}