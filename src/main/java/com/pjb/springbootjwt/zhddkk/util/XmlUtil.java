package com.pjb.springbootjwt.zhddkk.util;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtil {
    /**
     * 将对象结构体转换为xml字符串 注意:转换后的xml字符串中不包含xml头.
     *
     * @param object 对象
     * @return xml对象
     */
    public static String object2Xml(Object object) {
        String result = null;
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller mashal = context.createMarshaller();
            mashal.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mashal.setProperty(Marshaller.JAXB_FRAGMENT, true);
            mashal.marshal(object, writer);
            
            result = new String(writer.getBuffer());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 将xml字符串转换为对象结构体.
     *
     * @param xmlStr xml字符串
     * @param c 转换类对象
     * @return 转换后的结构体
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(String xmlStr, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return t;
    }
}
