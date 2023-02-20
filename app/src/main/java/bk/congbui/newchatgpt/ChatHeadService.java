package bk.congbui.newchatgpt;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView,finishView;
    private SpeechRecognizer speechRecognizer = null;
    private Intent speechRecognizerIntent;
    private WindowManager.LayoutParams params;
    private  ImageView chatHeadImage;
    private String result = "";

    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created

        unit();

        setOnclick();

        //Add the view to the window.
//        finishView= LayoutInflater.from(this).inflate(R.layout.close,null);
//        final WindowManager.LayoutParams params1 = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                PixelFormat.TRANSLUCENT);
//
//        params1.gravity = Gravity.BOTTOM | Gravity.CENTER;        //Initially view will be added to top-left corner
//        params1.x = 0;
//        params1.y = 100;



//        mWindowManager.addView(finishView, params1);

//        ImageView imageClose= (ImageView) finishView.findViewById(R.id.close_btn_big );
//        imageClose.setEnabled(false);


        //Set the close button.
    }

    private void setOnclick() {

        ImageView closeButton = (ImageView) mChatHeadView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the service and remove the chat head from the window
                speechRecognizer.stopListening();
                MainActivity.speech.speak("xin chào" , TextToSpeech.QUEUE_FLUSH , null);
//                stopSelf();
            }
        });

        //Drag and move chat head using user's touch action.
        chatHeadImage = mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //finishView.setEnabled(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.
//                            Intent intent = new Intent(ChatHeadService.this, ChatActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
                            //close the service and remove the chat heads
//                            stopSelf();
//                            Log.d("aaa","start listening");
//                            Toast.makeText(getApplicationContext() , "start listening" , Toast.LENGTH_LONG).show();
                            listenSpeak();
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        finishView.setEnabled(false);
                        return true;

                }
                return false;
            }


            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        });

        chatHeadImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                stopSelf();
                return false;
            }
        });
//        chatHeadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chatHeadImage.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
//                Toast.makeText(getApplicationContext() , "start listening" , Toast.LENGTH_LONG).show();
//                listenSpeak();
//            }
//        });
    }

    private void unit() {
        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head, null);
       params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;
        mWindowManager.addView(mChatHeadView, params);

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("aaa","listen result onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("aaa","listen result onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.d("aaa","listen result onRmsChanged");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {Log.d("aaa","listen result onBufferReceived");}

            @Override
            public void onEndOfSpeech() {
                Log.d("aaa","listen result onEndOfSpeech");
            }

            @Override
            public void onError(int i) {
                Log.d("aaa","listen result onError");
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data =  bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data.get(0) != null){
                    result = data.get(0);
                    speechRecognizer.stopListening();
                    chatHeadImage.setBackgroundResource(R.drawable.ic_android_circle);
                    Log.d("aaa",result);
                    Log.d("aaa","listen result finish");
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });

    }

    private void listenSpeak(){
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
//        speechRecognizer.stopListening();
    }
}
