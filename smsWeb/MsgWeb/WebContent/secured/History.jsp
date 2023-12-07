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
<script src="<%=request.getContextPath()%>/js/smshistory.js"></script>
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
								<h22>歷史資料</h22>

								<div class="table">
									<!-- <div class="container" style="position: relative;"> -->

										<table id="HistoryTbl" class="display compact cell-border"
											cellspacing="0" width="100%">
											<thead>
												<tr>
													<th>ID</th>
													<th>傳送門號</th>
													<th>簡訊內容</th>
													<th>傳送時間</th>
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





	
	


	<%@ include file="/include/footerA.jsp"%>
</body>
</html>