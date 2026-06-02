<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報新規登録</title>
</head>
<body>

	<h2>会員情報新規登録</h2>

	<%--FrontControllerへUserAdd.actionとして送信する --%>
	<form action="${pageContext.request.contextPath}/UserAdd.action"
		method="post">

		<div class="form-group name-group">
			<label>お名前:</label> <input type="text" id="lastName" name="lastName"
				placeholder="姓" required> <input type="text" id="firstName"
				name="firstName" placeholder="名" required>
		</div>

		<div class="form-group">
			<label for="address">住所:</label> <input type="text" id="address"
				name="address" required>
		</div>

		<div class="form-group">
			<label for="memberId">会員番号(ID):</label> <input type="text"
				id="memberId" name="memberId" required pattern="^[a-zA-Z0-9\-]+$"
				title="半角英数字とハイフンのみ">
		</div>

		<div class="form-group">
			<label for="password">パスワード:</label> <input type="password"
				id="password" name="password" required pattern="^[a-zA-Z0-9]+$"
				title="半角英数字のみ">
		</div>

		<div class="form-group">
			<label for="mailAddress">メールアドレス:</label> <input type="email"
				id="mailAddress" name="mailAddress" required>
		</div>

		<div class="form-group">
			<button type="submit" class="btn-submit">登録内容を確認する</button>
		</div>
	</form>


</body>
</html>