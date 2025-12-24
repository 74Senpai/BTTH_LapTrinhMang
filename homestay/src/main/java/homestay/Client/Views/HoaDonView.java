package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import homestay.DTOs.HoaDonDTO;

public class HoaDonView extends JPanel implements Components.IViewCheck {

    private JTable tbl;
    private DefaultTableModel tableModel;
    private Label lblTotal;
    private Button btnAdd, btnEdit, btnDelete, btnRefresh, btnSave, btnCancel;

    private int editingRow = -1;
    private boolean isChanged = false;
    private final NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    private Consumer<Object[]> onAdd;
    private BiConsumer<Integer, Object[]> onUpdate;
    private Consumer<Integer> onDelete;
    private Runnable onRefresh;

    public HoaDonView() {
        initUI();
        setupTableEditor();
        setupAutoCalculation();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        // --- Tiêu đề (North) ---
        Label lblTitle = new Label("QUẢN LÝ HÓA ĐƠN & DOANH THU", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // --- Cấu hình Table (Center) ---
        // 0:Thao tác, 1:ID, 2:Mã HĐ, 3:Khách, 4:Tiền phòng, 5:Phụ phí, 6:Tổng tiền, 7:Ngày, 8:Trạng thái
        String[] columns = {"Thao tác", "ID", "Mã HĐ", "Khách hàng", "Tiền phòng", "Phụ phí", "Tổng tiền", "Ngày", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (row != editingRow) {
                    return false;
                }
                // Nếu là dòng mới (ID null), cho sửa Mã Hợp đồng (2)
                if (getValueAt(row, 1) == null && col == 2) {
                    return true;
                }
                // Cho phép sửa Phụ phí (5) và Trạng thái (8)
                return col == 5 || col == 8;
            }
        };

        tbl = new JTable(tableModel);
        tbl.setRowHeight(35);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        //validate 
        TableColumn colPhuPhi = tbl.getColumnModel().getColumn(5);
        colPhuPhi.setCellEditor(new Components.NumericCellEditor());

        Components.centerTable(tbl);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        // --- Panel Buttons (South) ---
        Panel pnlSouth = new Panel(new BorderLayout());

        // Bên trái: Lưu & Hủy
        Panel pnlLeftActions = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnSave = new Button("LƯU THAY ĐỔI");
        btnCancel = new Button("HỦY BỎ");
        btnSave.setForeground(new Color(0, 100, 0));
        btnCancel.setForeground(new Color(150, 0, 0));
        pnlLeftActions.add(btnSave);
        pnlLeftActions.add(btnCancel);

        // Bên phải: Các chức năng chính & Tổng tiền
        Panel pnlRightActions = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        lblTotal = new Label("Tổng: 0 ₫");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

        btnAdd = new Button("Tạo hóa đơn");
        btnEdit = new Button("Sửa");
        btnDelete = new Button("Xóa");
        btnRefresh = new Button("Tải lại");


        pnlRightActions.add(lblTotal);
        pnlRightActions.add(btnAdd);
        pnlRightActions.add(btnEdit);
        pnlRightActions.add(btnDelete);
        pnlRightActions.add(btnRefresh);

        pnlSouth.add(pnlLeftActions, BorderLayout.WEST);
        pnlSouth.add(pnlRightActions, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        setupActionListeners();
        updateUIState();
    }

    private void setupTableEditor() {
        TableColumn statusCol = tbl.getColumnModel().getColumn(8);
        JComboBox<String> combo = new JComboBox<>(new String[]{"Chưa thanh toán", "Đã thanh toán"});
        statusCol.setCellEditor(new DefaultCellEditor(combo));
    }

    private void setupAutoCalculation() {
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                int r = e.getFirstRow();
                try {
                    double p = Double.parseDouble(valueAt(r, 4).replaceAll("[^0-9.]", ""));
                    double f = Double.parseDouble(valueAt(r, 5).replaceAll("[^0-9.]", ""));
                    tableModel.setValueAt(cur.format(p + f), r, 6);
                    updateGrandTotalLabel();
                } catch (Exception ex) {
                }
            }
        });
    }

    private void setupActionListeners() {
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
            tableModel.addRow(new Object[]{"🆕 Đang thêm...", null, "", "Khách mới", cur.format(0), 0, cur.format(0), "Vừa xong", "Chưa thanh toán"});
            editingRow = tableModel.getRowCount() - 1;
            tbl.setRowSelectionInterval(editingRow, editingRow);
            updateUIState();
        });

        btnEdit.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r == -1) {
                return;
            }
            editingRow = r;
            tableModel.setValueAt("📝 Đang sửa...", r, 0);
            updateUIState();
        });

        btnSave.addActionListener(e -> {
            if (editingRow == -1) {
                return;
            }
            if (tbl.isEditing()) {
                tbl.getCellEditor().stopCellEditing();
            }

            Object[] data = getRowData(editingRow);
            String idStr = valueAt(editingRow, 1);

            if (idStr == null || idStr.isEmpty()) {
                if (onAdd != null) {
                    onAdd.accept(data);
                }
            } else {
                if (onUpdate != null) {
                    onUpdate.accept(Integer.parseInt(idStr), data);
                }
            }
            exitEditMode();
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

        btnDelete.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r == -1 || editingRow != -1) {
                return;
            }
            int id = Integer.parseInt(valueAt(r, 1));
            if (confirm("Xóa hóa đơn này?")) {
                if (onDelete != null) {
                    onDelete.accept(id);
                }
            }
        });

        tbl.getSelectionModel().addListSelectionListener(e -> updateUIState());
    }

    public void setHoaDonData(List<HoaDonDTO.View> list) {
        tableModel.setRowCount(0);
        for (HoaDonDTO.View h : list) {
            tableModel.addRow(new Object[]{
                "✅", h.maThanhToan(), h.maHopDong(), h.tenKhachHang(),
                cur.format(h.tienPhong()), h.tienChiPhiPhu(), cur.format(h.tongTien()),
                h.ngayThanhToan(), h.trangThaiThanhToan() == 1 ? "Đã thanh toán" : "Chưa thanh toán"
            });
        }
        updateGrandTotalLabel();
        exitEditMode();
    }

    private void updateGrandTotalLabel() {
        double sum = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String s = valueAt(i, 6).replaceAll("[^0-9]", "");
            if (!s.isEmpty()) {
                sum += Double.parseDouble(s);
            }
        }
        lblTotal.setText("Tổng doanh thu: " + cur.format(sum));
    }

    private void updateUIState() {
        boolean ed = (editingRow != -1);
        boolean hasSel = tbl.getSelectedRow() != -1;

        btnSave.setEnabled(ed);
        btnCancel.setEnabled(ed);
        btnAdd.setEnabled(!ed);
        btnEdit.setEnabled(!ed && hasSel);
        btnDelete.setEnabled(!ed && hasSel);
        btnRefresh.setEnabled(!ed);
        isChanged = ed;
    }

    private void exitEditMode() {
        editingRow = -1;
        updateUIState();
    }

    private Object[] getRowData(int r) {
        Object[] d = new Object[tableModel.getColumnCount() - 1];
        for (int i = 1; i < tableModel.getColumnCount(); i++) {
            d[i - 1] = tableModel.getValueAt(r, i);
        }
        return d;
    }

    private String valueAt(int r, int c) {
        Object v = tableModel.getValueAt(r, c);
        return v == null ? "" : v.toString();
    }

    public void setOnAdd(Consumer<Object[]> cb) {
        this.onAdd = cb;
    }

    public void setOnUpdate(BiConsumer<Integer, Object[]> cb) {
        this.onUpdate = cb;
    }

    public void setOnDelete(Consumer<Integer> cb) {
        this.onDelete = cb;
    }

    public void setOnRefresh(Runnable cb) {
        this.onRefresh = cb;
    }

    private boolean confirm(String m) {
        return JOptionPane.showConfirmDialog(this, m, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean confirmBeforeSwitch() {
        if (editingRow == -1) {
            return true;
        }
        return confirm("Dữ liệu đang sửa chưa lưu. Vẫn muốn rời đi?");
    }
}
