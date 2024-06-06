package com.bin.im.common.mini.network;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import static java.net.StandardSocketOptions.SO_RCVBUF;
import static java.net.StandardSocketOptions.SO_REUSEADDR;

public final class ServerSocketSettings  {
	public static final int DEFAULT_BACKLOG = 16384;

	private static final byte DEF_BOOL = -1;
	private static final byte TRUE = 1;
	private static final byte FALSE = 0;

	private final int backlog;
	private final int receiveBufferSize;
	private final byte reuseAddress;

	// region builders
	private ServerSocketSettings(int backlog, int receiveBufferSize, byte reuseAddress) {
		this.backlog = backlog;
		this.receiveBufferSize = receiveBufferSize;
		this.reuseAddress = reuseAddress;
	}

	public static ServerSocketSettings create() {
		return create(150);
	}


	public static ServerSocketSettings create(int backlog) {
		return new ServerSocketSettings(backlog, 0, DEF_BOOL);
	}

	public ServerSocketSettings withBacklog(int backlog) {
		return new ServerSocketSettings(backlog, receiveBufferSize, reuseAddress);
	}

	public ServerSocketSettings withReceiveBufferSize(int receiveBufferSize) {
		return new ServerSocketSettings(backlog, receiveBufferSize, reuseAddress);
	}

	public ServerSocketSettings withReuseAddress(boolean reuseAddress) {
		return new ServerSocketSettings(backlog, receiveBufferSize, reuseAddress ? TRUE : FALSE);
	}
	// endregion

	public void applySettings(ServerSocketChannel channel) throws IOException {
		if (receiveBufferSize != 0) {
			channel.setOption(SO_RCVBUF, receiveBufferSize);
		}
		if (reuseAddress != DEF_BOOL) {
			channel.setOption(SO_REUSEADDR, reuseAddress != FALSE);
		}
	}

	public int getBacklog() {
		return backlog;
	}

	public boolean hasReceiveBufferSize() {
		return receiveBufferSize != 0;
	}


	public int getReceiveBufferSizeBytes() {
		return receiveBufferSize;
	}

	public boolean hasReuseAddress() {
		return reuseAddress != DEF_BOOL;
	}

	public boolean getReuseAddress() {
		return reuseAddress != FALSE;
	}
}