package clicker;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Made by icewormy3. If you are the billionare, fuck off.
 * 08/13/2024
 */
public class Clicker extends JFrame {
	
	private static final Dimension dimension = new Dimension(300, 300);
	
	private final Button click = new Button();
	private final List<Double> clickTimings = new ArrayList<Double>();
	private long lastClickTime;
	private boolean startTimer;
	private int countdownTicks = 100;
	
	public static void main(String[] args) {
		new Clicker();
	}
	
	private Clicker() {
		setTitle("Clicker Test");
		setLayout(null);
		setSize(dimension);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(0, 0, 0, 50));
		click.setLabel("Clicks: 0");
		click.setBounds(63, 50, 150, 150);
		click.addActionListener((a) -> {
			onButtonClick();
		});
		add(click);
		setVisible(true);
		setIgnoreRepaint(false);
		new Timer(45, (action) -> {
			tick();
		}).start();
	}
	
	private void onButtonClick() {
		long clickTime = System.currentTimeMillis();
		double cps = new BigDecimal(1000.0D / (clickTime - lastClickTime)).setScale(1, RoundingMode.HALF_UP).doubleValue();
		lastClickTime = clickTime;
		clickTimings.add(clickTimings.isEmpty() ? 1.0D : cps);
		startTimer = true;
		click.setLabel("Clicks: " + clickTimings.size());
	}
	
	private void tick() {
		if(!startTimer)
			return;
		repaint();
		if(countdownTicks == 0) {
			remove(click);
			Button restart = new Button();
			restart.setLabel("Restart");
			restart.setBounds(50, dimension.height - 100, 50, 50);
			Button quit = new Button();
			quit.setLabel("Quit");
			quit.setBounds(200, dimension.height - 100, 50, 50);
			quit.addActionListener((action) -> System.exit(-1));
			add(quit);
			restart.addActionListener((action) -> {
				startTimer = false;
				clickTimings.clear();
				countdownTicks = 100;
				lastClickTime = 0L;
				remove(restart);
				remove(quit);
				click.setLabel("Clicks: 0");
				add(click);
				repaint();
			});
			add(restart);
		} 
		countdownTicks--;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;
		Graphics2D last = (Graphics2D) graphics.create();
		if(countdownTicks >= 0) {
			graphics.translate(10, 50);
			graphics.scale(1.5, 1.5);
			graphics.drawString("Timer: " + countdownTicks / 20.0D, 0, 0);
			graphics = last;
			double cps = clickTimings.isEmpty() ? 0.0D : clickTimings.getLast();
			graphics.drawString("CPS: " + cps, (dimension.width / 2) - (graphics.getFontMetrics().stringWidth("CPS: " + cps) / 1.5F), dimension.height - 50);
		} else {
			double cpsAverage = 0.0D;
			for(double cps: clickTimings) {
				cpsAverage += cps;
			}
			graphics.scale(2, 2);
			graphics.drawString("Game Over!", 43, 40);
			graphics = last;
			cpsAverage = new BigDecimal(cpsAverage / clickTimings.size()).setScale(1, RoundingMode.HALF_UP).doubleValue();
			graphics.drawString("CPS Average: " + cpsAverage, 100, 120);
			graphics.drawString("Clicks: " + clickTimings.size(), 120, 140);
		}
	}
}
