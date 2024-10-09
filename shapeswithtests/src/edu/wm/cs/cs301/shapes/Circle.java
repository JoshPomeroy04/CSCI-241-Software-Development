package edu.wm.cs.cs301.shapes;
/**
 * Represents a simple Circle object
 * Stores radius as double
 * 
 * Default constructor initializes Circle to 
 * radius = 1.0
 * 
 * throws IllegalArgumentException if Circle does not
 * exist in Euclidean space
 * 
 * @param r the radius of the Circle
 * 
 * @author kevincoogan
 * 
 */
public class Circle {
	private double radius;
	
	public Circle(double r) {
		if (r <= 0.0) {
			throw new IllegalArgumentException("Cannot have 0 or negative radius");
		}
		this.radius = r;
	}
	
	public Circle() {
		// set the radius to default value 1.0
		this(1.0);
	}
	
	/**
	 * area
	 * @return the area of the Circle object
	 */
	public double area() {
		// area of circle = pi r^2

		// use negative value for stub until implemented
		return Math.PI * this.radius * this.radius;
	}
	/**
	 * perimeter
	 * @return the perimeter (circumference) of the Circle object
	 */
	public double perimeter() {
		// perimeter of circle = 2 * pi * r

		// use negative value for stub until implemented
		return 2.0 * Math.PI * this.radius;
	}
	
	/**
	 * setSize
	 * @param newRadius new radius of Circle as positive double
	 */
	public void setSize(double newRadius) {
		// if invalid circle, throw exception
		if (newRadius <= 0.0) {
			throw new IllegalArgumentException("Cannot have 0 or negative radius");
		}
		// store the new size in the instance parameter
		this.radius = newRadius;
	}
}
