/**
 * 会員情報新規登録＆変更＆管理者追加画面共通：リアルタイム＆送信時バリデーション（user-validation.js - 最終完全版）
 */
document.addEventListener("DOMContentLoaded", function() {
	// 💡 フォーム要素の取得
	const form = document.querySelector(".validated-form") || document.querySelector(".item-form-box");
	if (!form) return;

	// 正規表現パターンの定義
	const passwordRegex = /^[a-zA-Z0-9\-_\.#\$%]+$/;
	const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	// 警告文の表示・消去用共通関数
	function applyNeonRedStyle(element, message) {
		if (element) element.textContent = message;
	}
	function clearNeonError(element) {
		if (element) element.textContent = "";
	}

	// 各種入力項目・エラー表示スパンの要素取得（💡最上部で1回だけ宣言することで重複エラーを防止）
	const lastNameInput = document.getElementById("lastName");
	const firstNameInput = document.getElementById("firstName");
	const nameError = document.getElementById("name-error");

	const addressInput = document.getElementById("address");
	const addressError = document.getElementById("address-error");

	const zipInput = document.getElementById("zipCode") || document.getElementById("postCode");
	const zipError = document.getElementById("zipCode-error") || document.getElementById("postCode-error");

	const memberIdInput = document.getElementById("memberId");
	const memberIdError = document.getElementById("memberId-error");

	const passwordInput = document.getElementById("password");
	const passwordError = document.getElementById("password-error");
	const passwordConfirmInput = document.getElementById("passwordConfirm");
	const pwConfirmError = document.getElementById("passwordConfirm-error");

	const emailInput = document.getElementById("mailAddress");
	const emailError = document.getElementById("mailAddress-error");

	const currentPasswordInput = document.getElementById("currentPassword") || document.getElementById("currentAdminPassword");
	const currentPasswordError = document.getElementById("currentPassword-error") || document.getElementById("currentAdminPassword-error");

	// ==========================================================================
	// 1. お名前（姓・名）の検証ロジック
	// ==========================================================================
	function checkName() {
		if (!lastNameInput || !firstNameInput || !nameError) return true;

		const valLast = lastNameInput.value;
		const valFirst = firstNameInput.value;

		if (valLast === "" || valFirst === "") {
			applyNeonRedStyle(nameError, "⚠️ お名前（姓・名）は必須入力です。");
			return false;
		}
		if (valLast.indexOf(" ") !== -1 || valLast.indexOf("　") !== -1 ||
			valFirst.indexOf(" ") !== -1 || valFirst.indexOf("　") !== -1) {
			applyNeonRedStyle(nameError, "⚠️ お名前にスペース（空白）は使用できません。");
			return false;
		}

		clearNeonError(nameError);
		return true;
	}

	// ==========================================================================
	// 2. 住所の検証ロジック（必須入力：空欄はエラー）
	// ==========================================================================
	function checkAddress() {
		if (!addressInput || !addressError) return true;
		const value = addressInput.value;

		if (value.trim() === "") {
			applyNeonRedStyle(addressError, "⚠️ 住所は必須入力です。");
			return false;
		}

		clearNeonError(addressError);
		return true;
	}

	// ==========================================================================
	// 3. 郵便番号から住所を自動特定する機能（Zipcloud API 連携）
	// ==========================================================================

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

	// ==========================================================================
	// 4. 会員番号(ID)の検証ロジック
	// ==========================================================================
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
	// 5. 新しいパスワードの検証ロジック
	// ==========================================================================
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

		if (typeof passwordConfirmInput !== 'undefined' && passwordConfirmInput && passwordConfirmInput.value !== "") {
			checkPasswordConfirm();
		}
		return true;
	}

	// ==========================================================================
	// 6. パスワード（確認用）の検証ロジック
	// ==========================================================================
	function checkPasswordConfirm() {
		if (!passwordInput || !passwordConfirmInput || !pwConfirmError) return true;
		const val1 = passwordInput.value;
		const val2 = passwordConfirmInput.value;

		if (val2 === "") {
			applyNeonRedStyle(pwConfirmError, "⚠️ パスワード（確認用）を入力してください。");
			return false;
		}
		if (val1 !== val2) {
			applyNeonRedStyle(pwConfirmError, "⚠️ 上記で入力したパスワードと一致しません。");
			return false;
		}
		clearNeonError(pwConfirmError);
		return true;
	}

	// ==========================================================================
	// 7. メールアドレスの検証ロジック
	// ==========================================================================
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
	// 8. 現在のパスワード(本人確認用)の検証ロジック
	// ==========================================================================
	function checkCurrentPassword() {
		if (!currentPasswordInput || !currentPasswordError) return true;
		const value = currentPasswordInput.value;

		// 💡 解決：一回入力してバックスペース等で完全に消した瞬間、即座に必須エラーへ復帰させる
		if (value === "") {
			applyNeonRedStyle(currentPasswordError, "⚠️ 本人確認のため、現在のパスワードを入力してください。");
			return false;
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
	// 9. 各入力項目のリアルタイム監視（inputイベント）を確実に一斉登録！
	// ==========================================================================
	if (lastNameInput) { lastNameInput.addEventListener("input", checkName); }
	if (firstNameInput) { firstNameInput.addEventListener("input", checkName); }
	if (addressInput) { addressInput.addEventListener("input", checkAddress); }
	if (memberIdInput) { memberIdInput.addEventListener("input", checkMemberId); }
	if (passwordInput) { passwordInput.addEventListener("input", checkNewPassword); }
	if (passwordConfirmInput) { passwordConfirmInput.addEventListener("input", checkPasswordConfirm); }
	if (emailInput) { emailInput.addEventListener("input", checkEmailAddress); }

	// 現在のパスワードへのリアルタイム入力監視
	if (currentPasswordInput) { currentPasswordInput.addEventListener("input", checkCurrentPassword); }

	// 郵便番号入力時の監視（7桁に達した瞬間にZipcloud自動特定を実行）
	if (zipInput) {
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

	// ==========================================================================
	// 10. 送信ボタンが押されたときの最終チェック
	// ==========================================================================
	form.addEventListener("submit", function(e) {
		const isNameValid = checkName();
		const isZipValid = checkZipCode();
		const isAddressValid = checkAddress();
		const isIdValid = checkMemberId();
		const isNewPwValid = checkNewPassword();
		const isEmailValid = checkEmailAddress();

		let isConfirmValid = true;
		if (passwordConfirmInput) {
			isConfirmValid = checkPasswordConfirm();
		}

		let hasError = !isIdValid || !isNewPwValid || !isConfirmValid || !isEmailValid || !isNameValid || !isAddressValid || !isZipValid;

		// 現在のパスワード（本人確認）欄がある場合の検証
		if (currentPasswordInput) {
			if (currentPasswordInput.value === "") {
				hasError = true;
				applyNeonRedStyle(currentPasswordError, "⚠️ 本人確認のため、現在のパスワードを入力してください。");
				currentPasswordInput.focus();
			} else {
				const isCurrentPwValid = checkCurrentPassword();
				if (!isCurrentPwValid) hasError = true;
			}
		}

		// 画面上に1箇所でもエラー文字列（textContent）が残っているか全走査
		const errorSpans = form.querySelectorAll(".error-msg");
		errorSpans.forEach(span => {
			if (span.textContent !== "") {
				hasError = true;
			}
		});

		if (hasError) {
			e.preventDefault(); // JavaのAction/Servletへの送信動作を停止
			alert("正しく入力されていない項目があります。\n赤文字の指示に従って修正してください。");
		}
	});
});
