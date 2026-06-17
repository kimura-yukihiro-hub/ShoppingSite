<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><c:out value="${item.itemName}" /> - 厳選肉専門店</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="item-detail-page">

	<div class="list-container">

		<header class="list-header">
			<h1>
				厳選肉専門店 <span>Premium Meat Selection</span>
			</h1>
			<div class="header-user-nav">
				<c:choose>
					<c:when test="${not empty sessionScope.loginUser}">
						<span class="welcome-msg">ようこそ、<strong><c:out
									value="${sessionScope.loginUser.lastName}" /> <c:out
									value="${sessionScope.loginUser.firstName}" /></strong> 様
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

		<main class="detail-layout">

			<div class="detail-image-block">
				<c:set var="imgFile"
					value="${not empty item.imagePath ? item.imagePath : 'no-image.jpg'}" />
				<img
					src="${pageContext.request.contextPath}/img/<c:out value='${imgFile}' />"
					alt="<c:out value='${item.itemName}' />" class="detail-img"
					onerror="this.src='${pageContext.request.contextPath}/img/no-image.jpg';">

			</div>

			<div class="detail-info-block">

				<div class="info-text-area">
					<div class="item-detail-badges">
						<span
							class="badge-meat ${item.meatStatus == '熟成' ? 'status-aged' : 'status-fresh'}"><c:out
								value="${item.meatStatus}" /></span> <span
							class="badge-meat type-${item.meatType}"><c:out
								value="${item.meatType}" />肉・<c:out value="${item.category}" /></span>
					</div>
					<h2 class="detail-title">
						<c:out value="${item.itemName}" />
					</h2>
					<p class="detail-desc">
						<c:out value="${item.description}" />
					</p>

				</div>


				<div class="purchase-action-box">

					<p
						class="detail-stock ${item.stock ==0 ? 'out-of-stock' : 'in-stock'}">
						<c:choose>
							<c:when test="${item.stock > 0}">
							● 在庫あり （残り <c:out value="${item.stock}" /> パック）
						</c:when>
							<c:otherwise>
							⚠️ 申し訳ありません、現在売り切れです
						</c:otherwise>
						</c:choose>
					</p>

					<c:choose>
						<c:when test="${item.stock > 0}">
							<c:set var="escapedItemName">
								<c:out value="${item.itemName}" />
							</c:set>
							<form action="${pageContext.request.contextPath}/CartAdd.action"
								method="post"
								onsubmit="return confirm('「${escapedItemName}」をカートに追加しますか？');">

								<input type="hidden" name="itemId" value="${item.itemId}">

								<div class="quantity-selector">
									<label for="quantity">購入数量</label> <select name="quantity"
										id="quantity">
										<c:forEach var="i" begin="1"
											end="${item.stock > 10 ? 10 : item.stock}">
											<option value="${i}">${i}</option>
										</c:forEach>
									</select>
								</div>

								<button type="submit" class="btn-action-primary">🛒
									カートに入れる</button>
							</form>
						</c:when>
						<c:otherwise>
							<button class="btn-disabled" disabled>完売いたしました</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</main>

		<div class="footer-nav">
			<a href="${pageContext.request.contextPath}/ItemList.action"> ↩️
				商品一覧画面へ戻る </a>
		</div>
	</div>

</body>
</html>