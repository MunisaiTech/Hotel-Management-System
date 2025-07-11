package com.munisai;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Utility {
    public static SessionFactory sf;
    static {
        try{
            sf=new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static SessionFactory getSessionFactory(){
        return sf;
    }
}
