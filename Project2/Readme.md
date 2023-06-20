<h1>Instructions to run the program.</h1>

1. Download the code from this repository.
2. Navigate to the src folder.
3. In the terminal, run "javac *.java"
4. In the terminal, run "java VotingSystem"
5. You will be prompted with "Enter filename" - Enter the filename you wish to be read in. The file must be a CSV file.
6. Pass in the file and the program will output the winner depending on the style of voting the file has inherently.


<h1>Assumptions</h1>
We assume as stated that the inputted CSV file will always be in the correct format. The format as shown in the SRS documentation.

To look at the documention properly, open the file(VotingSystem.html,Ballot.html...) in a file explorer. Then you can right click and open the file in the browser and see the documentation properly.

Along with that, we have the assumption that there will be atleast one candidate and atleast vote/ballot that is being cast. 

Finally, our last assumption is that the machine that is running these programs has Java 18.0.2 atleast and has a Junit test runner. 