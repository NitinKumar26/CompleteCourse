package in.completecourse.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import in.completecourse.R;

public class SpinAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mLayoutInflater;
    private final String[] mStringArray;
    private final int mResource;

    public SpinAdapter(@NonNull Context context, int resource, String[] strArr) {
        super(context, resource, 0, strArr);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.mStringArray = strArr;
    }

    private View homeView(int i, ViewGroup viewGroup){
        View inflate = this.mLayoutInflater.inflate(this.mResource, viewGroup, false);
        ((TextView)inflate.findViewById(R.id.offer_type_txt)).setText(Html.fromHtml(this.mStringArray[i] +
                "<sup><small>th</small></sup> Class"));
        return inflate;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = this.mLayoutInflater.inflate(R.layout.spinner_new_view, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.offer_type_txt)).setText(Html.fromHtml(this.mStringArray[position] + "<sup><small>th</small></sup> Class"));
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           return homeView(position, parent);
    }
}
