<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gt 智慧生活-訊息便利站</title>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/gt.ico">

<!-- Bootstrap -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/gt/gt-unit.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jqui/jquery-ui.css">
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/css/jquery.tagit.css">
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/css/pretty-checkbox.min.css">
<link rel="stylesheet" type="text/css" 	href="<%=request.getContextPath()%>/css/swiper.min.css">
<link rel="stylesheet" type="text/css" 	href="<%=request.getContextPath()%>/css/bp_datepicker/bootstrap-datepicker3.min.css">

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleA.css">


<!--[if lt IE 9]>
	<script src="js/html5shiv.min.js"></script>
	<script src="js/respond.min.js"></script>
   <![endif]-->

<script type="text/javascript">
	var a_num = '${sessionScope.profile.mdn}';
	var c_id = '${sessionScope.profile.contractID}';
</script>

</head>
<body>
	<div class="gt gt-cp">
		<div class="gt-wrapper">
			<%@ include file="/include/navA.jsp"%>

			<div class="gt-contents pg-content-nopadding">
				<div class="container">
					<div class="row">
						<div class="col-md-12 main">

							<div class="section text-content">
								<h22> <span>STEP 1</span> 編輯訊息 </h22>
								<p>
									輸入訊息內容：
									<!-- <a class="btn btn-primary btn-xs" target="_blank" 	href="#"><span class="glyphicon glyphicon-alert"></span> 使用須知</a> -->
									<a class="btn btn-success btn-xs" id="savedraft"
										target="_blank"><span
										class="glyphicon glyphicon-folder-close"></span>儲存草稿</a>
								</p>
								<textarea rows="3" name="msgct"></textarea>
								<div class="msg" style="margin-bottom: 20px;">
									您目前發送的訊息種類為簡訊，輸入 <span class="charnum">0</span> 字元，則數 <span
										class="amount">0</span> 則。<br>
									如您發送的內容則數超過1則，為讓您的朋友一目了然簡訊依序的內容，系統將主動於每則簡訊內容加入簡訊的編碼。
								</div>
							</div>

							<div class="section phonenum">
								<h22> <span>STEP 2</span> 輸入接收門號</h22>
								<p>
									輸入接收門號：
									<c:choose>
										<c:when test="${not empty sessionScope.profile}">
											<button type="button" class="btn btn-primary btn-xs"
												onclick="openPhoneBook()">
												<span class="glyphicon glyphicon-book"></span> 通訊錄
											</button>
										</c:when>
										<c:otherwise>
											<a class="btn btn-primary btn-xs"
												href="<c:url value='/secured'/>"><span
												class="glyphicon glyphicon-book"></span> 通訊錄</a>
										</c:otherwise>
									</c:choose>
								</p>
								<ul name="phone[]" id="phones"></ul>
								<div class="msg">
									輸入完後按下Enter或空白鍵可再輸入門號，以逗號 ,分隔可輸入多個門號，每次最多同時發送100個門號，已加入 <span class="num">0</span>
									門
								</div>
							</div>


							<div class="section sendtime">
								<h22> <span>STEP 3</span> 選擇發送時間 </h22>
								<p>選擇發送時間：</p>

								<div class="container-fluid">
									<div class="row">
										<div class="sendnow">
											<div class="pretty p-default p-round p-smooth">
												<input type="radio" name="sendtime" value="1"
													onclick="changeSendTime(1)" checked />
												<div class="state p-success">
													<label>立即發送</label>
												</div>
											</div>

										</div>
										<!-- <div class="sendnow"> -->
										<div class="sendonce">
											<!-- <input class="datepicker" type="text" disabled> -->
											<div class="row row-no-gutters">
												<div class='col-sm-2'>
													<div class="pretty p-default p-round p-smooth">
														<input type="radio" name="sendtime" value="2"
															onclick="changeSendTime(2)" />
														<div class="state p-success">
															<label>單次發送</label>
														</div>
													</div>
												</div>
												<div class='col-sm-3'>
													<div class="form-group">
														<div class='input-group date datepicker_bt' id ='oncedt'>
															<input type='text' class="form-control"  disabled/> <span
																class="input-group-addon"> <span
																class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</div>
												</div>
												<div class='col-sm-3'>
													<select class="hour" disabled></select> 時 <select
														class="minute" disabled></select> 分
												</div>

											</div>
											<!-- <div class="row row-no-gutters"> -->
										</div>
										<!-- <div class="sendonce"> -->

										<div class="sendroutine">

											<div class="pretty p-default p-round p-smooth">
												<input type="radio" name="sendtime" value="3"
													onclick="changeSendTime(3)" />
												<div class="state p-success">
													<label>週期預約發送</label>
												</div>
											</div>
											<div class="row routine-type">
												<div class='col-sm-8 col-sm-offset-1'>
													<div class="day">
														<div class="pretty p-default p-round p-smooth">
															<input type="radio" name="sendroutine_opt" value="1"
																onclick="changeRoutineType(1)" />
															<div class="state p-success">
																<label>每天</label>
															</div>
														</div>
														<select class="hour" disabled></select> 時 <select
															class="minute" disabled></select> 分
													</div>
													<!-- <div class="day"> -->

													<div class="week">
														<div class="pretty p-default p-round p-smooth">
															<input type="radio" name="sendroutine_opt" value="2"
																onclick="changeRoutineType(2)" />
															<div class="state p-success">
																<label>每週</label>
															</div>
														</div>
														<select class="hour" disabled></select> 時 <select
															class="minute" disabled></select> 分
														<div style="padding-left: 20px;">
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Sun" />
																<div class="state p-success-o">
																	<label>星期日</label>
																</div>
															</div>
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Mon" />
																<div class="state p-success-o">
																	<label>星期一</label>
																</div>
															</div>
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Tue" />
																<div class="state p-success-o">
																	<label>星期二</label>
																</div>
															</div>
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Wed" />
																<div class="state p-success-o">
																	<label>星期三</label>
																</div>
															</div>
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Thu" />
																<div class="state p-success-o">
																	<label>星期四</label>
																</div>
															</div>
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Fri" />
																<div class="state p-success-o">
																	<label>星期五</label>
																</div>
															</div>
															<div class="pretty p-default p-curve p-thick p-smooth">
																<input type="checkbox" name="week[]" value="Sat" />
																<div class="state p-success-o">
																	<label>星期六</label>
																</div>
															</div>
														</div>
													</div>
													<!-- <div class="week"> -->

													<div class="month">
														<div class="pretty p-default p-round p-smooth">
															<input type="radio" name="sendroutine_opt" value="3"
																onclick="changeRoutineType(3)" />
															<div class="state p-success">
																<label>每月</label>
															</div>
														</div>
														<select class="hour" disabled></select> 時 <select
															class="minute" disabled></select> 分
														<div class="days" style="padding-left: 20px;"></div>
													</div>
													<!-- <div class="month"> -->



													<div class="routineperiod row">
														    <div class="col-sm-4 startDate">
															<div class="routineperiod">起始日期：</div>
															<div class="form-group">
																<div class='input-group date datepicker_bt' id ='r_stDate'>
																	<input type='text' class="form-control" disabled /> <span
																		class="input-group-addon"> <span
																		class="glyphicon glyphicon-calendar"></span>
																	</span>
																</div>
															</div>
														    </div>
														    
														    <div class="col-sm-4 endDate">
															<div class="routineperiod">結束日期：</div>
															<div class="form-group">
																<div class='input-group date datepicker_bt' id ='r_edDate'>
																	<input type='text' class="form-control" disabled /> <span
																		class="input-group-addon"> <span
																		class="glyphicon glyphicon-calendar"></span>
																	</span>
																</div>
															</div>
														</div>
													</div>

												</div>
												<!-- <div class='col-sm-8 col-sm-offset-1'>  -->
											</div>
											<!--  <div class="row ">  -->
										</div>
										<!--  <div class="sendroutine"> -->

										<center>
											<button type="button" class="btn btn-success"
												onclick="sendSmsJs('${sessionScope.profile.mdn}','${sessionScope.profile.contractID}')">發送訊息</button>
										</center>
									</div>
									<!-- <div class="row"> -->
								</div>
								<!-- <div class="container-fluid"> -->

							</div>
							<!-- <div class="section sendtime"> -->
						</div>
						<!-- <div class="col-md-12 main"> -->
					</div>
					<!-- <div class="row"> -->
				</div>
				<!-- <div class="container"> -->
			</div>
			<!-- <div class="gt-contents pg-content-nopadding"> -->


			<%@ include file="/include/footerA.jsp"%>

		</div>
		<!-- <div class="gt-wrapper"> -->
	</div>
	<!-- <div class="gt gt-cp"> -->





	<!-- 通訊錄跳窗 start -->
	<div class="modal fade" id="phoneBook" role="dialog">
		<div class="modal-dialog">

			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">通訊錄</h4>
				</div>
				<div class="modal-body">

					<!-- tab 分頁 start -->
					<ul class="nav nav-tabs">
						<li class="groups active"><a data-toggle="tab" href="#tab1">群組</a></li>
						<li class="all"><a data-toggle="tab" href="#tab2">全部</a></li>
					</ul>
					<div class="tab-content">
						<div id="tab1" class="tab-pane fade in active">

							<!-- 群組 start -->
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>
												<div class="pretty p-default p-curve p-thick p-smooth">
													<input type="checkbox" class="selectAll" />
													<div class="state p-success-o">
														<label></label>
													</div>
												</div>
											</th>
											<th>群組名稱</th>
											<th>筆數</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${sessionScope.groupAddressBookslist}"
											var="group">
											<tr>
												<td>
													<div class="pretty p-default p-curve p-thick p-smooth">
														<input type="checkbox" value="${group.groupName}" />
														<div class="state p-success-o">
															<label></label>
														</div>
													</div>
												</td>
												<td><c:out value="${group.groupName}" /></td>
												<td><c:out value="${group.groupDesc}" /></td>
												<td><c:out value="${group.count}" /></td>
											</tr>
										</c:forEach>

										<!--  
									<tr>
										<td>
											<div class="pretty p-default p-curve p-thick p-smooth">
										        <input type="checkbox" value="預設群組1" />
										        <div class="state p-success-o"><label></label></div>
										    </div>
										</td>
										<td>預設群組1</td>
										<td>1</td>
									</tr>
									<tr>
										<td>
											<div class="pretty p-default p-curve p-thick p-smooth">
										        <input type="checkbox" value="預設群組2" />
										        <div class="state p-success-o"><label></label></div>
										    </div>
										</td>
										<td>預設群組2</td>
										<td>1</td>
									</tr>
									<tr>
										<td>
											<div class="pretty p-default p-curve p-thick p-smooth">
										        <input type="checkbox" value="預設群組3" />
										        <div class="state p-success-o"><label></label></div>
										    </div>
										</td>
										<td>預設群組3</td>
										<td>1</td>
									</tr>
									-->
									</tbody>
								</table>
							</div>
							<!-- 群組 end -->

						</div>
						<div id="tab2" class="tab-pane fade">

							<!-- 全部 start -->
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>
												<div class="pretty p-default p-curve p-thick p-smooth">
													<input type="checkbox" class="selectAll" />
													<div class="state p-success-o">
														<label></label>
													</div>
												</div>
											</th>
											<th>群組名稱</th>
											<th>暱稱</th>
											<th>號碼</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${sessionScope.contactsList}" var="contact">
											<tr>
												<td>
													<div class="pretty p-default p-curve p-thick p-smooth">
														<input type="checkbox" value="${contact.cell}" />
														<div class="state p-success-o">
															<label></label>
														</div>
													</div>
												</td>
												<td>預設群組</td>
												<td><c:out
														value="${contact.familyName}${contact.givenName}" /></td>
												<td><c:out value="${contact.cell}" /></td>
											</tr>
										</c:forEach>

										<!--  
									<tr>
										<td>
											<div class="pretty p-default p-curve p-thick p-smooth">
										        <input type="checkbox" value="0987654321" />
										        <div class="state p-success-o"><label></label></div>
										    </div>
										</td>
										<td>預設群組1</td>
										<td>暱稱1</td>
										<td>0987654321</td>
									</tr>
									<tr>
										<td>
											<div class="pretty p-default p-curve p-thick p-smooth">
										        <input type="checkbox" value="0987654322" />
										        <div class="state p-success-o"><label></label></div>
										    </div>
										</td>
										<td>預設群組1</td>
										<td>暱稱2</td>
										<td>0987654322</td>
									</tr>
									<tr>
										<td>
											<div class="pretty p-default p-curve p-thick p-smooth">
										        <input type="checkbox" value="0987654323" />
										        <div class="state p-success-o"><label></label></div>
										    </div>
										</td>
										<td>預設群組1</td>
										<td>暱稱3</td>
										<td>0987654323</td>
									</tr>
									-->
									</tbody>
								</table>
							</div>
							<!-- 全部 end -->

						</div>
					</div>
					<!-- tab 分頁 end -->

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success btn-sm"
						onclick="bookConfirm()">確定使用</button>
					<button type="button" class="btn btn-default btn-sm"
						data-dismiss="modal">取消</button>
				</div>
			</div>

		</div>
	</div>
	<!-- 通訊錄跳窗 end -->
	<!-- 儲值草稿 start -->
	<div id="DraftModal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header btn-success">
					<button type="button" class="close" data-dismiss="modal"></button>
					<h4 class="text-center" id="myModalLabel">儲存草稿</h4>
				</div>
				<div class="modal-body">
					<form>

						<div class="form-group">
							<p class="text-info">簡訊內容</p>
							<textarea id="DraftMsg" class="form-control" rows="5"></textarea>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="savt_to_draft">儲存</button>
					<button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 儲值草稿 end -->

	<%--  <script src="<%=request.getContextPath()%>/js/plugins.js"></script> 
	--%>
	<script src="<%=request.getContextPath()%>/js/jquery/1.12.4/jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="<%=request.getContextPath()%>/js/jqui/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
	
	<script src="<%=request.getContextPath()%>/js/bootstrap/3.3.7/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/bp_datepicker/bootstrap-datepicker.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/tag-it/tag-it.min_v1.js"></script> 
	<script src="<%=request.getContextPath()%>/js/functions.js"></script>

</body>
</html>