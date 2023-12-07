<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="img/gt.ico">

<!-- Bootstrap -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleA.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/gt/gt-unit.css">



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

                                <h22>服務說明</h22>
								<h5>「SMS簡訊」費率:</h5>
								<table class="table table-bordered">
									<thead>
										<tr>
											<th scope="col">發送對象</th>
											<th scope="col">單則(4G用戶發送)</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>網內</td>
											<td>$1元/則</td>
										</tr>
										<tr>
											<td>網外</td>
											<td>$2元/則</td>
										</tr>
										<tr>
											<td>國際</td>
											<td>$5元/則</td>
										</tr>
									</tbody>
								</table>


								<h5>「SMS簡訊」預約功能說明:</h2>
								<div class="h4">可預約三個月內之文字簡訊。預約簡訊發送當時，若用戶為非正常使用中之狀態 (如：限話或停話.....等)
									，則該筆預約簡訊不會成功發送且不計費。</div>

								<h5>適用用戶說明:</h5>
								<div class="h4">本公司4G月租型行動用戶；預付卡用戶無法使用訊息便利站。</div>

								<h5>其他說明:</h5>

								<ol class="h4">
									<li>「訊息便利站」服務目前可傳送簡訊給亞太行動及中華電信、台灣大哥大、遠傳、台灣之星(威寶)等業者之行動電話。</li>
									<li>發送國際簡訊時門號字首需輸入「002」+國碼以供判別，例如韓國的國碼為82，而您想發送訊息的門號為9876543210，則門號請輸入002829876543210。</li>
									<li>單一則訊息純英文數字為160字，中文或與英數字混合為70字。
										長簡訊每一則訊息純英文數字為153字，中文或與英數字混合為63字。單次發送簡訊文字內文，以切割為12則簡訊發送為上限。為提供長簡訊的傳送，訊息便利站會進行簡訊的編碼(ＭN/12)，讓您的朋友一目了然長簡訊依序的內容。</li>
									<li>按下「確定傳送」後，網站便將使用者之簡訊送至後端簡訊系統，由後端簡訊系統負責處理發送。</li>
									<li>簡訊流量會因假日或節慶而增加，因此實際發送時間將視簡訊系統流量大小而有所誤差。</li>
									<li>若當時簡訊無法送至接收端手機(收訊不佳或關機)，簡訊系統將自動重試發送，直到24小時後仍無收到來自接收端手機之收訊成功回應，便自動放棄傳送。</li>
									<li>若您已設定''限撥簡訊(意即手機取消發送簡訊服務)''之功能，亦無法透過訊息便利站服務發送訊息給親朋好友。</li>
									<li>【慈濟專案/其他簡訊優惠專案】的優惠折扣範圍僅限於手機端發送使用，若由「訊息便利站」網頁所發送之簡訊恕無法享有【慈濟專案/其他簡訊優惠專案】所提供之簡訊優惠，將依現行簡訊牌價費率計收。</li>
									<li>請勿使用在不正當之用途上，若因此產生法律糾紛或犯罪行為，本公司有義務提供相關通訊記錄給檢警單位。</li>


								</ol>



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