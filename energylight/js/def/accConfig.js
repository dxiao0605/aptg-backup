$(document).ready(function() {

	// CallLocationInfoAPI();

	// MainPageClickEvent()

});

var OptionSelect = ''
// some code

function aftermenu() {

	ShowHeaderName();
	CallLocationInfoAPI();

	// CallAllUnbindDeviceLightListApi();
	// CallAllUnbindDeviceItouchListApi();

	CalliTochInfoAPI();
	CallLightInfoAPI()
	addAccountModal();

	// addRank();
	// addinfo();
	// addHtmlChart('北市用電圖', 'tpe1');
}

function ClickAddAccount() {

	var inputs = $('#AccModal .form-control');
	// console.log(inputs);

	CallAddAccount(inputs);

	// $.each(inputs, function(index, item) {
	// alert(index+','+item.value)
	// });

}

function ClickAddCustChild() {

	var inputs = $('#CustChildModal .form-control');

	CallAddLocation(inputs);

}

function addAccount() {

	var modal = $('#AccModal');

	modal.modal()

}

function addCustChild() {

	var modal = $('#CustChildModal');

	modal.modal()

}

function addAccountModal() {

	$("#add-acount").empty();
	$("#add-acount").load("include/addacc.html");

	$("#add-custchild").empty();
	$("#add-custchild").load("include/addcustchild.html");

}
function processAccList(customer_data) {

	// console.log(customer_data)
	ShowAccountTable(customer_data.accounts)
}

function processDeviceData(device_data) {

	ShowDeviceTable(device_data.devices)
}

function processAllUnbindDeviceLightList(device_data) {

	ShowDeviceTable(device_data.devices)
}

function processiTouchData(device_data) {

	ShowiTouchTable(device_data.devices)
}

function processAllUnbindDeviceItouchList(device_data) {

	ShowiTouchTable(device_data.devices)
}

function MainPageClickEvent() {

	$('.mainpage').on('click', function(event) {

		localStorage.setItem('currentid', 'N');

	})

}

function ShowiTouchTable(iTouch_data) {

	var comSelectHtml = '';

	comSelectHtml += "<select>";
	comSelectHtml += " <option value ='1'>萬丹鄉公所</option>"
	comSelectHtml += " <option value ='2'>內湖區公所</option>"
	comSelectHtml += " <option value='3'>士林區公所</option>"
	comSelectHtml += " <option value='4'>中山區公所</option>"
	comSelectHtml += "</select>"
	comSelectHtml += "<button type='button' name='bindAccount' class='btn btn-danger ml-3' id='apply_acc'>Itouch綁定</button>"

	var t = $('#itoch_tables').DataTable({
		searching : false,
		paging : false,
		info : false,
		data : iTouch_data,
		columns : [ {
			data : null
		}, {
			data : 'device_alias'
		}, {
			data : 'device_key'
		} ],
		columnDefs : [ {
			"searchable" : false,
			"orderable" : false,
			"title" : "序號",
			"targets" : 0,
			"width" : "5%",
			"className" : 'dt-center'
		}, {
			"title" : "裝置名稱",
			"targets" : 1,
			"width" : "10%",
			"className" : 'dt-center'
		}, {
			"title" : "裝置代碼",
			"targets" : 2,
			"width" : "10%",
			"className" : 'dt-center'
		}, {
			"title" : "公司綁定",
			"targets" : 3,
			"width" : "10%",
			"data" : null,
			"defaultContent" : comSelectHtml,
			"className" : 'dt-center'
		}

		],

		language : {
			"sProcessing" : "處理中...",
			"sLengthMenu" : "顯示 _MENU_ 項結果",
			"sZeroRecords" : "沒有匹配結果",
			"sInfo" : "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
			"sInfoEmpty" : "顯示第 0 至 0 項結果，共 0 項",
			"sInfoFiltered" : "(由 _MAX_ 項結果過濾)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中數據為空",
			"sLoadingRecords" : "載入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首頁",
				"sPrevious" : "上頁",
				"sNext" : "下頁",
				"sLast" : "末頁"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}

	});

	t.on('order.dt search.dt', function() {
		t.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();

}

function ShowDeviceTable(light_data) {

	var ItochSelectHtml = '';

	ItochSelectHtml += "<select>";
	ItochSelectHtml += " <option value ='1'>itouchA</option>"
	ItochSelectHtml += " <option value ='2'>itouchB</option>"
	ItochSelectHtml += " <option value='3'>itouchC</option>"
	ItochSelectHtml += " <option value='4'>itouchD</option>"
	ItochSelectHtml += "</select>"
	ItochSelectHtml += "<button type='button' name='bindAccount' class='btn btn-danger ml-3' id='apply_acc'>裝置綁定</button>"

	var t = $('#device_tables').DataTable({
		searching : false,
		paging : false,
		info : false,
		data : light_data,
		columns : [ {
			data : null
		}, {
			data : 'device_alias'
		}, {
			data : 'device_key'
		} ],
		columnDefs : [ {
			"searchable" : false,
			"orderable" : false,
			"title" : "序號",
			"targets" : 0,
			"width" : "5%",
			"className" : 'dt-center'
		}, {
			"title" : "裝置名稱",
			"targets" : 1,
			"width" : "10%",
			"className" : 'dt-center'
		}, {
			"title" : "裝置代碼",
			"targets" : 2,
			"width" : "10%",
			"className" : 'dt-center'
		}, {
			"title" : "Itouch綁定",
			"targets" : 3,
			"width" : "10%",
			"data" : null,
			"defaultContent" : ItochSelectHtml,
			"className" : 'dt-center'
		}

		],

		language : {
			"sProcessing" : "處理中...",
			"sLengthMenu" : "顯示 _MENU_ 項結果",
			"sZeroRecords" : "沒有匹配結果",
			"sInfo" : "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
			"sInfoEmpty" : "顯示第 0 至 0 項結果，共 0 項",
			"sInfoFiltered" : "(由 _MAX_ 項結果過濾)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中數據為空",
			"sLoadingRecords" : "載入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首頁",
				"sPrevious" : "上頁",
				"sNext" : "下頁",
				"sLast" : "末頁"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}

	});

	t.on('order.dt search.dt', function() {
		t.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();

}

function ShowAccountTable(custData) {

	var multiSelectHtml = ''

	multiSelectHtml += "<div class='LocSelect '>"

	multiSelectHtml += "<div class='d-inline-block align-top ml-2'>"
	multiSelectHtml += "<select multiple id='s_all'>"

	multiSelectHtml += OptionSelect
	// multiSelectHtml +="<option value='1'>內湖區公所</option>"
	// multiSelectHtml +="<option value='2'>中正區公胡</option>"
	// multiSelectHtml +="<option value='3'>萬丹鄉公所</option>"
	// multiSelectHtml +="<option value='4'>大同區公所</option>"
	multiSelectHtml += "</select>"
	multiSelectHtml += "<div class='h6'>可選區域</div>"
	multiSelectHtml += "</div>"

	multiSelectHtml += "<div class='add-reomove d-inline-block align-top ml-2 mt-4'>"
	// multiSelectHtml +="<a href='#' id='add' >add</a>"
	// multiSelectHtml +="<a href='#' id='remove' >remove</a>"
	multiSelectHtml += "<div class='h6 add_icon align-middle' id='add_icon' >&gt;&gt;&gt;&gt;</div>"
	multiSelectHtml += "<div class='h6 del_icon align-middle' id='del_icon' >&lt;&lt;&lt;&lt;</div>"
	multiSelectHtml += "</div>"

	multiSelectHtml += "<div class='d-inline-block align-top ml-2'>"
	multiSelectHtml += "<select multiple id='s_ok'></select>"
	multiSelectHtml += "<div class='h6'>已選區域</div>"
	multiSelectHtml += "</div>"

	multiSelectHtml += "<div class='d-inline-block align-middle ml-2 mt-5'>"
	multiSelectHtml += "<button type='button' name='bindAccount' class='btn btn-danger' id='apply_acc'>帳號綁定地點</button>"
	multiSelectHtml += "</div>"

	multiSelectHtml += "</div>"

	var acctbl = $('#acc_tables').DataTable(
			{
				searching : false,
				paging : false,
				info : false,
				data : custData,
				columns : [ {
					data : 'account'
				}, {
					data : 'user_name'
				}, {
					data : 'role'
				}, {
					data : 'createby'
				}, {
					data : 'enable'
				} ],
				destroy : true,
				columnDefs : [
						{
							"title" : "Account",
							"className" : 'dt-center',
							"targets" : 0
						},
						{
							"title" : "名稱",
							"className" : 'dt-center',
							"targets" : 1
						},
						{
							"title" : "角色",
							"className" : 'dt-center',
							"targets" : 2
						},
						{
							"title" : "建立者",
							"className" : 'dt-center',
							"targets" : 3
						},
						{
							"title" : "生效",
							"className" : 'dt-center',
							"targets" : 4
						},
						{
							"targets" : 5,
							"data" : null,
							"width" : "35%",
							// "defaultContent": multiSelectHtml
							"render" : function(data, type, row, meta) {
								console.log(meta.row)
								return multiSelectHtml.replace("id='s_all'",
										"id='s_all_" + meta.row + "'").replace(
										"id='s_ok'",
										"id='s_ok_" + meta.row + "'").replace(
										"id='add_icon'",
										"id='add_icon_" + meta.row + "'")
										.replace(
												"id='del_icon'",
												"id='del_icon_" + meta.row
														+ "'").replace(
												"id='apply_acc'",
												"id='apply_acc_" + meta.row
														+ "'")
							}
						} ],
				language : {
					"sProcessing" : "處理中...",
					"sLengthMenu" : "顯示 _MENU_ 項結果",
					"sZeroRecords" : "沒有匹配結果",
					"sInfo" : "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
					"sInfoEmpty" : "顯示第 0 至 0 項結果，共 0 項",
					"sInfoFiltered" : "(由 _MAX_ 項結果過濾)",
					"sInfoPostFix" : "",
					"sSearch" : "搜索:",
					"sUrl" : "",
					"sEmptyTable" : "表中數據為空",
					"sLoadingRecords" : "載入中...",
					"sInfoThousands" : ",",
					"oPaginate" : {
						"sFirst" : "首頁",
						"sPrevious" : "上頁",
						"sNext" : "下頁",
						"sLast" : "末頁"
					},
					"oAria" : {
						"sSortAscending" : ": 以升序排列此列",
						"sSortDescending" : ": 以降序排列此列"
					}
				}

			});

	/*
	 * $('.LocSelect .add').click(function() { return !$('#s_add
	 * option:selected').remove().appendTo('#s_mv'); }); $('.LocSelect
	 * .remove').click(function() { return !$('#s_mv
	 * option:selected').remove().appendTo('#s_add'); });
	 * 
	 */

	$('#acc_tables .add_icon').click(function() {

		// var data = acctbl.row($(this).parents('tr')).data();
		// console.log(data)

		var iconid = this.id
		var sel_all = "#" + iconid.replace('add_icon', 's_all')
		var sel_ok = "#" + iconid.replace('add_icon', 's_ok')
		// var selitem=$(sel_all+' option:selected')

		$(sel_all + ' option:selected').remove().appendTo(sel_ok);

		// alert(this.id+","+selitem);
		// $(this).parent().siblings('.s_all').find('select
		// option:selected').remove()

	});
	$('#acc_tables .del_icon').click(function() {

		var iconid = this.id
		var sel_all = "#" + iconid.replace('del_icon', 's_all')
		var sel_ok = "#" + iconid.replace('del_icon', 's_ok')

		$(sel_ok + ' option:selected').remove().appendTo(sel_all);

	});

	$('#acc_tables tbody').on('click', 'button[name=bindAccount]', function() {

		var data = acctbl.row($(this).parents('tr')).data();
		var buttonid = this.id
		var sel_ok = "#" + buttonid.replace('apply_acc', 's_ok')
		// var okitem=$(sel_ok)

		if ($(sel_ok).has('option').length < 1) {
			alert('empty select')

		} else {
			var options = $(sel_ok + ' option');

			var values = ''

			var values = $.map(options, function(option) {
				return option.value;
			});

			var locs = values.join();

			var obj = {};

			obj["account"] = data.account;
			obj["locs"] = locs;

			// alert(data.account+","+this.id+","+sel_ok+","+values)
			CallBindAccountLocation(obj)
		}

	})

}

function ShowBuildingChart(locdata) {
	
	
	var objarray = []
	
	for (var j = 0; j < locdata.location.length; j++) {
		objarray.push(locdata.location[j].name)
		console.log('root='+locdata.location[j].name);

		
	}
	
	for (var j = 0; j < locdata.location[0].next.length; j++) {
		objarray.push(locdata.location[0].next[j].name)
		console.log('child='+locdata.location[0].next[j].name);

		
	}

	google.charts.load('current', {
		packages : [ "orgchart" ]
	});
	google.charts.setOnLoadCallback(drawChart);

	function drawChart() {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Name');
		data.addColumn('string', 'Manager');

		data.addColumn('string', 'ToolTip');

		function cardhtml(header) {
			card = '<div class="card bg-light"  style="width: 100%">'
			card += '<div class="card-header h6">' + header + '</div>'

			card += '</div>'

			return card;
		}

		
		
		
		
	//	var objarray = [ '鴻海大樓(亞太電信總部)', '2F-亞太第一會議室-itouch', '2F-亞太行政室-itouch' ]

		var array = [];
		
		//array.push([ {'v' : 'root',	'f' : cardhtml(locdata) }, '', '' ]);
		objarray.forEach(function(item, index) {

			console.log(item, index);
			if (index == 0)
				array.push([ {
					'v' : 'root',
					'f' : cardhtml(item)
				}, '', '' ]);
			else
				array.push([ {
					'v' : 'child' + index,
					'f' : cardhtml(item)
				}, 'root', '' ]);
		});

		data.addRows(array);

		/*
		 * data.addRows([ [{'v':'e1', 'f':cardhtml('鴻海大樓(亞太電信總部)','0')},'', ''],
		 * [{'v':'e21', 'f':cardhtml('2F-亞太第一會議室-itouch','1')},'e1', ''],
		 * [{'v':'e22', 'f':cardhtml('2F-亞太行政室-itouch','2')},'e1', ''] ]);
		 */

		// Create the chart.
		var chart = new google.visualization.OrgChart(document
				.getElementById('org_building'));
		// Draw the chart, setting the allowHtml option to true for the
		// tooltips.

		function selectHandler() {
			/*
			 * var selectedItem = chart.getSelection()[0]; if (selectedItem) {
			 * var value = data.getValue(selectedItem.row, selectedItem.column);
			 * alert('The user selected ' + value); }
			 */

			/*
			 * var selection = chart.getSelection(); if (selection.length > 0) {
			 * var c = selection[0]; showCurrentDetailCard(data.getValue(c.row,
			 * 0)); }
			 */

			var selectedItem = chart.getSelection()[0];
			if (selectedItem) {
				// var selectedValue = data.getFormattedValue(selectedItem.row,
				// 0);
				// console.log('The user selected ' + selectedValue);
				// alert(selectedValue)
				var SelectedValue = data.getValue(selectedItem.row, 0);

				alert(SelectedValue + ',' + selectedItem.row)

				childs1 = chart.getChildrenIndexes(selectedItem.row);
				alert(childs1)

				data.removeRow(selectedItem.row);
				chart.draw(data, {
					allowHtml : true,
					size : 'large',
					nodeClass : 'myNodeClass',
					selectedNodeClass : 'mySelectedNodeClass'
				});

			}

		}

		// google.visualization.events.addListener(chart, 'select',
		// selectHandler);

		chart.draw(data, {
			allowHtml : true,
			size : 'large',
			nodeClass : 'myNodeClass'
		});

	}

}

function ShowCustChildTbl(custData) {

	var t = $('#custchild_tables').DataTable({
		searching : false,
		paging : false,
		info : false,
		data : custData.company,
		columns : [ {
			data : null
		}, {
			data : 'company_name'
		} ],
		columnDefs : [ {
			"searchable" : false,
			"orderable" : false,
			"title" : "序號",
			"targets" : 0,
			"width" : "5%",
			"className" : 'dt-center'
		}, {
			"title" : "地點名稱",
			"targets" : 1,
			"width" : "10%",
			"className" : 'dt-center'
		}

		],

		language : {
			"sProcessing" : "處理中...",
			"sLengthMenu" : "顯示 _MENU_ 項結果",
			"sZeroRecords" : "沒有匹配結果",
			"sInfo" : "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
			"sInfoEmpty" : "顯示第 0 至 0 項結果，共 0 項",
			"sInfoFiltered" : "(由 _MAX_ 項結果過濾)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中數據為空",
			"sLoadingRecords" : "載入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首頁",
				"sPrevious" : "上頁",
				"sNext" : "下頁",
				"sLast" : "末頁"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}

	});

	t.on('order.dt search.dt', function() {
		t.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();

}

function AppendAccoutSelect(location_data) {

	OptionSelect = '';

	for (var j = 0; j < location_data.location.length; j++) {
		console.log(location_data.location[j].name);

		OptionSelect += "<option value='"
				+ location_data.location[j].location_id + "'>"
				+ location_data.location[j].name + "</option>"
	}

}

function CallLocationInfoAPIOK(location_data) {

	LocInfo = location_data;

	// ShowCustChildTbl(LocInfo.location);

	ShowCustChildTbl(CompanyListInfo);
	ShowBuildingChart(LocInfo);
	AppendAccoutSelect(LocInfo)
	CallAccListApi();

}
