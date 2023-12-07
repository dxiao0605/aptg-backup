  






$(document).ready(function() {

	// ShowLoginInfo();
	
	
	   $("#header").load("include/header.html");
	   $("#side-bar").load("include/sidemenu.html");
	
	     
	
	CallLocationInfoAPI();
	
	MainPageClickEvent()
	
	
		
		

	//ShowWin();

	//setTimeout(function () { location.reload(); }, 20 * 1000);
	/*
	setInterval(function () {
		
		if(!$('#setdevModal').hasClass('show'))
			location.reload();
	
	
	}, 20 * 1000);
	*/
});

// some code


function MainPageClickEvent()
{
	
	
	$('.mainpage').on('click',function(event) {
				
				
		localStorage.setItem('currentid', 'N');
				
				
				

			})
	
	
	
	
}




function ShowLineChart(chartname,chartid) {


	function getRandomIntInclusive(min, max) {
	  min = Math.ceil(min);
	  max = Math.floor(max);
	  return Math.floor(Math.random() * (max - min + 1)) + min;
	}




	var html =""

	html += "<div class='chart' >"
	html +="<div id='"+chartid +"' ></div>"
	html += "</div>"

	$("#charts .chart").remove();	
	$("#charts").append(html);

	var dps = [];   //dataPoints. 
		

	      var chart = new CanvasJS.Chart(chartid,{
		   zoomEnabled: true,
	      	
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
		
	}



function ShowTable() {


				var table_array_data = [];

				function toDateStr(d) {

					var strDate = d.getFullYear() + "/" + (d.getMonth() + 1)
							+ "/" + d.getDate();

					return strDate
				}
				Date.prototype.addDays = function(days) {
					this.setDate(this.getDate() + days);
					return this;
				}

				var j = 0;
				var jsonobj = [];
				for (i = 0; i < 31; i++) {

					var obj = {};

					var today = new Date();
					table_array_data[i] = toDateStr(today.addDays(j--));

					obj["wdate"] = table_array_data[i];
					obj["w"] = getRandomIntInclusive(1, 100)

					jsonobj.push(obj)
				}

				function getRandomIntInclusive(min, max) {
					min = Math.ceil(min);
					max = Math.floor(max);
					return Math.floor(Math.random() * (max - min + 1)) + min;
				}

				$('#showtbl').DataTable({
					data : jsonobj,
					columns : [ {
						data : 'wdate'
					}, {
						data : 'w'
					} ],
					destroy : true,
					columnDefs : [ {
						"title" : "日期",
						"targets" : 0
					}, {
						"title" : "耗電量",
						"targets" : 1
					} ],
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
	html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 tbl-show' id ='tblbtn"+cardid+"' onclick='TblShow(this.id);'><i class='fas fa-table fa-2x' style='color:green'></i></a>"
	if(type=='light')
	html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 set-dev'  id ='setbtn"+cardid+"' onclick='DeviceSet(this.id);'><i class='fas fa-cog fa-2x' style='color:green'></i></a>"
	html += "<a type='button' class='btn-floating btn-small btn-fb ml-3 chat-show'  id ='chtbtn"+cardid+"' onclick='ChartShow(this.id);'><i class='fas fa-chart-line fa-2x' style='color:green'></i></a>"
	html += "</div>"
	html += "</div>"
	html += "<div class='cardmargin'>"
	html += "<span class='ml-3'></span>"
	html += "</div>"

	return html

}


function addHtmlbreadcrumb(name, link, id) {

	
	var html = "";

	html += "<li class='breadcrumb-item' id ='" + id + "'>"
	html += "<a href='" + link + "'>" + name + "</a>"
	html += "<i class='fa fa-caret-right mx-2'></i>"
	html += "</li>"

	return html

}

function addHtmlbreadcrumbDevice(name, link, id) {

	var html = "";

	html += "<li class='breadcrumb-item active b-device' id ='" + id + "'>"
	html += "" + name + ""
	html += "</li>"

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



function ChartShow(devid)
{
	
	var id = devid.substring(9)
	var type = devid.substring(6,9)
	//CallSingleLightInfoAPI(id);	

	var modal = $('#linechartModal');
	var headname;
	if(type=='Loc')
	headname = FindLocationName(id)
	else
	headname = FindDeviceName(id)	
	
	modal.find('.modal-title').text(headname)
	modal.modal()	

	ShowLineChart(headname,"linechart"+id);
	
}

function TblShow(devid)
{
	
	var id = devid.substring(9)
	var type = devid.substring(6,9)
	//CallSingleLightInfoAPI(id);	

	var modal = $('#tableModal');
	var headname;
	if(type=='Loc')
	headname = FindLocationName(id)
	else
	headname = FindDeviceName(id)	
	
	modal.find('.modal-title').text(headname)
	modal.modal()
	ShowTable()
	

	
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

// 畫面 選擇辦公室
function onShowLight() {

	var sel_text = $("#select_location_L3").find("option:selected").text()
	var sel_val = $("#select_location_L3").val();
	// $("#adjusts .adjust").remove();
	// $('#adjusts BR').remove();
	// alert(sel_text + "," + sel_val);

	CallLightInfoAPI(sel_val);

	// onShowLight2(sel_val);

	//ShowSlider();

}

function addDeviceClickEvent() {
	
	
	$('.distotal').css({
		 // position: 'absolute',
		  //    top: '35%',
		 //    width: '100%',
		     'text-shadow':  '0 1px 0 hsl(174,5%,80%), 0 2px 0 hsl(174,5%,75%),  0 3px 0 hsl(174,5%,70%), 0 4px 0 hsl(174,5%,66%), 0 5px 0 hsl(174,5%,64%), 0 6px 0 hsl(174,5%,62%), 0 7px 0 hsl(174,5%,61%), 0 8px 0 hsl(174,5%,60%), 0 0 5px rgba(0,0,0,.05), 0 1px 3px rgba(0,0,0,.2), 0 3px 5px rgba(0,0,0,.2), 0 5px 10px rgba(0,0,0,.2), 0 10px 10px rgba(0,0,0,.2), 0 20px 20px rgba(0,0,0,.3)' 
	});

	$('.card').on(
			'click',
			function(event) {
				
				
				//$(".breadcrumb .b-device").remove();

				var sid = this.id.substring(3);
				
				var devicename = FindDeviceName(sid);

				//$(".breadcrumb").append(addHtmlbreadcrumbDevice(devicename, "#", "bid"+ sid));
				
				
				

			//	var color = $(".card-body", this).css("background-color")

			//	$(".card-body").css("background-color", 'rgb(255, 255, 255)');
/*
				if (color == 'rgb(255, 255, 255)') {
					// $(".card-body", this).css("background-color", "Orange");
					$(".breadcrumb .b-device").remove();

					var sid = this.id.substring(3);
					
					var devicename = FindDeviceName(sid);

					$(".breadcrumb").append(addHtmlbreadcrumbDevice(devicename, "#", "bid"+ sid));

				}

				else {
					$(".card-body", this).css("background-color",
							'rgb(255, 255, 255)');
					$(".breadcrumb .b-device").remove();
				}
*/
			});

}


function BreadcrumbClickEvent(id) {
	
	
	
	
	if (id != 'main')

	{
		var sid = id.substring(3);
		parentlistid = []
		parentlistid.push(sid)
		FindAllParentid(sid)
		$(".breadcrumb .breadcrumb-item").remove();

		var html = "";

		for (i = parentlistid.length - 1; i > -1; i--) {

			var ids = parentlistid[i]
			var ids_name = FindLocationName(parentlistid[i])

			// console.log((i) + ": " + ids+ ": " +ids_name);

			if (ids == 'root')
				html = addHtmlbreadcrumb("首頁", "main.jsp", "bre" + ids);
			else
				html = addHtmlbreadcrumb(ids_name, "#", "bre" + ids);

			$(".breadcrumb").append(html);

		}
		addBreadcrumbClickEvent()
		ClickLocCard(sid)
	}
	
	
	
	
	
}


function addBreadcrumbClickEvent() {

	$('.breadcrumb-item').on('click', function(event) {

		BreadcrumbClickEvent(this.id)

	});
}

function addLocationClickEvent() {
	
	
	$('.distotal').css({
		 // position: 'absolute',
		  //    top: '35%',
		 //    width: '100%',
		     'text-shadow':  '0 1px 0 hsl(174,5%,80%), 0 2px 0 hsl(174,5%,75%),  0 3px 0 hsl(174,5%,70%), 0 4px 0 hsl(174,5%,66%), 0 5px 0 hsl(174,5%,64%), 0 6px 0 hsl(174,5%,62%), 0 7px 0 hsl(174,5%,61%), 0 8px 0 hsl(174,5%,60%), 0 0 5px rgba(0,0,0,.05), 0 1px 3px rgba(0,0,0,.2), 0 3px 5px rgba(0,0,0,.2), 0 5px 10px rgba(0,0,0,.2), 0 10px 10px rgba(0,0,0,.2), 0 20px 20px rgba(0,0,0,.3)' 
	});

	$('.card').on('dblclick', function(event) {

		var sid = this.id.substring(3);

		var locname = FindLocationName(sid);
		//var html = addHtmlbreadcrumb(locname, "#", "bre" + sid);

		$(".breadcrumb").append(html);

		//addBreadcrumbClickEvent()
		ClickLocCard(sid)

	});

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


function processDeviceData(deviceInfo) {

	$("#cards .card").remove();
	$("#cards .cardmargin").remove();

	DeviceInfo = deviceInfo;
	var device_id_set = jsonPath(DeviceInfo, "$.devices[*].device_id")

	device_id_set.forEach(function(itemid, index) {

		var device_name = FindDeviceName(itemid);
		var device_type = FindDeviceType(itemid);
		var device_data = FindDeviceData(itemid)
		//var device_data = FindDeviceData(itemid);

		//console.log(itemid + ',' + device_name)

		itemid = 'Dev' + itemid;

		if (device_type=='light')
			html = addHtmlCardDevice(device_name, device_data.lpwr , '0', itemid,device_type)
		else 
			html = addHtmlCardDevice(device_name, '0' , device_data.lpwr , itemid,device_type)
		
		$("#cards").append(html);
		
		var status_css ="#"+itemid+ " .fa-lightbulb"
		
		//if(device_data.power=='1')
		//$(status_css).css({"background-color": "GoldenRod"});
		
		//if(device_data.power=='0')
		  // $("#iconlightbulb").css({"background-color": "LightSteelBlue "});
			
		if(device_data.power=='1')
		$(status_css).css('color', 'GoldenRod');
		
	});

	//$("#cards .text-fan").remove();
	addDeviceClickEvent();

}

function ClickLocCard(id) {

	$("#cards .card").remove();
	$("#cards .cardmargin").remove();
	// $("#adjusts .adjust").remove();

	
	localStorage.setItem('currentid', id);
	
	if (id == 'N')
		id = this.id;

	var loc_set = jsonPath(LocInfo, "$..[?(@.location_id == '" + id + "')].next[*].location_id")

	if (loc_set == false) {

		CallLightInfoAPI(id)
	}

	else {

		loc_set.forEach(function(itemid, index) {

			var location_name = FindLocationName(itemid);
			
			var locdata = FindLocationData(itemid);
			//console.log(itemid + ',' + location_name)

			// $("#select_location_L2").append($("<option></option>").attr("value",
			// itemid).text(location_name));
			itemid = 'Loc' + itemid;

			
			
			
			
			//console.log(itemid + ',' + location_name)

			var eitouch = locdata[0];
			var elight = locdata[1];
				
				
		
			
			html = addHtmlCard(location_name, elight, eitouch, itemid)
			
			$("#cards").append(html);

		});

		$(".set-dev").remove();
		addLocationClickEvent()
		
		//alert(JSON.parse(localStorage.getItem('currentid')))
		
	}
	// addFloorClickEvent()
	// addCardClickEvent();

}

function FindAllParentid(id) {

	if (id != 'root') {
		retp = jsonPath(AddParentLocInfo, "$..[?(@.location_id == '" + id + "')].parent")

		// str ="location.**[location_id='"+id+"'].parent"
		// ret =jsonata(str).evaluate(pp);

		if (retp[0] != 'root') {
			parentlistid.push(retp[0]);
			FindAllParentid(retp[0])
		} else {
			parentlistid.push('root');
		}

	}

	return parentlistid
	// return jsonPath(Location,"$..[?(@.location_id == '3')].name")

}

function addParentObj(pobj,pp) {
	

	var pobjects = [];
	for ( var i in pobj) {
		if (!pobj.hasOwnProperty(i))	continue;
		if (typeof pobj[i] == 'object') {
			//console.log("Object="+i+"***"+obj[i])

			pobjects = pobjects.concat(addParentObj(pobj[i], pp));
		} else {

			if (i == 'location_id') {
				pobj['parent'] = pp
				pp = pobj['location_id']
			}
			 //console.log("key="+i+"***"+obj[i])
		}
	}
	return pobj;
}

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



function CallLocationInfoAPIOK(location_data) {
	
	ShowHeaderName();

	LocInfo = location_data;
	AddParentLocInfo = addParentObj(LocInfo,'root')
	var html = "";

	// $("#adjusts .adjust").remove();
	$("#set-dev").remove();

	$(".breadcrumb .breadcrumb-item").remove();

	$(".breadcrumb").append(addHtmlbreadcrumb("首頁", "main.jsp", "breroot"));

	var L1_id_set = jsonPath(LocInfo, "$.location[*].location_id")
	
	var light_total =0,itouch_total=0;
	
	L1_id_set.forEach(function(itemid, index) {

		var location_name = FindLocationName(itemid);
		var locdata = FindLocationData(itemid);
		//console.log(itemid + ',' + location_name)

		var eitouch = locdata[0];
		var elight = locdata[1];
			
			
		
		addHtmlChart(location_name,'Lct'+itemid);
		itemid = 'Loc' + itemid;
		
		

		html = addHtmlCard(location_name, elight, eitouch, itemid)
		$("#cards").append(html);
		
		light_total = light_total+ Number(elight) 
		itouch_total = itouch_total +Number(eitouch)

	});
	
	
	html = addHtmlCard('總秏能', light_total, itouch_total, 'total')
	$("#card_total").append(html);
	

	// addCardClickEvent();
	$(".set-dev").remove();
	//addLocationClickEvent();
	
	//alert(JSON.parse(localStorage.getItem('currentid')))
	
	var getStorage = JSON.parse(localStorage.getItem('currentid'))
	
	if(getStorage !='N')
	BreadcrumbClickEvent('reh'+getStorage)
	
	

}


