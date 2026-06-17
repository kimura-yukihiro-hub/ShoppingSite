/**
 * グラフ描画用スクリプト
 */
function renderChart(canvasId, labels, data) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    // Canvasが親のサイズを100%継承するようにする
    canvas.parentElement.style.height = "350px"; 

    new Chart(canvas.getContext('2d'), {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: '売上金額',
                data: data,
                backgroundColor: '#ff9900'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: { y: { beginAtZero: true } }
        }
    });
}

// 1. 描画を共通関数化
function initCharts() {
    if (typeof productData !== 'undefined') {
        renderChart('productChart', productData.names, productData.sales);
    }
    if (typeof partData !== 'undefined') {
        renderChart('categoryChart', partData.names, partData.sales);
    } else {
        console.warn('partData is undefined.');
    }
}

// 2. ページ読み込み時に実行
document.addEventListener('DOMContentLoaded', function() {
    // 初回描画
    setTimeout(initCharts, 500);

    // 3. タブ（label）がクリックされたら再描画を試みる（Canvasサイズ復帰用）
    const tabs = document.querySelectorAll('.tab-label');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            setTimeout(initCharts, 100); // 描画領域確保後の少しの待ち時間
        });
    });
});