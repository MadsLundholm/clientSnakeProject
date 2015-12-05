package client.presentation.panels;

import client.SDK.model.Game;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeleteGame extends JPanel
{
	private static final long serialVersionUID = 1496674849914571382L;
	private JLabel lblBackground;
	private ImageIcon imageBackground;
	private JButton btnDeleteGame;
	private JButton btnBack;
	private JTextField txtDeleteGame;
	private JLabel lblDeleteGame;
	private JComboBox comboBoxDeleteGame;

	public DeleteGame ()
	{
		setSize(700, 440);
		setLayout(null);

		btnBack = new JButton("BACK");
		btnBack.setBounds(10, 332, 83, 23);
		add(btnBack);

		btnDeleteGame = new JButton("DELETE SELECTED GAME");
		btnDeleteGame.setBounds(464, 332, 210, 23);
		add(btnDeleteGame);

		txtDeleteGame = new JTextField();
		txtDeleteGame.setColumns(10);
		txtDeleteGame.setBounds(310, 333, 161, 34);
		add(txtDeleteGame);

		lblDeleteGame = new JLabel("DELETE GAME");
		lblDeleteGame.setBounds(231, 342, 69, 14);
		add(lblDeleteGame);

		comboBoxDeleteGame = new JComboBox();
		comboBoxDeleteGame.setBounds(278, 291, 211, 34);
		add(comboBoxDeleteGame);

		imageBackground = new ImageIcon(getClass().getResource("/client/presentation/imgSrc/deleteGame.jpg"));
		lblBackground = new JLabel(imageBackground);
		lblBackground.setBounds(0, 0, 684, 402);
		add(lblBackground);
	}

	public void addActionListener(ActionListener l)
	{
		btnBack.addActionListener(l);
		btnDeleteGame.addActionListener(l);
	}

	public void setGamesInComboBox(ArrayList<Game> games){
		comboBoxDeleteGame.removeAllItems();
		for (Game g: games)
		{
			comboBoxDeleteGame.addItem(g.getName());
		}
	}

	public String getSelectedGame(){
		return (String) comboBoxDeleteGame.getSelectedItem();
	}

	public JButton getBtnDeleteGame()
	{
		return btnDeleteGame;
	}

	public JButton getBtnBack()
	{
		return btnBack;
	}

	public JTextField getTxtDeleteGame() {
		return txtDeleteGame;
	}

	public JComboBox getComboBoxDeleteGame() {
		return comboBoxDeleteGame;
	}
}
