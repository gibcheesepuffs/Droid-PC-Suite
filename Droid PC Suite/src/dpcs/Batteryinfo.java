/*****************************************************************************
 * dpcs/Batteryinfo.java: Battery Information Viewer JFrame for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2019 Shouko Komi
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package dpcs;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Batteryinfo extends JFrame {
	JTextArea BatteryInformationViewer;
	private JPanel contentPane;
	private JButton btnRefresh;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Batteryinfo frame = new Batteryinfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Batteryinfo() {
		setTitle("Battery Information");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Batteryinfo.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 343);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 378, 238);
		contentPane.add(scrollPane);

		BatteryInformationViewer = new JTextArea();
		BatteryInformationViewer.setEditable(false);
		scrollPane.setViewportView(BatteryInformationViewer);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell dumpsys battery > /sdcard/.batteryinfo.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.batteryinfo.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.batteryinfo.txt");
					p3.waitFor();
					Reader reader = new FileReader(new File(".batteryinfo.txt"));
					BatteryInformationViewer.read(reader, "");
					File file = new File(".batteryinfo.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRefresh.setToolTipText("Refetch battery information from android device");
		btnRefresh.setBounds(26, 250, 120, 47);
		contentPane.add(btnRefresh);

		JButton btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (BatteryInformationViewer.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Cannot save empty file!");
				} else {
					JFrame parentFrame = new JFrame();
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
					fileChooser.setFileFilter(filter);
					fileChooser.setDialogTitle("Save as a text file");
					int userSelection = fileChooser.showSaveDialog(parentFrame);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						FileWriter write = null;
						try {
							write = new FileWriter(fileToSave.getAbsolutePath() + ".txt");
							BatteryInformationViewer.write(write);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (write != null)
								try {
									write.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					}
				}
			}
		});
		btnSaveToFile.setToolTipText("Save battery information from screen to a file on the computer");
		btnSaveToFile.setBounds(223, 250, 120, 47);
		contentPane.add(btnSaveToFile);
		try {
			Process p1 = Runtime.getRuntime().exec("adb shell dumpsys battery > /sdcard/.batteryinfo.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.batteryinfo.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.batteryinfo.txt");
			p3.waitFor();
			Reader reader = new FileReader(new File(".batteryinfo.txt"));
			BatteryInformationViewer.read(reader, "");
			File file = new File(".batteryinfo.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}