package fgdiCalculator.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import fgdiCalculator.calc.Modes;
import fgdiCalculator.calc.Modes.*;
import fgdiCalculator.calc.NumberException;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GUI(String titel) {
		super(titel);
		modeSelector = new JComboBox<String>();
		modePanel = new JPanel();
		fontPlain = new Font(null, Font.PLAIN, 12);
		fontBig = fontPlain.deriveFont(30.0F);
		Modes.initPools();

	}

	private JComboBox<String> modeSelector;
	private JPanel modePanel, input, output;
	private Font fontBig, fontPlain;
	private Operations currentMode;
	private JTextField n, k;
	private JLabel result;
	private JScrollPane tableScroll;
	private JTable functionsTable;

	// create the most basic structure of the window and add the elements to the
	// frame
	private void createComponents() {
		Container c = this.getContentPane();
		JPanel base = new JPanel();
		base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
		c.add(base);
		base.add(Box.createVerticalStrut(10));
		for (JPanel panel : createPanels()) {
			base.add(panel);
			base.add(Box.createVerticalStrut(10));
		}

	}

	// create the main panels and collect them in one array for createComponents()
	private JPanel[] createPanels() {
		JPanel[] panels = new JPanel[2];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel();
		}
		createModeSelection();
		// Top-Panel
		panels[0].setLayout(new BoxLayout(panels[0], BoxLayout.X_AXIS));
		panels[0].add(Box.createHorizontalStrut(10));
		panels[0].add(new JLabel("Operation: "));
		panels[0].add(modeSelector);
		panels[0].add(Box.createHorizontalStrut(10));

		// Bottom-Panel
		createModePanel(Modes.Operations.NULL);
		modePanel.setLayout(new GridLayout(1, 2));
		panels[1].setLayout(new BoxLayout(panels[1], BoxLayout.X_AXIS));
		panels[1].add(Box.createHorizontalStrut(10));
		panels[1].add(modePanel);

		return panels;
	}

	// create the mode-selection panel
	private void createModeSelection() {
		for (Modes.Operations operation : Modes.Operations.values()) {
			modeSelector.addItem(operation.toString());
		}
		modeSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = modeSelector.getSelectedIndex();
				currentMode = Modes.Operations.values()[selection];
				createModePanel(currentMode);
			}
		});
	}

	// create a panel appropriate to the desired operation
	private void createModePanel(Modes.Operations operation) {
		result = new JLabel();
		result.setFont(fontBig);
		input = new JPanel();
		output = new JPanel();
		JLabel bracket1, bracket2;
		CalcListener pressedEnter = new CalcListener();
		JPanel stackedInput = new JPanel();
		modePanel.removeAll();
		output.removeAll();
		input.removeAll();
		input.setLayout(new BoxLayout(input, BoxLayout.X_AXIS));
		output.setLayout(new BorderLayout());
		stackedInput.setLayout(new BoxLayout(stackedInput, BoxLayout.Y_AXIS));
		setBorder(input, "Input");
		modePanel.add(input);
		modePanel.add(output);
		switch (operation) {
		case STIRLING_NUMBER:
			n = new JTextField("n", 3);
			n.setHorizontalAlignment(JTextField.CENTER);
			n.addActionListener(pressedEnter);
			k = new JTextField("k", 3);
			k.setHorizontalAlignment(JTextField.CENTER);
			k.addActionListener(pressedEnter);
			bracket1 = new JLabel("{");
			bracket1.setFont(fontBig);
			bracket2 = new JLabel("}");
			bracket2.setFont(fontBig);
			stackedInput.add(n);
			stackedInput.add(k);
			input.add(bracket1);
			input.add(stackedInput);
			input.add(bracket2);
			output.add(result);
			break;

		case BINOMIAL_COEFFICIENT:
			n = new JTextField("n", 3);
			n.setHorizontalAlignment(JTextField.CENTER);
			n.addActionListener(pressedEnter);
			k = new JTextField("k", 3);
			k.setHorizontalAlignment(JTextField.CENTER);
			k.addActionListener(pressedEnter);
			bracket1 = new JLabel("(");
			bracket1.setFont(fontBig);
			bracket2 = new JLabel(")");
			bracket2.setFont(fontBig);
			stackedInput.add(n);
			stackedInput.add(k);
			input.add(bracket1);
			input.add(stackedInput);
			input.add(bracket2);
			output.add(result);
			break;

		case BELL_NUMBER:
			JLabel b = new JLabel("B");
			b.setFont(fontBig);
			n = new JTextField("n", 3);
			n.addActionListener(pressedEnter);
			input.add(b);
			input.add(n);
			output.add(result);
			break;

		case FACTORIAL:
			n = new JTextField("n", 3);
			n.setHorizontalAlignment(JTextField.RIGHT);
			n.addActionListener(pressedEnter);
			JLabel exclam = new JLabel("!");
			input.add(n);
			input.add(exclam);
			output.add(result);
			break;

		case FUNCTIONS:
			n = new JTextField("n", 3);
			n.setHorizontalAlignment(JTextField.CENTER);
			n.addActionListener(pressedEnter);
			k = new JTextField("k", 3);
			k.setHorizontalAlignment(JTextField.CENTER);
			k.addActionListener(pressedEnter);
			JLabel arrow = new JLabel("-->");
			arrow.setFont(fontBig);
			input.add(n);
			input.add(arrow);
			input.add(k);
			break;
			
		case ABOUT:
			JTextArea text = new JTextArea();
			text.setText(Info.getInfoText());
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			JScrollPane textScroller = new JScrollPane(text);
			modePanel.removeAll();
			modePanel.add(textScroller);
			break;

		default:
			modePanel.removeAll();
		}
		resize();

	}

	// surrounds an element with a labeled border
	protected void setBorder(JComponent comp, String titel) {
		comp.setBorder(BorderFactory.createTitledBorder(titel));
	}

	// set the size of the frame
	protected void resize() {
		this.pack();
	}

	class CalcListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setBorder(output, currentMode.toString());

			// Display the results of the desired calculation
			switch (currentMode) {
			case STIRLING_NUMBER:
				try {
					int nValue, kValue;
					nValue = Integer.parseInt(n.getText());
					kValue = Integer.parseInt(k.getText());
					result.setText(String.valueOf(Modes.calcStirling(nValue, kValue)));

				} catch (NumberException ne) {
					JOptionPane.showMessageDialog(modePanel, ne.toString(), "Error", JOptionPane.ERROR_MESSAGE);

				} catch (Exception exception) {
					JOptionPane.showMessageDialog(modePanel, "Error\n" + exception.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;

			case BINOMIAL_COEFFICIENT:
				try {
					int nValue, kValue;
					nValue = Integer.parseInt(n.getText());
					kValue = Integer.parseInt(k.getText());
					result.setText(String.valueOf(Modes.calcBinomial(nValue, kValue)));

				} catch (NumberException ne) {
					JOptionPane.showMessageDialog(modePanel, ne.toString(), "Error", JOptionPane.ERROR_MESSAGE);

				} catch (Exception exception) {
					JOptionPane.showMessageDialog(modePanel, "Error\n" + exception.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;
			case BELL_NUMBER:
				try {
					int nValue;
					nValue = Integer.parseInt(n.getText());
					result.setText(String.valueOf(Modes.calcBell(nValue)));

				} catch (NumberException ne) {
					JOptionPane.showMessageDialog(modePanel, ne.toString(), "Error", JOptionPane.ERROR_MESSAGE);

				} catch (Exception exception) {
					JOptionPane.showMessageDialog(modePanel, "Error\n" + exception.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;

			case FACTORIAL:
				try {
					int nValue;
					nValue = Integer.parseInt(n.getText());
					result.setText(String.valueOf(Modes.calcFactorial(nValue)));

				} catch (NumberException ne) {
					JOptionPane.showMessageDialog(modePanel, ne.toString(), "Error", JOptionPane.ERROR_MESSAGE);

				} catch (Exception exception) {
					JOptionPane.showMessageDialog(modePanel, "Error\n" + exception.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;

			case FUNCTIONS:
				long[] values;
				String[][] data = new String[4][2];
				output.removeAll();
				try {
					int nValue, kValue;
					nValue = Integer.parseInt(n.getText());
					kValue = Integer.parseInt(k.getText());
					values = Modes.calcFunctions(nValue, kValue);

					data[0][0] = "injektiv";
					data[1][0] = "surjektiv";
					data[2][0] = "bijektiv";
					data[3][0] = "alle";

					for (int i = 0; i < values.length; i++) {
						data[i][1] = Long.toString(values[i]);
					}

					String[] headers = { "Art", "Anzahl" };
					functionsTable = new JTable(data, headers) {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}
					};
					tableScroll = new JScrollPane(functionsTable);
					functionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					functionsTable.doLayout();
					Dimension tableDim = functionsTable.getMinimumSize();
					functionsTable.setPreferredScrollableViewportSize(tableDim);
					output.add(tableScroll);

				} catch (NumberException ne) {
					JOptionPane.showMessageDialog(modePanel, ne.toString(), "Error", JOptionPane.ERROR_MESSAGE);

				} catch (Exception exception) {
					JOptionPane.showMessageDialog(modePanel, "Error\n" + exception.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;
			default:
				break;
			}
			resize();
		}

	}

	// create the GUI
	public static void createGUI() {
		GUI window = new GUI("FGdI Rechner");
		window.createComponents();
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
