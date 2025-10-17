package jp.ken.interiorshop.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.infrastructure.mapper.LoginMapper;
import jp.ken.interiorshop.infrastructure.mapper.MemberMapper;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;

@Repository
public class MemberRepository {

	private final JdbcTemplate jdbcTemplate;
	// DB の検索結果を MemberEntity に変換するためのマッパー
	private final RowMapper<MemberEntity> loginMapper = new LoginMapper();
	private final RowMapper<MemberEntity> memberMapper = new MemberMapper();

	public MemberRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 *  ログイン用：メールアドレスとパスワードで会員情報取得（該当なしの場合は null を返す）
	 */
	public MemberEntity getLoginData(String mail, String password) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("SELECT member_id, member_name ");
	    sb.append("FROM members ");
	    sb.append("WHERE mail = ? AND password = ? AND cancel = 0");	//cancel=0（退会していない会員）のみ対象
	    String sql = sb.toString();

	    try {
	    	// SQLを実行して1件の結果をMemberEntityに変換して返す
	        return jdbcTemplate.queryForObject(sql, loginMapper, mail, password);
	    } catch (org.springframework.dao.EmptyResultDataAccessException e) {
	    	// 検索結果が0件の場合（該当する会員がいない場合）は null を返す
	        return null;
	    }
	}

	/*
	 *  会員情報登録処理
	 */
	public int regist(MemberEntity memberEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO members (");
		sb.append("member_name, member_kana, mail, password, phone_number, ");
		sb.append("postal_code, address1, address2, address3, credit_no, cancel");
		sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		String sql = sb.toString();
		
		// SQL の ? に対応する値を配列にまとめる
		Object[] params = {
			memberEntity.getMemberName(),
			memberEntity.getMemberKana(),
			memberEntity.getMail(),
			memberEntity.getPassword(),
			memberEntity.getPhoneNumber(),
			memberEntity.getPostalCode(),
			memberEntity.getAddress1(),
			memberEntity.getAddress2(),
			memberEntity.getAddress3(),
			memberEntity.getCreditNo(),
			memberEntity.getCancel()
		};

		return jdbcTemplate.update(sql, params);	// SQL を実行し、登録件数を返す
	}

	// メールアドレスの重複チェック
	public boolean existsByMail(String mail) {
		String sql = "SELECT COUNT(*) FROM members WHERE mail = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mail);
		return count != null && count > 0;	// 1件以上あれば true
	}
	
	// 電話番号の重複チェック
	public boolean existsByPhoneNumber(String phoneNumber) {
		String sql = "SELECT COUNT(*) FROM members WHERE phone_number = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phoneNumber);
		return count != null && count > 0;	// 1件以上あれば true
	}

	// 会員IDから会員情報取得
	public MemberEntity findByMemberId(int memberId) {
		StringBuilder sb = createCommonSQL();
		sb.append(" WHERE member_id = ?");
		String sql = sb.toString();

		return jdbcTemplate.queryForObject(sql, memberMapper, memberId);
	}

	/*
	 *  メールアドレスから会員情報取得（該当なしの場合は null を返す）
	 */
	public MemberEntity findByMail(String mail) {
		StringBuilder sb = createCommonSQL();
		sb.append(" WHERE mail = ?");
		String sql = sb.toString();

		try {
			// SQL実行：該当する会員情報があれば MemberEntity に変換して返す
			return jdbcTemplate.queryForObject(sql, memberMapper, mail);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;		//該当なしの場合は null
		}
	}

	/*
	 *  電話番号から会員情報取得（該当なしの場合は null を返す）
	 */
	public MemberEntity findByPhoneNumber(String phoneNumber) {
		StringBuilder sb = createCommonSQL();
		sb.append(" WHERE phone_number = ?");
		String sql = sb.toString();

		try {
			// SQL実行：該当する会員情報が1件あれば MemberEntity にマッピングして返す
			return jdbcTemplate.queryForObject(sql, memberMapper, phoneNumber);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;		//該当なしの場合は null
		}
	}
	
	// 会員退会処理（cancelフラグを1に更新）
	public int memberWithdraw(MemberLoginForm form, MemberEntity memberEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE members SET cancel = 1 ");	// 退会フラグ更新SQL
		sb.append("WHERE member_id = ?");
		String sql = sb.toString();

		return jdbcTemplate.update(sql, form.getMemberId());
	}

	/*
	 *  会員情報更新処理
	 */
	public int memberUpdate(MemberEntity memberEntity) {
		StringBuilder sb = new StringBuilder();
		// SQL文を組み立て（更新するカラムを列挙）
		sb.append("UPDATE members SET ");
		sb.append("member_name = ?, ");
		sb.append("member_kana = ?, ");
		sb.append("mail = ?, ");
		sb.append("password = ?, ");
		sb.append("phone_number = ?, ");
		sb.append("postal_code = ?, ");
		sb.append("address1 = ?, ");
		sb.append("address2 = ?, ");
		sb.append("address3 = ?, ");
		sb.append("credit_no = ?, ");
		sb.append("cancel = ? ");
		sb.append("WHERE member_id = ?");
		String sql = sb.toString();

		// SQL の ? に対応するパラメータを配列に設定
		Object[] params = {
			memberEntity.getMemberName(),
			memberEntity.getMemberKana(),
			memberEntity.getMail(),
			memberEntity.getPassword(),
			memberEntity.getPhoneNumber(),
			normalize(memberEntity.getPostalCode()),	// null/空文字の変換
			memberEntity.getAddress1(),
			memberEntity.getAddress2(),
			memberEntity.getAddress3(),
			normalize(memberEntity.getCreditNo()),		// null/空文字の変換
			normalize(memberEntity.getCancel()),		// null/空文字の変換
			memberEntity.getMemberId()
		};

		return jdbcTemplate.update(sql, params);	// SQL を実行し、更新件数を返却
	}

	// 共通SELECT句生成（内部利用）
	private StringBuilder createCommonSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("member_id, member_name, member_kana, mail, password, ");
		sb.append("phone_number, postal_code, address1, address2, address3, ");
		sb.append("credit_no, cancel, point ");
		sb.append("FROM members");
		return sb;
	}

	/*
	 *  null・空文字・"null" を適切に変換
	 */
	private Object normalize(Object value) {
		if (value instanceof String str) {
			if (str.trim().isEmpty() || str.equalsIgnoreCase("null")) {
				return null;						// 空文字・"null"はnullに変換
			}
			try {
				return Integer.parseInt(str);		// 数字文字列なら Integer に変換
			} catch (NumberFormatException e) {
				return str;							// 数字でなければそのまま文字列
			}
		}
		return value;								// 文字列以外はそのまま返す
	}
}