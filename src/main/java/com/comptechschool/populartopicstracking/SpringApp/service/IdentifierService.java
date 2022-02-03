package com.comptechschool.populartopicstracking.SpringApp.service;

import com.comptechschool.populartopicstracking.SpringApp.Identifier;

import java.util.List;

public interface IdentifierService {

    List<Identifier> getAllTopNIdentifiers();

    Identifier getIdentifierById(Long id);

    List<Identifier> getLastAddedIdentifiers(int count);

}
