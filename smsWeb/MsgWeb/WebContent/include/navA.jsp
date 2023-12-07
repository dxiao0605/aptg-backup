<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- <nav class="navbar navbar-default navbar-fixed-top">
 -->


<!-- //Header -->
<header class="gt-header pg-content" data-offset-top="30">
	<div class="container">
		<div class="gt-brand">
			<h1 class="brand-logo-gt">
				<a href="/" title="亞太電信官方網站"><img src="<%=request.getContextPath()%>/img/gt/gt-logo.png"
					alt="亞太電信官方網站"></a>
			</h1>

			<h2 class="brand-logo-aptg">
				<a href="https://www.aptg.com.tw/my/" title="亞太電信"><img
					src="<%=request.getContextPath()%>/img/gt/apt-txt-logo.png" alt="亞太電信"></a>
			</h2>
		</div>

		<div class="gt-top-nav">
			<div class="pull-right gt-nav-level-one">
				<ul class="nav nav-pills">
					<li><a
						href="https://eshop.aptg.com.tw/konakart/Welcome.action"
						target="_self" title="網路門市" g="ecare">網路門市</a></li>
					<li class=""><a href="https://www.aptg.com.tw/my/"
						title="行動用戶" g="mobile">行動用戶</a></li>
					<li class=""><a href="https://www.aptg.com.tw/home/"
						title="家庭服務" g="home">家庭服務</a></li>
					<li class="active"><a href="https://www.aptg.com.tw/vas/"
						title="加值服務" g="vas">加值服務</a></li>
					<li class=""><a href="https://www.aptg.com.tw/esp/"
						title="企業服務" g="esp">企業服務</a></li>
					<li class=""><a href="https://www.aptg.com.tw/smartlife/"
						title="智慧生活" g="smartlife">智慧生活</a></li>
					<li><a href="https://www.aptg.com.tw/ecare/ecHome.seam"
						title="會員中心">會員中心</a></li>
					<li><a href="https://www.aptg.com.tw/" title="回官網首頁">回官網首頁</a></li>
				</ul>
			</div>
		</div>


		<div class="gt-nav-level-tow">

			<div class="nav-nemu">
				<ul class="con-menu">
			    	<li class="link-menu"><a title="加值服務" href="http://contact.aptg.com.tw/"> 	個人通訊錄</a></li>
					<li class="link-menu"><a title="加值服務" href="<%=request.getContextPath()%>/indexA.jsp"> 	傳送訊息</a></li>
					<li class="link-menu"><a title="加值服務" href="<%=request.getContextPath()%>/webpolicy.jsp"> 	服務說明</a></li>
					
						


					<c:choose>
						<c:when test="${not empty sessionScope.profile.mdn}">
						<li class="link-menu"><a title="加值服務"> 個人資料匣<span class="caret"></span></a>
						<div class="submenucontainer singlemenu">
							<ul class="nav navbar-nav">
								<li class="dropdown dropdown-submenu"><a href="<%=request.getContextPath()%>/secured/inboxA.jsp" target="_self" title="寄件匣">寄件匣</a></li>
								<li class="dropdown dropdown-submenu"><a href="<%=request.getContextPath()%>/secured/draftboxA.jsp" target="_self" title="草稿匣">草稿匣</a></li>
							    <li class="dropdown dropdown-submenu"><a href="<%=request.getContextPath()%>/secured/History.jsp" target="_self" title="草稿匣">歷史資料</a></li>
							</ul>
						</div></li>
						
							<li><h4>${sessionScope.profile.mdn}
									您好！</h4></li>
							<li><a href="<c:url value='/secured/logout.jsp'/>"><span
									class="glyphicon glyphicon-user"></span> 登出</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<c:url value='/secured'/>"><span
									class="glyphicon glyphicon-user"></span> 用戶登入</a></li>
						</c:otherwise>
					</c:choose>
					
					

				</ul>
			</div>

		</div>


	</div>
</header>
<!-- //Contents -->





<!-- //Footer -->



<!--  
</nav>
-->
