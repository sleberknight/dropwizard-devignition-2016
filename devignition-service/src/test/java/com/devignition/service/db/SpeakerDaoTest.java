package com.devignition.service.db;

import com.devignition.service.core.Speaker;
import com.devignition.service.db.mapper.SpeakerMapper;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.sql.PreparedStatement;
import java.util.Optional;

import static com.devignition.service.TestHelpers.newSpeaker;
import static com.devignition.service.TestHelpers.someLoremIpsumText;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringTestConfiguration.class)
public class SpeakerDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SpeakerDao speakerDao;

    @Test
    public void testGetAllSpeakers() {
        long bobId = insertSpeaker("Bob Smith", "@speakerbob", "JDBI is simple");
        long aliceId = insertSpeaker("Alice Jones", "@alice_jones", "Why I like Dropwizard");

        ImmutableList<Speaker> allSpeakers = speakerDao.getAllSpeakers();
        assertThat(allSpeakers).extracting("id").contains(bobId, aliceId);
    }

    @Test
    public void testGetSpeaker_WhenExists() {
        long bobId = insertSpeaker("Bob Smith", "@speakerbob", "JDBI is simple");

        Optional<Speaker> speakerOptional = speakerDao.getSpeaker(bobId);
        assertThat(speakerOptional.isPresent()).isTrue();
        Speaker speaker = speakerOptional.get();
        assertThat(speaker.getTwitterHandle()).isEqualTo("@speakerbob");
    }

    @Test
    public void testGetSpeaker_WhenDoesNotExist() {
        Optional<Speaker> speakerOptional = speakerDao.getSpeaker(Long.MIN_VALUE);
        assertThat(speakerOptional.isPresent()).isFalse();
    }

    @Test
    public void testCreateSpeaker() {
        Speaker unsavedSpeaker = newSpeaker("Bob Smith", "@speakerbob");
        long id = speakerDao.createSpeaker(unsavedSpeaker);
        Speaker savedSpeaker = unsavedSpeaker.withId(id);

        jdbcTemplate.query("select * from speakers where id = " + id, rs -> {
            Speaker found = new SpeakerMapper().map(0, rs, null);
            assertThat(found).isEqualToComparingFieldByField(savedSpeaker);
        });
    }

    @Test
    public void testDeleteSpeaker() {
        long bobId = insertSpeaker("Bob Smith", "@speakerbob", "JDBI is simple");
        assertThat(speakerExists(bobId)).isTrue();
        speakerDao.deleteSpeaker(bobId);
        assertThat(speakerExists(bobId)).isFalse();
    }

    private boolean speakerExists(long id) {
        int count = countRowsInTableWhere("speakers", "id = " + id);
        return count > 0;
    }

    private long insertSpeaker(String name, String twitterHandle, String talkTitle) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into speakers (speaker_name, twitter_handle, bio, talk_title, talk_abstract) values (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, twitterHandle);
            ps.setString(3, someLoremIpsumText());
            ps.setString(4, talkTitle);
            ps.setString(5, someLoremIpsumText());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

}
