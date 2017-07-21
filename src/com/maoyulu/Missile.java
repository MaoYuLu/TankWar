package com.maoyulu;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.rmi.transport.LiveRef;

/**
 * 子弹的类
 * @author Administrator
 *
 */
public class Missile {	
	/*
	 * 将子弹的x轴运动速度xspeed,y轴运动速度yspeed,子弹的宽和高, 定义为常量
	 */
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	/*
	 * 定义子弹的位置x,y 引用坦克的方向dir,大管家tc
	 * 定义子弹是否活着live和子弹是否是好的good
	 */
	int x, y;
	Direction dir;	
	private boolean live = true;
	private boolean good = true;
	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Map<String, Image> imgs =  new HashMap<String, Image>();
	private static Image missileImages[] = null;
	static {
		missileImages = new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLD.gif"))	
		};
		imgs.put("L", missileImages[0]);
		imgs.put("LU", missileImages[1]);
		imgs.put("U", missileImages[2]);
		imgs.put("RU", missileImages[3]);
		imgs.put("R", missileImages[4]);
		imgs.put("RD", missileImages[5]);
		imgs.put("D", missileImages[6]);
		imgs.put("LD", missileImages[7]);
	}
	//子弹的构造函数
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	//子弹的构造函数
	public Missile(int x, int y, Direction dir, boolean good, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	//绘制子弹的方法
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		ptdir_move(g);
		move();
	}
	
	private void ptdir_move(Graphics g) {
		switch(dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}
	}

	//子弹的移动方法
	private void move() {
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		//超出Frame的范围,子弹的live为false
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}

	/*
	 * 子弹打击坦克的类方法
	 *即可表示好子弹打坏坦克，也表示坏自担打好坦克
	 */
	public boolean hitTank(Tank t) {
		if( this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()){
				t.setLife(t.getLife()-20);
				if(t.life==0 ) {t.setLive(false);}
			}else {
				t.setLive(false);
			}
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
 		}
		return false;
	}
	
	/*
	 * 子弹打击坏坦克们的方法
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i))){
				return true;
			}
 		}
		return false;
	}
	
	
	/*
	 * 子弹打击墙壁的类
	 */
	public boolean hitWall(Wall wall) {
		if(this.live && this.getRect().intersects(wall.getRect())) {
			this.live = false;
			return true;
		}
		return false;		
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isLive() {
		return live;
	}
	
	
}
