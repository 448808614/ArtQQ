package org.artqq.utils.bytes;

import java.util.Arrays;

/**
 * @author luoluo
 */
public class _ByteBytes {
    public byte[] obj;

    public byte[] toByteArray(){
        return obj;
    }

    public ByteObject toByteObject(){
        return new ByteObject(obj);
    }



    @Override
    public String toString() {
        return new String(obj);
    }

}

