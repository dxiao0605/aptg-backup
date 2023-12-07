var sid="";
var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)); 
var url =window.location.protocol+"//"+ window.location.host +context;

$(document).ready(function () {
	$('#HistoryTbl').DataTable({
		"dom": '<lf<t>ip>',
		"processing": true,
		"bJQueryUI": true,
		"ordering": false,
		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "全部"]],
		"sPaginationType": "full_numbers",
		"ajax": {
			"url":  url+"/secured/GetHistory",
			"type": "GET",
			"dataType": "json",
			data: {
				contract : c_id 
		      },
		      dataSrc : function(result) {
					
					if (result.ret == 'list_his_no_record') {
						result.data ={};
					}
					
					return result.data;
		      }
		},
		"columns": [
			{ "data": "logrid","visible": false}, 
			{ "data": "toaddr"  }, 
			{ "data": "msginfo"},
			{ "data": "logtime"}
		],
		
		"language": {
			"lengthMenu": "_MENU_ 條記錄/每頁",
			"zeroRecords": "沒有記錄",
			'processing': "查询中...",
			"loadingRecords": "加载中...",
			"info": "第 _PAGE_ 頁 ( 總共 _PAGES_ 頁 )",
			"infoEmpty": "第 0 頁 ( 總共 _0_ 頁 )",
			"search": "搜尋字串",
			"infoFiltered": "(从 _MAX_ 條記錄過濾)",
			"paginate": {
				"previous": "上一頁",
				"next": "下一頁",
				"first": "第一頁",
				"last": "最末頁"

			}
		}

	});




	
});

