package com.yellow5a5.crashanalysis.core;

/**
 * Created by Yellow5A5 on 17/5/17.
 */

public interface IEncryptionCallback {

    String onEncryptionAlgorithm(String origin);

    String onDecryptionAalgorithm(String cipher);

}
