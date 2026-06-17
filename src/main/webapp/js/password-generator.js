/**
 * 会員情報変更画面：パスワード自動生成ロジック（制限記号準拠）
 */
document.addEventListener("DOMContentLoaded", function() {
	const generateBtn = document.getElementById("btn-generate-pw");
	const passwordInput = document.getElementById("password");

	if (generateBtn && passwordInput) {
		generateBtn.addEventListener("click", function(e) {
			e.preventDefault();

			const length = 12; // 扱いやすい12桁で生成
			const charset = {
				uppercase: "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
				lowercase: "abcdefghijklmnopqrstuvwxyz",
				numbers: "0123456789",
				symbols: "-_#$%" // 使用可能記号だけに厳格に制限
			};

			// 暗号学的に安全な乱数を取得するインライン関数
			const getSecureRandomInt = (max) => {
				const array = new Uint32Array(1);
				window.crypto.getRandomValues(array);
				return array[0] % max;
			};

			// 各文字種から最低1つを確実に入れて強度を担保
			let password = "";
			password += charset.uppercase[getSecureRandomInt(charset.uppercase.length)];
			password += charset.lowercase[getSecureRandomInt(charset.lowercase.length)];
			password += charset.numbers[getSecureRandomInt(charset.numbers.length)];
			password += charset.symbols[getSecureRandomInt(charset.symbols.length)];

			// 残りの桁を全文字種を混ぜたプールからランダム生成
			const allChars = Object.values(charset).join("");
			for (let i = password.length; i < length; i++) {
				password += allChars[getSecureRandomInt(allChars.length)];
			}

			// 完全シャッフル
			const arr = password.split("");
			for (let i = arr.length - 1; i > 0; i--) {
				const j = getSecureRandomInt(i + 1);
				const temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
			const securePassword = arr.join("");

			// 新しいパスワード欄にセット
			passwordInput.value = securePassword;

			// リアルタイムバリデーション（user-validation.js）を連動起動させてエラーを消去
			passwordInput.dispatchEvent(new Event("input"));

			// アラートのメッセージに、生成された最新のセキュアパスワードを表示
			alert("安全なパスワードを自動生成しました：\n" + securePassword + "\n\n※現在のパスワードを入力して変更を確認してください。");
		});
	}
});
