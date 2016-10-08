class ClientHandler extends Thread{

    private Server server = null;
    private ClientEntity client = null;
    private boolean alive = true;

    ClientHandler(Server server, ClientEntity client){
        this.server = server;
        this.client = client;
    }

    public void run(){
        try{
            while(alive){
                String message = client.readFrom();
                if(message.contains("/")){
                    command(message);
                }else{
                    server.broadcast(messageFormat(message), client);
                }
            }
        }catch (Exception e){
            System.out.println("Could not send message.");
        }finally {
            System.out.println("Closing connection");
            closeClient();
        }

    }
    private String messageFormat(String message){
        String formatMessage = "";
        formatMessage += client.getNickname()+": "+message;
        return formatMessage;
    }
    private void command(String commandString){

        String[] command = commandString.split(" ");
        System.out.println(command[0]);
        switch (command[0].toLowerCase()){

            case "/quit":
                client.closeReadSocket();
                client.closeWriteSocket();
                client.closeSocket();
                alive = false;
                break;
            case "/who":
                client.writeTo(server.listClientNick());
                break; // need to list all users
            case "/nick":
                System.out.println(client.getNickname() + " changed name to: " + command[1]);
                //client.setNickname(command[1]); // need to verify duplicates
                server.setNickname(client, command[1]);
                break;
            case "/help":
                client.writeTo("/quit - leave, /who - list of all users, /nick <nickname> - change your nickname, /help - list of available commands");
                break;
            default:
                System.out.println(client.getNickname() + " invalid command!");
                client.writeTo("Unknown command!");
                break;
        }
    }
    private void closeClient(){
        client.closeReadSocket();
        client.closeWriteSocket();
        client.closeSocket();
        alive = false;
        server.removeClient(client);
    }
}