package homestay.Client.Views;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import homestay.DTOs.DienNuocDTO;

public class UtilityBillingView extends javax.swing.JPanel implements Components.IViewCheck {

    private JTable tblData;
    private DefaultTableModel tableModel;
    private Label lblGrandTotal;

    private Button btnAdd, btnEdit, btnDelete, btnRefresh, btnSave, btnCancel;

    private int editingRow = -1;
    private final NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Callbacks cho Setup
    private Consumer<Object[]> onAddRecord;
    private BiConsumer<Integer, Object[]> onUpdateRecord;
    private Consumer<Integer> onDeleteRecord;
    private Runnable onRefresh;

    public UtilityBillingView() {
        initUI();
        setupAutoCalculation();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        Label lblTitle = new Label("QUẢN LÝ ĐIỆN NƯỚC", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // --- Cấu hình Table ---
        // 0:Thao tác, 1:ID, 2:Mã Phòng, 3:Tháng, 4:Năm, 5:Điện Cũ, 6:Điện Mới, 7:T.Thụ Đ, 8:Nước Cũ, 9:Nước Mới, 10:T.Thụ N, 11:Tổng
        String[] columns = {"Thao tác", "ID", "Mã Phòng", "Tháng", "Năm", "Điện Cũ", "Điện Mới", "T.Thụ Đ", "Nước Cũ", "Nước Mới", "T.Thụ N", "Thành tiền"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (row != editingRow) {
                    return false;
                }
                // Nếu là dòng mới (ID null), cho sửa Mã Phòng (2). 
                // Luôn cho sửa Điện mới (6) và Nước mới (9).
                Object id = getValueAt(row, 1);
                if (id == null && col == 2) {
                    return true;
                }
                return col == 6 || col == 9;
            }
        };

        tblData = new JTable(tableModel);
        tblData.setRowHeight(35);
        tblData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblData.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // validate 
        TableColumn colDienMoi = tblData.getColumnModel().getColumn(6);
        TableColumn colNuocMoi = tblData.getColumnModel().getColumn(9);
        colDienMoi.setCellEditor(new Components.NumericCellEditor());
        colNuocMoi.setCellEditor(new Components.NumericCellEditor());


        Components.centerTable(tblData);
        add(new JScrollPane(tblData), BorderLayout.CENTER);

        // --- Panel Buttons (South) ---
        Panel pnlSouth = new Panel(new BorderLayout());

        Panel pnlLeft = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnSave = new Button("LƯU THAY ĐỔI");
        btnCancel = new Button("HỦY BỎ");
        btnSave.setForeground(new Color(0, 100, 0));
        btnCancel.setForeground(new Color(150, 0, 0));
        pnlLeft.add(btnSave);
        pnlLeft.add(btnCancel);

        Panel pnlRight = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        lblGrandTotal = new Label("Tổng: 0 ₫");
        lblGrandTotal.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd = new Button("Ghi số mới");
        btnEdit = new Button("Sửa số");
        btnDelete = new Button("Xóa");
        btnRefresh = new Button("Tải lại");
        pnlRight.add(lblGrandTotal);
        pnlRight.add(btnAdd);
        pnlRight.add(btnEdit);
        pnlRight.add(btnDelete);
        pnlRight.add(btnRefresh);

        pnlSouth.add(pnlLeft, BorderLayout.WEST);
        pnlSouth.add(pnlRight, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- Gán sự kiện ---
        btnRefresh.addActionListener(e -> {
            exitEditMode();
            if (onRefresh != null) {
                onRefresh.run();
            }
        });

        btnAdd.addActionListener(e -> {
            if (editingRow != -1) {
                return;
            }
            LocalDate now = LocalDate.now();
            Object[] newRow = {"🆕 Đang thêm...", null, "", now.getMonthValue(), now.getYear(), 0, 0, 0, 0, 0, 0, cur.format(0)};
            tableModel.addRow(newRow);
            editingRow = tableModel.getRowCount() - 1;
            tblData.setRowSelectionInterval(editingRow, editingRow);
            updateUIState();
        });

        btnEdit.addActionListener(e -> {
            int row = tblData.getSelectedRow();
            if (row == -1) {
                return;
            }
            editingRow = row;
            tableModel.setValueAt("📝 Đang sửa...", row, 0);
            updateUIState();
        });

        btnCancel.addActionListener(e -> {
            if (editingRow == -1) {
                return;
            }
            if (valueAt(editingRow, 1) == null) {
                tableModel.removeRow(editingRow);
            }
            exitEditMode();
            if (onRefresh != null) {
                onRefresh.run();
            }
        });

        btnSave.addActionListener(e -> {
            if (editingRow == -1) {
                return;
            }
            if (tblData.isEditing()) {
                tblData.getCellEditor().stopCellEditing();
            }

            Object[] data = getRowDataForController(editingRow);
            String idStr = valueAt(editingRow, 1);

            if (idStr == null || idStr.isEmpty()) {
                if (onAddRecord != null) {
                    onAddRecord.accept(data);
                }
            } else {
                if (onUpdateRecord != null) {
                    onUpdateRecord.accept(Integer.parseInt(idStr), data);
                }
            }
            exitEditMode();
        });

        btnDelete.addActionListener(e -> {
            int row = tblData.getSelectedRow();
            if (row == -1 || editingRow != -1) {
                return;
            }
            int id = Integer.parseInt(valueAt(row, 1));
            if (confirm("Xóa bản ghi điện nước này?")) {
                if (onDeleteRecord != null) {
                    onDeleteRecord.accept(id);
                }
            }
        });

        tblData.getSelectionModel().addListSelectionListener(e -> updateUIState());
        updateUIState();
    }

    private void setupAutoCalculation() {
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                if (c == 6 || c == 9) { // Khi sửa Điện mới hoặc Nước mới
                    try {
                        int dCu = Integer.parseInt(valueAt(r, 5));
                        int dMoi = Integer.parseInt(valueAt(r, 6));
                        int nCu = Integer.parseInt(valueAt(r, 8));
                        int nMoi = Integer.parseInt(valueAt(r, 9));

                        int tD = Math.max(0, dMoi - dCu);
                        int tN = Math.max(0, nMoi - nCu);
                        double total = (tD * 3500) + (tN * 12000);

                        tableModel.setValueAt(tD, r, 7);
                        tableModel.setValueAt(tN, r, 10);
                        tableModel.setValueAt(cur.format(total), r, 11);
                        updateGrandLabel();
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }

    private void updateGrandLabel() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String s = valueAt(i, 11).replaceAll("[^0-9]", "");
            if (!s.isEmpty()) {
                total += Double.parseDouble(s);
            }
        }
        lblGrandTotal.setText("Tổng: " + cur.format(total));
    }

    // --- Helpers giống RoomView ---
    public void setData(java.util.List<DienNuocDTO.View> list) {
        tableModel.setRowCount(0);
        for (DienNuocDTO.View d : list) {
            double sum = (d.soDienTieuThu() * 3500) + (d.soNuocTieuThu() * 12000);
            tableModel.addRow(new Object[]{"✅", d.maDienNuoc(), d.maPhong(), d.thang(), d.nam(), d.chiSoDienCu(), d.chiSoDienMoi(), d.soDienTieuThu(), d.chiSoNuocCu(), d.chiSoNuocMoi(), d.soNuocTieuThu(), cur.format(sum)});
        }
        updateGrandLabel();
        exitEditMode();
    }

    private void updateUIState() {
        boolean isEditing = (editingRow != -1);
        boolean hasSelection = tblData.getSelectedRow() != -1;
        btnSave.setEnabled(isEditing);
        btnCancel.setEnabled(isEditing);
        btnAdd.setEnabled(!isEditing);
        btnEdit.setEnabled(!isEditing && hasSelection);
        btnDelete.setEnabled(!isEditing && hasSelection);
        btnRefresh.setEnabled(!isEditing);
    }

    private void exitEditMode() {
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

    private String valueAt(int r, int c) {
        Object v = tableModel.getValueAt(r, c);
        return v == null ? "" : v.toString();
    }

    public void setOnAdd(Consumer<Object[]> cb) {
        this.onAddRecord = cb;
    }

    public void setOnUpdate(BiConsumer<Integer, Object[]> cb) {
        this.onUpdateRecord = cb;
    }

    public void setOnDelete(Consumer<Integer> cb) {
        this.onDeleteRecord = cb;
    }

    public void setOnRefresh(Runnable cb) {
        this.onRefresh = cb;
    }

    public boolean confirm(String m) {
        return JOptionPane.showConfirmDialog(this, m, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean confirmBeforeSwitch() {
        return editingRow == -1 || confirm("Dữ liệu chưa lưu. Rời đi?");
    }
}
