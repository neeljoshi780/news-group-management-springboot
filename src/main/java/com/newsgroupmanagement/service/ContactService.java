package com.newsgroupmanagement.service;

import com.newsgroupmanagement.dto.ContactDto;
import com.newsgroupmanagement.model.Contact;
import com.newsgroupmanagement.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void saveContact(ContactDto contactDto) {
        Contact contact = new Contact();
        contact.setFullName(contactDto.getFullName());
        contact.setEmail(contactDto.getEmail());
        contact.setPhone(contactDto.getPhone());
        contact.setMessage(contactDto.getMessage());

        contactRepository.save(contact);
    }
}
