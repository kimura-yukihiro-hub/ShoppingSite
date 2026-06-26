/**
 * 商品追加＆編集画面用：リアルタイム＆スマートバリデーション
 */
document.addEventListener("DOMContentLoaded", function() {
	const form = document.querySelector(".item-form-box");
	if (!form) return;

	const inputs = {
		itemName: document.getElementById("itemName"),
		kanaName: document.getElementById("kanaName"),
		price: document.getElementById("price"),
		stock: document.getElementById("stock"),
		category: document.getElementById("category")
	};

	const rules = {
		itemName: { regex: /^(?!\s| +$)(?!.*\s{2,}|.* {2,})[^\s ].*[^\s ]$/, msg: " スペースのみ・連続・前後のスペースは使用できません。" },
		kanaName: {
			regex: /^[\u30A1-\u30F6\u30FC]+$/, msg: " 全角カタカナ（長音「ー」含む）で入力してください。"},
		price: { regex: /^[0-9]+$/, msg: "半角数字で入力してください。" },
		stock: { regex: /^[0-9]+$/, msg: "半角数字で入力してください。" },
		category: { regex: /^[^\s\u3000]+$/, msg: "カテゴリにスペースは使用できません。" }
	};

	function showMsg(id, message) {
		const span = document.getElementById(id + "-error");
		if (span) span.textContent = message;
	}

	function clearMsg(id) {
		const span = document.getElementById(id + "-error");
		if (span) span.textContent = "";
	}

	// 必須チェックを含まない「純粋なルール判定」関数
	function validateField(id) {
		const el = inputs[id];
		const val = el.value;

		// 空の場合はエラーを出さない（blurで必須チェックするため）
		if (val === "") {
			clearMsg(id);
			return true;
		}

		// ルール判定
		if (!rules[id].regex.test(val)) {
			showMsg(id, "⚠️ " + rules[id].msg);
			return false;
		} else {
			clearMsg(id);
			return true;
		}
	}

	// イベント設定
	Object.keys(inputs).forEach(id => {
		const el = inputs[id];
		if (!el) return;

		// 1. 入力中：ルール違反チェックのみ
		el.addEventListener("input", () => {
			if (el.value === "") {
				clearMsg(id); // 空になったら即クリア
			} else {
				validateField(id); // ルールチェック
			}
		});

		// 2. フォーカスが外れた時：必須チェック＋ルール再チェック
		el.addEventListener("blur", () => {
			if (el.value.trim() === "") {
				showMsg(id, "⚠️ この項目は必須です。");
			} else {
				validateField(id);
			}
		});
	});

	// 送信時の最終チェック
	form.addEventListener("submit", function(e) {
		let isValid = true;
		Object.keys(inputs).forEach(id => {
			const val = inputs[id].value.trim();
			if (val === "") {
				showMsg(id, "⚠️ この項目は必須です。");
				isValid = false;
			} else if (!validateField(id)) {
				isValid = false;
			}
		});

		if (!isValid) {
			e.preventDefault();
			alert("正しく入力されていない項目があります。赤字のメッセージを確認してください。");
		}
	});
});

