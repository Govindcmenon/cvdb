package ccv;
import java.sql.*;
import javax.swing.*;
import java.awt.*;

public class CVDisplay extends JFrame{
    JTextField uidfield = new JTextField(10);
    JTextArea display = new JTextArea(30,60);
    JTextField cgpaField = new JTextField(5);

    public CVDisplay(){
        setTitle("CV Viewer");
        JPanel top = new JPanel();
        top.add(new JLabel("Enter User ID:"));
        top.add(uidfield);
        top.add(new JLabel("New CGPA:"));
        top.add(cgpaField);
     
        JButton btn = new JButton("Show CV");
        btn.addActionListener(e->showcv());
        top.add(btn);
        JButton updateBtn = new JButton("Update CGPA");
updateBtn.addActionListener(e -> updateCGPA());
top.add(updateBtn);
        
        display.setEditable(false);
        add(top,BorderLayout.NORTH);
        add(new JScrollPane(display),BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    void showcv(){
        int uid = Integer.parseInt(uidfield.getText());
        try{
            Connection con = dataconnection.getConnection();
            String text = "";
            PreparedStatement ps = con.prepareStatement("Select * From user WHERE userid=?");
            ps.setInt(1,uid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                text+="USERDETAILS" + "\n";
                text += "NAME:" + rs.getString("firstname") + rs.getString("lastname") + "\n";
                text += "EMAIL:" + rs.getString("email") + "\n";
                text += "PHONE:" + rs.getString("phoneno")+"\n";
                text += "ADDRESS:" + rs.getString("address") + "\n";
            }

            ps = con.prepareStatement(
                    "SELECT * FROM qualification WHERE userid=?");
            ps.setInt(1, uid);
            rs = ps.executeQuery();

            while (rs.next()) {
                text += "QUALIFICATION\n";
                text += rs.getString("highestqualification") + " | ";
                text += rs.getString("university") + " | ";
                text += rs.getString("yearofgraduation") + " | ";
                text += rs.getString("cgpa") + "\n\n";
            }

            ps = con.prepareStatement(
                    "SELECT * FROM experience WHERE userid=?");
            ps.setInt(1, uid);
            rs = ps.executeQuery();

            while (rs.next()) {
                text += "EXPERIENCE\n";
                text += rs.getString("nameoforganization") + " | ";
                text += rs.getString("designation") + " | ";
                text += rs.getString("noofyears") + "\n\n";
            }

            // SKILLS
            ps = con.prepareStatement(
                    "SELECT * FROM skills WHERE userid=?");
            ps.setInt(1, uid);
            rs = ps.executeQuery();

            while (rs.next()) {
                text += "SKILLS\n";
                text += rs.getString("skills") + " | ";
                text += rs.getString("proficiency") + "\n\n";
            }

            // PROJECTS
            ps = con.prepareStatement(
                    "SELECT * FROM projects WHERE userid=?");
            ps.setInt(1, uid);
            rs = ps.executeQuery();

            while (rs.next()) {
                text += "PROJECTS\n";
                text += rs.getString("titles") + " ("
                        + rs.getString("noofprojects") + ")\n\n";
            }

            display.setText(text);
            con.close();
        }catch(Exception e){
               e.printStackTrace();
               display.setText("DATABASE ERROR :\n" + e.getMessage());
        }
    }
    void updateCGPA(){

    int uid = Integer.parseInt(uidfield.getText());
    double newCgpa = Double.parseDouble(cgpaField.getText());

    try {

        Connection con = dataconnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
            "UPDATE qualification SET cgpa=? WHERE userid=?"
        );

        ps.setDouble(1, newCgpa);
        ps.setInt(2, uid);

        int rows = ps.executeUpdate();

        if(rows > 0)
            display.setText("CGPA Updated Successfully");
        else
            display.setText("User not found");

        con.close();

    } catch(Exception e) {

        e.printStackTrace();
        display.setText("Update Error: " + e.getMessage());
    }
}
    
    public static void main(String[] args){
        new CVDisplay();
    }
}


