package me.vifez.core.util.mongo.subscribers;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class RequestSubscriber implements Subscriber<Document> {

    private long requested;

    public RequestSubscriber() {
        this(1);
    }

    public RequestSubscriber(long requested) {
        this.requested = requested;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(requested);
    }


    @Override
    public void onComplete(){}

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

}