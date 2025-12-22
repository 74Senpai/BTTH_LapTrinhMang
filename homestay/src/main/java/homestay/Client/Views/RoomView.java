package homestay.Client.Views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import homestay.Client.Controllers.BaseDataController;

public class RoomView extends javax.swing.JPanel implements Components.IViewCheck {

    private JTable tblRoom;
    private DefaultTableModel tableModel;

    private Button btnAdd, btnEdit, btnDelete, btnRefresh, btnSave, btnCancel;

    private boolean isChanged = false;
    private int editingRow = -1;

    private Consumer<Object[]> onAddRoom;
    private BiConsumer<Integer, Object[]> onUpdateRoom;
    private Consumer<Integer> onDeleteRoom;
    private Runnable onRefresh;

    public RoomView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250));

        Label lblTitle = new Label("QUẢN LÝ PHÒNG", Label.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // --- CẤU HÌNH CỘT ---
        // 0: Thao tác, 1: Mã phòng, 2: Tên phòng, 3: Trạng thái, 4: Giá/ngày, 5: Giá/tháng, 6: Số điện, 7: Số nước
        String[] columns = {"Thao tác", "Mã phòng", "Tên phòng", "Trạng thái", "Giá/ngày", "Giá/tháng", "Số điện", "Số nước"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // ĐIỀU KIỆN SỬA:
                // 1. Phải là hàng đang được nhấn nút "Sửa" (row == editingRow)
                // 2. Chỉ cho phép sửa Tên phòng (2), Trạng thái (3), Giá ngày (4), Giá tháng (5)
                // 3. KHÔNG cho sửa: Thao tác (0), Mã (1), Số điện (6), Số nước (7)
                return row == editingRow && (column >= 2 && column <= 5);
            }
        };

        tblRoom = new JTable(tableModel);
        tblRoom.setRowHeight(35);
        tblRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRoom.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // Cấu hình Dropdown cho cột Trạng thái (Index 3)
        setupStatusComboBox();

        Components.centerTable(tblRoom);
        add(new JScrollPane(tblRoom), BorderLayout.CENTER);

        // --- PANEL BUTTONS (SOUTH) ---
        Panel pnlSouth = new Panel(new BorderLayout());

        // Bên trái: Lưu/Hủy
        Panel pnlLeftActions = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnSave = new Button("LƯU THAY ĐỔI");
        btnCancel = new Button("HỦY BỎ");
        btnSave.setForeground(new Color(0, 100, 0));
        btnCancel.setForeground(new Color(150, 0, 0));
        pnlLeftActions.add(btnSave);
        pnlLeftActions.add(btnCancel);

        // Bên phải: Chức năng chính
        Panel pnlRightActions = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnAdd = new Button("Thêm phòng");
        btnEdit = new Button("Sửa");
        btnDelete = new Button("Xóa");
        btnRefresh = new Button("Tải lại");
        pnlRightActions.add(btnAdd);
        pnlRightActions.add(btnEdit);
        pnlRightActions.add(btnDelete);
        pnlRightActions.add(btnRefresh);

        pnlSouth.add(pnlLeftActions, BorderLayout.WEST);
        pnlSouth.add(pnlRightActions, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- ACTIONS ---
        btnRefresh.addActionListener(e -> {
            if (isChanged && !confirm("Dữ liệu chưa lưu sẽ bị mất, vẫn tải lại?")) {
                return;
            }
            exitEditMode();
            if (onRefresh != null) {
                onRefresh.run();
            }
        });

        btnAdd.addActionListener(e -> {
            if (editingRow != -1) {
                return;
            }
            Set<String> states = BaseDataController.getCachedRoomStates().getAllStateNames();
            String defaultState = states.isEmpty() ? "" : states.iterator().next();

            // Thêm hàng mới với giá trị mặc định cho điện nước là 0
            Object[] newRow = {"🆕 Đang thêm...", null, "Phòng mới", defaultState, 0, 0, 0, 0};
            tableModel.addRow(newRow);
            editingRow = tableModel.getRowCount() - 1;
            tblRoom.setRowSelectionInterval(editingRow, editingRow);
            updateUIState();
        });

        btnEdit.addActionListener(e -> {
            int row = tblRoom.getSelectedRow();
            if (row == -1) {
                return;
            }
            editingRow = row;
            tableModel.setValueAt("📝 Đang sửa...", row, 0);
            updateUIState();
        });

        btnSave.addActionListener(e -> {
            if (editingRow == -1) {
                return;
            }
            if (tblRoom.isEditing()) {
                tblRoom.getCellEditor().stopCellEditing();
            }

            Object[] data = getRowDataForController(editingRow);

            String idStr = valueAt(editingRow, 1);

            if (idStr == null || idStr.isEmpty()) {
                if (onAddRoom != null) {
                    onAddRoom.accept(data);
                }
            } else {
                try {
                    int roomId = Integer.parseInt(idStr);
                    if (onUpdateRoom != null) {
                        onUpdateRoom.accept(roomId, data);
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("ID không hợp lệ: " + idStr);
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
            int row = tblRoom.getSelectedRow();
            if (row == -1 || editingRow != -1) {
                return;
            }
            int roomId = Integer.parseInt(valueAt(row, 1));
            if (confirm("Xóa phòng này?")) {
                if (onDeleteRoom != null && roomId >= 0) {
                    onDeleteRoom.accept(roomId);
                }
                tableModel.removeRow(row);
            }
        });

        tblRoom.getSelectionModel().addListSelectionListener(e -> updateUIState());
        updateUIState();
    }

    private void setupStatusComboBox() {
        try {
            TableColumn statusColumn = tblRoom.getColumnModel().getColumn(3);
            Set<String> statesSet = new BaseDataController().getRoomStates().getAllStateNames();
            JComboBox<String> cbStatus = new JComboBox<>(statesSet.toArray(new String[0]));
            statusColumn.setCellEditor(new DefaultCellEditor(cbStatus));
        } catch (Exception e) {
            Components.showError(this, e.getMessage());
        }
    }

    private void updateUIState() {
        boolean isEditing = (editingRow != -1);
        boolean hasSelection = tblRoom.getSelectedRow() != -1;

        btnSave.setEnabled(isEditing);
        btnCancel.setEnabled(isEditing);

        btnAdd.setEnabled(!isEditing);
        btnEdit.setEnabled(!isEditing && hasSelection);
        btnDelete.setEnabled(!isEditing && hasSelection);
        btnRefresh.setEnabled(!isEditing);

        isChanged = isEditing;
    }

    private void exitEditMode() {
        if (editingRow != -1 && editingRow < tableModel.getRowCount()) {
            tableModel.setValueAt("✅", editingRow, 0);
        }
        editingRow = -1;
        updateUIState();
    }

    // --- DATA HELPERS ---
    public void setRoomData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            Object[] rowWithStatus = new Object[row.length + 1];
            rowWithStatus[0] = "✅";
            System.arraycopy(row, 0, rowWithStatus, 1, row.length);
            tableModel.addRow(rowWithStatus);
        }
        exitEditMode();
    }

    private Object[] getRowDataForController(int row) {
        int colCount = tableModel.getColumnCount();
        Object[] data = new Object[colCount - 1];
        for (int i = 1; i < colCount; i++) {
            data[i - 1] = tableModel.getValueAt(row, i);
        }
        return data;
    }

    public void updateRoomIdAtSelectedRow(int newId) {
        if (editingRow != -1) {
            tableModel.setValueAt(newId, editingRow, 1);
        }
    }

    public void setOnAddRoom(Consumer<Object[]> cb) {
        this.onAddRoom = cb;
    }

    public void setOnUpdateRoom(BiConsumer<Integer, Object[]> cb) {
        this.onUpdateRoom = cb;
    }

    public void setOnDeleteRoom(Consumer<Integer> cb) {
        this.onDeleteRoom = cb;
    }

    public void setOnRefresh(Runnable cb) {
        this.onRefresh = cb;
    }

    private String valueAt(int row, int col) {
        Object v = tableModel.getValueAt(row, col);
        return v == null ? null : v.toString();
    }

    public boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean confirmBeforeSwitch() {
        if (editingRow == -1) {
            return true;
        }
        return confirm("Dữ liệu đang sửa chưa lưu. Vẫn muốn rời đi?");
    }
}
