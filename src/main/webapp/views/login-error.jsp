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
		<h2>${not empty errorTitle ? errorTitle : 'ログインできませんでした'}</h2>

		<p>${not empty errorMessage ? errorMessage : 'エラーもしくはパスワードが違います'}</p>

		<button type="button" class="btn-back"
			onclick="location.href='${pageContext.request.contextPath}/${not empty errorBackUrl ? errorBackUrl : "views/login-in.jsp"}'">
			${not empty errorBtnText ? errorBtnText : 'ログイン画面へ戻る'}</button>
	</div>

</body>
</html>