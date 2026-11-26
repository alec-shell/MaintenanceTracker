/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.RepairDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Repository
public class RepairDAO {

    // DB fields
    private static final String REP_ID = "REP_ID";
    private static final String VEH_ID = "REP_VEH_ID";
    private static final String MILEAGE = "REP_MLG";
    private static final String TITLE = "REP_TITLE";
    private static final String DATE = "REP_DT";
    private static final String LOCATION = "REP_LOC";
    private static final String COST = "REP_COST";
    private static final String NOTES = "REP_NOTES";

    // class/instance vars
    private static final Logger logger = LoggerFactory.getLogger(RepairDAO.class);
    private final DataSource dataSource;


    public RepairDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a new repair record to the database
     * @param repDto RepairDTO
     * @return SQL-generated REP_ID or -1 : long
     * @throws SQLException
     */
    public long insertRepRcd(RepairDTO repDto) throws SQLException {
        logger.debug("RepairDAO.insertRepRcd");

        String sqlQuery = """
                INSERT INTO REP_REPAIRS (
                REP_VEH_ID, REP_MLG, REP_TITLE, REP_DT, REP_LOC, REP_COST, REP_NOTES)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setLong(1, repDto.getVehId());
            prepStmt.setInt(2, repDto.getMileage());
            prepStmt.setString(3, repDto.getEventTitle());
            prepStmt.setDate(4, Date.valueOf(repDto.getDate()));
            prepStmt.setString(5, repDto.getLocation());
            prepStmt.setBigDecimal(6, repDto.getCost());
            prepStmt.setString(7, repDto.getNotes());

            prepStmt.executeUpdate();

            try (ResultSet rs = prepStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }

                return -1;
            }
        } catch (SQLException e) {
            logger.error("insertRepRcd SQL Exception for {}", repDto, e);
            throw e;
        }
    }

    /**
     * Get repair record from the database by repId
     * @param repId long
     * @return RepairDTO or NULL
     * @throws SQLException
     */
    public RepairDTO getRepRcdByRepId(long repId) throws SQLException {
        logger.debug("RepairDAO.getRepRcdByRepId: ID = {}",  repId);

        String sqlQuery = "SELECT * FROM REP_REPAIRS WHERE REP_ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setLong(1, repId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {

                    return buildRepairDTO(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            logger.error("getRepRcdByRepId: SQL Exception for ID {} ", repId, e);
            throw e;
        }
    }

    /**
     * Get an ArrayList of RepairDTO objects from the database by date
     * @param date String
     * @return ArrayList<RepairDTO>
     * @throws SQLException
     */
    public ArrayList<RepairDTO> getRepRcdByDate(String date) throws SQLException {
        logger.debug("RepairDAO.getRepRcdByDt: Date = {}",  date);

        String sqlQuery = "SELECT * FROM REP_REPAIRS WHERE REP_DT = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = prepStmt.executeQuery()) {
                ArrayList<RepairDTO> repairDTOs = new ArrayList<>();

                while (rs.next()) {

                    repairDTOs.add(buildRepairDTO(rs));
                }

                return repairDTOs;
            }
        } catch (SQLException e) {
            logger.error("getRepRcdByDt: SQL Exception for {} ", date, e);
            throw e;
        }
    }

    /**
     * Get an ArrayList of RepairDTO objects from the database by vehId
     * @param vehId long
     * @return ArrayList<RepairDTO>
     * @throws SQLException
     */
    public ArrayList<RepairDTO> getRepRcdsByVehId(long vehId) throws SQLException {
        logger.debug("RepairDAO.getRepRcdsByVehId: ID = {}",  vehId);

        String sqlQuery = "SELECT * FROM REP_REPAIRS WHERE REP_VEH_ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setLong(1, vehId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                ArrayList<RepairDTO> repairDTOs = new ArrayList<>();

                while (rs.next()) {
                    repairDTOs.add(buildRepairDTO(rs));
                }

                return repairDTOs;
            }
        } catch (SQLException e) {
            logger.error("getRepRcdsByVehId: SQL Exception for ID {} ", vehId, e);
            throw e;
        }
    }

    /**
     * Get an ArrayList of RepairDTO objects from the database by an ArrayList of vehicle IDs
     * @param vehIdList ArrayList<Long>
     * @return ArrayList<RepairDTO>
     * @throws SQLException
     */
    public ArrayList<RepairDTO> getRepRcdsByAllVehId(ArrayList<Long> vehIdList) throws SQLException {
        logger.debug("RepairDAO.getRepRcdsByAllVehId: IDs = {}",  vehIdList);

        ArrayList<RepairDTO> repairDTOs = new ArrayList<>();

        if (vehIdList == null || vehIdList.isEmpty()) {
            return repairDTOs;
        }

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM REP_REPAIRS WHERE REP_VEH_ID IN (");
        sqlQuery.append(RepoUtility.buildParamsPlaceholders(vehIdList.size()));
        sqlQuery.append(")");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery.toString())) {

            int paramCnt = 1;
            while (paramCnt <= vehIdList.size()) {
                prepStmt.setLong(paramCnt, vehIdList.get(paramCnt - 1));
                paramCnt++;
            }

            try (ResultSet rs = prepStmt.executeQuery()) {

                while (rs.next()) {
                    repairDTOs.add(buildRepairDTO(rs));
                }

                return repairDTOs;
            }
        } catch (SQLException e) {
            logger.error("getRepRcdsByAllVehId: SQL Exception for IDs {} ", vehIdList, e);
            throw e;
        }
    }

    /**
     * Helper method for building RepairDTO objects from ResultSets
     * @param rs ResultSet
     * @return RepairDTO
     * @throws SQLException
     */
    private RepairDTO buildRepairDTO(ResultSet rs) throws SQLException {
        logger.debug("RepairDAO.buildRepairDTO");

        RepairDTO repairDTO = new RepairDTO();
        repairDTO.setRepairId(rs.getLong(REP_ID));
        repairDTO.setVehId(rs.getLong(VEH_ID));
        repairDTO.setMileage(rs.getInt(MILEAGE));
        repairDTO.setEventTitle(rs.getString(TITLE));
        repairDTO.setDate(rs.getString(DATE));
        repairDTO.setLocation(rs.getString(LOCATION));
        repairDTO.setCost(rs.getBigDecimal(COST));
        repairDTO.setNotes(rs.getString(NOTES));

        return repairDTO;
    }

}
