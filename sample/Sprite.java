package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;


public class Sprite
{
    final int MASS = 1;
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    public Sprite()
    {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary()
    {
       double test_width = width - 30;
       double test_height = height - 15;
       return new Rectangle2D(positionX,positionY,test_width,test_height);
    }

    public Circle getCircleBoundary(){
        Circle circle = new Circle();
        circle.setRadius(50.0f);
        return circle;
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }

    public boolean circleIntersects(Sprite otherSprite){
        if(this.positionX + this.getCircleBoundary().getRadius() + otherSprite.getCircleBoundary().getRadius() > otherSprite.getPositionX()
                && this.positionX < otherSprite.getPositionX() + this.getCircleBoundary().getRadius() + otherSprite.getCircleBoundary().getRadius()
                && this.getPositionY() + this.getCircleBoundary().getRadius() + otherSprite.getCircleBoundary().getRadius() > otherSprite.getPositionY()
                && this.getPositionY() < otherSprite.getPositionY() + this.getCircleBoundary().getRadius() + otherSprite.getCircleBoundary().getRadius())
        {
            //AABBs are overlapping
            double distance = Math.sqrt(
                    ((this.positionX - otherSprite.getPositionX()) * (this.positionX - otherSprite.getPositionX()))
            + ((this.getPositionY() - otherSprite.getPositionY()) * (this.getPositionY() - otherSprite.getPositionY()))
            );

            if (distance < otherSprite.getCircleBoundary().getRadius() + otherSprite.getCircleBoundary().getRadius())
            {
                return true;
            }
        }
        return false;
    }

    public void calcCollisionBounce(Sprite otherSprite){
        double newVelX1 = (this.getVelocityX() * (this.MASS - otherSprite.MASS) + (2 * otherSprite.MASS * otherSprite.getVelocityX())) / (this.MASS + otherSprite.MASS);
        double newVelY1 = (this.getVelocityY() * (this.MASS - otherSprite.MASS) + (2 * otherSprite.MASS * otherSprite.getPositionY())) / (this.MASS + otherSprite.MASS);
        double newVelX2 = (otherSprite.velocityX * (otherSprite.MASS - this.MASS) + (2 * this.MASS * this.getVelocityX())) / (this.MASS + otherSprite.MASS);
        double newVelY2 = (otherSprite.velocityY * (otherSprite.MASS - this.MASS) + (2 * this.MASS * this.velocityY)) / (this.MASS + otherSprite.MASS);

        this.setVelocity(newVelX1,newVelY1);
        otherSprite.setVelocity(newVelX2,newVelY2);
        double newX = this.getPositionX() + (newVelX1 / 10);
        double newY = this.getPositionY() + (newVelY1 / 10);
        double newX2 = otherSprite.getPositionX() + (newVelX2 / 10);
        double newY2 = otherSprite.getPositionY() + (newVelY2 / 10);
        this.setPosition(newX, newY);
        otherSprite.setPosition(newX2,newY2 );
    }

}
