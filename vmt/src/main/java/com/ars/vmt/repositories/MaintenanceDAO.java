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

    private static final String MAINT_ID = "MAINT_ID";
    private static final String VEH_ID = "MAINT_VEH_ID";
    private static final String MILEAGE = "MAINT_MLG";
    private static final String MAINT_DATE = "MAINT_DT";
    private static final String LOCATION = "MAINT_LOC";
    private static final String TYPE = "MAINT_TYPE";
    private static final String COST = "MAINT_COST";
    private static final String NOTES = "MAINT_NOTES";

    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(MaintenanceDAO.class);

    public MaintenanceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MaintenanceDTO getMaintRcdByMaintId(int maintId) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByMaintId: ID = {}", maintId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM MAINT_MAINTENANCE WHERE MAINT_ID = ?")) {
            prepStmt.setInt(1, maintId);

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

    public ArrayList<MaintenanceDTO> getMaintRcdByDate(String date) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByDate: Date = {}", date);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM MAINT_MAINTENANCE WHERE MAINT_DATE = ?")) {
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

    public ArrayList<MaintenanceDTO> getMaintRcdsByVehId(int vehId) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByVehId: ID = {}", vehId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM MAINT_MAINTENANCE WHERE MAINT_VEH_ID = ?")) {
            prepStmt.setInt(1, vehId);

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

    public ArrayList<MaintenanceDTO> getMaintRcdsByAllVehId(ArrayList<Integer> vehIdList) throws SQLException {
        logger.debug("MaintenanceDAO.getMaintRcdByAllVehId: IDs = {}", vehIdList);

        ArrayList<MaintenanceDTO> maintDTOs = new ArrayList<>();

        if (vehIdList == null || vehIdList.isEmpty()) {
            return maintDTOs;
        }

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM MAINT_MAINTENANCE WHERE MAINT_VEH_ID IN (");
        sqlQuery.append(RepoUtility.buildParamsPlaceholders(vehIdList.size()));
        sqlQuery.append(")");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery.toString())) {

            int paramCnt = 1;
            while (paramCnt <= vehIdList.size()) {
                prepStmt.setInt(paramCnt, vehIdList.get(paramCnt - 1));
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

    private MaintenanceDTO buildMaintDTO(ResultSet rs) throws SQLException {
        MaintenanceDTO maintDTO = new MaintenanceDTO();
        maintDTO.setMaintId(rs.getInt(MAINT_ID));
        maintDTO.setVehId(rs.getInt(VEH_ID));
        maintDTO.setMileage(rs.getInt(MILEAGE));
        maintDTO.setDate(rs.getString(MAINT_DATE));
        maintDTO.setLocation(rs.getString(LOCATION));
        maintDTO.setType(rs.getString(TYPE));
        maintDTO.setCost(rs.getDouble(COST));
        maintDTO.setNotes(rs.getString(NOTES));

        return maintDTO;
    }

}
