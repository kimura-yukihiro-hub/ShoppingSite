package jp.co.aforce.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.DataMigrationTask;

@WebServlet("/migrate-data")
public class MigrationServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 移行タスクを実行
		DataMigrationTask task = new DataMigrationTask();
		task.migrateItemsToLots();

		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().println("データ移行が正常に完了しました。");
	}
}