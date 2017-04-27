package com.kittipat.rxjava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imagezoom.ImageAttacher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txvRxFromCall)
    TextView txvFromCall;
    @BindView(R.id.txvRxJust)
    TextView txvJust;
    @BindView(R.id.txvRxSubscribeOn)
    TextView txvSubscribeOn;
    @BindView(R.id.imvDownload)ImageView imvDownload;
    @BindView(R.id.imvDownload2)ImageView imvDownload2;

    String downloadUrl = "http://www.mx7.com/i/19b/8K3ec7.png";
    ArrayList<String> arrUrl = new ArrayList<>();
    int mWidth;
    int mHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        arrUrl.add("http://www.mx7.com/i/19b/8K3ec7.png");
        arrUrl.add("http://www.mx7.com/i/bb4/ZmQ5YC.jpg");
        rxFromCallable();
        rxJust();
        rxSubscribeOn();
        //TODO ขั้นตอนการเรียกใช้งาน Observable (2)
        getNamePhoneObservable().subscribe(nameObserver);

        testAction();
        downloadImgObservable();

         mWidth = imvDownload.getLayoutParams().width;
         mHeight = imvDownload.getLayoutParams().height;
    }

    public String getNameList(int index) {
        List<String> nameList = Arrays.asList("Cupcake",
                "Donut",
                "Eclair",
                "Froyo",
                "Gingerbread",
                "Honeycomb",
                "Ice Cream Sandwich",
                "Jelly Bean",
                "Kitkat",
                "Lollipop",
                "Marshmallow",
                "Nugat");

        return nameList.get(index);
    }

    public void rxJust() {
        //TODO onError not working
        Observable.just(getNameList(0), getNameList(1), getNameList(2))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Rx", "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Rx", "onError");
                        txvJust.setText("-");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("Rx", "Name: " + s);
                        txvJust.setText("Name: " + s);
                    }

                });
    }

    public void rxFromCallable() {
        //TODO OnError Working
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getNameList(100);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d("Rx", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Rx", "onError");
                txvFromCall.setText("-");
            }

            @Override
            public void onNext(String s) {
                Log.d("Rx", "Name:" + s);
                txvFromCall.setText("Name: " + s);
            }
        });
    }

    public void rxSubscribeOn() {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getNameList(1);
            }
        }).subscribeOn(Schedulers.io())
                //TODO Fix show Content in MainThread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Rx", "onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Rx", "onError");
                        txvSubscribeOn.setText("-");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("Rx", "Name;" + s);
                        txvSubscribeOn.setText("Name: " + s);
                    }
                });
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////

    public String getNamePhone() {
        List<String> nameList = Arrays.asList("Nokia",
                "Motolola",
                "I-Phone",
                "Sumsang",
                "Sony",
                "Asus",
                "Nexus",
                "Window Phone",
                "Oppo");

        return nameList.get(3);
    }


    //TODO Create Observable ไว้ก่อนเลยก็ได้ (แยกกับส่วนบน)
    public Observable<String> getNamePhoneObservable() {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getNamePhone();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //TODO ขั้นตอนการเรียกใช้งาน Observable (1)
    Observer<String> nameObserver = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getPriceList() {
        List<Integer> priceList = Arrays.asList(3000,
                4000,
                20000,
                15000,
                12000,
                9000,
                9800,
                7500,
                12000);

        return priceList.get(3);
    }

    //TODO Create Observable
    public Observable<Integer> getPricePhoneObservable() {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceList();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void testAction() {
        getNamePhoneObservable().doOnNext(getNamePhoneAction)
                .doOnError(errorAction)
                .subscribe();

        getPricePhoneObservable().doOnNext(getPricePhoneAction)
                .doOnError(errorAction)
                .subscribe();

    }

    private Action1<String> getNamePhoneAction = new Action1<String>() {
        @Override
        public void call(String s) {
            Log.d("TestAction", s);

            //TODO Stop Working Observable (อาจจะต้องไปใส่ใน Action 0 น่าจะเหมาะสมกว่า ... " ไม่แน่ใจ " )
            Subscription getNamePhoneSubscription = getNamePhoneObservable().doOnNext(getNamePhoneAction)
                    .doOnError(errorAction)
                    .subscribe();
            if (getNamePhoneSubscription != null && !getNamePhoneSubscription.isUnsubscribed()) {
                getNamePhoneSubscription.unsubscribe();
            }
        }
    };

    private Action1<Integer> getPricePhoneAction = new Action1<Integer>() {
        @Override
        public void call(Integer integer) {
            Log.d("TestAction", String.valueOf(integer));
        }
    };

    private Action1<Throwable> errorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable e) {
            // On Error
            Log.d("TestAction", "onError");
        }
    };


/////////////////////////////////////////////////////////////////////////////////////////////////////


    public Observable<ArrayList<String>> getUrlObservable() {
        return Observable.fromCallable(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                return arrUrl;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void downloadImgObservable(){
        getUrlObservable().doOnNext(downloadImgAction)
                          .doOnError(errorAction)
                          .subscribe();
    }

    private Action1<ArrayList<String>> downloadImgAction = new Action1<ArrayList<String>>() {
        @Override
        public void call(ArrayList<String> arrayList) {
            imvDownload = (ImageView) findViewById(R.id.imvDownload);
            for(int i = 0; i < arrayList.size(); i++){
                if(i == 0) {
                    Picasso.with(MainActivity.this)
                            .load(arrayList.get(i))
                            //Change Shape
//                    .transform(new RoundedRectTransformation(radius,stroke,margin))
                            .into(imvDownload);
                    Toast.makeText(getApplicationContext(), String.valueOf(arrayList.get(i)), Toast.LENGTH_SHORT).show();
                    imvDownload.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.imvDownload, mWidth, mHeight));
                    usingSimpleImage(imvDownload);

                    //TODO Stop Working Observable (อาจจะต้องไปใส่ใน Action 0 น่าจะเหมาะสมกว่า ... " ไม่แน่ใจ " )
                    Subscription getDownloadImgSubscribe = getUrlObservable().doOnNext(downloadImgAction)
                            .doOnError(errorAction)
                            .subscribe();
                    if (getDownloadImgSubscribe != null && !getDownloadImgSubscribe.isUnsubscribed()) {
                        getDownloadImgSubscribe.unsubscribe();
                    }

                }else{
                    Picasso.with(MainActivity.this)
                            .load(arrayList.get(i))
                            //Change Shape
//                    .transform(new RoundedRectTransformation(radius,stroke,margin))
                            .into(imvDownload2);
                    Toast.makeText(getApplicationContext(), String.valueOf(arrayList.get(i)), Toast.LENGTH_SHORT).show();
                    imvDownload2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.imvDownload2, mWidth, mHeight));
                    usingSimpleImage(imvDownload2);

                    //TODO Stop Working Observable (อาจจะต้องไปใส่ใน Action 0 น่าจะเหมาะสมกว่า ... " ไม่แน่ใจ " )
                    Subscription getDownloadImgSubscribe = getUrlObservable().doOnNext(downloadImgAction)
                            .doOnError(errorAction)
                            .subscribe();
                    if (getDownloadImgSubscribe != null && !getDownloadImgSubscribe.isUnsubscribed()) {
                        getDownloadImgSubscribe.unsubscribe();
                    }
                }

            }
        }
    };


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //Import Library PinchZoom
    public void usingSimpleImage(ImageView imageView) {
        ImageAttacher mAttacher = new ImageAttacher(imageView);
        ImageAttacher.MAX_ZOOM = 0.5f; // Double the current Size
        ImageAttacher.MIN_ZOOM = -1.5f; // Half the current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
    }

    private class PhotoTapListener implements ImageAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }
    }

    private class MatrixChangeListener implements ImageAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }
}


