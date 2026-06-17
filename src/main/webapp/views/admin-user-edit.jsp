<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報編集 - 顧客管理システム</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div class="menu-container admin-customer edit-page">
		<header class="list-header">
			<h1>会員情報編集</h1>
		</header>

		<div class="item-form-box customer-list-box">
			<form
				action="${pageContext.request.contextPath}/AdminUserEditExecute.action"
				method="POST">
				<input type="hidden" name="memberId" value="${targetUser.memberId}">

				<div class="form-group">
					<label>会員番号</label>
					<p class="form-static-text">${targetUser.memberId}</p>
				</div>

				<div class="form-group">
					<label>氏名</label>
					<p class="form-static-text">${targetUser.lastName}
						${targetUser.firstName}</p>
				</div>

				<div class="form-group">
					<label>メールアドレス</label>
					<p class="form-static-text">${targetUser.mailAddress}</p>
				</div>

				<div class="form-group">
					<label>住所</label>
					<p class="form-static-text">${targetUser.address}</p>
				</div>

				<div class="form-group">
					<label>会員ランク変更</label> <select name="meatRank" class="form-control">
						<option value="1" ${targetUser.meatRank == 1 ? 'selected' : ''}>一般会員</option>
						<option value="2" ${targetUser.meatRank == 2 ? 'selected' : ''}>ゴールド会員</option>
						<option value="3" ${targetUser.meatRank == 3 ? 'selected' : ''}>プラチナ会員</option>
						<option value="4" ${targetUser.meatRank == 4 ? 'selected' : ''}>VIP会員</option>
					</select>
				</div>

				<div class="btn-area">
					<button type="submit" class="btn-user-action btn-user-edit"
						onclick="return confirm('【ランク更新の確認】\n\n会員：${targetUser.lastName} ${targetUser.firstName} 様\n\nランクを現在の設定に変更してよろしいでしょうか？\nこの操作により会員の権限が変更されます。');">
						ランクを更新する</button>

					<a href="${pageContext.request.contextPath}/AdminList.action"
						class="btn-menu">キャンセル</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>