/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.VehicleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Repository
public class VehicleDAO {

    // DB fields
    private static final String VEH_ID = "VEH_ID";
    private static final String USER_ID = "VEH_USER_ID";
    private static final String YEAR = "VEH_YEAR";
    private static final String VIN = "VEH_VIN";
    private static final String MAKE = "VEH_MAKE";
    private static final String MODEL = "VEH_MODEL";
    private static final String COLOR = "VEH_COLOR";
    private static final String PURCH_DT = "PURCHASE_DT";
    private static final String SOLD_DT = "SOLD_DT";

    // class/instance vars
    private static final Logger logger = LoggerFactory.getLogger(VehicleDAO.class);
    private final DataSource dataSource;

    public VehicleDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a new vehicle record into the database
     * @param vehDTO VehicleDTO
     * @return SQL-generated VEH_ID or -1 : long
     */
    public long insertVehRcd(VehicleDTO vehDTO) throws SQLException{
        logger.debug("VehicleDAO.insertVehRcd");

        String sqlQuery = """
                INSERT INTO VEH_VEHICLES
                (VEH_USER_ID, VEH_YEAR, VEH_VIN, VEH_MAKE, VEH_MODEL, VEH_COLOR, PURCHASE_DT, SOLD_DT)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepStmt.setLong(1, vehDTO.getUserId());
            prepStmt.setInt(2, vehDTO.getYear());
            prepStmt.setString(3, vehDTO.getVin());
            prepStmt.setString(4, vehDTO.getMake());
            prepStmt.setString(5, vehDTO.getModel());
            prepStmt.setString(6, vehDTO.getColor());
            prepStmt.setDate(7, Date.valueOf(vehDTO.getPurchaseDate()));
            prepStmt.setDate(8, Date.valueOf(vehDTO.getSoldDate()));

            prepStmt.executeUpdate();

            try (ResultSet rs = prepStmt.getGeneratedKeys()) {
                if (rs.next()) {

                    return rs.getLong(1);
                }

                return -1;
            }
        } catch (SQLException e) {
            logger.error("insertVehRcd: SQLException for {}", vehDTO, e);
            throw e;
        }
    }

    /**
     * Get a VehicleDTO from the database by vehId
     * @param vehId long
     * @return VehicleDTO or NULL
     * @throws SQLException
     */
    public VehicleDTO getVehicleByVehId(long vehId) throws SQLException {
        logger.debug("VehicleDAO.getVehicleByVehId: Id = {}",  vehId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM VEH_VEHICLES WHERE VEH_ID = ?")) {
            prepStmt.setLong(1, vehId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {

                    return buildVehicleDTO(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            logger.error("getVehicleByVehId: SQLException for ID {}", vehId, e);
            throw e;
        }
    }

    /**
     * Get a VehicleDTO from the database by vin
     * @param vin String
     * @return VehicleDTO or NULL
     * @throws SQLException
     */
    public VehicleDTO getVehicleByVin(String vin) throws SQLException {
        logger.debug("VehicleDAO.getVehicleByVin: VIN = {}",  vin);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM VEH_VEHICLES WHERE VEH_VIN = ?")) {
            prepStmt.setString(1, vin);

            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {

                    return buildVehicleDTO(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            logger.error("getVehicleByVin: SQLException for VIN {}", vin, e);
            throw e;
        }
    }

    /**
     * Get an ArrayList of VehicleDTO objects from the database by userId
     * @param userId long
     * @return ArrayList<VehicleDTO>
     * @throws SQLException
     */
    public ArrayList<VehicleDTO> getVehiclesByUserId(long userId) throws SQLException {
        logger.debug("VehicleDAO.getVehiclesByUserId: UserId = {}",  userId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM VEH_VEHICLES WHERE VEH_USER_ID = ?")) {
            prepStmt.setLong(1, userId);

            try (ResultSet rs = prepStmt.executeQuery()) {
                ArrayList<VehicleDTO> vehDTOs = new ArrayList<>();

                while (rs.next()) {
                    vehDTOs.add(buildVehicleDTO(rs));
                }

                return vehDTOs;
            }
        } catch (SQLException e) {
            logger.error("getVehiclesByUserId: SQLException for UserId {}",  userId, e);
            throw e;
        }
    }

    /**
     * Helper method for building VehicleDTO objects from a ResultSet
     * @param rs ResultSet
     * @return VehicleDTO
     * @throws SQLException
     */
    private VehicleDTO buildVehicleDTO(ResultSet rs) throws SQLException {
        logger.debug("VehicleDAO.buildVehicleDTO");

        VehicleDTO vehDTO = new VehicleDTO();
        vehDTO.setVehId(rs.getLong(VEH_ID));
        vehDTO.setUserId(rs.getLong(USER_ID));
        vehDTO.setYear(rs.getInt(YEAR));
        vehDTO.setVin(rs.getString(VIN));
        vehDTO.setMake(rs.getString(MAKE));
        vehDTO.setModel(rs.getString(MODEL));
        vehDTO.setColor(rs.getString(COLOR));
        vehDTO.setPurchaseDate(rs.getString(PURCH_DT));
        vehDTO.setSoldDate(rs.getString(SOLD_DT));

        return vehDTO;
    }

}
