import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class RollingLogFile {

    public static void main(String[] args) {
        Log log = new Log();
        log.gerarLog("teste");
    }
}

class Log{
    public static final int FILE_SIZE = 1024;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    public static void gerarLog(String str){
       Logger logger = Logger.getLogger(RollingLogFile.class.getName());
        try {
            // Creating an instance of FileHandler with 5 logging files
            // sequences.
            Date date = new Date();
            String data_corrente = dateFormat.format(date);
            FileHandler handler = new FileHandler("/home/biomata/backup_"+data_corrente+".log", FILE_SIZE,5, true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.warning("Failed to initialize logger handler.");
        }
        String username = System.getProperty("user.name");
        logger.info("Usuário: "+username);
        logger.info(str+": information message.");
        logger.warning("Logging warning message.");

    }
}
