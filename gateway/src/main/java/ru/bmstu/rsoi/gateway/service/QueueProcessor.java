package ru.bmstu.rsoi.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class QueueProcessor {

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    // ⚠️ هام: لا يوجد GatewayService هنا لحل مشكلة Circular Dependency

    // إضافة مهمة إلى الطابور
    public void addTask(Runnable task) {
        queue.add(task);
        log.info("Task added to queue. Queue size: {}", queue.size());
    }

    @PostConstruct
    public void startWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = queue.take(); // ينتظر حتى توجد مهمة
                    log.info("Executing queued task...");
                    try {
                        task.run();
                        log.info("Task completed successfully");
                    } catch (Exception e) {
                        log.error("Task failed, re-adding to queue", e);
                        queue.add(task); // إعادة المهمة للطابور
                        Thread.sleep(5000); // انتظار 5 ثوانٍ قبل المحاولة مجدداً
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        worker.setDaemon(true); // لا يمنع إغلاق التطبيق
        worker.start();
        log.info("Queue Processor started");
    }
}