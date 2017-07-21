package com.maoyulu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * ǽ����
 * @author Administrator
 *
 */
public class Wall {
	
	/*
	 * ����ǽ�ڵ�x,y,w,h����ܼ�tc
	 */
	int x, y, w, h;
	TankClient tc;
	
	
	//ǽ�ڵĹ��캯��
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	//���ƺ���
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
