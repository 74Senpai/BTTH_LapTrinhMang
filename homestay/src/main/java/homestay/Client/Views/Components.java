package homestay.Client.Views;

import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

public class Components {

    // ================== UI HELPERS ==================
    public static Button createMenuItem(String text) {
        Button btn = new Button(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static Panel createCard(String title, String value, Color bgColor) {
        Panel card = new Panel();
        card.setLayout(new GridLayout(2, 1));
        card.setBackground(bgColor);

        Label lblValue = new Label(value, Label.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 16));
        lblValue.setForeground(Color.WHITE);

        Label lblTitle = new Label(title, Label.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTitle.setForeground(Color.WHITE);

        card.add(lblValue);
        card.add(lblTitle);
        return card;
    }

    public static void centerTable(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // ================== VALIDATION EDITORS (JTable) ==================
    /**
     * Chặn nhập số âm, chấp nhận số thập phân (Dùng cho Giá tiền)
     */
    public static class NumericCellEditor extends DefaultCellEditor {

        public NumericCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            try {
                String value = (String) getCellEditorValue();
                if (Double.parseDouble(value.replace(",", "")) < 0) {
                    throw new Exception();
                }
            } catch (Exception e) {
                showError(null, "Giá trị phải là số dương!");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    /**
     * Chặn nhập số thập phân (Dùng cho Chỉ số Điện/Nước)
     */
    public static class IntegerCellEditor extends DefaultCellEditor {

        public IntegerCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            try {
                String value = (String) getCellEditorValue();
                if (Integer.parseInt(value) < 0) {
                    throw new Exception();
                }
            } catch (Exception e) {
                showError(null, "Phải là số nguyên dương (không có dấu chấm)!");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    /**
     * Kiểm tra số điện thoại (10 số, bắt đầu bằng 0)
     */
    public static class PhoneNumberCellEditor extends DefaultCellEditor {

        public PhoneNumberCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            String value = (String) getCellEditorValue();
            if (value == null || !value.matches("^0[35789]\\d{8}$")) {
                showError(null, "SĐT không hợp lệ! (Phải có 10 số, bắt đầu bằng 0)");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    /**
     * Kiểm tra CCCD (12 số cho CCCD gắn chip)
     */
    public static class CCCDCellEditor extends DefaultCellEditor {

        public CCCDCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            String value = (String) getCellEditorValue();
            if (value == null || !value.matches("^\\d{12}$")) {
                showError(null, "CCCD không hợp lệ! (Phải có đúng 12 chữ số)");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    /**
     * Kiểm tra định dạng ngày yyyy-MM-dd
     */
    public static class DateCellEditor extends DefaultCellEditor {

        private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public DateCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            try {
                LocalDate.parse((String) getCellEditorValue(), F);
            } catch (Exception e) {
                showError(null, "Định dạng ngày sai! (Phải là yyyy-MM-dd)");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    // ================== DIALOGS & MESSAGES ==================
    public static void showError(Container parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Container parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Đồng bộ tên hàm confirm để các Controller gọi được
     */
    public static boolean showConfirm(Container parent, String message) {
        return JOptionPane.showConfirmDialog(
                parent, message, "Xác nhận thao tác",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }

    // ================== NAVIGATION & STATE ==================
    public static void updateMenuState(Button activeBtn, Button... others) {
        if (activeBtn != null) {
            activeBtn.setEnabled(false);
        }
        for (Button b : others) {
            b.setEnabled(true);
        }
    }

    public static interface IViewCheck {

        boolean confirmBeforeSwitch();
    }

    public static IViewCheck switchView(CardLayout layout, Container container,
            IViewCheck current, IViewCheck target, String key) {
        if (current == target) {
            return current;
        }
        if (current != null && !current.confirmBeforeSwitch()) {
            return current;
        }
        layout.show(container, key);
        return target;
    }
}
