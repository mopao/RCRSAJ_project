package view;

import factory.BadRequestException;
import factory.RequestError;
import factory.RequestParser;
import interfaces.Observable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import object.AttackGraph;
import object.ConjonctiveRequest;
import request.rewriting.RequestManager;
import database.DBManager;

public class CqaView extends JFrame implements Observable{
	
	JTextField head;
	JTextArea  body;
	JLabel msg,j_head, j_body;
	
	JButton b_excute=new JButton("Compute complexity");
	
	private JScrollPane scroll;
	private ComplexityPanel panComplexity;	
	private DetailsPanel panDetails;
	private JPanel panCenter;
	private JScrollPane scrollTablePanel=null;
	private RecapPanel resumePanel;
	private JPanel centerPanel=new JPanel();
	private int w1=400, w2=600, h1=150, h2=360;
	JMenuBar menu_bar;
	JMenu menuFichier, menuEdition, menuRewriting;
	JMenuItem menuItem_loadDB, menuItem_displayanswer ,menuItem_chooseDB,menuItem_exit,
	menuItem_removeDB,menuItem_displayTable, menuItem_Rewdb,menuItem_Rewrc;
	
	
	private RequestParser parser=new RequestParser();
	private DBManager dbManager=null;
	private String dbSelected=null;
	private RequestManager rqManager=null;
	private ConjonctiveRequest q=null;
	private AttackGraph g=null;	
	public CqaView(){
		JComponent.setDefaultLocale(Locale.ENGLISH);	
		 try {
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (ClassNotFoundException ex) {
         } catch (InstantiationException ex) {
         } catch (IllegalAccessException ex) {
         } catch (UnsupportedLookAndFeelException ex) {
         }
        
		try {
			rqManager=new RequestManager();
			dbManager=new DBManager();
			dbManager.addObservable(this);
			
			setTitle("Consistent query answering");			
			/* Bar de menu */
		    menu_bar = new JMenuBar();
			/* différents menus */
			menuFichier = new JMenu("File");
			menuItem_loadDB=new JMenuItem("Load database");
			menuItem_chooseDB=new JMenuItem("Choose database");		
			menuItem_removeDB=new JMenuItem("Remove database");
			menuItem_exit=new JMenuItem("Exit");
			
			if(!dbManager.isthereData()){
				menuItem_chooseDB.setEnabled(false);
				menuItem_removeDB.setEnabled(false);				
			}	
			
			menuItem_chooseDB.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String[] tabDb;
					try {
						//ResourceBundle bundle = ResourceBundle.getBundle("JOptionPane", Locale.ENGLISH);
						tabDb = dbManager.getDbNames();
					    dbSelected = (String)JOptionPane.showInputDialog(null, 
					      "Choose one database",
					      "List of registered databases",
					      JOptionPane.QUESTION_MESSAGE,
					      null,
					      tabDb,
					      tabDb[0]);
					    if(dbSelected!=null){
					    	resumePanel.setDbSelected(dbSelected);
					    	menuItem_displayTable.setEnabled(true);
					    	if(g!=null&& g.isFOComplexity()){
					    		menuItem_Rewdb.setEnabled(true);
					    		menuItem_displayanswer.setEnabled(true);
					    	}
								
					    }
					    	
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    e.getClass().getSimpleName(),
							    JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
			
			menuItem_removeDB.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					try {
						DeleteDBDialog dialog=new DeleteDBDialog(null, "Delete databases", true, dbManager);
						if(dbSelected!=null && dialog.getListdbToDelete().contains(dbSelected)){
							menuItem_Rewdb.setEnabled(false);
							menuItem_displayanswer.setEnabled(false);
							menuItem_displayTable.setEnabled(false);
							dbSelected=null;
							resumePanel.setDbSelected(dbSelected);
							if(!dbManager.isthereData()){
								menuItem_chooseDB.setEnabled(false);
								menuItem_removeDB.setEnabled(false);				
							}
						}
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    e.getClass().getSimpleName(),
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			menuItem_exit.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});
			
			menuItem_loadDB.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					 JFileChooser fileChooser = new JFileChooser();
				     fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				     fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				     fileChooser.setFileFilter(new FileNameExtensionFilter("SQL file", "sql"));
				     fileChooser.setAcceptAllFileFilterUsed(true);
				     int result = fileChooser.showSaveDialog(null);
				     if (result == JFileChooser.APPROVE_OPTION) {
				            File selectedFile = fileChooser.getSelectedFile();
				            String dbName = JOptionPane.showInputDialog(null, " Enter the name of database!", 
				            		"Database!", JOptionPane.QUESTION_MESSAGE);
				            if(dbName!=null)
								try {
									dbManager.loadDb(selectedFile.getAbsolutePath(), dbName);
									
								} catch (ClassNotFoundException | IOException
										| SQLException e) {
									// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,
										    e.getMessage(),
										    e.getClass().getSimpleName(),
										    JOptionPane.ERROR_MESSAGE);
								}
				            	
				        }
				     
				        
				}
			});
			menuFichier.add(menuItem_loadDB);
			menuFichier.add(menuItem_chooseDB);
			menuFichier.add(menuItem_removeDB);
			menuFichier.add(menuItem_exit);
			menuFichier.addSeparator();
			
			
		    menuEdition = new JMenu("Display");
			menuItem_displayTable=new JMenuItem("Display Table of selected database");	
			menuItem_displayTable.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					try {
						//ResourceBundle bundle = ResourceBundle.getBundle("JOptionPane", Locale.ENGLISH);
						ArrayList<String> listTables = dbManager.getTableNames(dbSelected);
						String [] tabTable=new String [listTables.size()];
						for (int i = 0; i < listTables.size(); i++) {
							tabTable[i]=listTables.get(i);
						}
						
					    String tableSelected = (String)JOptionPane.showInputDialog(null, 
					      "Choose one table to display",
					      "List of tables of "+dbSelected,
					      JOptionPane.QUESTION_MESSAGE,
					      null,
					      tabTable,
					      tabTable[0]);
					    if(tableSelected!=null){					    	
					    	clearJPanelResult();					    	
					    	JTable dataTable= getJTableData(tableSelected, dbSelected);					    	
							scrollTablePanel=new JScrollPane(dataTable);		
							scrollTablePanel.setBorder(new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), tableSelected));
							centerPanel.add(BorderLayout.CENTER,scrollTablePanel);
							centerPanel.setVisible(false);
							centerPanel.setVisible(true);
					    	
					    }
					    	
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    e.getClass().getSimpleName(),
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			menuItem_displayanswer=new JMenuItem("Display answer of request q using selected database");
			menuItem_displayanswer.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					boolean isanswer=false;
					ArrayList<ArrayList<Object>> results = null;
					try {
						
						JPanel resultPanel=new JPanel();						
						if(q.getHead().size()==0){
							JLabel labelresult=new JLabel(rqManager.getboolCertainAnswer(q, dbSelected));
							labelresult.setFont(new Font("Arial", Font.BOLD, 30));
							resultPanel.add(labelresult);
							
						}
						else{
							results=rqManager.getNotboolCertainAnswer(q, dbSelected);
							if(results==null){
								JLabel labelresult=new JLabel("There is no answer(s).");
								labelresult.setFont(new Font("Arial", Font.BOLD, 20));
								resultPanel.add(labelresult);
							}
							else
								isanswer=true;
							
						}
						clearJPanelResult();
						if(!isanswer)
							scrollTablePanel=new JScrollPane(resultPanel);
						else
							scrollTablePanel=new JScrollPane(getJTableAnswers(results));
						
						
						scrollTablePanel.setBorder(new TitledBorder(BorderFactory.createMatteBorder(
            1, 5, 1, 1, Color.GRAY), " Answer of request"));
						centerPanel.add(BorderLayout.CENTER,scrollTablePanel);
						centerPanel.setVisible(false);
						centerPanel.setVisible(true);
						
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    e.getClass().getSimpleName(),
							    JOptionPane.ERROR_MESSAGE);
						
					} catch (BadRequestException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    e.getErrorType(),
							    JOptionPane.ERROR_MESSAGE);
						
					} 
				}
			});
			menuItem_Rewdb=new JMenuItem("using selected database");
			menuItem_Rewdb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					
						ConjonctiveRequest q2;
						try {
							q2 = rqManager.setupRelationSchemas(q, dbSelected);
							clearJPanelResult();
							final JTable requestTable= getJTableRewroteRequest(q2);						
							scrollTablePanel=new JScrollPane(requestTable);
							scrollTablePanel.setBorder(new TitledBorder(BorderFactory.createMatteBorder(
	            1, 5, 1, 1, Color.GRAY), "SQL Rewriting"));
							scrollTablePanel.addComponentListener(new ComponentListener() {
								
								@Override
								public void componentShown(ComponentEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void componentResized(ComponentEvent arg0) {
									// TODO Auto-generated method stub								
									if(scrollTablePanel.getHeight()>100)
										requestTable.setRowHeight(scrollTablePanel.getHeight()-60);
								}
								
								@Override
								public void componentMoved(ComponentEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void componentHidden(ComponentEvent arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							centerPanel.add(BorderLayout.CENTER,scrollTablePanel);
							centerPanel.setVisible(false);
							centerPanel.setVisible(true);
						} catch (ClassNotFoundException |SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,
								    e.getMessage(),
								    e.getClass().getSimpleName(),
								    JOptionPane.ERROR_MESSAGE);
						} catch (BadRequestException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,
								    e.getMessage(),
								    e.getErrorType(),
								    JOptionPane.ERROR_MESSAGE);
						}
				}
				
			});
			menuItem_Rewrc=new JMenuItem("using relation schemas");
			menuItem_Rewrc.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					RelationschemaDialog dialog=RelationschemaDialog.getInstance(null, "Setup relation schemas", true, q);
					
					if(dialog.getSchemas()!=null){
						try {
							ConjonctiveRequest q2=rqManager.setupRelationSchemas(q, dialog.getSchemas());
							clearJPanelResult();
							final JTable requestTable= getJTableRewroteRequest(q2);						
							scrollTablePanel=new JScrollPane(requestTable);
							scrollTablePanel.setBorder(new TitledBorder(BorderFactory.createMatteBorder(
	            1, 5, 1, 1, Color.GRAY), "SQL Rewriting"));
							scrollTablePanel.addComponentListener(new ComponentListener() {
								
								@Override
								public void componentShown(ComponentEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void componentResized(ComponentEvent arg0) {
									// TODO Auto-generated method stub								
									if(scrollTablePanel.getHeight()>100)
										requestTable.setRowHeight(scrollTablePanel.getHeight()-60);
								}
								
								@Override
								public void componentMoved(ComponentEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void componentHidden(ComponentEvent arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							centerPanel.add(BorderLayout.CENTER,scrollTablePanel);
							centerPanel.setVisible(false);
							centerPanel.setVisible(true);
						} catch (BadRequestException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,
								    e.getMessage(),
								    e.getErrorType(),
								    JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			menuItem_Rewdb.setEnabled(false);
			menuItem_Rewrc.setEnabled(false);
			menuRewriting=new JMenu("Display rewrote request in SQL");
			menuRewriting.add(menuItem_Rewdb);
			menuRewriting.add(menuItem_Rewrc);
			menuRewriting.addSeparator();
			menuEdition.add(menuRewriting);
			menuEdition.add(menuItem_displayanswer);
			menuEdition.add(menuItem_displayTable);		
			menuItem_displayanswer.setEnabled(false);
			menuItem_displayTable.setEnabled(false);
			menuEdition.addSeparator();
			menu_bar.add(menuFichier);
			menu_bar.add(menuEdition);
			
			setJMenuBar(menu_bar);
			JPanel panRequest=getRequestPanel(w1, h2);
			panComplexity=new ComplexityPanel(w1, h1);
			JPanel leftPanel=new JPanel();	
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(BorderLayout.CENTER,panRequest);
			leftPanel.add(BorderLayout.SOUTH,panComplexity);
			
			
			panCenter=new GraphPanel(w2, h2,null);
			panDetails=new DetailsPanel(w2, h1);
			resumePanel=new RecapPanel(w2, 95);			
		    
			scroll=new JScrollPane(panDetails);
			scroll.setPreferredSize(new Dimension(w2, h1));
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(BorderLayout.CENTER,panCenter);
			centerPanel.add(BorderLayout.SOUTH,scroll);
			centerPanel.add(BorderLayout.NORTH,resumePanel);
			
			getContentPane().add(BorderLayout.WEST, leftPanel);
			getContentPane().add(BorderLayout.CENTER, centerPanel);
			
			b_excute.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
											
							String shead=head.getText().trim();
							String sbody=body.getText().trim();
							if(!sbody.isEmpty()){
								
								try {
									
								    q =parser.getConjonctiveRequest(sbody, shead);									
									q.addObservable(resumePanel);
								    g=new AttackGraph(q);
									g.addObservable(panDetails);
									g.addObservable(panComplexity);
									if(panCenter==null){
										centerPanel.remove(scrollTablePanel);
										scrollTablePanel=null;
									}
									else
										centerPanel.remove(panCenter);								
									panCenter=new GraphPanel(w2, h2, g);	
									centerPanel.add(BorderLayout.CENTER,panCenter);
									centerPanel.setVisible(false);
									centerPanel.setVisible(true);
									if( g.isFOComplexity()){										
							    		menuItem_Rewrc.setEnabled(true);
							    		if(dbSelected!=null){
							    			menuItem_displayanswer.setEnabled(true);
							    			menuItem_Rewdb.setEnabled(true);
							    		}
							    			
									}
									else{
										menuItem_Rewdb.setEnabled(false);
										menuItem_Rewrc.setEnabled(false);
										menuItem_displayanswer.setEnabled(false);
									}
										
								} catch (RequestError e1) {
									// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,
										    e1.getMessage(),
										    e1.getErrorType(),
										    JOptionPane.ERROR_MESSAGE);
								}
								
							}
							
							
						}
					});
			setPreferredSize(new Dimension(1000, 650));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			this.pack();
			this.setVisible(true);
		} catch (ClassNotFoundException | SQLException e2) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,
				    e2.getMessage(),
				    e2.getClass().getSimpleName(),
				    JOptionPane.ERROR_MESSAGE);
		}
		
		
		
	}
	
	private void clearJPanelResult(){
		if(scrollTablePanel==null){
			centerPanel.remove(panCenter);
			panCenter=null;
		}
		else
			centerPanel.remove(scrollTablePanel);
	}
	
	/**
	 * 
	 * @param results: listes d'objets de la réponse
	 * @return retourne sous forme de table la réponse à une requête
	 */
	private JTable getJTableAnswers(ArrayList<ArrayList<Object>> results){
		ArrayList<Object>headers=results.get(0);
		int sizeHeaders=headers.size();
    	String []entetes=new String [sizeHeaders];
    	for (int i = 0; i < sizeHeaders; i++) {
			entetes[i]=headers.get(i).toString();
		}
    	int sizeData=results.size();
    	Object[][] donnees = new Object [sizeData-1][sizeHeaders];
    	for (int i = 1; i < sizeData; i++) {
    		for (int j = 0; j < sizeHeaders; j++) {
				donnees[i-1][j]=results.get(i).get(j);
			}
			
		}
    	JTable jtableAnswer = new JTable(donnees, entetes);
    	jtableAnswer.setEnabled(false);
    	return jtableAnswer;
    	
	}
	
	/**
	 * 
	 * @param table: table où on lit les données
	 * @param dbToRequest: nom de la bd où se trouve la table
	 * @return retourne les données d'une table de la bd sous forme de table
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	
	private JTable getJTableData(String table, String dbToRequest) throws ClassNotFoundException, SQLException{
		ArrayList<String>tableAttributes=rqManager.getRelationSchema(table, 
    			dbToRequest).get(0);
    	int sizeTableAttributes=tableAttributes.size();
    	String []entetes=new String [sizeTableAttributes];
    	for (int i = 0; i < sizeTableAttributes; i++) {
			entetes[i]=tableAttributes.get(i);
		}
    	ArrayList<ArrayList<Object>> resultsData=rqManager.selectTable(table, dbToRequest);
    	int sizeData=resultsData.size();
    	Object[][] donnees = new Object [sizeData][sizeTableAttributes];
    	for (int i = 0; i < sizeData; i++) {
    		for (int j = 0; j < sizeTableAttributes; j++) {
				donnees[i][j]=resultsData.get(i).get(j);
			}
			
		}
    	JTable tableau = new JTable(donnees, entetes);
        tableau.setEnabled(false);
    	return tableau;
	}
	
	private JTable getJTableRewroteRequest(ConjonctiveRequest q){		
		String request=rqManager.consistentRewrote(q);
		String []entetes={"Rewrote request"};
		Object[][] donnees = new Object [1][1];
		donnees[0][0]="\n"+request;
		final JTable tableau = new JTable(donnees, entetes); 		
		tableau.getColumnModel().getColumn(0).setCellRenderer(new CustomCellRenderer());
        tableau.getColumnModel().getColumn(0).setCellEditor(new CustomEditor());
        
    	return tableau;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @return retourne le panneau qui sert de formulaire
	 */
	public JPanel getRequestPanel(int width, int height){
		
		JPanel requestPanel=new JPanel();		
		TitledBorder title= new TitledBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.GRAY), "Conjonctive request");
		requestPanel.setBorder(title);
		requestPanel.setBackground(Color.LIGHT_GRAY);
		head=new JTextField();body=new JTextArea();
		msg=new JLabel("Enter your conjonctive request below.");
		j_head=new JLabel("Head : ");
		j_body=new JLabel("Body : ");
		
		JScrollPane scrollBody=new JScrollPane(body);
		JScrollPane scrollHead=new JScrollPane(head);
		requestPanel.setLayout(null);
		msg.setBounds(15, 25, width, 25);
		j_head.setBounds(10,75,100,30);
		scrollHead.setBounds(70,65,320,50);		
		j_body.setBounds(10,200,60,30);
		scrollBody.setBounds(70,200,320,170);
		b_excute.setBounds((width-180)/2,height+50,180,25);
		
		requestPanel.add(msg);
		requestPanel.add(j_head);
		requestPanel.add(scrollHead);		
		requestPanel.add(j_body);
		requestPanel.add(scrollBody);
		requestPanel.add(b_excute);
		
		return requestPanel;
	}
	
	
	public static void main( String args[] ){
		new CqaView();
	}

	@Override
	public void setView(int compl) {
		// TODO Auto-generated method stub
		if(compl<=0){
			menuItem_chooseDB.setEnabled(false);
			menuItem_removeDB.setEnabled(false);				
		}
		else{
			menuItem_chooseDB.setEnabled(true);
			menuItem_removeDB.setEnabled(true);
			
		}
	}

	@Override
	public void setView(String request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(AttackGraph g) {
		// TODO Auto-generated method stub
		
	}
	
	class CustomCellRenderer extends DefaultTableCellRenderer {

        private JTextArea textArea;
        private JScrollPane scrollPane;

        public CustomCellRenderer() {
            textArea = new JTextArea();
            textArea.setFont(new Font("Arial", Font.BOLD, 13));
            scrollPane = new JScrollPane(textArea);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            if(null != value)
                textArea.setText(value.toString());

            return scrollPane;
        }
        
        
    }
    class CustomEditor implements TableCellEditor {

	    private JTextArea textArea;
	    private JScrollPane scrollPane;
	
	    public CustomEditor() {
	        textArea = new JTextArea();
	        textArea.setFont(new Font("Arial", Font.BOLD, 13));
	        scrollPane = new JScrollPane(textArea);
	    }

		@Override
		public void addCellEditorListener(CellEditorListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void cancelCellEditing() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			 return textArea.getText();
		}

		@Override
		public boolean isCellEditable(EventObject arg0) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void removeCellEditorListener(CellEditorListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean shouldSelectCell(EventObject arg0) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean stopCellEditing() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Component getTableCellEditorComponent(JTable arg0, Object arg1,
				boolean arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			 if(null != arg1)
		            textArea.setText(arg1.toString());

		        return scrollPane;
		}
    }

}
