package com.example.androidscreenshotschedular.utils;

import androidx.annotation.Nullable;

import java.net.DatagramPacket;

public class PacketInfo {
    private String packetContent;
    private String ipAddress;

    public PacketInfo(DatagramPacket datagramPacket) {
        this.packetContent = new String(datagramPacket.getData(),
                0,
                datagramPacket.getLength());
        this.ipAddress = datagramPacket.getAddress().toString();
    }

    public String getPacketContent() {
        return packetContent;
    }

    public String getIpAddress() {
        return ipAddress;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return packetContent.equals(obj);
    }
}
