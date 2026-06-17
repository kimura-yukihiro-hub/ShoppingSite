<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理者一覧・管理</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div class="menu-container admin-customer-container">
		<header class="list-header">
			<h1>
				管理者アカウント管理 <span>Admin Account Control</span>
			</h1>
			<div class="header-user-nav">
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-nav">管理者メニューへ戻る</a>
			</div>
		</header>

		<div class="item-form-box customer-list-box scrollable-list-box">
			<h4>登録されている管理者一覧</h4>
			<table class="admin-user-table">
				<thead>
					<tr>
						<th>ID</th>
						<th>氏名</th>
						<th>メールアドレス</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="admin" items="${adminList}">
						<tr>
							<td><strong><c:out value="${admin.memberId}" /></strong></td>
							<td><c:out value="${admin.lastName}" />
								<c:out value="${admin.firstName}" /></td>
							<td><c:out value="${admin.mailAddress}" /></td>
							<td>
								<%-- 自分自身（ログイン中）には削除ボタンを表示しない --%> <c:choose>
									<c:when
										test="${admin.memberId == sessionScope.loginUser.memberId}">
										<span style="color: #888;">操作不可（ログイン中)</span>
									</c:when>
									<c:otherwise>
										<a
											href="${pageContext.request.contextPath}/AdminDeleteConfirm.action?targetId=${admin.memberId}"
											class="btn-user-action btn-user-delete">削除</a>
									</c:otherwise>
								</c:choose>
								</td>
								</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>