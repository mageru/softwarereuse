package reuze;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ib_FilterRoundedCorners extends ib_a_Ops {

    private int cornerRadius = 20;

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = new BufferedImage(src.getWidth(), src.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g = dst.createGraphics();
        ib_FilterFitInto.applyDefaultRenderingHints(g);
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, src.getWidth(), src.getHeight(),
                getCornerRadius() * 2, getCornerRadius() * 2);
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return dst;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

}