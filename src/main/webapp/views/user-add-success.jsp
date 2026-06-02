<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員登録完了</title>
</head>
<body>

	<div class="success-container">
		<h2 class="success-title">会員登録が完了しました。</h2>

		<p class="message">
			ご登録ありがとうございます。<br>登録した会員番号とパスワードを使って、以下からログインしてください。
		</p>

		<%--ログイン画面を表示するアクション(またはJSP)へ遷移させる --%>
		<a href="${pageContext.request.contextPath}/views/login-in.jsp"
			class="btn-login">ログイン画面へ戻る</a>

	</div>
</body>
</html>