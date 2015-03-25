package minicad;

import jmadonna.TextDialog;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import minicad.MyPanel.Mode;
import minicad.MyPanel.MyColor;
import minicad.MyPanel.State;

public class Canvas extends JPanel
	implements ActionListener, 
	MouseListener, 
	MouseMotionListener,
	PropertyChangeListener{

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1881002229987445251L;

	Mode mode;
	State state;
	MyColor color;
	int fontSize;
	
	BufferedImage image;
	
	MyPanel mainPanel;
	TextDialog textDialog;
	
	ArrayList<MyRectangle2D> rectList;
	ArrayList<Myllipse2D> ovalList; 
	ArrayList<MyLine2D> lineList;
	ArrayList<MyText> textList;
	
	/**indicate which area is selected
	 * 
	 */
	Rectangle selectRect = null;
	
	MyRectangle2D currentRect = null;
	MyRectangle2D rectToDraw = null;

	MyEllipse2D currentOval = null;  
	MyEllipse2D ovalToDraw = null;

	MyLine2D currentLine = null;
	MyLine2D lineToDraw = null;
	
	MyText currentText = null;
	int minx;
	int miny;
	int maxx;
	int maxy;
	
	int MIN_FONTSIZE = 11;
	
	final static float dash1[] = {5.0f};
	final static BasicStroke stroke = new BasicStroke(1.0f);
    final static BasicStroke dashed =
        new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        5.0f, dash1, 0.0f);
    
	public Canvas(MyPanel panel){	    
		mainPanel = panel;
		textDialog = new TextDialog(this);
		
		state = State.LINE;
		color = MyColor.BLACK;
		mode = Mode.NEW;		
		
		rectList = new ArrayList<MyRectangle2D>();
		ovalList = new ArrayList<MyEllipse2D>();
		lineList = new ArrayList<MyLine2D>();
		textList = new ArrayList<MyText>();
		
        addMouseListener(this);
        addMouseMotionListener(this);
        
        repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();  

		if (source == mainPanel.shapeCombo){
			int index = mainPanel.shapeCombo.getSelectedIndex();

			switch (index){
			case 1: state = State.RECT; break;
			case 2: state = State.OVAL; break;
			default: state = State.LINE;
			}

			mode = Mode.NEW;    
			selectRect = null;
			
		} else if (source == mainPanel.colorCombo){
			int index = mainPanel.colorCombo.getSelectedIndex();
			switch (index){
			case 0: color = MyColor.BLACK; break;
			case 1: color = MyColor.RED; break;
			case 2: color = MyColor.GREEN; break;
			case 3: color = MyColor.BLUE; break;
			default: color = MyColor.BLACK;
			}
			
			if (mode == Mode.MOVE || mode == Mode.SIZE){
				/**change the color of the target select
				 * 
				 */
				switch (state){
				case LINE:
					currentLine.setColor(color);
					break;
				case OVAL:
					currentOval.setColor(color);
					break;
				case RECT:
					currentRect.setColor(color);
					break;
				case TEXT:
					currentText.setColor(color);
					break;
				default:
					break;
				
				}
			}
			repaint();
		}
		else if (source == mainPanel.textBtn){
			//System.out.println("state TEXT");
			mode = Mode.NEW;
			selectRect = null;
			state = State.TEXT;
		}
		else if (source == mainPanel.sizeBtn){
			System.out.println("now change size");
			mode = Mode.SIZE;
		}
		else if (source == mainPanel.moveBtn){
			System.out.println("Now MOVE");
			mode = Mode.MOVE;
		}
		else if (source == mainPanel.saveBtn){
			image = new BufferedImage(this.getWidth(), this.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			paintComponent(g2);
			
			File saveFile = new File("savedimage.jpg");
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("./"));
			chooser.setSelectedFile(saveFile);
			
			int rval = chooser.showSaveDialog(mainPanel);
			if (rval == JFileChooser.APPROVE_OPTION) {
				saveFile = chooser.getSelectedFile();
				try {
					ImageIO.write(image, "jpg", saveFile);
				} catch (IOException ex){
					System.out.println("can not save a image");
				}
			}			
		}
		else if (source == mainPanel.loadBtn){
			mode = Mode.LOAD;
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("./"));
			FileFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
			chooser.setFileFilter(filter);
			int rval = chooser.showOpenDialog(mainPanel);
			File openFile = null;
			if (rval == JFileChooser.APPROVE_OPTION){
				openFile = chooser.getSelectedFile();
				try{
					image = ImageIO.read(openFile);
					repaint();
				} catch (IOException ex){
					System.out.println("can not load a image");
				}
			}
			System.out.println("load a jpg");
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouse cliked: "+e.getX()+", "+e.getY());

	}
	@Override
	public void mouseEntered(MouseEvent arg0) {

	}
	@Override
	public void mouseExited(MouseEvent arg0) {

	}
	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (mode == Mode.NEW){
			/*new a shape*/
			switch (state){
			case LINE:	
				currentLine = new MyLine2D(x, y, x, y, color);
				//lineList.add(currentLine);
				updateDrawableLine(minx, miny, maxx-minx, maxy- miny);
				repaint();
				break;
			case RECT:
				//System.out.println("NOW COLOR "+color);
				currentRect = new MyRectangle2D(x, y, 0, 0, color);
				updateDrawableRect(minx, miny, maxx-minx, maxy- miny);
				repaint();
				break;
			case OVAL:
				currentOval = new MyEllipse2D(x, y, 0, 0, color);
				updateDrawableOval(minx, miny, maxx-minx, maxy- miny);
				repaint();
				break;
			default:
				break;
			}
		}
		else if (mode == Mode.MOVE || mode == Mode.SIZE){
			/**select the target
			 * no matter you wan to move or change the size
			 * and set currentShape
			 */
			int i;
			switch (state){
			case LINE:
				MyLine2D line = null;
				for (i = lineList.size()-1; i>=0; i--){
					line = lineList.get(i);
					selectRect = new Rectangle();
					selectRect.setFrame(line.getLargerFrame());
					if (selectRect.contains(x, y)){
						System.out.println("Select A line, ready to move");
						break;
					}
				}
				
				if(i>=0){
					currentLine = lineList.get(i);
					currentLine.setStartXY(x, y);
				}
				
				repaint();
				break;
			case RECT:
				MyRectangle2D rect = null;
				for (i = rectList.size() - 1; i>=0; i--){
					rect = rectList.get(i);
					selectRect = new Rectangle();
					selectRect.setFrame(rect.getLargerFrame());
					if (selectRect.contains(x, y)){
						System.out.println("Select A RECT, ready to move");
						break;
					}
				}
				
				if (i>=0){
					currentRect = rectList.get(i);
					currentRect.setStartXY(x, y);
				}
				
				repaint();
				
				break;
			case OVAL:
				MyEllipse2D oval = null;
				for (i = ovalList.size() - 1; i>=0; i--){
					oval = ovalList.get(i);
					selectRect = new Rectangle();
					selectRect.setFrame(oval.getLargerFrame());
					if (selectRect.contains(x, y)){
						System.out.println("Select A OVAL, ready to move");
						break;
					}
				}
				
				if (i>=0){
					currentOval = ovalList.get(i);
					currentOval.setStartXY(x, y);
				}
				
				repaint();
				break;
			default:
				break;
			}
		}
		
		if (state == State.TEXT){
			MyText text = null;
			int i ;
			for (i = textList.size() - 1;i>=0; i--){
				text = textList.get(i);
				selectRect = new Rectangle();
				selectRect.setFrame(text.getLargerFrame());
				if (selectRect.contains(x, y)){
					System.out.println("Select some text, ready to move");
					break;
				}
			}
			
			System.out.println("NOW I "+i);
			if (i>=0){
				currentText = textList.get(i);
				currentText.setStartXY(x, y);
			}
			else {
				//new a text
				textDialog.setVisible(true);
				currentText = new MyText(x, y, color);
			}
			
			repaint();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (mode == Mode.NEW || mode == Mode.SIZE){
			updateSize(e);
			if (mode == Mode.NEW){
				switch (state){
				case LINE:
					lineList.add(new MyLine2D(lineToDraw));
					break;
				case OVAL:
					ovalList.add(new MyEllipse2D(ovalToDraw));
					break;
				case RECT:
					rectList.add(new MyRectangle2D(rectToDraw));		
					break;
				default:
					break;
				
				}
			}
		}
		System.out.println("mouse released: "+e.getX()+", "+e.getY());

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (mode == Mode.NEW || mode == Mode.SIZE){
			updateSize(e);
		}
		else if (mode == Mode.MOVE){
			updatePosition(e);
		}
		System.out.println("mouse dragged: "+e.getX() +", "+e.getY());
	}
	@Override
	public void mouseMoved(MouseEvent e) {

	}

	void updatePosition(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		int diff_x;
		int diff_y;
		
		switch (state){
		case LINE:
			diff_x = x - (int)currentLine.getStartX();
			diff_y = y - (int)currentLine.getStartY();
			currentLine.setLine((int)currentLine.getX1()+diff_x, (int)currentLine.getY1()+diff_y,
					(int)currentLine.getX2()+diff_x, (int)currentLine.getY2()+diff_y);
			currentLine.setStartXY(x, y);
			selectRect.setFrame(selectRect.getX()+diff_x, selectRect.getY()+diff_y, 
					selectRect.getWidth(), selectRect.getHeight());
			
			repaint();
			break;
		case OVAL:
			diff_x = x - (int)currentOval.getStartX();
			diff_y = y - (int)currentOval.getStartY();
			currentOval.setFrame(currentOval.getX()+diff_x, currentOval.getY()+diff_y, 
					currentOval.getWidth(), currentOval.getHeight());
			currentOval.setStartXY(x, y);
			selectRect.setFrame(currentOval.getLargerFrame());
			
			repaint();			
			break;
		case RECT:
			diff_x = x - (int)currentRect.getStartX();
			diff_y = y - (int)currentRect.getStartY();
			currentRect.setFrame(currentRect.getX()+diff_x, currentRect.getY()+diff_y, 
					currentRect.getWidth(), currentRect.getHeight());
			currentRect.setStartXY(x, y);
			selectRect.setFrame(currentRect.getLargerFrame());
			
			repaint();			
			break;
			
		case TEXT:
			diff_x = x - (int)currentText.getStartX();
			diff_y = y - (int)currentText.getStartY();
			currentText.setPosition(currentText.getX()+diff_x, currentText.getY()+diff_y);
			currentText.setStartXY(x, y);
			selectRect.setFrame(currentText.getLargerFrame());
			
			repaint();
			break;
		default:
			break;
		}
	}

	void updateSize(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		//if (state == State.LINE){
		switch (state){
		case LINE:
			Point2D newend = new Point2D.Double();
			newend.setLocation(x, y);
			currentLine.setLine(currentLine.getP1(), newend);
			if (mode == Mode.SIZE){
				selectRect.setFrame(currentLine.getLargerFrame());
			}
			updateDrawableLine(minx, miny, maxx-minx, maxy- miny);
			repaint();
			break;
		case RECT:
			currentRect.setFrame(currentRect.getX(), currentRect.getY(), 
					x - currentRect.getX(), y - currentRect.getY());
			updateDrawableRect(minx, miny, maxx-minx, maxy- miny);
			if (mode == Mode.SIZE){
				selectRect.setFrame(currentRect.getLargerFrame());
			}
			//Rectangle totalRepaint = rectToDraw.union(previousRectDrawn);
			repaint();
			break;
		case OVAL:
			currentOval.setFrame(currentOval.getX(), currentOval.getY(), 
					x - currentOval.getX(), y - currentOval.getY());
			if (mode == Mode.SIZE){
				selectRect.setFrame(currentOval.getLargerFrame());
			}
			updateDrawableOval(minx, miny, maxx-minx, maxy- miny);
			repaint();
			break;
		default:
			break;
		}
	}

	protected void paintComponent(Graphics g){


		//si.g2 = (Graphics2D) g;

		
		super.paintComponent(g);
		if (mode == Mode.LOAD){
			g.drawImage(image, mainPanel.getX(), mainPanel.getY(), mainPanel);
		}
		else {
			Graphics2D g2 = (Graphics2D) g;
			minx = this.getX() + 20;
			miny = this.getY();
			maxx = this.getX()+ this.getWidth()-20;
			maxy = this.getY()+this.getHeight()-60;

			g.setColor(new Color(200, 200, 200));

			g.fillRect(minx, miny, maxx-minx, maxy-miny);

			g2.setStroke(stroke);

			if (selectRect != null){
				System.out.println("DASH RECT "+ selectRect);
				g2.setColor(new Color(80, 80, 80));
				g2.setStroke(dashed);
				g2.draw(selectRect);
				g2.setStroke(stroke);
			}

			for (MyLine2D line: lineList){
				g.setColor(line.getColor());

				g.drawLine((int)line.getX1(), (int)line.getY1(), 
						(int)line.getX2(), (int)line.getY2());
			}
			for (MyRectangle2D rect: rectList){
				g.setColor(rect.getColor());
				g.drawRect((int)rect.getX(), (int)rect.getY(), 
						(int)rect.getWidth(), (int)rect.getHeight());
			}

			for (MyEllipse2D oval: ovalList){
				g.setColor(oval.getColor());
				g.drawOval((int)oval.getX(), (int)oval.getY(), 
						(int)oval.getWidth(), (int)oval.getHeight());
			}

			System.out.println("TEXTLIST SIZE: " + textList.size());
			for (MyText text: textList){
				g.setColor(text.getColor());
				g.drawString(text.getText(), (int)text.getX(), (int)text.getY());
			}

			if (mode == Mode.NEW){
				switch (state){
				case LINE:
					if (lineToDraw != null){
						g.setColor(lineToDraw.getColor());
						g.drawLine((int)lineToDraw.getX1(), (int)lineToDraw.getY1(),
								(int)lineToDraw.getX2(), (int)lineToDraw.getY2());
					}
					break;
				case RECT:		
					if (rectToDraw != null){
						g.setColor(rectToDraw.getColor());
						g.drawRect((int)rectToDraw.getX(), (int)rectToDraw.getY(), 
								(int)rectToDraw.getWidth(), (int)rectToDraw.getHeight());
					}
					break;
				case OVAL:
					if (ovalToDraw != null){
						g.setColor(ovalToDraw.getColor());
						g.drawOval((int)ovalToDraw.getX(), (int)ovalToDraw.getY(), 
								(int)ovalToDraw.getWidth(), (int)ovalToDraw.getHeight());
					}
					break;
					//		case TEXT:
					//			if (currentText != null){
					//				g.setColor(currentText.getColor());
					//				g.drawString(currentText.getText(), 
					//						(int) currentText.getX(), (int)currentText.getY());
					//			}
				default:
					break;

				}
			}
		}
	}

	private int mapToInterval(int a, int mina, int maxa){
		if (a<mina) return mina;
		if (a>maxa) return maxa;
		return a;
	}
	
	private void updateDrawableRect(int compX, int compY, int compWidth, int compHeight){
		int x = (int) currentRect.getX();
		int y = (int) currentRect.getY();
		int width = (int) currentRect.getWidth();
		int height = (int)currentRect.getHeight();

		//Make the width and height positive, if necessary.
		if (width < 0) {
			width = 0 - width;
			x = x - width + 1; 
			if (x < 0) {
				width += x; 
				x = 0;
			}
		}
		if (height < 0) {
			height = 0 - height;
			y = y - height + 1; 
			if (y < 0) {
				height += y; 
				y = 0;
			}
		}

		//The rectangle shouldn't extend past the drawing area.
		x = mapToInterval(x, compX, compX+compWidth);
		y = mapToInterval(y, compY, compY+compHeight);
		
		if ((x + width) > compX + compWidth) {
			width = compX + compWidth - x;
		}
		if ((y + height) > compY + compHeight) {
			height = compY + compHeight - y;
		}

		//Update rectToDraw after saving old value.
		if (rectToDraw != null) {
			rectToDraw.setFrame(x, y, width, height);
			rectToDraw.setColor(color);
		} else {
			rectToDraw = new MyRectangle2D(x, y, width, height, color);
		}
	}


	private void updateDrawableLine(int compX, int compY, int compWidth, int compHeight){
		int x = (int) currentLine.getX1();
		int y = (int) currentLine.getY1();
		int x2 = (int) currentLine.getX2();
		int y2 = (int) currentLine.getY2();
		
		//The line shouldn't extend past the drawing area.
		x = mapToInterval(x, compX, compX+compWidth);
		x2 = mapToInterval(x2, compX, compX+compWidth);
		y = mapToInterval(y, compY, compY+compHeight);
		y2 = mapToInterval(y2, compY, compY+compHeight);
		

		//Update rectToDraw after saving old value.
		if (lineToDraw != null) {
			//previousLineDrawn.setLine(lineToDraw);
			lineToDraw.setLine(x, y, x2, y2);
			lineToDraw.setColor(color);
			//rectToDraw.setBounds(x, y, width, height);
		} else {
			lineToDraw = new MyLine2D(x, y, x2, y2, color);
			//lineToDraw.setLine(x, y, x2, y2);
		}
	}

	private void updateDrawableOval(int compX, int compY, int compWidth, int compHeight){
		int x = (int)currentOval.getX();
		int y = (int)currentOval.getY();
		int width = (int)currentOval.getWidth();
		int height = (int)currentOval.getHeight();

		//Make the width and height positive, if necessary.
		if (width < 0) {
			width = 0 - width;
			x = x - width + 1; 
			if (x < 0) {
				width += x; 
				x = 0;
			}
		}
		if (height < 0) {
			height = 0 - height;
			y = y - height + 1; 
			if (y < 0) {
				height += y; 
				y = 0;
			}
		}

		//The ellipse shouldn't extend past the drawing area.
		x = mapToInterval(x, compX, compX+compWidth);
		y = mapToInterval(y, compY, compY+compHeight);
		
		if ((x + width) > compX + compWidth) {
			width = compX + compWidth - x;
		}
		if ((y + height) > compY + compHeight) {
			height = compY + compHeight - y;
		}

		//Update rectToDraw after saving old value.
		if (ovalToDraw != null) {
			ovalToDraw.setFrame(x, y, width, height);
			ovalToDraw.setColor(color);
		} else {
			ovalToDraw = new MyEllipse2D(x, y, width, height, color);					
		}
	}

	@Override
	/**text Dialog edit
	 * 
	 */
	public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (//text.isVisible()
         (e.getSource() == textDialog.optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
        	System.out.println("PRINT OUT "+textDialog.getValidatedText());
        	
        	Object value = textDialog.optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }
 
            System.out.println("CANVAS TEXT DIALOG "+value);
            
            //READ the text, reset the optionPane
            textDialog.optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);
            
        	if (textDialog.getValidatedText()!=null){
        		currentText.setText(textDialog.getValidatedText());
        		textList.add(currentText);        		
        		repaint();
        	}
        }
		
	}
}
