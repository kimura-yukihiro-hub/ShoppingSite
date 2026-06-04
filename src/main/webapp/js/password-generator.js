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

			// 各文字種から最低1つを確実に入れて強度を担保
			let password = "";
			password += charset.uppercase[Math.floor(Math.random() * charset.uppercase.length)];
			password += charset.lowercase[Math.floor(Math.random() * charset.lowercase.length)];
			password += charset.numbers[Math.floor(Math.random() * charset.numbers.length)];
			password += charset.symbols[Math.floor(Math.random() * charset.symbols.length)];

			// 残りの桁を全文字種を混ぜたプールからランダム生成
			const allChars = Object.values(charset).join("");
			for (let i = password.length; i < length; i++) {
				password += allChars[Math.floor(Math.random() * allChars.length)];
			}

			// 順番を予測不能にシャッフル
			const shuffledPassword = password.split("").sort(function() { return 0.5 - Math.random(); }).join("");

			// 新しいパスワード欄にセット
			passwordInput.value = shuffledPassword;

			// リアルタイムバリデーション（user-validation.js）を連動起動させてエラーを消去
			passwordInput.dispatchEvent(new Event("input"));

			alert("安全なパスワードを自動生成しました：\n" + shuffledPassword + "\n\n※現在のパスワードを入力して変更を確認してください。");
		});
	}
});
