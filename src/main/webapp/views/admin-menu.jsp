<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
//1. 画面データを一時保存(cache)することも、店舗(store)に記憶することも禁止
//さらに、次回開くときは必ずサーバーに「このセッションはまだ有効か？」を再確認(validate)させる
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//2. 過去の古いブラウザに対しても、同様にキャッシュを禁止する命令
response.setHeader("Pragma", "no-cache");
//3. この画面データの有効期限(Expires)を「0（今すぐ切れる）」に設定
//これにより、ブラウザは「この画面はもう古いから、使い回しちゃダメだ」と判断
response.setDateHeader("Expires", 0);
%>
<script>
	// ログイン通信の履歴(POST)を安全なURL(GET)へすり替えて、ブラウザの「戻る」による誤作動を防ぎます
	window.history.replaceState(null, null, window.location.href);
</script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理者専用メインメニュー</title>
<link rel="stylesheet" href="/ShoppingSite/css/style.css">
</head>
<body class="rank-a5">
	<div class="menu-container">
		<div class="user-card">
			<h3>管理者メニュー</h3>
			<p>
				ようこそ、<strong>${sessionScope.loginUser.lastName}</strong>様！
			</p>

			<!-- 💡 管理者（Admin）自身の登録データを確認用に配置するシースルー領域-->
			<div
				style="font-size: 13px; text-align: left; background: rgba(0, 0, 0, 0.2); padding: 10px; border-radius: 6px; margin: 10px 0; border: 1px solid #4a545c;">
				<div style="margin-bottom: 4px;">
					<strong>管理者番号 (ID):</strong> ${sessionScope.loginUser.memberId}
				</div>
				<div>
					<strong>メールアドレス:</strong> ${sessionScope.loginUser.mailAddress}
				</div>
			</div>
		</div>

		<nav class="menu-links">
			<h3>管理者アカウント管理</h3>
			<ul>
				<li>
					<button type="button" class="btn-menu btn-update"
						onclick="location.href='${pageContext.request.contextPath}/UserUpdateForm.action'">管理者情報の修正</button>
				</li>
				<li>
					<button type="button" class="btn-menu btn-delete"
						onclick="location.href='${pageContext.request.contextPath}/UserDelete.action'">
						管理者アカウントの削除</button>
				</li>
			</ul>

			<h3>商品管理システム</h3>
			<ul>
				<li>
					<button type="button" class="btn-menu btn-admin-add"
						onclick="alert('商品追加登録機能は次の演習で実装します。')">新規商品の追加登録</button>
				</li>
			</ul>

			<h3>システム制御</h3>
			<ul>
				<li>
					<button type="button" class="btn-menu btn-logout"
						onclick="if(confirm('本当にログアウトしますか？')) { location.href='${pageContext.request.contextPath}/Logout.action'; }">
						ログアウト</button>
				</li>
			</ul>
		</nav>
	</div>

</body>
</html>