package assistance.shopping.msc.assistant.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.model.ShoppingBroadcast;


public class ShoppingBroadcastViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView timeView;
    public ImageView shoppingAssistantPhoto;

    public ShoppingBroadcastViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        timeView = (TextView) itemView.findViewById(R.id.timeStamp);
        shoppingAssistantPhoto = (ImageView) itemView.findViewById(R.id.post_author_photo);
    }

    public void bindToPost(ShoppingBroadcast shoppingBroadcast, View.OnClickListener starClickListener) {
        titleView.setText(shoppingBroadcast.title);
        authorView.setText(shoppingBroadcast.author);
        numStarsView.setText(String.valueOf(shoppingBroadcast.starCount));
        bodyView.setText(shoppingBroadcast.body);
        timeView.setText(shoppingBroadcast.createdAt);
        starView.setOnClickListener(starClickListener);

    }
}
