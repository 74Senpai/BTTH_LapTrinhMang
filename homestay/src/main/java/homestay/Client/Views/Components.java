package homestay.Client.Views;

import java.awt.*;


public class Components {
    
    public static Button createMenuItem(String text) {
        Button btn = new Button(text);
        btn.setFont(new Font("Arial",Font.BOLD, 12));
        return btn;
    }
    
    // Hàm phụ trợ: Tạo Card (Ô vuông thống kê)
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
}
