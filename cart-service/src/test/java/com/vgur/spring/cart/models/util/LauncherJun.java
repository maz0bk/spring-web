package com.vgur.spring.cart.models.util;


import com.vgur.spring.cart.models.CartTest;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

public class LauncherJun {
    public static void main(String[] args) {
        Launcher launcherJunit = LauncherFactory.create();
        var summaryGeneratingListener = new SummaryGeneratingListener();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(CartTest.class))
                .build();
        launcherJunit.execute(request, summaryGeneratingListener);

        try(var writer = new PrintWriter(System.out)){
            summaryGeneratingListener.getSummary().printTo(writer);
        }
    }

}
