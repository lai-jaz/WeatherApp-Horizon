import java.sql.SQLException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JavafxUI extends Application {

    private DBManagerInterface dbHandler;

    public void setDBHandler() throws SQLException
    {
        Scanner scanner = new Scanner(System.in);
        int choicedb = 0;

        System.out.println("Choose the Database:");
        System.out.println("[1] SQL Database");
        System.out.println("[2] Text Database");
        System.out.print("Enter your choice: ");
        do{
            choicedb = scanner.nextInt();
        }while(choicedb!=1 && choicedb!=2);
        
        switch(choicedb)
        {
            case 1:
                dbHandler = new sqlDB();
            break;
            
            case 2:
                dbHandler = new TextDB();
            break;
        }

        scanner.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            
            try{
                setDBHandler();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("resource/weatherapp.fxml"));

            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == JavafxController.class) {
                    JavafxController controller = new JavafxController();
                    controller.setDBhandler(dbHandler);
                    return controller;
                } else {
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Parent root = loader.load();
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root));

            Image icon = new Image("resource/4052984.png"); // set icon
            primaryStage.getIcons().add(icon);
            primaryStage.show();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}