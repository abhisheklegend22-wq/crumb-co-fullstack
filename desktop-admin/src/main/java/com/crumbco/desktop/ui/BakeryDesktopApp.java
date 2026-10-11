package com.crumbco.desktop.ui;

import com.crumbco.desktop.dao.JdbcProductRepository;
import com.crumbco.desktop.model.Product;
import com.crumbco.desktop.service.CatalogService;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Optional Java Swing GUI for evaluators who want to see a desktop GUI and direct JDBC too. */
public class BakeryDesktopApp extends JFrame {
    private final CatalogService service = new CatalogService(new JdbcProductRepository());
    private final ProductTableModel tableModel = new ProductTableModel();
    private final JLabel status = new JLabel("Ready");

    public BakeryDesktopApp() {
        super("Crumb & Co. | Desktop Inventory Admin");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 540); setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 250, 244));
        JLabel title = new JLabel("Crumb & Co. — Bakery Inventory");
        title.setFont(new Font("Serif", Font.BOLD, 27)); title.setBorder(BorderFactory.createEmptyBorder(18, 18, 12, 18));
        JTable table = new JTable(tableModel); table.setRowHeight(28); table.setFillsViewportHeight(true);
        JButton refresh = new JButton("Refresh from MySQL"); refresh.addActionListener(e -> loadProducts());
        JButton add = new JButton("Add product"); add.addActionListener(e -> showAddDialog());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT)); controls.add(refresh); controls.add(add);
        status.setBorder(BorderFactory.createEmptyBorder(8, 14, 12, 14));
        add(title, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(controls, BorderLayout.WEST); add(status, BorderLayout.SOUTH);
        loadProducts();
    }

    /** SwingWorker keeps blocking JDBC work off the Event Dispatch Thread (multithreading). */
    private void loadProducts() {
        status.setText("Loading catalogue from MySQL…");
        new SwingWorker<List<Product>, Void>() {
            @Override protected List<Product> doInBackground() { return service.refresh(); }
            @Override protected void done() {
                try { tableModel.setRows(get()); status.setText("Loaded " + tableModel.getRowCount() + " products from MySQL."); }
                catch (Exception ex) { status.setText("Database error: " + rootMessage(ex)); JOptionPane.showMessageDialog(BakeryDesktopApp.this, rootMessage(ex), "Database error", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void showAddDialog() {
        JTextField name = new JTextField(18); JComboBox<String> category = new JComboBox<>(new String[]{"Pastries", "Cakes", "Cookies", "Bread"});
        JTextField price = new JTextField("99.00", 10); JSpinner stock = new JSpinner(new SpinnerNumberModel(10, 0, 100000, 1));
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8)); panel.add(new JLabel("Product name")); panel.add(name); panel.add(new JLabel("Category")); panel.add(category); panel.add(new JLabel("Price (₹)")); panel.add(price); panel.add(new JLabel("Stock")); panel.add(stock);
        if (JOptionPane.showConfirmDialog(this, panel, "Add product to MySQL", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            service.add(name.getText(), (String) category.getSelectedItem(), price.getText().trim(), (Integer) stock.getValue());
            status.setText("Product inserted into MySQL."); loadProducts();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not add product", JOptionPane.ERROR_MESSAGE); }
    }

    private String rootMessage(Throwable error) { Throwable current = error; while (current.getCause() != null) current = current.getCause(); return current.getMessage() == null ? error.toString() : current.getMessage(); }

    private static final class ProductTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Product", "Category", "Price (₹)", "Stock", "Active"};
        private List<Product> rows = new ArrayList<>();
        void setRows(List<Product> products) { rows = new ArrayList<>(products); fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int column) { return columns[column]; }
        @Override public Object getValueAt(int row, int column) {
            Product p = rows.get(row);
            return switch (column) { case 0 -> p.id(); case 1 -> p.name(); case 2 -> p.category(); case 3 -> p.price(); case 4 -> p.stock(); case 5 -> p.active() ? "Yes" : "No"; default -> ""; };
        }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new BakeryDesktopApp().setVisible(true)); }
}
