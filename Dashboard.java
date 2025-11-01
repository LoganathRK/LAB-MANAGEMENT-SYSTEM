import java.awt.*;
import javax.swing.*;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Lab Management System - Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 20, 20));

        JButton equipmentBtn = new JButton("Manage Equipment");
        JButton studentBtn = new JButton("Manage Students");
        JButton exitBtn = new JButton("Exit");

        equipmentBtn.addActionListener(e -> {
            dispose();
            new EquipmentManager();
        });
        studentBtn.addActionListener(e -> {
            dispose();
            new StudentManager();
        });
        exitBtn.addActionListener(e -> System.exit(0));

        add(equipmentBtn);
        add(studentBtn);
        add(exitBtn);

        setVisible(true);
    }
}
