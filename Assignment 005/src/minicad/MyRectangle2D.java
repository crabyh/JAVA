package minicad;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import minicad.MyPanel.MyColor;

public class MyRectangle2D{

	MyColor color;
	Rectangle2D rect;
		
	double start_x;
	double start_y;
	
	public MyRectangle2D(double x, double y, double w, double h, MyColor c){
		color = c;
		rect = new Rectangle2D.Double(x, y, w, h);
	}
	
	public MyRectangle2D(MyRectangle2D other){
		color = other.color;
		rect = new Rectangle2D.Double(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}
	public void setColor(MyColor c){
		color = c;
	}
	
	public void setFrame(double x, double y, double w, double h){
		rect.setFrame(x, y, w, h);
	}
	
	public void setStartXY(double x, double y){
		start_x = x;
		start_y = y;
	}
	
	public double getStartX(){
		return start_x;
	}
	
	public double getStartY(){
		return start_y;
	}

	public double getX() {
		return rect.getX();
	}
	
	public double getY(){
		return rect.getY();
	}
	
	public double getWidth(){
		return rect.getWidth();
	}
	
	public double getHeight(){
		return rect.getHeight();
	}
	
	public Rectangle2D getLargerFrame(){
		return (new Rectangle2D.Double(rect.getX() - 10, rect.getY()-10, 
				rect.getWidth()+20, rect.getHeight()+20));
	}
	
	public Color getColor(){
		int re = 0;
		int gr = 0;
		int bl = 0;
		switch (color){
		case RED: re = 255; break;
		case GREEN: gr = 255; break;
		case BLUE: bl = 255; break;
		default: re = 0; gr = 0; bl = 0;
		}
		
		Color c = new Color(re, gr, bl);
		
		return c;
	}
	
	public String toString(){
		return (new String(color + ", " + rect.getX() + "," + rect.getY() + "," + 
	rect.getWidth() + "," + rect.getHeight()));
	}
	
}
