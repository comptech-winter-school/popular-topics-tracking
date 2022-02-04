package com.comptechschool.populartopicstracking.SpringApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Table("testdb")  //or db2/db3
public class Identifier implements Serializable, Comparable<Identifier> {
    @PrimaryKey
    private Long id;
    private String action;
    private Long frequency;
    private Long timestamp;


    @Override
    public int compareTo(Identifier identifier) {
        return (int) (identifier.frequency - this.frequency);
    }
}
