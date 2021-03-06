package org.bench4Q.agent.rbe.util;

public class CharRangeStrPattern extends AbCharStrPattern {
	protected char first; // The character to find.
	protected char last; // The last character to match.

	public CharRangeStrPattern(char first, char last) {
		this.first = first;
		this.last = last;
	}

	// Does this character match.
	protected boolean charMatch(char c) {
		return ((c >= first) && (c <= last));
	};

}
