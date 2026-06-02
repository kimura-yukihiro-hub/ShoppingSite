<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退会完了</title>
</head>
<body>
	<div class="success-container">
		<h2 class="success-title">退会手続き完了</h2>

		<p class="message">
			退会手続きが正常に完了いたしました。<br> これまで当サイトをご利用いただき、誠にありがとうございました。<br>
			お客様の会員情報は安全に削除されました。
		</p>

		<%--セッションが完全に消えているため、ログイン画面(トップ)へ戻す --%>
		<a href="${pageContext.request.contextPath}/views/login-in.jsp"
			class="btn-top">ログイン画面へ</a>
	</div>

</body>
</html>