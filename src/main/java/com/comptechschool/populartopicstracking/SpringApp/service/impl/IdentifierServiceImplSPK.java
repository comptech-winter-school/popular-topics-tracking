package com.comptechschool.populartopicstracking.SpringApp.service.impl;

import com.comptechschool.populartopicstracking.SpringApp.dto.IdentifierDto;
import com.comptechschool.populartopicstracking.SpringApp.entity.Identifier;
import com.comptechschool.populartopicstracking.SpringApp.repos.IdentifierRepository;
import com.comptechschool.populartopicstracking.SpringApp.service.IdentifierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//For Single Primary Key (SPK)
@Service
@Slf4j
public class IdentifierServiceImplSPK implements IdentifierService {

    private final IdentifierRepository identifierRepository;

    @Autowired
    public IdentifierServiceImplSPK(IdentifierRepository identifierRepository) {
        this.identifierRepository = identifierRepository;
    }

    @Override
    public List<IdentifierDto> getTopNIdentifiers(int n) {
        List<IdentifierDto> identifierTopNDto = new ArrayList<>();
        List<Identifier> allIdentifiers = identifierRepository.findAll();
        Collections.sort(allIdentifiers);
        for (int i = 0; i < n; i++) {
            identifierTopNDto.add(new IdentifierDto(allIdentifiers.get(i).getId() , allIdentifiers.get(i).getAction() , allIdentifiers.get(i).getFrequency()));
        }
        log.info("IN getTopNIdentifiers  - top N identifiers: {}", identifierTopNDto);
        return identifierTopNDto;
    }

    @Override
    public IdentifierDto getIdentifierById(Long id) {
        Identifier identifier = identifierRepository.findById(id).orElse(null);
        if (identifier == null) {
            log.warn("IN getIdentifierById - no identifier found by id: {}", id);
            return null;
        }
        log.info("IN getIdentifierById  - identifier: {} found by id: {}", identifier, id);
        return new IdentifierDto(identifier.getId()
                , identifier.getAction()
                , identifier.getFrequency());
    }
    
    @Override
    public List<IdentifierDto> getAllIdentifiers(int count) {
        List<IdentifierDto> identifiersDto = new ArrayList<>();
        List<Identifier> identifierList = identifierRepository.findAll();
        for (Identifier identifier: identifierList) {
            identifiersDto.add(new IdentifierDto(identifier.getId() , identifier.getAction() , identifier.getFrequency()));
        }
        log.info("IN getAllQuestions - {} Identifiers found", identifierList.size());
        return identifiersDto;
    }
}
