package me.vifez.core.util.mongo.subscribers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class CommandSubscriber implements Subscriber<Object> {

    private long requested;

    public CommandSubscriber() {
        this(1);
    }

    public CommandSubscriber(long requested) {
        this.requested = requested;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(requested);
    }

    @Override
    public void onNext(Object o) {}

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {}

}