<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ご購入完了</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="purchase-success-page">

	<div class="list-container">
		<header class="list-header">
			<h1>
				厳選肉専門店 <span>Premium Meat Selection</span>
			</h1>
			<div class="header-user-nav">
				<span class="welcome-msg"><strong><c:out
							value="${sessionScope.loginUser.lastName}" /></strong> 様 </span>
			</div>
		</header>

		<main class="success-main-container">
			<div class="success-box">
				<div class="success-icon">🤝</div>
				<h2>ご注文ありがとうございました</h2>
				<p class="success-msg">
					お肉の注文手続きがすべて正常に完了いたしました。<br> 職人が厳選した最高品質の 肉を、最高の状態で<br>お届けできるよう、ただちに発送準備に入ります。
				</p>
				<p class="success-sub">商品の到着まで、今しばらく楽しみにお待ちくださいませ。</p>

				<div class="success-details-wrapper">
					<h3>📋 ご購入明細</h3>
					<div class="success-items-list">
						<c:forEach var="cartItem" items="${successCart}">
							<div class="success-item-row">
								<%-- 上段：商品名・数量・金額 --%>
								<div class="success-item-header">
									<span class="success-item-name"><c:out
											value="${cartItem.item.itemName.replaceAll('\\\\s*\\\\(.*\\\\)', '')}" /></span>
									<span class="success-item-qty"> <c:out
											value="${cartItem.quantity}" /> パック
									</span> <span class="success-item-subtotal"> <fmt:formatNumber
											value="${cartItem.subtotal}" pattern="#,###" /> 円
									</span>
								</div>

								<%-- 下段：ロット番号 (headerの外に出す) --%>
								<div class="success-item-lot">
									(ロットNo:
									<c:choose>
										<c:when test="${fn:length(cartItem.serialNumber) > 30}">
											<c:out value="${fn:substring(cartItem.serialNumber, 0, 30)}" />...</c:when>
										<c:otherwise>
											<c:out value="${cartItem.serialNumber}" />
										</c:otherwise>
									</c:choose>
									)
								</div>
							</div>
						</c:forEach>
					</div>

					<div class="success-summary">
						<div class="success-summary-row">
							<span>商品合計(税抜)：</span> <strong><fmt:formatNumber
									value="${not empty successTotalWithoutTax ? successTotalWithoutTax : 0}"
									pattern="#,###" /> 円</strong>
						</div>
						<div class="success-summary-row">
							<span>消費税(8%)：</span> <strong><fmt:formatNumber
									value="${not empty successTax ? successTax : 0}"
									pattern="#,###" /> 円</strong>
						</div>
						<div class="success-summary-row success-total-row">
							<span>総合計(税込)：</span> <strong><fmt:formatNumber
									value="${not empty successGrandTotal ? successGrandTotal : 0}"
									pattern="#,###" /> 円</strong>
						</div>
					</div>
				</div>

				<div class="success-actions">
					<a href="${pageContext.request.contextPath}/UserMenu.action"
						class="btn-action-primary btn-to-menu"> マイページへ戻る </a>
				</div>
			</div>
		</main>
	</div>

</body>
</html>