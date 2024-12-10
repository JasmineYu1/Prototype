//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

        //code to declare new instance & perform calculations on data
        //calculate B1 and B2: B1 is equal to the mean of presence count for user A for each day_hour time slot
        //B2 is equivalent to that of B1, except for the fact that it is mean of user B
        //each time a value of B1 or B2 is calculated for a given time slot, the value is first compared to the upperThreshold of user A / B
        //if B1 / B2 > upperThreshold, print "User A / B can do housework at xx:xx on [weekday]!" and B1 / B2 is going to passed on
        //for further comparison with the lowerThreshold of user A / B
        //if B1 / B2 < lowerThreshold, print "User A / B can use shared facilities at xx:xx on [weekday]!"
        //if B1 / B2 < upperThreshold in the first place, print nothing and continue to check for the next value of B1 / B2
    }
}