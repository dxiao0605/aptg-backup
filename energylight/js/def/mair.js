
var location_data;

var locations = [];
var floor = [[]];
var office = [[[]]];

var device = [];
var splitstr = "&&"

var aircon = [];

	$(document).ready(function () {
		
		
	
	});

//some code
	function aftermenu(){
		
		CallAirConInfo();
	}	
	
	

function processAirconInfo(aircon_data)
{
		
	Showtable(aircon_data.devices)
	ShowOther();
	
}

function ShowOther()
{

	ShowHeaderName();
	$('#energy').collapse('show');
}

function Showtable(aircon_data) {

			
		$('#airconTbl').DataTable({
			data: aircon_data,
			columns: [
			{data: 'Location'},
			{data: 'alias'},
			{data: 'voltage'},
			{data: 'ampere'},
			{data: 'active_power'},
			{data: 'positive_accum'},
			{data: 'a_phase_voltage'},
			{data: 'a_phase_ampere'},
			{data: 'b_phase_voltage'},
			{data: 'b_phase_ampere'},
			{data: 'c_phase_voltage'},
			{data: 'c_phase_ampere'}
				
				],
			destroy: true,
			columnDefs: [
				 { "visible": false, "targets": [12,13,14,15,16] },
				
				{
					"title": "隸屬場域",
					"data": "null",
					"targets": 0,
					"defaultContent": "艾可智能",
					"width": "10%"
				}, {
					"title": "裝置名稱",
					//"data": "null",
					"targets": 1,
					//"defaultContent": "艾可智能空調",
					"width": "13%"
				}, {
					"title": "電壓(V)",
					"targets": 2
				}, {
					"title": "電流(A)",
					"targets": 3
				},{
					"title": "耗能功率(w)",
					"targets": 4
				},{
					"title": "用電度數(+kWh)",
					"targets": 5
				},
				{
					"title": "三向(R)電壓(V)",
					"targets": 6
				},
				{
					"title": "三向(R)電流(A)",
					"targets": 7
				},
				{
					"title": "三向(S)電壓(V)",
					"targets": 8
				},
				{
					"title": "三向(S)電流(A)",
					"targets": 9
				},{
					"title": "三向(T)電壓(V)",
					"targets": 10
				},
				{
					"title": "三向(T)電流(A)",
					"targets": 11
				},
				
				{
					"targets": 12,
					"title": "電源",
					"data": "null",
					"width": "8%",
					"defaultContent": "<i class='fa fa-power-off mt-2' style='color:green;'></i><i class='fa fa-power-off mt-2 ml-3' style='color:grey'></i>"
				},
				{
					"targets": 13,
					"title": "冷房",
					"data": "null",
					"width": "8%",
					"defaultContent": "<i class='fas fa-snowflake mt-2 ml-3' style='color:blue'></i>"
				},
				{
					"targets": 14,
					"title": "送風",
					"data": "null",
					"width": "8%",
					"defaultContent": "<i class='fas fa-wind mt-2 ml-3' style='color:orange'></i>"
				},
				{
					"targets": 15,
					"title": "溫度調控",
					"data": "null",
					"width": "10%",
					"defaultContent": "<select name='YourLocation' class='ml-1' > 　<option value='1'>強冷</option> 　<option value='2'>中冷</option> 　<option value='3'>弱冷</option>　<option value='4'>送風</option> </select>"
				},
				{
					"targets": 16,
					"title": "管理",
					"data": "null",
					"width": "10%",
					"defaultContent": "<i class='fas fa-edit mt-2 ml-3' style='color:Violet'></i>"
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

























