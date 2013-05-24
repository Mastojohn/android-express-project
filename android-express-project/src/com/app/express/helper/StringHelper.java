package com.app.express.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringHelper {
	
	/**
	 * Convert StringBuffer to InputStream.
	 * @param buf
	 * @return InputStream
	 */
	public static InputStream fromStringBuffer(StringBuffer buf) {
		return new ByteArrayInputStream(buf.toString().getBytes());
	}
}
