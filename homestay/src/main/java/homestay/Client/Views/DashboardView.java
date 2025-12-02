package homestay.Client.Views;

import java.awt.Panel;

public class DashboardView extends Panel implements Components.IViewCheck{

    Panel pnlMain = new Panel();
    public void showDashboard(){
        // Thiết kế giao diện dashboard ở đây
        
    }
    
    // Hàm check trước khi chuyển tab
    @Override
    public boolean confirmBeforeSwitch() {
        return true; 
    }
}
