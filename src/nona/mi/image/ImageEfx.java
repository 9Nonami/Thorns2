package nona.mi.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

public class ImageEfx extends BaseImage{
    
    private Coordinates coordinates;
    
    private boolean move;
    private boolean endMoveAnimation; //<<<<<<
    private boolean endAnimationX;
    private boolean endAnimationY;   
    private int destinationX;
    private int destinationY;
    private float speedX;
    private float speedY;
    
    private boolean baseEndMx;
    private boolean baseEndMy;
    
    private boolean alpha;
    private float speedAlpha;
    private boolean endAlphaAnimation; //<<<<<<<
    private float aspect;
    private float aspectBase;
    
    private BufferedImage[] images;
    private float delay;
    private boolean animatedImage;
    private float id;
    private boolean lockId;
    private int style;
    private boolean endAnimatedImageAnimation; //<<<<<<<<
    
    public static final int SOLID = 1;
    public static final int TRANSPARENT = 0;
    public static final int ONCE = 2;
    public static final int LOOP = 3;
    
    public ImageEfx(BufferedImage image, Coordinates coordinates) {
        super(image);
        this.coordinates = coordinates;
        
        this.endMoveAnimation = true;
        this.endAlphaAnimation = true;
        this.endAnimatedImageAnimation = true;
    }
    
    public ImageEfx(BufferedImage[] images, Coordinates coordinates, float delay, int style){
        super(null);
        this.images = images;
        this.coordinates = coordinates;
        this.delay = delay;
        this.style = style;
        this.animatedImage = true;
        this.id = 0;
        
        this.endMoveAnimation = true;
        this.endAlphaAnimation = true;
        this.endAnimatedImageAnimation = false;
    } 
    
    public void setMoveTo(int destinationX, int destinationY, float speedX, float speedY){
        this.move = true;
        this.endMoveAnimation = false;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.speedX = speedX;
        this.speedY = speedY;
        
        baseEndMx = false;
        baseEndMy = false;
        
        if (destinationX < this.coordinates.getBaseX()) {
            this.speedX *= -1;
        } else if (destinationX == this.coordinates.getBaseX()) {
            this.endAnimationX = true;
            this.baseEndMx = true;
        }
        
        if (destinationY < this.coordinates.getBaseY()) {
            this.speedY *= -1;
        } else if (destinationY == this.coordinates.getBaseY()) {
            this.endAnimationY = true;
            this.baseEndMy = true;
        }

    }
    
    public void setAlpha(int aspect, float speedAlpha){
        this.alpha = true;
        this.endAlphaAnimation = false;
        this.aspect = aspect;
        this.aspectBase = aspect;
        this.speedAlpha = speedAlpha;
        if (aspect == SOLID) {
            this.speedAlpha *= -1;
        }
        if (speedAlpha == 0) {
            endAlphaAnimation = true;
        }
    }
    
    @Override
    public void update(){
        if (animatedImage) {
            //System.out.println("updating images");
            if (!lockId) {
                this.id += delay;
            }
            if (this.id >= this.images.length) {
                if (this.style == ONCE) {
                    this.id = (images.length -1);
                    this.lockId = true;
                } else {
                    this.id = 0;
                }
                this.endAnimatedImageAnimation = true;
            }
            //System.out.println(id);
        }
        
        if (move && !endMoveAnimation) {
            //System.out.println("updating move");
            if (!endAnimationX) {
                this.coordinates.addX(speedX);
                if ((this.coordinates.getX() <= this.destinationX && this.speedX < 0) || (this.coordinates.getX() >= this.destinationX && this.speedX > 0)) {
                    this.coordinates.setX(this.destinationX);
                    endAnimationX = true;
                }
            }
            
            if (!endAnimationY) {
                this.coordinates.addY(speedY);
                if ((this.coordinates.getY() <= this.destinationY && this.speedY < 0) || (this.coordinates.getY() >= this.destinationY && this.speedY > 0)) {
                    this.coordinates.setY(this.destinationY);
                    endAnimationY = true;
                }
            }
            
            if (endAnimationX && endAnimationY) {
                endMoveAnimation = true;
            }
        }
        
        if (alpha && !endAlphaAnimation) {
            //System.out.println("updating alpha");
            this.aspect += speedAlpha;
            if (this.aspect >= SOLID && speedAlpha > 0) {
                this.endAlphaAnimation = true;
                this.aspect = SOLID;
            } else if (this.aspect <= TRANSPARENT && speedAlpha < 0) {
                this.endAlphaAnimation = true;
                this.aspect = TRANSPARENT;
            }
        }
        
        //System.out.println("move: " + endMoveAnimation);
        //System.out.println("alpha: " + endAlphaAnimation);
        //System.out.println("anima: " + endAnimatedImageAnimation);
        //System.out.println("");
    }
    
    @Override
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        if (alpha) {
            g2d.setComposite(AlphaComposite.SrcOver.derive((float)aspect));
        }
        if (images != null) {
            g2d.drawImage(images[(int) id], (int) this.coordinates.getX(), (int) this.coordinates.getY(), null);
        } else {
            g2d.drawImage(image, (int) this.coordinates.getX(), (int) this.coordinates.getY(), null);
        }
        g2d.dispose();
    }

    public boolean getEndMoveAnimation() {
        return this.endMoveAnimation;
    }
    
    public boolean getEndAlphaAnimation(){
        return this.endAlphaAnimation;
    }
    
    public boolean getEndAnimatedImageAnimation(){
        return this.endAnimatedImageAnimation;
    }
    
    public boolean getAllFinishedAnimations(){
        return (getEndMoveAnimation() && getEndAlphaAnimation() && getEndAnimatedImageAnimation());
    }
    
    public void reset(){
        coordinates.reset();
        if (move) {
            endMoveAnimation = false;
            endAnimationX = baseEndMx;
            endAnimationY = baseEndMy;
        }
        if (alpha) {
            endAlphaAnimation = false;
            aspect = aspectBase;
        }
        if (animatedImage) {
            lockId = false;
            endAnimatedImageAnimation = false;
            id = 0;
        }
    }
    
    public Coordinates getCoordinates(){
        return this.coordinates;
    }
    
}
