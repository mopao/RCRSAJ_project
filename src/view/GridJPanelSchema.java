package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import object.Atom;
import object.RelationSchema;

public class GridJPanelSchema extends JPanel {
	private ArrayList<JTextField> tabJtext;
	private ArrayList<JComboBox > tabJcombo;
	private ArrayList<String> attributes, typeAttributes;
	private Atom r;
	private final String [] datatypes={"NONE", "TEXT","REAL","INTEGER","NUMERIC"};
	
	public GridJPanelSchema(Atom atom){
		super();
		TitledBorder title= new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), "Schema of relation "+atom.getName());
		setBorder(title);
		r=atom;
		tabJcombo=new ArrayList<>();
		tabJtext=new ArrayList<>();
		this.initComponent();
		//setBackground(Color.gray);
	}
	
	private void initComponent(){
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (int i = 0; i < r.getSize(); i++) {
			
			JTextField jtext=new JTextField();
			JComboBox jcombo=new JComboBox<>(datatypes);			
			tabJtext.add(jtext);
			tabJcombo.add(jcombo);
			
		}
		//VINS(c,m;q)
		for (int i = 0; i < r.getSize(); i++) {
			JPanel panel=new JPanel();
			panel.add(new JLabel(r.getAttributes().get(i).toString()+": "));
			tabJtext.get(i).setPreferredSize(new Dimension(200, 30));
			panel.add(tabJtext.get(i));
			panel.add(tabJcombo.get(i));
			panel.setPreferredSize(new Dimension(400, 40));
			add(panel);
			
		}
		
	}
	
	public ArrayList<String> getAttributes(){
		attributes=new ArrayList<>();
		for (int i = 0; i < tabJtext.size(); i++) {
			if(tabJtext.get(i).getText().trim().isEmpty())
				return null;
			attributes.add(tabJtext.get(i).getText().trim());
			
		}
		
		return attributes;
	}
	
	public ArrayList<String> getTypeAttributes(){
		typeAttributes=new ArrayList<>();
		for (int i = 0; i < tabJcombo.size(); i++) {
			
			typeAttributes.add((String)tabJcombo.get(i).getSelectedItem());
			
		}
		
		return typeAttributes;
	}
	
	

}
