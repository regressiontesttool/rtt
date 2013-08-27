package siple.semantics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import siple.ast.*;

/**
 * Simple dialog used throughout interpretation to ask the user for a new value
 * of a certain type, such that a <i>SiPLE</i> <tt>Read</tt> statement can be
 * processed.
 * @author C. Bürger
 */
public class ReadDialog {
	private static Object result = null;
	
	/**
	 * Open a new input dialog for a <i>SiPLE</i> <tt>Read</tt> statement. The
	 * dialog forces the user to enter a valid value of a certain type.
	 * @param type The type of the value to enter.
	 * @param vmOutput The interpreter's, so far throughout program execution
	 * produced, output.
	 * @return A value of the given type.
	 */
	public synchronized static Object execute(Type type, String vmOutput) {
		result = null;
		new ReadDialog(type, vmOutput);
		while(result == null) {
			try {Thread.sleep(300);}
			catch(InterruptedException ex) {}
		}
		return result;
	}
	
	private ReadDialog(final Type type, String vmOutput) {
		final JFrame dialog = new JFrame("Read Input");
		
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dialog.setLayout(new FlowLayout());
		dialog.setSize(600, 400);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((dimension.width - dialog.getSize().width ) / 2,
				(dimension.height - dialog.getSize().height) / 3);
		
		final JLabel description = new JLabel(
				"Read: "+ type.toString());
		dialog.add(description);
		final JTextField input = new JTextField(25);
		dialog.add(input);
		final JTextArea console = new JTextArea(vmOutput, 20, 50);
		dialog.add(console);
		JButton submit = new JButton("Submit");
		dialog.add(submit);
		submit.addActionListener(new ActionListener() {
			private int retries = 0;
			public void actionPerformed(ActionEvent e) {
				String inputText = input.getText();
				if (inputText == null || inputText.length() == 0)
					return;
				Constant inputValue = new Constant(inputText);
				switch (type.domain) {
				case Boolean:
					result = inputValue.Type() == Type.Boolean ?
							inputValue.AsBoolean() : null;
					break;
				case Real:
					result = inputValue.Type() == Type.Real ?
							inputValue.AsReal() : null;
					break;
				case Integer:
					result = inputValue.Type() == Type.Integer ?
							inputValue.AsInteger() : null;
					break;
				}
				if (result != null)
					dialog.dispose();
				else description.setText("Read ("+ ++retries +"'th retry): "+
						type.toString());
			}
		});
		
		dialog.validate();
		dialog.setVisible(true); 
		dialog.toFront();
	}
}
