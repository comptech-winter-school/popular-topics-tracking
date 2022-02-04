package com.comptechschool.populartopicstracking.SpringApp.repos;

import com.comptechschool.populartopicstracking.SpringApp.entity.Identifier;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface IdentifierRepository extends CassandraRepository<Identifier, Long> {

    @Query("Select * from testdb where id='id'")
    List<Identifier> findElementById(Long id);

}
