package reuze.demo;

import javax.swing.JApplet;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

import reuze.ml_KnowledgeBase;

import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;
import java.awt.Color;
import javax.swing.JInternalFrame;
import java.awt.Button;

public class demoPropoLogic extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6006363902138561283L;
	private JTextField textField;
	private ArrayList<String> assertions = new ArrayList<String>();
	TextArea textArea = new TextArea();
	private JTextField testField;
	private ml_KnowledgeBase kb = new ml_KnowledgeBase();
	private JLabel lblNewLabel = new JLabel("SUCCESS");
	private JLabel lblFalse = new JLabel("FALSE");
	JButton btnHelp = new JButton("Help");
	JInternalFrame internalFrame = new JInternalFrame("Help Pane");
	Button button = new Button("Close Help");
	/**
	 * Create the applet.
	 * @return 
	 */
	public void init() {
		this.resize(460, 300);
	}
	public demoPropoLogic() {
		setLocation(10, 12);
		
		getContentPane().setLayout(null);
		

		internalFrame.setBounds(0, 0, 440, 289);
		getContentPane().add(internalFrame);
		internalFrame.getContentPane().setLayout(null);
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setText("Help:\r\nAdd facts to the knowledge base by typing them into the \r\n\"Input Assertions\" field and either pressing enter or by \r\nclicking the \"Add\" button.\r\n\r\nYou can then verify facts assertions by putting them into \r\nthe \"Test\" field and clicking verify.\r\nFor example:\r\nInput:\r\n(B10 <=> (P11 OR (P20 OR P00)))\r\n(B01 <=> (P00 OR (P02 OR P11)))\r\n(B21 <=> (P20 OR (P22 OR (P31 OR P11))))\r\n(B12 <=> (P11 OR (P13 OR (P22 OR P02))))\r\n(NOT B21)\r\n(NOT B12)\r\n(B10)\r\n(B01)\r\n\r\nVerify:\r\n(P00) -> SUCCESS\r\n(NOT P00) -> FALSE\r\n");
		textArea_1.setBounds(0, 20, 424, 240);
		internalFrame.getContentPane().add(textArea_1);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.setVisible(false);
			}
		});
		button.setBounds(354, 0, 70, 22);
		internalFrame.getContentPane().add(button);
		internalFrame.setVisible(true);
		
		JLabel lblInputAssertions = new JLabel("Input Assertions");
		lblInputAssertions.setFont(new Font("Calibri", Font.BOLD, 18));
		lblInputAssertions.setBounds(20, 27, 131, 28);
		getContentPane().add(lblInputAssertions);
		textArea.setBounds(20, 61, 420, 160);

		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = textField.getText();
				assertions.add(text);
				kb.tell(text);
				textArea.append(text+"\n");
			}
		});
		textField.setBounds(149, 31, 196, 23);
		getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = textField.getText();
				assertions.add(text);
				textArea.append(text+"\n");
			}
		});
		btnAdd.setBounds(351, 31, 89, 23);
		getContentPane().add(btnAdd);
		

		getContentPane().add(textArea);
		
		JLabel lblTest = new JLabel("Test: ");
		lblTest.setFont(new Font("Calibri", Font.BOLD, 18));
		lblTest.setBounds(20, 227, 56, 23);
		getContentPane().add(lblTest);
		
		testField = new JTextField();
		testField.setBounds(71, 227, 276, 25);
		getContentPane().add(testField);
		testField.setColumns(10);
		
		//JLabel lblNewLabel = new JLabel("SUCCESS");
		internalFrame.setVisible(false);
		lblNewLabel.setVisible(false);
		lblNewLabel.setForeground(Color.GREEN);
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 31));
		lblNewLabel.setBounds(167, 263, 117, 37);
		getContentPane().add(lblNewLabel);
		
		JButton btnVerify = new JButton("Verify");
		btnVerify.setBounds(357, 228, 89, 23);
		btnVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String verify = testField.getText().toString();
				if(kb.askWithDpll(verify)) {
					lblNewLabel.setVisible(true);
					lblFalse.setVisible(false);
				}
				else {
					lblNewLabel.setVisible(false);
					lblFalse.setVisible(true);
				}
			}
		});
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.setVisible(true);
			}
		});
		getContentPane().add(btnVerify);
		
		lblFalse.setVisible(false);
		lblFalse.setForeground(Color.RED);
		lblFalse.setFont(new Font("Calibri", Font.BOLD, 31));
		lblFalse.setBounds(182, 263, 82, 37);
		getContentPane().add(lblFalse);
		
		JLabel lblPropositionalLogicApplet = new JLabel("Propositional Logic Applet");
		lblPropositionalLogicApplet.setFont(new Font("Calibri", Font.BOLD, 18));
		lblPropositionalLogicApplet.setBounds(107, 0, 204, 28);
		getContentPane().add(lblPropositionalLogicApplet);
		

		btnHelp.setBounds(355, 11, 85, 17);
		getContentPane().add(btnHelp);
		

		resize(400, 400);

	}
}
