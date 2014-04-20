package fr.mla.stop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class MoveRegistry {

	private ArrayList<Move> moveList;
	private HashMap<Integer, ArrayList<Move>> moveMap;

	private SortedMap<Integer, ArrayList<WMove>> wMoveMap;

	private HashMap<String, Move> connectableMap;

	private static final int ascii_point = 46;

	public MoveRegistry() {

		moveList = new ArrayList<Move>();
		moveMap = new HashMap<Integer, ArrayList<Move>>();
		connectableMap = new HashMap<String, Move>();

	}

	public void addMove(Move move) {
		// Add 'move' to the basic list
		moveList.add(move);

		// Add 'move' to the Dictionnary (per pattern lentgh)
		ArrayList<Move> l = moveMap.get(new Integer(move.pattern.length));
		if (l == null) {
			l = new ArrayList<Move>();
			moveMap.put(new Integer(move.pattern.length), l);
		}
		l.add(move);

		// Add 'move' to the 'connectable' Dictionnary
		String connectableKey = move.getConnectableKey();
		if (connectableKey != null) {
			connectableMap.put(connectableKey, move);
		}

	}

	public void addWMove(WMove wMove) {
		ArrayList<WMove> l = wMoveMap.get(new Integer(wMove.getPointsTotal()));
		if (l == null) {
			l = new ArrayList<WMove>();
			wMoveMap.put(new Integer(wMove.getPointsTotal()), l);
		}
		l.add(wMove);
	}

	public WMove bestWMove() {
		ArrayList<WMove> l = wMoveMap.get(wMoveMap.firstKey());
		return l.get(0);
	}
	
	public boolean isEmpty() {
		return (moveList.size() == 0);
	}
	
	public void writeRepport(String outPath) throws IOException {

		PrintWriter out = new PrintWriter(new FileWriter(outPath));

		for (int i = 0; i < moveList.size(); i++) {
			out.println(moveList.get(i).toString());
		}

		out.close();

	}

	public void writeRepportWMove(String outPath) throws IOException {

		PrintWriter out = new PrintWriter(new FileWriter(outPath));

		Iterator<ArrayList<WMove>> iter = wMoveMap.values().iterator();

		while (iter.hasNext()) {
			ArrayList<WMove> l = iter.next();

			for (int i = 0; i < l.size(); i++) {
				WMove wMove = l.get(i);
				out.println("move " + wMove.getMove().getPositionAsString()
						+ " " + wMove.getWordAsString() + " ("
						+ wMove.getPointsTotal() + " points)");
			}
		}

		out.close();

	}

	public void match(String letters, int jokers, ArrayList<String>[][] words) {
		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words[i].length; j++) {
				// System.out.println("[ " + (i + letters.length() - j) + " = "
				// + i
				// + " + ( " + letters.length() + " - " + j
				// + " ) ]");
				for (int k = 0; k < words[i][j].size(); k++) {
					match(letters, jokers, words[i][j].get(k));
				}
			}
		}

	}

	public void match(String letters, int jokers, String word) {

		try {
			Iterator<Move> iter = moveMap.get(new Integer(word.length())).iterator();
		
			while (iter.hasNext()) {
	
				Move move = iter.next();
				match(letters, jokers, word, move);
			}
			
		} catch (NullPointerException e) {
			return;
		}
	}

	public void match(String letters, int jokers, String word, Move move) {

		// System.out.println("BEGIN MATCH - " + letters + " - " + jokers +
		// " - " + word + " - " + move.toString());

		byte[] tLetters = letters.getBytes();
		byte[] tWord = word.getBytes();

		int jokersLeft = jokers;
		int points = 0;
		int coefMot = 1;
		int nbLetters = 0;

		byte[] xWord = new byte[move.pattern.length];

		for (int i = 0; i < move.pattern.length; i++) {
			if (move.pattern[i] == ascii_point) {

				int pointsLettre = 0;
				int coefLettre = 1;

				boolean ok = false;
				nbLetters++;
				for (int j = 0; j < tLetters.length; j++) {
					if (tLetters[j] == tWord[i]) {
						tLetters[j] = 0;

						xWord[i] = (byte) (tWord[i] + 32);

						pointsLettre = Board.score[tWord[i] - 65];

						ok = true;
						break;
					}
				}
				if (!ok) {
					if (jokersLeft > 0) {
						xWord[i] = tWord[i];
						jokersLeft--;
					} else {
						return;
					}
				}

				int modificateurPoints = (move.direction == Move.HORIZONTAL ? Board.coef[move.posI][move.posJ
						+ i]
						: Board.coef[move.posI + i][move.posJ]);

				if (modificateurPoints == Board.LCD) {
					coefLettre = 2;
				} else if (modificateurPoints == Board.LCT) {
					coefLettre = 3;
				} else if (modificateurPoints == Board.MCD) {
					coefMot *= 2;
				} else if (modificateurPoints == Board.MCT) {
					coefMot *= 3;
				}

				points += (pointsLettre * coefLettre);

			} else if (move.pattern[i] == tWord[i]) {
				points += Board.score[tWord[i] - 65];
				xWord[i] = (byte) (tWord[i] + 32);
			} else if (move.pattern[i] == tWord[i] + 32) {
				xWord[i] = (byte) (tWord[i] + 32);
			} else {
				return;
			}
		}

		points *= coefMot;
		if (nbLetters == 7) {
			points += Board.scrabbleBonus;
		}

		WMove wMove = new WMove(xWord, points, move);
		move.addWMove(wMove);

	}

	public void checkConnections(Move move) {

		for (int i = 0; i < move.pattern.length; i++) {
			if (move.pattern[i] == ascii_point) {

				int _posI = move.posI;
				int _posJ = move.posJ;

				if (move.direction == Move.HORIZONTAL) {
					_posJ += i;
				} else {
					_posI += i;
				}

				String connectableKey = Move.getPositionAsString(
						1 - move.direction, _posI, _posJ);

				Move crossMove = connectableMap.get(connectableKey);
				if (crossMove != null) {

					Iterator<WMove> iter = move.getWMoveIterator();
					while (iter.hasNext()) {
						WMove wMove = iter.next();
						if (wMove.isValid()) {

							int points = getScoreByConnection(crossMove, wMove
									.getWord()[i]);

							if (points < 0) {
								wMove.invalidate();
							} else {
								wMove.addCrossPoints(points);
							}
						}
					}
				}
			}
		}
	}

	public void checkConnections() throws IOException {

		Iterator<Move> iter = moveList.iterator();
		while (iter.hasNext()) {
			checkConnections(iter.next());
		}
	}

	// if the connection is possible through this 'crossMove' with this letter,
	// the number of points given by this connection is returned.
	// Otherwise, -1 is returned.
	private int getScoreByConnection(Move crossMove, byte letter) {

		// We are sure there is only 1 point in this pattern.
		// index is the position of this unique point.
		int index = 0;
		while (crossMove.pattern[index] != ascii_point) {
			index++;
		}

		Iterator<WMove> iter = crossMove.getWMoveIterator();
		while (iter.hasNext()) {
			WMove crossWMove = iter.next();
			if (crossWMove.getWord()[index] == letter) {
				return crossWMove.getPoints();
			}
		}
		return -1;
	}

	// Build a list of all possible WMove sorted by score decreasingly
	public void buildWMoveMap() {

		wMoveMap = new TreeMap<Integer, ArrayList<WMove>>(
				new Comparator<Integer>() {
					public int compare(Integer i1, Integer i2) {
						return i2.compareTo(i1);
					}
				});
		
		Iterator<Move> moveIter = moveList.iterator();
		
		while (moveIter.hasNext()) {
			Iterator<WMove> wMoveIter = moveIter.next().getWMoveIterator();
			while (wMoveIter.hasNext()) {
				WMove wMove = wMoveIter.next();
				if (wMove.isValid()) {
					addWMove(wMove);
				}
			}
		}
	}

}
