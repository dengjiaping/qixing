package com.qixing.qxlive.rongyun.ui.message;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.qxlive.rongyun.controller.EmojiManager;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class TextMsgView extends BaseMsgView {

    private TextView username;
    private TextView msgText;

    SpannableString msp = null;

    public TextMsgView(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.msg_text_view, this);
        username = (TextView) view.findViewById(R.id.username);
        msgText = (TextView) view.findViewById(R.id.msg_text);
    }

    @Override
    public void setContent(MessageContent msgContent) {
        TextMessage msg = (TextMessage) msgContent;
//        username.setText(msg.getUserInfo().getName() + ": ");
        username.setVisibility(View.GONE);
        msgText.setTextColor(getResources().getColor(R.color.text_live_chat));
//        msgText.setText(EmojiManager.parse(msg.getContent(), msgText.getTextSize()));

        msp = new SpannableString(msg.getUserInfo().getName() + ": " + EmojiManager.parse(msg.getContent(), msgText.getTextSize()));

        //设置字体前景色
        msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_live_name)), 0, msg.getUserInfo().getName().length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

        //设置字体背景色
//        msp.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.text_live_chat)), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置背景色为青色

        msgText.setText(msp);
        msgText.setMovementMethod(LinkMovementMethod.getInstance());
//        msgText.setText(Html.fromHtml("<font color='#3ce1ff'>"+msg.getUserInfo().getName() + ": "+"</font>")+EmojiManager.parse(msg.getContent(), msgText.getTextSize()));
    }
}
