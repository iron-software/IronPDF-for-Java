package com.ironsoftware.ironpdf.internal.staticapi;


import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

final class Utils_ReceivingCustomStreamObserver<T> implements StreamObserver<T> {

    private final CountDownLatch latch;
    private final List<T> results;

    Utils_ReceivingCustomStreamObserver(CountDownLatch latch, List<T> results) {
        this.latch = latch;
        this.results = results;
    }

    @Override
    public void onNext(T obj) {
        results.add(obj);
    }

    @Override
    public void onError(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    @Override
    public void onCompleted() {
        latch.countDown();
    }


}