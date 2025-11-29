package homestay.Client.Views;

import java.awt.*;

class BackgroundPanel extends Panel {
    private Image img;

    public BackgroundPanel(String imagePath) {
        //Load ảnh
        img = Toolkit.getDefaultToolkit().getImage(imagePath);
        // Nếu không có đoạn này, AWT sẽ vẽ trước khi ảnh kịp load -> màn hình trắng
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(img, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        //Vẽ ảnh căng tràn toàn bộ panel
        if (img != null) {
            
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this); 
        }
        
       
    }
}