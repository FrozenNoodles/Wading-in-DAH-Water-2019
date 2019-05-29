package converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum ConversionType {
	DECIMAL;
}

// TODO: have user input file name/location
// file overwriting and creation

// input should be North/South and then East/West
public class Converter {
		
	public static void main(String[] args) {
		Scanner scanReader = new Scanner(System.in);
		int choice;
		File outputLong = new File("longitude.txt");
		File outputLat = new File("latitude.txt");
		
		System.out.println("Converts from degrees. Pick your conversion: ");
		System.out.println("0 - Decimal Degrees");
		try {
			choice = scanReader.nextInt();
			if (choice < 0 || choice > 0) {
				System.out.println("It seems you did not input a valid number choice."
						+ " The program will now terminate.");
				return;
			}
		} catch(InputMismatchException e) {
			System.out.println("It seems you did not input a valid number choice."
					+ " The program will now terminate.");
			return;
		}
				
		try {
			BufferedReader reader =  new BufferedReader(new FileReader("input.txt"));
			BufferedWriter longWriter = new BufferedWriter(new FileWriter(outputLong));
			BufferedWriter latWriter = new BufferedWriter(new FileWriter(outputLat));
			
			if (choice == 0) {
				decimalDegreeConversion(reader, longWriter, latWriter);
			}
			
			reader.close();
			latWriter.close();
			longWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void decimalDegreeConversion(BufferedReader reader, BufferedWriter longWriter,
			BufferedWriter latWriter) {
		String line;
		try {
			line = reader.readLine();
			while (line != null) {
				line.trim();
				
				// Pre-formatted degree pattern
				String degreePattern = "^(\\d{1,2}\\s\\d{1,2}\'\\d{1,2}\"[N,S,E,W,n,s,e,w])"
						+ "\\s(\\d{1,2}\\s\\d{1,2}\\'\\d{1,2}\"[N,S,E,W,n,s,e,w])$";
				Pattern degreePatternR = Pattern.compile(degreePattern);
				
				// Regular decimal pattern
				String decimalPattern = "^(\\d{1,2}\\.*\\d*[N,S,E,W,n,s,e,w])" 
						+ "\\s(\\d{1,2}\\.*\\d*[N,S,E,W,n,s,e,w])$";
				Pattern decimalPatternR = Pattern.compile(decimalPattern);
				
				// Matcher object
				Matcher mDegree = degreePatternR.matcher(line);
				Matcher mDecimal = decimalPatternR.matcher(line);
				
				if (mDegree.find()) {
					String latitude = mDegree.group(1);
					String longitude = mDegree.group(2);
					String conversionPattern = "^(\\d{1,2})\\s(\\d{1,2})"
							+ "\'(\\d{1,2})\"([N,S,E,W,n,s,e,w])$";
					Pattern conversionPatternR = Pattern.compile(conversionPattern);
					
					Matcher mLat = conversionPatternR.matcher(latitude);
					Matcher mLong = conversionPatternR.matcher(longitude);
					
					DecimalFormat df = new DecimalFormat("#.######");
					
					if (mLat.find()) {
						double group1 = (double) Integer.parseInt(mLat.group(1));
						double group2 = (double) Integer.parseInt(mLat.group(2));
						double group3 = (double) Integer.parseInt(mLat.group(3));
						double lat = (double) group1 + group2/60 + group3/3600;
						if (mLat.group(4).equals("S")) {
							lat = lat * -1;
						}

						latWriter.append(df.format(lat));
						latWriter.newLine();
					}
					if (mLong.find()) {
						double group1 = (double) Integer.parseInt(mLong.group(1));
						double group2 = (double) Integer.parseInt(mLong.group(2));
						double group3 = (double) Integer.parseInt(mLong.group(3));
						double longi = (double) group1 + group2/60 + group3/3600;
						if (mLong.group(4).equals("W")) {
							longi = longi * -1;
						}

						longWriter.append(df.format(longi));
						longWriter.newLine();
					}
				}
				else if (mDecimal.find()) {
					String latitude = mDecimal.group(1);
					String longitude = mDecimal.group(2);
					String conversionPattern = "^(\\d{1,2}\\.*\\d*)([N,S,E,W,n,s,e,w])$"; 
					Pattern conversionPatternR = Pattern.compile(conversionPattern);
					
					Matcher mLat = conversionPatternR.matcher(latitude);
					Matcher mLong = conversionPatternR.matcher(longitude);
					
					if (mLat.find()) {
						Double lat = Double.parseDouble(mLat.group(1));
						if (mLat.group(2).equals("S")) {
							lat = lat * -1;
						}
						latWriter.append(lat.toString());
						latWriter.newLine();
					}
					if (mLong.find()) {
						Double longi = Double.parseDouble(mLong.group(1));
						if (mLong.group(2).equals("W")) {
							longi = longi * -1;
						}
						longWriter.append(longi.toString());
						longWriter.newLine();
					}
				}
				else {
					latWriter.newLine(); 
					longWriter.newLine();
				}
				
				line = reader.readLine();
			}
						
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
}

/*
Decimal Degrees = degrees + (minutes/60) + (seconds/3600)

DD = d + (min/60) + (sec/3600)

round to 5 decimal places
*/
