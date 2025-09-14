package com.bankingsystem.customerservice.controller;

import com.bankingsystem.customerservice.dto.RegisterRequest;
import com.bankingsystem.customerservice.dto.CustomerResponse;
import com.bankingsystem.customerservice.model.Customer;
import com.bankingsystem.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            CustomerResponse customerResponse = customerService.registerUser(registerRequest);
            return ResponseEntity.ok(customerResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(("Error: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllUsers() {
        return ResponseEntity.ok(customerService.getAllUsers());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity <Customer> findById(@PathVariable Long id) {
        return customerService.getUserByUserId(id)
                .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        customerService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
