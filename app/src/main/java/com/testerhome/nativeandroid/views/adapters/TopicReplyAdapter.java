package com.testerhome.nativeandroid.views.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.testerhome.nativeandroid.Config;
import com.testerhome.nativeandroid.R;
import com.testerhome.nativeandroid.models.TopicReplyEntity;
import com.testerhome.nativeandroid.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cvtpc on 2015/10/16.
 */
public class TopicReplyAdapter extends BaseAdapter<TopicReplyEntity> {

    public static String TAG = "TopicReplyAdapter";

    public TopicReplyAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.list_item_reply, null);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ReplyViewHolder holder = (ReplyViewHolder) viewHolder;

        holder.topic = mItems.get(position);
        if (holder.topic.isDeleted()) {
            holder.userAvatar.setVisibility(View.INVISIBLE);
            holder.topicItemAuthor.setVisibility(View.INVISIBLE);
            holder.topicTime.setVisibility(View.INVISIBLE);
            holder.praiseReplyLayout.setVisibility(View.INVISIBLE);
            holder.topicItemBody.setText("该楼层已被删除");
            holder.topicItemBody.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        } else {
            holder.praiseReplyLayout.setVisibility(View.VISIBLE);
            holder.userAvatar.setVisibility(View.VISIBLE);
            holder.topicItemAuthor.setVisibility(View.VISIBLE);
            holder.topicTime.setVisibility(View.VISIBLE);
            holder.topicTime.setText(StringUtils.formatPublishDateTime(holder.topic.getCreated_at()));
            holder.topicItemAuthor.setText(holder.topic.getUser().getName());
            String html = holder.topic.getBody_html();
//            html = html.replaceAll("src=\"/photo", "src=\"" + Config.BASEURL + "/photo");
            holder.topicItemBody.setText(Html.fromHtml(html));
            holder.topicItemBody.getPaint().setFlags(0);
            holder.userAvatar.setImageURI(Uri.parse(Config.getImageUrl(holder.topic.getUser().getAvatar_url())));

            if (position == mItems.size() - 1 && mListener != null) {
                mListener.onListEnded();
            }
        }
    }

    private EndlessListener mListener;

    public void setListener(EndlessListener mListener) {
        this.mListener = mListener;
    }

    public interface EndlessListener {
        void onListEnded();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.id_praise_reply_layout)
        LinearLayout praiseReplyLayout;

        @Bind(R.id.id_topic_reply_layout)
        RelativeLayout topicReplyLayout;

        @Bind(R.id.id_topic_item_author)
        TextView topicItemAuthor;

        @Bind(R.id.id_topic_item_content)
        TextView topicItemBody;
        @Bind(R.id.id_topic_time)
        TextView topicTime;

        @Bind(R.id.id_user_avatar)
        SimpleDraweeView userAvatar;

        public TopicReplyEntity topic;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
