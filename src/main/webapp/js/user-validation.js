/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	const form = document.querySelector(".validated-form");
	if (!form) return;

	const memberIdInput = document.getElementById("memberId");
	const passwordInput = document.getElementById("password");

	//会員番号のリアルタイムバリデーション 
	if (memberIdInput) {
		memberIdInput.addEventListener("input", function() {
			const errorSpan = document.getElementById("memberId-error");
			const regex = /^[a-zA-Z0-9\-]+$/;

			if (this.value === "") {
				errorSpan.textContent = "会員番号は必須入力です。";
			} else if (!regex.test(this.value)) {
				errorSpan.textContent = "半角英数字とハイフン（-）のみ使用できます。";
			} else {
				errorSpan.textContent = ""; // 正常時はクリア
			}
		});
	}

	//パスワードのリアルタイムバリデーション（半角英数字+記号のみ）
	if (passwordInput) {
		passwordInput.addEventListener("input", function() {
			const errorSpan = document.getElementById("password-error");
			const regex = /^[a-zA-Z0-9\-_\.#\$%]+$/;

			if (this.value === "") {
				errorSpan.textContent = "パスワードは必須入力です。";
			} else if (!regex.test(this.value)) {
				errorSpan.textContent = "半角英数字+記号のみ使用できます。";
			} else {
				errorSpan.textContent = ""; // 正常時はクリア
			}
		});
	}

	//現在のパスワード(本人確認用)欄のリアルタイム監視
	const currentPasswordInput = document.getElementById("currentPassword");
	const currentPasswordError = document.getElementById("currentPassword-error");

	if (currentPasswordInput && currentPasswordError) {
		currentPasswordInput.addEventListener("input", function() {
			const value = currentPasswordInput.value;
			if (value === "") {
				currentPasswordError.textContent = "";
			} else if (!regex.test(value)) {
				//不正な文字が入ったら、新設した専用spanにパッと赤文字警告を出します
				currentPasswordError.textContent = "半角英数字と一部の記号(-_.#$%)のみ使用できます。";
			} else {
				currentPasswordError.textContent = "";
			}
		});
	}

// 送信ボタンが押されたときの最終チェック（エラーがあれば送信を止める）
form.addEventListener("submit", function(e) {
	let hasError = false;
	const errorSpans = form.querySelectorAll(".error-msg");

	errorSpans.forEach(span => {
		if (span.textContent !== "") {
			hasError = true;
		}
	});

	if (hasError) {
		e.preventDefault(); // 画面遷移をブロック
		alert("入力内容にエラーがあります。修正してください。");
	}
});
});

