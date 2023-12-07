
var location_data;

var locations = [];
var floor = [[]];
var office = [[[]]];

var device = [];
var splitstr = "&&"

	$(document).ready(function () {

		
		
		
		
		// $("#scheduleroot").click();
		
		
		//ShowLoginInfo();
		//CallLocationInfo();

		//ShowWin();

		

	});

//some code

function aftermenu(){
	
	 CallLocationInfoAPI();
}



function ShowSch() {
	
	$('.input-group.date').datepicker({
	      format: 'yyyy-mm-dd',
		  autoclose: true,
	    startDate: "today",
	    clearBtn: true,
	    todayHighlight: true,
		  language: 'zh-TW'
	    });
	
	 
	
	$("#days .day").remove();

	var i;

	for (i = 1; i < 32; i++) {
		$("#days").append(addHtmlday(i));

	}

	function addHtmlday(val) {
		
		
		
		function pad(num, n) {
			var len = num.toString().length;
			while(len < n) {
			num = "0" +  num;
			len++;
			}
			return num;
		}
		

		var html = "";
		
		html += "<div class='pretty p-default day mt-4'>"
		html += "<input type='checkbox' name='month'  value ='"+val+"'/>"
		html += "<div class='state p-success'>"
		html += "<label>" + pad(val,2) + "日</label>"
		html += "</div>"
		html += "</div>"

		return html

	}
	
	

	AddTimeSelect()
	SendSchClick()
	SchTypeChange()
	
	
}



function SchTypeChange()
{
$('.schset *').prop("disabled",true);
$('#routineset *').prop("disabled",true);


$('input[name=schType]').on('change', function() {
	
	var sch_sel=$('input[name=schType]:checked').val()
	if(sch_sel =='9') {  
	
		$('#routineset *').prop("disabled",false);
	}
	else{
	
		$('#routineset *').prop("disabled",true);
		
	}
	});
}

function SendSchClick()
{
    //    button click
	
	
	$(".pretty label").css("font-weight", "bold");
	$("input[name='schType'][value='0']").prop("checked", true);
	
	$(".dp_input").css({ "font-weight": "bold", 'color' : 'black'});
	
    $("#SendSch").button().click(function(){
    	
    	var deviceid= $('#select_location_L2').val();
    	var attkey= $('#select_sreen').val();
    	var s1 =$('input[name=schType]:checked').val()
    	var s2 =$('input[name=routine]:checked').val()
    	var one_date =$('#dp_onetime').val();
    	
    	var date1_hh =$('#onetime_hour').val();
    	var date1_mm =$('#onetime_min').val();
    	
    	
    	var daily_hh =$('#daily_hour').val();
    	var daily_mm =$('#daily_min').val();
    	
    	
    	var weekly_hh =$('#weekly_hour').val();
    	var weekly_mm =$('#weekly_min').val();
    	
    	var weekday =$('input[name=week]:checked').map(function() {return this.value;}).get().join(',')
    	
    	
    	var monthly_hh =$('#monthly_hour').val();
    	var monthly_mm =$('#monthly_min').val();
    	
    	var monthday =$('input[name=month]:checked').map(function() {return this.value;}).get().join(',')
    	
    	var s_date =$('#dp_routine_st').val();
    	
    	var e_date =$('#dp_routine_end').val();
    	
    	var sel_type="";
    	
    	if(deviceid.substring(0,3) =='Led'){
    		  alert("請選擇iToch")
              return;  
    	}
    	
    	if(attkey.substring(0,3) =='Led'){
  		  alert("請選擇情境")
            return;  
  	}
    	
    	
    	if(s1 =='9'){
    	  sel_type =s2
          if(sel_type === undefined){
        	  alert("請選擇執行周期")
              return;  
          }
    	  
    	  if(s_date.length < 1 || e_date.length < 1)
    	  {
        	  alert("請輸入起始到期日")
              return;  
          }
    	  
    	  if(s_date >= e_date)
    	  {
        	  alert("到期日需大於起始日")
              return;  
          }
        	  
    	} 
    	  else
    	  sel_type =s1	  
    	 var send_time="";
    	 
    	 // (立即執行)
    	 if(sel_type =='0'){
   		  send_time=""; 
   		  s_date="";
   		  e_date="";
    	  weekday='';
    	  monthday='';
   	     }
    	 
    	 
    	 //(單次執行)
    	  if(sel_type =='1'){  
    		 send_time=one_date+" "+date1_hh+":"+date1_mm+":00"
    		 s_date="";
    		 e_date="";
    		 weekday='';
       	     monthday='';
       	     
       	     if(one_date.length < 1){
       	    	 alert("請輸入日期")
                return;       	     
       	     }
       	     
    	  }
    	  
    	 
    	  
    	  //(每天執行)
    	  if(sel_type =='2'){
    		  send_time= daily_hh+":"+daily_mm+":00"
    		  weekday=''
    		  monthday='';	  
    	  }
    	  
    	//(每周執行)
    	  if(sel_type =='3'){
    		  send_time= weekly_hh+":"+weekly_mm+":00"
    		  monthday=''
    			  
    			  if(weekday.length < 1){
    	       	    	 alert("請選擇周期日")
    	                return;       	     
    	       	     }  
    			  
    		  	  
    	  }
    	  
    	  
    	//(每周執行)
    	  if(sel_type =='4'){
    		  send_time= monthly_hh+":"+monthly_mm+":00"
    		  weekday=''
    			  
    			  if(monthday.length < 1){
 	       	    	 alert("請選擇月曆日")
 	                return;       	     
 	       	     }  
    		  	  
    	  }
    	
        //alert(deviceid+","+attkey+","+s1+","+s2+","+date1+","+date1_hh+","+date1_mm+","+daily_hh+","+daily_mm+","+weekly_hh+","+weekly_mm+","+weekday+","+monthday+","+date2+","+date3);
        
        
        
        var  setDevSchObj ={};
    	
        setDevSchObj['device_id']=deviceid
        setDevSchObj['att_key']=attkey
        setDevSchObj['type']=sel_type
        setDevSchObj['time']=send_time
        setDevSchObj['startdate']=s_date
        setDevSchObj['enddate']=e_date
        setDevSchObj['weekday']=weekday
        setDevSchObj['monthday']=monthday
        
        CallsetSchedule(setDevSchObj); 
        
    });    
}





function onShowSelect(thisid) {
	
	
	var this_id = "#" +thisid
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();
	var chgselect;
	//var thisid = this.id
	
	
	if (thisid=='select_location_L1'){
		chgselect ='#select_location_L2'
		//$("#select_location_L2 option[value^='Led' ]").remove()	
		//$("#select_location_L3 option[value^='Led' ]").remove()
		//$("#select_location_L4 option[value^=^Led' ]").remove()
		//$("#select_sreen option[value^='Led' ]").remove()	
		
			
			$("#select_location_L2 option").not('[value^="Led" ]').remove()
		    $("#select_location_L3 option").not('[value^="Led" ]').remove()
		    $("#select_location_L4 option").not('[value^="Led" ]').remove()
		    $("#select_sreen option").not('[value^="Led" ]').remove()
		   
			
	}		
	if (thisid=='select_location_L2'){
	 	chgselect ='#select_location_L3'		
	
	 		 $("#select_location_L3 option").not('[value^="Led" ]').remove()
			    $("#select_location_L4 option").not('[value^="Led" ]').remove()
			    $("#select_sreen option").not('[value^="Led" ]').remove()
			   
	}
	//alert(sel_text + ',' + sel_val+ ',' + thisid)

	//var i = 0;
	//$("#select_location_L2 option").remove();
	  
	  
	 // $(".ct option[value='X']").remove();
	//$("#select_location_L2").append($("<option></option>").attr("value", "-1").text("-樓層-"));
	//$("#select_location_L3").append($("<option></option>").attr("value", "-1").text("-辦公室-"));


	 		
 		//$("#select_location_L1").append($("<option></option>").attr("value", "Los-1").text("-地點-"));
		//$("#select_location_L2").append($("<option></option>").attr("value", "Los-2").text("-樓層-"));
		//$("#select_location_L3").append($("<option></option>").attr("value", "Los-3").text("-辦公室-"));
		
	 		
	var loc_set = jsonPath(LocInfo, "$..[?(@.location_id == '" + sel_val.substring(3) + "')].next[*].location_id")

	
	if (loc_set.length > 0)
	loc_set.forEach(function(itemid, index) {

		var location_name = FindLocationName(itemid);
				
		itemid = 'Los' + itemid;
		
		//console.log(itemid + ',' + location_name)

		$(chgselect).append($("<option></option>").attr("value", itemid).text(location_name));

	});
	


}

function AddTimeSelect() {
	
	
	
	function pad(num, n) {
		var len = num.toString().length;
		while(len < n) {
		num = "0" +  num;
		len++;
		}
		return num;
	}
	

	for (i = 0; i < 24; i++) {
		
		var val =pad(i,2)
		
		$(".hourset select").append($("<option></option>").attr("value",val ).text(val));
		
		}
	
	
     for (i = 0; i < 60; i++) {
		
		var val =pad(i,2)
		
		$(".minset select").append($("<option></option>").attr("value",val ).text(val));
		
		}

}

function ShowLoginInfo() {

	var accname = getSearchParams('accname'); ;
	$("#accname").text(accname);

}


function onShowiTouch(thisid){
	
	var this_id = "#" +thisid
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();

	CalliTochInfoAPI(sel_val.substring(3))
	
	
}


function onShowScreen(thisid){
	
	var this_id = "#" +thisid
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();

	getSceneListAPI(sel_val.substring(3))
}


function processiTouchData(data){
	
	// var itouch =data.devices
	DeviceInfo = data;
	var itouch;
	itouch= jsonPath(DeviceInfo, "$.devices[*].device_id")
	
	
	 //$("#select_location_L4 option[value^='Loi' ]").remove()
	 
	  $("#select_location_L2 option").not('[value^="Led" ]').remove()
	  $("#select_sreen option").not('[value^="Led" ]').remove()
			   
	 
	
	if (itouch.length > 0)
	itouch.forEach(function(itemid, index) {

		var device_name = FindDeviceName(itemid);
				
		itemid = 'Loi' + itemid;
		
		//alert(itemid + ',' + device_name)

		$("#select_location_L2").append($("<option></option>").attr("value", itemid).text(device_name));

	});
	
	

	
	console.log(data)
}


function processScreenList(screendata){
	
	var screenarray =screendata.scenes
	
	  $("#select_sreen option").not('[value^="Led" ]').remove()
	  if(screenarray.length >0 )
		  {
	  $('.schset *').prop("disabled",false);
	for ( var i = 0; i < screenarray.length; i++) {
		
		var obj = screenarray[i];
		var itemid ="att"+ obj.att_key
		
		$("#select_sreen").append($("<option></option>").attr("value", itemid).text(obj.att_value));
		
		console.log(obj.att_key+','+obj.att_value)
		
	}
	
		  }
	
	
	
	
}


function CallLocationInfoAPIOK(location_data){
	
	LocInfo = location_data;
	
	var L1_id_set
	
	$("#select_location_L1").append($("<option></option>").attr("value", "Led-1").text("-地點-"));
	$("#select_location_L2").append($("<option></option>").attr("value", "Led-2").text("-iTouch-"));
	//$("#select_location_L3").append($("<option></option>").attr("value", "Led-3").text("-辦公室-"));
	//$("#select_location_L4").append($("<option></option>").attr("value", "Led-4").text("-iTouch-"));
	$("#select_sreen").append($("<option></option>").attr("value", "Led-5").text("-情境選擇-"));
	

	L1_id_set = jsonPath(location_data, "$.location[*].location_id")
	L1_id_set.forEach(function(itemid, index) {

		var location_name = FindLocationName(itemid);
				
		itemid = 'Los' + itemid;
		
		//console.log(location_name + ',' + location_name)
		$("#select_location_L1").append($("<option></option>").attr("value", itemid).text(location_name));

		

	});
	
	
	ShowSch()
	ShowOther();
	
	
	
}

function ShowOther()
{

	ShowHeaderName();
	$('#schedule').collapse('show');
}









