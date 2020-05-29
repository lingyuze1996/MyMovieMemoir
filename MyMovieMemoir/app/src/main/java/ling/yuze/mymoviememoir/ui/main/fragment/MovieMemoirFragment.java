package ling.yuze.mymoviememoir.ui.main.fragment;

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
import ling.yuze.mymoviememoir.network.RestService;

public class MovieMemoirFragment extends Fragment {
    private List<Object[]> memoirs;
    private ListView listView;
    //private ListAdapterMemoirs adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memoir, container, false);

        new TaskGetAllMemoirs().execute();


        return v;
    }

    private class TaskGetAllMemoirs extends AsyncTask<Void, Void, List<Object[]>> {
        @Override
        protected List<Object[]> doInBackground(Void... voids) {
            RestService rs = new RestService();
            return rs.getAllMemoirs();
        }

        @Override
        protected void onPostExecute(List<Object[]> memoirsList) {
            memoirs = memoirsList;

        }
    }
}
