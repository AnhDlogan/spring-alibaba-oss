package xyz.dassiorleando.springalibabaoss.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by huyvv
 * Date: 17/01/2020
 * Time: 10:04 AM
 * for all issues, contact me: huyvv@vnpt-technology.vn
 **/
public class ObjectMapperUtil {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtil.class);

    private ObjectMapperUtil(){

    }

    public static <T> T objectMapper(String json, Class<?> type){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Object o =  mapper.readValue(json, type);
            return (T) o;
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.toString());
            return null;
        }
    }

    public static <T> List<T> listMapper(String json, Class<?> type){
        try{
            ObjectMapper mapper = new ObjectMapper();
            List<Object> o = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
            return (List<T>) o;
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    public static <T> String toJsonString(T object){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            return "";
        }
    }
}