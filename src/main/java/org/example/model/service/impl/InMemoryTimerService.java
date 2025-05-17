package org.example.model.service.impl;

import org.example.model.dto.TimerCreate;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerUpdate;
import org.example.model.service.TimerService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimerService implements TimerService {
    private final Map<Long, TimerDetails> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public CompletableFuture<TimerDetails> getTimerDetails(long id) {
        return null;
    }

    @Override
    public CompletableFuture<Void> createTimer(TimerCreate timer) {
        return null;
    }

    @Override
    public CompletableFuture<Void> updateTimer(long id, TimerUpdate request) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteTimer(long id) {
        return null;
    }

    @Override
    public CompletableFuture<List<TimerDetails>> getUserTimers() {
        return null;
    }

}
