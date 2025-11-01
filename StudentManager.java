import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManager extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField idField, nameField, deptField, assignField;

    public StudentManager() {
        setTitle("Student Manager");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Vertical input fields using BoxLayout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        idField = new JTextField();
        nameField = new JTextField();
        deptField = new JTextField();
        assignField = new JTextField();

        formPanel.add(createFormItem("ID", idField));
        formPanel.add(createFormItem("Name", nameField));
        formPanel.add(createFormItem("Department", deptField));
        formPanel.add(createFormItem("Assigned Equipment", assignField));

        String[] columns = {"ID", "Name", "Department", "Assigned Equipment"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        backBtn.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadTable();
        setVisible(true);
    }

    private JPanel createFormItem(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jlabel = new JLabel(label);
        panel.add(jlabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        return panel;
    }

    // ... Rest of your CRUD/loadTable/clearForm/fillFormFromTable methods remain unchanged ...
    // Paste your previous methods from above here.
    private void loadTable() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM student")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("assignedEquipment")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO student (id, name, department, assignedEquipment) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idField.getText());
            ps.setString(2, nameField.getText());
            ps.setString(3, deptField.getText());
            ps.setString(4, assignField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Added!");
            loadTable();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void updateStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE student SET name=?, department=?, assignedEquipment=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setString(2, deptField.getText());
            ps.setString(3, assignField.getText());
            ps.setString(4, idField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Updated Successfully!");
            loadTable();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM student WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Deleted Successfully!");
            loadTable();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void fillFormFromTable() {
        int selected = table.getSelectedRow();
        if (selected == -1) return;
        idField.setText(model.getValueAt(selected, 0).toString());
        nameField.setText(model.getValueAt(selected, 1).toString());
        deptField.setText(model.getValueAt(selected, 2).toString());
        assignField.setText(model.getValueAt(selected, 3).toString());
    }
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        deptField.setText("");
        assignField.setText("");
    }
}
