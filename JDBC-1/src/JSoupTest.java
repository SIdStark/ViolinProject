import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.PreparedStatement;

public class JSoupTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/violindatabase", "root", "root");

			/*
			 * Creates a table names violin with following attributes
			 * +-------+--------+--------+--------+---------+ | vname | vbirth | vdeath |
			 * vmusic | vactive | +-------+--------+--------+--------+---------+ | | | | | |
			 * +-------+--------+--------+--------+---------+ | | | | | |
			 * +-------+--------+--------+--------+---------+ | | | | | |
			 * +-------+--------+--------+--------+---------+
			 * 
			 */
			//String v_table_create = "create table violin(vname varchar(100)" + ",vbirth varchar(10),"
			//		+ "vdeath varchar(10)," + "vmusic varchar(100)," + "vactive varchar(10)" + ")";
			//Statement stmt = con.createStatement();
			//stmt.execute(v_table_create);
			int numrowsUpdated = 0;

			try {
				//Get the page of violinist from wikipedia
				Document violin = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_Indian_violinists").get();
				// Gets the table of violinsts from the above page
				Elements violin_table = violin.select("table.wikitable tr");

				//Remove the header of the table
				violin_table.remove(0);

				//Creating prepared Statement to include the Values
				String v_insert = "insert into violin values(?,?,?,?,?)";

				try (PreparedStatement ps = (PreparedStatement) con.prepareStatement(v_insert)) {
					con.setAutoCommit(false);

					for (Element violinist : violin_table) {
						//Get all the rows of the violinsts table
						Elements name = violinist.getElementsByTag("td");
						Element violinist_name = name.first();
						Element violinist_birthdate = name.get(1);
						Element violinist_deathdate = name.get(2);
						Element violinist_musictype = name.get(3);
						Element violinist_active = name.get(4);

						//Process and get their String Representations
						String v_name = violinist_name.text();
						String v_birth = violinist_birthdate.text().equalsIgnoreCase("-") ? "Unknown"
								: violinist_birthdate.text();
						String v_death = violinist_deathdate.text().isEmpty() ? "Unknown" : violinist_deathdate.text();
						String v_music = violinist_musictype.text();
						String v_active = violinist_active.text();
						
						//Set Values using Prepares Statement
						ps.setString(1, v_name);
						ps.setString(2, v_birth);
						ps.setString(3, v_death);
						ps.setString(4, v_music);
						ps.setString(5, v_active);

						//Check how many rows are getting added
						numrowsUpdated += ps.executeUpdate();

						con.commit();

						//Printing out the table in the console
						System.out.printf("Violinist :%-55s\t", violinist_name.text());
						System.out.printf("Birth :%-7s\t", violinist_birthdate.text().equalsIgnoreCase("-") ? "Unknown"
								: violinist_birthdate.text());
						System.out.printf("Death :%-7s\t",
								violinist_deathdate.text().isEmpty() ? "Unknown" : violinist_deathdate.text());
						System.out.printf("Music :%-25s\t", violinist_musictype.text());
						System.out.printf("Active :%-15s\n",
								violinist_active.text().isEmpty() ? "Unknown" : violinist_active.text());

					}
					
					//Check how many rows are getting added
					System.out.println("Number of rows added:"+numrowsUpdated);
				}

			} catch (IOException e) {
				e.printStackTrace();

			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
