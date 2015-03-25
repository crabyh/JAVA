package minicad;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MyPanel extends JPanel 
implements ActionListener
{  

	/**have no idea with serialVearsionUID
	 * but eclipse give me a warning and suggest me to do that....
	 * so, I randomly picked a suggestion.....
	 */
	private static final long serialVersionUID = 7314229047302411828L;

	enum State{
		LINE, RECT, OVAL, TEXT
	}

	enum MyColor{
		BLACK, RED, GREEN, BLUE
	}

	enum Mode{
		NEW, SIZE, MOVE, LOAD
	}

	Canvas canvas;
//	SaveImage saveImage;


	JComboBox colorCombo;
	JComboBox shapeCombo;
	
	JButton textBtn;
	JButton sizeBtn;
	JButton moveBtn;/*move the target*/

	JButton loadBtn;
	JButton saveBtn;
	JButton exitBtn;


	public MyPanel() {  

		this.setLayout(new BorderLayout());


		colorCombo = new JComboBox();
		colorCombo.addItem("Black");
		colorCombo.addItem("Red");
		colorCombo.addItem("Green");
		colorCombo.addItem("Blue");
		
		shapeCombo = new JComboBox();
		shapeCombo.addItem("Line");
		shapeCombo.addItem("Rect");
		shapeCombo.addItem("Oval");
		
		textBtn = new JButton("Text");

		sizeBtn = new JButton("Change Size");

		moveBtn = new JButton("Move");

		saveBtn = new JButton("Save");
		loadBtn = new JButton("Load");
		exitBtn = new JButton("Exit");

		canvas = new Canvas(this);
//		saveImage = new SaveImage(this);
		
		this.add(canvas, BorderLayout.CENTER);


		//currentLine = new Line2D.Double();
		//colorCombo.addItemListener(this);

		shapeCombo.addActionListener(canvas);
		colorCombo.addActionListener(canvas);
		
		textBtn.addActionListener(canvas);
		
		saveBtn.addActionListener(canvas);
		
		loadBtn.addActionListener(canvas);
		
		sizeBtn.addActionListener(canvas);
		moveBtn.addActionListener(canvas);

		exitBtn.addActionListener(this);

		JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
		header.add(new JLabel("Shape:"));
		header.add(shapeCombo);
		header.add(new JLabel("Color:"));
		header.add(colorCombo);
		header.add(textBtn);

		header.add(sizeBtn);
		header.add(moveBtn);
		this.add(header, BorderLayout.NORTH);
		
		JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER));
		foot.add(saveBtn);
		foot.add(loadBtn);
		foot.add(exitBtn);  
		this.add(foot, BorderLayout.SOUTH);
		
	}  

	public void actionPerformed(ActionEvent e) {  
		Object source = e.getSource();  
		if (source == exitBtn){ 
			System.exit(0);  
		}  
	}  
}  

