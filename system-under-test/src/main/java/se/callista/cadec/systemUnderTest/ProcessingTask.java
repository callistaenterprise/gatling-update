package se.callista.cadec.systemUnderTest;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

public class ProcessingTask extends TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessingTask.class);

	private String message;
	private DeferredResult<ProcessingStatus> deferredResult;
	private int processingTimeMs;
	
	public ProcessingTask(String message, int processingTimeMs, DeferredResult<ProcessingStatus> deferredResult) {
		this.message = message;
	    this.processingTimeMs = processingTimeMs;
		this.deferredResult = deferredResult;
	}
	
	@Override
	public void run() {
        
        if (deferredResult.isSetOrExpired()) {
            LOG.warn("Processing of non-blocking request already expired");        
	    } else {
	        boolean deferredStatus = deferredResult.setResult(new ProcessingStatus(message, processingTimeMs));
            LOG.debug("Processing of non-blocking request done, deferredStatus = {}", deferredStatus);        
	    }
	}
}