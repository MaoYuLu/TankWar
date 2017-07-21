package com.maoyulu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 墙壁类
 * @author Administrator
 *
 */
public class Wall {
	
	/*
	 * 定义墙壁的x,y,w,h。大管家tc
	 */
	int x, y, w, h;
	TankClient tc;
	
	
	//墙壁的构造函数
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	//绘制函数
	public void draw(Graphics g) {		
		Color c = g.getColor();
		g.setColor(Color.darkGray);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
}
