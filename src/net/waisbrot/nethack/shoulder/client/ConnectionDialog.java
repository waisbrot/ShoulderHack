package net.waisbrot.nethack.shoulder.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.waisbrot.nethack.shoulder.Main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

public class ConnectionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField hostnameField;
	private JFormattedTextField portField;
	private final ConnectHandler connectHandler;
		
	public interface ConnectHandler {
		public void ok(String hostname, int port);
		public void cancel();
	}
	

	/**
	 * Create the dialog.
	 */
	public ConnectionDialog(ConnectHandler connectHandler) {
		super(Main.self.mainFrame);
		
		this.connectHandler = connectHandler;
		setTitle("Connect to terminal server");
		setBounds(100, 100, 287, 243);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblHostname = new JLabel("hostname");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblHostname, 16, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblHostname, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblHostname);
		
		hostnameField = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.EAST, hostnameField, -10, SpringLayout.EAST, contentPanel);
		hostnameField.setText("localhost");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, hostnameField, -6, SpringLayout.NORTH, lblHostname);
		sl_contentPanel.putConstraint(SpringLayout.WEST, hostnameField, 6, SpringLayout.EAST, lblHostname);
		contentPanel.add(hostnameField);
		hostnameField.setColumns(10);
		
		JLabel lblPort = new JLabel("port");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblPort, 43, SpringLayout.SOUTH, lblHostname);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblPort, 0, SpringLayout.WEST, lblHostname);
		contentPanel.add(lblPort);
		
		portField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		sl_contentPanel.putConstraint(SpringLayout.EAST, portField, -10, SpringLayout.EAST, contentPanel);
		portField.setText("31337");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, portField, -6, SpringLayout.NORTH, lblPort);
		sl_contentPanel.putConstraint(SpringLayout.WEST, portField, 0, SpringLayout.WEST, hostnameField);
		contentPanel.add(portField);
		portField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ConnectionDialog.this.connectHandler.cancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ConnectionDialog.this.connectHandler.ok(hostnameField.getText(), Integer.parseInt(portField.getText()));
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
