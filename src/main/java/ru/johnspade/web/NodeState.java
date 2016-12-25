package ru.johnspade.web;

public class NodeState {

	private boolean expanded;

	public NodeState(boolean expanded) {
		this.expanded = expanded;
	}

	@SuppressWarnings("unused")
	public boolean isExpanded() {
		return expanded;
	}

	void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

}
