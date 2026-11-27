/**
 * 11/27/2026 Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.SessionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionDAO {

    //DB fields
    private static final String SES_ID = "SES_ID";
    private static final String USER_ID = "SES_USER_ID";
    private static final String ISSUE_TS = "SES_ISSUE_TS";
    private static final String LAST_ACT_TS = "SES_LAST_ACT_TS";
    private static final String EXP_TS = "SES_EXP_TS";

    // class/instance vars
    private static final Logger logger = LoggerFactory.getLogger(SessionDAO.class);
    private final DataSource dataSource;

    public SessionDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a new session to the database.
     * @param sesDto SessionDTO
     * @return number of rows affected: int
     * @throws SQLException
     */
    public int insertSession(SessionDTO sesDto) throws SQLException {
        logger.debug("SessionDAO.insertSession");

        String sqlQuery = """
                INSERT INTO SES_SESSION (
                SES_ID, SES_USER_ID, SES_ISSUE_TS, SES_LAST_ACT_TS, SES_EXP_TS)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setString(1, sesDto.getSessionId());
            prepStmt.setLong(2, sesDto.getUserId());
            prepStmt.setTimestamp(3, Timestamp.from(sesDto.getIssuedTs()));
            prepStmt.setTimestamp(4, Timestamp.from(sesDto.getLastActTs()));
            prepStmt.setTimestamp(5, Timestamp.from(sesDto.getExpTs()));

            return prepStmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("insertSession SQLException for {}", sesDto, e);
            throw e;
        }
    }

}
