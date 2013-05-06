package fr.iso3d.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

public class JImage extends JLayeredPane {
	private static final long serialVersionUID = -9103911430599172234L;
	
	/**
	 * Image affichée.
	 */
	private BufferedImage image;
	
	/**
	 * Position verticale de l'image
	 */
	private int x;
	
	/**
	 * Position horizontale de l'image
	 */
	private int y;
	
	/**
	 * Dimensions du cadre
	 */
	private Dimension dimensions;
	
	// Accesseurs
	
	public void setImage(String image) throws IOException {
		setImage(ImageIO.read(new File(image)));
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
		if(image != null) {
			setDimensions(new Dimension(image.getWidth(), image.getHeight()));
		}
	}
	
	public Dimension getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Dimension dimensions) {
		this.dimensions = dimensions;

		setSize(dimensions);
		setMinimumSize(dimensions);
		setMaximumSize(dimensions);
		setPreferredSize(dimensions);
	}

	public int getXPos() {
		return x;
	}
	
	public void setXPos(int x) {
		this.x = x;
	}

	public int getYPos() {
		return y;
	}
	
	public void setYPos(int y) {
		this.y = y;
	}
	
	// Constructeurs
	
	public JImage(BufferedImage image)
	{
	    setOpaque(true);
	    setBackground(new Color(0,0,0,0));
	    setImage(image);
	    x = 0;
	    y = 0;
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(image, x, y, null);
		
		// Dessinne les nœuds fils
		paintChildren(g2);
	}
	

}
