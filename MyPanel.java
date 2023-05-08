import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyPanel extends JPanel implements MouseListener {
	
	int windowW, windowH;
	
	int step = -1;
	int clickX = -1, clickY = -1;
	int pointSize = 12;
	int pointIndex = 1;
	
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Point> L1 = new ArrayList<Point>();
	ArrayList<Point> L2 = new ArrayList<Point>();
	
	ArrayList<Rectangle> matrix = new ArrayList<Rectangle>();
	ArrayList<Integer> matrixVal = new ArrayList<Integer>();
	ArrayList<Point> rect = new ArrayList<Point>();
	
	int qA, qB, qC, qD;
	boolean repaintRect = false;
	
	public MyPanel() {
		setLayout(null);
		addMouseListener(this);
	}
	
	void initSize() {
		this.windowW = getBounds().width;
		this.windowH = getBounds().height;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		if(step == -1)
			paintBackground(g2d, Color.white);
		
		if(clickX != -1 && step != 2)
			paintPoint(g2d);
		
		if(step == 1)
			orderPoints(g2d);
		
		if(step == 2 && clickX != -1) {
			if(rect.size() == 0) {
				rect.add(new Point(clickX, clickY));
				drawPoint(g2d, new Point(rect.get(0).x, rect.get(0).y), pointSize / 3, Color.red);
				qA = matrixVal.get(binarySearchX(0, L1.size() - 1, rect.get(0)) * (L1.size() + 1) + binarySearchY(0, L2.size() - 1, rect.get(0)));
			}
			else if(rect.size() == 1) {
				rect.add(new Point(clickX, rect.get(0).y));
				drawPoint(g2d, new Point(rect.get(1).x, rect.get(1).y), pointSize / 3, Color.red);
				g2d.setPaint(Color.red);
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(rect.get(0).x + pointSize / 6, rect.get(0).y + pointSize / 6, rect.get(1).x + pointSize / 6, rect.get(1).y + pointSize / 6);
				qB = matrixVal.get(binarySearchX(0, L1.size() - 1, rect.get(1)) * (L1.size() + 1) + binarySearchY(0, L2.size() - 1, rect.get(1)));
			}
			else if(rect.size() == 2) {
				rect.add(new Point(rect.get(1).x, clickY));
				rect.add(new Point(rect.get(0).x, rect.get(2).y));
				drawPoint(g2d, new Point(rect.get(2).x, rect.get(2).y), pointSize / 3, Color.red);
				drawPoint(g2d, new Point(rect.get(3).x, rect.get(3).y), pointSize / 3, Color.red);
				g2d.setPaint(Color.red);
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(rect.get(1).x + pointSize / 6, rect.get(1).y + pointSize / 6, rect.get(2).x + pointSize / 6, rect.get(2).y + pointSize / 6);
				g2d.drawLine(rect.get(2).x + pointSize / 6, rect.get(2).y + pointSize / 6, rect.get(3).x + pointSize / 6, rect.get(3).y + pointSize / 6);
				g2d.drawLine(rect.get(3).x + pointSize / 6, rect.get(3).y + pointSize / 6, rect.get(0).x + pointSize / 6, rect.get(0).y + pointSize / 6);
				qC = matrixVal.get(binarySearchX(0, L1.size() - 1, rect.get(2)) * (L1.size() + 1) + binarySearchY(0, L2.size() - 1, rect.get(2)));
				qD = matrixVal.get(binarySearchX(0, L1.size() - 1, rect.get(3)) * (L1.size() + 1) + binarySearchY(0, L2.size() - 1, rect.get(3)));
				
				Rectangle r = new Rectangle(rect.get(0).x, rect.get(0).y, rect.get(1).x - rect.get(0).x, rect.get(2).y - rect.get(1).y);
				drawCenteredString(g2d, (qB - qA - qC + qD) + "", r, new Font("Sans", Font.BOLD, 21), Color.red);
			}
			
			clickX = -1;
		}
		
		if(repaintRect == true) {
			g2d.setPaint(Color.white);
			g2d.fillRect(0, 0, windowW, windowH);
			rect.clear();
			
			drawMatrix(g2d);
			repaintRect = false;
		}
	}
	
	void paintBackground(Graphics2D g2d, Color color) {
		g2d.setPaint(color);
		g2d.fillRect(0, 0, windowW, windowH);
	}
	
	void paintPoint(Graphics2D g2d) {
		g2d.setPaint(Color.black);
		g2d.drawOval(clickX - pointSize / 2, clickY - pointSize / 2, pointSize, pointSize);
		drawCenteredString(g2d, pointIndex + "", new Rectangle(clickX - pointSize / 2, clickY - pointSize / 2, pointSize, pointSize), new Font("Sans", Font.BOLD, 9), Color.black);
		points.add(new Point(clickX, clickY));
		
		pointIndex++;
		clickX = -1; clickY = -1;
	}
	
	void paintPoints(Graphics2D g2d) {
		for(int i = 0; i < points.size(); i++) {
			g2d.setPaint(Color.black);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawOval(points.get(i).x - pointSize / 2, points.get(i).y - pointSize / 2, pointSize, pointSize);
			drawCenteredString(g2d, (i + 1) + "", new Rectangle(points.get(i).x - pointSize / 2, points.get(i).y - pointSize / 2, pointSize, pointSize), new Font("Sans", Font.BOLD, 9), Color.black);
		}		
	}
	
	void drawPoint(Graphics2D g2d, Point p, int size, Color color) {
		g2d.setPaint(color);
		g2d.fillOval(p.x, p.y, size, size);
	}
	
	void orderPoints(Graphics2D g2d) {
		L1.addAll(points);
		L1.sort(new PointXComparator());
		
		L2.addAll(points);
		L2.sort(new PointYComparator());
		
		drawMatrix(g2d);
	}
	
	void drawMatrix(Graphics2D g2d) {
		g2d.setPaint(Color.black);
		int v = 0, index = 0;
		for(int i = 0; i < L1.size() + 1; i++) {
			for(int j = 0; j < L2.size() + 1; j++) {
				if(i != L1.size() && j != L2.size()) {
					if(i == 0 && j != 0)
						matrix.add(new Rectangle(0, L2.get(j).y, L1.get(i).x, L2.get(j - 1).y - L2.get(j).y));
					else if(i != 0 && j == 0)
						matrix.add(new Rectangle(L1.get(i - 1).x, L2.get(j).y, L1.get(i).x - L1.get(i - 1).x, windowH - L2.get(j).y));
					else if(i == 0 && j == 0)
						matrix.add(new Rectangle(0, L2.get(j).y, L1.get(i).x, windowH - L2.get(j).y));
					else
						matrix.add(new Rectangle(L1.get(i - 1).x, L2.get(j).y, L1.get(i).x - L1.get(i - 1).x, L2.get(j - 1).y - L2.get(j).y));
				}
				else {
					if(i == L1.size() && j == L2.size())
						matrix.add(new Rectangle(L1.get(i - 1).x, 0, windowW - L1.get(i - 1).x, L2.get(j - 1).y));
					else if(j == L2.size()) {
						if(i == 0)
							matrix.add(new Rectangle(0, 0, L1.get(i).x, L2.get(L2.size() - 1).y));
						else
							matrix.add(new Rectangle(matrix.get(matrix.size() - 1).x, 0, matrix.get(matrix.size() - 1).width, L2.get(L2.size() - 1).y));
					}
					else if(i == L1.size()) {
						if(j == 0)
							matrix.add(new Rectangle(L1.get(i - 1).x, L2.get(j).y, windowW - L1.get(i - 1).x, windowH - L2.get(j).y));
						else
							matrix.add(new Rectangle(L1.get(i - 1).x, L2.get(j).y, windowW - L1.get(i - 1).x, L2.get(j - 1).y - L2.get(j).y));
					}
				}
				
				g2d.drawRect(matrix.get(matrix.size() - 1).x, matrix.get(matrix.size() - 1).y, matrix.get(matrix.size() - 1).width, matrix.get(matrix.size() - 1).height);
				if(i == 0) v = 0;
				else {
					index = L2.indexOf(L1.get(i - 1));
					if(j <= index)
						v = matrixVal.get((i - 1) * (L1.size() + 1) + j);
					else
						v = matrixVal.get((i - 1) * (L1.size() + 1) + j) + 1;
				}
				matrixVal.add(v);
				drawCenteredString(g2d, v + "", matrix.get(matrix.size() - 1), new Font("Sans", Font.BOLD, 21), Color.black);
			}
		}
		paintPoints(g2d);
		step = 2;
	}
	
	int binarySearchX(int l, int r, Point p) {
		if(l > r) return 0;
		else {
			int m = (l + r) / 2;
			
			if(m + 1 >= L1.size())
				return L1.size();
						
			if(p.x > L1.get(m).x && p.x < L1.get(m + 1).x)
				return m + 1;
			else if(p.x < L1.get(m).x)
				return binarySearchX(l, m - 1, p);
			else
				return binarySearchX(m + 1, r, p);
		}
	}
	
	int binarySearchY(int l, int r, Point p) {
		if(l > r) return 0;
		else {
			int m = (l + r) / 2;
			
			if(m + 1 >= L2.size())
				return L2.size();
						
			if(p.y < L2.get(m).y && p.y > L2.get(m + 1).y)
				return m + 1;
			else if(p.y > L2.get(m).y)
				return binarySearchY(l, m - 1, p);
			else
				return binarySearchY(m + 1, r, p);
		}
	}

	void drawCenteredString(Graphics2D g2d, String message, Rectangle r, Font font, Color color) {
		FontMetrics metrics = g2d.getFontMetrics(font);
		
		int x = r.x + (r.width - metrics.stringWidth(message)) / 2;
		int y = r.y + ((r.height - metrics.getHeight()) / 2) + metrics.getAscent();
		
		g2d.setFont(font);
		g2d.setPaint(color);
		g2d.drawString(message, x, y);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(step == -1)
			step = 0;
		if(step == 0 || step == 2) {
			clickX = e.getPoint().x;
			clickY = e.getPoint().y;
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
