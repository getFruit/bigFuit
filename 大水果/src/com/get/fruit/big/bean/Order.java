package com.get.fruit.big.bean;


import android.R.string;
import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

    public Order(User user, Fruit fruit, Integer count, double sum) {
		super();
		this.user = user;
		this.fruit = fruit;
		this.count = count;
		this.sum=sum;
	}



    /**
	 * 
	 */
	public Order() {
		super();
	}



	private User user;
    private Fruit fruit;
    private Integer count;
    private State state;//����״̬
    private SendWay sendway;//���ͷ�ʽ
    private Double sum;//���
    private Boolean pay;//�Ƿ�֧��
    
    //private UserAdress consignee;//�ջ���
    private String name;//�ջ�������
    private String phone;
    private String address;
    
    private String messenger;//����
    private String orderid;
    
    public  enum State{
    	
    	�����µ�,�ȴ�֧��,�ȴ�����,�������,���׹ر�
    }
    
    public enum SendWay{
    	�ͻ�����,��ȡ,���
    }
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Double getSum() {
		return sum;
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
	public Boolean getPay() {
		return pay;
	}
	public void setPay(Boolean pay) {
		this.pay = pay;
	}
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getMessenger() {
		return messenger;
	}
	public void setMessenger(String messenger) {
		this.messenger = messenger;
	}
    
    
    
    
    
}