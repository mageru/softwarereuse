package reuze;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ib_FilterDropShadow extends ib_a_Ops {

    private int direction = 135;
    private int distance = 5;
    private float blur = 5;
    private float alpha = 0.6f;

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {

        int blur = (int) Math.ceil(this.blur);
        int margin = distance + blur;
        int x = distance;
        int y = distance;
        int w = src.getWidth() + margin * 2;
        int h = src.getHeight() + margin * 2;

        int ix = direction < 337.5 && direction > 202.5 ? 0 : distance;
        ix += direction > 22.5 && direction < 157.5 ? distance : 0;
        ix = distance * 2 - ix;

        int iy = direction > 67.5 && direction < 337.5 ? distance : 0;
        iy += direction > 112.5 && direction < 247.5 ? distance : 0;
        iy = distance * 2 - iy;

        // Create mask
        BufferedImage mask = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = mask.createGraphics();
        g.drawImage(src, x, y, null);
        g
                .setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN,
                        getAlpha()));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, mask.getWidth(), mask.getHeight());
        g.dispose();

        // Create shadow
        BufferedImage shadow = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);

        shadow = new ib_FilterConvolveGaussian(blur).filter(mask, shadow);

        // Compose
        BufferedImage shadowedImage = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        g = shadowedImage.createGraphics();
        g.drawImage(shadow, 0, 0, null);
        g.drawImage(src, ix, iy, null);
        g.dispose();
        return shadowedImage;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public float getBlur() {
        return blur;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

}