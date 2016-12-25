package ru.johnspade.web;

import java.util.ArrayList;
import java.util.List;

public class Tree {

	private String text;
	private List<Tree> nodes;
	private String href;
	private NodeState state;
	private List<String> tags;

	public Tree(String text, NodeState state, List<Tree> nodes, String href) {
		this.text = text;
		this.state = state;
		this.nodes = nodes;
		this.href = href;
	}

	public Tree(String text, boolean expanded) {
		this(text, new NodeState(expanded), new ArrayList<Tree>(), null);
	}

	public String getText() {
		return text;
	}

	public List<Tree> getNodes() {
		return nodes;
	}

	@SuppressWarnings("unused")
	public NodeState getState() {
		return state;
	}

	@SuppressWarnings("unused")
	public boolean isSelectable() {
		return false;
	}

	public String getHref() {
		return href;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void expand(boolean expand) {
		this.state.setExpanded(expand);
	}

}
