package libarary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyFrame extends JFrame implements ActionListener {
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
	private JTable table = new JTable();
	MyConnect myConnect = new MyConnect();
	private JTextField tfId, tfNameBook, tfNameAuthor, tfNamePublic, tfType;
	private JButton btnOK, btnCancel;
	
	private boolean isUpdate = false;

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
	public MyFrame(){

		int i=myConnect.connect();
		if(i==0){
			JOptionPane.showMessageDialog(null, "Not connect to Database", "Error Connect", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		setTitle("My App Store Book");
		add(createMainPane());
		
		setDisplayInput(false, false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
	private JPanel createMainPane() {
		JPanel panel = new JPanel(new BorderLayout()); 
		panel.add(createTitlePane(),BorderLayout.PAGE_START);
		panel.add(createTablePane(),BorderLayout.CENTER);
		panel.add(createBottonPane(),BorderLayout.PAGE_END);

		return panel;
	}
//----------------------------------------------------------------------------------------------
		private JPanel createTitlePane() {
			JPanel panel = new JPanel();
			JLabel lbTitle = new JLabel("Book List");
			panel.add(lbTitle);
			return panel;
		}
//----------------------------------------------------------------------------------------------
		private JPanel createTablePane() {
			JPanel panel = new JPanel();
			table = new JTable();
			panel.add(new JScrollPane(table));
			return panel;
		}
//----------------------------------------------------------------------------------------------
	private JPanel createBottonPane() {
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setBorder(new EmptyBorder(5, 10, 10, 10));
		panel.add(createInputPanel(),BorderLayout.CENTER);
		panel.add(createButtonPane(),BorderLayout.PAGE_END);
		return panel;
	}
//----------------------------------------------------------------------------------------------
	private JPanel createInputPanel() {
		JPanel panel = new JPanel(new BorderLayout(10,10));
		
		JPanel panelLeft = new JPanel(new GridLayout(5, 1,5,5));
		panelLeft.add(new JLabel("Id"));
		panelLeft.add(new JLabel("name book"));
		panelLeft.add(new JLabel("name author"));
		panelLeft.add(new JLabel("name public"));
		panelLeft.add(new JLabel("type"));
		
		JPanel panelRight = new JPanel(new GridLayout(5, 1,5,5));
		tfId = new JTextField();
		tfNameBook = new JTextField();
		tfNameAuthor = new JTextField();
		tfNamePublic = new JTextField();
		tfType = new JTextField();
		panelRight.add(tfId);
		panelRight.add(tfNameBook);
		panelRight.add(tfNameAuthor);
		panelRight.add(tfNamePublic);
		panelRight.add(tfType);
		
		JPanel panelOK = new JPanel(new GridLayout(1, 2,5,5));
		btnOK= createButton("OK");
		btnCancel = createButton("Cancel");
		panelOK.setBorder(new EmptyBorder(0, 50, 0, 50));
		panelOK.add(btnOK );
		panelOK.add(btnCancel);
		
		panel.add(panelLeft, BorderLayout.WEST);
		panel.add(panelRight, BorderLayout.CENTER);
		panel.add(panelOK,BorderLayout.PAGE_END);
		

		return panel;
	}

//----------------------------------------------------------------------------------------------

	private JPanel createButtonPane() {
		JPanel panel = new JPanel(new GridLayout(1,5,5,5));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(createButton("Add"));
		panel.add(createButton("Update"));
		panel.add(createButton("Search"));
		panel.add(createButton("Delete"));
		panel.add(createButton("ShowAllBook"));

		return panel;
	}
//----------------------------------------------------------------------------------------------
	private JButton createButton(String text) {
		JButton btn = new JButton(text);
		btn.addActionListener(this);
		return btn;
	}
//----------------------------------------------------------------------------------------------
	private void loadData(ResultSet rs){
		DefaultTableModel model = new DefaultTableModel();
		try {
			ResultSetMetaData rsMD = rs.getMetaData();
			int columnNumber = rsMD.getColumnCount();
			String[] arr = new String[columnNumber];
			
			for(int i=0;i<columnNumber;i++){
				arr[i] = rsMD.getColumnName(i+1);
			}
			
			model.setColumnIdentifiers(arr);
			
			while (rs.next()) {
				for (int i = 0; i < columnNumber; i++) {
					 arr[i] = rs.getString(i+1);
				}
				
				model.addRow(arr);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.setModel(model);
	}

//---------------------------------------------------------------------------------------------------
	private Book getBook(){
		int id;
		try {
			id = Integer.parseInt(tfId.getText().trim());
		} catch (Exception e) {
			return null;
		}
		String nameBook = tfNameBook.getText().trim();
		String nameAuthor = tfNameAuthor.getText().trim();
		String namePublic = tfNamePublic.getText().trim();
		String type = tfType.getText().trim();
		
		Book book = new Book(id, nameBook, nameAuthor, namePublic, type);
		
		return book;
	}
//----------------------------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Delete") {
			delete();
			return;
		}
		if (e.getActionCommand() == "Update") {
			isUpdate = true;
			update();
			return;
		}
		if (e.getActionCommand() == "Add") {
			add();
			return;
		}
		if (e.getActionCommand() == "Search") {
			String bs= JOptionPane.showInputDialog(null, "What is Book'name?", "Search book", JOptionPane.DEFAULT_OPTION);
			loadData(myConnect.selectNameBook(bs));
			return;
		}
		if (e.getActionCommand() == "ShowAllBook") {
			loadData(myConnect.getBook());
			return;
		}
		if(e.getSource()==btnOK){
			addOrUpdate();
		}
		if(e.getSource()==btnCancel){
			cancel();
		}
	}
	
//-------------------------------------------------------------------------------------------------------------------------------=--
	private void delete() {
		int row = table.getSelectedRow();
		if(row<0){
			JOptionPane.showMessageDialog(null, "You must select a row ","error delete",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int select = JOptionPane.showOptionDialog(null, "Are you sure? ", "Delete", 0, JOptionPane.YES_NO_OPTION,null,null,1);
		if(select ==0){
			myConnect.deleteId((String) table.getValueAt(row, 0));
			loadData(myConnect.getBook());
		}
	}
	
//-------------------------------------------------------------------------------------------------------------------------------------
	private void update() {
		if (setDisplayInput(true, true)) {
			isUpdate = true;
		} else {
			JOptionPane.showMessageDialog(null, "Error update", "Error update",
					JOptionPane.ERROR_MESSAGE);
		}
	}
//---------------------------------------------------------------------------------------------------------------------------------------
	private void add() {
		setDisplayInput(true, false);
	}
//---------------------------------------------------------------------------------------------------------------------------------------
	private boolean setDisplayInput(boolean display , boolean update){
		if(update && table.getSelectedColumn()<0){
			return false;
		} else if(update){
			int row = table.getSelectedRow();
			tfId.setText((String) table.getValueAt(row, 0));
			tfNameBook.setText((String) table.getValueAt(row, 1));
			tfNameAuthor.setText((String) table.getValueAt(row, 2));
			tfNamePublic.setText((String) table.getValueAt(row, 3));
			tfType.setText((String) table.getValueAt(row, 4));
		}
		
		tfId.setEnabled(display);
		tfNameBook.setEnabled(display);
		tfNameAuthor.setEnabled(display);
		tfNamePublic.setEnabled(display);
		tfType.setEnabled(display);
		btnOK.setEnabled(display);
		btnCancel.setEnabled(display);
		
		return true;
	}
//----------------------------------------------------------------------------------------------
	private void addOrUpdate() {
		Book book = getBook();
		if(book!=null){
			if(isUpdate){
				myConnect.updateId(book);
				loadData(myConnect.getBook());
				isUpdate = false;
			}else {
				myConnect.insert(book);
				loadData(myConnect.getBook());
			}
			clear();
			setDisplayInput(false, false);
		}else {
			JOptionPane.showMessageDialog(null, "Information is error","Error info",JOptionPane.ERROR_MESSAGE);
		}
	}
//--------------------------------------------------------------------------------------------------------------
	private void cancel() {
		clear();
		setDisplayInput(false, false);
	}
//----------------------------------------------------------------------------------------------
	private void clear() {
		tfId.setText("");
		tfNameBook.setText("");
		tfNameAuthor.setText("");
		tfNamePublic.setText("");
		tfType.setText("");
	}
//-----------------------------------------------------------------------------------------------	
//-----------------------------------------------------------------------------------------------	

	public static void main(String[] args) {
		new MyFrame();
	}
//----------------------------------------------------------------------------------------------	
//-----------------------------------------------------------------------------------------------	

}
