package com.bankingsystem.customerservice.service;

import com.bankingsystem.customerservice.dto.RegisterRequest;
import com.bankingsystem.customerservice.dto.CustomerResponse;
import com.bankingsystem.customerservice.model.Customer;
import com.bankingsystem.customerservice.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;


    public CustomerResponse registerUser(RegisterRequest registerRequest) {
        if (customerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        Customer customer = new Customer();
        customer.setDateOfBirth(registerRequest.getDateOfBirth());
        customer.setEmail(registerRequest.getEmail());
        customer.setFirstName(registerRequest.getFirstName());
        customer.setLastName(registerRequest.getLastName());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());
        customer.setCreatedAt(LocalDate.now());

        Customer savedCustomer = customerRepository.save(customer);
        return mapToCustomerResponse(savedCustomer);
    }



    public Optional<Customer> getUserByUserId(Long userId) {
        return customerRepository.findById(userId);
    }


    public CustomerResponse mapToCustomerResponse(Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setFirstName(customer.getFirstName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setDateOfBirth(customer.getDateOfBirth());
        customerResponse.setCreatedAt(LocalDate.now());
        customerResponse.setUpdatedAt(LocalDate.now());
        customerResponse.setPhoneNumber(customer.getPhoneNumber());
        return customerResponse;

    }



    public void deleteUser(Long userId) {
        customerRepository.deleteById(userId);
    }

    public List<Customer> getAllUsers() {
        return customerRepository.findAll();
    }
}
