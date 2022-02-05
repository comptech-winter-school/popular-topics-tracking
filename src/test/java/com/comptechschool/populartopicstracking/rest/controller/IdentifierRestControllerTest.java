package com.comptechschool.populartopicstracking.rest.controller;

import com.comptechschool.populartopicstracking.rest.dto.IdentifierDto;
import com.comptechschool.populartopicstracking.rest.service.impl.IdentifierServiceImplSPK;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class IdentifierRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IdentifierServiceImplSPK identifierService;

    @Test
    void getIdentifierById() throws Exception {
        IdentifierDto identifierDto = new IdentifierDto();
        identifierDto.setId(1L);
        identifierDto.setAction("Like");
        identifierDto.setFrequency(50L);

        when(identifierService.getIdentifierById(1L))
                .thenReturn(identifierDto);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/identifier/get_by_id/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getTopNIdentifiers() throws Exception {
        IdentifierDto identifierDto1 = new IdentifierDto();
        identifierDto1.setId(1L);
        identifierDto1.setAction("Like");
        identifierDto1.setFrequency(50L);

        IdentifierDto identifierDto2 = new IdentifierDto();
        identifierDto2.setId(2L);
        identifierDto2.setAction("Like");
        identifierDto2.setFrequency(35L);

        IdentifierDto identifierDto3 = new IdentifierDto();
        identifierDto3.setId(3L);
        identifierDto3.setAction("Like");
        identifierDto3.setFrequency(85L);

        List<IdentifierDto> identifierDtoList = new ArrayList<>();
        identifierDtoList.add(identifierDto1);
        identifierDtoList.add(identifierDto2);
        identifierDtoList.add(identifierDto3);

        when(identifierService.getTopNIdentifiers(3))
                .thenReturn(identifierDtoList);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/identifier/get_top/{id}", 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllIdentifiers() throws Exception {
        IdentifierDto identifierDto1 = new IdentifierDto();
        identifierDto1.setId(1L);
        identifierDto1.setAction("Like");
        identifierDto1.setFrequency(50L);

        IdentifierDto identifierDto2 = new IdentifierDto();
        identifierDto2.setId(2L);
        identifierDto2.setAction("Like");
        identifierDto2.setFrequency(35L);

        IdentifierDto identifierDto3 = new IdentifierDto();
        identifierDto3.setId(3L);
        identifierDto3.setAction("Like");
        identifierDto3.setFrequency(85L);

        List<IdentifierDto> identifierDtoList = new ArrayList<>();
        identifierDtoList.add(identifierDto1);
        identifierDtoList.add(identifierDto2);
        identifierDtoList.add(identifierDto3);

        when(identifierService.getTopNIdentifiers(3))
                .thenReturn(identifierDtoList);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/identifier/get_all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}