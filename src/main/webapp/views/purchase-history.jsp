<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>購入履歴</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="history-page">

	<div class="list-container">
		<header class="list-header">
			<h1>
				厳選肉専門店 <span>Premium Meat Selection</span>
			</h1>
			<div class="header-user-nav">
				<c:choose>
					<c:when test="${not empty sessionScope.loginUser}">
						<span class="welcome-msg">ようこそ、<strong><c:out
									value="${sessionScope.loginUser.lastName}" /></strong> 様
						</span>
						<a href="${pageContext.request.contextPath}/UserMenu.action"
							class="btn-nav">マイページ</a>
					</c:when>
					<c:otherwise>
						<span class="welcome-msg">ゲスト 様</span>
						<a href="${pageContext.request.contextPath}/views/login-in.jsp"
							class="btn-nav btn-highlight">ログイン</a>
					</c:otherwise>
				</c:choose>
			</div>
		</header>

		<main class="list-main">
			<h2>購入履歴</h2>
			<hr>

			<c:choose>
				<c:when test="${empty history}">
					<div class="empty-message">
						<p>現在、購入履歴はありません。</p>
						<a href="${pageContext.request.contextPath}/ItemList.action"
							class="btn-nav">商品一覧を見る</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="table-scroll-wrapper">
						<table class="item-table">
							<thead>
								<tr>
									<th>購入日時</th>
									<th>商品名</th>
									<th>ロット番号</th>
									<th>数量</th>
									<th>単価</th>
									<th>合計</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="p" items="${history}">
									<tr>
										<td><fmt:formatDate value="${p.purchaseDate}"
												pattern="yyyy/MM/dd HH:mm" /></td>
										<td><a
											href="${pageContext.request.contextPath}/ItemDetail.action?itemId=${p.itemId}">
												<c:out value="${p.itemName}" />
										</a></td>
										<%-- ロット番号の表示（もし空なら'---'） --%>
										<td>${not empty p.serialNumber ? p.serialNumber : '---'}</td>
										<td><c:out value="${p.quantity}" /></td>
										<td><fmt:formatNumber value="${p.price}" type="number" />円</td>
										<td><fmt:formatNumber value="${p.price * p.quantity}"
												type="number" />円</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:otherwise>
			</c:choose>

			<div class="history-btn">
				<a href="${pageContext.request.contextPath}/UserMenu.action"
					class="btn-nav">マイページへ戻る</a>
			</div>
		</main>
	</div>
</body>
</html>