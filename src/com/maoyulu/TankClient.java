package com.maoyulu;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.omg.CORBA.TCKind;

import com.sun.prism.paint.Gradient;


/**
 * 坦克游戏显示的主页面
 * @author Administrator
 *
 */
public class TankClient extends Frame{
	/*
	 * 定义游戏界面宽度和高度常量
	 */
	public static int GAME_WIDTH = 800;
	public static int GAME_HEIGHT = 600;
	
	Tank mytank = new Tank(50, 50, true, Direction.STOP, this);
	List<Tank> tanks = new ArrayList<Tank>();
	Blood b = new Blood();
 	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	
	Wall wall1 = new Wall(300, 400, 200, 30, this);
	Wall wall2 = new Wall(600, 180, 30, 200, this);
	
	//绘制方法
	public void paint(Graphics g) {
		
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString("missile count:" +missiles.size(), 10, 50);
		g.drawString("explodes count:" + explodes.size(), 10, 70);
		g.drawString("tanks    count:" + tanks.size(), 10, 90);
		g.drawString("tanks     life:" + mytank.getLife(), 10, 110);		
		g.setColor(c);

		//用循环显示子弹集合，调用子弹打击好坦克、坏坦克们、墙壁的函数
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(mytank);
			m.hitWall(wall1);
			m.hitWall(wall2);
			m.draw(g);
		}
		
		//用循环显示爆炸集合
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		
		
		//用循环往tanks里添加坏坦克
		if(tanks.size() <= 0) {
			for(int i=0; i<Integer.parseInt(ProperMgr.getProperty("reProduceTankCount")); i++) {
				tanks.add(new Tank(50 + 40*(i+1), 50, false, Direction.D, this));
			}
		}
		
		//用循环显示坏坦克集合，调用坦克撞墙和坦克撞坦克的函数		
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(wall1);
			t.collidesWithWall(wall2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}

		mytank.draw(g);
		mytank.eat(b);
		wall1.draw(g);
		wall2.draw(g);
		b.draw(g);
	}
	
	//窗口显示方法
	public void launchFrame(String s) {
		int initTankCount = Integer.parseInt(ProperMgr.getProperty("initTankCount"));
		//用循环往tanks里添加坏坦克
		for(int i=0; i<initTankCount; i++) {
			tanks.add(new Tank(50 + 40*(i+1), 50, false, Direction.D, this));
		}
		
		setTitle(s);
		setBounds(100, 100, GAME_WIDTH, GAME_HEIGHT);
		setBackground(Color.BLACK);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setResizable(false);
		setVisible(true);
		
		this.addKeyListener(new KeyMonitor());		
		new Thread(new PaintThread()).start();
	}
	
	
	//键盘监听类
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			mytank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			mytank.keyPressed(e);
		}
		
	}

	
	//使用双缓冲消除闪烁现象
	Image offScreenImage = null;
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	//绘画线程
	private class PaintThread implements Runnable {
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame("TankWar");
 	}

}
