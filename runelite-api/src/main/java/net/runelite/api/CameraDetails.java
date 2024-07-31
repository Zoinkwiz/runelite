package net.runelite.api;

import lombok.Setter;

@Setter
public class CameraDetails {
    double x = Double.MAX_VALUE;

    double y = Double.MAX_VALUE;

    double z = Double.MAX_VALUE;

    public int getCameraX(Client client)
    {
        if (x != Double.MAX_VALUE) return (int) x;
        return client.getCameraX();
    }

    public double getCameraFpX(Client client)
    {
        if (x != Double.MAX_VALUE) return x;
        return client.getCameraFpX();
    }

    public int getCameraY(Client client)
    {
        if (y != Double.MAX_VALUE) return (int) y;
        return client.getCameraY();
    }

    public double getCameraFpY(Client client)
    {
        if (y != Double.MAX_VALUE) return y;
        return client.getCameraFpY();
    }

    public int getCameraZ(Client client)
    {
        if (z != Double.MAX_VALUE) return (int) z;
        return client.getCameraZ();
    }

    public double getCameraFpZ(Client client)
    {
        if (z != Double.MAX_VALUE) return z;
        return client.getCameraFpZ();
    }

    public void reset()
    {
        x = Double.MAX_VALUE;
        y = Double.MAX_VALUE;
        z = Double.MAX_VALUE;
    }
}
