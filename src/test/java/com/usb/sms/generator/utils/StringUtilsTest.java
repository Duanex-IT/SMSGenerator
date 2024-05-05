package com.usb.sms.generator.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Sergey on 22.07.2015.
 */
public class StringUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(StringUtilsTest.class);
    @Test
    public void testInterpolate(){
        String template = "Vam zarahovano #{sum} #{currency}";
        Map<String, Object> values = new HashMap<>();
        values.put("sum", new Double(9.16d));
        values.put("currency", "UAH");
        String text = StringUtils.interpolate(template, values);
        logger.info("Template text: {} \n Result text: {}", template, text);
        assertNotNull(text);
        assertNotEquals("Invalid result text", template, text);
        assertTrue("Vam zarahovano 9,16 UAH".equals(text));
        values.clear();
        template = "Vam zarahovano";
        values.put("sum", 9.16d);
        values.put("currency", "UAH");
        text = StringUtils.interpolate(template, values);
        logger.info("Template text: {} \n Result text: {}", template, text);
        assertNotNull(text);
        assertEquals("Invalid result text", template, text);
        assertTrue("Vam zarahovano".equals(text));
        values.clear();
        template = "Vam zarahovano #{sum} #{currency}";
        values.put("sum1", 9.16d);
        values.put("currency", "UAH");
        text = StringUtils.interpolate(template, values);
        logger.info("Template text: {} \n Result text: {}", template, text);
        assertNotNull(text);
        assertNotEquals("Invalid result text", template, text);
        assertTrue("Vam zarahovano  UAH".equals(text));

        values.clear();
        template = "Vam zarahovano #{sum} #{currency}";
        values.put("sum1", 9.16d);
        values.put("currency", null);
        values.put("currency1", null);
        text = StringUtils.interpolate(template, values);
        logger.info("Template text: {} \n Result text: {}", template, text);
        assertNotNull(text);
        assertNotEquals("Invalid result text", template, text);
        assertTrue("Vam zarahovano  ".equals(text));
    }

    @Test
    public void testGetTemplateVars(){
        String template = "Vam zarahovano #{sum} #{currency}";
        List<String> vars = StringUtils.getTemplateVars(template);
        logger.debug("Vars: {}", org.springframework.util.StringUtils.collectionToCommaDelimitedString(vars));
        assertTrue("Variables in template not found", !vars.isEmpty());
        template = "Vam zarahovano";
        vars = StringUtils.getTemplateVars(template);
        assertTrue("Variables in template must be empty", vars.isEmpty());
    }
}
