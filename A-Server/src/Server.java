
public class Server {
    public static final int SERVER_PORT = 10017;
    private CommHandler commHandler;

    public Server() {

    }

    public void start(){
            commHandler = new CommHandler(SERVER_PORT);
            System.out.println("Starting server...");
            while(true){
                if(listen()){
                    startGame();
                }
            }
            //close();
    }

    private boolean listen(){
        String msgReceived;
        boolean ready = false;
        while(true){
            msgReceived = commHandler.readMessage();

            if(msgReceived.equals("TIMEOUT")){
                commHandler.removeCurrentClient();
                return false;
            }else{
                if(msgReceived!=null) {
                    if (msgReceived.equals("HELLO")) {
                        commHandler.sendMessage("OK");
                        ready = true;
                    } else if (msgReceived.equals("START") && ready) {
                        commHandler.sendMessage("READY");
                        return true;
                    } else {
                        commHandler.sendMessage("ERROR");
                        return false;
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

