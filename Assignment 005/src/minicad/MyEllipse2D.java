package minicad;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import minicad.MyPanel.MyColor;

public class MyEllipse2D {
	MyColor color;
	Ellipse2D oval;
	
	double start_x;
	double start_y;
	
	public MyEllipse2D(double x, double y, double w, double h, MyColor c){
		color = c;
		oval = new Ellipse2D.Double(x, y, w, h);
	}
	
	public MyEllipse2D(MyEllipse2D other){
		color = other.color;
		oval = new Ellipse2D.Double(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}
	public void setColor(MyColor c){
		color = c;
	}
	
	public void setFrame(double x, double y, double w, double h){
		oval.setFrame(x, y, w, h);
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
		return oval.getX();
	}
	
	public double getY(){
		return oval.getY();
	}
	
	public double getWidth(){
		return oval.getWidth();
	}
	
	public double getHeight(){
		return oval.getHeight();
	}
	
	public Rectangle2D getLargerFrame(){
		return (new Rectangle2D.Double(oval.getX() - 10, oval.getY()-10, 
				oval.getWidth()+20, oval.getHeight()+20));
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
		return (new String(color + ", " + oval.getX() + "," + oval.getY() + "," + 
	oval.getWidth() + "," + oval.getHeight()));
	}
	
}
