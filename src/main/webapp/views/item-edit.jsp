<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品情報の編集 - 管理者専用</title>
<!-- 外部CSSの読み込み -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5 edit-item-form">

	<div class="menu-container">
		<div class="page-header-wrapper">
			<div class="header-left">
				<h1>商品情報の編集</h1>
				<p>変更したい項目を修正してください。</p>
			</div>
			<div class="header-right">
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-nav">管理者メニューへ戻る</a>
			</div>
		</div>

		<!-- エラーメッセージ表示領域 -->
		<c:if test="${not empty errorMessage}">
			<div class="err-msg">
				<c:out value="${errorMessage}" />
			</div>
		</c:if>

		<input type="radio" id="tab-basic" name="edit-tab" checked
			style="display: none;"> <input type="radio" id="tab-lot"
			name="edit-tab" style="display: none;">

		<div class="tab-menu">
			<label for="tab-basic" class="tab-btn">基本情報編集</label> <label
				for="tab-lot" class="tab-btn">ロット追跡・在庫管理</label>
		</div>

		<div class="tab-content-wrapper">

			<div id="basic-info" class="tab-content">

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
						<label>商品名<span>(必須)</span></label> <input type="text"
							id="itemName" name="itemName"
							value="<c:out value='${not empty keep_itemName ? keep_itemName : item.itemName}'/>"
							placeholder="例: 極上特選神戸牛カルビ" required>
							<span id="itemName-error" class="error-msg"></span>
					</div>

					<!-- フリガナ -->
					<div class="form-group">
						<label>ふりがな（カナ） <span>(必須)</span></label> <input type="text"
							id="kanaName" name="kanaName"
							value="<c:out value='${not empty keep_kanaName ? keep_kanaName : item.kanaName}'/>"
							placeholder="例: ゴクジョウトクセンコウベギュウカルビ" required>
							<span id="kanaName-error" class="error-msg"></span>
						<p style="font-size: 0.8em; color: #888; margin-top: 5px;">※全角カタカナで入力してください。</p>
					</div>

					<!-- 価格 -->
					<div class="form-group">
						<label>価格（税抜/円） <span>(必須)</span></label> <input type="number"
							id="price" name="price"
							value="<c:out value='${not empty keep_price ? keep_price : item.price}'/>"
							placeholder="例: 2980" min="0" required>
							<span id="price-error" class="error-msg"></span>
					</div>

					<!-- 在庫数 -->
					<div class="form-group">
						<label>在庫数 <span>(必須)</span></label> <input type="number"
							id="stock" name="stock"
							value="<c:out value='${not empty keep_stock ? keep_stock : item.stock}'/>">
							<span id="stock-error" class="error-msg"></span>
						<%-- 追加：在庫数変更の重みを伝える注意書き --%>
						<p style="font-size: 0.8em; color: #888; margin-top: 5px;">※在庫数を変更すると、現在庫との差分に合わせて「ロット（個体）データ」が自動調整されます。</p>
					</div>

					<!-- 肉の状態 -->
					<div class="form-group">
						<label>肉の状態</label> <input type="text"
							value="${item.meatStatus == 1 ? '熟成' : '生'}" readonly
							class="cart-select"> <input type="hidden"
							name="meatStatus" value="${item.meatStatus}">
						<p style="font-size: 0.8em; color: #888; margin-top: 5px;">
							※ロットごとの個別状態は「ロット追跡・在庫管理」タブで変更してください。</p>
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
							<span id="category-error" class="error-msg"></span>
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
						<label>商品画像 <span style="font-size: 0.8em; color: #888;">(現在の設定:
								<c:out value="${item.imagePath}" />)
						</span></label>

						<div id="dropZone" class="drop-zone" style="position: relative;">
							<input type="file" id="imageFile" name="imageFile"
								accept="image/*" class="hide-file-input">

							<div id="dropZoneText">
								<p>ここに新しい画像をドラッグ＆ドロップ</p>
								<p style="font-size: 0.8em; color: #555;">またはクリックしてファイルを選択</p>
							</div>

							<div id="previewContainer" class="preview-container">
								<img id="previewImage" class="preview-image" src="" alt="プレビュー">
							</div>
						</div>
					</div>

					<!-- ボタンエリア -->
					<div class="button-area">
						<button type="submit" class="btn-menu btn-admin-add">商品情報を更新する</button>
						<a href="${pageContext.request.contextPath}/AdminItemList.action"
							class="btn-continue">商品一覧へ戻る</a>
					</div>
				</form>
			</div>

			<div id="lot-info" class="tab-content">
				<div class="lot-management-section">
					<h3>現在の在庫・ロット一覧(入荷グループ単位)</h3>

					<form
						action="${pageContext.request.contextPath}/LotBulkUpdate.action"
						method="post">
						<input type="hidden" name="itemId" value="${item.itemId}">
						<div class="lot-scroll-box">
							<table class="lot-table">
								<thead>
									<tr>
										<th>選択</th>
										<th>入荷ロットNo.</th>
										<th>状態</th>
										<th>在庫数</th>
										<th>入荷日</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="lot" items="${lotList}">
										<tr>
											<td><input type="checkbox" name="batchIds"
												value="${lot.batchId}"></td>
											<td><a href="javascript:void(0);"
												onclick="openModal('${lot.batchId}')"> <c:out
														value="${lot.batchId}" />
											</a></td>
											<td><c:choose>
													<c:when test="${lot.meatStatus == 99}">
														<span class="status-badge mixed">混在</span>
													</c:when>
													<c:when test="${lot.meatStatus == 1}">
														<span class="status-badge aged">熟成中</span>
													</c:when>
													<c:otherwise>
														<span class="status-badge fresh">生</span>
													</c:otherwise>
												</c:choose></td>
											<td><strong>${lot.stock} 個</strong></td>
											<td><c:choose>
													<c:when test="${not empty lot.arrivalDate}">
													${lot.arrivalDate.toLocalDate()}</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="bulk-action-area">
							<div class="action-inner">
								<label>選択ロットの状態変更：</label> <select name="newStatus">
									<option value="1">熟成にする</option>
									<option value="0">生に戻す</option>
									<option value="9">廃棄にする</option>
								</select>
								<button type="submit" class="btn-menu">一括更新を実行</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div id="lotModal" class="modal-overlay" style="display: none;">
		<div class="modal-content">
			<span class="close-btn" onclick="closeModal()">&times;</span>
			<h3>ロット詳細情報</h3>
			<div id="modalBody">
				<p>情報を読み込んでいます...</p>
			</div>
		</div>
	</div>
	<!-- 外部JavaScriptファイルの読み込み -->
	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>
	<script src="${pageContext.request.contextPath}/js/image-upload.js"></script>
	<script src="${pageContext.request.contextPath}/js/item-validation.js"></script>
</body>
</html>
