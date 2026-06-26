/**
 * タブ表示機能
 */

function openTab(evt, tabName) {
	// すべてのタブコンテンツを非表示に
	const contents = document.getElementsByClassName("tab-content");
	for (let content of contents) {
		content.style.display = "none";
	}
	
	// すべてのタブボタンから active クラスを削除
	const btns = document.getElementsByClassName("tab-btn");
	for (let btn of btns) {
		btn.classList.remove("active");
	}
	
	// 指定されたタブを表示し、ボタンを active に
	document.getElementById(tabName).style.display = "block";
	evt.currentTarget.classList.add("active");
}