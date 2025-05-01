package gatodev;

import gatodev.config.DBConnector;
import gatodev.dao.ContactDAO;
import gatodev.models.Contact;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Iniciar la conexión y despliegue de la DB
        DBConnector dbConnector = DBConnector.dbConnector;
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido a su Agenda.");

        //Menu de operaciones
        while (true) {
            menu();
            try {
                String input = sc.nextLine().trim();
                int option = Integer.parseInt(input);
                //Opciones mencionas en la función menu
                switch (option) {
                    case 1:
                        System.out.println(ContactDAO.instance.save(createContact(sc)));
                        break;
                    case 2:
                        System.out.print("Ingrese el ID: ");
                        long id = Long.parseLong(sc.nextLine().trim());
                        System.out.println(ContactDAO.instance.findById(id));
                        break;
                    case 3:
                        System.out.println(ContactDAO.instance.findAll());
                        break;
                    case 4:
                        System.out.println(ContactDAO.instance.update(createContactWithID(sc)));
                        break;
                    case 5:
                        System.out.print("Ingrese el ID del contacto a eliminar: ");
                        long deleteId = Long.parseLong(sc.nextLine().trim());
                        ContactDAO.instance.delete(deleteId);
                        break;
                    case 6:
                        System.out.println("Saliendo...");
                        DBConnector.dbConnector.getCon().close();
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

    //Función para obtener un contacto mediante scanner
    private static Contact createContact(Scanner sc) {
        Contact contact = new Contact();
        System.out.println("Ingrese el nombre.");
        contact.setFirstName(sc.nextLine());
        System.out.println("Ingrese el apellido.");
        contact.setLastName(sc.nextLine());
        System.out.println("Ingrese la compañía.");
        contact.setCompany(sc.nextLine());
        System.out.println("Ingrese el numero de teléfono.");
        contact.setPhoneNumber(sc.nextLine());
        System.out.println("Ingrese el email.");
        contact.setEmail(sc.nextLine());
        System.out.println("Ingrese la fecha de nacimiento. Ejm 1999-12-31");
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(sc.nextLine());
            } catch (Exception e) {
                System.err.printf("Error en la entrada: %s\n", e.getMessage());
                System.out.println("Ingrese la fecha de nacimiento. Ejm 1999-12-31");
            }
        }
        contact.setBirthDate(date);
        System.out.println("Ingrese la dirección.");
        contact.setAddress(sc.nextLine());
        return contact;
    }

    private static Contact createContactWithID(Scanner sc) {
        Contact contact = createContact(sc);
        System.out.print("Ingrese el ID: ");
        contact.setId(sc.nextLong());
        return contact;
    }
}