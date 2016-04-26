import java.io.FileInputStream;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Rule;

public class Robot {
	
	private FIS fFile;
	private FileInputStream path;
	private int correct = 0, fail = 0;
	
	public Robot(String fisFile, String pathFile){
		loadFIS(fisFile);
		loadPath(pathFile);
	}
	
	private void loadFIS(String file){
	
		fFile = FIS.load(file, true);
		
		if (fFile == null) { // Error while loading?
			System.err.println("Cannot find file: \'" + file + "\'");
			return;
		}
	}
	
	private void loadPath(String pathFile){
		try{
			path = new FileInputStream(pathFile);
		}catch(Exception e){
			System.err.println("Cannot find file: \'" + pathFile + "\'");
			System.err.println();
			e.printStackTrace();
		}
	}
	
	private void printError(double l, double r, double bot){
		System.out.println("\n*************ERROR:**************");
		System.out.print(l + " ");
		System.out.print(bot + " ");
		System.out.println(r);
		System.out.println("Rule trigger values: ");
		for (Rule rule : fFile.getFunctionBlock("wall")
				.getFuzzyRuleBlock("No1").getRules())
			System.out.println(rule);
		System.out.println("********************************\n");
	}
	
	private void printSuccess(){
		System.out.println("\n----------------------------\n");
		System.out.println("Final Program Success Rate");
		System.out.println("Success: " + correct);
		System.out.println("Failure: " + fail);
	}
	
	public void run(){
		double left = 0; // left wall
		double right = 0; // right wall
		// debug
		FunctionBlock functionBlock = fFile.getFunctionBlock(null);

		double robot = 0; // out robot's position (x)
	
		try {		
			@SuppressWarnings("resource")
			Scanner pathVals = new Scanner(path);
			boolean flag = true;
			
			while (pathVals.hasNext()) {
				
				if (pathVals.hasNextInt()) {
					left = pathVals.nextInt();
				}
				if (pathVals.hasNextInt()) {
					right = pathVals.nextInt();
				}
				
				if(flag){
					robot = ThreadLocalRandom.current().nextDouble(left, right);
					System.out.println("Initial Robot Heading: " + robot);
					System.out.println("---------------------------------------------");
					flag = false;
				}
				System.out.println(left + "  " + robot + "  " + right);
				
				double dLeft = Math.abs(robot - left);
				double dRight = Math.abs(robot - right);

				functionBlock.setVariable("left", dLeft); 
				functionBlock.setVariable("right", dRight); 
				functionBlock.evaluate(); // Evaluate fuzzy system with inputs 1
											// and 2.

				double heading = functionBlock.getVariable("heading").getValue();
				double conversion = Math.toRadians(heading);
				robot = robot + Math.cos(conversion);


				if (robot > left && robot < right){
					correct++;
				}else {
					printError(left, right, robot);
					fail++;
				}

			}

			path.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		printSuccess();
	}
	
	public static void main(String[] args) {
		String fileName = "src/fuzzyControl.fcl";
		String pathFile = "src/PG4";
		
		Robot myBot = new Robot(fileName, pathFile);
		myBot.run();
		
		
	}
}