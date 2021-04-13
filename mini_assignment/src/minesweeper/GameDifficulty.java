/* GameDifficulty class to allow user the ability to choose different game difficulty levels (different board size and corresponding number of mines)
   Conventionally, classic Minesweeper games have 3 levels of difficulty: Beginner(9*9), Intermediate(16*16) & Hard(16*30)
*/  
  

package minesweeper;

enum Level {
    BEGINNER,
    MEDIUM,
    HIGH
}


public class GameDifficulty {
    private final Level level;

    public GameDifficulty(Level level) {
        this.level = level;
    }

    public int getRow(){
         switch (this.level){
            case BEGINNER -> {
                return 9;
            }
            case MEDIUM -> {
                return 16;
            }
            case HIGH -> {
                return 16;
            }
        }
        return 9;
    }

    public int getColumns(){
        switch (this.level){
            case BEGINNER -> {
                return 9;
            }
            case MEDIUM -> {
                return 16;
            }
            case HIGH -> {
                return 30;
            }
        }
        return 9;
    }

    public int getMineCount(){
        switch (this.level){
            case BEGINNER -> {
                return 10;
            }
            case MEDIUM -> {
                return 40;
            }
            case HIGH -> {
                return 99;
            }
        }
        return 10;
    }
