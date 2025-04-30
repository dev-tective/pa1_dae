package gatodev.dao;

import gatodev.config.DBConnector;
import gatodev.models.Contact;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class ContactDAO implements DAORepository<Contact, Long> {
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
                JOptionPane.showMessageDialog(null,
                        "Contacto no guardado.");
                return null;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                }
            }

            con.close();
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Contact update(Contact contact) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public List<Contact> findAll() {
        return List.of();
    }

    @Override
    public Contact findById(Long aLong) {
        return null;
    }
}
