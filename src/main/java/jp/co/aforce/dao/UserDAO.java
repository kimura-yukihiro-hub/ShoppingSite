package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.co.aforce.beans.User;

public class UserDAO extends DAO {

	// 購入合計金額を取得するメソッド
	public long getTotalPurchaseAmount(String memberId) throws Exception {
		long total = 0;
		String sql = "select sum(price * quantity) from purchases where member_id = ?";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, memberId);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					total = rs.getLong(1);
				}
			}
		}
		return total;
	}

	public User search(String memberId, String password) throws Exception {
		User user = null;

		//実行するSQL文
		String sql = "select member_id, password, last_name, first_name, address, mail_address, meat_rank "
				+ "from users where member_id = ? and password = ?";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			//プレースホルダーにJSPから届いた値をセットする
			st.setString(1, memberId);
			st.setString(2, password);

			//SQLを実行して結果を読み込む
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					user = new User();
					user.setMemberId(rs.getString("MEMBER_ID"));
					user.setPassword(rs.getString("PASSWORD"));
					user.setLastName(rs.getString("LAST_NAME"));
					user.setFirstName(rs.getString("FIRST_NAME"));
					user.setAddress(rs.getString("ADDRESS"));
					user.setMailAddress(rs.getString("MAIL_ADDRESS"));
					user.setMeatRank(rs.getInt("MEAT_RANK"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return user;
	}

	//会員情報を新しくデータベースに登録するメソッド
	//登録された行数　(成功時は1、失敗時は0)
	public int insert(User user) throws Exception {
		//1. 実行するINSERTのSQL文
		String sql = "insert into users (member_id, password, last_name, first_name, address, mail_address, meat_rank) values (?, ?, ?, ?, ?, ?, ?)";

		//成功した登録件数を数えるための変数
		int count = 0;

		//2. データベース接続とPreparedStatementの自動クローズを準備
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			//3. UserBeanから値を取り出し、プレースホルダー（?）に順番にセット
			st.setString(1, user.getMemberId());
			st.setString(2, user.getPassword());
			st.setString(3, user.getLastName());
			st.setString(4, user.getFirstName());
			st.setString(5, user.getAddress());
			st.setString(6, user.getMailAddress());
			st.setInt(7, user.getMeatRank());

			//4. SQLを実行して、登録が成功した行数を取得(更新系は executeUpdateを使用)
			count = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		//5. 登録結果の行数を返す
		return count;

	}

	//会員情報をデータベースで更新するメソッド
	//更新された行数(成功時は1、失敗時は0)
	public int update(User user) throws Exception {
		//1. 実行するUPDATEのSQL文
		// member_id を条件にして、それ以外の項目を最新の状態に書き換える
		String sql = "update users set password = ?, last_name = ?, first_name = ?, address = ?, mail_address = ? where member_id = ?";

		//成功した更新件数を数えるための変数
		int count = 0;

		// 2. データベース接続とPreparedStatementの自動クローズを準備
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			//UserBeanから値を取り出し、SQL文の「？」に順番をセット
			st.setString(1, user.getPassword());
			st.setString(2, user.getLastName());
			st.setString(3, user.getFirstName());
			st.setString(4, user.getAddress());
			st.setString(5, user.getMailAddress());
			st.setString(6, user.getMemberId());

			//4. SQL文を実行して、更新が成功した行数を取得(更新系なのでexecuteUpdateを使用)
			count = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		//4. 更新結果の行数を返す
		return count;
	}

	//会員情報をデータベースから削除(退会)するメソッド
	//削除された行数（成功時は1、失敗時は0）
	public int delete(String memberId) throws Exception {
		//1. 実行するDELETEのSQL文
		// 安全のため、必ず where 句で特定の member_id を指定します
		String sql = "delete from users where member_id = ?";

		// 成功した削除件数を数えるための変数
		int count = 0;

		// 2. データベース接続とPreparedStatementの自動クローズを準備
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			// 3. 引数で受け取った memberId をSQL文の「?」にセット
			st.setString(1, memberId);

			// 4. SQLを実行して、削除が成功した行数を取得(更新系なのでexecuteUpdateを使用)
			count = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		// 5. 削除結果の行数を返す
		return count;
	}

	//会員番号(ID)がすでにデータベースに登録されているかチェックするメソッド
	//すでに存在する場合は true、存在しない場合は false
	public boolean checkDuplicate(String memberId) throws Exception {
		//1. 実行するSQL文(パスワードは含めず、member_id だけを条件にする)
		String sql = "select count(*) from users where member_id = ?";

		//重複フラグ(初期値は false = 重複なし)
		boolean isDuplicate = false;

		// 2. データベース接続とPreparedStatementを準備
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			// 3. プレースホルダー（?）に会員番号をセット
			st.setString(1, memberId);

			// 4. SQLを実行して結果を読み込む
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					//件数(count(*))が1以上なら、すでにそのIDは使われている
					if (rs.getInt(1) > 0) {
						isDuplicate = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		// 5. 判定結果を返す
		return isDuplicate;

	}

	// 会員番号(ID)を元に1人の会員情報を取得するメソッド
	public User getUserById(String memberId) throws Exception {
		User user = null;
		String sql = "select * from users where member_id = ?";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, memberId);

			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					user = new User();
					user.setMemberId(rs.getString("MEMBER_ID"));
					user.setPassword(rs.getString("PASSWORD"));
					user.setLastName(rs.getString("LAST_NAME"));
					user.setFirstName(rs.getString("FIRST_NAME"));
					user.setAddress(rs.getString("ADDRESS"));
					user.setMailAddress(rs.getString("MAIL_ADDRESS"));
					user.setMeatRank(rs.getInt("MEAT_RANK"));
					user.setLastPurchaseDate(rs.getTimestamp("LAST_PURCHASE_DATE"));
				}
			}
		}
		return user;
	}

	// 全会員情報をデータベースから取得するメソッド
	public List<User> searchAll() throws Exception {
		//リストを初期化
		List<User> userList = new ArrayList<>();

		//全聚徳するSQL
		String sql = "select * from users";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {

			while (rs.next()) {
				User user = new User();
				user.setMemberId(rs.getString("MEMBER_ID"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setLastName(rs.getString("LAST_NAME"));
				user.setFirstName(rs.getString("FIRST_NAME"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setMailAddress(rs.getString("MAIL_ADDRESS"));
				user.setMeatRank(rs.getInt("MEAT_RANK"));

				// リストに追加
				userList.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userList;
	}

	// 管理者（meat_rank が 5）のみをデータベースから取得するメソッド
	public List<User> searchAllAdmins() throws Exception {
		List<User> adminList = new ArrayList<>();

		// ランクが5のユーザーだけを抽出するSQL
		String sql = "select * from users where meat_rank = 5";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {

			while (rs.next()) {
				User user = new User();
				user.setMemberId(rs.getString("MEMBER_ID"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setLastName(rs.getString("LAST_NAME"));
				user.setFirstName(rs.getString("FIRST_NAME"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setMailAddress(rs.getString("MAIL_ADDRESS"));
				user.setMeatRank(rs.getInt("MEAT_RANK"));
				adminList.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return adminList;
	}

	// 現在時刻で最終購入日を更新するメソッド
	public void updateLastPurchaseDate(String memberId) throws Exception {
		// 現在時刻(NOW)で最終購入日を更新するSQL
		String sql = "update users set last_purchase_date = now() where member_id = ?";
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, memberId);
			st.executeUpdate();
		}
	}

	//会員ランクを更新するメソッド
	public int updateRank(String memberId, int meatRank) throws Exception {

		// ランクのみを更新するSQL
		String sql = "update users set meat_rank = ? where member_id = ?";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, meatRank);
			st.setString(2, memberId);

			return st.executeUpdate();
		}
	}

	// パスワードを更新するメソッド
	public int updatePassword(int memberId, String password) throws Exception {
		// パスワードを更新するSQL
		String sql = "update users set password = ? where member_id = ?";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, password);
			st.setInt(2, memberId);

			return st.executeUpdate();
		}
	}
}
