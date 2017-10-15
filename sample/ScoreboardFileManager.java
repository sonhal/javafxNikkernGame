package sample;

import java.io.*;
import java.util.*;

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

            while(true) {
               line = inputStream.readLine();
                if(line == null){
                    break;
                }

               System.out.println(line);
               String[] playerInfo = line.split(",");

                System.out.println(line);

                scoreboard[i][0] = playerInfo[0];
                scoreboard[i][1] = playerInfo[1];
                i++;

            }
            System.out.println("scorebord " + scoreboard[0][0]);
            return  scoreboard;

        }
    }

    private void writeScoreboard(String[] scores) throws IOException {
        try (BufferedWriter outStream = new BufferedWriter(new FileWriter(filename))) {
            //ArrayList<String> scoreboard = new ArrayList<>();
            for(String line : scores) {
                System.out.println("Writing line: " + line);
                outStream.write(line);
                outStream.newLine();
            }
        }
    }

    private String[][] updateScoreBoard(String[] newScore) throws IOException {
       String[][] scoreboard = getScoreboard();
       String[] swap = newScore;

       outerloop:
       for(int  i = 0; i < scoreboard.length; i++) {


           for(int  j = 0; j < scoreboard.length; j++){
               if(scoreboard[j][0] == null || scoreboard[j][0].isEmpty()){
                   scoreboard[j] = swap;
                   break outerloop;
               }
               if(scoreboard[j][0].equals("null")){
                   scoreboard[j] = swap;
                   break outerloop;
               }
               if (Integer.parseInt(scoreboard[j][1]) < Integer.parseInt(swap[1])){
                   String[] oldScore = scoreboard[j];
                   scoreboard[j] = swap;
                   swap = oldScore;
               }

           }



       }
        ArrayList<String[]> scoreboardList = new ArrayList<>();
       for(String[] score : scoreboard){
           if(score[0] != null){
               scoreboardList.add(score);
           }
       }
        System.out.println(scoreboard[0][0]);
        String[][] returnArray = new String[scoreboardList.size()][2];
        for(int i = 0; i < scoreboardList.size(); i++){
            String[] score = scoreboardList.get(i);
            returnArray[i] = score;
        }
        return returnArray;
    }




    public void update(String[] newPlayerScore) throws IOException {
       String[][] newScoreboard = updateScoreBoard(newPlayerScore);
       String[] compactScoreboard = new String[5];
       int i = 0;
       for(String[] score : newScoreboard ){
           System.out.println(score[0]);
           compactScoreboard[i] = score[0] + "," + score[1];
           i++;
       }
       System.out.println(compactScoreboard[0] + " " + compactScoreboard[1]);
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
