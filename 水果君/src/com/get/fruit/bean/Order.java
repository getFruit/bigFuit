package com.get.fruit.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

	
    public Order() {
		super();
	}


    //private List<Fruit> fruits;//һ�����
    private User user;
    private Fruit fruit;
    private State state;//����״̬
    private SendWay sendway;//���ͷ�ʽ
    private Float sum;//���
    private PayWay payway;//֧����ʽ
    private Boolean pay;//�Ƿ�֧��
    private UserAdress consignee;//�ջ���
    private String meggenger;//����
    
    
    public  enum State{
    	
    	�����µ�,�ȴ�֧��,֧��ʧ��,�ȴ�����,�ѷ���,�������,���׹ر�
    }
    
    public enum SendWay{
    	�ͻ�����,��ȡ,���
    }
    public enum PayWay{
    	֧����,΢��֧��
    }
    
    
    
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Fruit getFruit() {
		return fruit;
	}
	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public SendWay getSendway() {
		return sendway;
	}
	public void setSendway(SendWay sendway) {
		this.sendway = sendway;
	}
	public Float getSum() {
		return sum;
	}
	public void setSum(Float sum) {
		this.sum = sum;
	}
	public PayWay getPayway() {
		return payway;
	}
	public void setPayway(PayWay payway) {
		this.payway = payway;
	}
	public Boolean getPay() {
		return pay;
	}
	public void setPay(Boolean pay) {
		this.pay = pay;
	}
	public UserAdress getConsignee() {
		return consignee;
	}
	public void setConsignee(UserAdress consignee) {
		this.consignee = consignee;
	}
	public String getMeggenger() {
		return meggenger;
	}
	public void setMeggenger(String meggenger) {
		this.meggenger = meggenger;
	}
    
    
    
    
    
}