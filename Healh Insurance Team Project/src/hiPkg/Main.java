package hiPkg;

import java.io.BufferedWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
* Main program to run the system within the console.
*
* @author  Gary Mc Monagle, Eoin Gralton, Declan McDaid, Emma Mc Namee, Fiona Bowdren
* @version 1.0
* @since   2016-08-01 
*/

public class Main {

	/** 
	 * @param user
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		int user = -1;
		Scanner keyboardIn = new Scanner(System.in);
		do{
			/*Added this in 09/09/16
			 * 
			 * 
			 */
			System.out.println("*******************************************");
			System.out.println("*");
			System.out.println("*"+ "WELCOME TO THE HEALTH INSURANCE CALCULATOR");
			System.out.println("*");
			System.out.println("*******************************************");

			System.out.println("1. New Policy\n2. Admin\n0. Exit");
			user = keyboardIn.nextInt();
			/**
			   * if else to create a menu system, uses email validation to not create the same policy twice
			   */
			if(user == 1){
				boolean uniqueEmail = false;
				String email = "";
				do{
					System.out.println("Please Input email");
					email = keyboardIn.next();
					if(DatabaseManager.isUniqueEmail(email))
						uniqueEmail = true;
					else
						System.out.println("Invalid Email");
				}while(!uniqueEmail);
				System.out.println("Please Enter Contact No");
				String contactNo = keyboardIn.next();
				System.out.println("Please Select Policy Type");
				/*Added this in 09/09/16
				 * 
				 * 
				 */
				/**
				 * @param sysout
				   * Policy Types
				   */
				System.out.println("Policy Types: GOLD-25% INCREASE PREMIUM CARE +PRIVATE ROOM");
				System.out.println("Policy Type: SILVER-10% INCREASE PRIVATE ROOM");
				System.out.println("Policy Type: Bronze-5% INCREASE BASIC POLICY");
				
				/**
				 * @param int policyChosen
				 */
				int policyChosen = 0; 
				ArrayList<PolicyType> policyTypes = DatabaseManager.readPolicyTypes();
				for(int i = 0; i< policyTypes.size(); i++){
					System.out.println(i+1 + ". "+policyTypes.get(i).getName());
				}
				policyChosen = keyboardIn.nextInt();
				/**
				   * @ param Client Array List
				   */
				ArrayList<Client> policyClients = new ArrayList<Client>();
				System.out.println("Please enter number of customer for policy");
				int clientsNeeded = keyboardIn.nextInt();
				for(int i = 0; i<clientsNeeded; i++){
					System.out.println("Please enter First Name");
					String fName = keyboardIn.next();
					System.out.println("Please enter Last Name");
					String lName = keyboardIn.next();
					System.out.println("Please enter Age");
					int age = keyboardIn.nextInt();
					/**
					   * @param Conditions Array List
					   */
					ArrayList<Condition> availCons = DatabaseManager.readConditions();
					ArrayList<Condition> custCons = new ArrayList<Condition>();
					int userConditions = -1;
					do{
						System.out.println("Please Select If Any Relate To You");
						for(int j = 0; j<availCons.size(); j++){
							System.out.println(j+1 + ". "+availCons.get(j).getName());
						}
						System.out.println("0. Next");
						userConditions = keyboardIn.nextInt();
						if(userConditions != 0){
							//-1 adjustment
							//userConditions--;
							custCons.add(availCons.get(userConditions-1));
							availCons.remove(userConditions-1);
						}			
					}while(userConditions != 0 || availCons.size() == 0);
					policyClients.add(new Client(fName, lName, age, custCons));
					
				}
				DatabaseManager.writePolicy(new Policy(policyClients, contactNo, email, policyTypes.get(policyChosen)));
			
				/**
				   * @ param writer
				   * Write to file section for backup purposes on machine
				   */
			Writer writer = null;
			/**
			 * try catch 
			 */
			try {
				{
					   Calendar cal = Calendar.getInstance();
					   DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL,
					        DateFormat.MEDIUM);

					    System.out.println(df.format(cal.getTime()));
			    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("policyTesterFile.txt"), "utf-8"));
			    writer.write("Conditions: " +policyClients+email+contactNo+policyChosen+df);
			}
			}
				catch (IOException ex) {
			  // report
			} finally {
			   try {writer.close();} catch (Exception ex) {/*ignore*/}
			}
			// Instantiate a Date object
			}
			//launch login
			/*
			 * Added 9/09/16
			 */
			/**
			 * @param 
			   * Call the login screen
			   */
			loginScreen.Login.main(args);
			/**
			   * Admin Section
			   */
			if(user == 2){
				int adminSelect = -1;
				do{
					System.out.println("1. View Policy. \n2. Edit Policy\n3. Delete Policy\n0. Exit");
					adminSelect = keyboardIn.nextInt();
					if(adminSelect == 1){
						System.out.println("Plase Enter Policy Number");
						int pNo = keyboardIn.nextInt();
						if(DatabaseManager.isPolicyNumber(pNo)){
							System.out.println(DatabaseManager.readPolicy(pNo));
						}
						else
							System.out.println("Policy Not Found");
					}
					//dialogue box popup of one to edit, create a policy and call method from dbm
					if(adminSelect == 2){
						System.out.println("Not Yet Implemented");
					}
					if (adminSelect == 3){
						System.out.println("Plase Enter Policy Number");
						int pNo = keyboardIn.nextInt();
						if(DatabaseManager.isPolicyNumber(pNo)){
							System.out.println(DatabaseManager.readPolicy(pNo));
							System.out.println("Are you sure you want to delete this policy ?(y/n)");
							if (keyboardIn.next().equals("y"))
								DatabaseManager.deletePolicy(pNo);
								
						}
						else
							System.out.println("Policy Not Found");
						
					}
				}while(adminSelect != 0);
						
			}
		}while(user != 0);
		keyboardIn.close();

	}
	/**
	   * Validate Input
	   */
	public static boolean validateInput(String input){
		boolean valid = true;
		for(int i = 0; i<input.length(); i++){
			if (!Character.isDigit(input.charAt(i)))
					valid = false;
		}
		return valid;
		
	}

}
