package ORG;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DB {
	private Connection cn = null;
	private PreparedStatement pstm = null;
	private List<PreparedStatement> stmts = null;
	
	public DataSet query(String query, String props) {
		cn = PoolManager.getConnection();
		try {
			this.pstm = this.cn.prepareStatement(new PropertiesMap().getValue(props, query));
			return new DataSet(this.pstm.executeQuery());
		} 
		
		catch (SQLException e) { e.printStackTrace(); return null; } 
		finally { this.free(); PoolManager.returnConnection(this.cn); }
	}
	
	public DataSet query(String query, String props, Object... params) {
		this.cn = PoolManager.getConnection();
		try {
			this.pstm = setParams(this.cn.prepareStatement(new PropertiesMap().getValue(props, query)), params);
			return new DataSet(this.pstm.executeQuery());
		} 
	
		catch (SQLException e) { e.printStackTrace(); return null; } 
		finally { free(); PoolManager.returnConnection(this.cn); }
	}
	
	public DataSet doInsert(String query, String props, Object ...params) {
		try {
			this.cn = PoolManager.getConnection();
			this.pstm = setParams(this.cn.prepareStatement(new PropertiesMap().getValue(props, query), PreparedStatement.RETURN_GENERATED_KEYS), params);
			this.pstm.executeUpdate();
			return new DataSet(this.pstm.getGeneratedKeys());
		} 
		
		catch (SQLException e) { e.printStackTrace(); return null; } 
		finally { this.free(); PoolManager.returnConnection(this.cn); }
	}
	
	public Integer update(String query, String props, Object ...params) {
		try {
			this.cn = PoolManager.getConnection();
			this.pstm = setParams(this.cn.prepareStatement(new PropertiesMap().getValue(props, query)), params);
			return new Integer(this.pstm.executeUpdate());
		} 
		
		catch (SQLException e) { e.printStackTrace(); return null; } 
		finally { this.free(); PoolManager.returnConnection(this.cn); }
	}
	
	public boolean transact(String[] queries, String props, Object[][] params) {
		int i = 0;
		this.stmts = new ArrayList<PreparedStatement>();
		try {
			this.cn = PoolManager.getConnection();
			this.cn.setAutoCommit(false);
			for (String q : queries) {
				PreparedStatement p = this.cn.prepareStatement(new PropertiesMap().getValue(props, q));
				this.pstm = setParams(p, params[i]);
				this.stmts.add(p);
				i++;
			}
			
			for (PreparedStatement pstm : this.stmts) { pstm.executeUpdate(); }
			this.cn.commit();
			return true;
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
			try { this.cn.rollback(); } 	
			catch (SQLException e1) { e1.printStackTrace(); }
			return false;
		} 
		
		finally {
			try { for (PreparedStatement pstm : this.stmts) { pstm.close(); } this.cn.setAutoCommit(true); } 
			catch (SQLException e) { e.printStackTrace(); }
			PoolManager.returnConnection(this.cn);
		}
	}
	
	public int[] task(String query, String props, Object[][] params) {
		try {
			this.cn = PoolManager.getConnection();
			this.cn.setAutoCommit(false);
			for (Object[] param : params) { this.pstm = this.setParams(this.cn.prepareStatement(new PropertiesMap().getValue(props, query)), param); this.pstm.addBatch(); }
			this.cn.commit();
			return this.pstm.executeBatch();
		} 
		
		catch (SQLException e) {
			try { this.cn.rollback(); } 
			catch (SQLException e1) { e1.printStackTrace(); }
			return null;
		} 
		
		finally { this.free(); PoolManager.returnConnection(this.cn); }
	}
	
	private PreparedStatement setParams(PreparedStatement p, Object[] params) throws SQLException {
		for (int i = 0; i < params.length; i++) { p.setObject(i + 1, params[i]); }
		return p;
	}	
	
	private void free() { try { pstm.close(); } catch (SQLException e) { e.printStackTrace(); } }
}