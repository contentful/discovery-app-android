package discovery.contentful.preview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import discovery.contentful.R;

public class GenericViewHolder extends AbsViewHolder {
  @Bind(R.id.tv_title) TextView tvTitle;
  @Bind(R.id.tv_body) TextView tvBody;
  @Bind(R.id.iv_thumbnail) ImageView ivThumbnail;

  public GenericViewHolder(Object factoryKey, View rootView) {
    super(factoryKey, rootView);
    ButterKnife.bind(this, rootView);
  }
}
