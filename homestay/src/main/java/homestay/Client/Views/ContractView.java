package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ContractView extends JPanel implements Components.IViewCheck {

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

        JLabel lblTitle = new JLabel("QUẢN LÝ HỢP ĐỒNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Thao tác", "Mã HD", "Tên khách hàng", "Số điện thoại", "CCCD", "Phòng (Mã - Tên)", "Ngày bắt đầu", "Ngày kết thúc", "Loại hình thuê"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (row != editingRow) {
                    return false;
                }
                return column != 0 && column != 1 && column != 7;
            }
        };
        
        tblContract = new JTable(tableModel);
        tblContract.setRowHeight(35);
        tblContract.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        // Validate 
        TableColumn colPhoneNumber = tblContract.getColumnModel().getColumn(3);
        TableColumn colCCCD = tblContract.getColumnModel().getColumn(4);
        TableColumn colDayBegin = tblContract.getColumnModel().getColumn(6);
        colPhoneNumber.setCellEditor(new Components.PhoneNumberCellEditor());
        colCCCD.setCellEditor(new Components.CCCDCellEditor());
        colDayBegin.setCellEditor(new Components.DateCellEditor());
        

        setupLeaseTypeComboBox();
        Components.centerTable(tblContract);
        add(new JScrollPane(tblContract), BorderLayout.CENTER);

        // --- BUTTONS (Dùng Swing Button) ---
        JPanel pnlSouth = new JPanel(new BorderLayout());
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSave = new Button("LƯU THAY ĐỔI");
        btnCancel = new Button("HỦY");
        btnSave.setForeground(new Color(0, 100, 0));
        btnCancel.setForeground(new Color(150, 0, 0));
        pnlLeft.add(btnSave);
        pnlLeft.add(btnCancel);

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new Button("Thêm mới");
        btnEdit = new Button("Sửa");
        btnDelete = new Button("Xóa");
        btnRefresh = new Button("Tải lại");
        pnlRight.add(btnAdd);
        pnlRight.add(btnEdit);
        pnlRight.add(btnDelete);
        pnlRight.add(btnRefresh);

        pnlSouth.add(pnlLeft, BorderLayout.WEST);
        pnlSouth.add(pnlRight, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- ACTIONS ---
        btnAdd.addActionListener(e -> {
            if (editingRow != -1) {
                return;
            }
            String today = LocalDate.now().format(formatter);
            Object[] newRow = {"🆕 Thêm", null, "", "", "", "Chọn phòng...", today, today, "Ngày"};
            tableModel.addRow(newRow);
            editingRow = tableModel.getRowCount() - 1;
            startEditing(editingRow, 2);
            updateUIState();
        });

        btnEdit.addActionListener(e -> {
            int row = tblContract.getSelectedRow();
            if (row != -1) {
                editingRow = row;
                tableModel.setValueAt("📝 Sửa", row, 0);
                startEditing(row, 2);
                updateUIState();
            }
        });

        btnSave.addActionListener(e -> {
            if (tblContract.isEditing()) {
                tblContract.getCellEditor().stopCellEditing();
            }

            Object[] data = getRowDataForController(editingRow);
            String idStr = valueAt(editingRow, 1);

            if (idStr == null || idStr.isEmpty()) {
                if (onAddContract != null) {
                    onAddContract.accept(data);
                }
            } else {
                if (onUpdateContract != null) {
                    onUpdateContract.accept(Integer.parseInt(idStr), data);
                }
            }
            exitEditMode();
        });

        btnCancel.addActionListener(e -> {
            if (editingRow != -1 && (valueAt(editingRow, 1) == null)) {
                tableModel.removeRow(editingRow);
            }
            exitEditMode();
            if (onRefresh != null) {
                onRefresh.run();
            }
        });

        btnRefresh.addActionListener(e -> {
            if (onRefresh != null) {
                onRefresh.run();

            }
            exitEditMode();
        });

        btnDelete.addActionListener(e -> {
            int row = tblContract.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng muốn xóa!");
                return;
            }

            String idStr = valueAt(row, 1); // Cột 1 là Mã HD

            // Nếu là dòng đang thêm mới (chưa có ID)
            if (idStr == null || idStr.isEmpty()) {
                tableModel.removeRow(row);
                exitEditMode();
                return;
            }

            // Nếu là dữ liệu đã có trong DB
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa hợp đồng mã số " + idStr + " không?",
                    "Xác nhận xóa",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                if (onDeleteContract != null) {
                    onDeleteContract.accept(Integer.parseInt(idStr));
                }
            }
        });

        setupDateCalculationListener();
        updateUIState();
    }

    // Hàm nhận mảng String[] nguyên thủy từ bên ngoài
    public void setRoomList(String[] roomStrings) {
        JComboBox<String> cbRoom = new JComboBox<>(roomStrings);
        tblContract.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(cbRoom));
    }

    private void setupLeaseTypeComboBox() {
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Ngày", "Tháng"});
        tblContract.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(cbType));
    }

    private void startEditing(int row, int col) {
        tblContract.requestFocusInWindow();
        tblContract.editCellAt(row, col);
        updateUIState();
    }

    private void setupDateCalculationListener() {
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row == editingRow && (e.getColumn() == 8 || e.getColumn() == 6)) {
                calculateEndDate(row);
            }
        });
    }

    private void calculateEndDate(int row) {
        try {
            LocalDate start = LocalDate.parse(valueAt(row, 6), formatter);
            String type = valueAt(row, 8);
            String end = type.equals("Ngày") ? start.plusDays(1).format(formatter) : start.plusDays(30).format(formatter);
            SwingUtilities.invokeLater(() -> tableModel.setValueAt(end, row, 7));
        } catch (Exception ignored) {
        }
    }

    private void updateUIState() {
        boolean isEdit = (editingRow != -1);
        btnSave.setEnabled(isEdit);
        btnCancel.setEnabled(isEdit);
        btnAdd.setEnabled(!isEdit);
        btnEdit.setEnabled(!isEdit);
        btnRefresh.setEnabled(!isEdit);
        btnDelete.setEnabled(!isEdit);
    }

    public void exitEditMode() {
        if (editingRow != -1 && editingRow < tableModel.getRowCount()) {
            tableModel.setValueAt("✅", editingRow, 0);
        }
        editingRow = -1;
        updateUIState();
    }

    public void setContractData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            Object[] fullRow = new Object[row.length + 1];
            fullRow[0] = "✅";
            System.arraycopy(row, 0, fullRow, 1, row.length);
            tableModel.addRow(fullRow);
        }
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

    public void setOnAddContract(Consumer<Object[]> cb) {
        this.onAddContract = cb;
    }

    public void setOnUpdateContract(BiConsumer<Integer, Object[]> cb) {
        this.onUpdateContract = cb;
    }

    public void setOnRefresh(Runnable cb) {
        this.onRefresh = cb;
    }

    public void setOnDeleteContract(Consumer<Integer> cb) {
        this.onDeleteContract = cb;
    }

    @Override
    public boolean confirmBeforeSwitch() {
        return editingRow == -1;
    }
}
