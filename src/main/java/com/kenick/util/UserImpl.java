package com.kenick.util;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * author: zhanggw
 * 创建时间:  2021/12/17
 */
public class UserImpl extends UnicastRemoteObject implements User {

    protected UserImpl() throws RemoteException {
    }

    @Override
    public void work(Object obj) throws RemoteException {
        System.out.println(obj.toString());
        System.out.println("work被调用了");
    }

}
