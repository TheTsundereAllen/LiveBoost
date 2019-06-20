package me.allen.liveboost.runnable;

import lombok.RequiredArgsConstructor;
import me.allen.liveboost.LiveBoost;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class BoostHeartbeatRunnable implements Runnable {

    private final LiveBoost liveBoost;
    private final long roomId;
    private final int amount;

    @Override
    public void run() {
        String url = "https://chushou.tv/room/flash/heartbeat.htm?_fVersion=407&_identifier=620afc6d393d462d97fa5d8276aba9b2&_timestamp=1560928909691&roomId={id}&token=&serverip=0&streamurl=&_sas=1000&_sc=null&_listKeynull&_sign=3119253094&_times=1"
                .replace("{id}", Long.toString(roomId));

        AtomicInteger amount = new AtomicInteger();
        for (String proxy : this.liveBoost.getProxy().getProxies()) {
            if (amount.get() > this.amount) break;
            System.out.println("正在使用 " + proxy + " 对 " + roomId + " 进行访问.");
            this.liveBoost.getHeartbeatPool().execute(() -> {
                String[] resolvedProxy = proxy.split(":");
                Proxy connectProxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(resolvedProxy[0], Integer.parseInt(resolvedProxy[1])));

                try {
                    URL heartbeatURL = new URL(url);
                    HttpsURLConnection connection = (HttpsURLConnection) heartbeatURL.openConnection(connectProxy);
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    connection.getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                amount.incrementAndGet();
            });
        }
    }

}
