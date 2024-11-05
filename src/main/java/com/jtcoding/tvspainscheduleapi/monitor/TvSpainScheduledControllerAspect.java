package com.jtcoding.tvspainscheduleapi.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TvSpainScheduledControllerAspect {

    private final MeterRegistry meterRegistry;

    @Before(
            value = "execution(* com.jtcoding.tvspainscheduleapi.services.ChannelService.getChannels(..))"
    )
    public void updateChannelsEndpointCounter(){
        Counter counter = Counter.builder("channels_calls")
                .description("a number of requests to /channels endpoint")
                .register(meterRegistry);
        counter.increment();
    }
}
