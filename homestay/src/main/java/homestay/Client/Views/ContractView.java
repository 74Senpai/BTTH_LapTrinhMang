package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ContractView extends javax.swing.JPanel implements Components.IViewCheck {

    private JTable tblContract;
    private DefaultTableModel tableModel;
    private Button btnAdd, btnEdit, btnDelete, btnRefresh, btnSave, btnCancel;

    private int editingRow = -1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Consumer<Object[]> onAddContract;
    private BiConsumer<Integer, Object[]> onUpdateContract;
    private Consumer<Integer> onDeleteContract;
    private Runnable onRefresh;

    public ContractView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        Label lblTitle = new Label("QUẢN LÝ HỢP ĐỒNG", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // 0: Thao tác, 1: Mã HD, 2: Tên khách, 3: SĐT, 4: CCCD, 5: Phòng, 6: Ngày BĐ, 7: Ngày KT, 8: Loại thuê
        String[] columns = {"Thao tác", "Mã HD", "Tên khách hàng", "Số điện thoại", "CCCD", "Phòng", "Ngày bắt đầu", "Ngày kết thúc", "Loại hình thuê"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép sửa khi đang edit
                if (row != editingRow) return false;
                // KHÔNG cho sửa: Thao tác(0), Mã HD(1), Ngày kết thúc(7 - tự tính)
                return column != 0 && column != 1 && column != 7;
            }
        };

        tblContract = new JTable(tableModel);
        tblContract.setRowHeight(35);
        tblContract.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // --- CẤU HÌNH CÁC DROPDOWN ---
        setupRoomComboBox();
        setupLeaseTypeComboBox();
        
        // --- LẮNG NGHE THAY ĐỔI ĐỂ TÍNH NGÀY ---
        setupDateCalculationListener();

        add(new JScrollPane(tblContract), BorderLayout.CENTER);

        // --- PANEL BUTTONS ---
        Panel pnlSouth = new Panel(new BorderLayout());
        Panel pnlLeft = new Panel(new FlowLayout(FlowLayout.LEFT));
        btnSave = new Button("LƯU"); btnCancel = new Button("HỦY");
        pnlLeft.add(btnSave); pnlLeft.add(btnCancel);

        Panel pnlRight = new Panel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new Button("Thêm hợp đồng"); btnEdit = new Button("Sửa"); 
        btnDelete = new Button("Xóa"); btnRefresh = new Button("Tải lại");
        pnlRight.add(btnAdd); pnlRight.add(btnEdit); pnlRight.add(btnDelete); pnlRight.add(btnRefresh);

        pnlSouth.add(pnlLeft, BorderLayout.WEST);
        pnlSouth.add(pnlRight, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- EVENTS ---
        btnAdd.addActionListener(e -> {
            if (editingRow != -1) return;
            String today = LocalDate.now().format(formatter);
            // Mặc định ban đầu là "Theo ngày" -> Ngày KT = Today + 1
            String endDate = LocalDate.now().plusDays(1).format(formatter);
            
            Object[] newRow = {"🆕 Thêm mới", null, "", "", "", "Chọn phòng...", today, endDate, "Theo ngày"};
            tableModel.addRow(newRow);
            editingRow = tableModel.getRowCount() - 1;
            updateUIState();
        });

        btnSave.addActionListener(e -> {
            if (tblContract.isEditing()) tblContract.getCellEditor().stopCellEditing();
            
            Object[] data = getRowDataForController(editingRow);
            String idStr = valueAt(editingRow, 1);

            if (idStr == null || idStr.isEmpty()) {
                if (onAddContract != null) onAddContract.accept(data);
            } else {
                if (onUpdateContract != null) onUpdateContract.accept(Integer.parseInt(idStr), data);
            }
            exitEditMode();
        });

        btnCancel.addActionListener(e -> {
            if (valueAt(editingRow, 1) == null) tableModel.removeRow(editingRow);
            exitEditMode();
            if (onRefresh != null) onRefresh.run();
        });

        btnEdit.addActionListener(e -> {
            int row = tblContract.getSelectedRow();
            if (row != -1) { editingRow = row; tableModel.setValueAt("📝 Sửa", row, 0); updateUIState(); }
        });

        btnRefresh.addActionListener(e -> { if (onRefresh != null) onRefresh.run(); exitEditMode(); });
        
        updateUIState();
    }

    private void setupRoomComboBox() {
        // Giả sử lấy danh sách tên phòng từ Controller
        // Set<String> roomNames = BaseDataController.getAvailableRoomNames(); 
        String[] demoRooms = {"Phòng 101", "Phòng 102", "Phòng 201", "Phòng 301"};
        JComboBox<String> cbRoom = new JComboBox<>(demoRooms);
        tblContract.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(cbRoom));
    }

    private void setupLeaseTypeComboBox() {
        String[] types = {"Theo ngày", "Theo tháng", "Dài hạn"};
        JComboBox<String> cbType = new JComboBox<>(types);
        tblContract.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(cbType));
    }

    private void setupDateCalculationListener() {
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            // Nếu thay đổi Loại hình thuê (8) hoặc Ngày bắt đầu (6)
            if (row == editingRow && (col == 8 || col == 6)) {
                calculateEndDate(row);
            }
        });
    }

    private void calculateEndDate(int row) {
        try {
            String startDateStr = valueAt(row, 6);
            String type = valueAt(row, 8);
            
            if (startDateStr == null || type == null) return;
            
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            String endDateStr = "";

            if ("Theo ngày".equals(type)) {
                endDateStr = startDate.plusDays(1).format(formatter);
            } else if ("Theo tháng".equals(type)) {
                endDateStr = startDate.plusDays(30).format(formatter);
            } else if ("Dài hạn".equals(type)) {
                endDateStr = "Vô thời hạn";
            }

            // Cập nhật vào cột Ngày kết thúc (7) mà không kích hoạt listener vô tận
            final String finalDate = endDateStr;
            SwingUtilities.invokeLater(() -> {
                tableModel.setValueAt(finalDate, row, 7);
            });
            
        } catch (Exception ex) {
            // Ngày không đúng định dạng yyyy-MM-dd
        }
    }

    private void updateUIState() {
        boolean editing = editingRow != -1;
        btnSave.setEnabled(editing); btnCancel.setEnabled(editing);
        btnAdd.setEnabled(!editing); btnEdit.setEnabled(!editing);
        btnRefresh.setEnabled(!editing);
    }

    private void exitEditMode() {
        if (editingRow != -1 && editingRow < tableModel.getRowCount()) 
            tableModel.setValueAt("✅", editingRow, 0);
        editingRow = -1;
        updateUIState();
    }

    private Object[] getRowDataForController(int row) {
        Object[] data = new Object[tableModel.getColumnCount() - 1];
        for (int i = 1; i < tableModel.getColumnCount(); i++) {
            data[i - 1] = tableModel.getValueAt(row, i);
        }
        return data;
    }

    private String valueAt(int row, int col) {
        Object v = tableModel.getValueAt(row, col);
        return v == null ? null : v.toString();
    }

    public void setContractData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            Object[] rowWithStatus = new Object[row.length + 1];
            rowWithStatus[0] = "✅";
            System.arraycopy(row, 0, rowWithStatus, 1, row.length);
            tableModel.addRow(rowWithStatus);
        }
    }

    public void setOnAddContract(Consumer<Object[]> cb) { this.onAddContract = cb; }
    public void setOnUpdateContract(BiConsumer<Integer, Object[]> cb) { this.onUpdateContract = cb; }
    public void setOnRefresh(Runnable cb) { this.onRefresh = cb; }

    @Override
    public boolean confirmBeforeSwitch() { return editingRow == -1; }
}