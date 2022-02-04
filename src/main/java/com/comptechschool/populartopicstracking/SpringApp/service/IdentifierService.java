package com.comptechschool.populartopicstracking.SpringApp.service;

import com.comptechschool.populartopicstracking.SpringApp.dto.IdentifierDto;

import java.util.List;

public interface IdentifierService {

    List<IdentifierDto> getTopNIdentifiers(int n);

    IdentifierDto getIdentifierById(Long id);

    List<IdentifierDto> getAllIdentifiers();

}
