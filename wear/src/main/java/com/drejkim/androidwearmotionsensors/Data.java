package com.drejkim.androidwearmotionsensors;

/**
 * Created by julian on 6/4/15.
 */
public class Data {

    //private variables
    float _dataX;
    float _dataY;
    float _dataZ;
    int _id;

    // Empty constructor
    public Data(){

    }
    // constructor
    public Data(float x, float y, float z){
        this._dataX = x;
        this._dataY = y;
        this._dataZ = z;
    }
    public Data(int id,float x, float y, float z){
        this._dataX = x;
        this._dataY = y;
        this._dataZ = z;
        this._id=id;
    }

    public float getX(){return this._dataX;}
    public float getY(){return this._dataY;}
    public float getZ(){return this._dataZ;}

    public void setX(String x){ this._dataX=Float.parseFloat(x);}
    public void setY(String y){ this._dataY=Float.parseFloat(y);}
    public void setZ(String z){ this._dataZ=Float.parseFloat(z);}
    public void setID(int id){this._id=id;}


}
