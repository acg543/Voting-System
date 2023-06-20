import java.io.*;
import java.util.*;
import java.time.LocalDate;

/**
 * VotingSystem.java
 * 
 * <p>This file implements the voting system for the IR and CPL forms of voting.</p>
 * 
 * @author Jackie Li, Dominic Deiman, Aahan Tyagi, Alex Grenier
 */

public class VotingSystem {
    private int numBallots;
    private Vector<Ballot> ballots;

    private int numCandidates;
    private Vector<Candidate> candidates;

    private VotingType calculator;
    private int numSeats;
    private String type;

    /**
     * 
     * @return numBallots to get
     */
    public int getNumBallots() {
        return this.numBallots;
    }

    /**
     * 
     * @param numBallots to set
     */
    public void setNumBallots(int numBallots) {
        this.numBallots = numBallots;
    }

    /**
     * 
     * @return ballots to get
     */
    public Vector<Ballot> getBallots(){
        return this.ballots;
    }

    /**
     * 
     * @param ballots to set
     */
    public void setBallots(Vector<Ballot> ballots) {
        this.ballots = ballots;
    }

    /**
     * 
     * @return numCandidates to get
     */
    public int getNumCandidates() {
        return this.numCandidates;
    }

    /**
     * 
     * @param numCandidates to set
     */
    public void setNumCandidates(int numCandidates) {
        this.numCandidates = numCandidates;
    }

    /**
     * 
     * @return candidates to get
     */
    public Vector<Candidate> getCandidates(){
        return this.candidates;
    }

    /**
     * 
     * @param candidates to set
     */
    public void setCandidates(Vector<Candidate> candidates) {
        this.candidates = candidates;
    }

    /**
     * 
     * @return calculator to get
     */
    public VotingType getCalculator() {
        return this.calculator;
    }

    /**
     * Sets calcuator to v
     * 
     * @param v
     */
    public void setCalculator(VotingType v){
        this.calculator = v;
    }
    
    /**
     * 
     * @return numSeats to get
     */
    public int getNumSeats() {
        return this.numSeats;
    }

    /**
     * 
     * @param numSeats to set
     */
    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    /**
     * 
     * @return type to get
     */
    public String getType() {
        return this.type;
    }

    /**
     * 
     * @param type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Empty VotingSystem constructor
     */
    public VotingSystem() {
        numBallots = 0;
        this.ballots = new Vector<Ballot>();
    }



    /**
     * runCalculator calls the calculateResults function from the calculator.
     * 
     * @return calculateResults
     */
    public String runCalculator(){
        return this.calculator.calculateResults(ballots, candidates);
    }

    public static void listFiles(File directory) {
        // Get all files in the directory
        File[] files = directory.listFiles();
        // Print the names of all
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".csv")) {
                System.out.println(file.getName());
            }
        }
    }
    
    /**
     * openFile opens file to be written.
     * 
     * @param filename
     * @return scanIRfile, scanCPLfile, or false
     * @throws FileNotFoundException
     */
    public boolean openFile(String filename) throws FileNotFoundException {
        // File file = new File(filename);
        // Scanner scanner = new Scanner(file);
        Scanner scanner = new Scanner(new File(filename));
        
        // Get the voting type and scan the file depending on the type.
        // while (scanner.hasNextLine()){v
        //     System.out.println(scanner.nextLine());
        // }
        if (scanner.hasNextLine()){
            // Check if type has been previously set (from entering multiple files).
            // Make sure the types match
            if(type == null){
                type = scanner.nextLine();
            } else {
                String newType = scanner.nextLine();
                if (!newType.equals(type)){
                    System.out.println("ERROR: Previous entered files were type " +  type + ", but " + filename + " has type " + newType);
                    return false;
                }
            }

            // System.out.println(type);
            if(type.equals("IR")){
                // debug
                // System.out.println("IR in");
                return scanIRfile(scanner);

            } else if(type.equals("CPL")){
                return scanCPLfile(scanner);

            } else if(type.equals("PO")){
                return scanPOfile(scanner);

            } else {
                System.out.println("ERROR: Invalid type \"" + type + "\" on first line of \"" + filename + "\"\nFirst line must be IR or CPL.");
                scanner.close();
                return false;
            }
        }
        else{
            scanner.close();
            return false;
        }
    }

    /**
     * scanIRfile scans in IR file if selected.
     * 
     * @param scanner
     * @return true or false
     */
    private boolean scanIRfile(Scanner scanner){

        // Get the number of candidates from the file
        numCandidates = Integer.parseInt(scanner.nextLine());

        // Read the comma-separated candidate names from the file, and create candidate objects for each of them.
        String[] enteredCandidates = scanner.nextLine().split(", ");
        this.candidates = new Vector<Candidate>();
        for(int i = 0; i < enteredCandidates.length; i++){
            candidates.add(new Candidate(enteredCandidates[i].strip()));
        }
        // Alert the user if the number of candidates is incorrect
        if(numCandidates != candidates.size()){
            System.out.println("ERROR: The expected number of candidates from line 2 of the input file is " + numCandidates + ", but " + candidates.size() + " names were found on line 3 of the file.");
            scanner.close();
            return false;
        }

        // Get the number of ballots from the file
        numBallots += Integer.parseInt(scanner.nextLine());

        // Read all the ballots from the rest of the file
        while (scanner.hasNextLine()) {
            String b = scanner.nextLine();
            ballots.add(new Ballot(b, numCandidates));
        }
        // Alert the user if the number of ballots is incorrect
        if(numBallots != ballots.size()){
            System.out.println("ERROR: The expected number of total ballots after scanning this input file is " + numBallots + ", but " + ballots.size() + " ballots were read from the files.");
            scanner.close();
            return false;
        }
        
        scanner.close();
        return true;
    }
    
    /**
     * scanCPLfile scans in CPL file if selected.
     * 
     * @param scanner
     * @return true or false
     */
    private boolean scanCPLfile(Scanner scanner){

        // Get the number of candidates from the file
        numCandidates = Integer.parseInt(scanner.nextLine());

        // Read the comma-separated party names from the file, and create candidate objects for each of them.
        String[] enteredCandidates = scanner.nextLine().split(", ");
        this.candidates = new Vector<Candidate>();
        for(int i = 0; i < enteredCandidates.length; i++){
            candidates.add(new Candidate(enteredCandidates[i].strip()));
        }
        // Alert the user if the number of parties is incorrect
        if(numCandidates != candidates.size()){
            System.out.println("ERROR: The expected number of parties from line 2 of the input file is " + numCandidates + ", but " + candidates.size() + " names were found on line 3 of the file.");
            scanner.close();
            return false;
        }

        // Get the number of seats and ballots from the file
        numSeats = Integer.parseInt(scanner.nextLine());
        numBallots += Integer.parseInt(scanner.nextLine());

        // Read all the ballots from the rest of the file
        while (scanner.hasNextLine()) {
            String b = scanner.nextLine();
            ballots.add(new Ballot(b, numCandidates));
        }
        // Alert the user if the number of ballots is incorrect
        if(numBallots != ballots.size()){
            System.out.println("ERROR: The expected number of total ballots after scanning this input file is " + numBallots + ", but " + ballots.size() + " ballots were read from the files.");
            scanner.close();
            return false;
        }
        
        scanner.close();
        return true;
    }

     /**
     * scanPOfile scans in PO file if selected.
     * 
     * @param scanner
     * @return true or false
     */
    private boolean scanPOfile(Scanner scanner){

        // Get the number of candidates from the file
        numCandidates = Integer.parseInt(scanner.nextLine());

        // Read the comma-separated party names from the file, and create candidate objects for each of them.
        String tempCandidates = scanner.nextLine();
        tempCandidates = tempCandidates.substring(1, tempCandidates.length()-1);
        String[] enteredCandidates = tempCandidates.split("\\], \\[");
        this.candidates = new Vector<Candidate>();
        for(int i = 0; i < enteredCandidates.length; i++){
            candidates.add(new Candidate(enteredCandidates[i].strip()));
        }
        // Alert the user if the number of parties is incorrect
        if(numCandidates != candidates.size()){
            System.out.println("ERROR: The expected number of parties from line 2 of the input file is " + numCandidates + ", but " + candidates.size() + " names were found on line 3 of the file.");
            scanner.close();
            return false;
        }

        // Get the number of seats and ballots from the file
        numBallots += Integer.parseInt(scanner.nextLine());

        // Read all the ballots from the rest of the file
        while (scanner.hasNextLine()) {
            String b = scanner.nextLine();
            ballots.add(new Ballot(b, numCandidates));
        }
        // Alert the user if the number of ballots is incorrect
        if(numBallots != ballots.size()){
            System.out.println("ERROR: The expected number of total ballots after scanning this input file is " + numBallots + ", but " + ballots.size() + " ballots were read from the files.");
            scanner.close();
            return false;
        }
        
        scanner.close();
        return true;
    }

    /**
     * main is used as drive code.
     * 
     * @param args
     */
    public static void main(String[] args) {
        VotingSystem votingSystem = new VotingSystem();
        Scanner scanner = new Scanner(System.in);
        String filename = "";

        // Code that displays files
        File currentDirectory = new File(".");
        listFiles(currentDirectory);

        // Check if the file name was passed in as a command line argument
        if (args.length > 0){
            filename = args[0];
        }

        // Prompt user for filename until they enter a valid name.
        // Any errors in scanning the file will alert the user of the error, and prompt for another file name.
        while (true) {
            // Prompt the user for a file name if needed.
            if(filename.equals("")){
                System.out.println("Enter filename:");
                filename = scanner.nextLine();
            }
            
            // Try to open the file, and catch any errors that occur in opening or reading file.
            try {
                if(votingSystem.openFile(filename)){
                    // openFile returns true if file is opened and scanned successfully.
                    // Prompt if they want another file name
                    System.out.println("Total ballots scanned: " + votingSystem.ballots.size());
                    System.out.println("Would you like to enter another file? (yes/no):");

                    String moreFiles = scanner.nextLine();
                    if(moreFiles.equals("yes") || moreFiles.equals("y")){
                        // If they want another file, set filename blank
                        // This causes the while loop to repeat, and prompt the user for a new file name.
                        filename = "";
                    } else {
                        // No more files, break from the loop
                        System.out.println("No more files will be entered");
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File \"" + filename + "\" not found.");
            } catch (NoSuchElementException e) {
                System.out.println("Failed to read lines from file. File header may be missing information.");
            }

            // This happens if there was an error in opening or scanning the file.
            // Set the filename to empty string so we can prompt the user for a different filename.
            filename = "";
        }

        scanner.close();


        // Set the audit file name to be the voting type and todays date.
        LocalDate today = LocalDate.now();
        String auditFileName = votingSystem.type + "_" + today + ".txt";

        System.out.println("Calculating results");

        // Set up the calculator depending on election type
        if(votingSystem.type.equals("IR")){
            votingSystem.setCalculator(new IR(auditFileName, votingSystem));

        } else if(votingSystem.type.equals("CPL")){
            votingSystem.setCalculator(new CPL(auditFileName, votingSystem, votingSystem.numSeats));

        }else if(votingSystem.type.equals("PO")){
            votingSystem.setCalculator(new PO(auditFileName, votingSystem, votingSystem.numSeats));

        }

        // Run the election calculator
        String winner = votingSystem.runCalculator();

        System.out.println("The winner is: " + winner);

    }
}
