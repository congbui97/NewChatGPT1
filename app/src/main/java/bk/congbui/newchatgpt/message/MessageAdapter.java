package bk.congbui.newchatgpt.message;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

import bk.congbui.newchatgpt.R;
import retrofit2.Retrofit;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private List<Message> lstMessage;

    @SuppressLint("ResourceType")
    public MessageAdapter(@NonNull Context context, @NonNull List<Message> objects) {
        super(context, R.layout.item_message_layout, objects);
        this.context = context;
        this.lstMessage = objects;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_message_layout, null,true);
        Message message = lstMessage.get(position);
        TextView titleText =  rowView.findViewById(R.id.tvMessage);
        LinearLayout lineMessage = rowView.findViewById(R.id.lineMessage);
        titleText.setText(message.getTextMessage());


        if (message.getWhoSend() == 0){
            lineMessage.setGravity(Gravity.END);
            titleText.setBackgroundResource(R.drawable.textview_person_style);
            titleText.setTextColor(ContextCompat.getColor(context , R.color.white));
        }else {
            lineMessage.setGravity(Gravity.START);
            titleText.setBackgroundResource(R.drawable.textview_ai_style);
            if (message.getWhoSend() == 1){
                titleText.setTextColor(ContextCompat.getColor(context,R.color.black));
            }else if (message.getWhoSend() == 2){
                titleText.setTextColor(ContextCompat.getColor(context,R.color.red));
            }

        }



        return rowView;

    }
}
