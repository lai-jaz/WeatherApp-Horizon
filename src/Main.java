// Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       /* Scanner scanner = new Scanner(System.in);
        
        System.out.println("Choose a combination:");
        System.out.println("1. Terminal UI with SQL Database");
        System.out.println("2. GUI with SQL Database");
        System.out.println("3. Terminal UI with Text Database");
        System.out.println("4. GUI with Text Database");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                runTerminalUIWithSQL();
                break;
            case 2:
                runGUIWithSQL();
                break;
            case 3:
                runTerminalUIWithText();
                break;
            case 4:
                runGUIWithText();
                break;
            default:
                System.out.println("Invalid choice!");
        }
        
        scanner.close();
    }

    private static void runTerminalUIWithSQL() {
        // Set the classpath including terminalUI.jar and SQLdb.jar
        String classpath = ".;libs/terminalUI.jar;libs/SQLdb.jar";
        runApplication(classpath);
    }

    private static void runGUIWithSQL() {
        // Set the classpath including GUI.jar and SQLdb.jar
        String classpath = ".;libs/GUI.jar;libs/SQLdb.jar";
        runApplication(classpath);
    }

    private static void runTerminalUIWithText() {
        // Set the classpath including terminalUI.jar and TEXTdb.jar
        String classpath = ".;./terminalUI.jar;./TEXTdb.jar";
        runApplication(classpath);
    }

    private static void runGUIWithText() {
        // Set the classpath including GUI.jar and TEXTdb.jar
        String classpath = ".;libs/GUI.jar;libs/TEXTdb.jar";
        runApplication(classpath);
    }

    private static void runApplication(String classpath) {
        try {
            // Run the application with the specified classpath
            Process process = Runtime.getRuntime().exec("java -cp " + classpath + " Main");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */ 
    
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Choose a combination:");
            System.out.println("1. Console UI with SQL Database");
            System.out.println("2. GUI with SQL Database");
            System.out.println("3. Console UI with Text Database");
            System.out.println("4. GUI with Text Database");
            System.out.print("Enter your choice: ");
    
            int choice = scanner.nextInt();
    
            switch (choice) {
                case 1: //----------------------CONSOLE + SQL DB
                    try{
                        DBManagerInterface db = new DBWeatherInstance();
                        new startAppConsole(db);
                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2: //----------------------GUI + SQL DB
                    try{
                        DBManagerInterface db = new DBWeatherInstance();
                        new startAppGUI(db);
                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3: //----------------------CONSOLE + TEXT DB
                    try{
                        DBManagerInterface db = new DB2WeatherInstance();
                        new startAppConsole(db);
                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4: //----------------------GUI + TEXT DB
                    try{
                        DBManagerInterface db = new DB2WeatherInstance();
                        new startAppGUI(db);
                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            
            scanner.close();
    

    }
}
