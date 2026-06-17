<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<script>
	window.history.replaceState(null, null, window.location.href);
</script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理者専用メインメニュー</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<!-- 画面全体を横長ダッシュボード化するための親コンテナ -->
	<div class="admin-dashboard-container">

		<!-- 左列エリア：管理者プロファイル情報カード -->
		<div class="dashboard-left-panel">
			<div class="user-card"
				style="margin: 0 !important; width: 100% !important;">
				<h3>管理者メニュー</h3>
				<p>
					ようこそ、<strong><c:out
							value="${sessionScope.loginUser.lastName}" /></strong>様！
				</p>

				<!-- 管理者（Admin）自身の登録データ表示領域 -->
				<div class="admin-info-badge-box">
					<div style="margin-bottom: 6px;">
						<strong>管理者番号 (ID):</strong><br> <span
							style="color: #ff9900; font-weight: bold;">${sessionScope.loginUser.memberId}</span>
					</div>
					<div>
						<strong>メールアドレス:</strong><br> <span style="color: #aaa;">${sessionScope.loginUser.mailAddress}</span>
					</div>
				</div>

				<!-- 自分自身の操作ボタン -->
				<div class="left-action-area">
					<button type="button" class="btn-menu btn-update"
						onclick="location.href='${pageContext.request.contextPath}/UserUpdateForm.action'">管理者情報の修正</button>
					<button type="button" class="btn-menu btn-delete"
						onclick="location.href='${pageContext.request.contextPath}/UserDelete.action'">
						自分自身のアカウントを削除</button>
				</div>
			</div>
		</div>

		<!-- 右列エリア：各種システム制御・操作ボタン群（大型の黒いコントロールパネル）-->
		<div class="dashboard-right-panel">
			<nav class="menu-links"
				style="padding: 0 !important; background: transparent !important; border: none !important; box-shadow: none !important; width: 100% !important; max-width: 100% !important; margin: 0 !important;">

				<h3>会員・アカウント管理システム</h3>
				<ul>
					<!-- 「別のadminを追加」ボタン -->
					<li><button type="button" class="btn-menu btn-admin-add"
							onclick="location.href='${pageContext.request.contextPath}/AdminAddForm.action'">
							別の管理者アカウントを追加</button></li>

					<!-- 「管理者管理」ボタン -->
					<li><button type="button" class="btn-menu btn-admin-add"
							onclick="location.href='${pageContext.request.contextPath}/AdminList.action'">
							管理者一覧・削除管理</button></li>

					<!-- 「会員管理」ボタン（一般会員・顧客のデータを統括する用） -->
					<li><button type="button" class="btn-menu btn-admin-add"
							onclick="location.href='${pageContext.request.contextPath}/AdminUserList.action'">
							顧客・一般会員情報の管理</button></li>
				</ul>

				<h3>商品管理システム</h3>
				<ul>
					<li>
						<button type="button" class="btn-menu btn-admin-add"
							onclick="location.href='${pageContext.request.contextPath}/AdminItemList.action'">商品の一覧・編集・削除</button>
					</li>
					<li><button type="button" class="btn-menu btn-admin-add"
							onclick="location.href='${pageContext.request.contextPath}/views/item-add.jsp'">新規商品の追加登録</button></li>
				</ul>

				<h3>店舗運営・分析</h3>
				<ul>
					<li>
						<button type="button" class="btn-menu btn-admin-add"
							onclick="location.href='${pageContext.request.contextPath}/AdminSales.action'">
							購入者傾向・売上分析</button>
					</li>
				</ul>

				<h3>システム制御</h3>
				<ul>
					<li><button type="button" class="btn-menu btn-logout"
							onclick="if(confirm('本当にログアウトしますか？')) { location.href='${pageContext.request.contextPath}/Logout.action'; }">ログアウト</button></li>
				</ul>

			</nav>
		</div>

	</div>

</body>
</html>
