package sk.tuke.gamestudio.game.consoleUi;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.core.Block;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.GameState;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import static sk.tuke.gamestudio.game.core.Block.*;
import static sk.tuke.gamestudio.game.core.Field.*;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final Block block;
    @Autowired
    private ScoreService scoreService;
    //    private Level level;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private Field field;


    public ConsoleUI(Field field, Block block) {
        this.field = field;
        this.block = block;
    }

    //printing out the board
    private static void printBoard() {
        for (int i = 0; i < boardSize + 1; i++) {
            if (i < boardSize) {
                System.out.print(" " + i + "|");
            } else {
                System.out.print("    ");
            }
            for (int j = 0; j < boardSize; j++) {
                if (i == boardSize) {
                    System.out.print(j + " ");
                } else if (board[i][j] == -1) {

                    System.out.print("\uD83C\uDFFB");
                } else {
                    System.out.print("❇");
                }
            }
            System.out.println();
        }
    }

    //printing all the available blocks
    private static void printBlock(int[][] block) {
        for (int[] ints : block) {
            for (int anInt : ints) {
                if (anInt == 1) {
                    System.out.print("❇");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    //main
    public void play() {
        endMenu();
    }

    //main game loop
    private void mainLoop(){
        field.initBoard();
        printBoard();
        //start of main loop fo game
        while (isGameOver() != GameState.SOLVED) {

            // Print the available blocks
            System.out.println("Available Blocks:");
            for (int i = 0; i < blockCount; i++) {
                if (!used[i]) {
                    System.out.println("Block " + (i + 1) + ":");
                    printBlock(blocksL1[i]);
                }
            }

            int choice = 0;

            //user choice of block to place
            do {
                try {
                    System.out.print("Enter the number of the block (1-" + blockCount + "): ");
                    choice = scanner.nextInt() - 1;
                }catch (Exception e){
                    System.out.println("wrong input");
                }
            } while (choice < 0 || choice >= blockCount);

            System.out.println("Enter if you want to place it or remove");
            String input = scanner.next();
            int row, col;

            //placing the block
            if (Objects.equals(input, "place")) {

                do {
                    System.out.print("Enter the row where you want to place the block: ");
                    row = scanner.nextInt();
                    System.out.print("Enter the column where you want to place the block: ");
                    col = scanner.nextInt();
                } while (!canPlaceBlock(choice, row, col));

                // Place the block on the board
                placeBlock(choice, row, col);
                // Mark the block as used
                used[choice] = true;
                // Print the board
                printBoard();

            }

            //removing the block
            else if (Objects.equals(input, "remove")) {

                do {
                    System.out.print("Enter the row where you want to remove the block: ");
                    row = scanner.nextInt();
                    System.out.print("Enter the column where you want to remove the block: ");
                    col = scanner.nextInt();
                } while (canPlaceBlock(choice, row, col));

                // Place the block on the board
                removeBlock(choice, row, col);
                // Mark the block as used
                used[choice] = false;
                // Print the board
                printBoard();

            } else {
                System.out.println("Wrong input");
                printBoard();
            }
        }
    }

    //menu you see at the start
    private String startMenu(){
        System.out.println("Welcome to Block Puzzle!");
        if (Objects.equals(getAnswer("Do you want to see top 10 score in game? y/n"), "y")) {
            printTopScores();
        }
        if (Objects.equals(getAnswer("Do you want to see comments and rating of game? y/n"), "y")) {
            printComments();
            printRating();
        }

        String answer = getAnswer("Are you ready to start playing the game? y/n");
        if (Objects.equals(answer, "y")) {
            System.out.println("Your objective is to place all the blocks on the board.");
            mainLoop();
        }

        return answer;

    }

    //menu at the end
    private void endMenu(){

        if(startMenu().equals("y")) {
            System.out.println("Congratulations! You win!");

            if (Objects.equals(getAnswer("Do you want to play next level? y/n"), "y")) {
                field = new Field(8);
                field.initBoard();
                block.initBlocks();
                play();
            }

            scoreService.addScore(new Score(System.getProperty("user.name"), "blockpuzzle", field.getScore(), new Date()));

            if (Objects.equals(getAnswer("Do you want to comment and rate the game? y/n"), "y")) {
                ratingComment();
            }
        }
        else {
            System.out.println("Thanks for 'playing' ");
        }
    }

    //printing top 10 score
    private void printTopScores() {
        var scores = scoreService.getTopScores("blockpuzzle");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < scores.size(); i++) {
            var score = scores.get(i);
            System.out.printf("%d. %s %d\n", i + 1, score.getPlayer(), score.getPoints());
        }

        System.out.println("---------------------------------------------------------------");
    }

    //printing comments ordered by date
    private void printComments() {
        var comments = commentService.getComments("blockpuzzle");
        System.out.println("Comments");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < comments.size(); i++) {
            var comment = comments.get(i);
            System.out.printf("%d. %s %s\n", i + 1, comment.getPlayer(), comment.getComment());
        }

        System.out.println("---------------------------------------------------------------");
    }

    //printing rating from best to worst
    private void printRating() {
        var rating = ratingService.getRating("blockpuzzle");
        System.out.println("Rating");
        System.out.println("---------------------------------------------------------------");
        System.out.println("All ratings");

        //printing rating by best to worst
        for (int i = 0; i < rating.size(); i++) {

            var rating1 = rating.get(i);
            System.out.printf("%d. %s ", i + 1, rating1.getPlayer());

            for (int j = 0; j < rating1.getRating(); j++) {
                System.out.print("\uD83C\uDF1F");
            }

            System.out.println();
        }
        System.out.println("---------------------------------------------------------------");
        //printing average rating
        System.out.println("Average rating");
        var averageRating = ratingService.getAverageRating("blockpuzzle");
        for (int j = 0; j < averageRating; j++) {
            System.out.print("\uD83C\uDF1F");
        }

        System.out.println();
        System.out.println("---------------------------------------------------------------");
        //printing average rating
        System.out.println("Your rating");
        var yourRating = ratingService.getYourRating("blockpuzzle",System.getProperty("user.name"));
        for (int j = 0; j < yourRating; j++) {
            System.out.print("\uD83C\uDF1F");
        }
        System.out.println();

        System.out.println("---------------------------------------------------------------");
    }


    //getting comment and rating from user at the end of game
    private void ratingComment(){
        System.out.println("How many stars would you give to this game?");
        int starsRating = scanner.nextInt();
        ratingService.addRating(new Rating(System.getProperty("user.name"), "blockpuzzle", starsRating, new Date()));
        System.out.println("Comment something what you like and what you dont like about game");
        String userComment1 = scanner.nextLine();
        String userComment = scanner.nextLine();
        commentService.addComment(new Comment(System.getProperty("user.name"), "blockpuzzle", userComment, new Date()));
    }

    //basic scanner to make code look a little better
    private String getAnswer(String message){
        System.out.println(message);
        return scanner.next();
    }
}