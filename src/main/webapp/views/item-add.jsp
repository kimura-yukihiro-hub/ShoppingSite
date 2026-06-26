<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規商品登録 - 管理者専用</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5">
	<div class="menu-container">
		<div class="user-card">
			<h3>新規商品の追加登録</h3>
			<p>新しく店舗に追加するお肉の情報を入力してください。</p>
		</div>

		<form action="${pageContext.request.contextPath}/ItemAdd.action"
			method="post" enctype="multipart/form-data"
			onsubmit="return confirm('この商品を新しく登録します。よろしいですか？');"
			class="item-form-box validated-form">

			<input type="hidden" name="itemId" value="${item.itemId}"> <input
				type="hidden" name="currentImagePath" value="${item.imagePath}">

			<div class="form-group">
				<label>商品名<span>(必須)</span></label> <input type="text" id="itemName"
					name="itemName" placeholder="例: 極上特選神戸牛カルビ"
					value="<c:out value='${savedItemName}' />" required> <span
					id="itemName-error" class="error-msg"></span>
			</div>

			<div class="form-group">
				<label>ふりがな（カナ） <span>(必須)</span></label> <input type="text"
					id="kanaName" name="kanaName" placeholder="例: ゴクジョウトクセンコウベギュウカルビ"
					value="<c:out value='${savedKanaName}' />" required> <span
					id="kanaName-error" class="error-msg"></span>
				<p style="font-size: 0.8em; color: #888; margin-top: 5px;">※全角カタカナで入力してください。（長音「ー」は使用可能です）</p>
			</div>

			<div class="form-group">
				<label>価格（税抜/円） <span>(必須)</span></label> <input type="number"
					id="price" name="price" placeholder="例: 2980" min="0"
					value="<c:out value='${savedPrice}' />" required> <span
					id="price-error" class="error-msg"></span>
			</div>

			<div class="form-group">
				<label>初期在庫数 <span>(必須)</span></label> <input type="number"
					id="stock" name="stock" placeholder="例: 30" min="0"
					value="<c:out value='${savedStock}' />" required> <span
					id="stock-error" class="error-msg"></span>
				<p style="font-size: 0.8em; color: #888; margin-top: 5px;">※ここに入力した数だけ「ロット（個体）番号」が自動生成されます。</p>
			</div>

			<div class="form-group">
				<label>肉の状態</label> <select id="meatStatus" name="meatStatus"
					class="cart-select">
					<option value="0" ${savedMeatStatus == '0' ? 'selected' : ''}>生</option>
					<option value="1" ${savedMeatStatus == '1' ? 'selected' : ''}>熟成</option>
				</select>
			</div>

			<div class="form-group">
				<label>肉の種類</label> <select id="meatType" name="meatType"
					class="cart-select">
					<option value="牛" ${savedMeatType == '牛' ? 'selected' : ''}>牛肉</option>
					<option value="豚" ${savedMeatType == '豚' ? 'selected' : ''}>豚肉</option>
					<option value="鶏" ${savedMeatType == '鶏' ? 'selected' : ''}>鶏肉</option>
				</select>
			</div>

			<div class="form-group">
				<label>部位 / カテゴリ <span>(必須)</span></label> <input type="text"
					id="category" name="category" placeholder="例: カルビ、ロース、モモ"
					maxlength="20" value="<c:out value='${savedCategory}' />" required>
				<span id="category-error" class="error-msg"></span>
			</div>

			<div class="form-group">
				<label>商品説明</label>
				<textarea id="description" name="description"
					placeholder="商品の特徴を入力してください" rows="4"><c:out
						value='${savedDescription}' /></textarea>
			</div>

			<div class="form-group">
				<label>商品画像</label>
				<div id="dropZone" class="drop-zone">
					<input type="file" id="imageFile" name="imageFile" accept="image/*"
						class="hide-file-input">
					<div id="dropZoneText">
						<p>ここに新しい画像をドラッグ＆ドロップ</p>
						<p style="font-size: 0.8em; color: #555;">またはクリックしてファイルを選択</p>
					</div>

					<div id="previewContainer" class="preview-container">
						<img id="previewImage" class="preview-image" src="" alt="プレビュー">
					</div>
				</div>
			</div>

			<div class="button-area">
				<button type="submit" class="btn-menu btn-admin-add">新規商品を登録する</button>
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-continue">管理者メニューへ戻る</a>
			</div>
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/js/image-upload.js"></script>
	<script src="${pageContext.request.contextPath}/js/item-validation.js"></script>
</body>
</html>