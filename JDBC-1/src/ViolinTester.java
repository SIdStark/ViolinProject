import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

// This demo uses PreparedStatemt to insert values into the student database
import com.mysql.jdbc.PreparedStatement;

public class ViolinTester {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdatabase", "root", "root");
			
			//String sql = "create table student(sno int,sname varchar(20),saddress varchar(20))";
			String sql = "insert into student values(?,?,?)";
			
			try(PreparedStatement nameupdate = (PreparedStatement) con.prepareStatement(sql)) {
				
				con.setAutoCommit(false);
				
				Scanner sc = new Scanner(System.in);
				
				System.out.println("Enter the number of records you want to add");
				
				int numberrecords = sc.nextInt();
				int numrowsUpdated = 0;
				
				while(numberrecords-- > 0) {
					System.out.println("Enter ID:");
					int id = sc.nextInt();
					
					System.out.println("Enter the name:");
					String name = sc.next();
					
					System.out.println("Enter the address:");
					String address = sc.next();
					
					nameupdate.setInt(1,id);
					nameupdate.setString(2,name);
					nameupdate.setString(3,address);
					
					numrowsUpdated += nameupdate.executeUpdate();
					
					con.commit();
					
					
				}
				System.out.println("Number of rows updates is "+numrowsUpdated);
				sc.close();
				
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			
			
			
			
			
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