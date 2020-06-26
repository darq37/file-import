package com.dzmudziak.fileimport.repository;

import com.dzmudziak.fileimport.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
