package com.example.climbxpert;


public class SensorBuffer {

	private int bufSize;
	
	private int index;
	
	private float[] buffer;
	
	public SensorBuffer(int bufferSize)
	{
		bufSize = (bufferSize>0 ? bufferSize : 1);
		index = 0;
		buffer = new float[bufSize];
		
	}
	
	public void addSensorData(float data)
	{
		buffer[index] = data;
		
		index = (index<bufSize-1 ? index + 1 : 0);
	}
	
	
	public float getAvarageData()
	{
		float avData = 0;
		
		for (float data : buffer)
		{
			avData += data / bufSize;
		}
		
		return avData;
		
	}
}
