<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<script>
	// このマイページが開いた瞬間に、ブラウザのログイン通信の履歴(POST)を、安全な現在のURL(GET)へ強制的に上書きしてすり替えます。
	window.history.replaceState(null, null, window.location.href);
</script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マイメニュー</title>
<link rel="stylesheet" href="/ShoppingSite/css/style.css">
</head>
<body class="rank-a${sessionScope.loginUser.meatRank}">
	<div class="menu-container">
		<div class="user-card">
			<h3>マイページ</h3>
			<p>
				ようこそ、<strong>${sessionScope.loginUser.lastName}</strong>さん！
			</p>
		</div>

		<nav class="menu-links">
			<h3>メインメニュー</h3>
			<ul>
				<li>
					<button type="button" class="btn-menu btn-update"
						onclick="location.href='${pageContext.request.contextPath}/UserUpdateForm.action'">修正</button>
				</li>
				<li>
					<button type="button" class="btn-menu btn-delete"
						onclick="location.href='${pageContext.request.contextPath}/UserDelete.action'">削除</button>
				</li>
				<li>
					<button type="button" class="btn-menu btn-logout"
						onclick="location.href='${pageContext.request.contextPath}/Logout.action'">ログアウト</button>
				</li>
			</ul>
		</nav>
	</div>
</body>
</html>