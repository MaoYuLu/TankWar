package com.maoyulu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;


public class Tank {
	/*
	 * 将坦克的x轴运动速度xspeed,y轴运动速度yspeed,坦克的宽和高,吃一个血包的补充血量 定义为常量
	 */
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int EATBLOOD = 50;
	
	/*
	 * 定义坦克的位置x、y,上一个位置的oldX、oldY,大管家tc
	 * 定义坦克是否活着live和坦克是否是好的good, 定义生命值life的值
	 * 生成一个随机函数,并生成一个随机的部数step
	 */
	private int x, y;
	TankClient tc;
	private int oldX, oldY;
	private boolean live = true;
	private boolean good =true;
	public int life = 100;	
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	private BloodBar bb = new BloodBar();
	
	/*
	 * 定义判断四个方向的是否按下的boolean类型值
	 * 定义坦克方向和炮筒方向的默认值
	 */
	private boolean bL=false, bU=false, bR=false, bD = false;
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	//定义一个图片数组实现图片化
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Map<String, Image> imgs =  new HashMap<String, Image>();
	private static Image tankImages[] = null;
	static {
		tankImages = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))	
		};
		imgs.put("L", tankImages[0]);
		imgs.put("LU", tankImages[1]);
		imgs.put("U", tankImages[2]);
		imgs.put("RU", tankImages[3]);
		imgs.put("R", tankImages[4]);
		imgs.put("RD", tankImages[5]);
		imgs.put("D", tankImages[6]);
		imgs.put("LD", tankImages[7]);
	}
	
	//坦克的构造方法
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	//坦克的构造方法
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}

	//绘制方法
	public void draw(Graphics g) {
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		
		if(good) bb.draw(g);
			//炮筒的方向
			switch(ptDir) {
			case L:
				g.drawImage(imgs.get("L"), x, y, null);
				break;
			case LU:
				g.drawImage(imgs.get("LU"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("U"), x, y, null);
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
				g.drawImage(imgs.get("D"), x, y, null);
				break;
			case LD:
				g.drawImage(imgs.get("LD"), x, y, null);
				break;
			}
		
		move();		
	}
	
	
	
	//使坦克向八个方向移动的函数
	void move() {
		this.oldX = x;
		this.oldY = y;
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
		//处理坦克不是停止状态，都让炮筒的方向和坦克的方向一致
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		//确定坦克可以运动的边界
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		//实现坏坦克的自由运动
		if(!good) {
			Direction dirs[] = Direction.values();
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40) > 38) this.fire();
		}
	}
	
	//将上一个位置的x,y传递给现在的位置
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	//监听按下的按键来确定是那个方向的移动
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2:
			if(!this.live) {
				setLive(true);
				setLife(100);
			}
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}


	//键盘监听器的抬起按键的方法
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_CONTROL:
			 fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		}
		locateDirection();		
	}

	/**
	 * 炮弹方法
	 * @return 返回一个子弹类型的m值
	 */
	public Missile fire() {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir, good, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * 有方向的炮弹方法
	 * @param 坦克的方向
	 * @return 返回一个子弹类型的m值
	 */
	public Missile fire(Direction dir) {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, dir, good, this.tc);
		tc.missiles.add(m);
		return m;		
	}
	
	//超级炮弹，可以多个方位发射炮弹
	private void superFire() {
		Direction dirs[] = Direction.values();
		for(int i=0; i<dirs.length; i++) {
			fire(dirs[i]); 
		}
	}
	
	/**
	 * 坦克撞击墙壁的方法，如果碰到调用stay(),回到碰到前的一个位置
	 * @param wall 墙壁
	 */
	public boolean collidesWithWall(Wall wall) {
		if(this.live && this.getRect().intersects(wall.getRect())) {
			this.stay();
			return true;
		}
		return false;				
	}
	
	/**
	 * 坏坦克撞击坏坦克的方法，如果碰到调用stay(),回到碰到前的一个位置
	 * @param tanks 坏坦克集
	 */
	public boolean collidesWithTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				if(this.live && this.getRect().intersects(t.getRect()) && t.isLive()) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;		
	}
	
	//按下一个或者两个按钮所实际表示的方向
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	//坦克血量条的显示
	private class BloodBar {
		public void draw(Graphics g) {			
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * life /100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}	
	}
	
	/*
	 * 坦克吃血包的方法
	 */
	public boolean eat(Blood b) {
		if(this.live && this.getRect().intersects(b.getRect()) && b.isLive()) {
			if((this.life) >50){
				this.life =100;
			}else {
				this.life += EATBLOOD;
			}
			b.setLive(false);
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

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

}
