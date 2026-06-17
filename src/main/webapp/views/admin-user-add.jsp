<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理者アカウントの追加登録 - 管理者専用</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div class="menu-container">
		<div class="user-card">
			<h3>別の管理者アカウントを追加</h3>
			<p>
				新しくシステム管理権限を付与する<br>スタッフの情報を入力してください。
			</p>
		</div>

		<!-- リアルタイムバリデーションJSと連動させるため class="validated-form" を適用 -->
		<form
			action="${pageContext.request.contextPath}/AdminUserAddExecute.action"
			method="post" class="item-form-box validated-form">

			<!-- エラーメッセージ表示領域（サーバーサイドでのエラー警告用） -->
			<c:if test="${not empty errorMessage}">
				<div class="err-msg">
					<c:out value="${errorMessage}" />
				</div>
			</c:if>

			<!-- 【左側カラム】基本プロフィール ＆ 操作者本人確認 -->
			<div class="form-column-left">

				<!-- 1. 新しい管理者のログインID -->
				<div class="form-group">
					<label>新規管理者のログインID (半角英数) <span>(必須)</span></label>
					<div class="input-block">
						<input type="text" id="memberId" name="memberId"
							value="<c:out value='${keep_memberId}'/>"
							placeholder="例: admin02" maxlength="10">
					</div>
					<span id="memberId-error" class="error-msg"></span>
				</div>

				<!-- 2. お名前（姓・名が横並び） -->
				<div class="form-group">
					<label>お名前 <span>(必須)</span></label>
					<div class="name-flex-group">
						<div>
							<input type="text" id="lastName" name="lastName"
								value="<c:out value='${keep_lastName}'/>" placeholder="姓"
								maxlength="20">
						</div>
						<div>
							<input type="text" id="firstName" name="firstName"
								value="<c:out value='${keep_firstName}'/>" placeholder="名"
								maxlength="20">
						</div>
					</div>
					<span id="name-error" class="error-msg"></span>
				</div>

				<!-- 3. メールアドレス -->
				<div class="form-group">
					<label>メールアドレス <span>(必須)</span></label>
					<div class="input-block">
						<input type="text" id="mailAddress" name="mailAddress"
							value="<c:out value='${keep_mailAddress}'/>"
							placeholder="example@meat-shop.com">
					</div>
					<span id="mailAddress-error" class="error-msg"></span>
				</div>

				<!-- 8. 操作中の管理者自身のパスワード（本人確認用） -->
				<div class="form-group">
					<label class="admin-auth-label">あなたの現在のパスワード (本人確認) <span>(必須)</span></label>
					<div class="input-block">
						<div class="password-wrapper">
							<input type="password" id="currentAdminPassword"
								name="currentAdminPassword" placeholder="本人確認のため、現在のパスワードを入力">

							<button type="button" id="toggle-password-admin-current"
								class="btn-toggle-pw" aria-label="パスワードを表示">
								<svg id="eye-icon-admin-current" xmlns="http://w3.org"
									width="20" height="20" viewBox="0 0 24 24" fill="none"
									stroke="currentColor" stroke-width="2" stroke-linecap="round"
									stroke-linejoin="round">
									<path
										d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
									<line x1="1" y1="1" x2="23" y2="23"></line>
								</svg>
							</button>
						</div>
					</div>
					<span id="currentAdminPassword-error" class="error-msg"></span>
				</div>

			</div>

			<!-- 【右側カラム】住所 ＆ セキュリティ・新規パスワード関連 -->
			<div class="form-column-right">

				<!-- 4. 郵便番号（任意入力・自動補完トリガー） -->
				<div class="form-group">
					<label for="zipCode">郵便番号 <span>(任意)</span></label>
					<div class="input-block">
						<input type="text" id="zipCode" name="postCode"
							value="<c:out value='${keep_postCode}'/>"
							placeholder="例: 1000001" maxlength="8">
					</div>
					<span id="zipCode-error" class="error-msg"></span>
				</div>

				<!-- 5. 住所（必須） -->
				<div class="form-group">
					<label for="address">住所 <span>(必須)</span></label>
					<div class="input-block">
						<input type="text" id="address" name="address"
							value="<c:out value='${keep_address}'/>"
							placeholder="郵便番号を入れると自動入力されます" maxlength="100">
					</div>
					<span id="address-error" class="error-msg"></span>
				</div>

				<!-- 6. 新しい管理者のパスワード -->
				<div class="form-group">
					<label>新規管理者のパスワード <span>(必須)</span></label>
					<div class="input-block">
						<div class="password-wrapper">
							<input type="password" id="password" name="password"
								placeholder="英数記号8文字以上" maxlength="32">

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
					</div>
					<span id="password-error" class="error-msg"></span>

					<button type="button" id="btn-generate-pw"
						class="btn-menu btn-admin-add" style="margin-top: 5px;">🔑
						安全なパスワードを自動生成</button>
				</div>

				<!-- 7. 新規パスワードの確認用入力欄 -->
				<div class="form-group">
					<label>新規管理者のパスワード（確認用） <span>(必須)</span></label>
					<div class="input-block">
						<div class="password-wrapper">
							<input type="password" id="passwordConfirm"
								name="passwordConfirm" placeholder="もう一度パスワードを入力してください"
								maxlength="32">

							<button type="button" id="toggle-password-confirm"
								class="btn-toggle-pw" aria-label="パスワードを表示">
								<svg id="eye-icon-confirm" xmlns="http://w3.org" width="20"
									height="20" viewBox="0 0 24 24" fill="none"
									stroke="currentColor" stroke-width="2" stroke-linecap="round"
									stroke-linejoin="round">
									<path
										d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
									<line x1="1" y1="1" x2="23" y2="23"></line>
								</svg>
							</button>
						</div>
					</div>
					<span id="passwordConfirm-error" class="error-msg"></span>
				</div>

			</div>

			<!-- 管理者登録のため会員ランクは自動で「5」を固定送信 -->
			<input type="hidden" name="meatRank" value="5">

			<!-- ボタンエリア -->
			<div class="button-area">
				<button type="submit" class="btn-menu btn-admin-add">管理者を新規登録する</button>
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-continue">管理者メニューへ戻る</a>
			</div>
		</form>
	</div>

	<!-- 外部JavaScriptファイルの一斉読み込み -->
	<script src="${pageContext.request.contextPath}/js/generate-pw.js"></script>
	<script src="${pageContext.request.contextPath}/js/password-toggle.js"></script>
	<script src="${pageContext.request.contextPath}/js/user-validation.js"></script>
</body>
</html>
