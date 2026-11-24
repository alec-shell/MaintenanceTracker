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

    // Class/Instance vars
    private static final Logger logger = LoggerFactory.getLogger(RepairDAO.class);
    private final DataSource dataSource;


    public RepairDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public RepairDTO getRepRcdByRepId(int repId) throws SQLException {
        logger.debug("RepairDAO.getRepRcdByRepId: ID = {}",  repId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM REP_REPAIRS WHERE REP_ID = ?")) {
            prepStmt.setInt(1, repId);

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

    public ArrayList<RepairDTO> getRepRcdByDate(String date) throws SQLException {
        logger.debug("RepairDAO.getRepRcdByDt: Date = {}",  date);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM REP_REPAIRS WHERE REP_DT = ?")) {
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

    public ArrayList<RepairDTO> getRepRcdsByVehId(int vehId) throws SQLException {
        logger.debug("RepairDAO.getRepRcdsByVehId: ID = {}",  vehId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM REP_REPAIRS WHERE REP_VEH_ID = ?")) {
            prepStmt.setInt(1, vehId);

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

    public ArrayList<RepairDTO> getRepRcdsByAllVehId(ArrayList<Integer> vehIdList) throws SQLException {
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
                prepStmt.setInt(paramCnt, vehIdList.get(paramCnt - 1));
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

    private RepairDTO buildRepairDTO(ResultSet rs) throws SQLException {
        RepairDTO repairDTO = new RepairDTO();
        repairDTO.setRepairId(rs.getInt(REP_ID));
        repairDTO.setVehId(rs.getInt(VEH_ID));
        repairDTO.setMileage(rs.getInt(MILEAGE));
        repairDTO.setEventTitle(rs.getString(TITLE));
        repairDTO.setDate(rs.getString(DATE));
        repairDTO.setLocation(rs.getString(LOCATION));
        repairDTO.setCost(rs.getDouble(COST));
        repairDTO.setNotes(rs.getString(NOTES));

        return repairDTO;
    }

}
