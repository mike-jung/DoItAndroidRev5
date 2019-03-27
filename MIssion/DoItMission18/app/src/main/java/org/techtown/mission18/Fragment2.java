package org.techtown.mission18;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *
 */
public class Fragment2 extends Fragment {
    ListView listView;
    BookAdapter adapter;

    OnDatabaseCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (OnDatabaseCallback) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new BookAdapter();

        ArrayList<BookInfo> result = callback.selectAll();
        adapter.setItems(result);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BookInfo item = (BookInfo) adapter.getItem(position);
                Toast.makeText(getContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BookInfo> result = callback.selectAll();
                adapter.setItems(result);
                adapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }


    class BookAdapter extends BaseAdapter {
        ArrayList<BookInfo> items = new ArrayList<BookInfo>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(BookInfo item) {
            items.add(item);
        }

        public void setItems(ArrayList<BookInfo> items) {
            this.items = items;
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            BookItemView view = new BookItemView(getContext());

            BookInfo item = items.get(position);
            view.setName(item.getName());
            view.setAuthor(item.getAuthor());
            view.setContents(item.getContents());

            return view;
        }
    }

}
