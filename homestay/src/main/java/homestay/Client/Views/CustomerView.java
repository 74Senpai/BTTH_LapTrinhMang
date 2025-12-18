package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * View quản lý khách thuê
 * CHỈ HIỂN THỊ + GỌI CONTROLLER
 */
public class CustomerView extends javax.swing.JPanel implements Components.IViewCheck {

    // ===== UI =====
    private JTable tblCustomer;
    private DefaultTableModel tableModel;

    private Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnRentRoom;
    private Button btnRefresh;

    private boolean isChanged = false;

    public CustomerView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        // ================== TITLE ==================
        Label lblTitle = new Label("QUẢN LÝ KHÁCH THUÊ", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        add(lblTitle, BorderLayout.NORTH);

        // ================== TABLE ==================
        String[] columns = {
                "Mã KH",
                "Họ tên",
                "SĐT",
                "CCCD",
                "Phòng đang thuê",
                "Ngày bắt đầu",
                "Ngày kết thúc"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Không cho sửa trực tiếp trên bảng
                return false;
            }
        };

        this.tblCustomer = new JTable(this.tableModel);
        this.tblCustomer.setRowHeight(32);
        this.tblCustomer.setFont(new Font("Arial", Font.PLAIN, 14));
        this.tblCustomer.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        this.tblCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Components.centerTable(this.tblCustomer);

        JScrollPane scrollPane = new JScrollPane(this.tblCustomer);
        add(scrollPane, BorderLayout.CENTER);

        // ================== BUTTONS ==================
        Panel pnlButtons = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        this.btnAdd = new Button("Thêm khách");
        this.btnEdit = new Button("Sửa thông tin");
        this.btnDelete = new Button("Xóa khách");
        this.btnRentRoom = new Button("Thuê / Trả phòng");
        this.btnRefresh = new Button("Tải lại");

        pnlButtons.add(this.btnAdd);
        pnlButtons.add(this.btnEdit);
        pnlButtons.add(this.btnDelete);
        pnlButtons.add(this.btnRentRoom);
        pnlButtons.add(this.btnRefresh);

        add(pnlButtons, BorderLayout.SOUTH);

        // ================== UI STATE ==================
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRentRoom.setEnabled(false);

        // Khi chọn dòng
        tblCustomer.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = tblCustomer.getSelectedRow() != -1;
            btnEdit.setEnabled(selected);
            btnDelete.setEnabled(selected);
            btnRentRoom.setEnabled(selected);
        });
    }

    // =====================================================
    // ================== PUBLIC METHODS ===================
    // =====================================================

    /**
     * Load dữ liệu khách từ Controller
     */
    public void setCustomerData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
        this.isChanged = false;
    }

    /**
     * Lấy khách đang chọn
     */
    public String getSelectedCustomerId() {
        int row = tblCustomer.getSelectedRow();
        if (row == -1) return null;
        return tableModel.getValueAt(row, 0).toString();
    }

    /**
     * Hiển thị thông báo
     */
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Xác nhận hành động
     */
    public boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(
                this,
                msg,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    // =====================================================
    // ================== CONTROLLER HOOK ==================
    // =====================================================

    public void addAddCustomerListener(ActionListener l) {
        btnAdd.addActionListener(l);
    }

    public void addEditCustomerListener(ActionListener l) {
        btnEdit.addActionListener(l);
    }

    public void addDeleteCustomerListener(ActionListener l) {
        btnDelete.addActionListener(l);
    }

    public void addRentRoomListener(ActionListener l) {
        btnRentRoom.addActionListener(l);
    }

    public void addRefreshListener(ActionListener l) {
        btnRefresh.addActionListener(l);
    }

    // =====================================================
    // ================== TAB CHECK ========================
    // =====================================================

    @Override
    public boolean confirmBeforeSwitch() {
        if (!this.isChanged) return true;

        JOptionPane.showMessageDialog(
                this,
                "Có thay đổi chưa được lưu!",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE
        );
        return false;
    }
}
