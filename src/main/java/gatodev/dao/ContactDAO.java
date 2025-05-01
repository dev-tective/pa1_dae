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

    private ContactDAO() {

    }

    @Override
    public Contact save(Contact c) {
        String query = "INSERT INTO contacts " +
                "(firstname," +
                " lastname, " +
                "company," +
                " phone_number, " +
                "email," +
                " birth_date, " +
                "address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = con
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getCompany());
            ps.setString(4, c.getPhoneNumber());
            ps.setString(5, c.getEmail());
            ps.setDate(6, Date.valueOf(c.getBirthDate()));
            ps.setString(7, c.getAddress());

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

    @Override
    public Contact update(Contact contact) {
        return null;
    }

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
                        .firstName(rs.getString("firstname"))
                        .lastName(rs.getString("lastname"))
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
                        .firstName(rs.getString("firstname"))
                        .lastName(rs.getString("lastname"))
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
