import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
	
	MyFrame frame;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Main();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public Main() {
		initialize();
	}
	
	private void initialize() {
		frame = new MyFrame("Problema 6");
		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(frame.panel.step == 0)
						frame.panel.step = 1;
					frame.panel.repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					frame.panel.repaintRect = true;
					frame.panel.repaint();
				}
			}
		});
	}
	
	public static void print(String msg) {
		System.out.print(msg);
	}
	
	public static void println(String msg) {
		System.out.println(msg);
	}
}
