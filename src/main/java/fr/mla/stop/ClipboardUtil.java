package fr.mla.stop;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardUtil implements ClipboardOwner {

    public static void copyString(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ClipboardUtil clipboardUtil = new ClipboardUtil();
        clipboard.setContents(stringSelection, clipboardUtil);
    }

    public void lostOwnership(Clipboard arg0, Transferable arg1) {
    }

}