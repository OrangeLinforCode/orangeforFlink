package com.orange.lin.learn.algorithm.basis;

public class traverseUtils {



    public static void traverse1(ListNode head){
        for(ListNode p =head;p!=null;p=p.next){
            //遍历逻辑
            System.out.println(p.val);
        }
    }

    public static void traverse2(ListNode head){
        traverse2(head.next);
    }


    public static void traverseTreeNode(TreeNode root){
        traverseTreeNode(root.left);
        traverseTreeNode(root.right);
    }
}
