package com.devignition.alexa.db.mapper;

import com.devignition.alexa.core.Nest;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NestMapper implements ResultSetMapper<Nest> {
    @Override
    public Nest map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return Nest.builder()
                .id(rs.getLong("id"))
                .location(rs.getString("location"))
                .locationId(rs.getLong("location_id"))
                .build();
    }
}
