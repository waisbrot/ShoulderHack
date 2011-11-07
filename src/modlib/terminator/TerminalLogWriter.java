package modlib.terminator;

import java.io.IOException;
import java.io.Writer;

public abstract class TerminalLogWriter {
	private static TerminalLogWriter self = null;
	public static TerminalLogWriter getInstance() throws IOException {
		if (self == null) {
			self = new FileTerminalLogWriter();
		}
		return self;
	}
    protected Writer suspendedWriter;
    protected Writer writer;

    public boolean isSuspended() {
        return (suspendedWriter != null);
    }

    public void suspend(boolean shouldSuspend) {
        flush();
        if (shouldSuspend == isSuspended()) {
            return;
        }
        if (shouldSuspend) {
            suspendedWriter = writer;
            writer = NullWriter.INSTANCE;
        } else {
            writer = suspendedWriter;
            suspendedWriter = null;
        }
    }

	public abstract String getInfo();

	public abstract void close();

	public abstract void flush();

	public abstract void append(char[] chars, int charCount, boolean sawNewline) throws IOException;
	public abstract void append(byte[] chars, int length, boolean sawNewline) throws IOException;

    public static class NullWriter extends Writer {
        public static final Writer INSTANCE = new NullWriter();
        
        private NullWriter() {
        }
        
        @Override public void close() {
        }
        
        @Override public void flush() {
        }
        
        @Override public void write(int c) {
        }
        
        @Override public void write(char[] buffer, int offset, int byteCount) {
        }
    }

}
