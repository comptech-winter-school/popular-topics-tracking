package com.comptechschool.populartopicstracking.SpringApp;

import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "testdb")
public class Identifier implements Serializable {
    @PrimaryKey
    private Long id;
    private Long frequency;
    private String action;
}
