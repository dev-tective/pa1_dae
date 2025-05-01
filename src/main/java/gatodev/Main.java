package gatodev;

import gatodev.dao.ContactDAO;
import gatodev.models.Contact;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido a su Agenda.");

        while (true) {
            menu();

            try {
                int option = sc.nextInt();
                switch (option) {
                    case 1:
                        System.out.println(ContactDAO.instance.save(createContact(sc)));
                        break;
                    case 2:
                        System.out.print("Ingrese el ID: ");
                        long id = sc.nextLong();
                        System.out.println(ContactDAO.instance.findById(id));
                        break;
                    case 3:
                        System.out.println(ContactDAO.instance.findAll());
                        break;
                    case 4:
                        System.out.println("Funcionalidad no implementada.");
                        break;
                    case 5:
                        System.out.print("Ingrese el ID del contacto a eliminar: ");
                        long deleteId = sc.nextLong();
                        ContactDAO.instance.delete(deleteId);
                        break;
                    case 6:
                        System.out.println("Saliendo...");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.err.printf("Error en la entrada: %s\n", e.getMessage());
            }
        }
    }

    private static void menu() {
        System.out.println("\nIngresa un número:");
        System.out.println("1. Crear Contacto");
        System.out.println("2. Buscar Contacto");
        System.out.println("3. Listar Contactos");
        System.out.println("4. Actualizar Contacto");
        System.out.println("5. Eliminar Contacto");
        System.out.println("6. Salir");
    }

    private static Contact createContact(Scanner sc) {
        Contact contact = new Contact();
        System.out.println("Ingrese el nombre.");
        contact.setFirstName(sc.next());
        System.out.println("Ingrese el apellido.");
        contact.setLastName(sc.next());
        System.out.println("Ingrese la compañía.");
        contact.setCompany(sc.next());
        System.out.println("Ingrese el numero de teléfono.");
        contact.setPhoneNumber(sc.next());
        System.out.println("Ingrese el email.");
        contact.setEmail(sc.next());
        System.out.println("Ingrese la fecha de nacimiento. Ejm 1999-12-31");
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(sc.next());
            } catch (Exception e) {
                System.err.printf("Error en la entrada: %s\n", e.getMessage());
                System.out.println("Ingrese la fecha de nacimiento. Ejm 1999-12-31");
            }
        }
        contact.setBirthDate(date);
        System.out.println("Ingrese la dirección.");
        contact.setAddress(sc.next());
        return contact;
    }
}