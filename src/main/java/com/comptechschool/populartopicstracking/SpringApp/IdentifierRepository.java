package com.comptechschool.populartopicstracking.SpringApp;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface IdentifierRepository extends CassandraRepository<Identifier , Long> {
}
