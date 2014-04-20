package fr.mla.stop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class STop {

	private String odsPath;
	private String inPath;

//	private String outMovePath = "E:\\workspace\\STop\\outMove.txt";
	private String outWMovePath = "out.txt";
//	private String outWMovePath = "C:\\Documents and Settings\\Mathieu\\workspace\\STop\\out.txt";
//	private String outOdsPath = "E:\\workspace\\STop\\outOds.txt";

	private byte[] letters;
	private byte[][] grid;
	private int jokers;

	private MoveRegistry moveRegistry;
	
	private ODS ods;
	
	private static final int ascii_question = 63;
	private static final int ascii_space = 32;
	private static final int ascii_point = 46;

	public STop(String odsPath, String inPath) throws IOException {
		this.odsPath = odsPath;
		this.inPath = inPath;
		
		readInput();
		
		moveRegistry = new MoveRegistry();

		ods = new ODS(new String(letters), this.odsPath);
		
	}

	private void readInput() throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(inPath));

		String sLetters = in.readLine().trim().toUpperCase();

		jokers = 0;
		byte[] b = sLetters.getBytes();
		for (int i = 0; i < b.length; i++) {
			if (b[i] == ascii_question) {
				jokers++;
			}
		}
		letters = new byte[sLetters.length() - jokers];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] != ascii_question) {
				letters[j++] = b[i];
			}
		}

		in.readLine();
		in.readLine();

		grid = new byte[15][];

		for (int i = 0; i < 15; i++) {
			in.readLine();
			byte[] l = in.readLine().getBytes();
			grid[i] = new byte[15];
			for (j = 0; j < 15; j++) {
				grid[i][j] = l[6 + 4 * j];
			}
		}

		in.close();
	}

	public void findMoves() {

		// HORIZONTAL
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 14; j++) {
				for (int k = j + 1; k < 15; k++) {
					if (((j == 0) || (grid[i][j - 1] == ascii_space))
							&& ((k == 14) || (grid[i][k + 1] == ascii_space))) {
						int freeCell = 0; // nombre de cases vides dans cet
						// intervalle
						byte[] b = new byte[k - j + 1];
						for (int l = j; l <= k; l++) {
							if (grid[i][l] == ascii_space) {
								b[l - j] = ascii_point;
								freeCell++;
							} else {
								b[l - j] = grid[i][l];
							}
						}

						if ((freeCell > 0) && (freeCell <= 7)) {
							boolean connex = true;
							if (k - j + 1 == freeCell) {
								connex = false;
								for (int l = j; l <= k; l++) {
									if (((i > 0) && (grid[i - 1][l] != ascii_space))
											|| ((i < 14) && (grid[i + 1][l] != ascii_space))) {
										connex = true;
										break;
									}
								}
							}
							if (connex) {
								moveRegistry.addMove(new Move(b, i, j, Move.HORIZONTAL));
							}
						}
					}
				}
			}
		}

		// VERTICAL
		for (int j = 0; j < 15; j++) {
			for (int i = 0; i < 14; i++) {
				for (int k = i + 1; k < 15; k++) {
					if (((i == 0) || (grid[i - 1][j] == ascii_space))
							&& ((k == 14) || (grid[k + 1][j] == ascii_space))) {
						int freeCell = 0; // nombre de cases vides dans cet
						// intervalle
						byte[] b = new byte[k - i + 1];
						for (int l = i; l <= k; l++) {
							if (grid[l][j] == ascii_space) {
								b[l - i] = ascii_point;
								freeCell++;
							} else {
								b[l - i] = grid[l][j];
							}
						}

						if ((freeCell > 0) && (freeCell <= 7)) {
							boolean connex = true;
							if (k - i + 1 == freeCell) {
								connex = false;
								for (int l = i; l <= k; l++) {
									if (((j > 0) && (grid[l][j - 1] != ascii_space))
											|| ((j < 14) && (grid[l][j + 1] != ascii_space))) {
										connex = true;
										break;
									}
								}
							}
							if (connex) {
								moveRegistry.addMove(new Move(b, i, j, Move.VERTICAL));
							}
						}
					}
				}
			}
		}
		
		if (moveRegistry.isEmpty()) {
			findMovesZero();
		}
		
	}


	public void findMovesZero() {

		// HORIZONTAL
		for (int l = 7; l > 1; l--) {
			for (int j = 8 - l; j < 8; j++) {
				byte[] b = new byte[l];
				for (int k = 0; k < l; k++) {
					b[k] = ascii_point;
				}
				moveRegistry.addMove(new Move(b, 7, j, Move.HORIZONTAL));
			}
		}
		
		// VERTICAL
		for (int l = 7; l > 1; l--) {
			for (int i = 8 - l; i < 8; i++) {
				byte[] b = new byte[l];
				for (int k = 0; k < l; k++) {
					b[k] = ascii_point;
				}
				moveRegistry.addMove(new Move(b, i, 7, Move.VERTICAL));
			}
		}
		
		
	}
	
	
	public void displayInput() {
		System.out.println("LETTERS: " + new String(letters));
		System.out.println("JOKERS: " + jokers);
		System.out.println("GRID:");
		for (int i = 0; i < 15; i++) {
			System.out.println(new String(grid[i]));
		}
	}


	private void displayElapsedTime(long beginTime, String message) {
		System.out.println(message + " - Elapsed Time = " + (((double) (System.currentTimeMillis() - beginTime)) / 1000) + " seconds");
	}
	
	
	public void process() throws IOException {

		long beginTime = System.currentTimeMillis();

		findMoves();
//		displayElapsedTime(beginTime, "findMove --> DONE");
		
		ods.findWords();
//		displayElapsedTime(beginTime, "findWords --> DONE");

		moveRegistry.match(new String(letters), jokers, ods.words);
//		displayElapsedTime(beginTime, "findMatches --> DONE");

		moveRegistry.checkConnections();
//		displayElapsedTime(beginTime, "findConnectable --> DONE");
		
		moveRegistry.buildWMoveMap();
//		displayElapsedTime(beginTime, "buildWMoveMap --> DONE");
		
//		ods.writeRepport(outOdsPath);
//		displayElapsedTime(beginTime, "Write Report ODS --> DONE");
//		moveRegistry.writeRepport(outMovePath);
//		displayElapsedTime(beginTime, "Write Report Moves --> DONE");
		
		moveRegistry.writeRepportWMove(outWMovePath);
//		displayElapsedTime(beginTime, "Write Report WMoves --> DONE");

		//moveRegistry.bestWMove().showResultAndCopyToClipboard();

		long endTime = System.currentTimeMillis();
		
		double elapsedTime = ((double) (endTime - beginTime)) / 1000; 
		
		WMove bestWMove = moveRegistry.bestWMove();
		String clipboardMessage = moveRegistry.bestWMove().toString();
		String popupMessage = clipboardMessage + " (" + bestWMove.getPointsTotal() + " points)" + "\n" + elapsedTime + " secondes";
		ClipboardUtil.copyString(clipboardMessage);
		JOptionPane.showMessageDialog(null, popupMessage, "STop", JOptionPane.INFORMATION_MESSAGE);
		
		
		
	}
	
	public static void main(String[] args) {

		try {
			String odsPath = args[0];
			String inPath = args[1];
			String type = args[2];

			if ("SCRABBLE".equals(type)) {
				Board.setTypeScrabble();
			} else if ("WORDFEUD".equals(type)) {
				Board.setTypeWordfeud();
			} else {
				usage();
			}
			
			
			STop sTop = new STop(odsPath, inPath);
			
			sTop.process();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void usage() {
		System.out.println("U S A G E");
	}
}
