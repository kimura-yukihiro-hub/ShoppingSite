<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登録内容確認</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="form-container">
		<h2>登録内容確認</h2>
		<p style="font-size: 13px; opacity: 0.7; margin-bottom: 20px;">
			以下の内容で登録します。<br>よろしければ「登録確定」ボタンを押してください。
		</p>

		<form
			action="${pageContext.request.contextPath}/UserAddExecute.action"
			method="post">

			<div class="form-group">
				<label>お名前</label>
				<!--静的表示用ボックス .confirm-value を使用して綺麗にデータを並べる -->
				<div class="confirm-value">
					<c:out value="${newUser.lastName}" />
					<c:out value="${newUser.firstName}" />
				</div>
			</div>

			<div class="form-group">
				<label>住所</label>
				<div class="confirm-value">
					<c:out value="${newUser.address}" />
				</div>
			</div>

			<div class="form-group">
				<label>メールアドレス</label>
				<div class="confirm-value">
					<c:out value="${newUser.mailAddress}" />
				</div>
			</div>

			<div class="form-group">
				<label>会員番号 (ID)</label>
				<div class="confirm-value">
					<c:out value="${newUser.memberId}" />
				</div>
			</div>

			<div class="form-group">
				<label>パスワード</label>
				<div class="confirm-value">******** (セキュリティのため非表示)</div>
			</div>

			<%--次のActionへ値を引き渡すための隠しパラメータ --%>
			<input type="hidden" name="lastName" value="<c:out value='${newUser.lastName}'/>">
			<input type="hidden" name="firstName" value="<c:out value='${newUser.firstName}'/>">
			<input type="hidden" name="address" value="<c:out value='${newUser.address}'/>">
			<input type="hidden" name="mailAddress" value="<c:out value='${newUser.mailAddress}'/>">
			<input type="hidden" name="memberId" value="<c:out value='${newUser.memberId}'/>">
			<input type="hidden" name="password" value="<c:out value='${newUser.password}'/>">
			<input type="hidden" name="meatRank" value="<c:out value='${newUser.meatRank}'/>">

			<%--縦並びのスタイリッシュなボタンエリア --%>
			<div class="btn-area">
				<button type="submit" class="btn-action-primary">登録確定</button>
				<button type="button" class="btn-action-secondary"
					onclick="history.back();">修正する</button>
			</div>

		</form>
	</div>

</body>
</html>
