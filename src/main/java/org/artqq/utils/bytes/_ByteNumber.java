package org.artqq.utils.bytes;

/**
 * @author luoluo
 */
public class _ByteNumber {

    public Double obj = 123.0;

    public int toInt(){
        return obj.intValue();
    }

    public double toDouble(){
        return obj;
    }

    public long toLong(){
        return obj.longValue();
    }

    public byte toByte(){
        return obj.byteValue();
    }

    @Override
    public String toString() {
        return "" + obj;
    }
}

