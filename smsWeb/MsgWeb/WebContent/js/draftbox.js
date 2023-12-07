var sid="";
var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)); 
var url =window.location.protocol+"//"+ window.location.host +context;

$(document).ready(function () {
	$('#DraftMsgTbl').DataTable({
		"dom": '<lf<t>ip>',
		"processing": true,
		"bJQueryUI": true,
		"ordering": false,
		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "全部"]],
		"sPaginationType": "full_numbers",
		"ajax": {
			"url":  url+"/secured/GetDraftbox",
			"type": "GET",
			"dataType": "json",
			data: {
				contract : c_id 
		      },
		      dataSrc : function(result) {
					
					if (result.ret == 'list_draft_no_record') {
						result.data ={};
					}
					
					return result.data;
		      }
		},
		"columns": [
				{ "data": "seq_id","visible": false}, 
				{ "data": "msg_c"  }, 
				{ "data": "submit_t"}

		],

		"columnDefs": [{
				"targets": 3,
				"data": "null",
				"width": "20%",
				"defaultContent": "<button type='button' name='deldraftrow' class='btn btn-danger'  style='margin-right:10px;'>刪除</button> <button type='button' name='viewdraftrowbtn' class='btn btn-primary'  style='margin-right:10px;'   >詳細資料</button>"
			}, {
				"targets": 1,
				"width": "70%",
				render: function (data, type, row) {
					return data.length > 70 ?
					data.substr(0, 70) + '…' :
					data;
				}
			}

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

//詳細資料
	$('#DraftMsgTbl tbody').on('click', 'button[name=viewdraftrowbtn]', function () {

		var table = $('#DraftMsgTbl').DataTable();
		var data = table.row($(this).parents('tr')).data();
		
		var Tspec=data.timespec
		var tmpstr="";
		
		$('#EditDraftMsg').val(data.msg_c)
		$('#CreateDraftTime').val(data.submit_t)
		$("#ViewDraftMsgModal").modal();
	});

//刪除草稿夾
	$('#DraftMsgTbl tbody').on('click', 'button[name=deldraftrow]', function () {

		var table = $('#DraftMsgTbl').DataTable();
		var data = table.row($(this).parents('tr')).data();
	    sid = data.seq_id
		var Tspec=data.timespec
		var tmpstr="";
		
		$('#DelDraftMsg').val(data.msg_c)
		$('#DelDraftCreateTime').val(data.submit_t)
		$("#DelDraftMsgModal").data('sid', sid).modal();
	});
	
	$('#deldraftconf').on('click',  function () {

	    //var sId = $("#DelDraftMsgModal").data('sid')
		  sId =sid;            
		//alert( "SID:"+sId);
		 $.ajax({
        type: "POST",
        url:  url+"/secured/DeleteDraftbox",
		dataType: "json",
        data: { 
            s_id: sId, // < note use of 'this' here
            contract: c_id 
        },
        success: function(result) {
			var retcode= result.ret
			
		    
			if(retcode == 'delete_draft_ok')
            alert('成功');
		    else if(retcode == 'delete_draft_no_record')
            alert('找不到資料');
			else
			alert('無法刪除');
		
		
			$("#DelDraftMsgModal").modal('hide');
			document.location.reload();
        },
        error: function(result) {
            alert('error');
        }
    });
	});
	
	
	$('#forward-tosend').on('click', function () {
		//alert( $('#EditMsg').val());
		//var msgForward =$('#EditDraftMsg').val()
		//var url='msgbody.jsp?msg='+msgForward;
		//var en_url=encodeURI(url);
		
		var msgForward =$('#EditDraftMsg').val()
		var urlforward=url+'/indexA.jsp?msg='+msgForward;
		var en_url=encodeURI(urlforward);
		//window.location.href = 'index.html?msg=' + encodeURIComponent(msgForward);
		window.location.href=en_url;
		
	});
});

