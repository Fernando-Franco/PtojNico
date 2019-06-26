package com.almost.peruibemelhor.DAO;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.almost.peruibemelhor.Entity.Message;

@androidx.room.Database(entities = {Message.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract MessageDao messageDao();

    public static synchronized Database getInstance(Context c){
        if (instance == null){
            instance = Room.databaseBuilder(c.getApplicationContext(),
                    Database.class, "database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private MessageDao messageDao;

        private PopulateDbAsyncTask(Database db){
            messageDao = db.messageDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //messageDao.insert(new Message(pram1, pram2, pram3));
            return null;
        }
    }

}
