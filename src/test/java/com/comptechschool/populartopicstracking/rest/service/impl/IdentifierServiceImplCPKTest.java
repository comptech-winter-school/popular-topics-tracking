package com.comptechschool.populartopicstracking.rest.service.impl;

import com.comptechschool.populartopicstracking.rest.dto.IdentifierDto;
import com.comptechschool.populartopicstracking.rest.entity.Identifier;
import com.comptechschool.populartopicstracking.rest.repos.IdentifierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IdentifierServiceImplCPKTest {

    @Mock
    private IdentifierRepository identifierRepository;

    @InjectMocks
    private IdentifierServiceImplCPK identifierServiceImplCPK;

    @Test
    void getTopNIdentifiers() {
        //given
        Identifier identifier1 = new Identifier();
        identifier1.setId(1L);
        identifier1.setAction("Like");
        identifier1.setFrequency(794L);
        identifier1.setTimestamp(963852741L);

        Identifier identifier2 = new Identifier();
        identifier2.setId(2L);
        identifier2.setAction("Like");
        identifier2.setFrequency(2023L);
        identifier2.setTimestamp(963854741L);

        Identifier identifier3 = new Identifier();
        identifier3.setId(3L);
        identifier3.setAction("Like");
        identifier3.setFrequency(452L);
        identifier3.setTimestamp(96385341L);

        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(identifier1);
        identifierList.add(identifier2);
        identifierList.add(identifier3);

        IdentifierDto identifierDto1 = new IdentifierDto();
        identifierDto1.setId(2L);
        identifierDto1.setAction("Like");
        identifierDto1.setFrequency(2023L);

        IdentifierDto identifierDto2 = new IdentifierDto();
        identifierDto2.setId(1L);
        identifierDto2.setAction("Like");
        identifierDto2.setFrequency(794L);

        IdentifierDto identifierDto3 = new IdentifierDto();
        identifierDto3.setId(3L);
        identifierDto3.setAction("Like");
        identifierDto3.setFrequency(452L);

        List<IdentifierDto> expectedIdentifierDtoList = new ArrayList<>();
        expectedIdentifierDtoList.add(identifierDto2);
        expectedIdentifierDtoList.add(identifierDto1);
        expectedIdentifierDtoList.add(identifierDto3);

        //when
        Mockito.when(identifierRepository.findAll()).thenReturn(identifierList);
        List<IdentifierDto> actualIdentifierDtoList = identifierServiceImplCPK.getTopNIdentifiers(2);

        //then
        assertThat(actualIdentifierDtoList).isEqualTo(expectedIdentifierDtoList);
    }

    @Test
    void getIdentifierById() {
        Identifier identifier = new Identifier();
        identifier.setId(1L);
        identifier.setAction("Like");
        identifier.setFrequency(794L);
        identifier.setTimestamp(963852741L);

        Identifier identifier2 = new Identifier();
        identifier2.setId(2L);
        identifier2.setAction("Like");
        identifier2.setFrequency(2023L);
        identifier2.setTimestamp(963854741L);


        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(identifier);
        identifierList.add(identifier2);

        IdentifierDto expectedIdentifierDto = new IdentifierDto();
        expectedIdentifierDto.setId(1L);
        expectedIdentifierDto.setAction("Like");
        expectedIdentifierDto.setFrequency(2817L);

        Mockito.when(identifierRepository.findElementById(1L)).thenReturn(identifierList);
        IdentifierDto actualIdentifierDto = identifierServiceImplCPK.getIdentifierById(1L);

        assertThat(actualIdentifierDto).isEqualTo(expectedIdentifierDto);
    }

    @Test
    void getAllIdentifiers() {
//given
        Identifier identifier1 = new Identifier();
        identifier1.setId(1L);
        identifier1.setAction("Like");
        identifier1.setFrequency(794L);
        identifier1.setTimestamp(963852741L);

        Identifier identifier2 = new Identifier();
        identifier2.setId(2L);
        identifier2.setAction("Like");
        identifier2.setFrequency(2023L);
        identifier2.setTimestamp(963854741L);

        Identifier identifier3 = new Identifier();
        identifier3.setId(3L);
        identifier3.setAction("Like");
        identifier3.setFrequency(452L);
        identifier3.setTimestamp(96385341L);

        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(identifier1);
        identifierList.add(identifier2);
        identifierList.add(identifier3);

        IdentifierDto identifierDto1 = new IdentifierDto();
        identifierDto1.setId(1L);
        identifierDto1.setAction("Like");
        identifierDto1.setFrequency(794L);

        IdentifierDto identifierDto2 = new IdentifierDto();
        identifierDto2.setId(2L);
        identifierDto2.setAction("Like");
        identifierDto2.setFrequency(2023L);

        IdentifierDto identifierDto3 = new IdentifierDto();
        identifierDto3.setId(3L);
        identifierDto3.setAction("Like");
        identifierDto3.setFrequency(452L);

        List<IdentifierDto> expectedIdentifierDtoList = new ArrayList<>();
        expectedIdentifierDtoList.add(identifierDto1);
        expectedIdentifierDtoList.add(identifierDto2);
        expectedIdentifierDtoList.add(identifierDto3);

        //when
        Mockito.when(identifierRepository.findAll()).thenReturn(identifierList);
        List<IdentifierDto> actualIdentifierDtoList = identifierServiceImplCPK.getAllIdentifiers();

        //then
        assertThat(actualIdentifierDtoList).isEqualTo(expectedIdentifierDtoList);
    }
}

