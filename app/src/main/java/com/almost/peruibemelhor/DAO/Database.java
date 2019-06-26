package com.almost.peruibemelhor.DAO;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.almost.peruibemelhor.Entity.Folder;
import com.almost.peruibemelhor.Entity.Message;

@androidx.room.Database(entities = {Folder.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public abstract FolderDao folderDao();

    public static Database getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), Database.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static Folder addFolder(final Database db, Folder folder) {
        db.folderDao().insertAll(folder);
        return folder;
    }

    private static void populateWithTestData(Database db) {
        Folder folder = new Folder();
        folder.setId_folder("f99");
        folder.setTitle("Teste");
        folder.setDescription("teste");
        addFolder(db, folder);
    }
}
