package com.comptechschool.populartopicstracking.rest.repos;

import com.comptechschool.populartopicstracking.rest.entity.Identifier;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface IdentifierRepository extends CassandraRepository<Identifier, Long> {

    @Query("Select * from topn where id='id'")
    List<Identifier> findElementById(Long id);

}
