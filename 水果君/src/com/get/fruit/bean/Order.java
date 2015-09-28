package com.get.fruit.bean;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

	
    public Order() {
		super();
	}


    
    public Order(User user, Fruit fruit, Integer count) {
		super();
		this.user = user;
		this.fruit = fruit;
		this.count = count;
	}



	//private List<Fruit> fruits;//一单多果
    private User user;
    private Fruit fruit;
    private Integer count;
    private State state;//订单状态
    private SendWay sendway;//派送方式
    private Double sum;//金额
    private Boolean pay;//是否支付
    private UserAdress consignee;//收货人
    private String messenger;//留言
    private String orderid;
    
    public  enum State{
    	
    	正在下单,等待支付,支付失败,等待发货,已发货,交易完成,交易关闭
    }
    
    public enum SendWay{
    	送货上门,自取,快递
    }
    public enum PayWay{
    	支付宝,微信支付
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
	public UserAdress getConsignee() {
		return consignee;
	}
	public void setConsignee(UserAdress consignee) {
		this.consignee = consignee;
	}
	public String getMessenger() {
		return messenger;
	}
	public void setMessenger(String messenger) {
		this.messenger = messenger;
	}
    
    
    
    
    
}