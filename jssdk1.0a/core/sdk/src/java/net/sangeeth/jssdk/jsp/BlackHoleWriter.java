package net.sangeeth.jssdk.jsp;

import java.io.IOException;
import java.io.Writer;

public class BlackHoleWriter extends Writer {

	public BlackHoleWriter() {
	}
	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	}

}
