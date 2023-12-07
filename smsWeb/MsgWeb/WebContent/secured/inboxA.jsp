<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gt 智慧生活-訊息便利站</title>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/gt.ico">


<script type="text/javascript">
	var a_num = '${sessionScope.profile.mdn}';
	var c_id = '${sessionScope.profile.contractID}';
	<%-- var ctx = "<%=request.getContextPath()%>"; --%>
</script>


<script src="<%=request.getContextPath()%>/js/jquery/1.12.4/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/dataTables/1.10.16/jquery.dataTables.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap/3.3.7/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/inbox.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/dataTables/1.10.16/css/jquery.dataTables.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleA.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/gt/gt-unit.css">




<!--[if lt IE 9]>
	<script src="js/html5shiv.min.js"></script>
	<script src="js/respond.min.js"></script>
   <![endif]-->



</head>
<body>


	<div class="gt gt-cp">
		<div class="gt-wrapper">

			<%-- <%@ include file="navA.jsp" %> --%>
			<%@ include file="/include/navA.jsp"%>


			<div class="gt-contents pg-content-nopadding">
				<div class="container">
					<div class="row">
						<div class="col-md-12 main">
							<div class="section text-content">
							<h22>寄件匣</h22>
							<div class="table">
									<!--  <div class="container" style="position: relative;"> -->
									
									<table id="MsgTbl" class="display compact cell-border"
										cellspacing="0" width="100%">
										<thead>
											<tr>
												<th>ID</th>
												<th>門號</th>
												<th>簡訊內容</th>
												<th>建立時間</th>
												<th>傳送時間</th>
												<th>刪除/詳細資料</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>

					</div>

				</div>
			</div>

		</div>

	</div>

	<!-- 訊息資訊 start -->
	<div id="ViewMsgModal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header btn-primary">
					<button type="button" class="close" data-dismiss="modal"></button>
					<h4 class="text-center" id="myModalLabel">詳細資料</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<p class="text-info">傳送門號</p>
							<textarea readonly id="SendMsisdn" class="form-control"
								style="width: 435px; height: 200px;"></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">簡訊內容</p>
							<textarea readonly id="EditMsg" class="form-control" rows="5"></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">建立時間</p>
							<input readonly id="CreateTime" class="form-control" type="text">
						</div>
						<div class="form-group">
							<p class="text-info">傳送時間</p>
							<textarea readonly id='TransTime' class='form-control' rows='5'></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">傳送狀態</p>
							<input readonly id="TransStatus" class="form-control" type="text">
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="forward-tosend">轉寄</button>
					<button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 訊息資訊 end -->
	<!-- 資訊 start -->
	<div id="DelMsgModal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header btn-danger">
					<button type="button" class="close" data-dismiss="modal"></button>
					<h4 class="text-center" id="myModalLabel">刪除訊息</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<p class="text-info">傳送門號</p>
							<textarea readonly id="DelSendMsisdn" class="form-control"
								style="width: 435px; height: 200px;"></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">簡訊內容</p>
							<textarea readonly id="DelEditMsg" class="form-control" rows="5"></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">建立時間</p>
							<input readonly id="DelCreateTime" class="form-control"
								type="text">
						</div>
						<div class="form-group">
							<p class="text-info">傳送時間</p>
							<textarea readonly id='DelTransTime' class='form-control'
								rows='5'></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">傳送狀態</p>
							<input readonly id="DelTransStatus" class="form-control"
								type="text">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="delconf">確認</button>
					<button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>


	<%@ include file="/include/footerA.jsp"%>






</body>
</html>