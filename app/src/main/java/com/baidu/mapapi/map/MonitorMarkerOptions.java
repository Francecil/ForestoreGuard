package com.baidu.mapapi.map;

import android.os.Bundle;

import com.baidu.mapapi.model.LatLng;
import com.france.forestoreguard.model.Monitor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/14.
 * 对MarkerOptions的重写，方便增加新函数，原类是final 不能被继承不然就直接继承比较好
 */
public class MonitorMarkerOptions extends OverlayOptions {
    private LatLng d;
    private BitmapDescriptor e;
    private float f = 0.5F;
    private float g = 1.0F;
    private boolean h = true;
    private boolean i = false;
    private float j;
    private String k;
    private int l; //y_offset
//    private int l2; //x_offset
    private boolean m = false;
    private ArrayList<BitmapDescriptor> n;
    private int o = 20;
    private float p = 1.0F;
    private int q;
    int a;
    boolean b;
    Bundle c;
    private Monitor monitor;
    public MonitorMarkerOptions() {
        this.q = MonitorMarkerOptions.MarkerAnimateType.none.ordinal();
        this.b = true;
    }

    public MonitorMarkerOptions icon(BitmapDescriptor var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("marker\'s icon can not be null");
        } else {
            this.e = var1;
            return this;
        }
    }

    public MonitorMarkerOptions animateType(MonitorMarkerOptions.MarkerAnimateType var1) {
        if(var1 == null) {
            var1 = MonitorMarkerOptions.MarkerAnimateType.none;
        }

        this.q = var1.ordinal();
        return this;
    }

    public MonitorMarkerOptions.MarkerAnimateType getAnimateType() {
        switch(this.q) {
            case 1:
                return MonitorMarkerOptions.MarkerAnimateType.drop;
            case 2:
                return MonitorMarkerOptions.MarkerAnimateType.grow;
            default:
                return MonitorMarkerOptions.MarkerAnimateType.none;
        }
    }

    public MonitorMarkerOptions alpha(float var1) {
        if(var1 >= 0.0F && var1 <= 1.0F) {
            this.p = var1;
            return this;
        } else {
            this.p = 1.0F;
            return this;
        }
    }

    public float getAlpha() {
        return this.p;
    }

    public BitmapDescriptor getIcon() {
        return this.e;
    }

    public MonitorMarkerOptions icons(ArrayList<BitmapDescriptor> var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("marker\'s icons can not be null");
        } else if(var1.size() == 0) {
            return this;
        } else {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
                if(var1.get(var2) == null || ((BitmapDescriptor)var1.get(var2)).a == null) {
                    return this;
                }
            }

            this.n = var1;
            return this;
        }
    }

    public ArrayList<BitmapDescriptor> getIcons() {
        return this.n;
    }

    public MonitorMarkerOptions period(int var1) {
        if(var1 <= 0) {
            throw new IllegalArgumentException("marker\'s period must be greater than zero ");
        } else {
            this.o = var1;
            return this;
        }
    }

    public int getPeriod() {
        return this.o;
    }

    public MonitorMarkerOptions position(LatLng var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("marker\'s position can not be null");
        } else {
            this.d = var1;
            return this;
        }
    }

    public LatLng getPosition() {
        return this.d;
    }

    public MonitorMarkerOptions perspective(boolean var1) {
        this.h = var1;
        return this;
    }

    public boolean isPerspective() {
        return this.h;
    }

    MonitorMarkerOptions a(int var1) {
        this.l = var1;
        return this;
    }

    public MonitorMarkerOptions draggable(boolean var1) {
        this.i = var1;
        return this;
    }

    public boolean isFlat() {
        return this.m;
    }

    public MonitorMarkerOptions flat(boolean var1) {
        this.m = var1;
        return this;
    }

    public boolean isDraggable() {
        return this.i;
    }

    public MonitorMarkerOptions anchor(float var1, float var2) {
        if(var1 >= 0.0F && var1 <= 1.0F) {
            if(var2 >= 0.0F && var2 <= 1.0F) {
                this.f = var1;
                this.g = var2;
                return this;
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public float getAnchorX() {
        return this.f;
    }

    public float getAnchorY() {
        return this.g;
    }

    public MonitorMarkerOptions rotate(float var1) {
        while(var1 < 0.0F) {
            var1 += 360.0F;
        }

        var1 %= 360.0F;
        this.j = var1;
        return this;
    }

    public float getRotate() {
        return this.j;
    }

    public MonitorMarkerOptions title(String var1) {
        this.k = var1;
        return this;
    }

    public String getTitle() {
        return this.k;
    }

    Overlay a() {
//        MonitorMarker var1 = new MonitorMarker();
        Marker var1 = new Marker();
        var1.s = this.b;
        var1.r = this.a;
        var1.t = this.c;
        if(this.d == null) {
            throw new IllegalStateException("when you add marker, you must set the position");
        } else {
            var1.a = this.d;
            if(this.e == null && this.n == null) {
                throw new IllegalStateException("when you add marker, you must set the icon or icons");
            } else {
                var1.b = this.e;
                var1.c = this.f;
                var1.d = this.g;
                var1.e = this.h;
                var1.f = this.i;
                var1.g = this.j;
                var1.h = this.k;
                var1.i = this.l;
//                var1.i2 = this.l2;
                var1.j = this.m;
                var1.n = this.n;
                var1.o = this.o;
                var1.l = this.p;
                var1.m = this.q;
                return var1;
            }
        }
    }

    public MonitorMarkerOptions visible(boolean var1) {
        this.b = var1;
        return this;
    }

    public boolean isVisible() {
        return this.b;
    }

    public MonitorMarkerOptions zIndex(int var1) {
        this.a = var1;
        return this;
    }

    public int getZIndex() {
        return this.a;
    }

    public MonitorMarkerOptions extraInfo(Bundle var1) {
        this.c = var1;
        return this;
    }

    public Bundle getExtraInfo() {
        return this.c;
    }

    public static enum MarkerAnimateType {
        none,
        drop,
        grow;

        private MarkerAnimateType() {
        }
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public MonitorMarkerOptions setMonitor(Monitor monitor) {
        this.monitor = monitor;
        return this;
    }
}
