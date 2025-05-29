package org.example.model;

import org.example.model.service.TimerService;
import org.example.model.service.impl.InMemoryTimerService;
import org.example.model.service.impl.RemoteTimerService;
// this class handles which timerservice class is going to be used
public final class TimerServiceFactory {
    private static final InMemoryTimerService GUEST = new InMemoryTimerService();

    public static TimerService get() {
        AppContext context = AppContext.getInstance();
        //simply check if it is guest, run the offline mode, use InMemoryTimerService
        //otherwise run the timer logic online, talk to the backend
        return context.isGuestMode()
                ? GUEST
                : new RemoteTimerService(context.getAuthToken());
    }
}

