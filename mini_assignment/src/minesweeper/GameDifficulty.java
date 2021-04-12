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

//
//    public Level getLevel() {
//        return this.level;
//    }
//
//    public void setLevel(Level address) {
//        this.addres


//    public int ExpertRow(){
//        return 24;
//    }
//    public int ExpertCol(){
//        return 24;
//    }
//    public int ExpertMines(){
//        return 24;
//    }

}
