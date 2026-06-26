<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>販売傾向分析 - 管理者専用</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="rank-a5 page-admin-sales-dashboard">

	<div class="list-container">

		<header class="list-header">
			<h1>
				販売傾向分析 <span>Marketing & Sales Dashboard</span>
			</h1>
			<div class="header-user-nav">
				<a href="${pageContext.request.contextPath}/AdminMenu.action"
					class="btn-nav">管理者メニューへ戻る</a>
			</div>
		</header>

		<section class="admin-sales-search-area">
			<form action="AdminSales.action" method="get"
				class="admin-sales-search-form">
				<label>分析期間：</label> <input type="date" name="startDate"
					value="${startDate}"> <span>～</span> <input type="date"
					name="endDate" value="${endDate}">

				<button type="submit">絞り込む</button>
				<a href="AdminSales.action">リセット</a>
			</form>
		</section>

		<div class="analytics-card-container">
			<div class="analytics-card">
				<p class="sales-total-text">
					<fmt:formatNumber value="${totalSales}" pattern="#,###" />
					円
				</p>
				<div class="comparison-result">
					<c:choose>
						<c:when test="${lastMonthSales > 0}">
							<span class="comparison-label">前月対比</span>
							<span
								class="comparison-value ${totalSales >= lastMonthSales ? 'up' : 'down'}">
								<fmt:formatNumber
									value="${(totalSales - lastMonthSales) / lastMonthSales * 100}"
									pattern="#.#" />% (${totalSales >= lastMonthSales ? '↑' : '↓'})
							</span>
						</c:when>
						<c:otherwise>
							<span class="comparison-label">前月実績なし</span>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>

		<div class="analytics-tabs-wrapper">
			<input type="radio" id="tab-products" name="analytics-tab" checked>
			<input type="radio" id="tab-categories" name="analytics-tab">
			<input type="radio" id="tab-members" name="analytics-tab">

			<div class="tab-button-container">
				<label for="tab-products" class="tab-label label-products">🥩
					売れ筋商品ランキング (Top 10)</label> <label for="tab-categories"
					class="tab-label label-categories">🏆 人気部位ヒットランキング (Top 5)</label>
				<label for="tab-members" class="tab-label">👥 会員分析</label>
			</div>

			<div class="tab-content-container">
				<div class="admin-table-container content-products">
					<div class="chart-box">
						<canvas id="productChart"></canvas>
					</div>
					<table class="admin-table">
						<thead>
							<tr>
								<th>順位</th>
								<th>商品名</th>
								<th>総購入数</th>
								<th>合計売上高</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="pStat" items="${productStats}" varStatus="status">
								<tr>
									<td><span class="badge-status-sm status-aged-sm">第
											${status.count} 位</span></td>
									<td><strong> <c:set var="n"
												value="${fn:contains(pStat.productName, '（') ? fn:substringBefore(pStat.productName, '（') : (fn:contains(pStat.productName, '(') ? fn:substringBefore(pStat.productName, '(') : pStat.productName)}" />
											<c:out value="${n}" />
									</strong></td>
									<td><c:out value="${pStat.totalQuantity}" /> パック</td>
									<td><span class="text-highlight"> <fmt:formatNumber
												value="${pStat.totalSales}" pattern="#,###" /> 円
									</span></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div class="admin-table-container content-categories">
					<div class="chart-box">
						<canvas id="categoryChart"></canvas>
					</div>
					<table class="admin-table">
						<thead>
							<tr>
								<th>順位</th>
								<th>部位・カテゴリ</th>
								<th>総購入数</th>
								<th>合計売上高</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="cStat" items="${categoryStats}"
								varStatus="status">
								<tr>
									<td><span class="badge-status-sm status-aged-sm">第
											${status.count} 位</span></td>
									<td><strong><c:out value="${cStat.category}" /></strong></td>
									<td><c:out value="${cStat.totalQuantity}" />パック</td>
									<td><span class="text-highlight"><fmt:formatNumber
												value="${cStat.totalSales}" pattern="#,###" />円</span></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div class="admin-table-container content-members">
					<h3>👥 会員ランク別の購入者傾向分析</h3>
					<c:choose>
						<c:when test="${not empty rankStats}">
							<table class="admin-table">
								<thead>
									<tr>
										<th>会員ランク</th>
										<th>購入者数</th>
										<th>総購入パック数</th>
										<th>合計売上金額</th>
										<th>平均客単価</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="stat" items="${rankStats}">
										<tr>
											<td><strong> <c:choose>
														<c:when test="${stat.meatRank == 4}">VIP会員（ランク4）</c:when>
														<c:when test="${stat.meatRank == 3}">プラチナ会員（ランク3）</c:when>
														<c:when test="${stat.meatRank == 2}">ゴールド会員（ランク2）</c:when>
														<c:otherwise>一般会員（ランク1）</c:otherwise>
													</c:choose>
											</strong></td>
											<td><c:out value="${stat.uniqueUsers}" /> 名</td>
											<td><c:out value="${stat.totalQuantity}" /> パック</td>
											<td><span class="text-highlight"><fmt:formatNumber
														value="${stat.totalSales}" pattern="#,###" />円</span></td>
											<td><fmt:formatNumber
													value="${stat.uniqueUsers > 0 ? stat.totalSales / stat.uniqueUsers : 0}"
													pattern="#,###" />円</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:when>
						<c:otherwise>
							<div class="no-items-msg">
								<p>⚠️ 購入履歴がありません。</p>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>

		<script>
    // 商品ランキングデータ
    const productData = {
        names: [
            <c:forEach var="p" items="${productStats}" varStatus="status">
                <c:set var="cleanName" value="${fn:contains(p.productName, '(') ? fn:substringBefore(p.productName, '(') : p.productName}" />
                "${fn:replace(fn:replace(cleanName, "'", "\\'"), "\"", "\\\"")}"${!status.last ? ',' : ''}
            </c:forEach>
        ],
        sales: [
            <c:forEach var="p" items="${productStats}" varStatus="status">
                ${p.totalSales}${!status.last ? ',' : ''}
            </c:forEach>
        ]
    };
    
 // 人気部位ランキングデータ
    const partData = {
            names: [
                <c:forEach var="c" items="${categoryStats}" varStatus="status">
                    <c:set var="name" value="${fn:replace(fn:replace(c.category, \"'\", \"\\\\'\"), \"\\\"\", \"\\\\\\\"\")}" />
                    "${fn:trim(name)}"${!status.last ? ',' : ''}
                </c:forEach>
            ],
            sales: [
                <c:forEach var="c" items="${categoryStats}" varStatus="status">
                    ${c.totalSales}${!status.last ? ',' : ''}
                </c:forEach>
            ]
        };
console.log("読み込み成功:", productData, partData);
</script>


		<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
		<script
			src="${pageContext.request.contextPath}/js/analytics-charts.js" defer></script>
</body>
</html>