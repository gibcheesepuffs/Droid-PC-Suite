package dpcs;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ADBHelp extends JFrame {
	JTextArea LicenseViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADBHelp frame = new ADBHelp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ADBHelp() {
		setResizable(false);
		setTitle("ADB Help");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ADBHelp.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 755, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 747, 468);
		contentPane.add(scrollPane);

		LicenseViewer = new JTextArea();
		LicenseViewer.setEditable(false);
		scrollPane.setViewportView(LicenseViewer);
		try {
			URL licenseobj = ADBHelp.class.getResource("/others/adbhelp.txt");
			File licenseobj2 = new File(licenseobj.toURI());
			Reader reader = new FileReader(new File(licenseobj2.toURI()));
			LicenseViewer.read(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
