/* マイページ用インクションスクリプト*/

document.addEventListener('DOMContentLoaded', () => {

	// 1. ランクゲージのアニメーション処理
	setTimeout(() => {
		const gauge = document.querySelector('.rank-circle .circle');
		if (gauge) {
			const progress = gauge.getAttribute('data-progress');

			// CSS変数にセットして、CSS側でそれを参照させる
			gauge.style.setProperty('--progress', `${progress}, 100`);
			gauge.classList.add('animate-gauge');
		}
	}, 300); // 0.3秒後

	// 2. ランクアップ通知の表示処理

	const checkToast = () => {
		const toast = document.getElementById('rank-up-toast');

		// 要素があり、かつ中身（alert-rank-up）があるかチェック
		if (toast && toast.querySelector('.alert-rank-up')) {
			toast.style.display = 'block';
			setTimeout(() => {
				toast.style.transition = 'opacity 0.5s';
				toast.style.opacity = '0';
				setTimeout(() => { toast.style.display = 'none'; }, 500);
			}, 4000);
		}
	};
	checkToast();
});

document.addEventListener('DOMContentLoaded', () => {
	if (rankUpMessage && rankUpMessage.trim() !== "") {
		console.log("メッセージ発見:", rankUpMessage);
		const toast = document.getElementById('rank-up-toast');
		if (toast) {
			toast.style.display = 'block';
			// 必要に応じて数秒後に隠す処理を入れる
		}
	}
});

