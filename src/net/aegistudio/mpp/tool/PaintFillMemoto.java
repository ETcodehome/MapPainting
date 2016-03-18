package net.aegistudio.mpp.tool;

import java.awt.Color;

import net.aegistudio.mpp.Memoto;
import net.aegistudio.mpp.canvas.Canvas;

/**
 * The paint memoto is not fully revertable.
 * Will be fixed in the follow versions.
 * 
 * @author aegistudio
 */

public class PaintFillMemoto implements Memoto {
	private final Canvas canvas;
	private final int i, j;
	private final Color fillColor;
	private final String fillMessage;
	public PaintFillMemoto(Canvas canvas, int i, int j, Color c, String fillMessage) {
		this.canvas = canvas;
		this.i = i; this.j = j;
		this.fillColor = c;
		this.fillMessage = fillMessage;
	}
	
	private Color seedColor;
	
	@Override
	public void exec() {
		seedColor = this.canvas.look(i, j);
		this.seedFill(canvas, i, j, fillColor, seedColor);
	}

	public String toString() {
		if(this.fillMessage == null) return null;
		return fillMessage.replace("$x", Integer.toString(i))
				.replace("$y", Integer.toString(j))
				.replace("$r", Integer.toString(fillColor.getRed()))
				.replace("$g", Integer.toString(fillColor.getGreen()))
				.replace("$b", Integer.toString(fillColor.getBlue()));
	}
	
	@Override
	public void undo() {
		Color postColor = this.canvas.look(i, j);
		this.seedFill(canvas, i, j, seedColor, postColor);
	}
	
	private void seedFill(Canvas canvas, int x, int y, Color fill, Color seed) {
		if(fill == null || seed == null) return;
		if(fill.getRGB() == seed.getRGB()) return;
		if(!inRange(canvas, x, y)) return;
		
		canvas.paint(x, y, fill);
		
		int xmin = x - 1;
		for(; xmin >= 0; xmin --) {
			Color color = canvas.look(xmin, y);
			if(color == null) break;
			if(color.getRGB() != seed.getRGB()) break;
			canvas.paint(xmin, y, fill);
		}
		
		int xmax = x + 1;
		for(; xmax <= canvas.size(); xmax ++) {
			Color color = canvas.look(xmax, y);
			if(color == null) break;
			if(color.getRGB() != seed.getRGB()) break;
			canvas.paint(xmax, y, fill);
		}
		
		for(int p = xmin + 1; p < xmax; p ++) {
			Color up = canvas.look(p, y + 1);
			if(up != null) {
				if(up.getRGB() == seed.getRGB()) 
					seedFill(canvas, p, y + 1, fill, seed);
			}
			
			Color down = canvas.look(p, y - 1);
			if(down != null) {
				if(down.getRGB() == seed.getRGB())
					seedFill(canvas, p, y - 1, fill, seed);
			}
		}
	}
	
	private boolean inRange(Canvas c, int i, int j) {
		if(i < 0) return false;
		if(j < 0) return false;
		if(i >= c.size()) return false;
		if(j >= c.size()) return false;
		return true;
	}
}
