<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html class="no-js" lang="zh-TW">

<head>
<jsp:include page="/include/title.jsp" />
<jsp:include page="/include/head.jsp" />

<!-- Style Sheets -->
<link rel="stylesheet" href="http://esp.aptg.com.tw/web/old/css/business/layout.css" />
<link rel="stylesheet" href="http://esp.aptg.com.tw/web/assets/css/apt-unit.css" />
<link rel="stylesheet" href="http://esp.aptg.com.tw/web/assets/css/esp.css" />
<link rel="stylesheet" href="http://esp.aptg.com.tw/web/assets/css/responsive.css" />
<link rel="stylesheet" href="http://esp.aptg.com.tw/web/old/css/ui-lightness/jquery-ui-1.8.22.custom.css" />
<link rel="stylesheet" href="http://esp.aptg.com.tw/web/old/css/adjust.css" />

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
        <script src="/error/assets-cp/js/html5shiv.js"></script>
        <![endif]-->
<!--[if (gte IE 6)&(lte IE 8)]>
        <script type="text/javascript" src="/error/assets-cp/js/selectivizr-min.js"></script>
        <![endif]-->

</head>

<body class="apt">
	<div class="wrapper">
		<!-- Header
	========================================================================================-->
		<header>
			<jsp:include page="/include/header.jsp" />
		</header>

		<!-- Contents
	========================================================================================-->
		<div class="container main">
			<!-- [.apt-contents]start -->
			<div class="row-fluid apt-contents">

				<!--Start Body************************************************* -->
				<div class="page-404">
					<h3>系統公告</h3>
					<div class="page-404-text">
						<p>
							很抱歉 <br> 系統無法找到該頁 <br> 請您回上一頁重新點選與瀏覽 謝謝您
						</p>
					</div>
				</div>
				<!--End Body************************************************* -->

			</div>
			<!-- [.apt-contents]end -->
		</div>
	</div>

	<!-- Footer
========================================================================================-->
	<footer>
		<jsp:include page="/include/footerA.jsp" />
	</footer>

	<!-- Mobile Mask -->
	<div class="mbi-mask"></div>

	<!-- Javascript
========================================================================================-->

</body>
</html>