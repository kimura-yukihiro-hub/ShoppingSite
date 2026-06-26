<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品管理一覧 - 管理者専用</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div class="list-container">

		<header class="list-header">
			<h1>
				商品管理システム <span>Product Control Panel</span>
			</h1>
			<div class="header-user-nav">
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-nav">管理者メニューへ戻る</a>
			</div>
		</header>

		<div class="admin-table-container">
			<h3>登録商品一覧（全件）</h3>
			<p>現在データベースに登録されているすべてのお肉です。情報の変更・削除が可能です。</p>
			<c:choose>
				<c:when test="${not empty adminItemList}">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>画像</th>
								<th>商品名</th>
								<th>状態</th>
								<th>種類</th>
								<th>部位</th>
								<th>価格(税抜)</th>
								<th>在庫数</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${adminItemList}">
								<tr>
									<td><strong><c:out value="${item.itemId}"></c:out></strong></td>

									<td><img
										src="${pageContext.request.contextPath}/img/${not empty item.imagePath ? item.imagePath : 'no-image.jpg'}"
										alt="商品画像" class="admin-thumb"
										onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/no-image.jpg';">
									</td>

									<td><c:out value="${item.itemName}" /></td>

									<td><c:choose>
											<%-- 生と熟成の両方がある場合 --%>
											<c:when test="${item.rawStock > 0 && item.agedStock > 0}">
												<span class="badge-status-sm status-mixed-sm">混在</span>
											</c:when>
											<%-- 熟成のみある場合 --%>
											<c:when test="${item.agedStock > 0}">
												<span class="badge-status-sm status-aged-sm">熟成</span>
											</c:when>
											<%-- 生のみある場合 --%>
											<c:when test="${item.rawStock > 0}">
												<span class="badge-status-sm status-fresh-sm">生</span>
											</c:when>
											<%-- 在庫がない場合 --%>
											<c:otherwise>
												<span class="badge-status-sm status-none-sm">在庫なし</span>
											</c:otherwise>
										</c:choose></td>

									<td><c:out value="${item.meatType}" />肉</td>

									<td><c:out value="${item.category}" /></td>

									<td><fmt:formatNumber value="${item.price}"
											pattern="#,###" />円</td>

									<td class="<c:if test='${item.stock == 0}'>out-of-stock</c:if>">
										<c:choose>
											<c:when test="${item.stock == 0}">売り切れ</c:when>
											<c:otherwise>
												<c:out value="${item.stock}" />
												<c:if test="${item.stock < 5}">
													<br>
													<span>(要入荷)</span>
												</c:if>
											</c:otherwise>
										</c:choose>
									</td>

									<td class="<c:if test='${item.stock == 0}'>out-of-stock</c:if>">
										<c:choose>
											<c:when test="${item.stock == 0}">売り切れ</c:when>
											<c:otherwise>
												<strong>合計: <c:out value="${item.stock}" />個
												</strong>
												<br>
												<span> (生: ${item.rawStock} / 熟成: ${item.agedStock})
												</span>
												<c:if test="${item.stock < 5}">
													<br>
													<span class="text-danger">(要入荷)</span>
												</c:if>
											</c:otherwise>
										</c:choose>
									</td>

									<td><a
										href="${pageContext.request.contextPath}/ItemEdit.action?itemId=${item.itemId}"
										class="btn-action-sm btn-edit-sm">編集</a> <c:set
											var="escapedName">
											<c:out value="${item.itemName}" />
										</c:set> <a
										href="${pageContext.request.contextPath}/ItemDelete.action?itemId=${item.itemId}"
										class="btn-action-sm btn-delete-sm"
										onclick="return confirm('商品「${escapedName}」を完全に削除します。よろしいですか？\n※この操作は取り消せません。');">削除</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="no-item-msg">
						<p>⚠️ 現在、登録されている商品が1件もありません。</p>
						<a href="${pageContext.request.contextPath}/views/item-add.jsp"
							class="btn-menu btn-admin-add">最初の新しい商品を登録する</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>