package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import shared.GameBaseApp;

public class Listener implements WindowListener {
	private GameBaseApp app;

	public Listener(GameBaseApp app) {
		this.app = app;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("dispose");
		app.dispose();
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
