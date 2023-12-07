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
<link rel="shortcut icon" href="img/gt.ico">
<script src="<%=request.getContextPath()%>/js/jquery/1.12.4/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/dataTables/1.10.16/jquery.dataTables.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap/3.3.7/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/draftbox.js"></script>
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

<script type="text/javascript">
	var a_num = '${sessionScope.profile.mdn}';
	var c_id = '${sessionScope.profile.contractID}';
</script>

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
								<h22>草稿匣</h22>

								<div class="table">
									<!-- <div class="container" style="position: relative;"> -->

										<table id="DraftMsgTbl" class="display compact cell-border"
											cellspacing="0" width="100%">
											<thead>
												<tr>
													<th>ID</th>
													<th>簡訊內容</th>
													<th>創建日期</th>
													<th></th>
												</tr>
											</thead>
										</table>
									<!-- </div> -->
								</div>
							</div>

						</div>

					</div>
				</div>

			</div>
		</div>
	</div>





	<!-- 草稿資訊 start -->
	<div id="ViewDraftMsgModal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header btn-primary">
					<button type="button" class="close" data-dismiss="modal"></button>
					<h4 class="text-center">詳細資料</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<p class="text-info">簡訊內容</p>
							<textarea readonly id="EditDraftMsg" class="form-control"
								rows="5"></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">建立時間</p>
							<input readonly id="CreateDraftTime" class="form-control"
								type="text">
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
	<!-- 草稿資訊 end -->
	<!-- 刪除草稿 start -->
	<div id="DelDraftMsgModal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header btn-danger">
					<button type="button" class="close" data-dismiss="modal"></button>
					<h4 class="text-center">刪除草稿</h4>
				</div>
				<div class="modal-body">
					<form>

						<div class="form-group">
							<p class="text-info">簡訊內容</p>
							<textarea readonly id="DelDraftMsg" class="form-control" rows="5"></textarea>
						</div>
						<div class="form-group">
							<p class="text-info">建立時間</p>
							<input readonly id="DelDraftCreateTime" class="form-control"
								type="text">
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="deldraftconf">確認</button>
					<button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 刪除草稿 end -->


	<%@ include file="/include/footerA.jsp"%>
</body>
</html>