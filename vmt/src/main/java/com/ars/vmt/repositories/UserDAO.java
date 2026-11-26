/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;


@Repository
public class UserDAO {

    // DB fields
    private static final String USER_ID = "USER_ID";
    private static final String FIRST_NM = "USER_FIRST_NM";
    private static final String LAST_NM = "USER_LAST_NM";
    private static final String EMAIL = "USER_EMAIL";
    private static final String JOIN_DT = "USER_JOIN_DT";

    // class/instance vars
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a new user to the database
     * @param user UserDTO
     * @param password String
     * @return SQL-generated USER_ID or -1 : long
     * @throws SQLException
     */
    public long insertUserRcd(UserDTO user, String password) throws SQLException {
        logger.debug("userDAO.insertUserRcd: User = {}, Password = {}", user, password);

        String sqlQuery = """
                INSERT INTO USER_USERS
                (USER_FIRST_NM, USER_LAST_NM, USER_EMAIL, USER_PASSWORD, USER_JOIN_DT)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepStmt.setString(1,user.getFirstName());
            prepStmt.setString(2, user.getLastName());
            prepStmt.setString(3, user.getEmail());
            prepStmt.setString(4, password);
            prepStmt.setDate(5, Date.valueOf(user.getJoinDt()));

            prepStmt.executeUpdate();

            try (ResultSet rs = prepStmt.getGeneratedKeys()) {
                if (rs.next()) {

                    return rs.getLong(1);
                }

                return -1;
            }
        } catch (SQLException e){
            logger.error("insertUserRcd SQLException for {} with password hash {}", user, password, e);
            throw e;
        }
    }

    /**
     * Get a user from the database by userId
     * @param userId long
     * @return UserDTO or NULL
     * @throws SQLException
     */
    public UserDTO getUserById(long userId) throws SQLException {
        logger.debug("UserDAO.getUserByID: ID = {}", userId);

        String sqlQuery = "SELECT * FROM USER_USERS WHERE " + USER_ID + " = ?";

        try (Connection conn =  dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setLong(1, userId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {
                    UserDTO userDTO = new UserDTO();

                    userDTO.setUserId(rs.getLong(USER_ID));
                    userDTO.setFirstName(rs.getString(FIRST_NM));
                    userDTO.setLastName(rs.getString(LAST_NM));
                    userDTO.setEmail(rs.getString(EMAIL));
                    userDTO.setJoinDt(rs.getString(JOIN_DT));

                    return userDTO;
                } else {

                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error("getUserByID: SQL Exception for ID {} \n",  userId, e);
            throw e;
        }
    }

}
