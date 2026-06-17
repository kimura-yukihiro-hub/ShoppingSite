<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退会完了</title>
<meta http-equiv="refresh"
	content="10;URL=${pageContext.request.contextPath}/Login.action">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<div class="form-container delete-success-box">
		<h2>退会手続き完了</h2>

		<p class="delete-msg">
			退会手続きが正常に完了いたしました。<br> これまで当サイトをご利用いただき<br>誠にありがとうございました。<br>
			<br> お客様の会員情報は安全に削除されました。
		</p>

		<p class="timer-msg">⏰ 10秒後に自動的にログイン画面へ戻ります。</p>

		<div class="btn-area">
			<a href="${pageContext.request.contextPath}/Login.action"
				class="btn-action-primary">ログイン画面へ</a>
		</div>
	</div>

</body>
</html>