package com.comptechschool.populartopicstracking.rest.service;

import com.comptechschool.populartopicstracking.rest.dto.IdentifierDto;

import java.util.List;

public interface IdentifierService {

    List<IdentifierDto> getTopNIdentifiers(int n);

    IdentifierDto getIdentifierById(Long id);

    List<IdentifierDto> getAllIdentifiers();

}
