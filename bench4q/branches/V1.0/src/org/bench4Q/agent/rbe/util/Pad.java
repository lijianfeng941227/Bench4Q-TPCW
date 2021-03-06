package org.bench4Q.agent.rbe.util;

public class Pad {
	/* Pad on the right with spaces. */
	public static String r(int size, String s) {
		return (r(size, " ", s));
	}

	public static String r(int size, double v) {
		return (r(size, String.valueOf(v)));
	}

	public static String r(int size, int v) {
		return (r(size, String.valueOf(v)));
	}

	public static String r(int size, char v) {
		return (r(size, String.valueOf(v)));
	}

	public static String r(int size, Object v) {
		return (r(size, v.toString()));
	}

	/* Pad on the right with zeros. */
	public static String rz(int size, String s) {
		return (r(size, "0", s));
	}

	public static String rz(int size, double v) {
		return (rz(size, String.valueOf(v)));
	}

	public static String rz(int size, int v) {
		return (rz(size, String.valueOf(v)));
	}

	public static String rz(int size, char v) {
		return (rz(size, String.valueOf(v)));
	}

	public static String rz(int size, Object v) {
		return (rz(size, v.toString()));
	}

	/* Pad on the right with arbitrary characters. */
	public static String r(int size, String pad, String s) {
		if (s.length() >= size) {
			return (s);
		}

		return (s + expandRight(size - s.length(), pad));
	}

	public static String r(int size, String pad, double v) {
		String s = String.valueOf(v);
		return (r(size, pad, s));
	}

	public static String r(int size, String pad, int v) {
		String s = String.valueOf(v);
		return (r(size, pad, s));
	}

	public static String r(int size, String pad, char v) {
		String s = String.valueOf(v);
		return (r(size, pad, s));
	}

	public static String r(int size, String pad, Object v) {
		String s = v.toString();
		return (r(size, pad, s));
	}

	/* Pad on the left with zeros. */
	public static String lz(int size, String s) {
		return (l(size, "0", s));
	}

	public static String lz(int size, double v) {
		return (lz(size, String.valueOf(v)));
	}

	public static String lz(int size, int v) {
		return (lz(size, String.valueOf(v)));
	}

	public static String lz(int size, char v) {
		return (lz(size, String.valueOf(v)));
	}

	public static String lz(int size, Object v) {
		return (lz(size, v.toString()));
	}

	/* Pad on the left with spaces. */
	public static String l(int size, String s) {
		return (l(size, " ", s));
	}

	public static String l(int size, double v) {
		return (l(size, String.valueOf(v)));
	}

	public static String l(int size, int v) {
		return (l(size, String.valueOf(v)));
	}

	public static String l(int size, char v) {
		return (l(size, String.valueOf(v)));
	}

	public static String l(int size, Object v) {
		return (l(size, v.toString()));
	}

	/* Pad on the left with arbitrary characters. */
	public static String l(int size, String pad, String s) {
		if (s.length() >= size) {
			return (s);
		}

		return (expandLeft(size - s.length(), pad) + s);
	}

	public static String l(int size, String pad, double v) {
		String s = String.valueOf(v);
		return (l(size, pad, s));
	}

	public static String l(int size, String pad, int v) {
		String s = String.valueOf(v);
		return (l(size, pad, s));
	}

	public static String l(int size, String pad, char v) {
		String s = String.valueOf(v);
		return (l(size, pad, s));
	}

	public static String l(int size, String pad, Object v) {
		String s = v.toString();
		return (l(size, pad, s));
	}

	public static String expandRight(int size, String s) {
		while (s.length() < size)
			s = s + s;

		return (s.substring(0, size));
	}

	public static String expandLeft(int size, String s) {
		while (s.length() < size)
			s = s + s;

		return (s.substring(s.length() - size));
	}
}
