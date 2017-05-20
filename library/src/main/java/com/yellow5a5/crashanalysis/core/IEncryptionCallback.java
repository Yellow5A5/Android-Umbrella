package com.yellow5a5.crashanalysis.core;

/**
 * Created by Yellow5A5 on 17/5/17.
 */

public interface IEncryptionCallback {

    /**
     * 加密方法
     * @param origin
     * @return
     */
    String onEncryptionAlgorithm(String origin);

    /**
     * 解密方法
     * @param cipher
     * @return
     */
    String onDecryptionAalgorithm(String cipher);

}
