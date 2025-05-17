package org.example.model.service.impl;

import org.example.model.dto.TimerCreate;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerUpdate;
import org.example.model.service.TimerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        return CompletableFuture.completedFuture(store.get(id));
    }

    @Override
    public CompletableFuture<List<TimerDetails>> getUserTimers() {
        return CompletableFuture.completedFuture(new ArrayList<>(store.values()));
    }

    @Override
    public CompletableFuture<Void> createTimer(TimerCreate req) {
        long id = seq.getAndIncrement();
        TimerDetails dto = new TimerDetails(
                id,
                req.getName(),
                LocalDateTime.now(),
                req.getWorkDuration(),
                req.getShortBreakDuration(),
                req.getLongBreakDuration(),
                req.getPomodoroCount()
        );
        store.put(id, dto);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> updateTimer(long id, TimerUpdate req) {
        TimerDetails t = store.get(id);
        if (t == null) return CompletableFuture.completedFuture(null);
        t.setName(req.getName());
        t.setWorkDuration(req.getWorkDuration());
        t.setShortBreakDuration(req.getShortBreakDuration());
        t.setLongBreakDuration(req.getLongBreakDuration());
        t.setPomodoroCount(req.getPomodoroCount());
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> deleteTimer(long id) {
        store.remove(id);
        return CompletableFuture.completedFuture(null);
    }
}