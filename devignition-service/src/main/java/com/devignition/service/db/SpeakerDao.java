package com.devignition.service.db;

import com.devignition.service.core.Speaker;
import com.devignition.service.db.mapper.SpeakerMapper;
import com.google.common.collect.ImmutableList;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

public interface SpeakerDao {

    @SqlQuery("select * from speakers order by speaker_name")
    @Mapper(SpeakerMapper.class)
    ImmutableList<Speaker> getAllSpeakers();

    @SqlQuery("select * from speakers where id = :id")
    @Mapper(SpeakerMapper.class)
    @SingleValueResult(Speaker.class)
    Optional<Speaker> getSpeaker(@Bind("id") long id);

    @GetGeneratedKeys
    @SqlUpdate("insert into speakers (speaker_name, twitter_handle, bio, talk_title, talk_abstract)"
            + " values (:name, :twitterHandle, :bio, :talkTitle, :talkAbstract)")
    long createSpeaker(@BindBean Speaker speaker);

    @SqlUpdate("update speakers set speaker_name = :name,"
            + " twitter_handle = :twitterHandle,"
            + " bio = :bio,"
            + " talk_title = :talkTitle,"
            + " talk_abstract = :talkAbstract"
            + " where id = :id")
    void updateSpeaker(@BindBean Speaker speaker);

    @SqlUpdate("delete from speakers where id = :it")
    void deleteSpeaker(@Bind long id);
}
