package com.devignition.alexa.db;

import com.devignition.alexa.core.Nest;
import com.devignition.alexa.db.mapper.NestMapper;
import com.google.common.collect.ImmutableList;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

public interface NestDao {

    @SqlQuery("select * from nests order by location")
    @Mapper(NestMapper.class)
    ImmutableList<Nest> getAllNests();

    @SqlQuery("select * from nests where location = :it")
    @Mapper(NestMapper.class)
    @SingleValueResult(Nest.class)
    Optional<Nest> getNest(@Bind String location);

    @GetGeneratedKeys
    @SqlUpdate("insert into nests (location, location_id) values (:location, :locationId)")
    long createNest(@BindBean Nest nest);

    @SqlUpdate("delete from nests where location = :it")
    void deleteNest(@Bind String location);

}
