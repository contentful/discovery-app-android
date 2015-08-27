package discovery.contentful.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import discovery.contentful.R;
import discovery.contentful.ui.FieldViewHolder;
import discovery.contentful.utils.IntentConsts;
import discovery.contentful.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class StringListActivity extends CFListActivity implements AbsListView.RecyclerListener {
  @SuppressWarnings("unchecked")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Configure ListView
    listView.setAdapter(new StringListAdapter(this,
        (ArrayList<Object>) getIntent().getSerializableExtra(IntentConsts.EXTRA_LIST)));

    listView.setRecyclerListener(this);

    // Title
    Utils.setTitleFromIntent(this);
  }

  @Override public void onMovedToScrapHeap(View view) {
    Object tag = view.getTag();

    if (tag != null && tag instanceof FieldViewHolder) {
      ((FieldViewHolder) tag).tvValue.setText(null);
    }
  }

  private class StringListAdapter extends ArrayAdapter<Object> {
    @LayoutRes private static final int LAYOUT_RES_ID = R.layout.view_field;

    public StringListAdapter(Context context, List<Object> objects) {
      super(context, LAYOUT_RES_ID, objects);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      FieldViewHolder vh;

      if (convertView == null) {
        convertView = getLayoutInflater().inflate(LAYOUT_RES_ID, parent, false);
        convertView.setTag(vh = new FieldViewHolder(convertView));
        vh.tvValue.setVisibility(View.GONE);
        vh.ivArrow.setVisibility(View.GONE);

        vh.tvTitle.setFont(false);
      } else {
        vh = (FieldViewHolder) convertView.getTag();
      }

      vh.tvTitle.setText(getItem(position).toString());

      return convertView;
    }
  }
}
