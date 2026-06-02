<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報変更完了</title>
</head>
<body>

	<div class="success-container">
		<h2 class="success-title">会員情報の変更が完了しました。</h2>

		<p class="message">
			会員情報の更新処理が正常に終了しました。<br> 新しい登録内容は即座に反映されています。
		</p>

		<%--マイページを表示するための「UserMenu.Action」へリダイレクト(遷移)させる --%>
		<a href="${pageContext.request.contextPath}/UserMenu.action"
			class="btn-home">マイページへ戻る</a>
	</div>
</body>
</html>