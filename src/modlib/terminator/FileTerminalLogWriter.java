package modlib.terminator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.Timer;

import org.apache.commons.io.FileUtils;

import modlib.e.util.Log;
import modlib.e.util.StringUtilities;

/**
 * Logs terminal output to a file.
 * Logging can be temporarily suspended.
 * If the terminal logs directory does not exist or we can't open the log file for some other reason, logging is automatically suspended, and can't be un-suspended.
 */
public class FileTerminalLogWriter extends TerminalLogWriter {
    // We can't use ':' to separate the hours, minutes, and seconds because it's not allowed on all file systems.
//    private static final DateFormat FILENAME_TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss.SSSZ");
	private static final String CURRENT_LOGFILE = "/tmp/shoulderhack.log";
	private static final String LAST_LOGFILE = "/tmp/shoulderhack.old";
    
    private String info = "(not logging)";
    private Timer flushTimer;
    
    public FileTerminalLogWriter() throws IOException {
        // Establish the invariant that writer != null.
        // suspendedWriter is still null - when we're not suspended.
        this.writer = NullWriter.INSTANCE;
        this.flushTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flush();
            }
        });
        flushTimer.setRepeats(true);
        initLogging();
        flushTimer.start();
    }
    
    public static InputStream replayLastLog() throws IOException {
    	System.out.println("Moving "+CURRENT_LOGFILE+" to "+LAST_LOGFILE);
    	FileUtils.deleteQuietly(new File(LAST_LOGFILE));
    	FileUtils.moveFile(new File(CURRENT_LOGFILE), new File(LAST_LOGFILE));
    	return new FileInputStream(new File(LAST_LOGFILE));
    }
        
    private void initLogging() throws IOException {
    	File logFile = new File(CURRENT_LOGFILE);
    	this.info = "(\"" + logFile + "\" could not be opened for writing)";
    	this.writer = new BufferedWriter(new FileWriter(logFile));
    	this.info = logFile.toString();
    	System.out.println("Logging to "+logFile);
    }
    
    @Override
    public void append(char[] chars, int charCount, boolean sawNewline) throws IOException {
        writer.write(chars, 0, charCount);
        if (sawNewline) {
            flushTimer.restart();
        }
    }
    
    @Override
    public void append(byte[] chars, int length, boolean sawNewline) throws IOException {
    	for (int i = 0; i < length; i++) {
    		writer.write(chars[i]);
    	}
    	if (sawNewline)
    		flushTimer.restart();
    }
    
    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (Throwable th) {
            Log.warn("Exception occurred flushing log writer \"" + info + "\".", th);
        }
    }
    
    @Override
    public void close() {
        try {
            suspend(false);
            writer.close();
            writer = NullWriter.INSTANCE;
        } catch (Throwable th) {
            Log.warn("Exception occurred closing log writer \"" + info + "\".", th);
        }
    }
    
    @Override
    public String getInfo() {
        return info;
    }
}
