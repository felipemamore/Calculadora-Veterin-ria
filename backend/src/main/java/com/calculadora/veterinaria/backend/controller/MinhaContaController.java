package com.calculadora.veterinaria.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.calculadora.veterinaria.backend.dto.AccountDetailsDTO;
import com.calculadora.veterinaria.backend.dto.ProfileUpdateDTO;
import com.calculadora.veterinaria.backend.service.MinhaContaService;

@RestController
@RequestMapping("/api/conta-do-usuario")
public class MinhaContaController {

     @Autowired
    private MinhaContaService accountService;
    @GetMapping
    public ResponseEntity<AccountDetailsDTO> getAccountDetails() {
        AccountDetailsDTO details = accountService.getLoggedUserDetails();
        return ResponseEntity.ok(details);
    }

    @PutMapping
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDto) {
        accountService.updateLoggedUserProfile(profileUpdateDto);
        return ResponseEntity.ok().build();
    }
}

