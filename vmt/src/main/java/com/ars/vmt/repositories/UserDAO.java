/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

    public UserDTO getUserById(int userId) throws SQLException {
        logger.debug("UserDAO.getUserByID: ID = {}", userId);

        try (Connection conn =  dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM USER_USERS WHERE USER_ID = ?")) {
            prepStmt.setInt(1, userId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(rs.getInt(USER_ID));
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
