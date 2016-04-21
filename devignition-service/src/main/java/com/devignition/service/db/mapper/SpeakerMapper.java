package com.devignition.service.db.mapper;

import com.devignition.service.core.Speaker;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpeakerMapper implements ResultSetMapper<Speaker> {
    @Override
    public Speaker map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return Speaker.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("speaker_name"))
                .twitterHandle(rs.getString("twitter_handle"))
                .bio(rs.getString("bio"))
                .talkTitle(rs.getString("talk_title"))
                .talkAbstract(rs.getString("talk_abstract"))
                .build();
    }
}
