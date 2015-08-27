package discovery.contentful.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import discovery.contentful.R;
import discovery.contentful.utils.Utils;
import com.contentful.java.cda.CDAAsset;
import java.util.ArrayList;

public class AssetsAdapter extends BaseAdapter {
  private final Context context;
  private ArrayList<CDAAsset> data = new ArrayList<>();
  private final int imageSize;

  public AssetsAdapter(Context context) {
    this.context = context;
    this.imageSize = context.getResources().getDimensionPixelSize(R.dimen.gallery_image_size);
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public CDAAsset getItem(int position) {
    return data.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  public void setData(ArrayList<CDAAsset> data) {
    this.data = data;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder vh;

    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.view_asset, parent, false);
      convertView.setTag(vh = new ViewHolder(convertView));
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    Utils.loadThumbnailForAssetWithSize(context, getItem(position), vh.ivThumbnail, imageSize,
        imageSize, true);

    return convertView;
  }

  public void clear() {
    data.clear();
  }

  static class ViewHolder {
    @Bind(R.id.iv_thumbnail) ImageView ivThumbnail;

    ViewHolder(View v) {
      ButterKnife.bind(this, v);
    }
  }
}
