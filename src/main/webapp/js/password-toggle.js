/**
 * ログイン画面:パスワードの表示・非表示(目玉アイコン)切り替えスクリプト
 */
document.addEventListener('DOMContentLoaded', function() {
	const passwordInput = document.getElementById('password');
	const toggleButton = document.getElementById('toggle-password');
	const eyeIcon = document.getElementById('ete-icon');

	//エラー防止: 画面に要素があるときだけ要素を動かす
	if (passwordInput && toggleButton && eyeIcon) {

		//目を開ける(表示)/目を閉じる(非表示)のSVGの中身
		const openEyePath = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>';
		const closedEyePath = '<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>';
		
		toggleButton.addEventListener('click', function () {
			if (passwordInput.type ==='password') {
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
});