package com.comptechschool.populartopicstracking.rest.controller;


import com.comptechschool.populartopicstracking.rest.config.Swagger2Config;
import com.comptechschool.populartopicstracking.rest.dto.IdentifierDto;
import com.comptechschool.populartopicstracking.rest.service.IdentifierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Api(tags = {Swagger2Config.TAG})
public class IdentifierRestController {

    private final IdentifierService identifierService;

    @Autowired
    public IdentifierRestController(@Qualifier("identifierServiceImplSPK") IdentifierService identifierService) {
        this.identifierService = identifierService;
    }

    @GetMapping(value = "/get_by_id/{id}", produces = "application/json")
    @Operation(summary = "Get identifier by id")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<IdentifierDto> getIdentifierById(
            @Parameter(description = "Entity id", required = true)
            @PathVariable long id) {

        IdentifierDto identifier = identifierService.getIdentifierById(id);

        return new ResponseEntity<>(identifier, HttpStatus.OK);
    }

    @GetMapping(value = "/get_top/{n}", produces = "application/json")
    @Operation(summary = "Get topn")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<IdentifierDto>> getTopNIdentifiers(
            @Parameter(description = "Elements count", required = true)
            @PathVariable int n) {

        List<IdentifierDto> identifierDtoList = identifierService.getTopNIdentifiers(n);

        return new ResponseEntity<>(identifierDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/get_all", produces = "application/json")
    @Operation(summary = "Get all identifiers")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<IdentifierDto>> getAllIdentifiers() {

        List<IdentifierDto> identifierDtoList = identifierService.getAllIdentifiers();

        return new ResponseEntity<>(identifierDtoList, HttpStatus.OK);
    }
}
