package com.codegym.dao;

import com.codegym.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{

    public static final String URlJBDC = "jdbc:mysql://localhost:3306/demo";
    public static final String UserJDBC = "root";
    public static final String PASSWORDJDBC = "123456";
    public static final String CreatNewUsers = "INSERT INTO users(name ,email,country)values (?,?,?)";
    public static final String displayListUser = "SELECT *FROM users ";
    public static final String DELETE_FROM_USERS_WHERE_ID = "delete from users where id = ?;";

    protected Connection getConnection (){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URlJBDC, UserJDBC, PASSWORDJDBC);
            System.out.println("Ok rồi bạn ơiiiiiiiiiii");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chưa dược rồi bạn ơiiiiiiiiiiii");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    };
    
    @Override
    public void insertUser(User user) throws SQLException {
        try(Connection connection = getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(CreatNewUsers)) {
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User selectUser(int id) {
        return null;
    }

    @Override
    public  List<User> selectAllUsers() {
        List<User>users = new ArrayList<>();
        try(
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(displayListUser)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id,name,email,country));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean  rowDeleted;
        try (
                Connection connection = getConnection();
                PreparedStatement statement= connection.prepareStatement(DELETE_FROM_USERS_WHERE_ID))
        {
            statement.setInt(1,id);
            rowDeleted = statement.executeUpdate()>0;
        }

        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try(
                Connection connection=getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE user set name =? , email=? , country=? where id=?;")
        ){
            statement.setString(1,user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getCountry());
            statement.setInt(4,user.getId());
            rowUpdated = statement.executeUpdate()>0;
        }

        return rowUpdated;
    }
}
