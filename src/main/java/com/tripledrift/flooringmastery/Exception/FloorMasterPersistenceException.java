package com.tripledrift.flooringmastery.Exception;

public class FloorMasterPersistenceException extends Exception
{
    public FloorMasterPersistenceException(String msg){
        super(msg);
    }
    public FloorMasterPersistenceException(String msg, Throwable e){
        super(msg, e);
    }
}
