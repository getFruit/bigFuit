package com.get.fruit.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private String content;//��������  

    private Integer point;//���֣���
    
    private User user;//���۵��û���Pointer���ͣ�һ��һ��ϵ

    private Fruit fruit; //�����۵�ˮ��
}