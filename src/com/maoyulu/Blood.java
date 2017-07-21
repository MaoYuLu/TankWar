package com.maoyulu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


/**
 * ����Ѫ���ĺ���
 * @author Administrator
 *
 */
public class Blood {
	
	/*
	 * Ѫ����x,y,w,h�� ��ܼ�tc,Ѫ���˶���step, ��ʾѪ���Ƿ����ŵ�live
	 * Ѫ�����ƶ��켣pos[][]
	 */
	int x, y, w, h;
	TankClient tc;
	int step = 0;
	private boolean live =true;
	private int[][] pos = {
	          {350, 300}, {60, 30}, {175, 205}, {40, 200}, {360, 70}, {365, 290}, {40, 280}
			  };
	
	//Ѫ���Ĺ��캯��
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	
	//����Ѫ��
	public void draw(Graphics g) {
		if(!live) return;		
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	
	//Ѫ�����ƶ�
	private void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	
}
