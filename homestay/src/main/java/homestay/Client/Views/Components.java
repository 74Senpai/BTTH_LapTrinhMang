package homestay.Client.Views;

import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Lớp chứa các hàm dùng chung
 */
public class Components {

    // Các hàm dùng chung trả về View ở đây
    public static Button createMenuItem(String text) {
        Button btn = new Button(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    // Hàm phụ trợ: Tạo Card
    public static Panel createCard(String title, String value, Color bgColor) {
        Panel card = new Panel();
        card.setLayout(new GridLayout(2, 1));
        card.setBackground(bgColor);

        // Label Value (Số to)
        Label lblValue = new Label(value, Label.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 14));
        lblValue.setForeground(Color.WHITE); // Chữ trắng

        // Label Title (Tiêu đề nhỏ)
        Label lblTitle = new Label(title, Label.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitle.setForeground(Color.WHITE);

        card.add(lblValue);
        card.add(lblTitle);
        return card;
    }

    // Hàm phụ trợ: Tạo dòng trong danh sách Insights
    public static Panel createListItem(String title, String sub) {
        Panel item = new Panel(new FlowLayout(FlowLayout.LEFT));
        item.setBackground(new Color(245, 245, 245));

        Label lblIcon = new Label("[Icon]");

        Panel pnlText = new Panel(new GridLayout(2, 1));
        Label lblTitle = new Label(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        Label lblSub = new Label(sub);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 10));

        pnlText.add(lblTitle);
        pnlText.add(lblSub);

        item.add(lblIcon);
        item.add(pnlText);

        // Thêm nút mũi tên giả
        item.add(new Label(">"));

        return item;
    }

    // Hàm căn giữa toàn bảng
    public static void centerTable(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // Hàm để kiểm tra number
    public static class NumericCellEditor extends DefaultCellEditor {

        public NumericCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            String value = (String) getCellEditorValue();

            try {
                double num = Double.parseDouble(value);
                if (num < 0) {
                    JOptionPane.showMessageDialog(null, "Giá phải là số dương!");
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Giá phải là số!");
                return false;
            }

            return super.stopCellEditing();
        }
    }

    // Hàm để kiểm tra số điện thoại việt nam
    public static class PhoneNumberCellEditor extends DefaultCellEditor {

        public PhoneNumberCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            String value = (String) getCellEditorValue();
            if (value == null || !value.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có đúng 10 chữ số.");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    // Hàm để kiểm tra số CCCD việt nam
    public static class CCCDCellEditor extends DefaultCellEditor {

        public CCCDCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean stopCellEditing() {
            String value = (String) getCellEditorValue();
            if (value == null || !value.matches("^0\\d{11}$")) {
                JOptionPane.showMessageDialog(null, "CCCD không hợp lệ. Phải bắt đầu bằng 0 và có đúng 12 chữ số.");
                return false;
            }
            return super.stopCellEditing();
        }
    }

    // Hàm cập nhật trạng thái của nút
    public static void updateMenuState(Button activeBtn, Button... otherButtons) {
        // Tắt nút đang active
        if (activeBtn != null) {
            activeBtn.setEnabled(false);
        }

        // Bật tất cả các nút khác
        for (Button btn : otherButtons) {
            btn.setEnabled(true);
        }
    }

    // Định nghĩa Interface để chuyển view
    public static interface IViewCheck {

        boolean confirmBeforeSwitch();
    }

    // Hàm chuyển view
    public static IViewCheck switchView(CardLayout cardLayout, Container container,
            IViewCheck currentView, IViewCheck targetView,
            String keyName) {
        // Nếu view hiện tại và view đích là một -> Không làm gì cả
        if (currentView == targetView) {
            return currentView;
        }

        // Kiểm tra View hiện tại
        if (currentView != null) {
            // Nếu View hiện tại trả về false -> Không chuyển view
            if (!currentView.confirmBeforeSwitch()) {
                return currentView;
            }
        }

        cardLayout.show(container, keyName);

        return targetView;
    }

    public static void showError(Container parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message != null ? message : "Đã xảy ra lỗi",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showInfo(Container parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static boolean confirmDialog(Container parent, String message) {
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}
