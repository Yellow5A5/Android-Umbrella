package com.yellow5a5.crashanalysis.monitor;

/**
 * Created by Yellow5A5 on 17/3/4.
 */

public interface Subject {

    void registerObserver(IOrz obs);

    void removeObserver(IOrz obs);

    void notifyObservers();

}
