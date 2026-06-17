<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>削除確認：パスワード入力</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div
		class="menu-container admin-customer-container delete-confirm-page">
		<header class="list-header">
			<h1>
				削除確認 <span>Delete Account</span>
			</h1>
		</header>

		<div class="item-form-box validated-form">
			<p>
				<strong>警告:</strong> アカウントの削除を実行します。<br>
				実行するには、あなた自身のパスワードを入力して本人確認を行ってください。
			</p>

			<form
				action="${pageContext.request.contextPath}/AdminUserDelete.action"
				method="POST">
				<input type="hidden" name="targetId" value="${param.targetId}">

				<div class="form-group">
					<label for="currentAdminPassword">現在のパスワード（本人確認用）:</label> <input
						type="password" name="password" id="currentAdminPassword">

					<button type="button" id="toggle-password-admin-current"
						class="toggle-password-btn">
						<svg id="eye-icon-admin-current" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2">
                            <path
								d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                            <line x1="1" y1="1" x2="23" y2="23"></line>
                        </svg>
					</button>

					<span id="currentAdminPassword-error" class="error-msg"></span>
				</div>

				<div class="btn-area">
					<button type="submit" class="btn-user-action btn-user-delete"
						onclick="return confirm('本当に会員「${targetUser.lastName} ${targetUser.firstName}」様を削除してもよろしいですか？\nこの操作は取り消せません。');">
						削除を実行する</button>
					<a href="${pageContext.request.contextPath}/AdminList.action"
						class="btn-menu">キャンセル</a>
				</div>
			</form>
		</div>
	</div>

	<script src="${pageContext.request.contextPath}/js/user-validation.js"></script>
	<script src="${pageContext.request.contextPath}/js/password-toggle.js"></script>
</body>
</html>