package edu.wm.cs.cs301.shapes;
/**
 * Represents a simple Rectangle object
 * Stores length and width as doubles
 * 
 * Default constructor initializes Rectangle to 
 * length = 1.0
 * width = 1.0
 * 
 * throws IllegalArgumentException if Rectangle does not
 * exist in Euclidean space
 * 
 * @param l the length of the Rectangle
 * @param w the width of the Rectangle
 * 
 * @author kevincoogan
 * 
 */
public class Rectangle {
	private double length;
	private double width;
	
	public Rectangle(double l, double w) {
		if (l <= 0.0 || w <= 0.0) {
			throw new IllegalArgumentException("Cannot have 0 or negative length or width");
		}
		// set the length and width
		this.length = l;
		this.width = w;
	}
	
	public Rectangle() {
		// if not specified, set length and width to 1.0
		this(1.0, 1.0);
	}
	
	/**
	 * 
	 * @return the area of the Rectangle object
	 */
	public double area() {
		// area of rectangle = length * width
		
		// use negative value for stub until implemented
		return this.length * this.width;
	}
	/**
	 * 
	 * @return the perimeter of the Rectangle object
	 */
	public double perimeter() {
		// perimeter of rectangle = 2 * (length + width)

		// use negative value for stub until implemented
		return 2.0 * (this.length + this.width);
	}
	
	/**
	 * 
	 * @param newLength new length of Rectangle as positive double
	 * @param newWidth new width of the Rectangle as positive double
	 */
	public void setSize(double newLength, double newWidth) {
		// store the new size in the instance parameters
		if (newLength <= 0.0 || newWidth <= 0.0) {
			throw new IllegalArgumentException("Cannot have 0 or negative length or width");
		}
		// set the length and width
		this.length = newLength;
		this.width = newWidth;
	}
}
