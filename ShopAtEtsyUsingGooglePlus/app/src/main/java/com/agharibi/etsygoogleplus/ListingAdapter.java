package com.agharibi.etsygoogleplus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.agharibi.etsygoogleplus.api.Etsy;
import com.agharibi.etsygoogleplus.google.GoogleServicesHelper;
import com.agharibi.etsygoogleplus.model.ActiveListings;
import com.agharibi.etsygoogleplus.model.Listing;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.PUT;


public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder>
    implements Callback<ActiveListings>, GoogleServicesHelper.GoogleServicesListener{

    private static final String TAG = ListingAdapter.class.getSimpleName();

    public static final int REQUEST_CODE_PLUS_ONE = 10;
    public static final int REQUEST_CODE_SHARE = 11;

    private MainActivity mActivity;
    private LayoutInflater mInflater;
    private ActiveListings mActiveListings;

    private boolean isGooglePlayServicesAvailable;

    public ListingAdapter(MainActivity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        this.isGooglePlayServicesAvailable = false;
    }

    @Override
    public ListingHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ListingHolder(mInflater.inflate(R.layout.layout_listing, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ListingHolder holder, int position) {
        final Listing listing = mActiveListings.results[position];

        holder.titleView.setText(listing.title);
        holder.priceView.setText(listing.price);
        holder.shopNameView.setText(listing.Shop.shop_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(listing.url));
                mActivity.startActivity(intent);
            }
        });


        if (isGooglePlayServicesAvailable) {
            holder.plusOneButton.setVisibility(View.VISIBLE);
            holder.plusOneButton.initialize(listing.url, REQUEST_CODE_PLUS_ONE);
            holder.plusOneButton.setAnnotation(PlusOneButton.ANNOTATION_NONE);

            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new PlusShare.Builder(mActivity)
                            .setType("text/plain")
                            .setText("Checkout this item on Etsy " + listing.title)
                            .setContentUrl(Uri.parse(listing.url))
                            .getIntent();

                    mActivity.startActivityForResult(intent, REQUEST_CODE_SHARE);
                }
            });
        }
        else {
            holder.plusOneButton.setVisibility(View.GONE);
            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,
                            "Checkout this item on Etsy " + listing.title + " " + listing.url);
                    intent.setType("text/plain");
                    mActivity.startActivityForResult(Intent.createChooser(intent, "Share"), REQUEST_CODE_SHARE);
                }
            });
        }

        Picasso.with(holder.imageView.getContext())
                .load(listing.Images[0].url_570xN)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if(mActiveListings == null) {
            return 0;
        }
        if (mActiveListings.results == null) {
            return 0;
        }
        return mActiveListings.results.length;
    }

    @Override
    public void success(ActiveListings activeListings, Response response) {
        mActiveListings = activeListings;
        notifyDataSetChanged();
        mActivity.showList();
    }

    @Override
    public void failure(RetrofitError error) {
        mActivity.showError();
    }

    public ActiveListings getActiveListings () {
        return mActiveListings;
    }

    @Override
    public void onConnected() {

        if(getItemCount() == 0) {
            Etsy.getActiveListings(this);
        }

        isGooglePlayServicesAvailable = true;
        notifyDataSetChanged();
    }

    @Override
    public void onDisconnected() {

        if(getItemCount() == 0) {
            Etsy.getActiveListings(this);
        }

        isGooglePlayServicesAvailable = false;
        notifyDataSetChanged();
    }

    public class ListingHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView titleView;
        public TextView shopNameView;
        public TextView priceView;

        public PlusOneButton plusOneButton;
        public ImageButton shareButton;

        public ListingHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.listing_image);
            titleView = (TextView) itemView.findViewById(R.id.listing_title);
            shopNameView = (TextView) itemView.findViewById(R.id.listing_shop_name);
            priceView = (TextView) itemView.findViewById(R.id.listing_price);

            plusOneButton = (PlusOneButton) itemView.findViewById(R.id.listing_plus_one_button);
            shareButton = (ImageButton) itemView.findViewById(R.id.listing_share_button);
        }
    }

}
