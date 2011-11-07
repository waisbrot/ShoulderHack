package net.waisbrot.nethack.shoulder.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import modlib.terminator.FileTerminalLogWriter;
import modlib.terminator.model.TextLine;
import modlib.terminator.terminal.TerminalControl;
import net.waisbrot.nethack.shoulder.client.ConnectionDialog.ConnectHandler;

/**
 * Connect to the terminal server, collect data as it trickles in, offer regex-matching listeners
 * @author nate
 *
 */
public class Client extends Thread {
	private static final int INPUT_BUFFER_SIZE = 4096;


	public Client() throws IOException {
		super("Queue writer");
		control = new TerminalControl();
	}
	private String hostname=null;
	private int port;
	private ConnectionDialog cdlg = null;
	
	private ConnectHandler cdlg_handler = new ConnectHandler(){
		@Override
		public void ok(String hostname, int port) {
			Client.this.cdlg.setVisible(false);
			Client.this.cdlg.dispose();
			Client.this.hostname = hostname;
			Client.this.port = port;
			Client.this.start();
			queueReaderThread.start();
		}

		@Override
		public void cancel() {
			Client.this.cdlg.setVisible(false);
			Client.this.cdlg.dispose();
			Client.this.hostname = null;
		}
	};
	
	public void replay()  {
		try {
			System.out.println("Replaying old log");
			InputStream is = FileTerminalLogWriter.replayLastLog();
			// queue reader is not started yet, so we look at the thing by hand
			// try parsing after every byte to avoid missing anything
			byte[] replay = new byte[3];
			for (int c = is.read(replay); c != -1; c = is.read(replay)) {
				control.processBytes(replay, c);
				boolean[] dirtyLines = control.checkDirtyData();
				if (dirtyLines != null)
					fireDataListeners(dirtyLines);
			}
			System.out.println("Done replaying");
		} catch (IOException e) {
			// whatever
		}
		
	}
		
	public void begin() {
		if (hostname == null) {
			cdlg = new ConnectionDialog(cdlg_handler);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					cdlg.setVisible(true);
				}});
		} else {
			throw new IllegalStateException("Can't begin twice");
		}
	}
		
	private volatile byte[] chars = new byte[INPUT_BUFFER_SIZE];
	private final TerminalControl control;
	
	@Override
	public void run() {
		if (hostname == null)
			throw new IllegalStateException("Can't start before beginning");
		// else
		System.out.println("Connect to "+hostname+":"+port);
		try {
			Socket socket = new Socket(InetAddress.getByName(hostname), port);
			InputStream is = socket.getInputStream();
			
			for (int c = is.read(chars); c != -1; c = is.read(chars)) {
				control.processBytes(chars, c);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public interface DataListener {
		public void data(TextLine s, int lineNumber);
	}
	
	private List<DataListener> listeners = Collections.synchronizedList(new ArrayList<DataListener>());
	
	public void addListener(DataListener listener) {
		listeners.add(listener);
	}
	
	private void fireDataListeners(boolean[] dirtyLines) {
		int sz = control.size();
		int cap = Math.min(sz, 24);
		for (int i = 0; i < cap; i++) {
			if (dirtyLines[i]) {
				TextLine line = control.getLine(sz-(i+1));
				for(DataListener l : listeners) {
					l.data(line, i);
				}
			}
		}
	}
	
	private Thread queueReaderThread = new Thread("Queue reader") {
		@Override
		public void run() {
			for(;;) {
				boolean[] dirtyLines = control.waitForData();
				fireDataListeners(dirtyLines);
			}
		}
	};
}
