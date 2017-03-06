package com.evolutionnext.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import org.junit.Test;

import java.time.LocalTime;

public class BasicObservableTest {

    @Test
    public void testManualObservableWithManualObserver() {
        Observable<Integer> a = Observable.create(
                emitter -> {
                    emitter.onNext(40);
                    emitter.onNext(45);
                    emitter.onComplete();
                });

        //-----------1001 lines code------------------

        a.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.println();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer x) {
                System.out.printf("On Next: %d\n", x);
            }
        });
    }

    @Test
    public void testManualObservableWithManualObserverSimplified() {
        Observable<Integer> a = Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onComplete();
                }
        );
        a.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.println();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer x) {
                System.out.printf("On Next: %d\n", x);
            }
        });
    }

    @Test
    public void testManualObservableWithExplicitActions() {
        Observable.create(source -> {
            source.onNext(40);
            source.onNext(45);
            source.onComplete();
        }).subscribe(new Consumer<Object>() {
                         @Override
                         public void accept(@NonNull Object x) throws Exception {
                             System.out.println(x);
                         }
                     },
                Throwable::printStackTrace,
                () -> System.out.println("Completed"));
    }

    @Test
    public void testManualObservableWithLambdaActions() {
        Observable<Integer> a = Observable.create(subscriber -> {
            subscriber.onNext(40);
            subscriber.onNext(45);
            subscriber.onComplete();
        });
        a.subscribe(
                integer -> System.out.println("Received: " + integer),
                e -> {
                    e.printStackTrace();
                    System.out.println();
                },
                () -> System.out.println("Completed"));
    }

    @Test
    public void testManualObservableWithObserverVastlySimplified() {
        Observable<Integer> a = Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onComplete();
                }
        );
        a.subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Completed"));
    }


    @Test
    public void testBasic() throws InterruptedException {
        Observable.just(40, 45)
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Done, son"));
        Thread.sleep(2000);
    }

    @Test
    public void testUsingTwoObservablesBasedOneAnother() throws InterruptedException {
        Observable<Integer> a = Observable.just(50, 100, 122);
        Observable<Integer> b =
                a.flatMap(x -> Observable.just(x - 1, x, x + 1));

        b.subscribe(System.out::println);
        Thread.sleep(2000);

        b.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    /**
     * Defer will delay any emission of items until an Observer subscribes
     *
     * @throws InterruptedException
     */
    @Test
    public void testDefer() throws InterruptedException {
        Observable<LocalTime> localTimeObservable =
                Observable.just(LocalTime.now()).repeat(3);

        localTimeObservable.subscribe(System.out::println);
        Thread.sleep(3000);
        System.out.println("Next Subscriber");
        localTimeObservable.subscribe(System.out::println);
    }

    @Test
    public void testTicker() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};
        Observable<String> tickerObservable =
                Observable.fromArray(ticker);
        TickerPriceFinder tickerPriceFinder =
                TickerPriceFinder.create();
        tickerObservable
                .flatMap(s -> Observable.fromFuture(
                        tickerPriceFinder.getPrice(s)))
                .subscribe(System.out::println);
    }

}
