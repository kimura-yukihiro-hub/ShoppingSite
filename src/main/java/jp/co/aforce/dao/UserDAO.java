package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.co.aforce.beans.User;

public class UserDAO extends DAO{
	public User search(String memberId, String password) throws Exception {
		User user = null;
		
		//実行するSQL文
		String sql = "select * from users where member_id = ? and password = ?";
		
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
	

}
