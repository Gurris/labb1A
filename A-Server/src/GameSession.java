
import java.util.Random;


public class GameSession {
    private boolean isRunning;
    private CommHandler commHandler;
    public GameSession(CommHandler commHandler) {
        isRunning = true;
        this.commHandler = commHandler;
    }
    public void start() {
        String msgReceived;
        String regex = "\\d+";
        Random rng = new Random();
        int ans = rng.nextInt(100) + 1;
        System.out.println(ans);
        while(isRunning){
            msgReceived = commHandler.readMessage();
            if(msgReceived!=null) {
                if(msgReceived.equals("TIMEOUT")){
                    commHandler.removeCurrentClient();
                    isRunning = false;
                }else if (!msgReceived.matches(regex) || msgReceived.length() > 3) {
                    commHandler.sendMessage("ERROR");
                }else if (ans == Integer.parseInt(msgReceived)) {
                    commHandler.sendMessage("CORRECT, GAME ENDING");
                    System.out.println("Ending game...");
                    commHandler.removeCurrentClient();
                    isRunning = false;
                } else if (ans > Integer.parseInt(msgReceived)) {
                    commHandler.sendMessage("TOO LOW");
                } else if (ans < Integer.parseInt(msgReceived)) {
                    commHandler.sendMessage("TOO HIGH");
                }
            }
        }
    }
}
