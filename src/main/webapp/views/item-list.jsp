<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>厳選肉専門店 - 商品一覧</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="item-list-page">

	<!-- 1. 画面全体の枠 -->
	<div class="list-container">

		<!-- 2. ヘッダーエリア -->
		<header class="list-header">
			<h1>
				厳選肉専門店 <span>Premium Meat Selection</span>
			</h1>
			<div class="header-user-nav">
				<!-- カート画面へのリンクボタン（バッジ付き） -->
				<a href="${pageContext.request.contextPath}/CartList.action"
					class="btn-cart-nav"> 🛒 カート <%-- カートに商品が入っている時だけ、右上に赤い数字バッジを表示する --%>
					<c:if
						test="${not empty sessionScope.cart and sessionScope.cart.size() > 0}">
						<span class="cart-badge"><c:out
								value="${sessionScope.cart.size()}" /></span>
					</c:if>
				</a>
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
		<!-- 3. 検索」＆「並び替え」のコントロールバー -->
		<div class="control-bar">
			<form action="${pageContext.request.contextPath}/ItemList.action"
				method="get" class="search-sort-form">

				<!-- 検索条件を維持するための隠しパラメータ -->
				<input type="hidden" name="meatType"
					value="<c:out value='${meatType}'/>"> <input type="hidden"
					name="category" value="<c:out value='${category}'/>">

				<!-- 要件：商品名による検索機能 -->
				<div class="search-box">
					<input type="text" name="keyword"
						value="<c:out value='${keyword}'/>" placeholder="お肉の名前で検索...">
					<button type="submit" class="btn-search">🔍 検索</button>
				</div>

				<!-- 要件：価格・商品名のカテゴリでの昇順・降順の並び替え -->
				<div class="sort-box">
					<label for="sortType">並び替え：</label> <select name="sortType"
						id="sortType" onchange="this.form.submit();">
						<option value="item_id_asc"
							${sortType == 'item_id_asc' ? 'selected' : ''}>標準（おすすめ順）</option>
						<option value="new_arrival"
							${sortType == 'new_arrival' ? 'selected' : ''}>新着順</option>
						<option value="price_asc"
							${sortType == 'price_asc' ? 'selected' : ''}>価格の安い順 ↑</option>
						<option value="price_desc"
							${sortType == 'price_desc' ? 'selected' : ''}>価格の高い順 ↓</option>
						<option value="name_asc"
							${sortType == 'name_asc' ? 'selected' : ''}>商品名順（50音）</option>
					</select>
				</div>
			</form>
		</div>

		<!--4. クイック肉種類フィルターナビ -->
		<nav class="category-nav">
			<a
				href="${pageContext.request.contextPath}/ItemList.action?keyword=<c:out value='${keyword}'/>&sortType=<c:out value='${sortType}'/>"
				class="cat-link ${empty meatType ? 'active' : ''}">全商品</a> <a
				href="${pageContext.request.contextPath}/ItemList.action?meatType=牛&keyword=<c:out value='${keyword}'/>&sortType=<c:out value='${sortType}'/>"
				class="cat-link ${meatType == '牛' ? 'active' : ''}">🐂 牛肉</a> <a
				href="${pageContext.request.contextPath}/ItemList.action?meatType=豚&keyword=<c:out value='${keyword}'/>&sortType=<c:out value='${sortType}'/>"
				class="cat-link ${meatType == '豚' ? 'active' : ''}">🐖 豚肉</a> <a
				href="${pageContext.request.contextPath}/ItemList.action?meatType=鶏&keyword=<c:out value='${keyword}'/>&sortType=<c:out value='${sortType}'/>"
				class="cat-link ${meatType == '鶏' ? 'active' : ''}">🐓 鶏肉</a>
		</nav>

		<!--5. 肉のカードが並ぶメイングリッドエリア -->
		<main class="item-grid">
			<c:choose>
				<c:when test="${not empty itemList}">

					<!-- 各商品をループ処理で全自動でカード配置していきます -->
					<c:forEach var="item" items="${itemList}">
						<div class="item-card">

							<div class="item-image-wrapper">
								<img
									src="${pageContext.request.contextPath}/img/<c:out value='${not empty item.imagePath ? item.imagePath : "no-image.jpg"}'/>"
									alt="${item.itemName}" class="item-img"
									onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/no-image.jpg';">

								<!--状態とカテゴリのバッジを独立させて横並びにするコンテナ -->
								<div class="item-badges">
									<span
										class="badge-meat ${item.meatStatus == '熟成' ? 'status-aged' : 'status-fresh'}">${item.meatStatus}</span>
									<span class="badge-meat type-${item.meatType}"><c:out
											value="${item.meatType}" />肉・<c:out value="${item.category}" /></span>
								</div>
							</div>

							<div class="item-info">
								<h3>
									<c:out value="${item.itemName}" />
								</h3>
								<p class="item-desc">
									<c:out value="${item.description}" />
								</p>
								<p class="item-price">
									<fmt:formatNumber value="${item.price}" pattern="#,###" />
									<span>円 (税抜)</span>
								</p>

								<p class="item-stock ${item.stock == 0 ? 'out-of-stock' : ''}">
									在庫：
									<c:choose>
										<c:when test="${item.stock > 0}">
											<c:out value="${item.stock}" /> パック</c:when>
										<c:otherwise>売り切れ</c:otherwise>
									</c:choose>
								</p>

								<div class="card-action-area">
									<a
										href="${pageContext.request.contextPath}/ItemDetail.action?itemId=${item.itemId}"
										class="btn-detail">詳細を見る</a>
								</div>
							</div>
						</div>
					</c:forEach>

				</c:when>
				<c:otherwise>
					<div class="no-items-msg">
						<p>⚠️ 条件に合致するお肉が見つかりませんでした。</p>
						<a href="${pageContext.request.contextPath}/ItemList.action"
							class="btn-action-primary">一覧へ戻る</a>
					</div>
				</c:otherwise>
			</c:choose>
		</main>

	</div>

</body>
</html>
