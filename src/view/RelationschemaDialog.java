package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import object.ConjonctiveRequest;
import object.RelationSchema;

public class RelationschemaDialog extends JDialog{
	private ConjonctiveRequest q;
	private ArrayList<RelationSchema> schemas;
	private GridJPanelSchema [] tabJpanel;
	private static RelationschemaDialog dialogSchema=null;
	
	private RelationschemaDialog(JFrame parent, String title, boolean modal,ConjonctiveRequest request) {
	    super(parent, title, modal);
		this.q = request;
		this.setSize(450, 600);
		this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.initComponent();
	}
	
	public ArrayList<RelationSchema> getSchemas() {
		
		return schemas;
	}
    public static RelationschemaDialog getInstance(JFrame parent, String title, boolean modal,ConjonctiveRequest request){
    	if(dialogSchema==null)
    		dialogSchema=new RelationschemaDialog(parent, title, modal, request);
    	else{
    		if(!dialogSchema.q.toString().split("<--")[1].equals(request.toString().split("<--")[1]))
    			dialogSchema=new RelationschemaDialog(parent, title, modal, request);
    		else
    		dialogSchema.setVisible(true);
    	}
    		
    	return dialogSchema;
    }
	private void initComponent(){
		
		   JPanel control = new JPanel();
		    JButton okBouton = new JButton("Validate");
		    okBouton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					int i = 0;
					schemas=new ArrayList<>();
					for (; i < tabJpanel.length; i++) {
						
						if(tabJpanel[i].getAttributes()==null)
							break;
						schemas.add(new RelationSchema(q.getAtoms().get(i).getName(), 
								tabJpanel[i].getAttributes(), tabJpanel[i].getTypeAttributes(), 
								q.getAtoms().get(i).getKeySize()));
					}
					if(i>=tabJpanel.length)
						setVisible(false);
					else
						schemas=null;
					
				}
			});
		    JButton cancelBouton = new JButton("Cancel");
		    cancelBouton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					schemas=null;
					setVisible(false);
					//dispose();
				}
		      });
		    control.add(okBouton);
		    control.add(cancelBouton);
		    
		    int lines=q.getAtoms().size();
		    tabJpanel=new GridJPanelSchema[lines];
		    JPanel content = new JPanel();
		    content.setBackground(Color.white);
		    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		    for (int i = 0; i < lines; i++) {
				tabJpanel[i]=new GridJPanelSchema(q.getAtoms().get(i));
				//content.add(new JLabel("-"+q.getAtoms().get(i).getName()));
				content.add(tabJpanel[i]);
				
				
			}
		    
		    JScrollPane scroll=new JScrollPane(content);
		    this.getContentPane().add(scroll, BorderLayout.CENTER);
		    this.getContentPane().add(control, BorderLayout.SOUTH);
		    setVisible(true);
		    
		
	}

}
