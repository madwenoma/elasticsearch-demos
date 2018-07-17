package com.lee.learn;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2018/7/11.
 */
@Configuration
public class MyElasticSearchConfig {

    @Bean
    public TransportClient client() throws UnknownHostException {

        InetSocketTransportAddress address = new InetSocketTransportAddress(InetAddress.getByName("100.100.16.55"), 9300);

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(address);
        return client;
    }
}
