package gatodev;

import gatodev.dao.ContactDAO;
import gatodev.models.Contact;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Contact contact = Contact.builder()
                .firstName("Oswaldo")
                .lastName("Maguiño")
                .company("Ninguna")
                .phoneNumber("999999999")
                .email("oswaldo@gmail.com")
                .birthDate(LocalDate.of(2003,2,26))
                .address("Lima, Peru")
                .build();
        System.out.println(ContactDAO.instance.save(contact));
    }
}