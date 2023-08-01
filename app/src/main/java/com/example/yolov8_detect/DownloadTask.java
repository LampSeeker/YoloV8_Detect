package com.example.yolov8_detect;

import android.os.AsyncTask;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;




public class DownloadTask extends AsyncTask<String, Void, Boolean> {

    private static final int BUFFER_SIZE = 1024;

    @Override
    protected Boolean doInBackground(String... params) {
        String fileUrl = params[0];
        String destinationPath = params[1];

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = new BufferedInputStream(url.openStream());
                FileOutputStream output = new FileOutputStream(destinationPath);

                byte data[] = new byte[BUFFER_SIZE];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}