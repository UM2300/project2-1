import java.util.Scanner;

public class game {
    board board = new board();

    public static void main(String[] args){
        
        game game = new game();

        String input;
        boolean choice;

        boolean running = true;

        Scanner scanner = new Scanner(System.in);


        do{ 
            System.out.print("> ");
            input=scanner.nextLine();
            //scanner.close();
            choice = game.comCheck(input);

            if(choice==false)
                System.out.println("\nI don't recognize that command\n");
            else
                game.comStart(input);
        }
        while(running==true);


    }


    public boolean comCheck(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"add","move","check"};
        boolean stat=false;
        String one=split[0].toLowerCase();
        for(int i=0; i<comType.length; i++){
            if(one.equals(comType[i]))
                stat=true;
        }
        return stat;
    }

    public void comStart(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"add","move","check"};
        String one=split[0].toLowerCase();
        int num=0;
        for(int i=0; i<comType.length; i++){
            if(one.equals(comType[i])){
                num=i;
            }  
        }

        switch (num){
            case 0:
                add(command);
                break;
            case 1:
                move(command);
                break;
            case 2:
                check(command);
                break;
        }
    }


    public void add(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"0","1","2","3","4","5"};
        int two=Integer.parseInt(split[1]);
        boolean check=false;
        for(int i=0; i<comType.length; i++){
            if(two==Integer.parseInt(comType[i])){
                check=true;
            }  
        }
        if(check==false){
            System.out.println("\nInvalid Piece\n");
        }
        else
            board.addPiece(two, Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }


    public void move(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"1","2","3","4","5"};
        int two=Integer.parseInt(split[1]);
        boolean check=false;
        for(int i=0; i<comType.length; i++){
            if(two==Integer.parseInt(comType[i])){
                check=true;
            }  
        }
        if(check==false){
            System.out.println("\nInvalid Chord\n");
        }
        else
            board.move(two, Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
    }

    public void check(String command){
        board.checkState();
    }
    
}
