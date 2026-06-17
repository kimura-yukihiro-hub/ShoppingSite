<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退会手続き</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="form-container container-danger">
		<h2 style="color: #ff5252;">⚠️ 退会確認</h2>

		<div
			style="font-size: 13px; opacity: 0.9; line-height: 1.6; text-align: left; margin-bottom: 20px;">
			<p>
				一度退会手続きを完了すると、これまでの<br>注文履歴の確認や、同じ会員番号での<br>ログインが一切できなくなります。
			</p>
			<p>本当に退会してもよろしいですか？</p>
		</div>
		<div class="form-group">
			<label>現在の会員番号 (ID)</label>
			<div class="confirm-value"
				style="border-color: rgba(255, 82, 82, 0.2);">
				<c:out value="${sessionScope.loginUser.memberId}" />
			</div>
		</div>

		<div class="form-group">
			<label>お名前</label>
			<div class="confirm-value"
				style="border-color: rgba(255, 82, 82, 0.2);">
				<c:out value="${sessionScope.loginUser.lastName}" />
				<c:out value="${sessionScope.loginUser.firstName}" />
			</div>
		</div>

		<div class="form-group">
			<label>メールアドレス</label>
			<div class="confirm-value"
				style="border-color: rgba(255, 82, 82, 0.2);">
				<c:out value="${sessionScope.loginUser.mailAddress}" />
			</div>
		</div>

		<%--実際に削除処理を実行するアクションへポスト --%>
		<form
			action="${pageContext.request.contextPath}/UserDeleteExecute.action"
			method="post" class="validated-form">

			<%-- 誰のデータを消すかを特定するため、会員番号を hidden で送信 --%>
			<input type="hidden" name="memberId"
				value="<c:out value='${sessionScope.loginUser.memberId}'/>">

			<%--退会確定前の最終本人確認（パスワード入力欄） --%>
			<div class="form-group"
				style="margin-top: 15px !important; text-align: left;">
				<label for="currentPassword">本人確認のためパスワードを入力してください</label>
				<div class="password-wrapper">
					<input type="password" id="currentPassword" name="currentPassword"
						required placeholder="現在のパスワードを入力" maxlength="32">

					<button type="button" id="toggle-password-current"
						class="btn-toggle-pw" aria-label="パスワードを表示">
						<svg id="eye-icon-current" xmlns="http://w3.org" width="18"
							height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
							<path
								d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
							<line x1="1" y1="1" x2="23" y2="23"></line>
						</svg>
					</button>
				</div>
				<span id="currentPassword-error" class="error-msg"></span>
			</div>

			<%--ボタンエリア --%>
			<div class="btn-area" style="margin-top: 15px;">
				<button type="submit" class="btn-action-primary btn-action-danger">本当に退会する</button>
				<button type="button" class="btn-action-secondary"
					onclick="location.href='${pageContext.request.contextPath}/UserMenu.action'">マイページに戻る</button>
			</div>

		</form>
	</div>
	<script src="${pageContext.request.contextPath}/js/user-validation.js"></script>
	<script src="${pageContext.request.contextPath}/js/password-toggle.js"></script>
</body>
</html>

