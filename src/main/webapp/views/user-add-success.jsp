<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員登録完了</title>
<!--5秒後に自動的にログイン画面へ遷移する命令 -->
<meta http-equiv="refresh"
	content="10;URL=${pageContext.request.contextPath}/views/login-in.jsp">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="form-container" style="text-align: center;">
		<h2 style="color: #81c784;">🎉 登録完了</h2>

		<p
			style="font-size: 14px; line-height: 1.6; margin-bottom: 30px; opacity: 0.9;">
			ご登録ありがとうございます！<br> 会員情報の新規登録が<br>正常に完了いたしました。
		</p>

		<!--自動で戻ることをユーザーに優しく知らせるカウントダウン案内 -->
		<p style="font-size: 12px; color: #aaaaaa; margin-bottom: 20px;">
			⏰ 10秒後に自動的にログイン画面へ戻ります。</p>

		<div
			style="font-size: 13px; opacity: 0.7; margin-bottom: 25px; text-align: left; background: rgba(0, 0, 0, 0.1); padding: 12px; border-radius: 6px;">
			※登録した会員番号とパスワードを入力してログインしてください。</div>
		<div class="btn-area">
			<a href="${pageContext.request.contextPath}/views/login-in.jsp"
				class="btn-action-primary" style="text-decoration: none;">ログイン画面へ</a>
		</div>
	</div>

</body>
</html>
