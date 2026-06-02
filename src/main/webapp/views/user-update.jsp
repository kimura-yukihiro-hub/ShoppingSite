<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報変更</title>
</head>
<body>

	<h2>会員情報変更</h2>
	<p>変更したい項目を入力し、「変更内容を確認する」ボタンを押してください。</p>

	<form action="${pageContext.request.contextPath}/UserUpdate.action"
		method="post">

		<%--会員番号は主キーのため、変更できないようにreadonly(読み取り専用)にする --%>
		<div class="form-group">
			<label>会員番号(ID):</label> <input type="text" name="memberId"
				value="${sessionScope.loginUser.memberId}" readonly
				style="background-color: #e0e0e0;">
			<p class="note">※会員番号は変更できません。</p>
		</div>

		<div class="form-group name-group">
			<label>お名前:</label> <input type="text" name="lastName"
				value="${sessionScope.loginUser.lastName}" required> <input
				type="text" name="firstName"
				value="${sessionScope.loginUser.firstName}" required>
		</div>

		<div class="form-group">
			<label for="address">住所:</label> <input type="text" id="address"
				name="address" value="${sessionScope.loginUser.address}" required>
		</div>

		<div class="form-group">
			<label for="mailAddress">メールアドレス:</label> <input type="email"
				id="mailAddress" name="mailAddress"
				value="${sessionScope.loginUser.mailAddress}" required>
		</div>

		<div class="form-group">
			<label for="password">新しいパスワード:</label> <input type="password"
				id="password" name="password"
				value="${sessionScope.loginUser.password}" required
				pattern="^[a-zA-Z0-9]+$" title="半角英数字のみ">
		</div>

		<div class="form-group">
			<button type="submit" class="btn-submit">変更内容を確認する</button>
		</div>
	</form>
</body>
</html>