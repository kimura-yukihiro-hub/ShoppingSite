/**
 * パスワード表示・非表示(目玉アイコン)切り替えスクリプト（完全決定版）
 * ログイン画面、会員情報新規登録画面、会員情報変更画面、管理者追加画面のすべてで安全に動作します。
 */
document.addEventListener('DOMContentLoaded', function() {

	// 目を開ける(表示) / 目を閉じる(非表示) のSVGパス定義
	const closedEyePath = '<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>';
	const openEyePath = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>';

	/**
	 * パスワードの可視性を切り替える共通関数（JSPバグ回避版）
	 */
	function togglePasswordVisibility(input, btn, icon) {
		if (input.type === 'password') {
			input.type = 'text';
			icon.innerHTML = openEyePath;
			btn.setAttribute('aria-label', 'パスワードを非表示');
		} else {
			input.type = 'password';
			icon.innerHTML = closedEyePath;
			btn.setAttribute('aria-label', 'パスワードを表示');
		}
	}

	// ==========================================================================
	// 1. 新しいパスワード / 通常ログイン用パスワードの表示切り替え（右側・上）
	// ==========================================================================
	const passwordInput = document.getElementById('password');
	const toggleButton = document.getElementById('toggle-password');
	const eyeIcon = document.getElementById('eye-icon');

	if (passwordInput && toggleButton && eyeIcon) {
		toggleButton.style.position = "absolute";
		toggleButton.style.zIndex = "20";
		toggleButton.style.cursor = "pointer";

		toggleButton.addEventListener('click', function(e) {
			e.preventDefault();
			e.stopPropagation();
			togglePasswordVisibility(passwordInput, toggleButton, eyeIcon);
		});
	}

	// ==========================================================================
	// 2. 現在のパスワード（会員情報変更画面用）の表示切り替え
	// ==========================================================================
	const currentInput = document.getElementById('currentPassword');
	const toggleCurrentBtn = document.getElementById('toggle-password-current');
	const eyeIconCurrent = document.getElementById('eye-icon-current');

	if (currentInput && toggleCurrentBtn && eyeIconCurrent) {
		toggleCurrentBtn.style.position = "absolute";
		toggleCurrentBtn.style.zIndex = "20";
		toggleCurrentBtn.style.cursor = "pointer";

		toggleCurrentBtn.addEventListener('click', function(e) {
			e.preventDefault();
			e.stopPropagation();
			togglePasswordVisibility(currentInput, toggleCurrentBtn, eyeIconCurrent);
		});
	}

	// ==========================================================================
	// 3. パスワード（確認用・新規登録画面用＆管理者追加画面用）の制御（右側・下）
	// ==========================================================================
	const confirmInput = document.getElementById('passwordConfirm');
	const toggleConfirmBtn = document.getElementById('toggle-password-confirm');
	const eyeIconConfirm = document.getElementById('eye-icon-confirm');

	if (confirmInput && toggleConfirmBtn && eyeIconConfirm) {
		toggleConfirmBtn.style.position = "absolute";
		toggleConfirmBtn.style.zIndex = "20";
		toggleConfirmBtn.style.cursor = "pointer";

		toggleConfirmBtn.addEventListener('click', function(e) {
			e.preventDefault(); e.stopPropagation();
			togglePasswordVisibility(confirmInput, toggleConfirmBtn, eyeIconConfirm);
		});
	}

	// ==========================================================================
	// 4. 💡あなたの現在のパスワード（管理者追加画面・本人確認用、左側・下）の制御
	// ==========================================================================
	const adminCurrentInput = document.getElementById('currentAdminPassword');
	const toggleAdminCurrentBtn = document.getElementById('toggle-password-admin-current');
	const eyeIconAdminCurrent = document.getElementById('eye-icon-admin-current');

	if (adminCurrentInput && toggleAdminCurrentBtn && eyeIconAdminCurrent) {
		toggleAdminCurrentBtn.style.position = "absolute";
		toggleAdminCurrentBtn.style.zIndex = "20";
		toggleAdminCurrentBtn.style.cursor = "pointer";

		toggleAdminCurrentBtn.addEventListener('click', function(e) {
			e.preventDefault(); e.stopPropagation();
			togglePasswordVisibility(adminCurrentInput, toggleAdminCurrentBtn, eyeIconAdminCurrent);
		});
	}

	// ==========================================================================
	// 5. ログインフォーム専用のバリデーション（入力チェック）処理
	// ==========================================================================
	const loginForm = document.getElementById('login-form');
	const memberIdInput = document.getElementById('memberId');
	const memberIdError = document.getElementById('memberId-error');
	const passwordError = document.getElementById('password-error');

	// ログインフォームが画面に存在する時だけバリデーションを実行（変更画面でのエラーを防ぐ）
	if (loginForm && memberIdInput && passwordInput && memberIdError && passwordError) {

		memberIdInput.addEventListener('input', checkMemberId);
		passwordInput.addEventListener('input', checkPassword);

		loginForm.addEventListener('submit', function(event) {
			const isIdValid = checkMemberId();
			const isPwValid = checkPassword();

			if (!isIdValid || !isPwValid) {
				event.preventDefault(); // エラーがあればサーバーへの送信を停止
			}
		});

		function checkMemberId() {
			const value = memberIdInput.value;
			if (value === "") {
				showError(memberIdInput, memberIdError, "会員番号を入力してください");
				return false;
			}
			if (/\s/g.test(value)) { //スペースが1文字でも含まれていたらエラー
				showError(memberIdInput, memberIdError, "会員番号にスペース（空白）は使用できません");
				return false;
			}
			if (!/^[a-zA-Z0-9\-]+$/.test(value)) {
				showError(memberIdInput, memberIdError, "会員番号は半角英数字・ハイフンのみ有効です");
				return false;
			}
			clearError(memberIdInput, memberIdError);
			return true;
		}

		function checkPassword() {
			const value = passwordInput.value;

			if (value === "") {
				showError(passwordInput, passwordError, "パスワードを入力してください");
				return false;
			}
			if (/\s/g.test(value)) { //スペースが含まれているか判定
				showError(passwordInput, passwordError, "パスワードにスペース（空白）は使用できません");
				return false;
			}
			if (value.length < 4) {
				showError(passwordInput, passwordError, "パスワードは4文字以上で入力してください");
				return false;
			}
			if (!/^[a-zA-Z0-9!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]+$/.test(value)) {
				showError(passwordInput, passwordError, "パスワードは半角英数字・記号のみで入力してください");
				return false;
			}
			clearError(passwordInput, passwordError);
			return true;
		}

		function showError(inputElement, errorElement, message) {
			inputElement.classList.add('is-invalid');
			errorElement.textContent = message;
			return false;
		}

		function clearError(inputElement, errorElement) {
			inputElement.classList.remove('is-invalid');
			errorElement.textContent = "";
			return true;
		}
	}
});
