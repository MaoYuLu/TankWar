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
 * ̹����Ϸ��ʾ����ҳ��
 * @author Administrator
 *
 */
public class TankClient extends Frame{
	/*
	 * ������Ϸ�����Ⱥ͸߶ȳ���
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
	
	//���Ʒ���
	public void paint(Graphics g) {
		
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString("missile count:" +missiles.size(), 10, 50);
		g.drawString("explodes count:" + explodes.size(), 10, 70);
		g.drawString("tanks    count:" + tanks.size(), 10, 90);
		g.drawString("tanks     life:" + mytank.getLife(), 10, 110);		
		g.setColor(c);

		//��ѭ����ʾ�ӵ����ϣ������ӵ������̹�ˡ���̹���ǡ�ǽ�ڵĺ���
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(mytank);
			m.hitWall(wall1);
			m.hitWall(wall2);
			m.draw(g);
		}
		
		//��ѭ����ʾ��ը����
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		
		
		//��ѭ����tanks����ӻ�̹��
		if(tanks.size() <= 0) {
			for(int i=0; i<Integer.parseInt(ProperMgr.getProperty("reProduceTankCount")); i++) {
				tanks.add(new Tank(50 + 40*(i+1), 50, false, Direction.D, this));
			}
		}
		
		//��ѭ����ʾ��̹�˼��ϣ�����̹��ײǽ��̹��ײ̹�˵ĺ���		
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
	
	//������ʾ����
	public void launchFrame(String s) {
		int initTankCount = Integer.parseInt(ProperMgr.getProperty("initTankCount"));
		//��ѭ����tanks����ӻ�̹��
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
	
	
	//���̼�����
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			mytank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			mytank.keyPressed(e);
		}
		
	}

	
	//ʹ��˫����������˸����
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

	//�滭�߳�
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
