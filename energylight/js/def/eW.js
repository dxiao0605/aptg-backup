  


$(document).ready(function() {

	
	
	
});


function aftermenu(){
	
	ShowHeaderName();
	
	
	addOverViewModal();
	//addOverViewOtherModal();
	
	
	
	
	
}

// some code
function showDeviceTable(device_data) {

	//device_data =[];
	widthtbl='15%'
	
	var t = $('#statusTbl').DataTable({
			data: [],
			searching : false,
			paging : false,
			info : false,
			columns: [],
			columnDefs: [
			    	{
					"title": "裝置名稱",
					"targets": 0,
					"width": widthtbl,
					"className": 'dt-center'
				},
				
				{
					"title": "連線狀態",
					"targets": 1,
					"width": widthtbl,
					"className": 'dt-center'
				},{
					"title": "裝置名稱",
					"targets": 2,
					"width": widthtbl,
					"className": 'dt-center'
				},
				
				{
					"title": "連線狀態",
					"targets": 3,
					"width": widthtbl,
					"className": 'dt-center'
				},{
					"title": "裝置名稱",
					"targets": 4,
					"width": widthtbl,
					"className": 'dt-center'
				},
				
				{
					"title": "連線狀態",
					"targets": 5,
					"width": widthtbl,
					"className": 'dt-center'
				}
				
			]
			
			

		});
	
	
	/*
	$('#statusTbl tbody').on( 'click', 'td', function () {
	    alert( t.row( this ).index() +','+t.cell( this ).data()+','+t.column( this ).index() );
	} );
	*/
		
	// var table = $('#statusTbl').DataTable({sorting : false});
	/* t.row.add( [
          'd1',
         'on',
          'd2',
          'off',
         'd3',
         'off'
      ] ).draw( false );
	*/
	
	rowarray=[]
	var j=0
	 for (var i = 0; i < device_data.length; i++) {
			j++
			
		 rowarray.push(device_data[i].alias)
		 rowarray.push(device_data[i].status)
			console.log(i+','+device_data[i].alias+','+device_data[i].status)
			
			if(j ==3){
				
				t.row.add(rowarray).draw( false );
				j=0
				rowarray=[]
			}
			
		}
	
	for(j;j<3;j++){
		
		 rowarray.push('')
		  rowarray.push('')
	}
	
	t.row.add(rowarray).draw( false );
	//if(j>0)
		//t.row.add(rowarray).draw( false );
	
}

function addOverViewOther() {

	var modal = $('#OverViewFormOtherModal');

	modal.modal()
	
	//alert("新增裝置");

}


function addOverViewOtherModal() {

	$("#add-view-other").empty();
//	$("#add-view-other").load("include/addOverviewFormOther.html");

	
	$("#add-view-other").load( "include/addOverviewFormOther.html", function() {
		 
		CallgetOverviewFormData();
		CallgetOverviewCategory();
		CallgetOverviewForm();
		CallgetDevices('electric');
		
		 
	
	 
	 
	});
	

}

function ResetOverView()
{
	
	CallresetOverviewForm();
}

function OverViewOK(){
	
	
	var rootname =$("#viewrootname").val();
	
	var sel_text = $("#viewrootsel").find("option:selected").text()
	var sel_val = $("#viewrootsel").val();

	if(sel_val!='x')
	CalladdOverviewForm(rootname,'01','1',sel_val,'0')
	
	//alert(rootname +','+sel_text+','+sel_val)
	
}

function CloseOverView(){
	location.reload();
}


function OverViewOtherOK(){
	
	
	var viewname =$("#viewname").val();
	
	var sel_text = $("#viewsel").find("option:selected").text()
	var sel_val = $("#viewsel").val();

	//alert(viewname +','+sel_text+','+sel_val)
	
	if(sel_val!='x')
		CalladdOverviewForm(viewname,'02','1',sel_val,'1')
		
	
	
	//if(sel_val!='x')
	//CalladdOverviewForm(rootname,'01','1',sel_val,'0')
	
	//alert(rootname +','+sel_text+','+sel_val)
	
}



function addOverView() {

	var modal = $('#OverViewFormModal');

	modal.modal()
	//alert("新增裝置");
}



function addEuiConfigModal() {

	$("#euiConfig").empty();
	//$("#add-view").load("include/addOverviewForm.html");

	

	 $("#euiConfig").load( "include/EUIConfig.html", function() {
		 
			
		 CallgetCompanyCoefficient();
	 
	 
	});
	
	
}


function addOverViewModal() {

	$("#add-view").empty();
	//$("#add-view").load("include/addOverviewForm.html");

	

	 $("#add-view").load( "include/addOverviewForm.html", function() {
		 
			
		 addOverViewOtherModal();
	    
		 addEuiConfigModal();
	 
	 
	});
	
	
}

function SetCompanyCoefficient(){
	
	
	var square_m = $("#manspace").val();
	var man_count = $("#mancount").val();
	
	
	CallupdateCompanyCoefficient(square_m,man_count)
	
	//alert(square_m +','+man_count)
	
}


function CallgetCompanyCoefficientOK(data)
{
	console.log(data.msg.coefficient[0].square_meters);
	
	 $("#manspace").val(data.msg.coefficient[0].square_meters);
	
	
	console.log(data.msg.coefficient[0].people);
	
	 $("#mancount").val(data.msg.coefficient[0].people);
}

function CallgetDevicesOK(device_data)
{
	console.log(device_data);
	//alert(device_data);
	
	showOverViewRootSel(device_data)
	
	showDeviceTable(device_data)
	
	
}


function showOverViewRootSel(device_data){
	
	$("#viewrootsel").empty();
	
	$("#viewrootsel").append($("<option></option>").attr("value", 'x').text('請選擇總表迴路'));
	 
$("#viewsel").empty();
	
	$("#viewsel").append($("<option></option>").attr("value", 'x').text('請選擇分表迴路'));
	
	
	

	
	for (var i = 0; i < device_data.length; i++) {
		
		$("#viewrootsel").append($("<option></option>").attr("value", device_data[i].device_id).text(device_data[i].alias));
		$("#viewsel").append($("<option></option>").attr("value", device_data[i].device_id).text(device_data[i].alias));
		
		console.log(device_data[i].device_id +','+device_data[i].alias)
		
	}
	
	
}


function CallgetOverviewFormOk(OverviewForm){
	
	console.log(OverviewForm);
	showOverViewOtherSel(OverviewForm)
	
}


function showOverViewOtherSel(OverviewFormOther){
	
	
   formlist =	OverviewFormOther.forms
	
   $("#viewothersel").empty();
	
	
	 
	
	
	for (var i = 0; i < formlist.length; i++) {
		
		$("#viewothersel").append($("<option></option>").attr("value", formlist[i].device_id).text(formlist[i].name));
		
		
		console.log(formlist[i].device_id +','+formlist[i].name)
		
	}
	
}


function EUIconfig(){
	
	var modal = $('#EUIconfigModal');

	modal.modal()
}


function EUPIconfig(){
	
	var modal = $('#EUIconfigModal');

	modal.modal()
}

function CallgetOverviewCategoryOk(OverviewFormData){
	
	ShowOverviewCategory(OverviewFormData.categories)
	
}


function ShowOverviewCategory(OverviewFormData){
	
	
	for (var i = 0; i < OverviewFormData.length; i++) {

		var obj = OverviewFormData[i];
		 console.log(obj.name+','+obj.value)

		if(obj.category_id =='1')
			 $("#showTotalW").text(obj.value+' kwh');
		 
		 if(obj.category_id =='2')
			 $("#showEUI").text(obj.value+' kwh/m2');
		 
		 if(obj.category_id =='3')
			 $("#showEUPI").text(obj.value+' kwh/人');
		 
		 if(obj.category_id =='4')
			 $("#showCO2").text(obj.value+' 公斤CO2e/度');
			
			
	}
	
}



function CallgetOverviewFormDataOk(OrgData){
	
	ShowOrgChart(OrgData)
}


function ShowOrgChart(OrgData){
	
	var OrgDataArray = OrgData.forms
	$("#chart_div").empty();
	if (OrgDataArray.length ==0){
		//alert('請進行單線圖設定')
		
		$("#chart_div").append($("<h5>無任何單線圖設定，請設定</h5>"));
		return;
	}
		
	for (var i = 0; i < OrgDataArray.length; i++) {

		var obj = OrgDataArray[i];
		 console.log(obj.name+','+obj.data)

		

	}
	
	
	

google.charts.load('current', {
    packages: ["orgchart"]
});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Name');
    data.addColumn('string', 'Manager');



    data.addColumn('string', 'ToolTip');



    function cardhtml(header,itemData) {
    	
    	var i_V,i_A,i_kW;
    	
    	
    	
    	for (var i = 0; i < itemData.length; i++) {

    		var obj = itemData[i];
    		// console.log(obj.name+','+obj.data)

    		if (obj.item == 'V')
    			i_V =obj.alias +':'+ obj.value

    		if (obj.item == 'A')
    			i_A = obj.alias +':'+ obj.value

    		if (obj.item == 'KW')
    			i_kW = obj.alias +':'+ obj.value

    	}
    	
    	
    	
    	
        card = '<div class="card bg-light"  style="width: 100%">'
        card += '<div class="card-header h6">' + header + '</div>'
        card += '<div class="card-body">'

        card += '<ul class="list-group list-group-flush">'
        card += '<li class="list-group-item">'+i_V+'</li>'
        card += '<li class="list-group-item">'+i_A+'</li>'
        card += '<li class="list-group-item">'+i_kW+'</li>'
        card += '</ul>'
        card += '</div>'
        card += '</div>'

        return card;
    }



   

    var arry = [];
    OrgDataArray.forEach(function (item, index) {

        console.log(item.name, index);
        if (index == 0)
            arry.push([{
                'v': 'root',
                'f': cardhtml(item.name,item.data)
            }, '', '']);
        else
            arry.push([{
                'v': 'child' + index,
                'f': cardhtml(item.name,item.data)
            }, 'root', '']);
    });



   

    data.addRows(arry);


    // Create the chart.
    var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
    // Draw the chart, setting the allowHtml option to true for the tooltips.


    function selectHandler() {
       
        var selectedItem = chart.getSelection()[0];
        if (selectedItem) {
            
            var SelectedValue = data.getValue(selectedItem.row, 0);

            alert(SelectedValue + ',' + selectedItem.row)



            //childs1=chart.getChildrenIndexes(selectedItem.row);
            //alert(childs1)


            //data.removeRow(selectedItem.row);
            //chart.draw(data, {allowHtml:true,size: 'large', nodeClass: 'myNodeClass',selectedNodeClass :'mySelectedNodeClass'});

        }



    }

   // google.visualization.events.addListener(chart, 'select', selectHandler);


    /*
google.visualization.events.addListener(chart, 'ready', function(){
    $("#chart_div").unbind("click");
});
*/
    chart.draw(data, {allowHtml:true,size: 'large', nodeClass: 'myNodeClass',selectedNodeClass :'mySelectedNodeClass'});

   /* chart.draw(data, {
        allowHtml: true,
        size: 'large',
        nodeClass: 'myNodeClass'
    });

*/



}

}
