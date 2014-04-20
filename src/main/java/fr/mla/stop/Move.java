package fr.mla.stop;

import java.util.ArrayList;
import java.util.Iterator;


public class Move {

	public byte[] pattern;
    public int posI;
    public int posJ;
    public int direction;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    private ArrayList<WMove> wMoveList;
    
	private static final int ascii_point = 46;

	public Move(byte[] pattern, int posI, int posJ, int direction) {
        this.pattern = pattern;
        this.posI = posI;
        this.posJ = posJ;
        this.direction = direction;
        
        wMoveList = new ArrayList<WMove>();
    }

    public void addWMove(WMove wMove) {
    	wMoveList.add(wMove);
    }
    
//    public void removeWMove(WMove wMove) {
//    	wMoveList.remove(wMove);
//    }
    
    public Iterator<WMove> getWMoveIterator() {
    	return wMoveList.iterator();
    }
    
    public String getConnectableKey() {
    	int nbPoint = 0;
    	
    	int _posI = posI;
    	int _posJ = posJ;
    	
    	for (int i = 0; i < pattern.length; i++) {
    		if (pattern[i] == ascii_point) {
    			nbPoint++;
    			if (nbPoint > 1) {
    				return null;
    			}
    			if (direction == HORIZONTAL) {
    				_posJ += i;
    			} else {
    				_posI += i;
    			}
    		}
    	}
    	return getPositionAsString(direction, _posI, _posJ);
    }
    
    public static String getPositionAsString(int _direction, int _posI, int _posJ) {
        String position = null;
        if (_direction == HORIZONTAL) {
            position = new String(new byte[]{(byte) (_posI + 65)});
            position += (_posJ + 1);
        }
        if (_direction == VERTICAL) {
            position = new String(new byte[]{(byte) (_posI + 65)});
            position = (_posJ + 1) + position;
        }
        return position;
    }

    public String getPositionAsString() {
    	return getPositionAsString(direction, posI, posJ);
    }
    
    
    public String toString() {
    	String result = getPositionAsString() + " [" + posI + "," + posJ + "]" + "\t" + new String(this.pattern);
    	
    	String connectableKey = getConnectableKey();
    	if (connectableKey != null) {
    		result += "\tConnectable --> " + connectableKey;
    	}
    	
//    	Iterator<WMove> iter = wMoveList.iterator();
//    	while (iter.hasNext()) {
//    		WMove wMove = iter.next();
//    		result += "\n\t\tmove " + getPositionAsString() + " " + wMove.getWordAsString() + " (" + wMove.getPointsTotal() + " points)";
//    	}

    	return result;
    }
    
}
