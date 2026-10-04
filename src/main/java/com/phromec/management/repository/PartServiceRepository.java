package com.phromec.management.repository;

import com.phromec.management.model.Machine;
import com.phromec.management.model.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PartServiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Part> getAllParts() {

        List<Part> partList=new ArrayList<>();

        partList=jdbcTemplate.query("SELECT\n" +
                "    p.part_id,\n" +
                "    p.part_no,\n" +
                "    p.part_name,\n" +
                "    p.description,\n" +
                "    p.machine_id,\n" +
                "    m.machine_name,\n" +
                "    m.machine_type,\n" +
                "    p.category,\n" +
                "    p.material,\n" +
                "    p.unit,\n" +
                "    p.base_price,\n" +
                "    p.stock_quantity,\n" +
                "    p.lead_time,\n" +
                "    p.status,\n" +
                "    p.created_at,\n" +
                "    p.updated_at\n" +
                "FROM parts p\n" +
                "INNER JOIN machines m\n" +
                "    ON p.machine_id = m.machine_id;", new RowMapper<Part>(){

            @Override
            public Part mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub

                Part part = new Part();

                part.setPartId(rs.getLong("part_id"));
                part.setPartNo(rs.getString("part_no"));
                part.setPartName(rs.getString("part_name"));
                part.setDescription(rs.getString("description"));
                /*part.setMachineId(rs.getLong("machine_id"));*/
                part.setCategory(rs.getString("category"));
                part.setMaterial(rs.getString("material"));
                part.setUnit(rs.getString("unit"));
                part.setBasePrice(rs.getBigDecimal("base_price"));
                part.setStockQuantity(rs.getInt("stock_quantity"));
                part.setLeadTime(rs.getString("lead_time"));
                part.setStatus(rs.getString("status"));

                if (rs.getTimestamp("created_at") != null) {
                    part.setCreatedAt(
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }

                if (rs.getTimestamp("updated_at") != null) {
                    part.setUpdatedAt(
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                }

                return part;
            }
        });

        return partList;
    }
}
