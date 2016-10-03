package view;

import interfaces.Observable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import object.AttackGraph;
import object.OrientedEdge;
import object.EdgeKind;

public class DetailsPanel extends JPanel implements Observable{
	
	//R(x; y), G (y ;z), B(z;x), U(x;u), V (x, u; v)
	//R(x;y,u), T(u;x,z)
	private AttackGraph g=null;
	
    
	
	public DetailsPanel(int width, int height){
		setPreferredSize(new Dimension(width, 3*height));	
		TitledBorder title= new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), "Details");
		setBorder(title);
		
		
		
	}

	@Override
    public void paintComponent(Graphics g2) {		
		
		if(g!=null){
					
					Graphics2D gg= (Graphics2D) g2;
					gg.setColor(Color.WHITE);
			        gg.fillRect(0, 0, this.getWidth(), this.getHeight());
			        
			        gg.setColor(Color.BLACK);
					if(g.isFOComplexity()){
						gg.drawString("Cycles: no cycle detected", 10,40);
						
					}
					
					else{
						boolean isweakcycle=false, isstrongcycle=false;
						int startx=10, starty=40;
						gg.drawString("Weak cycles : ", startx,starty);
						startx=110; starty=42;
						for(ArrayList<OrientedEdge> cycle : g.getCycles().keySet()){
							if(g.getCycles().get(cycle)==EdgeKind.WEAK){
								isweakcycle=true;
								gg.drawString("*"+(g.toStringCycle(cycle)), startx,starty);
								starty+=20;
							}
							
						}
						if(!isweakcycle){
							int width = g2.getFontMetrics().stringWidth("Weak cycles : ");
							startx=10+width+5; starty=40;
							gg.drawString("no weak cycle detected",startx ,starty);
						}
							
						startx=10;starty+=20;	
						
						gg.drawString("Strong cycles : ",startx,starty);
						startx+=10;starty+=15;
						for(ArrayList<OrientedEdge> cycle : g.getCycles().keySet()){
							if(g.getCycles().get(cycle)==EdgeKind.STRONG){
								isstrongcycle=true;
								gg.drawString("*"+(g.toStringCycle(cycle)), startx,starty);
								starty+=20;
							}
							
						}
						if(!isstrongcycle){
							int width = g2.getFontMetrics().stringWidth("Strong cycles : ");
							startx=10+width+5;starty-=15;
							gg.drawString("no Strong cycle detected",startx,starty);
						}
							
					}
					
				}
		
	}
	@Override
	public void setView(int compl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(String request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(AttackGraph g) {
		// TODO Auto-generated method stub
		this.g=g;
		repaint();
	}
	

}
