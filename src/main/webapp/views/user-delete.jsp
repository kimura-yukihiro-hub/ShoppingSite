<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退会手続き</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<!--container-danger クラスを掛け合わせることで、赤枠の警告ボックスに変身させます -->
	<div class="form-container container-danger">
		<h2 style="color: #ff5252;">⚠️ 退会確認</h2>

		<div
			style="font-size: 13px; opacity: 0.9; line-height: 1.6; text-align: left; margin-bottom: 20px;">
			<p>
				一度退会手続きを完了すると,
				これまでの<br>注文履歴の確認や、同じ会員番号での<br>ログインが一切できなくなります。
			</p>
			<p>本当に退会してもよろしいですか？</p>
		</div>

		<%--現在ログインしているユーザーの情報を、確認しやすいシースルーのボックスで表示 --%>
		<div class="form-group">
			<label>現在の会員番号 (ID)</label>
			<div class="confirm-value"
				style="border-color: rgba(255, 82, 82, 0.2);">${sessionScope.loginUser.memberId}</div>
		</div>

		<div class="form-group">
			<label>お名前</label>
			<div class="confirm-value"
				style="border-color: rgba(255, 82, 82, 0.2);">${sessionScope.loginUser.lastName}
				${sessionScope.loginUser.firstName}</div>
		</div>

		<div class="form-group">
			<label>メールアドレス</label>
			<div class="confirm-value"
				style="border-color: rgba(255, 82, 82, 0.2);">${sessionScope.loginUser.mailAddress}</div>
		</div>

		<%--実際に削除処理を実行するアクションへポスト --%>
		<form
			action="${pageContext.request.contextPath}/UserDeleteExecute.action"
			method="post">

			<%-- 誰のデータを消すかを特定するため、会員番号を hidden で送信 --%>
			<input type="hidden" name="memberId"
				value="${sessionScope.loginUser.memberId}">

			<%--ボタンエリア：退会確定は鮮やかなネオンレッド、戻るボタンは透過枠線で押し分けやすくします --%>
			<div class="btn-area">
				<button type="submit" class="btn-action-primary btn-action-danger">本当に退会する</button>
				<button type="button" class="btn-action-secondary"
					onclick="location.href='${pageContext.request.contextPath}/UserMenu.action'">マイページに戻る</button>
			</div>

		</form>
	</div>

</body>
</html>
