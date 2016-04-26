/**

Instructions:

	Use this file to generate a data set that randomly creates your 
	path for the fuzzy controlled robot to follow.
	
	Values will be between -100 and 100
	
	Usage: java PathGenerator <output-file> <number-of-ticks>
	

*/

import java.io.*;
import java.util.*;

public class PathGenerator{

	final static int SIZE = 200;
	final static int SEPERATION = 25;

	public static void main(String[] args){

		/* Because people do not follow instructions */
		if(args.length != 2){
			System.err.println("Usage: java PathGenerator <output-file> <number-of-ticks>");
			System.exit((int)(Math.pow(2, (int)(Math.random()*128))));
		}
		
		/* Members */
		BufferedWriter out = null;
		int leftBound, rightBound, currentPosition;
		int numTicks = 0;
		Random random;

		/* Open and read the user supplied file */
		try{ out = new BufferedWriter(new FileWriter(args[0])); } 
		catch (IOException ioe) { 
			System.err.println("Cannot open file: " + args[0]); 
			System.exit((int)(Math.pow(2, (int)(Math.random()*128))));
		}
	
		/* Parse the int from args, and jic, catch excpetion */
		try{ numTicks = Integer.parseInt(args[1]); }
		catch (NumberFormatException nfe) {
			System.err.println("This is not an integer: " + args[1]);
			System.exit((int)(Math.pow(2, (int)(Math.random()*128))));
		}

		/* Set up an initial start bound */
		random = new Random();

		leftBound = random.nextInt(SIZE) - SIZE/2;
		leftBound = (leftBound >= SIZE/2 - SEPERATION) ? SIZE/2 - SEPERATION : leftBound; 
		rightBound = random.nextInt(SIZE) - SIZE/2;
		rightBound = (rightBound - SEPERATION <= leftBound) ? leftBound + SEPERATION : rightBound;

		

		/* Now write current tick, then retick, then detick */
		for(int i = 0; i < numTicks; i++){


			/* Write to file */		
			try{ out.write(leftBound + " " + rightBound + "\n"); }
			catch (IOException ioe){
				System.err.println("Error writing to file");
				System.exit((int)(Math.pow(2, (int)(Math.random()*128))));
			}		
			
			/* Choose a new leftbound */
			leftBound = (random.nextInt(SIZE) % 2 == 0) ? 
				leftBound - (random.nextInt(SIZE) % 4) :
				leftBound + (random.nextInt(SIZE) % 4);

			/* Choose a new rightbound */
			rightBound = (random.nextInt() % 2 == 0) ?
				rightBound - (random.nextInt(SIZE) % 4) :
				rightBound + (random.nextInt(SIZE) % 4);

			/* Restrict those sizes */
			leftBound = (leftBound <= -SIZE/2) ? -SIZE/2 : (leftBound + SEPERATION >= rightBound) ? rightBound - SEPERATION : leftBound;
			rightBound = (rightBound >= SIZE/2) ? SIZE/2 : (rightBound - SEPERATION <= leftBound) ? leftBound + SEPERATION : rightBound;
		}

		/* Clean up and exit nicely */
		try{ out.close(); }
		catch (IOException ioe){
			System.err.println("Error closing file");
			System.exit((int)(Math.pow(2, (int)(Math.random()*128))));
		}
		System.exit(0/(int)(Math.pow(2, (int)(Math.random()*128))));	

	}

}


				

		

		

			
		
	
