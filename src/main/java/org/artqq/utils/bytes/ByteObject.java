package org.artqq.utils.bytes;

/**
 * 一瞬冻千秋
 * @author 伏秋洛
 * @creed: 对于number，bytes的一次性扩展
 * @date 2020/6/6 18:15
 */
public class ByteObject extends ByteFactory {

    public ByteObject(byte[] _data){
        data = _data;
    }

    public ByteObject(){
        init();
    }

    public void WriteStringAndLen(String _data) {
        WriteBytesAndLen(_data.getBytes(), 0);
    }
    
    public void WriteStringAndLen(String _data, int add) {
        WriteBytesAndLen(_data.getBytes(), add);
    }
    
    public void WriteBytesAndLen(byte[] _data) {
        WriteBytesAndLen(_data, 0);
    }

    public void WriteBytesAndLen(byte[] _data, int add) {
        if(_data == null){
            _data = new byte[0];
        }
        WriteInt(add+_data.length);
        WriteBytes(_data);   
    }

    public void WriteBytesAndShortLen(byte[] _data) {
        WriteBytesAndShortLen(_data, 0);
    }

    public void WriteStringAndShortLen(String _data) {
        WriteBytesAndShortLen(_data.getBytes(), 0);
    }
    
    public void WriteStringAndShortLen(String _data, int add) {
        WriteBytesAndShortLen(_data.getBytes(), add);
    }
    
    public void WriteBytesAndShortLen(byte[] _data, int add) {
        if(_data == null){
            _data = new byte[0];
        }
        WriteShort(add+_data.length);
        WriteBytes(_data);   
    }
    
    public _ByteNumber readShortNumber() {
        int a = super.readShort();
        _ByteNumber n = new _ByteNumber();
        n.obj = Double.valueOf(a);
        return n;
    }

    public _ByteNumber readByteNumber() {
        int a = super.readByte() & 0xff;
        _ByteNumber n = new _ByteNumber();
        n.obj = Double.valueOf(a);
        return n;
    }
    
    public _ByteNumber readIntNumber() {
        long a = super.readInt();
        _ByteNumber n = new _ByteNumber();
        n.obj = Double.valueOf(a);
        return n;
    }

    public _ByteBytes readStringObj(long length){
        String src = readString(length);
        if(src == null)
            return null;
        _ByteBytes b = new _ByteBytes();
        b.obj = src.getBytes();
        return b;
    }

    public _ByteBytes readBytesObj(long length){
        byte[] src = readBytes(length);
        if(src == null)
            return null;
        _ByteBytes b = new _ByteBytes();
        b.obj = src;
        return b;
    }

    public ByteObject disUseData(int len){
        readBytes(len);
        return this;
    }

    public ByteObject init(){
        return this;
    };

	public ByteObject WriteString(byte[] bs) {
	    WriteBytes(bs);
	    return this;
	}
}

