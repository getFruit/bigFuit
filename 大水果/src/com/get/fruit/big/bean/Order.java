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
    private State state;//订单状态
    private SendWay sendway;//派送方式
    private Double sum;//金额
    private Boolean pay;//是否支付
    
    //private UserAdress consignee;//收货人
    private String name;//收货人姓名
    private String phone;
    private String address;
    
    private String messenger;//留言
    private String orderid;
    
    public  enum State{
    	
    	正在下单,等待支付,等待发货,交易完成,交易关闭
    }
    
    public enum SendWay{
    	送货上门,自取,快递
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