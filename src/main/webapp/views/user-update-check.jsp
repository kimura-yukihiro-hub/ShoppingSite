<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>変更内容確認</title>
</head>
<body>

	<h2>変更内容確認</h2>
	<p>以下の内容に変更します。よろしければ「変更確定」ボタンを押してください。</p>

	<%--最終的なデータベース更新処理を行うアクションへポストする --%>
	<form
		action="${pageContext.request.contextPath}/UserUpdateExecute.action"
		method="post">

		<%--画面表示用の確認項目 --%>
		<div class="confirm-item">
			<span class="label">会員番号 (ID):</span>
			<%-- 会員番号は変更不可ですが、誰のデータを更新するか特定するために表示・送信します --%>
			<span class="value">${updatedUser.memberId}</span>
		</div>

		<div class="confirm-item">
			<span class="label">お名前(姓):</span> <span class="value">${updatedUser.lastName}</span>
		</div>

		<div class="confirm-item">
			<span class="label">お名前(名):</span> <span class="value">${updatedUser.firstName}</span>
		</div>

		<div class="confirm-item">
			<span class="label">住所:</span> <span class="value">${updatedUser.address}</span>
		</div>

		<div class="confirm-item">
			<span class="label">メールアドレス:</span> <span class="value">${updatedUser.mailAddress}</span>
		</div>

		<div class="confirm-item">
			<span class="label">新しいパスワード:</span> <span class="value">********
				(セキュリティのため非表示)</span>
		</div>

		<%--次のUserUpdateExecuteActionへ値を引き渡すための隠しパラメータ(hidden) --%>
		<input type="hidden" name="memberId" value="${updatedUser.memberId}">
		<input type="hidden" name="lastName" value="${updatedUser.lastName}">
		<input type="hidden" name="firstName" value="${updatedUser.firstName}">
		<input type="hidden" name="address" value="${updatedUser.address}">
		<input type="hidden" name="mailAddress"
			value="${updatedUser.mailAddress}"> <input type="hidden"
			name="password" value="${updatedUser.password}">

		<div class="btn-group">
			<button type="submit" class="btn">変更確定</button>
			<button type="button" class="btn"
				onclick="location.href='${pageContext.request.contextPath}/views/user-update.jsp'">修正する</button>

		</div>
	</form>


</body>
</html>