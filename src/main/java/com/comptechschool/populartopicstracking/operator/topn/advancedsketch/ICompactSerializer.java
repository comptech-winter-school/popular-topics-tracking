package com.comptechschool.populartopicstracking.operator.topn.advancedsketch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public interface ICompactSerializer<T> {


    void serialize(T t, DataOutputStream dos) throws IOException;


    T deserialize(DataInputStream dis) throws IOException;
}
