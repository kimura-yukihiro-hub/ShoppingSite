<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>変更内容確認</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<div class="form-container">
		<h2>変更内容確認</h2>
		<p style="font-size: 13px; opacity: 0.7; margin-bottom: 20px;">以下の内容に変更します。よろしければ「変更確定」ボタンを押してください。</p>

		<form
			action="${pageContext.request.contextPath}/UserUpdateExecute.action"
			method="post">

			<div class="form-group">
				<label>会員番号 (ID)</label>
				<div class="confirm-value">${updatedUser.memberId}</div>
			</div>

			<div class="form-group">
				<label>お名前</label>
				<div class="confirm-value">${updatedUser.lastName}
					${updatedUser.firstName}</div>
			</div>

			<div class="form-group">
				<label>住所</label>
				<div class="confirm-value">${updatedUser.address}</div>
			</div>

			<div class="form-group">
				<label>メールアドレス</label>
				<div class="confirm-value">${updatedUser.mailAddress}</div>
			</div>

			<div class="form-group">
				<label>新しいパスワード</label>
				<div class="confirm-value">******** (セキュリティのため非表示)</div>
			</div>

			<%--次のActionへ値を引き渡すための隠しパラメータ --%>
			<input type="hidden" name="memberId" value="${updatedUser.memberId}">
			<input type="hidden" name="lastName" value="${updatedUser.lastName}">
			<input type="hidden" name="firstName"
				value="${updatedUser.firstName}"> <input type="hidden"
				name="address" value="${updatedUser.address}"> <input
				type="hidden" name="mailAddress" value="${updatedUser.mailAddress}">
			<input type="hidden" name="password" value="${updatedUser.password}">

			<div class="btn-area">
				<button type="submit" class="btn-action-primary">変更確定</button>
				<button type="button" class="btn-action-secondary"
					onclick="location.href='${pageContext.request.contextPath}/views/user-update.jsp'">修正する</button>
			</div>

		</form>
	</div>

</body>
</html>
