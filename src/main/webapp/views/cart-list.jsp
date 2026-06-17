<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>買い物かご - 厳選肉専門店</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="cart-page">

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

		<main class="cart-main-container">
			<h2>🛒 カートに入っている商品</h2>

			<c:choose>
				<c:when test="${not empty cart}">
					<div class="cart-table-wrapper">
						<table class="cart-table">
							<thead>
								<tr>
									<th>商品画像</th>
									<th>商品名</th>
									<th>価格(税抜)</th>
									<th>数量</th>
									<th>小計</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="cartItem" items="${cart}">
									<tr>
										<td class="td-img"><c:set var="imgFile"
												value="${not empty cartItem.item.imagePath ? cartItem.item.imagePath : 'no-image.jpg'}" />
											<img
											src="${pageContext.request.contextPath}/img/<c:out value='${imgFile}' />"
											alt="" class="cart-item-img"
											onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/no-image.jpg';">
										</td>

										<td class="td-name"><strong class="cart-item-title"><c:out
													value="${cartItem.item.itemName}" /></strong> <span
											class="cart-item-badge"><c:out
													value="${cartItem.item.meatType}" />・<c:out
													value="${cartItem.item.category}" /></span></td>

										<td class="td-price"><fmt:formatNumber
												value="${cartItem.item.price}" pattern="#,###" /> 円</td>

										<td class="td-quantity">
											<form
												action="${pageContext.request.contextPath}/CartUpdate.action"
												method="post" class="inline-form">
												<input type="hidden" name="itemId"
													value="${cartItem.item.itemId}"> <select
													name="quantity" onchange="this.form.submit();"
													class="cart-select">
													<%-- 在庫上限（最大10）までループ --%>
													<c:forEach var="i" begin="1"
														end="${cartItem.item.stock > 10 ? 10 : cartItem.item.stock}">
														<option value="${i}"
															${cartItem.quantity == i ? 'selected' : ''}>${i}
														</option>
													</c:forEach>
												</select>
											</form>
										</td>

										<td class="td-subtotal"><fmt:formatNumber
												value="${cartItem.subtotal}" pattern="#,###" /> 円</td>

										<td class="td-action"><c:set var="escapedItemName">
												<c:out value="${cartItem.item.itemName}" />
											</c:set>
											<form
												action="${pageContext.request.contextPath}/CartDelete.action"
												method="post"
												onsubmit="return confirm('「${escapedItemName}」を買い物かごから削除しますか？');"
												class="inline-form">
												<input type="hidden" name="itemId"
													value="${cartItem.item.itemId}">
												<button type="submit" class="btn-delete-item">❌ 削除</button>
											</form></td>
									</tr>
								</c:forEach>
							</tbody>

						</table>
					</div>

					<div class="cart-summary-area">
						<div class="summary-box">
							<div class="summary-row">
								<span>商品合計(税抜)：</span> <strong><fmt:formatNumber
										value="${subtotalWithoutTax}" pattern="#,###" /> 円</strong>
							</div>

							<c:if var="hasDiscount"
								test="${not empty discountRatePercent && discountRatePercent > 0}">
								<div class="summary-row discount-row">
									<span class="discount-label">✨ <c:out
											value="${rankName}" />特典 (<c:out
											value="${discountRatePercent}" />%OFF)：
									</span> <strong class="discount-price">-<fmt:formatNumber
											value="${discountAmount}" pattern="#,###" /> 円
									</strong>
								</div>
								<div class="summary-row">
									<span>割引後合計(税抜)：</span> <strong><fmt:formatNumber
											value="${totalWithoutTax}" pattern="#,###" /> 円</strong>
								</div>
							</c:if>

							<div class="summary-row">
								<span>消費税(8%)：</span> <strong><fmt:formatNumber
										value="${tax}" pattern="#,###" /> 円</strong>
							</div>
							<div class="summary-row total-row">
								<span>総合計(税込)：</span> <strong class="grand-total-price"><fmt:formatNumber
										value="${grandTotal}" pattern="#,###" /> 円</strong>
							</div>
						</div>

						<div class="summary-actions">
							<a href="${pageContext.request.contextPath}/ItemList.action"
								class="btn-continue">← お買い物を続ける</a>
							<form
								action="${pageContext.request.contextPath}/PurchasePreview.action"
								method="get">
								<button type="submit" class="btn-action-primary">購入手続きへ進む
									➔</button>
							</form>
						</div>
					</div>
				</c:when>

				<c:otherwise>
					<div class="empty-cart-box">
						<p>⚠️ 現在、買い物かごには何も入っていません。</p>
						<a href="${pageContext.request.contextPath}/ItemList.action"
							class="btn-action-primary">商品一覧へ戻る</a>
					</div>
				</c:otherwise>
			</c:choose>
		</main>
	</div>

</body>
</html>