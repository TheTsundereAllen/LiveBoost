package me.allen.liveboost.proxy;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.allen.liveboost.LiveBoost;
import me.allen.liveboost.util.HttpUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Proxy {
    private Set<String> proxies = Sets.newConcurrentHashSet();

    private LiveBoost liveBoost;
    private final Pattern IP_REGEX = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:\\d{1,5}");
    private final List<String> proxiUrls = Arrays.asList(
            "http://www.66ip.cn/mo.php?tqsl=9999",
            "http://www.89ip.cn/tqdl.html?api=1&num=9999"
    );

    public Proxy(LiveBoost liveBoost) {
        this.liveBoost = liveBoost;
        this.liveBoost
                .getSharedExecutor()
                .submit(() -> proxiUrls.forEach(this::fetch));
        //this.liveBoost.getSharedExecutor().scheduleAtFixedRate(() -> proxiUrls.forEach(this::fetch), 0L, 60L, TimeUnit.MINUTES);
    }


    private void fetch(String url) {
        System.out.println("Fetching " + url);
        String proxyList = HttpUtil.sendGet(url);
        Matcher matcher = IP_REGEX.matcher(proxyList);
        System.out.println(proxyList);
        while (matcher.find()) {
            String proxy = matcher.group();
            System.out.println("Added " + proxy);
            proxies.add(proxy);
        }
    }
}
