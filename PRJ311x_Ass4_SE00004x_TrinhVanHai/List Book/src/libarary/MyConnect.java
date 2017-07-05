package libarary;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class MyConnect {
	
	private final String className = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/books?useSSL=true";
	private final String user = JOptionPane.showInputDialog("Please enter your account to connect Database");
	private final String pass = JOptionPane.showInputDialog("Please enter your pass to connect Database");
	
	public String table = "my_book";

	private Connection connection; 
//---------------------------------------------------------------------------------------------------------	
	public int connect(){
		try {
			Class.forName(className);
			connection = DriverManager.getConnection(url,user,pass);
			return 1;
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found!");
			return 0;
		} catch (SQLException e) {
			System.out.println("Error connection!");
			return 0;
		}
	}
//-----------------------------------------------------------------------------------
	public void showBook(ResultSet rs){
		try {
			while(rs.next()){
				System.out.printf("%-5s %-10s %-20s %-10s %-10s \n", rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//--------------------------------------------------------------------------------------------------------
	public ResultSet getBook(){
		ResultSet rs = null;
		String sqlCommand = " select  *  from  " + table;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sqlCommand);
		} catch (SQLException e) {
			System.out.println("select error \n"+e.toString());
		}
		
		return rs;
	}
//-----------------------------------------------------------------------------------------
	public void deleteId(String id){
		String sqlCommand = " delete from  " + table + " where id=? ";
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(sqlCommand);
			pst.setString(1, id);
			if(pst.executeUpdate()>0){
				System.out.println("delete success");
			}else{
				System.out.println("delete error \n");
			};
		} catch (SQLException e) {
			System.out.println("delete error \n"+e.toString());
		}
	}
//------------------------------------------------------------------------------
	public void insert(Book b){
		String sqlCommand = " insert into  " + table + " value(?,?,?,?,?) ";
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(sqlCommand);
			pst.setInt(1, b.getId());
			pst.setString(2, b.getNameBook());
			pst.setString(3, b.getNameAuthor());
			pst.setString(4, b.getNamePublic());
			pst.setString(5, b.getType());

			if(pst.executeUpdate()>0){
				JOptionPane.showMessageDialog(null, "add success");
			}else{
				JOptionPane.showMessageDialog(null, "add error");
			};
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Id that you entered was existed");
		}
	}
//-------------------------------------------------------------------------------------------------	
	public void updateId(Book b) {
		String sqlCommand = "update " + table
				+ " set name_book = ?, name_author = ?, name_public = ?, type = ? where id = ?";
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(sqlCommand);
			pst.setString(1, b.getNameBook());
			pst.setString(2, b.getNameAuthor());
			pst.setString(3, b.getNamePublic());
			pst.setString(4, b.getType());
			pst.setInt(5, b.getId());
			if (pst.executeUpdate() > 0) {
				System.out.println("update success");
			} else {
				System.out.println("update error \n");
			}
		} catch (SQLException e) {
			System.out.println("update error \n" + e.toString());
		}
	}
//---------------------------------------------------------------	
	public ResultSet selectNameBook(String s) {
		ResultSet rs = null;
		String sqlCommand = " select * from my_book where name_book = ? " ;
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(sqlCommand);
			pst.setString(1, s);
			rs = pst.executeQuery();
		} catch (SQLException e) {
			System.out.println("search error \n"+e.toString());
		}
		
		return rs;
	}
//---------------------------------------------------------------------------------------------------------------------------	
	public ResultSet getBookId(String id){
			ResultSet rs = null;
			String sqlCommand = " select  *  from  " + table + " where id=? ";
			PreparedStatement pst = null;
			try {
				pst = connection.prepareStatement(sqlCommand);
				pst.setString(1, id);
				rs = pst.executeQuery(sqlCommand);
			} catch (SQLException e) {
				System.out.println("select error \n"+e.toString());
			}
			
			return rs;
	}
//-------------------------------------------------------------------------------------------------	

}	