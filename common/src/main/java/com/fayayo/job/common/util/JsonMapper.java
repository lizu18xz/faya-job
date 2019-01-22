package com.fayayo.job.common.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

@Slf4j
public class JsonMapper {


    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // support  解决  字段不匹配的实体对象
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * Object => String
     *
     * @param src
     * @return
     */
    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }

        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.info("parse Object to String error", e);
            return null;
        }
    }

    /**
     * Object => byte[]
     *
     * @param src
     * @return
     */
    public static <T> byte[] obj2Byte(T src) {
        if (src == null) {
            return null;
        }

        try {
            return src instanceof byte[] ? (byte[]) src : objectMapper.writeValueAsBytes(src);
        } catch (Exception e) {
            log.info("parse Object to byte[] error", e);
            return null;
        }
    }

    /**
     * String => Object
     *
     * @param str
     * @param clazz
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (str == null || clazz == null) {
            return null;
        }
        try {
            if (clazz == int.class || clazz == Integer.class) {
                return (T) Integer.valueOf(str);
            } else if (clazz == long.class || clazz == Long.class) {
                return (T) Long.valueOf(str);
            } else {
                return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
            }
        } catch (Exception e) {
            log.info("parse String to Object error, String:{}, Class<T>:{}, error:{}", str, clazz.getName(), e);
            return null;
        }
    }

    /**
     * byte[] => Object
     *
     * @param bytes
     * @param clazz
     * @return
     */
    public static <T> T byte2Obj(byte[] bytes, Class<T> clazz) {
        if (bytes == null || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(byte[].class) ? (T) bytes : objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            log.info("parse byte[] to Object error, byte[]:{}, Class<T>:{}, error:{}", bytes, clazz.getName(), e);
            return null;
        }
    }

    /**
     * String => Object
     *
     * @param str
     * @param typeReference
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (str == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.info("parse String to Object error, String:{}, TypeReference<T>:{}, error:{}", str, typeReference.getType(), e);
            return null;
        }
    }

    /**
     * byte[] => Object
     *
     * @param bytes
     * @param typeReference
     * @return
     */
    public static <T> T byte2Obj(byte[] bytes, TypeReference<T> typeReference) {
        if (bytes == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(byte[].class) ? bytes : objectMapper.readValue(bytes, typeReference));
        } catch (Exception e) {
            log.info("parse byte[] to Object error, byte[]:{}, TypeReference<T>:{}, error:{}", bytes, typeReference.getType(), e);
            return null;
        }
    }
}
