<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退会手続き</title>
</head>
<body>

	<div class="delete-container">
		<h2 class="delete-title">退会確認</h2>

		<div class="warning-text">
			<p>一度退会手続きを完了すると、これまでの注文履歴の確認や、同じ会員番号でのログインが一切できなくなります。</p>
			<p>本当に退会してもよろしいですか？</p>
		</div>

		<%--現在ログインしているユーザーの情報をセッションから表示 --%>
		<div class="user-card user-info">
			<div class="info-row">
				<strong>会員番号 (ID):</strong> ${sessionScope.loginUser.memberId}
			</div>
			<div class="info-row">
				<strong>お名前:</strong> ${sessionScope.loginUser.lastName}
				${sessionScope.loginUser.firstName}
			</div>
			<div class="info-row">
				<strong>メールアドレス:</strong> ${sessionScope.loginUser.mailAddress}
			</div>
		</div>

		<%--実際に削除処理を実行するアクションへポストします。 --%>
		<form
			action="${pageContext.request.contextPath}/UserDeleteExecute.action"
			method="post">

			<%--誰のデータを消すかを特定するため、会員番号をhiddenで送信する --%>
			<input type="hidden" name="memberId"
				value="${sessionScope.loginUser.memberId}">

			<div class="btn-group">
				<button type="submit" class="btn btn-danger">本当に退会する</button>
				<%-- やめる場合は、安全にマイページへ戻します --%>
				<button type="button" class="btn btn-secondary"
					onclick="location.href='${pageContext.request.contextPath}/UserMenu.action'">マイページに戻る</button>
			</div>
		</form>
	</div>
</body>
</html>