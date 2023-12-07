
var location_data;

var locations = [];
var floor = [[]];
var office = [[[]]];

var device = [];
var splitstr = "&&"
var sel_id;	
	

	$(document).ready(function () {

		
		// CallLocationInfoAPI();
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

function aftermenu(){
		
		 CallLocationInfoAPI();
}
function ShowConfig() {
	
	
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
		
		
		
	});

	showDeviceTable(tableDataArray);
	configClick();
	
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
				},
				
				{
					"title": "狀態",
					"targets": 4,
					"width": "5%",
					"className": 'dt-center'
				},
				{
					"targets": 5,
					"title": "設定",
					"data": "null",
					"width": "7%",
					"defaultContent": "<button type='button' name='configDev' class='btn btn-danger'  style='margin-right:10px;'>設定</button> "
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
   

function configClick() {

$('#tblData tbody').on('click', 'button[name=configDev]', function () {

	var table = $('#tblData').DataTable();
	var data = table.row($(this).parents('tr')).data();
	
	CallSingleLightInfoAPI(data.id)
	
	//alert(data.id)
	//alert(FindDeviceData(data.id));
});

}

function onShowSelect(thisid) {
	
	sel_id = thisid
	var this_id = "#" +thisid
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();
	var chgselect;
	//var thisid = this.id
	
	
	if(thisid=='root')
	{
		chgselect ='#select_location_L1'
		$("#select_location_L1").append($("<option></option>").attr("value", "Led-1").text("-地點-"));
		$("#select_location_L2").append($("<option></option>").attr("value", "Led-2").text("-Itouch-"));
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
			   
			   
	 		 
	 		 $("#setConfig").empty();
		  $("#setConfig").load("include/setdev.html");
	 		 
	}
	
	
	 		
	var loc_set 

	
	if(thisid!='root')
	loc_set = jsonPath(LocInfo, "$..[?(@.location_id == '" + sel_val.substring(3) + "')].next[*].location_id")
    else
	loc_set = jsonPath(LocInfo, "$.location[*].location_id")
	
	
	
	
	if (loc_set.length > 0)
	loc_set.forEach(function(itemid, index) {

		//console.log(itemid + ',' + location_name)
		var location_name = FindLocationName(itemid);
		
		itemid = 'Los' + itemid;

		$(chgselect).append($("<option></option>").attr("value", itemid).text(location_name));

	})
	else 
	{

		CallLightInfoAPI(sel_val.substring(3))
	}
	
	
	
	//showLocTable(tableDataArray);
	ShowOther();

}


function ShowOther()
{

	ShowHeaderName();
	$('#deviceconfig').collapse('show');
}

function CallLocationInfoAPIOK(location_data){
LocInfo = location_data;

localStorage.setItem('SelId', 'root');
onShowSelect('root')

}







