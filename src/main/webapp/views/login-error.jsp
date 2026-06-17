<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログインエラー</title>
<link rel="stylesheet" href="/ShoppingSite/css/style.css">
</head>
<body class="error-page">

	<div class="error-container">
		<div class="error-icon">⚠️</div>
		<h2>
			<c:out value="${not empty errorTitle ? errorTitle : 'ログインできませんでした'}" />
		</h2>

		<p>
			<c:out
				value="${not empty errorMessage ? errorMessage : 'エラーもしくはパスワードが違います'}" />
		</p>

		<c:choose>
			<c:when test="${not empty errorBackUrl}">
				<button type="button" class="btn-back"
					onclick="location.href='${pageContext.request.contextPath}/<c:out value="${errorBackUrl}" />'">
					<c:out value="${not empty errorBtnText ? errorBtnText : '戻る'}" />
				</button>
			</c:when>
			<c:otherwise>
				<button type="button" class="btn-back" onclick="history.back();">
					<c:out
						value="${not empty errorBtnText ? errorBtnText : 'ログイン画面へ戻る'}" />
				</button>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>