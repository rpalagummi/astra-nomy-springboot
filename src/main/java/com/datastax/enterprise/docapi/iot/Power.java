package com.datastax.yasa.docapi.iot;

public class Power {
	
	private static final long serialVersionUID = 2798538288964412234L;
	
	private String date;
	private String time;
	private String global_active_power;
	private String global_reactive_power;
	private String voltage;
	private String global_intensity;
	private String sub_metering_1;
	private String sub_metering_2;
	private String sub_metering_3;
	
	
	public Power(String date, String time, String global_active_power, String global_reactive_power, String voltage,
			String global_intensity, String sub_metering_1, String sub_metering_2, String sub_metering_3) {
		super();
		this.date = date;
		this.time = time;
		this.global_active_power = global_active_power;
		this.global_reactive_power = global_reactive_power;
		this.voltage = voltage;
		this.global_intensity = global_intensity;
		this.sub_metering_1 = sub_metering_1;
		this.sub_metering_2 = sub_metering_2;
		this.sub_metering_3 = sub_metering_3;
	}

	public Power() {
		
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getGlobal_active_power() {
		return global_active_power;
	}

	public void setGlobal_active_power(String global_active_power) {
		this.global_active_power = global_active_power;
	}

	public String getGlobal_reactive_power() {
		return global_reactive_power;
	}

	public void setGlobal_reactive_power(String global_reactive_power) {
		this.global_reactive_power = global_reactive_power;
	}

	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getGlobal_intensity() {
		return global_intensity;
	}
	public void setGlobal_intensity(String global_intensity) {
		this.global_intensity = global_intensity;
	}
	public String getSub_metering_1() {
		return sub_metering_1;
	}
	public void setSub_metering_1(String sub_metering_1) {
		this.sub_metering_1 = sub_metering_1;
	}
	public String getSub_metering_2() {
		return sub_metering_2;
	}
	public void setSub_metering_2(String sub_metering_2) {
		this.sub_metering_2 = sub_metering_2;
	}
	public String getSub_metering_3() {
		return sub_metering_3;
	}
	public void setSub_metering_3(String sub_metering_3) {
		this.sub_metering_3 = sub_metering_3;
	}
	

}
