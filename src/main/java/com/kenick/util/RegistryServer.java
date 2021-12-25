package com.kenick.util;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * author: zhanggw
 * 创建时间:  2021/12/17
 */
public class RegistryServer {

    public static void main(String[] args) throws Exception {
        User user = new UserImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("user", user);
        System.out.println("rmi running....");
    }

}
