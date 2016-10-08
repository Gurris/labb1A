
public class Server {
    public static final int SERVER_PORT = 10017;
    private CommHandler commHandler;

    public Server() {

    }
    public void start(){
            commHandler = new CommHandler(SERVER_PORT);
            System.out.println("Starting server...");
            while(true){
                listen();
                startGame();
            }
            //close();
    }

    private void listen(){
        String msgReceived;
        boolean isListening = true;
        boolean ready = false;
        while (isListening){
            msgReceived = commHandler.readMessage();
            if(msgReceived.equals("TIMEOUT")){
            }else{
                if(msgReceived!=null) {
                    if (msgReceived.equals("HELLO")) {
                        commHandler.sendMessage("OK");
                        ready = true;
                    } else if (msgReceived.equals("START") && ready) {
                        commHandler.sendMessage("READY");
                        isListening = false;
                    } else {
                        commHandler.sendMessage("ERROR");
                    }
                }
            }

        }

    }

    private void startGame() {
        System.out.println("starting game");
        GameSession session = new GameSession(commHandler);
        session.start();
    }

    private void close(){
        commHandler.closeSock();
        System.out.println("Server shutdown.");
        System.exit(-1);
    }
}

