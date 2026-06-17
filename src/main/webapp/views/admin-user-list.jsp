<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>顧客管理システム - 管理者専用</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div class="menu-container admin-customer-container">

		<header class="list-header">
			<h1>
				顧客管理システム <span>Customer Control Panel</span>
			</h1>
			<div class="header-user-nav">
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-nav">管理者メニューへ戻る</a>
			</div>
		</header>

		<div class="item-form-box customer-list-box">
			<h4>登録会員一覧（全件）</h4>
			<p>現在データベースに登録されているすべてのユーザーです。情報の変更・削除（強制退会）が可能です。</p>

			<table class="admin-user-table">
				<thead>
					<tr>
						<th class="col-id">ID</th>
						<th class="col-name">会員名（氏名）</th>
						<th class="col-status">区分</th>
						<th class="col-email">メールアドレス</th>
						<th class="col-rank">会員ランク</th>
						<th class="col-action">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${userList}">
						<%-- 1. 管理者（ランク5）は一覧に表示しない --%>
						<c:if test="${user.meatRank != 5}">
							<tr>
								<td><strong><c:out value="${user.memberId}" /></strong></td>
								<td><span class="user-name-bold"> <c:out
											value="${user.lastName}" /> <c:out value="${user.firstName}" />
								</span></td>
								<td><span class="badge-user-status user-status-general">一般会員</span></td>
								<td><c:out value="${user.mailAddress}" /></td>
								<td><span class="user-meat-rank-text"><c:out
											value="${user.meatRankName}" /></span></td>
								<td><a
									href="${pageContext.request.contextPath}/AdminUserEdit.action?targetId=${user.memberId}"
									class="btn-user-action btn-user-edit">編集</a> <c:set
										var="escapedUserName">
										<c:out value="${user.lastName} ${user.firstName}" />
									</c:set> <a
									href="${pageContext.request.contextPath}/AdminDeleteConfirm.action?targetId=${user.memberId}"
									class="btn-user-action btn-user-delete">削除</a></td>
							</tr>
						</c:if>
					</c:forEach>

				</tbody>
			</table>
		</div>

	</div>

</body>
</html>