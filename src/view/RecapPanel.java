package view;

import interfaces.Observable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import object.AttackGraph;

public class RecapPanel extends JPanel implements Observable{
	
	private String request=null;
	private String dbSelected="none";
	
	public void setDbSelected(String dbSelected) {
		if(dbSelected==null)
			this.dbSelected = "none";
		else
			this.dbSelected = dbSelected;
		repaint();
	}

	public RecapPanel(int width, int height){
		
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		//FILMS(x;y,z,w),PRODUCTIONS(x;"Will SMITH",b),	ROLES(x,p;"Idriss ELBA","Principal"),
		TitledBorder title= new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), "Recap request");
		setBorder(title);
		
	}
	
	public void setRequest(String request) {
		this.request = request;
		repaint();
	}

	@Override
    public void paintComponent(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        int startx=20, starty=40, lengthLine=30;
        Font f = new Font(Font.SERIF, Font.BOLD, 15);
		g2.setFont(f);
    	g2.setColor(Color.BLACK);
    	g2.drawString("Selected database: "+dbSelected, startx, starty);
        if(request!=null){
        	
        	String []tab=request.split("<--");
        	starty=65;
        	g2.setFont(new Font(Font.SERIF, Font.BOLD, 13));
        	g2.drawString(tab[0], startx, starty);
        	int width = g2.getFontMetrics().stringWidth(tab[0]);
        	g2.drawLine(startx+width+10, starty-4,startx+width+10+lengthLine , starty-4);
        	g2.drawLine(startx+width+10, starty-4,startx+width+10+5 , starty+1);
        	g2.drawLine(startx+width+10, starty-4,startx+width+10+5 , starty-9);
        	g2.drawString(tab[1], startx+width+10+lengthLine+10, starty);
        }
		
	}

	@Override
	public void setView(int compl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(String request) {
		// TODO Auto-generated method stub
		this.request=request;
		repaint();
	}

	@Override
	public void setView(AttackGraph g) {
		// TODO Auto-generated method stub
		
	}

}
