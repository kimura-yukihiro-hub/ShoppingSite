<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退会完了</title>
<!--10秒後に自動的にログイン画面へ戻る命令-->
<meta http-equiv="refresh"
	content="10;URL=${pageContext.request.contextPath}/views/login-in.jsp">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<div class="form-container" style="text-align: center;">
		<h2>退会手続き完了</h2>

		<p
			style="font-size: 14px; line-height: 1.6; margin-bottom: 30px; opacity: 0.8; text-align: left;">
			退会手続きが正常に完了いたしました。<br> これまで当サイトをご利用いただき<br>誠にありがとうございました。<br>
			<br> お客様の会員情報は安全に削除されました。
		</p>

		<!--案内文も10秒に統一 -->
		<p style="font-size: 12px; color: #aaaaaa; margin-bottom: 25px;">
			⏰ 10秒後に自動的にログイン画面へ戻ります。</p>

		<%--セッションが完全に消去されているため、ログイン画面へ戻す --%>
		<div class="btn-area">
			<a href="${pageContext.request.contextPath}/views/login-in.jsp"
				class="btn-action-primary" style="text-decoration: none;">ログイン画面へ</a>
		</div>
	</div>

</body>
</html>
