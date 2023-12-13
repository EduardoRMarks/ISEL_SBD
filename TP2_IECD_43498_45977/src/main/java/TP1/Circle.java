package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

/**
 * Classe auxiliar do 4 em linha. Criação de círculos.
 */
public class Circle extends JComponent {
	private static final long serialVersionUID = 1L;
	private int radius;
	private Color color;
	private Point position;

	public Circle(int radius, Color color) {
		this.radius = radius;
		this.color = color;
		position = new Point(0, 0);
	}

	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
		repaint();
	}

	public void setColor(Color color) {
		this.color = color;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillOval(position.x - radius, position.y - radius, radius * 2, radius * 2);
	}
}
