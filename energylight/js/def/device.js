
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
		
	ShowHeaderName();
	CallgetDevices('electric')
	addDeviceModal();
}

function ShowConfig() {
	
	
}

function addDevice() {

	var modal = $('#DeviceModal');

	modal.modal()
	
	//alert("新增裝置");

}

function addDeviceModal() {

	$("#add-device").empty();
	$("#add-device").load("include/adddevice.html");

	

}




function CallgetDevicesOK(device_data)
{
	
	console.log(device_data)
	
	showDeviceTable(device_data)
	
}






function showDeviceTable(device_data) {

	
	var t = $('#tblData').DataTable({
			data: device_data,
			searching : false,
			paging : false,
			info : false,
			columns: [
				{data: null},
				{data: 'alias'},
				{data: 'vendor_name'},
				{data: 'type_name'},
				{data: 'status'}
				],
			columnDefs: [
			    {
	            "searchable": false,
	            "orderable": false,
	            "title": "序號",
	            "targets": 0,
	            "width": "10%",
	            "className": 'dt-center'
	        },
				{
					"title": "裝置名稱",
					"targets": 1,
					"width": "20%",
					"className": 'dt-center'
				},{
					"title": "裝置廠商名稱",
					"targets": 2,
					"width": "15%",
					"className": 'dt-center'
				},
				{
					"title": "裝置類型",
					"targets": 3,
					"width": "15%",
					"className": 'dt-center'
				},
				{
					"title": "連線狀態",
					"targets": 4,
					"width": "15%",
					"className": 'dt-center'
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
		
		
		
		t.on( 'order.dt search.dt', function () {
	        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );
	    } ).draw();
	
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







