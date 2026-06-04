<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員登録完了</title>
<!--10秒後に自動的にマイページ画面へ遷移する命令 -->
<meta http-equiv="refresh"
	content="10;URL=${pageContext.request.contextPath}/UserMenu.action">
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

		<!--自動でマイページ画面に遷移することをユーザーに優しく知らせるカウントダウン案内 -->
		<p style="font-size: 12px; color: #aaaaaa; margin-bottom: 20px;">
			⏰ 10秒後に自動的にマイページ画面へ移動します。</p>
		<div class="btn-area">
			<a href="${pageContext.request.contextPath}/UserMenu.action"
				class="btn-action-primary" style="text-decoration: none;">マイページ（トップ）へ</a>
		</div>
	</div>

</body>
</html>
