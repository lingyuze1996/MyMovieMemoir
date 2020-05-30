package ling.yuze.mymoviememoir.data.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ling.yuze.mymoviememoir.data.room.dao.MovieToWatchDAO;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;

@Database(entities = {MovieToWatch.class}, version = 2, exportSchema = false)
public abstract class MovieToWatchDatabase extends RoomDatabase {
    public abstract MovieToWatchDAO movieToWatchDao();
    private static MovieToWatchDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static synchronized MovieToWatchDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MovieToWatchDatabase.class, "MovieToWatchDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}