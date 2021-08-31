package example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;


import static org.junit.jupiter.api.Assertions.*;

public class ByteBufTest {

  @Test
  public void test() {
    ByteBuf buf = Unpooled.wrappedBuffer(new byte[] { '\0' });
    assertFalse(buf.isWritable());

    Charset utf8 = Charset.forName("UTF-8");
    ByteBuf buf2 = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

    // predicate
    assertTrue(buf2.isReadable());
    assertTrue(buf2.isWritable());

    // Read
    assertEquals('N', buf2.readByte());
    assertEquals('e', buf2.readByte());

    // write
    buf2.writeByte('Z');

    // Read after write
    assertEquals('N', buf2.getByte(0));
  }
}
