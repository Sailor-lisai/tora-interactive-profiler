package com.tivo.jipviewer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author li sai 20140213
 * 
 */
public class SearchBar extends JPanel implements ActionListener, KeyListener {

	private MethodRowTableModel allMethods;
	private ValueModel<JipMethod> currentMethod;
	private JTextField mSearch;
	private JButton mConfirm;
	private SearchResult result;

	public SearchBar(MethodRowTableModel allMethods,
			ValueModel<JipMethod> currentMethod) {
		this.allMethods = allMethods;
		this.currentMethod = currentMethod;
		mSearch = new JTextField(25);
		mSearch.addKeyListener(this);
		mConfirm = new JButton("Find");
		mConfirm.addActionListener(this);
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(mSearch);
		this.add(mConfirm);
	}

	private void search() {
		String text = mSearch.getText();
		if (text.equals("")) {
			return;
		}
		search(text);
		if (result.getResult().size() == 0) {
			JOptionPane.showMessageDialog(null, "Can not find any result.");
			return;
		}
		if (result.getResultIter().hasNext()) {
			currentMethod.setValue(result.getResultIter().next());
		} else {
			result.resetIter();
			currentMethod.setValue(result.getResultIter().next());
		}
	}

	private void search(String text) {
		if (result == null) {
			result = new SearchResult(text, doSearch(text));
		}
		if (!result.getSearchText().equalsIgnoreCase(text)) {
			result = new SearchResult(text, doSearch(text));
		}
	}

	private List<JipMethod> doSearch(String text) {
		List<JipMethod> result = new ArrayList<JipMethod>();
		int nRow = allMethods.getRowCount();
		for (int i = 0; i < nRow; i++) {
			MethodRow scan = allMethods.getRow(i);
			if (scan.getMethod().getMethodName().toLowerCase()
					.contains(text.toLowerCase())) {
				result.add(scan.getMethod());
			}
			if (scan.getMethod().getClassName().toLowerCase()
					.contains(text.toLowerCase())) {
				result.add(scan.getMethod());
			}
		}
		return result;
	}

	private static class SearchResult {
		private String searchText;
		private List<JipMethod> result;
		private Iterator<JipMethod> iter;

		public SearchResult(String searchText, List<JipMethod> result) {
			this.searchText = searchText;
			this.result = result;
			this.iter = result.iterator();
		}

		public String getSearchText() {
			return searchText;
		}

		public List<JipMethod> getResult() {
			return result;
		}

		public Iterator<JipMethod> getResultIter() {
			return iter;
		}

		public void resetIter() {
			iter = result.iterator();
		}
	}

	// Button click action
	@Override
	public void actionPerformed(ActionEvent e) {
		search();
	}

	// key listener
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			search();
		}
	}

}
