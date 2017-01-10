/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgs.launcher;

import java.io.File;

class commandReaderThread extends Thread{
    String COMMANDFILEPATH = "command.txt";
    String command;
    File commandFile;
    @Override
    public void run(){
        //fileRead();
        emailRead();
    }
    public void emailRead(){
        emailReader commandReader = new emailReader();
        commandReader.read();
    }
    /*public void fileRead(){
        while((commandFile = new File(COMMANDFILEPATH)).exists()){
            try (BufferedReader commandReader = new BufferedReader(new FileReader(COMMANDFILEPATH))){
                while((command = commandReader.readLine()) != null){
                    parseCommand(command);
                }
                commandReader.close();
                System.out.println(commandFile.delete());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(commandReaderThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(commandReaderThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
    public void parseCommand(String command){
        System.out.println(command);
        if (command.equals("hello")){
            System.out.println("Command says 'hello'");
        } else{
            System.out.println("Invalid command");
        }
    }
}