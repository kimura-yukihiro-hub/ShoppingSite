<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登録内容確認</title>
</head>
<body>

	<h2>登録内要確認</h2>
	<p>以下の内容で登録します。よろしければ「登録確定」ボタンを押してください。</p>

	<%--最終的なデータベース登録処理を行うアクションへポストする --%>
	<form action="${pageContext.request.contextPath}/UserAddExecute.action"
		method="post">

		<%--画面表示用の確認項目 --%>
		<div class="confilm-item">
			<span class="label">お名前(姓)</span> <span class="value">${newUser.lastName}</span>
		</div>

		<div class="confirm-item">
			<span class="label">お名前(名):</span> <span class="value">${newUser.firstName}</span>
		</div>

		<div class="confirm-item">
			<span class="label">住所:</span> <span class="value">${newUser.address}</span>
		</div>

		<div class="confirm-item">
			<span class="label">メールアドレス:</span> <span class="value">${newUser.mailAddress}</span>
		</div>

		<div class="confirm-item">
			<span class="label">会員番号 (ID):</span> <span class="value">${newUser.memberId}</span>
		</div>

		<div class="confirm-item">
			<span class="label">パスワード:</span> <span class="value">********
				(セキュリティのため非表示)</span>
		</div>

		<%--次のActionへ値を引き渡すための隠しパラメータ(hidden) --%>
		<input type="hidden" name="lastName" value="${newUser.lastName}">
		<input type="hidden" name="firstName" value="${newUser.firstName}">
		<input type="hidden" name="address" value="${newUser.address}">
		<input type="hidden" name="mailAddress" value="${newUser.mailAddress}">
		<input type="hidden" name="memberId" value="${newUser.memberId}">
		<input type="hidden" name="password" value="${newUser.password}">
		<input type="hidden" name="meatRank" value="${newUser.meatRank}">

		<div class="btn-group">
			<button type="submit" class="btn">登録確定</button>
			<%--戻るボタンを押したときは、ブラウザのきおぬで前の入力画面に戻す --%>
			<button type="button" class="btn" onclick="history.back()">修正する</button>
		</div>
	</form>
</body>
</html>