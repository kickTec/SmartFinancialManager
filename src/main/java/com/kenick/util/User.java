package com.kenick.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * author: zhanggw
 * 创建时间:  2021/12/17
 */
public interface User extends Remote {

    void work(Object obj) throws RemoteException;

}
