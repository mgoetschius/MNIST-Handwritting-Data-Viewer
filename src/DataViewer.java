/*
 * A simple GUI to display the training data
 * Images can be saved if need be.  Gets the
 * data through the FileReader class
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DataViewer extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private FileReader reader;
	
	public class DisplayPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		
		private JButton previousButton, nextButton, jumpButton, saveButton;
		private JTextField numberField;
		private JLabel imageDataLabel, imageLabel;
		
		private int currentImageNumber;
		private int[][] img;
		private BufferedImage image;
		
		public DisplayPanel() {
			currentImageNumber = 1;
			
			previousButton = new JButton("Previous");
				previousButton.addActionListener(this);
			nextButton = new JButton("Next");
				nextButton.addActionListener(this);
			jumpButton = new JButton("Jump");
				jumpButton.addActionListener(this);
			numberField = new JTextField();
				numberField.setColumns(6);
				numberField.setText(Integer.toString(currentImageNumber));
			//get image for imageLabel
			img = reader.getImage(currentImageNumber);
			image = reader.getImage(img);
			imageLabel = new JLabel();
				imageLabel.setIcon(new ImageIcon(image));
			imageDataLabel = new JLabel(Integer.toString(reader.getLabel(currentImageNumber)));
			saveButton = new JButton("Save");
				saveButton.addActionListener(this);
			
			this.add(previousButton);
			this.add(numberField);
			this.add(jumpButton);
			this.add(nextButton);
			this.add(imageDataLabel);
			this.add(imageLabel);
			this.add(saveButton);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == previousButton) {
				if(currentImageNumber > 1)
					currentImageNumber--;
			}
			else if(e.getSource() == nextButton) {
				if(currentImageNumber < 60000)
					currentImageNumber++;
			}
			else if(e.getSource() == jumpButton) {
				int temp = Integer.parseInt(numberField.getText());
				if(temp > 0 && temp <= 60000) 
					currentImageNumber = temp;
			}
			else if(e.getSource() == saveButton) {
				try {
					String filename = "./data/image" + currentImageNumber + ".png";
					ImageIO.write(image, "PNG", new FileImageOutputStream(new File(filename)));
					JOptionPane.showMessageDialog(this, "Saved as: " + filename, "Success", JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException f) {
					// TODO Auto-generated catch block
					f.printStackTrace();
				} catch (IOException g) {
					// TODO Auto-generated catch block
					g.printStackTrace();
				}
			}
			
			numberField.setText(Integer.toString(currentImageNumber));
			imageDataLabel.setText(Integer.toString(reader.getLabel(currentImageNumber)));
			img = reader.getImage(currentImageNumber);
			image = reader.getImage(img);
			imageLabel.setIcon(new ImageIcon(image));
		}
	}
	
	public DataViewer() {
		
		String imageFilename ="./data/train-images.idx3-ubyte";
		String labelFilename = "./data/train-labels.idx1-ubyte";
		
		reader = new FileReader(labelFilename, imageFilename);
		
		this.setTitle("MNIST Viewer");
		this.setSize(500, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new DisplayPanel();
		this.add(panel);
		
	    this.setVisible(true);
	}

}
