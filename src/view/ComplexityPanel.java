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

public class ComplexityPanel extends JPanel implements Observable{
	
	private  String complexities []= {"FO", "P", "coNP-complet"};
	private int indexComplexity=-1;
	
	
	public ComplexityPanel(int width, int height){
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		TitledBorder title= new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), "Complexity class");
		setBorder(title);
		
		
	}
	//ABUS("Pierre",c,m;),VINS(c,m;"A")
	private void setComplexity(int complexity) {
		this.indexComplexity = complexity;
		repaint();
	}


	@Override
    public void paintComponent(Graphics g) {
        
		Graphics2D g2= (Graphics2D) g;
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        int h=getHeight()/5;
        int w=getWidth()/4;
        int starty=h;
        int startx=w;
        
        
        // Définir la police
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		g2.setFont(f);
		
		g2.setColor(Color.GRAY);
        
        for (int i = 0; i < complexities.length; i++) {
        	starty+=h;
        	if(i==indexComplexity){
        		
        		g2.setColor(Color.BLACK);
        		g2.fillOval(startx, starty, 5, 5);
            	g2.drawString(complexities[i], startx+7, starty+7);
            	g2.setColor(Color.GRAY);
        	}
        	else{
        		
        		g2.fillOval(startx, starty, 5, 5);
            	g2.drawString(complexities[i], startx+7, starty+7);
        		
        	}
        	
			
		}
    }

	

	@Override
	public void setView(int compl) {
		// TODO Auto-generated method stub
		setComplexity(compl);
		
	}

	@Override
	public void setView(String request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(AttackGraph g) {
		// TODO Auto-generated method stub
		
	}

}
