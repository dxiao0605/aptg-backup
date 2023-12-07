
var location_data;



	$(document).ready(function () {

		CallDemandApi();
		

		setInterval(function () {
			
			location.reload();
		}, 60 * 1000);
		
	});

//some code

	
function processDemand(){
	
	CallLocationInfoAPI();
}	


function addAlarmStatus(contract,current) {
	
	var html =""
	if(contract >= current)
		{
		html += "<div class='alarm_text form-inline' >"
		html += "<div class='spinner-grow text-success form-group' ></div>"
		html +="<div class='h2 form-group'>用電正常</div>"
		html += "</div>"
		
		}
		
	else
	{
		html += "<div class='alarm_text form-group form-inline' >"
			html += "<div class='spinner-grow text-danger form-group' ></div>"
			html +="<div class='h2 form-group'>預測十五分鐘後超越契約容量</div>"
			html += "</div>"
		
	}
	

		

		$("#alarm-status").append(html);
	
}


function addHtmlChart(chartname,chartid) {


var html =""

html += "<div class='chart mt-3' >"
html +="<div id='"+chartid +"' style='height: 600px; width: 1400px;'></div>"
html += "</div>"

$("#charts").append(html);

var dps = [];   //dataPoints. 
var dps2 = [];	

      var chart = new CanvasJS.Chart(chartid,{
    	animationEnabled: true,
	   zoomEnabled: true,
	   lineThickness: 50,
      	title :{
      		text: chartname
      	},
      	axisX: {						
      		//title: "日期",
			interval: 10,
			crosshair: {
				enabled: true,
				snapToDataPoint: true
			},
			labelAngle: 75
      	},
      	axisY: {						
      		title: "耗能(瓦數)",
      		crosshair: {
    			enabled: true
    		}
      	},
      	data: [{
      		type: "line",
      		showInLegend: true,
      		markerType: "square",
      		markerSize: 6,
      		name: "每日電量",
      		dataPoints : dps
      	},
      	
      	{
      		type: "line",
      		showInLegend: true,
      		//lineDashType: "dash",
      		name: "契約電量:"+DemandInfo.contract_capacity,
      		dataPoints : dps2
      		
      	}
      	
      	
      	]
      });
  
	 
      var x1,y1,y2
	  var obj
      
      for(var i = 0; i < DemandInfo.demands.length; i++) {
    	    
    	
    	 // if(i <DemandInfo.demands.length){
    	 // if(i < 90){
    	     obj = DemandInfo.demands[i];

    	   // console.log(i+"--"+obj.time+"--"+obj.demand);
    	    
    	     x1 = obj.time
    	     y1 = parseFloat(obj.demand)
    	     y2= parseFloat(DemandInfo.contract_capacity)
    	    
    	    if (i < DemandInfo.demands.length-1){
    	    dps.push({label: x1,y:y1,lineColor:"blue"});
    	 	dps2.push({label: x1,y:y2 });
    	    }else{
    	    	
    	    	dps.push({label: x1,y:y1,lineColor:"red", lineDashType:"dash"});
        	 	dps2.push({label: x1,y:y2 });
    	    }
    	 	
    	 // }else
    	 		
      }
      
      var yy=parseFloat(DemandInfo.predict_demand)
      var xx=DemandInfo.time
      
      dps.push({label:xx ,y:yy,lineColor:"red", markerType: "square",  markerColor: "red",markerSize: 10});
		 	 	
	  dps2.push({label: xx,y:y2 });
      
      chart.render();	
      
      
      addAlarmStatus(parseFloat(DemandInfo.contract_capacity),parseFloat(DemandInfo.predict_demand));
	  
      
    //  addAlarmStatus(2000,parseFloat(DemandInfo.predict_demand));
	 /* 
	  var xVal = "";
      var yVal = 15;	
      var updateInterval = 1000;
	  var i=0;
    //  var updateChart = function () {
	  for (ii = 0; ii < 30; ii++) {
      	i++;
      	xVal = i +'日'
      	yVal = getRandomIntInclusive(1,100);
		//console.log(xVal +"**"+yVal);
      	dps.push({label: xVal,y: yVal});
      	dps2.push({label: xVal,y: 80});
      	
      	xVal++;
      	if (dps.length >  30 )
      	{
      		dps.shift();
      		dps2.shift();
      	}

      	chart.render();		
	  
	  }
	  */
	
}



function CallLocationInfoAPIOK(location_data) {

	
	LocInfo =location_data;
						
			var L1_id_set = jsonPath(LocInfo, "$.location[*].location_id")
						
			L1_id_set.forEach(function (itemid, index) {
				
				
			  var location_name = 	FindLocationName(itemid);
			  
			  itemid = 'lne'+itemid
	          //console.log(itemid+','+location_name)
	          
	        
	          		if(index < 1)
					addHtmlChart(location_name,itemid);
			
	        });
			
		
}





