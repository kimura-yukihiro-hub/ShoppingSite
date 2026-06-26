/**
 * 上に戻るボタン
 */
window.addEventListener('scroll', () => {
	const btn = document.getElementById("scrollToTopBtn");
	if (!btn) return;

	// 300pxを超えたら 'show' クラスを追加、それ以外は削除
	if (window.scrollY > 300) {
		btn.classList.add('show');
	} else {
		btn.classList.remove('show');
	}
});

document.addEventListener('DOMContentLoaded', () => {
	const btn = document.getElementById("scrollToTopBtn");
	if (btn) {
		btn.addEventListener('click', () => {
			window.scrollTo({
				top: 0,
				behavior: 'smooth'
			});
		});
	}
});