package com.almost.peruibemelhor.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.almost.peruibemelhor.Entity.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    // --------- < Query Session > ---------- //

    @Query("SELECT * FROM message_table ORDER BY date DESC")
    LiveData<List<Message>> getAllStations();

}
