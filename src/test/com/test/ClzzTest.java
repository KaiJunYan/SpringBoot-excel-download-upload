package com.test;

import com.test.Model.CloudServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ClzzTest {

    @Test
    public void test() {
        CloudServer cloudServer = new CloudServer();
        cloudServer.setAdminUser("admin");
        cloudServer.setHostname("hostName");
        cloudServer.setInstanceName("instanceName");

        Class<? extends CloudServer> aClass = cloudServer.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field f : declaredFields) {
            List<String> titles = new ArrayList<>();
            titles.add(f.getName());
            f.setAccessible(true);
            try {
                System.out.println(f.getName());
                System.out.println(f.get(cloudServer));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testForeach() {
        List<String> stringList = new ArrayList<>();
        stringList.add("aaa");
        stringList.add("bbb");
        stringList.add("ccc");
        stringList.add("ddd");

        for (int i = 0; i < 3; i++) {
            for (String s : stringList) {
                System.out.println(s);
                if (s.equals("ccc")){
                    break;
                }
            }
        }
    }

}
