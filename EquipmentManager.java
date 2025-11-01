    import java.awt.*;
    import java.sql.*;
    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;

    public class EquipmentManager extends JFrame {
        private DefaultTableModel model;
        private JTable table;
        private JTextField idField, nameField, typeField, quantityField, statusField;

        public EquipmentManager() {
            setTitle("Equipment Manager");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Vertical input fields using BoxLayout
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            idField = new JTextField();
            nameField = new JTextField();
            typeField = new JTextField();
            quantityField = new JTextField();
            statusField = new JTextField();

            formPanel.add(createFormItem("ID", idField));
            formPanel.add(createFormItem("Name", nameField));
            formPanel.add(createFormItem("Type", typeField));
            formPanel.add(createFormItem("Quantity", quantityField));
            formPanel.add(createFormItem("Status", statusField));

            String[] columns = {"ID", "Name", "Type", "Quantity", "Status"};
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

            addBtn.addActionListener(e -> addEquipment());
            updateBtn.addActionListener(e -> updateEquipment());
            deleteBtn.addActionListener(e -> deleteEquipment());
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
                ResultSet rs = stmt.executeQuery("SELECT * FROM equipment")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getInt("quantity"),
                            rs.getString("status")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        private void addEquipment() {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO equipment (id, name, type, quantity, status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, idField.getText());
                ps.setString(2, nameField.getText());
                ps.setString(3, typeField.getText());
                ps.setInt(4, Integer.parseInt(quantityField.getText()));
                ps.setString(5, statusField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Equipment Added!");
                loadTable();
                clearForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        private void updateEquipment() {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE equipment SET name=?, type=?, quantity=?, status=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setString(2, typeField.getText());
                ps.setInt(3, Integer.parseInt(quantityField.getText()));
                ps.setString(4, statusField.getText());
                ps.setString(5, idField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Updated Successfully!");
                loadTable();
                clearForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        private void deleteEquipment() {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM equipment WHERE id=?";
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
            nameField.setText(model.getValueAt(selected, 0).toString());
            typeField.setText(model.getValueAt(selected, 0).toString());
            quantityField.setText(model.getValueAt(selected, 0).toString());
            statusField.setText(model.getValueAt(selected, 0).toString());
        }
        private void clearForm() {
            idField.setText("");
            nameField.setText("");
            typeField.setText("");
            quantityField.setText("");
            statusField.setText("");
        }
    }
