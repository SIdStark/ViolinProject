import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UiTester {
	public static void main(String[] args) {
		try {
//1.registering the driver class
			Class.forName("com.mysql.jdbc.Driver");
//2.getting database connection
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdatabase", "root", "root");
//3.compose sql
			//String sql = "create table student(sno int,sname varchar(20),saddress varchar(20))";
			String sql = "update student set sname = 'Sid' where sname = 'Siddharth'";
//4.create statement
			Statement stmt = con.createStatement();
			stmt.execute(sql);
//close the connection
			System.out.println("Added a record");
			con.close();
		} catch (ClassNotFoundException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}