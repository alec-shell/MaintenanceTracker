/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.AuthenticationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AuthenticationDAO {

    // DB fields
    private static final String USER_ID = "USER_ID";
    private static final String EMAIL = "USER_EMAIL";

    // class/instance vars
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationDAO.class);

    public AuthenticationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Validate user credentials on sign-in
     * @param email String
     * @param password String
     * @return AuthenticationDTO or NULL
     * @throws SQLException
     */
    public AuthenticationDTO getAuthenticatedUser(String email, String password) throws SQLException {
        logger.debug("AuthenticationDAO.getUserByPasswordHash: hash = {}",  password);

        String sqlQuery = "SELECT * FROM USER_USERS WHERE USER_EMAIL = ? AND USER_PASSWORD = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)){
            prepStmt.setString(1, email);
            prepStmt.setString(2, password);

            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {
                    AuthenticationDTO authDTO = new AuthenticationDTO();
                    authDTO.setUserId(rs.getInt(USER_ID));
                    authDTO.setEmail(rs.getString(EMAIL));

                    return authDTO;
                } else {

                    return null;
                }
            }
        }  catch (SQLException e) {
            logger.error("getAuthenticatedUser: SQL Exception for password hash {} \n", password, e);
            throw e;
        }
    }
}
