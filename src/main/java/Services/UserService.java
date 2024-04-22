package Services;

import Entities.User;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private Connection conn;

    public UserService()
    {
        conn = DataSource.getInstance().getCnx();
    }


    public ArrayList<User> readAll() {
        String requete = "SELECT * FROM user";
        ArrayList<User> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("email")

                );
                list.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
