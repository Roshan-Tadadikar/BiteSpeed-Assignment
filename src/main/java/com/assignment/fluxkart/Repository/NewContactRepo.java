package com.assignment.fluxkart.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class NewContactRepo {

    private final JdbcTemplate jdbcTemplate;

    public NewContactRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Integer findPrimaryIdByEmailIfExistOrMinId(String email) {
        String query = "SELECT COALESCE((SELECT c1.linked_id  FROM Contact c1 WHERE c1.email = ? ORDER BY c1.id ASC LIMIT 1), (SELECT MIN(c2.id) FROM Contact c2 WHERE c2.email = ? LIMIT 1)) FROM Contact c";

        return jdbcTemplate.queryForObject(query, new Object[]{email}, Integer.class);
    }

    public Integer findPrimaryIdByPhNumberIfExistOrMinId(Integer number) {
        String query = "SELECT COALESCE((SELECT c1.linked_id FROM Contact c1 WHERE c1.phone_number = ?   ORDER BY c1.id ASC LIMIT 1), (SELECT MIN(c2.id) FROM Contact c2 WHERE c2.phone_number = ?  LIMIT 1)) FROM Contact c";

        return jdbcTemplate.queryForObject(query, new Object[]{number, number}, Integer.class);
    }

}
