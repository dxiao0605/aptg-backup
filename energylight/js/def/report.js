
var location_data;

var locations = [];
var floor = [[]];
var office = [[[]]];

var device = [];
var splitstr = "&&"
var sel_id;	

var look_type=''
var chartname=''	
var charttype=''	
var ExportData=[]	
	

	$(document).ready(function () {

		
		
		
		ShowDate();
		ShowMonth()
		
		 //CallLocationInfoAPI();
		 
		 
		// LoadCommonPage();
		//ShowLoginInfo();
		//CallLocationInfo();

		//ShowWin();

		//ShowConfig()
		
		
		/*
		setInterval(function () {
			
			if(!$('#setdevModal').hasClass('show')){
				
				onShowSelect(sel_id)
			}
				
		
		
		}, 20 * 1000);
	
	
		*/

	});

//some code



function exportCSV(){
	
	
	
	
	
	
	
	
	
	
	function ConvertToCSV(objArray) {
            var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
            var str = '';

            for (var i = 0; i < array.length; i++) {
                var line = '';
                for (var index in array[i]) {
                    if (line != '') line += ','

                    line += array[i][index];
                }

                str += line + '\r\n';
            }

            return str;
        }

		
		
		function downloadCSV(csvStr) {

    var hiddenElement = document.createElement('a');
    hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csvStr);
    hiddenElement.target = '_blank';
    hiddenElement.download = 'output.csv';
    hiddenElement.click();
}
	
		var items = [
            { name: "Item 1", color: "Green", size: "X-Large" },
            { name: "Item 2", color: "Green", size: "X-Large" },
            { name: "Item 3", color: "Green", size: "X-Large" }];
	
		
		ExportData.splice(0, 0, {label:"time", y: "value"});
	
		console.log(ExportData)
		//var jsonObject = JSON.stringify(items);
		//downloadCSV(ConvertToCSV(jsonObject));
		
		var jsonObject = JSON.stringify(ExportData);
		downloadCSV(ConvertToCSV(jsonObject));
		//alert('export Data')
	
	
	
	
	
	
}


function ShowDate() {
	
	
	$('#dailyDay').datepicker({
	      format: 'yyyy-mm-dd',
		  autoclose: true,
	   // startDate: "today",
	    clearBtn: true,
	    todayHighlight: true,
		  language: 'zh-TW'
	    });
	
}


function ShowMonth() {
	
	
	$('#MonthlyDay').datepicker({
	      format: "yyyy-mm",
	      viewMode: "months", 
	      clearBtn: true,
	      autoclose: true,
	      minViewMode: "months",   
		  language: 'zh-TW'
	    });
	
}


function ShowOther()
{

	//ShowHeaderName();
	//$('#energy').collapse('show');
}



function aftermenu(){
	
	 ShowHeaderName();
	 //CallDeviceListAPI('electric')
	 CallgetDevices('electric')
	 //Showtable();
	 //CallLocationInfoAPI();
}


function CallgetDevicesOK(device_data)
{
	
	console.log(device_data)
	
	ShowSelectMenu(device_data)
	
	//showDeviceTable(device_data)
	
}



function onMonthlySelect(){
	
	chartname='monthlychart';
	look_type ='KWH'
		charttype='column'
		
		
		var sel_devid = $('#rp_monthly_device').val();
	var sel_day = $('#MonthlyDay').val();
	
	
	if(sel_day=='')
		alert('請選擇日期')
	
	//alert(sel_devid+','+sel_day);

	if(sel_devid!='x' && sel_day!='')	
		CallgetEnergyReport(sel_devid,'03',sel_day,'')	
		
	
}



function onDailySelect(){
	
	chartname='dailychart';
	look_type ='KWH'
	charttype='column'
	
	var sel_devid = $('#rp_daily_device').val();
	var sel_day = $('#dailyDay').val();
	
	
	if(sel_day=='')
		alert('請選擇日期')
	
	//alert(sel_devid+','+sel_day);

	if(sel_devid!='x' && sel_day!='')	
		CallgetEnergyReport(sel_devid,'02',sel_day,'')
	
}

function onShowSelect(SelId){
	
	
	chartname='chartNow';
	charttype='line'
	
	sel_id = SelId
	var this_id = "#" +sel_id
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();

	
	var sel_devid = $('#rp_sel_device').val();
	var sel_type = $('#rp_sel_type').val();
	
	look_type = $('#rp_sel_type').find("option:selected").text()
	
	
	if(sel_devid!='x' && sel_type!='x')
	CallgetEnergyReport(sel_devid,'01','',sel_type)
	
}

function CallgetEnergyReportOk(EnergyReportData){
	
	
	//for(var ii of EnergyReportData.report)
	//	ExportData.push(ii)
		
		
	ExportData = []
	
	if(charttype =='line')
	  LineChartData(EnergyReportData);
	
	if(charttype =='column')
	  ColChartData(EnergyReportData);	
	

	
}


function LineChartData(EnergyReportData){
	
	
	
console.log(EnergyReportData)
	
	var reportlist = EnergyReportData.report
	var dpsarray=[]
		
	for (var i = 0; i < reportlist.length; i++) {

		//var obj = reportlist[i];
		 
		var objdata = reportlist
		 
		 
		
			 
			 objdata[i]['x'] = new Date(objdata[i]['time']);
			 
			 delete objdata[i]['time'];
			 
             objdata[i]['y'] = parseFloat(objdata[i]['value']);
			 
			 delete objdata[i]['value'];
			 
	}
		 
		 var objtmp ={} 
		
		 objtmp["name"] = 'test';
		 
		 objtmp["type"] = charttype;
		 
		// objtmp["showInLegend"] = true;
		 
		 objtmp["lineColor"] = 'red' ;
		 //objtmp["circle"] = 'circle' ;
		// objtmp["markerSize"] = 2 ;
		 objtmp["lineThickness"] = 3 ;
		 		 
		 objtmp['dataPoints']=objdata
		 
		
		 
		 
		 lineColor: "red",
		dpsarray.push(objtmp)
		 //console.log('EnergyPowerOk Data:'+objdata)
		 
	//ShowLineChart2(dpsarray)
	
	ShowLineChart(dpsarray)
	//console.log(EnergyReportData)
	
	
	
}


function ColChartData(EnergyReportData){
	
	
	
	console.log(EnergyReportData)
		
		var reportlist = EnergyReportData.report
		var dpsarray=[]
			
		for (var i = 0; i < reportlist.length; i++) {

			//var obj = reportlist[i];
			 
			var objdata = reportlist
			 
			 
			    if(chartname=='monthlychart')
			    	 objdata[i]['label'] = new Date(objdata[i]['time']).getDate();
			    else
				 objdata[i]['label'] = new Date(objdata[i]['time']).getHours();
			
			
			     
				 
				 delete objdata[i]['time'];
				 
	             objdata[i]['y'] = parseFloat(objdata[i]['value']);
				 
				 delete objdata[i]['value'];
				 
		}
			 
			 var objtmp ={} 
			
			// objtmp["name"] = 'test';
			 
			 objtmp["type"] = charttype;
			 
			 objtmp["showInLegend"] = true;
			 
			 legendText: "日期",
			 
			// objtmp["lineColor"] = 'red' ;
			 //objtmp["circle"] = 'circle' ;
			// objtmp["markerSize"] = 2 ;
			 objtmp["lineThickness"] = 3 ;
			 		 
			 objtmp['dataPoints']=objdata
			 
			 //ExportData=objdata
			 
			 
			 ExportData = jQuery.extend(true, [], objdata);
			 
			 
			 lineColor: "red",
			dpsarray.push(objtmp)
			 //console.log('EnergyPowerOk Data:'+objdata)
			 
		//ShowLineChart2(dpsarray)
		
		ShowBarChart(dpsarray)
		//console.log(EnergyReportData)
		
		
		
	}


function ShowBarChart(dps){
	
	
	function toggleDataSeries(e){
		if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
			e.dataSeries.visible = false;
		}
		else{
			e.dataSeries.visible = true;
		}
		chart.render();
	}

	var chartid = chartname;

	
	
	var chart = new CanvasJS.Chart(chartid, {
		animationEnabled: true,
		theme: "light2", // "light1", "light2", "dark1", "dark2"

		axisX : {
			title : "時間",
			interval : 1
		},
		axisY : {
			title : look_type
			//includeZero: true
		},	
		toolTip: {
			shared: true
		},legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		data :dps
	});

	
	
	
	chart.render();
	
	
}


function ShowLineChart(dps){
	
	
	function toggleDataSeries(e){
		if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
			e.dataSeries.visible = false;
		}
		else{
			e.dataSeries.visible = true;
		}
		chart.render();
	}

	var chartid = chartname;

	
	
	var chart = new CanvasJS.Chart(chartid, {
		zoomEnabled : true,

		axisX : {
			title : "時間",
			//interval : 2
		},
		axisY : {
			title : look_type
		},legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		data :dps
	});

	
	
	
	chart.render();
	
	
}

function ShowSelectMenu(device_data){
	
	
	 $("#rp_sel_device").append($("<option></option>").attr("value", 'x').text('請選擇裝置'));
	 
	 $("#rp_daily_device").append($("<option></option>").attr("value", 'x').text('請選擇裝置'));
	 $("#rp_monthly_device").append($("<option></option>").attr("value", 'x').text('請選擇裝置'));
	
	for (var i = 0; i < device_data.length; i++) {
		
		$("#rp_sel_device").append($("<option></option>").attr("value", device_data[i].device_id).text(device_data[i].alias));
		
		$("#rp_daily_device").append($("<option></option>").attr("value", device_data[i].device_id).text(device_data[i].alias));
		
		$("#rp_monthly_device").append($("<option></option>").attr("value", device_data[i].device_id).text(device_data[i].alias));
		
		console.log(device_data[i].device_id +','+device_data[i].alias)
		
	}
	
	    $("#rp_sel_type").append($("<option></option>").attr("value", 'x').text('請選擇查看類別'));
		$("#rp_sel_type").append($("<option></option>").attr("value", 'v').text('電壓'));
		$("#rp_sel_type").append($("<option></option>").attr("value", 'a').text('電流'));
		$("#rp_sel_type").append($("<option></option>").attr("value", 'kw').text('功率'));
		$("#rp_sel_type").append($("<option></option>").attr("value", 'kwh').text('累積耗能'));
		
		
	
}



function ShowDeviceTable(light_data) {

	var ItochSelectHtml = '';

	ItochSelectHtml += "<select>";
	ItochSelectHtml += " <option value ='1'>itouchA</option>"
	ItochSelectHtml += " <option value ='2'>itouchB</option>"
	ItochSelectHtml += " <option value='3'>itouchC</option>"
	ItochSelectHtml += " <option value='4'>itouchD</option>"
	ItochSelectHtml += "</select>"
	ItochSelectHtml += "<button type='button' name='bindAccount' class='btn btn-danger ml-3' id='apply_acc'>裝置綁定</button>"

	var t = $('#device_tables').DataTable({
		searching : false,
		paging : false,
		info : false,
		data : light_data,
		columns : [ {
			data : null
		}, {
			data : 'device_alias'
		}, {
			data : 'device_key'
		} ],
		columnDefs : [ {
			"searchable" : false,
			"orderable" : false,
			"title" : "序號",
			"targets" : 0,
			"width" : "5%",
			"className" : 'dt-center'
		}, {
			"title" : "裝置名稱",
			"targets" : 1,
			"width" : "10%",
			"className" : 'dt-center'
		}, {
			"title" : "裝置代碼",
			"targets" : 2,
			"width" : "10%",
			"className" : 'dt-center'
		}, {
			"title" : "Itouch綁定",
			"targets" : 3,
			"width" : "10%",
			"data" : null,
			"defaultContent" : ItochSelectHtml,
			"className" : 'dt-center'
		}

		],

		language : {
			"sProcessing" : "處理中...",
			"sLengthMenu" : "顯示 _MENU_ 項結果",
			"sZeroRecords" : "沒有匹配結果",
			"sInfo" : "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
			"sInfoEmpty" : "顯示第 0 至 0 項結果，共 0 項",
			"sInfoFiltered" : "(由 _MAX_ 項結果過濾)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中數據為空",
			"sLoadingRecords" : "載入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首頁",
				"sPrevious" : "上頁",
				"sNext" : "下頁",
				"sLast" : "末頁"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}

	});

	t.on('order.dt search.dt', function() {
		t.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();

}








