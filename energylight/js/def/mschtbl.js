
var location_data;

var locations = [];
var floor = [[]];
var office = [[[]]];

var device = [];
var splitstr = "&&"

var schedule = [];

	$(document).ready(function () {
		
		 $("#scheduleroot").click();

		ShowLoginInfo();
		CallLocationInfo();

		//ShowWin();

		//ShowSch()
		
		CallSchInfo();

	});

//some code



function Showtable() {

	   var report_data=''
	
			
		$('#example').DataTable({
			data: reportdata,
			columns: [{data: 'name'},
			{data: 'Location'},
			{data: 'SchSetting'},
			{data: 'Action'}				
				],
			destroy: true,
			columnDefs: [{
					"title": "排程名稱",
					"targets": 0
				}, {
					"title": "控制裝置",
					"targets": 1
				}, {
					"title": "排程周期",
					"targets": 2
				}, {
					"title": "執行動作",
					"targets": 3
				},
				{
				"targets": 4,
				"data": "null",
				"width": "20%",
				"defaultContent": "<button type='button' name='delrow' class='btn btn-danger btn-md'  style='margin-right:10px;'>刪除</button> <button type='button' name='viewrowbtn' class='btn btn-primary btn-md'  style='margin-right:10px;'   >修改</button>"
			}
			],
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

	

}

function addHtmlSlider() {

	var html = "";
	html += "<BR>"
	html += "<BR>"
	html += "<div class='adjust'>"
	html += "<input id='brightness' type='text' data-slider-min='-5' data-slider-max='100' data-slider-step='1' data-slider-value='10' data-slider-id='GC'/>"
	html += "<BR>"
	html += "<span id='brightnessValLabel'>亮度調整<span id='brightnessVal'>10</span></span>"
	html += "</div>"
	html += "<BR>"
	html += "<div class='adjust'>"
	html += "<input id='tempness' type='text' data-slider-min='-5' data-slider-max='100' data-slider-step='1' data-slider-value='60' data-slider-id='GC2'/>"
	html += "<BR>"
	html += "<span id='tempnessValLabel'>色溫調整<span id='tempnessVal'>60</span></span>"
	html += "<BR>"
	html += "<BR>"
	html += "<button type='button' class='btn btn-danger btn-circle btn-xl' ><i class='fa fa-power-off'></i></button>"
	html += "<BR>"
	html += "<span>電源開關</span>"
	html += "</div>"

	return html

}

function addHtmlCard(head, eW, ewP, cardid) {

	var html = "";
	html += "<div class='card' id='" + cardid + "'>"
	html += "<h6 class='card-header'>" + head + "</h6>"
	html += "<div class='card-body'>"
	html += "<div class='text-center'>"
	html += "<h5 class='font-light mb-0'>" + eW + " W</h5>"
	html += "<span class='text-muted'>今日耗電</span>"
	html += "</div>"
	html += "<span class='text-success'>" + ewP + "%</span>"
	html += "<div class='progress'>"
	html += "<div class='progress-bar bg-success' role='progressbar' style='width:" + ewP + "%; height: 6px;' aria-valuenow='25' aria-valuemin='0' aria-valuemax='100'></div>"
	html += "</div>"
	html += "</div>"
	html += "<div class='card-footer'><a type='button' class='btn-floating btn-small btn-fb' id='report1' data-toggle='modal' data-target='#exampleModal' data-whatever='" + head + "'><i class='fa fa-table fa-fw'></i></a></div>"
	html += "</div>"
	html += "<div class='cardmargin'>"
	html += "<span class='ml-3'></span>"
	html += "</div>"

	return html

}

function addHtmlConditionCard(head) {
	
	var html = "";
	html += "<div class='card' id=''>"
	html += "<h6 class='card-header'>" + head + "</h6>"
	html += "<div class='card-body'>"
	html += "<div class='text-center'>"
	html += "<h5 class='font-light mb-0'></h5>"
	html += "<span class='text-muted'></span>"
	html += "</div>"
	html += "</div>"
	html += "</div>"
	
	return html

}


function ShowSlider() {

	var html = "";
	html = addHtmlSlider();

	$("#adjusts").append(html);

	$("#brightness").slider();
	$("#brightness").on("slide", function (slideEvt) {
		$("#brightnessVal").text(slideEvt.value);
	});

	$("#tempness").slider();
	$("#tempness").on("slide", function (slideEvt) {
		$("#tempnessVal").text(slideEvt.value);
	});

}


function ShowCondition() {
	
	 $('#dateset *').attr("disabled",false);

	var condition =["會議","午休","下班","省電"]
	
	
	
	condition.forEach(function (element) {
		
		
		html = addHtmlConditionCard(element)
			$("#conditions").append(html);

	})

	addCardClickEvent();
	

}

//畫面 選擇辦公室
function onShowLight() {

	var sel_text = $("#select_location_L3").find("option:selected").text()
		var sel_val = $("#select_location_L3").val();
	$("#adjusts .adjust").remove();
	$('#adjusts  BR').remove();
	$('#dateset *').attr("disabled",false);
	//$("#conditions .card").remove();
	//alert(sel_text + "," + sel_val);

	//CallLightInfo(sel_val)

	//ShowSlider();
   
    //ShowCondition()
     

}

function addCardClickEvent() {

	$('.card').on('click', function (event) {
		//alert($(".card-header", this).css("background-color"));

		var color = $(".card-header", this).css("background-color")
			//var idstr ='#'+this.id
			//$(idstr).color = red;
			//$(this .card-header).css("background-color","red");

			//rgba(0, 0, 0, 0.03)

			//rgb(255, 255, 0)


			if (color == 'rgba(0, 0, 0, 0.03)')
				$(".card-header", this).css("background-color", "yellow");
			else
				$(".card-header", this).css("background-color", 'rgba(0, 0, 0, 0.03)');

	});

}

function onShowLight2() {

	$("#cards .card").remove();
	$("#cards .cardmargin").remove();

	device.forEach(function (element) {
		var lightinfo_str = []
		lightinfo_str = element.split(splitstr);
		html = addHtmlCard(lightinfo_str[0], lightinfo_str[1], lightinfo_str[1] / 2, lightinfo_str[2])
			$("#cards").append(html);

	})

	addCardClickEvent();

}

function onSelectOffice() {
	var sel_location = $("#select_location_L1").find("option:selected").text()
		var sel_l_val = $("#select_location_L1").val();
	var sel_floor = $("#select_location_L2").find("option:selected").text()
		var sel_f_val = $("#select_location_L2").val();

	//alert(sel_location + "," + sel_l_val+ "," + sel_floor+ "," + sel_f_val); //可以取得 select.text

	var i = 0;
	$("#select_location_L3 option").remove();
	$("#select_location_L3").append($("<option></option>").attr("value", "-1").text("-辦公室-"));

	$("#cards .card").remove();
	$("#cards .cardmargin").remove();
	$("#adjusts .adjust").remove();

	office[sel_l_val][sel_f_val].forEach(function (element) {

		var office_str = []

		office_str = element.split(splitstr);

		var office_code = office[sel_l_val][sel_f_val][i]
			//office[sel_l_val][sel_f_val][i]

			$("#select_location_L3").append($("<option></option>").attr("value", office_str[0]).text(office_str[1]));

		var html = "";
		var eW = Math.floor(Math.random() * 50)
			var ewP = eW / 2

			html = addHtmlCard(office_str[1], eW, ewP, '')

			$("#cards").append(html);

		i++;
	});

	//addCardClickEvent();

}

function onSelectFloor() {
	var sel_text = $("#select_location_L1").find("option:selected").text()
		var sel_val = $("#select_location_L1").val();
	//alert(sel_text + "," + sel_val); //可以取得 select.text


	var i = 0;
	$("#select_location_L2 option").remove();
	$("#select_location_L3 option").remove()
	$("#select_location_L2").append($("<option></option>").attr("value", "-1").text("-樓層-"));
	$("#select_location_L3").append($("<option></option>").attr("value", "-1").text("-辦公室-"));

	$("#cards .card").remove();
	$("#cards .cardmargin").remove();
	$("#adjusts .adjust").remove();

	floor[sel_val].forEach(function (element) {

		$("#select_location_L2").append($("<option></option>").attr("value", i).text(element));

		var html = "";
		var eW = Math.floor(Math.random() * 50)
			var ewP = eW / 2

			html = addHtmlCard(floor[sel_val][i], eW, ewP, '')
			$("#cards").append(html);
		i++;

	});

	//addCardClickEvent();

}

function ShowLoginInfo() {

	var accname = getSearchParams('accname'); ;
	$("#accname").text(accname);

}


function CallSchInfo() {
	schedule = [];

	$.ajax({

		//url: 'http://localhost:3000/Schedule/',
		//url: 'http://localhost:8446/energy/getDeviceList?location_id=' + deviceid,
		url: 'sch.json' ,
		dataType: 'json',
		method: "GET",
		success: function (data) {
			schedule = data.Sch

/*
				$.each(data.Sch, function (i, val) {
					schedule[i] = val.name + splitstr + val.Location + splitstr + val.Location + splitstr + val.SchSetting  + splitstr + val.Action
				})
*/
				//onShowLight2()
				Showtable();

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}

	})

}











function CallLightInfo(deviceid) {
	device = [];

	$.ajax({

		//url: 'http://localhost:3000/device/',
		url: 'http://localhost:8446/energy/getDeviceList?location_id=' + deviceid,
		dataType: 'json',
		method: "GET",
		success: function (data) {
			device_data = data

				$.each(data.msg.devices, function (i, val) {
					device[i] = val.device_alias + splitstr + val.data + splitstr + val.device_id
				})

				onShowLight2()

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}

	})

}

function CallLocationInfo() {

	$.ajax({
		//url: 'http://localhost:3000/location3/',
		//url: 'http://localhost:8446/energy/getLocation',
		url: url_getLocation ,
		dataType: 'json',
		success: function (data) {
			location_data = data
				var items = [];
			//var i=0
			//var j=0
			//var k=0

			$("#select_location_L1").append($("<option></option>").attr("value", "-1").text("-地點-"));
			$("#select_location_L2").append($("<option></option>").attr("value", "-1").text("-樓層-"));
			$("#select_location_L3").append($("<option></option>").attr("value", "-1").text("-辦公室-"));
			$("#adjusts .adjust").remove();
			$("#conditions .card").remove();

			//locations[0]="-地點-";
			//floor[0][0]="-樓層-";
			//office[0][0][0] ="" ;
			
			 ShowCondition()
			 Showtable()
			
             $('#dateset *').attr("disabled",true);

			$.each(data.msg.location, function (i, L1_val) {

				//alert(key+","+val.id+","+val.name);
				//var location_L1_id=val.name
				//var location_L1_name = val.name

				locations[i] = L1_val.name
					floor[i] = new Array();
				office[i] = new Array();
				//長第一層
				$("#select_location_L1").append($("<option></option>").attr("value", i).text(locations[i]));


               
				//var html = "";
				//var eW = Math.floor(Math.random() * 50)
				//	var ewP = eW / 2

				//html = addHtmlCard(locations[i], eW, ewP)
				//$("#cards").append(html);

				$.each(L1_val.next, function (j, L2_val) {

					floor[i][j] = L2_val.name
						//alert(j+","+val.id+","+val.name);

						office[i][j] = new Array()
						$.each(L2_val.next, function (k, L3_val) {

							office[i][j][k] = L3_val.location_id + splitstr + L3_val.name
								//alert(j+","+val.id+","+val.name);
						})

				})

			});

			//addCardClickEvent();

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}

function getSearchParams(k) {
	var p = {};
	location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (s, k, v) {
		p[k] = v
	})
	return k ? p[k] : p;
}
