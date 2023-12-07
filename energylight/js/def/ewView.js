$(document).ready(function() {

	// CallLocationInfoAPI();

	// MainPageClickEvent()

});

// some code

var device_list;


function aftermenu() {

	ShowHeaderName();
	//ShowLineChart();
	ShowpPieChart();
	CallgetEnergyConsume();
	CallgetEnergyRank();
	//CallgetEnergyPower(device_list);
	
	
	
	//setInterval(CallgetEnergyRank(),5000);
	
	
}

function CallgetEnergyPowerOk(EnergyPowerData){
	
	console.log(EnergyPowerData)
	
	var powerlist = EnergyPowerData.power
	var dpsarray=[]
		
	for (var i = 0; i < powerlist.length; i++) {

		var obj = powerlist[i];
		 console.log('EnergyPowerOk:'+obj.device_name)
		 
		 
		 var objdata =  obj.data
		 
		 
		 for (var j = 0; j < objdata.length; j++) {
			 
			 objdata[j]['x'] = new Date(objdata[j]['time']);
			 
			 delete objdata[j]['time'];
			 
             objdata[j]['y'] = parseFloat(objdata[j]['kw']);
			 
			 delete objdata[j]['kw'];
			 
		 }
		 
		 var objtmp ={} 
		
		 objtmp["name"] = obj.device_name;
		 
		 objtmp["type"] = 'line';
		 
		 objtmp["showInLegend"] = true;
		 
		 objtmp['dataPoints']=objdata
		 
		 dpsarray.push(objtmp)
		 //console.log('EnergyPowerOk Data:'+objdata)
		 
		 
	}
	
	
	ShowLineChart2(dpsarray)
	
}


function CallgetEnergyRankOk(EnergyRankData){
	
	//console.log(EnergyRankData)
	
	var arrayranks = EnergyRankData.ranks
	
	$(".e_rank tbody").empty();
	
	for (var i = 0; i < arrayranks.length; i++) {
		
    		var obj = arrayranks[i];
    		 console.log(obj.device_name+','+obj.kw+','+obj.kwh)

    		 html  ='<tr>'
    		 html +='<th scope="row">'+i+'</th>'
    		 html +='<td>'+obj.device_name+'</td>'
    		 html +='<td>'+obj.kw+'</td>'	 
    		 html +='<td>'+obj.kwh+'</td>'
    		 html +='</tr>'
			 
    		$(".e_rank tbody").append(html)
    		  
    	}
	
}

function CallgetEnergyConsumeOk(EnergyConsumeData){
	
	var last_total_power = EnergyConsumeData.last_total_power
	var total_power = EnergyConsumeData.total_power
	
	 $("#last_total_power").text(last_total_power+' KWH');
	
	 $("#total_power").text(total_power+' KWH');
	
	
	
	console.log(last_total_power+','+total_power)
	
	
	var itemData = EnergyConsumeData.device_power
	
	$("#itempower").empty();
	
	$("#lastitempower").empty();
	
	device_list ='';
	
	
	var piechartdata =[]
	
	
	for (var i = 0; i < EnergyConsumeData.device_power.length; i++) {
	
    		var obj = itemData[i];
    		 var pieobj ={}
    		 console.log(obj.device_id+','+obj.name+','+obj.power+','+obj.last_power)
 
    		 
    		 device_list +=obj.device_id+','
    		 
              var html='<li class="list-group-item border-0">'+obj.name +': <span class="Wnum">'+obj.power+'</span></li>'
    			
    			
    			$("#itempower").append(html);
    		 
    		 
    		 var html='<li class="list-group-item border-0">'+obj.name +': <span class="Wnum">'+obj.last_power+'</span></li>'
 			
 			
 			$("#lastitempower").append(html);
    		 
    		  pieobj['y'] =obj.percentage
    		  pieobj['label'] =obj.name
    		  
    		  piechartdata.push(pieobj)
    		 
    		 
    	}
	
	
	
	
	device_list =device_list.substring(0, device_list.length - 1)
	
	CallgetEnergyPower(device_list);
	
	ShowpPieChart(piechartdata)
	
}

function ShowpPieChart(dps) {
	var chart = new CanvasJS.Chart("PieContainer", {
		exportEnabled: true,
		animationEnabled: true,
		legend:{
			cursor: "pointer",
			itemclick: explodePie
		},
		data: [{
			type: "pie",
		//	showInLegend: true,
			//toolTipContent: "{name}: <strong>{y}%</strong>",
			indexLabel: "{label} - {y}%",
			dataPoints: dps
		}]
	});
	chart.render();
	}

function explodePie (e) {
		if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
			e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
		} else {
			e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
		}
		e.chart.render();
	
	
	
}



function ShowLineChart() {

	function getRandomIntInclusive(min, max) {
		min = Math.ceil(min);
		max = Math.floor(max);
		return Math.floor(Math.random() * (max - min + 1)) + min;
	}
	
	function toggleDataSeries(e){
		if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
			e.dataSeries.visible = false;
		}
		else{
			e.dataSeries.visible = true;
		}
		chart.render();
	}

	var chartid ='chartContainer'

	var dps = []; // dataPoints.

	var dps1 = [];
	var dps2 = [];
	var dps3 = [];
	var dps4 = [];
	
	var chart = new CanvasJS.Chart(chartid, {
		zoomEnabled : true,

		axisX : {
			//title : "",
			interval : 2
		},
		axisY : {
			title : "KWH"
		},legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		data : [ {
			type : "line",
			name: "總電表",
			showInLegend: true,
			dataPoints : dps
		},{
			type : "line",
			name: "空調",
			showInLegend: true,
			dataPoints : dps1
		},{
			type : "line",
			name: "照明",
			showInLegend: true,
			dataPoints : dps2
		},{
			type : "line",
			name: "冷凍",
			showInLegend: true,
			dataPoints : dps3
		},{
			type : "line",
			name: "其它",
			showInLegend: true,
			dataPoints : dps4
		} ]
	});

	
	var xVal = "";
	var yVal = 15;
	var day  = 0
	
	function appendData(){
		
		
		
		    day++;
			xVal = day 
			//yVal = ;
			// console.log(xVal +"**"+yVal);
			dps.push({
				x : xVal,
				y : getRandomIntInclusive(80, 100)
			});

			dps1.push({
				x : xVal,
				y : getRandomIntInclusive(1, 10)
			});
			
			dps2.push({
				x : xVal,
				y : getRandomIntInclusive(30, 50)
			});
			
			dps3.push({
				x : xVal,
				y : getRandomIntInclusive(60, 70)
			});
			
			
			dps4.push({
				x : xVal,
				y : getRandomIntInclusive(70, 80)
			});
			
			if (dps.length > 30) {
		    		dps.shift();
		    		dps1.shift();
		    		dps2.shift();
		    		dps3.shift();
		    		dps4.shift();
			}

			chart.render();

		
	}
	
	setInterval(appendData,1000);

}




function ShowLineChart2(dps) {

	
	
	function toggleDataSeries(e){
		if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
			e.dataSeries.visible = false;
		}
		else{
			e.dataSeries.visible = true;
		}
		chart.render();
	}

	var chartid ='chartContainer'

	
	
	var chart = new CanvasJS.Chart(chartid, {
		zoomEnabled : true,

		axisX : {
			title : "時間",
			//interval : 2
		},
		axisY : {
			title : "KWH"
		},legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		data :dps
	});

	
	
	
	chart.render();
	

}































