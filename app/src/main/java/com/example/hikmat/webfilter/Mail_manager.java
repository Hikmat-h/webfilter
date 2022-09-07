package com.example.hikmat.webfilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Hikmat on 11/20/17.
 */

public class Mail_manager extends AppCompatActivity {
    Session session = null;
    String receiver, subject, message_text;
    String file_name;
    File file;
    private Multipart multipart;
    public void Send()
    {
        receiver = "habibullaevh@gmail.com";
        subject = "Daily report";
        message_text = "Hello, \nHere is the attachment of your child's daily web search history. " +
                "If you see any inapropriate word in the list, please add it on black list in application.\n " +
                "Sincerely Web Guard";
        multipart= new MimeMultipart();
        Filter_service path=new Filter_service();
        file_name=path.FILENAME;
        file =new File(path.path+"/"+file_name);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("guardweb25@gmail.com", "Brigada.26");
            }
        });

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();

    }
    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("guardweb25@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
                message.setSubject(subject);

                BodyPart messageBodyPart = new MimeBodyPart();          //message text
                messageBodyPart.setText(message_text);
                multipart.addBodyPart(messageBodyPart);

                BodyPart attachment=new MimeBodyPart();                 // attachment
                DataSource source=new FileDataSource(file);
                attachment.setDataHandler(new DataHandler(source));
                attachment.setFileName("daily report");
                multipart.addBodyPart(attachment);

                message.setContent(multipart);

                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}
