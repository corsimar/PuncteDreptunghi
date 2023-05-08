import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	
	MyPanel panel;
	
	public MyFrame(String title) {
		super(title);
		setMinimumSize(new Dimension(1000, 800));
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new MyPanel();
		panel.setBounds(0, 0, getContentPane().getBounds().width, getContentPane().getBounds().height);
		panel.initSize();
		getContentPane().add(panel);
	}
}
