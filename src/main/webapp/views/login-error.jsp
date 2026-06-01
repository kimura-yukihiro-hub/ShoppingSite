<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログインエラー</title>
<link rel="stylesheet" href="/ShoppingSite/css/style.css">
</head>
<body class="error-page">

	<div class="error-container">
		<div class="error-icon">⚠️</div>
		<h2>ログインできませんでした</h2>

		<p>${not empty errorMessage ? errorMessage : 'エラーもしくはパスワードが違います'}</p>

		<button type="button" class="btn-back"
			onclick="location.href='${pageContext.request.contextPath}/views/login-in.jsp'">ログイン画面へ戻る</button>
	</div>

</body>
</html>