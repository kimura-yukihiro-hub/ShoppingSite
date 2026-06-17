<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文内容の確認</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="purchase-preview-page">

	<div class="list-container">

		<header class="list-header">
			<h1>
				厳選肉専門店 <span>Premium Meat Selection</span>
			</h1>
			<div class="header-user-nav">
				<span class="welcome-msg">ようこそ、<strong><c:out
							value="${sessionScope.loginUser.lastName}" /></strong> 様
				</span>
			</div>
		</header>

		<main class="preview-main-container">
			<h2>📋 ご注文内容の最終確認</h2>

			<div class="preview-split-layout">

				<div class="preview-left-block">

					<div class="info-confilm-section">
						<h3>🚚 お届け先情報</h3>
						<div class="address-card">
							<%-- 郵便番号がある場合のみ表示（任意対応） --%>
							<c:if test="${not empty sessionScope.loginUser.zipCode}">
								<p class="address-zip">
									〒
									<c:out value="${sessionScope.loginUser.zipCode}" />
								</p>
							</c:if>
							<p class="address-text">
								<c:out value="${sessionScope.loginUser.address}" />
							</p>
							<p class="address-name">
								<strong><c:out
										value="${sessionScope.loginUser.lastName}" /> </strong> 様
							</p>
						</div>
					</div>

					<div class="items-confilm-section">
						<h3>🛒 ご注文商品</h3>
						<div class="preview-items-list">
							<c:forEach var="cartItem" items="${cart}">
								<div class="preview-item-row">
									<img
										src="${pageContext.request.contextPath}/img/${not empty cartItem.item.imagePath ? cartItem.item.imagePath : 'no-image.jpg'}"
										alt="<c:out value='${cartItem.item.itemName}' />"
										class="preview-item-img"
										onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/no-image.jpg';">
									<div class="preview-item-detail">
										<span class="preview-item-title"><c:out
												value="${cartItem.item.itemName}" /></span> <span
											class="preview-item-sub"><c:out
												value="${cartItem.item.meatStatus}" />・<c:out
												value="${cartItem.item.meatType}" />肉・<c:out
												value="${cartItem.item.category}" /></span>
									</div>
									<div class="preview-item-price-qty">
										<span class="preview-item-qty"><fmt:formatNumber
												value="${cartItem.item.price}" pattern="#,###" /> 円 × <c:out
												value="${cartItem.quantity}" /></span> <span
											class="preview-item-subtotal"><fmt:formatNumber
												value="${cartItem.subtotal}" pattern="#,###" /> 円</span>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>

				<div class="preview-right-block">
					<div class="summary-box">
						<h3>💰 お支払い合計</h3>
						<div class="summary-row">
							<span>商品合計(税抜)：</span> <strong><fmt:formatNumber
									value="${not empty totalWithoutTax ? totalWithoutTax : 0}"
									pattern="#,###" /> 円</strong>
						</div>
						<div class="summary-row">
							<span>消費税(8%)：</span> <strong><fmt:formatNumber
									value="${not empty tax ? tax : 0}" pattern="#,###" /> 円</strong>
						</div>
						<div class="summary-row total-row">
							<span>総合計(税込)：</span> <strong class="grand-total-price"><fmt:formatNumber
									value="${not empty grandTotal ? grandTotal : 0}"
									pattern="#,###" /> 円</strong>
						</div>

						<form
							action="${pageContext.request.contextPath}/PurchaseExecute.action"
							method="post" onsubmit="return confirm('注文を確定します。よろしいですか？');"
							class="execute-form">
							<input type="hidden" name="totalWithoutTax"
								value="<c:out value='${totalWithoutTax}'/>"> <input
								type="hidden" name="tax" value="<c:out value='${tax}'/>">
							<input type="hidden" name="grandTotal"
								value="<c:out value='${grandTotal}'/>">
							<button type="submit"
								class="btn-action-primary btn-execute-order">🤝 注文を確定する</button>
						</form>

						<div class="preview-back-area">
							<a href="${pageContext.request.contextPath}/CartList.action"
								class="btn-continue">← カートに戻って修正する</a>
						</div>
					</div>
				</div>
			</div>
		</main>
	</div>

</body>
</html>