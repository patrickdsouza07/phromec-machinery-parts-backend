package com.phromec.management.repository;

import com.phromec.management.model.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MachineServiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Machine> getAllMachines() {

        List<Machine> machineList=new ArrayList<>();

        machineList=jdbcTemplate.query("SELECT\n" +
                "    machine_id,\n" +
                "    machine_name,\n" +
                "    description,\n" +
                "    machine_type,\n" +
                "    model_no,\n" +
                "    manufacturer,\n" +
                "    capacity,\n" +
                "    parts_count,\n" +
                "    status,\n" +
                "    created_at,\n" +
                "    updated_at\n" +
                "FROM machines;", new RowMapper<Machine>(){

            @Override
            public Machine mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub

                Machine machine = new Machine();

                machine.setMachineId(rs.getLong("machine_id"));
                machine.setMachineName(rs.getString("machine_name"));
                machine.setDescription(rs.getString("description"));
//                machine.setMachineType(rs.getString("machine_type"));
//                machine.setModelNo(rs.getString("model_no"));
                machine.setManufacturer(rs.getString("manufacturer"));
                machine.setCapacity(rs.getString("capacity"));
                machine.setPartsCount(rs.getInt("parts_count"));
                machine.setStatus(rs.getString("status"));

                if (rs.getTimestamp("created_at") != null) {
                    machine.setCreatedAt(
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }

                if (rs.getTimestamp("updated_at") != null) {
                    machine.setUpdatedAt(
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                }

                return machine;
            }
        });

        return machineList;
    }
}
