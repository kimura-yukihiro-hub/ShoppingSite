<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報新規登録</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="form-container user-add-page">
		<h2>会員情報新規登録</h2>

		<!-- リアルタイムバリデーションJSを連動させるためのクラスを付与 -->
		<form action="${pageContext.request.contextPath}/UserAdd.action"
			method="post" class="validated-form">

			<!-- お名前（姓・名が横並び） -->
			<div class="form-group">
				<label>お名前</label>
				<div class="name-flex-group">
					<div>
						<input type="text" id="lastName" name="lastName" placeholder="姓"
							maxlength="20">
					</div>
					<div>
						<input type="text" id="firstName" name="firstName" placeholder="名"
							maxlength="20">
					</div>
				</div>
				<span id="name-error" class="error-msg"></span>
			</div>
			<div class="form-group">
				<label for="zipCode">郵便番号</label>
				<!-- 最大7桁。ハイフンがあってもなくても動くようにJSで制御 -->
				<input type="text" id="zipCode" name="zipCode"
					placeholder="例: 1000001" maxlength="8"> <span
					id="zipCode-error" class="error-msg"></span>
			</div>
			<div class="form-group">
				<label for="address">住所</label> <input type="text" id="address"
					name="address" placeholder="郵便番号を入れると自動入力されます" maxlength="100">
				<span id="address-error" class="error-msg"></span>
			</div>

			<div class="form-group">
				<label for="mailAddress">メールアドレス</label> <input type="text"
					id="mailAddress" name="mailAddress"> <span
					id="mailAddress-error" class="error-msg"></span>
			</div>

			<div class="form-group">
				<label for="memberId">会員番号 (ID)</label> <input type="text"
					id="memberId" name="memberId" maxlength="10"> <span
					id="memberId-error" class="error-msg"></span>
			</div>

			<div class="form-group">
				<label for="password">パスワード</label>
				<div class="password-wrapper">
					<input type="password" id="password" name="password" maxlength="32">

					<button type="button" id="toggle-password" class="btn-toggle-pw"
						aria-label="パスワードを表示">
						<svg id="eye-icon" xmlns="http://w3.org" width="20" height="20"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path
								d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                            <line x1="1" y1="1" x2="23" y2="23"></line>
                        </svg>
					</button>
				</div>
				<span id="password-error" class="error-msg"></span>
				<%-- 自動生成を呼び出すボタン--%>
				<button type="button" id="btn-generate-pw"
					class="btn-action-secondary"
					style="margin-top: 5px; padding: 6px !important; font-size: 13px !important;">
					🔑 安全なパスワードを自動生成する</button>
			</div>
			<div class="form-group" style="margin-top: 10px !important;">
				<label for="passwordConfirm">パスワード（確認用）</label>
				<div class="password-wrapper">
					<input type="password" id="passwordConfirm" name="passwordConfirm"
						placeholder="もう一度パスワードを入力してください" maxlength="32">

					<button type="button" id="toggle-password-confirm"
						class="btn-toggle-pw" aria-label="パスワードを表示">
						<svg id="eye-icon-confirm" xmlns="http://w3.org" width="20"
							height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
							<path
								d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
							<line x1="1" y1="1" x2="23" y2="23"></line>
						</svg>
					</button>
				</div>
				<span id="passwordConfirm-error" class="error-msg"></span>
			</div>
			<div class="btn-area">
				<button type="submit" class="btn-action-primary">登録内容を確認する</button>
				<a href="${pageContext.request.contextPath}/views/login-in.jsp"
					class="btn-action-secondary">ログイン画面へ戻る</a>
			</div>

		</form>
	</div>
	<script
		src="${pageContext.request.contextPath}/js/password-generator.js"></script>
	<script src="${pageContext.request.contextPath}/js/user-validation.js"></script>
	<script src="${pageContext.request.contextPath}/js/password-toggle.js"></script>
</body>
</html>

