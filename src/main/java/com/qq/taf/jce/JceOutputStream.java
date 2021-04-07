package com.qq.taf.jce;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.artqq.utils.HexUtil;

public class JceOutputStream
{
  private ByteBuffer bs;
  
  public JceOutputStream(ByteBuffer bs)
  {
    this.bs = bs;
  }
  
  public JceOutputStream(int capacity)
  {
    this.bs = ByteBuffer.allocate(capacity);
  }
  
  public JceOutputStream()
  {
    this(128);
  }
  
  public ByteBuffer getByteBuffer()
  {
    return this.bs;
  }
  
  public byte[] toByteArray()
  {
    byte[] newBytes = new byte[this.bs.position()];
    System.arraycopy(this.bs.array(), 0, newBytes, 0, this.bs.position());
    return newBytes;
  }
  
  public void reserve(int len) {
    if (this.bs.remaining() < len) {
      int n = (this.bs.capacity() + len) * 2;
      ByteBuffer bs2 = ByteBuffer.allocate(n);
      bs2.put(this.bs.array(), 0, this.bs.position());
      this.bs = bs2;
    }
  }
  
  public void writeHead(byte type, int tag)
  {
    if (tag < 15)
    {
      byte b = (byte)(tag << 4 | type);
      this.bs.put(b);
    }
    else if (tag < 256)
    {
      byte b = (byte)(0xF0 | type);
      this.bs.put(b);
      this.bs.put((byte)tag);
    }
    else
    {
      throw new JceEncodeException("tag is too large: " + tag);
    }
  }
  
  public void write(boolean b, int tag)
  {
    byte by = (byte)(b ? 1 : 0);
    write(by, tag);
  }
  
  public void write(byte b, int tag)
  {
    reserve(3);
    if (b == 0)
    {
      writeHead((byte)12, tag);
    }
    else
    {
      writeHead((byte)0, tag);
      this.bs.put(b);
    }
  }
  
  public void write(short n, int tag)
  {
    reserve(4);
    if ((n >= -128) && (n <= 127))
    {
      write((byte)n, tag);
    }
    else
    {
      writeHead((byte)1, tag);
      this.bs.putShort(n);
    }
  }
  
  public void write(int n, int tag) {
    reserve(6);
    if(n==0){
        writeHead((byte)12, tag);
    }else {
      writeHead((byte)2, tag);
      this.bs.putInt(n);
    }
  }
  
  public void write(long n, int tag)
  {
    reserve(10);
    if ((n >= -2147483648L) && (n <= 2147483647L))
    {
      write((int)n, tag);
    }
    else
    {
      writeHead((byte)3, tag);
      this.bs.putLong(n);
    }
  }
  
  public void write(float n, int tag)
  {
    reserve(6);
    writeHead((byte)4, tag);
    this.bs.putFloat(n);
  }
  
  public void write(double n, int tag)
  {
    reserve(10);
    writeHead((byte)5, tag);
    this.bs.putDouble(n);
  }
  
  public void writeStringByte(String s, int tag) {
    byte[] by = HexUtil.HexString2Bytes(s);
    reserve(10 + by.length);
    if (by.length > 255)
    {
      writeHead((byte)7, tag);
      this.bs.putInt(by.length);
      this.bs.put(by);
    }
    else
    {
      writeHead((byte)6, tag);
      this.bs.put((byte)by.length);
      this.bs.put(by);
    }
  }
  
  public void writeByteString(String s, int tag)
  {
    reserve(10 + s.length());
    byte[] by = HexUtil.HexString2Bytes(s);
    if (by.length > 255)
    {
      writeHead((byte)7, tag);
      this.bs.putInt(by.length);
      this.bs.put(by);
    }
    else
    {
      writeHead((byte)6, tag);
      this.bs.put((byte)by.length);
      this.bs.put(by);
    }
  }
  
  public void write(String s, int tag)
  {
    byte[] by;
    try
    {
      by = s.getBytes(this.sServerEncoding);
    }
    catch (UnsupportedEncodingException e)
    {
      by = s.getBytes();
    }
    reserve(10 + by.length);
    if (by.length > 255)
    {
      writeHead((byte)7, tag);
      this.bs.putInt(by.length);
      this.bs.put(by);
    }
    else
    {
      writeHead((byte)6, tag);
      this.bs.put((byte)by.length);
      this.bs.put(by);
    }
  }
  
  public <K, V> void write(Map<K, V> m, int tag)
  {
    reserve(8);
    writeHead((byte)8, tag);
    write(m == null ? 0 : m.size(), 0);
    if (m != null) {
      for (Entry<K, V> en : m.entrySet())
      {
        write(en.getKey(), 0);
        write(en.getValue(), 1);
      }
    }
  }
  
  public void write(boolean[] l, int tag)
  {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    boolean[] arrayOfBoolean;
    int j = (arrayOfBoolean = l).length;
    for (int i = 0; i < j; i++)
    {
      boolean e = arrayOfBoolean[i];
      write(e, 0);
    }
  }
  
  public void write(byte[] l, int tag)
  {
    reserve(8 + l.length);
    writeHead((byte)13, tag);
    writeHead((byte)0, 0);
    write(l.length, 0);
    this.bs.put(l);
  }
  
  public void write(short[] l, int tag)
  {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    short[] arrayOfShort;
    int j = (arrayOfShort = l).length;
    for (int i = 0; i < j; i++)
    {
      short e = arrayOfShort[i];
      write(e, 0);
    }
  }
  
  public void write(int[] l, int tag)
  {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    int[] arrayOfInt;
    int j = (arrayOfInt = l).length;
    for (int i = 0; i < j; i++)
    {
      int e = arrayOfInt[i];
      write(e, 0);
    }
  }
  
  public void write(long[] l, int tag)
  {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    long[] arrayOfLong;
    int j = (arrayOfLong = l).length;
    for (int i = 0; i < j; i++)
    {
      long e = arrayOfLong[i];
      write(e, 0);
    }
  }
  
  public void write(float[] l, int tag)
  {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    float[] arrayOfFloat;
    int j = (arrayOfFloat = l).length;
    for (int i = 0; i < j; i++)
    {
      float e = arrayOfFloat[i];
      write(e, 0);
    }
  }
  
  public void write(double[] l, int tag) {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    double[] arrayOfDouble;
    int j = (arrayOfDouble = l).length;
    for (int i = 0; i < j; i++) {
      double e = arrayOfDouble[i];
      write(e, 0);
    }
  }
  
  public <T> void write(T[] l, int tag)
  {
    writeArray(l, tag);
  }
  
  private void writeArray(Object[] l, int tag) {
    reserve(8);
    writeHead((byte)9, tag);
    write(l.length, 0);
    Object[] arrayOfObject;
    int j = (arrayOfObject = l).length;
    for (int i = 0; i < j; i++) {
      Object e = arrayOfObject[i];
      write(e, 0);
    }
  }
  
  public <T> void write(Collection<T> l, int tag) {
    reserve(8);
    writeHead((byte)9, tag);
    write(l == null ? 0 : l.size(), 0);
    if (l != null) {
      for (T e : l) {
        write(e, 0);
      }
    }
  }
  
  public void write(JceStruct o, int tag) {
    reserve(2);
    writeHead((byte)10, tag);
    o.writeTo(this);
    reserve(2);
    writeHead((byte)11, 0);
  }
  
  public void write(Byte o, int tag)
  {
    write(o.byteValue(), tag);
  }
  
  public void write(Boolean o, int tag)
  {
    write(o.booleanValue(), tag);
  }
  
  public void write(Short o, int tag)
  {
    write(o.shortValue(), tag);
  }
  
  public void write(Integer o, int tag)
  {
    write(o.intValue(), tag);
  }
  
  public void write(Long o, int tag)
  {
    write(o.longValue(), tag);
  }
  
  public void write(Float o, int tag)
  {
    write(o.floatValue(), tag);
  }
  
  public void write(Double o, int tag)
  {
    write(o.doubleValue(), tag);
  }
  
  public void write(Object o, int tag) {
    if ((o instanceof Byte)) {
      write(((Byte)o).byteValue(), tag);
    } else if ((o instanceof Boolean)) {
      write(((Boolean)o).booleanValue(), tag);
    } else if ((o instanceof Short)) {
      write(((Short)o).shortValue(), tag);
    } else if ((o instanceof Integer)) {
      write(((Integer)o).intValue(), tag);
    } else if ((o instanceof Long)) {
      write(((Long)o).longValue(), tag);
    } else if ((o instanceof Float)) {
      write(((Float)o).floatValue(), tag);
    } else if ((o instanceof Double)) {
      write(((Double)o).doubleValue(), tag);
    } else if ((o instanceof String)) {
      write((String)o, tag);
    } else if ((o instanceof Map)) {
      write((Map)o, tag);
    } else if ((o instanceof List)) {
      write((List)o, tag);
    } else if ((o instanceof JceStruct)) {
      write((JceStruct)o, tag);
    } else if ((o instanceof byte[])) {
      write((byte[])o, tag);
    } else if ((o instanceof boolean[])) {
      write((boolean[])o, tag);
    } else if ((o instanceof short[])) {
      write((short[])o, tag);
    } else if ((o instanceof int[])) {
      write((int[])o, tag);
    } else if ((o instanceof long[])) {
      write((long[])o, tag);
    } else if ((o instanceof float[])) {
      write((float[])o, tag);
    } else if ((o instanceof double[])) {
      write((double[])o, tag);
    } else if (o.getClass().isArray()) {
      writeArray((Object[])o, tag);
    } else if ((o instanceof Collection)) {
      write((Collection)o, tag);
    } else {
      throw new JceEncodeException(
        "write object error: unsupport type. " + o.getClass());
    }
  }
  
  protected String sServerEncoding = "GBK";
  
  public int setServerEncoding(String se) {
    this.sServerEncoding = se;
    return 0;
  }

}
