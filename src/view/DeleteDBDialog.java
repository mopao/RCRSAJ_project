package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.DBManager;

/**
 * boîte de dialogue permettant de supprimer des bases de données
 * @author franck
 *
 */
public class DeleteDBDialog extends JDialog {

	private DBManager dbManager;
	private ArrayList<String> listdbToDelete;
	private String [] dbNames;
	private JCheckBox []items;

	public DeleteDBDialog(JFrame parent, String title, boolean modal,DBManager dbManager) throws ClassNotFoundException, SQLException{
	    super(parent, title, modal);
		this.dbManager = dbManager;
		this.setSize(450, 170);
		this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.initComponent();
	}
	
	private void initComponent() throws ClassNotFoundException, SQLException{
		
	   JPanel control = new JPanel();
	    JButton okBouton = new JButton("OK");
	    okBouton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(listdbToDelete.size()>0){
					try {
						dispose();
						dbManager.removeDb(listdbToDelete);
						
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    e.getClass().getSimpleName(),
							    JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
		});
	    JButton cancelBouton = new JButton("Cancel");
	    cancelBouton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//setVisible(false);
				dispose();
			}
	      });
	    
	    control.add(okBouton);
	    control.add(cancelBouton);
	    
	    JPanel content = new JPanel();
	    content.setBackground(Color.white);
	    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        listdbToDelete=new ArrayList<String>();
		dbNames=dbManager.getDbNames();
		items=new JCheckBox[dbNames.length];
		for (int i = 0; i < dbNames.length; i++) {
			items[i]=new JCheckBox(dbNames[i]);
			items[i].addItemListener(new CheckboxListener(dbNames[i]));
			content.add(items[i]);
		}
	    JScrollPane scroll=new JScrollPane(content);
	    this.getContentPane().add(scroll, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	    setVisible(true);
	}

	public ArrayList<String> getListdbToDelete() {
		return listdbToDelete;
	}
	
	private class CheckboxListener implements ItemListener{
	      
		private String item;
		public CheckboxListener(String item) {
			super();
			this.item = item;
		}
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange()==1){
				listdbToDelete.add(item);
			   
			}
			else{
				listdbToDelete.remove(item);
				
				
			}
			
		}
		
	}
	
}
