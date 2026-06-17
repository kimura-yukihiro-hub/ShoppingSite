package jp.co.aforce.servlet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.CategoryStat;
import jp.co.aforce.beans.ProductStat;
import jp.co.aforce.beans.PurchaseStat;
import jp.co.aforce.beans.TypeStat;
import jp.co.aforce.dao.SalesDAO;
import jp.co.aforce.tool.Action;

public class AdminSalesAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			SalesDAO salesDao = new SalesDAO();

			// 1. 画面から期間パラメータを取得
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");

			// デフォルト期間の設定（今月1日～今日）
			if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
				LocalDate now = LocalDate.now();
				startDate = now.withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);
				endDate = now.format(DateTimeFormatter.ISO_DATE);
			}

			// 2. SQL検索用の時刻付き文字列を作成
			String queryStartDate = startDate + " 00:00:00";
			String queryEndDate = endDate + " 23:59:59";

			// 3. DAOからデータを取得
			int totalSales = salesDao.getTotalSalesAmount(queryStartDate, queryEndDate);
			List<PurchaseStat> rankStats = salesDao.getPurchaseStatsByRank(queryStartDate, queryEndDate);
			List<TypeStat> typeStats = salesDao.getSalesStatsByType(queryStartDate, queryEndDate);
			List<CategoryStat> categoryStats = salesDao.getSalesStatsByCategory(queryStartDate, queryEndDate);
			List<ProductStat> productStats = salesDao.getSalesStatsByProduct(queryStartDate, queryEndDate);

			// 4. 前月比較用データの計算
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);
			long daysBetween = ChronoUnit.DAYS.between(start, end);

			// 先月分の期間を算出
			String lastStartDate = start.minusDays(daysBetween + 1).toString() + " 00:00:00";
			String lastEndDate = end.minusDays(daysBetween + 1).toString() + " 23:59:59";
			int lastMonthSales = salesDao.getTotalSalesAmount(lastStartDate, lastEndDate);

			// 5. リクエスト属性へ格納
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("totalSales", totalSales);
			request.setAttribute("lastMonthSales", lastMonthSales);
			request.setAttribute("rankStats", rankStats);
			request.setAttribute("typeStats", typeStats);
			request.setAttribute("categoryStats", categoryStats);
			request.setAttribute("productStats", productStats);
			System.out.println("DEBUG: categoryStats size = " + (categoryStats != null ? categoryStats.size() : "null"));

			return "admin-sales-dashbord.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "分析エラー");
			request.setAttribute("errorMessage", "売上データの集計中にエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");
			return "login-error.jsp";
		}
	}
}