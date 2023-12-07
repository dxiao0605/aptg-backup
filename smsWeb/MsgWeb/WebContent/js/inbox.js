
var sid="";
var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)); 
var url =window.location.protocol+"//"+ window.location.host +context;
//alert(url) 
//var ctx = "${pageContext.request.contextPath}"
	
$(document).ready(function () {
	$('#MsgTbl').DataTable({
		"dom": '<lf<t>ip>',
		"processing": true,
		"bJQueryUI": true,
		"ordering": false,
		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "全部"]],
		"sPaginationType": "full_numbers",
		"ajax": {
			"url": url+"/secured/GetInbox",
			"type": "GET",
			"dataType": "json",
			data: {
				contract : c_id 
		      },
		      dataSrc : function(result) {
					
					if (result.ret == 'list_req_no_record') {
						result.data ={};
					}
					
					return result.data;
		      }
		      
		},
		"columns": [
				{ "data": "seq_id","visible": false},
				{ "data": "ms_lsts"}, 
				{ "data": "msg_c"  }, 
				{ "data": "submit_t"},
				{ "data": "timespec"}

		],

		"columnDefs": [
			 {
				"targets": 1,
				//"width": "10%",
				render: function (data, type, row) {
					//return data.length > 25 ?					data.substr(0, 22) + '…' :					data;
					return data.split(",").join("<br/>");
				}
			},
			{
				"targets": 2,
				//"width": "50%",
				render: function (data, type, row) {
					return data.length > 70 ?	data.substr(0, 70) + '…' :	data;
					//return data.split(",").join("<br/>");
				}
			},
			 {
				"targets": 4,
				"width": "30%",
				render: function (data, type, row) {
					//return data.length > 25 ?					data.substr(0, 22) + '…' :					data;
					return getSendTime(data).split("\n").join("<br/>");
				}
			},
			{
				"targets": 5,
				"data": "null",
				//"width": "20%",
				"defaultContent": "<button type='button' name='delrow' class='btn btn-danger btn-md'  style='margin-right:10px;'>刪除</button> <button type='button' name='viewrowbtn' class='btn btn-primary btn-md'  style='margin-right:10px;'   >詳細資料</button>"
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

	function getSendTime(Tspec) {
		var tmpstr="";
		if (Tspec.indexOf("selectOption-now") >= 0)
			Tspec="立即傳送"
		
		if (Tspec.indexOf("selectOption-future") >= 0)
			Tspec="預約傳送:"+Tspec.split(";time_spec-")[1].substr(0,17).replace('.','時')+"分";
		
		if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-daily;") >= 0){
			tmpstr =Tspec;
		    tmpstr="每日傳送:"+Tspec.split(";time_spec-")[1].substr(0,5).replace('.','時')+"分\n";
		    tmpstr+="起始/到期日:"+Tspec.split(";period_spec-")[1].replace('.','~')+"\n"
		    Tspec=tmpstr;
		}
		if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-weekly;") >= 0){
			tmpstr =Tspec;
            tmpstr="每周傳送\n"
		    tmpstr+="傳送時間:"+Tspec.split(";time_spec-")[1].substr(0,5).replace('.','時')+"分\n"
	        tmpstr+="傳送週期:"+Tspec.split("weekdays-")[1].split(";period_spec-")[0].replace("Mon", "星期一").replace("Tue", "星期二").replace("Wed", "星期三").replace("Thu", "星期四").replace("Fri", "星期五").replace("Sat", "星期六").replace("Sun", "星期日")+"\n";
            tmpstr+="起始/到期日:"+Tspec.split(";period_spec-")[1].replace('.','~')+"\n"
	        Tspec=tmpstr;
	   
		}
		if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-monthly;") >= 0){
			tmpstr =Tspec;
		   tmpstr="每月傳送\n"
		   tmpstr+="傳送時間:"+Tspec.split(";time_spec-")[1].substr(0,5).replace('.','時')+"分\n"
		   tmpstr+="傳送月曆日:"+Tspec.split("monthdays-")[1].split(";period_spec-")[0]+"\n"
		   tmpstr+="起始/到期日:"+Tspec.split(";period_spec-")[1].replace('.','~')+"\n"
		   Tspec=tmpstr;
		   //$( '#TransTime' ).replaceWith( "<textarea readonly id='TransTime' class='form-control' rows='3' ></textarea>" );
		}
	    return Tspec;
	}
	
	$('#MsgTbl tbody').on('click', 'button[name=viewrowbtn]', function () {

		var table = $('#MsgTbl').DataTable();
		//var data = table.row( this ).data();
		var data = table.row($(this).parents('tr')).data();
		
		var TspecRet=getSendTime(data.timespec);
		
		
		
		//selectOption-routine;selectedRoutineOption-weekly;time_spec-04.04;weekdays-Mon,Wen,Fri
		//selectOption-routine;selectedRoutineOption-monthly;time_spec-05.05;monthdays-01,03,05,07,09,11,13,16,18,20,22,30;period_spec-2018/05/16.2018/05/30
		
		//alert( "門號:"+data.ms_lsts+"\n"+"簡訊內容:"+ data.msg_c);
		$('#SendMsisdn').val(data.ms_lsts);
		$('#EditMsg').val(data.msg_c)
		$('#CreateTime').val(data.submit_t)
		$('#TransTime').val(TspecRet)
		$('#TransStatus').val('已傳送')
		$('#ViewMsgModal').modal('show');
		
		
		
	});
	
	$('.dataTables_filterinput[type="search"]').css( {'width':'350px','display':'inline-block'} );
//刪除資料
	$('#MsgTbl tbody').on('click', 'button[name=delrow]', function () {

		var table = $('#MsgTbl').DataTable();
		//var data = table.row( this ).data();
		var data = table.row($(this).parents('tr')).data();
		 sid = data.seq_id
		//alert( "門號:"+data.ms_lsts+"\n"+"簡訊內容:"+ data.msg_c);
		
		//var Tspec=data.timespec
		var TspecRet=getSendTime(data.timespec);
		
		$('#DelMsgModal #DelSendMsisdn').val(data.ms_lsts);
		$('#DelMsgModal #DelEditMsg').val(data.msg_c)
		$('#DelMsgModal #DelCreateTime').val(data.submit_t)
		$('#DelMsgModal #DelTransTime').val(TspecRet)
		$('#DelMsgModal #DelTransStatus').val('已傳送')
		$("#DelMsgModal").modal();
	});

	
	$('#delconf ').on('click',  function () {

	   // var sId = $("#DelMsgModal").data('sid')
		sId =sid;
		//alert( "SID:"+sId);
		 $.ajax({
        type: "POST",
        url: url+"/secured/DeleteInbox",
		dataType: "json",
        data: { 
            sid: sId, // < note use of 'this' here
            contract: c_id 
        },
        success: function(result) {
			var retcode= result.ret
			
		    
			if(retcode == 'delete_req_ok')
            alert('成功');
		    else if(retcode == 'delete_req_no_record')
            alert('找不到資料');
			else
			alert('無法刪除');
		
		
			$("#DelMsgModal").modal('hide');
			document.location.reload();
        },
        error: function(result) {
            alert('error');
        }
    });
	});
	
	
	$('#forward-tosend').on('click', function () {
		//alert( $('#EditMsg').val());
		var msgForward =$('#EditMsg').val()
		var urlforward=url+'/indexA.jsp?msg='+msgForward;
		var en_url=encodeURI(urlforward);
		//window.location.href = 'index.html?msg=' + encodeURIComponent(msgForward);
		window.location.href=en_url;
		
	});

});
