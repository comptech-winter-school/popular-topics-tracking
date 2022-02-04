package com.comptechschool.populartopicstracking.rest.service.impl;


import com.comptechschool.populartopicstracking.rest.dto.IdentifierDto;
import com.comptechschool.populartopicstracking.rest.entity.Identifier;
import com.comptechschool.populartopicstracking.rest.repos.IdentifierRepository;
import com.comptechschool.populartopicstracking.rest.service.IdentifierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.apache.flink.runtime.state.changelog.inmemory.InMemoryStateChangelogStorageFactory.identifier;


//For Composite Primary Key (CPK)
@Service
@Slf4j
public class IdentifierServiceImplCPK implements IdentifierService {

    private final IdentifierRepository identifierRepository;

    @Autowired
    public IdentifierServiceImplCPK(IdentifierRepository identifierRepository) {
        this.identifierRepository = identifierRepository;
    }

    @Override
    public List<IdentifierDto> getTopNIdentifiers(int n) {
        Map<Long, IdentifierDto> identifiersDto = new TreeMap<>();
        List<Identifier> identifierList = identifierRepository.findAll();
        Collections.sort(identifierList);
        for (Identifier identifier : identifierList) {
            if (identifiersDto.containsKey(identifier.getId())) {
                Long freq = identifiersDto.get(identifier.getId()).getFrequency();
                Long sum = identifier.getFrequency() + freq;
                identifiersDto.put(identifier.getId(),
                        new IdentifierDto(identifier.getId(), identifier.getAction(), sum));
            } else {
                identifiersDto.put(identifier.getId(),
                        new IdentifierDto(identifier.getId(),
                                identifier.getAction(),
                                identifier.getFrequency()));
            }
        }
        List<IdentifierDto> identifierDtos = new ArrayList<>();
        identifierDtos = (List<IdentifierDto>) identifiersDto.values();
        log.info("IN getTopNIdentifiers  - top N identifiers: {}", identifierList);
        return identifierDtos;

    }

    @Override
    public IdentifierDto getIdentifierById(Long id) {
        long frequency = 0;
        List<Identifier> elements = identifierRepository.findElementById(id);
        if (elements == null || elements.size() == 0) {
            log.warn("IN getIdentifierById - no identifier found by id: {}", id);
            return null;
        }
        for (Identifier identifierTemp : elements) {
            frequency += identifierTemp.getFrequency();
        }
        String actionType = elements.get(0).getAction();
        IdentifierDto identifierDto = new IdentifierDto(id, actionType, frequency);
        log.info("IN getIdentifierById  - identifier: {} found by id: {}", identifier, id);
        return identifierDto;
    }


    @Override
    public List<IdentifierDto> getAllIdentifiers() {
        Map<Long, IdentifierDto> identifiersDto = new TreeMap<>();
        List<Identifier> identifierList = identifierRepository.findAll();
        for (Identifier identifier : identifierList) {
            if (identifiersDto.containsKey(identifier.getId())) {
                Long freq = identifiersDto.get(identifier.getId()).getFrequency();
                Long sum = identifier.getFrequency() + freq;
                identifiersDto.put(identifier.getId(),
                        new IdentifierDto(identifier.getId(), identifier.getAction(), sum));
            } else {
                identifiersDto.put(identifier.getId(), new IdentifierDto(identifier.getId(),
                        identifier.getAction(),
                        identifier.getFrequency()));
            }
        }
        List<IdentifierDto> identifierDtos = new ArrayList<>();
        identifierDtos = (List<IdentifierDto>) identifiersDto.values();
        log.info("IN getAllIdentifiers - {} Identifiers found", identifierList.size());
        return identifierDtos;
    }
}
