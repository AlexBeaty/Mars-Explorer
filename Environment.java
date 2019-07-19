import java.awt.image.*;
import java.awt.*;


public class Environment {
	
	
	//Environment parameters.
	private int width = 30;
	private int height = 30;
	private int[][] data = new int[height][width];
	
	
	//getWidth - returns width of environment.
	public int getWidth() {
		return width;
	}
	
	
	//getHeight - returns height of environment.
	public int getHeight() {
		return height;
	}
	
	
	//setDataValue - receives an x,y,value and sets element at [y][x] in data to value. [y][x] because Java in annoying.
	public void setDataValue(int x, int y, int value) {
		data[y][x] = value;
	}
	
	
	//getDataValue - returns value in data at [y][x].
	public int getDataValue(int x, int y) {
		return data[y][x];
	}
	
	
	//getDataAsImage - converts 2D int array 'data' to image object. calls get2DTo1D.
	public Image getDataAsImage() {
		if (data == null) {
			return null;
		}
		int[] data1D = get2DTo1D();
		int[] pixels = new int [data1D.length];
		for (int i = 0; i < pixels.length; i++) {
			int value = (int) data1D[i];
			switch (value) {
				//empty surface
				case 1:
					//orange
					Color ora = new Color(255,178,102);
					pixels[i] = ora.getRGB();
					break;
				//obstacle	
				case 2:
					//dark gray
					Color dar = new Color(64,64,64);
					pixels[i] = dar.getRGB();
					break;
				//rock sample	
				case 3:
					//blue
					Color blu = new Color(0,0,255);
					pixels[i] = blu.getRGB();
					break;
				//mothership	
				case 4:
					//red
					Color red = new Color(255,0,0);
					pixels[i] = red.getRGB();
					break;
				default:
					//black
					Color bla = new Color(0,0,0);
					pixels[i] = bla.getRGB();
			}
		}
		MemoryImageSource m = new MemoryImageSource(data[0].length, data.length, pixels, 0, data[0].length); 
		Panel panel = new Panel();
		Image image = panel.createImage(m);
		return image;	
	}
	
	
	//get2DTo1D - converts received 2D int array into 1D int array. called from getDataAsImage.
	int[] get2DTo1D() {
		int tempArray[] = new int[data.length * data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				tempArray[(i * data[0].length) + j] = data[i][j];
			}
		}
		return tempArray;	
	}
	
	
}