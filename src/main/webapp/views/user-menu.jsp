<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<script>
	// このマイページが開いた瞬間に、ブラウザのログイン通信の履歴(POST)を、安全な現在のURL(GET)へ強制的に上書きしてすり替えます。
	window.history.replaceState(null, null, window.location.href);
</script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マイメニュー</title>
<link rel="stylesheet" href="/ShoppingSite/css/style.css">
</head>
<body class="rank-a${sessionScope.loginUser.meatRank} user-page">
	<%--  ランク変動通知エリア --%>
	<div class="alert-area">
		<c:set var="displayMsg"
			value="${not empty requestScope.rankUpMessage ? requestScope.rankUpMessage : sessionScope.rankUpMessage}" />

		<c:if test="${not empty displayMsg}">
			<div id="rank-up-toast" class="alert-rank-up">
				<p>${displayMsg}</p>
			</div>
			<%-- 表示後に消す処理 --%>
			<c:remove var="rankUpMessage" scope="session" />
			<c:remove var="rankUpMessage" scope="request" />
		</c:if>
	</div>

	<%-- ランクダウン：固定表示エリア --%>
	<c:if test="${not empty requestScope.rankDownMessage}">
		<div class="alert-rank-down">
			<p>⚠️ ${requestScope.rankDownMessage}</p>
		</div>
	</c:if>
	</div>
	<%--  通知エリアここまで  --%>
	<div class="menu-container">
		<div class="menu-left">
			<div class="user-card">
				<h3>マイページ</h3>
				<p>
					ようこそ、<strong>${sessionScope.loginUser.lastName}</strong>様！
				</p>
			</div>

			<nav class="menu-links">
				<h3>メインメニュー</h3>
				<ul>
					<!-- お買い物（商品一覧）へ進むボタン -->
					<li>
						<button type="button" class="btn-menu btn-shop"
							onclick="location.href='${pageContext.request.contextPath}/ItemList.action'">🛒
							お買い物へ進む</button>
					</li>
					<li>
						<button type="button" class="btn-menu btn-history"
							onclick="location.href='${pageContext.request.contextPath}/PurchaseHistory.action'">購入履歴</button>
					<li>
						<button type="button" class="btn-menu btn-update"
							onclick="location.href='${pageContext.request.contextPath}/UserUpdateForm.action'">修正</button>
					</li>
					<li>
						<button type="button" class="btn-menu btn-delete"
							onclick="location.href='${pageContext.request.contextPath}/UserDelete.action'">削除</button>
					</li>
					<%--JavaScriptの confirm を使い、ユーザーが「OK」を押したときだけログアウトする --%>
					<li>
						<button type="button" class="btn-menu btn-logout"
							onclick="if(confirm('本当にログアウトしますか？')) { location.href='${pageContext.request.contextPath}/Logout.action'; }">ログアウト</button>
					</li>
				</ul>
			</nav>
		</div>
		<div class="menu-right">
			<div class="rank-circle-container">
				<div class="rank-circle">
					<svg viewBox="0 0 36 36" class="circular-chart">
        <%-- VIP(ランク4)の時だけグラデーションを定義 --%>
        <c:if test="${sessionScope.loginUser.meatRank == 4}">
            <defs>
                <linearGradient id="vipGradient" x1="0%" y1="0%"
								x2="100%" y2="0%">
                    <stop offset="0%" style="stop-color:#bf953f;" />
                    <stop offset="50%" style="stop-color:#fcf6ba;" />
                    <stop offset="100%" style="stop-color:#b38728;" />
                </linearGradient>
            </defs>
        </c:if>

        <path class="circle-bg"
							d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
        <path class="circle"
							data-progress="${sessionScope.loginUser.totalPurchaseAmount == 0 ? 0 : rankProgressPercent}"
							d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
    </svg>

					<div class="rank-text">
						<div class="rank-title-wrapper">
							<strong class="rank-text-color">${sessionScope.loginUser.meatRankName}</strong>
							<a href="${pageContext.request.contextPath}/RankGuide.action"
								class="rank-info-icon" title="ランク特典詳細">?</a>
						</div>
						<p
							class="rank-message ${sessionScope.loginUser.meatRank >= 4 ? 'hidden' : ''}">
							${remainingMessage}</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		// JSPでセッション値をJS変数に変換してから読み込む
		const rankUpMessage = "${sessionScope.rankUpMessage}";
	</script>
	<script src="${pageContext.request.contextPath}/js/my-page.js"></script>
</body>
</html>