/**
 * 商品画像アップロード・プレビュー制御用スクリプト
 */
document.addEventListener('DOMContentLoaded', () => {
	const dropZone = document.getElementById('dropZone');
	const fileInput = document.getElementById('imageFile');
	const previewContainer = document.getElementById('previewContainer');
	const previewImage = document.getElementById('previewImage');

	if (!dropZone || !fileInput) return;

	// エリアをクリックしたら隠しinputをクリックしたことにする
	dropZone.addEventListener('click', () => fileInput.click());

	// ドラッグ中のエフェクト制御
	['dragenter', 'dragover'].forEach(eventName => {
		dropZone.addEventListener(eventName, (e) => {
			e.preventDefault();
			e.stopPropagation(); 
			dropZone.classList.add('drag-over');
		}, false);
	});

	['dragleave', 'drop'].forEach(eventName => {
		dropZone.addEventListener(eventName, (e) => {
			e.preventDefault();
			e.stopPropagation(); 
			dropZone.classList.remove('drag-over');
		}, false);
	});

	// ドロップされた時の処理
	dropZone.addEventListener('drop', (e) => {
		const dt = e.dataTransfer;
		const files = dt.files;
		if (files && files.length > 0) {
			fileInput.files = files; // 隠しinputにファイルをセット
			handleFiles(files[0]);   // プレビュー処理へ
		}
	});

	// ファイルダイアログから選択された時の処理
	fileInput.addEventListener('change', (e) => {
		if (e.target.files && e.target.files.length > 0) {
			handleFiles(e.target.files[0]);
		}
	});

	/**
	 * プレビュー表示の共通処理
	 */
	function handleFiles(file) {
		// 画像ファイル以外が放り込まれた場合は即座に弾く
		if (!file.type.startsWith('image/')) {
			alert('画像ファイル（PNG、JPEGなど）を選択してください。');
			return;
		}

		// JSPに追加した文字の塊（dropZoneText）をJavaScriptに連動させる
		const dropZoneText = document.getElementById('dropZoneText');

		const reader = new FileReader();

		// 💡【安全対策】読み込み開始時に、前回の古いプレビュー画像を一度クリアしてバグを防ぐ
		if (previewImage) {
			previewImage.src = '';
		}

		reader.onload = function(e) {
			if (previewImage && previewContainer) {
				previewImage.src = e.target.result;
				previewContainer.style.display = 'block';
			}

			// 画像が読み込まれたら、枠の中の案内テキスト（「ここにドロップ」など）を完全に非表示にする！
			if (dropZoneText) {
				dropZoneText.style.display = 'none';
			}
		};

		reader.readAsDataURL(file);
	}
});