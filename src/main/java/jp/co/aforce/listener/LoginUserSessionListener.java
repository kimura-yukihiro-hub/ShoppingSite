package jp.co.aforce.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;

import jp.co.aforce.beans.User;

@WebListener
public class LoginUserSessionListener implements HttpSessionAttributeListener {
	// ログイン中の「会員ID」と「セッションオブジェクト」を紐づけて管理するマップ
	private static final Map<String, HttpSession> loginUsers = new ConcurrentHashMap<>();

	//指定された会員IDがすでに別のブラウザで稼働中かどうかを調べるメソッド
	public static boolean isLoggedIn(String memberId) {
		HttpSession session = loginUsers.get(memberId);
		if (session != null) {
			try {
				// セッションが有効か確認
				session.getLastAccessedTime();

				//セッションの中に正しくユーザー情報が残っている場合のみ「ログイン中」とする
				Object loginUser = session.getAttribute("loginUser");
				if (loginUser != null) {
					return true; //ログイン中
				}
			} catch (IllegalStateException e) {
				//すでに切れているセッションならマップから消去してログイン中でない」とする
				loginUsers.remove(memberId);
			}
		}
		return false;
	}

	public void attributeAdded(HttpSessionBindingEvent event) {
		// セッションに "loginUser" が保存されたとき
		if ("loginUser".equals(event.getName())) {
			User user = (User) event.getValue();
			if (user != null) {
				// ログイン成功時にマップに登録する
				loginUsers.put(user.getMemberId(), event.getSession());

			}
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		// ログアウトなどでセッションから "loginUser" が削除されたらマップから消去
		if ("loginUser".equals(event.getName())) {
			User user = (User) event.getValue();
			if (user != null) {
				loginUsers.remove(user.getMemberId());
			}
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		// 属性が上書きされた場合も同様に処理
		if ("loginUser".equals(event.getName())) {
			attributeAdded(event);
		}
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();

		// サーバーのメモリからセッションが完全消滅したとき、マップ内からも連動して削除し、重複ログインロックを解放する
		loginUsers.values().removeIf(savedSession -> {
			try {
				return savedSession.getId().equals(session.getId());
			} catch (IllegalStateException e) {
				return true;
			}
		});
	}

	public void sessionCreated(HttpSessionEvent se) {
		// 新規セッション作成時はなにもしない
	}
}