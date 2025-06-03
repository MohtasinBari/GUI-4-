package GUI;

import Entity.Product;
import Service.ProductService;
import Service.PaymentService;
import File.HistoryUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SkinCareShop extends JFrame {
    private ProductService productService = new ProductService();
    private PaymentService paymentService = new PaymentService();
    private List<Product> selected = new ArrayList<>();
    private JTextField totalField, phoneField, receivedField, changeField, customerIdField;
    private JComboBox<String> paymentCombo;

    private final Color pinkBg = new Color(255, 240, 245);
    private final Color pinkButton = new Color(255, 182, 193);
    private final Color pinkAccent = new Color(255, 105, 180);

    private ImageIcon background;
    private JPanel panel;

    public SkinCareShop() {
        setTitle("Skin Care Shop");
        setSize(700, 700);
        setLayout(new CardLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setIconImage(Toolkit.getDefaultToolkit().getImage("HelloKitty.jpg"));
        background = new ImageIcon("Meera.jpg");

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        int y = 0;

        JLabel welcome = new JLabel("Welcome to SkinCare Shop", JLabel.CENTER);
        welcome.setFont(new Font("Serif", Font.BOLD, 22));
        welcome.setForeground(pinkAccent);
        panel.add(welcome, gb(gbc, 0, y++, 2));

        panel.add(new JLabel("Username:"), gb(gbc, 0, y, 1));
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gb(gbc, 1, y++, 1));
        panel.add(new JLabel("Customer ID:"), gb(gbc, 0, y, 1));
        customerIdField = new JTextField(15);
        panel.add(customerIdField, gb(gbc, 1, y++, 1));
        panel.add(new JLabel("Password:"), gb(gbc, 0, y, 1));
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gb(gbc, 1, y++, 1));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(pinkButton);
        loginBtn.addActionListener(e -> {
            if (!new String(passwordField.getPassword()).equals("1234")) {
                JOptionPane.showMessageDialog(this, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = customerIdField.getText().trim();
            if (!id.equals("24-58305-2") && !id.equals("24-57849-2")) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "products");
        });
        panel.add(loginBtn, gb(gbc, 0, y++, 2));
        add(panel, "login");

       
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(pinkBg);
        JLabel shopTitle = new JLabel("WELCOME TO MY SKIN CARE SHOP", JLabel.CENTER);
        shopTitle.setFont(new Font("Serif", Font.BOLD, 20));
        shopTitle.setForeground(pinkAccent);
        productPanel.add(shopTitle, BorderLayout.NORTH);

        JPanel prodPanel = new JPanel(new GridLayout(0, 2));
        prodPanel.setBackground(pinkBg);

       
        productService.insertProduct(new Product("Facial Cleanser", "Beauty Of Joseon", 1200));
        productService.insertProduct(new Product("Hydrating Toner", "Some By Mi", 2100));
        productService.insertProduct(new Product("Vitamin C Serum", "Anua", 2100));
        productService.insertProduct(new Product("Moisturizer with SPF", "Beauty Of Joseon", 1500));
        productService.insertProduct(new Product("Cleansing Oil", "Dr. Klairs", 1900));
        productService.insertProduct(new Product("Eye Cream", "Axis-Y", 1800));
        productService.insertProduct(new Product("Sheet Mask", "Innisfree", 400));

        for (Product p : productService.getProducts()) {
            JButton btn = new JButton(p.getDisplayText());
            btn.setBackground(pinkButton);
            btn.addActionListener(e -> {
                selected.add(p);
                totalField.setText(String.format("%.2f", productService.calculateTotal(selected)));
            });
            prodPanel.add(btn);
        }

        productPanel.add(new JScrollPane(prodPanel), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(6, 2));
        bottom.setBackground(pinkBg);

        totalField = new JTextField(); totalField.setEditable(false);
        phoneField = new JTextField();
        receivedField = new JTextField();
        changeField = new JTextField(); changeField.setEditable(false);

        paymentCombo = new JComboBox<>(new String[]{"Bkash/Nagad", "Card"});

        bottom.add(new JLabel("Total:")); bottom.add(totalField);
        bottom.add(new JLabel("Payment Number:")); bottom.add(phoneField);
        bottom.add(new JLabel("Payment Method:")); bottom.add(paymentCombo);
        bottom.add(new JLabel("Amount Received:")); bottom.add(receivedField);
        bottom.add(new JLabel("Change:")); bottom.add(changeField);

        JButton payBtn = new JButton("Pay");
        payBtn.setBackground(pinkButton);
        payBtn.addActionListener(e -> {
            String number = phoneField.getText().trim();
            double received = Double.parseDouble(receivedField.getText().trim());
            double total = productService.calculateTotal(selected);
            String method = paymentCombo.getSelectedItem().toString();

            boolean isValid = method.equals("Card") ? paymentService.validateCard(number)
                    : paymentService.validateBkashNagad(number);

            if (!isValid) {
                JOptionPane.showMessageDialog(this, "Invalid " + method + " number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (received < total) {
                JOptionPane.showMessageDialog(this, "Insufficient amount received.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double change = received - total;
            changeField.setText(String.format("%.2f", change));
            HistoryUtil.saveHistory(customerIdField.getText().trim(), method, selected, total);
            JOptionPane.showMessageDialog(this, "Payment successful! Thank you.");
        });

        bottom.add(payBtn);
        productPanel.add(bottom, BorderLayout.SOUTH);
        add(productPanel, "products");
    }

    private GridBagConstraints gb(GridBagConstraints gbc, int x, int y, int w) {
        gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = w;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
}
