<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員ランク特典一覧</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a${sessionScope.loginUser.meatRank} user-page">

	<div class="menu-container">
		<div class="menu-left">
			<div class="user-card">
				<h3>会員ランク特典一覧</h3>
				<p>
					現在のランク：<strong>${sessionScope.loginUser.meatRankName}</strong>
				</p>
			</div>
			<div class="rank-circle-container">
				<div class="rank-circle">
					<svg viewBox="0 0 36 36" class="circular-chart">
                <%-- VIP(ランク4)ならグラデーション定義を挿入 --%>
                <c:if test="${sessionScope.loginUser.meatRank == 4}">
                    <defs>
                        <linearGradient id="vipGradient" x1="0%" y1="0%"
								x2="100%" y2="0%">
                            <stop offset="0%"
								style="stop-color:#bf953f;" />
                            <stop offset="50%"
								style="stop-color:#fcf6ba;" />
                            <stop offset="100%"
								style="stop-color:#b38728;" />
                        </linearGradient>
                    </defs>
                </c:if>

                <path class="circle-bg"
							d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
                <path class="circle"
							data-progress="${rankProgressPercent}"
							d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
            </svg>

					<div class="rank-text">
						<strong class="rank-text-color">${sessionScope.loginUser.meatRankName}</strong>
						<p>${remainingMessage}</p>
					</div>
				</div>
			</div>
		</div>

		<div class="menu-right">
			<nav class="rank-guide-container">
				<ul class="rank-list">
					<li class="rank-card">
						<h3>レギュラー会員</h3>
						<p class="off-rate">特典なし</p>
					</li>
					<li class="rank-card">
						<h3>ゴールド会員</h3>
						<p class="off-rate">2% OFF</p>
					</li>
					<li class="rank-card">
						<h3>プラチナ会員</h3>
						<p class="off-rate">5% OFF</p>
					</li>
					<li class="rank-card is-vip">
						<h3>VIP会員</h3>
						<p class="off-rate vip-color">10% OFF</p>
					</li>
				</ul>
			</nav>

			<div class="rank-rules">
				<p>※ランクは購入実績に応じて、条件を達成した時点でリアルタイムに更新されます。</p>
				<p>※最終購入日から180日間ご利用がない場合、ランクが降格する場合がございます。あらかじめご了承ください。</p>
			</div>

			<div class="button-area">
				<a href="${pageContext.request.contextPath}/UserMenu.action"
					class="btn-menu">マイページへ戻る</a>
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