package Model;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Registration  {
	
	private Connection con;
	HttpSession se;
	public Registration(HttpSession session) {
	try {
	Class.forName("com.mysql.cj.jdbc.Driver"); // load the drivers
	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pentagondb", "root", "tiger");
	// connection with data base
	se = session; //local session assigned to global session
	} catch (Exception e) {
	e.printStackTrace();
	}
	}
	public String Registration(String name, String phone, String email, String pw) {
	PreparedStatement ps;
	String status = "";
	try {
	Statement st = null;
	ResultSet rs = null;
	st = con.createStatement();
	//Before storing into database we are checking whether this record present or not based on phone or email
	rs = st.executeQuery("select * from sookshmas1 where phone='" + phone + "' or email='" + email + "';");
	boolean b = rs.next();
	//if data present then b variable will hold true and if not then false
	if (b) {
	status = "existed";
	} else {
		
	ps = (PreparedStatement) con.prepareStatement("insert into sookshmas1 values(0,?,?,?,?,now())");
	ps.setString(1, name);
	ps.setString(2, phone);
	ps.setString(3, email);
	ps.setString(4, pw);
	int a = ps.executeUpdate();
	if (a > 0) {
	status = "success";
	} else {
	status = "failure";
	}
	}
	} catch (Exception e) {
	e.printStackTrace();
	}
	return status;
	
		
	}
	
	
public String login(String email, String pass) {
String status1 = "";
String id;
String name = "", emails = "";
try {
Statement st = null;
ResultSet rs = null;
st = con.createStatement();
rs = st.executeQuery("select * from sookshmas1 where email='" + email + "' and password='" + pass + "';");
boolean b = rs.next();
if (b == true) {
id = rs.getString("slno");
name = rs.getString("name");
emails = rs.getString("email");

//session //
se.setAttribute("uname", name);
se.setAttribute("email", emails);
se.setAttribute("id", id);
status1 = "success";
} else {
status1 = "failure";
}
} catch (Exception e) {
e.printStackTrace();
}
return status1;
}


public student getInfo() {
Statement st = null;
ResultSet rs = null;
student s = null;
try {
st = con.createStatement();
rs = st.executeQuery("select * from sookshmas1 where slno= '" + se.getAttribute("id") + "'");
boolean b = rs.next();
if (b == true) {
s = new student();
s.setname(rs.getString("name"));
s.setphone(rs.getString("phone"));
s.setemail(rs.getString("email"));
} else {
s = null;
}
} catch (Exception e) {
e.printStackTrace();
}
return s;
}


public String update(String name, String pno, String email) {
String status = "";
Statement st = null;
ResultSet rs = null;
try {
st = con.createStatement();
st.executeUpdate("update sookshmas1 set name='" + name + "',phone='" + pno + "',email='" + email + "' where slno= '" + se.getAttribute("id") + "' ");
se.setAttribute("uname", name);
status = "success";
} catch (Exception e) {
status = "failure";
e.printStackTrace();
}
return status;
}
	


public ArrayList<student> getUserinfo(String id) {
Statement st = null;
ResultSet rs = null;
ArrayList<student> al = new ArrayList<student>();
try {
st = con.createStatement();
String qry = "select * from sookshmas1 where slno = " + id + ";";
rs = st.executeQuery(qry);
boolean b=rs.next();
//while (rs.next()) {
////System.out.println("inside the while loop");
//student p = new student();
//p.setid(rs.getString("slno"));
//p.setname(rs.getString("name"));
//p.setemail(rs.getString("email"));
//p.setphone(rs.getString("phone"));
//p.setDate(rs.getString("date"));
//al.add(p);
//
//}

if(b) {
	student p = new student();
	p.setid(rs.getString("slno"));
	p.setname(rs.getString("name"));
	p.setemail(rs.getString("email"));
	p.setphone(rs.getString("phone"));
	p.setDate(rs.getString("date"));
	al.add(p);
}
} catch (Exception e) {
e.printStackTrace();
}

return al;
}



public String delete(int id) {
Statement ps=null;
int a;
String status="";
try {
ps=con.createStatement();
String sql="DELETE FROM sookshmas1 WHERE slno='"+id+"';";
a=ps.executeUpdate(sql);
if(a>0) {
status="sucess";
}
else {
status="failure";
}
}catch(Exception e) {
e.printStackTrace();
}
return status;
}
public ArrayList<student> getUserDetails() {
Statement st=null;
ResultSet rs=null;
ArrayList<student> al = new ArrayList<student>();
try {
st = con.createStatement();
String qry = "select * from sookshmas1 where slno not in(1);";
rs = st.executeQuery(qry);
while (rs.next()) {
student p = new student();
p.setid(rs.getString("slno"));
p.setname(rs.getString("name"));
p.setemail(rs.getString("email"));
p.setphone(rs.getString("phone"));
p.setDate(rs.getString("date"));
al.add(p);
}
} catch (Exception e) {
e.printStackTrace();
}
return al;
}
public boolean updatePassword(String email, String password) {
	boolean check = false;
	Statement st = null;
	try {
		st = con.createStatement();
	int a = st.executeUpdate("update sookshmas1 set password='" + password + "'where email='" + email + "' ");
	if (a > 0) {
		check = true;
	} else {
		System.out.println("Update password failed ");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

		return check;
	}


}	
	
	
	
	


