package com.company.Database;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Scanner;

public class DatabaseHandler {
    private Connection conn;
    private Frame frame;
    public boolean loggedIn;
    public String username;
    private User user;
    public boolean updated;
    public DatabaseHandler(Frame frame, String host){
        try {
            this.frame = frame;
            this.loggedIn = false;
            this.user = null;
            this.updated = false;
            String myUrl = "jdbc:mysql://"+host+":3306/game";
            System.out.println("Connecting to database...");
            this.conn = DriverManager.getConnection(myUrl, "root", "");
            System.out.println("Connected...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createUser(){
        try {
            String username = JOptionPane.showInputDialog(frame, "Please enter your username: ");
            String password = null;
            if(checkUser(username)){
                JOptionPane.showMessageDialog(frame, "User with username: " + username + " already exists, try another username");
                createUser();
                return;
            }else{
                password = JOptionPane.showInputDialog(frame, "Enter your password: ");
            }
            if(password != null || password.length() != 0) {
                String query = "INSERT INTO users (username, password) values (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.execute();
                JOptionPane.showMessageDialog(frame, "Congrats, your account was created successfully!");
            }else{
                JOptionPane.showMessageDialog(frame, "The password you chose is invalid, try again...");
                createUser();
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Sorry unexpected error happened, please try again...");
            createUser();
            return;
        }
    }
    public boolean checkUser(String username){
        try{
            String query = "SELECT * FROM users WHERE username=?";
            PreparedStatement pStatement = conn.prepareStatement(query);
            pStatement.setString(1, username);

            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next()){
                String uname = resultSet.getString("username");
                String psw = resultSet.getString("password");
                int w = resultSet.getInt("wins");
                int l = resultSet.getInt("losses");
                int rtg = resultSet.getInt("rating");
                System.out.format("User: %s exists, Username: %s Password: %s Wins: %d Losses: %d Rating: %d\n",
                        uname, uname, psw, w, l, rtg);
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public User getUserData(String username, String password){
        try{
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pStatement = conn.prepareStatement(query);
            pStatement.setString(1, username);
            pStatement.setString(2, password);

            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next()){
                String uname = resultSet.getString("username");
                String psw = resultSet.getString("password");
                int w = resultSet.getInt("wins");
                int l = resultSet.getInt("losses");
                int rtg = resultSet.getInt("rating");
                System.out.format("Username: %s Password: %s Wins: %d Losses: %d Rating: %d\n",
                        uname, psw, w, l, rtg);
                this.user = new User(uname, psw, w, l, rtg);
                return this.user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return this.user;
    }
    public void login(){
        try{
            String username = JOptionPane.showInputDialog(frame, "Enter your username: ");
            if(checkUser(username)){
                String password = JOptionPane.showInputDialog(frame, "Enter your password: ");
                this.user = getUserData(username, password);
                if(this.user == null){
                    JOptionPane.showMessageDialog(frame, "There is no user with data you entered, please try again...");
                    login();
                    return;
                }
                this.loggedIn = true;
                System.out.format("Logged in as [Username] %s [Wins] %d [Losses] %d [Rating] %d\n", user.username, user.wins, user.losses, user.rating);
            }else{
                JOptionPane.showMessageDialog(frame, "There is no user with data you entered, please try again...");
                login();
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public User getUser(){
        this.user.password = null;
        return this.user;
    }
    public void updateStats(boolean result){
        try {
            String query = null;
            PreparedStatement preparedStatement = null;
            if(result){
                this.user.wins+=1;
                this.user.rating+=3;
                query = "UPDATE users SET wins=?, rating=? WHERE username=?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, user.wins);
                preparedStatement.setInt(2, user.rating);
                preparedStatement.setString(3, user.username);
            } else {
                this.user.losses+=1;
                this.user.rating-=2;
                query = "UPDATE users SET losses=?, rating=? WHERE username=?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, user.losses);
                preparedStatement.setInt(2, user.rating);
                preparedStatement.setString(3, user.username);
            }
            preparedStatement.execute();
            this.updated = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public class User {
        public String username;
        public String password;
        public int wins;
        public int losses;
        public int rating;
        public User(String username, String password, int wins, int losses, int rating){
            this.username = username;
            this.password = password;
            this.wins = wins;
            this.losses = losses;
            this.rating = rating;
        }
    }
}
