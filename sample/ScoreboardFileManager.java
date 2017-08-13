package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ScoreboardFileManager {
    String filename;

   public ScoreboardFileManager(String filename){
       this.filename = filename;

   }


   private String[][] getScoreboard() throws IOException {
        try (BufferedReader inputStream = new BufferedReader(new FileReader(filename));) {
            String[][] scoreboard = new String[5][2];

            String line;
            int i = 0;

            while((line = inputStream.readLine()) != null || i == 4 ) {

               String[] playerInfo = line.split(",");
               System.out.print(playerInfo[0]);
                System.out.print(line);
               scoreboard[i][0] = playerInfo[0];
                scoreboard[i][1] = playerInfo[1];
                i++;

            }
            return  scoreboard;

        }
    }

    private void writeScoreboard(String[] scores) throws IOException {
        try (BufferedWriter outStream = new BufferedWriter(new FileWriter(filename))) {
            //ArrayList<String> scoreboard = new ArrayList<>();
            for(String line : scores) {
                outStream.write(line);
                outStream.newLine();
            }
        }
    }

    private String[][] updateScoreBoard(String[] newScore) throws IOException {
       String[][] scoreboard = getScoreboard();

       for(int  i = 0; i < scoreboard.length; i++) {
           String[] swap = newScore;

           for(int  j = 0; i < scoreboard.length; i++){
               if (Integer.parseInt(scoreboard[i][1]) < Integer.parseInt(swap[1])){
                   String[] oldScore = scoreboard[i];
                   scoreboard[i] = swap;
               }
           }



       }
        return scoreboard;
    }




    public void update(String[] newPlayerScore) throws IOException {
       String[][] newScoreboard = updateScoreBoard(newPlayerScore);
       String[] compactScoreboard = new String[5];
       int i = 0;
       for(String[] score : newScoreboard ){
           compactScoreboard[i] = score[0] + "," + score[1];
       }

       writeScoreboard(compactScoreboard);

    }

    public String getFormatedScoreboard() throws IOException {
      String[][] scoreboard = getScoreboard();
      String formatedScore = "";

      for(String[] score : scoreboard){
          formatedScore += score[0] + " - score: " + score[1] + "\n";
      }
      return formatedScore;

    }




}
