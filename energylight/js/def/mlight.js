
var location_data;

var locations = [];
var floor = [[]];
var office = [[[]]];

var device = [];
var splitstr = "&&"
var sel_id;	
	

	$(document).ready(function () {

		
		
		
		
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


function ShowConfig() {
	
	
}


function addHtmlChart(chartname,idx) {


	function getRandomIntInclusive(min, max) {
	  min = Math.ceil(min);
	  max = Math.floor(max);
	  return Math.floor(Math.random() * (max - min + 1)) + min;
	}


	var chartid = "chartid" +idx

	var html =""

	html += "<div class='chart' >"
	html +="<div id='"+chartid +"' style='height: 300px; width: 1400px;'></div>"
	html += "</div>"

	$("#charts").append(html);

	//var dps = [{x: 1, y: 10}, {x: 2, y: 13}, {x: 3, y: 18}, {x: 4, y: 20}, {x: 5, y: 17},{x: 6, y: 10}, {x: 7, y: 13}, {x: 8, y: 18}, {x: 9, y: 20}, {x: 10, y: 17}];   //dataPoints. 

	var dps = [];   //dataPoints. 
		

	      var chart = new CanvasJS.Chart(chartid,{
		   zoomEnabled: true,
	      	title :{
	      		text: chartname
	      	},
	      	axisX: {						
	      		title: "日期",
				 interval: 2
	      	},
	      	axisY: {						
	      		title: "耗能(瓦數)"
	      	},
	      	data: [{
	      		type: "line",
	      		dataPoints : dps
	      	}]
	      });

	      //chart.render();
		  
		  
		  
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
	      	
	      	xVal++;
	      	if (dps.length >  30 )
	      	{
	      		dps.shift();				
	      	}

	      	chart.render();		
		  
		  }
		// update chart after specified time. 

	};


function FindLocationData(id) {

	var data =jsonPath(LocInfo, "$..[?(@.location_id == '" + id + "')].data")[0]
	
	var itouch ="0"
	var light="0"
	var ret =[]
	
	for ( var i = 0; i < data.length; i++) {
		
		var obj = data[i];
		//console.log(obj.name+','+obj.data)
		
		if (obj.device_type == 'itouch')
			itouch = obj.value
			
		if (obj.device_type == 'light')
				light = obj.value	
		
	}
	
	ret.push(itouch)
	ret.push(light)
	
	return ret
	
}




function addHtmlCard(head, elight, eaircon, cardid) {

	var html = "";
	var total = Number(elight) + Number(eaircon)

	html += "<div class='card mt-3 text-white border border-success' style='width: 18rem' id='"	+ cardid + "'>"
	html += "<div class='card-header text-center h5' style='background-color:YellowGreen ;'>"+ head + "</div>"
	html += "<div class='card-body' style='background-color:White;'>"
	//html += "<div class='round hollow green'>"
	//	html += "</div>"
	html += "<div class='text-danger distotal text-center font-weight-bold  h1 pt-1 pb-1 display-3'>"+ total + "</div>"
	html += "<div class='text-danger distotal text-right font-weight-bold h4 pt-1 pb-1'>Total</div>"
	html += "<div class='text-success h4 mt-1 text-lightbulb'> <i class='fas fa-lightbulb fa-fw mr-3' ></i>照明:"	+ elight + " W</div>"
	html += "<div class='text-success h4 mt-1 text-itoch' > <i class='fas fa-chalkboard fa-fw mr-3'></i>空調:"+ eaircon + " W</div>"
	html += "</div>"
	html += "<div class='card-footer' style='background-color:YellowGreen ;'>"
	//html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 tbl-show' id ='tblbtn"+cardid+"' onclick='TblShow(this.id);'><i class='fas fa-table fa-2x' style='color:green'></i></a>"
	//html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 set-dev'  id ='setbtn"+cardid+"' onclick='DeviceSet(this.id);'><i class='fas fa-cog fa-2x' style='color:green'></i></a>"
	//html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 chat-show'  id ='chtbtn"+cardid+"' onclick='ChartShow(this.id);'><i class='fas fa-chart-line fa-2x' style='color:green'></i></a>"
	html += "</div>"
	html += "</div>"
	html += "<div class='cardmargin'>"
	html += "<span class='ml-3'></span>"
	html += "</div>"

	return html

}





function addHtmlCardDevice(head, elight, eaircon, cardid,type) {

	var html = "";
	var total = Number(elight) + Number(eaircon)

	html += "<div class='card mt-3 text-white border border-success' style='width: 18rem' id='"	+ cardid + "'>"
	html += "<div class='card-header text-center h5' style='background-color:YellowGreen ;'>"+ head + "</div>"
	html += "<div class='card-body' style='background-color:White;'>"
		
	//html += "<div class='card-msg round hollow text-left '></div>"
	//html += "<div class='text-success text-center h1 '>"+ total + "</div>"
	//html += "<div class='text-success text-right  h4 pt-1 pb-2'></div>"
		if(type=='light')
	html += "<div class='text-success h1  font-weight-bold text-center mt-4 mb-4 text-lightbulb'> <i class='fas fa-lightbulb fa-fw mr-3 '  style='color:black' ></i> <span class=''>"+ elight +" W</span> </div>"  
	    else
	html += "<div class='text-success h1  font-weight-bold text-center mt-4 mb-4 text-itoch' > <i class='fas fa-chalkboard fa-fw mr-3 '></i><span class=''>"+ eaircon +" W</span> </div>" 
	html += "</div>"
	html += "<div class='card-footer' style='background-color:YellowGreen ;'>"
	//html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 tbl-show' id ='tblbtn"+cardid+"' onclick='TblShow(this.id);'><i class='fas fa-table fa-2x' style='color:green'></i></a>"
	if(type=='light')
		{
	//html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 set-dev'  id ='setbtn"+cardid+"' onclick='DeviceSet(this.id);'><i class='fas fa-cog fa-2x' style='color:green'></i></a>"
	//html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 chat-show'  id ='chtbtn"+cardid+"' onclick='ChartShow(this.id);'><i class='fas fa-chart-line fa-2x' style='color:green'></i></a>"
  //  html += "<a class='h5 align-top'>設定</a>"
		}
	html += "</div>"
	html += "</div>"
	html += "<div class='cardmargin'>"
	html += "<span class='ml-3'></span>"
	html += "</div>"

	return html

}

function setDevSend()
{
	
	var brival= $('#brightnessVal').text();
	var cctval= $('#cctVal').text();
	var power=$('input[name=powerstate]:checked').val()
	//alert(brival+","+cctval+","+power+","+devid_now)

	if (power=='on')
		power =1
		else 
			power =0

	var  setDevSendOnj ={};
	
	setDevSendOnj['device_id']=devid_now
	setDevSendOnj['power']=power
	setDevSendOnj['bri']=brival
	setDevSendOnj['cct']=cctval
	
	CallDeviceCmd(setDevSendOnj)
	
}

function DeviceSet(devid)
{
	
	var id = devid.substring(9)
	CallSingleLightInfoAPI(id);	


	
}

function ShowSlider(did,devinfo) {

	var html = "";
	
	
	var brinow
	var cctnow
	var lpwrnow
	var powernow
	
	for ( var i = 0; i < devinfo.length; i++) {
		
		var obj = devinfo[i];
		//console.log(obj.name+','+obj.data)
		
		if (obj.name == 'bri')
			brinow = obj.value
			
		if (obj.name == 'cct')
				cctnow = obj.value	
				
		if (obj.name == 'lpwr')
			   lpwrnow = obj.value	
			   
		if (obj.name == 'power'){
			   powernow = obj.value;
			   
		}
		
	}
	
	//$(".power_radio input:radio").removeAttr("checked");	
	$('input[name=powerstate]:checked').removeAttr('checked');
	
	if (powernow =='1')
	{
		
	  $(".power_radio input[name='powerstate'][value='on']").prop("checked", true);
	  $(".power_radio .status").text("目前狀態為: 開啟");
	}else{
		
		$(".power_radio input[name='powerstate'][value='off']").prop("checked", true);	
		$(".power_radio .status").text("目前狀態為: 關閉");
	}
	
	
	

	// $("#brightnessVal").text('10');
	$("#brightnessDefaultVal").text(brinow);

	var briSlider = $("#brightness").slider();
	briSlider.slider('setValue', brinow)
	$("#brightnessVal").text(brinow);
	$("#brightness").on("slide", function(slideEvt) {
		$("#brightnessVal").text(slideEvt.value);
	});

	// $("#tempness").slider();

	// $("#cctVal").text('10');
	$("#cctDefaultVal").text(cctnow);

	var cctSlider = $("#cctness").slider();
	cctSlider.slider('setValue', cctnow)
	$("#cctVal").text(cctnow);
	$("#cctness").on("slide", function(slideEvt) {
		$("#cctVal").text(slideEvt.value);
	});

}

function processSingleDeviceData(deviceInfo) {

	
	var id= deviceInfo.device_id
	var devinfo = deviceInfo.data;
	
	devid_now = id;
	
	console.log(devinfo)
	//var device_id_set = jsonPath(DeviceInfo, "$.data[*].device_id")


	ShowSlider(id,devinfo)
	var modal = $('#setdevModal');
	
	modal.find('.modal-title').text(FindDeviceName(id))
	modal.modal()
	

}


function FindDeviceData(id) {

	var data =jsonPath(DeviceInfo, "$..[?(@.device_id == '" + id + "')].data")[0]
	  
		
		var brinow
		var cctnow
		var lpwrnow
		var deviceobj={}
		
		for ( var i = 0; i < data.length; i++) {
			
			var obj = data[i];
			//console.log(obj.name+','+obj.data)
			
			if (obj.name == 'bri')
				brinow = obj.value
				
				
			if (obj.name == 'cct')
				cctnow = obj.value	
				 	
			if (obj.name == 'lpwr')
			    lpwrnow = obj.value			
			
			
			
			deviceobj[obj.name]= obj.value
			
		}
		
		
		
		return deviceobj
		
	//return jsonPath(DeviceInfo, "$..[?(@.device_id == '" + id + "')].data")

}



function processDeviceData(deviceInfo) {

	$("#cards .card").remove();
	$("#cards .cardmargin").remove();

    $("#tables .table-view").remove();
	
	$("#tables").append('<div class="table-view mt-4"></div>')
	
	$(".table-view").append('<table id="tblData" class="cell-border stripe order-column hover" style="width:120%"></table>')
	
	
	DeviceInfo = deviceInfo;
	var device_id_set = jsonPath(DeviceInfo, "$.devices[*].device_id")

	
	
	var tableDataArray =[];
	
	
	
	if(device_id_set.length >0)
	device_id_set.forEach(function(itemid, index) {

		var tmpJson={};
		tmpJson.id=itemid
		
		
		var device_name = FindDeviceName(itemid);
		var device_type = FindDeviceType(itemid);
		var device_data = FindDeviceData(itemid)
		
		tmpJson.device_name=device_name
		tmpJson.device_type=device_type
		tmpJson.epower=device_data.lpwr
		tmpJson.status=device_data.power
		
		tableDataArray.push(tmpJson);
		//var device_data = FindDeviceData(itemid);

		//console.log(itemid + ',' + device_name)

		//itemid = 'Dev' + itemid;

		//if (device_type=='light')
		//	html = addHtmlCardDevice(device_name, device_data.lpwr , '0', itemid,device_type)
		//else 
		//	html = addHtmlCardDevice(device_name, '0' , device_data.lpwr , itemid,device_type)
		//
		//$("#cards").append(html);
		
		//var status_css ="#"+itemid+ " .fa-lightbulb"
		
		//if(device_data.power=='1')
		//$(status_css).css({"background-color": "GoldenRod"});
		
		//if(device_data.power=='0')
		  // $("#iconlightbulb").css({"background-color": "LightSteelBlue "});
			
		//if(device_data.power=='1')
		//$(status_css).css('color', 'GoldenRod');
		
	});

	
	showDeviceTable(tableDataArray);
	//$("#cards .text-fan").remove();
	//addDeviceClickEvent();

}


function showDeviceTable(device_data) {

			
	var t = $('#tblData').DataTable({
			data: device_data,
			columns: [
				{data: null},
				{data: 'device_name'},
				{data: 'device_type'},
				{data: 'epower'},
				{data: 'status', render: function (data, type, full, meta) { 
					if (data==='1') return '<span style="color:green" class="fas fa-lightbulb" ></span>' 
					if (data==='0') return '<span style="color:black" class="fas fa-lightbulb" ></span>' 
					else return ''
					}
				}
				],
			columnDefs: [
			    {
	            "searchable": false,
	            "orderable": false,
	            "title": "序號",
	            "targets": 0,
	            "width": "5%",
	            "className": 'dt-center'
	        },
				{
					"title": "裝置名稱",
					"targets": 1,
					"width": "10%",
					"className": 'dt-center'
				},{
					"title": "裝置類別",
					"targets": 2,
					"width": "20%",
					"className": 'dt-center'
				},{
					"title": "秏能(KW)",
					"targets": 3,
					"width": "20%",
					"className": 'dt-center'
				},{
					"title": "狀態",
					"targets": 4,
					"width": "5%",
					"className": 'dt-center'
				}
				
				
			],
			
			"rowCallback": function( row, data, index ) {
				
				 $('td', row).css('font-size', '18px');
				
				function isEven(n) {
					   return n % 2 == 0;
					}
				
		       
		       if(isEven(index)){
		            $('td', row).css('background-color', 'orange');
		           
		       }
		       else
		    	   $('td', row).css('background-color', 'yellow');
		    },
			
			
			language: {
				"sProcessing": "處理中...",
				"sLengthMenu": "顯示 _MENU_ 項結果",
				"sZeroRecords": "沒有匹配結果",
				"sInfo": "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
				"sInfoEmpty": "顯示第 0 至 0 項結果，共 0 項",
				"sInfoFiltered": "(由 _MAX_ 項結果過濾)",
				"sInfoPostFix": "",
				"sSearch": "搜索:",
				"sUrl": "",
				"sEmptyTable": "表中數據為空",
				"sLoadingRecords": "載入中...",
				"sInfoThousands": ",",
				"oPaginate": {
					"sFirst": "首頁",
					"sPrevious": "上頁",
					"sNext": "下頁",
					"sLast": "末頁"
				},
				"oAria": {
					"sSortAscending": ": 以升序排列此列",
					"sSortDescending": ": 以降序排列此列"
				}
			}

		});
		
		
		
		t.on( 'order.dt search.dt', function () {
	        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );
	    } ).draw();
	
}
   






function showLocTable(light_data) {

		
	
	
	var t = $('#tblData').DataTable({
			data: light_data,
			columns: [
				{data: null},
				{data: 'name'},
				{data: 'eitouch'},
				{data: 'elight'},
				{data: 'etotal'}				
				],
			columnDefs: [
			    {
	            "searchable": false,
	            "orderable": false,
	            "title": "序號",
	            "targets": 0,
	            "width": "5%",
	            "className": 'dt-center'
	        },
				{
					"title": "地點名稱",
					"targets": 1,
					"width": "10%",
					"className": 'dt-center'
				},{
					"title": "itouch秏能(KW)",
					"targets": 2,
					"width": "20%",
					"className": 'dt-center'
				},{
					"title": "照明秏能(KW)",
					"targets": 3,
					"width": "20%",
					"className": 'dt-center'
				},{
					"title": "總秏能(KW)",
					"targets": 4,
					"width": "30%",
					"className": 'dt-center'
				}
				
				
			],
			
			"rowCallback": function( row, data, index ) {
				
				 $('td', row).css('font-size', '18px');
				
				function isEven(n) {
					   return n % 2 == 0;
					}
				
		       
		       if(isEven(index)){
		            $('td', row).css('background-color', 'orange');
		           
		       }
		       else
		    	   $('td', row).css('background-color', 'yellow');
		    },
			
			
			language: {
				"sProcessing": "處理中...",
				"sLengthMenu": "顯示 _MENU_ 項結果",
				"sZeroRecords": "沒有匹配結果",
				"sInfo": "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
				"sInfoEmpty": "顯示第 0 至 0 項結果，共 0 項",
				"sInfoFiltered": "(由 _MAX_ 項結果過濾)",
				"sInfoPostFix": "",
				"sSearch": "搜索:",
				"sUrl": "",
				"sEmptyTable": "表中數據為空",
				"sLoadingRecords": "載入中...",
				"sInfoThousands": ",",
				"oPaginate": {
					"sFirst": "首頁",
					"sPrevious": "上頁",
					"sNext": "下頁",
					"sLast": "末頁"
				},
				"oAria": {
					"sSortAscending": ": 以升序排列此列",
					"sSortDescending": ": 以降序排列此列"
				}
			}

		});
		
		
		
		t.on( 'order.dt search.dt', function () {
	        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );
	    } ).draw();
	
}
   




function onShowSelect(thisid) {
	
	sel_id = thisid
	var this_id = "#" +thisid
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();
	var chgselect;
	//var thisid = this.id
	
	localStorage.setItem('SelId', sel_val);
	
	$("#cards .card").remove();
	$("#cards .cardmargin").remove();
	
	$("#charts .chart").remove();
	
	$("#tables .table-view").remove();
	
	$("#tables").append('<div class="table-view mt-4"></div>')
	
	$(".table-view").append('<table id="tblData" class="cell-border stripe order-column hover" style="width:120%"></table>')
	
	if(thisid=='root')
	{
		chgselect ='#select_location_L1'
		$("#select_location_L1").append($("<option></option>").attr("value", "Led-1").text("-地點-"));
		$("#select_location_L2").append($("<option></option>").attr("value", "Led-2").text("-iTouch-"));
		//$("#select_location_L3").append($("<option></option>").attr("value", "Led-3").text("-辦公室-"));
		
		
		
	}
	
	
	
	
	if (thisid=='select_location_L1'){
		chgselect ='#select_location_L2'	
			$("#select_location_L2 option").not('[value^="Led" ]').remove()
		    $("#select_location_L3 option").not('[value^="Led" ]').remove()
		    
		   
			
	}		
	if (thisid=='select_location_L2'){
	 	chgselect ='#select_location_L3'		
	
	 		 $("#select_location_L3 option").not('[value^="Led" ]').remove()
			   
			   
	}
	
	 		
	var loc_set 
	
	if(thisid!='root')
	loc_set = jsonPath(LocInfo, "$..[?(@.location_id == '" + sel_val.substring(3) + "')].next[*].location_id")
    else
	loc_set = jsonPath(LocInfo, "$.location[*].location_id")
	
	
	var tableDataArray =[];
	
	if (loc_set.length > 0)
	loc_set.forEach(function(itemid, index) {

		var tmpJson={};
		tmpJson.id=itemid
		
		var tmpJson={};
		
		var location_name = FindLocationName(itemid);
				
		tmpJson.name=location_name[0]
		
		var locdata = FindLocationData(itemid);
		
		addHtmlChart(location_name,'Lct'+itemid);
		
		itemid = 'Los' + itemid;
		
		
		//console.log(itemid + ',' + location_name)

		var eitouch = locdata[0];
		var elight = locdata[1];
		
		
		var etotal = parseFloat(eitouch) + 	parseFloat(elight)
		
		tmpJson.eitouch=eitouch
		
		tmpJson.elight=elight
		
		tmpJson.etotal=etotal
		
		tableDataArray.push(tmpJson);
		
		//console.log(itemid + ',' + location_name)

		$(chgselect).append($("<option></option>").attr("value", itemid).text(location_name));
		
		//html = addHtmlCard(location_name, elight, eitouch, itemid)
		//$("#cards").append(html);
		

	})
	else 
	{

		CallLightInfoAPI(sel_val.substring(3))
	}
	
	
	
	showLocTable(tableDataArray);
	ShowOther();
	//if(localStorage.getItem('SelId')!='root')
	//onShowSelect(localStorage.getItem('SelId'));
	

}


function ShowOther()
{

	ShowHeaderName();
	$('#energy').collapse('show');
}



function aftermenu(){
	
	 CallLocationInfoAPI();
}



function CallLocationInfoAPIOK(location_data){
	
	LocInfo = location_data;
	
	localStorage.setItem('SelId', 'root');
	onShowSelect('root')
	
	/*
	var L1_id_set
	
	$("#cards .card").remove();
	$("#cards .cardmargin").remove();
	
	$("#charts .chart").remove();
	
	$("#select_location_L1").append($("<option></option>").attr("value", "Led-1").text("-地點-"));
	$("#select_location_L2").append($("<option></option>").attr("value", "Led-2").text("-樓層-"));
	$("#select_location_L3").append($("<option></option>").attr("value", "Led-3").text("-辦公室-"));
	

	L1_id_set = jsonPath(location_data, "$.location[*].location_id")
	
	
	var tableDataArray =[];
	
	
	//showTables(location_data.location)
	
	L1_id_set.forEach(function(itemid, index) {

		var tmpJson={};
		
		tmpJson.id=itemid
		
		var location_name = FindLocationName(itemid);
				
		tmpJson.name=location_name[0]
		
		var locdata = FindLocationData(itemid);
		
		
		//addHtmlChart(location_name,'Lct'+itemid);
		
		itemid = 'Los' + itemid;
		
		//console.log(location_name + ',' + location_name)
		
		
		
		//console.log(itemid + ',' + location_name)

		var eitouch = locdata[0];
		var elight = locdata[1];
		var etotal = parseFloat(eitouch) + 	parseFloat(elight)
			
		tmpJson.eitouch=eitouch
		
		tmpJson.elight=elight
		
		tmpJson.etotal=etotal
		
		tableDataArray.push(tmpJson);
		//html = addHtmlCard(location_name, elight, eitouch, itemid)
		//$("#cards").append(html);
		
		
		$("#select_location_L1").append($("<option></option>").attr("value", itemid).text(location_name));

		

	});
	
	
	
	showTables(tableDataArray);
	*/
	
}







