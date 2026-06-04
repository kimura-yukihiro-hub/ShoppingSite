/**
 * 会員情報新規登録＆変更画面共通：リアルタイム＆送信時バリデーション（user-validation.js - 最終完全版）
 */
document.addEventListener("DOMContentLoaded", function() {
	const form = document.querySelector(".validated-form");
	if (!form) return;

	// 正規表現パターンの定義
	const passwordRegex = /^[a-zA-Z0-9\-_\.#\$%]+$/;
	const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	// 警告文の表示・消去用共通関数
	function applyNeonRedStyle(element, message) {
		element.textContent = message;
	}
	function clearNeonError(element) {
		element.textContent = "";
	}

	// ==========================================================================
	// 1. 会員番号(ID)の検証ロジック
	// ==========================================================================
	const memberIdInput = document.getElementById("memberId");
	const memberIdError = document.getElementById("memberId-error");

	function checkMemberId() {
		if (!memberIdInput || !memberIdError) return true;
		const value = memberIdInput.value;

		if (value === "") {
			applyNeonRedStyle(memberIdError, "⚠️ 会員番号は必須入力です。");
			return false;
		}
		if (value.indexOf(" ") !== -1 || value.indexOf("　") !== -1) {
			applyNeonRedStyle(memberIdError, "⚠️ 会員番号にスペース（空白）は使用できません。");
			return false;
		}
		if (!/^[a-zA-Z0-9\-]+$/.test(value)) {
			applyNeonRedStyle(memberIdError, "⚠️ 半角英数字とハイフン（-）のみ使用できます。");
			return false;
		}
		clearNeonError(memberIdError);
		return true;
	}

	// ==========================================================================
	// 2. 新しいパスワードの検証ロジック
	// ==========================================================================
	const passwordInput = document.getElementById("password");
	const passwordError = document.getElementById("password-error");

	function checkNewPassword() {
		if (!passwordInput || !passwordError) return true;
		const value = passwordInput.value;

		if (value === "") {
			applyNeonRedStyle(passwordError, "⚠️ パスワードは必須入力です。");
			return false;
		}
		if (value.indexOf(" ") !== -1 || value.indexOf("　") !== -1) {
			applyNeonRedStyle(passwordError, "⚠️ パスワードにスペース（空白）は使用できません。");
			return false;
		}
		if (value.length < 4) {
			applyNeonRedStyle(passwordError, "⚠️ パスワードは4文字以上で入力してください。");
			return false;
		}
		if (!passwordRegex.test(value)) {
			applyNeonRedStyle(passwordError, "⚠️ 半角英数字・特定の記号（- _ . # $ %）のみ使用できます。");
			return false;
		}
		clearNeonError(passwordError);

		// パスワード確認欄に入力がある場合は、連動して一致チェックも走らせる
		if (typeof passwordConfirmInput !== 'undefined' && passwordConfirmInput && passwordConfirmInput.value !== "") {
			checkPasswordConfirm();
		}
		return true;
	}

	// ==========================================================================
	// 3. パスワード（確認用）の検証ロジック（💡 リアルタイム監視用関数）
	// ==========================================================================
	const passwordConfirmInput = document.getElementById("passwordConfirm");
	const passwordConfirmError = document.getElementById("passwordConfirm-error");

	function checkPasswordConfirm() {
		if (!passwordInput || !passwordConfirmInput || !passwordConfirmError) return true;
		const val1 = passwordInput.value;
		const val2 = passwordConfirmInput.value;

		if (val2 === "") {
			applyNeonRedStyle(passwordConfirmError, "⚠️ パスワード（確認用）を入力してください。");
			return false;
		}
		if (val1 !== val2) {
			applyNeonRedStyle(passwordConfirmError, "⚠️ 上記で入力したパスワードと一致しません。");
			return false;
		}
		clearNeonError(passwordConfirmError);
		return true;
	}

	// ==========================================================================
	// 4. メールアドレスの検証ロジック
	// ==========================================================================
	const emailInput = document.getElementById("mailAddress");
	const emailError = document.getElementById("mailAddress-error");

	function checkEmailAddress() {
		if (!emailInput || !emailError) return true;
		const value = emailInput.value;

		if (value === "") {
			applyNeonRedStyle(emailError, "⚠️ メールアドレスは必須入力です。");
			return false;
		}
		if (value.indexOf(" ") !== -1 || value.indexOf("　") !== -1) {
			applyNeonRedStyle(emailError, "⚠️ メールアドレスにスペース（空白）は使用できません。");
			return false;
		}
		if (!emailRegex.test(value)) {
			applyNeonRedStyle(emailError, "⚠️ 正しいメールアドレスの形式で入力してください。");
			return false;
		}
		clearNeonError(emailError);
		return true;
	}

	// ==========================================================================
	// 5. 現在のパスワード(本人確認用)の検証ロジック（変更画面用）
	// ==========================================================================
	const currentPasswordInput = document.getElementById("currentPassword");
	const currentPasswordError = document.getElementById("currentPassword-error");

	function checkCurrentPassword() {
		if (!currentPasswordInput || !currentPasswordError) return true;
		const value = currentPasswordInput.value;

		if (value === "") {
			clearNeonError(currentPasswordError);
			return true;
		}
		if (value.indexOf(" ") !== -1 || value.indexOf("　") !== -1) {
			applyNeonRedStyle(currentPasswordError, "⚠️ パスワードにスペース（空白）は使用できません。");
			return false;
		}
		if (value.length < 4) {
			applyNeonRedStyle(currentPasswordError, "⚠️ パスワードは4文字以上で入力してください。");
			return false;
		}
		if (!passwordRegex.test(value)) {
			applyNeonRedStyle(currentPasswordError, "⚠️ 半角英数字・特定の記号（- _ . # $ %）のみ使用できます。");
			return false;
		}
		clearNeonError(currentPasswordError);
		return true;
	}

	// ==========================================================================
	// 各入力項目のリアルタイム監視（inputイベント）を確実に一斉登録！
	// ==========================================================================
	if (memberIdInput) { memberIdInput.addEventListener("input", checkMemberId); }
	if (passwordInput) { passwordInput.addEventListener("input", checkNewPassword); }
	if (passwordConfirmInput) { passwordConfirmInput.addEventListener("input", checkPasswordConfirm); }
	if (emailInput) { emailInput.addEventListener("input", checkEmailAddress); }
	if (currentPasswordInput) { currentPasswordInput.addEventListener("input", checkCurrentPassword); }


	// ==========================================================================
	// 6. 送信ボタンが押されたときの最終チェック
	// ==========================================================================
	form.addEventListener("submit", function(e) {
		const isIdValid = checkMemberId();
		const isNewPwValid = checkNewPassword();
		const isEmailValid = checkEmailAddress();

		// 画面に「パスワード確認用入力欄」が存在するときだけ強制起動
		let isConfirmValid = true;
		if (typeof passwordConfirmInput !== 'undefined' && passwordConfirmInput) {
			isConfirmValid = checkPasswordConfirm();
		}

		let hasError = !isIdValid || !isNewPwValid || !isConfirmValid || !isEmailValid;

		// 変更画面用の「現在のパスワード」欄が存在するときだけチェックを行う
		if (typeof currentPasswordInput !== 'undefined' && currentPasswordInput && currentPasswordError) {
			if (currentPasswordInput.value === "") {
				hasError = true;
				applyNeonRedStyle(currentPasswordError, "⚠️ 本人確認のため、現在のパスワードを入力してください。");
				currentPasswordInput.focus();
			}
		}

		// 他のリアルタイムエラーメッセージが残っていないか走査
		const errorSpans = form.querySelectorAll(".error-msg");
		errorSpans.forEach(span => {
			if (span.textContent !== "") {
				hasError = true;
			}
		});

		if (hasError) {
			e.preventDefault(); // JavaのActionへの送信を停止
			alert("入力内容にエラーがあります。修正してください。");
		}
	});

	// ==========================================================================
	// 7. 郵便番号から住所を自動特定する機能（Zipcloud API 連携）
	// ==========================================================================
	const zipInput = document.getElementById("zipCode");
	const zipError = document.getElementById("zipCode-error");
	const addressInput = document.getElementById("address");

	if (zipInput && addressInput) {

		// 郵便番号専用のリアルタイムバリデーション関数
		const checkZipCode = () => {
			const value = zipInput.value;
			const cleanVal = value.replace(/-/g, "").trim();

			if (value === "") {
				clearNeonError(zipError);
				return true;
			}
			if (value.indexOf(" ") !== -1 || value.indexOf("　") !== -1) {
				applyNeonRedStyle(zipError, "⚠️ 郵便番号にスペース（空白）は使用できません。");
				return false;
			}
			if (/[^\d-]/.test(value)) {
				applyNeonRedStyle(zipError, "⚠️ 郵便番号は半角数字とハイフン（-）のみ使用できます。");
				return false;
			}
			if (cleanVal.length > 7) {
				applyNeonRedStyle(zipError, "⚠️ 郵便番号は7桁で入力してください。");
				return false;
			}

			clearNeonError(zipError);
			return true;
		};

		// 住所検索を実行する共通関数
		const searchAddress = () => {
			if (!checkZipCode()) return;

			const zipValue = zipInput.value.replace(/-/g, "").trim();

			if (zipValue === "") {
				applyNeonRedStyle(zipError, "⚠️ 郵便番号を入力してください。");
				return;
			}
			if (!/^\d{7}$/.test(zipValue)) {
				applyNeonRedStyle(zipError, "⚠️ 郵便番号は半角数字7桁で入力してください。");
				return;
			}

			clearNeonError(zipError);
			applyNeonRedStyle(zipError, "⏳ 住所を検索中...");

			fetch(`https://zipcloud.ibsnet.co.jp/api/search?zipcode=${zipValue}`)
				.then(response => response.json())
				.then(data => {
					if (data.status === 200 && data.results) {
						const result = data.results[0];
						const fullAddress = result.address1 + result.address2 + result.address3;

						addressInput.value = fullAddress;
						clearNeonError(zipError);
						addressInput.focus();
					} else {
						applyNeonRedStyle(zipError, "⚠️ 該当する住所が見つかりませんでした。");
					}
				})
				.catch(error => {
					console.error("Error:", error);
					applyNeonRedStyle(zipError, "⚠️ 通信エラーが発生しました。時間をおいてお試しください。");
				});
		};

		// 郵便番号のリアルタイム監視
		zipInput.addEventListener("input", function() {
			const isValid = checkZipCode();
			if (isValid) {
				const cleanVal = this.value.replace(/-/g, "").trim();
				if (cleanVal.length === 7) {
					searchAddress();
				}
			}
		});
	}

});