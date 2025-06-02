    package org.example.service;

    import org.example.model.dto.TimerCreate;
    import org.example.model.dto.TimerDetails;
    import org.example.model.dto.TimerUpdate;
    import org.example.service.interfaces.TimerService;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.concurrent.CompletableFuture;
    import java.util.concurrent.ConcurrentHashMap;
    import java.util.concurrent.atomic.AtomicLong;

    //This class handles the timer crud operations locally within the application
    public class InMemoryTimerService implements TimerService {
        private final Map<Long, TimerDetails> store = new ConcurrentHashMap<>();
        //Atomic long ID is a clever way of storing IDs, thread-safe
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
        public CompletableFuture<TimerDetails> createTimer(TimerCreate req) {
            return CompletableFuture.supplyAsync(() -> {
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
                System.out.println("debugg, InMem → stored, size=" + store.size());
                return dto;
            });
        }

        @Override
        public CompletableFuture<TimerDetails> updateTimer(long id, TimerUpdate req) {
            return CompletableFuture.supplyAsync(() -> {
                TimerDetails t = store.get(id);
                if (t == null) {
                    throw new RuntimeException(String.format("Timer of id %s not found", id));
                }
                t.setName(req.getName());
                t.setWorkDuration(req.getWorkDuration());
                t.setShortBreakDuration(req.getShortBreakDuration());
                t.setLongBreakDuration(req.getLongBreakDuration());
                t.setPomodoroCount(req.getPomodoroCount());
                return t;
            });
        }

        @Override
        public CompletableFuture<Void> deleteTimer(long id) {
            store.remove(id);
            return CompletableFuture.completedFuture(null);
        }
    }