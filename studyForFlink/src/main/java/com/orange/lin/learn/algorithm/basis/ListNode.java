package com.orange.lin.learn.algorithm.basis;

public class ListNode {
    int val;
    ListNode next;

    public void traverse1(ListNode head){
        for(ListNode p =head;p!=null;p=p.next){
            //遍历逻辑
            System.out.println(p.val);
        }
    }

    public void traverse2(ListNode head){
        traverse2(head.next);
    }
}
