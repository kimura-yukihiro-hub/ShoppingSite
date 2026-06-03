<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報変更完了</title>
<!--10秒後に自動的にマイページを表示するActionへ戻る命令を -->
<meta http-equiv="refresh"
	content="10;URL=${pageContext.request.contextPath}/UserMenu.action">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<div class="form-container" style="text-align: center;">
		<h2 style="color: #81c784;">✨変更完了</h2>

		<p
			style="font-size: 14px; line-height: 1.6; margin-bottom: 30px; opacity: 0.9;">
			会員情報の更新処理が<br>正常に終了いたしました。
		</p>

		<!--案内文も10秒に統一 -->
		<p style="font-size: 12px; color: #aaaaaa; margin-bottom: 20px;">
			⏰ 10秒後に自動的にマイページへ戻ります。</p>

		<div
			style="font-size: 13px; opacity: 0.7; margin-bottom: 25px; text-align: left; background: rgba(0, 0, 0, 0.1); padding: 12px; border-radius: 6px;">
			※新しい登録内容はすでに<br>現在のマイページへ即座に反映されています。</div>

		<%--安全にマイページを表示するActionへリダイレクトさせる --%>
		<div class="btn-area">
			<a href="${pageContext.request.contextPath}/UserMenu.action"
				class="btn-action-primary" style="text-decoration: none;">マイページへ戻る</a>
		</div>
	</div>

</body>
</html>
