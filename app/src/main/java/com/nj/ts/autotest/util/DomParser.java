package com.nj.ts.autotest.util;

import com.nj.ts.autotest.entity.Config;
import com.nj.ts.autotest.entity.Module;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ts on 17-9-28.
 */

public class DomParser {
    private static final String TAG = "DomParser";
    //返回配置信息
    public static List<Config> getConfig(InputStream is) throws ParserConfigurationException, IOException, SAXException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例
        Document doc = builder.parse(is);   //解析输入流 得到Document实例
        List<Config> configs = new ArrayList<>();
        NodeList items = doc.getElementsByTagName("project");
        for(int i=0;i < items.getLength();i++){
            Element e = (Element)items.item(i);
            Config c =new Config();
            String []name = e.getFirstChild().getNodeValue().split("\n");
            c.setProjectName(name[0]);
            List<Module> modules = new ArrayList<>();
            NodeList module_items = e.getElementsByTagName("module");
            for(int j=0;j<module_items.getLength();j++){
                Element me =(Element) module_items.item(j);
                Module module = new Module();
                module.setName(me.getFirstChild().getNodeValue());
                modules.add(module);
            }
            c.setModule(modules);
            configs.add(c);
        }

//        for(int i =0;i<configs.size();i++){
//            List<Module> modules = new ArrayList<>();
//            int len =
//        }

        return configs;
    }
}
