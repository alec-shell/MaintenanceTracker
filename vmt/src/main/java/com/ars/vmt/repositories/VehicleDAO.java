/**
 * 11/23/2026   Class created.
 */

package com.ars.vmt.repositories;

import com.ars.vmt.DTO.VehicleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public VehicleDTO getVehicleByVehId(int vehId) throws SQLException {
        logger.debug("VehicleDAO.getVehicleByVehId: Id = {}",  vehId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM VEH_VEHICLES WHERE VEH_ID = ?")) {
            prepStmt.setInt(1, vehId);

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

    public ArrayList<VehicleDTO> getVehiclesByUserId(int userId) throws SQLException {
        logger.debug("VehicleDAO.getVehiclesByUserId: UserId = {}",  userId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM VEH_VEHICLES WHERE VEH_USER_ID = ?")) {
            prepStmt.setInt(1, userId);

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

    private VehicleDTO buildVehicleDTO(ResultSet rs) throws SQLException{
        logger.debug("VehicleDAO.buildVehicleDTO");

        VehicleDTO vehDTO = new VehicleDTO();
        vehDTO.setVehId(rs.getInt(VEH_ID));
        vehDTO.setUserId(rs.getInt(USER_ID));
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
