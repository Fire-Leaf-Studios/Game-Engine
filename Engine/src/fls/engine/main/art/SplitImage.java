package fls.engine.main.art;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class SplitImage {

    private String loc;
    private int xs, ys;
    private BufferedImage pre_image;
    private BufferedImage[][] images;
    private int defaultImageType;

    public SplitImage(String loc) {
        this.loc = loc;
        this.pre_image=load();
    }
    
    public SplitImage(BufferedImage i){
    	this.loc = "Pre-loaded";
    	this.pre_image = i;
    	this.defaultImageType = this.pre_image.getType();
    }

    public BufferedImage getImage() {
        return pre_image;
    }

    public BufferedImage getImage(int x, int y) {
        if (xs == -1 && ys == -1) return getImage();
        return images[x][y];
    }

    /**
     * 
     * @return BufferedImage
     */
    public BufferedImage load() {
        try {
            BufferedImage org = ImageIO.read(Art.class.getResourceAsStream(loc));
            this.defaultImageType = org.getType();
            return org;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(loc + " : can't be found");
        }
    }

    public BufferedImage[][] split(int xs,int ys) {
        BufferedImage src = this.pre_image;
        int xSlide = src.getWidth() / xs;
        int ySlide = src.getHeight() / ys;
        BufferedImage[][] res = new BufferedImage[xSlide][ySlide];
        for (int x = 0; x < xSlide; x++) {
            for (int y = 0; y < ySlide; y++) {
            	BufferedImage newImage = new BufferedImage(xs, ys, this.defaultImageType);
                Graphics g = newImage.getGraphics();
                g.drawImage(src, -xs * x, -ys * y, null);
                g.dispose();
                res[x][y] = newImage;
            }
        }
        return res;
    }
    
    public BufferedImage[][] split(int xs) {
    	return split(xs, xs);
    }

    public BufferedImage[][] altsplit(int xs,int ys) {
        BufferedImage src = this.pre_image;
        int xSlide = src.getHeight() / xs;
        int ySlide = src.getWidth() / ys;
        BufferedImage[][] res = new BufferedImage[xSlide][ySlide];
        for (int x = 0; x < xSlide; x++) {
            for (int y = 0; y < ySlide; y++) {
                res[x][y] = new BufferedImage(xs, ys, this.defaultImageType);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, xs, 0, 0, ys, x * xs, y * ys, (x + 1) * xs, (y + 1) * ys, null);
                g.dispose();
            }
        }
        return res;
    }

    public static BufferedImage scale(BufferedImage src, int scale) {
        int w = src.getWidth() * scale;
        int h = src.getHeight() * scale;
        BufferedImage res = new BufferedImage(w, h, src.getType());
        Graphics g = res.getGraphics();
        g.drawImage(src.getScaledInstance(w, h, BufferedImage.SCALE_AREA_AVERAGING), 0, 0, null);
        return res;
    }
    
    public SplitImage changeImageColor(ABSColor oc,ABSColor nc){
    	int[] pixels = new int[this.pre_image.getWidth() * this.pre_image.getHeight()];
    	int w = this.pre_image.getWidth();
    	int h = this.pre_image .getHeight();
    	BufferedImage res = new BufferedImage(w ,h, BufferedImage.TYPE_INT_ARGB);
    	this.pre_image.getRGB(0, 0, w, h, pixels, 0, w);
    	for(int x = 0; x < w; x++){
    		for(int y = 0; y < h; y++){
    			if(pixels[x + y * w] == oc.getRGB()){
    				res.setRGB(x, y, nc.getRGB());
    			}
    		}
    	}
    	return new SplitImage(res);
    }

}
