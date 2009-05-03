package co.uk.techmeetup.misc;

public class WeightedString implements Comparable<WeightedString> {

	private final String tag;
	private int weight;

	public WeightedString(String tag, int weight) {
		this.tag = tag;
		this.weight = weight;
	}

	public WeightedString(String tag) {
		this.tag = tag;
	}

	@Override
	public int compareTo(WeightedString in) {
		if (this.equals(in)) {
			return 0;
		}
		int out = in.getWeight() - weight;
		if (out != 0) {
			return out;
		} else {
			return tag.compareTo(in.getTag());
		}
	}

	public String getTag() {
		return tag;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeightedString other = (WeightedString) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return tag;
	}

}
