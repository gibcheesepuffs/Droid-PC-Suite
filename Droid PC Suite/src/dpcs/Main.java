package dpcs;

import java.io.File;

import javax.swing.SwingUtilities;
import java.awt.Color;

public class Main {
	@SuppressWarnings("static-access")
	public static void main(String args[]) throws Exception {
		Splash image = new Splash();
		image.getContentPane().setBackground(Color.WHITE);
		image.setVisible(true);
		Thread thread = Thread.currentThread();
		thread.sleep(5000);
		image.dispose();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Interface().setVisible(true);
				try {
					Process p1 = Runtime.getRuntime().exec("adb kill-server");
					p1.waitFor();
					File file1 = new File(".checkadbconnection"); // Old files
																	// remover
					if (file1.exists() && !file1.isDirectory()) {
						file1.delete();
						File file2 = new File("su");
						if (file2.exists() && !file2.isDirectory()) {
							file2.delete();
						}
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});
	}
}
