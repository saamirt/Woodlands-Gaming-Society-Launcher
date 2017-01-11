/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgs.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class FileCommandReaderThread extends Thread{
    String COMMANDFILEPATH = "command.txt";
    String command;
    File commandFile;
    boolean alreadyRead = false;
    @Override
    public void run(){
        while(true){
            if((commandFile = new File(COMMANDFILEPATH)).exists() && !alreadyRead){
                alreadyRead = true;
                try (BufferedReader commandReader = new BufferedReader(new FileReader(COMMANDFILEPATH))){
                    while((command = commandReader.readLine()) != null && command.contains(";")){
                        command = command.substring(0,command.indexOf(";"));
                        parseCommand(command);
                    }
                    commandReader.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileCommandReaderThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FileCommandReaderThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if (!commandFile.exists()){
                alreadyRead = false;
            }
        }
    }
    public void parseCommand(String command){
        System.out.println("Command: " + command);
        if (command.toLowerCase().equals("sayhello")){
            JOptionPane.showMessageDialog(new JFrame(), "hello!");
            System.out.println("Command Activated");
        } else{
            System.out.println("Invalid Command");
        }
    }
}