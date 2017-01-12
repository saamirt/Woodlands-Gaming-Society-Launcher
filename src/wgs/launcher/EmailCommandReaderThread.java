/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgs.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class EmailCommandReaderThread extends Thread {
    String COMMANDFILEPATH = "command.txt";
    String command;
    File commandFile;

    @Override
    public void run() {
        Properties props = new Properties();
        int messageCount;
        try {
            props.load(new FileInputStream(new File("smtp.properties")));
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imaps");
            store.connect("smtp.gmail.com", "woodlandsgamingclub@gmail.com", "********");

            /* listing folders
            for (Folder f : store.getDefaultFolder().list()) {
                System.out.println(f.getFullName());
            }*/
            Folder emails = store.getFolder("NEW-COMMANDS");
            emails.open(Folder.READ_ONLY);
            int initialMessageCount = emails.getMessageCount();

            while (true) {
                /*displaying total number of messages
                System.out.println("Total Messages:- " + messageCount);
                 */

                if ((messageCount = emails.getMessageCount() - initialMessageCount) > 0) {
                    Message[] messages = emails.getMessages(initialMessageCount + 1, emails.getMessageCount());
                    for (int i = 0; i < messageCount; i++) {
                        String messageBody = getTextFromMessage(messages[i]);
                        System.out.println("Command: " + messageBody.substring(messageBody.length() / 2 + 1));
                        parseCommand(messageBody.substring(messageBody.length() / 2 + 1));
                        initialMessageCount++;
                    }
                }
                /*emails.close(true);
            store.close();*/
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0) {
            throw new MessagingException("Multipart with no body parts not supported.");
        }
        boolean multipartAlt = mimeMultipart.getContentType().matches("multipart/alternative");
        if (multipartAlt) // alternatives appear in an order of increasing 
        // faithfulness to the original content. Customize as req'd.
        {
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        }
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private String getTextFromBodyPart(
            BodyPart bodyPart) throws IOException, MessagingException {

        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }
}
