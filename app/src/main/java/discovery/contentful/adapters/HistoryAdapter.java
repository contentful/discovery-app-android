package discovery.contentful.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import discovery.contentful.R;
import discovery.contentful.api.Credentials;
import java.util.ArrayList;
import org.joda.time.DateTime;

public class HistoryAdapter extends BaseAdapter {
  private static final String DATE_PATTERN = "EEE', 'MMM dd yyyy";

  private final Context context;
  private ArrayList<Credentials> data = new ArrayList<>();

  public HistoryAdapter(Context context) {
    this.context = context;
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public Credentials getItem(int position) {
    return data.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder vh;

    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.view_credentials, parent, false);

      convertView.setTag(vh = new ViewHolder(convertView));
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    Credentials credentials = getItem(position);
    convertView.setTag(R.id.tag_credentials, credentials);

    vh.tvSpaceName.setText(credentials.getSpaceName());

    Long lastLogin = credentials.getLastLogin();

    if (lastLogin == null || lastLogin == 0) {
      vh.tvLastLogin.setVisibility(View.GONE);
    } else {
      vh.tvLastLogin.setText(context.getString(R.string.history_last_login,
          new DateTime(lastLogin).toString(DATE_PATTERN)));
    }

    return convertView;
  }

  public void setData(ArrayList<Credentials> data) {
    this.data = data;
  }

  static class ViewHolder {
    @Bind(R.id.tv_space_name) TextView tvSpaceName;
    @Bind(R.id.tv_last_login) TextView tvLastLogin;

    ViewHolder(View v) {
      ButterKnife.bind(this, v);
    }
  }
}
