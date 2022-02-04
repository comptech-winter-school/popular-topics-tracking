package com.comptechschool.populartopicstracking.SpringApp.controller;


import com.comptechschool.populartopicstracking.SpringApp.dto.IdentifierDto;
import com.comptechschool.populartopicstracking.SpringApp.service.IdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/identifier")
public class IdentifierRestController {

    private final IdentifierService identifierService;

    @Autowired
    public IdentifierRestController(@Qualifier("identifierServiceImplSPK") IdentifierService identifierService) {
        this.identifierService = identifierService;
    }

    @GetMapping(value = "/get_by_id/{id}")
    public ResponseEntity<IdentifierDto> getIdentifierById(@PathVariable long id) {

        IdentifierDto identifier = identifierService.getIdentifierById(id);

        return new ResponseEntity<>(identifier, HttpStatus.OK);
    }

    @GetMapping(value = "/get_top/{n}")
    public ResponseEntity<List<IdentifierDto>> getTopNIdentifiers(@PathVariable int n) {

        List<IdentifierDto> identifierDtoList = identifierService.getTopNIdentifiers(n);

        return new ResponseEntity<>(identifierDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/get_all")
    public ResponseEntity<List<IdentifierDto>> getAllIdentifiers() {

        List<IdentifierDto> identifierDtoList = identifierService.getAllIdentifiers();

        return new ResponseEntity<>(identifierDtoList, HttpStatus.OK);
    }
}
