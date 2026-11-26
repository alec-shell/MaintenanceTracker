/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.MaintenanceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Repository
public class MaintenanceDAO {

    // DB fields
    private static final String MAINT_ID = "MAINT_ID";
    private static final String VEH_ID = "MAINT_VEH_ID";
    private static final String MILEAGE = "MAINT_MLG";
    private static final String MAINT_DATE = "MAINT_DT";
    private static final String LOCATION = "MAINT_LOC";
    private static final String TYPE = "MAINT_TYPE";
    private static final String COST = "MAINT_COST";
    private static final String NOTES = "MAINT_NOTES";

    // class/instance vars
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(MaintenanceDAO.class);

    public MaintenanceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a new maintenance record to the database
     * @param maintDto MaintenanceDTO
     * @return SQL-generated MAINT_ID or -1 : long
     * @throws SQLException
     */
    public long insertMaintRcd(MaintenanceDTO maintDto) throws SQLException {
        logger.debug("MaintenanceDAO.insertMaintRcd");

        String sqlQuery = """
                INSERT INTO MAINT_MAINTENANCE (
                MAINT_VEH_ID, MAINT_MLG, MAINT_DT, MAINT_LOC, MAINT_TYPE, MAINT_COST, MAINT_NOTES
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            prepStmt.setLong(1, maintDto.getVehId());
            prepStmt.setInt(2, maintDto.getMileage());
            prepStmt.setDate(3, Date.valueOf(maintDto.getDate()));
            prepStmt.setString(4, maintDto.getLocation());
            prepStmt.setString(5, maintDto.getType());
            prepStmt.setBigDecimal(6, maintDto.getCost());
            prepStmt.setString(7, maintDto.getNotes());

            prepStmt.executeUpdate();

            try (ResultSet rs = prepStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }

                return -1;
            }
        } catch (SQLException e) {
            logger.error("insertMaintDto: SQL Exception for {}", maintDto, e);
            throw e;
        }
    }

    /**
     * Returns a MaintenanceDTO from the database by maintId
     * @param maintId long
     * @return MaintenanceDTO or NULL
     * @throws SQLException
     */
    public MaintenanceDTO getMaintRcdByMaintId(long maintId) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByMaintId: ID = {}", maintId);

        String sqlQuery = "SELECT * FROM MAINT_MAINTENANCE WHERE " + MAINT_ID + " = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setLong(1, maintId);

            try (ResultSet rs = prepStmt.executeQuery()) {

                if (rs.next()) {

                    return buildMaintDTO(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            logger.error("getMaintRcdByMaintId: SQLException for ID {}", maintId, e);
            throw e;
        }
    }

    /**
     * Get ArrayList of MaintenanceDTO objects from the database by date
     * @param date String
     * @return ArrayList<MaintenanceDTO>
     * @throws SQLException
     */
    public ArrayList<MaintenanceDTO> getMaintRcdByDate(String date) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByDate: Date = {}", date);

        String sqlQuery = "SELECT * FROM MAINT_MAINTENANCE WHERE " + MAINT_DATE + " = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = prepStmt.executeQuery()) {
                ArrayList<MaintenanceDTO> maintDTOs = new ArrayList<>();

                while (rs.next()) {

                    maintDTOs.add(buildMaintDTO(rs));
                }

                return maintDTOs;
            }
        } catch (SQLException e) {
            logger.error("getMaintRcdByDate: SQLException for {}", date, e);
            throw e;
        }
    }

    /**
     * Get ArrayList of MaintenanceDTO objects from the database by vehId
     * @param vehId long
     * @return ArrayList<MaintenanceDTO>
     * @throws SQLException
     */
    public ArrayList<MaintenanceDTO> getMaintRcdsByVehId(long vehId) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByVehId: ID = {}", vehId);

        String sqlQuery = "SELECT * FROM MAINT_MAINTENANCE WHERE " + VEH_ID + " = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery)) {
            prepStmt.setLong(1, vehId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                ArrayList<MaintenanceDTO> maintDTOs = new ArrayList<>();

                while (rs.next()) {

                    maintDTOs.add(buildMaintDTO(rs));
                }

                return maintDTOs;
            }
        } catch (SQLException e) {
            logger.error("getMaintRcdByVehId: SQLException for ID {}", vehId, e);
            throw e;
        }
    }

    /**
     * Get ArrayList of MaintenanceDTO objects from the database by an ArrayList of vehIds
     * @param vehIdList ArrayList<Long>
     * @return ArrayList<MaintenanceDTO>
     * @throws SQLException
     */
    public ArrayList<MaintenanceDTO> getMaintRcdsByAllVehId(ArrayList<Long> vehIdList) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByAllVehId: IDs = {}", vehIdList);

        ArrayList<MaintenanceDTO> maintDTOs = new ArrayList<>();

        if (vehIdList == null || vehIdList.isEmpty()) {
            return maintDTOs;
        }

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM MAINT_MAINTENANCE WHERE "); sqlQuery.append(VEH_ID);
        sqlQuery.append(" IN (");
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
                    maintDTOs.add(buildMaintDTO(rs));
                }

                return  maintDTOs;
            }
        } catch (SQLException e) {
            logger.error("getMaintRcdsByAllVehId: SQLException for IDs {}", vehIdList, e);
            throw e;
        }
    }

    /**
     * Helper method for building MaintenanceDTO objects from ResultSet rows
     * @param rs ResultSet
     * @return MaintenanceDTO
     * @throws SQLException
     */
    private MaintenanceDTO buildMaintDTO(ResultSet rs) throws SQLException {
        logger.debug("MaintenanceDAO.buildMaintDTO");

        MaintenanceDTO maintDTO = new MaintenanceDTO();
        maintDTO.setMaintId(rs.getLong(MAINT_ID));
        maintDTO.setVehId(rs.getLong(VEH_ID));
        maintDTO.setMileage(rs.getInt(MILEAGE));
        maintDTO.setDate(rs.getString(MAINT_DATE));
        maintDTO.setLocation(rs.getString(LOCATION));
        maintDTO.setType(rs.getString(TYPE));
        maintDTO.setCost(rs.getBigDecimal(COST));
        maintDTO.setNotes(rs.getString(NOTES));

        return maintDTO;
    }

}
