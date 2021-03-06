package com.devignition.alexa.db;

import com.devignition.alexa.core.Nest;
import com.devignition.alexa.db.mapper.NestMapper;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.sql.PreparedStatement;
import java.util.Optional;

import static com.devignition.alexa.TestHelpers.newNest;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringTestConfiguration.class)
public class NestDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NestDao nestDao;

    @Test
    public void testGetAllNests() {
        long kitchenNestId = insertNest(uniquedLocation("Kitchen"), 8765);
        long bedroomNestId = insertNest(uniquedLocation("Bedroom"), 4242);

        ImmutableList<Nest> allNests = nestDao.getAllNests();
        assertThat(allNests).extracting("id").contains(kitchenNestId, bedroomNestId);
    }

    @Test
    public void testGetNest_WhenExists() {
        String location = uniquedLocation("Bedroom");
        long nestId = insertNest(location, 12345);

        Optional<Nest> nestOptional = nestDao.getNest(location);
        assertThat(nestOptional.isPresent()).isTrue();
        Nest nest = nestOptional.get();
        assertThat(nest.getId()).isEqualTo(nestId);
        assertThat(nest.getLocation()).isEqualTo(location);
    }

    @Test
    public void testGetNest_WhenDoesNotExist() {
        Optional<Nest> nestOptional = nestDao.getNest(uniquedLocation("asdf"));
        assertThat(nestOptional.isPresent()).isFalse();
    }

    @Test
    public void testCreateNest() {
        Nest unsavedNest = newNest(uniquedLocation("Hallway"), 5656);
        long nestId = nestDao.createNest(unsavedNest);
        Nest expected = unsavedNest.withId(nestId);
        assertPersistedNestEqualsExpected(nestId, expected);
    }

    private void assertPersistedNestEqualsExpected(long id, Nest expected) {
        jdbcTemplate.query("select * from nests where id = " + id, rs -> {
            Nest found = new NestMapper().map(0, rs, null);
            assertThat(found).isEqualToComparingFieldByField(expected);
        });
    }

    @Test
    public void testDeleteNest() {
        String location = uniquedLocation("Bedroom");
        long nestId = insertNest(location, 12345);
        assertThat(nestExists(nestId)).isTrue();
        nestDao.deleteNest(location);
        assertThat(nestExists(nestId)).isFalse();
    }

    private boolean nestExists(long id) {
        int count = countRowsInTableWhere("nests", "id = " + id);
        return count > 0;
    }

    private String uniquedLocation(String location) {
        return location + " " + System.nanoTime();
    }

    private long insertNest(String location, long locationId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into nests (location, location_id) values (?, ?)");
            ps.setString(1, location);
            ps.setLong(2, locationId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

}