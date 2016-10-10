package se.callista.cadec.systemUnderTest;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.sun.management.UnixOperatingSystemMXBean;

@RestController
public class ProcessingController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingController.class);

    private Timer timer = new Timer();

    @Value("${statistics.requestsPerLog}")
    private int STAT_REQS_PER_LOG;
    private int defaultMinMs = 500;
    private int defaultMaxMs = 1500;

    @RequestMapping("/set-default-processing-time")
    public void setDefaultProcessingTime(
            @RequestParam(value = "minMs", required = true) int minMs,
            @RequestParam(value = "maxMs", required = true) int maxMs) {
        this.defaultMinMs = minMs;
        this.defaultMaxMs = maxMs;
        LOG.info("Set default response time to {} - {} ms.", minMs, maxMs);
    }

    @RequestMapping("/logIn")
    public DeferredResult<ProcessingStatus> logIn(@RequestParam(value="userName", defaultValue="aUser") String userName) {
        return processRequest("loggedIn " + userName);
    }

    @RequestMapping("/doThis")
    public DeferredResult<ProcessingStatus> doThis() {
        return processRequest("doneThis");
    }

    @RequestMapping("/doThat")
    public DeferredResult<ProcessingStatus> doThat() {
        return processRequest("doneThat");
    }

    @RequestMapping("/logOut")
    public DeferredResult<ProcessingStatus> logOut() {
        return processRequest("loggedOut");
    }

    public DeferredResult<ProcessingStatus> processRequest(String message) {
        int processingTimeMs = calculateProcessingTime(defaultMinMs, defaultMaxMs);

        // Create the deferredResult and initiate a callback object, task, with it
        DeferredResult<ProcessingStatus> deferredResult = new DeferredResult<>();
        ProcessingTask task = new ProcessingTask(message, processingTimeMs, deferredResult);

        // Schedule the task for asynch completion in the future
        timer.schedule(task, processingTimeMs);

        // Return to let go of the precious thread we are holding on to...
        return deferredResult;
    }

    private int calculateProcessingTime(int minMs, int maxMs) {
        if (minMs == 0 && maxMs == 0) {
            minMs = defaultMinMs;
            maxMs = defaultMaxMs;
        }

        if (maxMs < minMs) maxMs = minMs;
        int processingTimeMs = minMs + (int) (Math.random() * (maxMs - minMs));
        return processingTimeMs;
    }
    
}