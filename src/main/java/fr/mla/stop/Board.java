package fr.mla.stop;

public class Board {

	public final static int TYPE_SCRABBLE = 1;
	public final static int TYPE_WORDFEUD = 2;
	
	public final static int LCD = 1;
	public final static int LCT = 2;
	public final static int MCD = 3;
	public final static int MCT = 4;

	private final static int[][] coefScrabble = {
		{ MCT, 0, 0, LCD, 0, 0, 0, MCT, 0, 0, 0, LCD, 0, 0, MCT },
		{ 0, MCD, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, MCD, 0 },
		{ 0, 0, MCD, 0, 0, 0, LCD, 0, LCD, 0, 0, 0, MCD, 0, 0 },
		{ LCD, 0, 0, MCD, 0, 0, 0, LCD, 0, 0, 0, MCD, 0, 0, LCD },
		{ 0, 0, 0, 0, MCD, 0, 0, 0, 0, 0, MCD, 0, 0, 0, 0 },
		{ 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0 },
		{ 0, 0, LCD, 0, 0, 0, LCD, 0, LCD, 0, 0, 0, LCD, 0, 0 },
		{ MCT, 0, 0, LCD, 0, 0, 0, MCD, 0, 0, 0, LCD, 0, 0, MCT },
		{ 0, 0, LCD, 0, 0, 0, LCD, 0, LCD, 0, 0, 0, LCD, 0, 0 },
		{ 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0 },
		{ 0, 0, 0, 0, MCD, 0, 0, 0, 0, 0, MCD, 0, 0, 0, 0 },
		{ LCD, 0, 0, MCD, 0, 0, 0, LCD, 0, 0, 0, MCD, 0, 0, LCD },
		{ 0, 0, MCD, 0, 0, 0, LCD, 0, LCD, 0, 0, 0, MCD, 0, 0 },
		{ 0, MCD, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, MCD, 0 },
		{ MCT, 0, 0, LCD, 0, 0, 0, MCT, 0, 0, 0, LCD, 0, 0, MCT } };

	private final static int[][] coefWordfeud = {
		{ LCT, 0, 0, 0, MCT, 0, 0, LCD, 0, 0, MCT, 0, 0, 0, LCT },
		{ 0, LCD, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCD, 0 },
		{ 0, 0, MCD, 0, 0, 0, LCD, 0, LCD, 0, 0, 0, MCD, 0, 0 },
		{ 0, 0, 0, LCT, 0, 0, 0, MCD, 0, 0, 0, LCT, 0, 0, 0 },
		{ MCT, 0, 0, 0, MCD, 0, LCD, 0, LCD, 0, MCD, 0, 0, 0, MCT },
		{ 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0 },
		{ 0, 0, LCD, 0, LCD, 0, 0, 0, 0, 0, LCD, 0, LCD, 0, 0 },
		{ LCD, 0, 0, MCD, 0, 0, 0, 0, 0, 0, 0, MCD, 0, 0, LCD },
		{ 0, 0, LCD, 0, LCD, 0, 0, 0, 0, 0, LCD, 0, LCD, 0, 0 },
		{ 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCT, 0 },
		{ MCT, 0, 0, 0, MCD, 0, LCD, 0, LCD, 0, MCD, 0, 0, 0, MCT },
		{ 0, 0, 0, LCT, 0, 0, 0, MCD, 0, 0, 0, LCT, 0, 0, 0 },
		{ 0, 0, MCD, 0, 0, 0, LCD, 0, LCD, 0, 0, 0, MCD, 0, 0 },
		{ 0, LCD, 0, 0, 0, LCT, 0, 0, 0, LCT, 0, 0, 0, LCD, 0 },
		{ LCT, 0, 0, 0, MCT, 0, 0, LCD, 0, 0, MCT, 0, 0, 0, LCT } };

	private final static int[] scoreScrabble = {
		1,  // A
		3,  // B
		3,  // C
		2,  // D
		1,  // E
		4,  // F
		2,  // G
		4,  // H
		1,  // I
		8,  // J
		10, // K
		1,  // L
		2,  // M
		1,  // N
		1,  // O
		3,  // P
		8,  // Q
		1,  // R
		1,  // S
		1,  // T
		1,  // U
		4,  // V
		10, // W
		10, // X
		10, // Y
		10  // Z
	};
	
	private final static int[] scoreWordfeud = {
		1,  // A
		3,  // B
		3,  // C
		2,  // D
		1,  // E
		4,  // F
		2,  // G
		4,  // H
		1,  // I
		8,  // J
		10, // K
		2,  // L
		2,  // M
		1,  // N
		1,  // O
		3,  // P
		8,  // Q
		1,  // R
		1,  // S
		1,  // T
		1,  // U
		5,  // V
		10, // W
		10, // X
		10, // Y
		10  // Z
	};
	
	private final static int scrabbleBonusScrabble = 50;
	private final static int scrabbleBonusWordfeud = 40;

	
	public static int[][] coef;
	public static int[] score;
	public static int scrabbleBonus;

	public static void setTypeScrabble() {
		coef = coefScrabble;
		score = scoreScrabble;
		scrabbleBonus = scrabbleBonusScrabble;
	}
	
	public static void setTypeWordfeud() {
		coef = coefWordfeud;
		score = scoreWordfeud;
		scrabbleBonus = scrabbleBonusWordfeud;
	}
	
	public static void display() {
		for (int i = 0; i < coef.length; i++) {
			for (int j = 0; j < coef[i].length; j++) {
				System.out.print(coef[i][j] + " ");
			}
			System.out.println();
		}
	}

}
