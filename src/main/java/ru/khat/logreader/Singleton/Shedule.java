package ru.khat.logreader.Singleton;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.io.File;
import java.util.Date;

@Singleton
public class Shedule {

    @Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
    public void removeFiles() {


        File folder = new File(System.getProperty("user.dir") + "\\downloads\\");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {

                Date now = new Date();
                Date date = new Date(file.lastModified());
                long diff = now.getTime() - date.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                if (diffMinutes > 5) {
                    file.delete();
                }

            }
        }
    }


}
