
var sid="";


$(document).ready(function () {

	$('#MsgTbl').DataTable({
		"dom": '<lf<t>ip>',
		"processing": true,
		"bJQueryUI": true,
		"ordering": false,
		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "全部"]],
		"sPaginationType": "full_numbers",
		"ajax": {
			"url": "GetInbox",
			"type": "GET",
			"dataType": "json",
			data: {
				contract : c_id 
		      },
		      dataSrc : function(result) {
					
					if (result.ret == 'list_req_no_record') {
						result.data ={};
					}else{
					
					var f_data = $.grep(result.data, function (h) {
						return h.timespec.indexOf('selectOption-routine') > -1 || h.timespec.indexOf('selectOption-future') > -1
					});
					
					if (f_data != "[]") {
						result.data=f_data;
		         }

				
					
		      }
				
				return result.data;
		      
		}},
		"columns": [
				{ "data": "seq_id","visible": false},
				{ "data": "ms_lsts"}, 
				{ "data": "msg_c"  }, 
				{ "data": "submit_t"}

		],

		"columnDefs": [{
				"targets": 4,
				"data": "null",
				"width": "20%",
				"defaultContent": "<button type='button' name='delrow' class='btn btn-danger'  style='margin-right:10px;'>刪除</button> <button type='button' name='viewrowbtn' class='btn btn-primary'  style='margin-right:10px;'   >詳細資料</button>"
			}, {
				"targets": 1,
				"width": "10%",
				render: function (data, type, row) {
					//return data.length > 25 ?					data.substr(0, 22) + '…' :					data;
					return data.split(",").join("<br/>");
				}
			},
			{
				"targets": 2,
				"width": "50%",
				render: function (data, type, row) {
					return data.length > 70 ?	data.substr(0, 70) + '…' :	data;
					//return data.split(",").join("<br/>");
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
    })

//詳細資料
//$('button[name=viewrowbtn]').on('click',  function () {

$(document).on('click', '#MsgTbl button[name=viewrowbtn]', function () {

	var table = $('#MsgTbl').DataTable();
	//var data = table.row( this ).data();
	var data = table.row($(this).parents('tr')).data();

	var Tspec = data.timespec
		var tmpstr = "";
	if (Tspec.indexOf("selectOption-now") >= 0)
		Tspec = "立即傳送"

			if (Tspec.indexOf("selectOption-future") >= 0)
				Tspec = "預約傳送:" + Tspec.split(";time_spec-")[1].substr(0, 17).replace('.', '時') + "分";

			if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-daily;") >= 0)
				Tspec = "每日傳送:" + Tspec.split(";time_spec-")[1].substr(0, 5).replace('.', '時') + "分";

			if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-weekly;") >= 0) {
				tmpstr = Tspec;
				tmpstr = "每周傳送\n"
					tmpstr += "傳送時間:" + Tspec.split(";time_spec-")[1].substr(0, 5).replace('.', '時') + "分\n"
					tmpstr += "傳送週期:" + Tspec.split("weekdays-")[1].replace("Mon", "星期一").replace("Tue", "星期二").replace("Wen", "星期三").replace("Thu", "星期四").replace("Fri", "星期五").replace("Sat", "星期六").replace("Sun", "星期日");
				Tspec = tmpstr;

			}
			if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-monthly;") >= 0) {
				tmpstr = Tspec;
				tmpstr = "每月傳送\n"
					tmpstr += "傳送時間:" + Tspec.split(";time_spec-")[1].substr(0, 5).replace('.', '時') + "分\n"
					 tmpstr+="傳送月曆日:"+Tspec.split("monthdays-")[1].split(";period_spec-")[0]+"\n"
					tmpstr += "起始到期日:" + Tspec.split(";period_spec-")[1].replace('.', '~') + "\n"
					Tspec = tmpstr;
				//$( '#TransTime' ).replaceWith( "<textarea readonly id='TransTime' class='form-control' rows='3' ></textarea>" );
			}

			//selectOption-routine;selectedRoutineOption-weekly;time_spec-04.04;weekdays-Mon,Wen,Fri
			//selectOption-routine;selectedRoutineOption-monthly;time_spec-05.05;monthdays-01,03,05,07,09,11,13,16,18,20,22,30;period_spec-2018/05/16.2018/05/30

			//alert( "門號:"+data.ms_lsts+"\n"+"簡訊內容:"+ data.msg_c);
			$('#SendMsisdn').val(data.ms_lsts);
	$('#EditMsg').val(data.msg_c)
	$('#CreateTime').val(data.submit_t)
	$('#TransTime').val(Tspec)
	$('#TransStatus').val('已傳送')
	$("#ViewMsgModal").modal();
});
//刪除資料
//$('#MsgTbl tbody').on('click', 'button[name=delrow]', function () {
$(document).on('click', '#MsgTbl button[name=delrow]', function () {
	var table = $('#MsgTbl').DataTable();
	//var data = table.row( this ).data();
	var data = table.row($(this).parents('tr')).data();
	sid = data.seq_id
		//alert( "門號:"+data.ms_lsts+"\n"+"簡訊內容:"+ data.msg_c);

		var Tspec = data.timespec
		var tmpstr = "";
	if (Tspec.indexOf("selectOption-now") >= 0)
		Tspec = "立即傳送"

			if (Tspec.indexOf("selectOption-future") >= 0)
				Tspec = "預約傳送:" + Tspec.split(";time_spec-")[1].substr(0, 17).replace('.', '時') + "分";

			if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-daily;") >= 0)
				Tspec = "每日傳送:" + Tspec.split(";time_spec-")[1].substr(0, 5).replace('.', '時') + "分";

			if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-weekly;") >= 0) {
				tmpstr = Tspec;
				tmpstr = "每周傳送\n"
					tmpstr += "傳送時間:" + Tspec.split(";time_spec-")[1].substr(0, 5).replace('.', '時') + "分\n"
					tmpstr += "傳送週期:" + Tspec.split("weekdays-")[1].replace("Mon", "星期一").replace("Tue", "星期二").replace("Wen", "星期三").replace("Thu", "星期四").replace("Fri", "星期五").replace("Sat", "星期六").replace("Sun", "星期日");
				Tspec = tmpstr;

			}
			if (Tspec.indexOf("selectOption-routine;selectedRoutineOption-monthly;") >= 0) {
				tmpstr = Tspec;
				tmpstr = "每月傳送\n"
					tmpstr += "傳送時間:" + Tspec.split(";time_spec-")[1].substr(0, 5).replace('.', '時') + "分\n"
					 tmpstr+="傳送月曆日:"+Tspec.split("monthdays-")[1].split(";period_spec-")[0]+"\n"
					tmpstr += "起始到期日:" + Tspec.split(";period_spec-")[1].replace('.', '~') + "\n"
					Tspec = tmpstr;
				//$( '#TransTime' ).replaceWith( "<textarea readonly id='TransTime' class='form-control' rows='3' ></textarea>" );
			}

			$('#DelMsgModal #DelSendMsisdn').val(data.ms_lsts);
	$('#DelMsgModal #DelEditMsg').val(data.msg_c)
	$('#DelMsgModal #DelCreateTime').val(data.submit_t)
	$('#DelMsgModal #DelTransTime').val(Tspec)
	$('#DelMsgModal #DelTransStatus').val('已傳送')
	$("#DelMsgModal").data('sid', sid).modal();
});

$(document).on('click', '#delconf', function () {

	var sId = $("#DelMsgModal").data('sid')

		//alert( "SID:"+sId);
		$.ajax({
			type: "POST",
			url: "DeleteInbox",
			dataType: "json",
        data: { 
            sid: sId, // < note use of 'this' here
            contract: c_id 
        },
			success: function (result) {
				var retcode = result.ret

					if (retcode == 'delete_req_ok')
						alert('成功');
					else if (retcode == 'delete_req_no_record')
						alert('找不到資料');
					else
						alert('無法刪除');

					$("#DelMsgModal").modal('hide');
				document.location.reload();
			},
			error: function (result) {
				alert('error');
			}
		});
});

$('#forward-tosend').on('click', function () {
	//alert( $('#EditMsg').val());
	var msgForward = $('#EditMsg').val()
		var url = 'msgbody.jsp?msg=' + msgForward;
	var en_url = encodeURI(url);
	//window.location.href = 'index.html?msg=' + encodeURIComponent(msgForward);
	window.location.href = en_url;

});
});