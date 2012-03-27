package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.server.db.DatabaseAPI;

import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Gui for selecting a room
 *
 */
public class SelectRoomDialog extends JDialog implements ListSelectionListener, ActionListener{

	private JButton btnClose, btnAdd;
	private JList listRooms;
	private DefaultListModel defaultListModel;
	private DefaultListSelectionModel defaultListSelectionModel;
	private Appointment model;
	
	/**
	 * Create a new dialog with an appointment as model
	 * @param model
	 */
	public SelectRoomDialog(Appointment model){
		this.model = model;
		setUp();
		addFreeRooms();
	}
	
	private void setUp(){
		setLayout(new FlowLayout());
		//User list
		defaultListModel = new DefaultListModel();
		listRooms = new JList();
		listRooms.setModel(defaultListModel);
		listRooms.setCellRenderer(new RoomListRenderer());
		defaultListSelectionModel = new DefaultListSelectionModel();
		listRooms.setSelectionModel(defaultListSelectionModel);
		defaultListSelectionModel.addListSelectionListener(this);
		JScrollPane scrollUsers = new JScrollPane(listRooms);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		btnClose = new JButton("Lukk");
		btnAdd = new JButton("Velg rom");
		
		add(scrollUsers);
		add(btnAdd);
		add(btnClose);
		
		defaultListSelectionModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		//actionlisteners
		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);
		
		//behaviour
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
	}
	private void addFreeRooms(){
		ArrayList<Room >list =(ArrayList<Room>)ServerAPI.getFreeRooms(model.getDateStart(),model.getDateEnd());
		for (Room room : list) {
			if(!room.getName().equals(model.getRoom().getName())){				
				defaultListModel.addElement(room);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd) {
			if(!defaultListSelectionModel.isSelectionEmpty()){
				int i = defaultListSelectionModel.getAnchorSelectionIndex();
				Room a = (Room)defaultListModel.get(i);
				model.getRoom().setRoom(a);
				dispose();		

			}
		}
		
		if(e.getSource() == btnClose){
			dispose();
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
