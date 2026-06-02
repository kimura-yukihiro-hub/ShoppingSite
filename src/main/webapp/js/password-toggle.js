/**
 * ログイン画面:パスワードの表示・非表示(目玉アイコン)切り替えスクリプト
 */
document.addEventListener('DOMContentLoaded', function() {

	// ==========================================================================
	// 1. パスワードの表示・非表示（目のマーク）切り替え処理
	// ==========================================================================

	const passwordInput = document.getElementById('password');
	const toggleButton = document.getElementById('toggle-password');
	const eyeIcon = document.getElementById('eye-icon');

	//エラー防止: 画面に要素があるときだけ要素を動かす
	if (passwordInput && toggleButton && eyeIcon) {

		//目を開ける(表示)/目を閉じる(非表示)のSVGの中身
		const closedEyePath = '<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>';
		const openEyePath = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>';

		toggleButton.addEventListener('click', function() {
			if (passwordInput.type === 'password') {
				passwordInput.type = 'text'; //文字を見せる
				eyeIcon.innerHTML = openEyePath; //目に斜線が入ったアイコンに戻す
				toggleButton.setAttribute('aria-label', 'パスワードを非表示');
			} else {
				passwordInput.type = 'password'; //伏字に戻す
				eyeIcon.innerHTML = closedEyePath; //目に斜線が入ったアイコンに返信
				toggleButton.setAttribute('aria-label', 'パスワードを表示');
			}
		});
	}
	// ==========================================================================
	// 2. ログインフォームのバリデーション（入力チェック）処理
	// ==========================================================================

	const loginForm = document.getElementById('login-form');
	const memberIdInput = document.getElementById('memberId');
	const memberIdError = document.getElementById('memberId-error');
	const passwordError = document.getElementById('password-error');

	if (loginForm && memberIdInput && passwordInput) {

		// リアルタイムで入力内容をチェックするイベントを設定
		memberIdInput.addEventListener('input', checkMemberId);
		passwordInput.addEventListener('input', checkPassword);

		// ログインボタンが押されたときの最終チェック
		loginForm.addEventListener('submit', function(event) {
			const isIdValid = checkMemberId();
			const isPwValid = checkPassword();

			// エラーがある場合は、サーバー（Java）への送信を完全にストップする
			if (!isIdValid || !isPwValid) {
				event.preventDefault();
			}
		});

		// 会員番号（ID）のチェック基準
		function checkMemberId() {
			const value = memberIdInput.value.trim();

			if (value === "") {
				showError(memberIdInput, memberIdError, "会員番号を入力してください");
				return false;
			}
			if (!/^[a-zA-Z0-9\-]+$/.test(value)) {
				showError(memberIdInput, memberIdError, "会員番号は半角英数字またはハイフンで入力してください");
				return false;
			}

			clearError(memberIdInput, memberIdError);
			return true;
		}

		// パスワードのチェック基準
		function checkPassword() {
			const value = passwordInput.value.trim();

			if (value === "") {
				showError(passwordInput, passwordError, "パスワードを入力してください");
				return false;
			}
			if (value.length < 4) {
				showError(passwordInput, passwordError, "パスワードは4文字以上で入力してください");
				return false;
			}
			if (!/^[a-zA-Z0-9]+$/.test(value)) {
				showError(passwordInput, passwordError, "パスワードは半角英数字のみで入力してください");
				return false;
			}
			clearError(passwordInput, passwordError);
			return true;
		}

		// エラーを表示する関数
		function showError(inputElement, errorElement, message) {
			inputElement.classList.add('is-invalid');
			errorElement.textContent = message;
		}

		// エラーを消去する関数
		function clearError(inputElement, errorElement) {
			inputElement.classList.remove('is-invalid');
			errorElement.textContent = "";
		}
	}
});