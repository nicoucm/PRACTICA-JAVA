package extra.jdialog;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

class DishSelectionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<Dish> _dishes;
	private DefaultComboBoxModel<Dish> _dishesModel;

	public DishSelectionDialog() {
		super((Frame) null, true);
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Food Selector");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Select your favorite");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_dishesModel = new DefaultComboBoxModel<>();
		_dishes = new JComboBox<>(_dishesModel);

		viewsPanel.add(_dishes);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> {
			_status = 0;
			setVisible(false);
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener((e) -> {
			if (_dishesModel.getSelectedItem() != null) {
				_status = 1;
				DishSelectionDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open(Frame parent, List<Dish> dishes) {

		// update the comboxBox model -- if you always use the same no
		// need to update it, you can initialize it in the constructor.
		//
		_dishesModel.removeAllElements();
		for (Dish v : dishes)
			_dishesModel.addElement(v);

		// You can change this to place the dialog in the middle of the parent window.
		//
		setLocation(parent.getLocation().x + 10, parent.getLocation().y + 10);

		setVisible(true);
		return _status;
	}

	Dish getDish() {
		return (Dish) _dishesModel.getSelectedItem();
	}

}
