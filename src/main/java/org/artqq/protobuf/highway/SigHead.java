package org.artqq.protobuf.highway;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.artqq.utils.HexUtil;

public class SigHead {
    @Protobuf(fieldType = FieldType.UINT32, order = 1)
    public Integer serviceid = null;

    @Protobuf(fieldType = FieldType.UINT64, order = 2)
    public long filesize;

    @Protobuf(fieldType = FieldType.UINT64, order = 3)
    public long dataoffset = 0;

    @Protobuf(fieldType = FieldType.UINT64, order = 4)
    public long datalength;

    @Protobuf(fieldType = FieldType.UINT32, order = 5)
    public Integer rtcode = null;

    @Protobuf(fieldType = FieldType.BYTES, order = 6)
    public byte[] serviceticket = HexUtil.hexStringToByte("A60831322875C13CDE2E7BD0F9612FB9FA35720EE35BFE0F8C8AAD69F0486245050CE5F3975B0298BF62678A6D7B5CEB749457F7AAF182C1C989D4EF4F9EFC162AC581E4E9D4E8E22DA064E59C07B77A9BD5691546FEE4C1E9EF632BAFF9482469F6D60BDA536A27");

    @Protobuf(fieldType = FieldType.UINT32, order = 7)
    public Integer flag = null;

    @Protobuf(fieldType = FieldType.BYTES, order = 8)
    public byte[] md5;

    @Protobuf(fieldType = FieldType.BYTES, order = 9)
    public byte[] file_md5;

    @Protobuf(fieldType = FieldType.INT64, order = 10)
    public Long cacheAddr = null;

    @Protobuf(fieldType = FieldType.INT64, order = 11)
    public Long queryTimes = null;

    @Protobuf(fieldType = FieldType.INT64, order = 12)
    public Long updateCacheip = null;
}

