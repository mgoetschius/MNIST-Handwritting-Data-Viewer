/*
 * This class opens, reads, and pulls data from the
 * MNIST handwritten digital database.  The files and file
 * spec can be found at: http://yann.lecun.com/exdb/mnist/
 */
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileReader {
	
	private String labelFileName;
	private String imageFileName;
	// label file stuff
	private int labelDataOffset;
	private int labelMagicNumber;		//2049 * see spec at website above
	private int labelNumberOfItems;
	// image file stuff
	private int imageDataOffset;
	private int imageMagicNumber;		//2051  * see spec at website above
	private int numberOfImages;
	private int numberOfRows;
	private int numberOfColumns;

	public FileReader(String labelFileName, String imageFileName) {
		this.labelFileName = labelFileName;
		this.imageFileName = imageFileName;
		labelDataOffset = 8;
		imageDataOffset = 16;
		
		// get header info from 
		try {
			RandomAccessFile file = new RandomAccessFile(labelFileName, "r");
			file.seek(0);
			labelMagicNumber = file.readInt();
			labelNumberOfItems = file.readInt();
			file.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		//get image file header info
		try {
			RandomAccessFile file = new RandomAccessFile(imageFileName, "r");
			file.seek(0);
			imageMagicNumber = file.readInt();
			numberOfImages = file.readInt();
			numberOfRows = file.readInt();
			numberOfColumns = file.readInt();
			
			file.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// returns an int of the value of the image data at labelNum
	public int getLabel(int labelNum) {
		int label = -1;
		try {
			RandomAccessFile file = new RandomAccessFile(labelFileName, "r");
			file.seek((labelNum - 1) + labelDataOffset);
			byte b = file.readByte();
			label = b;
			file.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return label;
	}
	
	//returns an array of grayscale values (0 - 255) with top left at arr[0][0]
	public int[][] getImage(int imageNum) {
		int[][] arr = new int[numberOfColumns][numberOfRows];
		try {
			RandomAccessFile file = new RandomAccessFile(imageFileName, "r");
			file.seek(imageDataOffset + ( (imageNum - 1) * numberOfColumns * numberOfRows) );
			for(int y = 0; y < numberOfRows; y++) {
				for(int x = 0; x < numberOfColumns; x++) {
					int temp = file.readByte() & 0xFF;				// this is to account of unsigned byte not present in Java
					arr[y][x] = temp;
					//System.out.print(temp + "\t");
				}
				//System.out.println();
			}
			file.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	//returns a greyscale BufferedImage created from an array pulled from the database
	public BufferedImage getImage(int[][] values) {
		int[] data = new int[numberOfColumns * numberOfRows];
		int i = 0;
		for(int y = 0; y < numberOfRows; y++) {
			for(int x = 0; x < numberOfColumns; x++) {
				int red = values[y][x];
				int green = values[y][x];
				int blue = values[y][x];
				data[i++] = (red << 16) | (green << 8) | blue;
			}
		}
		BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, numberOfColumns, numberOfRows, data, 0, numberOfColumns);
		
		return image;
	}

}
