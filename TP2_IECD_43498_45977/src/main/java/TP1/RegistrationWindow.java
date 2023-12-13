package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * A classe RegistrationWindow é a janela de registo.
 */
public class RegistrationWindow {

    private JFrame frame;
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JComboBox<String> nationalityComboBox;
    private JTextField ageField;
    private JTextField imagePathTextField;
    private JLabel pictureLabel;
    private File selectedFile = null;
    UserXmlWriter userXmlWriter;
    String newRegister;
    boolean novoRegisto = false;
  
    public RegistrationWindow(UserXmlWriter userXmlWriter) {
    	this.userXmlWriter = userXmlWriter;
    	
        initialize();
    }

	private void initialize() {
		//criação dos fields e das labels
    	frame = new JFrame();
        frame.setTitle("Registration Window");
        frame.setBounds(100, 100, 450, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        JLabel lblRegistration = new JLabel("Registration");
        lblRegistration.setForeground(Color.BLUE);
        lblRegistration.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        lblRegistration.setBounds(167, 21, 117, 20);
        frame.getContentPane().add(lblRegistration);

        JLabel lblNickname = new JLabel("Nickname:");
        lblNickname.setBounds(38, 68, 61, 16);
        frame.getContentPane().add(lblNickname);

        nicknameField = new JTextField();
        nicknameField.setBounds(133, 63, 220, 26);
        frame.getContentPane().add(nicknameField);
        nicknameField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(38, 106, 61, 16);
        frame.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(133, 101, 220, 26);
        frame.getContentPane().add(passwordField);

        JLabel lblNationality = new JLabel("Nationality:");
        lblNationality.setBounds(38, 144, 75, 16);
        frame.getContentPane().add(lblNationality);

        nationalityComboBox = new JComboBox<String>(getAllCountries());
        nationalityComboBox.setBounds(133, 139, 220, 27);
        frame.getContentPane().add(nationalityComboBox);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(38, 182, 61, 16);
        frame.getContentPane().add(lblAge);

        ageField = new JTextField();
        ageField.setBounds(133, 177, 220, 26);
        frame.getContentPane().add(ageField);
        ageField.setColumns(10);

        JLabel lblPicture = new JLabel("Picture:");
        lblPicture.setBounds(38, 220, 61, 16);
        frame.getContentPane().add(lblPicture);
        
        imagePathTextField = new JTextField();
        imagePathTextField.setBounds(133, 250, 220, 26);
        frame.getContentPane().add(imagePathTextField);
        imagePathTextField.setColumns(10);
        imagePathTextField.disable();

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(133, 215, 117, 29);
        frame.getContentPane().add(btnBrowse);

        pictureLabel = new JLabel("");
        pictureLabel.setBounds(315, 63, 105, 105);
        frame.getContentPane().add(pictureLabel);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(167, 313, 117, 29);
        frame.getContentPane().add(btnRegister);
        
   

        // add action listener to the browse button
        btnBrowse.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
        	        JFileChooser fileChooser = new JFileChooser();
        	        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        	        FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGES", "jpeg", "gif", "png");
        	        fileChooser.addChoosableFileFilter(filter);
        	        int result = fileChooser.showSaveDialog(null);
        	        if (result == JFileChooser.APPROVE_OPTION) {
        	            File selectedFile = fileChooser.getSelectedFile();
        	            imagePathTextField.setText(selectedFile.getAbsolutePath());
        	        }
        	        else if (result == JFileChooser.CANCEL_OPTION) {
        	            System.out.println("No File selected");
        	        }
        	    }
        	});

        	// add action listener to the register button
        btnRegister.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
        	        String nickname = nicknameField.getText();
        	        String password = String.valueOf(passwordField.getPassword());
        	        String nationality = (String) nationalityComboBox.getSelectedItem();
        	        String age = ageField.getText();
        	        String imagePath = imagePathTextField.getText();
        	        
        	        User user = new User(nickname, password, nationality, age, imagePath,"0","0","0","#ffffff");
        	        					
					try {
						newRegister = userXmlWriter.writeUserToFile(user);
						novoRegisto = true;
						JOptionPane.showMessageDialog(frame, "Success!");
						dispose();
					} catch (ParserConfigurationException | SAXException | IOException e1) {
						JOptionPane.showMessageDialog(frame, "Couldn't regiser.");
						e1.printStackTrace();
					} catch (TransformerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        	    }
        	    
        	    	
        	});
        	
    }
    
    public String[] getAllCountries() {
        String[] countries = new String[Locale.getISOCountries().length];
        String[] countryCodes = Locale.getISOCountries();
        for (int i = 0; i < countryCodes.length; i++) {
            Locale obj = new Locale("", countryCodes[i]);
            countries[i] = obj.getDisplayCountry();
        }
        return countries;
     }
    
    private void dispose() {
    	frame.dispose();
    }
    
    public String getUpdatedXML() {
    	return newRegister;
    }
    
    public boolean novoRegisto() {
    	return novoRegisto;
    }
    
    public void setNovoRegisto() {
    	this.novoRegisto = false;
    }
}
