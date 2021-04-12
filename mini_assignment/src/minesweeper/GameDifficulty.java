package minesweeper;

enum Level {
    NEW,
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
             case NEW -> {
                 return 10;
             }
            case BEGINNER -> {
                return 12;
            }
            case MEDIUM -> {
                return 15;
            }
            case HIGH -> {
                return 24;
            }
        }
        return 10;
    }

    public int getColumns(){
        switch (this.level){
            case NEW -> {
                return 10;
            }
            case BEGINNER -> {
                return 12;
            }
            case MEDIUM -> {
                return 15;
            }
            case HIGH -> {
                return 30;
            }
        }
        return 10;
    }

    public int getMineCount(){
        switch (this.level){
            case NEW -> {
                return 10;
            }
            case BEGINNER -> {
                return 12;
            }
            case MEDIUM -> {
                return 50;
            }
            case HIGH -> {
                return 80;
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
