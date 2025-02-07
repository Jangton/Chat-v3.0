package net;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class ChatMgr3 {
	
	private DBConnectionMgr pool;
	
	public ChatMgr3() {
		try {
			pool = DBConnectionMgr.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Login
	public boolean loginChk(String id, String pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "select id from tblRegister where id = ?  and pwd = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs = pstmt.executeQuery();
			flag = rs.next();
			/* if(rs.next()) { flag = true; }*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
	
	//Message Insert
	public void insertMgr(MessageBean3 bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			con = pool.getConnection();
			sql = "insert tblMessage values (null, ?, ?, ?, now())";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getFid());
			pstmt.setString(2, bean.getTid());
			pstmt.setString(3, bean.getMsg());
			pstmt.executeUpdate(); //DB 실행
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(con, pstmt);
		}
	}
	
	//Message List : 조건의 id가 보낸 쪽지, 받은 쪽지
	public Vector<MessageBean3> getMsgList(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		MessageBean3 bean = new MessageBean3();
		Vector<MessageBean3> vlist = new Vector<MessageBean3>();
		try {
			con = pool.getConnection();
			sql = "select * from tblMessage where fid = ? or tid = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery(); //DB 실행
			while(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setFid(rs.getString(2));
				bean.setTid(rs.getString(3));
				bean.setMsg(rs.getString(4));
				//db에 모든 타입은 String으로 리턴 가능
				bean.setMdate(rs.getString(5));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
}
