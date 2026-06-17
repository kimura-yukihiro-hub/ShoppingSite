<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ECサイト-ログイン画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="login-container">
		<h2>Login</h2>
		<form action="${pageContext.request.contextPath}/Login.action"
			method="post" id="login-form">
			<!-- IDの入力フォーム -->
			<div class="input-group">
				<label for="memberId">会員番号(ID)</label><input type="text"
					id="memberId" name="memberId" maxlength="10"> <span
					id="memberId-error" class="error-msg"></span>
			</div>
			<!-- パスワードの入力フォーム -->
			<div class="input-group">
				<label for="password">パスワード</label>
				<!-- 目のマークを黒ボックスの右端に浮かせるための囲み(wrapper)と目のSVGを挿入 -->
				<div class="password-wrapper">
					<input type="password" id="password" name="password" maxlength="32">

					<!--パスワード入力エリアボタンの中身を、初期状態で「閉じ目」のSVGにする-->
					<button type="button" id="toggle-password" class="btn-toggle-pw"
						aria-label="パスワードを表示">
						<!--目のアイコン（初期状態：斜線が入った閉じ目の状態を描画します） -->
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
			</div>
			<!-- ログインボタン -->
			<button type="submit" class="btn-submit">ログイン</button>
			<!-- 新規会員登録ボタン -->
			<button type="button" class="btn-register"
				onclick="location.href='${pageContext.request.contextPath}/views/user-add.jsp'">新規会員登録</button>
		</form>

		<!-- ゲストとして商品一覧(サーバー経由)へ直接戻るための動線 -->
		<div>
			<a href="${pageContext.request.contextPath}/ItemList.action"
				class="link-guest"> ログインせずに商品一覧を見る（ゲスト） </a>
		</div>
	</div>
	<script src="${pageContext.request.contextPath}/js/password-toggle.js"></script>
</body>
</html>