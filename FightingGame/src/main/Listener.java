package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import game.GameApplet;

public class Listener implements WindowListener {
	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("dispose");
		GameApplet.app.dispose();
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

}
