package me.liuwj.site.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UncheckedIOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Created by vince on Mar 21, 2021.
 */
public class IpUtils {

    /**
     * 取得本机网络地址列表
     */
    public static List<InetAddress> getLocalAddresses() {
        try {
            List<InetAddress> result = new ArrayList<>();

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    result.add(addresses.nextElement());
                }
            }

            return result;
        } catch (SocketException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 返回第一个非 127.0.0.1 的内网 ipv4 地址
     */
    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || netInterface.isPointToPoint() || !netInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }

            return null;
        } catch (SocketException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 取得本机的一个公网 IP
     * 若没有公网 IP，则返回一个私网 IP
     * 若也没有私网 IP，返回 127.0.0.1
     *
     * Note: 此方法只返回 IPv4 地址
     */
    public static String getPublicIp() {
        List<InetAddress> allAddresses = getLocalAddresses();
        // 排除 IPv6 地址和 loopback 地址
        allAddresses.removeIf(address -> address instanceof Inet6Address || address.isLoopbackAddress());

        Optional<InetAddress> publicAddress = allAddresses.stream().filter(address -> !address.isSiteLocalAddress()).findAny();
        if (publicAddress.isPresent()) {
            return publicAddress.get().getHostAddress();
        }

        Optional<InetAddress> privateAddress = allAddresses.stream().filter(InetAddress::isSiteLocalAddress).findAny();
        return privateAddress.map(InetAddress::getHostAddress).orElse("127.0.0.1");
    }

    /**
     * 在有反向代理的环境下，获取 HTTP 请求的客户端 IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String result = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(result)) {
            String[] arr = result.split(",");
            result = arr[arr.length - 1];
        } else {
            result = request.getHeader("X-Real-IP");
        }

        if (StringUtils.isNotBlank(result) && !result.trim().equalsIgnoreCase("unknown")) {
            return result.trim();
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 获取当前正在请求的客户端的 IP
     */
    public static String getCurrentClientIp() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            return getClientIp(request);
        }
    }

    /**
     * 将一个 host 字符串转换成一个整数表示的形式
     * host 可以是主机名、域名、IP 字符串，如果传入主机名或域名，先将其解析为 IP，再转换成整数
     * 此方法不支持 IPv6
     */
    public static int hostToNumber(String host) {
        if (StringUtils.isBlank(host)) {
            return -1;
        }

        String ip;
        try {
            ip = InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException e) {
            return -1;
        }

        String[] seg = ip.split("\\.");
        return (Integer.parseInt(seg[0]) << 24)
                + (Integer.parseInt(seg[1]) << 16)
                + (Integer.parseInt(seg[2]) << 8)
                + Integer.parseInt(seg[3]);
    }

    /**
     * 将一个整数表示的 IP 解析成字符串形式
     * 此方法不支持 IPv6
     */
    public static String numberToIp(int num) {
        return ((num >> 24) & 0xFF) + "." + ((num >> 16) & 0xFF) + "." + ((num >> 8) & 0xFF) + "." + (num & 0xFF);
    }
}
