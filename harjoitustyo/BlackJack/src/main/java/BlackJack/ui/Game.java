
package blackjack.ui;
import BlackJack.ui.GameService;
import BlackJack.cards.Card;
import blackjack.player.Player;
import java.io.File;  
import java.io.FileWriter;
import java.io.IOException;   

import java.util.Scanner;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author suvik
 */
public class Game {
    Boolean gameIsOn;
    Player player; 
    Player ai;
    Scanner scanner;
    int bet;
    Boolean checkBalance;
    int profit;
    File stats;
    double oldStats;
    Stage stage;
    HBox playerHand;
    HBox handValues;
    HBox opponentHand;
    HBox showWinner;

    
    
    int input;
    public Game(Stage stage){
        this.stage = stage;
        bet = 0;
        profit = 0;
        scanner = new Scanner(System.in);
        player = new Player("Boris");
        ai = new Player("AI");
        gameIsOn = true;
        checkBalance = false;
        oldStats = 0;
        playerHand = new HBox();
        opponentHand = new HBox();
        handValues = new HBox();
        showWinner = new HBox();
        
    }
    public String getHandValue(Player player) {
        return String.valueOf(player.getHandValue());
    }
    public Button quitGameButton() {
        Button quitGame = new Button("Quit game");
        quitGame.setMinWidth(250);
        quitGame.setMinHeight(80);
        quitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               stage.close();    
            }
        });
        return quitGame;
        
    }
    public void startOptionsUI() {
        Button newGame = new Button("New game");
        newGame.setMinWidth(250);
        newGame.setMinHeight(80);
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               gameUI();
            }
        });
        Button loadGame = new Button("Load game");
        loadGame.setMinWidth(250);
        loadGame.setMinHeight(80);

        VBox optionsSetup = new VBox(8);
        optionsSetup.setMaxSize(250, 250);
        optionsSetup.setPrefSize(250, 250);
        optionsSetup.getChildren().add(newGame);
        optionsSetup.getChildren().add(loadGame);
        optionsSetup.getChildren().add(quitGameButton());
       
        Scene startScene = new Scene(optionsSetup);
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
       
        
    }
    
    public void gameUI() {
        GameService newGame = new GameService(ai, player);
        newGame.startGame();
        Card aiFirstCard = newGame.dealAI();
        Card aiSecondCard = newGame.dealAI();
        Card playerFirstCard = newGame.dealPlayer();
        Card playerSecondCard = newGame.dealPlayer();
        

        HBox actionButtons = new HBox();
        Button hitButton = new Button("Hit");
        hitButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               Card newCard = newGame.dealPlayer();
               playerHand.getChildren().add(new Label(newCard.getSuit() + " " + newCard.getValue()));
               if (player.getHandValue() > 21) {
                   player.setLoser();
                   ai.setWinner();
                   showWinner.getChildren().add(new Label("Ai WON!"));
               }
               //handValues = new HBox();
               
               
            }
        });
        Button stayButton = new Button("Stay");
           stayButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               while (ai.getHandValue() < 17) {
                Card newCard = newGame.dealAI();
                opponentHand.getChildren().add(new Label(newCard.getSuit() + " " + newCard.getValue()));
                if (ai.getHandValue() > 21) {
                    player.setWinner();
                    ai.setLoser();
                    showWinner.getChildren().add(new Label("Player WON!"));
                } else if (ai.getHandValue() < 22 || ai.getHandValue() > 16) {
                    newGame.checkWinner(50);
                    if (player.getWinner()) {
                        showWinner.getChildren().add(new Label("Player WON!"));
                        
                    } else {
                        showWinner.getChildren().add(new Label("AI WON!"));
                        
                    }
                  
                }
                   
               }
               
               //handValues = new HBox();
               
               
            }
        });
        Button doubleButton = new Button("Double");
        
        actionButtons.getChildren().add(hitButton);
        actionButtons.getChildren().add(stayButton);
        actionButtons.getChildren().add(doubleButton);

        
 
        opponentHand.getChildren().add(new Label(aiFirstCard.getSuit() + " " + aiFirstCard.getValue()));
        opponentHand.getChildren().add(new Label(aiSecondCard.getSuit() + " " + aiSecondCard.getValue()));

        playerHand.getChildren().add(new Label(playerFirstCard.getSuit() + " " + playerFirstCard.getValue()));
        playerHand.getChildren().add(new Label(playerSecondCard.getSuit() + " " + playerSecondCard.getValue()));
        VBox testi = new VBox();
        testi.getChildren().add(new Label(playerFirstCard.getSuit() + " " + playerFirstCard.getValue()));
        testi.getChildren().add(new Label(playerSecondCard.getSuit() + " " + playerSecondCard.getValue()));
        
        VBox testi2 = new VBox();
        testi2.getChildren().add(new Label(playerFirstCard.getSuit() + " " + playerFirstCard.getValue()));
        testi2.getChildren().add(new Label(playerSecondCard.getSuit() + " " + playerSecondCard.getValue()));
        
        handValues.getChildren().add(new Label("Player hand value: " + getHandValue(player)));
        handValues.getChildren().add(new Label("AI hand value: " + getHandValue(ai)));
        handValues.setSpacing(100);
        TilePane board = new TilePane();
        board.getChildren().add(handValues);
        board.getChildren().add(testi2);
        board.getChildren().add(hitButton);
        board.getChildren().add(stayButton);
        board.getChildren().add(testi);
        board.getChildren().add(showWinner);
        
        System.out.println("Test " + handValues.toString());
        
        board.setMaxWidth(600);
        board.setHgap(10);
        board.setVgap(10);
        



        
        Scene gameView = new Scene(board,600,600);
        stage.setScene(gameView);
        
        final long startNanoTime = System.nanoTime();
         new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {
            double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
            double x = 232 + 128 * Math.cos(t);
            double y = 232 + 128 * Math.sin(t);
            handValues.getChildren().clear();
            handValues.getChildren().add(new Label("Player hand value: " + getHandValue(player)));
            handValues.getChildren().add(new Label("Ai hand value: " + getHandValue(ai)));
            
            if(player.getLoser()) {
                System.out.println("Player lost");
            }
            if (ai.getLoser()) {
                System.out.println("Ai lost");
            }

            
        }
    }.start();
        
        
        
        //stage.setResizable(false);
        stage.show();
       
    }
    
    
    public void createStatsFile(){
        try {
          stats = new File("stats.txt");
        if (stats.createNewFile()) {
          System.out.println("File created: " + stats.getName());
        } else {
          System.out.println("File already exists.");
        }
      } catch (IOException e) {
        System.out.println("An error occurred.");
        
    }
    }
    public double getStats() {
            try {
            File statsFile = new File("stats.txt");
            Scanner myReader = new Scanner(statsFile);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
                System.out.println("Testi " + data);
              oldStats = Double.parseDouble(data);
                System.out.println("old stats:" + oldStats);
            }
            myReader.close();
          } catch (IOException e) {
            System.out.println("An error occurred.!!!!!!");
            
          }
            return oldStats;
  }
    
        
    public void saveStats() {

    
    try {
      FileWriter saveFiles = new FileWriter("stats.txt");
      double newBalance = getStats();
       
      newBalance =+ player.getAccountBalance();
      System.out.println("toimiikos balanze "  + newBalance);
      saveFiles.write(Integer.toString((int) player.getAccountBalance()));
      saveFiles.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      
    }
  }
    
    
    public void gameOptions() {
            createStatsFile();
            GameService newGame = new GameService(ai,player);
            player.setBalance(getStats());
            
           
            
            
           
   
            while (checkBalance == false) {
            System.out.println("Your balance is " + player.getAccountBalance());
            System.out.println("Set bet!");
                bet = scanner.nextInt();
            if (bet > player.getAccountBalance()) {
                System.out.println("Not enough money.Bet again");
                
            } else {
                checkBalance = true;                 
            }
            }

            newGame.startGame();
            System.out.println("Game is on!");
            System.out.println("Dealing cards:");
            newGame.dealAI();
            newGame.dealAI();
            System.out.println(newGame.ai.getHandValue());
            newGame.dealPlayer();
            newGame.dealPlayer();
            System.out.println(newGame.player.getHandValue());
            newGame.checkBlackJack(bet);
            while(!player.loser && !player.winner) {    
            System.out.println("[1] Hit");
            System.out.println("[2] Stay");
            System.out.println("[3] Double");
            input = scanner.nextInt();
            
            switch (input) {
                case 1:                   
                    newGame.dealPlayer();
                    System.out.println("You got now:");
                    newGame.player.showHand();
                    System.out.println(newGame.player.getHandValue());
                    if (this.player.getHandValue() > 21) {
                        System.out.println("OVER! You lose!");
                        this.player.setLoser();
                        player.setBalance(-bet);
                        
                    }
                    break;
                case 2:
                    
                    while (ai.getHandValue() < 17) {
                        System.out.println("AI takes more");
                        newGame.dealAI();
                        System.out.println("Ai got now:");
                        newGame.ai.showHand();
                    }
                    if (ai.getHandValue() >= 17) {
                        System.out.println("AI final hand:");
                        newGame.ai.showHand();
                        System.out.println("Ai hand value: " + newGame.ai.getHandValue());
                        System.out.println("Your final hand:");
                        newGame.player.showHand();
                        System.out.println("Player hand value: " + newGame.player.getHandValue());
                        newGame.checkWinner(bet);
                    }
                    break;
                case 3:
                    if (player.getAccountBalance() < (bet * 2)) {
                        System.out.println("Not enough funds to double!");
                    } else {
                        
                    System.out.println("Double! Dealing one card to the player");
                    newGame.dealPlayer();
                    System.out.println("Player got now:");
                    newGame.player.showHand();
                    System.out.println("Player hand value is " + player.getHandValue());
                    if (player.getHandValue() > 21) {
                        System.out.println("OVER! You lose!");
                        this.player.setLoser();
                        bet = bet * 2 ;
                        player.setBalance(-bet);
                        
                    } else {
                       while (ai.getHandValue() < 17) {
                        System.out.println("AI takes more");
                        newGame.dealAI();
                        System.out.println("Ai got now:");
                        newGame.ai.showHand();
                        System.out.println("Ai hand value "+ newGame.ai.getHandValue());
                    }
                       if (ai.getHandValue() >= 17) {
                        System.out.println("AI final hand:");
                        newGame.ai.showHand();
                        System.out.println("Ai hand value: " + newGame.ai.getHandValue());
                        System.out.println("Your final hand:");
                        newGame.player.showHand();
                        System.out.println("Player hand value: " + newGame.player.getHandValue());
                        bet = bet * 2;
                        newGame.checkWinner(bet);
                    }
                      
                    }
                        
                    }
                    break;
                        
                default:
                    System.out.println("Incorrect input");
                    break;
            }
            
            
            
            gameIsOn = false;
        }
    }
    public void afterGameOptions() {
         System.out.println("[3] Quit");
         System.out.println("[4] Play again");
         System.out.println("[5] Deposit money");
         input = scanner.nextInt();
            
            switch (input) {
                case 3:
                    System.out.println("Closing game");            
                    saveStats();
                    scanner.close();
                    break;
                case 4:
                    System.out.println("New game!");
                    player.clearStats();
                    ai.clearStats();
                    this.checkBalance = false;
                    this.startGame();
                    break;
                case 5:
                    System.out.println("How much you want to invest: (1-1000)");
                    input = scanner.nextInt();
                    if (input < 1 || input > 1000) {
                        System.out.println("incorrect amount");
                    } else {
                        System.out.println("Deposit " + input + " to your account");
                        player.setBalance(input);
                        System.out.println("Current balance is " + player.getAccountBalance());
                        afterGameOptions();
                        
                    }
                default:
                    afterGameOptions();
                    break;
            }
        
    }
    
    public void startGame() {

            startOptionsUI();
            //gameOptions();
            
    
            //afterGameOptions();
            
                    
    }
    
    
    
}