package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.IDBConnection;

public class ConnectionDBDialog extends JDialog {
	
	
	
	private ArrayList<JTextField> listJtext;
	private JPasswordField jtPassword;
	private String [] tabTypeDb={" ","MYSQL"};
	
	private String [] tablabel={"Database type: ","Database name: ", "Address: ", "Username: ",
			"Password"};
	private String typeDb=null;
	
	//private IDBConnection connect=null;
	
	
	public ConnectionDBDialog(JFrame parent, String title, boolean modal){
		
		super(parent, title, modal);		
		this.setSize(450, 400);
		this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.initComponent();
		
	}
	
	
	public String getDbName() {
		return listJtext.get(0).getText().trim();
	}


	public IDBConnection getConnect() {
		return null;
	}


	private void initComponent(){
		
		
		JPanel control = new JPanel();
		JButton BConn = new JButton("Connection");
		JButton BCancel = new JButton("Cancel");
		BCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		BConn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		BCancel.setPreferredSize(new Dimension(120, 30));
		BConn.setPreferredSize(new Dimension(120, 30));
		control.add(BCancel);
		control.add(BConn);
	    
		JPanel content = new JPanel();
	    content.setBackground(Color.white);
	    content.setLayout(new GridLayout(5, 2, 10, 45));
		
		
		listJtext=new ArrayList<>();
		for (int i = 0; i < tablabel.length; i++) {
			
			//JPanel panel=new JPanel();
			content.add(new JLabel(tablabel[i]));
			if(i==0){
				JComboBox<String> jcombo=new JComboBox<>(tabTypeDb);
				jcombo.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JComboBox cb = (JComboBox)e.getSource();
				        typeDb = (String)cb.getSelectedItem();
				        
				        System.out.println(typeDb);
						
					}
				});
				jcombo.setPreferredSize(new Dimension(200, 30));
				content.add(jcombo);
				
			}
			else if(i==4){
				jtPassword=new JPasswordField();
				jtPassword.setPreferredSize(new Dimension(200, 30));
				content.add(jtPassword);
			}
			else{
				listJtext.add(new JTextField());
			    listJtext.get(listJtext.size()-1).setPreferredSize(new Dimension(200, 30));
				content.add(listJtext.get(listJtext.size()-1));
				
			}
			
			
		}
		content.setPreferredSize(new Dimension(300, 350));
		
		JPanel panelWest=new JPanel();		
		panelWest.setPreferredSize(new Dimension(75, 350));
		
		JPanel paneleast=new JPanel();
		paneleast.setPreferredSize(new Dimension(75, 350));
		
		JPanel panelnorth=new JPanel();
		JLabel jlDefaultdb=new JLabel("Use default SQLite database");
		jlDefaultdb.setForeground(Color.blue);
		Font font = jlDefaultdb.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		jlDefaultdb.setFont(font.deriveFont(attributes));
		jlDefaultdb.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		       
		       
		       new CqaView(null, null);
		       dispose();

		    }  
		}); 
		panelnorth.setPreferredSize(new Dimension(450, 30));
		panelnorth.add(jlDefaultdb, BorderLayout.WEST);
		
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(panelWest, BorderLayout.WEST);
		this.getContentPane().add(panelnorth, BorderLayout.NORTH);
		this.getContentPane().add(paneleast, BorderLayout.EAST);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
		setVisible(true);
		
	}
	
	public static void main( String args[] ){
		new ConnectionDBDialog(null, "", true);
	}

}
