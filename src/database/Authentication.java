package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Authentication {

    public static boolean login(String username, String password){
        boolean login = false;
        String url;
        String user;
        String pass;
        int passwordHash = password.hashCode();
        try(FileInputStream input = new FileInputStream(new File("db.properties"))){
            Properties props = new Properties();
            props.load(input);
            user = (String) props.getProperty("username");
            pass = (String) props.getProperty("password");
            url = (String) props.getProperty("URL");

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {

        String loginQuery = "SELECT password_hash FROM User_Info WHERE username =?";

        PreparedStatement statement = connection.prepareStatement(loginQuery);
        statement.setString(1, username);

        ResultSet rs = statement.executeQuery();

        while(rs.next()){
            int actualHash = rs.getInt(1);
            if(actualHash == passwordHash){
                login = true;
            }
        }
        
        } catch (SQLException e) {
            System.out.println("Connection not successfull");
            return false;
        }
        }catch (IOException e){
            System.out.println("No properties found");
            return false;
        }
        return login;
    }
    public static boolean newAccount(String username, String password){

        String url;
        String user;
        String pass;
        int passwordHash = password.hashCode();
        boolean success = false;
        try(FileInputStream input = new FileInputStream(new File("db.properties"))){
            Properties props = new Properties();
            props.load(input);
            user = (String) props.getProperty("username");
            pass = (String) props.getProperty("password");
            url = (String) props.getProperty("URL");

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String newEntry = "INSERT INTO User_Info (username, password_hash) VALUES (?,?);";
            String newHistory = "INSERT INTO match_history (username, games_played, games_won) VALUES (?,0,0);";
            PreparedStatement statement = connection.prepareStatement(newEntry);
            statement.setString(1, username);
            statement.setInt(2, passwordHash);
            statement.executeUpdate();
            PreparedStatement statement1 = connection.prepareStatement(newHistory);
            statement1.setString(1,username);
            statement1.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.out.println("Username already exists");
            return false;
        }
        }catch (IOException e){
            System.out.println("No properties found");
            return false;
        }
        return success;
    }

}