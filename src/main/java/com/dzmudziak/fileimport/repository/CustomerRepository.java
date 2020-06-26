package com.dzmudziak.fileimport.repository;

import com.dzmudziak.fileimport.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
