package com.game;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class WaterTank2 extends Frame implements Runnable {

	private static final int TOTAL_CAPACITY = 300;
	private static final int INLET = 10;
	private static final int OUTLET = 15;

	int bottomLevel = 200, height = 200, currentCapacity = 280;
	int percent = 70;
	private Thread inletThread;
	private Thread outletThread;
	private Thread controller;

	public WaterTank2() {
		super("Water Tank");
		setBackground(Color.CYAN);
		setSize(500, 500);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		inletThread = new Thread(this, "INLET");
		outletThread = new Thread(this, "OUTLET");
		controller = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (height <= 240) {
							System.out.println("Outlet stop");
							outletThread.suspend();
							inletThread.resume();
						} else if (height >= 260) {
							System.out.println("Inlet stop");
							inletThread.suspend();
							outletThread.resume();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, "Controller");
		controller.setDaemon(true);
		inletThread.start();
		outletThread.start();
		// controller.start();
	}

	public void paint(Graphics g) {
		g.drawRect(150, 100, 200, 300);
		g.setColor(Color.RED);
		g.fillRect(150, bottomLevel, 200, height);
		g.drawString(Integer.toString(TOTAL_CAPACITY - height) + "%", 380, 200);
	}

	public static void main(String[] args) {
		Frame mf = new WaterTank2();
		mf.setSize(500, 500);
		mf.setVisible(true);
	}

	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Thread currentThread = Thread.currentThread();
		while (true) {
			if (currentThread.getName().equals("INLET")) {
				if (height < TOTAL_CAPACITY) {
					bottomLevel -= INLET;
					height += INLET;
					// percent = TOTAL_CAPACITY - height;
					// System.out.println(bottomLevel);
					System.out.println("I:" + height);
				}
			} else {
				if (height > 0) {
					bottomLevel += OUTLET;
					height -= OUTLET;
					// percent -= 10;
					// System.out.println(bottomLevel);
					System.out.println("O:" + height);
					sleep(1000);
					repaint();
				}
			}
			/*synchronized (this) {
				if (height <= 240) {
					try {
						System.out.println("Wait");
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}*/
			sleep(1000);
			repaint();
		}
	}
}
