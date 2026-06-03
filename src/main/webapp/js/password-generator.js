/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	//パスワード自動生成ボタンをクリックしたときの処理
	const generateBtn = document.getElementById("btn-generate-pw");
	if (!generateBtn) return;

	generateBtn.addEventListener("click", function() {
		const passwordInput = document.getElementById("password");
		if (!passwordInput) return;

		//1. パスワードに使用する強力な文字のプール(半角英数字)
		const chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.#$%";
		const passwordLength = 12; // 12桁の強力なパスワードにします
		let generatedPassword = "";

		//ランダムに文字を12回選んでつなげる
		for (let i = 0; i < passwordLength; i++) {
			const randomNumber = Math.floor(Math.random() * chars.length);
			generatedPassword += chars.substring(randomNumber, randomNumber + 1);
		}

		//2. 生成したパスワードを入力ボックスに自動セット
		passwordInput.value = generatedPassword;

		//3. 文字タイプを「password」（非表示）のまま維持する
		passwordInput.type = "password";

		//4. 目のアイコンは「閉じ目」の形をキープ
		const eyeIcon = document.getElementById("eye-icon");
		if (eyeIcon) {
			eyeIcon.innerHTML = `
		               <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
		               <line x1="1" y1="1" x2="23" y2="23"></line>
		           `;
		}


		//5. user-validation.js のリアルタイムチェックを強制起動させて「エラー(必須入力です等)」を瞬時に消す
		passwordInput.dispatchEvent(new Event("input"));

		//ユーザーに生成されたことを通知
		alert("記号を含んだ強力なパスワードを自動生成しました。\n\n生成された文字:  " + generatedPassword + "\n\n⚠️ 画面上は「●●●」で隠されています。忘れないように今すぐメモしてください。");
	});
});