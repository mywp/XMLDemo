package com.example.scorpio.xmldemo.tes;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;
import android.util.Xml;

import com.example.scorpio.xmldemo.Person;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scorpio on 16/1/12.
 */
public class TestCase extends AndroidTestCase {

    public void test() {
       // writeXmlTo();
        List<Person> personLlist=parserXmlFromLocal();
        for(Person person : personLlist){
            Log.i("TestCase",person.toString());
        }
    }

    //写xml文件到本地
    private void writeXmlTo() {
        List<Person> personList = getPersonList();

        //获得序列化对象
        XmlSerializer serializer = Xml.newSerializer();
        try {
            File path = new File(Environment.getExternalStorageDirectory(), "persons.xml");
            FileOutputStream fos = new FileOutputStream(path);
            //指定序列化对象输出的位置和编码
            serializer.setOutput(fos, "utf-8");

            serializer.startDocument("utf-8", true);//写开始<?xml version='1.0' encoding='utf-8' standalone='yes' ?>

            serializer.startTag(null, "persons");// <person>

            for (Person person : personList) {
                //开始写person
                serializer.startTag(null, "person");//<person>
                serializer.attribute(null, "id", String.valueOf(person.getId()));

                //写名字
                serializer.startTag(null, "name");
                serializer.text(person.getName());
                serializer.endTag(null, "name");

                //写年龄
                serializer.startTag(null, "age");
                serializer.text(String.valueOf(person.getAge()));
                serializer.endTag(null, "age");


                serializer.endTag(null, "person");//<person>
            }

            serializer.endTag(null, "persons");//</person>


            serializer.endDocument();//结束

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Person> getPersonList() {
        List<Person> personList = new ArrayList<Person>();

        for (int i = 0; i < 30; i++) {
            personList.add(new Person(i, "wang" + i, 18 + i));
        }
        return personList;
    }

    private List<Person> parserXmlFromLocal() {
        try {
            File path = new File(Environment.getExternalStorageDirectory(), "persons.xml");
            FileInputStream fis = new FileInputStream(path);

            //获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            //指定解析的文本和编码格式
            parser.setInput(fis, "utf-8");

            int evenType = parser.getEventType();//获得事件类型

            List<Person> personList = null;
            Person person = null;
            String id;
            while (evenType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();//获得当前节点的名称

                switch (evenType) {
                    case XmlPullParser.START_TAG://当前等于节点<person>
                        if ("persons".equals(tagName)) {//<person>
                            personList = new ArrayList<Person>();
                        } else if ("person".equals(tagName)) {//<person id="1">
                            person = new Person();
                            id = parser.getAttributeValue(null, "id");
                            person.setId(Integer.valueOf(id));
                        } else if ("name".equals(tagName)) {//<name>
                            person.setName(parser.nextText());

                        } else if ("age".equals(tagName)) {//<age>
                            person.setAge(Integer.parseInt(parser.nextText()));

                        }
                        break;
                    case XmlPullParser.END_TAG://</persons>
                        if ("person".equals(tagName)) {
                            //需要把上面设置好的值的person对象添加到集合中
                            personList.add(person);
                        }
                        break;
                    default:
                        break;
                }
                evenType = parser.next();//获得下一个事件类型
            }
            return personList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
