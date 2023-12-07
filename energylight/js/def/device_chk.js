
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
	//CallgetDevices('ecodevice')
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

function QueryDevice(){
	
	var taxid =$('#companyid').val();
	var devkey =$('#deviceid').val();
	
	if(taxid.length != 8)
	alert('統編輸入錯誤')
	else
	CallqueryDevices(taxid,devkey)
	//alert(taxid +','+devkey);
}

function CallqueryDevicesOK(device_data)
{
	
	console.log(device_data)
	
	if(device_data.length < 1){
		alert('此統編無資料')
		$('#tblData').empty();
	}
	else
	showDeviceTable(device_data)
	
}

function CallgetDevicesOK(device_data)
{
	
	console.log(device_data)
	
	showDeviceTable(device_data)
	
}






function showDeviceTable(device_data) {

	
	$('#tblData').empty();
	
	
	var t = $('#tblData').DataTable({
			data: device_data,
			destroy:true,
			searching : false,
			paging : false,
			info : false,
			columns: [
				{data: null},
				{data: 'device_key'},
				{data: 'status'},
				{data: 'type_name'},
				{data: 'report_time'},
				],
			columnDefs: [
			    {
	            "searchable": false,
	            "orderable": false,
	            "title": "序號",
	            "targets": 0,
	            "width": "10%",
	            "className": 'dt-center'
	        },{
	            targets: 1, // this means controlling cells in column 1
	            render: function(data, type, row, meta) { 
	             
	            	
	            	console.log(row + ',' + data+ ',' + meta+ ',' + type)
	                return '<div class="device-link">' + data + '</div>';
	             
	            }
	          },{
		            targets: 2, // this means controlling cells in column 1
		            render: function(data, type, row, meta) { 
		             
		            	
		            	console.log(row + ',' + data+ ',' + meta+ ',' + type)
		            	if(data=='online')
		                return '<div class="online">連線中</div>';
		            	else
		            	return '<div class="offline">斷線</div>';
		             
		            }
		          },
				
				{
					"title": "裝置編號",
					"targets": 1,
					"width": "20%",
					"className": 'dt-center'
				},{
					"title": "狀態",
					"targets": 2,
					"width": "15%",
					"className": 'dt-center'
				},
				{
					"title": "類別",
					"targets": 3,
					"width": "15%",
					"className": 'dt-center'
				},
				{
					"title": "最近資料時間",
					"targets": 4,
					"width": "15%",
					"className": 'dt-center'
				}
			],
			
			"rowCallback": function( row, data, index ) {
				
				
				console.log(row + ',' + data+ ',' + index)
				/*
				if(index==1){
				 $('td', row).css('font-size', '18px');  
		         $('td', row).css('color', 'blue');
				}
		      */
		    	 
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
		
		
		$('#tblData tbody').on( 'click', 'td', function (){
			
			
			if($(this).index()==1){
			    //alert('Data:'+$(this).html().trim());
				//alert('Data:'+$(this).text());
		        //alert('Row:'+$(this).parent().find('td').html().trim());
		        //alert('Column:'+$('#tblData thead tr th').eq($(this).index()).html().trim());
				var devno =$(this).text()
				
				$('#device-no').text('Device 編號  : '+devno);
				
				CallqueryDeviceData(devno);
				//ShowDevData();
			}
			});
	
}
   
function CallqueryDeviceDataOK(device_data){
	
	console.log(device_data)
	
	if(device_data.length < 1)
		{
		alert('此裝置無資料')
		$('#devData').empty();
		}
		else
		showDeviceData(device_data)
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




function showDeviceData(device_data) {

	
	$('#devData').empty();
	
	
	var t = $('#devData').DataTable({
			data: device_data,
			destroy:true,
			searching : false,
			paging : false,
			info : false,
			columns: [
				{data: null},
				{data: 'report_time'},
				{data: null},
				{data: null},
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
					"title": "資料時間",
					"targets": 1,
					"width": "20%",
					"className": 'dt-center'
				},
				
				{
					"title": "電壓",
					"targets": 2,
					"width": "20%",
					"className": 'dt-center'
				},
				
				{
					"title": "電流",
					"targets": 3,
					"width": "20%",
					"className": 'dt-center'
				}
				
				
			],
			
			"rowCallback": function( row, data, index ) {
				
				//console.log(row + ',' + data+ ',' + index)	
				
				for (var i = 0; i < data.items.length; i++) {
					var obj = data.items[i];

					
					if(obj.item=='V')
						$("td:eq(2)", row).text(obj.value);
					
					if(obj.item=='A')
						$("td:eq(3)", row).text(obj.value);
					
				    console.log(obj.item +'---'+obj.value);
            }
				
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


