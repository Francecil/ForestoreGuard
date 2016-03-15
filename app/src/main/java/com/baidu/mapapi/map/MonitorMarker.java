package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.ParcelItem;
import com.baidu.mapapi.model.inner.GeoPoint;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MonitorMarker extends Overlay {
    LatLng a;
    BitmapDescriptor b;
    float c;
    float d;
    boolean e;
    boolean f;
    float g;
    String h;
    int i;
    int i2;//x_offset
    boolean j = false;
    boolean k = false;
    float l;
    int m;
    ArrayList<BitmapDescriptor> n;
    int o = 20;

    MonitorMarker() {
        this.q = q.c;
    }

    public void setIcon(BitmapDescriptor var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("marker\'s icon can not be null");
        } else {
            this.b = var1;
            this.listener.b(this);
        }
    }

    public BitmapDescriptor getIcon() {
        return this.b;
    }

    public void setIcons(ArrayList<BitmapDescriptor> var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("marker\'s icons can not be null");
        } else if(var1.size() != 0) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
                if(var1.get(var2) == null || ((BitmapDescriptor)var1.get(var2)).a == null) {
                    return;
                }
            }

            this.n = var1;
            this.listener.b(this);
        }
    }

    public ArrayList<BitmapDescriptor> getIcons() {
        return this.n;
    }

    public void setPeriod(int var1) {
        if(var1 <= 0) {
            throw new IllegalArgumentException("marker\'s period must be greater than zero ");
        } else {
            this.o = var1;
            this.listener.b(this);
        }
    }

    public void setAlpha(float var1) {
        if(var1 >= 0.0F && (double)var1 <= 1.0D) {
            this.l = var1;
            this.listener.b(this);
        } else {
            this.l = 1.0F;
        }
    }

    public float getAlpha() {
        return this.l;
    }

    public int getPeriod() {
        return this.o;
    }

    public void setToTop() {
        this.k = true;
        this.listener.b(this);
    }

    public void setPosition(LatLng var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("marker\'s position can not be null");
        } else {
            this.a = var1;
            this.listener.b(this);
        }
    }

    public LatLng getPosition() {
        return this.a;
    }

    public void setPerspective(boolean var1) {
        this.e = var1;
        this.listener.b(this);
    }

    public boolean isPerspective() {
        return this.e;
    }

    public void setDraggable(boolean var1) {
        this.f = var1;
        this.listener.b(this);
    }

    public boolean isDraggable() {
        return this.f;
    }

    public boolean isFlat() {
        return this.j;
    }

    public void setFlat(boolean var1) {
        this.j = var1;
        this.listener.b(this);
    }

    public void setAnchor(float var1, float var2) {
        if(var1 >= 0.0F && var1 <= 1.0F) {
            if(var2 >= 0.0F && var2 <= 1.0F) {
                this.c = var1;
                this.d = var2;
                this.listener.b(this);
            }
        }
    }

    public float getAnchorX() {
        return this.c;
    }

    public float getAnchorY() {
        return this.d;
    }

    public void setRotate(float var1) {
        while(var1 < 0.0F) {
            var1 += 360.0F;
        }

        var1 %= 360.0F;
        this.g = var1;
        this.listener.b(this);
    }

    public float getRotate() {
        return this.g;
    }

    public void setTitle(String var1) {
        this.h = var1;
    }

    public String getTitle() {
        return this.h;
    }

    Bundle a(Bundle var1) {
        super.a(var1);
        Bundle var2 = new Bundle();
        if(this.b != null) {
            var1.putBundle("image_info", this.b.b());
        }

        GeoPoint var3 = CoordUtil.ll2mc(this.a);
        var1.putInt("animatetype", this.m);
        var1.putDouble("location_x", var3.getLongitudeE6());
        var1.putDouble("location_y", var3.getLatitudeE6());
        var1.putInt("perspective", this.e?1:0);
        var1.putFloat("anchor_x", this.c);
        var1.putFloat("anchor_y", this.d);
        var1.putFloat("rotate", this.g);
        var1.putInt("y_offset", this.i);
        var1.putInt("x_offset", this.i2);
        var1.putInt("isflat", this.j?1:0);
        var1.putInt("istop", this.k?1:0);
        var1.putInt("period", this.o);
        var1.putFloat("alpha", this.l);
        if(this.n != null && this.n.size() > 0) {
            this.a(this.n, var1);
        }

        var2.putBundle("param", var1);
        return var1;
    }

    private void a(ArrayList<BitmapDescriptor> var1, Bundle var2) {
        ArrayList var3 = new ArrayList();
        Iterator var4 = var1.iterator();

        while(var4.hasNext()) {
            BitmapDescriptor var5 = (BitmapDescriptor)var4.next();
            ParcelItem var6 = new ParcelItem();
            Bundle var7 = new Bundle();
            Bitmap var8 = var5.a;
            ByteBuffer var9 = ByteBuffer.allocate(var8.getWidth() * var8.getHeight() * 4);
            var8.copyPixelsToBuffer(var9);
            byte[] var10 = var9.array();
            var7.putByteArray("image_data", var10);
            var7.putInt("image_width", var8.getWidth());
            var7.putInt("image_height", var8.getHeight());
            MessageDigest var11 = null;

            try {
                var11 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var15) {
                var15.printStackTrace();
            }

            var11.update(var10, 0, var10.length);
            byte[] var12 = var11.digest();
            StringBuilder var13 = new StringBuilder("");

            for(int var14 = 0; var14 < var12.length; ++var14) {
                var13.append(Integer.toString((var12[var14] & 255) + 256, 16).substring(1));
            }

            String var18 = var13.toString();
            var7.putString("image_hashcode", var18);
            var6.setBundle(var7);
            var3.add(var6);
        }

        if(var3.size() > 0) {
            ParcelItem[] var16 = new ParcelItem[var3.size()];

            for(int var17 = 0; var17 < var3.size(); ++var17) {
                var16[var17] = (ParcelItem)var3.get(var17);
            }

            var2.putParcelableArray("icons", var16);
        }

    }
}
