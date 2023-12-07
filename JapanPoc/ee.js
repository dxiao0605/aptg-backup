

$(document).ready(function () {
	
	getenergy()
	
	




setInterval(function () {
		
		if(!$('#HistoryModal').hasClass('show'))
			location.reload();
	
	
	}, 20 * 1000);




	
})


function clickid(val){
	
	//alert(val);
	
	gethistory(val);
	
	var modal = $('#HistoryModal');
	modal.modal();
	//$('#HistoryModal').css("margin-right", $(window).width() - $('.modal-content').width());
	
}


function gethistory(id){
	
	
	
	$.ajax({

	//url: 'energy.json',
    //url: 'http://localhost:8446/monitor/historyData',
	url: '/monitor/historyData',
	data: {
		'group_id' : id ,
		'cust_id'  : 'japan001'
	},
	dataType: 'json',
	method: "GET",
	
	
	success: function (data) {
		
		ShowHistory(data.msg)
			

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	
}


function ShowHistory(data) {
	
	
	
	
	$('#showtbl').DataTable({
		info: false,
		searching: false,
		destroy: true,
		processing: true,
		paging: true,		
		data: data,
		
		columns: [
			{data: 'group_id'},
			{ "data": null,"defaultContent": '大阪'},
			{data: 'update_stamp'},
			{data: 'imp1'},
			{data: 'imp2'},
			{data: 'imp3'},
			{data: 'imp4'},
			{data: 'v1'},
			{data: 'v2'},
			{data: 'v3'},
			{data: 'v4'},
			{data: 'temperature'},
			{ "data": null,"defaultContent": '<span style="color: green ">正常</span>'},
			],
		columnDefs: [
		    {
            "title": "送信機ID",
            "targets": 0,
            "className": 'dt-center',
			  render: function ( data, type, row ) {
                  var color = 'blue';
                  var datatext ="'"+data+"'"
                  
                  return '<span onclick=clickid('+datatext+') class="ddid" style="color:' + color + '">' + data + '</span>';
                }
        },
			{
				"title": "地域",
				"targets": 1,
				"className": 'dt-center'
			},
			{
				"title": "更新時間",
				"targets": 2,
				"className": 'dt-center'
			},
			{
				"title": "[UΩ]CH1",
				"targets": 3,
				"className": 'dt-center'
			},
			{
				"title": "CH2",
				"targets": 4,
				"className": 'dt-center'
			},
			{
				"title": "CH3",
				"targets": 5,
				"className": 'dt-center'
			},
			{
				"title": "CH4",
				"targets": 6,
				"className": 'dt-center'
			},
			{
				"title": "[V]CH1",
				"targets": 7,
				"className": 'dt-center'
			},
			{
				"title": "CH2",
				"targets": 8,
				"className": 'dt-center'
			},
			{
				"title": "CH3",
				"targets": 9,
				"className": 'dt-center'
			},
			{
				"title": "CH4",
				"targets": 10,
				"className": 'dt-center'
			},
			{
				"title": "[℃]",
				"targets": 11,
				"className": 'dt-center'
			},
			{
				"title": "劣化判断結果",
				"targets": 12,
				"className": 'dt-center'
				  
			}
			
		],
		
		
		
		
		language: {
			"sProcessing": "處理中...",
			"sLengthMenu": "ページあたり _MENU_ レコード",
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
				"sPrevious": "前へ",
				"sNext": "次へ",
				"sLast": "末頁"
			},
			"oAria": {
				"sSortAscending": ": 以升序排列此列",
				"sSortDescending": ": 以降序排列此列"
			}
		}

	});
	
	
	
	
}

function getenergy(){
	
	
	
	$.ajax({

	//url: 'http://localhost:8446/monitor/monitorData',
	url: '/monitor/monitorData',
   // url: 'https://www.gtething.tw/monitor/monitorData',
	dataType: 'json',
	method: "GET",
	data: {
		'cust_id'  : 'japan001'
	},
	success: function (data) {
		
		ShowTable(data.msg)
			

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	
}




function ShowTable(data) {
	
		
	$('#energy').DataTable({
		info: false,
		searching: false,
		paging: false,		
		data: data,
		columns: [
			{data: 'group_id'},
			{ "data": null,"defaultContent": '大阪'},
			{data: 'update_stamp'},
			{data: 'imp1'},
			{data: 'imp2'},
			{data: 'imp3'},
			{data: 'imp4'},
			{data: 'v1'},
			{data: 'v2'},
			{data: 'v3'},
			{data: 'v4'},
			{data: 'temperature'},
			{ "data": null,"defaultContent": '<span style="color: green ">正常</span>'},
			],
		columnDefs: [
		    {
            "title": "送信機ID",
            "targets": 0,
            "className": 'dt-center',
			  render: function ( data, type, row ) {
                  var color = 'blue';
                  var datatext ="'"+data+"'"
                  
                  return '<span onclick=clickid('+datatext+') class="ddid" style="color:' + color + '">' + data + '</span>';
                }
        },
			{
				"title": "地域",
				"targets": 1,
				"className": 'dt-center'
			},
			{
				"title": "更新時間",
				"targets": 2,
				"className": 'dt-center'
			},
			{
				"title": "[UΩ]CH1",
				"targets": 3,
				"className": 'dt-center'
			},
			{
				"title": "CH2",
				"targets": 4,
				"className": 'dt-center'
			},
			{
				"title": "CH3",
				"targets": 5,
				"className": 'dt-center'
			},
			{
				"title": "CH4",
				"targets": 6,
				"className": 'dt-center'
			},
			{
				"title": "[V]CH1",
				"targets": 7,
				"className": 'dt-center'
			},
			{
				"title": "CH2",
				"targets": 8,
				"className": 'dt-center'
			},
			{
				"title": "CH3",
				"targets": 9,
				"className": 'dt-center'
			},
			{
				"title": "CH4",
				"targets": 10,
				"className": 'dt-center'
			},
			{
				"title": "[℃]",
				"targets": 11,
				"className": 'dt-center'
			},
			{
				"title": "劣化判断結果",
				"targets": 12,
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
	
	
	
	

	
	
}