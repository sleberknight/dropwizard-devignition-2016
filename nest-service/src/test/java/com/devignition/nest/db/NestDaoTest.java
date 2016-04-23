package com.devignition.nest.db;

import com.devignition.nest.core.Mode;
import com.devignition.nest.core.NestThermostat;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.sql.PreparedStatement;
import java.util.List;

import static com.devignition.nest.TestHelpers.newNest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ContextConfiguration(classes = SpringTestConfiguration.class)
public class NestDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    private NestDao nestDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Before
    public void setUp() {
        nestDao = new NestDao(sessionFactory);
    }

    /**
     * Ensures un-persisted changes in session are flushed to the database.
     */
    private void flushSession() {
        sessionFactory.getCurrentSession().flush();
    }

    @Test
    public void testGetAll() {
        long diningRoomId = insertNest(uniquedLocation("Dining Room"), 73, Mode.COOL);
        long masterBedroomId = insertNest(uniquedLocation("Master Bedroom"), 72, Mode.COOL);

        List<NestThermostat> nests = nestDao.getAll();
        assertThat(nests).extracting("id").contains(diningRoomId, masterBedroomId);
    }

    @Test
    public void testGetById_WhenExists() {
        long id = insertNest(uniquedLocation("Dining Room"), 73, Mode.COOL);

        NestThermostat nest = nestDao.getById(id);
        assertThat(nest.getTemperature()).isEqualTo(73);
    }

    @Test
    public void testGetById_WhenDoesNotExist() {
        NestThermostat nest = nestDao.getById(Long.MIN_VALUE);
        assertThat(nest).isNull();
    }

    @Test
    public void testCreate_WhenNestHasId() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> nestDao.create(newNest(42L, "Hallway", 72, Mode.HEAT)))
                .withMessage("New nests cannot have an id");
    }

    @Test
    public void testCreate() {
        NestThermostat nest = newNest(uniquedLocation("Hallway"), 70, Mode.HEAT);
        long id = nestDao.create(nest);
        assertThat(nest.getId()).isNotNull();

        assertExpectedNest(id, nest);
    }

    @Test
    public void testUpdate_WhenNestDoesNotHaveId() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> nestDao.update(newNest("Basement", 67, Mode.HEAT)))
                .withMessage("Nest must have an id in order to update it");
    }

    @Test
    public void testUpdate() {
        String location = uniquedLocation("Hallway");
        long id = insertNest(location, 70, Mode.HEAT);

        NestThermostat changedNest = NestThermostat.builder()
                .id(id)
                .location(location)
                .temperature(72)
                .mode(Mode.HEAT)
                .build();

        nestDao.update(changedNest);
        flushSession();
        assertExpectedNest(id, changedNest);
    }

    @Test
    public void testDelete() {
        long id = insertNest(uniquedLocation("Basement"), 68, Mode.HEAT);

        assertThat(nestExists(id)).isTrue();
        nestDao.delete(id);
        flushSession();
        assertThat(nestExists(id)).isFalse();
    }

    private long insertNest(String location, int temp, Mode mode) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into nests (location, temperature, mode) values (?, ?, ?)");
            ps.setString(1, location);
            ps.setInt(2, temp);
            ps.setString(3, mode.name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private String uniquedLocation(String location) {
        return location + " " + System.nanoTime();
    }

    private boolean nestExists(long id) {
        int count = countRowsInTableWhere("nests", "id = " + id);
        return count > 0;
    }

    private void assertExpectedNest(long id, NestThermostat expected) {
        jdbcTemplate.query("select * from nests where id = " + id, rs -> {
            assertThat(rs.getString("location")).isEqualTo(expected.getLocation());
            assertThat(rs.getInt("temperature")).isEqualTo(expected.getTemperature());
            assertThat(rs.getString("mode")).isEqualTo(expected.getMode().name());
        });
    }

}