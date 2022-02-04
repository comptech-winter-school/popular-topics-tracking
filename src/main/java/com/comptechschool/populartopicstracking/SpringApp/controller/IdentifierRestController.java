package com.comptechschool.populartopicstracking.SpringApp.controller;


import com.comptechschool.populartopicstracking.SpringApp.entity.Identifier;
import com.comptechschool.populartopicstracking.SpringApp.service.IdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/identifier")
public class IdentifierRestController {

    private final IdentifierService identifierService;

    @Autowired
    public IdentifierRestController(IdentifierService identifierService) {
        this.identifierService = identifierService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Identifier> getIdentifierById(@PathVariable long id) {

        identifierService.getIdentifierById(id);

        return new ResponseEntity<>(identifier, HttpStatus.OK);
    }
}
