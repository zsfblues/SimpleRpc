package com.rpc.common.protocol;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created on 2017/5/29.
 *
 * @author zhoushengfan
 */
public class RpcProtocol {
    public class RpcPosterEncoder extends MessageToByteEncoder<Object> {

        private Class<?> genericClass;

        public RpcPosterEncoder(Class<?> genericClass){
            this.genericClass = genericClass;
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) throws Exception {
            if (genericClass.isInstance(obj)) {
                byte[] data = SerializationUtil.serialize(obj);
                out.writeInt(data.length);   //先将消息长度写入，也就是消息头
                out.writeBytes(data);       //消息体中包含我们要发送的数据
            }
        }
    }

    public class RpcPosterDecoder extends ByteToMessageDecoder {

        private Class<?> genericClass;

        public RpcPosterDecoder(Class<?> genericClass){
            this.genericClass = genericClass;
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            if (in.readableBytes() >= 4){
                int dataLength = in.readInt();
                if (dataLength <= 0 || in.readableBytes() < dataLength){
                    return;
                }
                byte[] body = new byte[dataLength];
                in.readBytes(body);
                out.add(SerializationUtil.deserialize(body, genericClass));
            }
        }
    }

    private static class SerializationUtil {
        private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
        private static Objenesis objenesis = new ObjenesisStd(true);

        static <T> byte[] serialize(T obj){
            Class<T> clazz = (Class<T>) obj.getClass();
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            try {
                Schema<T> schema = getCachedSchema(clazz);
                return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            } finally {
                buffer.clear();
            }
        }

        static <T> T deserialize(byte[] data, Class<T> clazz) {
            try {
                //绕过类的构造函数实例化一个对象，用于在缺少无参构造函数不会抛出异常
                T message = objenesis.newInstance(clazz);
                Schema<T> schema = getCachedSchema(clazz);
                ProtostuffIOUtil.mergeFrom(data, message, schema);
                return message;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        static <T> Schema<T> getCachedSchema(Class<T> clazz){
            Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
            if (schema == null){
                schema = RuntimeSchema.createFrom(clazz);
                cachedSchema.put(clazz, schema);
            }
            return schema;
        }
    }
}
