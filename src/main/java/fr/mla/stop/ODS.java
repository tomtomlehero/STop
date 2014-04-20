package fr.mla.stop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ODS {

	private String odsPath;

	private int deltaWord;
	private int deltaLetters;

	private String sLetters;
	private byte[] letters;

	private int[] alpha;

	public ArrayList<String>[][] words;

	public ODS(String sLetters, String odsPath) {

		this.odsPath = odsPath;
		this.sLetters = sLetters;

		deltaWord = 15;
		deltaLetters = sLetters.length();

		letters = sLetters.getBytes();
		alpha = new int[26];
	}

	@SuppressWarnings("unchecked")
	public void findWords() throws IOException {

		words = new ArrayList[deltaWord + 1][deltaLetters + 1];

		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words[i].length; j++) {
				words[i][j] = new ArrayList<String>();
			}
		}

		BufferedReader in = new BufferedReader(new FileReader(odsPath));

		String word;

		while ((word = in.readLine()) != null) {
			int m = delta(word);
			if (((word.length() - m) <= deltaWord)
					&& ((sLetters.length() - m) <= deltaLetters)) {
				words[word.length() - m][sLetters.length() - m].add(word);
			}
		}
		in.close();
	}

	private int delta(String word) {
		resetAlpha();
		int result = 0;
		for (int i = 0; i < letters.length; i++) {
			int index = word.indexOf(letters[i], alpha[letters[i] - 65]);
			if (index >= 0) {
				alpha[letters[i] - 65] = index + 1;
				result++;
			}
		}
		return result;
	}

	private void resetAlpha() {
		for (int i = 0; i < alpha.length; i++) {
			alpha[i] = 0;
		}
	}

	private String display(String word) {
		resetAlpha();
		byte[] tWord = word.getBytes();
		for (int i = 0; i < tWord.length; i++) {
			int index = sLetters.indexOf(tWord[i], alpha[tWord[i] - 65]);
			if (index >= 0) {
				alpha[tWord[i] - 65] = index + 1;
			} else {
				tWord[i] += 32;
			}
		}
		return new String(tWord);
	}

	public void writeRepport(String outPath) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(outPath));

		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words[i].length; j++) {
				out.println("\n$#### " + (i + sLetters.length() - j) + " = " + i
						+ " + ( " + sLetters.length() + " - " + j
						+ " ) #######################");
				for (int k = 0; k < words[i][j].size(); k++) {
					out.println(display(words[i][j].get(k)));
				}
			}
		}

		int total = 0;
		
		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words[i].length; j++) {
				out.println("§# " + (i + sLetters.length() - j) + " = " + i
						+ " + ( " + sLetters.length() + " - " + j
						+ " ) ########\t" + words[i][j].size() + "\t#####");
				total += words[i][j].size();
			}
		}
		
		out.println("TOTAL = " + total);

		out.close();

	}


}
