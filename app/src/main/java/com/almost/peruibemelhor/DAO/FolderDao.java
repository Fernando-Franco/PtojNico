package com.almost.peruibemelhor.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.almost.peruibemelhor.Entity.Folder;

import java.util.List;

@Dao
public interface FolderDao {

    @Query("SELECT * FROM folder")
    List<Folder> getAll();

    @Query("SELECT * FROM folder where id_folder LIKE  :idFolder")
    Folder findByName(String idFolder);

    @Query("SELECT COUNT(*) from folder")
    int countUsers();

    @Insert
    void insertAll(Folder... folder);

    @Delete
    void delete(Folder folder);
}
