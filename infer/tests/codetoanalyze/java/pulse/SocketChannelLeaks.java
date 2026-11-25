/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package codetoanalyze.java.infer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

// SocketChannel.open returns a fresh channel that owns a socket until close().
public class SocketChannelLeaks {

  public void socketChannelOpenNotClosedBad() throws IOException {
    SocketChannel.open();
  }

  public void socketChannelNotClosedAfterReadBad() throws IOException {
    SocketChannel channel = SocketChannel.open();
    ByteBuffer buffer = ByteBuffer.allocate(8);
    channel.read(buffer);
  }

  public void socketChannelNotClosedAfterConnectBad() throws IOException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(new InetSocketAddress("127.0.0.1", 9));
  }

  public void socketChannelFamilyNotClosedBad() throws IOException {
    SocketChannel.open(StandardProtocolFamily.INET);
  }

  public void socketChannelRemoteNotClosedBad() throws IOException {
    SocketChannel.open(new InetSocketAddress("127.0.0.1", 9));
  }

  public void socketChannelClosedOk() throws IOException {
    SocketChannel channel = SocketChannel.open();
    try {
      ByteBuffer buffer = ByteBuffer.allocate(8);
      channel.read(buffer);
    } finally {
      channel.close();
    }
  }

  public void socketChannelTryWithResourcesOk() throws IOException {
    try (SocketChannel channel = SocketChannel.open()) {
      ByteBuffer buffer = ByteBuffer.allocate(8);
      channel.read(buffer);
    }
  }

  public void socketChannelConnectTryWithResourcesOk() throws IOException {
    try (SocketChannel channel = SocketChannel.open()) {
      channel.connect(new InetSocketAddress("127.0.0.1", 9));
    }
  }

  public SocketChannel socketChannelReturnedOk() throws IOException {
    return SocketChannel.open();
  }

  public SocketChannel socketChannelConnectedReturnedOk() throws IOException {
    return SocketChannel.open(new InetSocketAddress("127.0.0.1", 9));
  }
}
