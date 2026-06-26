<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ロット詳細情報</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<div class="lot-detail-container">
		<h3>ロット詳細情報</h3>
		<p>入荷ロットNo: ${param.batchId} のシリアルNo.です。</p>
		<div class="lot-detail-scroll">
			<table class="lot-detail-table">
				<thead>
					<tr>
						<th>シリアルNo.</th>
						<th>状態</th>
						<th>熟成開始日</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="lot" items="${lotDetails}">
						<tr>
							<td>${lot.serialNumber}</td>
							<td><span
								class="status-badge ${lot.meatStatus == 1 ? 'aged' : 'fresh'}">
									${lot.meatStatus == 1 ? '熟成中' : '生'} </span></td>
							<td>${not empty lot.agingStartDate ? lot.agingStartDate.toLocalDate() : '-'}
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>