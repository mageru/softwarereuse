package reuze;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ib_FilterFitInto extends ib_a_Ops {

    private int w = 128;
    private int h = 128;
    private boolean allowEnlarging = false;

    public int getWidth() {
        return w;
    }

    public void setWidth(int w) {
        this.w = w;
    }

    public int getHeight() {
        return h;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    public ib_FilterFitInto() {
    }

    public ib_FilterFitInto(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return "FitIntoFilter w=" + w + ", h=" + h;
    }

    public BufferedImage filter(BufferedImage image, BufferedImage dst) {

        // Calculate scale
        double x_scale = (double) w / (double) image.getWidth();
        double y_scale = (double) h / (double) image.getHeight();
        if (!allowEnlarging) {
            if (x_scale > 1f) {
                x_scale = 1f;
            }
            if (y_scale > 1f) {
                y_scale = 1f;
            }
        }

        double scale = x_scale < y_scale ? x_scale : y_scale;
        int nw = (int) (image.getWidth() * scale);
        int nh = (int) (image.getHeight() * scale);

        // Create new image if not given
        if (dst == null) {
            dst = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g = dst.createGraphics();
        applyDefaultRenderingHints(g);

        Image scaleImage = image.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        g.drawImage(scaleImage, 0, 0, null);
        g.dispose();
        return dst;
    }
    public static void applyDefaultRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }
}