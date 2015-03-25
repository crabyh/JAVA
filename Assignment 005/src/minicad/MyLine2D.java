package minicad;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import minicad.MyPanel.MyColor;

public class MyLine2D {

	MyColor color;
	Line2D line;
	
	double start_x;
	double start_y;
	
	public MyLine2D(double x1, double y1, double x2, double y2, MyColor c){
		color = c;
		line = new Line2D.Double(x1, y1, x2, y2);
	}
	
	public MyLine2D(MyLine2D other){
		color = other.color;
		line = new Line2D.Double(other.getX1(), other.getY1(), other.getX2(), other.getY2());
	}
	public void setColor(MyColor c){
		color = c;
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
	
	public void setLine(Point2D p1, Point2D p2){
		line.setLine(p1, p2);
	}
	
	public void setLine(double x1, double y1, double x2, double y2){
		line.setLine(x1, y1, x2, y2);
	}

	public double getX1() {
		return line.getX1();
	}
	
	public double getY1(){
		return line.getY1();
	}
	
	public double getX2(){
		return line.getX2();
	}
	
	public double getY2(){
		return line.getY2();
	}
	
	public Point2D getP1(){
		return line.getP1();
	}
	
	public Point2D getP2(){
		return line.getP2();
	}
	
	public Rectangle2D getLargerFrame(){
		double x1 = line.getX1();
		double x2 = line.getX2();
		double y1 = line.getY1();
		double y2 = line.getY2();
		double tmp;
		if (x1 < x2 ){
			x1 = x1 - 5;
			x2 = x2 + 5;
		}
		else {			
			tmp = x1 + 5;
			x1 = x2 - 5;
			x2 = tmp;
		}
		
		if (y1 < y2){
			y1 = y1 - 5;
			y2 = y2 + 5;
		}
		else {
			tmp = y1 + 5;
			y1 = y2 - 5;
			y2 = tmp;
		}
		
		return (new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
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
		return (new String(color + ", " + line.getX1() + "," + line.getY1() + "," + 
	line.getX2() + "," + line.getY2()));
	}
}
