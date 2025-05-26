package gatodev.dao;

import gatodev.config.DBConnector;
import gatodev.models.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements DAORepository<Contact, Long> {
    private final Logger log = LoggerFactory.getLogger(ContactDAO.class);
    private final Connection con = DBConnector.dbConnector.getCon();
    public final static ContactDAO instance = new ContactDAO();

    //Crea la tabla si no existe
    private ContactDAO() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS contacts (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                firstname VARCHAR(100),
                lastname VARCHAR(100),
                company VARCHAR(100),
                phone_number VARCHAR(20),
                email VARCHAR(100),
                birth_date DATE,
                address VARCHAR(255)
        );""";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Devuelve el objeto si es guardado
    @Override
    public Contact save(Contact c) {
        String query = """
            INSERT INTO contacts
                (firstname,
                lastname,
                company,
                phone_number,
                email,
                birth_date,
                address)
                VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try(PreparedStatement ps = con
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setContactData(c, ps);

            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0) {
                log.warn("Contacto no guardado.");
                return null;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                }
            }

            log.info("Contacto guardado.");
            return c;
        } catch (SQLException e) {
            log.error("Error al guardar contacto: {}", e.getMessage());
        }
        return null;
    }

    //Actualiza y devuelve si existe
    @Override
    public Contact update(Contact contact) {
        String query = """
            UPDATE contacts SET
                firstname = ?,
                lastname = ?,
                company = ?,
                phone_number = ?,
                email = ?,
                birth_date = ?,
                address = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(query)) {
            setContactData(contact, ps);
            ps.setLong(8, contact.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Contacto con ID {} actualizado con éxito.", contact.getId());
                return contact;
            }

            log.warn("Contacto con ID {} no encontrado.", contact.getId());
            return null;
        } catch (SQLException e) {
            log.error("Error al actualizar contacto: {}", e.getMessage());
            return null;
        }
    }

    //Coloca los datos del query SAVE y UPDATE
    private void setContactData(Contact contactUpdate, PreparedStatement ps) throws SQLException {
        ps.setString(1, contactUpdate.getFirstname());
        ps.setString(2, contactUpdate.getLastname());
        ps.setString(3, contactUpdate.getCompany());
        ps.setString(4, contactUpdate.getPhoneNumber());
        ps.setString(5, contactUpdate.getEmail());
        ps.setDate(6, Date.valueOf(contactUpdate.getBirthDate()));
        ps.setString(7, contactUpdate.getAddress());
    }

    //Elimina un contacto por su id
    @Override
    public void delete(Long id) {
        String query = "DELETE FROM contacts WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                log.warn("No se encontró el contacto con el ID {}.", id);
                return;
            }

            log.info("Contacto eliminado exitosamente.");
        } catch (SQLException e) {
            log.error("Error al eliminar el contacto: {}", e.getMessage());
        }
    }

    //Lista todos los contactos
    @Override
    public List<Contact> findAll() {
        String query = "SELECT * FROM contacts";
        List<Contact> contacts = new ArrayList<>();

        try(PreparedStatement ps = con
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contacts.add(Contact.builder()
                        .id(rs.getLong("id"))
                        .firstname(rs.getString("firstname"))
                        .lastname(rs.getString("lastname"))
                        .company(rs.getString("company"))
                        .phoneNumber(rs.getString("phone_number"))
                        .email(rs.getString("email"))
                        .birthDate(LocalDate.parse(rs.getString("birth_date")))
                        .address(rs.getString("address"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error al consultar los contactos: {}", e.getMessage());
        }
        log.info("Contactos consultados exitosamente.");
        return contacts;
    }

    //Busca un contacto por su id
    @Override
    public Contact findById(Long id) {
        String query = "SELECT * FROM contacts WHERE id = ?";

        try(PreparedStatement ps = con
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Contact contact = Contact.builder()
                        .id(rs.getLong("id"))
                        .firstname(rs.getString("firstname"))
                        .lastname(rs.getString("lastname"))
                        .company(rs.getString("company"))
                        .phoneNumber(rs.getString("phone_number"))
                        .email(rs.getString("email"))
                        .birthDate(LocalDate.parse(rs
                                .getString("birth_date")))
                        .address(rs.getString("address"))
                        .build();
                log.info("Contacto encontrado exitosamente.");
                return contact;
            }
        } catch (SQLException e) {
            log.error("Error al consultar el contacto: {}", e.getMessage());
        }
        log.warn("Contacto no encontrado.");
        return null;
    }
}
