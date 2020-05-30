package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterMemoir;
import ling.yuze.mymoviememoir.data.MemoirItem;
import ling.yuze.mymoviememoir.network.RestService;

public class MovieMemoirFragment extends Fragment {
    private List<MemoirItem> memoirs;
    private ListView listView;
    private ListAdapterMemoir adapter;
    private int personId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memoir, container, false);

        SharedPreferences shared = getContext().getSharedPreferences("Info", Context.MODE_PRIVATE);
        personId = shared.getInt("id", 0);

        listView = v.findViewById(R.id.list_view_memoir);
        memoirs = MemoirItem.createList();
        adapter = new ListAdapterMemoir(getContext(), R.layout.list_view_memoir, memoirs);
        listView.setAdapter(adapter);

        new TaskGetAllMemoirs().execute();


        return v;
    }

    private class TaskGetAllMemoirs extends AsyncTask<Void, Void, List<Object[]>> {
        @Override
        protected List<Object[]> doInBackground(Void... voids) {
            RestService rs = new RestService();
            return rs.getAllMemoirsByPerson(personId);
        }

        @Override
        protected void onPostExecute(List<Object[]> memoirsList) {
            for (Object[] memoir : memoirsList) {
                String name = (String) memoir[0];
                String release = (String) memoir[1];
                String watching = (String) memoir[2];
                String comment = (String) memoir[3];
                String suburb = (String) memoir[4];
                float rating = (float) ((double) memoir[5]);

                MemoirItem item = new MemoirItem(name, release, watching, suburb, null, comment, rating, 0);
                memoirs.add(item);
            }

            adapter.notifyDataSetChanged();
        }
    }
}
