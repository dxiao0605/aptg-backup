var loc = window.location.href;

var LocInfo;
var DeviceInfo;
var CompanyListInfo;
var device = [];
var parentlistid = []
var AddParentLocInfo;
var devid_now;  
var DemandInfo;

localStorage.setItem('superuserclick', '0');

if ( loc.indexOf("localhost") >= 0 ){
url_getLocation='http://localhost:8446/energy/getLocation?token='+localStorage.getItem('Token')
url_getDeviceList='http://localhost:8446/energy/getDeviceList?token='+localStorage.getItem('Token')+'&location_id='
url_getSingleDevice=' http://localhost:8446/energy/queryDevice?token='+localStorage.getItem('Token')+'&device_id='
url_login='http://localhost:8446/energy/authorization?'
url_device_set='http://localhost:8446/energy/setDeviceCmd?token='+localStorage.getItem('Token')
url_screen_list='http://localhost:8446/energy/getSceneList?token='+localStorage.getItem('Token')+'&device_id='
url_set_schedule='http://localhost:8446/energy/setSchedule?token='+localStorage.getItem('Token')	
url_aircon_show='http://localhost:8446/energy/getEnergyAttribute?token='+localStorage.getItem('Token')
url_getEgDemand='http://localhost:8446/energy/getEnergyDemand?token='+localStorage.getItem('Token')
url_getCustomerList='http://localhost:8446/energy/getCustomerList?token='+localStorage.getItem('Token')
url_getAccList='http://localhost:8446/energy/getAccountList?token='+localStorage.getItem('Token')
url_specifyCustomer='http://localhost:8446/energy/specifyCustomer?token='+localStorage.getItem('Token')+'&cust_id='
url_addAccount='http://localhost:8446/energy/addAccount?token='+localStorage.getItem('Token')
url_addCustomer='http://localhost:8446/energy/addCustomer?token='+localStorage.getItem('Token')
url_addLocation='http://localhost:8446/energy/addLocation?'
url_bindAccountLocation='http://localhost:8446/energy/bindAccountLocation?'	
url_getUnbindDeviceList='http://localhost:8446/energy/getUnbindDeviceList?'		
url_sidemenu='role/menu.json'
url_getCompanyList='http://localhost:8446/energy/getCompanyList?'
url_specifyCompany='http://localhost:8446/energy/specifyCompany?'	
url_getOverviewFormData='http://localhost:8446/energy/getOverviewFormData?'
url_getEnergyConsume='http://localhost:8446/energy/getEnergyConsume?'
url_getEnergyRank='http://localhost:8446/energy/getEnergyRank?'
url_getEnergyPower='http://localhost:8446/energy/getEnergyPower?'
url_getEnergyReport='http://localhost:8446/energy/getEnergyReport?'
url_getOverviewCategory='http://localhost:8446/energy/getOverviewCategory?'
url_getOverviewForm='http://localhost:8446/energy/getOverviewFormList?'
url_getDevices='http://localhost:8446/energy/getDevices?'
url_getCompanyCoefficient='http://localhost:8446/energy/getCompanyCoefficient?'
url_updateCompanyCoefficient='http://localhost:8446/energy/updateCompanyCoefficient?'
url_resetOverviewForm='http://localhost:8446/energy/resetOverviewForm?'
url_addOverviewForm='http://localhost:8446/energy/addOverviewForm?'
url_queryDevices='http://localhost:8446/energy/queryDevices?'
url_queryDeviceData='http://localhost:8446/energy/queryDeviceData?'
}
else {	
url_getLocation='/energy/getLocation?token='+localStorage.getItem('Token')
url_getDeviceList='/energy/getDeviceList?token='+localStorage.getItem('Token')+'&location_id='
url_getSingleDevice='/energy/queryDevice?token='+localStorage.getItem('Token')+'&device_id='
url_login='/energy/authorization?'
url_device_set='/energy/setDeviceCmd?token='+localStorage.getItem('Token')
url_screen_list='/energy/getSceneList?token='+localStorage.getItem('Token')+'&device_id='
url_set_schedule='/energy/setSchedule?token='+localStorage.getItem('Token')
url_aircon_show='/energy/getEnergyAttribute?token='+localStorage.getItem('Token')
url_getEgDemand='/energy/getEnergyDemand?token='+localStorage.getItem('Token')
url_getCustomerList='/energy/getCustomerList?token='+localStorage.getItem('Token')
url_getAccList='/energy/getAccountList?token='+localStorage.getItem('Token')
url_specifyCustomer='/energy/specifyCustomer?token='+localStorage.getItem('Token')+'&cust_id='
url_addAccount='/energy/addAccount?token='+localStorage.getItem('Token')
url_addCustomer='/energy/addCustomer?token='+localStorage.getItem('Token')
url_addLocation='/energy/addLocation?'
url_bindAccountLocation='/energy/bindAccountLocation?'
url_getUnbindDeviceList='/energy/getUnbindDeviceList?'		
url_sidemenu='role/menu.json'
url_getCompanyList='/energy/getCompanyList?'	
url_specifyCompany='/energy/specifyCompany?'
url_getOverviewFormData='/energy/getOverviewFormData?'
url_getEnergyConsume='/energy/getEnergyConsume?'
url_getEnergyRank='/energy/getEnergyRank?'
url_getEnergyPower='/energy/getEnergyPower?'
url_getEnergyReport='/energy/getEnergyReport?'
url_getOverviewCategory='/energy/getOverviewCategory?'
url_getOverviewForm='/energy/getOverviewFormList?'
url_getDevices='/energy/getDevices?'
url_getCompanyCoefficient='/energy/getCompanyCoefficient?'
url_updateCompanyCoefficient='/energy/updateCompanyCoefficient?'
url_resetOverviewForm='/energy/resetOverviewForm?'
url_addOverviewForm='/energy/addOverviewForm?'
url_queryDevices='/energy/queryDevices?'
url_queryDeviceData='/energy/queryDeviceData?'
}

$(window).on("load", function() {

});



$(document).ready(function () {

	
	if ( loc.indexOf("login") <= 0 && loc.indexOf("html") > 0)
		LoadCommonPage();
		
});








function ShowHeaderName() {
if ( loc.indexOf("login") <= 0 && loc.indexOf("device_chk") <= 0){
	if(localStorage.getItem('session')==='111'){
		$('#headuser').text(localStorage.getItem('username')+",您好!");
	    $('#logouttext').text("登出");
	}
	else
		window.location = "login.html";
	}

}


function LoadCommonPage(){
	
	
	$("#footer").empty();
	 $("#footer").load( "include/footer.html", function() {
		 
	 })
	
	 $("#header").empty();
	 
	 $("#header").load( "include/header.html", function() {
		 
		
			 CallSideMenu();
		
		 
		 
		});
	 
	 
	  //$("#header").load("include/header.html");
	 // CallSideMenu();
	  //$("#side-bar").load("include/sidemenu.html");

	  
	  //$('#energy').collapse('show')
	  //$("#energy").click();
	  
}


function processCompanyList(data) {

	var head_val;
	
	$("#select_company").empty();
	
	if(data.company.length >1){
	//$("#select_company").append($("<option></option>").attr("value", "-1").text("-請選擇公司-"));
	$.each(data.company, function(index, obj) {
		  console.log(index +'---'+ obj.company_id+'----' +obj.company_name);
		  
		  $("#select_company").append($("<option></option>").attr("value", obj.company_id).text(obj.company_name));
		  
		  if(obj.company_role=='00')
			  head_val =obj.company_id;
		  
		});
	}
	
	
	/*
	data.forEach(function(itemid, index) {
		
		console.log(itemid)
		//$("#select_company").append($("<option></option>").attr("value", "Led-1").text("-地點-"));
		
	})
	
	*/
	
	// if(localStorage.getItem('role')!='SuperAdmin' || (localStorage.getItem('role')=='SuperUser' && localStorage.getItem('selcompany')!='-1'))

	
	 if(localStorage.getItem('role')!='SuperUser' || (localStorage.getItem('role')=='SuperUser' && localStorage.getItem('superuserclick')=='0'))
	 {
	CallSideMenu();
	 }
	 
	 
	 
	 if(localStorage.getItem('selcompany') =='-1'){
			$('#select_company option[value='+head_val+']').attr('selected','selected');
			localStorage.setItem('selcompany', head_val);
		}
		else
	    	$('#select_company option[value='+localStorage.getItem('selcompany')+']').attr('selected','selected');
		
}


function onSelectCompany(id) {
	
	var this_id = "#" +id
	var sel_text = $(this_id).find("option:selected").text()
	var sel_val = $(this_id).val();
	
	localStorage.setItem('selcompany', sel_val);
	
	CallspecifyCompany(sel_val);
	//alert(sel_text+","+sel_val)
	
}






function CallSideMenu() {
    device = [];
    
    
    if(localStorage.getItem('role')=='SuperUser')
    	url_sidemenu = "role/super.json";
    
    
    if(localStorage.getItem('role')=='SuperAdmin')
    	url_sidemenu = "role/admin.json";
		
    if(localStorage.getItem('role')=='Admin')
    	url_sidemenu = "role/admin.json";
		
	if(localStorage.getItem('role')=='User')
			url_sidemenu = "role/user.json";
	
	if(localStorage.getItem('role')=='UserReadOnly')
		url_sidemenu = "role/user.json";
    

    $.ajax({

        url: url_sidemenu,
        dataType: 'json',
        method: "GET",
        success: function (data) {
            var treedata = data

            //console.log(treedata)
            showmenu(treedata)
            //$("#c1").click();
            
            
            aftermenu();
           
            
           
           // alert('show')
            //$("#c1").click();
            
            //alert('show')

        },
        statusCode: {
            404: function () {
                alert('There was a problem with the server.  Try again soon!');
            }
        }

    })

}


function showmenu(data) {


    var json = data.children;
    $("#side-bar").append('<ul class="list-group">')
    $("#side-bar").append('</ul>')
    
        for (var i = 0; i < json.length; i++) {
           
            var obj = json[i];

            //console.log();

            if(obj.children.length > 0){
            $("#side-bar .list-group").append(addTreeNode(obj.id,obj.description,1,obj.hrefid));
            lpson(obj.children,obj.hrefid)
            }
            else
            $("#side-bar .list-group").append(addNode(obj.id,obj.description,1,obj.hrefid));
        }
    
    
        function lpgdson(data,id){



            var json = data;



            for (var i = 0; i < json.length; i++) {
                   
                var obj = json[i];

                console.log(obj);

                if(obj.children.length > 0){
                    $("#"+id).append(addTreeNode(obj.id,obj.description,3,obj.hrefid));
                    lpgdson(obj.children,obj.hrefid)
                    }
                    else
                    $("#"+id).append(addNode(obj.id,obj.description,3,obj.hrefid));

              
            }


        }
        
        function lpson(data,id){

            var json = data;



            for (var i = 0; i < json.length; i++) {
                   
                var obj = json[i];

                //console.log();

                if(obj.children.length > 0){
                $("#"+id).append(addTreeNode(obj.id,obj.description,2,obj.hrefid));
                lpgdson(obj.children,obj.hrefid)
                }
                else
                $("#"+id).append(addNode(obj.id,obj.description,2,obj.hrefid));
            }

           



        }


        
        function addTreeNode(id, ShowName,dp,hrefid) {

            var html = "";

           if(dp==1)
            html += '<a href="#'+ hrefid +'" id=' + id + ' data-toggle="collapse" aria-expanded="false"  class="list-group-item list-group-item-action list-group-item-dark">'
           
            if(dp==2)
            html += '<a href="#'+ hrefid +'" id=' + id + ' data-toggle="collapse" aria-expanded="false"  class="list-group-item list-group-item-action list-group-item-info">'
           
            if(dp==3)
            html += '<a href="#'+ hrefid +'" id=' + id + ' data-toggle="collapse" aria-expanded="false"  class="list-group-item list-group-item-action list-group-item-primary">'
           


            if(dp ==2)
            html += '<span class="ml-5 pl-1"></span>'

            if(dp ==3)
            html += '<span class="ml-5 pl-5"></span>'
           

            html += '<span>' + ShowName + '</span>'
            html += '<span class="fas fa-angle-double-right mt-1"></span>'
            html += '</a>'



            html +='<div id="'+ hrefid +'" class="collapse">'

            html +='</div>'

            return html

        }


        function addNode(id,ShowName,dp,hrefid) {

            var html = "";
            //html += '<a href="#" class="list-group-item list-group-item-action list-group-item-success">'


            if(dp==1)
            html += '<a href="'+ hrefid +'" class="list-group-item list-group-item-action list-group-item-dark">'
           
            if(dp==2)
            html += '<a href="'+ hrefid +'" class="list-group-item list-group-item-action list-group-item-info">'
           
            if(dp==3)
            html += '<a href="'+ hrefid +'" class="list-group-item list-group-item-action list-group-item-primary">'
           



            if(dp ==2)
            html += '<span class="ml-5 pl-1"></span>'

            if(dp ==3)
            html += '<span class="ml-5 pl-5"></span>'
           

            html += '<span>' + ShowName + '</span>'
            html += '</a>'

            return html

        }

        


}



function FindLocationName(id) {

	return jsonPath(LocInfo, "$..[?(@.location_id == '" + id + "')].name")

}


function FindDeviceName(id) {

	return jsonPath(DeviceInfo, "$..[?(@.device_id == '" + id + "')].device_alias")

}


function FindDeviceType(id) {

	return jsonPath(DeviceInfo, "$..[?(@.device_id == '" + id + "')].device_type" +
			"")

}


function addHealderInfo() {

	
	var html = "";

	
	$("#headitem").append(html);
	
	html += "<li class='nav-item'>"
	html += "<a class='nav-link' href='#' >"+UserName+", 您好</a>"
	html += "</li>"
    
	html += "<li class='nav-item'>"
	html += "<a class='nav-link' href='logout.jsp'> 登出</a>"
	html += "</li>"
	
		
		$("#headitem").append(html);
	
	

}



function CallgetEnergyReport(deviceid,type,report_time,param){
	
	var EnergyReportData;
		
		$.ajax({

		url: url_getEnergyReport,
		dataType: 'json',
		method: "GET",
		data: {
			'token'     : localStorage.getItem('Token'),
			'device_id'   : deviceid,
			'type'   : type,
			'report_time'   : report_time,
			'param'   : param,	
		},
		success: function (data) {
			EnergyReportData = data.msg

			CallgetEnergyReportOk(EnergyReportData);

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}

		})	
		
}




function CallgetEnergyPower(device_list){
	
	var EnergyPowerData;
		
		$.ajax({

		url: url_getEnergyPower,
		dataType: 'json',
		method: "GET",
		data: {
			'token'     : localStorage.getItem('Token'),
			'devices'   : device_list
		},
		success: function (data) {
			EnergyPowerData = data.msg

			CallgetEnergyPowerOk(EnergyPowerData);

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}

		})	
		
}

function CallgetEnergyRank(){
	
	
var EnergyRankData;
	
	$.ajax({

	url: url_getEnergyRank,
	dataType: 'json',
	method: "GET",
	data: {
		'token'  : localStorage.getItem('Token'),
		'type'   : '01'
	},
	success: function (data) {
		EnergyRankData = data.msg

		CallgetEnergyRankOk(EnergyRankData);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
	
}


function CallgetEnergyConsume(){
	
	var EnergyConsumeData;
	
	$.ajax({

	url: url_getEnergyConsume,
	dataType: 'json',
	method: "GET",
	data: {
		'token'  : localStorage.getItem('Token')
	},
	success: function (data) {
		EnergyConsumeData = data.msg

		CallgetEnergyConsumeOk(EnergyConsumeData);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
}


function CallresetOverviewForm(){
		
	//var OverviewCategoryData;
		
		$.ajax({

		url: url_resetOverviewForm,
		dataType: 'json',
		method: "POST",
		data: {
			'token'  : localStorage.getItem('Token')
		},
		success: function (data) {
			if(data.code=="00"){
				alert("重設成功");	
				location.reload();
				}
				else
					alert("重設失敗");

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}

		})	
			
	}








function CalladdOverviewForm(name,type,category_id,device_id,order){
	
	
var OverviewCategoryData;
	
	$.ajax({

	url: url_addOverviewForm,
	dataType: 'json',
	method: "POST",
	data: {
		
		'token'  : localStorage.getItem('Token'),
		name : name,
		type : type,
		category_id : category_id,
		device_id : device_id,
		order : order
		
	},
	success: function (data) {
		if(data.code=="00"){
			alert("新增成功");	
			}
			else
				alert("新增失敗");

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
	
	
	
}



function CallgetOverviewCategory(){
	
	
var OverviewCategoryData;
	
	$.ajax({

	url: url_getOverviewCategory,
	dataType: 'json',
	method: "GET",
	data: {
		'token'  : localStorage.getItem('Token')
	},
	success: function (data) {
		OverviewFormData = data.msg

		CallgetOverviewCategoryOk(OverviewFormData);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
	
	
	
	
}

function CallgetOverviewFormData(){
	
	var OverviewFormData;
	
	$.ajax({

	url: url_getOverviewFormData,
	dataType: 'json',
	method: "GET",
	data: {
		'token'  : localStorage.getItem('Token'),
		'category_id' : '1'
	},
	success: function (data) {
		OverviewFormData = data.msg

		CallgetOverviewFormDataOk(OverviewFormData);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
}

function CallgetOverviewForm(){
	
	var OverviewForm;
	
	$.ajax({

	url: url_getOverviewForm,
	dataType: 'json',
	method: "GET",
	data: {
		'token'  : localStorage.getItem('Token'),
		'category_id' : '1',
		 type : '02'
	},
	success: function (data) {
		OverviewForm = data.msg

		CallgetOverviewFormOk(OverviewForm);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
}



function CallDemandApi(){
	
	var demand_data;
	
	$.ajax({

	url: url_getEgDemand,
	dataType: 'json',
	method: "GET",
	success: function (data) {
		DemandInfo = data.msg

			processDemand();

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})	
}

function CallspecifyCustomerApi(custid){
	
	
	$.ajax({

		url: url_specifyCustomer+custid,
		dataType: 'json',
		method: "GET",
		success: function (data) {
			

				processspecifyCustomer(data);

		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}

		})
		
	
}



function CallCustListApi(){
	
	var customer_data;
	
	$.ajax({

	url: url_getCustomerList,
	dataType: 'json',
	method: "GET",
	success: function (data) {
		customer_data = data.msg

			processCustList(customer_data);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	
	
	
	
}


function CallgetCompanyCoefficient() {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_getCompanyCoefficient ,
		method:"Get",
		dataType: 'json',
		data: {
			'token'  : localStorage.getItem('Token')
		},
		success: function (data) {
			
			CallgetCompanyCoefficientOK(data);
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}



function CallupdateCompanyCoefficient(meters,mancount) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_updateCompanyCoefficient ,
		method:"POST",
		dataType: 'json',
		data: {
			'token'  : localStorage.getItem('Token'),
			'square_meters' :meters,
			'people' : mancount
		},
		success: function (data) {
			if(data.code=="00"){
			alert("設定成功");	
			location.reload();
			}
			else
				alert("設定失敗");
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}


function CallAddLocation(data) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_addLocation ,
		method:"POST",
		dataType: 'json',
		data: {
			'token'  : localStorage.getItem('Token'),
			'name' :data[0].value,
			'parent_id' :'0'
		},
		success: function (data) {
			if(data.code=="00")
			alert("新增成功");	
			else
				alert("新增失敗");
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}

function CallBindAccountLocation(data) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_bindAccountLocation ,
		method:"POST",
		dataType: 'json',
		data: {
			'token'  : localStorage.getItem('Token'),
			'account' : data.account,
			'location_id' :data.locs
		},
		success: function (data) {
			if(data.code=="00")
			alert("綁定成功");	
			else
				alert("綁定失敗");
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}




function CallAddCust(data) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_addCustomer ,
		method:"POST",
		dataType: 'json',
		data: {
			'cust_name' :data[0].value,
			'contact_person' :data[1].value,
			'contact_num' :data[2].value,
			'address' :data[3].value,
			'email' :data[4].value,
			'type' :data[5].value,
			'sales' :data[6].value,
			'sales_num' :data[7].value,
			'sales_email' :data[8].value
		},
		success: function (data) {
			if(data.code=="00")
			alert("新增會員成功");	
			else
				alert("新增失敗");
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}




function CallAddAccount(data) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_addAccount ,
		method:"POST",
		dataType: 'json',
		data: {
			'account' :data[0].value,
			'password' :data[1].value,
			'user_name' :data[2].value
		},
		success: function (data) {
			if(data.code=="00")
			alert("新增成功");	
			else
				alert("新增失敗");
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}


function CallAllUnbindDeviceLightListApi(){
	
	
	
	$.ajax({

	url: url_getUnbindDeviceList,
	dataType: 'json',
	method: "GET",
	data: {
		'token' :localStorage.getItem('Token'),
		'type' :'light'
	},
	success: function (data) {
		

			processAllUnbindDeviceLightList(data.msg);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	
}


function CallAllUnbindDeviceItouchListApi(){
	
	
	
	$.ajax({

	url: url_getUnbindDeviceList,
	dataType: 'json',
	method: "GET",
	data: {
		'token' :localStorage.getItem('Token'),
		'type' :'itouch'
	},
	success: function (data) {
		

			processAllUnbindDeviceItouchList(data.msg);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	
}






function CallAccListApi(){
	
	var customer_data;
	
	$.ajax({

	url: url_getAccList,
	dataType: 'json',
	method: "GET",
	success: function (data) {
		customer_data = data.msg

			processAccList(customer_data);

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	
}


function CallAirConInfo() {
	
	var aircon_data;
		
	$.ajax({

	url: url_aircon_show,
	dataType: 'json',
	method: "GET",
	success: function (data) {
		aircon_data = data.msg

			processAirconInfo(aircon_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}



function CalliTochInfoAPI(officeid) {
	
	var device_data;
		
	$.ajax({

	//url: 'http://localhost:3000/device/',
	//url: 'http://localhost:8446/energy/getDeviceList?location_id='+deviceid,
	url: url_getDeviceList,
	data: {
		'token' :localStorage.getItem('Token'),
		//'location_id' : officeid,
		'type' :'itouch'
	},
	dataType: 'json',
	method: "GET",
	success: function (data) {
		device_data = data.msg

		//return device_data;
	    
			processiTouchData(device_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}



function CallgetDevices(device_type) {
	
	var device_data;
		
	$.ajax({

		url: url_getDevices,
	dataType: 'json',
	method: "GET",
	data: {
		'token' :localStorage.getItem('Token'),
		//'location_id' : officeid,
		'type' : device_type
	},
	success: function (data) {
		device_data = data.msg.devices

		//return device_data;
	    
		CallgetDevicesOK(device_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}






function CallDeviceListAPI(device_type) {
	
	var device_data;
		
	$.ajax({

		url: url_getDeviceList,
	dataType: 'json',
	method: "GET",
	data: {
		'token' :localStorage.getItem('Token'),
		//'location_id' : officeid,
		'type' : device_type
	},
	success: function (data) {
		device_data = data.msg.devices

		//return device_data;
	    
			processDeviceData(device_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}




function CallqueryDevices(taxid,device_key) {
	
	var device_data;
		
	$.ajax({

		url: url_queryDevices,
	dataType: 'json',
	method: "GET",
	data: {
		'tax_id' : taxid,
		'device_key' : device_key
	},
	success: function (data) {
		device_data = data.msg.devices

		//return device_data;
	    
		CallqueryDevicesOK(device_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}



function CallqueryDeviceData(device_key) {
	
	var device_data;
		
	$.ajax({

		url: url_queryDeviceData,
	dataType: 'json',
	method: "GET",
	data: {
		'device_key' : device_key
	},
	success: function (data) {
		device_data = data.msg.data

		//return device_data;
	    
		CallqueryDeviceDataOK(device_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}





function CallLightInfoAPI() {
	
var device_data;
	
$.ajax({

	url: url_getDeviceList,
dataType: 'json',
method: "GET",
data: {
	'token' :localStorage.getItem('Token'),
	//'location_id' : officeid,
	'type' : 'light'
},
success: function (data) {
	device_data = data.msg

	//return device_data;
    
		processDeviceData(device_data)

},
statusCode: {
	404: function () {
		alert('There was a problem with the server.  Try again soon!');
	}
}

})
}


function CallSingleLightInfoAPI(deviceid) {
	
	var device_data;
		
	$.ajax({

	
	url: url_getSingleDevice+deviceid,
	dataType: 'json',
	method: "GET",
	success: function (data) {
		device_data = data.msg

		//return device_data;
	    
			processSingleDeviceData(device_data)

	},
	statusCode: {
		404: function () {
			alert('There was a problem with the server.  Try again soon!');
		}
	}

	})
	}


function CallLocationInfoAPI() {
	
	var location_data;

	$.ajax({
		
		url: url_getLocation ,
		dataType: 'json',
		success: function (data) {
			location_data = data
			CallLocationInfoAPIOK(location_data.msg);	
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}


function CallDeviceCmd(objcmd) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_device_set ,
		method:"POST",
		dataType: 'json',
		data: {
			'device_id' :objcmd.device_id,
			'power' :objcmd.power,
			'bri' :objcmd.bri,
			'cct':objcmd.cct
		},
		success: function (data) {
			if(data.code=="00")
			alert("成功");	
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}


function CallsetSchedule(objcmd) {
	
	//var param ='device_id='+objcmd.device_id +'&power='+objcmd.power+'&bri='+objcmd.bri+'&cct='+objcmd.cct
	
	$.ajax({
		
		url: url_set_schedule ,
		method:"POST",
		dataType: 'json',
		data: {
			'device_id' :objcmd.device_id.substring(3),
			'att_key' :objcmd.att_key,
			'type' :objcmd.type,
			'time':objcmd.time,
			'startdate':objcmd.startdate,
			'enddate':objcmd.enddate,
			'weekday':objcmd.weekday,
			'monthday':objcmd.monthday
		},
		success: function (data) {
			if(data.code=="00")
			alert("成功!");	
			else
			alert("失敗!"+data.msg);
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}









function getSceneListAPI(devid) {
	
	
	$.ajax({
		
		url: url_screen_list + devid,
		method: "GET",
		dataType: 'json',
		success: function (data) {
			processScreenList(data.msg)	
		},
		statusCode: {
			404: function () {
				alert('There was a problem with the server.  Try again soon!');
			}
		}
	});

}


