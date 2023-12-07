// initialize Swiper
var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)); 
var url =window.location.protocol+"//"+ window.location.host +context;

var oneMsgLen_E = 160
var MultiMsgLen_E = 153
var oneMsgLen_C = 70
var MultiMsgLen_C = 63

/*
 * window.addEventListener("beforeunload", function (e) { //var
 * confirmationMessage = "\o/";
 * 
 * //(e || window.event).returnValue = confirmationMessage; //Gecko + IE
 * //return confirmationMessage; //Webkit, Safari, Chrome
 * 
 * alert("tab close");
 * 
 * });
 */



/*
 * 
 * new Swiper ('.swiper-container', { loop: true, autoplay: { delay: 3000,
 * disableOnInteraction: false }, pagination: { el: '.swiper-pagination', } })
 */


for(var i=0;i<24;i++){
	$('.hour').append('<option value="'+i+'">'+( i<10?("0"+i):i )+'</option>');
}
for(var i=0;i<60;i++){
	$('.minute').append('<option value="'+i+'">'+( i<10?("0"+i):i )+'</option>');
}

/*
for(var i=1;i<=31;i++){
	$('.days').append(
		`<div class="pretty p-default p-curve p-thick p-smooth">
			<input type="checkbox" name="days[]" value="`+( i<10?("0"+i):i )+`"/>
	        <div class="state p-success-o">
	            <label>`+( i<10?("0"+i):i )+`日</label>
	        </div>
	    </div>`
    );
}*/

for(var i=1;i<=31;i++){
	$('.days').append(
		'<div class="pretty p-default p-curve p-thick p-smooth">' +
		'<input type="checkbox" name="days[]" value="' + ( i<10?("0"+i):i ) +'"/>' + 
		 '<div class="state p-success-o">' +
		 '<label>' +( i<10?("0"+i):i ) +
		 '日</label>' + 
		   '</div>' +
		    '</div>'
    );
}


$.date2str = function(dateObject) {
    var d = new Date(dateObject);
    var day = d.getDate();
    var month = d.getMonth() + 1;
    var year = d.getFullYear();
    if (day < 10) {
        day = "0" + day;
    }
    if (month < 10) {
        month = "0" + month;
    }
    var date = year + "/" + month + "/" +day ;

    return date;
};

$(document).on('ready', function(){
	var param ='msg';
	// var now=decodeURI(test1);
	// var val = decodeURI(document.URL);
    // var url = val.substr(val.indexOf(param))
    // var msg_str=url.replace(param+"=","");
	var msg_str=getURLParameter(param);
   // var msg_str=$.url.attr('message');
    // alert(msg_str);
	$(".text-content textarea").val(msg_str);
   
})

function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,''])[1]
    );
}


function pad (str, max) {
  str = str.toString();
  return str.length < max ? pad("0" + str, max) : str;
}


function sendtoserver() {
    // var p_ids = document.forms[0].elements["phones"];
	// var list = document.getElementById("phones").value;
	// var msgString= document.getElementsByTagName('textarea')[0].value
	
	// var ss = '<%= Session["profile.mdn"].ToString() %>';
    // alert(ss);
	
	var futureDate =$('#oncedt').bootstrapDP("getDate");
	var routine_stDate = $('#r_stDate').bootstrapDP("getDate");
	var routine_enddate = $('#r_edDate').bootstrapDP("getDate"); 
	var rt_stDate='';
	var rt_endDate='';
	var sd1 ="";
	
	
		
	if(routine_stDate != '' &&  routine_stDate!= null )
		rt_stDate =$.date2str(routine_stDate);
		
	if(routine_enddate != '' &&  routine_enddate!= null)
		rt_endDate =$.date2str(routine_enddate);
	
	if(futureDate != '' &&  futureDate!= null)
	   sd1 =$.date2str(futureDate);
	
		
		
	// 簡訊內容
	var msgString= $(".text-content textarea").val()
	
	// 1=立即發送 2=單次發送 3=周期預約發送
	var selectedOption = $("input:radio[name=sendtime]:checked").val()
	
	// 單次發送
	//var sd1 = $('.sendonce .datepicker_bt').val();
	
	var sh = $('.sendonce .hour').val()
	var sm = $('.sendonce .minute').val()
	
	// 周期
	// 每天
	var selectedRoutineOption = $("input:radio[name=sendroutine_opt]:checked").val()
	var rsh = $('.sendroutine .day .hour').val()
	var rsm = $('.sendroutine .day .minute').val()
	
	
    
   
   // 每周
   var rwsh = $('.sendroutine .week .hour').val()
   var rwsm = $('.sendroutine .week .minute').val()
   
   var weekdays="";
   var ArrayWeek = [];
   $('.week input[type=checkbox]').each(function() 
   {
	   
	  
       if (this.checked) 
       {
          
		  // weekdays += $(this).parent().find('label').text() + ",";
		  weekdays = $(this).val();
		  ArrayWeek.push(weekdays);
       }
   });
	
	
	// 每月
	var rmsh = $('.sendroutine .month .hour').val()
   var rmsm = $('.sendroutine .month .minute').val()
   
   
   
   //起始到期
	//var start_day = $('.startDate').val().trim()
	//var end_day=$('.endDate').val().trim()
	
   var start_day =rt_stDate
   var end_day=rt_endDate
	
	
   
   
	var period_spec=start_day+"."+end_day;
	var days="";
	var Arrayday = [];
	
   $('.days input[type=checkbox]').each(function() 
   {
	   
       if (this.checked) 
       {
		  days = $(this).val().trim();
		  Arrayday.push(days);
       }
   });
	
	
	// 電話號碼
	// var msisdns=$("#phones").map(function() { return $(this).text();}).get();
    var ms_array =$("input[name=msisdn]")
   
   
    var tags = $("#phones").tagit("assignedTags");
    var tagsJSON = JSON.stringify(tags);
   
	var ms_len =$("input[name=msisdn]").length
	
	var send_msisdn=""
	// var m_len= document.getElementsByName('msisdn').length
	
	var ArrayMsisdn = [];
	
	
	for(i=0;i< ms_len ; i++)
    {
    // send_msisdn+= document.getElementsByName('msisdn')[i].value+ ",";
	send_msisdn+=$("input[name=msisdn]")[i].value+ ",";
	// ArrayMsisdn.push(send_msisdn);
    }
	
	var jsonObj ={"msisdns":ArrayMsisdn}
	var ArrayMsJson =JSON.stringify(jsonObj);
	
	var time_spec ="";
	
	var timeObj="";
	
	
	
	if(selectedOption =="1"){
		
		timeObj="selectOption-now"
	}
	else if (selectedOption =="2")
	{
		
		timeObj="selectOption-future"
		timeObj+=";time_spec-"+sd1+"," +pad(sh,2)+"."+pad(sm,2)
		
		// time_spec = sd1+"-" +pad(sh,2)+":"+pad(sm,2)
		// timeObj["selectOption"]="2"
		// timeObj["time_spec"]=time_spec
		
	}else if (selectedOption =="3")
	{
		// timeObj["selectOption"]="3"
		timeObj="selectOption-routine"
		if(selectedRoutineOption=="1"){
			timeObj+=";selectedRoutineOption-daily"
			timeObj+=";time_spec-"+pad(rsh,2)+"."+pad(rsm,2)
			timeObj+=";period_spec-"+period_spec
		   // time_spec = pad(rsh,2)+":"+pad(rsm,2)
		   // timeObj["selectedRoutineOption"]="1"
		   // timeObj["time_spec"]=time_spec
		}
	    else if(selectedRoutineOption=="2"){
			
			
			timeObj+=";selectedRoutineOption-weekly"
			timeObj+=";time_spec-"+pad(rwsh,2)+"." + pad(rwsm,2)
			timeObj+=";weekdays-"+ArrayWeek
			timeObj+=";period_spec-"+period_spec
		    // time_spec =pad(rwsh,2)+";" + pad(rwsm,2)
			// timeObj["selectedRoutineOption"]="2"
			// timeObj["time_spec"]=time_spec
			// timeObj["weekdays"]=ArrayWeek
		}
		else if(selectedRoutineOption=="3"){
			 
			 timeObj+=";selectedRoutineOption-monthly"
			 timeObj+=";time_spec-"+pad(rmsh,2)+"."+ pad(rmsm,2)
			 timeObj+=";monthdays-"+Arrayday
			 timeObj+=";period_spec-"+period_spec
			 
			 
		     // time_spec =pad(rmsh,2)+":"+ pad(rmsm,2)
			 // var period_spec=start_day+","+end_day
			// timeObj["selectedRoutineOption"]="3"
			// timeObj["time_spec"]=time_spec
			 // timeObj["day_spec"]=Arrayday
			 // timeObj["period_spec"]=period_spec
		}
	}
	
	// var timeJson =JSON.stringify(timeObj);
	
	
	$.ajax({
      type: "GET",
      url: url+ "/secured/SmsSend",
      
      
      data: {
		Anumber :a_num, 
        Bnumber: send_msisdn,
		contract : c_id ,
        msg: msgString,
		timespec : timeObj
      },
      dataType: "json",
      success: function(result) {
    	  if(result.ret == "ins_req_ok")
        	  alert("發送成功");
        	  else 
        		 // alert(data.msg+data.check);
        		  alert(result.ret);
      },
	  
	  error: function (jqXHR, exception) {
        var msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        console.log(msg);
		alert(msg);
    }
	  
	  
    });
	
	
	
	

}


// Add by David
$('#savedraft').on('click',  function () {

	
	var msgString= $(".text-content textarea").val()
	// alert( "門號:"+data.ms_lsts+"\n"+"簡訊內容:"+ data.msg_c);
	$('#DraftMsg').val(msgString);
	$("#DraftModal").modal();
});

// Add by David
$('#savt_to_draft ').on('click',  function () {

   var msg = $('#DraftMsg').val();
   
	// alert( "SID:"+sId);
	 $.ajax({
    type: "POST",
    url: url+"/secured/SaveDraftBox",
	dataType: "json",
    data: { 
	    draftmsg: msg,
        msisdn: a_num, 
        contract: c_id  	
    },
    success: function(result) {
		var retcode= result.ret
		
	    
		if(retcode == 'ins_draft_ok')
        alert('成功');
	    else if(retcode == 'ins_draft_input_error')
        alert('資料不正確');
		else
		alert('無法儲存');
	
	    
		$("#DraftModal").modal('hide');
		
		// document.location.reload();
		 document.location.assign("indexA.jsp");
    },
    error: function(result) {
        alert('error');
    }
});
});


$(".text-content textarea").keyup(function(){
	var msgString= $(".text-content textarea").val();
	showTextNum(msgString);
	
	// var count = $(this).val().length;
	// $('.text-content .charnum').text(count);
	// $('.text-content .amount').text( Math.ceil(count/70) );
});






function showTextNum(val) {
	var len = 0;
	var splitNum = 0;
	// var val = msgString;
	if (val.length != 0) {
		len = val.length;
	}
	if (isChinese(val)) {
		if (len <= oneMsgLen_C && len > 0) {
			splitNum = 1;
		} else {
			splitNum = Math.ceil(len/MultiMsgLen_C);
		}
	} else {
		if (len <= oneMsgLen_E && len > 0) {
			splitNum = 1;
		} else {
			splitNum = Math.ceil(len/MultiMsgLen_E);
		}
	}
	
	if (splitNum > 12) { 
		/*
		 var splitLimit = 12 ; 
		var splitCh;
		var splitEn;
		if (splitLimit == 1) {
			splitCh = 70;
			splitEn = 160;
		} else {
			splitCh = 63;
			splitEn = 153;
		}
		if (isChinese(val)) { 
			var maxLen = splitLimit * splitCh;
			val = val.substring(0, maxLen);
		} else {
			var maxLen = splitLimit * splitEn;
			val = val.substring(0, maxLen);
		}
		$(this).val(val);
		$(this).focus();
		*/
		alert('超過一般訊息分則上限 ');
		//splitNum = splitLimit;
		//len = $(this).val().length;
	}
	
	$('.text-content .charnum').text(len);
	$('.text-content .amount').text( splitNum );
}

function isChinese(value) {
    if (value.length == 0) {
    	return false;
    }
    for(n=0; n < value.length; n++) {
        var c=value.charCodeAt(n);
        if (c>127) {
        	return true;
        }
    }
	return false;
}

//initialize tag-it
var eventTags = $("#phones").tagit({
	
	//add by David	
	fieldName: "msisdn",
	singleField: true,
	singleFieldDelimiter : ",",
	
    beforeTagAdded: function(event, ui) {
    	// console.log(eventTags.tagit('tagLabel', ui.tag))
        if( /002[0-9]{5,17}$|^09[0-9]{8}$/.test(eventTags.tagit('tagLabel', ui.tag)  ) == false ){
        	$('.tagit-new input').val("");
        	alert('手機格式是錯誤');
        	return false;
        }
        if( $('.phonenum .tagit-choice').length == 100 ){
        	$('.tagit-new input').val("");
        	alert('最多同時發送100個門號');
        	return false;
        }
    },
    afterTagAdded: function(event, ui) {
    	$('.phonenum .num').text( $('.phonenum .tagit-choice').length );
    },
    afterTagRemoved: function(event, ui) {
    	$('.phonenum .num').text( $('.phonenum .tagit-choice').length );
    },
    onTagExists: function(event, ui) {
    	$('.tagit-new input').val("");
    	alert(eventTags.tagit('tagLabel', ui.existingTag)+'已加入');
    	return false;
    }
});

// 顯示通訊錄
function openPhoneBook(){
	// ajax

	$('#phoneBook').modal('show');
}

// click通訊錄確認btn
/*
 * function bookConfirm(){ for(var i=0;i<5;i++){ // 模擬匯入電話號碼
 * $('#phones').tagit('createTag', '090000000'+i); //
 * $("#phones").tagit("assignedTags"); } $('#phoneBook').modal('hide'); }
 */

// click通訊錄確認btn
function bookConfirm(){
	if( $('#phoneBook .nav-tabs .groups').hasClass('active') ){
		console.log('groups');
		$('#phoneBook #tab1 td input[type=checkbox]:checked').each(function(){
			var groupName = $(this).val();
			getPhoneNumByGroupName(groupName);
		});
	}else if( $('#phoneBook .nav-tabs .all').hasClass('active') ){
		console.log('all');
		$('#phoneBook #tab2 td input[type=checkbox]:checked').each(function(){
			$('#phones').tagit('createTag', $(this).val());
		});
	}else{
		console.log('????');
	}
	$('#phoneBook').modal('hide');
}

function getPhoneNumByGroupName(groupName){
	$('#phoneBook #tab2 tr').each(function(){
		// console.log( $(this).find('td').eq(1).text() );
		if($(this).find('td').eq(1).text()==groupName){
			$('#phones').tagit('createTag', $(this).find('td').eq(3).text());
		}
	});
}

// initialize datepicker
/*
 $(".datepicker").datepicker({
	changeMonth: true,
	changeYear: true,
	dateFormat: "yy/mm/dd",
	showOn: "both",
	buttonText: '<span class="glyphicon
	glyphicon-calendar"></span>',
	minDate: 0,
	orientation: "top"
});
$(function () {
	$(".datepicker").datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy/mm/dd",
		showOn: "both",
		buttonText: '<span
		class="glyphicon glyphicon-calendar"></span>',
		minDate: 0,
		orientation:
		"top"
	})
});

 */


$(function(){
	if (!$.fn.bootstrapDP && $.fn.datepicker && $.fn.datepicker.noConflict) {
      var datepicker = $.fn.datepicker.noConflict();
      $.fn.bootstrapDP = datepicker;
     }
		//$('.dp-jqueryui').datepicker();
		// $('.dp-bootstrap').bootstrapDP();
		
	$.fn.bootstrapDP.dates['zh-TW'] = {
			  days: ["星期日", "星期一", "星期二",  "星期三", "星期四", "星期五", "星期六"], 
			  daysShort: ["週日", "週一", "週二", "週三", "週四", "週五",  "週六"], 
			  daysMin: ["日", "一", "二", "三", "四", "五", "六"],
			  months: ["一月", "二月",  "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			  monthsShort:  ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            today: "今天", 
            format: "yyyy年mm月dd日", 
            weekStart: 1, 
            clear: "清除" }; 
	
		
		$('.datepicker_bt').bootstrapDP({
		    format: "yyyy/mm/dd",
		    autoclose: true,
		    startDate: "today",
		    clearBtn: true,
		    calendarWeeks: true,
		    todayHighlight: true,
		    language: 'zh-TW',
		    pickerPosition: "top-left",
		    showButtonPanel: true
		});

		
	
});
   
			//$('.dp-jqueryui').datepicker();
			//$('.dp-bootstrap').bootstrapDP();



  
 





// $('.sendtime .ui-datepicker-trigger, .sendtime input[type=checkbox],
// .sendroutine input[type=radio],.s-end-date').attr("disabled", true);
// $(".routineperiod").css({ opacity: 0.5 });

$('.sendtime .datepicker_bt').attr("disabled", true);
$('.routine-type input[type=radio]').attr("disabled", true);
$('.sendtime input[type=checkbox], .routine-type input[type=radio]').attr("disabled", true);

// $(".routineperiod").css({ opacity: 0.5 });


function changeSendTime(type){
	// $('.sendtime input[type=text], .sendtime .ui-datepicker-trigger,
	// .sendtime input[type=checkbox], .sendtime select, .sendroutine
	// input[type=radio]').attr("disabled", true);
	 $('.sendroutine .routine-type input[type=radio]').attr("disabled", true);
	// $(".routineperiod").css({ opacity: 0.5 });
	
	$('.sendtime input[type=text],   .sendtime select').attr("disabled", true);

	if(type==2){
		$('.sendtime .sendonce input[type=text],  .sendtime .sendonce select').removeAttr("disabled");
	}else if(type==3){
		$('.sendtime .sendroutine input[type=radio]').removeAttr('disabled');
		$(".routineperiod").css({ opacity: 1 });
	}
}
function changeRoutineType(type){
	$('.sendroutine select, .sendroutine input[type=checkbox]').attr("disabled", true);
	
	if(type==1){
		$('.sendroutine .day select, .routineperiod .datepicker_bt .form-control').removeAttr('disabled');
	}else if(type==2){
		$('.sendroutine .week select, .sendroutine .week input[type=checkbox], .routineperiod .datepicker_bt .form-control').removeAttr('disabled');
	}else{
		$('.sendroutine .month select, .sendroutine .month input[type=checkbox], .routineperiod .datepicker_bt .form-control').removeAttr('disabled');
		
	}
}

$(document).on('click', '.selectAll', function(){
	if( !$(this).is(":checked") ){
		$(this).closest('table').find('input[type=checkbox]').prop('checked', false);
	}else{
		$(this).closest('table').find('input[type=checkbox]').prop('checked', true);
	}
});
$(document).on('click', 'table input[type=checkbox]', function(){
	if( $(this).closest('table').find('td').find('input[type=checkbox]:checked').length == 0 ){
		$(this).closest('table').find('.selectAll').prop('checked', false);
	}
	if(  $(this).closest('table').find('td').find('input[type=checkbox]:checked').length == $(this).closest('table').find('tr').length-1 ){
		$(this).closest('table').find('.selectAll').prop('checked', true);
	}
});

function paddingLeft(str,lenght){
	if(str.length >= lenght)
	return str;
	else
	return paddingLeft("0" +str,lenght);
}

// submit前 Check

function sendSmsJs(){
var answer = confirm("確定傳送?")
if (answer) {
	sendSms();
}
else {
    return false;
}

}


function cheklen(val) {
	var len = 0;
	var splitNum = 0;
	// var val = msgString;
	if (val.length != 0) {
		len = val.length;
	}
	if (isChinese(val)) {
		if (len <= oneMsgLen_C && len > 0) {
			splitNum = 1;
		} else {
			splitNum = Math.ceil(len/MultiMsgLen_C);
		}
	} else {
		if (len <= oneMsgLen_E && len > 0) {
			splitNum = 1;
		} else {
			splitNum = Math.ceil(len/MultiMsgLen_E);
		}
	}
	
	
	return splitNum;
	
}


function sendSms(){
	
	// 預約發送
	
	
	
	if(c_id == null || c_id ==''){
		alert('請登入');
		return false;
	}
		
	
	if(cheklen($(".text-content textarea").val()) > 12){
		alert('超過簡訊上限');
		return false;
	}
	
	
	var futureDate =$('#oncedt').bootstrapDP("getDate");
	var routine_stDate = $('#r_stDate').bootstrapDP("getDate");
	var routine_enddate = $('#r_edDate').bootstrapDP("getDate"); 
	
	
	//check 預約傳送
	if($('.sendtime input[name=sendtime]:checked').val()==2){
		if(futureDate==""){
			alert('請輸入發送日期');
			return false;
		}
		
		// var sd1 = $('.sendonce .datepicker').val();
		
		//var futureDate = new Date($('.sendonce .datepicker_bt').val())
		
		
		
		var future_year = futureDate.getFullYear();
		var future_month = futureDate.getMonth();
		var future_day = futureDate.getDate();
		var future_hour = $('.sendonce .hour').val()
		
		futureDate.setHours(future_hour)
		
		var future_min = $('.sendonce .minute').val()
		
		futureDate.setMinutes(future_min)
		
		var future = future_year +","+future_month+","+future_day+","+future_hour+","+future_min
		
		var today = new Date();
		var now_year = today.getFullYear();
		var now_month = today.getMonth();
		var now_day = today.getDate();
		var now_hour = today.getHours();
		var now_min = today.getMinutes();
		
		var now = now_year +","+now_month+","+now_day+","+now_hour+","+now_min
		console.log("future="+future+","+"now="+now);
		
		
		if(futureDate.getTime() < today.getTime()){
			alert('發送時間必需大於現在時間');
		return false;
	}
		
		 // var today = new Date();
		 var numberOfMonths = 3
		 var today_add_3_month = new Date(today.setMonth(today.getMonth()+numberOfMonths))
		 var today_add_3_month_time = today_add_3_month.getTime();
		 
		 if( futureDate.getTime()>today_add_3_month_time ){
				alert('發送日期不得大於三個月');
				return false;
			}
		
	}
	// check 周期傳送
	else if( $('.sendtime input[name=sendtime]:checked').val()==3 ){
		if($('.sendroutine input[name=sendroutine_opt]:checked').val()==2){
			if($('.week input[type=checkbox]:checked').length==0){
				alert('請選擇發送周期天');
				return false;
			}
		}
		else if($('.month input[name=sendroutine_opt]:checked').val()==3){
			if($('.month input[type=checkbox]:checked').length==0){
				alert('請選擇每月發送日');
				return false;
			}
			
		}
		
	/*
		if($('.startDate').val().trim()==""){
			alert('請輸入起始日期');
			return false;
		}
		if($('.endDate').val().trim()==""){
			alert('請輸入結束日期');
			return false;
		}
		
		
		
		var startDate = new Date($('.startDate').val()).getTime();
		var endDate = new Date($('.endDate').val()).getTime();
		*/
		
		  
		   
		
		
		 if(routine_stDate=="" || routine_stDate==null){
				alert('請輸入起始日期');
				return false;
			}
			if(routine_enddate=="" || routine_enddate== null){
				alert('請輸入結束日期');
				return false;
			}
		 
		var startDate = routine_stDate.getTime();
		var endDate = routine_enddate.getTime();
		
		 var today = new Date();
		 var numberOfMonths = 3
		 var today_add_3_month = new Date(today.setMonth(today.getMonth()+numberOfMonths))
		 var today_add_3_month_time = today_add_3_month.getTime();
		 
		
		console.log(startDate, endDate)
		if( startDate>endDate ){
			alert('起始日期不得大於結束日期');
			return false;
		}
		
		
		if( endDate>today_add_3_month_time ){
			alert('結束日期不得大於三個月');
			return false;
		}
		
	}
	
	
	sendtoserver();
	
}