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
        Map<Long, IdentifierDto> identifiersMapDto = new TreeMap<>();
        List<Identifier> identifierList = identifierRepository.findAll();
        Collections.sort(identifierList);
        for (Identifier identifier : identifierList) {
            if (identifiersMapDto.containsKey(identifier.getId())) {
                Long freq = identifiersMapDto.get(identifier.getId()).getFrequency();
                Long sum = identifier.getFrequency() + freq;
                identifiersMapDto.put(identifier.getId(),
                        new IdentifierDto(identifier.getId(), identifier.getAction(), sum));
            } else {
                identifiersMapDto.put(identifier.getId(),
                        new IdentifierDto(identifier.getId(),
                                identifier.getAction(),
                                identifier.getFrequency()));
            }
        }
        List<IdentifierDto> identifierDtos = new ArrayList<>();
        //identifierDtos = (List<IdentifierDto>) identifiersDto.values();
        for (int i = 1; i < identifiersMapDto.size()+1; i++) {
            identifierDtos.add(identifiersMapDto.get((long)i));
        }
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
        Map<Long, IdentifierDto> identifierDtoTreeMap = new TreeMap<>();
        List<Identifier> identifierList = identifierRepository.findAll();
        for (Identifier identifier : identifierList) {
            if (identifierDtoTreeMap.containsKey(identifier.getId())) {
                Long freq = identifierDtoTreeMap.get(identifier.getId()).getFrequency();
                Long sum = identifier.getFrequency() + freq;
                identifierDtoTreeMap.put(identifier.getId(),
                        new IdentifierDto(identifier.getId(), identifier.getAction(), sum));
            } else {
                identifierDtoTreeMap.put(identifier.getId(), new IdentifierDto(identifier.getId(),
                        identifier.getAction(),
                        identifier.getFrequency()));
            }
        }
        List<IdentifierDto> identifierDtos = new ArrayList<>();
        for (int i = 1; i < identifierDtoTreeMap.size()+1; i++) {
            identifierDtos.add(identifierDtoTreeMap.get((long)i));
        }
        log.info("IN getAllIdentifiers - {} Identifiers found", identifierList.size());
        return identifierDtos;
    }
}
