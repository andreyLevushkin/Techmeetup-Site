package co.uk.techmeetup.misc;

public class Selectable {

	private boolean selected;
	private final String label;
	private final int id;

	public Selectable(boolean selected, String label, int id) {
		this.selected = selected;
		this.label = label;
		this.id = id;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
