package dpcs;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.codec.digest.DigestUtils;

@SuppressWarnings("serial")
public class Interface extends JFrame {
	JLabel FlasherDone, GeneralDone, WiperDone, BackupAndRestoreDone, ADBConnectionLabel, RootStatusLabel, AppStatus;
	boolean adbconnected = false, rooted = false;
	private JPanel contentPane;
	JTextArea CalculatedCrypto;

	volatile boolean flag = true;
	Runnable r = new Runnable() {
		public void run() {
			while (flag) {
				try {
					adbconnected = false; // ADB initial state
					Process p1 = Runtime.getRuntime().exec("adb devices");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb shell touch /sdcard/.checkadbconnection");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb pull /sdcard/.checkadbconnection");
					p3.waitFor();
					Process p4 = Runtime.getRuntime().exec("adb shell rm /sdcard/.checkadbconnection");
					p4.waitFor();
					File file = new File(".checkadbconnection");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						adbconnected = true;
						ADBConnectionLabel.setText("Device is connected");
					} else {
						adbconnected = false;
						ADBConnectionLabel.setText("Connect your device");
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}

				try {
					File file = new File("su");
					Process p1 = Runtime.getRuntime().exec("adb pull /system/xbin/su");
					p1.waitFor();
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						rooted = true;
						RootStatusLabel.setText("Device is rooted");
					} else {
						if (adbconnected == true) {
							rooted = false;
							RootStatusLabel.setText("Device is not rooted");
						} else {
							rooted = false;
							RootStatusLabel.setText("");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		}
	};

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Interface() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Interface.class.getResource("/graphics/Icon.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) { // Cache remover
				File file1 = new File("explorer.properties");
				if (file1.exists() && !file1.isDirectory()) {
					file1.delete();
				}
				File file2 = new File("log.txt");
				if (file2.exists() && !file2.isDirectory()) {
					file2.delete();
				}
				File file3 = new File("lastLog.txt");
				if (file3.exists() && !file3.isDirectory()) {
					file3.delete();
				}
				File file4 = new File(".logcat.txt");
				if (file4.exists() && !file4.isDirectory()) {
					file3.delete();
				}
				File file5 = new File(".userapps.txt");
				if (file5.exists() && !file5.isDirectory()) {
					file5.delete();
				}
				File file6 = new File(".privapps.txt");
				if (file6.exists() && !file6.isDirectory()) {
					file6.delete();
				}
				File file7 = new File(".systemapps.txt");
				if (file7.exists() && !file7.isDirectory()) {
					file7.delete();
				}
			}
		});

		setTitle("Droid PC Suite");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1088, 715);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem mntmExit = new JMenuItem("Exit");

		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		mnFile.add(mntmExit);
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmCommonWorkarounds = new JMenuItem("Common workarounds");
		mntmCommonWorkarounds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workarounds obj = new Workarounds();
				obj.setVisible(true);
			}
		});

		JMenuItem mntmChangelog = new JMenuItem("Changelog tracker");
		mntmChangelog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Changelog obj = new Changelog();
				obj.setVisible(true);
			}
		});

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About obj = new About();
				obj.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		JMenu mnADBTools = new JMenu("ADB tools");
		mnHelp.add(mnADBTools);

		JMenuItem mntmForceConnect = new JMenuItem("Force connect");
		mnADBTools.add(mntmForceConnect);
		mntmForceConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Go to developer options and turn off android debugging and turn it on again");
				JOptionPane.showMessageDialog(null,
						"Now tap on Revoke USB debugging authorizations and confirm it by tapping OK on android device");
				JOptionPane.showMessageDialog(null, "Now disconnect your android device and reconnect it via USB");
				JOptionPane.showMessageDialog(null, "Reboot your device. After it completely boots up click OK");
				try {
					adbconnected = false;
					Process p1 = Runtime.getRuntime().exec("adb kill-server");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb devices");
					p2.waitFor();
					JOptionPane.showMessageDialog(null, "Check if your device asks to Allow USB debugging");
					JOptionPane.showMessageDialog(null,
							"If yes check always allow from this computer checkbox and tap OK on your android device");
					Process p3 = Runtime.getRuntime().exec("adb shell touch /sdcard/.checkadbconnection");
					p3.waitFor();
					Process p4 = Runtime.getRuntime().exec("adb pull /sdcard/.checkadbconnection");
					p4.waitFor();
					Process p5 = Runtime.getRuntime().exec("adb shell rm /sdcard/.checkadbconnection");
					p5.waitFor();
					File file = new File(".checkadbconnection");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						adbconnected = true;
						ADBConnectionLabel.setText("Device is connected");
						JOptionPane.showMessageDialog(null, "Success!");
					} else {
						adbconnected = false;
						ADBConnectionLabel.setText("");
						ADBConnectionLabel.setText("Connect your device");
						JOptionPane.showMessageDialog(null,
								"Please try again or perhaps try installing your android device adb drivers on PC");
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
				try {
					File file = new File("su");
					Process p1 = Runtime.getRuntime().exec("adb pull /system/xbin/su");
					p1.waitFor();
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						rooted = true;
						RootStatusLabel.setText("Device is rooted");
					} else {
						if (adbconnected == true) {
							rooted = false;
							RootStatusLabel.setText("Device is not rooted");
						} else {
							rooted = false;
							RootStatusLabel.setText("");
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmState = new JMenuItem("View device state");
		mntmState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb get-state");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, "State: " + reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmAdbHelp = new JMenuItem("View ADB help");
		mntmAdbHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ADBHelp obj = new ADBHelp();
				obj.setVisible(true);
			}
		});
		mnADBTools.add(mntmAdbHelp);

		JMenuItem mntmAdbVersion = new JMenuItem("View ADB version");
		mnADBTools.add(mntmAdbVersion);
		mntmAdbVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb version");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnADBTools.add(mntmState);

		JMenuItem mntmSerialNo = new JMenuItem("View serial no.");
		mnADBTools.add(mntmSerialNo);
		mntmSerialNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb get-serialno");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, "Serial No: " + reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmWaitForDevice = new JMenuItem("Wait for device");
		mntmWaitForDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb wait-for-device");
					p1.waitFor();
					JOptionPane.showMessageDialog(null, "Waiting...");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnADBTools.add(mntmWaitForDevice);
		mnHelp.add(mntmChangelog);

		JMenuItem mntmCheckForUpdates = new JMenuItem("Check for updates");
		mntmCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Checkout downloads section in XDA-Developers thread");
				try {
					Desktop.getDesktop()
							.browse(new URL(
									"http://forum.xda-developers.com/android/development/tool-droid-pc-suite-t3398599")
											.toURI());
				} catch (Exception e1) {
				}
			}
		});
		mnHelp.add(mntmCheckForUpdates);
		mnHelp.add(mntmCommonWorkarounds);

		JMenuItem mntmNeedHelp = new JMenuItem("Online help");
		mntmNeedHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JOptionPane.showMessageDialog(null, "Post your question on XDA-Developers thread");
					Desktop.getDesktop()
							.browse(new URL(
									"http://forum.xda-developers.com/android/development/tool-droid-pc-suite-t3398599")
											.toURI());
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});

		JMenu mnLegalInformation = new JMenu("Legal information");
		mnHelp.add(mnLegalInformation);

		JMenuItem mntmDroidPcSuite = new JMenuItem("Droid PC Suite license");
		mntmDroidPcSuite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GPLLicense obj = new GPLLicense();
				obj.setVisible(true);
			}
		});
		mnLegalInformation.add(mntmDroidPcSuite);

		JMenuItem mntmOpenSourceLicenses = new JMenuItem("Open source licenses");
		mntmOpenSourceLicenses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ApacheLicense obj = new ApacheLicense();
				obj.setVisible(true);
			}
		});
		mnLegalInformation.add(mntmOpenSourceLicenses);
		mnHelp.add(mntmNeedHelp);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		AppStatus = new JLabel("");
		AppStatus.setBounds(12, 230, 1062, 17);
		contentPane.add(AppStatus);

		RootStatusLabel = new JLabel("");
		RootStatusLabel.setForeground(Color.RED);
		RootStatusLabel.setBounds(921, 12, 153, 17);
		contentPane.add(RootStatusLabel);

		ADBConnectionLabel = new JLabel("");
		ADBConnectionLabel.setForeground(Color.GREEN);
		ADBConnectionLabel.setBounds(921, 0, 152, 17);
		contentPane.add(ADBConnectionLabel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 255, 1075, 447);
		contentPane.add(tabbedPane);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.WHITE);
		tabbedPane.addTab("General", null, panel_7, null);
		panel_7.setLayout(null);

		final JButton btnInstallUserApp = new JButton("Install Apps as User");
		btnInstallUserApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneralDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Installing...");
						String[] commands = new String[3];
						commands[0] = "adb";
						commands[1] = "install";
						commands[2] = file.getAbsolutePath();
						AppStatus.setText("Installing App...");
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText(filename + " has been successfully installed on your android device!");
						GeneralDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnInstallUserApp.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		final JButton btnInstallAsPrivApp = new JButton("Install Apps to priv-app *#");
		btnInstallAsPrivApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GeneralDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						AppStatus.setText("Installing...");
						Process p1 = Runtime.getRuntime().exec("adb remount");
						p1.waitFor();
						String[] pushcommand = new String[4];
						pushcommand[0] = "adb";
						pushcommand[1] = "push";
						pushcommand[2] = file.getAbsolutePath();
						pushcommand[3] = "/system/priv-app/";
						AppStatus.setText("Installing App...");
						Process p2 = Runtime.getRuntime().exec(pushcommand, null);
						p2.waitFor();
						AppStatus.setText("Rebooting your android device");
						Process p3 = Runtime.getRuntime().exec("adb reboot");
						p3.waitFor();
						AppStatus.setText("");
						GeneralDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnInstallAsPrivApp.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		JButton btnUninstallApps = new JButton("Uninstall Apps");
		btnUninstallApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneralDone.setText("");
				Uninstaller obj = new Uninstaller();
				obj.setVisible(true);
			}
		});

		final JButton btnInstallAsSystemApp = new JButton("Install Apps to System *");
		btnInstallAsSystemApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GeneralDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						AppStatus.setText("Installing...");
						Process p1 = Runtime.getRuntime().exec("adb remount");
						p1.waitFor();
						String[] pushcommand = new String[4];
						pushcommand[0] = "adb";
						pushcommand[1] = "push";
						pushcommand[2] = file.getAbsolutePath();
						pushcommand[3] = "/system/app/";
						Process p2 = Runtime.getRuntime().exec(pushcommand, null);
						p2.waitFor();
						AppStatus.setText("Rebooting your android device");
						Process p3 = Runtime.getRuntime().exec("adb reboot");
						p3.waitFor();
						AppStatus.setText("");
						GeneralDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnInstallAsSystemApp.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		JButton btnADBTerminal = new JButton("ADB Terminal");
		btnADBTerminal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Terminal obj = new Terminal();
				obj.setVisible(true);
			}
		});

		JButton btnBuildpropeditor = new JButton("build.prop Editor");
		btnBuildpropeditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Buildpropeditor obj = new Buildpropeditor();
				obj.setVisible(true);
			}
		});
		btnBuildpropeditor.setBounds(25, 236, 220, 75);
		panel_7.add(btnBuildpropeditor);
		btnADBTerminal.setBounds(548, 131, 220, 75);
		panel_7.add(btnADBTerminal);

		JLabel lblNoteInstallationTo = new JLabel("# Only for android 4.4.x and higher");
		lblNoteInstallationTo.setBounds(12, 338, 490, 15);
		panel_7.add(lblNoteInstallationTo);
		btnInstallAsSystemApp.setBounds(548, 27, 220, 75);
		panel_7.add(btnInstallAsSystemApp);

		GeneralDone = new JLabel("");
		GeneralDone.setText("");
		GeneralDone.setBounds(766, 27, 300, 200);
		panel_7.add(GeneralDone);
		btnUninstallApps.setBounds(282, 131, 220, 75);
		panel_7.add(btnUninstallApps);

		JButton btnFileManager = new JButton("File Manager");
		btnFileManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneralDone.setText("");
				try {
					Process p1 = Runtime.getRuntime().exec("java -jar .filemanager.jar");
					p1.waitFor();
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnFileManager.setBounds(25, 131, 220, 75);
		panel_7.add(btnFileManager);

		JLabel lblNeedsRoot = new JLabel(
				"* Needs root access, also may not work with some devices regardless of root access");
		lblNeedsRoot.setBounds(12, 365, 689, 15);
		panel_7.add(lblNeedsRoot);
		btnInstallAsPrivApp.setBounds(282, 27, 220, 75);
		panel_7.add(btnInstallAsPrivApp);
		btnInstallUserApp.setBounds(25, 27, 220, 75);
		panel_7.add(btnInstallUserApp);

		JButton btnScreenshot = new JButton("Screenshot");
		btnScreenshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell screencap -p /sdcard/screenshot.png");
					p1.waitFor();
					JFileChooser directorychooser = new JFileChooser();
					directorychooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					directorychooser.setDialogTitle("Select path to save the screenshot");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png");
					directorychooser.setFileFilter(filter);
					directorychooser.setApproveButtonText("Save");
					int returnVal = directorychooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/screenshot.png "
								+ directorychooser.getSelectedFile().getAbsolutePath());
						p2.waitFor();
					}
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/screenshot.png");
					p3.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnScreenshot.setBounds(282, 236, 220, 75);
		panel_7.add(btnScreenshot);

		JButton btnScreenRecorder = new JButton("Screen Recorder #");
		btnScreenRecorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] options = new String[] { "5 Sec", "30 Sec", "60 Sec", "180 Sec", "Custom" };
				int response = JOptionPane.showOptionDialog(null, "Select duration of recording", "Screen Recorder",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				int time = 0, bitrate = 8000000;
				try {
					if (response == 0) {
						time = 5;
					}
					if (response == 1) {
						time = 30;
					}
					if (response == 2) {
						time = 60;
					}
					if (response == 3) {
						time = 180;
					}
					if (response == 4) {
						time = Integer.parseInt(JOptionPane.showInputDialog(null,
								"Enter the duration of recording in seconds (1 - 180): for ex. 25 for 25 Seconds"));
						bitrate = Integer.parseInt(JOptionPane.showInputDialog(null,
								"Enter the bitrate of recording (Default = 8000000 (8Mbps))"));
					}
					JOptionPane.showMessageDialog(null, "You will need to wait for " + time + " seconds, Click ok");
					Process p1 = Runtime.getRuntime().exec("adb shell screenrecord --bit-rate " + bitrate
							+ " --time-limit " + time + " /sdcard/videorecording.mp4");
					p1.waitFor();
					JOptionPane.showMessageDialog(null, "Recording finished, Select destination to save the file");
					JFileChooser directorychooser = new JFileChooser();
					directorychooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					directorychooser.setDialogTitle("Select path to save the recording");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("MP4 Files", "mp4");
					directorychooser.setFileFilter(filter);
					directorychooser.setApproveButtonText("Save");
					int returnVal = directorychooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/videorecording.mp4 "
								+ directorychooser.getSelectedFile().getAbsolutePath());
						p2.waitFor();
					}
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/videorecording.mp4");
					p3.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnScreenRecorder.setBounds(548, 236, 220, 75);
		panel_7.add(btnScreenRecorder);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		tabbedPane.addTab("Flasher", null, panel, null);
		panel.setLayout(null);

		final JButton btnFlashSystem = new JButton("System");
		btnFlashSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase system");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "system";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashSystem.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		final JButton btnFlashData = new JButton("Data");
		btnFlashData.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase data");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "data";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashData.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		final JButton btnFlashViaRecovery = new JButton("Flash via Recovery");
		btnFlashViaRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("zip Files", "zip");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						JOptionPane.showMessageDialog(null,
								"Select Update via ADB from recovery menu using physical keys on your device");
						String[] commands = new String[3];
						commands[0] = "adb";
						commands[1] = "sideload";
						commands[2] = file.getAbsolutePath();
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText("Sideloaded...");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashViaRecovery.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		JLabel lblAndroidIsNot = new JLabel("android is not booted ex. fastboot, bootloader, booting etc.");
		lblAndroidIsNot.setBounds(568, 334, 475, 19);
		panel.add(lblAndroidIsNot);

		JLabel lblDontWorry = new JLabel("Note: Don't worry if the app says to connect your device while");
		lblDontWorry.setBounds(525, 317, 518, 19);
		panel.add(lblDontWorry);

		FlasherDone = new JLabel("");
		FlasherDone.setText("");
		FlasherDone.setBounds(760, 29, 300, 200);
		panel.add(FlasherDone);
		btnFlashViaRecovery.setBounds(275, 154, 200, 75);
		panel.add(btnFlashViaRecovery);
		btnFlashData.setBounds(525, 29, 200, 75);
		panel.add(btnFlashData);
		btnFlashSystem.setBounds(275, 261, 200, 75);
		panel.add(btnFlashSystem);

		JLabel lblDeviceMust = new JLabel("* Device must be in fastboot mode");
		lblDeviceMust.setBounds(574, 356, 469, 19);
		panel.add(lblDeviceMust);

		final JButton btnFlashCache = new JButton("Cache");
		btnFlashCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase cache");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "cache";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashCache.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashCache.setBounds(275, 29, 200, 75);
		panel.add(btnFlashCache);

		final JButton btnBootImage = new JButton("Boot");
		btnBootImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase boot");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "boot";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnBootImage.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnBootImage.setBounds(30, 29, 200, 75);
		panel.add(btnBootImage);

		final JButton btnFlashDatazip = new JButton("Data.zip");
		btnFlashDatazip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("zip Files", "zip");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						String[] commands = new String[3];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = file.getAbsolutePath();
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashDatazip.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashDatazip.setBounds(30, 146, 200, 75);
		panel.add(btnFlashDatazip);

		final JButton btnFlashRecovery = new JButton("Recovery");
		btnFlashRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase recovery");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "recovery";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashRecovery.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashRecovery.setBounds(525, 154, 200, 75);
		panel.add(btnFlashRecovery);

		JLabel lblYouMust = new JLabel("* You must have a bootloader that supports fastboot commands");
		lblYouMust.setBounds(22, 356, 533, 19);
		panel.add(lblYouMust);

		final JButton btnFlashSplash = new JButton("Splash");
		btnFlashSplash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase splash");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "splash";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashSplash.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashSplash.setBounds(30, 261, 200, 75);
		panel.add(btnFlashSplash);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("Wiper", null, panel_1, null);
		panel_1.setLayout(null);

		JLabel label_11 = new JLabel("android is not booted ex. fastboot, bootloader, booting etc.");
		label_11.setBounds(313, 323, 475, 19);
		panel_1.add(label_11);

		JLabel label_3 = new JLabel("Note: Don't worry if the app says to connect your device while");
		label_3.setBounds(270, 306, 518, 19);
		panel_1.add(label_3);

		WiperDone = new JLabel("");
		WiperDone.setText("");
		WiperDone.setBounds(758, 26, 300, 200);
		panel_1.add(WiperDone);

		JLabel lblYouDeviceBootloader = new JLabel("You device bootloader must support fastboot commands");
		lblYouDeviceBootloader.setBounds(523, 357, 470, 19);
		panel_1.add(lblYouDeviceBootloader);

		JLabel label_14 = new JLabel("Device must be in fastboot mode");
		label_14.setBounds(270, 357, 252, 19);
		panel_1.add(label_14);

		JLabel label_13 = new JLabel("** Device must be rooted");
		label_13.setBounds(6, 357, 252, 19);
		panel_1.add(label_13);

		JButton btnWipeRecovery = new JButton("Recovery");
		btnWipeRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase cache");
					p1.waitFor();
					AppStatus.setText("Recovery has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeRecovery.setBounds(270, 137, 200, 75);
		panel_1.add(btnWipeRecovery);

		JButton btnWipeBoot = new JButton("Boot");
		btnWipeBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase boot");
					p1.waitFor();
					AppStatus.setText("Boot has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeBoot.setBounds(25, 26, 200, 75);
		panel_1.add(btnWipeBoot);

		JButton btnWipeSystem = new JButton("System");
		btnWipeSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase system");
					p1.waitFor();
					AppStatus.setText("System has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeSystem.setBounds(25, 241, 200, 75);
		panel_1.add(btnWipeSystem);

		JButton btnWipeSplash = new JButton("Splash");
		btnWipeSplash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase splash");
					p1.waitFor();
					AppStatus.setText("Splash has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeSplash.setBounds(523, 137, 200, 75);
		panel_1.add(btnWipeSplash);

		JButton btnWipeData = new JButton("Data");
		btnWipeData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase data");
					p1.waitFor();
					AppStatus.setText("Data has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeData.setBounds(25, 137, 200, 75);
		panel_1.add(btnWipeData);

		JButton btnFlashDalvikCache = new JButton("Dalvik Cache **");
		btnFlashDalvikCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("adb shell su -c rm * /data/dalvik-cache");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb shell su -c rm * /cache/dalvik-cache");
					p2.waitFor();
					AppStatus.setText("Dalvik Cache has been wiped! Now rebooting device...");
					Process p3 = Runtime.getRuntime().exec("adb reboot");
					p3.waitFor();
					AppStatus.setText("Done");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnFlashDalvikCache.setBounds(518, 26, 200, 75);
		panel_1.add(btnFlashDalvikCache);

		JButton btnWipeCache = new JButton("Cache");
		btnWipeCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase cache");
					p1.waitFor();
					AppStatus.setText("Cache has been wiped! Now rebooting device...");
					Process p2 = Runtime.getRuntime().exec("adb reboot");
					p2.waitFor();
					AppStatus.setText("Done");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeCache.setBounds(270, 26, 200, 75);
		panel_1.add(btnWipeCache);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		tabbedPane.addTab("Rebooter", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel label_12 = new JLabel("Note: Don't worry if the app says to connect your device while");
		label_12.setBounds(491, 314, 518, 19);
		panel_2.add(label_12);

		JLabel label_16 = new JLabel("android is not booted ex. fastboot, bootloader, booting etc.");
		label_16.setBounds(534, 332, 464, 19);
		panel_2.add(label_16);

		JLabel lblRebootFrom = new JLabel("Reboot from :");
		lblRebootFrom.setBounds(28, 182, 200, 15);
		panel_2.add(lblRebootFrom);

		JLabel lblRebootTo = new JLabel("Reboot to :");
		lblRebootTo.setBounds(28, 12, 200, 15);
		panel_2.add(lblRebootTo);

		JLabel lblNotFor = new JLabel("# Not for Samsung devices");
		lblNotFor.setBounds(491, 359, 238, 19);
		panel_2.add(lblNotFor);

		JLabel lblDeviceMust_1 = new JLabel("Device must be in fastboot mode (Except for Reboot System)");
		lblDeviceMust_1.setBounds(10, 332, 479, 19);
		panel_2.add(lblDeviceMust_1);

		JLabel lblYouMust_1 = new JLabel("* You must have a bootloader that supports fastboot commands");
		lblYouMust_1.setBounds(10, 359, 470, 19);
		panel_2.add(lblYouMust_1);

		JButton btnRebootFromFastboot = new JButton("Fastboot *");
		btnRebootFromFastboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("fastboot reboot");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootFromFastboot.setBounds(28, 230, 200, 75);
		panel_2.add(btnRebootFromFastboot);

		JButton btnRebootToBootloaderFromFastboot = new JButton("Fastboot to Bootloader *");
		btnRebootToBootloaderFromFastboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("fasboot reboot-bootloader");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToBootloaderFromFastboot.setBounds(279, 230, 240, 75);
		panel_2.add(btnRebootToBootloaderFromFastboot);

		JButton btnRebootToRecovery = new JButton("Recovery");
		btnRebootToRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot recovery");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToRecovery.setBounds(785, 55, 200, 75);
		panel_2.add(btnRebootToRecovery);

		JButton btnRebootToFastboot = new JButton("Fastboot");
		btnRebootToFastboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot fastboot");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToFastboot.setBounds(529, 55, 200, 75);
		panel_2.add(btnRebootToFastboot);

		JButton btnRebootToBootloader = new JButton("Bootloader #");
		btnRebootToBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot bootloader");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToBootloader.setBounds(279, 55, 200, 75);
		panel_2.add(btnRebootToBootloader);

		JButton btnRebootSystem = new JButton("System");
		btnRebootSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootSystem.setBounds(28, 55, 200, 75);
		panel_2.add(btnRebootSystem);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		tabbedPane.addTab("Bootloader", null, panel_3, null);
		panel_3.setLayout(null);

		JLabel label_17 = new JLabel("Note: Don't worry if the app says to connect your device while");
		label_17.setBounds(23, 320, 518, 19);
		panel_3.add(label_17);

		JLabel label_18 = new JLabel("android is not booted ex. fastboot, bootloader, booting etc.");
		label_18.setBounds(66, 337, 475, 19);
		panel_3.add(label_18);

		JLabel lblOnlyForNexus = new JLabel(
				"Works only with specific devices ex. Nexus, Android One, few MTK devices etc.");
		lblOnlyForNexus.setBounds(25, 358, 660, 19);
		panel_3.add(lblOnlyForNexus);

		JButton btnUnlockBootloader = new JButton("Unlock Bootloader");
		btnUnlockBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText(
							"Unlocking bootloader will factory reset your device and may void your device warranty!");
					JOptionPane.showMessageDialog(null,
							"You will need to re-enable USB debugging later as your device will get factory reset");
					Process p1 = Runtime.getRuntime().exec("adb reboot bootloader");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("fastboot oem unlock");
					p2.waitFor();
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnUnlockBootloader.setBounds(425, 194, 230, 75);
		panel_3.add(btnUnlockBootloader);

		JButton btnLockBootloader = new JButton("Lock Bootloader");
		btnLockBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb reboot bootloader");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("fastboot oem lock");
					p2.waitFor();
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnLockBootloader.setBounds(425, 57, 230, 75);
		panel_3.add(btnLockBootloader);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		tabbedPane.addTab("Logger", null, panel_4, null);
		panel_4.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 72, 1072, 311);
		panel_4.add(scrollPane);

		final JTextArea LogViewer = new JTextArea();
		LogViewer.setEditable(false);
		scrollPane.setViewportView(LogViewer);

		JButton btnSaveAsTextFile = new JButton("Save as a text file");
		btnSaveAsTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
						LogViewer.write(write);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (write != null)
							try {
								write.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
					}
				}
			}
		});

		btnSaveAsTextFile.setBounds(420, 13, 200, 47);
		panel_4.add(btnSaveAsTextFile);

		JButton btnClearLogact = new JButton("Clear");
		btnClearLogact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LogViewer.setText("");
				AppStatus.setText("Logcat cleared");
			}
		});
		btnClearLogact.setBounds(845, 12, 200, 48);
		panel_4.add(btnClearLogact);

		JButton btnViewLogcat = new JButton("View Logcat");
		btnViewLogcat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Getting logcat...");
					Process p1 = Runtime.getRuntime().exec("adb logcat -d > /sdcard/.logcat.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.logcat.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb logcat -c");
					p3.waitFor();
					Process p4 = Runtime.getRuntime().exec("adb shell rm /sdcard/.logcat.txt");
					p4.waitFor();
					try {
						Reader reader = new FileReader(new File(".logcat.txt"));
						LogViewer.read(reader, "");
						AppStatus.setText("");
					} catch (Exception e) {
						e.printStackTrace();
					}
					File file = new File(".logcat.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});

		btnViewLogcat.setBounds(37, 13, 200, 47);
		panel_4.add(btnViewLogcat);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		tabbedPane.addTab("Backup and Restore", null, panel_5, null);
		panel_5.setLayout(null);

		JLabel lblNoteThisIs = new JLabel("Note: This is android native backup and restore utility");
		lblNoteThisIs.setBounds(270, 346, 420, 15);
		panel_5.add(lblNoteThisIs);

		BackupAndRestoreDone = new JLabel("");
		BackupAndRestoreDone.setText("");
		BackupAndRestoreDone.setBounds(758, 70, 300, 200);
		panel_5.add(BackupAndRestoreDone);

		JLabel lblRestoreOperations = new JLabel("Restore Operations");
		lblRestoreOperations.setBounds(510, 12, 142, 36);
		panel_5.add(lblRestoreOperations);

		final JButton btnRestoreFromCustomLocationBackup = new JButton("From Custom Location");
		btnRestoreFromCustomLocationBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Android Backup Files", "ab");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						AppStatus.setText("Restoring may take upto several minutes, please be patient...");
						JOptionPane.showMessageDialog(null,
								"Unlock your device and confirm the restore operation when asked");
						String[] commands = new String[3];
						commands[0] = "adb";
						commands[1] = "restore";
						commands[2] = file.getAbsolutePath();
						AppStatus.setText("Restoring...");
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText("Restore completed successfully!");
						BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnRestoreFromCustomLocationBackup.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnRestoreFromCustomLocationBackup.setBounds(510, 184, 200, 75);
		panel_5.add(btnRestoreFromCustomLocationBackup);

		JLabel lblBackup = new JLabel("Backup Operations");
		lblBackup.setBounds(25, 12, 142, 36);
		panel_5.add(lblBackup);

		final JButton btnBackupInternelStorage = new JButton("Internel Storage");
		btnBackupInternelStorage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-shared";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupInternelStorage.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupInternelStorage.setBounds(25, 301, 200, 75);
		panel_5.add(btnBackupInternelStorage);

		final JButton btnBackupSystem = new JButton("System");
		btnBackupSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-system";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupSystem.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupSystem.setBounds(25, 186, 200, 75);
		panel_5.add(btnBackupSystem);

		final JButton btnBackupSingleApp = new JButton("Single App");
		btnBackupSingleApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					String message = JOptionPane.showInputDialog(null, "Please specify a package name to backup");
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = message;
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupSingleApp.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupSingleApp.setBounds(270, 185, 200, 75);
		panel_5.add(btnBackupSingleApp);

		final JButton btnBackupAppAndAppData = new JButton("App and App Data");
		btnBackupAppAndAppData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-all";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupAppAndAppData.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupAppAndAppData.setBounds(270, 70, 200, 75);
		panel_5.add(btnBackupAppAndAppData);

		final JButton btnBackupWholeDevice = new JButton("Whole Device");
		btnBackupWholeDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[6];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-all";
					commands[3] = "-apk";
					commands[4] = "-shared";
					commands[5] = "-system";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupWholeDevice.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupWholeDevice.setBounds(25, 70, 200, 75);
		panel_5.add(btnBackupWholeDevice);

		final JButton btnRestorePreviousBackup = new JButton("Previous Backup");
		btnRestorePreviousBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Restoring can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the restore operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "restore";
					commands[2] = "backup.ab";
					AppStatus.setText("Restoring...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Restore completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnRestorePreviousBackup.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRestorePreviousBackup.setBounds(510, 70, 200, 75);
		panel_5.add(btnRestorePreviousBackup);

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.WHITE);
		tabbedPane.addTab("Bypass Security", null, panel_9, null);
		panel_9.setLayout(null);

		JLabel lblRootOperationsexperimental = new JLabel("Root Operations [EXPERIMENTAL] :");
		lblRootOperationsexperimental.setBounds(12, 12, 388, 15);
		panel_9.add(lblRootOperationsexperimental);

		JLabel lblMethodbetter = new JLabel("Method #1 (Recommended)");
		lblMethodbetter.setBounds(12, 42, 163, 15);
		panel_9.add(lblMethodbetter);

		JButton btnPattern = new JButton("Pattern #");
		btnPattern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Trying to break into security...");
					Process p1 = Runtime.getRuntime().exec("adb shell su -c rm /data/system/gesture.key");
					p1.waitFor();
					AppStatus.setText(
							"Done, now try to unlock the device with a random pattern then change security manually from settings");
				} catch (Exception e1) {
				}
			}
		});

		btnPattern.setBounds(200, 75, 240, 75);
		panel_9.add(btnPattern);

		JButton btnPasswordPin = new JButton("Password/ PIN #");
		btnPasswordPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Trying to break into security...");
					Process p1 = Runtime.getRuntime().exec("adb shell su -c rm /data/system/password.key");
					p1.waitFor();
					AppStatus.setText("Done, check your device...");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnPasswordPin.setBounds(630, 75, 240, 75);
		panel_9.add(btnPasswordPin);

		JLabel lblMayNot = new JLabel("# Works on Android 4.4.x and lower");
		lblMayNot.setBounds(630, 250, 269, 15);
		panel_9.add(lblMayNot);

		JLabel lblNonRoot = new JLabel("Non - Root/ Root Operations [EXPERIMENTAL] :");
		lblNonRoot.setBounds(12, 191, 388, 15);
		panel_9.add(lblNonRoot);

		JButton btnJellyBeanPatternPasswordPin = new JButton("Pattern/ Password/ PIN *");
		btnJellyBeanPatternPasswordPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Trying to break into security...");
					Process p1 = Runtime.getRuntime().exec(
							"adb shell am start -n com.android.settings/com.android.settings.ChooseLockGeneric --ez confirm_credentials false --ei lockscreen.password_type 0 --activity-clear-task");
					p1.waitFor();
					AppStatus.setText("Rebooting...");
					Process p2 = Runtime.getRuntime().exec("adb reboot");
					p2.waitFor();
					AppStatus.setText("Done, check your device...");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnJellyBeanPatternPasswordPin.setBounds(200, 250, 240, 75);
		panel_9.add(btnJellyBeanPatternPasswordPin);

		JLabel lblWorksWell = new JLabel("* Works well on Jelly Bean Devices but");
		lblWorksWell.setBounds(630, 273, 285, 15);
		panel_9.add(lblWorksWell);

		JLabel lblNewLabel = new JLabel("may work for older android versions");
		lblNewLabel.setBounds(640, 293, 285, 15);
		panel_9.add(lblNewLabel);

		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		tabbedPane.addTab("Cryptography", null, panel_6, null);
		panel_6.setLayout(null);

		JButton btnSHA512 = new JButton("SHA-512");
		btnSHA512.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha512Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		JButton btnClearCalculatedCrypto = new JButton("Clear");
		btnClearCalculatedCrypto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CalculatedCrypto.setText("");
			}
		});
		btnClearCalculatedCrypto.setBounds(625, 168, 200, 75);
		panel_6.add(btnClearCalculatedCrypto);

		JLabel lblLabelCalculatedSum = new JLabel("Calculated Sum :");
		lblLabelCalculatedSum.setBounds(12, 317, 264, 17);
		panel_6.add(lblLabelCalculatedSum);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(12, 346, 1046, 28);
		panel_6.add(scrollPane_1);

		CalculatedCrypto = new JTextArea();
		scrollPane_1.setViewportView(CalculatedCrypto);
		CalculatedCrypto.setEditable(false);
		btnSHA512.setBounds(625, 26, 200, 75);
		panel_6.add(btnSHA512);

		JButton btnSHA384 = new JButton("SHA-384");
		btnSHA384.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha384Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA384.setBounds(325, 168, 200, 75);
		panel_6.add(btnSHA384);

		JButton btnSHA256 = new JButton("SHA-256");
		btnSHA256.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha256Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA256.setBounds(325, 26, 200, 75);
		panel_6.add(btnSHA256);

		JButton btnSHA1 = new JButton("SHA-1");
		btnSHA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha1Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA1.setBounds(27, 168, 200, 75);
		panel_6.add(btnSHA1);

		JButton btnMD5 = new JButton("MD5");
		btnMD5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.md5Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnMD5.setBounds(27, 26, 200, 75);
		panel_6.add(btnMD5);

		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Interface_logo.png")));
		label_2.setBounds(50, 0, 1038, 256);
		contentPane.add(label_2);

		Thread t = new Thread(r); // Connection service
		t.start();
	}
}
