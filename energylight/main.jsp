<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta name="generator"
	content="HTML Tidy for HTML5 for Windows version 5.6.0" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>亞太電信-能源平台</title>
<link rel="stylesheet" href="css/pub/bootstrap.min.css" />
<link rel="stylesheet" href="css/pub/all.css" />
<link href="css/pub/bootstrap-slider.css" rel="stylesheet" />
<link rel="stylesheet" href="css/pub/pretty-checkbox.min.css"  />
<link rel="stylesheet" href="css/pub/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/def/main.css" />
<link rel="stylesheet" href="css/def/common.css" />

<script src="js/pub/jquery.min.js"></script>
<script src="js/pub/bootstrap.min.js"></script>
<script src="js/pub/bootstrap-slider.js"></script>
<script src="js/pub/jquery.dataTables.min.js"></script>
<script src="js/pub/canvasjs.min.js"></script>
<script src="js/pub/jsonpath-0.8.0.js"></script>

<script src="js/def/common.js"></script>
<script src="js/def/main.js"></script>



</head>
<body>

	<c:if test="${empty sessionScope.name}">
		<c:set var="name" value='${param["accname"]}' scope="session" />
	</c:if>



	<%@ include file="include/header.jsp"%>


	<div class="container-fluid">
		<div class="row">
			<%@ include file="include/sidemenu.jsp"%>

			<div class="col" id="main">
				<div class="container-fluid ml-4">
					<div class="row">
						<div class="col-lg-8">
							<div class="row ml-2 mt-3">
							
							<ol class="breadcrumb">
								
							</ol>
								
							</div>

							
							<div class="row ml-4 mb-4">
								<div class="row " id="cards"></div>
							</div>
						</div>

						

					</div>

				</div>
			</div>
		</div>
	</div>

	<%@ include file="include/popup.jsp"%>
	<%@ include file="include/setdev.jsp"%>
	<%@ include file="include/linechart.jsp"%>
</body>
</html>
