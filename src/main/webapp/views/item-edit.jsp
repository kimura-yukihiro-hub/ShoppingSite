<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品情報の編集 - 管理者専用</title>
<!-- 外部CSSの読み込み -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">

	<div class="menu-container">
		<div class="user-card">
			<h3>商品情報の編集</h3>
			<p>変更したい項目を修正してください。</p>
		</div>

		<!-- エラーメッセージ表示領域 -->
		<c:if test="${not empty errorMessage}">
			<div class="err-msg">
				<c:out value="${errorMessage}" />
			</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/ItemUpdate.action"
			method="post" enctype="multipart/form-data"
			onsubmit="return confirm('この内容で更新します。よろしいですか？');"
			class="item-form-box">

			<!-- 更新対象を特定する隠しパラメータ -->
			<input type="hidden" name="itemId"
				value="<c:out value='${item.itemId}'/>"> <input
				type="hidden" name="currentImagePath"
				value="<c:out value='${item.imagePath}'/>">

			<!-- 商品名 -->
			<div class="form-group">
				<label>商品名<span>(必須)</span></label> <input type="text" id="itemName"
					name="itemName"
					value="<c:out value='${not empty keep_itemName ? keep_itemName : item.itemName}'/>"
					placeholder="例: 極上特選神戸牛カルビ" required>
			</div>

			<!-- 価格 -->
			<div class="form-group">
				<label>価格（税抜/円） <span>(必須)</span></label> <input type="number"
					id="price" name="price"
					value="<c:out value='${not empty keep_price ? keep_price : item.price}'/>"
					placeholder="例: 2980" min="0" required>
			</div>

			<!-- 在庫数 -->
			<div class="form-group">
				<label>在庫数 <span>(必須)</span></label> <input type="number" id="stock"
					name="stock"
					value="<c:out value='${not empty keep_stock ? keep_stock : item.stock}'/>"
					placeholder="例: 30" min="0" required>
			</div>

			<!-- 肉の状態 -->
			<c:set var="currentStatus"
				value="${not empty keep_meatStatus ? keep_meatStatus : item.meatStatus}" />
			<div class="form-group">
				<label>肉の状態</label> <select id="meatStatus" name="meatStatus"
					class="cart-select">
					<option value="生" ${currentStatus == '生' ? 'selected' : ''}>生（フレッシュ）</option>
					<option value="熟成" ${currentStatus == '熟成' ? 'selected' : ''}>熟成（エイジング）</option>
				</select>
			</div>

			<!-- 肉の種類 -->
			<c:set var="currentType"
				value="${not empty keep_meatType ? keep_meatType : item.meatType}" />
			<div class="form-group">
				<label>肉の種類</label> <select id="meatType" name="meatType"
					class="cart-select">
					<option value="牛" ${currentType == '牛' ? 'selected' : ''}>牛肉</option>
					<option value="豚" ${currentType == '豚' ? 'selected' : ''}>豚肉</option>
					<option value="鶏" ${currentType == '鶏' ? 'selected' : ''}>鶏肉</option>
				</select>
			</div>

			<!-- 部位/カテゴリ -->
			<div class="form-group">
				<label>部位 / カテゴリ <span>(必須)</span></label> <input type="text"
					id="category" name="category"
					value="<c:out value='${not empty keep_category ? keep_category : item.category}'/>"
					placeholder="例: カルビ、ロース、モモ" maxlength="20" required>
			</div>

			<!-- 商品説明 -->
			<div class="form-group">
				<label>商品説明</label>
				<textarea id="description" name="description"
					placeholder="商品の特徴やこだわりを入力してください" rows="4"><c:out
						value="${not empty keep_description ? keep_description : item.description}" /></textarea>
			</div>

			<!-- 商品画像-->
			<div class="form-group">
				<label>商品画像 <span
					style="font-size: 0.8em; color: #888; font-weight: normal;">(現在の設定:
						<c:out value="${item.imagePath}" />)
				</span></label> <input type="file" id="imageFile" name="imageFile" accept="image/*"
					style="display: none;">
				<div id="dropZone" class="drop-zone">
					<p>ここに新しい画像をドラッグ＆ドロップ</p>
					<p style="font-size: 0.75em; color: #555;">またはクリックしてファイルを選択</p>
					<div id="previewContainer" class="preview-container"
						style="display: none;">
						<img id="previewImage" class="preview-image" src="" alt="プレビュー">
					</div>
				</div>
			</div>

			<!-- ボタンエリア -->
			<div class="button-area">
				<button type="submit" class="btn-menu btn-admin-add">商品情報を更新する</button>
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-continue">管理者メニューへ戻る</a>
			</div>
		</form>
	</div>


	<!-- 外部JavaScriptファイルの読み込み -->
	<script src="${pageContext.request.contextPath}/js/upload.js"></script>
</body>
</html>
