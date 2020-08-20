package com.agro.agro.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.agro.agro.database.AppDatabase;
import com.agro.agro.entity.DateEntity;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

//ViewModel for accessing database.
public class DatabaseViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    @SuppressLint("CheckResult")
    public DatabaseViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(application.getApplicationContext());

        appDatabase.dateDao().getLiveDateRX(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<DateEntity>() {
                    @Override
                    public void onSuccess(DateEntity dateEntity) {
                        Timber.e("Su :%s", dateEntity.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "err : ");
                        Observable.just(1)
                                .doOnEach(new Observer<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Integer integer) {
                                        appDatabase.dateDao().insert(new DateEntity(1, Calendar.getInstance().getTime()));
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<Integer>() {
                                    @Override
                                    public void onNext(Integer integer) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        Timber.e("Done");
                                    }
                                });
                    }
                });
    }

    public LiveData<List<DateEntity>> check() {
        return appDatabase.dateDao().getLiveDate(1);
    }

    @SuppressLint("CheckResult")
    public void update() {
        appDatabase.dateDao().updateDate(new DateEntity(1, Calendar.getInstance().getTime()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.e("Success");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
