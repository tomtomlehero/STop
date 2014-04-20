package fr.mla.stop;

import javax.swing.JOptionPane;

public class WMove {

	private byte[] word;

	private int points;
	private int crossPoints;
	
	private Move move;

	private boolean valid;
	
	public WMove(byte[] word, int points, Move move) {
		this.word = word;
		this.points = points;
		this.move = move;
		valid = true;
		crossPoints = 0;
	}

	public void showResultAndCopyToClipboard() {
		String clipboardMessage = toString();
		String popupMessage = clipboardMessage + " (" + getPointsTotal() + " points)";
		ClipboardUtil.copyString(clipboardMessage);
		JOptionPane.showMessageDialog(null, popupMessage, "STop",
				JOptionPane.INFORMATION_MESSAGE);

	}

	
    public String toString() {
    	return "move " + move.getPositionAsString() + " " + new String(word);
    }
    
	public void addCrossPoints(int points) {
		this.crossPoints += points;
	}
	
	public int getPointsTotal() {
		return points + crossPoints;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getCrossPoints() {
		return crossPoints;
	}
	
	public byte[] getWord() {
		return word;
	}

	public Move getMove() {
		return move;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public String getWordAsString() {
		return new String(word);
	}
	
	public void invalidate() {
		valid = false;
	}
	
}
