package com.yellow5a5.crashanalysis.monitor;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/5/29.
 */

public class FixedQueue<E> {

    private LinkedList<E> list;

    private int fixedCount = 1;

    public FixedQueue(int count){
        if (count < 1){
            count = 1;
        }
        fixedCount = count;
        list = new LinkedList<E>();
    }

    /**
     * push the element to list.
     * @param e
     */
    public void push(E e){
        list.add(e);
        if (list.size() > fixedCount){
            list.removeFirst();
        }
    }

    /**
     * get the recently element.
     * @return
     */
    public E get(){
        if (list.size() > 0){
            return list.getLast();
        } else {
            return null;
        }
    }

    /**
     * pick the recently from list.
     * @param e
     * @return
     */
    public E pick(E e){
        if (list.size() > 0){
            return list.getFirst();
        } else {
            return null;
        }
    }

    public boolean isEmpty(){
        if (list == null || list.size() == 0){
            return true;
        } else {
            return false;
        }
    }
}
